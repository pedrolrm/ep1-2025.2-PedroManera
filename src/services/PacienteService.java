package services;

import entidades.*;
import java.util.List;

public class PacienteService {

    // Cadastrar paciente
    public static Paciente cadastrarPaciente(String nome, String cpf, int idade, List<Paciente> pacientes) {
        Paciente paciente = new Paciente(nome, cpf, idade);
        pacientes.add(paciente);
        System.out.println("Paciente cadastrado: " + paciente);
        return paciente;
    }

    // Atualizar idade
    public static void atualizarIdade(Paciente paciente, int novaIdade) {
        paciente.setIdade(novaIdade);
        System.out.println("Idade atualizada: " + paciente);
    }

    // Mostrar histórico do paciente
    public static void mostrarHistorico(Paciente paciente) {
        System.out.println("Histórico de consultas de " + paciente.getNome() + ": " + paciente.getHistoricoConsultas());
        System.out.println("Histórico de internações de " + paciente.getNome() + ": " + paciente.getHistoricoInternacoes());
    }
}
