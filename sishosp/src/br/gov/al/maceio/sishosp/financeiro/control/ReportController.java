package br.gov.al.maceio.sishosp.financeiro.control;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import br.gov.al.maceio.sishosp.acl.model.FuncionarioBean;
import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;
import br.gov.al.maceio.sishosp.financeiro.model.BancoBean;
import br.gov.al.maceio.sishosp.financeiro.model.ClienteBean;
import br.gov.al.maceio.sishosp.financeiro.model.FiltroBean;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperRunManager;

@ManagedBean
@ViewScoped
public class ReportController implements Serializable {

	private static final long serialVersionUID = 1L;

	private Date periodoinicial;
	private Date periodofinal;
	private Integer mes, ano;
	private ClienteBean clienteBean;
	private BancoBean bancoBean;
	private FiltroBean filtro;
	/**
	 * relatorios receber financeiro *
	 */
	private Integer codCliente;
	private Integer codTipoDocumento;
	private java.util.Date dataVencimento;
	private String tipoRelatorio;
	private String nomeRel;

	// **fim **//
	/**
	 * relatorios pagar financeiro *
	 */
	private Integer codFornecedor;
	private Integer codfonRec;
	private Integer codcccusto;
	private Integer coddespesa;
	private Integer codportador;
	private Date dataInicio;
	private Date dataFim;
	private String situacao;
	private String agrupar;
	private String agruparReceber;
	private String dataRef;
	private String dataRefe;
	private String valor;
	private Integer idbanco;
	private boolean mostrarBotaoImprimir;

	public ReportController() throws ProjetoException {

		this.tipoRelatorio = "S";
		clienteBean = new ClienteBean();
		bancoBean = new BancoBean();
		filtro = new FiltroBean();
		filtro.setAgrupaRel("A");
		agrupar = "F";
		agruparReceber = "C";
		situacao = "T";
		dataRef = "V";
		dataRefe = "E";
		mostrarBotaoImprimir = true;
		
	}

	public void limpar() {

		clienteBean = new ClienteBean();
		periodofinal = null;
		periodoinicial = null;
		bancoBean = new BancoBean();

	}
	public void pagarPorTipoDeDocumento(ActionEvent e) throws IOException, ParseException {
		if (tipoRelatorio.equals("S")) {
			String caminho = "/WEB-INF/relatorios/financeiro/";
			String relatorio = "";
			relatorio = caminho + "pagarDebitosportipodedocumentoSintetico.jasper";

			FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
					.getSessionMap().get("obj_usuario");

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("REPORT_LOCALE", new Locale("pt", "BR"));
			map.put("datainicio", new java.sql.Date(dataInicio.getTime()));
			map.put("datafim", new java.sql.Date(dataFim.getTime()));
			map.put("referencia", dataRef); // aspas - ireport, verde java
			map.put("codempresa", user_session.getEmpresa().getCodEmpresa()); 
			map.put("situacao", situacao);
			map.put("codFornecedor", codFornecedor);
			map.put("codTipoDocumento", codTipoDocumento);
			map.put("codccusto", codcccusto);
			map.put("coddespesa", coddespesa);
			map.put("codportador", codportador);
			map.put("tipoRelatorio", tipoRelatorio);
			// map.put("filtro", filtro);

			this.executeReport(relatorio, map, "PagarDocumentoSintetico.pdf");
		}

		if (tipoRelatorio.equals("A")) {
			String caminho = "/WEB-INF/relatorios/financeiro/";
			String relatorio = "";
			relatorio = caminho + "pagarDebitosportipodedocumentoAnalitico.jasper";

			FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
					.getSessionMap().get("obj_usuario");

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("REPORT_LOCALE", new Locale("pt", "BR"));
			map.put("datainicio", new java.sql.Date(dataInicio.getTime()));
			map.put("datafim", new java.sql.Date(dataFim.getTime()));
			map.put("referencia", dataRef); // aspas - ireport, verde java
			map.put("situacao", situacao);
			map.put("codempresa", user_session.getEmpresa().getCodEmpresa()); 
			map.put("codFornecedor", codFornecedor);
			map.put("codTipoDocumento", codTipoDocumento);
			map.put("codccusto", codcccusto);
			map.put("coddespesa", coddespesa);
			map.put("codportador", codportador);
			map.put("tipoRelatorio", tipoRelatorio);
			this.executeReport(relatorio, map, "PagarDocumentoAnalitico.pdf");
		}
	}

