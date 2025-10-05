package persistencia;

import entidades.*;
import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;

public class ArquivoService {

    private static final String ARQUIVO_MEDICOS = "medicos.csv";
    private static final String ARQUIVO_PACIENTES = "pacientes.csv";
    private static final String ARQUIVO_PLANOS_SAUDE = "planos_saude.csv";
    private static final String ARQUIVO_QUARTOS = "quartos.csv";
    private static final String ARQUIVO_CONSULTAS = "consultas.csv";
    private static final String ARQUIVO_INTERNACOES = "internacoes.csv";

    // salvamento
    public void salvarPlanosSaude(List<PlanoSaude> planos) {
        salvarDados(ARQUIVO_PLANOS_SAUDE, planos, plano -> String.join(",",
                plano.getFornecedor(),
                String.valueOf(plano.isMenosQueUmaSemana()),
                serializarDescontos(plano.getDescontosPorEspecialidade())));
    }

    public void salvarQuartos(List<Quarto> quartos) {
        salvarDados(ARQUIVO_QUARTOS, quartos, quarto -> String.join(",", quarto.getNumero(), quarto.getTipo().name()));
    }

    public void salvarMedicos(List<Medico> medicos) {
        salvarDados(ARQUIVO_MEDICOS, medicos, medico -> String.join(",",
                medico.getNome(),
                medico.getCrm(),
                medico.getEspecialidade().name(),
                String.valueOf(medico.getCustoConsulta())));
    }

    public void salvarPacientes(List<Paciente> pacientes) {
        salvarDados(ARQUIVO_PACIENTES, pacientes, paciente -> {
            String tipo = "COMUM";
            String idPlano = "N/A";
            if (paciente instanceof PacienteEspecial especial) {
                tipo = "ESPECIAL";
                idPlano = especial.getPlanoSaude().getFornecedor();
            }
            return String.join(",", tipo, paciente.getNome(), paciente.getCpf(), String.valueOf(paciente.getIdade()), idPlano);
        });
    }

    public void salvarConsultas(List<Consulta> consultas) {
        salvarDados(ARQUIVO_CONSULTAS, consultas, consulta -> {
            String diagnostico = consulta.getDiagnostico() != null ? consulta.getDiagnostico() : "N/A";
            String prescricao = consulta.getPrescricao() != null ? consulta.getPrescricao() : "N/A";

            return String.join(",",
                    consulta.getPaciente().getCpf(),
                    consulta.getMedico().getCrm(),
                    consulta.getHorario().toString(),
                    consulta.getLocal(),
                    consulta.getStatus().name(),
                    diagnostico,
                    prescricao);
        });
    }

    public void salvarInternacoes(List<Internacao> internacoes, List<Paciente> pacientes) {
        salvarDados(ARQUIVO_INTERNACOES, internacoes, internacao -> String.join(",",
                internacao.getPaciente().getCpf(),
                internacao.getMedicoResponsavel().getCrm(),
                internacao.getQuarto().getNumero(),
                internacao.getDataEntrada().toString(),
                internacao.getDataSaida().toString(),
                String.valueOf(internacao.getCusto())));
    }

    // carregamento
    public List<PlanoSaude> carregarPlanosSaude() {
        return carregarDados(ARQUIVO_PLANOS_SAUDE, linha -> {
            String[] dados = linha.split(",");
            PlanoSaude plano = new PlanoSaude(dados[0], Boolean.parseBoolean(dados[1]));
            Map<String, Double> descontos = deserializarDescontos(dados[2]);
            descontos.forEach(plano::adicionarDesconto);
            return plano;
        });
    }

    public List<Quarto> carregarQuartos() {
        return carregarDados(ARQUIVO_QUARTOS, linha -> {
            String[] partes = linha.split(",");
            return new Quarto(partes[0], TipoQuarto.valueOf(partes[1]));
        });
    }

    public List<Medico> carregarMedicos() {
        return carregarDados(ARQUIVO_MEDICOS, linha -> {
            String[] dados = linha.split(",");
            return new Medico(dados[0], dados[1], Especialidade.valueOf(dados[2]), Double.parseDouble(dados[3]));
        });
    }

