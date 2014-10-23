package Programa.usuario;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.UniqueConstraint;


/*
 * Essa class trabalha no escopo request = 
 * 	uma nova instÂncia para cada requisição solicitada para o usuário
 * -- toda vez q for feita alguma alteração ou for chamado algum metodo uma nova instância é criada
 */

@Entity
public class Usuario implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -800393671723501776L;
	
	@Id
	@GeneratedValue
	private Integer codigo;
	private String nome;
	private String email;
	
	@org.hibernate.annotations.NaturalId // defini q so pode haver 1 por usuï¿½rio 
	private String login;
	private String senha;
    private Date nascimento;
	private String celular;
	private String idioma;
	private Boolean ativo;
	private String ProgramacaoPredileta;
	
	
	//-------------------------------------------------
	
	@ElementCollection(targetClass = String.class) // declara qual tipo de class sera carregada no conjunto Set de valores
	@JoinTable(
			name="usuario_permissao", // nome da tabela (existe 1 usuário para muitos tipos de permissões)
			uniqueConstraints = {@UniqueConstraint(columnNames = {"usuario","permissao"})},// cria um indice unico na tabela , 1 para 1
			joinColumns = @JoinColumn(name="usuario")) // esse usaario é da tebale usuario_permissao / informa a qual campo a tabela Pai(Usuario) irá se ligar a tabela filho(usuario_permissao)  
	// campo Id(indice) da tabela usuario é vinculado a usuario(indice) da tabela usuario_permissao 
	//fim joinTable
	
	@Column(name = "permissao", length=50) // vincula ao banco referente a tabela acima (usuario_permissao)
	private Set<String> permissao = new HashSet<String>(); // Set Coleção que não permite tipos repetidos

	//-------------------------------------------------
	
	
	
	
	
	public Integer getCodigo() {
		return codigo;
	}


	


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((ProgramacaoPredileta == null) ? 0 : ProgramacaoPredileta
						.hashCode());
		result = prime * result + ((ativo == null) ? 0 : ativo.hashCode());
		result = prime * result + ((celular == null) ? 0 : celular.hashCode());
		result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((idioma == null) ? 0 : idioma.hashCode());
		result = prime * result + ((login == null) ? 0 : login.hashCode());
		result = prime * result
				+ ((nascimento == null) ? 0 : nascimento.hashCode());
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		result = prime * result
				+ ((permissao == null) ? 0 : permissao.hashCode());
		result = prime * result + ((senha == null) ? 0 : senha.hashCode());
		return result;
	}





	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Usuario other = (Usuario) obj;
		if (ProgramacaoPredileta == null) {
			if (other.ProgramacaoPredileta != null)
				return false;
		} else if (!ProgramacaoPredileta.equals(other.ProgramacaoPredileta))
			return false;
		if (ativo == null) {
			if (other.ativo != null)
				return false;
		} else if (!ativo.equals(other.ativo))
			return false;
		if (celular == null) {
			if (other.celular != null)
				return false;
		} else if (!celular.equals(other.celular))
			return false;
		if (codigo == null) {
			if (other.codigo != null)
				return false;
		} else if (!codigo.equals(other.codigo))
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (idioma == null) {
			if (other.idioma != null)
				return false;
		} else if (!idioma.equals(other.idioma))
			return false;
		if (login == null) {
			if (other.login != null)
				return false;
		} else if (!login.equals(other.login))
			return false;
		if (nascimento == null) {
			if (other.nascimento != null)
				return false;
		} else if (!nascimento.equals(other.nascimento))
			return false;
		if (nome == null) {
			if (other.nome != null)
				return false;
		} else if (!nome.equals(other.nome))
			return false;
		if (permissao == null) {
			if (other.permissao != null)
				return false;
		} else if (!permissao.equals(other.permissao))
			return false;
		if (senha == null) {
			if (other.senha != null)
				return false;
		} else if (!senha.equals(other.senha))
			return false;
		return true;
	}





	public String getProgramacaoPredileta() {
		return ProgramacaoPredileta;
	}





	public void setProgramacaoPredileta(String programacaoPredileta) {
		ProgramacaoPredileta = programacaoPredileta;
	}





	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}


	public String getNome() {
		return nome;
	}


	public void setNome(String nome) {
		this.nome = nome;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public String getLogin() {
		return login;
	}


	public void setLogin(String login) {
		this.login = login;
	}


	public String getSenha() {
		return senha;
	}


	public void setSenha(String senha) {
		this.senha = senha;
	}


	public Date getNascimento() {
		return nascimento;
	}


	public void setNascimento(Date nascimento) {
		this.nascimento = nascimento;
	}


	public String getCelular() {
		return celular;
	}


	public void setCelular(String celular) {
		this.celular = celular;
	}


	public String getIdioma() {
		return idioma;
	}


	public void setIdioma(String idioma) {
		this.idioma = idioma;
	}


	public Boolean getAtivo() {
		return ativo;
	}


	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}


	public Set<String> getPermissao() {
		return permissao;
	}


	public void setPermissao(Set<String> permissao) {
		this.permissao = permissao;
	}


	public static long getSerialversionuid() {
		return serialVersionUID;
	}
			
	
	
	
	

}
