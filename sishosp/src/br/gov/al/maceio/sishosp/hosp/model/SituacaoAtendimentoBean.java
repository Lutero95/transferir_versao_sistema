package br.gov.al.maceio.sishosp.hosp.model;

public class SituacaoAtendimentoBean {
	
	private Integer id;
	private String descricao;
	private boolean atendimentoRealizado;
	private boolean abonoFalta;
	private boolean pacienteFaltou;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public boolean getAtendimentoRealizado() {
		return atendimentoRealizado;
	}
	public void setAtendimentoRealizado(boolean atendimentoRealizado) {
		this.atendimentoRealizado = atendimentoRealizado;
	}

	public boolean isAtendimentoRealizado() {
		return atendimentoRealizado;
	}

	public boolean isAbonoFalta() {
		return abonoFalta;
	}

	public void setAbonoFalta(boolean abonoFalta) {
		this.abonoFalta = abonoFalta;
	}
	public boolean isPacienteFaltou() {
		return pacienteFaltou;
	}
	public void setPacienteFaltou(boolean pacienteFaltou) {
		this.pacienteFaltou = pacienteFaltou;
	}
}
