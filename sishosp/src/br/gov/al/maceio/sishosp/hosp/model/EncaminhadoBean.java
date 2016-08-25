package br.gov.al.maceio.sishosp.hosp.model;

import java.io.Serializable;



public class EncaminhadoBean implements Serializable{

	private Integer codencaminhado;
	private String descencaminhado;
	
	
	public EncaminhadoBean(){
		
	}


	public Integer getCodencaminhado() {
		return codencaminhado;
	}


	public void setCodencaminhado(Integer codencaminhado) {
		this.codencaminhado = codencaminhado;
	}


	public String getDescencaminhado() {
		return descencaminhado;
	}


	public void setDescencaminhado(String descencaminhado) {
		this.descencaminhado = descencaminhado;
	}

	

}
