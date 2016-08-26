package br.gov.al.maceio.sishosp.hosp.model;

import java.io.Serializable;

public class RacaBean implements Serializable{
	
	private Integer codRaca;
	private String descRaca;
	
	public RacaBean(){
		
	}

	public Integer getCodRaca() {
		return codRaca;
	}

	public void setCodRaca(Integer codRaca) {
		this.codRaca = codRaca;
	}

	public String getDescRaca() {
		return descRaca;
	}

	public void setDescRaca(String descRaca) {
		this.descRaca = descRaca;
	}
	
	

}
