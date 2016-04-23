package br.gov.al.maceio.sishosp.hosp.control;

import java.sql.SQLException;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.hosp.dao.ProgramaDAO;
import br.gov.al.maceio.sishosp.hosp.model.ProgramaBean;

public class ProgramaController {
	
	private ProgramaBean prog;
	
	public ProgramaController() {
		this.prog = new ProgramaBean();
	}

	public void gravarPrograma() throws ProjetoException, SQLException {
		ProgramaDAO pDao = new ProgramaDAO();
		boolean cadastrou = pDao.gravarPrograma(prog);

		if (cadastrou == true) {
			limparDados();
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Programa cadastrado com sucesso!",
					"Sucesso");
			FacesContext.getCurrentInstance().addMessage(null, msg);

		} else {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ocorreu um erro durante o cadastro!",
					"Erro");
			FacesContext.getCurrentInstance().addMessage(null, msg);

		}

	}
	public void limparDados(){
		prog = new ProgramaBean();
	
	}

	public ProgramaBean getProg() {
		return prog;
	}

	public void setProg(ProgramaBean prog) {
		this.prog = prog;
	}

}
