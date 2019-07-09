package br.gov.al.maceio.sishosp.hosp.control;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import br.gov.al.maceio.sishosp.acl.model.FuncionarioBean;
import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;
import br.gov.al.maceio.sishosp.comum.util.JSFUtil;
import br.gov.al.maceio.sishosp.hosp.dao.RelatorioDAO;
import br.gov.al.maceio.sishosp.hosp.model.GerenciarPacienteBean;
import br.gov.al.maceio.sishosp.hosp.model.GrupoBean;
import br.gov.al.maceio.sishosp.hosp.model.PacienteBean;
import br.gov.al.maceio.sishosp.hosp.model.ProcedimentoBean;
import br.gov.al.maceio.sishosp.hosp.model.ProgramaBean;
import br.gov.al.maceio.sishosp.hosp.model.TipoAtendimentoBean;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;

@ManagedBean(name = "RelatoriosController")
@ViewScoped
public class RelatoriosController implements Serializable {

	private static final long serialVersionUID = 1L;
	private ProgramaBean programa;
	private PacienteBean paciente;
	private GrupoBean grupo;
	private GerenciarPacienteBean pacienteInstituicao;
	private TipoAtendimentoBean tipo;
	private ProcedimentoBean procedimento;
	private FuncionarioBean prof;
	private List<GrupoBean> listaGrupos;
	private List<TipoAtendimentoBean> listaTipos;
	private String atributoGenerico1;
	private String atributoGenerico2;
	
	private Date dataInicial;
	private Date dataFinal;

	private String recurso;
	private String tipoExameAuditivo;
	private String situacao;
	private String tipoAnalSint;
	private String dataDia;

	private Date dataEspec;
	private Integer diaSemana;
	private Integer mes;
	private Integer ano;
	
	FuncionarioBean user_session = (FuncionarioBean) FacesContext
            .getCurrentInstance().getExternalContext().getSessionMap()
            .get("obj_usuario");

	public RelatoriosController() {
		this.programa = new ProgramaBean();
		this.grupo = new GrupoBean();
		this.tipo = new TipoAtendimentoBean();
		this.prof = new FuncionarioBean();
		this.listaGrupos = new ArrayList<GrupoBean>();
		this.listaTipos = new ArrayList<TipoAtendimentoBean>();
		procedimento = new ProcedimentoBean();
		this.dataInicial = null;
		this.dataFinal = null;
		this.tipoExameAuditivo = new String("TODOS");
		this.tipoAnalSint = new String("A");
		this.dataDia = new String("DS");
		this.dataEspec = null;
		pacienteInstituicao = new GerenciarPacienteBean();

	}

	public void limparDados() {
		this.listaGrupos = new ArrayList<GrupoBean>();
		this.listaTipos = new ArrayList<TipoAtendimentoBean>();
		this.programa = new ProgramaBean();
		this.grupo = new GrupoBean();
		this.tipo = new TipoAtendimentoBean();
		this.prof = new FuncionarioBean();
		this.dataInicial = null;
		this.dataFinal = null;
		this.tipoExameAuditivo = new String("TODOS");
		this.tipoAnalSint = new String("A");
		this.dataDia = new String("DS");
		this.ano = null;
		this.mes = null;
		this.dataEspec = null;
	}

	public boolean verificarMesesIguais(Date dataInicial, Date dataFinal) {
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		c1.setTime(dataInicial);
		c2.setTime(dataFinal);
		int mes1 = c1.get(Calendar.MONTH);
		int mes2 = c2.get(Calendar.MONTH);
		if (mes1 == mes2)
			return true;
		else
			return false;
	}

	public void preparaRelLaudosVencer() {
		atributoGenerico1="N";
	}
	
	public void preparaRelFrequencia() {
		atributoGenerico1="P";
	}
	
