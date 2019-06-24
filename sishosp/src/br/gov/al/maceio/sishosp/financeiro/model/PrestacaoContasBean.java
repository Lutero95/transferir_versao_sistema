package br.gov.al.maceio.sishosp.financeiro.model;

import java.io.Serializable;

public class PrestacaoContasBean implements Serializable {
	
	
	
	
	
	public PrestacaoContasBean() {
		super();
		cliente = new ClienteBean();
		tippag = new TipoPagBean();
		titulopagar = new TituloPagarBean();
		tituloreceber = new TituloReceberBean();
		tituloReceberAnterior = new TituloReceberBean();
		caixa = new CaixaBean();
		descfonrec = new FonteReceitaBean();
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer codDeb,codCred, codVenda2;
	private CaixaBean caixa;
	private String historico;
	private boolean medico;
	private Double valor;
	private java.util.Date dt_confirmado;
	private TipoPagBean tippag;
	private ClienteBean cliente;
	private Integer periodicidade;
	private Integer qtdparc;
	private String estornado;
	private String tipo;
	private TituloReceberBean tituloreceber, tituloReceberAnterior;
	private TituloPagarBean titulopagar;
	private String creddeb;
	private FonteReceitaBean descfonrec;
	public Integer getCodDeb() {
	 

	
		return codDeb;
	}

	public void setCodDeb(Integer codDeb) {
		this.codDeb = codDeb;
	}

	public String getHistorico() {
		return historico;
	}

	public void setHistorico(String historico) {
		this.historico = historico;
	}

	public Double getValor() {
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}

	public java.util.Date getDt_confirmado() {
		return dt_confirmado;
	}

	public void setDt_confirmado(java.util.Date dt_confirmado) {
		this.dt_confirmado = dt_confirmado;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public TipoPagBean getTippag() {
		return tippag;
	}

	public ClienteBean getCliente() {
		return cliente;
	}

	public Integer getPeriodicidade() {
		return periodicidade;
	}

	public Integer getQtdparc() {
		return qtdparc;
	}

	public String getEstornado() {
		return estornado;
	}

	public void setTippag(TipoPagBean tippag) {
		this.tippag = tippag;
	}

	public void setCliente(ClienteBean cliente) {
		this.cliente = cliente;
	}

	public void setPeriodicidade(Integer periodicidade) {
		this.periodicidade = periodicidade;
	}

	public void setQtdparc(Integer qtdparc) {
		this.qtdparc = qtdparc;
	}

	public void setEstornado(String estornado) {
		this.estornado = estornado;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public Integer getCodCred() {
		return codCred;
	}

	public void setCodCred(Integer codCred) {
		this.codCred = codCred;
	}

	public TituloReceberBean getTituloreceber() {
		return tituloreceber;
	}

	public TituloPagarBean getTitulopagar() {
		return titulopagar;
	}

	public void setTituloreceber(TituloReceberBean tituloreceber) {
		this.tituloreceber = tituloreceber;
	}

	public void setTitulopagar(TituloPagarBean titulopagar) {
		this.titulopagar = titulopagar;
	}

	public String getCreddeb() {
		return creddeb;
	}

	public void setCreddeb(String creddeb) {
		this.creddeb = creddeb;
	}

	public CaixaBean getCaixa() {
		return caixa;
	}

	public void setCaixa(CaixaBean caixa) {
		this.caixa = caixa;
	}

	public Integer getCodVenda2() {
		return codVenda2;
	}

	public void setCodVenda2(Integer codVenda2) {
		this.codVenda2 = codVenda2;
	}

	public FonteReceitaBean getDescfonrec() {
		return descfonrec;
	}

	public void setDescfonrec(FonteReceitaBean descfonrec) {
		this.descfonrec = descfonrec;
	}

	public boolean isMedico() {
		return medico;
	}

	public void setMedico(boolean medico) {
		this.medico = medico;
	}

	public TituloReceberBean getTituloReceberAnterior() {
		return tituloReceberAnterior;
	}

	public void setTituloReceberAnterior(TituloReceberBean tituloReceberAnterior) {
		this.tituloReceberAnterior = tituloReceberAnterior;
	}

	

	

	

}
