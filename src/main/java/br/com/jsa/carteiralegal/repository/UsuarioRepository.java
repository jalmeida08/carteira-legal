package br.com.jsa.carteiralegal.repository;

import br.com.jsa.carteiralegal.model.Usuario;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends CrudRepository<Usuario, Long> {

    public Optional<Usuario> findByUsuario(String usuario);
    public Optional<Usuario> findByEmail(String email);
    public Optional<Usuario> findByChaveAtivacao(String chaveAtivacao);


}
