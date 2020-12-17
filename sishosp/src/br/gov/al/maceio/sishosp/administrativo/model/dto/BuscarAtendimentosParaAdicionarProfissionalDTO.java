package br.gov.al.maceio.sishosp.administrativo.model.dto;

public class BuscarAtendimentosParaAdicionarProfissionalDTO {
	
	private Integer idAtendimento;
	private String turno;

	public Integer getIdAtendimento() {
		return idAtendimento;
	}
	public void setIdAtendimento(Integer idAtendimento) {
		this.idAtendimento = idAtendimento;
	}
	public String getTurno() {
		return turno;
	}
	public void setTurno(String turno) {
		this.turno = turno;
	}
}
