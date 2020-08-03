package br.com.jsa.carteiralegal.repository;

import br.com.jsa.carteiralegal.model.Despesa;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DespesaRepository extends CrudRepository<Despesa, Long> {

    @Query(value="SELECT d.* FROM Despesa d INNER JOIN pessoa_despesa pd on pd.despesa_id = d.id inner join Pessoa p on p.id = pd.pessoa_id where p.id=?1 and data_vencimento between ?2 and ?3", nativeQuery = true)
    public List<Despesa> listarDespesaMesAtual(Long idPessoa, LocalDate dataInicio, LocalDate dataFim);

}
