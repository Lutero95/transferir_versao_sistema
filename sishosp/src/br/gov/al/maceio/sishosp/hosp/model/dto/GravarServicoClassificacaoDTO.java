package br.gov.al.maceio.sishosp.hosp.model.dto;

public class GravarServicoClassificacaoDTO {
	private Integer idServico;
	private Integer idClassificacao;
	private Integer idProcedimentoMensal;
	
	
	public Integer getIdServico() {
		return idServico;
	}
	public void setIdServico(Integer idServico) {
		this.idServico = idServico;
	}
	public Integer getIdClassificacao() {
		return idClassificacao;
	}
	public void setIdClassificacao(Integer idClassificacao) {
		this.idClassificacao = idClassificacao;
	}
	public Integer getIdProcedimentoMensal() {
		return idProcedimentoMensal;
	}
	public void setIdProcedimentoMensal(Integer idProcedimentoMensal) {
		this.idProcedimentoMensal = idProcedimentoMensal;
	}
}
