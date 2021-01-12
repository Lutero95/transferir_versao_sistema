package br.gov.al.maceio.sishosp.comum.model;

import java.io.Serializable;
import java.sql.Time;

public class HorarioFuncionamento implements Serializable{
	
	private static final long serialVersionUID = -1273406828915890957L;
	private Time horarioInicio;
	private Time horarioFim;
	
	public Time getHorarioInicio() {
		return horarioInicio;
	}
	public void setHorarioInicio(Time horarioInicio) {
		this.horarioInicio = horarioInicio;
	}
	public Time getHorarioFim() {
		return horarioFim;
	}
	public void setHorarioFim(Time horarioFim) {
		this.horarioFim = horarioFim;
	}
}
