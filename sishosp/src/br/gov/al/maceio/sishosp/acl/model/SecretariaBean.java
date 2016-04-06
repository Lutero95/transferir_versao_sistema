package br.gov.al.maceio.sishosp.acl.model;

public class SecretariaBean {
	private String Codigo;
	private Integer empfil;
	private String descricao;
	public String getCodigo() {
		return Codigo;
	}
	public void setCodigo(String codigo) {
		Codigo = codigo;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public Integer getEmpfil() {
		return empfil;
	}
	public void setEmpfil(Integer empfil) {
		this.empfil = empfil;
	}
}
