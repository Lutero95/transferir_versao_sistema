package br.gov.al.maceio.sishosp.hosp.control;

import br.gov.al.maceio.sishosp.hosp.model.GrupoBean;

public class AgendaController {
	
	private GrupoBean grupo;

	public AgendaController() {
		this.grupo = new GrupoBean();
	}

	public GrupoBean getGrupo() {
		return grupo;
	}

	public void setGrupo(GrupoBean grupo) {
		this.grupo = grupo;
	}
	
}
