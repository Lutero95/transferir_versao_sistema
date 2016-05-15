package br.gov.al.maceio.sishosp.hosp.control;

import java.sql.SQLException;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import br.gov.al.maceio.sishosp.hosp.dao.ProfissionalDAO;
import br.gov.al.maceio.sishosp.hosp.model.ProfissionalBean;

public class ProfissionalController {

	private ProfissionalBean profissional;
	private ProfissionalDAO pDao = new ProfissionalDAO();

	public ProfissionalController() {
		this.profissional = new ProfissionalBean();
	}

	public void limparDados() {
		this.profissional = new ProfissionalBean();
	}

	public ProfissionalBean getProfissional() {
		return profissional;
	}

	public void setProfissional(ProfissionalBean profissional) {
		this.profissional = profissional;
	}

	public void gravarProfissional() throws SQLException {
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
	}
}
