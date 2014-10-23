package Programa.web.util;

import java.util.Date;
import java.util.Properties;

//import javax.mail.Session;


import javax.mail.Session;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;

import Programa.util.UtilException;

public class EmailUtil {

	public void enviarEmail(String de, String para, String assunto, String mensagem) throws UtilException {
		try {
			// ---------------------inicio: recupera session para envio de email, base configurações feitas em context.xml
			Context initialContext = new InitialContext();
			Context envContext = (Context) initialContext.lookup("java:comp/env");
			Session session = (Session) envContext.lookup("mail/Session"); // busca no web.xml q busca no context.xml cria uma sessão de ação
			// ---------------------fim: recupera session para envio de email
			
			SimpleEmail email = new SimpleEmail(); // cria o formato de envio de email
			email.setMailSession(session); // atribui a instancia de simpleEmail 
			if (de != null) {
				email.setFrom(de); // verifica se ja não existe um email pre definido usado para enviar a mensagen
			} else { // caso não tenha
				// recupera os variaveis responsaveis pelo envio do email
			Properties p = session.getProperties(); // obtem as propriedades q foram configuradas no resource do Email Session do web.xml
			email.setFrom(p.getProperty("mail.smtp.user")); // valor definido em context.xml (usuario.email)
			}
			email.addTo(para);
			email.setSubject(assunto);
			email.setMsg(mensagem);
			email.setSentDate(new Date());
			email.send(); // envia o email
		} catch (EmailException e) {
			throw new UtilException(e);
		} catch (NamingException e) {
			throw new UtilException(e);
		}
	}
}