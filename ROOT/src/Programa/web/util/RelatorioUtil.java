/*
 * gera relatorios em tempo de execu��o
 */
package Programa.web.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

import javax.faces.context.FacesContext;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRHtmlExporter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.oasis.JROdtExporter;
import net.sf.jasperreports.engine.util.JRLoader;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import Programa.util.UtilException;

public class RelatorioUtil {

	public static final int	RELATORIO_PDF = 1;
	public static final int	RELATORIO_EXCEL= 2;
	public static final int	RELATORIO_HTML= 3;
	public static final int	RELATORIO_PLANILHA_OPEN_OFFICE= 4;

	//HashMap recebe parametros a serem passados para o relatorios  
	public StreamedContent geraRelatorio(HashMap parametrosRelatorio, String nomeRelatorioJasper, String nomeRelatorioSaida, int tipoRelatorio) throws UtilException {
		StreamedContent arquivoRetorno = null;

		try {
			FacesContext context = FacesContext.getCurrentInstance();
			Connection conexao = this.getConexao(); // recupera conex�o com o banco (metodo abaixo)
			String caminhoRelatorio = context.getExternalContext().getRealPath("relatorios"); // caminho onde esta a pasta relatorios (webContent)
			String caminhoArquivoJasper = caminhoRelatorio + File.separator + nomeRelatorioJasper + ".jasper"; // jun��o do caminho da pasta relatorio mais o resto q corresponde ao arquivo
			String caminhoArquivoRelatorio = null;
			JasperReport relatorioJasper = (JasperReport) JRLoader.loadObject(caminhoArquivoJasper);// carrega arquivo compilado do relatorio em memoria			
			
			/* JasperPrint (cria arquivo com extens�o .jrprint q sera exportado para um formato especifico adiante),
			JasperFillManager(preenche as informa��es q ser�o utilizadas no relatorio) */
			JasperPrint impressoraJasper = JasperFillManager.fillReport(relatorioJasper, parametrosRelatorio, conexao); 
			
			JRExporter tipoArquivoExportado = null; // class generica de exporta��o , o formato sera definido adiante
			String extensaoArquivoExportado = "";
			File arquivoGerado = null;

			// defini��o do tipo de saida do relatorio
			switch (tipoRelatorio) {
				case RelatorioUtil.RELATORIO_PDF :
					tipoArquivoExportado = new JRPdfExporter();
					extensaoArquivoExportado = "pdf";
					break;
				case RelatorioUtil.RELATORIO_HTML :
					tipoArquivoExportado = new JRHtmlExporter();
					extensaoArquivoExportado = "html";
					break;
				case RelatorioUtil.RELATORIO_EXCEL :
					tipoArquivoExportado = new JRXlsExporter();
					extensaoArquivoExportado = "xls";
					break;
				case RelatorioUtil.RELATORIO_PLANILHA_OPEN_OFFICE :
					tipoArquivoExportado = new JROdtExporter();
					extensaoArquivoExportado = "ods";
					break;
				default :
					tipoArquivoExportado = new JRPdfExporter();
					extensaoArquivoExportado = "pdf";
					break;
			}
			caminhoArquivoRelatorio = caminhoRelatorio + File.separator + nomeRelatorioSaida + "." + extensaoArquivoExportado;
			arquivoGerado = new java.io.File(caminhoArquivoRelatorio); // gera o arquivo final
			tipoArquivoExportado.setParameter(JRExporterParameter.JASPER_PRINT, impressoraJasper); // qual objeto jasper_print sera usado na exporta��o , dados q ir�o conter no relatorio
			tipoArquivoExportado.setParameter(JRExporterParameter.OUTPUT_FILE, arquivoGerado); // qual o nome do arquivo fisico para qual os dados ser�o exportados
			tipoArquivoExportado.exportReport(); // a��o de imprimir na tela o pdf 
			arquivoGerado.deleteOnExit(); // exclui o arquivo apos a execu��o

			InputStream conteudoRelatorio = new FileInputStream(arquivoGerado); // inf arquivo de relatorio q foi gerado
			// DefaultStreamedContent , da suporte para jsf (<p:fileDownload>) q fornece arquivos para download
			arquivoRetorno = new DefaultStreamedContent(conteudoRelatorio, "application/" + extensaoArquivoExportado, nomeRelatorioSaida + "." + extensaoArquivoExportado);
			
		} catch (JRException e) {
			throw new UtilException("N�o foi poss�vel gerar o relat�rio.", e);
		} catch (FileNotFoundException e) {
			throw new UtilException("Arquivo do relat�rio n�o encontrado.", e);
		}
		return arquivoRetorno;
	}

	// obtem a conex�o com o banco 
	private Connection getConexao() throws UtilException {
		java.sql.Connection conexao = null;
		try {
			Context initContext = new InitialContext();
			Context envContext = (Context) initContext.lookup("java:/comp/env/");
			javax.sql.DataSource ds = (javax.sql.DataSource) envContext.lookup("jdbc/JavaDB"); // context.xml , web.xml
			conexao = (java.sql.Connection) ds.getConnection();
		} catch (NamingException e) {
			throw new UtilException("N�o foi poss�vel encontrar o nome da conex�o do banco.", e);
		} catch (SQLException e) {
			throw new UtilException("Ocorreu um erro de SQL.", e);
		}
		return conexao;
	}
}
