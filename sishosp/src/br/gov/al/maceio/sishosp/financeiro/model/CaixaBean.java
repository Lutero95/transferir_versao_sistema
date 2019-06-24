package br.gov.al.maceio.sishosp.financeiro.model;

import java.io.Serializable;
import java.sql.Time;

import br.gov.al.maceio.sishosp.acl.model.FuncionarioBean;

public class CaixaBean implements Serializable {

	public CaixaBean() {
		super();
		funcPrestacaoContas = new FuncionarioBean();
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer codCaixaLoja;
	private Integer codResponsavel;
	private String turno;
	private String situacao;
	private Time horaAbertura;
	private String horaFechamento;
	private Double saldoInicial;
	private Double saldoFinal;
	private java.util.Date dataAbertura;
	private java.util.Date dataFechamento;
	private String numcaixa;
	private Double totalCreditos, totalDebitos;

	private Double valorRecebido;
	private Double valorDevido;
	private String historico;
	private String prestacaocontas;
	private FuncionarioBean funcPrestacaoContas;

	public Integer getCodCaixaLoja() {
		return codCaixaLoja;
	}

	public void setCodCaixaLoja(Integer codCaixaLoja) {
		this.codCaixaLoja = codCaixaLoja;
	}

	public Integer getCodResponsavel() {
		return codResponsavel;
	}

	public void setCodResponsavel(Integer codResponsavel) {
		this.codResponsavel = codResponsavel;
	}

	public String getTurno() {
		return turno;
	}

	public void setTurno(String turno) {
		this.turno = turno;
	}

	public String getSituacao() {
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	public Time getHoraAbertura() {
		return horaAbertura;
	}

	public void setHoraAbertura(Time horaAbertura) {
		this.horaAbertura = horaAbertura;
	}

	public String getHoraFechamento() {
		return horaFechamento;
	}

	public void setHoraFechamento(String horaFechamento) {
		this.horaFechamento = horaFechamento;
	}

	public Double getSaldoInicial() {
		return saldoInicial;
	}

	public void setSaldoInicial(Double saldoInicial) {
		this.saldoInicial = saldoInicial;
	}

	public Double getSaldoFinal() {
		return saldoFinal;
	}

	public void setSaldoFinal(Double saldoFinal) {
		this.saldoFinal = saldoFinal;
	}

	public java.util.Date getDataAbertura() {
		return dataAbertura;
	}

	public void setDataAbertura(java.util.Date dataAbertura) {
		this.dataAbertura = dataAbertura;
	}

	public java.util.Date getDataFechamento() {
		return dataFechamento;
	}

	public void setDataFechamento(java.util.Date dataFechamento) {
		this.dataFechamento = dataFechamento;
	}

	public Double getValorRecebido() {
		return valorRecebido;
	}

	public void setValorRecebido(Double valorRecebido) {
		this.valorRecebido = valorRecebido;
	}

	public Double getValorDevido() {
		return valorDevido;
	}

	public void setValorDevido(Double valorDevido) {
		this.valorDevido = valorDevido;
	}

	public String getHistorico() {
		return historico;
	}

	public void setHistorico(String historico) {
		this.historico = historico;
	}

	public String getNumcaixa() {
		return numcaixa;
	}

	public void setNumcaixa(String numcaixa) {
		this.numcaixa = numcaixa;
	}

	public Double getTotalCreditos() {
		return totalCreditos;
	}

	public Double getTotalDebitos() {
		return totalDebitos;
	}

	public void setTotalCreditos(Double totalCreditos) {
		this.totalCreditos = totalCreditos;
	}

	public void setTotalDebitos(Double totalDebitos) {
		this.totalDebitos = totalDebitos;
	}

	public String getPrestacaocontas() {
		return prestacaocontas;
	}

	public FuncionarioBean getFuncPrestacaoContas() {
		return funcPrestacaoContas;
	}

	public void setPrestacaocontas(String prestacaocontas) {
		this.prestacaocontas = prestacaocontas;
	}

	public void setFuncPrestacaoContas(FuncionarioBean funcPrestacaoContas) {
		this.funcPrestacaoContas = funcPrestacaoContas;
	}

}
