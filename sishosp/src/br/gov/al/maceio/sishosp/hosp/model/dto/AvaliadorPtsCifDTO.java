package br.gov.al.maceio.sishosp.hosp.model.dto;

import java.util.Date;

import br.gov.al.maceio.sishosp.acl.model.FuncionarioBean;

public class AvaliadorPtsCifDTO {
	
	private FuncionarioBean avaliador;
	private Date data;
	private Boolean validado;
	
	public AvaliadorPtsCifDTO() {
		this.avaliador = new FuncionarioBean();
	}
	
	public FuncionarioBean getAvaliador() {
		return avaliador;
	}
	public void setAvaliador(FuncionarioBean avaliador) {
		this.avaliador = avaliador;
	}
	public Date getData() {
		return data;
	}
	public void setData(Date data) {
		this.data = data;
	}
	public Boolean getValidado() {
		return validado;
	}
	public void setValidado(Boolean validado) {
		this.validado = validado;
	}
}
