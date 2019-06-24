package br.gov.al.maceio.sishosp.financeiro.model;

import java.io.Serializable;
import java.sql.Time;
import java.util.Date;

public class CaixaDiarioBean implements Serializable{

	private Integer seqcaixadiario;
	private String status;
	private Date dataCaixaAbertura;
	
	public Integer getSeqcaixadiario() {
		return seqcaixadiario;
	}
	public String getStatus() {
		return status;
	}
	public void setSeqcaixadiario(Integer seqcaixadiario) {
		this.seqcaixadiario = seqcaixadiario;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Date getDataCaixaAbertura() {
		return dataCaixaAbertura;
	}
	public void setDataCaixaAbertura(Date dataCaixaAbertura) {
		this.dataCaixaAbertura = dataCaixaAbertura;
	}
	}
