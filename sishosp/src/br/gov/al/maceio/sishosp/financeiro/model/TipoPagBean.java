package br.gov.al.maceio.sishosp.financeiro.model;

import java.io.Serializable;

public class TipoPagBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer codtippag;
	private String tipo;
	private String descricao;
	private Double valorMinimo;
	private Double fator;
	private Double comissao;
	private Integer tipoDoc;
	private String descReduz;
	private Integer periodicidade;
	private Integer qtdParc;
	private String tipoPeriod;
	private Integer codFonrec;
	private String forma;
	private String pagparc;
	private Integer codcli;
	private Integer codcf;
	private String desccf;
	private Integer codport;
	private BancoBean banco;
	private Double taxaAdm;
	private String tipoDocString;
	private Integer codFilial;
	private boolean baixaAutFin;
	private ClienteBean cliente;
	private String nomeTipo;

	public TipoPagBean() {
		super();
		cliente = new ClienteBean();
		banco = new BancoBean();
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Double getValorMinimo() {
		return valorMinimo;
	}

	public void setValorMinimo(Double valorMinimo) {
		this.valorMinimo = valorMinimo;
	}

	public Double getFator() {
		return fator;
	}

	public void setFator(Double fator) {
		this.fator = fator;
	}

	public Double getComissao() {
		return comissao;
	}

	public void setComissao(Double comissao) {
		this.comissao = comissao;
	}

	public Integer getTipoDoc() {
		return tipoDoc;
	}

	public void setTipoDoc(Integer tipoDoc) {
		this.tipoDoc = tipoDoc;
	}

	public String getDescReduz() {
		return descReduz;
	}

	public void setDescReduz(String descReduz) {
		this.descReduz = descReduz;
	}

	public Integer getPeriodicidade() {
		return periodicidade;
	}

	public void setPeriodicidade(Integer periodicidade) {
		this.periodicidade = periodicidade;
	}

	public Integer getQtdParc() {
		return qtdParc;
	}

	public void setQtdParc(Integer qtdParc) {
		this.qtdParc = qtdParc;
	}

	public Integer getCodFilial() {
		return codFilial;
	}

	public void setCodFilial(Integer codFilial) {
		this.codFilial = codFilial;
	}

	public String getTipoPeriod() {
		return tipoPeriod;
	}

	public void setTipoPeriod(String tipoPeriod) {
		this.tipoPeriod = tipoPeriod;
	}

	public Integer getCodFonrec() {
		return codFonrec;
	}

	public void setCodFonrec(Integer codFonrec) {
		this.codFonrec = codFonrec;
	}

	public String getForma() {
		return forma;
	}

	public void setForma(String forma) {
		this.forma = forma;
	}

	public String getPagparc() {
		return pagparc;
	}

	public void setPagparc(String pagparc) {
		this.pagparc = pagparc;
	}

	public Integer getCodcli() {
		return codcli;
	}

	public void setCodcli(Integer codcli) {
		this.codcli = codcli;
	}

	public Integer getCodcf() {
		return codcf;
	}

	public void setCodcf(Integer codcf) {
		this.codcf = codcf;
	}

	public String getDesccf() {
		return desccf;
	}

	public void setDesccf(String desccf) {
		this.desccf = desccf;
	}

	public Integer getCodport() {
		return codport;
	}

	public void setCodport(Integer codport) {
		this.codport = codport;
	}

	public Double getTaxaAdm() {
		return taxaAdm;
	}

	public void setTaxaAdm(Double taxaAdm) {
		this.taxaAdm = taxaAdm;
	}

	public String getTipoDocString() {
		return tipoDocString;
	}

	public void setTipoDocString(String tipoDocString) {
		this.tipoDocString = tipoDocString;
	}

	public Integer getCodtippag() {
		return codtippag;
	}

	public void setCodtippag(Integer codtippag) {
		this.codtippag = codtippag;
	}

	public boolean isBaixaAutFin() {
		return baixaAutFin;
	}

	public void setBaixaAutFin(boolean baixaAutFin) {
		this.baixaAutFin = baixaAutFin;
	}

	public ClienteBean getCliente() {
		return cliente;
	}

	public void setCliente(ClienteBean cliente) {
		this.cliente = cliente;
	}

	public BancoBean getBanco() {
		return banco;
	}

	public void setBanco(BancoBean banco) {
		this.banco = banco;
	}

	public String getNomeTipo() {
		return nomeTipo;
	}

	public void setNomeTipo(String nomeTipo) {
		this.nomeTipo = nomeTipo;
	}

}
