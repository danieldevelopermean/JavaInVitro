package Programa.web;

import java.io.Serializable;

import java.util.Date;

import java.util.HashMap;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;


import org.primefaces.model.StreamedContent;

import Programa.categoria.Categoria;
import Programa.lancamento.Lancamento;
import Programa.lancamento.LancamentoRN;
import Programa.util.RNException;
import Programa.util.UtilException;
import Programa.web.util.ContextoUtil;
import Programa.web.util.RelatorioUtil;


@ManagedBean(name = "lancamentoBean")
@ViewScoped // enquanto o usuario estiver na tela a instancia esta ativa no banco (somente enquanto a tela estiver aberta)
public class LancamentoBean implements Serializable {

	private static final long serialVersionUID = -7567699670680933556L;
	
	private List<Lancamento>	lista;
	private Lancamento			editado	= new Lancamento();
	
	
	
	//-----------relatorio
	private StreamedContent	arquivoRetorno;
	private int tipoRelatorio;

	
	public LancamentoBean() {
		this.novo();
	}

	// metodo zera o objeto editado e atribui a data atual
	public void novo() {
		this.editado = new Lancamento();
		this.editado.setData(new Date(System.currentTimeMillis()));
		
	}

	public void editar() {
		
	}

	
	public void salvar() {
		ContextoBean contextoBean = ContextoUtil.getContextoBean();
		this.editado.setUsuario(contextoBean.getUsuarioLogado());
		
		LancamentoRN lancamentoRN = new LancamentoRN();
		lancamentoRN.salvar(this.editado); // salvar o lancamento atual para a conta ativa (selecionada no comboBox)
		this.novo(); // inicia um novo objeto editado
		this.lista = null; // zera a lista parq q o getLista seja chamado automaticamente
	}
	
	public void update() {
		ContextoBean contextoBean = ContextoUtil.getContextoBean();
		this.editado.setUsuario(contextoBean.getUsuarioLogado());
		
		LancamentoRN lancamentoRN = new LancamentoRN();
		lancamentoRN.update(this.editado);
		this.novo();
		this.lista = null;
	}
	

	public void excluir() {
		LancamentoRN lancamentoRN = new LancamentoRN();
		this.editado = lancamentoRN.carregar(this.editado.getLancamento()); // carrega o lancamento q sera excluido (desta sessão)
		lancamentoRN.excluir(this.editado);
		this.lista = null;
	}

	public List<Lancamento> getLista() {
		if (this.lista == null) {
			ContextoBean contextoBean = ContextoUtil.getContextoBean(); // retorna usuario logado

			LancamentoRN lancamentoRN = new LancamentoRN();
			this.lista = lancamentoRN.listar(contextoBean.getUsuarioLogado()); // usuario logado é atribuyido aou metodo listar do hibernate
		}
		return this.lista;
	}
	
		   
	
	   // -------------------relatorio
	
	
	
		
		public StreamedContent getArquivoRetorno() {
		FacesContext context = FacesContext.getCurrentInstance();
		ContextoBean contextoBean = ContextoUtil.getContextoBean();
		String usuario = contextoBean.getUsuarioLogado().getLogin();
		String nomeRelatorioJasper = "relatorioUsuario"; // nome fisico do relatorio q sera exportado (feito no ireport)
		String nomeRelatorioSaida = usuario + "_relatorioUsuario"; // nome do relatorio de saida é concatenado com o nome do usuario q fez o download
		RelatorioUtil relatorioUtil = new RelatorioUtil();
		HashMap parametrosRelatorio = new HashMap(); // monta hashMap com parametros do relatorio
		parametrosRelatorio.put("codigoUsuario", contextoBean.getUsuarioLogado().getCodigo());// parametro codigoUsuario é o mesmo criado dentro do relatorio(ireport)
		
		try {
			this.arquivoRetorno = relatorioUtil.geraRelatorio(parametrosRelatorio, nomeRelatorioJasper, nomeRelatorioSaida, this.tipoRelatorio);
		} catch (UtilException e) {
			context.addMessage(null, new FacesMessage(e.getMessage()));
			return null;
		} 
		return this.arquivoRetorno;
	}
	   
	   //-------fim: relatorio

	

	




	public Lancamento getEditado() {
		return editado;
	}

	public int getTipoRelatorio() {
		return tipoRelatorio;
	}

	public void setTipoRelatorio(int tipoRelatorio) {
		this.tipoRelatorio = tipoRelatorio;
	}

	public void setEditado(Lancamento editado) {
		this.editado = editado;
	}

	public void setLista(List<Lancamento> lista) {
		this.lista = lista;
	}

	

	

	public void setArquivoRetorno(StreamedContent arquivoRetorno) {
		this.arquivoRetorno = arquivoRetorno;
	}
	
	
   
}