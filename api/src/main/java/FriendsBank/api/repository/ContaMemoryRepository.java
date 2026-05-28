package FriendsBank.api.repository;

import FriendsBank.api.model.Conta; // Corrigido tirando o 'com.'
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository 
public class ContaMemoryRepository implements ContaRepository {

    private final List<Conta> bancoDeDadosFake = new ArrayList<>();

    @Override
    public Conta salvar(Conta conta) {
        bancoDeDadosFake.add(conta);
        return conta;
    }

    @Override
    public Optional<Conta> buscarPorNumero(String numero) {
        // Usando Streams para filtrar e buscar a conta pelo número
        return bancoDeDadosFake.stream()
                .filter(c -> c.getNumero().equals(numero))
                .findFirst();
    }

    @Override
    public List<Conta> listarTodas() {
        return new ArrayList<>(bancoDeDadosFake);
    }
}