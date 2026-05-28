package FriendsBank.api.dto; // Corrigido aqui!

public record TransferenciaRequestDTO(
    String numeroOrigem,
    String numeroDestino,
    double valor
) {}