package br.gov.al.maceio.sishosp.hosp.model.dto;

import br.gov.al.maceio.sishosp.hosp.model.PacienteBean;
import br.gov.al.maceio.sishosp.hosp.model.PesquisaBean;

public class PacientePesquisaDTO {

	private PacienteBean paciente;
	private PesquisaBean pesquisa;
	private boolean respondido;
	
	public PacientePesquisaDTO() {
		this.paciente = new PacienteBean();
		this.pesquisa = new PesquisaBean();
	}
	
	public PacienteBean getPaciente() {
		return paciente;
	}
	public void setPaciente(PacienteBean paciente) {
		this.paciente = paciente;
	}
	
	public PesquisaBean getPesquisa() {
		return pesquisa;
	}

	public void setPesquisa(PesquisaBean pesquisa) {
		this.pesquisa = pesquisa;
	}

	public boolean isRespondido() {
		return respondido;
	}
	public void setRespondido(boolean respondido) {
		this.respondido = respondido;
	}
}
