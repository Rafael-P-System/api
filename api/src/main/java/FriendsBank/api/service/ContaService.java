package FriendsBank.api.service;

import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.springframework.stereotype.Service;

import FriendsBank.api.model.Conta;
import FriendsBank.api.repository.ContaRepository;

@Service
public class ContaService {

    private final ContaRepository contaRepository;
    // Criamos uma trava (Lock) explícita para controlar o acesso ao saldo
    private final Lock lock = new ReentrantLock();

    public ContaService(ContaRepository contaRepository) {
        this.contaRepository = contaRepository;
    }

    public Conta abrirConta(String numero, String titular, String tipoConta, String senha) {
        if (contaRepository.buscarPorNumero(numero).isPresent()) {
            throw new RuntimeException("Uma conta com este número já existe!");
        }
        Conta novaConta = new Conta(numero, titular, tipoConta, senha);
        return contaRepository.salvar(novaConta);
    }

    public Conta login(String numero, String senha) {
        Conta conta = contaRepository.buscarPorNumero(numero)
                .orElseThrow(() -> new RuntimeException("Conta não encontrada."));
        if (!conta.getSenha().equals(senha)) {
            throw new RuntimeException("Senha incorreta!");
        }
        return conta;
    }

    public void transferir(String numeroOrigem, String numeroDestino, double valor) {
        if (valor <= 0) {
            throw new RuntimeException("O valor da transferência deve ser maior que zero.");
        }

        // Ativamos a trava antes de ler os saldos. 
        // Se outra requisição tentar transferir ao mesmo tempo, ela espera aqui.
        lock.lock(); 
        try {
            Conta origen = contaRepository.buscarPorNumero(numeroOrigem)
                    .orElseThrow(() -> new RuntimeException("Conta de origem não encontrada."));
            Conta destino = contaRepository.buscarPorNumero(numeroDestino)
                    .orElseThrow(() -> new RuntimeException("Conta de destino não encontrada."));

            // Validação de Consistência (O 'C' do ACID)
            if (origen.getSaldo() < valor) {
                throw new RuntimeException("Saldo insuficiente para realizar a transferência!");
            }

            // Executa a movimentação com segurança
            origen.sacar(valor);
            destino.depositar(valor);

            contaRepository.salvar(origen);
            contaRepository.salvar(destino);

        } finally {
            // O bloco finally garante que, mesmo se der erro de saldo insuficiente,
            // a trava SERÁ LIBERADA para a próxima transação não travar o sistema.
            lock.unlock(); 
        }
    }

    public double calcularPatrimonioTotal() {
        return contaRepository.listarTodas().stream()
                .mapToDouble(Conta::getSaldo)
                .sum();
    }

    public List<Conta> listarTodasAsContas() {
        return contaRepository.listarTodas();
    }
}