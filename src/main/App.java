// Localização: src/main/App.java

package main;

// Para usar a classe Paciente, que está em outro pacote, precisamos importá-la!
import entidades.Medico;

public class App {

    public static void main(String[] args) {
        System.out.println("--- Laboratório de Testes da Classe Paciente ---");

        // 1. TESTE DE CRIAÇÃO (CONSTRUTOR)
        // Vamos criar uma instância (um objeto real) da classe Paciente
        // A sintaxe é: NomeDaClasse nomeDaVariavel = new NomeDaClasse(parametros do construtor);
        Medico paciente1 = new Medico("Carlos de Souza", "123", "cirurgião", 100.000);
        
        System.out.println("Paciente criado com sucesso!");
        
        // 2. TESTE DO toString()
        // Ao tentar imprimir o objeto diretamente, o Java chama o método toString() que escrevemos.
        System.out.println(paciente1); 
    }
}