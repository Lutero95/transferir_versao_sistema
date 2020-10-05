package br.gov.al.maceio.sishosp.hosp.log.model;

import java.util.Date;

public class LogBean extends LogAbstract{

	private String descricao;

	public LogBean() {
		this.descricao = new String();
	}

	public LogBean(Long idFuncionario, String descricao, String rotina) {
		this.descricao = descricao;
		this.idFuncionario = idFuncionario;
		this.rotina = rotina;
	}

	public LogBean(Long idFuncionario, String descricao, String rotina, boolean alterou) {
		this.descricao = descricao;
		this.idFuncionario = idFuncionario;
		this.rotina = rotina;
		setAlteracaoRealizada(alterou);
	}


	public String getDescricao() {
		return descricao;
	}
	public void adicionarDescricao(String campo, String valorAntigos, String valorNovos) {
		this.descricao += campo+ ": Valor Antigo: "+valorAntigos+", Valor Novo: "+valorNovos+"\n";
	}




}
