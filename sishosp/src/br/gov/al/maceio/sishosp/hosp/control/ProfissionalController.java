package br.gov.al.maceio.sishosp.hosp.control;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;

import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.hosp.dao.ProfissionalDAO;
import br.gov.al.maceio.sishosp.hosp.model.ProfissionalBean;

@ManagedBean(name = "ProfissionalController")
@ViewScoped
public class ProfissionalController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private ProfissionalBean profissional;

	private List<ProfissionalBean> listaProfissional;

	private ProfissionalDAO pDao = new ProfissionalDAO();
	private Integer tipoBuscar;
	private String descricaoBusca;
	private String cabecalho;
	private String tipo;
	private Integer abaAtiva = 0;

	public ProfissionalController() {
		this.profissional = new ProfissionalBean();
		this.descricaoBusca = new String();
		this.tipo = new String();
	}

	public void limparDados() {
		this.profissional = new ProfissionalBean();
		this.descricaoBusca = new String();
		this.listaProfissional = pDao.listarProfissional();
	}

	public ProfissionalBean getProfissional() {
		return profissional;
	}

	public void setProfissional(ProfissionalBean profissional) {
		this.profissional = profissional;
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

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public Integer getAbaAtiva() {
		return abaAtiva;
	}

	public void setAbaAtiva(Integer abaAtiva) {
		this.abaAtiva = abaAtiva;
	}

	public List<ProfissionalBean> getListaProfissional() {
		if (listaProfissional == null) {
			listaProfissional = pDao.listarProfissional();
		}
		return listaProfissional;
	}

	public void setListaProfissional(List<ProfissionalBean> listaProfissional) {
		this.listaProfissional = listaProfissional;
	}

	public void gravarProfissional() throws SQLException {
		if (this.profissional.getCbo().getCodCbo() == null
				|| this.profissional.getCns().isEmpty()
				|| this.profissional.getDescricaoProf().isEmpty()
				|| this.profissional.getEspecialidade().getCodEspecialidade() == null) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"CBO, CNS, especialidade e descrição obrigatórios!",
					"Campos obrigatórios!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return;
		}

		if (this.profissional.getPrograma().isEmpty()
				|| this.profissional.getGrupo().isEmpty()) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Programa e Grupo obrigatórios!", "Campos obrigatórios!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return;
		}

		boolean cadastrou = pDao.gravarProfissional(profissional);

		if (cadastrou == true) {
			limparDados();
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Profissional cadastrado com sucesso!", "Sucesso");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		} else {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Ocorreu um erro durante o cadastro!", "Erro");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
		this.listaProfissional = pDao.listarProfissional();
	}

	public void excluirProfissional() throws ProjetoException {
		boolean ok = pDao.excluirProfissional(profissional);
		if (ok == true) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Profissional excluido com sucesso!", "Sucesso");
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
		this.listaProfissional = pDao.listarProfissional();
	}

	public String alterarProfissional() throws ProjetoException {
		if (this.profissional.getCbo().getCodCbo() == null
				|| this.profissional.getCns().isEmpty()
				|| this.profissional.getDescricaoProf().isEmpty()
				|| this.profissional.getEspecialidade().getCodEspecialidade() == null) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"CBO, CNS, especialidade e descrição obrigatórios!",
					"Campos obrigatórios!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return "";
		}

		if (this.profissional.getPrograma().isEmpty()
				|| this.profissional.getGrupo().isEmpty()) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Programa e Grupo obrigatórios!", "Campos obrigatórios!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return "";
		}

		boolean alterou = pDao.alterarProfissional(profissional);

		if (alterou == true) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Profissional alterado com sucesso!", "Sucesso");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			this.listaProfissional = pDao.listarProfissional();
			return "/pages/sishosp/gerenciarProfissional.faces?faces-redirect=true";
		} else {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Ocorreu um erro durante o cadastro!", "Erro");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return "";
		}

	}

	public void buscarProfissional() {
		if (this.tipoBuscar == 0) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Escolha uma opção de busca válida!", "Erro");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		} else {
			this.listaProfissional = pDao.listarProfissionalBusca(
					descricaoBusca, tipoBuscar);
		}
	}

	public String getCabecalho() {
		if (this.tipo.equals("I")) {
			cabecalho = "CADASTRO DE PROFISSIONAL";
		} else if (this.tipo.equals("A")) {
			cabecalho = "ALTERAR PROFISSIONAL";
		}
		return cabecalho;
	}

	public void setCabecalho(String cabecalho) {
		this.cabecalho = cabecalho;
	}

	public List<ProfissionalBean> listaProfissionalAutoComplete(String query)
			throws ProjetoException {
		List<ProfissionalBean> result = pDao.listarProfissionalBusca(query, 1);
		return result;
	}
}
