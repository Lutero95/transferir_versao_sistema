package br.gov.al.maceio.sishosp.hosp.model;

public class PerguntaBean {

	private Integer id;
	private String descricao;
	private RespostaBean resposta;
	
	public PerguntaBean() {
		resposta = new RespostaBean();
	}
	
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

	public RespostaBean getResposta() {
		return resposta;
	}

	public void setResposta(RespostaBean resposta) {
		this.resposta = resposta;
	}
}