    public List<Paciente> carregarPacientes(List<PlanoSaude> planos) {
        return carregarDados(ARQUIVO_PACIENTES, linha -> {
            String[] dados = linha.split(",");
            String tipo = dados[0];
            String nome = dados[1];
            String cpf = dados[2];
            int idade = Integer.parseInt(dados[3]);
            String idPlano = dados[4];

            if ("ESPECIAL".equals(tipo)) {
                PlanoSaude planoEncontrado = planos.stream()
                        .filter(p -> p.getFornecedor().equals(idPlano))
                        .findFirst()
                        .orElse(null);
                return new PacienteEspecial(nome, idade, cpf, planoEncontrado);
            } else {
                return new Paciente(nome, cpf, idade);
            }
        });
    }

    public List<Consulta> carregarConsultas(List<Paciente> pacientes, List<Medico> medicos) {
        return carregarDados(ARQUIVO_CONSULTAS, linha -> {
            String[] dados = linha.split(",");
            String cpfPaciente = dados[0];
            String crmMedico = dados[1];
            LocalDateTime horario = LocalDateTime.parse(dados[2]);
            String local = dados[3];
            StatusConsulta status = StatusConsulta.valueOf(dados[4]);
            String diagnostico = dados[5];
            String prescricao = dados[6];

            Paciente paciente = pacientes.stream().filter(p -> p.getCpf().equals(cpfPaciente)).findFirst().orElse(null);
            Medico medico = medicos.stream().filter(m -> m.getCrm().equals(crmMedico)).findFirst().orElse(null);

            if (paciente != null && medico != null) {
                Consulta consulta = new Consulta(paciente, medico, horario, local, status);
                consulta.setStatus(status);

                if (!"N/A".equals(diagnostico)) consulta.setDiagnostico(diagnostico);
                if (!"N/A".equals(prescricao)) consulta.setPrescricao(prescricao);

                return consulta;
            }
            return null;
        });
    }

    public List<Internacao> carregarInternacoes(List<Paciente> pacientes, List<Medico> medicos, List<Quarto> quartos) {
        return carregarDados(ARQUIVO_INTERNACOES, linha -> {
            String[] dados = linha.split(",");
            String cpfPaciente = dados[0];
            String crmMedico = dados[1];
            String numQuarto = dados[2];
            LocalDate dataEntrada = LocalDate.parse(dados[3]);
            LocalDate dataSaida = LocalDate.parse(dados[4]);
            double custo = Double.parseDouble(dados[5]);

            Paciente paciente = pacientes.stream().filter(p -> p.getCpf().equals(cpfPaciente)).findFirst().orElse(null);
            Medico medico = medicos.stream().filter(m -> m.getCrm().equals(crmMedico)).findFirst().orElse(null);
            Quarto quarto = quartos.stream().filter(q -> q.getNumero().equals(numQuarto)).findFirst().orElse(null);

            if (paciente != null && medico != null && quarto != null) {
                int dias = dataEntrada.until(dataSaida).getDays();
                return new Internacao(paciente, medico, quarto, dataEntrada, dias, custo / dias);
            }
            return null;
        });
    }

    // métodos genéricos
    private <T> void salvarDados(String nomeArquivo, List<T> lista, Function<T, String> mapper) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(nomeArquivo))) {
            for (T item : lista) {
                writer.write(mapper.apply(item));
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Erro ao salvar dados em " + nomeArquivo + ": " + e.getMessage());
        }
    }

    private <T> List<T> carregarDados(String nomeArquivo, Function<String, T> mapper) {
        List<T> lista = new ArrayList<>();
        File arquivo = new File(nomeArquivo);
        if (!arquivo.exists()) return lista;

        try (BufferedReader reader = new BufferedReader(new FileReader(arquivo))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                T objeto = mapper.apply(linha);
                if (objeto != null) lista.add(objeto);
            }
        } catch (IOException | RuntimeException e) {
            System.err.println("Erro ao carregar dados de " + nomeArquivo + ": " + e.getMessage());
        }
        return lista;
    }

    private String serializarDescontos(Map<String, Double> descontos) {
        if (descontos == null || descontos.isEmpty()) return "N/A";
        List<String> partes = new ArrayList<>();
        for (Map.Entry<String, Double> entry : descontos.entrySet()) {
            partes.add(entry.getKey() + ":" + entry.getValue());
        }
        return String.join(";", partes);
    }

    private Map<String, Double> deserializarDescontos(String dados) {
        Map<String, Double> descontos = new HashMap<>();
        if (dados == null || dados.equals("N/A")) return descontos;
        for (String parte : dados.split(";")) {
            String[] kv = parte.split(":");
            descontos.put(kv[0], Double.parseDouble(kv[1]));
        }
        return descontos;
    }
}
