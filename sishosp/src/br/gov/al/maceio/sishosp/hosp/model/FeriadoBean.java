package br.gov.al.maceio.sishosp.hosp.model;

import java.io.Serializable;
import java.sql.Date;

public class FeriadoBean implements Serializable {

	private Integer codFeriado;
	private String descFeriado;
	private Date dataFeriado;

	public FeriadoBean() {

	}

	public Integer getCodFeriado() {
		return codFeriado;
	}

	public String getDescFeriado() {
		return descFeriado;
	}

	public void setCodFeriado(Integer codFeriado) {
		this.codFeriado = codFeriado;
	}

	public void setDescFeriado(String descFeriado) {
		this.descFeriado = descFeriado;
	}

	public Date getDataFeriado() {
		return dataFeriado;
	}

	public void setDataFeriado(Date dataFeriado) {
		this.dataFeriado = dataFeriado;
	}

}
