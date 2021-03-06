package br.com.jsa.carteiralegal.service;

import br.com.jsa.carteiralegal.exception.DadoInexistenteException;
import br.com.jsa.carteiralegal.exception.SessaoInexistenteException;
import br.com.jsa.carteiralegal.model.Despesa;
import br.com.jsa.carteiralegal.model.Pessoa;
import br.com.jsa.carteiralegal.repository.DespesaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class DespesaService {

    @Autowired
    private DespesaRepository despesaRepository;

    @Autowired
    private UtilServices utilServices;

    public void salvar(Despesa despesa) throws SessaoInexistenteException {
        Pessoa p = utilServices.recuperarPessoaPorToken();
        List<Pessoa> listPessoa = new ArrayList<Pessoa>();
        listPessoa.add(p);
        despesa.setPessoa(listPessoa);
        despesa.setAtivo(true);
        despesa.setDataReferencia(new Date());
        despesaRepository.save(despesa);
    }

    public List<Despesa> listarDespesaMesAtual() throws SessaoInexistenteException {
        LocalDate data = LocalDate.now();
        LocalDate dataInicio = LocalDate.of(data.getYear(), data.getMonth(), LocalDate.MIN.getDayOfMonth());
        LocalDate dataFim = LocalDate.of(data.getYear(), data.getMonth(), LocalDate.MAX.getDayOfMonth());
        Pessoa p = utilServices.recuperarPessoaPorToken();

        List<Despesa> listaDespesa = despesaRepository.listarDespesaMesAtual(p.getId(), dataInicio, dataFim);

        return listaDespesa;
    }

    public void remover(Long id) throws DadoInexistenteException {
        despesaRepository.delete(getDespesa(id));
    }

    public Despesa getDespesa(Long id) throws DadoInexistenteException {
        Optional<Despesa> d = despesaRepository.findById(id);
        if(!d.isPresent()) throw new DadoInexistenteException("Despesa");
        return d.get();
    }

    public void atualizarDespesa(Despesa despesa) throws DadoInexistenteException {
        Despesa d = getDespesa(despesa.getId());
        despesa.setPessoa(d.getPessoa());
        despesaRepository.save(despesa);
    }

    public void pagarDespesa(Long id, Date dataPagamento) throws DadoInexistenteException {
        Despesa d = getDespesa(id);
        d.setDataPagamento(dataPagamento);
        despesaRepository.save(d);
    }

}
