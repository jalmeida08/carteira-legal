package br.com.jsa.carteiralegal.service;

import br.com.jsa.carteiralegal.Util;
import br.com.jsa.carteiralegal.config.JwtTokenUtil;
import br.com.jsa.carteiralegal.exception.DadoDuplicadoException;
import br.com.jsa.carteiralegal.exception.DadoInexistenteException;
import br.com.jsa.carteiralegal.exception.SessaoInexistenteException;
import br.com.jsa.carteiralegal.exception.pessoa.NumCpfJaCadastradoException;
import br.com.jsa.carteiralegal.exception.usuario.EmailJaCadastradoException;
import br.com.jsa.carteiralegal.model.Pessoa;
import br.com.jsa.carteiralegal.model.Usuario;
import br.com.jsa.carteiralegal.repository.PessoaRepository;
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
    private PessoaRepository pessoaRepository;

    @Autowired
    private MensageriaService mensageriaService;

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
                return new User(findByUsuario.getUsuario(), usuario.getSenha(), new ArrayList<>());
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

    public Usuario entrarCarteiraLegal(Usuario usuario) throws EmailJaCadastradoException, NumCpfJaCadastradoException {

        Optional<Usuario> u = usuarioRepository.findByEmail(usuario.getEmail());
        if(u.isPresent()) throw new EmailJaCadastradoException();

        Optional<Pessoa> p = pessoaRepository.findByNumCpf(usuario.getPessoa().getNumCpf());
        if(p.isPresent()) throw new NumCpfJaCadastradoException();

        pessoaRepository.save(usuario.getPessoa());
        Usuario u2 = usuarioRepository.save(usuario);
        return gerarChaveAtivacaoUsuario(u2);
    }

    public Usuario buscarUsuarioChaveAtivacao(String chaveAtivacao) throws DadoInexistenteException {
        Optional<Usuario> u = usuarioRepository.findByChaveAtivacao(chaveAtivacao);
        if(!u.isPresent()) throw new DadoInexistenteException("Chave de ativação");
        return u.get();
    }

    public Usuario finilizarCadastroUsuario(Usuario usuario) throws DadoDuplicadoException, DadoInexistenteException {
        Optional<Usuario> u = null;

        u = usuarioRepository.findByUsuario(usuario.getUsuario());
        if(u.isPresent()) throw new DadoDuplicadoException("Usuário");

        u = usuarioRepository.findByChaveAtivacao(usuario.getChaveAtivacao());
        if(!u.isPresent()) throw new DadoInexistenteException("Chave de ativação");

        Usuario userASerSalvo = u.get();
        String senha = BCrypt.hashpw(usuario.getSenha(), BCrypt.gensalt());
        userASerSalvo.setSenha(senha);
        userASerSalvo.setAtivo(false);
        userASerSalvo.setUsuario(usuario.getUsuario());
        userASerSalvo.setChaveAtivacao("");
        Usuario user = usuarioRepository.save(userASerSalvo);
        return loginAutomaticoViaSistema(user, usuario.getSenha());
    }

    private Usuario loginAutomaticoViaSistema(Usuario user, String senha) {
        UserDetails userDetails = new User(user.getUsuario(), senha, new ArrayList<>());
        user.setToken(jwtTokenUtil.generateToken(userDetails));
        user.setUltimoLogin(new Date());
        if(!user.isAtivo()) user.setAtivo(true);
        Usuario u = usuarioRepository.save(user);
        u.setSenha("");
        return u;
    }

    private Usuario gerarChaveAtivacaoUsuario(Usuario usuario){
        usuario.setChaveAtivacao(Util.criptografar(usuario.getId().toString()));
        usuarioRepository.save(usuario);
        mensageriaService.enviarEmailNovoUsuario(usuario.getEmail(), usuario.getPessoa().getNome(), usuario.getChaveAtivacao());
        return usuario;
    }

    public Usuario buscarDadosUsuario(String usuario) {
        return usuarioRepository.findByUsuario(usuario).get();
    }

    public void verificarUsuarioRequisicao(Long idUsuario, String tokenUsuario) throws SessaoInexistenteException {
        String usuario = jwtTokenUtil.getUsernameFromToken(tokenUsuario);
        if(usuario.isEmpty())
            throw new SessaoInexistenteException();
        Usuario userLogado = this.buscarDadosUsuario(usuario);

        if(userLogado.getId() != idUsuario)
            throw new SessaoInexistenteException();
    }
}
