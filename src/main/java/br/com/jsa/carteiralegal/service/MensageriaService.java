package br.com.jsa.carteiralegal.service;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class MensageriaService {

	@Autowired private JavaMailSender mailSender;
	private final String linkBaseAtivacao = "http://localhost:4200";
	
	private boolean enviarEmail(String destinatario, String tituloEmail, String textoEmail) {
		
		try {
			MimeMessage mail = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mail);
			helper.setTo(destinatario);
			helper.setSubject(tituloEmail);
			helper.setText(textoEmail, true);
			
			mailSender.send(mail);
			return true;
		}catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	public void enviarEmailNovoUsuario(String destinatario, String nome, String chaveAtivacao){
		final String tituloEmail = "Bem Vindo ao Carteira Legal";
		String linkParaAtivarUsuario = linkBaseAtivacao + "/area-comum/confirm/atv";
		linkParaAtivarUsuario += chaveAtivacao;
		String textoEmail = "<html> <head>"
		+ "<style>"
		+ ".botao:hover {background-color: rgba(135,206,250,0.5)}"
		+ ".botao {background-color: #87CEFA; padding: 10px; color: #000; border-radius: 3px; text-decoration: none}"
		+ "</style>"
		+ "</head> <body>"
		+ "<h1>"+ nome +", seja muito bem vindo(a). </h1>"
		+ "<br>"
		+ "<p>Aqui você consegue administrar o seu dinheiro.</p>"
		+ "<br>"
		+ "<p>Clique no botão para ativar o seu usuário: "
		+ "<a class='botao' href='"+linkParaAtivarUsuario+"'>Ativar Usuário</a></p>"
		+ "<br><br><br><br><br><br><br><br>"
		+ "Caso o botão não funcione, clique no link: <a href="+ linkParaAtivarUsuario +">" + linkParaAtivarUsuario + "</a>"
		+ "</body></html>";
		enviarEmail(destinatario, tituloEmail, textoEmail);
	}


}