	public void pagarPorCentroDeCusto(ActionEvent e) throws IOException, ParseException {
		if (tipoRelatorio.equals("S")) {
			String caminho = "/WEB-INF/relatorios/";
			String relatorio = "";
			relatorio = caminho + "pagarDebitosporCentrodeCustoSintetico.jasper";

			FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
					.getSessionMap().get("obj_usuario");

			Map<String, Object> map = new HashMap<String, Object>();
			
			map.put("REPORT_LOCALE", new Locale("pt", "BR"));
			map.put("datainicio", new java.sql.Date(dataInicio.getTime()));
			map.put("datafim", new java.sql.Date(dataFim.getTime()));
			map.put("referencia", dataRef); // aspas - ireport, verde java
			map.put("situacao", situacao);
			map.put("codFornecedor", codFornecedor);
			map.put("codempresa", user_session.getEmpresa().getCodEmpresa()); 
			map.put("codTipoDocumento", codTipoDocumento);
			map.put("codccusto", codcccusto);
			map.put("coddespesa", coddespesa);
			map.put("codportador", codportador);
			map.put("tipoRelatorio", tipoRelatorio);
			this.executeReport(relatorio, map, "PagarCentroDeCustoSintetico.pdf");
		}

		if (tipoRelatorio.equals("A")) {
			String caminho = "/WEB-INF/relatorios/";
			String relatorio = "";
			relatorio = caminho + "pagarDebitospoCentrodeCustoAnalitico.jasper";

			FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
					.getSessionMap().get("obj_usuario");

			Map<String, Object> map = new HashMap<String, Object>();
			
			map.put("REPORT_LOCALE", new Locale("pt", "BR"));
			map.put("datainicio", new java.sql.Date(dataInicio.getTime()));
			map.put("datafim", new java.sql.Date(dataFim.getTime()));
			map.put("referencia", dataRef); // aspas - ireport, verde java
			map.put("situacao", situacao);
			map.put("codFornecedor", codFornecedor);
			map.put("codTipoDocumento", codTipoDocumento);
			map.put("codempresa", user_session.getEmpresa().getCodEmpresa()); 
			map.put("codccusto", codcccusto);
			map.put("coddespesa", coddespesa);
			map.put("codportador", codportador);
			map.put("tipoRelatorio", tipoRelatorio);
			this.executeReport(relatorio, map, "PagarCentroDeCustoAnalitico.pdf");
		}
	}

	public void pagarPorFornecedor(ActionEvent e) throws IOException, ParseException {
		if (tipoRelatorio.equals("S")) {
			String caminho = "/WEB-INF/relatorios/financeiro/";
			String relatorio = "";
			relatorio = caminho + "pagarFornecedorSintetico.jasper";

			FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
					.getSessionMap().get("obj_usuario");

			Map<String, Object> map = new HashMap<String, Object>();
			
			map.put("REPORT_LOCALE", new Locale("pt", "BR"));
			map.put("datainicio", new java.sql.Date(dataInicio.getTime()));
			map.put("datafim", new java.sql.Date(dataFim.getTime()));
			map.put("referencia", dataRef); // aspas - ireport, verde java
			map.put("situacao", situacao);
			map.put("codFornecedor", codFornecedor);
			map.put("codTipoDocumento", codTipoDocumento);
			map.put("codempresa", user_session.getEmpresa().getCodEmpresa()); 
			map.put("codccusto", codcccusto);
			map.put("coddespesa", coddespesa);
			map.put("codportador", codportador);
			map.put("tipoRelatorio", tipoRelatorio);
			this.executeReport(relatorio, map, "pagarFornecedorSintetico.pdf");
		}

		if (tipoRelatorio.equals("A")) {
			String caminho = "/WEB-INF/relatorios/financeiro/";
			String relatorio = "";
			relatorio = caminho + "pagarFornecedor.jasper";

			FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
					.getSessionMap().get("obj_usuario");

			Map<String, Object> map = new HashMap<String, Object>();
			
			map.put("REPORT_LOCALE", new Locale("pt", "BR"));
			map.put("datainicio", new java.sql.Date(dataInicio.getTime()));
			map.put("datafim", new java.sql.Date(dataFim.getTime()));
			map.put("referencia", dataRef); // aspas - ireport, verde java
			map.put("situacao", situacao);
			map.put("codFornecedor", codFornecedor);
			map.put("codTipoDocumento", codTipoDocumento);
			map.put("codccusto", codcccusto);
			map.put("codempresa", user_session.getEmpresa().getCodEmpresa()); 
			map.put("coddespesa", coddespesa);
			map.put("codportador", codportador);
			map.put("tipoRelatorio", tipoRelatorio);
			this.executeReport(relatorio, map, "PagarPorFornecedorAnalitico.pdf");
		}
	}

