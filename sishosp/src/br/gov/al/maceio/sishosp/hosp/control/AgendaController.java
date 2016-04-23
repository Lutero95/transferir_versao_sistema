package br.gov.al.maceio.sishosp.hosp.control;

import br.gov.al.maceio.sishosp.hosp.model.GrupoBean;
import br.gov.al.maceio.sishosp.hosp.model.ProgramaBean;

public class AgendaController {
	
	private GrupoBean grupo;
	private ProgramaBean programa;

	public AgendaController() {
		this.grupo = new GrupoBean();
		this.programa = new ProgramaBean();
	}
	
	public void limparDados(){
		grupo = new GrupoBean();
		programa = new ProgramaBean();
	}

	public GrupoBean getGrupo() {
		return grupo;
	}

	public void setGrupo(GrupoBean grupo) {
		this.grupo = grupo;
	}

	public ProgramaBean getPrograma() {
		return programa;
	}

	public void setPrograma(ProgramaBean programa) {
		this.programa = programa;
	}
	
}
