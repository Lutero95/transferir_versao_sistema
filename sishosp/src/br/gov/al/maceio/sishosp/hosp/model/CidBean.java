package br.gov.al.maceio.sishosp.hosp.model;

public class CidBean {
	
	private Integer idCid;
	private String descCid;
	
	
	public CidBean(){
		
	}
	
	public CidBean(Integer idCid, String descCid) {
		this.idCid = idCid;
		this.descCid = descCid;
		
	}

	public Integer getIdCid() {
		return idCid;
	}

	public void setIdCid(Integer idCid) {
		this.idCid = idCid;
	}

	public String getDescCid() {
		return descCid;
	}

	public void setDescCid(String descCid) {
		this.descCid = descCid;
	}

	
	
	
	

}