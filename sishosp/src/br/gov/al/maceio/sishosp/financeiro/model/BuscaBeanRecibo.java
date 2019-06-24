package br.gov.al.maceio.sishosp.financeiro.model;

import java.io.Serializable;
import java.util.Date;

import br.gov.al.maceio.sishosp.acl.model.FuncionarioBean;

public class BuscaBeanRecibo implements Serializable {
    
	private FornecedorBean fornecedor;
    private ClienteBean cliente;
    private FuncionarioBean vendedor;
    private Date periodoinicial,periodofinal;
    private String ordenacao, situacao, documento;
    private Integer codvenda, iddespesa, codigo, codindicador2;
    private TipoPagBean tipoPag;

    public BuscaBeanRecibo() {
        cliente = new ClienteBean();
        vendedor = new FuncionarioBean();
        fornecedor = new FornecedorBean();
    }

    public ClienteBean getCliente() {
        return cliente;
    }

    public void setCliente(ClienteBean cliente) {
        this.cliente = cliente;
    }

    public FuncionarioBean getVendedor() {
        return vendedor;
    }

    public void setVendedor(FuncionarioBean vendedor) {
        this.vendedor = vendedor;
    }

    public Date getPeriodoinicial() {
        return periodoinicial;
    }

    public void setPeriodoinicial(Date periodoinicial) {
        this.periodoinicial = periodoinicial;
    }

    public Date getPeriodofinal() {
        return periodofinal;
    }

    public void setPeriodofinal(Date periodofinal) {
        this.periodofinal = periodofinal;
    }

    public String getOrdenacao() {
        return ordenacao;
    }

    public void setOrdenacao(String ordenacao) {
        this.ordenacao = ordenacao;
    }

    public Integer getCodvenda() {
        return codvenda;
    }

    public void setCodvenda(Integer codvenda) {
        this.codvenda = codvenda;
    }

	public String getSituacao() {
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	public FornecedorBean getFornecedor() {
		return fornecedor;
	}

	public void setFornecedor(FornecedorBean fornecedor) {
		this.fornecedor = fornecedor;
	}

	public Integer getIddespesa() {
		return iddespesa;
	}

	public void setIddespesa(Integer iddespesa) {
		this.iddespesa = iddespesa;
	}

	public String getDocumento() {
		return documento;
	}

	public void setDocumento(String documento) {
		this.documento = documento;
	}

	public Integer getCodigo() {
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public TipoPagBean getTipoPag() {
		return tipoPag;
	}

	public void setTipoPag(TipoPagBean tipoPag) {
		this.tipoPag = tipoPag;
	}

	public Integer getCodindicador2() {
		return codindicador2;
	}

	public void setCodindicador2(Integer codindicador2) {
		this.codindicador2 = codindicador2;
	}
}