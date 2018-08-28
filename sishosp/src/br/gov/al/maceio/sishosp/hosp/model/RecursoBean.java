package br.gov.al.maceio.sishosp.hosp.model;

import java.io.Serializable;

public class RecursoBean implements Serializable {

	private Integer idRecurso;
	private String descRecurso;

	public RecursoBean() {

	}

	public Integer getIdRecurso() {
		return idRecurso;
	}

	public void setIdRecurso(Integer idRecurso) {
		this.idRecurso = idRecurso;
	}

	public String getDescRecurso() {
		return descRecurso;
	}

	public void setDescRecurso(String descRecurso) {
		this.descRecurso = descRecurso;
	}

}
