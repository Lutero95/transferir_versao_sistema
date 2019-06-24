package br.gov.al.maceio.sishosp.financeiro.model;

import java.io.Serializable;
import java.util.Date;

import br.gov.al.maceio.sishosp.acl.model.FuncionarioBean;



public class TituloPagarBaixaBean  implements Serializable {


	private Integer codigo;
	private Integer ord;
	private Date dtpagto;
	private double valoratual;
	private double valorpago;
	private String tpbaixa;
	private String frbaixa;
	private double vlrdesc;
	private double vlrjuros;
	private double vlrmulta;
	private BancoBean banco;
	private String numcheque;
	private FuncionarioBean func;
	private Date dtcadastro;
	private String autpag;
	private String obs;
	private Integer codchqemitido;

	public TituloPagarBaixaBean() {

		banco = new BancoBean();
		func = new FuncionarioBean();
				
	}

	public Integer getCodigo() {
		return codigo;
	}

	public Integer getOrd() {
		return ord;
	}

	public Date getDtpagto() {
		return dtpagto;
	}

	public double getValoratual() {
		return valoratual;
	}

	public double getValorpago() {
		return valorpago;
	}

	public String getTpbaixa() {
		return tpbaixa;
	}

	public String getFrbaixa() {
		return frbaixa;
	}

	public double getVlrdesc() {
		return vlrdesc;
	}

	public double getVlrjuros() {
		return vlrjuros;
	}

	public double getVlrmulta() {
		return vlrmulta;
	}

	public BancoBean getBanco() {
		return banco;
	}

	public String getNumcheque() {
		return numcheque;
	}

	public FuncionarioBean getFunc() {
		return func;
	}

	public Date getDtcadastro() {
		return dtcadastro;
	}

	public String getAutpag() {
		return autpag;
	}

	public String getObs() {
		return obs;
	}

	public Integer getCodchqemitido() {
		return codchqemitido;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public void setOrd(Integer ord) {
		this.ord = ord;
	}

	public void setDtpagto(Date dtpagto) {
		this.dtpagto = dtpagto;
	}

	public void setValoratual(double valoratual) {
		this.valoratual = valoratual;
	}

	public void setValorpago(double valorpago) {
		this.valorpago = valorpago;
	}

	public void setTpbaixa(String tpbaixa) {
		this.tpbaixa = tpbaixa;
	}

	public void setFrbaixa(String frbaixa) {
		this.frbaixa = frbaixa;
	}

	public void setVlrdesc(double vlrdesc) {
		this.vlrdesc = vlrdesc;
	}

	public void setVlrjuros(double vlrjuros) {
		this.vlrjuros = vlrjuros;
	}

	public void setVlrmulta(double vlrmulta) {
		this.vlrmulta = vlrmulta;
	}

	public void setBanco(BancoBean banco) {
		this.banco = banco;
	}

	public void setNumcheque(String numcheque) {
		this.numcheque = numcheque;
	}

	public void setFunc(FuncionarioBean func) {
		this.func = func;
	}

	public void setDtcadastro(Date dtcadastro) {
		this.dtcadastro = dtcadastro;
	}

	public void setAutpag(String autpag) {
		this.autpag = autpag;
	}

	public void setObs(String obs) {
		this.obs = obs;
	}

	public void setCodchqemitido(Integer codchqemitido) {
		this.codchqemitido = codchqemitido;
	}

	


}
