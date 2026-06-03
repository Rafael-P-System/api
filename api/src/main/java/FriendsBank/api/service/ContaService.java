package FriendsBank.api.service;

import java.util.List;

import org.springframework.stereotype.Service;

import FriendsBank.api.model.Conta;
import FriendsBank.api.repository.ContaRepository;

@Service
public class ContaService {

    private final ContaRepository contaRepository;

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
        Conta origem = contaRepository.buscarPorNumero(numeroOrigem)
                .orElseThrow(() -> new RuntimeException("Conta de origem não encontrada."));
        Conta destino = contaRepository.buscarPorNumero(numeroDestino)
                .orElseThrow(() -> new RuntimeException("Conta de destino não encontrada."));

        synchronized (origem) {
            origem.sacar(valor);
            destino.depositar(valor);
            contaRepository.salvar(origem);
            contaRepository.salvar(destino);
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