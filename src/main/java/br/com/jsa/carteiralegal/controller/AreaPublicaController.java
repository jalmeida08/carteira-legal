package br.com.jsa.carteiralegal.controller;

import br.com.jsa.carteiralegal.config.JwtTokenUtil;
import br.com.jsa.carteiralegal.exception.DadoDuplicadoException;
import br.com.jsa.carteiralegal.exception.DadoInexistenteException;
import br.com.jsa.carteiralegal.exception.pessoa.NumCpfJaCadastradoException;
import br.com.jsa.carteiralegal.exception.usuario.EmailJaCadastradoException;
import br.com.jsa.carteiralegal.model.Usuario;
import br.com.jsa.carteiralegal.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@RestController
@RequestMapping("area-publica")
@CrossOrigin
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AreaPublicaController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @PostMapping("/entrar-carteira-legal")
    public ResponseEntity<?> entrarCarteiraLegal(@RequestBody Usuario usuario) {
        try {
            Usuario u = usuarioService.entrarCarteiraLegal(usuario);
            return ResponseEntity.ok(u);
        } catch (EmailJaCadastradoException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (NumCpfJaCadastradoException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/buscar-informacao-chave-ativacao")
    public ResponseEntity<?> buscarInformacoesPorChaveAtivacao (@RequestBody String chaveAtivacao) {
        try {
            Usuario u = usuarioService.buscarUsuarioChaveAtivacao(chaveAtivacao);
            return ResponseEntity.ok(u);
        } catch (DadoInexistenteException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/salvar-dados-usuario")
    public ResponseEntity<?> salvarInformacoesUsuario(@RequestBody Usuario usuario){
        try {
            Usuario u = usuarioService.finilizarCadastroUsuario(usuario);
            return ResponseEntity.ok(u);
        } catch (DadoDuplicadoException | DadoInexistenteException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login (@RequestBody Usuario usuario) {
        try {
            Usuario user = new Usuario();

            final UserDetails userDetails = usuarioService.login(usuario);
            user = usuarioService.buscarDadosUsuario(usuario.getUsuario());
            user.setSenha("");
            user.setToken(jwtTokenUtil.generateToken(userDetails));

            return ResponseEntity.ok(user);
        }catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(e.getMessage());
        }
    }
}
