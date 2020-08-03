package br.com.jsa.carteiralegal.exception;

public class SessaoInexistenteException extends Exception{

    public SessaoInexistenteException(){
        super("Sua sessão expirou, tente logar novamente");
    }
}
