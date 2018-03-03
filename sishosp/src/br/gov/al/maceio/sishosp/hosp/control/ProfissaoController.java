package br.gov.al.maceio.sishosp.hosp.control;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;

import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.hosp.dao.FormaTransporteDAO;
import br.gov.al.maceio.sishosp.hosp.dao.PacienteDAO;
import br.gov.al.maceio.sishosp.hosp.dao.ProfissaoDAO;
import br.gov.al.maceio.sishosp.hosp.model.PacienteBean;
import br.gov.al.maceio.sishosp.hosp.model.ProfissaoBean;

@ManagedBean(name = "ProfissaoController")
@ViewScoped
public class ProfissaoController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ProfissaoBean profissao;
	private Integer abaAtiva = 0;
	// BUSCAS
	private String cabecalho;
	private int tipo;
	private Integer tipoBuscaProfissao;
	private String campoBuscaProfissao;
	private String statusProfissao;

	private List<ProfissaoBean> listaProfissoes;

	public ProfissaoController() {

		profissao = new ProfissaoBean();

		listaProfissoes = new ArrayList<>();
		listaProfissoes = null;

		// BUSCA
		tipoBuscaProfissao = 1;
		campoBuscaProfissao = "";
		statusProfissao = "P";
	}

	public void gravarProfissao() throws ProjetoException {
		ProfissaoDAO udao = new ProfissaoDAO();
		boolean cadastrou = udao.cadastrar(profissao);
		listaProfissoes = null;
		if (cadastrou == true) {
			limparDados();
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Profissão cadastrada com sucesso!", "Sucesso");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			listaProfissoes = null;

		} else {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Ocorreu um erro durante o cadastro!", "Erro");
			FacesContext.getCurrentInstance().addMessage(null, msg);

		}

	}

	public void alterarProfissao() throws ProjetoException {

		ProfissaoDAO rdao = new ProfissaoDAO();
		boolean alterou = rdao.alterar(profissao);
		listaProfissoes = null;
		if (alterou == true) {
			// limparDados();
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Profissão alterada com sucesso!", "Sucesso");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			// return
			// "/pages/sishosp/gerenciarProfissoes.faces?faces-redirect=true";

			// RequestContext.getCurrentInstance().execute("dlgAltMenu.hide();");
		} else {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Ocorreu um erro durante o cadastro!", "Erro");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			// return "";
			// RequestContext.getCurrentInstance().execute("dlgAltMenu.hide();");
		}

	}

	public void excluirProfissao() throws ProjetoException {
		ProfissaoDAO udao = new ProfissaoDAO();

		boolean excluio = udao.excluir(profissao);

		if (excluio == true) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Profissao excluído com sucesso!", "Sucesso");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			listarProfissoes();
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

	public void buscarProfissoes() throws ProjetoException {

		List<ProfissaoBean> listaAux = null;
		listaProfissoes = new ArrayList<>();

		ProfissaoDAO adao = new ProfissaoDAO();

		listaAux = adao.buscarTipoProfissao(campoBuscaProfissao,
				tipoBuscaProfissao);

		if (listaAux != null && listaAux.size() > 0) {
			// listaAss = null;
			listaProfissoes = listaAux;
		} else {
			// listaAss = null;
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN,
					"Nenhuma Profissao encontrada.", "Aviso");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}

	}

	public void limparBuscaDados() {
		tipoBuscaProfissao = 1;
		campoBuscaProfissao = "";
		statusProfissao = "P";
		listaProfissoes = null;
	}

	public void limparDados() {
		profissao = new ProfissaoBean();

	}

	public ProfissaoBean getProfissao() {
		return profissao;
	}

	public void setProfissao(ProfissaoBean profissao) {
		this.profissao = profissao;
	}

	public void listarProfissoes() throws ProjetoException {
		ProfissaoDAO fdao = new ProfissaoDAO();
		listaProfissoes = fdao.listaProfissoes();

	}

	public void setListaProfissoes(List<ProfissaoBean> listaProfissoes) {
		this.listaProfissoes = listaProfissoes;
	}

	public int getTipo() {
		return tipo;
	}

	public void setTipo(int tipo) {
		this.tipo = tipo;
	}

	public Integer getTipoBuscaProfissao() {
		return tipoBuscaProfissao;
	}

	public void setTipoBuscaProfissao(Integer tipoBuscaProfissao) {
		this.tipoBuscaProfissao = tipoBuscaProfissao;
	}

	public String getCampoBuscaProfissao() {
		return campoBuscaProfissao;
	}

	public void setCampoBuscaProfissao(String campoBuscaProfissao) {
		this.campoBuscaProfissao = campoBuscaProfissao;
	}

	public String getStatusProfissao() {
		return statusProfissao;
	}

	public void setStatusProfissao(String statusProfissao) {
		this.statusProfissao = statusProfissao;
	}

	public Integer getAbaAtiva() {
		return abaAtiva;
	}

	public void setAbaAtiva(Integer abaAtiva) {
		this.abaAtiva = abaAtiva;
	}

	public String getCabecalho() {
		if (this.tipo == 1) {
			cabecalho = "Inclusão de Profissão";
		} else if (this.tipo == 2) {
			cabecalho = "Alteração de Profissão";
		}
		return cabecalho;
	}

	public void setCabecalho(String cabecalho) {
		this.cabecalho = cabecalho;
	}

	public void getEditProfissao() throws ProjetoException {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		Map<String, String> params = facesContext.getExternalContext()
				.getRequestParameterMap();
		if (params.get("id") != null) {
			ProfissaoDAO pDao = new ProfissaoDAO();
			Integer id = Integer.parseInt(params.get("id"));
			tipo = Integer.parseInt(params.get("tipo"));
			this.profissao = pDao.buscaprofissaocodigo(id);
		} else {
			tipo = Integer.parseInt(params.get("tipo"));

		}

	}

	public String redirectInsert() {
		return "cadastroProfissoes?faces-redirect=true&amp;tipo=" + this.tipo;
	}

	public String redirectEdit() {
		return "cadastroProfissoes?faces-redirect=true&amp;id="
				+ this.profissao.getCodprofissao() + "&amp;tipo=" + this.tipo;
	}

	public List<ProfissaoBean> getListaProfissoes() {
		return listaProfissoes;
	}
}