	public void pagarPorPortador(ActionEvent e) throws IOException, ParseException {
		if (tipoRelatorio.equals("S")) {
			String caminho = "/WEB-INF/relatorios/financeiro/";
			String relatorio = "";
			relatorio = caminho + "pagarDebitosporPortadorSintetico.jasper";

			FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
					.getSessionMap().get("obj_usuario");

			Map<String, Object> map = new HashMap<String, Object>();
			
			map.put("REPORT_LOCALE", new Locale("pt", "BR"));
			map.put("datainicio", new java.sql.Date(periodoinicial.getTime()));
			map.put("datafim", new java.sql.Date(periodofinal.getTime()));
			map.put("referencia", dataRef); // aspas - ireport, verde java
			map.put("situacao", situacao);
			map.put("codFornecedor", codFornecedor);
			map.put("codempresa", user_session.getEmpresa().getCodEmpresa()); 
			map.put("codTipoDocumento", codTipoDocumento);
			map.put("codccusto", codcccusto);
			map.put("coddespesa", coddespesa);
			map.put("codportador", codportador);
			map.put("tipoRelatorio", tipoRelatorio);
			this.executeReport(relatorio, map, "PagarPorPortadorSintetico.pdf");
		}

		if (tipoRelatorio.equals("A")) {
			String caminho = "/WEB-INF/relatorios/financeiro/";
			String relatorio = "";
			relatorio = caminho + "pagarDebitosporPortadorAnalitico.jasper";

			FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
					.getSessionMap().get("obj_usuario");

			Map<String, Object> map = new HashMap<String, Object>();
			
			map.put("REPORT_LOCALE", new Locale("pt", "BR"));
			map.put("datainicio", new java.sql.Date(dataInicio.getTime()));
			map.put("datafim", new java.sql.Date(dataFim.getTime()));
			map.put("codempresa", user_session.getEmpresa().getCodEmpresa()); 
			map.put("referencia", dataRef); // aspas - ireport, verde java
			map.put("situacao", situacao);
			map.put("codFornecedor", codFornecedor);
			map.put("codTipoDocumento", codTipoDocumento);
			map.put("codccusto", codcccusto);
			map.put("coddespesa", coddespesa);
			map.put("codportador", codportador);
			map.put("tipoRelatorio", tipoRelatorio);
			this.executeReport(relatorio, map, "PagarPorPortadorAnalitico.pdf");
		}
	}

	public void pagarPorDespesa(ActionEvent e) throws IOException, ParseException {
		if (tipoRelatorio.equals("S")) {
			String caminho = "/WEB-INF/relatorios/financeiro/";
			String relatorio = "";
			relatorio = caminho + "pagarDebitosporDespesaSintetico.jasper";

			FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
					.getSessionMap().get("obj_usuario");

			Map<String, Object> map = new HashMap<String, Object>();
			
			map.put("REPORT_LOCALE", new Locale("pt", "BR"));
			map.put("datainicio", new java.sql.Date(dataInicio.getTime()));
			map.put("datafim", new java.sql.Date(dataFim.getTime()));
			map.put("codempresa", user_session.getEmpresa().getCodEmpresa()); 
			map.put("referencia", dataRef); // aspas - ireport, verde java
			map.put("situacao", situacao);
			map.put("codFornecedor", codFornecedor);
			map.put("codTipoDocumento", codTipoDocumento);
			map.put("codccusto", codcccusto);
			map.put("coddespesa", coddespesa);
			map.put("codportador", codportador);
			map.put("tipoRelatorio", tipoRelatorio);
			this.executeReport(relatorio, map, "PagarPorDespesaSintetico.pdf");
		}

		if (tipoRelatorio.equals("A")) {
			String caminho = "/WEB-INF/relatorios/financeiro/";
			String relatorio = "";
			relatorio = caminho + "pagarDebitosporDespesaAnalitico.jasper";

			FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
					.getSessionMap().get("obj_usuario");

			Map<String, Object> map = new HashMap<String, Object>();
			
			map.put("REPORT_LOCALE", new Locale("pt", "BR"));
			map.put("datainicio", new java.sql.Date(dataInicio.getTime()));
			map.put("codempresa", user_session.getEmpresa().getCodEmpresa()); 
			map.put("datafim", new java.sql.Date(dataFim.getTime()));
			map.put("referencia", dataRef); // aspas - ireport, verde java
			map.put("situacao", situacao);
			map.put("codFornecedor", codFornecedor);
			map.put("codTipoDocumento", codTipoDocumento);
			map.put("codccusto", codcccusto);
			map.put("coddespesa", coddespesa);
			map.put("codportador", codportador);
			map.put("tipoRelatorio", tipoRelatorio);
			this.executeReport(relatorio, map, "PagarPorDespesaAnalitico.pdf");
		}
	}

