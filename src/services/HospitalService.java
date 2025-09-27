package services;

import entidades.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class HospitalService {

    public static boolean isMedicoDisponivel(Medico medico, LocalDateTime horario, List<Consulta> todasConsultas) {
        for (Consulta c : todasConsultas) {
            if (c.getMedico().equals(medico) &&
                c.getHorario().equals(horario) &&
                c.getStatus() != StatusConsulta.CANCELADA) {
                return false; // conflito
            }
        }
        return true;
    }

    public static boolean isQuartoDisponivel(Quarto quarto, LocalDate data, List<Internacao> todasInternacoes) {
        for (Internacao i : todasInternacoes) {
            if (i.getQuarto().equals(quarto) && !i.isCancelada() &&
                !(data.isBefore(i.getDataEntrada()) || data.isAfter(i.getDataSaida()))) {
                return false; // conflito
            }
        }
        return true;
    }

    public static void registrarConsulta(Consulta consulta) {
        consulta.getPaciente().adicionarConsultaAoHistorico(consulta.toString());
    }

    public static void registrarInternacao(Internacao internacao) {
        internacao.getPaciente().adicionarInternacaoAoHistorico(internacao.toString());
    }

    public static void cancelarInternacao(Internacao internacao) {
        internacao.cancelarInternacao();
    }
}