	public void geraLaudoVencer(ProgramaBean programa, GrupoBean grupo) throws IOException,
			ParseException, ProjetoException {
		
		if (atributoGenerico1.equals(null)) {
			 JSFUtil.adicionarMensagemErro("Informe o período a vencer!", "Erro!");
			}
		else
		{
		String caminho = "/WEB-INF/relatorios/";
		String relatorio = "";
		if (atributoGenerico1.equals("N"))
				relatorio = caminho + "laudosvencernominal.jasper";
		else
				relatorio = caminho + "laudovencer.jasper";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("periodolaudovencer", this.atributoGenerico2);
		map.put("codusuario", user_session.getEmpresa().getCodEmpresa());
			if (programa!=null)
			map.put("codprograma", programa.getIdPrograma());
			
			if (grupo!=null)
			map.put("codgrupo", grupo.getIdGrupo());
			map.put("cod_laudo", null);
			this.executeReport(relatorio, map, "relatorio.pdf");
		//	this.executeReportNewTab(relatorio, "laudovencer.pdf",
			//		map);
			
		
		}
	}
	
	public void geraLaudo(Integer idLaudo) throws IOException,
	ParseException, ProjetoException {

		String caminho = "/WEB-INF/relatorios/";
		String relatorio = "";
		relatorio = caminho + "laudo.jasper";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("codempresa", user_session.getEmpresa().getCodEmpresa());
		map.put("cod_laudo", idLaudo);
		this.executeReport(relatorio, map, "laudo.pdf");

	}
	
	public void geraFrequencia(GerenciarPacienteBean pacienteInstituicao, ProgramaBean programa, GrupoBean grupo) throws IOException,
	ParseException, ProjetoException, NoSuchAlgorithmException {
		//lista criada para ser populada e mostrar as linhas na frequencia
		pacienteInstituicao.setPrograma(programa);
		pacienteInstituicao.setGrupo(grupo);
		List<Integer> lista = new ArrayList<Integer>();
		lista.add(1);
		lista.add(2);
		int randomico = JSFUtil.geraNumeroRandomico();
		RelatorioDAO rDao = new RelatorioDAO();
		rDao.popularTabelaTemporariaFrequencia(randomico, pacienteInstituicao.getGrupo().getQtdFrequencia());
		
		JRBeanCollectionDataSource itemsJRBean = new JRBeanCollectionDataSource(lista);
	if ((pacienteInstituicao.getPrograma()==null) && (pacienteInstituicao.getPaciente()==null)) {
		 JSFUtil.adicionarMensagemErro("Informe o Programa ou Paciente obrigatoriamente!", "Erro!");
		}
	else
	{
	String caminho = "/WEB-INF/relatorios/";
	String relatorio = "";
	relatorio = caminho + "frequencia.jasper";
	Map<String, Object> map = new HashMap<String, Object>();
		map.put("chave", randomico);
		map.put("codusuario", user_session.getCodigo());
		if (pacienteInstituicao.getPrograma()!=null)
		map.put("codprograma", pacienteInstituicao.getPrograma().getIdPrograma());
		
		if (pacienteInstituicao.getGrupo()!=null)
		map.put("codgrupo", pacienteInstituicao.getGrupo().getIdGrupo());
		
		if (pacienteInstituicao.getId()!=null)
		map.put("codpacienteinstituicao",pacienteInstituicao.getId());
		
		map.put("SUBREPORT_DIR", this.getServleContext().getRealPath(caminho)
				+ File.separator);
		//this.executeReport(relatorio, map, "relatorio.pdf");
		this.executeReportNewTab(relatorio, "frequencia.pdf",
				map);
		rDao.limparTabelaTemporariaFrequencia(randomico);
		
	
	}
}

	
	public void gerarMapaLaudoOrteseProtese() throws IOException,
	ParseException, ProjetoException {

if (this.dataFinal == null || this.dataInicial == null
		|| this.programa == null || this.grupo == null) {
	FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
			"Todos os campos devem ser preenchidos.",
			"Campos invÃ¡lidos!");
	FacesContext.getCurrentInstance().addMessage(null, msg);
	return;
}

if (!verificarMesesIguais(this.dataInicial, this.dataFinal)) {
	FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
			"As datas devem possuir o mesmo mÃªs.", "Datas InvÃ¡lidas!");
	FacesContext.getCurrentInstance().addMessage(null, msg);
	return;
}

