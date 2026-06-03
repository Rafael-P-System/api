package FriendsBank.api.model;

import java.util.UUID;

public class Conta {
    private final String id;
    private final String numero;
    private final String titular;
    private final String tipoConta;
    private final String senha;

    // Patrimônio segregado
    private double saldoCorrente;
    private double saldoInvestimento;
    private double limiteCartao;

    public Conta(String numero, String titular, String tipoConta, String senha) {
        this.id = UUID.randomUUID().toString();
        this.numero = numero;
        this.titular = titular;
        this.tipoConta = tipoConta;
        this.senha = senha;
        this.saldoCorrente = 0.0;
        this.saldoInvestimento = 0.0;
        this.limiteCartao = 0.0;
    }

    // --- MÉTODOS DE OPERAÇÃO (Exigidos pelo ContaService) ---

    public void depositar(double valor) {
        if (valor <= 0) throw new RuntimeException("Depósito deve ser positivo.");
        this.saldoCorrente += valor;
    }

    public void sacar(double valor) {
        if (valor <= 0) throw new RuntimeException("O valor do saque deve ser maior que zero.");
        if (valor > this.saldoCorrente) {
            throw new RuntimeException("Saldo insuficiente na conta corrente.");
        }
        this.saldoCorrente -= valor;
    }

    // --- MÉTODOS DE PATRIMÔNIO (Para cálculos e compatibilidade) ---

    public double getSaldo() {
        return this.saldoCorrente + this.saldoInvestimento;
    }

    public void depositarInvestimento(double valor) {
        if (valor <= 0) throw new RuntimeException("Investimento deve ser positivo.");
        this.saldoInvestimento += valor;
    }

    public void setLimiteCartao(double valor) {
        this.limiteCartao = valor;
    }

    // --- GETTERS ---
    public String getId() { return id; }
    public String getNumero() { return numero; }
    public String getTitular() { return titular; }
    public String getTipoConta() { return tipoConta; }
    public String getSenha() { return senha; }
    public double getSaldoCorrente() { return saldoCorrente; }
    public double getSaldoInvestimento() { return saldoInvestimento; }
    public double getLimiteCartao() { return limiteCartao; }
}