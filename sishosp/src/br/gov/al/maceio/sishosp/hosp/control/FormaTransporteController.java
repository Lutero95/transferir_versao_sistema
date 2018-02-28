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

import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.hosp.dao.EspecialidadeDAO;
import br.gov.al.maceio.sishosp.hosp.dao.FormaTransporteDAO;
import br.gov.al.maceio.sishosp.hosp.model.FormaTransporteBean;

@ManagedBean(name = "FormaTransporteController")
@ViewScoped
public class FormaTransporteController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer abaAtiva = 0;
	private FormaTransporteBean transporte;
	private String cabecalho;

	// BUSCAS
	private int tipo;
	private Integer tipoBuscaTransporte;
	private String campoBuscaTransporte;
	private String statusTransporte;

	private List<FormaTransporteBean> listaTransporte;

	public FormaTransporteController() {
		transporte = new FormaTransporteBean();

		// BUSCA
		tipoBuscaTransporte = 1;
		campoBuscaTransporte = "";
		statusTransporte = "P";

		listaTransporte = new ArrayList<>();
		listaTransporte = null;

	}

	public String redirectEdit() {
		return "cadastroFormaTransporte?faces-redirect=true&amp;id="
				+ this.transporte.getCodformatransporte() + "&amp;tipo=" + tipo;
	}

	public String redirectInsert() {
		return "cadastroFormaTransporte?faces-redirect=true&amp;tipo=" + tipo;
	}

	public void getEditFormaTransporte() throws ProjetoException {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		Map<String, String> params = facesContext.getExternalContext()
				.getRequestParameterMap();
		System.out.println("vai ve se entrar no editar");
		if (params.get("id") != null) {
			System.out.println("entrou no editar");
			Integer id = Integer.parseInt(params.get("id"));
			tipo = Integer.parseInt(params.get("tipo"));

			FormaTransporteDAO udao = new FormaTransporteDAO();
			this.transporte = udao.buscatransportecodigo(id);
		} else {

			tipo = Integer.parseInt(params.get("tipo"));

		}

	}

	public void gravarTransporte() throws ProjetoException {
		FormaTransporteDAO udao = new FormaTransporteDAO();
		boolean cadastrou = udao.cadastrar(transporte);

		if (cadastrou == true) {
			limparDados();
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Transporte cadastrado com sucesso!", "Sucesso");
			FacesContext.getCurrentInstance().addMessage(null, msg);

			listaTransporte = null;

		} else {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Ocorreu um erro durante o cadastro!", "Erro");
			FacesContext.getCurrentInstance().addMessage(null, msg);

		}

	}

	public void alterarTransporte() throws ProjetoException {

		FormaTransporteDAO rdao = new FormaTransporteDAO();
		boolean alterou = rdao.alterar(transporte);

		if (alterou == true) {
			//limparDados();
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Transporte alterado com sucesso!", "Sucesso");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			//return "/pages/sishosp/gerenciarFormaTransporte.faces?faces-redirect=true";

			// RequestContext.getCurrentInstance().execute("dlgAltMenu.hide();");
		} else {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Ocorreu um erro durante o cadastro!", "Erro");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			//return "";
			// RequestContext.getCurrentInstance().execute("dlgAltMenu.hide();");
		}

	}

	public void excluirTransporte() throws ProjetoException {
		FormaTransporteDAO udao = new FormaTransporteDAO();

		boolean excluio = udao.excluir(transporte);

		if (excluio == true) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Transporte excluído com sucesso!", "Sucesso");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			
			RequestContext.getCurrentInstance().execute(
					"PF('dialogAtencao').hide();");
			listarTransporte();
		} else {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Ocorreu um erro durante a exclusao!", "Erro");
			FacesContext.getCurrentInstance().addMessage(null, msg);

			RequestContext.getCurrentInstance().execute(
					"PF('dialogAtencao').hide();");
		}
	}

	public void buscarTransportes() throws ProjetoException {

		List<FormaTransporteBean> listaAux = null;
		listaTransporte = new ArrayList<>();

		FormaTransporteDAO adao = new FormaTransporteDAO();

		listaAux = adao.buscarTipoTransporte(campoBuscaTransporte,
				tipoBuscaTransporte);

		if (listaAux != null && listaAux.size() > 0) {
			// listaAss = null;
			listaTransporte = listaAux;
		} else {
			// listaAss = null;
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN,
					"Nenhum Paciente encontrado.", "Aviso");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}

	}

	public void limparBuscaDados() {
		tipoBuscaTransporte = 1;
		campoBuscaTransporte = "";
		statusTransporte = "P";
		listaTransporte = null;
	}

	public void limparDados() {
		transporte = new FormaTransporteBean();

	}

	public FormaTransporteBean getTransporte() {
		return transporte;
	}

	public void setTransporte(FormaTransporteBean transporte) {
		this.transporte = transporte;
	}

	public Integer getAbaAtiva() {
		return abaAtiva;
	}

	public void setAbaAtiva(Integer abaAtiva) {
		this.abaAtiva = abaAtiva;
	}

	public Integer getTipoBuscaTransporte() {
		return tipoBuscaTransporte;
	}

	public void setTipoBuscaTransporte(Integer tipoBuscaTransporte) {
		this.tipoBuscaTransporte = tipoBuscaTransporte;
	}

	public String getCampoBuscaTransporte() {
		return campoBuscaTransporte;
	}

	public void setCampoBuscaTransporte(String campoBuscaTransporte) {
		this.campoBuscaTransporte = campoBuscaTransporte;
	}

	public String getStatusTransporte() {
		return statusTransporte;
	}

	public void setStatusTransporte(String statusTransporte) {
		this.statusTransporte = statusTransporte;
	}

	public void listarTransporte() throws ProjetoException {

		FormaTransporteDAO fdao = new FormaTransporteDAO();
		listaTransporte = fdao.listaTransportes();

	}

	public void setListaTransporte(List<FormaTransporteBean> listaTransporte) {
		this.listaTransporte = listaTransporte;
	}

	public String getCabecalho() {
		if (this.tipo == 1) {
			cabecalho = "Inclusão de Forma de Transporte";
		} else if (this.tipo == 2) {
			cabecalho = "Alteração de Forma de Transporte";
		}
		return cabecalho;
	}

	public void setCabecalho(String cabecalho) {
		this.cabecalho = cabecalho;
	}

	public int getTipo() {
		return tipo;
	}

	public void setTipo(int tipo) {
		this.tipo = tipo;
	}

	public List<FormaTransporteBean> getListaTransporte() {
		return listaTransporte;
	}

}
