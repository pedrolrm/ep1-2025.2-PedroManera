package services;

import entidades.*;
import java.util.List;

public class PlanoSaudeService {

    // Cadastrar plano de saúde
    public static PlanoSaude cadastrarPlano(String fornecedor, boolean menosQueUmaSemana, List<PlanoSaude> planos) {
        PlanoSaude plano = new PlanoSaude(fornecedor, menosQueUmaSemana);
        planos.add(plano);
        System.out.println("Plano de saúde cadastrado: " + plano);
        return plano;
    }

    // Adicionar desconto
    public static void adicionarDesconto(PlanoSaude plano, String especialidade, double percentual) {
        plano.adicionarDesconto(especialidade, percentual);
        System.out.println("Desconto adicionado: " + especialidade + " - " + percentual + "%");
    }

    // Listar planos
    public static void listarPlanos(List<PlanoSaude> planos) {
        System.out.println("Planos de saúde cadastrados:");
        for (PlanoSaude p : planos) {
            System.out.println(p);
        }
    }
}
