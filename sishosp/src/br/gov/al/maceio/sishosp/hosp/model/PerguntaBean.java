package br.gov.al.maceio.sishosp.hosp.model;

import java.util.ArrayList;
import java.util.List;

public class PerguntaBean {

	private Integer id;
	private String descricao;
	List<RespostaBean> respostas;
	
	public PerguntaBean() {
		respostas = new ArrayList<>();
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
	public List<RespostaBean> getRespostas() {
		return respostas;
	}
	public void setRespostas(List<RespostaBean> respostas) {
		this.respostas = respostas;
	}
	
}
