package br.gov.al.maceio.sishosp.hosp.model;

public class SituacaoAtendimentoBean {
	private Integer id;
	private String descricao;
	private boolean atendimentoRealizado;
	
	
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
}
