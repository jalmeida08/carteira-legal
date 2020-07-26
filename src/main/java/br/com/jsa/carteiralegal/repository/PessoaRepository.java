package br.com.jsa.carteiralegal.repository;

import br.com.jsa.carteiralegal.model.Pessoa;
import br.com.jsa.carteiralegal.model.Usuario;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PessoaRepository extends CrudRepository<Pessoa, Long> {

    public Optional<Pessoa> findByNumCpf(Long numCpf);
}
