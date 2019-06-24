package br.gov.al.maceio.sishosp.financeiro.model;

import java.io.Serializable;
import java.util.Date;

public class BuscaBeanPagar implements Serializable{
	private FornecedorBean fornecedor;
	private Date periodoinicial,periodofinal;
	private String ordenacao, situacao, documento;
	private Integer  codigo, iddespesa;
	
	
	public BuscaBeanPagar() {
		fornecedor = new FornecedorBean();
		
	}


	public FornecedorBean getFornecedor() {
		return fornecedor;
	}


	public void setFornecedor(FornecedorBean fornecedor) {
		this.fornecedor = fornecedor;
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


	public String getDocumento() {
		return documento;
	}


	public void setDocumento(String documento) {
		this.documento = documento;
	}


	public Integer getIddespesa() {
		return iddespesa;
	}


	public void setIddespesa(Integer iddespesa) {
		this.iddespesa = iddespesa;
	}
	
}
