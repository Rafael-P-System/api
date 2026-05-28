package FriendsBank.api.service;

import FriendsBank.api.model.Conta;
import FriendsBank.api.repository.ContaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service // Avisa ao Spring que esta classe cuida das regras de negócio do banco
public class ContaService {

    // O Spring vai varrer o container, achar o ContaMemoryRepository e injetar aqui automaticamente
    private final ContaRepository contaRepository;

    // Injeção de dependência por construtor (melhor prática de mercado)
    public ContaService(ContaRepository contaRepository) {
        this.contaRepository = contaRepository;
    }

    // Regra de Negócio: Abrir Conta (ATUALIZADO: Agora recebe senha)
    public Conta abrirConta(String numero, String titular, String tipoConta, String senha) {
        // Validação simples: não permitir número duplicado
        if (contaRepository.buscarPorNumero(numero).isPresent()) {
            throw new RuntimeException("Uma conta com este número já existe no FriendsBank!");
        }
        // Cria a conta passando a senha para o novo construtor do modelo
        Conta novaConta = new Conta(numero, titular, tipoConta, senha);
        return contaRepository.salvar(novaConta);
    }

    // NOVA Regra de Negócio: Autenticar Login
    public Conta login(String numero, String senha) {
        // Busca a conta pelo número. Se não achar, estoura erro
        Conta conta = contaRepository.buscarPorNumero(numero)
                .orElseThrow(() -> new RuntimeException("Conta não encontrada."));

        // Valida se a senha digitada bate com a senha guardada
        if (!conta.getSenha().equals(senha)) {
            throw new RuntimeException("Senha incorreta!");
        }

        return conta; // Retorna a conta autenticada com sucesso
    }

    // Regra de Negócio: Transferência entre amigos
    public void transferir(String numeroOrigem, String numeroDestino, double valor) {
        Conta origem = contaRepository.buscarPorNumero(numeroOrigem)
                .orElseThrow(() -> new RuntimeException("Conta de origem não encontrada."));

        Conta destino = contaRepository.buscarPorNumero(numeroDestino)
                .orElseThrow(() -> new RuntimeException("Conta de destino não encontrada."));

        // O próprio modelo executa as validações de saldo que fizemos no encapsulamento
        origem.sacar(valor);
        destino.depositar(valor);

        // Atualiza as duas contas no nosso banco fake
        contaRepository.salvar(origem);
        contaRepository.salvar(destino);
    }

    // Regra de Negócio usando STREAMS (Java 21): Calcular patrimônio total do banco
    public double calcularPatrimonioTotal() {
        return contaRepository.listarTodas().stream()
                .mapToDouble(Conta::getSaldo)
                .sum();
    }

    // Listar todas as contas cadastradas
    public List<Conta> listarTodasAsContas() {
        return contaRepository.listarTodas();
    }
}