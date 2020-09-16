package br.gov.al.maceio.sishosp.hosp.log.model;


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
	

	public String getDescricao() {
		return descricao;
	}
	public void adicionarDescricao(String campo, String valorAntigos, String valorNovos) {
		this.descricao += campo+ ": Valor Antigo: "+valorAntigos+", Valor Novo: "+valorNovos+"\n";
	}

	
}
