package services;

import entidades.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class RelatorioService {
    private final DateTimeFormatter formatterData = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private final DateTimeFormatter formatterDataHora = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    // Relatório 1: Pacientes com Histórico Completo
    public void gerarRelatorioPacientesComHistorico(List<Paciente> pacientes, List<Consulta> consultas, List<Internacao> internacoes) {
        if (pacientes.isEmpty()) {
            System.out.println("\nNenhum paciente cadastrado.");
            return;
        }
        pacientes.forEach(p -> {
            System.out.println("\n--------------------------------------------------");
            System.out.printf("Paciente: %s (CPF: %s, Idade: %d)\n", p.getNome(), p.getCpf(), p.getIdade());

            if (p instanceof PacienteEspecial) {
                System.out.println("Plano de Saúde: " + ((PacienteEspecial) p).getPlanoSaude().getFornecedor());
            }

            System.out.println("--- Histórico de Consultas ---");
            List<Consulta> histConsultas = consultas.stream()
                .filter(c -> c.getPaciente().getCpf().equals(p.getCpf()))
                .collect(Collectors.toList());
            if (histConsultas.isEmpty()) System.out.println("  Nenhuma consulta registrada.");
            else histConsultas.forEach(c -> System.out.printf("  - Data: %s, Médico: %s, Status: %s\n", c.getHorario().format(formatterDataHora), c.getMedico().getNome(), c.getStatus()));

            System.out.println("--- Histórico de Internações ---");
            List<Internacao> histInternacoes = internacoes.stream()
                .filter(i -> i.getPaciente().getCpf().equals(p.getCpf()))
                .collect(Collectors.toList());
            if (histInternacoes.isEmpty()) System.out.println("  Nenhuma internação registrada.");
            else histInternacoes.forEach(i -> System.out.printf("  - Entrada: %s, Quarto: %s, Status: %s\n", i.getDataEntrada().format(formatterData), i.getQuarto().getNumero(), i.isCancelada() ? "Cancelada" : "Ativa/Concluída"));
        });
    }

    // Relatório 2: Médicos com Estatísticas
    public void gerarRelatorioMedicosComEstatisticas(List<Medico> medicos, List<Consulta> consultas) {
        medicos.forEach(m -> {
            long consultasRealizadas = consultas.stream()
                .filter(c -> c.getMedico().getCrm().equals(m.getCrm()) && c.getStatus() == StatusConsulta.CONCLUIDA)
                .count();
            
            System.out.println("\n--------------------------------------------------");
            System.out.printf("Médico: %s (CRM: %s, Especialidade: %s)\n", m.getNome(), m.getCrm(), m.getEspecialidade());
            System.out.printf("Consultas Concluídas: %d\n", consultasRealizadas);

            System.out.println("--- Agenda Futura ---");
            List<Consulta> agenda = consultas.stream()
                .filter(c -> c.getMedico().getCrm().equals(m.getCrm()) && c.getStatus() == StatusConsulta.AGENDADA)
                .sorted(Comparator.comparing(Consulta::getHorario))
                .collect(Collectors.toList());
            if (agenda.isEmpty()) System.out.println("  Nenhuma consulta agendada.");
            else agenda.forEach(c -> System.out.printf("  - Data: %s, Paciente: %s\n", c.getHorario().format(formatterDataHora), c.getPaciente().getNome()));
        });
    }
    
    // Relatório 4: Pacientes Internados Atualmente
    public void gerarRelatorioPacientesInternados(List<Internacao> internacoes) {
        List<Internacao> ativas = internacoes.stream()
            .filter(i -> !i.isCancelada() && i.getDataSaida().isAfter(LocalDate.now()))
            .collect(Collectors.toList());

        if (ativas.isEmpty()) {
            System.out.println("\nNenhum paciente internado no momento.");
            return;
        }
        
        System.out.println("\n--- PACIENTES INTERNADOS ATUALMENTE ---");
        ativas.forEach(i -> {
            if (ChronoUnit.DAYS.between(i.getDataEntrada(), LocalDate.now()) <= 0){
                long diasInternado = 0;
                System.out.printf("Paciente: %s, Quarto: %s, Médico: %s, Dias Internado: %d\n",
                    i.getPaciente().getNome(), i.getQuarto().getNumero(), i.getMedicoResponsavel().getNome(), diasInternado);
                
            }
            else{
                long diasInternado = ChronoUnit.DAYS.between(i.getDataEntrada(), LocalDate.now());
                System.out.printf("Paciente: %s, Quarto: %s, Médico: %s, Dias Internado: %d\n",
                    i.getPaciente().getNome(), i.getQuarto().getNumero(), i.getMedicoResponsavel().getNome(), diasInternado);
            }
        });
    }

    // Relatório 5: Estatísticas Gerais
    public void gerarEstatisticasGerais(List<Medico> medicos, List<Consulta> consultas) {
        System.out.println("\n--- ESTATÍSTICAS GERAIS DO HOSPITAL ---");

        // Médico que mais atendeu
        Optional<Medico> medicoMaisAtendeu = medicos.stream()
            .max(Comparator.comparingLong(m -> consultas.stream().filter(c -> c.getMedico().equals(m) && c.getStatus() == StatusConsulta.CONCLUIDA).count()));
        medicoMaisAtendeu.ifPresent(medico -> System.out.println("Médico com mais atendimentos: " + medico.getNome()));

        // Especialidade mais procurada
        Optional<Map.Entry<Especialidade, Long>> espMaisProcurada = consultas.stream()
            .filter(c -> c.getStatus() != StatusConsulta.CANCELADA)
            .collect(Collectors.groupingBy(c -> c.getMedico().getEspecialidade(), Collectors.counting()))
            .entrySet().stream()
            .max(Map.Entry.comparingByValue());
        espMaisProcurada.ifPresent(entry -> System.out.println("Especialidade mais procurada: " + entry.getKey()));
    }

    // Relatório 6: Análise de Planos de Saúde
    public void gerarAnalisePlanosSaude(List<PlanoSaude> planos, List<Paciente> pacientes, List<Consulta> consultas) {
        System.out.println("\n--- ANÁLISE DE PLANOS DE SAÚDE ---");
        planos.forEach(plano -> {
            long totalPacientesNoPlano = pacientes.stream()
                .filter(p -> p instanceof PacienteEspecial && ((PacienteEspecial)p).getPlanoSaude().equals(plano))
                .count();

            double totalEconomizado = consultas.stream()
                .filter(c -> c.getPaciente() instanceof PacienteEspecial && ((PacienteEspecial)c.getPaciente()).getPlanoSaude().equals(plano))
                .mapToDouble(c -> {
                    double custoBase = c.getMedico().getCustoConsulta();
                    double percentual = plano.getDesconto(c.getMedico().getEspecialidade().name());
                    return custoBase * (percentual / 100.0);
                }).sum();

            System.out.println("\n--------------------------------------------------");
            System.out.printf("Plano: %s\n", plano.getFornecedor());
            System.out.printf("Total de Pacientes: %d\n", totalPacientesNoPlano);
            System.out.printf("Total Economizado pelos Pacientes: R$ %.2f\n", totalEconomizado);
        });
    }
}
