package br.gov.al.maceio.sishosp.financeiro.model;

import java.io.Serializable;

public class BuscaBeanFornecedor implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6811779633741481011L;
	private String valor;
	private String ordenacao, tipoBusca;
	public String getValor() {
		return valor;
	}
	public void setValor(String valor) {
		this.valor = valor;
	}
	public String getOrdenacao() {
		return ordenacao;
	}
	public void setOrdenacao(String ordenacao) {
		this.ordenacao = ordenacao;
	}
	public String getTipoBusca() {
		return tipoBusca;
	}
	public void setTipoBusca(String tipoBusca) {
		this.tipoBusca = tipoBusca;
	}
	
		
}
