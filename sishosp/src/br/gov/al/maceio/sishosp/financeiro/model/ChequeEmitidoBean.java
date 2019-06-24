package br.gov.al.maceio.sishosp.financeiro.model;

import java.io.Serializable;
import java.util.Date;

public class ChequeEmitidoBean implements Serializable{

	public ChequeEmitidoBean() {
		caixa = new CaixaDiarioBean();
		banco = new BancoBean();
		portador = new PortadorBean();
		cliente = new ClienteBean();
		tipodocumento = new TipoDocumentoBean();
		caixaLoja = new CaixaBean();
		pcontas = new PrestacaoContasBean();
	}
	private Integer codcheque;
	private BancoBean banco;
	private String numcheque;
	private Date dtemissao;
	private String tipo;
	private double valor;
	private String nominal;
	private String pendente;
	private String compensado;
	private String status;
	private Date dtvencimento;
	private Date dtcompensado;
	private CaixaDiarioBean caixa;
	private PortadorBean portador;
	private ClienteBean cliente;
	private TipoDocumentoBean tipodocumento;
	private Integer parcela;
	private CaixaBean caixaLoja;
	private PrestacaoContasBean pcontas;
	
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
	public String getTipo() {
		return tipo;
	}
	public double getValor() {
		return valor;
	}
	public String getNominal() {
		return nominal;
	}
	public String getPendente() {
		return pendente;
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
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public void setValor(double valor) {
		this.valor = valor;
	}
	public void setNominal(String nominal) {
		this.nominal = nominal;
	}
	public void setPendente(String pendente) {
		this.pendente = pendente;
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
	public ClienteBean getCliente() {
		return cliente;
	}
	public void setCliente(ClienteBean cliente) {
		this.cliente = cliente;
	}
	public TipoDocumentoBean getTipodocumento() {
		return tipodocumento;
	}
	public void setTipodocumento(TipoDocumentoBean tipodocumento) {
		this.tipodocumento = tipodocumento;
	}
	public Integer getParcela() {
		return parcela;
	}
	public void setParcela(Integer parcela) {
		this.parcela = parcela;
	}
	public CaixaBean getCaixaLoja() {
		return caixaLoja;
	}
	public void setCaixaLoja(CaixaBean caixaLoja) {
		this.caixaLoja = caixaLoja;
	}
	public PrestacaoContasBean getPcontas() {
		return pcontas;
	}
	public void setPcontas(PrestacaoContasBean pcontas) {
		this.pcontas = pcontas;
	}
	

}
