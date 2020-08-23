package br.com.jsa.carteiralegal.controller;

import br.com.jsa.carteiralegal.exception.DadoInexistenteException;
import br.com.jsa.carteiralegal.exception.SessaoInexistenteException;
import br.com.jsa.carteiralegal.model.Despesa;
import br.com.jsa.carteiralegal.service.DespesaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("despesa")
@CrossOrigin
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class DespesaController {

    @Autowired
    private DespesaService despesaService;

    @PostMapping("/adicionar")
    public ResponseEntity<?> salvar(@RequestBody Despesa despesa){
        try {
            despesaService.salvar(despesa);
            return ResponseEntity.ok().build();
        } catch (SessaoInexistenteException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/lista-despesa")
    public ResponseEntity<?> listaDespesa(){
        try {
            List<Despesa> listaDespesa = despesaService.listarDespesaMesAtual();
            return ResponseEntity.ok(listaDespesa);
        } catch (SessaoInexistenteException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("{/id}")
    public ResponseEntity<?> getDespesa(@PathVariable("id") Long id){
        try{
            Despesa d = despesaService.getDespesa(id);
            return ResponseEntity.ok(d);
        }catch (DadoInexistenteException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> remover(@PathVariable("id") Long id){
        try{
            despesaService.remover(id);
            return ResponseEntity.ok().build();
        }catch (DadoInexistenteException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("atualizar")
    public ResponseEntity<?> atualizarDespesa(@RequestBody Despesa despesa){
        try{
            despesaService.atualizarDespesa(despesa);
            return ResponseEntity.ok().build();
        }catch (DadoInexistenteException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}/{dataPagamento}")
    public ResponseEntity<?> fecharDespesa(@PathVariable("id") Long id, @PathVariable("dataPagamento") Date dataPagamento){
        try {
            despesaService.pagarDespesa(id, dataPagamento);
            return ResponseEntity.ok().build();
        } catch (DadoInexistenteException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
