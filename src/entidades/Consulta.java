package entidades;
import java.time.LocalDateTime;

public class Consulta {
    private Paciente paciente;
    private Medico medico;
    private LocalDateTime horario;
    private String local;
    private StatusConsulta status;

    public Consulta(Paciente paciente, Medico medico, LocalDateTime horario, String local, StatusConsulta status){
        this.paciente = paciente;
        this.medico = medico;
        this.horario = horario;
        this.local = local;
        this.status = status;
    }

    public Paciente getPaciente(){
        return paciente;
    }

    public Medico getMedico(){
        return medico;
    }

    public LocalDateTime getHorario(){
        return horario;
    }

    public String getLocal(){
        return local;
    }

    public StatusConsulta getStatus(){
        return status;
    }

    public void setStatus(StatusConsulta novoStatus){
        this.status = novoStatus;
    }

     @Override
    public String toString() {
        return "Consulta {" +
                "\n  Paciente: " + paciente +
                ",\n  Medico: " + medico +
                ",\n  Data/Hora: " + horario +
                ",\n  Local: '" + local + '\'' +
                ",\n  Status: " + getStatus() +
                "\n}";
    }
    
}
