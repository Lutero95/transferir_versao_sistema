package br.gov.al.maceio.sishosp.hosp.model;

import java.util.Date;
import java.util.List;

public class ConfigAgendaParte1Bean {
	
	private Integer mes;
	private Integer ano;
	private ProfissionalBean profissional;
	private List<Integer> diasSemana;
	private Date dataEspecifica;
	private String turno;
	private Integer qtdMax;
	
	public ConfigAgendaParte1Bean(){
		
	}
	
	public ConfigAgendaParte1Bean(Integer mes, Integer ano,
			ProfissionalBean profissional, List<Integer> diasSemana,
			Date dataEspecifica, String turno, Integer qtdMax) {
		this.mes = mes;
		this.ano = ano;
		this.profissional = profissional;
		this.diasSemana = diasSemana;
		this.dataEspecifica = dataEspecifica;
		this.turno = turno;
		this.qtdMax = qtdMax;
	}
	public Integer getMes() {
		return mes;
	}
	public void setMes(Integer mes) {
		this.mes = mes;
	}
	public Integer getAno() {
		return ano;
	}
	public void setAno(Integer ano) {
		this.ano = ano;
	}
	public ProfissionalBean getProfissional() {
		return profissional;
	}
	public void setProfissional(ProfissionalBean profissional) {
		this.profissional = profissional;
	}
	public List<Integer> getDiasSemana() {
		return diasSemana;
	}
	public void setDiasSemana(List<Integer> diasSemana) {
		this.diasSemana = diasSemana;
	}
	public Date getDataEspecifica() {
		return dataEspecifica;
	}
	public void setDataEspecifica(Date dataEspecifica) {
		this.dataEspecifica = dataEspecifica;
	}
	public String getTurno() {
		return turno;
	}
	public void setTurno(String turno) {
		this.turno = turno;
	}
	public Integer getQtdMax() {
		return qtdMax;
	}
	public void setQtdMax(Integer qtdMax) {
		this.qtdMax = qtdMax;
	}
		
	
}
