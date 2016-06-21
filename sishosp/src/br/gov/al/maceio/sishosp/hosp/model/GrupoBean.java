package br.gov.al.maceio.sishosp.hosp.model;

public class GrupoBean {

	private Integer idGrupo;
	private String descGrupo;
	private Integer qtdFrequencia;
	private boolean auditivo;
	private String tipoExameAuditivo;

	public GrupoBean() {
		this.idGrupo = null;
		this.descGrupo = new String();
		this.qtdFrequencia = null;
		this.tipoExameAuditivo = new String();
	}

	public GrupoBean(Integer idGrupo, String descGrupo, Integer qtdFrequencia) {
		this.idGrupo = idGrupo;
		this.descGrupo = descGrupo;
		this.qtdFrequencia = qtdFrequencia;
	}

	public Integer getIdGrupo() {
		return idGrupo;
	}

	public void setIdGrupo(Integer idGrupo) {
		this.idGrupo = idGrupo;
	}

	public String getDescGrupo() {
		return descGrupo;
	}

	public void setDescGrupo(String descGrupo) {
		this.descGrupo = descGrupo;
	}

	public Integer getQtdFrequencia() {
		return qtdFrequencia;
	}

	public void setQtdFrequencia(Integer qtdFrequencia) {
		this.qtdFrequencia = qtdFrequencia;
	}

	public String getTipoExameAuditivo() {
		return tipoExameAuditivo;
	}

	public void setTipoExameAuditivo(String tipoExameAuditivo) {
		this.tipoExameAuditivo = tipoExameAuditivo;
	}

	public boolean isAuditivo() {
		return auditivo;
	}

	public void setAuditivo(boolean auditivo) {
		this.auditivo = auditivo;
	}

}
