package br.gov.al.maceio.sishosp.acl.model;

import java.io.Serializable;
import java.util.List;

public class Rotina implements Serializable {

	private Long id;
	private String descricao;
	private String descSistema;
	private List<Menu> listaMenusAss;
	private List<Funcao> listaFuncoesAss;

	// Variáveis da url do menu item. Adcionadas para Rotina!
	private String descPagina;
	private String diretorio;
	private String extensao;

	public Rotina() {

	}

	public Rotina(Long id, String descricao, String descSistema) {
		this.id = id;
		this.descricao = descricao;
		this.descSistema = descSistema;
	}

	public Rotina(String descricao, List<Menu> listaMenusAss) {
		this.descricao = descricao;
		this.listaMenusAss = listaMenusAss;
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

	public String getDescSistema() {
		return descSistema;
	}

	public void setDescSistema(String descSistema) {
		this.descSistema = descSistema;
	}

	public List<Menu> getListaMenusAss() {
		return listaMenusAss;
	}

	public void setListaMenusAss(List<Menu> listaMenusAss) {
		this.listaMenusAss = listaMenusAss;
	}

	public List<Funcao> getListaFuncoesAss() {
		return listaFuncoesAss;
	}

	public void setListaFuncoesAss(List<Funcao> listaFuncoesAss) {
		this.listaFuncoesAss = listaFuncoesAss;
	}

	public String getDescPagina() {
		return descPagina;
	}

	public void setDescPagina(String descPagina) {
		this.descPagina = descPagina;
	}

	public String getDiretorio() {
		return diretorio;
	}

	public void setDiretorio(String diretorio) {
		this.diretorio = diretorio;
	}

	public String getExtensao() {
		return extensao;
	}

	public void setExtensao(String extensao) {
		this.extensao = extensao;
	}

}