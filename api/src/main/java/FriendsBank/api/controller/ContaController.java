package FriendsBank.api.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import FriendsBank.api.dto.ContaRequestDTO;
import FriendsBank.api.dto.LoginRequestDTO;
import FriendsBank.api.dto.TransferenciaRequestDTO;
import FriendsBank.api.model.Conta;
import FriendsBank.api.service.ContaService;

@RestController
@RequestMapping("/api/friendsbank")
@CrossOrigin(origins = "*")
public class ContaController {

    private final ContaService contaService;

    public ContaController(ContaService contaService) {
        this.contaService = contaService;
    }

    // 1. Endpoint para criar uma conta nova
    @PostMapping("/contas")
    public ResponseEntity<?> criarConta(@RequestBody ContaRequestDTO dto) {
        try {
            Conta novaConta = contaService.abrirConta(dto.numero(), dto.titular(), dto.tipoConta(), dto.senha());
            return ResponseEntity.ok(novaConta);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 2. Endpoint para fazer Login
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO dto) {
        try {
            Conta contaLogada = contaService.login(dto.numero(), dto.senha());
            return ResponseEntity.ok(contaLogada);
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }

    // 3. Endpoint para listar todas as contas
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

    // 5. Endpoint para ver patrimônio total
    @GetMapping("/patrimonio")
    public ResponseEntity<Double> verPatrimonio() {
        return ResponseEntity.ok(contaService.calcularPatrimonioTotal());
    }
}