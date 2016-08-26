package br.gov.al.maceio.sishosp.hosp.model;

import java.io.Serializable;

public class EscolaBean implements Serializable{
	private Integer codEscola;
	private String descescola;
	private Integer codtipoescola;	
	private String desctipoescola;
	
	public EscolaBean(){
		
	}
	
	public Integer getCodEscola() {
		return codEscola;
	}

	public void setCodEscola(Integer codEscola) {
		this.codEscola = codEscola;
	}
	public String getDescescola() {
		return descescola;
	}
	public void setDescescola(String descescola) {
		this.descescola = descescola;
	}
	public Integer getCodtipoescola() {
		return codtipoescola;
	}
	public void setCodtipoescola(Integer codtipoescola) {
		this.codtipoescola = codtipoescola;
	}

	public String getDesctipoescola() {
		return desctipoescola;
	}

	public void setDesctipoescola(String desctipoescola) {
		this.desctipoescola = desctipoescola;
	}
	
}
