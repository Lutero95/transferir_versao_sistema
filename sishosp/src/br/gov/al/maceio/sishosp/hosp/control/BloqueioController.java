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
import br.gov.al.maceio.sishosp.hosp.dao.BloqueioDAO;
import br.gov.al.maceio.sishosp.hosp.model.BloqueioBean;

@ManagedBean(name = "BloqueioController")
@ViewScoped
public class BloqueioController implements Serializable {

	private static final long serialVersionUID = 1L;
	private BloqueioBean bloqueio;
	private List<BloqueioBean> listaBloqueios;
	private BloqueioDAO bDao = new BloqueioDAO();

	private String tipo;

	public BloqueioController() {
		this.bloqueio = new BloqueioBean();
		this.listaBloqueios = null;
		this.tipo = new String();
	}

	public void limparDados() {
		this.bloqueio = new BloqueioBean();
		this.listaBloqueios = null;
	}

	public void gravarBloqueio() throws ProjetoException, SQLException {
		boolean cadastrou = bDao.gravarBloqueio(bloqueio);

		if (cadastrou == true) {
			limparDados();
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Bloqueio cadastrado com sucesso!", "Sucesso");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		} else {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Ocorreu um erro durante o cadastro!", "Erro");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
	}

	public void alterarBloqueio() throws ProjetoException {
		boolean alterou = bDao.alterarBloqueio(bloqueio);
		if (alterou == true) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Bloqueio alterado com sucesso!", "Sucesso");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		} else {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Ocorreu um erro durante o cadastro!", "Erro");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
		this.listaBloqueios = bDao.listarBloqueio();

	}

	public void excluirBloqueio() throws ProjetoException {
		boolean ok = bDao.excluirBloqueio(bloqueio);
		if (ok == true) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Bloqueio excluído com sucesso!", "Sucesso");
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
		this.listaBloqueios = bDao.listarBloqueio();
	}

	public void atualizarListaBloqueio() throws ProjetoException {
		this.listaBloqueios = bDao.listarBloqueioPorProfissional(bloqueio
				.getProf());
	}

	public BloqueioBean getBloqueio() {
		return bloqueio;
	}

	public void setBloqueio(BloqueioBean bloqueio) {
		this.bloqueio = bloqueio;
	}

	public List<BloqueioBean> getListaBloqueios() throws ProjetoException {
		if (this.listaBloqueios == null) {
			this.listaBloqueios = bDao.listarBloqueio();
		}
		return listaBloqueios;
	}

	public void setListaBloqueios(List<BloqueioBean> listaBloqueios) {
		this.listaBloqueios = listaBloqueios;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

}
