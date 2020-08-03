package br.com.jsa.carteiralegal.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "pessoa")
@JsonIdentityInfo(scope = Pessoa.class, generator = ObjectIdGenerators.PropertyGenerator.class, property="id")
public class Pessoa implements Serializable {

    private static final long serialVersionUID = -4123264665285698307L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;
    @OneToOne(mappedBy = "pessoa")
    private Usuario usuario;
    private String nome;
    @Column(name = "num_cpf", length = 11)
    private Long numCpf;
    @OneToMany(mappedBy = "pessoa")
    private List<Pagamento> pagamento = new ArrayList<Pagamento>();
    @ManyToMany(mappedBy = "pessoa")
    private List<Despesa> despesa = new ArrayList<Despesa>();
    @Version
    private Long versao;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Long getNumCpf() {
        return numCpf;
    }

    public void setNumCpf(Long numCpf) {
        this.numCpf = numCpf;
    }

    public List<Pagamento> getPagamento() {
        return pagamento;
    }

    public void setPagamento(List<Pagamento> pagamento) {
        this.pagamento = pagamento;
    }

    public List<Despesa> getDespesa() {
        return despesa;
    }

    public void setDespesa(List<Despesa> despesa) {
        this.despesa = despesa;
    }

    public Long getVersao() {
        return versao;
    }

}
