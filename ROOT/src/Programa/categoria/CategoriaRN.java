package Programa.categoria;


import java.util.List;

import Programa.usuario.Usuario;
import Programa.util.DAOFactory;

public class CategoriaRN {

	private CategoriaDAO	categoriaDAO;

	public CategoriaRN() {
		this.categoriaDAO = DAOFactory.criarCategoriaDAO();
	}

	public List<Categoria> listar(Usuario usuario) {
		return this.categoriaDAO.listar(usuario);
	}

	public Categoria salvar(Categoria categoria) {
		Categoria pai = categoria.getPai();
        
		// não permite salvar , caso a categoria não possua um Pai (pq as categorias sem pais já estão definidas(categoria = null são os Pais) (base de ação))
		if (pai == null) {
			String msg = "A Categoria " + categoria.getDescricao() + " deve ter um pai definido";
			throw new IllegalArgumentException(msg);
		}

		boolean mudouFator = pai.getFator() != categoria.getFator(); // verifica se a pergunta é false ou true

		categoria.setFator(pai.getFator()); // atribui o fator do pai (1 ou -1)a categoria
		categoria = this.categoriaDAO.salvar(categoria); // salva

		// caso mudou fator for true (filho mudou de pai)
		if (mudouFator == true) {
			categoria = this.carregar(categoria.getCodigo()); // carrega a categoria atual do banco
			this.replicarFator(categoria, categoria.getFator()); // chama o metodo abaixo
		}

		return categoria;
	}

	private void replicarFator(Categoria categoria, int fator) {
		
		// caso a categoria possua filhos, caso não não entra 
		if (categoria.getFilhos() != null) {
			for (Categoria filho : categoria.getFilhos()) { // lista os filhos
				filho.setFator(fator); // atribui o fator do pai
				this.categoriaDAO.salvar(filho);
				this.replicarFator(filho, fator); // recusividade
			}
		}
	}

	public void excluir(Categoria categoria) {

		//OrcamentoRN orcamentoRN = new OrcamentoRN();
		//orcamentoRN.excluir(categoria);

		this.categoriaDAO.excluir(categoria);
	}
	
	// metodo utilizado em UsuarioRN.java , excluindo um usuario se lista as categorias pertencentes ao mesmo e as exclui também
	public void excluir(Usuario usuario) {
		List<Categoria> lista = this.listar(usuario);
		for (Categoria categoria:lista) {
			this.categoriaDAO.excluir(categoria); // ira excluir uma a uma cada categoria pertencente a este usuario
		}
	}

	public Categoria carregar(Integer categoria) {
		return this.categoriaDAO.carregar(categoria);
	}

	// metodo é chamado no UsuarioRN.java para salvar altomaticamente para um novo usuario a regra de categorias abaixo 
	public void salvaEstruturaPadrao(Usuario usuario) {
		
		// utiliza o metodo construtor de Categoria.java 
		Categoria program = new Categoria(null, usuario, "LINGUAGENS&FRAMEWORKS", -1); // Pai
		program = this.categoriaDAO.salvar(program);
		this.categoriaDAO.salvar(new Categoria(program, usuario,"Java", -1)); // filhos
		this.categoriaDAO.salvar(new Categoria(program, usuario,"SQL", -1)); // filhos

		Categoria base = new Categoria(null, usuario, "BASE", 1); // Pai
		base = this.categoriaDAO.salvar(base);
		this.categoriaDAO.salvar(new Categoria(base, usuario,"JavaComoProgramar", 1)); // filhos
		this.categoriaDAO.salvar(new Categoria(base, usuario,"NeriNeitzke", 1)); // filhos
	}
}