package entidades;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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

    public void setLocal (String novoLocal){
        this.local = novoLocal;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return String.format("Consulta [Paciente: %s, Médico: %s, Data/Hora: %s, Local: %s, Status: %s]",
            this.paciente.getNome(),
            this.medico.getNome(),
            this.horario.format(formatter),
            this.local,
            this.status);
    }
    
}
