package br.gov.al.maceio.sishosp.hosp.model;

import java.io.Serializable;

public class FormaTransporteBean implements Serializable {

	private Integer codformatransporte;
	private String descformatransporte;

	public FormaTransporteBean() {

	}

	public Integer getCodformatransporte() {
		return codformatransporte;
	}

	public void setCodformatransporte(Integer codformatransporte) {
		this.codformatransporte = codformatransporte;
	}

	public String getDescformatransporte() {
		return descformatransporte;
	}

	public void setDescformatransporte(String descformatransporte) {
		this.descformatransporte = descformatransporte;
	}

}
