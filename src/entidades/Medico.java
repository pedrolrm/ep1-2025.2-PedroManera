package entidades;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Medico extends Pessoa{
    private String crm;
    private Especialidade especialidade;
    private double custoConsulta;
    private ArrayList<LocalDateTime> horariosDisponiveis;

    public Medico(String nome, String crm, Especialidade especialidade, double custoConsulta){
        super(nome);
        this.crm = crm;
        this.especialidade = especialidade;
        this.custoConsulta = custoConsulta;
        this.horariosDisponiveis = new ArrayList<>();
    }

    //getters
    public String getCrm(){
        return crm;
    }

    public Especialidade getEspecialidade(){
        return especialidade;
    }

    public Double getCustoConsulta(){
        return custoConsulta;
    }

    public ArrayList<LocalDateTime> getHorariosDisponiveis(){
        return horariosDisponiveis;
    }

    //setters
    public void setCrm (String novoCrm){
        this.crm = novoCrm;
    }

    public void setEspecialidade(Especialidade novaEspecialidade){
        this.especialidade = novaEspecialidade;
    }

    @Override
    public String toString() {
        return "Médico [Nome=" + getNome() + ", CRM=" + this.crm + ", Especialidade=" + this.especialidade + " custo da consulta: " + custoConsulta + "]";
    }
}
