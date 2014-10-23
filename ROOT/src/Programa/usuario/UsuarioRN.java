package Programa.usuario;

import java.util.List;
import java.util.Locale;

import Programa.categoria.CategoriaRN;
import Programa.util.DAOFactory;
import Programa.util.RNException;
import Programa.util.UtilException;
import Programa.web.util.EmailUtil;
import Programa.web.util.MensagemUtil;

public class UsuarioRN {

	private UsuarioDao usuarioDAO;
	
	public UsuarioRN()
	{
		this.usuarioDAO = DAOFactory.criarUsuarioDao();
	} 

	public Usuario carregar(Integer codigo)
	{
		return this.usuarioDAO.carregar(codigo);
	}

	public Usuario buscaPorLogin(String login)
	{
		return this.usuarioDAO.buscarPorLogin(login);		
	}
	
	
	public void salvar(Usuario usuario)
	{
		Integer codigo = usuario.getCodigo();
		if(codigo == null || codigo == 0)
		{
			usuario.getPermissao().add("ROLE_USUARIO");
			this.usuarioDAO.salvar(usuario);
			
			// inicio:Categoria
			// ira salvar um modelo de categoria para um novo usuario (somente para novos usuarios / modelo)
			CategoriaRN categoriaRN = new CategoriaRN();
			categoriaRN.salvaEstruturaPadrao(usuario);
			// Fim:Categoria
			
		} 
		else
		{
			this.usuarioDAO.atualizar(usuario);
		}
		
	}
	
	public void excluir(Usuario usuario)
	{
		// inicio:Categoria
		// ira excluir todas as categoria de um usuario cadastrado (automaticamente)
		CategoriaRN categoriaRN = new CategoriaRN();
		categoriaRN.excluir(usuario);
		// Fim:Categoria
					
		
		
		this.usuarioDAO.excluir(usuario);
	}
	
	public List<Usuario> listar()
	{
		return this.usuarioDAO.listar();
	}
	
	// envia mensagem conforme idioma de cadastro do usuario
	public void enviarEmailPosCadastramento(Usuario usuario) throws RNException {
		//Enviando um e-mail conforme o idioma do usuário
		String[] info = usuario.getIdioma().split("_");
		Locale locale = new Locale(info[0], info[1]); // recupera idioma com base no objeto
			
		String titulo = MensagemUtil.getMensagem(locale, "email_titulo"); // email_titulo e´encontardo de forma sub entendida no mensagens (pasta idiomas)
		String mensagem = MensagemUtil.getMensagem(locale, "email_mensagem", usuario.getNome(), usuario.getLogin(), usuario.getSenha());
		try {		
			EmailUtil emailUtil = new EmailUtil();
			emailUtil.enviarEmail(null, usuario.getEmail(), titulo, mensagem); // recupera mensagem especifica para o idioma
		} catch (UtilException e) {
			throw new RNException(e);
		}
	}
	
	
}
