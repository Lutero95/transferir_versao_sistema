package br.gov.al.maceio.sishosp.financeiro.model;

import java.io.Serializable;

public class DebitoCaixaBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer codDeb, codcaixaloja;

	private String historico;
	private Double valor;
	private java.util.Date dt_confirmado;

	public Integer getCodDeb() {
		return codDeb;
	}

	public void setCodDeb(Integer codDeb) {
		this.codDeb = codDeb;
	}

	public Integer getCodcaixaloja() {
		return codcaixaloja;
	}

	public void setCodcaixaloja(Integer codcaixaloja) {
		this.codcaixaloja = codcaixaloja;
	}

	public String getHistorico() {
		return historico;
	}

	public void setHistorico(String historico) {
		this.historico = historico;
	}

	public Double getValor() {
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}

	public java.util.Date getDt_confirmado() {
		return dt_confirmado;
	}

	public void setDt_confirmado(java.util.Date dt_confirmado) {
		this.dt_confirmado = dt_confirmado;
	}

}
