package main;

import entidades.*;
import persistencia.ArquivoService;
import services.*;
import view.Display;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class App {

    private final List<Medico> medicos;
    private final List<Paciente> pacientes;
    private final List<Consulta> consultas;
    private final List<PlanoSaude> planosSaude;
    private final List<Quarto> quartos;
    private final List<Internacao> internacoes;

    private final ArquivoService arquivoService;
    private final RelatorioService relatorioService;
    private final Scanner scanner;
    private final DateTimeFormatter formatterDataHora = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private final DateTimeFormatter formatterData = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public App() {
        this.arquivoService = new ArquivoService();
        this.relatorioService = new RelatorioService();
        this.scanner = new Scanner(System.in);
        this.planosSaude = arquivoService.carregarPlanosSaude();
        this.quartos = arquivoService.carregarQuartos();
        this.medicos = arquivoService.carregarMedicos();
        this.pacientes = arquivoService.carregarPacientes(this.planosSaude);
        this.consultas = arquivoService.carregarConsultas(this.pacientes, this.medicos);
        this.internacoes = arquivoService.carregarInternacoes(this.pacientes, this.medicos, this.quartos);
    }

    public static void main(String[] args) {
        App sistema = new App();
        sistema.executar();
    }

    public void executar() {
        int opcao = -1;
        while (opcao != 0) {
            Display.exibirMenuPrincipal();
            System.out.print("Escolha uma opção: ");
            try {
                opcao = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                Display.erro("Opção inválida. Por favor, digite um número.");
                opcao = -1; // Reseta para evitar loop infinito se a próxima linha falhar
                continue;
            }
            // opcoes do menu
            switch (opcao) {
                case 1: agendarNovaConsulta(); break;
                case 2: concluirConsulta(); break;
                case 3: cancelarConsulta(); break;
                case 4: agendarNovaInternacao(); break;
                case 5: cancelarInternacao(); break;
                case 6: cadastrarNovoPaciente(); break;
                case 7: cadastrarNovoMedico(); break;
                case 8: cadastrarNovoPlanoSaude(); break;
                case 9: cadastrarNovoQuarto(); break;
                case 10: Display.listarConsultas(this.consultas); break;
                case 11: Display.listarInternacoes(this.internacoes); break;
                case 12: Display.listarPacientes(this.pacientes); break;
                case 13: Display.listarMedicos(this.medicos); break;
                case 14: exibirMenuRelatorios(); break;
                case 0: salvarESair(); break;
                default: Display.erro("Opção inválida! Tente novamente."); break;
            }

            if (opcao != 0) {
                Display.mensagem("\nPressione Enter para continuar...");
                scanner.nextLine();
            }
        }
        scanner.close();
    }

    private void exibirMenuRelatorios() {
        int opcao = -1;
        while (opcao != 0) {
            Display.exibirMenuRelatorios();
            System.out.print("Escolha uma opção de relatório: ");
            try {
                opcao = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                Display.erro("Opção inválida.");
                continue;
            }

            switch (opcao) {
                case 1: relatorioService.gerarRelatorioPacientesComHistorico(pacientes, consultas, internacoes); break;
                case 2: relatorioService.gerarRelatorioMedicosComEstatisticas(medicos, consultas); break;
                case 3: relatorioConsultasComFiltro(); break;
                case 4: relatorioService.gerarRelatorioPacientesInternados(internacoes); break;
                case 5: relatorioService.gerarEstatisticasGerais(medicos, consultas); break;
                case 6: relatorioService.gerarAnalisePlanosSaude(planosSaude, pacientes, consultas); break;
                case 0: break;
                default: Display.erro("Opção inválida!"); break;
            }
            if (opcao != 0) {
                Display.mensagem("\nPressione Enter para continuar...");
                scanner.nextLine();
            }
        }
    }
    
    // Métodos de cadastro, agendamento, etc. 
    private void cadastrarNovoPaciente() {
        Display.cabecalho("Cadastro de Novo Paciente");
        System.out.print("Nome: "); String nome = scanner.nextLine();
        System.out.print("CPF: "); String cpf = scanner.nextLine();
        System.out.print("Idade: "); int idade = Integer.parseInt(scanner.nextLine());
        System.out.print("Possui plano de saúde? (S/N): "); String temPlano = scanner.nextLine();

        if (temPlano.equalsIgnoreCase("S")) {
            if (planosSaude.isEmpty()) { Display.erro("Nenhum plano de saúde cadastrado."); return; }
            Display.mensagem("Planos disponíveis:");
            for (int i = 0; i < planosSaude.size(); i++) System.out.println((i + 1) + " - " + planosSaude.get(i).getFornecedor());
            System.out.print("Escolha o plano (pelo número): "); int idxPlano = Integer.parseInt(scanner.nextLine()) - 1;
            pacientes.add(new PacienteEspecial(nome, idade, cpf, planosSaude.get(idxPlano)));
        } else {
            pacientes.add(new Paciente(nome, cpf, idade));
        }
        Display.sucesso("Paciente cadastrado!");
    }

    private void cadastrarNovoMedico() {
        Display.cabecalho("Cadastro de Novo Médico");
        System.out.print("Nome: "); String nome = scanner.nextLine();
        System.out.print("CRM: "); String crm = scanner.nextLine();
        System.out.print("Especialidade (ex: CARDIOLOGIA, PEDIATRIA): "); Especialidade especialidade = Especialidade.valueOf(scanner.nextLine().toUpperCase());
        System.out.print("Custo da Consulta: R$ "); double custo = Double.parseDouble(scanner.nextLine());
        MedicoService.cadastrarMedico(nome, crm, especialidade, custo, this.medicos);
        Display.sucesso("Médico cadastrado!");
    }

    private void cadastrarNovoPlanoSaude() {
        Display.cabecalho("Cadastro de Novo Plano de Saúde");
        System.out.print("Nome do fornecedor: "); String fornecedor = scanner.nextLine();
        System.out.print("Oferece internação gratuita (< 7 dias)? (S/N): "); boolean cobertura = scanner.nextLine().equalsIgnoreCase("S");
        PlanoSaude novoPlano = PlanoSaudeService.cadastrarPlano(fornecedor, cobertura, this.planosSaude);
        while (true) {
            System.out.print("Adicionar desconto para especialidade? (S/N): ");
            if (!scanner.nextLine().equalsIgnoreCase("S")) break;
            System.out.print("Especialidade (ex: CARDIOLOGIA): "); String especialidade = scanner.nextLine().toUpperCase();
            System.out.print("Percentual de desconto (ex: 20): "); double percentual = Double.parseDouble(scanner.nextLine());
            PlanoSaudeService.adicionarDesconto(novoPlano, especialidade, percentual);
        }
        Display.sucesso("Plano de Saúde cadastrado!");
    }

    private void cadastrarNovoQuarto() {
        Display.cabecalho("Cadastro de Novo Quarto");
        System.out.print("Número do quarto: "); String numero = scanner.nextLine();
        System.out.print("Tipo (SIMPLES, SUITE, UTI): "); TipoQuarto tipo = TipoQuarto.valueOf(scanner.nextLine().toUpperCase());
        QuartoService.cadastrarQuarto(numero, tipo, this.quartos);
        Display.sucesso("Quarto cadastrado!");
    }

    private void agendarNovaConsulta() {
        Display.cabecalho("Agendamento de Nova Consulta");
        if (pacientes.isEmpty() || medicos.isEmpty()) { Display.erro("É preciso ter ao menos um paciente e um médico cadastrados."); return; }
        System.out.print("Digite o CPF do paciente: "); Paciente paciente = buscarPacientePorCpf(scanner.nextLine());
        if (paciente == null) { Display.erro("Paciente não encontrado."); return; }
        System.out.print("Digite o CRM do médico: "); Medico medico = buscarMedicoPorCrm(scanner.nextLine());
        if (medico == null) { Display.erro("Médico não encontrado."); return; }
        System.out.print("Digite a data e hora (dd/MM/yyyy HH:mm): ");
        try {
            LocalDateTime horario = LocalDateTime.parse(scanner.nextLine(), formatterDataHora);
            System.out.print("Digite o local da consulta: "); String local = scanner.nextLine();
            Consulta novaConsulta = ConsultaService.marcarConsulta(paciente, medico, horario, local, this.consultas);
            if (novaConsulta != null) {
                Display.sucesso("Consulta agendada!");
                calcularEExibirCustoConsulta(novaConsulta);
            }
        } catch (DateTimeParseException e) {
            Display.erro("Formato de data/hora inválido. Use dd/MM/yyyy HH:mm.");
        }
    }
    
    private void relatorioConsultasComFiltro() {
        Display.cabecalho("Filtro de Consultas");
        System.out.print("Filtrar por CPF do paciente (deixe em branco para ignorar): "); String cpf = scanner.nextLine();
        System.out.print("Filtrar por CRM do médico (deixe em branco para ignorar): "); String crm = scanner.nextLine();

        List<Consulta> filtradas = this.consultas.stream()
            .filter(c -> cpf.isEmpty() || c.getPaciente().getCpf().equals(cpf))
            .filter(c -> crm.isEmpty() || c.getMedico().getCrm().equals(crm))
            .collect(Collectors.toList());

        Display.listarConsultas(filtradas);
    }
    
    private void concluirConsulta() {
        Display.cabecalho("Concluir Consulta");
        List<Consulta> agendadas = this.consultas.stream().filter(c -> c.getStatus() == StatusConsulta.AGENDADA).collect(Collectors.toList());
        if (agendadas.isEmpty()) { Display.mensagem("Não há consultas agendadas para concluir."); return; }
        Display.listarConsultas(agendadas);
        System.out.print("Digite o número da linha da consulta a ser concluída: "); int idx = Integer.parseInt(scanner.nextLine()) - 1;
        if (idx >= 0 && idx < agendadas.size()) {
            Consulta consulta = agendadas.get(idx);
            System.out.print("Diagnóstico: "); consulta.setDiagnostico(scanner.nextLine());
            System.out.print("Prescrição: "); consulta.setPrescricao(scanner.nextLine());
            consulta.setStatus(StatusConsulta.CONCLUIDA);
            Display.sucesso("Consulta concluída e informações registradas no histórico do paciente!");
        } else {
            Display.erro("Seleção inválida.");
        }
    }

    private void cancelarConsulta() {
        Display.cabecalho("Cancelar Consulta");
        List<Consulta> agendadas = this.consultas.stream().filter(c -> c.getStatus() == StatusConsulta.AGENDADA).collect(Collectors.toList());
        if (agendadas.isEmpty()) { Display.mensagem("Não há consultas agendadas para cancelar."); return; }
        Display.listarConsultas(agendadas);
        System.out.print("Digite o número da linha da consulta a ser cancelada: "); int idx = Integer.parseInt(scanner.nextLine()) - 1;
        if (idx >= 0 && idx < agendadas.size()) {
            Consulta consulta = agendadas.get(idx);
            ConsultaService.cancelarConsulta(consulta);
            Display.sucesso("Consulta cancelada. O horário das " + consulta.getHorario().format(formatterDataHora) + " com " + consulta.getMedico().getNome() + " está novamente disponível.");
        } else {
            Display.erro("Seleção inválida.");
        }
    }

    private void agendarNovaInternacao() {
        Display.cabecalho("Agendar Nova Internação");
        if (pacientes.isEmpty() || medicos.isEmpty() || quartos.isEmpty()) { Display.erro("É preciso ter pacientes, médicos e quartos cadastrados."); return; }
        System.out.print("Digite o CPF do paciente: "); Paciente paciente = buscarPacientePorCpf(scanner.nextLine());
        if (paciente == null) { Display.erro("Paciente não encontrado."); return; }
        System.out.print("Digite o CRM do médico: "); Medico medico = buscarMedicoPorCrm(scanner.nextLine());
        if (medico == null) { Display.erro("Médico não encontrado."); return; }
        List<Quarto> disponiveis = this.quartos.stream().filter(q -> !q.isOcupado()).collect(Collectors.toList());
        if (disponiveis.isEmpty()) { Display.erro("Nenhum quarto disponível."); return; }
        System.out.println("Quartos disponíveis:");
        for(int i = 0; i < disponiveis.size(); i++) System.out.printf("%d - Número: %s, Tipo: %s\n", (i+1), disponiveis.get(i).getNumero(), disponiveis.get(i).getTipo());
        System.out.print("Escolha o quarto: "); Quarto quarto = disponiveis.get(Integer.parseInt(scanner.nextLine()) - 1);
        System.out.print("Data de entrada (dd/MM/yyyy): "); LocalDate dataEntrada = LocalDate.parse(scanner.nextLine(), formatterData);
        System.out.print("Duração prevista (dias): "); int dias = Integer.parseInt(scanner.nextLine());
        System.out.print("Custo da diária: R$ "); double valorDiaria = Double.parseDouble(scanner.nextLine());
        InternacaoService.agendarInternacao(paciente, medico, quarto, dataEntrada, dias, valorDiaria, this.internacoes);
        Display.sucesso("Internação agendada!");
    }

    private void cancelarInternacao() {
        Display.cabecalho("Cancelar Internação");
        List<Internacao> ativas = this.internacoes.stream().filter(i -> !i.isCancelada()).collect(Collectors.toList());
        if (ativas.isEmpty()) { Display.mensagem("Nenhuma internação ativa para cancelar."); return; }
        Display.listarInternacoes(ativas);
        System.out.print("Digite o número da linha da internação a ser cancelada: "); int idx = Integer.parseInt(scanner.nextLine()) - 1;
        if (idx >= 0 && idx < ativas.size()) {
            InternacaoService.cancelarInternacao(ativas.get(idx));
        } else {
            Display.erro("Seleção inválida.");
        }
    }
    
    private void salvarESair() {
        Display.cabecalho("Salvando dados e encerrando");
        arquivoService.salvarPlanosSaude(this.planosSaude);
        arquivoService.salvarQuartos(this.quartos);
        arquivoService.salvarMedicos(this.medicos);
        arquivoService.salvarPacientes(this.pacientes);
        arquivoService.salvarConsultas(this.consultas);
        arquivoService.salvarInternacoes(this.internacoes, this.pacientes);
        Display.sucesso("Dados salvos. Até logo!");
    }

    private Paciente buscarPacientePorCpf(String cpf) {
        return this.pacientes.stream().filter(p -> p.getCpf().equals(cpf)).findFirst().orElse(null);
    }

    private Medico buscarMedicoPorCrm(String crm) {
        return this.medicos.stream().filter(m -> m.getCrm().equals(crm)).findFirst().orElse(null);
    }
    
    private void calcularEExibirCustoConsulta(Consulta consulta) {
        double custoBase = consulta.getMedico().getCustoConsulta();
        double custoFinal = custoBase;
        Paciente paciente = consulta.getPaciente();

        Display.mensagem("--- Custo da Consulta ---");
        System.out.printf("Valor base: R$ %.2f%n", custoBase);

        if (paciente instanceof PacienteEspecial) {
            PacienteEspecial especial = (PacienteEspecial) paciente;
            double percentualDesconto = especial.getPlanoSaude().getDesconto(consulta.getMedico().getEspecialidade().name());
            if (percentualDesconto > 0) {
                double valorDesconto = custoBase * (percentualDesconto / 100.0);
                custoFinal -= valorDesconto;
                System.out.printf("Desconto Plano (%s): - R$ %.2f (%.0f%%)\n", especial.getPlanoSaude().getFornecedor(), valorDesconto, percentualDesconto);
            }
        }

        if (paciente.getIdade() >= 60) {
            double descontoIdade = custoFinal * 0.10;
            custoFinal -= descontoIdade;
            System.out.printf("Desconto por idade (60+): - R$ %.2f (10%%)\n", descontoIdade);
        }

        System.out.printf("VALOR FINAL: R$ %.2f%n", custoFinal);
        Display.mensagem("-------------------------");
    }
}