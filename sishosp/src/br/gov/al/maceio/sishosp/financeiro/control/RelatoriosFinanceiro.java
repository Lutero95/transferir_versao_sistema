package br.gov.al.maceio.sishosp.financeiro.control;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ManagedBean
@ViewScoped
public class RelatoriosFinanceiro implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String valueRendered;

	public RelatoriosFinanceiro() {

		this.valueRendered = "C";

	}

	public String getValueRendered() {
		return valueRendered;
	}

	public void setValueRendered(String valueRendered) {
		this.valueRendered = valueRendered;
	}

}
