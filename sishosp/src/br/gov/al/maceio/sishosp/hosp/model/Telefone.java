package br.gov.al.maceio.sishosp.hosp.model;

import java.io.Serializable;

public class Telefone implements Serializable {

	private Integer idTelefone;
	private Integer ddd;
	private Integer telefone;
	private Boolean whatsapp;
	private String responsavel;
	private Parentesco parentesco;

	public Telefone() {
		parentesco = new Parentesco();
	}

	public Integer getIdTelefone() {
		return idTelefone;
	}

	public void setIdTelefone(Integer idTelefone) {
		this.idTelefone = idTelefone;
	}

	public Integer getDdd() {
		return ddd;
	}

	public void setDdd(Integer ddd) {
		this.ddd = ddd;
	}

	public Integer getTelefone() {
		return telefone;
	}

	public void setTelefone(Integer telefone) {
		this.telefone = telefone;
	}

	public Boolean getWhatsapp() {
		return whatsapp;
	}

	public void setWhatsapp(Boolean whatsapp) {
		this.whatsapp = whatsapp;
	}

	public String getResponsavel() {
		return responsavel;
	}

	public void setResponsavel(String responsavel) {
		this.responsavel = responsavel;
	}

	public Parentesco getParentesco() {
		return parentesco;
	}

	public void setParentesco(Parentesco parentesco) {
		this.parentesco = parentesco;
	}
}
