package br.gov.al.maceio.sishosp.hosp.model;

public class PalavraChaveEvolucaoBean {
	
	private Integer id;
	private String descricao;
	private boolean atendimentoRealizado;
	private boolean excluido;
	
	
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
	public boolean isAtendimentoRealizado() {
		return atendimentoRealizado;
	}
	public void setAtendimentoRealizado(boolean atendimentoRealizado) {
		this.atendimentoRealizado = atendimentoRealizado;
	}
	public boolean isExcluido() {
		return excluido;
	}
	public void setExcluido(boolean excluido) {
		this.excluido = excluido;
	}
}
