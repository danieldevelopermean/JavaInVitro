package Programa.web;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.model.SelectItem;

import org.primefaces.event.NodeSelectEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

import Programa.categoria.Categoria;
import Programa.categoria.CategoriaRN;
import Programa.web.util.ContextoUtil;

@ManagedBean(name = "categoriaBean")
@RequestScoped
public class CategoriaBean {

	private TreeNode categoriasTree; // permite q a arvore seja criada seguindo um modelo
	private Categoria editada = new Categoria(); // objeto q alimentara o formulario e recebera a categoria selecionada na arvore
	private List<SelectItem> categoriasSelect; // lista q alimentara a caixa de combina��o da categoria pai
	private boolean	mostraEdicao = false; // define q a exibi��o normal dos nos seja falsa 
	
	
	public void novo() {
		Categoria pai = null; // variavel q ira receber status da categoria , reenicializado
		//  valor de editada � recebido de forma oculta no categoria.jsf
		if (this.editada.getCodigo() != null) {
			CategoriaRN categoriaRN = new CategoriaRN();
			pai = categoriaRN.carregar(this.editada.getCodigo());
		}
		this.editada = new Categoria(); // reenicia a editada
		this.editada.setPai(pai); // atribui a nova categoria ao Pai
		this.mostraEdicao = true; // define q a exibi��o permaneca exibindo
	}

	// evento q interage com primefaces quando uma categoria � selecionada
	public void selecionar(NodeSelectEvent event) { // associa informa��es a Tree
		this.editada = (Categoria) event.getTreeNode().getData(); // associa a data atual ao evento (n�o existe uma a��o visivel a principio)

		Categoria pai = this.editada.getPai();
		// so permite q a categoria seja editada , caso ela tenha pai e q o pai tenha um codigo (pq os pais por padr�o s�o null)
		if (this.editada != null && pai != null && pai.getCodigo() != null) {
			this.mostraEdicao = true;
		} else {
			this.mostraEdicao = false;
		}
	}

	public void salvar() {
		ContextoBean contextoBean = ContextoUtil.getContextoBean();

		CategoriaRN categoriaRN = new CategoriaRN();
		this.editada.setUsuario(contextoBean.getUsuarioLogado());
		categoriaRN.salvar(this.editada);

		this.editada = null;
		this.mostraEdicao = false; // garante q ao salvar o formulario ele retorne ao formato original de exibi��o sem mostrar os nos dos Pais
		this.categoriasTree = null; // zera para buscar novamente no banco
		this.categoriasSelect = null; // zera para buscar novamente no banco
	}

	public void excluir() {
		CategoriaRN categoriaRN = new CategoriaRN();
		categoriaRN.excluir(this.editada);

		this.editada = null;
		this.mostraEdicao = false;
		this.categoriasTree = null;
		this.categoriasSelect = null;
	}

	
	//---------------get e set das variaveis-------------- 
	
	//--------------------Constru��o da arvore para o jsf------------------------------------
	public TreeNode getCategoriasTree() {
		if (this.categoriasTree == null) { // toda vez q existe uma altera��o o metodo q o alterou torna o categoriaTree = null
			ContextoBean contextoBean = ContextoUtil.getContextoBean();

			CategoriaRN categoriaRN = new CategoriaRN();
			List<Categoria> categorias = categoriaRN.listar(contextoBean.getUsuarioLogado()); // lista a estrutura hieraquica das categorias

			this.categoriasTree = new DefaultTreeNode(null, null); // cria o modelo zerado q sera recebido pelo primefaces (dessa o forma o jsf entende q � uma arvore)
			// parametros (objeto Tree, lista de categorias)
			this.montaDadosTree(this.categoriasTree, categorias); // metodo q ira carregar o pai e os nos
		}
		return this.categoriasTree;
	}

	// metodo recursivo q chama a s mesmo toda vez q for necessario descer um nivel de hierarquia
	private void montaDadosTree(TreeNode pai, List<Categoria> lista) {
		if (lista != null && lista.size() > 0) {
			TreeNode filho = null;
			for (Categoria categoria : lista) { // la�o for , roda enquanto o banco fornece
				filho = new DefaultTreeNode(categoria, pai);
				this.montaDadosTree(filho, categoria.getFilhos());
			}
		}
	}
	//--------------------fim: Constru��o da arvore------------------------------------
	
	
	
    //----------------------ira listar as categorias na caixa de sele��o----------------
	public List<SelectItem> getCategoriasSelect() {
		 // por padr�o categoriasSelect =  null e apos salvar tambem trona-se null
		if (this.categoriasSelect == null) {
			this.categoriasSelect = new ArrayList<SelectItem>(); // converte para o tipo array list
			ContextoBean contextoBean = ContextoUtil.getContextoBean(); // busca conex�o com o banco 
			
			CategoriaRN categoriaRN = new CategoriaRN(); // instancia o objeto
			List<Categoria> categorias = categoriaRN.listar(contextoBean.getUsuarioLogado()); // lista as categorias do usuario logado
			this.montaDadosSelect(this.categoriasSelect, categorias, ""); // chamo o metodo q ira mostrar as categorias no jsf
		}
		return categoriasSelect;
	}
    
	// recebe como parametros (array vazio, lista, String prefixo(para todos os itens) )
	private void montaDadosSelect(List<SelectItem> select, List<Categoria> categorias, String prefixo) {

		SelectItem item = null; // zera o item todas as vezes para reiniciar o ciclo
		// confirma q as categorias existem 
		if (categorias != null) {
			for (Categoria categoria : categorias) { // la�o for para preencher 
				item = new SelectItem(categoria, prefixo + categoria.getDescricao()); // (valor do item(nesse caso especifico � um objeto), rotulo a ser exibido (nome da categoria(descricao)))
				// existe um conversor q � chamado (CategoriaConverter.java) funciona automaticamente devido a annotaicion
				item.setEscape(false); // serve para o jsf interpretar o espa�o "&nbsp;&nbsp;
				
				select.add(item); // seleciona o item marcado na caixa de selecao
				// chama o metodo recursivamente ate o fim da lista de objetos
				this.montaDadosSelect(select, categoria.getFilhos(), prefixo + "&nbsp;&nbsp;");// "&nbsp;&nbsp; desloca um espa�o na exibi��o
				
			}
		}
	}
	
	//---------------------------------------------------------------------------

	public boolean isMostraEdicao() {
		return mostraEdicao;
	}

	public Categoria getEditada() {
		return editada;
	}
	
	public void setMostraEdicao(boolean mostraEdicao) {
		this.mostraEdicao = mostraEdicao;
	}

	public void setEditada(Categoria editada) {
		this.editada = editada;
	}
	
	public void setCategoriasTree(TreeNode categoriasTree) {
		this.categoriasTree = categoriasTree;
	}

	public void setCategoriasSelect(List<SelectItem> categoriasSelect) {
		this.categoriasSelect = categoriasSelect;
	}
}
