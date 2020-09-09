package br.gov.al.maceio.sishosp.hosp.model.dto;

import br.gov.al.maceio.sishosp.hosp.model.GrupoBean;
import br.gov.al.maceio.sishosp.hosp.model.ProgramaBean;

public class PendenciaEvolucaoProgramaGrupoDTO {
	
	private ProgramaBean programa;
	private GrupoBean grupo;
	private Integer totalPendencia;
	
	public PendenciaEvolucaoProgramaGrupoDTO() {
		this.programa = new ProgramaBean();
		this.grupo = new GrupoBean();
	}
	
	public ProgramaBean getPrograma() {
		return programa;
	}
	public void setPrograma(ProgramaBean programa) {
		this.programa = programa;
	}
	public GrupoBean getGrupo() {
		return grupo;
	}
	public void setGrupo(GrupoBean grupo) {
		this.grupo = grupo;
	}
	public Integer getTotalPendencia() {
		return totalPendencia;
	}
	public void setTotalPendencia(Integer totalPendencia) {
		this.totalPendencia = totalPendencia;
	}
}
