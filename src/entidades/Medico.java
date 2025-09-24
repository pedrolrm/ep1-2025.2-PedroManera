package entidades;

import java.util.ArrayList;

public class Medico extends Pessoa{
    private String crm;
    private String especialidade;
    private double custoConsulta;
    private ArrayList<Integer> horariosDisponiveis;
    private ArrayList<String> listaEspecialidades;

    public Medico(String nome, String crm, String especialidade, double custoConsulta){
        super(nome);
        this.crm = crm;
        this.especialidade = especialidade;
        this.custoConsulta = custoConsulta;
        this.horariosDisponiveis = new ArrayList<>();
        this.listaEspecialidades = new ArrayList<>();
    }

    //getters
    public String getCrm(){
        return this.crm;
    }

    public String getEspecialidade(){
        return this.especialidade;
    }

    public String getCustoConsulta(){
        return "O custo da consulta com o médico " + getNome() + "é " + custoConsulta;
    }

    public ArrayList<Integer> getHorariosDisponiveis(){
        return horariosDisponiveis;
    }

    //setters
    public void setCrm (String novoCrm){
        this.crm = novoCrm;
    }

    public void setEspecialidade(String novaEspecialidade){
        if (listaEspecialidades.contains(novaEspecialidade)){
            this.especialidade = novaEspecialidade;
        }
    }

    @Override
    public String toString() {
        return "Médico [Nome=" + getNome() + ", CRM=" + this.crm + ", Especialidade=" + this.especialidade + " custo da consulta: " + custoConsulta + "]";
    }
}
