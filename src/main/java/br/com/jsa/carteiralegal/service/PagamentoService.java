package br.com.jsa.carteiralegal.service;

import br.com.jsa.carteiralegal.exception.SessaoInexistenteException;
import br.com.jsa.carteiralegal.model.Pagamento;
import br.com.jsa.carteiralegal.model.Pessoa;
import br.com.jsa.carteiralegal.repository.PagamentoRepository;
import br.com.jsa.carteiralegal.repository.PessoaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class PagamentoService {

    @Autowired
    private PagamentoRepository pagamentoRepository;

    @Autowired
    private PessoaRepository pessoaRepository;

    @Autowired
    private UtilServices utilServices;

    public List<Pagamento> listaPagamentos() throws SessaoInexistenteException {
        LocalDate dataCorrent = LocalDate.now();


        LocalDate dataInicio = LocalDate.of(dataCorrent.getYear(), dataCorrent.getMonthValue(), LocalDate.MIN.getDayOfMonth());
        LocalDate dataFim = LocalDate.of(dataCorrent.getYear(), dataCorrent.getMonthValue(), LocalDate.MAX.getDayOfMonth());
        return pagamentoRepository.listaPagamentosUsuario(utilServices.recuperarPessoaPorToken().getId(), dataInicio, dataFim);
    }

    public Pagamento detalharPagamento(Long id){
        return pagamentoRepository.findById(id).get();
    }

    public void salvarPagamento(Pagamento pagamento) throws SessaoInexistenteException {
        Pessoa pessoa = utilServices.recuperarPessoaPorToken();
        pagamento.setAtivo(true);
        pagamento.setPessoa(pessoa);
        pagamentoRepository.save(pagamento);
    }

    private Long recuperarUsuarioId() throws SessaoInexistenteException {
        return utilServices.recuperarIdUsuarioToken();
    }
}

