package br.gov.al.maceio.sishosp.hosp.model;

public class TipoAtendimentoBean {
	
	private Integer idTipo;
	private String descTipoAt;
	private Integer idGrupo;
	private boolean equipe;
	private boolean primeiroAt;
	private Integer codEmpresa;
	
	public TipoAtendimentoBean(){
		
	}
	
	public TipoAtendimentoBean(Integer idTipo, String descTipoAt, Integer idGrupo,
			boolean equipe, boolean primeiroAt, Integer codEmpresa) {
		this.idTipo = idTipo;
		this.descTipoAt = descTipoAt;
		this.idGrupo = idGrupo;
		this.equipe = equipe;
		this.primeiroAt = primeiroAt;
		this.codEmpresa = codEmpresa;
	}
	public String getDescTipoAt() {
		return descTipoAt;
	}
	public void setDescTipoAt(String descTipoAt) {
		this.descTipoAt = descTipoAt;
	}
	
	public Integer getIdGrupo() {
		return idGrupo;
	}

	public void setIdGrupo(Integer idGrupo) {
		this.idGrupo = idGrupo;
	}

	public boolean isEquipe() {
		return equipe;
	}
	public void setEquipe(boolean equipe) {
		this.equipe = equipe;
	}
	public boolean isPrimeiroAt() {
		return primeiroAt;
	}
	public void setPrimeiroAt(boolean primeiroAt) {
		this.primeiroAt = primeiroAt;
	}

	public Integer getIdTipo() {
		return idTipo;
	}

	public void setIdTipo(Integer idTipo) {
		this.idTipo = idTipo;
	}

	public Integer getCodEmpresa() {
		return codEmpresa;
	}

	public void setCodEmpresa(Integer codEmpresa) {
		this.codEmpresa = codEmpresa;
	}

}
