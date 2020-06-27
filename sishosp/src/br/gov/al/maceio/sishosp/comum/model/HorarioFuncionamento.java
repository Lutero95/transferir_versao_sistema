package br.gov.al.maceio.sishosp.comum.model;

import java.sql.Time;
import java.util.Date;

public class HorarioFuncionamento {
	
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
