package br.gov.al.maceio.sishosp.hosp.model.dto;

public class RelacaoObjetoComProcedimentoDTO {
	
	private String codigoProcedimento;
	private String codigo;
	private String codigoClassificacao;
	
	public RelacaoObjetoComProcedimentoDTO() {
	}
	
	public RelacaoObjetoComProcedimentoDTO(String codigoProcedimento, String codigo) {
		this.codigoProcedimento = codigoProcedimento;
		this.codigo = codigo;
	}
	
	public RelacaoObjetoComProcedimentoDTO(String codigoProcedimento, String codigo, String codigoClassificacao) {
		this.codigoProcedimento = codigoProcedimento;
		this.codigo = codigo;
		this.codigoClassificacao = codigoClassificacao;
	}
	
	public String getCodigoProcedimento() {
		return codigoProcedimento;
	}
	public void setCodigoProcedimento(String codigoProcedimento) {
		this.codigoProcedimento = codigoProcedimento;
	}
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getCodigoClassificacao() {
		return codigoClassificacao;
	}

	public void setCodigoClassificacao(String codigoClassificacao) {
		this.codigoClassificacao = codigoClassificacao;
	}
}
