package br.gov.al.maceio.sishosp.hosp.model;

import java.sql.Timestamp;
import java.util.Date;

public class HistoricoSigtapBean {
	
	private Integer id;
	private Integer mes;
	private String mesString;
	private Integer ano;
	private String status;
	private Integer idFuncionario;
	private Timestamp dataIhHoraRegistro;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getMes() {
		return mes;
	}
	public void setMes(Integer mes) {
		this.mes = mes;
	}
	public String getMesString() {
		return mesString;
	}
	public void setMesString(String mesString) {
		this.mesString = mesString;
	}
	public Integer getAno() {
		return ano;
	}
	public void setAno(Integer ano) {
		this.ano = ano;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Integer getIdFuncionario() {
		return idFuncionario;
	}
	public void setIdFuncionario(Integer idFuncionario) {
		this.idFuncionario = idFuncionario;
	}
	public Timestamp getDataIhHoraRegistro() {
		return dataIhHoraRegistro;
	}
	public void setDataIhHoraRegistro(Timestamp dataIhHoraRegistro) {
		this.dataIhHoraRegistro = dataIhHoraRegistro;
	}	
}	