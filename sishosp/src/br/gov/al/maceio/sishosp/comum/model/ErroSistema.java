package br.gov.al.maceio.sishosp.comum.model;

import java.util.Date;

public class ErroSistema {
	
	private Integer id;
	private String descricao;
	private Date dataHoraErro;
	private Long idUsuarioLogado;
	private String banco;
	private String nomeClasse;
	private String nomeMetodo;
	private Integer linhaErro;
	
	
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
	public Date getDataHoraErro() {
		return dataHoraErro;
	}
	public void setDataHoraErro(Date dataHoraErro) {
		this.dataHoraErro = dataHoraErro;
	}
	public Long getIdUsuarioLogado() {
		return idUsuarioLogado;
	}
	public void setIdUsuarioLogado(Long idUsuarioLogado) {
		this.idUsuarioLogado = idUsuarioLogado;
	}
	public String getBanco() {
		return banco;
	}
	public void setBanco(String banco) {
		this.banco = banco;
	}
	public String getNomeClasse() {
		return nomeClasse;
	}
	public void setNomeClasse(String nomeClasse) {
		this.nomeClasse = nomeClasse;
	}
	public String getNomeMetodo() {
		return nomeMetodo;
	}
	public void setNomeMetodo(String nomeMetodo) {
		this.nomeMetodo = nomeMetodo;
	}
	public Integer getLinhaErro() {
		return linhaErro;
	}
	public void setLinhaErro(Integer linhaErro) {
		this.linhaErro = linhaErro;
	}
}
