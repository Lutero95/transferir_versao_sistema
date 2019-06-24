package br.gov.al.maceio.sishosp.financeiro.model;

import java.io.Serializable;

public class PortadorBean implements Serializable{


	private Integer codportador;
	private String descricao;
	public Integer getCodportador() {
		return codportador;
	}
	public String getDescricao() {
		return descricao;
	}
	

	public void setCodportador(Integer codportador) {
		this.codportador = codportador;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	

	
	
}