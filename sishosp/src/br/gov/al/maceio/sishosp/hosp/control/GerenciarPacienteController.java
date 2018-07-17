package br.gov.al.maceio.sishosp.hosp.control;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import net.bootsfaces.component.row.RowBeanInfo;

import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;

import br.gov.al.maceio.sishosp.acl.model.FuncionarioBean;
import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.hosp.dao.CboDAO;
import br.gov.al.maceio.sishosp.hosp.dao.EnderecoDAO;
import br.gov.al.maceio.sishosp.hosp.dao.GerenciarPacienteDAO;
import br.gov.al.maceio.sishosp.hosp.dao.GrupoDAO;
import br.gov.al.maceio.sishosp.hosp.dao.InsercaoPacienteDAO;
import br.gov.al.maceio.sishosp.hosp.dao.LaudoDAO;
import br.gov.al.maceio.sishosp.hosp.dao.ProgramaDAO;
import br.gov.al.maceio.sishosp.hosp.model.CidBean;
import br.gov.al.maceio.sishosp.hosp.model.EquipamentoBean;
import br.gov.al.maceio.sishosp.hosp.model.EquipeBean;
import br.gov.al.maceio.sishosp.hosp.model.FornecedorBean;
import br.gov.al.maceio.sishosp.hosp.model.GerenciarPacienteBean;
import br.gov.al.maceio.sishosp.hosp.model.GrupoBean;
import br.gov.al.maceio.sishosp.hosp.model.InsercaoPacienteBean;
import br.gov.al.maceio.sishosp.hosp.model.LaudoBean;
import br.gov.al.maceio.sishosp.hosp.model.PacienteBean;
import br.gov.al.maceio.sishosp.hosp.model.ProcedimentoBean;
import br.gov.al.maceio.sishosp.hosp.model.ProgramaBean;
import br.gov.al.maceio.sishosp.hosp.model.TipoAtendimentoBean;

@ManagedBean(name = "GerenciarPacienteController")
@ViewScoped
public class GerenciarPacienteController implements Serializable {

	private static final long serialVersionUID = 1L;
	private GerenciarPacienteBean gerenciarpaciente;
	private GerenciarPacienteBean rowBean;
	private List<GerenciarPacienteBean> listaPacientes;
	private GerenciarPacienteDAO gDao = new GerenciarPacienteDAO();
	private String busca = "N";
	private Boolean apenasLeitura;
	private InsercaoPacienteBean insercao;
	private String tipo;
	private ArrayList<InsercaoPacienteBean> listaLaudosVigentes;
	private InsercaoPacienteDAO iDao;
	private ArrayList<GerenciarPacienteBean> listaDiasProfissional;

	public GerenciarPacienteController() {
		gerenciarpaciente = new GerenciarPacienteBean();
		gerenciarpaciente.setPrograma(null);
		gerenciarpaciente.setStatus("T");
		listaPacientes = new ArrayList<GerenciarPacienteBean>();
		apenasLeitura = false;
		rowBean = new GerenciarPacienteBean();
		insercao = new InsercaoPacienteBean();
		tipo = "";
		listaLaudosVigentes = new ArrayList<InsercaoPacienteBean>();
		iDao = new InsercaoPacienteDAO();
		listaDiasProfissional = new ArrayList<GerenciarPacienteBean>();
	}

	public void buscarPacientesInstituicao() throws ProjetoException {
		busca = "S";
		carregarPacientesInstituicao();
		apenasLeitura = true;

	}

	public void limparBusca() throws ProjetoException {
		apenasLeitura = false;
	}

	public List<GrupoBean> listaGrupoAutoComplete(String query)
			throws ProjetoException {

		GrupoDAO gDao = new GrupoDAO();

		if (gerenciarpaciente.getPrograma().getIdPrograma() != null) {
			return gDao.listarGruposAutoComplete(query,
					this.gerenciarpaciente.getPrograma());
		} else {
			return null;
		}

	}

	public List<GrupoBean> listaGrupoLista() throws ProjetoException {

		GrupoDAO gDao = new GrupoDAO();
		if (gerenciarpaciente.getPrograma() != null) {
			if (gerenciarpaciente.getPrograma().getIdPrograma() != null) {
				return gDao.listarGruposPorPrograma(this.gerenciarpaciente
						.getPrograma().getIdPrograma());
			} else {
				return null;
			}
		} else {
			return null;
		}

	}

