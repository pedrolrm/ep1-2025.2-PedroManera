package view;

import entidades.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public final class Display {

    // Construtor privado para impedir a instanciação 
    private Display() {}

    private static final DateTimeFormatter FORMATADOR_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter FORMATADOR_DATA_HORA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public static void exibirMenuPrincipal() {
        System.out.println("\n========= MENU PRINCIPAL - GESTÃO HOSPITALAR =========");
        System.out.println(" 1. Agendar Nova Consulta");
        System.out.println(" 2. Concluir Consulta");
        System.out.println(" 3. Cancelar Consulta");
        System.out.println(" 4. Agendar Nova Internação");
        System.out.println(" 5. Cancelar Internação");
        System.out.println("\n--- CADASTROS ---");
        System.out.println(" 6. Cadastrar Novo Paciente");
        System.out.println(" 7. Cadastrar Novo Médico");
        System.out.println(" 8. Cadastrar Novo Plano de Saúde");
        System.out.println(" 9. Cadastrar Novo Quarto");
        System.out.println("\n--- LISTAGENS E RELATÓRIOS ---");
        System.out.println("10. Listar Todas as Consultas");
        System.out.println("11. Listar Todas as Internações");
        System.out.println("12. Listar Todos os Pacientes");
        System.out.println("13. Listar Todos os Médicos");
        System.out.println("14. Acessar Módulo de Relatórios Avançados");
        System.out.println("\n 0. Salvar e Sair");
        System.out.println("======================================================");
    }

    public static void exibirMenuRelatorios() {
        cabecalho("Módulo de Relatórios Avançados");
        System.out.println("1. Relatório de Pacientes com Histórico Completo");
        System.out.println("2. Relatório de Médicos com Agenda e Estatísticas");
        System.out.println("3. Consultas com Filtros (por Paciente ou Médico)");
        System.out.println("4. Relatório de Pacientes Internados Atualmente");
        System.out.println("5. Estatísticas Gerais do Hospital");
        System.out.println("6. Análise de Planos de Saúde (Uso e Economia)");
        System.out.println("-------------------------------------------------");
        System.out.println("0. Voltar ao Menu Principal");
    }
    
    public static void cabecalho(String titulo) {
        System.out.println("\n--- " + titulo.toUpperCase() + " ---");
    }

    public static void mensagem(String msg) {
        System.out.println(msg);
    }

    public static void sucesso(String msg) {
        System.out.println("[SUCESSO] " + msg);
    }

    public static void erro(String msg) {
        System.err.println("[ERRO] " + msg);
    }

    public static void listarMedicos(List<Medico> medicos) {
        cabecalho("Lista de Médicos Cadastrados");
        if (medicos.isEmpty()) {
            mensagem("Nenhum médico cadastrado no sistema.");
            return;
        }
        System.out.println("--------------------------------------------------------------------------");
        System.out.printf("%-25s | %-15s | %-20s | %-10s%n", "NOME", "CRM", "ESPECIALIDADE", "CONSULTA");
        System.out.println("--------------------------------------------------------------------------");
        for (Medico medico : medicos) {
            String custo = String.format("R$ %.2f", medico.getCustoConsulta());
            System.out.printf("%-25s | %-15s | %-20s | %-10s%n",
                    medico.getNome(), medico.getCrm(), medico.getEspecialidade(), custo);
        }
        System.out.println("--------------------------------------------------------------------------");
    }

    public static void listarPacientes(List<Paciente> pacientes) {
        cabecalho("Lista de Pacientes Cadastrados");
        if (pacientes.isEmpty()) {
            mensagem("Nenhum paciente cadastrado no sistema.");
            return;
        }
        System.out.println("------------------------------------------------------------------------------------");
        System.out.printf("%-25s | %-15s | %-5s | %-15s | %-15s%n", "NOME", "CPF", "IDADE", "TIPO", "PLANO DE SAÚDE");
        System.out.println("------------------------------------------------------------------------------------");
        for (Paciente paciente : pacientes) {
            String tipo = "Comum";
            String plano = "N/A";
            if (paciente instanceof PacienteEspecial) {
                tipo = "Especial";
                plano = ((PacienteEspecial) paciente).getPlanoSaude().getFornecedor();
            }
            System.out.printf("%-25s | %-15s | %-5d | %-15s | %-15s%n",
                    paciente.getNome(), paciente.getCpf(), paciente.getIdade(), tipo, plano);
        }
        System.out.println("------------------------------------------------------------------------------------");
    }

    public static void listarConsultas(List<Consulta> consultas) {
        cabecalho("Lista de Consultas");
        if (consultas.isEmpty()) {
            mensagem("Nenhuma consulta encontrada.");
            return;
        }
        System.out.println("------------------------------------------------------------------------------------------------------------");
        System.out.printf("%-4s | %-25s | %-25s | %-16s | %-20s | %-10s%n", "Nº", "PACIENTE", "MÉDICO", "DATA/HORA", "LOCAL", "STATUS");
        System.out.println("------------------------------------------------------------------------------------------------------------");
        for (int i = 0; i < consultas.size(); i++) {
            Consulta consulta = consultas.get(i);
            System.out.printf("%-4d | %-25s | %-25s | %-16s | %-20s | %-10s%n",
                    (i + 1),
                    consulta.getPaciente().getNome(),
                    consulta.getMedico().getNome(),
                    consulta.getHorario().format(FORMATADOR_DATA_HORA),
                    consulta.getLocal(),
                    consulta.getStatus());
        }
        System.out.println("------------------------------------------------------------------------------------------------------------");
    }

    public static void listarInternacoes(List<Internacao> internacoes) {
        cabecalho("Lista de Internações");
        if (internacoes.isEmpty()) {
            mensagem("Nenhuma internação registrada.");
            return;
        }
        System.out.println("---------------------------------------------------------------------------------------------------------------------------");
        System.out.printf("%-4s | %-25s | %-25s | %-8s | %-10s | %-10s | %-12s | %-10s%n", "Nº", "PACIENTE", "MÉDICO RESP.", "QUARTO", "ENTRADA", "SAÍDA PREV.", "CUSTO TOTAL", "STATUS");
        System.out.println("---------------------------------------------------------------------------------------------------------------------------");
        for (int i = 0; i < internacoes.size(); i++) {
            Internacao internacao = internacoes.get(i);
            String status = internacao.isCancelada() ? "Cancelada" : "Ativa";
            double custoTotal = internacao.getCusto();
            String custoFmt = String.format("R$ %.2f", custoTotal);
            
            System.out.printf("%-4d | %-25s | %-25s | %-8s | %-10s | %-10s | %-12s | %-10s%n",
                    (i + 1),
                    internacao.getPaciente().getNome(),
                    internacao.getMedicoResponsavel().getNome(),
                    internacao.getQuarto().getNumero(),
                    internacao.getDataEntrada().format(FORMATADOR_DATA),
                    internacao.getDataSaida().format(FORMATADOR_DATA),
                    custoFmt,
                    status);
        }
        System.out.println("---------------------------------------------------------------------------------------------------------------------------");
    }
}

