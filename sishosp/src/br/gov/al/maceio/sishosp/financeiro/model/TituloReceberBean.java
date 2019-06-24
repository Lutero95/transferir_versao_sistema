package br.gov.al.maceio.sishosp.financeiro.model;

import java.io.Serializable;
import java.util.Date;

public class TituloReceberBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer idFinanceiro;
    private TipoDocumentoBean tipoDocumento;
    private ClienteBean cliente;
    private String competencia;
    private Date dataEmissao;
    private PortadorBean portador;
    private Double valor, valor2;
    private double desconto;
    private Date vencimento;
    private String historico;
    private String descricaoFatura;
    private String objCobranca;
    private String status;
    private FonteReceitaBean fonrec;
    private TipoPagBean tippag;
    private BaixaReceber tituloBaixareceber;
    private String parcela;
    private String documento;
    private CaixaBean caixaloja;
    private double valortaxaadm;
    private double valortaxaadmperc;
    private double valoraberto;
    
    private Integer codigo;
    private double totalpago;
	private boolean vencido, vencer, pago;
	private String duplicata;
	private Date dtprevisao;
	private String dtcompete;
	private Date dtemissao;
	private Date dtvcto;
    private String formaBaixa;
    private Double juros;
    private String contaBancaria;
    private Double multa;
	private double icmsst;
	private String situacao;
	private String notaFiscal;
	private DespesaBean despesa;
	private CentroCustoBean ccusto;
	private FornecedorBean forn;
    

    public TituloReceberBean() {
        cliente = new ClienteBean();
        tipoDocumento = new TipoDocumentoBean();
        tituloBaixareceber = new BaixaReceber();
        fonrec = new FonteReceitaBean();
        portador = new PortadorBean();
        tippag = new TipoPagBean();
        caixaloja = new CaixaBean();
        forn = new FornecedorBean();
    }

    public Integer getIdFinanceiro() {
        return idFinanceiro;
    }

    public void setIdFinanceiro(Integer idFinanceiro) {
        this.idFinanceiro = idFinanceiro;
    }

    public TipoDocumentoBean getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(TipoDocumentoBean tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public ClienteBean getCliente() {
        return cliente;
    }

    public void setCliente(ClienteBean cliente) {
        this.cliente = cliente;
    }


    public Date getDataEmissao() {
        return dataEmissao;
    }

    public void setDataEmissao(Date dataEmissao) {
        this.dataEmissao = dataEmissao;
    }

    public PortadorBean getPortador() {
        return portador;
    }

    public void setPortador(PortadorBean portador) {
        this.portador = portador;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public double getDesconto() {
        return desconto;
    }

    public void setDesconto(double desconto) {
        this.desconto = desconto;
    }

    public Date getVencimento() {
        return vencimento;
    }

    public void setVencimento(Date vencimento) {
        this.vencimento = vencimento;
    }

    public String getHistorico() {
        return historico;
    }

    public void setHistorico(String historico) {
        this.historico = historico;
    }

    public String getDescricaoFatura() {
        return descricaoFatura;
    }

    public void setDescricaoFatura(String descricaoFatura) {
        this.descricaoFatura = descricaoFatura;
    }

    public String getObjCobranca() {
        return objCobranca;
    }

    public void setObjCobranca(String objCobranca) {
        this.objCobranca = objCobranca;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public FonteReceitaBean getFonrec() {
        return fonrec;
    }

    public void setFonrec(FonteReceitaBean fonrec) {
        this.fonrec = fonrec;
    }

    public TipoPagBean getTippag() {
        return tippag;
    }

    public void setTippag(TipoPagBean tippag) {
        this.tippag = tippag;
    }

    public BaixaReceber getTituloBaixareceber() {
        return tituloBaixareceber;
    }

    public void setTituloBaixareceber(BaixaReceber tituloBaixareceber) {
        this.tituloBaixareceber = tituloBaixareceber;
    }

    public String getParcela() {
        return parcela;
    }

    public void setParcela(String parcela) {
        this.parcela = parcela;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public CaixaBean getCaixaloja() {
        return caixaloja;
    }

    public void setCaixaloja(CaixaBean caixaloja) {
        this.caixaloja = caixaloja;
    }

    public double getValortaxaadm() {
        return valortaxaadm;
    }

    public void setValortaxaadm(double valortaxaadm) {
        this.valortaxaadm = valortaxaadm;
    }

    public double getValoraberto() {
        return valoraberto;
    }

    public void setValoraberto(double valoraberto) {
        this.valoraberto = valoraberto;
    }



	/**
	 * @return the competencia
	 */
	public String getCompetencia() {
		return competencia;
	}

	/**
	 * @param competencia the competencia to set
	 */
	public void setCompetencia(String competencia) {
		this.competencia = competencia;
	}

	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}



	public Double getValor2() {
		return valor2;
	}

	public void setValor2(Double valor2) {
		this.valor2 = valor2;
	}

	public double getValortaxaadmperc() {
		return valortaxaadmperc;
	}

	public void setValortaxaadmperc(double valortaxaadmperc) {
		this.valortaxaadmperc = valortaxaadmperc;
	}

	public Integer getCodigo() {
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public boolean isPago() {
		return pago;
	}

	public void setPago(boolean pago) {
		this.pago = pago;
	}

	public boolean isVencer() {
		return vencer;
	}

	public void setVencer(boolean vencer) {
		this.vencer = vencer;
	}

	public boolean isVencido() {
		return vencido;
	}

	public void setVencido(boolean vencido) {
		this.vencido = vencido;
	}

	public double getTotalpago() {
		return totalpago;
	}

	public void setTotalpago(double totalpago) {
		this.totalpago = totalpago;
	}

	public String getDuplicata() {
		return duplicata;
	}

	public void setDuplicata(String duplicata) {
		this.duplicata = duplicata;
	}

	public Date getDtprevisao() {
		return dtprevisao;
	}

	public void setDtprevisao(Date dtprevisao) {
		this.dtprevisao = dtprevisao;
	}

	public String getDtcompete() {
		return dtcompete;
	}

	public void setDtcompete(String dtcompete) {
		this.dtcompete = dtcompete;
	}

	public Date getDtemissao() {
		return dtemissao;
	}

	public void setDtemissao(Date dtemissao) {
		this.dtemissao = dtemissao;
	}

	public Date getDtvcto() {
		return dtvcto;
	}

	public void setDtvcto(Date dtvcto) {
		this.dtvcto = dtvcto;
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

	public Double getMulta() {
		return multa;
	}

	public void setMulta(Double multa) {
		this.multa = multa;
	}

	public String getContaBancaria() {
		return contaBancaria;
	}

	public void setContaBancaria(String contaBancaria) {
		this.contaBancaria = contaBancaria;
	}

	public double getIcmsst() {
		return icmsst;
	}

	public void setIcmsst(double icmsst) {
		this.icmsst = icmsst;
	}

	public String getSituacao() {
		return situacao;
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

	public DespesaBean getDespesa() {
		return despesa;
	}

	public void setDespesa(DespesaBean despesa) {
		this.despesa = despesa;
	}

	public CentroCustoBean getCcusto() {
		return ccusto;
	}

	public void setCcusto(CentroCustoBean ccusto) {
		this.ccusto = ccusto;
	}

	public FornecedorBean getForn() {
		return forn;
	}

	public void setForn(FornecedorBean forn) {
		this.forn = forn;
	}
}