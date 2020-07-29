package br.com.jsa.carteiralegal.exception.pessoa;

public class NumCpfJaCadastradoException extends Throwable {
    private static final long serialVersionUID = 5358878489937174113L;

    public NumCpfJaCadastradoException(){
        super("Essa número de CPF já está cadastrado no sistema.");
    }
}
