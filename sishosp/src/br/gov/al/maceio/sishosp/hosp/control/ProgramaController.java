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
import br.gov.al.maceio.sishosp.hosp.dao.GrupoDAO;
import br.gov.al.maceio.sishosp.hosp.dao.PacienteDAO;
import br.gov.al.maceio.sishosp.hosp.dao.ProgramaDAO;
import br.gov.al.maceio.sishosp.hosp.model.GrupoBean;
import br.gov.al.maceio.sishosp.hosp.model.ProgramaBean;
import br.gov.al.maceio.sishosp.hosp.model.RacaBean;

@ManagedBean(name = "ProgramaController")
@ViewScoped
public class ProgramaController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ProgramaBean prog;
	private List<ProgramaBean> listaProgramas;
	private List<ProgramaBean> buscalistaProgramas;
	private Integer tipoBuscar;
	private String descricaoBusca;
	private int tipo;
	private String cabecalho;
	private Integer abaAtiva = 0;

	ProgramaDAO pDao = new ProgramaDAO();
	GrupoDAO gDao = new GrupoDAO();

	public ProgramaController() {

		this.prog = new ProgramaBean();
		this.listaProgramas = null;
		buscalistaProgramas = new ArrayList<>();
		buscalistaProgramas = null;
		this.descricaoBusca = new String();

	}

	public String redirectEdit() throws ProjetoException {
		return "cadastroPrograma?faces-redirect=true&amp;id="
				+ this.prog.getIdPrograma() + "&amp;tipo=" + tipo;
	}

	public String redirectInsert() {
		return "cadastroPrograma?faces-redirect=true&amp;tipo=" + tipo;
	}

	public void getEditProg() throws ProjetoException {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		Map<String, String> params = facesContext.getExternalContext()
				.getRequestParameterMap();
		if (params.get("id") != null) {
			Integer id = Integer.parseInt(params.get("id"));
			tipo = Integer.parseInt(params.get("tipo"));
			ProgramaDAO cDao = new ProgramaDAO();
			this.prog = cDao.listarProgramaPorId(id);
			prog.setGrupo(gDao.listarGruposDoPrograma(prog.getIdPrograma()));
		} else {
			tipo = Integer.parseInt(params.get("tipo"));

		}

	}

	public void limparDados() throws ProjetoException {
		prog = new ProgramaBean();
		descricaoBusca = new String();
		tipoBuscar = 1;
		listaProgramas = pDao.listarProgramas();
	}

	public void gravarPrograma() throws ProjetoException, SQLException {
		ProgramaDAO pDao = new ProgramaDAO();
		boolean cadastrou = pDao.gravarPrograma(prog);

		if (cadastrou == true) {
			limparDados();
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Programa cadastrado com sucesso!", "Sucesso");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		} else {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Ocorreu um erro durante o cadastro!", "Erro");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
		listaProgramas = pDao.listarProgramas();
	}

	public void excluirPrograma() throws ProjetoException {
		boolean ok = pDao.excluirPrograma(prog);
		if (ok == true) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Programa excluído com sucesso!", "Sucesso");
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
		listaProgramas = pDao.listarProgramas();
	}

	public void alterarPrograma() throws ProjetoException {
		boolean alterou = pDao.alterarPrograma(prog);
		if (alterou == true) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Programa alterado com sucesso!", "Sucesso");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			listaProgramas = pDao.listarProgramas();
			// return
			// "/pages/sishosp/gerenciarPrograma.faces?faces-redirect=true";
		} else {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Ocorreu um erro durante o cadastro!", "Erro");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			// return "";
		}

	}

	public ProgramaBean getProg() {
		return prog;
	}

	public void setProg(ProgramaBean prog) {
		this.prog = prog;
	}

	public List<ProgramaBean> getListaProgramas() throws ProjetoException {
		if (listaProgramas == null) {
			listaProgramas = pDao.listarProgramas();
		}
		return listaProgramas;
	}

	public void setListaProgramas(List<ProgramaBean> listaProgramas) {
		this.listaProgramas = listaProgramas;
	}

	public Integer getTipoBuscar() {
		return tipoBuscar;
	}

	public void setTipoBuscar(Integer tipoBuscar) {
		this.tipoBuscar = tipoBuscar;
	}

	public void buscarProgramas() throws ProjetoException {
		this.listaProgramas = pDao.listarProgramasBusca(descricaoBusca,
				tipoBuscar);
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
		if (this.tipo == 1) {
			cabecalho = "Inclusão de Programa";
		} else if (this.tipo == 2) {
			cabecalho = "Alteração de Programa";
		}
		return cabecalho;
	}

	public void setCabecalho(String cabecalho) {
		this.cabecalho = cabecalho;
	}

	public List<ProgramaBean> listaProgramaAutoComplete(String query)
			throws ProjetoException {
		ProgramaDAO pDao = new ProgramaDAO();
		List<ProgramaBean> result = pDao.listarProgramasBusca(query, 1);
		return result;
	}

	public List<ProgramaBean> listaProgramaAutoCompleteUsuario(String query)
			throws ProjetoException {
		ProgramaDAO pDao = new ProgramaDAO();
		List<ProgramaBean> result = pDao.listarProgramasBuscaUsuario(query, 1);
		return result;
	}

	public int getTipo() {
		return tipo;
	}

	public void setTipo(int tipo) {
		this.tipo = tipo;
	}

	public List<ProgramaBean> getBuscalistaProgramas() throws ProjetoException {
		if (buscalistaProgramas == null) {
			ProgramaDAO pdaos = new ProgramaDAO();
			buscalistaProgramas = pdaos.BuscalistarProgramas();
		}
		return buscalistaProgramas;
	}

	public void setBuscalistaProgramas(List<ProgramaBean> buscalistaProgramas) {
		this.buscalistaProgramas = buscalistaProgramas;
	}

}
