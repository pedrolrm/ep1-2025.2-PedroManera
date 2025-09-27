package entidades;

import java.time.LocalDate;

public class Internacao {
    private Paciente paciente;
    private Medico medicoResponsavel;
    private Quarto quarto;
    private LocalDate dataEntrada;
    private LocalDate dataSaida; 
    private double custo;
    private boolean cancelada;

    public Internacao(Paciente paciente, Medico medicoResponsavel, Quarto quarto, LocalDate dataEntrada, int diasPrevistos, double valorDiaria) {
        this.paciente = paciente;
        this.medicoResponsavel = medicoResponsavel;
        this.quarto = quarto;
        this.dataEntrada = dataEntrada;
        this.dataSaida = dataEntrada.plusDays(diasPrevistos);
        this.cancelada = false;
        this.quarto.setOcupado(true);

        if (paciente instanceof PacienteEspecial) {
            PacienteEspecial especial = (PacienteEspecial) paciente;
            if (especial.getPlanoSaude().isMenosQueUmaSemana() && diasPrevistos < 7) {
                this.custo = 0.0;
            } else {
                this.custo = diasPrevistos * valorDiaria;
            }
        } else {
            this.custo = diasPrevistos * valorDiaria;
        }

        paciente.adicionarInternacaoAoHistorico(this.toString());
    }

    // Getters
    public Paciente getPaciente() {
        return paciente;
    }

    public Medico getMedicoResponsavel() {
        return medicoResponsavel;
    }

    public Quarto getQuarto() {
        return quarto;
    }

    public LocalDate getDataEntrada() {
        return dataEntrada;
    }

    public LocalDate getDataSaida() {
        return dataSaida;
    }

    public double getCusto() {
        return custo;
    }

    public boolean isCancelada() {
        return cancelada;
    }

    // Cancelamento da internação
    public void cancelarInternacao() {
        this.cancelada = true;
        this.quarto.setOcupado(false);
    }

    @Override
    public String toString() {
        return "Internacao {" +
               "Paciente=" + paciente.getNome() +
               ", MedicoResponsavel=" + medicoResponsavel.getNome() +
               ", Quarto=" + quarto.getNumero() +
               ", DataEntrada=" + dataEntrada +
               ", DataSaida=" + dataSaida +
               ", Custo=" + custo +
               ", Cancelada=" + cancelada +
               '}';
    }
}
