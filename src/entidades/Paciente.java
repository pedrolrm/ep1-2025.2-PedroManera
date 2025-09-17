package entidades;

import java.util.ArrayList;

public class Paciente {
    private String nome;
    private String cpf;
    private int idade;
    private ArrayList<String> historicoConsultas;
    private ArrayList<String> historicoInternacoes;
    
    //construtor
    public Paciente(String nome, String cpf, int idade) {
        this.nome = nome;
        this.cpf = cpf;
        this.idade = idade;
        this.historicoConsultas = new ArrayList<>();
        this.historicoInternacoes = new ArrayList<>();
    }

    //getters
    public String getNome() {
        return this.nome;
    }

    public String getCpf() {
        return this.cpf;
    }

    public int getIdade() {
        return this.idade;
    }

    public ArrayList<String> getHistoricoConsultas() {
        return this.historicoConsultas;
    }

    public ArrayList<String> getHistoricoInternacoes() {
        return this.historicoInternacoes;
    }

    //setters
    public void setNome(String novoNome){
        this.nome = novoNome;
    }

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

    //formatando print do paciente
    @Override
    public String toString() {
        return "Paciente [Nome=" + this.nome + ", CPF=" + this.cpf + ", Idade=" + this.idade + "]";
    }
}