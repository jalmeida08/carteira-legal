package br.com.jsa.carteiralegal.exception;

public class DadoDuplicadoException extends Throwable {

    private static final long serialVersionUID = 476412844180972279L;

    public DadoDuplicadoException(String nomeCapo) {
        super(nomeCapo + " já cadastrado na base de dados");
    }
}
