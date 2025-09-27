package services;
import java.time.LocalDateTime;
import java.util.List;

import entidades.*;

public class ConsultaService {

    public static Consulta marcarConsulta(Paciente paciente, Medico medico, LocalDateTime horario, String local, List<Consulta> todasConsultas){
        if (HospitalService.isMedicoDisponivel(medico, horario, todasConsultas)){
        Consulta novaConsulta = new Consulta(paciente, medico, horario, local, StatusConsulta.AGENDADA);
        todasConsultas.add(novaConsulta);

        HospitalService.registrarConsulta(novaConsulta);

        return novaConsulta;

        }
        System.out.println("O médico " + medico.getNome() + " está ocupado");
        return null;
    }

    public static void cancelarConsulta(Consulta consulta) {
        if (consulta.getStatus() == StatusConsulta.AGENDADA) {
            consulta.setStatus(StatusConsulta.CANCELADA);
            System.out.println("Consulta cancelada com sucesso: " + consulta);
        } else {
            System.out.println("Não é possível cancelar uma consulta que já não está AGENDADA.");
        }
    }

    public static void listarConsultasPorPaciente(Paciente paciente, List<Consulta> todasConsultas) {
        System.out.println("Consultas do paciente " + paciente.getNome() + ":");
        for (Consulta c : todasConsultas) {
            if (c.getPaciente().equals(paciente)) {
                System.out.println(c);
            }
        }
    }

    public static void listarConsultasPorMedico(Medico medico, List<Consulta> todasConsultas) {
        System.out.println("Consultas do médico " + medico.getNome() + ":");
        for (Consulta c : todasConsultas) {
            if (c.getMedico().equals(medico)) {
                System.out.println(c);
            }
        }
    }
    
}
