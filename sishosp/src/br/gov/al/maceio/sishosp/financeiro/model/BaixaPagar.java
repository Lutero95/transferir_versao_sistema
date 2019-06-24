package br.gov.al.maceio.sishosp.financeiro.model;

import java.io.Serializable;
import java.util.Date;

import br.gov.al.maceio.sishosp.acl.model.FuncionarioBean;

public class BaixaPagar implements Serializable {

	public BaixaPagar() {
		banco = new BancoBean();
		func = new FuncionarioBean();
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer duplicata;
	private Date dtPagamento;
	private Integer tipoBaixa;
	private String tipoBaixaString;
	private Double vlrAtual;
	private Double desconto;
	private String formaBaixa;
	private Double juros;
	private String contaBancaria;
	private Double multa;
	private String correcao;
	private String retencao;
	private Double valorRecebido;
	private Double valorBaixado;
	private Double vlrCorrecao;
	private BancoBean banco;
	private FuncionarioBean func;
	private Integer ord;
	private Integer id;
	private Integer codChequeEmitido;


	private String idBancoString;

	private Integer codSeqCaixa;

	public Date getdtPagamento() {
		return dtPagamento;
	}

	public void setdtPagamento(Date dtPagamento) {
		this.dtPagamento = dtPagamento;
	}

	public Integer getTipoBaixa() {
		return tipoBaixa;
	}

	public void setTipoBaixa(Integer tipoBaixa) {
		this.tipoBaixa = tipoBaixa;
	}

	public String getTipoBaixaString() {
		return tipoBaixaString;
	}

	public void setTipoBaixaString(String tipoBaixaString) {
		this.tipoBaixaString = tipoBaixaString;
	}

	public Double getVlrAtual() {
		return vlrAtual;
	}

	public void setVlrAtual(Double vlrAtual) {
		this.vlrAtual = vlrAtual;
	}

	public Double getDesconto() {
		return desconto;
	}

	public void setDesconto(Double desconto) {
		this.desconto = desconto;
	}

	public String getFormaBaixa() {
		return formaBaixa;
	}

	public void setFormaBaixa(String formaBaixa) {
		this.formaBaixa = formaBaixa;
	}

	public Double getJuros() {
		return juros;
	}

	public void setJuros(Double juros) {
		this.juros = juros;
	}

	public String getContaBancaria() {
		return contaBancaria;
	}

	public void setContaBancaria(String contaBancaria) {
		this.contaBancaria = contaBancaria;
	}

	public Double getMulta() {
		return multa;
	}

	public void setMulta(Double multa) {
		this.multa = multa;
	}

	public String getCorrecao() {
		return correcao;
	}

	public void setCorrecao(String correcao) {
		this.correcao = correcao;
	}

	public String getRetencao() {
		return retencao;
	}

	public void setRetencao(String retencao) {
		this.retencao = retencao;
	}

	public Double getValorRecebido() {
		return valorRecebido;
	}

	public void setValorRecebido(Double valorRecebido) {
		this.valorRecebido = valorRecebido;
	}

	public Double getValorBaixado() {
		return valorBaixado;
	}

	public void setValorBaixado(Double valorBaixado) {
		this.valorBaixado = valorBaixado;
	}

	public Integer getDuplicata() {
		return duplicata;
	}

	public void setDuplicata(Integer duplicata) {
		this.duplicata = duplicata;
	}

	public Double getVlrCorrecao() {
		return vlrCorrecao;
	}

	public void setVlrCorrecao(Double vlrCorrecao) {
		this.vlrCorrecao = vlrCorrecao;
	}



	public String getIdBancoString() {
		return idBancoString;
	}

	public void setIdBancoString(String idBancoString) {
		this.idBancoString = idBancoString;
	}

	public Integer getCodSeqCaixa() {
		return codSeqCaixa;
	}

	public void setCodSeqCaixa(Integer codSeqCaixa) {
		this.codSeqCaixa = codSeqCaixa;
	}

	public BancoBean getBanco() {
		return banco;
	}

	public void setBanco(BancoBean banco) {
		this.banco = banco;
	}

	public FuncionarioBean getFunc() {
		return func;
	}

	public void setFunc(FuncionarioBean func) {
		this.func = func;
	}

	public Date getDtPagamento() {
		return dtPagamento;
	}

	public void setDtPagamento(Date dtPagamento) {
		this.dtPagamento = dtPagamento;
	}

	public Integer getOrd() {
		return ord;
	}

	public void setOrd(Integer ord) {
		this.ord = ord;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getCodChequeEmitido() {
		return codChequeEmitido;
	}

	public void setCodChequeEmitido(Integer codChequeEmitido) {
		this.codChequeEmitido = codChequeEmitido;
	}

}
