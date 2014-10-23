/*
 * C�digo-fonte do livro "Programa��o Java para a Web"
 * Autores: D�cio Heinzelmann Luckow <decioluckow@gmail.com>
 *          Alexandre Altair de Melo <alexandremelo.br@gmail.com>
 *
 * ISBN: 978-85-7522-238-6
 * http://www.javaparaweb.com.br
 * http://www.novatec.com.br/livros/javaparaweb
 * Editora Novatec, 2010 - todos os direitos reservados
 *
 * LICEN�A: Este arquivo-fonte est� sujeito a Atribui��o 2.5 Brasil, da licen�a Creative Commons,
 * que encontra-se dispon�vel no seguinte endere�o URI: http://creativecommons.org/licenses/by/2.5/br/
 * Se voc� n�o recebeu uma c�pia desta licen�a, e n�o conseguiu obt�-la pela internet, por favor,
 * envie uma notifica��o aos seus autores para que eles possam envi�-la para voc� imediatamente.
 *
 *
 * Source-code of "Programa��o Java para a Web" book
 * Authors: D�cio Heinzelmann Luckow <decioluckow@gmail.com>
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

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import Programa.usuario.Usuario;

public class CategoriaDAOHibernate implements CategoriaDAO {

	private Session	session;

	public void setSession(Session session) {
		this.session = session;
	}

	@Override
	public Categoria salvar(Categoria categoria) { // merge , mescla os dados do contexto atual , com o contexto persistente(no banco)
		Categoria merged = (Categoria) this.session.merge(categoria); // merge , funciona como saveorupdate (util quando a tabela se auto referencia)
		this.session.flush(); // for�a a sincroniza��o do objeto em memoria
		this.session.clear(); // remove da memoria do hibernate todos os objetos carregados, para exibir o formulario atualizado
		return merged;
	}

	@Override
	public void excluir(Categoria categoria) {
		categoria = (Categoria) this.carregar(categoria.getCodigo()); // como a tabela se auto referencia � ne,cessario carrega-�a por completo (Pai e filhos no banco ) para q ooorra a exclus�o
		this.session.delete(categoria);// deleta 
		this.session.flush(); // sincroniza
		this.session.clear(); // limpa o hibernate
	}

	// retorna a categoria para o hibernate q retorna para o jsf (salva automaticamente)
	@Override
	public Categoria carregar(Integer categoria) {
		return (Categoria) this.session.get(Categoria.class, categoria);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Categoria> listar(Usuario usuario) {
		//                          carrrega    enquanto Pai = 0 e usario.categoria = usuario.usuario                                     
		String hql = "select c from Categoria c where c.pai is null and c.usuario = :usuario";//carrega somente os niveis superiores de Categoria (somente as "tabelas" Pai ser�o carregadas )
		Query query = this.session.createQuery(hql);
		query.setInteger("usuario", usuario.getCodigo());

		List<Categoria> lista = query.list();

		return lista;
	}
}
