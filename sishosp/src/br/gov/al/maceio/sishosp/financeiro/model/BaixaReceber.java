package br.gov.al.maceio.sishosp.financeiro.model;

import java.io.Serializable;
import java.util.Date;

import br.gov.al.maceio.sishosp.acl.model.FuncionarioBean;

public class BaixaReceber implements Serializable {

  /*  public BaixaReceber(Integer id, Integer duplicata, Date dtRecebimento,
			Integer tipoBaixa, String tipoBaixaString, Double vlrAtual,
			Double desconto, String formaBaixa, Double juros,
			String contaBancaria, Double multa, String correcao,
			String retencao, Double valorRecebido, Double valorBaixado,
			Double vlrCorrecao, BancoBean banco, Integer codChequeEmitido,
			FuncionarioBean func, Integer codSeqCaixa, Double valorRetencao,
			Double valorEmAberto, Double valorAtual) {
		super();
		this.id = id;
		this.duplicata = duplicata;
		this.dtRecebimento = dtRecebimento;
		this.tipoBaixa = tipoBaixa;
		this.tipoBaixaString = tipoBaixaString;
		this.vlrAtual = vlrAtual;
		this.desconto = desconto;
		this.formaBaixa = formaBaixa;
		this.juros = juros;
		this.contaBancaria = contaBancaria;
		this.multa = multa;
		this.correcao = correcao;
		this.retencao = retencao;
		this.valorRecebido = valorRecebido;
		this.valorBaixado = valorBaixado;
		this.vlrCorrecao = vlrCorrecao;
		this.banco = banco;
		this.codChequeEmitido = codChequeEmitido;
		this.func = func;
		this.codSeqCaixa = codSeqCaixa;
		this.valorRetencao = valorRetencao;
		this.valorEmAberto = valorEmAberto;
		this.valorAtual = valorAtual;
	}
*/
	private static final long serialVersionUID = 1L;
    
    private Integer id;
    private Integer duplicata;
    private Date dtRecebimento;
    private Integer tipoBaixa;
    private String tipoBaixaString;
    private Double vlrAtual;
    private Double desconto;
    private String formaBaixa;
    private Double juros;
    private String contaBancaria;
    private Double multa;
    private String correcao;
    private String retencao;
    private Double valorRecebido;
    private Double valorBaixado;
    private Double vlrCorrecao;
    private BancoBean banco;
    private Integer codChequeEmitido;
    private FuncionarioBean func;
    
    //private String idBancoString;
    private Integer codSeqCaixa;
    
    private Double valorRetencao;
    private Double valorEmAberto;
  //teste
    // Valor usado para o calculo do novo valor do título(incluindo juros, multa, etc...).
    private Double valorAtual;
    
    public BaixaReceber() {
        banco = new BancoBean();
        func = new FuncionarioBean();
    }

    public Integer getDuplicata() {
        return duplicata;
    }

    public void setDuplicata(Integer duplicata) {
        this.duplicata = duplicata;
    }

    public Date getDtRecebimento() {
        return dtRecebimento;
    }

    public void setDtRecebimento(Date dtRecebimento) {
        this.dtRecebimento = dtRecebimento;
    }

    public Integer getTipoBaixa() {
        return tipoBaixa;
    }

    public void setTipoBaixa(Integer tipoBaixa) {
        this.tipoBaixa = tipoBaixa;
    }

    public String getTipoBaixaString() {
        return tipoBaixaString;
    }

    public void setTipoBaixaString(String tipoBaixaString) {
        this.tipoBaixaString = tipoBaixaString;
    }

    public Double getVlrAtual() {
        return vlrAtual;
    }

    public void setVlrAtual(Double vlrAtual) {
        this.vlrAtual = vlrAtual;
    }

    public Double getDesconto() {
        return desconto;
    }

    public void setDesconto(Double desconto) {
        this.desconto = desconto;
    }

    public String getFormaBaixa() {
        return formaBaixa;
    }

    public void setFormaBaixa(String formaBaixa) {
        this.formaBaixa = formaBaixa;
    }

    public Double getJuros() {
        return juros;
    }

    public void setJuros(Double juros) {
        this.juros = juros;
    }

    public String getContaBancaria() {
        return contaBancaria;
    }

    public void setContaBancaria(String contaBancaria) {
        this.contaBancaria = contaBancaria;
    }

    public Double getMulta() {
        return multa;
    }

    public void setMulta(Double multa) {
        this.multa = multa;
    }

    public String getCorrecao() {
        return correcao;
    }

    public void setCorrecao(String correcao) {
        this.correcao = correcao;
    }

    public String getRetencao() {
        return retencao;
    }

    public void setRetencao(String retencao) {
        this.retencao = retencao;
    }

    public Double getValorRecebido() {
        return valorRecebido;
    }

    public void setValorRecebido(Double valorRecebido) {
        this.valorRecebido = valorRecebido;
    }

    public Double getValorBaixado() {
        return valorBaixado;
    }

    public void setValorBaixado(Double valorBaixado) {
        this.valorBaixado = valorBaixado;
    }

    public Double getVlrCorrecao() {
        return vlrCorrecao;
    }

    public void setVlrCorrecao(Double vlrCorrecao) {
        this.vlrCorrecao = vlrCorrecao;
    }

    public BancoBean getBanco() {
        return banco;
    }

    public void setBanco(BancoBean banco) {
        this.banco = banco;
    }

    public Integer getCodSeqCaixa() {
        return codSeqCaixa;
    }

    public void setCodSeqCaixa(Integer codSeqCaixa) {
        this.codSeqCaixa = codSeqCaixa;
    }

    public Double getValorRetencao() {
        return valorRetencao;
    }

    public void setValorRetencao(Double valorRetencao) {
        this.valorRetencao = valorRetencao;
    }

    public Double getValorEmAberto() {
        return valorEmAberto;
    }

    public void setValorEmAberto(Double valorEmAberto) {
        this.valorEmAberto = valorEmAberto;
    }
    
    public Double getValorAtual() {
        return valorAtual;
    }

    public void setValorAtual(Double valorAtual) {
        this.valorAtual = valorAtual;
    }

	public Integer getCodChequeEmitido() {
		return codChequeEmitido;
	}

	public void setCodChequeEmitido(Integer codChequeEmitido) {
		this.codChequeEmitido = codChequeEmitido;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public FuncionarioBean getFunc() {
		return func;
	}

	public void setFunc(FuncionarioBean func) {
		this.func = func;
	}
}