package br.gov.al.maceio.sishosp.hosp.model;

import java.io.Serializable;

public class EscolaridadeBean  implements Serializable{

	private Integer codescolaridade;
	private String  descescolaridade;
	
	public EscolaridadeBean(){
		
	}

	public Integer getCodescolaridade() {
		return codescolaridade;
	}

	public void setCodescolaridade(Integer codescolaridade) {
		this.codescolaridade = codescolaridade;
	}

	public String getDescescolaridade() {
		return descescolaridade;
	}

	public void setDescescolaridade(String descescolaridade) {
		this.descescolaridade = descescolaridade;
	}

}