	public void receberPorTipoDeDocumento(ActionEvent e) throws IOException, ParseException {
		if (tipoRelatorio.equals("S")) {
			String caminho = "/WEB-INF/relatorios/";
			String relatorio = "";
			nomeRel = "teste";
			// Faltando o ReceberPorTipoDeDocumentoSintetico
			relatorio = caminho + "receberPortipodedocumentoAnalitico.jasper";
			FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
					.getSessionMap().get("obj_usuario");

			Map<String, Object> map = new HashMap<String, Object>();
			
			map.put("REPORT_LOCALE", new Locale("pt", "BR"));
			map.put("id_cliente", clienteBean.getCodcliente());
			map.put("datainicio", new java.sql.Date(periodoinicial.getTime()));
			map.put("datafim", new java.sql.Date(periodofinal.getTime()));
			map.put("referencia", dataRef); // aspas - ireport, verde java
			map.put("situacao", situacao);
			map.put("codfonrec", codfonRec);
			map.put("nomerel", nomeRel);
			// map.put("filtro", filtro);
			this.executeReport(relatorio, map, "ReceberDocumentoSintetico.pdf");
		}

		if (tipoRelatorio.equals("A")) {
			String caminho = "/WEB-INF/relatorios/";
			String relatorio = "";
			relatorio = caminho + "receberPortipodedocumentoAnalitico.jasper";

			FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
					.getSessionMap().get("obj_usuario");

			Map<String, Object> map = new HashMap<String, Object>();
			
			map.put("REPORT_LOCALE", new Locale("pt", "BR"));
			map.put("id_cliente", clienteBean.getCodcliente());
			map.put("datainicio", new java.sql.Date(periodoinicial.getTime()));
			map.put("datafim", new java.sql.Date(periodofinal.getTime()));
			map.put("referencia", dataRef); // aspas - ireport, verde java
			map.put("situacao", situacao);
			map.put("codfonrec", codfonRec);
			map.put("nomerel", nomeRel);
			// map.put("filtro", filtro);
			this.executeReport(relatorio, map, "ReceberDocumentoAnalitico.pdf");
		}
	}

	public void receberPorCliente(ActionEvent e) throws IOException, ParseException {

		if (tipoRelatorio.equals("S")) {
			String caminho = "/WEB-INF/relatorios/";
			String relatorio = "";
			relatorio = caminho + "receberCreditosPorClienteSintetico.jasper";

			FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
					.getSessionMap().get("obj_usuario");

			Map<String, Object> map = new HashMap<String, Object>();
			
			map.put("REPORT_LOCALE", new Locale("pt", "BR"));
			map.put("id_cliente", clienteBean.getCodcliente());
			map.put("datainicio", new java.sql.Date(periodoinicial.getTime()));
			map.put("datafim", new java.sql.Date(periodofinal.getTime()));
			map.put("referencia", dataRef); // aspas - ireport, verde java
			map.put("situacao", situacao);
			map.put("codfonrec", codfonRec);
			map.put("nomerel", nomeRel);
			map.put("codTipoDocumento", codTipoDocumento);
			map.put("codportador", codportador);
			map.put("tipoRelatorio", tipoRelatorio);
			// map.put("filtro", filtro);
			this.executeReport(relatorio, map, "ReceberPorClienteSintetico.pdf");
		}

		if (tipoRelatorio.equals("A")) {
			String caminho = "/WEB-INF/relatorios/";
			String relatorio = "";
			relatorio = caminho + "receberCreditosporClienteAnalitico.jasper";

			FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
					.getSessionMap().get("obj_usuario");

			Map<String, Object> map = new HashMap<String, Object>();
			
			map.put("REPORT_LOCALE", new Locale("pt", "BR"));
			map.put("id_cliente", clienteBean.getCodcliente());
			map.put("datainicio", new java.sql.Date(periodoinicial.getTime()));
			map.put("datafim", new java.sql.Date(periodofinal.getTime()));
			map.put("referencia", dataRef); // aspas - ireport, verde java
			map.put("situacao", situacao);
			map.put("codfonrec", codfonRec);
			map.put("nomerel", nomeRel);
			map.put("codTipoDocumento", codTipoDocumento);
			map.put("codportador", codportador);
			map.put("tipoRelatorio", tipoRelatorio);
			// map.put("filtro", filtro);
			this.executeReport(relatorio, map, "ReceberPorClienteAnalitico.pdf");
		}
	}

