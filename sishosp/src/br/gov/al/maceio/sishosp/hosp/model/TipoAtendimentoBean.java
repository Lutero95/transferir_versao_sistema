package br.gov.al.maceio.sishosp.hosp.model;

import br.gov.al.maceio.sishosp.comum.util.JSFUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

public class TipoAtendimentoBean implements Serializable {

	private Integer idTipo;
	private String descTipoAt;
	private Boolean equipe;
	private boolean primeiroAt;
	private Integer codEmpresa;
	private Integer intervaloMinimo;

	// LISTAS
	private List<GrupoBean> grupo;
	private List<GrupoBean> grupoNovo;

	// HERDADOS
	private GrupoBean grupoParaAdd;

	public TipoAtendimentoBean() {
		this.grupo = new ArrayList<GrupoBean>();
		this.grupoNovo = new ArrayList<GrupoBean>();
		this.descTipoAt = new String();
		this.equipe = null;
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

	public void addGrupoLista() {
		boolean existe = false;
		if (grupo.size() == 0) {
			this.grupo.add(grupoParaAdd);
		} else {
			for (int i = 0; i < grupo.size(); i++) {
				if (grupo.get(i).getIdGrupo() == grupoParaAdd.getIdGrupo()) {
					existe = true;
				}
			}
			if (existe == false) {
				this.grupo.add(grupoParaAdd);
			} else {
				JSFUtil.adicionarMensagemAdvertencia("Esse grupo já foi adicionado!", "Advertência");
			}
		}
	}

	public void rmvGrupoLista() {
		this.grupo.remove(grupoParaAdd);
	}

	public List<GrupoBean> getGrupoNovo() {
		this.grupoNovo = this.grupo;
		return grupoNovo;
	}

	public void setGrupoNovo(List<GrupoBean> grupoNovo) {
		this.grupoNovo = grupoNovo;
	}

	public Integer getIntervaloMinimo() {
		return intervaloMinimo;
	}

	public void setIntervaloMinimo(Integer intervaloMinimo) {
		this.intervaloMinimo = intervaloMinimo;
	}

	public Boolean getEquipe() {
		return equipe;
	}

	public void setEquipe(Boolean equipe) {
		this.equipe = equipe;
	}
}
