package br.gov.al.maceio.sishosp.financeiro.model;

import java.io.Serializable;

public class CentroCustoBean implements Serializable{


	private Integer idccusto;
	private String descricao;
	private String tipo;
	
	
	
	public Integer getIdccusto() {
		return idccusto;
	}
	
	public String getDescricao() {
		return descricao;
	}
	public String getTipo() {
		return tipo;
	}
	
	public void setIdccusto(Integer idccusto) {
		this.idccusto = idccusto;
	}
	
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	
	
	
	
	
	
}
