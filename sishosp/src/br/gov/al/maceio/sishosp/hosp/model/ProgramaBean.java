package br.gov.al.maceio.sishosp.hosp.model;

import java.util.ArrayList;
import java.util.List;

public class ProgramaBean {
	private Integer idPrograma;
	private String descPrograma;
	private Double codFederal;
	
	private GrupoBean grupoParaAdd;
	private List<GrupoBean> grupo;
	
	public ProgramaBean(){
		this.grupo = new ArrayList<GrupoBean>();
		this.idPrograma = null;
		this.descPrograma = new String();
		this.codFederal = null;
	}
	
	public ProgramaBean(Integer idPrograma, String descPrograma, Double codFederal) {
		this.idPrograma = idPrograma;
		this.descPrograma = descPrograma;
		this.codFederal = codFederal;
	}

	public Integer getIdPrograma() {
		return idPrograma;
	}

	public void setIdPrograma(Integer idPrograma) {
		this.idPrograma = idPrograma;
	}

	public String getDescPrograma() {
		return descPrograma;
	}

	public void setDescPrograma(String descPrograma) {
		this.descPrograma = descPrograma;
	}

	public Double getCodFederal() {
		return codFederal;
	}

	public void setCodFederal(Double codFederal) {
		this.codFederal = codFederal;
	}
	
	public List<GrupoBean> getGrupo() {
		return grupo;
	}

	public void setGrupo(List<GrupoBean> grupo) {
		this.grupo = grupo;
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
	
}