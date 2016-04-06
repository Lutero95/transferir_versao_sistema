package br.gov.al.maceio.sishosp.hosp.model;

import java.util.Date;
import java.util.List;

public class EnderecoBean {
    // ATRIBUTOS
	private Long id;
	private String municipio;
	private String referencia;
	private String uf;
	private String numero;
	private String logradouro;
	private String complemento;
	private Integer cep;
	private String bairro;
	
	private Date dataCadastro;
	private Date dataAlteracao;
	
	//LISTA
	 private List<Integer> listaEstado;
	 
	public EnderecoBean(){
		
		
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMunicipio() {
		return municipio;
	}

	public void setMunicipio(String municipio) {
		this.municipio = municipio;
	}

	public String getReferencia() {
		return referencia;
	}

	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}

	public String getUf() {
		return uf;
	}

	public void setUf(String uf) {
		this.uf = uf;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getLogradouro() {
		return logradouro;
	}

	public void setLogradouro(String logradouro) {
		this.logradouro = logradouro;
	}

	public String getComplemento() {
		return complemento;
	}

	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}

	public Integer getCep() {
		return cep;
	}

	public void setCep(Integer cep) {
		this.cep = cep;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public Date getDataCadastro() {
		return dataCadastro;
	}

	public void setDataCadastro(Date dataCadastro) {
		this.dataCadastro = dataCadastro;
	}

	public Date getDataAlteracao() {
		return dataAlteracao;
	}

	public void setDataAlteracao(Date dataAlteracao) {
		this.dataAlteracao = dataAlteracao;
	}

	public List<Integer> getListaEstado() {
		return listaEstado;
	}

	public void setListaEstado(List<Integer> listaEstado) {
		this.listaEstado = listaEstado;
	}
}
