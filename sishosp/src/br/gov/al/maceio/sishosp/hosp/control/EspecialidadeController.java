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
import br.gov.al.maceio.sishosp.hosp.dao.EscolaridadeDAO;
import br.gov.al.maceio.sishosp.hosp.dao.EspecialidadeDAO;
import br.gov.al.maceio.sishosp.hosp.model.EspecialidadeBean;


@ManagedBean(name = "EspecialidadeController")
@ViewScoped
public class EspecialidadeController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private EspecialidadeBean espec;
	private List<EspecialidadeBean> listaEspecialidade;
	private Integer tipoBuscar;
	private String descricaoBusca;
	private int tipo;
	private Integer abaAtiva = 0;
	private String cabecalho;

	EspecialidadeDAO eDao = new EspecialidadeDAO();

	public EspecialidadeController() {
		this.espec = new EspecialidadeBean();
		this.listaEspecialidade = null;
		this.descricaoBusca = new String();
		this.descricaoBusca = new String();
	}

	public void limparDados() throws ProjetoException {
		espec = new EspecialidadeBean();
		this.descricaoBusca = new String();
		this.tipoBuscar = 1;
		this.listaEspecialidade = eDao.listarEspecialidades();
	}
	
	public String redirectEdit() {
		return "cadastroEspecialidade?faces-redirect=true&amp;id=" + this.espec.getCodEspecialidade()+"&amp;tipo="+tipo;
	}	
	
	
	public String redirectInsert() {
		//System.out.println("tipo do redir "+tipoesc);
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
		.put("tipo", tipo);
		int tipoesc2=  (int) FacesContext
				.getCurrentInstance().getExternalContext().getSessionMap()
				.get("tipo");
		
		return "cadastroEspecialidade?faces-redirect=true&amp;tipo="+tipo;
	}	
	
	
	public void getEditEspecialidade() throws ProjetoException {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		Map<String,String> params = facesContext.getExternalContext().getRequestParameterMap();
		System.out.println("vai ve se entrar no editar");
		if(params.get("id") != null) {
			System.out.println("entrou no editar");
			Integer id = Integer.parseInt(params.get("id"));
			tipo =Integer.parseInt(params.get("tipo"));			
			
			EspecialidadeDAO udao = new EspecialidadeDAO();
			this.espec = udao.listarEspecialidadePorId((id));
		}
		else{
			
			tipo =Integer.parseInt(params.get("tipo"));
			
		}
		
	}
		

	public EspecialidadeBean getEspec() {
		return espec;
	}

	public void setEspec(EspecialidadeBean espec) {
		this.espec = espec;
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

	public void getListarTodasEspecialidades() throws ProjetoException {
		
			this.listaEspecialidade = eDao.listarEspecialidades();
			System.out.println("listaEspecialidade .size "+listaEspecialidade .size());
		
		
	}

	public String gravarEspecialidade() throws ProjetoException, SQLException {

		boolean cadastrou = eDao.gravarEspecialidade(espec);

		if (cadastrou == true) {
			limparDados();
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Especialidade cadastrada com sucesso!", "Sucesso");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return "cadastroEspecialidade?faces-redirect=true&amp;tipo="+tipo+"&amp;sucesso=Especialidade cadastrada com sucesso!";
		} else {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Ocorreu um erro durante o cadastro!", "Erro");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return "";
		}
	}

	public void buscarEspecialidades() throws ProjetoException {
		this.listaEspecialidade = eDao.listarEspecialidadesBusca(
				descricaoBusca, tipoBuscar);
	}

	public String alterarEspecialidade() throws ProjetoException {
		boolean alterou = eDao.alterarEspecialidade(espec);
		if (alterou == true) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Especialidade alterada com sucesso!", "Sucesso");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			this.listaEspecialidade = eDao.listarEspecialidades();
			return "/pages/sishosp/gerenciarEspecialidade.faces?faces-redirect=true";
		} else {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Ocorreu um erro durante o cadastro!", "Erro");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			this.listaEspecialidade = eDao.listarEspecialidades();
			return "";
		}
	}

	public void excluirEspecialidade() throws ProjetoException {
		boolean ok = eDao.excluirEspecialidade(espec);
		if (ok == true) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Especialidade excluida com sucesso!", "Sucesso");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			RequestContext.getCurrentInstance().execute(
					"PF('dialogAtencao').hide();");
		} else {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Ocorreu um erro durante a exclusao!", "Erro");
			FacesContext.getCurrentInstance().addMessage(null, msg);

			RequestContext.getCurrentInstance().execute(
					"PF('dialogAtencao').hide();");
		}
		this.listaEspecialidade = eDao.listarEspecialidades();
	}

	public String getCabecalho() {
		if (this.tipo==1) {
			cabecalho = "CADASTRO DE ESPECIALIDADE";
		} else if (this.tipo==2) {
			cabecalho = "ALTERAR ESPECIALIDADE";
		}
		return cabecalho;
	}

	public void setCabecalho(String cabecalho) {
		this.cabecalho = cabecalho;
	}

	/**
	 * @return the tipo
	 */
	public int getTipo() {
		return tipo;
	}

	/**
	 * @param tipo the tipo to set
	 */
	public void setTipo(int tipo) {
		this.tipo = tipo;
	}

	public List<EspecialidadeBean> getListaEspecialidade() {
		return listaEspecialidade;
	}

	public void setListaEspecialidade(List<EspecialidadeBean> listaEspecialidade) {
		this.listaEspecialidade = listaEspecialidade;
	}
}
