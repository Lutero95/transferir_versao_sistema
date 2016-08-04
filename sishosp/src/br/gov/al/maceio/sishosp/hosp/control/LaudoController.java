package br.gov.al.maceio.sishosp.hosp.control;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;

import br.gov.al.maceio.sishosp.acl.model.UsuarioBean;
import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.hosp.dao.LaudoDAO;
import br.gov.al.maceio.sishosp.hosp.model.CidBean;
import br.gov.al.maceio.sishosp.hosp.model.EquipamentoBean;
import br.gov.al.maceio.sishosp.hosp.model.EquipeBean;
import br.gov.al.maceio.sishosp.hosp.model.FornecedorBean;
import br.gov.al.maceio.sishosp.hosp.model.GrupoBean;
import br.gov.al.maceio.sishosp.hosp.model.LaudoBean;
import br.gov.al.maceio.sishosp.hosp.model.PacienteBean;
import br.gov.al.maceio.sishosp.hosp.model.ProcedimentoBean;
import br.gov.al.maceio.sishosp.hosp.model.ProfissionalBean;
import br.gov.al.maceio.sishosp.hosp.model.ProgramaBean;
import br.gov.al.maceio.sishosp.hosp.model.TipoAtendimentoBean;

@ManagedBean(name = "LaudoController")
@ViewScoped
public class LaudoController implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer abaAtiva = 0;
	private UsuarioBean usuario;
	private LaudoBean laudo;
	private PacienteBean paciente;
	private GrupoBean grupo;
	private ProgramaBean programa;
	private TipoAtendimentoBean tipoAt;
	private ProfissionalBean profissional;
	private EquipeBean equipe;
	private ProcedimentoBean procedimento;
	private FornecedorBean fornecedor;
	private CidBean cid;
	private EquipamentoBean equipamento;

	private String cabecalho;

	// LISTAS
	private List<LaudoBean> listaLaudo;
	private List<LaudoBean> listaLaudoDigita;

	// BUSCAS
	private String tipo;
	private Integer tipoBuscaLaudo;
	private String campoBuscaLaudo;
	private Integer campoBuscaNumero;
	private Date campoBuscaData;
	private String statusLaudo;

	// BUSCA PERSONALIZADA
	private String situacao;
	private String nome;
	private String recurso;
	private Date dataAtorizacao;
	private Date dataSolicitacao;
	private Integer prontuario;
	private String programa2;

	public LaudoController() {
		// CLASSES
		laudo = new LaudoBean();

		this.tipoAt = null;// new TipoAtendimentoBean();

		// BUSCA
		tipo = "";
		tipoBuscaLaudo = 1;
		campoBuscaLaudo = "";
		statusLaudo = "P";
		campoBuscaNumero = 1;
		campoBuscaData = null;
		this.cabecalho = "";
		listaLaudo = new ArrayList<>();
		listaLaudo = null;

		listaLaudoDigita = new ArrayList<>();
		listaLaudoDigita = null;

	}

	public void gravarLaudo() throws ProjetoException {
		LaudoDAO udao = new LaudoDAO();
		boolean cadastrou = udao.cadastrarLaudo(laudo);

		if (cadastrou == true) {
			limparDados();
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Laudo cadastrado com sucesso!", "Sucesso");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			listaLaudo = null;

		} else {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Ocorreu um erro durante o cadastro!", "Erro");
			FacesContext.getCurrentInstance().addMessage(null, msg);

		}

	}

	public String alterarLaudo() throws ProjetoException {

		LaudoDAO rdao = new LaudoDAO();
		boolean alterou = rdao.alterarLaudo(laudo);
		listaLaudo = null;
		if (alterou == true) {
			limparDados();
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Laudo alterado com sucesso!", "Sucesso");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return "/pages/sishosp/gerenciarLaudo.faces?faces-redirect=true";
			// RequestContext.getCurrentInstance().execute("dlgAltMenu.hide();");
		} else {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Ocorreu um erro durante o cadastro!", "Erro");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return "";
			// RequestContext.getCurrentInstance().execute("dlgAltMenu.hide();");
		}

	}

	public void excluirLaudo() throws ProjetoException {
		LaudoDAO udao = new LaudoDAO();

		boolean excluio = udao.excluirLaudo(laudo);

		if (excluio == true) {

			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Laudo excluido com sucesso!", "Sucesso");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			listaLaudo = null;
			RequestContext.getCurrentInstance().execute(
					"PF('dialogAtencao').hide();");
		} else {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Ocorreu um erro durante a exclusao!", "Erro");
			FacesContext.getCurrentInstance().addMessage(null, msg);

			RequestContext.getCurrentInstance().execute(
					"PF('dialogAtencao').hide();");
		}
	}

	public void gravarLaudoDigita() throws ProjetoException {
		LaudoDAO udao = new LaudoDAO();
		boolean cadastrou = udao.cadastrarLaudoDigita(laudo);

		if (cadastrou == true) {
			limparDados();
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Laudo cadastrado com sucesso!", "Sucesso");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			listaLaudoDigita = null;

		} else {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Ocorreu um erro durante o cadastro!", "Erro");
			FacesContext.getCurrentInstance().addMessage(null, msg);

		}

	}

	public String alterarLaudoDigita() throws ProjetoException {

		LaudoDAO rdao = new LaudoDAO();
		boolean alterou = rdao.alterarLaudoDigita(laudo);
		listaLaudoDigita = null;
		if (alterou == true) {
			limparDados();
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Laudo alterado com sucesso!", "Sucesso");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return "/pages/sishosp/gerenciarLaudoDigita.faces?faces-redirect=true";
			// RequestContext.getCurrentInstance().execute("dlgAltMenu.hide();");
		} else {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Ocorreu um erro durante a alteração!", "Erro");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return "";
			// RequestContext.getCurrentInstance().execute("dlgAltMenu.hide();");
		}

	}

	public void excluirLaudoDigita() throws ProjetoException {
		LaudoDAO udao = new LaudoDAO();

		boolean excluio = udao.excluirLaudoDigita(laudo);

		if (excluio == true) {

			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Laudo excluido com sucesso!", "Sucesso");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			listaLaudoDigita = null;
			RequestContext.getCurrentInstance().execute(
					"PF('dialogAtencao').hide();");
		} else {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Ocorreu um erro durante a exclusao!", "Erro");
			FacesContext.getCurrentInstance().addMessage(null, msg);

			RequestContext.getCurrentInstance().execute(
					"PF('dialogAtencao').hide();");
		}
	}

	public void buscarLaudo() throws ProjetoException {

		List<LaudoBean> listaAux = null;
		listaLaudo = new ArrayList<>();

		LaudoDAO adao = new LaudoDAO();

		listaAux = adao.buscarTipoLaudo(this.nome, this.situacao, this.recurso,
				this.prontuario, this.dataAtorizacao, this.dataSolicitacao,
				this.laudo.getPrograma());

		if (listaAux != null && listaAux.size() > 0) {
			// listaAss = null;
			listaLaudo = listaAux;
		} else {
			// listaAss = null;
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN,
					"Nenhum Laudo encontrada.", "Aviso");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}

	}

	public void buscarLaudoDigita() throws ProjetoException {

		List<LaudoBean> listaAux = null;
		listaLaudoDigita = new ArrayList<>();

		LaudoDAO adao = new LaudoDAO();

		listaAux = adao.buscarTipoLaudoDigita(campoBuscaLaudo, tipoBuscaLaudo);

		if (listaAux != null && listaAux.size() > 0) {
			// listaAss = null;
			listaLaudoDigita = listaAux;
		} else {
			// listaAss = null;
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN,
					"Nenhum Laudo encontrada.", "Aviso");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}

	}

	public void buscaPersonalizada() throws ProjetoException {
		System.out.println("TESTE:" + laudo.getDtautorizacao() + "||"
				+ laudo.getPrograma().getIdPrograma());
		List<LaudoBean> listaAux = null;
		listaLaudo = new ArrayList<>();

		LaudoDAO adao = new LaudoDAO();

		listaAux = adao.buscarLaudoPersonalizado();

		if (listaAux != null && listaAux.size() > 0) {
			// listaAss = null;
			listaLaudo = listaAux;
		} else {
			// listaAss = null;
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN,
					"Nenhum Laudo encontrada.", "Aviso");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}

	}

	public void limparBuscaDados() {
		tipoBuscaLaudo = 1;
		campoBuscaLaudo = "";
		statusLaudo = "P";
		listaLaudo = null;
		listaLaudoDigita = null;
		this.dataAtorizacao = null;
		this.dataSolicitacao = null;
		this.situacao = null;
		this.recurso = null;
		this.nome = null;
		this.prontuario = null;
	}

	public void limparDados() {
		laudo = new LaudoBean();
		fornecedor = new FornecedorBean();
		paciente = new PacienteBean();
		grupo = new GrupoBean();
		programa = new ProgramaBean();
		profissional = new ProfissionalBean();
		equipe = new EquipeBean();
		procedimento = new ProcedimentoBean();

	}

	public void calcularDias() {
		try {
			if (laudo.getProrrogar() != null) {
				Integer dias = this.laudo.getProrrogar();
				Date dataFim = this.laudo.getDtasolicitacao();

				// System.out.println("DATA:"+laudo.getProrrogar()+"||"+laudo.getDtainicio()+"||"+laudo.getDtavencimento());

				// System.out.println("DIAS: " + (dias - 1));

				Calendar cl = Calendar.getInstance();
				cl.setTime(dataFim);
				cl.add(Calendar.DATE, (dias - 1));

				Date dataFinal = cl.getTime();

				// System.out.println("DATA FINAL: " + dataFinal);

				this.laudo.setDtafim(dataFinal);
			}
		} catch (Exception ex) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN,
					"Informe a Data de autorização.", "Aviso");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			this.laudo.setProrrogar(null);
		}

	}

	@SuppressWarnings("deprecation")
	public void calcularDiasCalendario() {
		if (laudo.getDtautorizacao() != null) {
			// System.out.println("Entrou aqui"+ laudo.getDtavencimento()
			// +"---"+ laudo.getDtautorizacao());
			// Usuário informa uma data
			Date dataDoUsuario = this.laudo.getDtautorizacao();

			// Através do Calendar, trabalhamos a data informada e adicionamos 1
			// dia nela
			Calendar c = Calendar.getInstance();
			c.setTime(dataDoUsuario);
			c.add(Calendar.DATE, 30);

			// Obtemos a data alterada
			dataDoUsuario = c.getTime();

			this.laudo.setDtavencimento(dataDoUsuario);
		}
	}

	public Integer getAbaAtiva() {
		return abaAtiva;
	}

	public void setAbaAtiva(Integer abaAtiva) {
		this.abaAtiva = abaAtiva;
	}

	public LaudoBean getLaudo() {
		return laudo;
	}

	public void setLaudo(LaudoBean laudo) {
		this.laudo = laudo;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public Integer getTipoBuscaLaudo() {
		return tipoBuscaLaudo;
	}

	public void setTipoBuscaLaudo(Integer tipoBuscaLaudo) {
		this.tipoBuscaLaudo = tipoBuscaLaudo;
	}

	public String getCampoBuscaLaudo() {
		return campoBuscaLaudo;
	}

	public void setCampoBuscaLaudo(String campoBuscaLaudo) {
		this.campoBuscaLaudo = campoBuscaLaudo;
	}

	public String getStatusLaudo() {
		return statusLaudo;
	}

	public void setStatusLaudo(String statusLaudo) {
		this.statusLaudo = statusLaudo;
	}

	public PacienteBean getPaciente() {
		return paciente;
	}

	public void setPaciente(PacienteBean paciente) {
		this.paciente = paciente;
	}

	public GrupoBean getGrupo() {
		return grupo;
	}

	public void setGrupo(GrupoBean grupo) {
		this.grupo = grupo;
	}

	public ProgramaBean getPrograma() {
		return programa;
	}

	public void setPrograma(ProgramaBean programa) {
		this.programa = programa;
	}

	public TipoAtendimentoBean getTipoAt() {
		return tipoAt;
	}

	public void setTipoAt(TipoAtendimentoBean tipoAt) {
		this.tipoAt = tipoAt;
	}

	public ProfissionalBean getProfissional() {
		return profissional;
	}

	public void setProfissional(ProfissionalBean profissional) {
		this.profissional = profissional;
	}

	public EquipeBean getEquipe() {
		return equipe;
	}

	public void setEquipe(EquipeBean equipe) {
		this.equipe = equipe;
	}

	public ProcedimentoBean getProcedimento() {
		return procedimento;
	}

	public void setProcedimento(ProcedimentoBean procedimento) {
		this.procedimento = procedimento;
	}

	public String getCabecalho() {
		if (this.tipo.equals("I")) {
			cabecalho = "CADASTRO DE APAC/BPI";
		} else if (this.tipo.equals("A")) {
			cabecalho = "ALTERAR APAC/BPI";
		}
		return cabecalho;
	}

	public void setCabecalho(String cabecalho) {
		this.cabecalho = cabecalho;
	}

	public FornecedorBean getFornecedor() {
		return fornecedor;
	}

	public void setFornecedor(FornecedorBean fornecedor) {
		this.fornecedor = fornecedor;
	}

	public List<LaudoBean> getListaLaudo() throws ProjetoException {
		if (listaLaudo == null) {

			LaudoDAO fdao = new LaudoDAO();
			listaLaudo = fdao.listaLaudos();
		}
		return listaLaudo;
	}

	public void setListaLaudo(List<LaudoBean> listaLaudo) {
		this.listaLaudo = listaLaudo;
	}

	public String getSituacao() {
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	public CidBean getCid() {
		return cid;
	}

	public void setCid(CidBean cid) {
		this.cid = cid;
	}

	public EquipamentoBean getEquipamento() {
		return equipamento;
	}

	public void setEquipamento(EquipamentoBean equipamento) {
		this.equipamento = equipamento;
	}

	public List<LaudoBean> getListaLaudoDigita() throws ProjetoException {
		if (listaLaudoDigita == null) {

			LaudoDAO fdao = new LaudoDAO();
			listaLaudoDigita = fdao.listaLaudosDigita();
		}
		return listaLaudoDigita;
	}

	public void setListaLaudoDigita(List<LaudoBean> listaLaudoDigita) {
		this.listaLaudoDigita = listaLaudoDigita;
	}

	public String getCabecalho2() {
		if (this.tipo.equals("I")) {
			cabecalho = "CADASTRO DE LAUDO DIGITA";
		} else if (this.tipo.equals("A")) {
			cabecalho = "ALTERAR LAUDO DIGITA";
		}
		return cabecalho;
	}

	public String getCabecalho3() {
		if (this.tipo.equals("I")) {
			cabecalho = "CONTROLE LAUDO";
		} else if (this.tipo.equals("A")) {
			cabecalho = "ALTERAR LAUDO DIGITA";
		}
		return cabecalho;
	}

	public Integer getCampoBuscaNumero() {
		return campoBuscaNumero;
	}

	public void setCampoBuscaNumero(Integer campoBuscaNumero) {
		this.campoBuscaNumero = campoBuscaNumero;
	}

	public Date getCampoBuscaData() {
		return campoBuscaData;
	}

	public void setCampoBuscaData(Date campoBuscaData) {
		this.campoBuscaData = campoBuscaData;
	}

	public UsuarioBean getUsuario() {
		return usuario;
	}

	public void setUsuario(UsuarioBean usuario) {
		this.usuario = usuario;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getRecurso() {
		return recurso;
	}

	public void setRecurso(String recurso) {
		this.recurso = recurso;
	}

	public Date getDataAtorizacao() {
		return dataAtorizacao;
	}

	public void setDataAtorizacao(Date dataAtorizacao) {
		this.dataAtorizacao = dataAtorizacao;
	}

	public Date getDataSolicitacao() {
		return dataSolicitacao;
	}

	public void setDataSolicitacao(Date dataSolicitacao) {
		this.dataSolicitacao = dataSolicitacao;
	}

	public Integer getProntuario() {
		return prontuario;
	}

	public void setProntuario(Integer prontuario) {
		this.prontuario = prontuario;
	}

	public String getPrograma2() {
		return programa2;
	}

	public void setPrograma2(String programa2) {
		this.programa2 = programa2;
	}

}
