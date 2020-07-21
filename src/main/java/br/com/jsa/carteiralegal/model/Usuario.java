package br.com.jsa.carteiralegal.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity(name = "usuario")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property="id")
public class Usuario implements Serializable {

    private static final long serialVersionUID = 5556490826941567882L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    private Pessoa pessoa;
    @Column(length = 20)
    private String usuario;
    @Column(length = 200)
    private String email;
    @Column(length = 200)
    private String senha;
    @Transient
    private String token;
    @ManyToMany
    @JoinTable(name = "acesso_usuario")
    @JoinColumn(name="acesso_id")
    private List<Acesso> acesso = new ArrayList<Acesso>();
    @Column(name = "ultimo_login")
    private Date ultimoLogin;
    @Column(name = "chave_ativacao", length = 200)
    private String chaveAtivacao;
    private boolean ativo;
    @Version
    private Long versao;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public List<Acesso> getAcesso() {
        return acesso;
    }

    public void setAcesso(List<Acesso> acesso) {
        this.acesso = acesso;
    }

    public Date getUltimoLogin() {
        return ultimoLogin;
    }

    public void setUltimoLogin(Date ultimoLogin) {
        this.ultimoLogin = ultimoLogin;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getChaveAtivacao() {
        return chaveAtivacao;
    }

    public void setChaveAtivacao(String chaveAtivacao) {
        this.chaveAtivacao = chaveAtivacao;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public Long getVersao() {
        return versao;
    }

}
