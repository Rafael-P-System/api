package FriendsBank.api.dto;

public record ContaRequestDTO(
    String numero,
    String titular,
    String tipoConta,
    String senha // Adicionado!
) {}