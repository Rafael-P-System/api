package FriendsBank.api.model;

import java.util.UUID;

public class Conta {
    private String id;
    private String numero;
    private String titular;
    private double saldo;
    private String tipoConta;
    private String senha; // Nova engrenagem para o login seguro!

    // Construtor completo atualizado para receber os 4 parâmetros
    public Conta(String numero, String titular, String tipoConta, String senha) {
        this.id = UUID.randomUUID().toString(); // Gera um ID único automático
        this.numero = numero;
        this.titular = titular;
        this.tipoConta = tipoConta;
        this.senha = senha;
        this.saldo = 0.0; // Toda conta premium começa com saldo zerado até receber depósito
    }

    // Regra de Negócio: Realizar um saque seguro com validação de saldo
    public void sacar(double valor) {
        if (valor <= 0) {
            throw new RuntimeException("O valor do saque deve ser maior que zero.");
        }
        if (valor > this.saldo) {
            throw new RuntimeException("Saldo insuficiente para realizar a operação.");
        }
        this.saldo -= valor;
    }

    // Regra de Negócio: Realizar um depósito verificado
    public void depositar(double valor) {
        if (valor <= 0) {
            throw new RuntimeException("O valor do depósito deve ser maior que zero.");
        }
        this.saldo += valor;
    }

    // ========== GETTERS (Os acessos que as outras camadas precisam) ==========
    
    public String getId() {
        return id;
    }

    public String getNumero() {
        return numero; // ✔ Isso vai limpar o erro do ContaMemoryRepository!
    }

    public String getTitular() {
        return titular;
    }

    public double getSaldo() {
        return saldo;
    }

    public String getTipoConta() {
        return tipoConta;
    }

    public String getSenha() {
        return senha; // Permite ao ContaService validar o login no backend
    }
}