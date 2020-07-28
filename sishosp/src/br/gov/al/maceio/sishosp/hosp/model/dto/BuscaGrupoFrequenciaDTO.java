package br.gov.al.maceio.sishosp.hosp.model.dto;

import br.gov.al.maceio.sishosp.hosp.model.GrupoBean;

public class BuscaGrupoFrequenciaDTO {
	private GrupoBean grupo;
	private Integer frequencia;
	
	public BuscaGrupoFrequenciaDTO() {
		this.grupo = new GrupoBean();
	}
	
	public BuscaGrupoFrequenciaDTO(GrupoBean grupo, Integer frequencia) {
		this.grupo = grupo;
		this.frequencia = frequencia;
	}
	
	public GrupoBean getGrupo() {
		return grupo;
	}
	public void setGrupo(GrupoBean grupo) {
		this.grupo = grupo;
	}
	public Integer getFrequencia() {
		return frequencia;
	}
	public void setFrequencia(Integer frequencia) {
		this.frequencia = frequencia;
	}
}
