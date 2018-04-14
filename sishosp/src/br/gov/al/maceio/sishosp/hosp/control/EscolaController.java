package br.gov.al.maceio.sishosp.hosp.control;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;

import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.hosp.dao.EscolaDAO;
import br.gov.al.maceio.sishosp.hosp.model.EscolaBean;

@ManagedBean(name = "EscolaController")
@ViewScoped
public class EscolaController implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer abaAtiva = 0;
	private EscolaBean escola;

	// LISTAS
	private List<EscolaBean> listaTipoEscola;
	private List<EscolaBean> listaEscolas;

	// BUSCAS
	private int tipoesc;

	private Integer tipoBuscaEscola;
	private String campoBuscaEscola;
	private String statusEscola;
	private String cabecalho;

	public EscolaController() {

		escola = new EscolaBean();

		// BUSCA

		tipoBuscaEscola = 1;
		campoBuscaEscola = "";
		statusEscola = "P";

		listaEscolas = new ArrayList<>();
		listaEscolas = null;
		listaTipoEscola = new ArrayList<>();
		listaTipoEscola = null;
	}

	public String redirectEdit() {
		return "cadastroEscola?faces-redirect=true&amp;id="
				+ this.escola.getCodEscola() + "&amp;tipo=" + tipoesc;
	}

	public String redirectInsert() {
		return "cadastroEscola?faces-redirect=true&amp;tipo=" + tipoesc;
	}

	public void getEditEscola() throws ProjetoException {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		Map<String, String> params = facesContext.getExternalContext()
				.getRequestParameterMap();
		if (params.get("id") != null) {
			Integer id = Integer.parseInt(params.get("id"));
			tipoesc = Integer.parseInt(params.get("tipo"));

			EscolaDAO udao = new EscolaDAO();
			this.escola = udao.buscaescolacodigo(id);
		} else {

			tipoesc = Integer.parseInt(params.get("tipo"));

		}

	}

	public void gravarEscola() throws ProjetoException, SQLException {
		EscolaDAO udao = new EscolaDAO();
		boolean cadastrou = udao.cadastrar(escola);

		if (cadastrou == true) {
			limparDados();
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Escola cadastrada com sucesso!", "Sucesso");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			// return
			// "gerenciarEscola?faces-redirect=true&amp;tipo="+tipoesc+"&amp;sucesso=Escola cadastrada com sucesso!";
		} else {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Ocorreu um erro durante o cadastro!", "Erro");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			// return "";
		}
	}

	public void alterarEscola() throws ProjetoException {

		EscolaDAO rdao = new EscolaDAO();
		boolean alterou = rdao.alterar(escola);
		listaEscolas = null;
		if (alterou == true) {
			// limparDados();
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Escola alterada com sucesso!", "Sucesso");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			// return
			// "/pages/sishosp/gerenciarEscola.faces?faces-redirect=true";
			// RequestContext.getCurrentInstance().execute("dlgAltMenu.hide();");
		} else {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Ocorreu um erro durante o cadastro!", "Erro");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			// return "";
			// RequestContext.getCurrentInstance().execute("dlgAltMenu.hide();");
		}

	}

	public void excluirEscola() throws ProjetoException {
		EscolaDAO udao = new EscolaDAO();

		boolean excluio = udao.excluir(escola);

		if (excluio == true) {

			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Escola excluída com sucesso!", "Sucesso");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			buscarEscola();
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

	public void gravarTipoEscola() throws ProjetoException {
		EscolaDAO udao = new EscolaDAO();

		boolean cadastrou = udao.cadastrarTipoEscola(escola);

		if (cadastrou == true) {
			limparDados();
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Tipo Escola cadastrado com sucesso!", "Sucesso");
			FacesContext.getCurrentInstance().addMessage(null, msg);

		} else {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Ocorreu um erro durante o cadastro!", "Erro");
			FacesContext.getCurrentInstance().addMessage(null, msg);

		}

	}

	public void alterarTipoEscola() throws ProjetoException {

		EscolaDAO rdao = new EscolaDAO();
		boolean alterou = rdao.alterarTipoEscola(escola);

		if (alterou == true) {

			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Tipo Escola alterado com sucesso!", "Sucesso");
			FacesContext.getCurrentInstance().addMessage(null, msg);

			// RequestContext.getCurrentInstance().execute("dlgAltMenu.hide();");
		} else {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Ocorreu um erro durante o cadastro!", "Erro");
			FacesContext.getCurrentInstance().addMessage(null, msg);

			// RequestContext.getCurrentInstance().execute("dlgAltMenu.hide();");
		}

	}

	public void excluirTipoEscola() throws ProjetoException {
		EscolaDAO udao = new EscolaDAO();

		boolean excluio = udao.excluirTipoEscola(escola);

		if (excluio == true) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Tipo Escola excluido com sucesso!", "Sucesso");
			FacesContext.getCurrentInstance().addMessage(null, msg);

			// RequestContext.getCurrentInstance().execute("PF('dialogAtencao').hide();");
		} else {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Ocorreu um erro durante a exclusao!", "Erro");
			FacesContext.getCurrentInstance().addMessage(null, msg);

			// RequestContext.getCurrentInstance().execute("PF('dialogAtencao').hide();");
		}
	}

	public void buscarEscolas() throws ProjetoException {

		List<EscolaBean> listaAux = null;
		listaEscolas = new ArrayList<>();

		EscolaDAO adao = new EscolaDAO();

		listaAux = adao.buscarTipoEscola(campoBuscaEscola, tipoBuscaEscola);

		if (listaAux != null && listaAux.size() > 0) {
			// listaAss = null;
			listaEscolas = listaAux;
		} else {
			// listaAss = null;
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN,
					"Nenhuma Escola encontrada.", "Aviso");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}

	}

	public void limparBuscaDados() {
		tipoBuscaEscola = 1;
		campoBuscaEscola = "";
		statusEscola = "P";
		listaEscolas = null;
	}

	public void limparDados() {
		escola = new EscolaBean();

	}

	public EscolaBean getEscola() {
		return escola;
	}

	public void setEscola(EscolaBean escola) {
		this.escola = escola;
	}

	public List<EscolaBean> getListaTipoEscola() throws ProjetoException {
		if (listaTipoEscola == null) {

			EscolaDAO fdao = new EscolaDAO();
			listaTipoEscola = fdao.listaTipoEscola();
		}
		return listaTipoEscola;
	}

	public void setListaTipoEscola(List<EscolaBean> listaTipoEscola) {
		this.listaTipoEscola = listaTipoEscola;
	}

	public Integer getTipoBuscaEscola() {
		return tipoBuscaEscola;
	}

	public void setTipoBuscaEscola(Integer tipoBuscaEscola) {
		this.tipoBuscaEscola = tipoBuscaEscola;
	}

	public String getCampoBuscaEscola() {
		return campoBuscaEscola;
	}

	public void setCampoBuscaEscola(String campoBuscaEscola) {
		this.campoBuscaEscola = campoBuscaEscola;
	}

	public String getStatusEscola() {
		return statusEscola;
	}

	public void setStatusEscola(String statusEscola) {
		this.statusEscola = statusEscola;
	}

	public void buscarEscola() throws ProjetoException {
		EscolaDAO fdao = new EscolaDAO();
		listaEscolas = fdao.listaEscolas();
	}

	public void setListaEscolas(List<EscolaBean> listaEscolas) {
		this.listaEscolas = listaEscolas;
	}

	public Integer getAbaAtiva() {
		return abaAtiva;
	}

	public void setAbaAtiva(Integer abaAtiva) {
		this.abaAtiva = abaAtiva;
	}

	public String getCabecalho() {
		if (this.tipoesc == 1) {
			cabecalho = "Inclusão de Escola";
		} else if (this.tipoesc == 2) {
			cabecalho = "Alteração de Escola";
		}
		return cabecalho;
	}

	public void setCabecalho(String cabecalho) {
		this.cabecalho = cabecalho;
	}

	public List<EscolaBean> getListaEscolas() {
		return listaEscolas;
	}

	public int getTipoesc() {
		return tipoesc;
	}

	public void setTipoesc(int tipoesc) {
		this.tipoesc = tipoesc;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
