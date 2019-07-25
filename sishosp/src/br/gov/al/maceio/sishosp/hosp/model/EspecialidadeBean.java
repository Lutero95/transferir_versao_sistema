package br.gov.al.maceio.sishosp.hosp.model;

import java.io.Serializable;

public class EspecialidadeBean implements Serializable {

	private Integer codEspecialidade;
	private String descEspecialidade;

	public EspecialidadeBean() {

	}

	public Integer getCodEspecialidade() {
		return codEspecialidade;
	}

	public void setCodEspecialidade(Integer codEspecialidade) {
		this.codEspecialidade = codEspecialidade;
	}

	public String getDescEspecialidade() {
		return descEspecialidade;
	}

	public void setDescEspecialidade(String descEspecialidade) {
		this.descEspecialidade = descEspecialidade;
	}

	

}
