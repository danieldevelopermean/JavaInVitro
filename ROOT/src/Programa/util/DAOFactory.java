package Programa.util;


import Programa.categoria.CategoriaDAO;
import Programa.categoria.CategoriaDAOHibernate;
import Programa.lancamento.LancamentoDAO;
import Programa.lancamento.LancamentoDAOHibernate;
import Programa.usuario.UsuarioDao;
import Programa.usuario.UsuarioDaoHibernate;

public class DAOFactory {
	
	// cria um metodo de retorno UsuarioDao
	public static UsuarioDao criarUsuarioDao()
	{
		UsuarioDaoHibernate usuarioDao = new UsuarioDaoHibernate(); // objeto usuarioDaoHibernate (onde existem os metodos de interação com o Banco de Dados)
		usuarioDao.setSession(HibernateUtil.getSessionFactory().getCurrentSession()); // cria a conexão com o banco 
		return usuarioDao; // retorna o objeto conectado ao banco com as ações de interação disponiveis 
	}

	public static CategoriaDAO criarCategoriaDAO()
	{
		CategoriaDAOHibernate categoriaDao = new CategoriaDAOHibernate(); 
		categoriaDao.setSession(HibernateUtil.getSessionFactory().getCurrentSession()); // cria a conexão com o banco 
		return categoriaDao; // retorna o objeto conectado ao banco com as ações de interação disponiveis 
	}
	
	public static LancamentoDAO criarLancamentoDAO()
	{
		LancamentoDAOHibernate lancamentoDAO = new LancamentoDAOHibernate(); 
		lancamentoDAO.setSession(HibernateUtil.getSessionFactory().getCurrentSession()); // cria a conexão com o banco 
		return lancamentoDAO; // retorna o objeto conectado ao banco com as ações de interação disponiveis 
	}
	
}
