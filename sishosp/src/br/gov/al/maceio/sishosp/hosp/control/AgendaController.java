package br.gov.al.maceio.sishosp.hosp.control;

import br.gov.al.maceio.sishosp.hosp.model.GrupoBean;
import br.gov.al.maceio.sishosp.hosp.model.PacienteBean;
import br.gov.al.maceio.sishosp.hosp.model.ProgramaBean;
import br.gov.al.maceio.sishosp.hosp.model.TipoAtendimentoBean;

public class AgendaController {
	
	private PacienteBean paciente;
	private GrupoBean grupo;
	private ProgramaBean programa;
	private TipoAtendimentoBean tipoAt;
	private boolean prontoGravar;
	

	public AgendaController() {
		this.paciente = null;//new PacienteBean();
		this.grupo = null;//new GrupoBean();
		this.programa = null;//new ProgramaBean();
		this.tipoAt = null;//new TipoAtendimentoBean();
		this.prontoGravar = false;
	}
	
	public void limparDados(){
		grupo = new GrupoBean();
		programa = new ProgramaBean();
		paciente = new PacienteBean();
		tipoAt = new TipoAtendimentoBean();
		this.prontoGravar = false;
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

	public PacienteBean getPaciente() {
		return paciente;
	}

	public void setPaciente(PacienteBean paciente) {
		this.paciente = paciente;
	}

	public boolean isProntoGravar() {
		return prontoGravar;
	}

	public void setProntoGravar(boolean prontoGravar) {
		this.prontoGravar = prontoGravar;
	}

	public TipoAtendimentoBean getTipoAt() {
		return tipoAt;
	}

	public void setTipoAt(TipoAtendimentoBean tipoAt) {
		this.tipoAt = tipoAt;
	}
	
}
