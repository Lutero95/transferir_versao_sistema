package br.gov.al.maceio.sishosp.hosp.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

public class ProgramaBean implements Serializable {

	private Integer idPrograma;
	private String descPrograma;
	private Double codFederal;

	// LISTAS
	private List<GrupoBean> grupo;
	private List<GrupoBean> grupoNovo;

	// HERDADOS
	private GrupoBean grupoBean;
	private GrupoBean grupoParaAdd;
	private GrupoBean grupoRmv;

	public ProgramaBean() {
		this.grupoBean = new GrupoBean();
		this.grupo = new ArrayList<GrupoBean>();
		this.grupoRmv = new GrupoBean();
		this.grupoParaAdd = new GrupoBean();
		this.grupoNovo = new ArrayList<GrupoBean>();
		this.idPrograma = null;
		this.descPrograma = new String();
		this.codFederal = null;
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

	public List<GrupoBean> getGrupoNovo() {
		this.grupoNovo = this.grupo;
		return grupoNovo;
	}

	public void setGrupoNovo(List<GrupoBean> grupoNovo) {
		this.grupoNovo = grupoNovo;
	}

	public void setGrupoParaAdd(GrupoBean grupoParaAdd) {
		this.grupoParaAdd = grupoParaAdd;
	}

	public GrupoBean getGrupoRmv() {
		return grupoRmv;
	}

	public void setGrupoRmv(GrupoBean grupoRmv) {
		this.grupoRmv = grupoRmv;
	}

	public void addGrupoLista() {
		boolean existe = false;
		if (grupo.size() == 0) {
			this.grupo.add(this.grupoParaAdd);
		} else {
			for (int i = 0; i < grupo.size(); i++) {
				if (grupo.get(i).getIdGrupo() == grupoParaAdd.getIdGrupo()) {
					existe = true;
				}
			}
			if (existe == false) {
				this.grupo.add(this.grupoParaAdd);
			} else {
				FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
						"Esse Grupo já foi adicionado!", "Sucesso");
				FacesContext.getCurrentInstance().addMessage(null, msg);
			}
		}
	}

	public void removeGrupoLista() {
		this.grupo.remove(this.grupoRmv);
	}

	public GrupoBean getGrupoBean() {
		return grupoBean;
	}

	public void setGrupoBean(GrupoBean grupoBean) {
		this.grupoBean = grupoBean;
	}
}
