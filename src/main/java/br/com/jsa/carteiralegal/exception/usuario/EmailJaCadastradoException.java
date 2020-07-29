package br.com.jsa.carteiralegal.exception.usuario;

public class EmailJaCadastradoException extends Exception{

    private static final long serialVersionUID = 6549707790651559313L;

    public EmailJaCadastradoException (){
        super("Esse e-mail já está cadastrado no sistema");
    }

}
