/*
 * class q retorna as mensagens traduzidas
 * menssagens de erro  
 * metodo é utilizado pela class ChequeBean.java
 * 
 */
package Programa.web.util;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.faces.context.FacesContext;

public class MensagemUtil {

	private static final String PACOTE_MENSAGENS_IDIOMAS = "Programa.idioma.mensagens";

	// metodo retorna a mensagem conforme a chave q é passada de parametro para o metodo
	public static String getMensagem(String propriedade) {
		FacesContext context = FacesContext.getCurrentInstance(); // contexto da instancia atual
		ResourceBundle bundle = context.getApplication().getResourceBundle(context, "msg"); // atraves do metodo as mensganes traduzidas são repassadas para o contexto atual 
		return bundle.getString(propriedade); // retorna as strings de idiomas construidas  
	}
	
	// metodo identico ao anterior , so q pode repassar parametros para mensagem (apesar de apenas 2 parametros listados o metodo aceita mais de 2 parametros)
	public static String getMensagem(String propriedade, Object...parametros) {
		String mensagem = getMensagem(propriedade);
		MessageFormat formatter = new MessageFormat(mensagem);
		return formatter.format(parametros);
	}
	
	public static String getMensagem(Locale locale, String propriedade) {
		ResourceBundle bundle = ResourceBundle.getBundle(MensagemUtil.PACOTE_MENSAGENS_IDIOMAS, locale);
		return bundle.getString(propriedade);
	}

	public static String getMensagem(Locale locale, String propriedade, Object... parametros) {
		String mensagem = getMensagem(locale, propriedade);
		MessageFormat formatter = new MessageFormat(mensagem);
		return formatter.format(parametros);
	}
}
