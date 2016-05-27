package br.gov.al.maceio.sishosp.hosp.model;

public class ProcedimentoBean {
	
	private Integer idProc;
	private Integer codProc;
	private String nomeProc;
	private Boolean apac;
	private Boolean bpi;
	private Boolean auditivo;
	private String tipoExameAuditivo;
	private Boolean utilizaEquipamento;
	private Integer codEmpresa;
	
	
	public ProcedimentoBean() {
		super();
		this.idProc = null;
		this.codProc = null;
		this.nomeProc = new String();
		this.apac = null;
		this.bpi = null;
		this.auditivo = false;
		this.tipoExameAuditivo = new String();
		this.utilizaEquipamento = false;
		this.codEmpresa = null;
	}

	public Integer getIdProc() {
		return idProc;
	}

	public void setIdProc(Integer idProc) {
		this.idProc = idProc;
	}

	public Integer getCodProc() {
		return codProc;
	}

	public void setCodProc(Integer codProc) {
		this.codProc = codProc;
	}

	public String getNomeProc() {
		return nomeProc;
	}

	public void setNomeProc(String nomeProc) {
		this.nomeProc = nomeProc;
	}

	public Boolean getApac() {
		return apac;
	}

	public void setApac(Boolean apac) {
		this.apac = apac;
	}

	public Boolean getBpi() {
		return bpi;
	}

	public void setBpi(Boolean bpi) {
		this.bpi = bpi;
	}

	public Boolean getAuditivo() {
		return auditivo;
	}

	public void setAuditivo(Boolean auditivo) {
		this.auditivo = auditivo;
	}

	public String getTipoExameAuditivo() {
		return tipoExameAuditivo;
	}

	public void setTipoExameAuditivo(String tipoExameAuditivo) {
		this.tipoExameAuditivo = tipoExameAuditivo;
	}

	public Boolean getUtilizaEquipamento() {
		return utilizaEquipamento;
	}

	public void setUtilizaEquipamento(Boolean utilizaEquipamento) {
		this.utilizaEquipamento = utilizaEquipamento;
	}

	public Integer getCodEmpresa() {
		return codEmpresa;
	}

	public void setCodEmpresa(Integer codEmpresa) {
		this.codEmpresa = codEmpresa;
	}

	@Override
	public String toString() {
		return "ProcedimentoBean [idProc=" + idProc + ", codProc=" + codProc
				+ ", nomeProc=" + nomeProc + ", apac=" + apac + ", bpi=" + bpi
				+ ", auditivo=" + auditivo + ", tipoExameAuditivo="
				+ tipoExameAuditivo + ", utilizaEquipamento="
				+ utilizaEquipamento + ", codEmpresa=" + codEmpresa + "]";
	}
	
}
