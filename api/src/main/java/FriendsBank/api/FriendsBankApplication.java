package FriendsBank.api;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import FriendsBank.api.model.Conta;
import FriendsBank.api.repository.ContaRepository;

@SpringBootApplication
public class FriendsBankApplication {

    public static void main(String[] args) {
        SpringApplication.run(FriendsBankApplication.class, args);
    }

    @Bean
    public CommandLineRunner iniciarBancoFake(ContaRepository repository) {
        return args -> {
            System.out.println("\n===========================================");
            System.out.println("====== POPULANDO BANCO DE DADOS FAKE ======");
            System.out.println("===========================================");

            // Conta Rafael - Configurando saldos completos
            Conta conta1 = new Conta("123-x", "Rafael", "CORRENTE", "Raf@Pim2026#");
            conta1.depositar(5000.0);
            conta1.depositarInvestimento(12000.0);
            conta1.setLimiteCartao(8000.0);
            repository.salvar(conta1);

            // Conta Jaqueline - Configurando saldos completos
            Conta conta2 = new Conta("456-y", "Jaqueline", "INVESTIMENTO", "Jaq@Premium$88");
            conta2.depositar(2000.0);
            conta2.depositarInvestimento(10000.0);
            conta2.setLimiteCartao(5000.0);
            repository.salvar(conta2);

            System.out.println("✔ Dados de Rafael e Jaqueline inicializados com sucesso!");
            System.out.println("===========================================\n");
        };
    }
}