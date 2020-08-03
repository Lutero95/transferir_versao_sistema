package br.gov.al.maceio.sishosp.hosp.model.dto;

import br.gov.al.maceio.sishosp.hosp.model.ProcedimentoBean;

public class ProcedimentoIdadeEspecificaDTO {
	
	private ProcedimentoBean procedimento;
	private Integer idadeMinima;
	private Integer idadeMaxima;
	
	public ProcedimentoIdadeEspecificaDTO() {
		this.procedimento = new ProcedimentoBean();
	}
	
	public ProcedimentoBean getProcedimento() {
		return procedimento;
	}
	public void setProcedimento(ProcedimentoBean procedimento) {
		this.procedimento = procedimento;
	}

	public Integer getIdadeMinima() {
		return idadeMinima;
	}

	public void setIdadeMinima(Integer idadeMinima) {
		this.idadeMinima = idadeMinima;
	}

	public Integer getIdadeMaxima() {
		return idadeMaxima;
	}

	public void setIdadeMaxima(Integer idadeMaxima) {
		this.idadeMaxima = idadeMaxima;
	}
}
