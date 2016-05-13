package br.gov.al.maceio.sishosp.hosp.control;

import br.gov.al.maceio.sishosp.hosp.model.ProfissionalBean;

public class ProfissionalController {
	
	private ProfissionalBean profissional;
	
	public ProfissionalController() {
		this.profissional = new ProfissionalBean();
	}
	
	public void limparDados(){
		this.profissional = new ProfissionalBean();
	}

	public ProfissionalBean getProfissional() {
		return profissional;
	}

	public void setProfissional(ProfissionalBean profissional) {
		this.profissional = profissional;
	}
	
	public void gravarProfissional(){
		
	}

}
