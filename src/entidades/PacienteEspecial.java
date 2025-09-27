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
        return String.format("Paciente Especial [Nome: %s, CPF: %s, Idade: %d, Plano: %s]",
            this.getNome(),
            this.getCpf(),
            this.getIdade(),
            this.planoSaude.getFornecedor());
    }
    
}
