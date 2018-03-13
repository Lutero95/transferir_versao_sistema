package br.gov.al.maceio.sishosp.acl.model;

import java.io.Serializable;
import java.util.List;

public class Perfil implements Serializable {

	private Long id;
	private String descricao;
	private List<Integer> listaSistemas;
	private List<Long> listaPermissoes;

	public Perfil() {

	}

	public Perfil(Long id, String descricao, List<Integer> listaSistemas,
			List<Long> listaPermissoes) {
		this.id = id;
		this.descricao = descricao;
		this.listaSistemas = listaSistemas;
		this.listaPermissoes = listaPermissoes;
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

	public List<Integer> getListaSistemas() {
		return listaSistemas;
	}

	public void setListaSistemas(List<Integer> listaSistemas) {
		this.listaSistemas = listaSistemas;
	}

	public List<Long> getListaPermissoes() {
		return listaPermissoes;
	}

	public void setListaPermissoes(List<Long> listaPermissoes) {
		this.listaPermissoes = listaPermissoes;
	}

}