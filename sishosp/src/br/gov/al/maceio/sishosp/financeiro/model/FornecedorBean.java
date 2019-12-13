package br.gov.al.maceio.sishosp.financeiro.model;

import java.io.Serializable;
import java.util.Date;

public class FornecedorBean implements Serializable {



	/**
	 * 
	 */

	private Integer codforn;
	private String nome;
	private String fantasia;
	private String cpfcnpj;
	private String endereco;
	private String complemento;
	private String cep;
	private String telefone;
	private String site;
	private String email;
	private String inscest;
	private String tipoPessoa;
	

	public Integer getCodforn() {
		return codforn;
	}

	public String getNome() {
		return nome;
	}

	public String getFantasia() {
		return fantasia;
	}

	

	public void setCodforn(Integer codforn) {
		this.codforn = codforn;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public void setFantasia(String fantasia) {
		this.fantasia = fantasia;
	}

	

	

	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
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

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public String getSite() {
		return site;
	}

	public void setSite(String site) {
		this.site = site;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getInscest() {
		return inscest;
	}

	public void setInscest(String inscest) {
		this.inscest = inscest;
	}

	public String getCpfcnpj() {
		return cpfcnpj;
	}

	public String getTipoPessoa() {
		return tipoPessoa;
	}

	public void setCpfcnpj(String cpfcnpj) {
		this.cpfcnpj = cpfcnpj;
	}

	public void setTipoPessoa(String tipoPessoa) {
		this.tipoPessoa = tipoPessoa;
	}

}
