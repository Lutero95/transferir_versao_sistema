package br.gov.al.maceio.sishosp.hosp.control;

import java.sql.SQLException;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.hosp.dao.CboDAO;
import br.gov.al.maceio.sishosp.hosp.model.CboBean;

public class CboController {
	
	private CboBean cbo;
	
	CboDAO cDao = new CboDAO();
	
	public CboController() {
		this.cbo = new CboBean();
	}

	public void limparDados() {
		cbo = new CboBean();
	}

	public CboBean getCbo() {
		return cbo;
	}

	public void setCbo(CboBean cbo) {
		this.cbo = cbo;
	}

	public void gravarCBO() throws ProjetoException, SQLException {
		
		boolean cadastrou = cDao.gravarCBO(cbo);

		if (cadastrou == true) {
			limparDados();
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "CBO cadastrado com sucesso!", "Sucesso");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		} else {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ocorreu um erro durante o cadastro!",
					"Erro");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
	}

}