	public void receberPorFonteDeReceita(ActionEvent e) throws IOException, ParseException {
		if (tipoRelatorio.equals("S")) {
			String caminho = "/WEB-INF/relatorios/";
			String relatorio = "";
			relatorio = caminho + "receberPorfontedeReceitaSintetico.jasper";

			FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
					.getSessionMap().get("obj_usuario");

			Map<String, Object> map = new HashMap<String, Object>();
			
			map.put("REPORT_LOCALE", new Locale("pt", "BR"));
			map.put("id_cliente", clienteBean.getCodcliente());
			map.put("datainicio", new java.sql.Date(periodoinicial.getTime()));
			map.put("datafim", new java.sql.Date(periodofinal.getTime()));
			map.put("referencia", dataRef); // aspas - ireport, verde java
			map.put("situacao", situacao);
			map.put("codfonrec", codfonRec);
			map.put("nomerel", nomeRel);
			map.put("codTipoDocumento", codTipoDocumento);
			map.put("codportador", codportador);
			map.put("tipoRelatorio", tipoRelatorio);
			// map.put("filtro", filtro);
			this.executeReport(relatorio, map, "ReceberPorFonteReceitaSintetico.pdf");
		}

		if (tipoRelatorio.equals("A")) {
			String caminho = "/WEB-INF/relatorios/";
			String relatorio = "";
			relatorio = caminho + "receberPorfontedereceitaAnalitico.jasper";

			FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
					.getSessionMap().get("obj_usuario");

			Map<String, Object> map = new HashMap<String, Object>();
			
			map.put("REPORT_LOCALE", new Locale("pt", "BR"));
			map.put("id_cliente", clienteBean.getCodcliente());
			map.put("datainicio", new java.sql.Date(periodoinicial.getTime()));
			map.put("datafim", new java.sql.Date(periodofinal.getTime()));
			map.put("referencia", dataRef); // aspas - ireport, verde java
			map.put("situacao", situacao);
			map.put("codfonrec", codfonRec);
			map.put("nomerel", nomeRel);
			map.put("codTipoDocumento", codTipoDocumento);
			map.put("codportador", codportador);
			map.put("tipoRelatorio", tipoRelatorio);
			// map.put("filtro", filtro);
			this.executeReport(relatorio, map, "ReceberPorFonteReceitaAnalitico.pdf");
		}
	}

	public void receberPorMesDeVencimento(ActionEvent e) throws IOException, ParseException {
		if (tipoRelatorio.equals("S")) {
			String caminho = "/WEB-INF/relatorios/";
			String relatorio = "";
			relatorio = caminho + "receberPormesdevencimentoSintetico.jasper";

			FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
					.getSessionMap().get("obj_usuario");

			Map<String, Object> map = new HashMap<String, Object>();
			
			map.put("REPORT_LOCALE", new Locale("pt", "BR"));
			map.put("id_cliente", clienteBean.getCodcliente());
			map.put("datainicio", new java.sql.Date(periodoinicial.getTime()));
			map.put("datafim", new java.sql.Date(periodofinal.getTime()));
			map.put("referencia", dataRef); // aspas - ireport, verde java
			map.put("situacao", situacao);
			map.put("codfonrec", codfonRec);
			map.put("nomerel", nomeRel);
			map.put("codTipoDocumento", codTipoDocumento);
			map.put("codportador", codportador);
			map.put("tipoRelatorio", tipoRelatorio);
			// map.put("filtro", filtro);
			this.executeReport(relatorio, map, "ReceberPorMesVencimentoSintetico.pdf");
		}

		if (tipoRelatorio.equals("A")) {
			String caminho = "/WEB-INF/relatorios/";
			String relatorio = "";
			relatorio = caminho + "receberPormesdevencimentoAnalitico.jasper";

			FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
					.getSessionMap().get("obj_usuario");

			Map<String, Object> map = new HashMap<String, Object>();
			
			map.put("REPORT_LOCALE", new Locale("pt", "BR"));
			map.put("id_cliente", clienteBean.getCodcliente());
			map.put("datainicio", new java.sql.Date(periodoinicial.getTime()));
			map.put("datafim", new java.sql.Date(periodofinal.getTime()));
			map.put("referencia", dataRef); // aspas - ireport, verde java
			map.put("situacao", situacao);
			map.put("codfonrec", codfonRec);
			map.put("nomerel", nomeRel);
			map.put("codTipoDocumento", codTipoDocumento);
			map.put("codportador", codportador);
			map.put("tipoRelatorio", tipoRelatorio);
			// map.put("filtro", filtro);
			this.executeReport(relatorio, map, "ReceberPorMesVencimentoAnalitico.pdf");
		}
	}

