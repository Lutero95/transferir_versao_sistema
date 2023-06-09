package br.gov.al.maceio.sishosp.hosp.model;

import java.io.Serializable;

public class Parentesco implements Serializable {

	private Integer codParentesco;
	private String descParentesco;
	private String tipoParentesco;

	public Parentesco() {

	}

	public Integer getCodParentesco() {
		return codParentesco;
	}

	public void setCodParentesco(Integer codParentesco) {
		this.codParentesco = codParentesco;
	}

	public String getDescParentesco() {
		return descParentesco;
	}

	public void setDescParentesco(String descParentesco) {
		this.descParentesco = descParentesco;
	}

	public String getTipoParentesco() {
		return tipoParentesco;
	}

	public void setTipoParentesco(String tipoParentesco) {
		this.tipoParentesco = tipoParentesco;
	}
}
