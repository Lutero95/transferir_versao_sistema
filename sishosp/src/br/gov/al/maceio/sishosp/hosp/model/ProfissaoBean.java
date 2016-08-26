package br.gov.al.maceio.sishosp.hosp.model;

import java.io.Serializable;

public class ProfissaoBean implements Serializable {

	private Integer codprofissao;
	private String  descprofissao;
	
	public ProfissaoBean(){
		
	}

	public Integer getCodprofissao() {
		return codprofissao;
	}

	public void setCodprofissao(Integer codprofissao) {
		this.codprofissao = codprofissao;
	}

	public String getDescprofissao() {
		return descprofissao;
	}

	public void setDescprofissao(String descprofissao) {
		this.descprofissao = descprofissao;
	}

	
	
	
}
