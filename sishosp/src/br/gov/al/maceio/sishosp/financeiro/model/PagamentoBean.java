package br.gov.al.maceio.sishosp.financeiro.model;

import java.io.Serializable;
import java.util.Date;

public class PagamentoBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private double valor;
	private Integer id_desistencia;
	private Integer parcela;
	private String descTipo;
	private String idTipo;
	private String tipo;
	private Date dtCadastro;
	private Date vencimento;
	private Double valorEmAberto;
	private Double valorDesconto;
	private Double totalPagar;

	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public double getValor() {
		return valor;
	}
	public Integer getParcela() {
		return parcela;
	}
	public String getDescTipo() {
		return descTipo;
	}
	public String getIdTipo() {
		return idTipo;
	}
	public String getTipo() {
		return tipo;
	}
	public void setValor(double valor) {
		this.valor = valor;
	}
	public void setParcela(Integer parcela) {
		this.parcela = parcela;
	}
	public void setDescTipo(String descTipo) {
		this.descTipo = descTipo;
	}
	public void setIdTipo(String idTipo) {
		this.idTipo = idTipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public Date getDtCadastro() {
		return dtCadastro;
	}
	public void setDtCadastro(Date dtCadastro) {
		this.dtCadastro = dtCadastro;
	}
	public Integer getId_desistencia() {
		return id_desistencia;
	}
	public void setId_desistencia(Integer id_desistencia) {
		this.id_desistencia = id_desistencia;
	}
	public Date getVencimento() {
		return vencimento;
	}
	public void setVencimento(Date vencimento) {
		this.vencimento = vencimento;
	}
	public Double getValorEmAberto() {
		return valorEmAberto;
	}
	public void setValorEmAberto(Double valorEmAberto) {
		this.valorEmAberto = valorEmAberto;
	}
	public Double getValorDesconto() {
		return valorDesconto;
	}
	public void setValorDesconto(Double valorDesconto) {
		this.valorDesconto = valorDesconto;
	}
	public Double getTotalPagar() {
		return totalPagar;
	}
	public void setTotalPagar(Double totalPagar) {
		this.totalPagar = totalPagar;
	}
	


}
