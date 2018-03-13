package br.gov.al.maceio.sishosp.acl.model;

import java.io.Serializable;

public class Permissoes implements Serializable {

	private Integer idSistema;
	private String descSistema;

	private Integer idPermissao;
	private String descPermissao;

	private Integer idRotina;
	private String descRotina;

	private String codigoFuncao;
	private Boolean funcaoAtiva;
	private Integer idSistemaFunc;

	private Integer idPerfil;
	private String descPerfil;

	private Menu menu;
	private Rotina rotina;

	public Permissoes() {

	}

	public Permissoes(Integer idSistema, String descSistema,
			Integer idPermissao, String descPermissao, Integer idRotina,
			String descRotina, String codigoFuncao, Boolean funcaoAtiva,
			Integer idSistemaFunc, Integer idPerfil, String descPerfil,
			Menu menu, Rotina rotina) {
		this.idSistema = idSistema;
		this.descSistema = descSistema;
		this.idPermissao = idPermissao;
		this.descPermissao = descPermissao;
		this.idRotina = idRotina;
		this.descRotina = descRotina;
		this.codigoFuncao = codigoFuncao;
		this.funcaoAtiva = funcaoAtiva;
		this.idSistemaFunc = idSistemaFunc;
		this.idPerfil = idPerfil;
		this.descPerfil = descPerfil;
		this.menu = menu;
		this.rotina = rotina;
	}

	public Integer getIdSistema() {
		return idSistema;
	}

	public void setIdSistema(Integer idSistema) {
		this.idSistema = idSistema;
	}

	public String getDescSistema() {
		return descSistema;
	}

	public void setDescSistema(String descSistema) {
		this.descSistema = descSistema;
	}

	public Integer getIdPermissao() {
		return idPermissao;
	}

	public void setIdPermissao(Integer idPermissao) {
		this.idPermissao = idPermissao;
	}

	public String getDescPermissao() {
		return descPermissao;
	}

	public void setDescPermissao(String descPermissao) {
		this.descPermissao = descPermissao;
	}

	public Integer getIdRotina() {
		return idRotina;
	}

	public void setIdRotina(Integer idRotina) {
		this.idRotina = idRotina;
	}

	public String getDescRotina() {
		return descRotina;
	}

	public void setDescRotina(String descRotina) {
		this.descRotina = descRotina;
	}

	public String getCodigoFuncao() {
		return codigoFuncao;
	}

	public void setCodigoFuncao(String codigoFuncao) {
		this.codigoFuncao = codigoFuncao;
	}

	public Boolean getFuncaoAtiva() {
		return funcaoAtiva;
	}

	public void setFuncaoAtiva(Boolean funcaoAtiva) {
		this.funcaoAtiva = funcaoAtiva;
	}

	public Integer getIdSistemaFunc() {
		return idSistemaFunc;
	}

	public void setIdSistemaFunc(Integer idSistemaFunc) {
		this.idSistemaFunc = idSistemaFunc;
	}

	public Integer getIdPerfil() {
		return idPerfil;
	}

	public void setIdPerfil(Integer idPerfil) {
		this.idPerfil = idPerfil;
	}

	public String getDescPerfil() {
		return descPerfil;
	}

	public void setDescPerfil(String descPerfil) {
		this.descPerfil = descPerfil;
	}

	public Menu getMenu() {
		return menu;
	}

	public void setMenu(Menu menu) {
		this.menu = menu;
	}

	public Rotina getRotina() {
		return rotina;
	}

	public void setRotina(Rotina rotina) {
		this.rotina = rotina;
	}
}