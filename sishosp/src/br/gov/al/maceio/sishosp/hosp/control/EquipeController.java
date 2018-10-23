package br.gov.al.maceio.sishosp.hosp.control;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import br.gov.al.maceio.sishosp.comum.util.JSFUtil;
import br.gov.al.maceio.sishosp.comum.util.RedirecionarUtil;

import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.hosp.dao.EquipeDAO;
import br.gov.al.maceio.sishosp.hosp.model.EquipeBean;

@ManagedBean(name = "EquipeController")
@ViewScoped
public class EquipeController implements Serializable {

	private static final long serialVersionUID = 1L;
	private EquipeBean equipe;
	private List<EquipeBean> listaEquipe;
	private int tipo;
	private String cabecalho;
	private EquipeDAO eDao = new EquipeDAO();

	//CONSTANTES
	private static final String ENDERECO_CADASTRO = "cadastroEquipe?faces-redirect=true";
	private static final String ENDERECO_TIPO = "&amp;tipo=";
	private static final String ENDERECO_ID = "&amp;id=";
	private static final String CABECALHO_INCLUSAO = "Inclusão de Equipe";
	private static final String CABECALHO_ALTERACAO = "Alteração de Equipe";

	public EquipeController() {
		this.equipe = new EquipeBean();
		this.listaEquipe = null;
	}

	public void limparDados() throws ProjetoException {
		equipe = new EquipeBean();
		this.listaEquipe = eDao.listarEquipe();
	}

	public String redirectEdit() {
		return RedirecionarUtil.redirectEdit(ENDERECO_CADASTRO, ENDERECO_ID, this.equipe.getCodEquipe(), ENDERECO_TIPO, tipo);
	}

	public String redirectInsert() {
		return RedirecionarUtil.redirectInsert(ENDERECO_CADASTRO, ENDERECO_TIPO, tipo);
	}

	public void getEditEquipe() throws ProjetoException {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		Map<String, String> params = facesContext.getExternalContext()
				.getRequestParameterMap();
		if (params.get("id") != null) {
			Integer id = Integer.parseInt(params.get("id"));
			tipo = Integer.parseInt(params.get("tipo"));
			EquipeDAO cDao = new EquipeDAO();
			this.equipe = cDao.buscarEquipePorID(id);

		} else {
			tipo = Integer.parseInt(params.get("tipo"));

		}
	}

	public void ListarTodasEquipes() throws ProjetoException {
		this.listaEquipe = eDao.listarEquipe();
	}

	public List<EquipeBean> listarEquipes() throws ProjetoException {
		this.listaEquipe = eDao.listarEquipe();

		return listaEquipe;
	}

	public void gravarEquipe() throws ProjetoException {
		if (this.equipe.getProfissionais().isEmpty()) {
			JSFUtil.adicionarMensagemAdvertencia("É necessário ao menos um profissional na equipe!", "Advertência");
		}

		else {
			boolean cadastrou = eDao.gravarEquipe(this.equipe);

			if (cadastrou == true) {
				limparDados();
				JSFUtil.adicionarMensagemSucesso("Equipe cadastrada com sucesso!", "Sucesso");
				JSFUtil.atualizarComponente("msgPagina");
			} else {
				JSFUtil.adicionarMensagemErro("Ocorreu um erro durante o cadastro!", "Erro");
			}
		}
	}

	public void alterarEquipe() {

		if (this.equipe.getProfissionais().isEmpty()) {
			JSFUtil.adicionarMensagemAdvertencia("É necessário ao menos um profissional na equipe!", "Advertência");
		}

		else {

			boolean alterou = eDao.alterarEquipe(equipe);

			if (alterou == true) {
				JSFUtil.adicionarMensagemSucesso("Equipe alterada com sucesso!", "Sucesso");
				JSFUtil.atualizarComponente("msgPagina");
			} else {
				JSFUtil.adicionarMensagemErro("Ocorreu um erro durante a alteração!", "Erro");
			}
		}
	}

	public void excluirEquipe() throws ProjetoException {

		boolean excluiu = eDao.excluirEquipe(equipe);

		if (excluiu == true) {
			JSFUtil.adicionarMensagemSucesso("Equipe excluída com sucesso!", "Sucesso");
			JSFUtil.fecharDialog("dialogExclusao");
		} else {
			JSFUtil.adicionarMensagemErro("Ocorreu um erro durante a alteração!", "Erro");
			JSFUtil.fecharDialog("dialogExclusao");
		}
		listarEquipes();
	}

	public List<EquipeBean> listaEquipeAutoComplete(String query)
			throws ProjetoException {
		List<EquipeBean> result = eDao.listarEquipeBusca(query);
		return result;
	}

	public String getCabecalho() {
		if (this.tipo == 1) {
			cabecalho = CABECALHO_INCLUSAO;
		} else if (this.tipo == 2) {
			cabecalho = CABECALHO_ALTERACAO;
		}
		return cabecalho;
	}

	public void setCabecalho(String cabecalho) {
		this.cabecalho = cabecalho;
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

	public EquipeBean getEquipe() {
		return equipe;
	}

	public void setEquipe(EquipeBean equipe) {
		this.equipe = equipe;
	}

}
