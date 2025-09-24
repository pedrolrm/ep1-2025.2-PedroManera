package entidades;

public class PacienteEspecial extends Paciente {
    private PlanoSaude planoSaude;

    public PacienteEspecial(String nome, int idade, String cpf, PlanoSaude planoSaude) {
        super(nome, cpf, idade);
        this.planoSaude = planoSaude;
    }

    public PlanoSaude getPlanoSaude() {
        return planoSaude;
    }

    public void setPlanoSaude(PlanoSaude planoSaude) {
        this.planoSaude = planoSaude;
    }

    public double calcularValorConsulta(String especialidade, double valorBase) {
        double desconto = planoSaude.getDesconto(especialidade);
        return valorBase * (1 - desconto / 100.0);
    }

    @Override
    public String toString() {
        return super.toString() +
               "\nPacienteEspecial {" +
               "planoSaude=" + planoSaude +
               '}';
    }
}
