package br.gov.al.maceio.sishosp.hosp.model.dto;

import br.gov.al.maceio.sishosp.hosp.model.EquipeBean;
import br.gov.al.maceio.sishosp.hosp.model.GrupoBean;
import br.gov.al.maceio.sishosp.hosp.model.ProgramaBean;
import br.gov.al.maceio.sishosp.hosp.model.UnidadeBean;

public class EquipeGrupoProgramaUnidadeDTO {
	
	private EquipeBean equipe;
	private GrupoBean grupo;
	private ProgramaBean programa;
	private UnidadeBean unidade;
	
	public EquipeGrupoProgramaUnidadeDTO() {
		this.equipe = new EquipeBean();
		this.grupo = new GrupoBean();
		this.programa = new ProgramaBean();
		this.unidade = new UnidadeBean();
	}
	
	public EquipeBean getEquipe() {
		return equipe;
	}
	public void setEquipe(EquipeBean equipe) {
		this.equipe = equipe;
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
