package FriendsBank.api.controller;

import FriendsBank.api.dto.ContaRequestDTO;
import FriendsBank.api.dto.LoginRequestDTO; // Importando o novo DTO de Login
import FriendsBank.api.dto.TransferenciaRequestDTO;
import FriendsBank.api.model.Conta;
import FriendsBank.api.service.ContaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // Define que esta classe é uma API REST
@RequestMapping("/api/friendsbank") // A URL base para acessar este banco
public class ContaController {

    private final ContaService contaService;

    // O Spring conecta o Service aqui por Injeção de Dependência
    public ContaController(ContaService contaService) {
        this.contaService = contaService;
    }

    // 1. Endpoint para criar uma conta nova (ATUALIZADO com tratamento de erro)
    @PostMapping("/contas")
    public ResponseEntity<?> criarConta(@RequestBody ContaRequestDTO dto) {
        try {
            // Repassando os 4 parâmetros: numero, titular, tipoConta e senha
            Conta novaConta = contaService.abrirConta(dto.numero(), dto.titular(), dto.tipoConta(), dto.senha());
            return ResponseEntity.ok(novaConta);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 2. NOVO: Endpoint para fazer Login e autenticar
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO dto) {
        try {
            Conta contaLogada = contaService.login(dto.numero(), dto.senha());
            return ResponseEntity.ok(contaLogada);
        } catch (RuntimeException e) {
            // Retorna o status 401 (Não Autorizado) caso a senha ou conta estejam erradas
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }

    // 3. Endpoint para listar todas as contas (Usa nossa lista fake)
    @GetMapping("/contas")
    public ResponseEntity<List<Conta>> listarContas() {
        return ResponseEntity.ok(contaService.listarTodasAsContas());
    }

    // 4. Endpoint para fazer uma transferência entre amigos
    @PostMapping("/transferir")
    public ResponseEntity<String> transferir(@RequestBody TransferenciaRequestDTO dto) {
        try {
            contaService.transferir(dto.numeroOrigem(), dto.numeroDestino(), dto.valor());
            return ResponseEntity.ok("Transferência de R$ " + dto.valor() + " realizada com sucesso!");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 5. Endpoint usando STREAMS: Ver patrimônio total do banco
    @GetMapping("/patrimonio")
    public ResponseEntity<Double> verPatrimonio() {
        return ResponseEntity.ok(contaService.calcularPatrimonioTotal());
    }
}