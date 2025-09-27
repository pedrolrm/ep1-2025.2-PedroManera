package entidades;

import java.util.ArrayList;

public class Paciente extends Pessoa{
    private String cpf;
    private int idade;
    private ArrayList<String> historicoConsultas;
    private ArrayList<String> historicoInternacoes;
    
    //construtor
    public Paciente(String nome, String cpf, int idade) {
        super(nome);
        this.cpf = cpf;
        this.idade = idade;
        this.historicoConsultas = new ArrayList<>();
        this.historicoInternacoes = new ArrayList<>();
    }

    //getters
    public String getCpf() {
        return cpf;
    }

    public int getIdade() {
        return idade;
    }

    public ArrayList<String> getHistoricoConsultas() {
        return historicoConsultas;
    }

    public ArrayList<String> getHistoricoInternacoes() {
        return historicoInternacoes;
    }

    //setters
    public void setIdade(int novaIdade){
        if (novaIdade > 0) {
            this.idade = novaIdade;
        }
    }


    //consulta e internação
    public void adicionarConsultaAoHistorico(String registroConsulta) {
        this.historicoConsultas.add(registroConsulta);
    }

    public void adicionarInternacaoAoHistorico(String registroInternacao) {
        this.historicoInternacoes.add(registroInternacao);
    }

    @Override
    public String toString() {
        return String.format("Paciente [Nome: %s, CPF: %s, Idade: %d]",
            this.getNome(),
            this.cpf,
            this.idade);
    }
    
}