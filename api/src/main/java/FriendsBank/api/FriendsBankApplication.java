package FriendsBank.api;

import FriendsBank.api.model.Conta;
import FriendsBank.api.repository.ContaRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class FriendsBankApplication {

    public static void main(String[] args) {
        SpringApplication.run(FriendsBankApplication.class, args);
    }

    // CommandLineRunner: Executa esse bloco automaticamente logo após o servidor subir
    @Bean
    public CommandLineRunner iniciarBancoFake(ContaRepository repository) {
        return args -> {
            System.out.println("\n===========================================");
            System.out.println("====== POPULANDO BANCO DE DADOS FAKE ======");
            System.out.println("===========================================");

            // Amigo 1 - Conta do Rafael com senha forte de teste//
            Conta conta1 = new Conta("123-x", "Rafael", "CORRENTE", "Raf@Pim2026#");
            repository.salvar(conta1);

            // Amigo 2 - Conta da Jaqueline com senha forte de teste
            Conta conta2 = new Conta("456-y", "Jaqueline", "POUPANCA", "Jaq@Premium$88");
            repository.salvar(conta2);

            System.out.println("✔ Conta do Rafael criada (Nº 123-x)");
            System.out.println("✔ Conta da Jaqueline criada (Nº 456-y)");
            System.out.println("===========================================\n");
        };
    }
}