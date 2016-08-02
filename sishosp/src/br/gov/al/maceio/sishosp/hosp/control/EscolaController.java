package br.gov.al.maceio.sishosp.hosp.control;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;

import br.gov.al.maceio.sishosp.acl.model.UsuarioBean;
import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.hosp.dao.EscolaDAO;
import br.gov.al.maceio.sishosp.hosp.model.EscolaBean;

@ManagedBean(name="EscolaController")
@ViewScoped
public class EscolaController implements Serializable{
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
		System.out.println("constur");

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
		return "cadastroEscola?faces-redirect=true&amp;id=" + this.escola.getCodEscola()+"&amp;tipo="+tipoesc;
	}	
	
	
	public String redirectInsert() {
		//System.out.println("tipo do redir "+tipoesc);
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
		.put("tipo", tipoesc);
		int tipoesc2=  (int) FacesContext
				.getCurrentInstance().getExternalContext().getSessionMap()
				.get("tipo");
		System.out.println("tipoesc2 "+tipoesc2);
		return "cadastroEscola?faces-redirect=true";
	}	
	
	
	public void getEditEscola() throws ProjetoException {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		Map<String,String> params = facesContext.getExternalContext().getRequestParameterMap();
		System.out.println("vai ve se entrar no editar");
		if(params.get("id") != null) {
			System.out.println("entrou no editar");
			Integer id = Integer.parseInt(params.get("id"));
			tipoesc =Integer.parseInt(params.get("tipo"));			
			
			EscolaDAO udao = new EscolaDAO();
			this.escola = udao.buscaescolacodigo(id);
		}
		else{
			
			tipoesc =Integer.parseInt(params.get("tipo"));
			
		}
		
	}

	
	
	
	
	public String gravarEscola() throws ProjetoException, SQLException {
		EscolaDAO udao = new EscolaDAO();
		boolean cadastrou = udao.cadastrar(escola);

		if (cadastrou == true) {
			//limparDados();
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"CBO cadastrado com sucesso!", "Sucesso");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return "gerenciarEscola?faces-redirect=true&amp;sucesso=CBO cadastrado com sucesso!";	
		} else {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Ocorreu um erro durante o cadastro!", "Erro");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return "";
		}
	}

	
	
	public String alterarEscola() throws ProjetoException {

		EscolaDAO rdao = new EscolaDAO();
		boolean alterou = rdao.alterar(escola);
		listaEscolas = null;
		if (alterou == true) {
			limparDados();
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Escola alterado com sucesso!", "Sucesso");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return "/pages/sishosp/gerenciarEscola.faces?faces-redirect=true";
			// RequestContext.getCurrentInstance().execute("dlgAltMenu.hide();");
		} else {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Ocorreu um erro durante o cadastro!", "Erro");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return "";
			// RequestContext.getCurrentInstance().execute("dlgAltMenu.hide();");
		}

	}

	public void excluirEscola() throws ProjetoException {
		EscolaDAO udao = new EscolaDAO();

		boolean excluio = udao.excluir(escola);

		if (excluio == true) {

			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Escola excluido com sucesso!", "Sucesso");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			listaEscolas = null;
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

	public void buscarEscolas() {

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

	public List<EscolaBean> getListaTipoEscola() {
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

	
	
	public void buscarEscola() {
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
		if (this.tipoesc==1) {
			cabecalho = "CADASTRO DE ESCOLA";
		} else if (this.tipoesc==2) {
			cabecalho = "ALTERAR ESCOLA";
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
