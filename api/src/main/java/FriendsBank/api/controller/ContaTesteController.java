package FriendsBank.api.controller;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class ContaTesteController {

    // Simula um banco de dados com duas contas: a "antiga" e a "nova de teste"
    private static final Map<String, Double> saldosNoBanco = new HashMap<>();
    private static final Map<String, List<Map<String, Object>>> extratosNoBanco = new HashMap<>();

    // Bloco estático para inicializar as duas contas de teste na memória
    static {
        // Conta Antiga (exemplo: '123-x')
        saldosNoBanco.put("123-x", 1500.00);
        extratosNoBanco.put("123-x", new ArrayList<>(List.of(
            Map.of("id", 1L, "descricao", "Saldo Inicial Conta Antiga", "valor", 1500.00, "data", "28/05/2026")
        )));

        // Conta de Teste Nova (exemplo: qualquer outra que o front enviar, ou '4321-y')
        saldosNoBanco.put("4321-y", 5000.00);
        extratosNoBanco.put("4321-y", new ArrayList<>(List.of(
            Map.of("id", 2L, "descricao", "Saldo Inicial Conta Premium", "valor", 5000.00, "data", "28/05/2026")
        )));
    }

    // 1. BUSCAR SALDO DINÂMICO
    @GetMapping("/contas/{numero}/saldo")
    public ResponseEntity<Map<String, Object>> getSaldo(@PathVariable String numero) {
        // Se a conta não existir no map, inicia com 1000.00 para não quebrar o teste
        double saldo = saldosNoBanco.computeIfAbsent(numero, k -> 1000.00);
        return ResponseEntity.ok(Map.of("saldo", saldo));
    }

    // 2. BUSCAR EXTRATO DINÂMICO
    @GetMapping("/contas/{numero}/extrato")
    public ResponseEntity<List<Map<String, Object>>> getExtrato(@PathVariable String numero) {
        List<Map<String, Object>> extrato = extratosNoBanco.computeIfAbsent(numero, k -> new ArrayList<>());
        return ResponseEntity.ok(extrato);
    }

    // 3. PROCESSAR PIX ENTRE CONTAS
    @PostMapping("/transacoes/pix")
    public ResponseEntity<Map<String, String>> processarPix(@RequestBody Map<String, Object> payload) {
        String contaOrigem = payload.get("contaOrigem").toString();
        double valor = Double.parseDouble(payload.get("valor").toString());
        String destitnatarioChave = payload.get("destinatario").toString(); // Aqui seria o número da outra conta no teste

        double saldoOrigem = saldosNoBanco.computeIfAbsent(contaOrigem, k -> 1000.00);

        if (valor > saldoOrigem) {
            return ResponseEntity.badRequest().body(Map.of("mensagem", "Saldo insuficiente para transferência."));
        }

        // Subtrai da conta de Origem
        saldosNoBanco.put(contaOrigem, saldoOrigem - valor);
        extratosNoBanco.get(contaOrigem).add(0, Map.of(
            "id", System.currentTimeMillis(),
            "descricao", "Pix enviado para " + destitnatarioChave,
            "valor", -valor,
            "data", "28/05/2026"
        ));

        // SE O DESTINATÁRIO FOR A OUTRA CONTA QUE ESTÁ NA MEMÓRIA, ADICIONA O SALDO LÁ!
        if (saldosNoBanco.containsKey(destitnatarioChave)) {
            double saldoDestino = saldosNoBanco.get(destitnatarioChave);
            saldosNoBanco.put(destitnatarioChave, saldoDestino + valor);
            extratosNoBanco.get(destitnatarioChave).add(0, Map.of(
                "id", System.currentTimeMillis() + 1,
                "descricao", "Pix recebido de " + contaOrigem,
                "valor", valor, // Valor positivo porque entrou dinheiro
                "data", "28/05/2026"
            ));
        }

        return ResponseEntity.ok(Map.of("mensagem", "Transferência Pix realizada com sucesso!"));
    }
}