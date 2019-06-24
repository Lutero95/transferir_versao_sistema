package br.gov.al.maceio.sishosp.financeiro.model;

import java.io.Serializable;
import java.util.Date;

public class ChequeRecebidoBean implements Serializable{

	public ChequeRecebidoBean() {
		caixa = new CaixaDiarioBean();	
		banco = new BancoBean();
		portador = new PortadorBean();
		

	}
	private Integer codcheque;
	private BancoBean banco;
	
	private String numcheque;
	private Date dtemissao;
	private double valor;
	private String nominal;
	private String compensado;
	private String status;
	private Date dtvencimento;
	private Date dtcompensado;
	private Date dtdeposito;
	private Date dtdevolucao;
	private Date dtreapresentacao;
	private CaixaDiarioBean caixa;
	private PortadorBean portador;
	
	
	public Integer getCodcheque() {
		return codcheque;
	}
	public BancoBean getBanco() {
		return banco;
	}
	
	public String getNumcheque() {
		return numcheque;
	}
	public Date getDtemissao() {
		return dtemissao;
	}
	
	public double getValor() {
		return valor;
	}
	public String getNominal() {
		return nominal;
	}
	
	public String getCompensado() {
		return compensado;
	}
	public String getStatus() {
		return status;
	}
	public Date getDtvencimento() {
		return dtvencimento;
	}
	public Date getDtcompensado() {
		return dtcompensado;
	}
	public CaixaDiarioBean getCaixa() {
		return caixa;
	}
	public void setCodcheque(Integer codcheque) {
		this.codcheque = codcheque;
	}
	public void setBanco(BancoBean banco) {
		this.banco = banco;
	}

	public void setNumcheque(String numcheque) {
		this.numcheque = numcheque;
	}
	public void setDtemissao(Date dtemissao) {
		this.dtemissao = dtemissao;
	}
	
	public void setValor(double valor) {
		this.valor = valor;
	}
	public void setNominal(String nominal) {
		this.nominal = nominal;
	}
	
	public void setCompensado(String compensado) {
		this.compensado = compensado;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public void setDtvencimento(Date dtvencimento) {
		this.dtvencimento = dtvencimento;
	}
	public void setDtcompensado(Date dtcompensado) {
		this.dtcompensado = dtcompensado;
	}
	public void setCaixa(CaixaDiarioBean caixa) {
		this.caixa = caixa;
	}
	public PortadorBean getPortador() {
		return portador;
	}
	public void setPortador(PortadorBean portador) {
		this.portador = portador;
	}

	public Date getDtdeposito() {
		return dtdeposito;
	}
	public void setDtdeposito(Date dtdeposito) {
		this.dtdeposito = dtdeposito;
	}
	public Date getDtdevolucao() {
		return dtdevolucao;
	}
	public void setDtdevolucao(Date dtdevolucao) {
		this.dtdevolucao = dtdevolucao;
	}
	public Date getDtreapresentacao() {
		return dtreapresentacao;
	}
	public void setDtreapresentacao(Date dtreapresentacao) {
		this.dtreapresentacao = dtreapresentacao;
	}
	
	

}
