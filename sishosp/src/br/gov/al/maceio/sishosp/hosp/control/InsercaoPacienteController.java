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
import br.gov.al.maceio.sishosp.hosp.dao.BloqueioDAO;
import br.gov.al.maceio.sishosp.hosp.dao.InsercaoPacienteDAO;
import br.gov.al.maceio.sishosp.hosp.model.BloqueioBean;
import br.gov.al.maceio.sishosp.hosp.model.InsercaoPacienteBean;

@ManagedBean(name = "InsercaoController")
@ViewScoped
public class InsercaoPacienteController implements Serializable {

	private static final long serialVersionUID = 1L;
	private InsercaoPacienteBean insercao;
	private InsercaoPacienteDAO iDao = new InsercaoPacienteDAO();
	private ArrayList<InsercaoPacienteBean> listaLaudosVigentes;
	private String tipo;

	public InsercaoPacienteController() {
		this.insercao = new InsercaoPacienteBean();
		listaLaudosVigentes = new ArrayList<InsercaoPacienteBean>();
		this.tipo = "E";
	}

	public void limparDados() {
		this.insercao = new InsercaoPacienteBean();
	}

	public ArrayList<InsercaoPacienteBean> listarLaudosVigentes()
			throws ProjetoException {
		return listaLaudosVigentes = iDao.listarLaudosVigentes();
	}
	
	public void carregarLaudoPaciente() throws ProjetoException{
		int id = insercao.getLaudo().getId();
		limparDados();
		insercao = iDao.carregarLaudoPaciente(id);
		
	}

	public InsercaoPacienteBean getInsercao() {
		return insercao;
	}

	public void setInsercao(InsercaoPacienteBean insercao) {
		this.insercao = insercao;
	}

	public InsercaoPacienteDAO getiDao() {
		return iDao;
	}

	public void setiDao(InsercaoPacienteDAO iDao) {
		this.iDao = iDao;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public ArrayList<InsercaoPacienteBean> getListaLaudosVigentes() {
		return listaLaudosVigentes;
	}

	public void setListaLaudosVigentes(
			ArrayList<InsercaoPacienteBean> listaLaudosVigentes) {
		this.listaLaudosVigentes = listaLaudosVigentes;
	}

}
