package br.gov.al.maceio.sishosp.hosp.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

public class GrupoBean implements Serializable {

	private Integer idGrupo;
	private String descGrupo;
	private boolean auditivo;
	private boolean equipeSim;
	private boolean insercao_pac_institut;
	private Integer codUnidade;
	

	// LISTAS
	private List<EquipeBean> equipes;
	private List<EquipeBean> equipesNovo;

	// HERDADO
	private EquipeBean equipeAdd;

	public GrupoBean() {
		this.idGrupo = null;
		this.equipes = new ArrayList<EquipeBean>();
		this.equipesNovo = new ArrayList<EquipeBean>();
		this.equipeAdd = null;
		equipeSim = false;
		
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
		return equipeAdd;
	}

	public void setEquipeAdd(EquipeBean equipeAdd) {
		this.equipeAdd = equipeAdd;
	}

	public void addEquipe() {
		boolean existe = false;
		if (equipes.size() == 0) {
			this.equipes.add(this.equipeAdd);

		} else {

			for (int i = 0; i < equipes.size(); i++) {
				if (equipes.get(i).getCodEquipe() == equipeAdd.getCodEquipe()) {
					existe = true;
				}
			}
			if (existe == false) {
				this.equipes.add(this.equipeAdd);
			} else {
				FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Essa equipe já foi adicionada!", "Erro");
				FacesContext.getCurrentInstance().addMessage(null, msg);
			}

		}
	}

	public void rmvEquipe() {
		this.equipes.remove(this.equipeAdd);
	}


	public boolean isinsercao_pac_institut() {
		return insercao_pac_institut;
	}

	public void setinsercao_pac_institut(boolean insercao_pac_institut) {
		this.insercao_pac_institut = insercao_pac_institut;
	}

	public boolean isEquipeSim() {
		return equipeSim;
	}

	public void setEquipeSim(boolean equipeSim) {
		this.equipeSim = equipeSim;
	}

	public boolean isInsercao_pac_institut() {
		return insercao_pac_institut;
	}

	
	public void setInsercao_pac_institut(boolean insercao_pac_institut) {
		this.insercao_pac_institut = insercao_pac_institut;
	}

	public Integer getCodUnidade() {
		return codUnidade;
	}

	public void setCodUnidade(Integer codUnidade) {
		this.codUnidade = codUnidade;
	}

	

}
