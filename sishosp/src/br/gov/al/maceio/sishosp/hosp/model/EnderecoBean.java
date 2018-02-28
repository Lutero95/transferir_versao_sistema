package br.gov.al.maceio.sishosp.hosp.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class EnderecoBean implements Serializable {
    // ATRIBUTOS
	private Long id;
	private String municipio;
	private String referencia;
	private String uf;
	private String numero;
	private String logradouro;
	private Integer codbairro;
	private Integer codmunicipio;
	private Integer codfederal;
	private Integer codmacregiao;
	private String complemento;
	private String cep;
	private String bairro;
	
	private String telefoneres;
	private String telefonecel;
	private String telefonetrab;
	private String telefoneorelhao;
	
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

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
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

	public String getTelefoneres() {
		return telefoneres;
	}

	public void setTelefoneres(String telefoneres) {
		this.telefoneres = telefoneres;
	}

	public String getTelefonecel() {
		return telefonecel;
	}

	public void setTelefonecel(String telefonecel) {
		this.telefonecel = telefonecel;
	}

	public String getTelefonetrab() {
		return telefonetrab;
	}

	public void setTelefonetrab(String telefonetrab) {
		this.telefonetrab = telefonetrab;
	}

	public String getTelefoneorelhao() {
		return telefoneorelhao;
	}

	public void setTelefoneorelhao(String telefoneorelhao) {
		this.telefoneorelhao = telefoneorelhao;
	}

	public Integer getCodbairro() {
		return codbairro;
	}

	public void setCodbairro(Integer codbairro) {
		this.codbairro = codbairro;
	}

	public Integer getCodmunicipio() {
		return codmunicipio;
	}

	public void setCodmunicipio(Integer codmunicipio) {
		this.codmunicipio = codmunicipio;
	}

	public Integer getCodfederal() {
		return codfederal;
	}

	public void setCodfederal(Integer codfederal) {
		this.codfederal = codfederal;
	}

	public Integer getCodmacregiao() {
		return codmacregiao;
	}

	public void setCodmacregiao(Integer codmacregiao) {
		this.codmacregiao = codmacregiao;
	}


	
	
}
