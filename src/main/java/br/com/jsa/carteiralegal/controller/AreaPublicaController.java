package br.com.jsa.carteiralegal.controller;

import br.com.jsa.carteiralegal.exception.pessoa.NumCpfJaCadastradoException;
import br.com.jsa.carteiralegal.exception.usuario.EmailJaCadastradoException;
import br.com.jsa.carteiralegal.model.Usuario;
import br.com.jsa.carteiralegal.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    @PostMapping("/login")
    public ResponseEntity<?> login (@RequestBody Usuario usuario) {
        return ResponseEntity.ok(usuarioService.login(usuario));
    }
}
