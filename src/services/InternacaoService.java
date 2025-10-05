package services;

import entidades.*;
import java.time.LocalDate;
import java.util.List;

public class InternacaoService {

    public static Internacao agendarInternacao(Paciente paciente, Medico medico, Quarto quarto, LocalDate dataEntrada, int dias, double custo, List<Internacao> todasInternacoes) {
        if (HospitalService.isQuartoDisponivel(quarto, dataEntrada, todasInternacoes)) {
            Internacao novaInternacao = new Internacao(paciente, medico, quarto, dataEntrada, dias, custo, false);
            todasInternacoes.add(novaInternacao);
            HospitalService.registrarInternacao(novaInternacao);
            return novaInternacao;
        } else {
            System.out.println("Quarto ocupado neste período!");
            return null;
        }
    }

    // Cancelar internação
    public static void cancelarInternacao(Internacao internacao) {
        if (!internacao.isCancelada()) {
            internacao.cancelarInternacao();
            System.out.println("Internação cancelada com sucesso: " + internacao);
        } else {
            System.out.println("A internação já está cancelada.");
        }
    }

    // Listar internações de um paciente
    public static void listarInternacoesPaciente(Paciente paciente, List<Internacao> todasInternacoes) {
        System.out.println("Internações de " + paciente.getNome() + ":");
        for (Internacao i : todasInternacoes) {
            if (i.getPaciente().equals(paciente)) {
                System.out.println(i);
            }
        }
    }
}
