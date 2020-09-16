package br.gov.al.maceio.sishosp.hosp.log.model;

import java.util.Date;

public abstract class LogAbstract {
	
	protected Integer id;
	protected Long idFuncionario;
	protected Date dataHora;
	protected String rotina;
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Long getIdFuncionario() {
		return idFuncionario;
	}
	public void setIdFuncionario(Long idFuncionario) {
		this.idFuncionario = idFuncionario;
	}
	public Date getDataHora() {
		return dataHora;
	}
	public void setDataHora(Date dataHora) {
		this.dataHora = dataHora;
	}
	public String getRotina() {
		return rotina;
	}
	public void setRotina(String rotina) {
		this.rotina = rotina;
	}
}
