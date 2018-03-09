package br.gov.al.maceio.sishosp.hosp.model;

import java.io.Serializable;
import java.sql.Date;

public class EncaminhamentoBean implements Serializable {

	private Integer cod;
	private Integer codpaciente;
	private Integer codfornecedor;
	private Integer codproc1;
	private Integer codproc2;
	private Integer codproc3;
	private String tipo1;
	private String tipo2;
	private String tipo3;
	private String especificacao;
	private Date dataentrada;
	private Date dataencaminhado;
	private Date dataentrega;
	private Integer codprograma;
	private Date datarecebido;
	private String sta;

	public EncaminhamentoBean() {

	}

	public Integer getCod() {
		return cod;
	}

	public void setCod(Integer cod) {
		this.cod = cod;
	}

	public Integer getCodpaciente() {
		return codpaciente;
	}

	public void setCodpaciente(Integer codpaciente) {
		this.codpaciente = codpaciente;
	}

	public Integer getCodfornecedor() {
		return codfornecedor;
	}

	public void setCodfornecedor(Integer codfornecedor) {
		this.codfornecedor = codfornecedor;
	}

	public Integer getCodproc1() {
		return codproc1;
	}

	public void setCodproc1(Integer codproc1) {
		this.codproc1 = codproc1;
	}

	public Integer getCodproc2() {
		return codproc2;
	}

	public void setCodproc2(Integer codproc2) {
		this.codproc2 = codproc2;
	}

	public Integer getCodproc3() {
		return codproc3;
	}

	public void setCodproc3(Integer codproc3) {
		this.codproc3 = codproc3;
	}

	public String getTipo1() {
		return tipo1;
	}

	public void setTipo1(String tipo1) {
		this.tipo1 = tipo1;
	}

	public String getTipo2() {
		return tipo2;
	}

	public void setTipo2(String tipo2) {
		this.tipo2 = tipo2;
	}

	public String getTipo3() {
		return tipo3;
	}

	public void setTipo3(String tipo3) {
		this.tipo3 = tipo3;
	}

	public String getEspecificacao() {
		return especificacao;
	}

	public void setEspecificacao(String especificacao) {
		this.especificacao = especificacao;
	}

	public Date getDataentrada() {
		return dataentrada;
	}

	public void setDataentrada(Date dataentrada) {
		this.dataentrada = dataentrada;
	}

	public Date getDataencaminhado() {
		return dataencaminhado;
	}

	public void setDataencaminhado(Date dataencaminhado) {
		this.dataencaminhado = dataencaminhado;
	}

	public Date getDataentrega() {
		return dataentrega;
	}

	public void setDataentrega(Date dataentrega) {
		this.dataentrega = dataentrega;
	}

	public Integer getCodprograma() {
		return codprograma;
	}

	public void setCodprograma(Integer codprograma) {
		this.codprograma = codprograma;
	}

	public Date getDatarecebido() {
		return datarecebido;
	}

	public void setDatarecebido(Date datarecebido) {
		this.datarecebido = datarecebido;
	}

	public String getSta() {
		return sta;
	}

	public void setSta(String sta) {
		this.sta = sta;
	}

}
