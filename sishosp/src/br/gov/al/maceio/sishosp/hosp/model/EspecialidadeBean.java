package br.gov.al.maceio.sishosp.hosp.model;

public class EspecialidadeBean {

	private Integer codEspecialidade;
	private String descEspecialidade;
	private Integer codEmpresa;
	
	public EspecialidadeBean(){
		
	}

	public Integer getCodEspecialidade() {
		return codEspecialidade;
	}

	public void setCodEspecialidade(Integer codEspecialidade) {
		this.codEspecialidade = codEspecialidade;
	}

	public String getDescEspecialidade() {
		return descEspecialidade;
	}

	public void setDescEspecialidade(String descEspecialidade) {
		this.descEspecialidade = descEspecialidade;
	}

	public Integer getCodEmpresa() {
		return codEmpresa;
	}

	public void setCodEmpresa(Integer codEmpresa) {
		this.codEmpresa = codEmpresa;
	}

	
	
}
