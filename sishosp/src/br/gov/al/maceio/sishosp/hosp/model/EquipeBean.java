package br.gov.al.maceio.sishosp.hosp.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

public class EquipeBean implements Serializable {

	private Integer codEquipe;
	private String descEquipe;
	private Integer codEmpresa;

	// LISTAS
	private List<ProfissionalBean> profissionais;
	private List<ProfissionalBean> profissionaisNovo;

	// HERDADO
	private ProfissionalBean profAdd;

	public EquipeBean() {
		this.profissionais = new ArrayList<ProfissionalBean>();
		this.profissionaisNovo = new ArrayList<ProfissionalBean>();
		this.profAdd = new ProfissionalBean();

	}

	public Integer getCodEquipe() {
		return codEquipe;
	}

	public void setCodEquipe(Integer codEquipe) {
		this.codEquipe = codEquipe;
	}

	public String getDescEquipe() {
		return descEquipe;
	}

	public void setDescEquipe(String descEquipe) {
		this.descEquipe = descEquipe;
	}

	public Integer getCodEmpresa() {
		return codEmpresa;
	}

	public void setCodEmpresa(Integer codEmpresa) {
		this.codEmpresa = codEmpresa;
	}

	public List<ProfissionalBean> getProfissionais() {
		return profissionais;
	}

	public void setProfissionais(List<ProfissionalBean> profissionais) {
		this.profissionais = profissionais;
	}

	public List<ProfissionalBean> getProfissionaisNovo() {
		this.profissionaisNovo = this.profissionais;
		return profissionaisNovo;
	}

	public void setProfissionaisNovo(List<ProfissionalBean> profissionaisNovo) {
		this.profissionaisNovo = profissionaisNovo;
	}

	public ProfissionalBean getProfAdd() {
		return profAdd;
	}

	public void setProfAdd(ProfissionalBean profAdd) {
		this.profAdd = profAdd;
	}

	public void addProfList() {
		boolean existe = false;
		if (profissionais.size() == 0) {
			this.profissionais.add(this.profAdd);

		} else {

			for (int i = 0; i < profissionais.size(); i++) {
				if (profissionais.get(i).getIdProfissional() == profAdd
						.getIdProfissional()) {
					existe = true;
				}
			}
			if (existe == false) {
				this.profissionais.add(this.profAdd);
			} else {
				FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
						"Esse profissional já foi adicionado!", "Sucesso");
				FacesContext.getCurrentInstance().addMessage(null, msg);
			}

		}
	}

	public void removeProfList() {
		this.profissionais.remove(this.profAdd);
	}
}
