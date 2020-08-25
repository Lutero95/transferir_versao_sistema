package br.gov.al.maceio.sishosp.hosp.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PesquisaBean {
	
	private Integer id;
	private String titulo;
	private Date dataInicial;
	private Date dataFinal;
	private List<PerguntaBean> perguntas;
	
	public PesquisaBean() {
		perguntas = new ArrayList<>();
	}
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	public Date getDataInicial() {
		return dataInicial;
	}
	public void setDataInicial(Date dataInicial) {
		this.dataInicial = dataInicial;
	}
	public Date getDataFinal() {
		return dataFinal;
	}
	public void setDataFinal(Date dataFinal) {
		this.dataFinal = dataFinal;
	}
	public List<PerguntaBean> getPerguntas() {
		return perguntas;
	}
	public void setPerguntas(List<PerguntaBean> perguntas) {
		this.perguntas = perguntas;
	}
	
}
