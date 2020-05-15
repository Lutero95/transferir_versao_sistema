package br.gov.al.maceio.sishosp.hosp.model.dto;

import sigtap.br.gov.saude.servicos.schema.sigtap.procedimento.v1.procedimento.ProcedimentoType;

public class GravarProcedimentoMensalDTO {
	private Long idProcedimentoMensal;
	private Integer idProcedimento;
	private ProcedimentoType procedimentoMensal;
	
	public Long getIdProcedimentoMensal() {
		return idProcedimentoMensal;
	}
	public void setIdProcedimentoMensal(Long idProcedimentoMensal) {
		this.idProcedimentoMensal = idProcedimentoMensal;
	}
	public Integer getIdProcedimento() {
		return idProcedimento;
	}
	public void setIdProcedimento(Integer idProcedimento) {
		this.idProcedimento = idProcedimento;
	}
	public ProcedimentoType getProcedimentoMensal() {
		return procedimentoMensal;
	}
	public void setProcedimentoMensal(ProcedimentoType procedimentoMensal) {
		this.procedimentoMensal = procedimentoMensal;
	}	    
}
