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
import org.primefaces.event.SelectEvent;

import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.hosp.dao.ProfissionalDAO;
import br.gov.al.maceio.sishosp.hosp.model.GrupoBean;
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
	private int tipo;
	private Integer abaAtiva = 0;

	private GrupoBean grupoSelecionado;

	public ProfissionalController() {
		this.profissional = new ProfissionalBean();
		this.descricaoBusca = new String();
	}

	public void limparDados() throws ProjetoException {
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

	public int getTipo() {
		return tipo;
	}

	public void setTipo(int tipo) {
		this.tipo = tipo;
	}

	public Integer getAbaAtiva() {
		return abaAtiva;
	}

	public void setAbaAtiva(Integer abaAtiva) {
		this.abaAtiva = abaAtiva;
	}

	public void ListarTodosProfissionais()
			throws ProjetoException {
		listaProfissional = pDao.listarProfissional();
		
	}

	public void setListaProfissional(List<ProfissionalBean> listaProfissional) {
		this.listaProfissional = listaProfissional;
	}

	public void gravarProfissional() throws SQLException, ProjetoException {
		if (this.profissional.getCbo().getCodCbo() == null
				|| this.profissional.getCns().isEmpty()
				|| this.profissional.getDescricaoProf().isEmpty()
				|| this.profissional.getEspecialidade().getCodEspecialidade() == null) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"CBO, CNS, especialidade e descri��o obrigat�rios!",
					"Campos obrigat�rios!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return;
		}

		if (this.profissional.getPrograma().isEmpty()
				|| this.profissional.getGrupo().isEmpty()) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Programa e Grupo obrigat�rios!", "Campos obrigat�rios!");
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
					"CBO, CNS, especialidade e descri��o obrigat�rios!",
					"Campos obrigat�rios!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return "";
		}

		if (this.profissional.getPrograma().isEmpty()
				|| this.profissional.getGrupo().isEmpty()) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Programa e Grupo obrigat�rios!", "Campos obrigat�rios!");
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

	public void buscarProfissional() throws ProjetoException {
		if (this.tipoBuscar == 0) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Escolha uma op��o de busca v�lida!", "Erro");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		} else {
			this.listaProfissional = pDao.listarProfissionalBusca(
					descricaoBusca, tipoBuscar);
		}
	}

	public String getCabecalho() {
		if (this.tipo == 1) {
			cabecalho = "CADASTRO DE PROFISSIONAL";
		} else if (this.tipo == 2) {
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

	public void atualizaListaProfPorGrupo(SelectEvent event)
			throws ProjetoException {
		System.out.println("VAI ATualahsdas");
		this.grupoSelecionado = (GrupoBean) event.getObject();
		this.listaProfissional = pDao
				.listarProfissionalPorGrupo(this.grupoSelecionado.getIdGrupo());
	}

	public List<ProfissionalBean> getListaProfissional() {
		return listaProfissional;
	}

	public String redirectInsert() {
		return "cadastroProfissional?faces-redirect=true&amp;tipo=" + this.tipo;
	}

	public String redirectEdit() {
		return "cadastroProfissional?faces-redirect=true&amp;id="
				+ this.profissional.getIdProfissional() + "&amp;tipo=" + tipo;
	}

	public void getEditProfissional() throws ProjetoException, SQLException {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		Map<String, String> params = facesContext.getExternalContext()
				.getRequestParameterMap();
		if (params.get("id") != null) {
			Integer id = Integer.parseInt(params.get("id"));
			tipo = Integer.parseInt(params.get("tipo"));
			this.profissional = pDao.buscarProfissionalPorId(id);
		} else {
			tipo = Integer.parseInt(params.get("tipo"));

		}

	}
}
