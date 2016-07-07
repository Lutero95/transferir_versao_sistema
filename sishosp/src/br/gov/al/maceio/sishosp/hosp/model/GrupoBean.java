package br.gov.al.maceio.sishosp.hosp.model;

import java.util.ArrayList;
import java.util.List;

public class GrupoBean {

	private Integer idGrupo;
	private String descGrupo;
	private Integer qtdFrequencia;
	private boolean auditivo;
	private List<EquipeBean> equipes;
	private List<EquipeBean> equipesNovo;
	private EquipeBean equipeAdd;

	public GrupoBean() {
		this.idGrupo = null;
		this.descGrupo = new String();
		this.qtdFrequencia = 1;
		this.equipes = new ArrayList<EquipeBean>();
		this.equipesNovo = new ArrayList<EquipeBean>();
		this.equipeAdd = null;
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

	public List<EquipeBean> getEquipes() {
		return equipes;
	}

	public void setEquipes(List<EquipeBean> equipes) {
		this.equipes = equipes;
	}

	public List<EquipeBean> getEquipesNovo() {
		this.equipesNovo = this.equipes;
		return equipesNovo;
	}

	public void setEquipesNovo(List<EquipeBean> equipesNovo) {
		this.equipesNovo = equipesNovo;
	}

	public EquipeBean getEquipeAdd() {
		if(this.equipeAdd == null){
			this.equipeAdd = new EquipeBean();
		}
		return equipeAdd;
	}

	public void setEquipeAdd(EquipeBean equipeAdd) {
		this.equipeAdd = equipeAdd;
	}

	public void addEquipe() {
		this.equipes.add(this.equipeAdd);
	}

	public void rmvEquipe() {
		System.out.println("VAI REMOVER");
		this.equipes.remove(this.equipeAdd);
	}
}
