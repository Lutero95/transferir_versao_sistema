package br.gov.al.maceio.sishosp.financeiro.model;

import java.io.Serializable;
import java.util.Date;

public class NegociarBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private ClienteBean cliente;
	private Double multa;
	private Double juros;
	private String tipo;
	private Double totalAberto;
	private Double totalFinal;
	private Date dtNegociacao;
	private String situacao;
	private Integer opcad;
	private Double multaInicial;
	private Double multaFinal;
	private Integer qtdParc;
	private String pagVista;
	private Double abatimenCargos;

	private Date competencia;

	public NegociarBean() {
		cliente = new ClienteBean();
	}

	public ClienteBean getCliente() {
		return cliente;
	}

	public void setCliente(ClienteBean cliente) {
		this.cliente = cliente;
	}

	public Double getMulta() {
		return multa;
	}

	public void setMulta(Double multa) {
		this.multa = multa;
	}

	public Double getJuros() {
		return juros;
	}

	public void setJuros(Double juros) {
		this.juros = juros;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public Double getTotalAberto() {
		return totalAberto;
	}

	public void setTotalAberto(Double totalAberto) {
		this.totalAberto = totalAberto;
	}

	public Double getTotalFinal() {
		return totalFinal;
	}

	public void setTotalFinal(Double totalFinal) {
		this.totalFinal = totalFinal;
	}

	public Date getDtNegociacao() {
		return dtNegociacao;
	}

	public void setDtNegociacao(Date dtNegociacao) {
		this.dtNegociacao = dtNegociacao;
	}

	public String getSituacao() {
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	public Integer getOpcad() {
		return opcad;
	}

	public void setOpcad(Integer opcad) {
		this.opcad = opcad;
	}

	public Double getMultaInicial() {
		return multaInicial;
	}

	public void setMultaInicial(Double multaInicial) {
		this.multaInicial = multaInicial;
	}

	public Double getMultaFinal() {
		return multaFinal;
	}

	public void setMultaFinal(Double multaFinal) {
		this.multaFinal = multaFinal;
	}

	public Integer getQtdParc() {
		return qtdParc;
	}

	public void setQtdParc(Integer qtdParc) {
		this.qtdParc = qtdParc;
	}

	public String getPagVista() {
		return pagVista;
	}

	public void setPagVista(String pagVista) {
		this.pagVista = pagVista;
	}

	public Double getAbatimenCargos() {
		return abatimenCargos;
	}

	public void setAbatimenCargos(Double abatimenCargos) {
		this.abatimenCargos = abatimenCargos;
	}

	public Date getCompetencia() {
		return competencia;
	}

	public void setCompetencia(Date competencia) {
		this.competencia = competencia;
	}

}
