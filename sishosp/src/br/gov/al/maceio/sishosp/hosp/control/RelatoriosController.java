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
import br.gov.al.maceio.sishosp.hosp.model.ProgramaBean;

public class RelatoriosController {

	private ProgramaBean programa;
	private GrupoBean grupo;
	private Date dataInicial;
	private Date dataFinal;
	private String recurso;
	private String tipoExameAuditivo;

	public RelatoriosController() {
		this.programa = new ProgramaBean();
		this.grupo = new GrupoBean();
		this.dataInicial = null;
		this.dataFinal = null;
		this.tipoExameAuditivo = new String("TODOS");
	}

	public void limparDados() {
		this.programa = new ProgramaBean();
		this.grupo = new GrupoBean();
		this.dataInicial = null;
		this.dataFinal = null;
		this.tipoExameAuditivo = new String("TODOS");
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

	public boolean verificarMesesIguais(Date dataInicial, Date dataFinal) {
		System.out.println("1 "+this.grupo.getDescGrupo()+" "+this.grupo.isAuditivo() + " "+ this.grupo.getQtdFrequencia());
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
		System.out.println(this.grupo.getDescGrupo()+" "+this.grupo.isAuditivo());
		if(this.grupo.isAuditivo()){
			System.out.println("EH AUDITIVO");
			relatorio = caminho + "mapaLaudoOrteseProteseAuditivo.jasper";
			map.put("dt_inicial", this.dataInicial);
			map.put("dt_final", this.dataFinal);
			map.put("cod_programa", this.programa.getIdPrograma());
			map.put("cod_grupo", this.grupo.getIdGrupo());
			map.put("recurso", this.recurso);
			map.put("tipo_exame_auditivo", this.tipoExameAuditivo);
		}else{
			System.out.println("NAO EH AUDITIVO");
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
		//limparDados();
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
