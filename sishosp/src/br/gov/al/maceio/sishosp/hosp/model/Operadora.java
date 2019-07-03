package br.gov.al.maceio.sishosp.hosp.model;

import java.io.Serializable;

public class Operadora implements Serializable {

	private Integer id;
	private String descricao;

	public Operadora() {

	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
}