		public void reportCaixaDiario(ActionEvent e) throws IOException, ParseException {
		String caminho = "/WEB-INF/relatorios/";
		String relatorio = "";
		nomeRel = "teste";
		relatorio = caminho + "caixaDiarioAnalitico.jasper";
		FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
				.get("obj_usuario");

		Map<String, Object> map = new HashMap<String, Object>();

		// if (bancoBean.getId().equals("")){
		// map.put("idbanco", null);

		// }
		// else{
		map.put("idbanco", bancoBean.getId());
		// map.put("idbanco", 5);
		// }
		map.put("SUBREPORT_DIR", this.getServleContext().getRealPath(caminho) + File.separator);
		;
		
		map.put("REPORT_LOCALE", new Locale("pt", "BR"));
		map.put("datainicio", new java.sql.Date(periodoinicial.getTime()));
		map.put("datafim", new java.sql.Date(periodofinal.getTime()));

		this.executeReport(relatorio, map, "RelatorioCaixaDiario.pdf");

	}

	
	public void pagarporFornecedor(ActionEvent action) throws IOException, ParseException {

		String caminho = "/WEB-INF/relatorios/";
		String relatorio = caminho + "pagarFornecedor.jasper";
		FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
				.get("obj_usuario");
		Map<String, Object> map = new HashMap<String, Object>();

		
		map.put("nomerel", "DÉBITOS POR FORNECEDOR");
		map.put("filtro", "DÉBITOS POR FORNECEDOR");
		map.put("datainicio", this.dataInicio);
		map.put("datafim", this.dataFim);
		map.put("referencia", dataRef);

		map.put("situacao", this.situacao);
		map.put("codforn", this.codFornecedor);
		map.put("REPORT_LOCALE", new Locale("pt", "BR"));
		this.executeReport(relatorio, map, "pagarFornecedor.pdf");
	}

	public void pagarPorCentro(ActionEvent action) throws IOException, ParseException {

		String caminho = "/WEB-INF/relatorios/";
		String relatorio = caminho + "pagarDebitospoCentrodeCustoAnalitico.jasper";

		Map<String, Object> map = new HashMap<String, Object>();
		FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
				.get("obj_usuario");

		
		map.put("nomerel", "DÉBITOS POR CENTRO DE CUSTO - ANALITICO");
		map.put("filtro", "DÉBITOS POR CENTRO DE CUSTO");
		map.put("datainicio", this.dataInicio);
		map.put("datafim", this.dataFim);
		map.put("referencia", dataRef);

		map.put("situacao", this.situacao);
		map.put("codforn", this.codFornecedor);
		map.put("REPORT_LOCALE", new Locale("pt", "BR"));
		this.executeReport(relatorio, map, "pagarCCusto.pdf");
	}

	public void pagarPorTipoDoc(ActionEvent action) throws IOException, ParseException {

		String caminho = "/WEB-INF/relatorios/";
		String relatorio = caminho + "pagarTipoDoc.jasper";

		Map<String, Object> map = new HashMap<String, Object>();

		FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
				.get("obj_usuario");
		
		map.put("nomerel", "DÉBITOS POR FORNECEDOR");
		map.put("filtro", "DÉBITOS POR FORNECEDOR");
		map.put("datainicio", this.dataInicio);
		map.put("datafim", this.dataFim);
		map.put("referencia", dataRef);

		map.put("situacao", this.situacao);
		map.put("codforn", this.codFornecedor);
		map.put("REPORT_LOCALE", new Locale("pt", "BR"));
		this.executeReport(relatorio, map, "pagarTipoDocumento.pdf");
	}

