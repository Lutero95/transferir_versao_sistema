package br.gov.al.maceio.sishosp.hosp.log.model;

import java.util.Date;


public class LogBean {
	
	private Integer id;
	private Long idFuncionario;
	private Date dataHora;
	private String descricao;
	private String rotina;
	
	public LogBean() {
		this.descricao = new String();
	}
	
	public LogBean(Long idFuncionario, String descricao, String rotina) {
		this.descricao = descricao;
		this.idFuncionario = idFuncionario;
		this.rotina = rotina;
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Long getIdFuncionario() {
		return idFuncionario;
	}
	public void setIdFuncionario(Long idFuncionario) {
		this.idFuncionario = idFuncionario;
	}
	public Date getDataHora() {
		return dataHora;
	}
	public void setDataHora(Date dataHora) {
		this.dataHora = dataHora;
	}
	public String getDescricao() {
		return descricao;
	}
	public void adicionarDescricao(String campo, String valorAntigos, String valorNovos) {
		this.descricao += campo+ ": Valor Antigo: "+valorAntigos+", Valor Novo: "+valorNovos+"\n";
	}

	public String getRotina() {
		return rotina;
	}

	public void setRotina(String rotina) {
		this.rotina = rotina;
	}
	
}
