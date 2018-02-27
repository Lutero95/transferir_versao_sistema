package br.gov.al.maceio.sishosp.hosp.control;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;

import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.hosp.dao.CboDAO;
import br.gov.al.maceio.sishosp.hosp.model.CboBean;

@ManagedBean(name="CboController")
@ViewScoped
public class CboController implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private CboBean cbo;
	private List<CboBean> listaCbo, listaCboFiltrada;
	private Integer tipoBuscar;
	private String descricaoBusca;
	private int tipo;
	private String cabecalho;

	private Integer abaAtiva = 0;

	private CboDAO cDao = new CboDAO();

	public CboController() {
		this.cbo = new CboBean();
		this.descricaoBusca = new String();
		
	}
	
	public String redirectEdit() {
		System.out.println("edit"+this.cbo.getCodCbo());
		System.out.println("tipo"+this.cbo.getCodCbo());
		return "cadastroCbo?faces-redirect=true&amp;id=" + this.cbo.getCodCbo()+"&amp;tipo="+tipo;
	}	
	
	
	public String redirectInsert() {
		return "cadastroCbo?faces-redirect=true&amp;tipo="+tipo;
	}		
	
	public void getEditCbo() throws ProjetoException {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		Map<String,String> params = facesContext.getExternalContext().getRequestParameterMap();
		System.out.println("vai ve se entrar no editar");
		if(params.get("id") != null) {
			System.out.println("entrou no editar");
			Integer id = Integer.parseInt(params.get("id"));
			tipo =Integer.parseInt(params.get("tipo"));			
			System.out.println("tipo do walter"+tipo);
			this.cbo = cDao.listarCboPorId(id);
		}
		else{
			System.out.println("tipo sera"+tipo);
			tipo =Integer.parseInt(params.get("tipo"));
			
		}
		
	}

	public void limparDados() {
		this.descricaoBusca = new String();
		
	}
	

	public String gravarCbo() throws ProjetoException, SQLException {
		boolean cadastrou = cDao.gravarCBO(this.cbo);

		if (cadastrou == true) {
			//limparDados();
			FacesMessage msg = new FacesMessage("CBO cadastrado com sucesso!");
			FacesContext.getCurrentInstance().addMessage("Error", msg);
			RequestContext.getCurrentInstance().update("msgCadAlt");
			return "gerenciarCbo?faces-redirect=true&amp;sucesso=CBO cadastrado com sucesso!";
		} else {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Ocorreu um erro durante o cadastro!", "Erro");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return "";
		}
	}

	public String alterarCbo() throws ProjetoException {
		boolean alterou = cDao.alterarCbo(cbo);
		if (alterou == true) {
			FacesMessage msg = new FacesMessage("CBO alterado com sucesso!");
			FacesContext.getCurrentInstance().addMessage("Error", msg);
			RequestContext.getCurrentInstance().update("msgCadAlt");
			return "gerenciarCbo?faces-redirect=true&amp;sucesso=CBO alterado com sucesso!";	
		} else {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Ocorreu um erro durante o cadastro!", "Erro");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return "";
		}
		

	}

	public void excluirCbo() throws ProjetoException {
		boolean ok = cDao.excluirCbo(cbo);
		if (ok == true) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Cbo excluido com sucesso!", "Sucesso");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			RequestContext.getCurrentInstance().execute(
					"PF('dialogAtencao').hide();");
			getListarCbo();			
		} else {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Ocorreu um erro durante a exclusao!", "Erro");
			FacesContext.getCurrentInstance().addMessage(null, msg);

			RequestContext.getCurrentInstance().execute(
					"PF('dialogAtencao').hide();");
		}
		listaCbo = cDao.listarCbo();
	}

	public CboBean getCbo() {
		return cbo;
	}

	public void setCbo(CboBean cbo) {
		this.cbo = cbo;
	}
	
	public void listarCbo() throws ProjetoException {
			listaCbo = cDao.listarCbo();
	
	}

	public List<CboBean> getListarCbo() throws ProjetoException {
		System.out.println("lst cbo");
			listaCbo = cDao.listarCbo();
	
		return listaCbo;
	}

	public void setListaCbo(List<CboBean> listaCbo) {
		this.listaCbo = listaCbo;
	}

	public void buscarCbo() throws ProjetoException {
		System.out.println("buscar cbo");
		this.listaCbo = cDao.listarCboBusca(descricaoBusca, tipoBuscar);
		System.out.println("tam"+listaCbo.size());
	}

	public Integer getTipoBuscar() {
		return tipoBuscar;
	}

	public void setTipoBuscar(Integer tipoBuscar) {
		this.tipoBuscar = tipoBuscar;
	}

	public String getDescricaoBusca() {
		return descricaoBusca;
	}

	public void setDescricaoBusca(String descricaoBusca) {
		this.descricaoBusca = descricaoBusca;
	}

	

	public Integer getAbaAtiva() {
		return abaAtiva;
	}

	public void setAbaAtiva(Integer abaAtiva) {
		this.abaAtiva = abaAtiva;
	}
	
	public String getCabecalho() {
		if(this.tipo==1){
			cabecalho = "Inclusão de CBO";
		}else if(this.tipo==2){
			cabecalho = "Alteração de CBO";
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

	public List<CboBean> getListaCbo() {
		return listaCbo;
	}

	public List<CboBean> getListaCboFiltrada() {
		return listaCboFiltrada;
	}

	public void setListaCboFiltrada(List<CboBean> listaCboFiltrada) {
		this.listaCboFiltrada = listaCboFiltrada;
	}

}
