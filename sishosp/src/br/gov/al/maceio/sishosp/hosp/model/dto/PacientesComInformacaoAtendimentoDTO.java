package br.gov.al.maceio.sishosp.hosp.model.dto;

import br.gov.al.maceio.sishosp.hosp.model.PacienteBean;

public class PacientesComInformacaoAtendimentoDTO {
	
	private PacienteBean paciente;
	private boolean presenca;
	private Integer idCidPrimario;
	
	public PacientesComInformacaoAtendimentoDTO() {
		this.paciente = new PacienteBean();
	}
	
	public PacienteBean getPaciente() {
		return paciente;
	}
	public void setPaciente(PacienteBean paciente) {
		this.paciente = paciente;
	}
	public boolean isPresenca() {
		return presenca;
	}
	public void setPresenca(boolean presenca) {
		this.presenca = presenca;
	}

	public Integer getIdCidPrimario() {
		return idCidPrimario;
	}

	public void setIdCidPrimario(Integer idCidPrimario) {
		this.idCidPrimario = idCidPrimario;
	}
}
