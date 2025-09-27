package entidades;

import java.util.HashMap;
import java.util.Map;

public class PlanoSaude{
    private String fornecedor;
    private Map<String, Double> descontosPorEspecialidade;
    private boolean menosQueUmaSemana;

    public PlanoSaude(String fornecedor, boolean menosQueUmaSemana){
        this.fornecedor = fornecedor;
        this.descontosPorEspecialidade = new HashMap<>();
        this.menosQueUmaSemana = menosQueUmaSemana;
    }

    public String getFornecedor(){
        return fornecedor;
    }

    public void adicionarDesconto(String especialidade, double percentual) {
        descontosPorEspecialidade.put(especialidade, percentual);
    }

    public double getDesconto(String especialidade) {
        return descontosPorEspecialidade.getOrDefault(especialidade, 0.0);
    }

    public void setFornecedor(String novoFornecedor){
        this.fornecedor = novoFornecedor;
    }

    public boolean isMenosQueUmaSemana(){
        return menosQueUmaSemana;
    }

      @Override
    public String toString() {
        return "PlanoSaude {" +
                "nome='" + fornecedor + '\'' +
                ", descontos=" + descontosPorEspecialidade +
                ", internacaoGratisMenosDeUmaSemana=" + menosQueUmaSemana +
                '}';
    }

}