package br.gov.al.maceio.sishosp.financeiro.model;

import java.io.Serializable;
import java.util.Date;

public class AntecipacaoBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private Integer id_antecipa;
    
    private ClienteBean cliente;
    private TipoDocumentoBean tipo;
    private BancoBean banco;
    private FornecedorBean fornecedor;
    
    private Date dataIniFiltro;
    private Date dataFimFiltro;
    private String tipoDoc;
    private String dataRef;
    
    private Date dataEmissao;
    private Date dataRecebimento;
    private Double valorAntecipado;
    private Integer tipoBaixa;
    private String tipoBaixaString;
    private String contaBancaria;
    private Integer formaBaixa;
    private Double encargos;
    private Double liquidoReceber;
    private Integer codigo;
    private String situacao;

    public AntecipacaoBean() {
        cliente = new ClienteBean();
        tipo = new TipoDocumentoBean();
        banco = new BancoBean();
        fornecedor = new FornecedorBean();
        dataRef = "V";
    }

    public ClienteBean getCliente() {
        return cliente;
    }

    public void setCliente(ClienteBean cliente) {
        this.cliente = cliente;
    }

    public TipoDocumentoBean getTipo() {
        return tipo;
    }

    public void setTipo(TipoDocumentoBean tipo) {
        this.tipo = tipo;
    }

    public Date getDataIniFiltro() {
        return dataIniFiltro;
    }

    public void setDataIniFiltro(Date dataIniFiltro) {
        this.dataIniFiltro = dataIniFiltro;
    }

    public Date getDataFimFiltro() {
        return dataFimFiltro;
    }

    public void setDataFimFiltro(Date dataFimFiltro) {
        this.dataFimFiltro = dataFimFiltro;
    }

    public String getTipoDoc() {
        return tipoDoc;
    }

    public void setTipoDoc(String tipoDoc) {
        this.tipoDoc = tipoDoc;
    }

	public String getDataRef() {
		return dataRef;
	}

	public void setDataRef(String dataRef) {
		this.dataRef = dataRef;
	}

	public Date getDataEmissao() {
		return dataEmissao;
	}

	public void setDataEmissao(Date dataEmissao) {
		this.dataEmissao = dataEmissao;
	}

	public Date getDataRecebimento() {
		return dataRecebimento;
	}

	public void setDataRecebimento(Date dataRecebimento) {
		this.dataRecebimento = dataRecebimento;
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

	public String getContaBancaria() {
		return contaBancaria;
	}

	public void setContaBancaria(String contaBancaria) {
		this.contaBancaria = contaBancaria;
	}

	public Integer getFormaBaixa() {
		return formaBaixa;
	}

	public void setFormaBaixa(Integer formaBaixa) {
		this.formaBaixa = formaBaixa;
	}

	public Double getEncargos() {
		return encargos;
	}

	public void setEncargos(Double encargos) {
		this.encargos = encargos;
	}

	public Double getLiquidoReceber() {
		return liquidoReceber;
	}

	public void setLiquidoReceber(Double liquidoReceber) {
		this.liquidoReceber = liquidoReceber;
	}

	public BancoBean getBanco() {
		return banco;
	}

	public void setBanco(BancoBean banco) {
		this.banco = banco;
	}
	//teste
	public Double getValorAntecipado() {
		return valorAntecipado;
	}

	public void setValorAntecipado(Double valorAntecipado) {
		this.valorAntecipado = valorAntecipado;
	}

	public Integer getId_antecipa() {
		return id_antecipa;
	}

	public void setId_antecipa(Integer id_antecipa) {
		this.id_antecipa = id_antecipa;
	}

	public FornecedorBean getFornecedor() {
		return fornecedor;
	}

	public void setFornecedor(FornecedorBean fornecedor) {
		this.fornecedor = fornecedor;
	}

	public Integer getCodigo() {
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public String getSituacao() {
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}
	
}