package br.gov.al.maceio.sishosp.hosp.model;

import java.io.Serializable;

public class ConvenioBean implements Serializable {

	private Integer codconvenio;
	private String descconvenio;

	public ConvenioBean() {

	}

	public Integer getCodconvenio() {
		return codconvenio;
	}

	public void setCodconvenio(Integer codconvenio) {
		this.codconvenio = codconvenio;
	}

	public String getDescconvenio() {
		return descconvenio;
	}

	public void setDescconvenio(String descconvenio) {
		this.descconvenio = descconvenio;
	}

}
