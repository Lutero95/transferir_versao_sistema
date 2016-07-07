package br.gov.al.maceio.sishosp.hosp.control;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;

import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.hosp.dao.CboDAO;
import br.gov.al.maceio.sishosp.hosp.model.CboBean;

public class CboController implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private CboBean cbo;
	private List<CboBean> listaCbo;
	private Integer tipoBuscar;
	private String descricaoBusca;
	private String tipo;
	private String cabecalho;

	private Integer abaAtiva = 0;

	private CboDAO cDao = new CboDAO();

	public CboController() {
		this.cbo = new CboBean();
		this.listaCbo = null;
		this.descricaoBusca = new String();
		this.tipo = new String();
	}

	public void limparDados() {
		this.cbo = new CboBean();
		this.listaCbo = new ArrayList<>();
		this.descricaoBusca = new String();
		//this.tipo = new String();
		listaCbo = cDao.listarCbo();
	}

	public void gravarCbo() throws ProjetoException, SQLException {
		boolean cadastrou = cDao.gravarCBO(this.cbo);

		if (cadastrou == true) {
			limparDados();
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"CBO cadastrado com sucesso!", "Sucesso");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		} else {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Ocorreu um erro durante o cadastro!", "Erro");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
	}

	public void alterarCbo() throws ProjetoException {
		boolean alterou = cDao.alterarCbo(cbo);
		if (alterou == true) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Cbo alterado com sucesso!", "Sucesso");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		} else {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Ocorreu um erro durante o cadastro!", "Erro");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
		listaCbo = cDao.listarCbo();

	}

	public void excluirCbo() throws ProjetoException {
		boolean ok = cDao.excluirCbo(cbo);
		if (ok == true) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Cbo excluido com sucesso!", "Sucesso");
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
		listaCbo = cDao.listarCbo();
	}

	public CboBean getCbo() {
		return cbo;
	}

	public void setCbo(CboBean cbo) {
		this.cbo = cbo;
	}

	public List<CboBean> getListaCbo() {
		if (listaCbo == null) {
			listaCbo = cDao.listarCbo();
		}
		return listaCbo;
	}

	public void setListaCbo(List<CboBean> listaCbo) {
		this.listaCbo = listaCbo;
	}

	public void buscarCbo() {
		this.listaCbo = cDao.listarCboBusca(descricaoBusca, tipoBuscar);
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
	
	public String getCabecalho() {
		if(this.tipo.equals("I")){
			cabecalho = "CADASTRO DE CBO";
		}else if(this.tipo.equals("A")){
			cabecalho = "ALTERAR CBO";
		}
		return cabecalho;
	}

	public void setCabecalho(String cabecalho) {
		this.cabecalho = cabecalho;
	}

}
