
package Programa.lancamento;


import java.util.List;

import org.hibernate.Criteria;

import org.hibernate.Session;

import org.hibernate.criterion.Restrictions;

import Programa.usuario.Usuario;

public class LancamentoDAOHibernate implements LancamentoDAO {

	private Session	session;

	public void setSession(Session session) {
		this.session = session;
	}

	public void salvar(Lancamento lancamento) {
		this.session.save(lancamento);
	}
	
	public void update(Lancamento lancamento) {
		this.session.update(lancamento);
	}
	
		

	public void excluir(Lancamento lancamento) {
		this.session.delete(lancamento);
	}

	public Lancamento carregar(Integer lancamento) {
		return (Lancamento) this.session.get(Lancamento.class, lancamento);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Lancamento> listar(Usuario usuario) {
		Criteria criteria = this.session.createCriteria(Lancamento.class);
		criteria.add(Restrictions.eq("usuario", usuario));

		return criteria.list(); 
	}

	
	

	
}
