package br.com.jsa.carteiralegal.repository;

import br.com.jsa.carteiralegal.model.Pagamento;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PagamentoRepository extends CrudRepository<Pagamento, Long> {

    @Query(value="SELECT p.* FROM Pagamento p where p.pessoa_id = ?1 and ativo = 1 and  data_referencia between ?2 and ?3", nativeQuery = true)
    public List<Pagamento> listaPagamentosUsuario(Long idPessoa, LocalDate dataInicio, LocalDate dataFim);

}
