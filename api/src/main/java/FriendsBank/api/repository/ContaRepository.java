package FriendsBank.api.repository;

import java.util.List;
import java.util.Optional;

import FriendsBank.api.model.Conta;

public interface ContaRepository {
    Conta salvar(Conta conta);
    Optional<Conta> buscarPorNumero(String numero);
    List<Conta> listarTodas();
}