package services;

import entidades.*;
import java.util.List;

public class MedicoService {

    // Cadastrar médico
    public static Medico cadastrarMedico(String nome, String crm, Especialidade especialidade, double custo, List<Medico> medicos) {
        Medico medico = new Medico(nome, crm, especialidade, custo);
        medicos.add(medico);
        System.out.println("Médico cadastrado: " + medico);
        return medico;
    }

    // Alterar especialidade
    public static void alterarEspecialidade(Medico medico, Especialidade novaEspecialidade) {
        medico.setEspecialidade(novaEspecialidade);
        System.out.println("Especialidade atualizada: " + medico);
    }

    // Listar médicos
    public static void listarMedicos(List<Medico> medicos) {
        System.out.println("Médicos cadastrados:");
        for (Medico m : medicos) {
            System.out.println(m);
        }
    }
}
