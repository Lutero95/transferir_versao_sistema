package br.gov.al.maceio.sishosp.hosp.model;

public class DiaAtendimento {
	private Integer diaSemana;
	private String turno;
	private String horarioAtendimento;
	
	
	public Integer getDiaSemana() {
		return diaSemana;
	}
	public String getTurno() {
		return turno;
	}
	public String getHorarioAtendimento() {
		return horarioAtendimento;
	}
	public void setDiaSemana(Integer diaSemana) {
		this.diaSemana = diaSemana;
	}
	public void setTurno(String turno) {
		this.turno = turno;
	}
	public void setHorarioAtendimento(String horarioAtendimento) {
		this.horarioAtendimento = horarioAtendimento;
	}
}
