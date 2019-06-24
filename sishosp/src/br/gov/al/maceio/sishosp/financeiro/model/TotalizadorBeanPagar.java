package br.gov.al.maceio.sishosp.financeiro.model;

import java.io.Serializable;
import java.util.Date;

public class TotalizadorBeanPagar implements Serializable{
	private double totalgeral, totalvaloraberto, totalpago, totalabertovencido, totalabertovencer ;

	public double getTotalgeral() {
		return totalgeral;
	}

	public void setTotalgeral(double totalgeral) {
		this.totalgeral = totalgeral;
	}

	public double getTotalvaloraberto() {
		return totalvaloraberto;
	}

	public void setTotalvaloraberto(double totalvaloraberto) {
		this.totalvaloraberto = totalvaloraberto;
	}

	public double getTotalpago() {
		return totalpago;
	}

	public void setTotalpago(double totalpago) {
		this.totalpago = totalpago;
	}

	public double getTotalabertovencido() {
		return totalabertovencido;
	}

	public void setTotalabertovencido(double totalabertovencido) {
		this.totalabertovencido = totalabertovencido;
	}

	public double getTotalabertovencer() {
		return totalabertovencer;
	}

	public void setTotalabertovencer(double totalabertovencer) {
		this.totalabertovencer = totalabertovencer;
	} 
		
}
