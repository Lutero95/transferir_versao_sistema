package br.gov.al.maceio.sishosp.hosp.model;

import java.io.Serializable;

public class FornecedorBean implements Serializable {
	
	private Integer idFornecedor;
	private String descFornecedor;
	private String cnpj;
	private String endereco;
	private String tecnico;
	private Double valor;
	private String bairrofornecedor;
	private String cepfornecedor;
	private String cidadefornecedor;
	private String estadofornecedor;
	private String tel1;
	private String tel2;
	private String ie;
	private Integer codempresa;
	
	public FornecedorBean(){
		idFornecedor = null;
		descFornecedor = null;
	}

	public Integer getIdFornecedor() {
		return idFornecedor;
	}

	public void setIdFornecedor(Integer idFornecedor) {
		this.idFornecedor = idFornecedor;
	}

	public String getDescFornecedor() {
		return descFornecedor;
	}

	public void setDescFornecedor(String descFornecedor) {
		this.descFornecedor = descFornecedor;
	}

	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	public String getTecnico() {
		return tecnico;
	}

	public void setTecnico(String tecnico) {
		this.tecnico = tecnico;
	}

	public Double getValor() {
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}

	public String getBairrofornecedor() {
		return bairrofornecedor;
	}

	public void setBairrofornecedor(String bairrofornecedor) {
		this.bairrofornecedor = bairrofornecedor;
	}

	public String getCepfornecedor() {
		return cepfornecedor;
	}

	public void setCepfornecedor(String cepfornecedor) {
		this.cepfornecedor = cepfornecedor;
	}

	public String getCidadefornecedor() {
		return cidadefornecedor;
	}

	public void setCidadefornecedor(String cidadefornecedor) {
		this.cidadefornecedor = cidadefornecedor;
	}

	public String getEstadofornecedor() {
		return estadofornecedor;
	}

	public void setEstadofornecedor(String estadofornecedor) {
		this.estadofornecedor = estadofornecedor;
	}

	public String getTel1() {
		return tel1;
	}

	public void setTel1(String tel1) {
		this.tel1 = tel1;
	}

	public String getTel2() {
		return tel2;
	}

	public void setTel2(String tel2) {
		this.tel2 = tel2;
	}

	public String getIe() {
		return ie;
	}

	public void setIe(String ie) {
		this.ie = ie;
	}

	public Integer getCodempresa() {
		return codempresa;
	}

	public void setCodempresa(Integer codempresa) {
		this.codempresa = codempresa;
	}
	
	
	

}
