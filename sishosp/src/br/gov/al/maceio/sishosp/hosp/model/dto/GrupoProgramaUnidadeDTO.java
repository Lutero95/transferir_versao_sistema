package br.gov.al.maceio.sishosp.hosp.model.dto;

import br.gov.al.maceio.sishosp.hosp.model.GrupoBean;
import br.gov.al.maceio.sishosp.hosp.model.ProgramaBean;
import br.gov.al.maceio.sishosp.hosp.model.UnidadeBean;

public class GrupoProgramaUnidadeDTO {
	
	private GrupoBean grupo;
	private ProgramaBean programa;
	private UnidadeBean unidade;
	
	public GrupoProgramaUnidadeDTO() {
		this.grupo = new GrupoBean();
		this.programa = new ProgramaBean();
		this.unidade = new UnidadeBean();
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
	public UnidadeBean getUnidade() {
		return unidade;
	}
	public void setUnidade(UnidadeBean unidade) {
		this.unidade = unidade;
	}
}
