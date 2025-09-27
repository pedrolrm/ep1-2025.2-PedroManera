package services;

import entidades.*;
import java.util.List;

public class QuartoService {

    // Cadastrar quarto
    public static Quarto cadastrarQuarto(String numero, TipoQuarto tipo, List<Quarto> quartos) {
        Quarto quarto = new Quarto(numero, tipo);
        quartos.add(quarto);
        System.out.println("Quarto cadastrado: " + quarto.getNumero() + " - " + tipo);
        return quarto;
    }

    // Marcar como ocupado
    public static void ocuparQuarto(Quarto quarto) {
        quarto.setOcupado(true);
        System.out.println("Quarto " + quarto.getNumero() + " agora está ocupado.");
    }

    // Listar quartos
    public static void listarQuartos(List<Quarto> quartos) {
        System.out.println("Quartos cadastrados:");
        for (Quarto q : quartos) {
            System.out.println("Número: " + q.getNumero() + ", Tipo: " + q.getTipo() + ", Ocupado: " + q.isOcupado());
        }
    }
}
