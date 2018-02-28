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
import br.gov.al.maceio.sishosp.hosp.dao.EquipeDAO;
import br.gov.al.maceio.sishosp.hosp.model.EquipeBean;

@ManagedBean(name = "EquipeController")
@ViewScoped
public class EquipeController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private EquipeBean equipe;
	private List<EquipeBean> listaEquipe;
	private Integer tipoBuscar;
	private String descricaoBusca;
	private int tipo;
	private Integer abaAtiva = 0;
	private String cabecalho;

	EquipeDAO eDao = new EquipeDAO();

	public EquipeController() {
		this.equipe = new EquipeBean();
		this.listaEquipe = null;
		this.descricaoBusca = new String();

		this.descricaoBusca = new String();
	}

	public void limparDados() throws ProjetoException {
		equipe = new EquipeBean();
		this.descricaoBusca = new String();
		this.tipoBuscar = 1;
		this.listaEquipe = eDao.listarEquipe();
	}

	public EquipeBean getEquipe() {
		return equipe;
	}

	public void setEquipe(EquipeBean equipe) {
		this.equipe = equipe;
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

	public String redirectEdit() {
		return "cadastroEquipe?faces-redirect=true&amp;id="
				+ this.equipe.getCodEquipe() + "&amp;tipo=" + tipo;
	}

	public String redirectInsert() {
		return "cadastroEquipe?faces-redirect=true&amp;tipo=" + tipo;
	}

	public void getEditEquipe() throws ProjetoException, SQLException {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		Map<String, String> params = facesContext.getExternalContext()
				.getRequestParameterMap();
		System.out.println("vai ve se entrar no editar");
		if (params.get("id") != null) {
			System.out.println("entrou no editar");
			Integer id = Integer.parseInt(params.get("id"));
			tipo = Integer.parseInt(params.get("tipo"));
			System.out.println("tipo do walter" + tipo);
			EquipeDAO cDao = new EquipeDAO();
			this.equipe = cDao.buscarEquipePorID(id);

		} else {
			System.out.println("tipo sera" + tipo);
			tipo = Integer.parseInt(params.get("tipo"));

		}

	}

	public void ListarTodasEquipes() throws ProjetoException {

		this.listaEquipe = eDao.listarEquipe();

	}

	public void gravarEquipe() throws ProjetoException, SQLException {
		if (this.equipe.getProfissionais().isEmpty()) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"É necessário pelo menos um profissional na equipe!",
					"Erro");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			// return "";
		}
		if (this.equipe.getDescEquipe().isEmpty()) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Descrição Obrigatória!", "Erro");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			// return "";
		}
		boolean cadastrou = eDao.gravarEquipe(this.equipe);

		if (cadastrou == true) {
			limparDados();
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Equipe cadastrada com sucesso!", "Sucesso");
			FacesContext.getCurrentInstance().addMessage(null, msg);

			RequestContext.getCurrentInstance().update("msgError");

			// return
			// "gerenciarEquipe?faces-redirect=true&amp;tipo="+tipo+"&amp;sucesso=CBO cadastrado com sucesso!";
		} else {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Ocorreu um erro durante o cadastro!", "Erro");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			// return "";
		}
	}

	public void buscarEquipes() throws ProjetoException {
		this.listaEquipe = eDao.listarEquipeBusca(descricaoBusca, tipoBuscar);
	}

	public void alterarEquipe() throws ProjetoException {
		if (this.equipe.getProfissionais().isEmpty()) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"É necessário peo menos um profissional na equipe!", "");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			// return "";
		}

		else {

			boolean alterou = eDao.alterarEquipe(equipe);

			if (alterou == true) {
				FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
						"Equipe alterada com sucesso!", "Sucesso");
				FacesContext.getCurrentInstance().addMessage(null, msg);
				RequestContext.getCurrentInstance().update("msgError");
				this.listaEquipe = eDao.listarEquipe();
				// return
				// "/pages/sishosp/gerenciarEquipe.faces?faces-redirect=true";
			} else {
				FacesMessage msg = new FacesMessage(
						FacesMessage.SEVERITY_ERROR,
						"Ocorreu um erro durante o cadastro!", "Erro");
				FacesContext.getCurrentInstance().addMessage(null, msg);
				// return "";
			}
		}
	}

	public void excluirEquipe() throws ProjetoException {
		boolean ok = eDao.excluirEquipe(equipe);
		if (ok == true) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Equipe excluida com sucesso!", "Sucesso");
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
		this.listaEquipe = eDao.listarEquipe();
	}

	public String getCabecalho() {
		if (this.tipo == 1) {
			cabecalho = "Inclusão de Equipe";
		} else if (this.tipo == 2) {
			cabecalho = "Alteração de Equipe";
		}
		return cabecalho;
	}

	public void setCabecalho(String cabecalho) {
		this.cabecalho = cabecalho;
	}

	public List<EquipeBean> listaEquipeAutoComplete(String query)
			throws ProjetoException {
		List<EquipeBean> result = eDao.listarEquipeBusca(query, 1);
		return result;
	}

	public void setListaEquipe(List<EquipeBean> listaEquipe) {
		this.listaEquipe = listaEquipe;
	}

	public int getTipo() {
		return tipo;
	}

	public void setTipo(int tipo) {
		this.tipo = tipo;
	}

	public List<EquipeBean> getListaEquipe() {
		return listaEquipe;
	}
}
