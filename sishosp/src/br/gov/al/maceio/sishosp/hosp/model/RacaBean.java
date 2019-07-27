package br.gov.al.maceio.sishosp.hosp.model;

import java.io.Serializable;

public class RacaBean implements Serializable {

	private Integer codRaca;
	private String descRaca;
	private String codigoIbge;

	public RacaBean() {

	}

	public Integer getCodRaca() {
		return codRaca;
	}

	public void setCodRaca(Integer codRaca) {
		this.codRaca = codRaca;
	}

	public String getDescRaca() {
		return descRaca;
	}

	public void setDescRaca(String descRaca) {
		this.descRaca = descRaca;
	}

	public String getCodigoIbge() {
		return codigoIbge;
	}

	public void setCodigoIbge(String codigoIbge) {
		this.codigoIbge = codigoIbge;
	}

}
