package br.gov.al.maceio.sishosp.financeiro.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class TituloPagarBean implements Serializable {
	
	private Integer codigo;
	private Date dtvcto;
	private String duplicata;
	private String parcela;
	private double valoraberto;
	private double valor;
	private double desconto;
	private double vlr_retencao;
	private FornecedorBean forn;
	private Date dtprevisao;
	private String dtcompete;
	private Date dtemissao;
	private TipoDocumentoBean tipoDocumento;
	private String excluido;
	private DespesaBean despesa;
	private CentroCustoBean ccusto;
	private PortadorBean portador;
	private String historico;
	private String situacao;
	private String notaFiscal;
	private boolean contabil;
	private double juros;
	private double multa;
	private double icmsst;
	private TituloPagarBaixaBean titulobaixa;
	private List<ImpostoBean> lstImposto;
	private String nominal;
	private double totalpago;
	private boolean vencido, vencer, pago;

	private Date vencimento;

	public TituloPagarBean() {
		lstImposto = new ArrayList<ImpostoBean>();
		forn = new FornecedorBean();
		tipoDocumento = new TipoDocumentoBean();
		ccusto = new CentroCustoBean();
		despesa = new DespesaBean();
		portador = new PortadorBean();
		titulobaixa = new TituloPagarBaixaBean();
	}

	public Integer getCodigo() {
		return codigo;
	}

	public Date getDtvcto() {
		return dtvcto;
	}

		public String getDuplicata() {
		return duplicata;
	}

	public String getParcela() {
		return parcela;
	}

	public double getValoraberto() {
		return valoraberto;
	}

	public double getValor() {
		return valor;
	}

	public double getDesconto() {
		return desconto;
	}

	public double getVlr_retencao() {
		return vlr_retencao;
	}

	public FornecedorBean getForn() {
		return forn;
	}

	public Date getDtprevisao() {
		return dtprevisao;
	}

	public Date getDtemissao() {
		return dtemissao;
	}

	public TipoDocumentoBean getTipoDocumento() {
		return tipoDocumento;
	}

	public String getExcluido() {
		return excluido;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public void setDtvcto(Date dtvcto) {
		this.dtvcto = dtvcto;
	}

	public void setDuplicata(String duplicata) {
		this.duplicata = duplicata;
	}

	public void setParcela(String parcela) {
		this.parcela = parcela;
	}

	public void setValoraberto(double valoraberto) {
		this.valoraberto = valoraberto;
	}

	public void setValor(double valor) {
		this.valor = valor;
	}

	public void setDesconto(double desconto) {
		this.desconto = desconto;
	}

	public void setVlr_retencao(double vlr_retencao) {
		this.vlr_retencao = vlr_retencao;
	}

	public void setForn(FornecedorBean forn) {
		this.forn = forn;
	}

	public void setDtprevisao(Date dtprevisao) {
		this.dtprevisao = dtprevisao;
	}

	public void setDtemissao(Date dtemissao) {
		this.dtemissao = dtemissao;
	}

	public void setTipoDocumento(TipoDocumentoBean tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	public void setExcluido(String excluido) {
		this.excluido = excluido;
	}

	public DespesaBean getDespesa() {
		return despesa;
	}

	public CentroCustoBean getCcusto() {
		return ccusto;
	}

	public void setDespesa(DespesaBean despesa) {
		this.despesa = despesa;
	}

	public void setCcusto(CentroCustoBean ccusto) {
		this.ccusto = ccusto;
	}

	public PortadorBean getPortador() {
		return portador;
	}

	public void setPortador(PortadorBean portador) {
		this.portador = portador;
	}

	public String getHistorico() {
		return historico;
	}

	public String getSituacao() {
		return situacao;
	}

	public void setHistorico(String historico) {
		this.historico = historico;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}



	public String getNotaFiscal() {
		return notaFiscal;
	}

	public void setNotaFiscal(String notaFiscal) {
		this.notaFiscal = notaFiscal;
	}

	public boolean isContabil() {
		return contabil;
	}

	public void setContabil(boolean contabil) {
		this.contabil = contabil;
	}

	public double getJuros() {
		return juros;
	}

	public void setJuros(double juros) {
		this.juros = juros;
	}

	public double getMulta() {
		return multa;
	}

	public void setMulta(double multa) {
		this.multa = multa;
	}

	public Date getVencimento() {
		return vencimento;
	}

	public void setVencimento(Date vencimento) {
		this.vencimento = vencimento;
	}	

	public String getDtcompete() {
		return dtcompete;
	}

	public void setDtcompete(String dtcompete) {
		this.dtcompete = dtcompete;
	}

	public TituloPagarBaixaBean getTitulobaixa() {
		return titulobaixa;
	}

	public void setTitulobaixa(TituloPagarBaixaBean titulobaixa) {
		this.titulobaixa = titulobaixa;
	}

	public List<ImpostoBean> getLstImposto() {
		return lstImposto;
	}

	public void setLstImposto(List<ImpostoBean> lstImposto) {
		this.lstImposto = lstImposto;
	}

	public double getIcmsst() {
		return icmsst;
	}

	public void setIcmsst(double icmsst) {
		this.icmsst = icmsst;
	}

	public String getNominal() {
		return nominal;
	}

	public void setNominal(String nominal) {
		this.nominal = nominal;
	}

	public double getTotalpago() {
		return totalpago;
	}

	public void setTotalpago(double totalpago) {
		this.totalpago = totalpago;
	}

	public boolean isVencido() {
		return vencido;
	}

	public void setVencido(boolean vencido) {
		this.vencido = vencido;
	}

	public boolean isVencer() {
		return vencer;
	}

	public void setVencer(boolean vencer) {
		this.vencer = vencer;
	}

	public boolean isPago() {
		return pago;
	}

	public void setPago(boolean pago) {
		this.pago = pago;
	}
	
}
