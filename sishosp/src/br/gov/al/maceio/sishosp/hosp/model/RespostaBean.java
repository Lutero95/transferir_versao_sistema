package br.gov.al.maceio.sishosp.hosp.model;

public class RespostaBean {
	
	private Integer id;
	private String resposta;
	private PacienteBean paciente;
	
	public RespostaBean() {
		this.paciente = new PacienteBean();
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getResposta() {
		return resposta;
	}
	public void setResposta(String resposta) {
		this.resposta = resposta;
	}
	public PacienteBean getPaciente() {
		return paciente;
	}
	public void setPaciente(PacienteBean paciente) {
		this.paciente = paciente;
	}
	
}
