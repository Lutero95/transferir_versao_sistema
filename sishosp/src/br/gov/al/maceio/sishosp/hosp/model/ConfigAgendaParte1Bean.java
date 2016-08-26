package br.gov.al.maceio.sishosp.hosp.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ConfigAgendaParte1Bean implements Serializable {

	private Integer idConfiAgenda;
	private Integer mes;
	private Integer ano;
	private ProfissionalBean profissional;
	private EquipeBean equipe;
	private List<String> diasSemana;
	private Date dataEspecifica;
	private String turno;
	private Integer qtdMax;
	private Integer diaDaSemana;

	public ConfigAgendaParte1Bean() {
		this.profissional = new ProfissionalBean();
		this.equipe = new EquipeBean();
		this.diasSemana = new ArrayList<String>();
	}

	public ConfigAgendaParte1Bean(Integer mes, Integer ano,
			ProfissionalBean profissional, List<String> diasSemana,
			Date dataEspecifica, String turno, Integer qtdMax,
			Integer idConfiAgenda, Integer diaDaSemana) {
		this.mes = mes;
		this.ano = ano;
		this.profissional = profissional;
		this.diasSemana = diasSemana;
		this.dataEspecifica = dataEspecifica;
		this.turno = turno;
		this.qtdMax = qtdMax;
		this.idConfiAgenda = idConfiAgenda;
		this.diaDaSemana = diaDaSemana;
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

	public List<String> getDiasSemana() {
		return diasSemana;
	}

	public void setDiasSemana(List<String> diasSemana) {
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

	public Integer getIdConfiAgenda() {
		return idConfiAgenda;
	}

	public void setIdConfiAgenda(Integer idConfiAgenda) {
		this.idConfiAgenda = idConfiAgenda;
	}

	public Integer getDiaDaSemana() {
		return diaDaSemana;
	}

	public void setDiaDaSemana(Integer diaDaSemana) {
		this.diaDaSemana = diaDaSemana;
	}

	public EquipeBean getEquipe() {
		return equipe;
	}

	public void setEquipe(EquipeBean equipe) {
		this.equipe = equipe;
	}
	
}
