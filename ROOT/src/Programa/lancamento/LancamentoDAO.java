
package Programa.lancamento;


import java.util.List;

import Programa.usuario.Usuario;



public interface LancamentoDAO {

	public void salvar(Lancamento lancamento);
	
	public void update(Lancamento lancamento);

	public void excluir(Lancamento lancamento);

	public Lancamento carregar(Integer lancamento);

	public List<Lancamento> listar(Usuario usuario);

	
}
