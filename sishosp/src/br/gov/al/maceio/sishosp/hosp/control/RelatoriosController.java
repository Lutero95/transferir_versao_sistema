package br.gov.al.maceio.sishosp.hosp.control;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperRunManager;
import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;
import br.gov.al.maceio.sishosp.hosp.model.GrupoBean;
import br.gov.al.maceio.sishosp.hosp.model.ProfissionalBean;
import br.gov.al.maceio.sishosp.hosp.model.ProgramaBean;
import br.gov.al.maceio.sishosp.hosp.model.TipoAtendimentoBean;

public class RelatoriosController {

	private ProgramaBean programa;
	private GrupoBean grupo;
	private TipoAtendimentoBean tipo;
	private ProfissionalBean prof;
	private Date dataInicial;
	private Date dataFinal;
	private String recurso;
	private String tipoExameAuditivo;
	private String situacao;
	private String tipoAnalSint;

	public RelatoriosController() {
		this.programa = new ProgramaBean();
		this.grupo = new GrupoBean();
		this.tipo = new TipoAtendimentoBean();
		this.prof = new ProfissionalBean();
		this.dataInicial = null;
		this.dataFinal = null;
		this.tipoExameAuditivo = new String("TODOS");
		this.tipoAnalSint = new String("A");
	}

	public void limparDados() {
		this.programa = new ProgramaBean();
		this.grupo = new GrupoBean();
		this.tipo = new TipoAtendimentoBean();
		this.prof = new ProfissionalBean();
		this.dataInicial = null;
		this.dataFinal = null;
		this.tipoExameAuditivo = new String("TODOS");
		this.tipoAnalSint = new String("A");
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

	public ProfissionalBean getProf() {
		return prof;
	}

	public void setProf(ProfissionalBean prof) {
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

	public void gerarMapaLaudoOrteseProtese() throws IOException,
			ParseException {

		if (this.dataFinal == null || this.dataInicial == null
				|| this.programa == null || this.grupo == null) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Todos os campos devem ser preenchidos.",
					"Campos inválidos!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return;
		}

		if (!verificarMesesIguais(this.dataInicial, this.dataFinal)) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"As datas devem possuir o mesmo mês.", "Datas Inválidas!");
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
		limparDados();
	}

	public void gerarFinanceiroOrteseProtese() throws IOException,
			ParseException {

		if (this.dataFinal == null || this.dataInicial == null
				|| this.programa == null || this.grupo == null) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Todos os campos devem ser preenchidos.",
					"Campos inválidos!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return;
		}

		if (!verificarMesesIguais(this.dataInicial, this.dataFinal)) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"As datas devem possuir o mesmo mês.", "Datas Inválidas!");
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
		limparDados();
	}

	public void gerarAgendamentosPorProfissional() throws IOException,
			ParseException {

		if (this.dataFinal == null || this.dataInicial == null
				|| this.programa == null || this.grupo == null
				|| this.tipo == null) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Todos os campos devem ser preenchidos.",
					"Campos inválidos!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return;
		}

		if (!verificarMesesIguais(this.dataInicial, this.dataFinal)) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"As datas devem possuir o mesmo mês.", "Datas Inválidas!");
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
			map.put("cod_medico", this.prof.getIdProfissional());
		}
		map.put("SUBREPORT_DIR", this.getServleContext().getRealPath(caminho)
				+ File.separator);

		this.executeReport(relatorio, map, "relatorio.pdf");
		limparDados();
	}

	public void gerarAtendimentosPorPrograma() throws IOException,
			ParseException {

		if (this.dataFinal == null || this.dataInicial == null
				|| this.programa == null) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Datas e programa devem ser preenchidos.",
					"Campos inválidos!");
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
		limparDados();
	}

	public void gerarAtendimentosPorProcedimento() throws IOException,
			ParseException {

		if (this.dataFinal == null || this.dataInicial == null) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Datas devem ser preenchidas.",
					"Campos inválidos!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return;
		}

		String caminho = "/WEB-INF/relatorios/";
		String relatorio = "";
		if(this.tipoAnalSint.equals("A")){
			relatorio = caminho + "atendPorProcedimentosAnalitico.jasper";
		}else if(this.tipoAnalSint.equals("S")){
			relatorio = caminho + "atendPorProcedimentosSintetico.jasper";
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("dt_inicial", this.dataInicial);
		map.put("dt_final", this.dataFinal);
		map.put("SUBREPORT_DIR", this.getServleContext().getRealPath(caminho)
				+ File.separator);

		this.executeReport(relatorio, map, "relatorio.pdf");
		limparDados();
	}

	private void executeReport(String relatorio, Map<String, Object> map,
			String filename) throws IOException, ParseException {
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

	private FacesContext getFacesContext() {
		FacesContext context = FacesContext.getCurrentInstance();
		return context;
	}

	private ServletContext getServleContext() {
		ServletContext scontext = (ServletContext) this.getFacesContext()
				.getExternalContext().getContext();
		return scontext;
	}

}
