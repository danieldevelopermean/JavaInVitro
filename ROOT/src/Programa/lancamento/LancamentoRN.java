package Programa.lancamento;

import java.util.*;

import Programa.usuario.Usuario;
import Programa.util.DAOFactory;

public class LancamentoRN {

	private LancamentoDAO	lancamentoDAO;

	public LancamentoRN() {
		this.lancamentoDAO = DAOFactory.criarLancamentoDAO();
	}

	public void salvar(Lancamento lancamento) {
		this.lancamentoDAO.salvar(lancamento);
	}

	public void update(Lancamento lancamento) {
		this.lancamentoDAO.update(lancamento);
	}
	
	public void excluir(Lancamento lancamento) {
		this.lancamentoDAO.excluir(lancamento);
	}

	public Lancamento carregar(Integer lancamento) {
		return this.lancamentoDAO.carregar(lancamento);
	}
	
	public List<Lancamento> listar(Usuario usuario) {
		return this.lancamentoDAO.listar(usuario);
	}
	
}
