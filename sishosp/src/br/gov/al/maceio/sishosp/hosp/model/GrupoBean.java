package br.gov.al.maceio.sishosp.hosp.model;

public class GrupoBean {

	private Integer idGrupo;
	private String descGrupo;
	private Integer qtdFrequencia;
	private boolean auditivo;

	public GrupoBean() {
		this.idGrupo = null;
		this.descGrupo = new String();
		this.qtdFrequencia = null;
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

	public boolean isAuditivo() {
		return auditivo;
	}

	public void setAuditivo(boolean auditivo) {
		this.auditivo = auditivo;
	}

}
