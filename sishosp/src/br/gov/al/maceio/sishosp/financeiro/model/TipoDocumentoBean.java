package br.gov.al.maceio.sishosp.financeiro.model;

import java.io.Serializable;

public class TipoDocumentoBean implements Serializable {


	private Integer codtipodocumento;
	private String descricao;
	private Boolean devolucao_venda;
	private Boolean cheque;
	private Boolean cartaoDebito;
	private Boolean cartaoCredito;
	private Boolean promissoria;
	
	
	
	public Integer getCodtipodocumento() {
		return codtipodocumento;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setCodtipodocumento(Integer codtipodocumento) {
		this.codtipodocumento = codtipodocumento;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public Boolean getDevolucao_venda() {
		return devolucao_venda;
	}
	public void setDevolucao_venda(Boolean devolucao_venda) {
		this.devolucao_venda = devolucao_venda;
	}
	public Boolean getCheque() {
		return cheque;
	}
	public void setCheque(Boolean cheque) {
		this.cheque = cheque;
	}
	public Boolean getCartaoDebito() {
		return cartaoDebito;
	}
	public void setCartaoDebito(Boolean cartaoDebito) {
		this.cartaoDebito = cartaoDebito;
	}
	public Boolean getCartaoCredito() {
		return cartaoCredito;
	}
	public void setCartaoCredito(Boolean cartaoCredito) {
		this.cartaoCredito = cartaoCredito;
	}
	public Boolean getPromissoria() {
		return promissoria;
	}
	public void setPromissoria(Boolean promissoria) {
		this.promissoria = promissoria;
	}
}
