package br.com.jsa.carteiralegal.service;

import br.com.jsa.carteiralegal.config.JwtTokenUtil;
import br.com.jsa.carteiralegal.exception.SessaoInexistenteException;
import br.com.jsa.carteiralegal.model.Pessoa;
import br.com.jsa.carteiralegal.model.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class UtilServices {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UsuarioService usuarioService;



    private String recuperarToken (){
        return request.getHeader("Authorization");
    }

    public Long recuperarIdUsuarioToken() throws SessaoInexistenteException {
        String usuarioToken = jwtTokenUtil.getUsernameFromToken(recuperarTokenUsuario());
        Usuario usuario = usuarioService.buscarDadosUsuario(usuarioToken);
        if(usuario != null) {
            if (usuario.getId() == Long.valueOf(recuperarToken().split("#")[2]))
                return Long.valueOf(recuperarToken().split("#")[2]);
        }
        throw new SessaoInexistenteException();
    }

    public Pessoa recuperarPessoaPorToken() throws SessaoInexistenteException {
        String usuarioToken = jwtTokenUtil.getUsernameFromToken(recuperarTokenUsuario());
        Usuario usuario = usuarioService.buscarDadosUsuario(usuarioToken);
        if(usuario != null) {
            if (usuario.getId() == Long.valueOf(recuperarToken().split("#")[2]))
                return usuario.getPessoa();
        }
        throw new SessaoInexistenteException();
    }

    public String recuperarTokenUsuario(){
        return recuperarToken().split("#")[1];
    }
}
