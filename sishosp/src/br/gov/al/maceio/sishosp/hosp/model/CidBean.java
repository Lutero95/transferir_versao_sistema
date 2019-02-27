package br.gov.al.maceio.sishosp.hosp.model;

import java.io.Serializable;

public class CidBean implements Serializable {

	private Integer idCid;
	private String descCid;
	private String cid;

	public CidBean() {
		super();
		this.idCid = null;
		this.descCid = new String();
		this.cid = new String();
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

	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}

}
