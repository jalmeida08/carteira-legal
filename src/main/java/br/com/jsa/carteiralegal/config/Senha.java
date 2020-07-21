package br.com.jsa.carteiralegal.config;

import org.springframework.security.crypto.bcrypt.BCrypt;

public class Senha {
	
	public static void main(String[] args) {
		System.out.println(BCrypt.hashpw("123123", BCrypt.gensalt()));
	}

}
