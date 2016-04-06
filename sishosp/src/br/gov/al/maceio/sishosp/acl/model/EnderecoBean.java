package br.gov.al.maceio.sishosp.acl.model;

import java.util.Date;

public class EnderecoBean {

	private int id;
	private int codsecretaria;
	private String rua;
	private String numero;
	private String complemento;
	private String cep;
	private String bairro;
	private Date vigenciaInicial;
	private Date vigenciaFinal;
	private int opcad;
	private Date dataCadastro;
	private int opalt;
	private Date dataAlteracao;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getCodsecretaria() {
		return codsecretaria;
	}

	public void setCodsecretaria(int codsecretaria) {
		this.codsecretaria = codsecretaria;
	}

	public String getRua() {
		return rua;
	}

	public void setRua(String rua) {
		this.rua = rua;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
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

	public Date getVigenciaInicial() {
		return vigenciaInicial;
	}

	public void setVigenciaInicial(Date vigenciaInicial) {
		this.vigenciaInicial = vigenciaInicial;
	}

	public Date getVigenciaFinal() {
		return vigenciaFinal;
	}

	public void setVigenciaFinal(Date vigenciaFinal) {
		this.vigenciaFinal = vigenciaFinal;
	}

	public int getOpcad() {
		return opcad;
	}

	public void setOpcad(int opcad) {
		this.opcad = opcad;
	}

	public Date getDataCadastro() {
		return dataCadastro;
	}

	public void setDataCadastro(Date dataCadastro) {
		this.dataCadastro = dataCadastro;
	}

	public int getOpalt() {
		return opalt;
	}

	public void setOpalt(int opalt) {
		this.opalt = opalt;
	}

	public Date getDataAlteracao() {
		return dataAlteracao;
	}

	public void setDataAlteracao(Date dataAlteracao) {
		this.dataAlteracao = dataAlteracao;
	}

}
