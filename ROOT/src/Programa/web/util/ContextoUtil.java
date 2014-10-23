/*
 obtem o usuário logado no sistema 
 */
package Programa.web.util;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import Programa.web.ContextoBean;

public class ContextoUtil {

	// obtem o usuário logado no sistema, apos ele ja ter sido reconhecido pelo contextoBean.java
	public static ContextoBean getContextoBean() {
		FacesContext context = FacesContext.getCurrentInstance(); // afetado pelo context.xml
		ExternalContext external = context.getExternalContext();
		HttpSession session = (HttpSession) external.getSession(true);// inf q a sessão esta ativa // conexao com o banco

		ContextoBean contextoBean = (ContextoBean) session.getAttribute("contextoBean"); // recebe os dados do usuario logado
		
		return contextoBean;
	}
}
