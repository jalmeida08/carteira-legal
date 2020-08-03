package br.com.jsa.carteiralegal.controller;

import br.com.jsa.carteiralegal.exception.SessaoInexistenteException;
import br.com.jsa.carteiralegal.model.Pagamento;
import br.com.jsa.carteiralegal.service.PagamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@RestController
@RequestMapping("pagamento")
@CrossOrigin
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PagamentoController {

    @Autowired
    private PagamentoService pagamentoService;

    @GetMapping("/listar-pagamentos")
    public ResponseEntity<?> listaPagamento(){
        try {
            List<Pagamento> p = pagamentoService.listaPagamentos();
            return ResponseEntity.ok(p);
        } catch (SessaoInexistenteException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @PostMapping("/adicionar")
    public ResponseEntity<?> salvarPagamento(@RequestBody Pagamento pagamento){
        try{
            pagamentoService.salvarPagamento(pagamento);
            return ResponseEntity.ok().build();
        } catch (SessaoInexistenteException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
