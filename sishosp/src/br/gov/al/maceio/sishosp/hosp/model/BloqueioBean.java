package br.gov.al.maceio.sishosp.hosp.model;

import java.io.Serializable;
import java.util.Date;

import br.gov.al.maceio.sishosp.acl.model.FuncionarioBean;

public class BloqueioBean implements Serializable {

	private Integer idBloqueio;
	private Date dataInicio;
	private Date dataFim;
	private String turno;
	private String descBloqueio;
	private Integer codEmpresa;

	// HERDADO
	private FuncionarioBean prof;

	public BloqueioBean() {
		this.prof = new FuncionarioBean();
		this.dataInicio = null;
		this.dataFim = null;
		this.descBloqueio = new String();
	}

	public Integer getIdBloqueio() {
		return idBloqueio;
	}

	public void setIdBloqueio(Integer idBloqueio) {
		this.idBloqueio = idBloqueio;
	}

	public FuncionarioBean getProf() {
		return prof;
	}

	public void setProf(FuncionarioBean prof) {
		this.prof = prof;
	}

	public Date getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	public Date getDataFim() {
		return dataFim;
	}

	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
	}

	public String getTurno() {
		return turno;
	}

	public void setTurno(String turno) {
		this.turno = turno;
	}

	public String getDescBloqueio() {
		return descBloqueio;
	}

	public void setDescBloqueio(String descBloqueio) {
		this.descBloqueio = descBloqueio;
	}

	public Integer getCodEmpresa() {
		return codEmpresa;
	}

	public void setCodEmpresa(Integer codEmpresa) {
		this.codEmpresa = codEmpresa;
	}

}
