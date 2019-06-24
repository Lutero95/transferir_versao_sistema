package br.gov.al.maceio.sishosp.financeiro.model;

import java.io.Serializable;
import java.util.Date;

import br.gov.al.maceio.sishosp.acl.model.FuncionarioBean;

public class TesourariaBean implements Serializable{

	
	private Integer codseq;
	private BancoBean banco;
	private Date dtmovimento;
	private double valor;
	private String complemento;
	private String tipo;
	private String ativo;
	private FuncionarioBean func;
	private Integer codreceber;
	private Integer codpagar;
	private Integer seqcaixadiario;
	private String obs;
	private String operacao;
	private CaixaDiarioBean caixa;
	private CaixaBean caixaloja;
	
	public TesourariaBean() {
		func = new FuncionarioBean();
		banco = new BancoBean();
		caixa = new CaixaDiarioBean();
		caixaloja = new CaixaBean();
		// TODO Auto-generated constructor stub
	}


	public Integer getCodseq() {
		return codseq;
	}


	public void setCodseq(Integer codseq) {
		this.codseq = codseq;
	}


	


	public Date getDtmovimento() {
		return dtmovimento;
	}


	public void setDtmovimento(Date dtmovimento) {
		this.dtmovimento = dtmovimento;
	}


	public double getValor() {
		return valor;
	}


	public void setValor(double valor) {
		this.valor = valor;
	}


	public String getComplemento() {
		return complemento;
	}


	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}


	public String getTipo() {
		return tipo;
	}


	public void setTipo(String tipo) {
		this.tipo = tipo;
	}


	public String getAtivo() {
		return ativo;
	}


	public void setAtivo(String ativo) {
		this.ativo = ativo;
	}


	public FuncionarioBean getFunc() {
		return func;
	}


	public void setFunc(FuncionarioBean func) {
		this.func = func;
	}


	public Integer getCodreceber() {
		return codreceber;
	}


	public void setCodreceber(Integer codreceber) {
		this.codreceber = codreceber;
	}


	public Integer getCodpagar() {
		return codpagar;
	}


	public void setCodpagar(Integer codpagar) {
		this.codpagar = codpagar;
	}


	public Integer getSeqcaixadiario() {
		return seqcaixadiario;
	}


	public void setSeqcaixadiario(Integer seqcaixadiario) {
		this.seqcaixadiario = seqcaixadiario;
	}




	

	public String getObs() {
		return obs;
	}


	public void setObs(String obs) {
		this.obs = obs;
	}


	public String getOperacao() {
		return operacao;
	}


	public void setOperacao(String operacao) {
		this.operacao = operacao;
	}


	public BancoBean getBanco() {
		return banco;
	}


	public void setBanco(BancoBean banco) {
		this.banco = banco;
	}


	public CaixaDiarioBean getCaixa() {
		return caixa;
	}


	public void setCaixa(CaixaDiarioBean caixa) {
		this.caixa = caixa;
	}


	public CaixaBean getCaixaloja() {
		return caixaloja;
	}


	public void setCaixaloja(CaixaBean caixaloja) {
		this.caixaloja = caixaloja;
	}

}