package Programa.web;

import java.util.List;
import java.util.Set;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;


import Programa.usuario.Usuario;
import Programa.usuario.UsuarioRN;
import Programa.util.RNException;

@ManagedBean(name="usuarioBean")
@RequestScoped
public class UsuarioBean {

	private Usuario usuario = new Usuario();
	private String confirmarSenha;
	private List<Usuario> lista;
	private String destinoSalvar;
	String senhaCriptografada; 
	
	
	
	
	public String novo()
	{
		this.destinoSalvar = "UsuarioSucesso";
		this.usuario = new Usuario();
		this.usuario.setAtivo(true);
		return "usuario";
	}
	
	// os metodos hashCode e equals da class Usuario.java garantem o salvamento automï¿½tico apoï¿½s qualquer alteraï¿½ï¿½o do objeto usuario a ser modificado 
	public String atribuiPermissao(Usuario usuario, String permissao) {

		this.usuario = usuario;

		Set<String> permissoes = this.usuario.getPermissao();
		
		//verifica se usuario ja contem a permissao adicionada no jsf (contains ï¿½ metodo do Set<String>)
		if (permissoes.contains(permissao)) {
			permissoes.remove(permissao);
		} else {
			permissoes.add(permissao);
		}
		return null; // retorna para a mesma pagina chamadora
	}
	
	
	public String salvar() {
		FacesContext context = FacesContext.getCurrentInstance();

		String senha = this.usuario.getSenha();
		if(!senha.equals(this.confirmarSenha))
		 {
			 FacesMessage facesMessage = new FacesMessage("Senha errada");
			 context.addMessage(null, facesMessage);
			 return null; // reexibe a mesma pagina q ocorreu o erro
		 }
		
		
		UsuarioRN usuarioRN = new UsuarioRN();
		usuarioRN.salvar(this.usuario);
		
		//Envia email após o cadastramento de um usuário novo
		
		if (this.destinoSalvar.equals("UsuarioSucesso")) {
			try {
				usuarioRN.enviarEmailPosCadastramento(this.usuario);
			} catch (RNException e) {
				FacesMessage facesMessage = new FacesMessage("Não foi possível enviar o e-mail de cadastro do usuário. Erro: " + e.getMessage());
				context.addMessage(null, facesMessage);
				return null;
			}
		} 
		

return this.destinoSalvar;
		
	}

	
	
	public String editar() {
	    this.senhaCriptografada = this.usuario.getSenha();
	    return "/publico/usuario";
	}

	public String excluir()
	{
		UsuarioRN usuarioRN = new UsuarioRN();
                usuarioRN.excluir(this.usuario);
		this.lista = null; // reexibe a lista sem o usuario excluido
		return null; // exibe novamente a pagina chamadora
	}
	
	public String ativar()
	{
		if(this.usuario.getAtivo() )
			this.usuario.setAtivo(false);
		else
			this.usuario.setAtivo(true);
		
		UsuarioRN usuarioRN = new UsuarioRN();
		usuarioRN.salvar(this.usuario);
		return null;
	}
	
	// jsf ao chamar "usario.lista" entende que deve chamar getList
	public List<Usuario> getLista()
	{
		// economizar memoria ao chamar somente se a lista estiver vazia
		if(this.lista == null){
		UsuarioRN usuarioRN = new UsuarioRN();
		this.lista = usuarioRN.listar();
		}
		
		return this.lista;
                
	}
	
		
	public String getSenhaCriptografada() {
		return senhaCriptografada;
	}

	public void setSenhaCriptografada(String senhaCriptografada) {
		this.senhaCriptografada = senhaCriptografada;
	}

	public String getDestinoSalvar() {
		return destinoSalvar;
	}

	public void setDestinoSalvar(String destinoSalvar) {
		this.destinoSalvar = destinoSalvar;
	}

	public Usuario getUsuario() {
		return usuario;
	}
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	public String getConfirmarSenha() {
		return confirmarSenha;
	}
	public void setConfirmarSenha(String confirmarSenha) {
		this.confirmarSenha = confirmarSenha;
	}
	
	
	
}
