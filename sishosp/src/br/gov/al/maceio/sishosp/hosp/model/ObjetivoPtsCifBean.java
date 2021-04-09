package br.gov.al.maceio.sishosp.hosp.model;

public class ObjetivoPtsCifBean {
	
	private Integer id;
	private String objetivo;
	private String intervencao;
	private String formaAvaliacao;
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getObjetivo() {
		return objetivo;
	}
	public void setObjetivo(String objetivo) {
		this.objetivo = objetivo;
	}
	public String getIntervencao() {
		return intervencao;
	}
	public void setIntervencao(String intervencao) {
		this.intervencao = intervencao;
	}
	public String getFormaAvaliacao() {
		return formaAvaliacao;
	}
	public void setFormaAvaliacao(String formaAvaliacao) {
		this.formaAvaliacao = formaAvaliacao;
	}
}
