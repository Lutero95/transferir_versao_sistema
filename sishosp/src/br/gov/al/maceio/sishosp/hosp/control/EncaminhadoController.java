package br.gov.al.maceio.sishosp.hosp.control;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;

import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.hosp.dao.EncaminhadoDAO;
import br.gov.al.maceio.sishosp.hosp.model.EncaminhadoBean;

public class EncaminhadoController implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer abaAtiva = 0;
	private EncaminhadoBean encaminhado;
	// BUSCAS
	private String tipo;
	private Integer tipoBuscaEncaminhado;
	private String campoBuscaEncaminhado;
	private String statusEncaminhado;
	private String cabecalho;

	private List<EncaminhadoBean> listaEncaminhado;

	public EncaminhadoController() {
		encaminhado = new EncaminhadoBean();
		// BUSCA
		tipo = "";
		tipoBuscaEncaminhado = 1;
		campoBuscaEncaminhado = "";
		statusEncaminhado = "P";

		listaEncaminhado = new ArrayList<>();
		listaEncaminhado = null;
	}

	public void gravarEncaminhado() throws ProjetoException {
		EncaminhadoDAO udao = new EncaminhadoDAO();
		boolean cadastrou = udao.cadastrar(encaminhado);

		if (cadastrou == true) {
			limparDados();
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Encaminhado cadastrado com sucesso!", "Sucesso");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			listaEncaminhado = null;

		} else {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Ocorreu um erro durante o cadastro!", "Erro");
			FacesContext.getCurrentInstance().addMessage(null, msg);

		}

	}

	public String alterarEncaminhado() throws ProjetoException {

		EncaminhadoDAO rdao = new EncaminhadoDAO();
		boolean alterou = rdao.alterar(encaminhado);

		if (alterou == true) {
			limparDados();
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Encaminhado alterado com sucesso!", "Sucesso");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return "/pages/sishosp/gerenciarTipoEncaminhamento.faces?faces-redirect=true";
			// RequestContext.getCurrentInstance().execute("dlgAltMenu.hide();");
		} else {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Ocorreu um erro durante o cadastro!", "Erro");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return "";
			// RequestContext.getCurrentInstance().execute("dlgAltMenu.hide();");
		}

	}

	public void excluirEncaminhado() throws ProjetoException {
		EncaminhadoDAO udao = new EncaminhadoDAO();

		boolean excluio = udao.excluir(encaminhado);

		if (excluio == true) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Encaminhado excluido com sucesso!", "Sucesso");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			listaEncaminhado = null;
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

	public void buscarEncaminhados() {

		List<EncaminhadoBean> listaAux = null;
		listaEncaminhado = new ArrayList<>();

		EncaminhadoDAO adao = new EncaminhadoDAO();

		listaAux = adao.buscarTipoEncaminhado(campoBuscaEncaminhado,
				tipoBuscaEncaminhado);

		if (listaAux != null && listaAux.size() > 0) {
			// listaAss = null;
			listaEncaminhado = listaAux;
		} else {
			// listaAss = null;
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN,
					"Nenhum Encaminhado encontrado.", "Aviso");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}

	}

	public void limparBuscaDados() {
		tipoBuscaEncaminhado = 1;
		campoBuscaEncaminhado = "";
		statusEncaminhado = "P";
		listaEncaminhado = null;
	}

	public void limparDados() {

		encaminhado = new EncaminhadoBean();
	}

	public EncaminhadoBean getEncaminhado() {
		return encaminhado;
	}

	public void setEncaminhado(EncaminhadoBean encaminhado) {
		this.encaminhado = encaminhado;
	}

	public Integer getAbaAtiva() {
		return abaAtiva;
	}

	public void setAbaAtiva(Integer abaAtiva) {
		this.abaAtiva = abaAtiva;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public Integer getTipoBuscaEncaminhado() {
		return tipoBuscaEncaminhado;
	}

	public void setTipoBuscaEncaminhado(Integer tipoBuscaEncaminhado) {
		this.tipoBuscaEncaminhado = tipoBuscaEncaminhado;
	}

	public String getCampoBuscaEncaminhado() {
		return campoBuscaEncaminhado;
	}

	public void setCampoBuscaEncaminhado(String campoBuscaEncaminhado) {
		this.campoBuscaEncaminhado = campoBuscaEncaminhado;
	}

	public String getStatusEncaminhado() {
		return statusEncaminhado;
	}

	public void setStatusEncaminhado(String statusEncaminhado) {
		this.statusEncaminhado = statusEncaminhado;
	}

	public List<EncaminhadoBean> getListaEncaminhado() {
		if (listaEncaminhado == null) {

			EncaminhadoDAO fdao = new EncaminhadoDAO();
			listaEncaminhado = fdao.listaEncaminhados();

		}
		return listaEncaminhado;
	}

	public void setListaEncaminhado(List<EncaminhadoBean> listaEncaminhado) {
		this.listaEncaminhado = listaEncaminhado;
	}

	public String getCabecalho() {
		if (this.tipo.equals("I")) {
			cabecalho = "CADASTRO DE ENCAMINHADO";
		} else if (this.tipo.equals("A")) {
			cabecalho = "ALTERAR ENCAMINHADO";
		}
		return cabecalho;
	}

	public void setCabecalho(String cabecalho) {
		this.cabecalho = cabecalho;
	}

}