	public void fornecedorListagem(ActionEvent action) throws IOException, ParseException {

		String caminho = "/WEB-INF/relatorios/";
		String relatorio = caminho + "FornecedorListagem.jasper";
		FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
				.get("obj_usuario");
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("REPORT_LOCALE", new Locale("pt", "BR"));
		this.executeReport(relatorio, map, "ListagemFornecedor.pdf");
	}

	public void executeReceberCliente(ActionEvent action) throws IOException, ParseException {
		String caminho = "/WEB-INF/relatorios/";
		String relatorio = caminho + "receberCliente.jasper";
		FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
				.get("obj_usuario");
		Map<String, Object> map = new HashMap<String, Object>();

		
		map.put("nomerel", "CRÉDITOS POR CLIENTE");
		map.put("filtro", "CRÉDITOS POR CLIENTE");
		map.put("datainicio", this.dataInicio);
		map.put("datafim", this.dataFim);
		map.put("referencia", dataRef);

		map.put("situacao", this.situacao);
		map.put("id_cliente", this.codCliente);
		map.put("REPORT_LOCALE", new Locale("pt", "BR"));
		this.executeReport(relatorio, map, "receberCliente.pdf");
	}

	
	public void executeCaixaBancos(ActionEvent action) throws IOException, ParseException {
		String caminho = "/WEB-INF/relatorios/";
		String relatorio = caminho + "caixaDiarioAnalitico.jasper";

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("datainicio", dataInicio);
		map.put("datafim", dataFim);
		FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
				.get("obj_usuario");
		
		map.put("nomerel", "DÉBITOS POR FORNECEDOR");
		map.put("filtro", "DÉBITOS POR FORNECEDOR");

		map.put("idbanco", idbanco);

		map.put("SUBREPORT_DIR", this.getServleContext().getRealPath(caminho) + File.separator);
		map.put("REPORT_LOCALE", new Locale("pt", "BR"));
		this.executeReport(relatorio, map, "caixaDiario.pdf");
	}

	
	private void executeReport(String relatorio, Map<String, Object> map, String filename)
			throws IOException, ParseException {

		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();

		InputStream reportStream = context.getExternalContext().getResourceAsStream(relatorio);

		response.setContentType("application/pdf");
		response.setHeader("Content-disposition", "attachment;filename=" + filename);

		ServletOutputStream servletOutputStream = response.getOutputStream();

		try {
			Connection connection = ConnectionFactory.getConnection();

			// JRProperties.setProperty("net.sf.jasperreports.awt.ignore.missing.font",
			// "true");
			// JRProperties.setProperty("net.sf.jasperreports.default.font.name",
			// defaultPDFFont);
			/*
			 * DefaultJasperReportsContext ct = DefaultJasperReportsContext.getInstance();
			 * JRPropertiesUtil.getInstance(ct)
			 * .setProperty("net.sf.jasperreports.awt.igno‌​re.missing.font", "true");
			 * JRPropertiesUtil.getInstance(ct)
			 * .setProperty("net.sf.jasperreports.default.font.name", defaultPDFFont);
			 */

			JasperRunManager.runReportToPdfStream(reportStream, response.getOutputStream(), map, connection);

			if (connection != null) {
				connection.close();
			}
			
		} catch (JRException e) {
			e.printStackTrace();
		} catch (ProjetoException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
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
		ServletContext scontext = (ServletContext) this.getFacesContext().getExternalContext().getContext();
		return scontext;
	}

	/*	private byte[] executeReportInternamente() throws IOException, ParseException, ProjetoException {

		//arquivo = null;
		Map<String, Object> parametros = new HashMap<String, Object>();
		String nomeArquivoSaida = "";
		// byte[] bytes = null;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("codorcamento", 47);

		Connection conexao = null;
		try {
			conexao = ConnectionFactory.getConnection();

			// JasperReport jasperReport =
			// JasperCompileManager.compileReport("/WEB-INF/relatorios/iprev/relCertNegativaNovo.jrxml");
			JasperReport jasperReport = JasperCompileManager.compileReport(
					this.getServleContext().getRealPath("/WEB-INF/relatorios/") + File.separator + "Orcamento.jrxml");

			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, conexao);

			byte[] outputFile = JasperExportManager.exportReportToPdf(jasperPrint);

			// byte[] outputFile = JasperRunManager.runReportToPdf(jasperReport,
			// parametros, conexao);
			// bytes = JasperRunManager.runReportToPdf(reportStream, parametros,
			// conexao);
			arquivo = new ArquivoBean();
			arquivo.setDescricao(nomeArquivoSaida);
			arquivo.setArquivo(outputFile);
		} catch (JRException ex) {
			throw new RuntimeException(ex);
		} finally {
			try {
				conexao.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		return arquivo.getArquivo();
	}

*/
	
	public Integer getCodcccusto() {
		return codcccusto;
	}

	public String getSituacao() {
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	public Date getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	public Date getDataFim() {
		return dataFim;
	}

	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
	}

	public String getTipoRelatorio() {
		return tipoRelatorio;
	}

	public void setTipoRelatorio(String tipoRelatorio) {
		this.tipoRelatorio = tipoRelatorio;
	}

	public void setCodcccusto(Integer codcccusto) {
		this.codcccusto = codcccusto;
	}

	public Integer getCodCliente() {
		return codCliente;
	}

	public Integer getCodFornecedor() {
		return codFornecedor;
	}

	public void setCodFornecedor(Integer codFornecedor) {
		this.codFornecedor = codFornecedor;
	}

	public void setCodCliente(Integer codCliente) {
		this.codCliente = codCliente;
	}

	public Date getPeriodoinicial() {
		return periodoinicial;
	}

	public Date getPeriodofinal() {
		return periodofinal;
	}

	public Integer getCodTipoDocumento() {
		return codTipoDocumento;
	}

	public void setCodTipoDocumento(Integer codTipoDocumento) {
		this.codTipoDocumento = codTipoDocumento;
	}

	public java.util.Date getDataVencimento() {
		return dataVencimento;
	}

	public void setDataVencimento(java.util.Date dataVencimento) {
		this.dataVencimento = dataVencimento;
	}

	public void setPeriodoinicial(Date periodoinicial) {
		this.periodoinicial = periodoinicial;
	}

	public void setPeriodofinal(Date periodofinal) {
		this.periodofinal = periodofinal;
	}

	public Integer getMes() {
		return mes;
	}

	public void setMes(Integer mes) {
		this.mes = mes;
	}

	public Integer getAno() {
		return ano;
	}

	public void setAno(Integer ano) {
		this.ano = ano;
	}

		public ClienteBean getClienteBean() {
		return clienteBean;
	}

	public void setClienteBean(ClienteBean clienteBean) {
		this.clienteBean = clienteBean;
	}

		public FiltroBean getFiltro() {
		return filtro;
	}

	public void setFiltro(FiltroBean filtro) {
		this.filtro = filtro;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}


	public String getAgrupar() {
		return agrupar;
	}

	public void setAgrupar(String agrupar) {
		this.agrupar = agrupar;
	}

	public String getDataRef() {
		return dataRef;
	}

	public void setDataRef(String dataRef) {
		this.dataRef = dataRef;
	}

	public Integer getCoddespesa() {
		return coddespesa;
	}

	public Integer getCodportador() {
		return codportador;
	}

	public void setCoddespesa(Integer coddespesa) {
		this.coddespesa = coddespesa;
	}

	public void setCodportador(Integer codportador) {
		this.codportador = codportador;
	}

	public Integer getCodfonRec() {
		return codfonRec;
	}

	public void setCodfonRec(Integer codfonRec) {
		this.codfonRec = codfonRec;
	}

	public String getAgruparReceber() {
		return agruparReceber;
	}

	public void setAgruparReceber(String agruparReceber) {
		this.agruparReceber = agruparReceber;
	}

	public Integer getIdbanco() {
		return idbanco;
	}

	public void setIdbanco(Integer idbanco) {
		this.idbanco = idbanco;
	}


	public BancoBean getBancoBean() {
		return bancoBean;
	}

	public void setBancoBean(BancoBean bancoBean) {
		this.bancoBean = bancoBean;
	}

	public String getDataRefe() {
		return dataRefe;
	}

	public void setDataRefe(String dataRefe) {
		this.dataRefe = dataRefe;
	}

	
	public boolean isMostrarBotaoImprimir() {
		return mostrarBotaoImprimir;
	}

	public void setMostrarBotaoImprimir(boolean mostrarBotaoImprimir) {
		this.mostrarBotaoImprimir = mostrarBotaoImprimir;
	}

	
}
