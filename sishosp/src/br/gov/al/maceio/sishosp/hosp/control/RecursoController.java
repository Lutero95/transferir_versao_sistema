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
import br.gov.al.maceio.sishosp.hosp.dao.EscolaDAO;
import br.gov.al.maceio.sishosp.hosp.dao.RecursoDAO;
import br.gov.al.maceio.sishosp.hosp.model.EscolaBean;
import br.gov.al.maceio.sishosp.hosp.model.RecursoBean;

@ManagedBean(name = "RecursoController")
@ViewScoped
public class RecursoController implements Serializable {

	private RecursoBean recurso;
	private ArrayList<RecursoBean> listaRecursos;
	private int tipo;
	private String cabecalho;
	private RecursoDAO rdao;

	public RecursoController() {
		
		rdao = new RecursoDAO();
		recurso = new RecursoBean();
		listaRecursos = new ArrayList<>();
		listaRecursos = null;

	}

	public String redirectEdit() {
		return "cadastroRecurso?faces-redirect=true&amp;id="
				+ this.recurso.getIdRecurso() + "&amp;tipo=" + tipo;
	}

	public String redirectInsert() {
		return "cadastroRecurso?faces-redirect=true&amp;tipo=" + tipo;
	}

	public void getEditRecurso() throws ProjetoException {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		Map<String, String> params = facesContext.getExternalContext()
				.getRequestParameterMap();
		if (params.get("id") != null) {
			Integer id = Integer.parseInt(params.get("id"));
			tipo = Integer.parseInt(params.get("tipo"));

			this.recurso = rdao.buscaRecursoCodigo(id);
		} else {

			tipo = Integer.parseInt(params.get("tipo"));

		}

	}

	public void gravarRecurso() throws ProjetoException, SQLException {
		boolean cadastrou = rdao.cadastrar(recurso);

		if (cadastrou == true) {
			limparDados();
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Recurso cadastrado com sucesso!", "Sucesso");
			FacesContext.getCurrentInstance().addMessage(null, msg);

		} else {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Ocorreu um erro durante o cadastro!", "Erro");
			FacesContext.getCurrentInstance().addMessage(null, msg);

		}
	}

	public void alterarRecurso() throws ProjetoException {

		boolean alterou = rdao.alterar(recurso);
		listaRecursos = null;
		if (alterou == true) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Recurso alterado com sucesso!", "Sucesso");
			FacesContext.getCurrentInstance().addMessage(null, msg);

		} else {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Ocorreu um erro durante o cadastro!", "Erro");
			FacesContext.getCurrentInstance().addMessage(null, msg);

		}

	}

	public void excluirRecurso() throws ProjetoException {

		boolean excluir = rdao.excluir(recurso);

		if (excluir == true) {

			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Recurso excluído com sucesso!", "Sucesso");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			RequestContext.getCurrentInstance().execute(
					"PF('dialogAtencao').hide();");
			buscarRecurso();
		} else {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Ocorreu um erro durante a exclusao!", "Erro");
			FacesContext.getCurrentInstance().addMessage(null, msg);

			RequestContext.getCurrentInstance().execute(
					"PF('dialogAtencao').hide();");
		}
	}

	public void limparDados() {
		recurso = new RecursoBean();

	}
	
	public void buscarRecurso() throws ProjetoException {
		listaRecursos = rdao.listaRecursos();
	}

	public String getCabecalho() {
		if (this.tipo == 1) {
			cabecalho = "Inclusão de Recurso";
		} else if (this.tipo == 2) {
			cabecalho = "Alteração de Recurso";
		}
		return cabecalho;
	}

	public void setCabecalho(String cabecalho) {
		this.cabecalho = cabecalho;
	}

	public RecursoBean getRecurso() {
		return recurso;
	}

	public void setRecurso(RecursoBean recurso) {
		this.recurso = recurso;
	}

	public ArrayList<RecursoBean> getListaRecursos() {
		return listaRecursos;
	}

	public void setListaRecursos(ArrayList<RecursoBean> listaRecursos) {
		this.listaRecursos = listaRecursos;
	}

	public int getTipo() {
		return tipo;
	}

	public void setTipo(int tipo) {
		this.tipo = tipo;
	}

}
