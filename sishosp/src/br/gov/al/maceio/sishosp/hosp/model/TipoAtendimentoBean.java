package br.gov.al.maceio.sishosp.hosp.model;

public class TipoAtendimentoBean {
	
	private Integer idTipo;
	private String descTipoAt;
	private GrupoBean grupo;
	private boolean equipe;
	private boolean primeiroAt;
	private Integer codEmpresa;
	
	public TipoAtendimentoBean(){
		this.grupo = null;
		this.descTipoAt = new String();
		this.equipe = false;
		this.primeiroAt = false;
		
	}
	
	public String getDescTipoAt() {
		return descTipoAt;
	}
	public void setDescTipoAt(String descTipoAt) {
		this.descTipoAt = descTipoAt;
	}
	
	public GrupoBean getGrupo() {
		return grupo;
	}

	public void setGrupo(GrupoBean grupo) {
		this.grupo = grupo;
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