String caminho = "/WEB-INF/relatorios/";
String relatorio = "";
Map<String, Object> map = new HashMap<String, Object>();
if (this.grupo.isAuditivo()) {
	relatorio = caminho + "mapaLaudoOrteseProteseAuditivo.jasper";
	map.put("dt_inicial", this.dataInicial);
	map.put("dt_final", this.dataFinal);
	map.put("cod_programa", this.programa.getIdPrograma());
	map.put("cod_grupo", this.grupo.getIdGrupo());
	map.put("recurso", this.recurso);
	map.put("tipo_exame_auditivo", this.tipoExameAuditivo);
} else {
	relatorio = caminho + "mapaLaudoOrteseProteseNormal.jasper";
	map.put("dt_inicial", this.dataInicial);
	map.put("dt_final", this.dataFinal);
	map.put("cod_programa", this.programa.getIdPrograma());
	map.put("cod_grupo", this.grupo.getIdGrupo());
	map.put("recurso", this.recurso);
}

map.put("SUBREPORT_DIR", this.getServleContext().getRealPath(caminho)
		+ File.separator);
this.executeReport(relatorio, map, "relatorio.pdf");
// limparDados();
}

	public void gerarFinanceiroOrteseProtese() throws IOException,
			ParseException, ProjetoException {

		if (this.dataFinal == null || this.dataInicial == null
				|| this.programa == null || this.grupo == null) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Todos os campos devem ser preenchidos.",
					"Campos invÃ¡lidos!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return;
		}

		if (!verificarMesesIguais(this.dataInicial, this.dataFinal)) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"As datas devem possuir o mesmo mÃªs.", "Datas InvÃ¡lidas!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return;
		}

		String caminho = "/WEB-INF/relatorios/";
		String relatorio = "";
		Map<String, Object> map = new HashMap<String, Object>();
		if (this.grupo.isAuditivo()) {
			relatorio = caminho + "mapaFinanceiroOrteseProtese.jasper";
			map.put("img_adefal",
					this.getServleContext().getRealPath(
							"/WEB-INF/relatorios/adefal.png"));
			map.put("dt_inicial", this.dataInicial);
			map.put("dt_final", this.dataFinal);
			map.put("cod_programa", this.programa.getIdPrograma());
			map.put("cod_grupo", this.grupo.getIdGrupo());
			map.put("recurso", this.recurso);
			map.put("tipo_exame_auditivo", this.tipoExameAuditivo);

		} else {
			relatorio = caminho + "mapaFinanceiroOrteseProtese.jasper";
			map.put("img_adefal",
					this.getServleContext().getRealPath(
							"/WEB-INF/relatorios/adefal.png"));
			map.put("dt_inicial", this.dataInicial);
			map.put("dt_final", this.dataFinal);
			map.put("cod_programa", this.programa.getIdPrograma());
			map.put("cod_grupo", this.grupo.getIdGrupo());
			map.put("recurso", this.recurso);
		}

		map.put("SUBREPORT_DIR", this.getServleContext().getRealPath(caminho)
				+ File.separator);
		this.executeReport(relatorio, map, "relatorio.pdf");
		// limparDados();
	}

	public void gerarAgendamentosPorProfissional() throws IOException,
			ParseException, ProjetoException {

		if (this.dataFinal == null || this.dataInicial == null
				|| this.programa == null || this.grupo == null
				|| this.tipo == null) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Todos os campos devem ser preenchidos.",
					"Campos invÃ¡lidos!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return;
		}

		String caminho = "/WEB-INF/relatorios/";
		String relatorio = caminho + "agendamentosProfissional.jasper";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("img_adefal",
				this.getServleContext().getRealPath(
						"/WEB-INF/relatorios/adefal.png"));
		map.put("dt_inicial", this.dataInicial);
		map.put("dt_final", this.dataFinal);
		map.put("cod_tipo_atend", this.tipo.getIdTipo());
		if (this.prof == null) {
			map.put("cod_medico", null);
		} else {
			map.put("cod_medico", this.prof.getId());
		}
		map.put("SUBREPORT_DIR", this.getServleContext().getRealPath(caminho)
				+ File.separator);

		this.executeReport(relatorio, map, "relatorio.pdf");
		// limparDados();
	}

	public void gerarAtendimentosPorProfissional() throws IOException,
			ParseException, ProjetoException {

		if (this.dataFinal == null || this.dataInicial == null
				|| this.programa == null) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Datas e programa devem ser preenchidos.",
					"Campos invÃ¡lidos!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return;
		}

		String caminho = "/WEB-INF/relatorios/";
		String relatorio = caminho + "atendPorProfissional.jasper";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("img_adefal",
				this.getServleContext().getRealPath(
						"/WEB-INF/relatorios/adefal.png"));
		map.put("dt_inicial", this.dataInicial);
		map.put("dt_final", this.dataFinal);
		map.put("cod_programa", this.programa.getIdPrograma());
		if (this.prof == null) {
			map.put("cod_medico", null);
		} else {
			map.put("cod_medico", this.prof.getId());
		}
		map.put("SUBREPORT_DIR", this.getServleContext().getRealPath(caminho)
				+ File.separator);

		this.executeReport(relatorio, map, "relatorio.pdf");
		// limparDados();
	}

	public void gerarAtendimentosPorPrograma() throws IOException,
			ParseException, ProjetoException {

		if (this.dataFinal == null || this.dataInicial == null
				|| this.programa == null) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Datas e programa devem ser preenchidos.",
					"Campos invÃ¡lidos!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return;
		}

		String caminho = "/WEB-INF/relatorios/";
		String relatorio = caminho + "atendimentosPorPrograma.jasper";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("dt_inicial", this.dataInicial);
		map.put("dt_final", this.dataFinal);
		map.put("cod_programa", this.programa.getIdPrograma());
		if (this.grupo == null) {
			map.put("cod_grupo", null);
		} else {
			map.put("cod_grupo", this.grupo.getIdGrupo());
		}
		if (this.situacao.equals("T")) {
			map.put("situacao", null);
		} else {
			map.put("situacao", this.situacao);
		}
		map.put("SUBREPORT_DIR", this.getServleContext().getRealPath(caminho)
				+ File.separator);

		this.executeReport(relatorio, map, "relatorio.pdf");
		// limparDados();
	}

	public void gerarAtendimentosPorProcedimento() throws IOException,
			ParseException, ProjetoException {

		if (this.dataFinal == null || this.dataInicial == null) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Datas e programa devem ser preenchidos.",
					"Campos invÃ¡lidos!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return;
		}

		String caminho = "/WEB-INF/relatorios/";
		String relatorio = "";
		if (this.tipoAnalSint.equals("A")) {
			relatorio = caminho + "atendPorProcedimentosAnalitico.jasper";
		} else if (this.tipoAnalSint.equals("S")) {
			relatorio = caminho + "atendPorProcedimentosSintetico.jasper";
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("dt_inicial", this.dataInicial);
		map.put("dt_final", this.dataFinal);
		map.put("SUBREPORT_DIR", this.getServleContext().getRealPath(caminho)
				+ File.separator);

		this.executeReport(relatorio, map, "relatorio.pdf");
		// limparDados();
	}

	public void gerarAtendimentosPorProcedimentoGrupo() throws IOException,
			ParseException, ProjetoException {
		if (this.dataInicial == null || this.dataFinal == null
				|| this.programa == null) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Datas e programa devem ser preenchidos.",
					"Campos invÃ¡lidos!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		} else {

			String caminho = "/WEB-INF/relatorios/";
			String relatorio = "";
			if (this.tipoAnalSint.equals("A")) {
				relatorio = caminho
						+ "atendPorProcedimentosGrupoAnalitico.jasper";
			} else if (this.tipoAnalSint.equals("S")) {
				relatorio = caminho
						+ "atendPorProcedimentosGrupoSintetico.jasper";
			}
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("dt_inicial", this.dataInicial);
			map.put("dt_final", this.dataFinal);
			map.put("cod_programa", this.programa.getIdPrograma());

			if (this.listaGrupos.isEmpty()) {
				map.put("cod_grupo", null);
			} else {
				map.put("cod_grupo", strIdGrupos());
			}

			map.put("SUBREPORT_DIR",
					this.getServleContext().getRealPath(caminho)
							+ File.separator);

			this.executeReport(relatorio, map, "relatorio.pdf");
			// limparDados();
		}
	}

	public void gerarAtendimentosPorProcedimentoTipoAt() throws IOException,
			ParseException, ProjetoException {
		if (this.dataInicial == null || this.dataFinal == null
				|| this.programa == null || this.grupo == null) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Datas, programa e grupo devem ser preenchidos.",
					"Campos invÃ¡lidos!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		} else {

			String caminho = "/WEB-INF/relatorios/";
			String relatorio = "";
			if (this.tipoAnalSint.equals("A")) {
				relatorio = caminho
						+ "atendPorProcedimentosTipoAtAnalitico.jasper";
			} else if (this.tipoAnalSint.equals("S")) {
				relatorio = caminho
						+ "atendPorProcedimentosTipoAtSintetico.jasper";
			}
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("dt_inicial", this.dataInicial);
			map.put("dt_final", this.dataFinal);
			map.put("cod_programa", this.programa.getIdPrograma());
			map.put("cod_grupo", this.grupo.getIdGrupo());

			if (this.listaTipos.isEmpty()) {
				map.put("cod_tipoatendimento", null);
			} else {
				map.put("cod_tipoatendimento", strIdTipos());
			}

			map.put("SUBREPORT_DIR",
					this.getServleContext().getRealPath(caminho)
							+ File.separator);

			this.executeReport(relatorio, map, "relatorio.pdf");
			// limparDados();
		}
	}

	public void gerarConfigAgendaProfissional() throws IOException,
			ParseException, ProjetoException {

		if (this.grupo == null || this.programa == null) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Programa e Grupo devem ser preenchidos.",
					"Campos invÃ¡lidos!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return;
		}

		Map<String, Object> map = new HashMap<String, Object>();
		String caminho = "/WEB-INF/relatorios/";
		String relatorio = "";
		if (this.dataDia.equals("DS")) {
			if (this.mes == null || this.ano == null) {
				FacesMessage msg = new FacesMessage(
						FacesMessage.SEVERITY_ERROR,
						"MÃªs e Ano devem ser preenchidos.", "Campos invï¿½lidos!");
				FacesContext.getCurrentInstance().addMessage(null, msg);
				return;
			}
			relatorio = caminho + "configAgenda-DS.jasper";
			if (this.diaSemana == 0)
				map.put("dia_semana", null);
			else
				map.put("dia_semana", this.diaSemana);
			map.put("mes", this.mes);
			map.put("ano", this.ano);
		} else {
			if (this.dataEspec == null) {
				FacesMessage msg = new FacesMessage(
						FacesMessage.SEVERITY_ERROR,
						"Data especÃ­fica deve ser preenchida.",
						"Campo invÃ¡lido!");
				FacesContext.getCurrentInstance().addMessage(null, msg);
				return;
			}
			relatorio = caminho + "configAgenda-DE.jasper";
			map.put("dt_espec", this.dataEspec);
		}
		map.put("cod_programa", this.programa.getIdPrograma());
		map.put("cod_grupo", this.grupo.getIdGrupo());

		if (this.prof == null)
			map.put("cod_medico", null);
		else
			map.put("cod_medico", this.prof.getId());

		map.put("SUBREPORT_DIR", this.getServleContext().getRealPath(caminho)
				+ File.separator);
		this.executeReport(relatorio, map, "relatorio.pdf");
		limparDados();
	}

	
	public void executeReportNewTab(String caminhoRelatorio,
			String nomeRelatorio, Map parametros) throws ProjetoException {

		Connection conexao = null;
		ServletOutputStream outputStream = null;
		FacesContext context = FacesContext.getCurrentInstance();

		HttpServletResponse response = (HttpServletResponse) context
				.getExternalContext().getResponse();
		InputStream relatorioStream = context.getExternalContext()
				.getResourceAsStream(caminhoRelatorio);

		try {
			conexao = ConnectionFactory.getConnection();
			JasperPrint print = JasperFillManager.fillReport(relatorioStream,
					parametros, conexao);

			JRExporter exportador = new JRPdfExporter();
			exportador.setParameter(JRExporterParameter.OUTPUT_STREAM,
					response.getOutputStream());
			exportador.setParameter(JRExporterParameter.JASPER_PRINT, print);

			response.setContentType("application/pdf");
			response.setHeader("Content-disposition",
					"inline; filename=arquivo.pdf");
			ServletOutputStream servletOutputStream = response
					.getOutputStream();

			exportador.exportReport();
			servletOutputStream.flush();
			servletOutputStream.close();
			context.renderResponse();
			context.responseComplete();
		} catch (JRException ex) {
			throw new RuntimeException(ex);
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		} finally {
			try {
				conexao.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
	
	private void executeReport(String relatorio, Map<String, Object> map,
			String filename) throws IOException, ParseException,
			ProjetoException {
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletResponse response = (HttpServletResponse) context
				.getExternalContext().getResponse();
		InputStream reportStream = context.getExternalContext()
				.getResourceAsStream(relatorio);

		response.setContentType("application/pdf");
		response.setHeader("Content-disposition", "attachment;filename="
				+ filename);

		ServletOutputStream servletOutputStream = response.getOutputStream();

		try {
			Connection connection = ConnectionFactory.getConnection();

			JasperRunManager.runReportToPdfStream(reportStream,
					response.getOutputStream(), map, connection);

			if (connection != null)
				connection.close();
		} catch (JRException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		this.getFacesContext().responseComplete();
		servletOutputStream.flush();
		servletOutputStream.close();
	}

	public void gerarFaltasPorPrograma() throws IOException, ParseException,
			ProjetoException {

		if (this.dataFinal == null || this.dataInicial == null
				|| this.programa == null) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Datas e programa devem ser preenchidas.",
					"Campos invÃ¡lidos!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return;
		}

		String caminho = "/WEB-INF/relatorios/";
		String relatorio = caminho + "faltasNoPrograma.jasper";

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("dt_inicial", this.dataInicial);
		map.put("dt_final", this.dataFinal);
		map.put("cod_programa", this.programa.getIdPrograma());
		map.put("SUBREPORT_DIR", this.getServleContext().getRealPath(caminho)
				+ File.separator);

		this.executeReport(relatorio, map, "relatorio.pdf");
		limparDados();
	}

	public void addGrupoLst() {
		this.listaGrupos.add(this.grupo);
	}

	public void rmvGrupoLst() {
		this.listaGrupos.remove(this.grupo);
	}

	public void addTipoLst() {
		this.listaTipos.add(this.tipo);
	}

	public void rmvTipoLst() {
		this.listaTipos.remove(this.tipo);
	}

	public String strIdGrupos() {
		int si = this.listaGrupos.size();
		int i = 1;
		String str = new String();
		for (GrupoBean grupo : listaGrupos) {

			if (i < si) {
				str += String.valueOf(grupo.getIdGrupo()) + ", ";
			} else {
				str += String.valueOf(grupo.getIdGrupo());
			}
			i++;
		}
		return str;
	}

	public String strIdTipos() {
		int si = this.listaTipos.size();
		int i = 1;
		String str = new String();
		for (TipoAtendimentoBean tipos : listaTipos) {

			if (i < si) {
				str += String.valueOf(tipos.getIdTipo()) + ", ";
			} else {
				str += String.valueOf(tipos.getIdTipo());
			}
			i++;
		}
		return str;
	}

	private FacesContext getFacesContext() {
		FacesContext context = FacesContext.getCurrentInstance();
		return context;
	}

	private ServletContext getServleContext() {
		ServletContext scontext = (ServletContext) this.getFacesContext()
				.getExternalContext().getContext();
		return scontext;
	}

	public ProgramaBean getPrograma() {
		return programa;
	}

	public void setPrograma(ProgramaBean programa) {
		this.programa = programa;
	}

	public GrupoBean getGrupo() {
		return grupo;
	}

	public void setGrupo(GrupoBean grupo) {
		this.grupo = grupo;
	}

	public Date getDataInicial() {
		return dataInicial;
	}

	public void setDataInicial(Date dataInicial) {
		this.dataInicial = dataInicial;
	}

	public Date getDataFinal() {
		return dataFinal;
	}

	public void setDataFinal(Date dataFinal) {
		this.dataFinal = dataFinal;
	}

	public String getRecurso() {
		return recurso;
	}

	public void setRecurso(String recurso) {
		this.recurso = recurso;
	}

	public String getTipoExameAuditivo() {
		return tipoExameAuditivo;
	}

	public void setTipoExameAuditivo(String tipoExameAuditivo) {
		this.tipoExameAuditivo = tipoExameAuditivo;
	}

	public TipoAtendimentoBean getTipo() {
		return tipo;
	}

	public void setTipo(TipoAtendimentoBean tipo) {
		this.tipo = tipo;
	}

	public FuncionarioBean getProf() {
		return prof;
	}

	public void setProf(FuncionarioBean prof) {
		this.prof = prof;
	}

	public String getSituacao() {
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	public String getTipoAnalSint() {
		return tipoAnalSint;
	}

	public void setTipoAnalSint(String tipoAnalSint) {
		this.tipoAnalSint = tipoAnalSint;
	}

	public String getDataDia() {
		return dataDia;
	}

	public void setDataDia(String dataDia) {
		this.dataDia = dataDia;
	}

	public Date getDataEspec() {
		return dataEspec;
	}

	public Integer getDiaSemana() {
		return diaSemana;
	}

	public Integer getMes() {
		return mes;
	}

	public Integer getAno() {
		return ano;
	}

	public void setDataEspec(Date dataEspec) {
		this.dataEspec = dataEspec;
	}

	public void setDiaSemana(Integer diaSemana) {
		this.diaSemana = diaSemana;
	}

	public void setMes(Integer mes) {
		this.mes = mes;
	}

	public void setAno(Integer ano) {
		this.ano = ano;
	}

	public List<GrupoBean> getListaGrupos() {
		return listaGrupos;
	}

	public void setListaGrupos(List<GrupoBean> listaGrupos) {
		this.listaGrupos = listaGrupos;
	}

	public List<TipoAtendimentoBean> getListaTipos() {
		return listaTipos;
	}

	public void setListaTipos(List<TipoAtendimentoBean> listaTipos) {
		this.listaTipos = listaTipos;
	}

	public String getAtributoGenerico1() {
		return atributoGenerico1;
	}

	public void setAtributoGenerico1(String atributoGenerico1) {
		this.atributoGenerico1 = atributoGenerico1;
	}

	public String getAtributoGenerico2() {
		return atributoGenerico2;
	}

	public void setAtributoGenerico2(String atributoGenerico2) {
		this.atributoGenerico2 = atributoGenerico2;
	}

	public ProcedimentoBean getProcedimento() {
		return procedimento;
	}

	public void setProcedimento(ProcedimentoBean procedimento) {
		this.procedimento = procedimento;
	}

	public PacienteBean getPaciente() {
		return paciente;
	}

	public GerenciarPacienteBean getPacienteInstituicao() {
		return pacienteInstituicao;
	}

	public void setPaciente(PacienteBean paciente) {
		this.paciente = paciente;
	}

	public void setPacienteInstituicao(GerenciarPacienteBean pacienteInstituicao) {
		this.pacienteInstituicao = pacienteInstituicao;
	}

}
