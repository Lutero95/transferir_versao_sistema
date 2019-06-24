package br.gov.al.maceio.sishosp.financeiro.model;

import java.io.Serializable;

public class CreditoCaixaBean implements Serializable {

	public CreditoCaixaBean() {
		super();
		cliente = new ClienteBean();
		tippag = new TipoPagBean();
		titreceber = new TituloReceberBean();
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer codDeb,codCred, codcaixaloja;
	private String historico;
	private Double valor;
	private java.util.Date dt_confirmado;
	private TipoPagBean tippag;
	private ClienteBean cliente;
	private Integer periodicidade;
	private Integer qtdparc;
	private String estornado;
	private String tipo;
	private Integer codvenda2;
	private TituloReceberBean titreceber;
	
	public Integer getCodDeb() {
		return codDeb;
	}

	public void setCodDeb(Integer codDeb) {
		this.codDeb = codDeb;
	}

	public Integer getCodcaixaloja() {
		return codcaixaloja;
	}

	public void setCodcaixaloja(Integer codcaixaloja) {
		this.codcaixaloja = codcaixaloja;
	}

	public String getHistorico() {
		return historico;
	}

	public void setHistorico(String historico) {
		this.historico = historico;
	}

	public Double getValor() {
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}

	public java.util.Date getDt_confirmado() {
		return dt_confirmado;
	}

	public void setDt_confirmado(java.util.Date dt_confirmado) {
		this.dt_confirmado = dt_confirmado;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public TipoPagBean getTippag() {
		return tippag;
	}

	public ClienteBean getCliente() {
		return cliente;
	}

	public Integer getPeriodicidade() {
		return periodicidade;
	}

	public Integer getQtdparc() {
		return qtdparc;
	}

	public String getEstornado() {
		return estornado;
	}

	public void setTippag(TipoPagBean tippag) {
		this.tippag = tippag;
	}

	public void setCliente(ClienteBean cliente) {
		this.cliente = cliente;
	}

	public void setPeriodicidade(Integer periodicidade) {
		this.periodicidade = periodicidade;
	}

	public void setQtdparc(Integer qtdparc) {
		this.qtdparc = qtdparc;
	}

	public void setEstornado(String estornado) {
		this.estornado = estornado;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public Integer getCodCred() {
		return codCred;
	}

	public void setCodCred(Integer codCred) {
		this.codCred = codCred;
	}

	

	public Integer getCodvenda2() {
		return codvenda2;
	}

	public void setCodvenda2(Integer codvenda2) {
		this.codvenda2 = codvenda2;
	}

	public TituloReceberBean getTitreceber() {
		return titreceber;
	}

	public void setTitreceber(TituloReceberBean titreceber) {
		this.titreceber = titreceber;
	}

}
