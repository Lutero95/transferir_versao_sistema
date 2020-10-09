package br.gov.al.maceio.sishosp.hosp.model.dto;

import br.gov.al.maceio.sishosp.hosp.model.GrupoBean;
import br.gov.al.maceio.sishosp.hosp.model.ProcedimentoBean;

public class BuscaGrupoFrequenciaDTO {
	private GrupoBean grupo;
	private Integer frequencia;
	private ProcedimentoBean procedimentoPadao;
	
	public BuscaGrupoFrequenciaDTO() {
		this.grupo = new GrupoBean();
		this.procedimentoPadao = new ProcedimentoBean();
	}
	
	public BuscaGrupoFrequenciaDTO(GrupoBean grupo, Integer frequencia, ProcedimentoBean procedimento) {
		this.grupo = grupo;
		this.frequencia = frequencia;
		this.procedimentoPadao = procedimento;
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

	public ProcedimentoBean getProcedimentoPadao() {
		return procedimentoPadao;
	}

	public void setProcedimentoPadao(ProcedimentoBean procedimentoPadao) {
		this.procedimentoPadao = procedimentoPadao;
	}
	
}