	public void inicioDesligar() {
		RequestContext.getCurrentInstance().execute("PF('dlgDeslPac').show();");
	}

	public void fecharDialogDesligamento() {
		RequestContext.getCurrentInstance().execute("PF('dlgDeslPac').hide();");
	}

	public void carregarPacientesInstituicao() throws ProjetoException {
		if (busca.equals("N")) {
			listaPacientes = gDao.carregarPacientesInstituicao();
		} else {
			listaPacientes = gDao
					.carregarPacientesInstituicaoBusca(gerenciarpaciente);
		}
	}

	public void onRowSelect(SelectEvent event) throws ProjetoException {
		// IMPLEMENTAR ALGO SE PRECISAR
		// System.out.println("rowbean: " + rowBean.getStatus());
	}

	public void desligarPaciente() throws ProjetoException, SQLException {

		Boolean cadastrou = false;

		cadastrou = gDao.desligarPaciente(rowBean, gerenciarpaciente);

		if (cadastrou == true) {

			RequestContext.getCurrentInstance().execute(
					"PF('dlgDeslPac').hide();");

			listaPacientes = gDao.carregarPacientesInstituicao();

			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Paciente desligado com sucesso!", "Sucesso");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		} else {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Ocorreu um erro durante o desligamento!", "Erro");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}

	}

	public String redirectRenovacao() {
		return "renovacaoPaciente?faces-redirect=true&amp;id="
				+ this.rowBean.getId();
	}

	public void carregaRenovacao() throws ProjetoException {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		Map<String, String> params = facesContext.getExternalContext()
				.getRequestParameterMap();
		if (params.get("id") != null) {
			Integer id = Integer.parseInt(params.get("id"));
			this.insercao = gDao.carregarPacientesInstituicaoRenovacao(id);
			if(insercao.getEquipe().getCodEquipe() != null && insercao.getEquipe().getCodEquipe() > 0){
				tipo = "E";
				listaDiasProfissional = gDao.listarDiasAtendimentoProfissional(id);
				System.out.println("Tamanho: "+listaDiasProfissional.size());
			}
			if(insercao.getFuncionario().getId() != null && insercao.getFuncionario().getId() > 0){
				tipo = "P";
			}
			// carregar as listas
		} else {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Ocorreu um erro!", "Erro");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}

	}
	
	
	public ArrayList<InsercaoPacienteBean> listarLaudosVigentes()
			throws ProjetoException {
		return iDao.listarLaudosVigentes();
	}
	
	public void carregarLaudoPaciente() throws ProjetoException {
		int id = insercao.getLaudo().getId();
		insercao = iDao.carregarLaudoPaciente(id);

	}

	public GerenciarPacienteBean getGerenciarpaciente() {
		return gerenciarpaciente;
	}

	public void setGerenciarpaciente(GerenciarPacienteBean gerenciarpaciente) {
		this.gerenciarpaciente = gerenciarpaciente;
	}

	public List<GerenciarPacienteBean> getListaPacientes() {
		return listaPacientes;
	}

	public void setListaPacientes(List<GerenciarPacienteBean> listaPacientes) {
		this.listaPacientes = listaPacientes;
	}

	public Boolean getApenasLeitura() {
		return apenasLeitura;
	}

	public void setApenasLeitura(Boolean apenasLeitura) {
		this.apenasLeitura = apenasLeitura;
	}

	public String getBusca() {
		return busca;
	}

	public void setBusca(String busca) {
		this.busca = busca;
	}

	public GerenciarPacienteBean getRowBean() {
		return rowBean;
	}

	public void setRowBean(GerenciarPacienteBean rowBean) {
		this.rowBean = rowBean;
	}

	public InsercaoPacienteBean getInsercao() {
		return insercao;
	}

	public void setInsercao(InsercaoPacienteBean insercao) {
		this.insercao = insercao;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public ArrayList<InsercaoPacienteBean> getListaLaudosVigentes() {
		return listaLaudosVigentes;
	}

	public void setListaLaudosVigentes(
			ArrayList<InsercaoPacienteBean> listaLaudosVigentes) {
		this.listaLaudosVigentes = listaLaudosVigentes;
	}

	public ArrayList<GerenciarPacienteBean> getListaDiasProfissional() {
		return listaDiasProfissional;
	}

	public void setListaDiasProfissional(
			ArrayList<GerenciarPacienteBean> listaDiasProfissional) {
		this.listaDiasProfissional = listaDiasProfissional;
	}

}
