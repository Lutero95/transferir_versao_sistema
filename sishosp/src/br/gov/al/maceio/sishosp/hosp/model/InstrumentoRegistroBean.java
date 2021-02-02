package br.gov.al.maceio.sishosp.hosp.model;

import java.io.Serializable;

public class InstrumentoRegistroBean implements Serializable{
	
	private static final long serialVersionUID = -7805518124764340389L;
	private Integer id;
	private String codigo;
	private String nome;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
}
