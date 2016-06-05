package br.gov.al.maceio.sishosp.hosp.model;

import java.util.ArrayList;
import java.util.List;

public class TipoAtendimentoBean {
	
	private Integer idTipo;
	private String descTipoAt;
	private boolean equipe;
	private boolean primeiroAt;
	private Integer codEmpresa;
	private GrupoBean grupoParaAdd;
	
	private List<GrupoBean> grupo;
	
	public TipoAtendimentoBean(){
		this.grupo = new ArrayList<GrupoBean>();
		this.descTipoAt = new String();
		this.equipe = false;
		this.primeiroAt = false;
		this.grupoParaAdd = new GrupoBean();
		this.idTipo = null;
		
	}
	
	public List<GrupoBean> getGrupo() {
		return grupo;
	}

	public void setGrupo(List<GrupoBean> grupo) {
		this.grupo = grupo;
	}

	public String getDescTipoAt() {
		return descTipoAt;
	}
	public void setDescTipoAt(String descTipoAt) {
		this.descTipoAt = descTipoAt;
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
	
	public GrupoBean getGrupoParaAdd() {
		return grupoParaAdd;
	}

	public void setGrupoParaAdd(GrupoBean grupoParaAdd) {
		this.grupoParaAdd = grupoParaAdd;
	}

	public void addGrupoLista(){
		this.grupo.add(grupoParaAdd);
	}

	@Override
	public String toString() {
		return "TipoAtendimentoBean [idTipo=" + idTipo + ", descTipoAt="
				+ descTipoAt + ", equipe=" + equipe + "]";
	}
	
	

}
