package br.gov.al.maceio.sishosp.hosp.model.dto;

import br.gov.saude.servicos.schema.sigtap.procedimento.cid.v1.cid.CIDType;

public class CidDTO {
	private Long id;
	private CIDType cid;
	
	public CidDTO() {
		this.cid = new CIDType();
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public CIDType getCid() {
		return cid;
	}
	public void setCid(CIDType cid) {
		this.cid = cid;
	}
}
