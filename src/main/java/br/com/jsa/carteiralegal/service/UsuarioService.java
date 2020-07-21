package br.com.jsa.carteiralegal.service;

import br.com.jsa.carteiralegal.Util;
import br.com.jsa.carteiralegal.config.JwtTokenUtil;
import br.com.jsa.carteiralegal.model.Usuario;
import br.com.jsa.carteiralegal.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

@Service
public class UsuarioService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Override
    public UserDetails loadUserByUsername(String usuario) throws UsernameNotFoundException {
        return null;
    }

    public UserDetails login(Usuario usuario) {
        Usuario findByUsuario = usuarioRepository.findByUsuario(usuario.getUsuario()).get();
        if(findByUsuario != null) {
            if(usuario.getUsuario().equals(findByUsuario.getUsuario()) && BCrypt.checkpw(usuario.getSenha(), findByUsuario.getSenha())) {
                return new User(findByUsuario.getUsuario(), findByUsuario.getSenha(), new ArrayList<>());
            } else {
                throw new RuntimeException("Senha inválida.");
            }
        } else {
            throw new UsernameNotFoundException("Usuário inválido");
        }
    }

    public UserDetails dadosAutenticacaoAutorizacao(String usuario) {
        Usuario user = usuarioRepository.findByUsuario(usuario).get();
        return new User(user.getUsuario(), user.getSenha(), new ArrayList<>());
    }

    public Usuario entrarCarteiraLegal(Usuario usuario) {
        Optional<Usuario> u = usuarioRepository.findByEmail(usuario.getEmail());
        verificarpresencausuario( u, "Esse e-mail já esta cadastrado");
        Usuario u2 = usuarioRepository.save(usuario);
        return gerarChaveAtivacaoUsuario(u2.getId());
    }

    public Usuario buscarUsuarioChaveAtivacao(String chaveAtivacao){
        Optional<Usuario> u = usuarioRepository.findByChaveAtivacao(chaveAtivacao);
        verificarpresencausuario(u, "Chave de ativacao inválida");
        return u.get();
    }

    public Usuario finilizarCadastroUsuario(Usuario usuario){
        Optional<Usuario> u = null;
        u = usuarioRepository.findByUsuario(usuario.getUsuario());
        verificarpresencausuario(u, "Já existe esse usuário na base de dados.");
        u = usuarioRepository.findByEmail(usuario.getEmail());
        verificarpresencausuario(u, "E-mail não localizado no sistema.");

        usuario.setId(u.get().getId());
        String senha = BCrypt.hashpw(usuario.getSenha(), BCrypt.gensalt());
        usuario.setSenha(senha);
        usuario.setAtivo(false);
        usuario.setChaveAtivacao("");
        Usuario user = usuarioRepository.save(usuario);
        return loginAutomaticoViaSistema(user);
    }

    private void verificarpresencausuario(Optional<Usuario> u, String mensagemErroUsuarioNaoPresente) {
        if(u.isPresent()){
            throw new RuntimeException(mensagemErroUsuarioNaoPresente);
        }
    }

    private Usuario loginAutomaticoViaSistema(Usuario user) {
        UserDetails userDetails = new User(user.getUsuario(), user.getSenha(), new ArrayList<>());
        user.setToken(jwtTokenUtil.generateToken(userDetails));
        user.setSenha("");
        user.setUltimoLogin(new Date());
        user.setAtivo(true);
        usuarioRepository.save(user);
        return user;
    }

    private Usuario gerarChaveAtivacaoUsuario(Long id){
        Optional<Usuario> u = usuarioRepository.findById(id);
        if(u.isPresent()){
            Usuario usuario = u.get();
            usuario.setChaveAtivacao(Util.criptografar(usuario.getId().toString()));
            usuarioRepository.save(usuario);
            return usuario;
        }else{
            throw new RuntimeException("Não foi possível localizar o usuario");
        }
    }
}