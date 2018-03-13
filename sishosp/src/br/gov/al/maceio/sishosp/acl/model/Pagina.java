package br.gov.al.maceio.sishosp.acl.model;

import java.io.Serializable;

public class Pagina implements Serializable {

	private Long id;
	private String descricao;
	private String url;

	public Pagina() {

	}

	public Pagina(Long id, String descricao, String url) {
		this.id = id;
		this.descricao = descricao;
		this.url = url;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}