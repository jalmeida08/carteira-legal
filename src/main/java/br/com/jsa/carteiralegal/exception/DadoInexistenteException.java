package br.com.jsa.carteiralegal.exception;

public class DadoInexistenteException extends Throwable {
    public DadoInexistenteException(String s) {
        super(s + " não localizado no sistema.");
    }
}
