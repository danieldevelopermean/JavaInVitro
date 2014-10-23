package Programa.web;

/*
 * essa class existe enquanto o usuario estiver logado , não a refresh após o inicio da sessão
 * usuáro logou irá permanecer logado
 */


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;

import Programa.usuario.Usuario;
import Programa.usuario.UsuarioRN;

/*Essa classe fornecera informacoes do usuario logado, assim como o objeto completo
da conta bancaria ativa para o sistema nesse momento*/


@ManagedBean(name = "contextoBean") // nomeia a class para ser acessada pelo jsf
@SessionScoped // define q o escopo de existencia da class será durante toda a sessão que o usuário permanecer conectado
public class ContextoBean implements Serializable {
	
	
	private static final long serialVersionUID = -8618308438844041039L;
	
	// cria um objeto usuario e conta , os define como vazios // 
	private Usuario	usuarioLogado = null;
	
	
	private Locale localizacao = null;// variavel vinculada a locale configurada no faces-config.xml
	private List<Locale> idiomas; // lista dos idiomas mapeados 
	
	
	
	public Usuario getUsuarioLogado() {
		// FacesContext = identifica a solicitação de uma sessão (DAO) pelo jsf 	
		FacesContext context = FacesContext.getCurrentInstance(); // identifica a instancia da sessão atual
		ExternalContext external = context.getExternalContext(); // Esta classe permite que o API Faces não ter conhecimento da natureza do seu ambiente de aplicativos que contém. Em particular, esta classe permite appications JavaServer Faces base para correr em qualquer uma Servlet ou um ambiente de Portlet.
		String login = external.getRemoteUser(); // retorna o login do usuario , caso ele tenha sido identificado (spring security) , usuarioRemoto

		/*Exemplo:
		 *  login = mario64bits 
		 * 
		 * 
		 */
		
		// !login = equivale a inversão do estado ser for true , torna-se false
		// só entra no if se o usuarioLogado for = null || se o valor da variavel login acima for diferente do valor da variavel login da class usuario.java
		if (this.usuarioLogado == null || !login.equals(this.usuarioLogado.getLogin())) { //se for a primeira vez ou se o login do usuario remoto for diferente do usuario logado

			// entrou no if pq o login é diferente de null e também é diferente de um usuario logado em outra sessão q evetualmente estava ocorrendo, ativa o if abaixo 
			if (login != null) {
				UsuarioRN usuarioRN = new UsuarioRN();
				this.usuarioLogado = usuarioRN.buscaPorLogin(login); //atribui a instancia do usuario carregado (pelo login do usuario remoto) ao usuario logado
				
			}
		}
		return usuarioLogado;
	}

	public void setUsuarioLogado(Usuario usuario)
	{
		this.usuarioLogado = usuario;
	}
	
	

	

	//---------------------------inicio:Idiomas----------------------
	
	// troca o idioma padrão da aplicação no login do usuario com base no idioma de preferencia do mesmo
	public Locale getLocaleUsuario() {
		if (this.localizacao == null) { // idioma inicial  = null
			Usuario usuario = this.getUsuarioLogado(); 
			String idioma = usuario.getIdioma(); // recupera o idioma escolhido pelo usuario ao se cadastrar
			String[] info = idioma.split("_"); // split divide o a string retornada por getIdioma ex:(pt_BR), torna-se pt BR q atribuido a linha abaixo
			this.localizacao = new Locale(info[0], info[1]); // Locale(pt , BR) retorna o idioma padrão para o sistema q foi definido no faces-config.xml como customizado pelo usuario 
		}
		return this.localizacao; // agora a pagina sera exibida conforme a preferencia do usuario logado
	}

	// metodo q lista e monta as bandeiras listadas em cheque (metodo repeat em cheque.xhtml)
	public List<Locale> getIdiomas() {
		FacesContext context = FacesContext.getCurrentInstance(); // instancia atual (usuarioLogado)
		// interator é como um for percorrendo uma coleção
		Iterator<Locale> locales = context.getApplication().getSupportedLocales(); // retorna os idiomas listados faces-config.xml q customizou as mensagens disponiveis   
		this.idiomas = new ArrayList<Locale>();  // zera a variavel para prencher a lista
		while (locales.hasNext()) { // while lista os idiomas disponiveis (nesse caso 3)
			this.idiomas.add(locales.next());
		}
		return idiomas; // retorna a lista de idomas (3)
	}
	
	// metodo chamado pelo cheque.xhtml para alterar o idioma a ser exibido em tela
	public void setIdiomaUsuario(String idioma) {
		
		// recarrega o usuario antes de salvar 
		// pq o escopo da sessão do usuario logado é diferente da sessão necessaria para carregar um novo idioma,
		// desta forma o usuario é recarregado para q o metodo possa salvar a modificação
		UsuarioRN usuarioRN = new UsuarioRN();
		this.usuarioLogado = usuarioRN.carregar(this.getUsuarioLogado().getCodigo());
		this.usuarioLogado.setIdioma(idioma);
		usuarioRN.salvar(this.usuarioLogado); // salva o usuario com idioma alterado para exibição das modificações
		// a cada requisição do usuario uma nova sessão é aberta, como esta logado desde o principio o hibernate não o considera 
		// dentro da sessão atual, por isso é necessário recarregar o usuario para q ele seja afetado pela requisição desse momento 
		
		String[] info = idioma.split("_"); // divide o idioma para ser reconhecido pela lina abaixo 
		Locale locale = new Locale(info[0], info[1]); // informado idioma e pais (Locale("pt","BR"))

		// metodo q define para o sistema da pagina o idioma a ser exibido 
		FacesContext context = FacesContext.getCurrentInstance(); // 
		context.getViewRoot().setLocale(locale); // define o idioma a ser exibido como padrão 
		// comunicação feita com o faces-config.xml q customizou os idiomas
	}	
	
	//---------------------------fim:Idiomas----------------------
	
	
	
	
	
}
