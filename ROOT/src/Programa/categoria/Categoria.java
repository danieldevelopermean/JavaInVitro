/*
 * Código-fonte do livro "Programação Java para a Web"
 * Autores: Décio Heinzelmann Luckow <decioluckow@gmail.com>
 *          Alexandre Altair de Melo <alexandremelo.br@gmail.com>
 *
 * ISBN: 978-85-7522-238-6
 * http://www.javaparaweb.com.br
 * http://www.novatec.com.br/livros/javaparaweb
 * Editora Novatec, 2010 - todos os direitos reservados
 *
 * LICENÇA: Este arquivo-fonte está sujeito a Atribuição 2.5 Brasil, da licença Creative Commons,
 * que encontra-se disponível no seguinte endereço URI: http://creativecommons.org/licenses/by/2.5/br/
 * Se você não recebeu uma cópia desta licença, e não conseguiu obtê-la pela internet, por favor,
 * envie uma notificação aos seus autores para que eles possam enviá-la para você imediatamente.
 *
 *
 * Source-code of "Programação Java para a Web" book
 * Authors: Décio Heinzelmann Luckow <decioluckow@gmail.com>
 *          Alexandre Altair de Melo <alexandremelo.br@gmail.com>
 *
 * ISBN: 978-85-7522-238-6
 * http://www.javaparaweb.com.br
 * http://www.novatec.com.br/livros/javaparaweb
 * Editora Novatec, 2010 - all rights reserved
 *
 * LICENSE: This source file is subject to Attribution version 2.5 Brazil of the Creative Commons
 * license that is available through the following URI:  http://creativecommons.org/licenses/by/2.5/br/
 * If you did not receive a copy of this license and are unable to obtain it through the web, please
 * send a note to the authors so they can mail you a copy immediately.
 *
 */
package Programa.categoria;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import Programa.usuario.Usuario;

@Entity
public class Categoria implements Serializable {

	
   private static final long serialVersionUID = 2012480144613107499L;

   /*para um pai , existem muitos filhos
	 * @ManyToOne = muitos filhos na outra tabela ou coluna , se relacionando a um pai
	 * @OneToMany = 1 pai para muitos filhos se relacionando entre tabelas ou colunas
	 */
   
    @Id
	@GeneratedValue
	private Integer	      codigo;

    //--------------------------------------------------------------------
	//------------------aqui é feita a arvore recursiva no banco------------------------------ 
    @ManyToOne //muitos para um , @JoinColumn aponta uma coluna q servira de chave primaria na uniao com outra coluna ou tabela
	@JoinColumn(name = "categoria_pai", nullable = true) // name = nome da coluna na tabela , permite nulo 
	@org.hibernate.annotations.ForeignKey(name = "fk_categoria_categoria")// nomeando na tabela a foreign key
	private Categoria pai; // campo q se auto referencia a tabela

	// 1 para muitos , cascade = CascadeType.REMOVE remove os filhos caso o pai seja excluido
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE) // fetch = FetchType.EAGER , faz carga imediata dos filhos no momento da consulta
	@JoinColumn(name = "categoria_pai", updatable = false) // carrega todos q tenham o nome igual da categoria atual, update false significa q pode salvar uma categoria mudando apenas seu nome sem afetar os filhos
	@org.hibernate.annotations.OrderBy(clause = "descricao asc") // tipo de ordenação (como sql) , vai ordenar na exibição pelo nome da categoria
	private List<Categoria>	filhos; // lista os filhos vinculados a categoria_pai no banco
	//--------------------------------------------------------------------
	
	@ManyToOne // existe uma fk para muitos usuarios
	@OnDelete(action=OnDeleteAction.CASCADE)
	@JoinColumn(name = "usuario")
	@org.hibernate.annotations.ForeignKey(name = "fk_categoria_usuario") // nomeando na tabela a foreign key
	private Usuario	      usuario;

	private String	         descricao; // nome da nova categoria

	private int	            fator; // define a qual pai o filho esta relacionado
	
			
	public Categoria() {
	} // (construtor vazio) obrigatorio para usar um construtor personalizado (abaixo)

	public Categoria(Categoria pai, Usuario usuario, String descricao, int fator) {
		this.pai = pai;
		this.usuario = usuario;
		this.descricao = descricao;
		this.fator = fator;
	}

	public Integer getCodigo() {
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Categoria getPai() {
		return pai;
	}

	public void setPai(Categoria pai) {
		this.pai = pai;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public List<Categoria> getFilhos() {
		return filhos;
	}

	public void setFilhos(List<Categoria> filhos) {
		this.filhos = filhos;
	}

	public int getFator() {
		return fator;
	}

	public void setFator(int fator) {
		this.fator = fator;
	}
	
	// hashCode e equals existem somente para a variavel codigo, pois ele é unico item q diferencia uma linha da outra
	//isso é importante pq o codigo será utilizado no CategoriaConverter.java
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
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
		Categoria other = (Categoria) obj;
		if (codigo == null) {
			if (other.codigo != null)
				return false;
		} else if (!codigo.equals(other.codigo))
			return false;
		return true;
	}

	
	
	
}
