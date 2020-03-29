package br.gov.al.maceio.sishosp.hosp.model.dto;

import br.gov.saude.servicos.schema.cbo.v1.cbo.CBOType;

public class CboDTO {
	private Long id;
	private CBOType cbo;
	
	public CboDTO() {
		this.cbo = new CBOType();
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public CBOType getCbo() {
		return cbo;
	}
	public void setCbo(CBOType cbo) {
		this.cbo = cbo;
	}
}
