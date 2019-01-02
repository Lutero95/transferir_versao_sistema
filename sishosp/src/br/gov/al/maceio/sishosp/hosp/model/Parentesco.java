package br.gov.al.maceio.sishosp.hosp.model;

import java.io.Serializable;

public class Parentesco implements Serializable {

	private Integer codParentesco;
	private String descParentesco;

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
}
