package br.gov.al.maceio.sishosp.hosp.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import br.gov.al.maceio.sishosp.acl.model.FuncionarioBean;
import br.gov.al.maceio.sishosp.comum.util.JSFUtil;

public class EquipeBean implements Serializable {

	private Integer codEquipe;
	private String descEquipe;
	private Integer codUnidade;

	// LISTAS
	private List<FuncionarioBean> profissionais;
	private List<FuncionarioBean> profissionaisNovo;

	// HERDADO
	private FuncionarioBean profAdd;

	public EquipeBean() {
		this.profissionais = new ArrayList<FuncionarioBean>();
		this.profissionaisNovo = new ArrayList<FuncionarioBean>();
		this.profAdd = new FuncionarioBean();

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

	

	public List<FuncionarioBean> getProfissionais() {
		return profissionais;
	}

	public void setProfissionais(List<FuncionarioBean> profissionais) {
		this.profissionais = profissionais;
	}

	public List<FuncionarioBean> getProfissionaisNovo() {
		this.profissionaisNovo = this.profissionais;
		return profissionaisNovo;
	}

	public void setProfissionaisNovo(List<FuncionarioBean> profissionaisNovo) {
		this.profissionaisNovo = profissionaisNovo;
	}

	public FuncionarioBean getProfAdd() {
		return profAdd;
	}

	public void setProfAdd(FuncionarioBean profAdd) {
		this.profAdd = profAdd;
	}

	public void addProfList() {
		boolean existe = false;
		if (profissionais.size() == 0) {
			this.profissionais.add(this.profAdd);

		} else {

			for (int i = 0; i < profissionais.size(); i++) {
				if (profissionais.get(i).getId() == profAdd
						.getId()) {
					existe = true;
				}
			}
			if (existe == false) {
				this.profissionais.add(this.profAdd);
			} else {
				JSFUtil.adicionarMensagemAdvertencia("Esse profissional já foi adicionado!", "Advertência");
			}

		}
	}

	public void removeProfList() {
		this.profissionais.remove(this.profAdd);
	}

	public Integer getCodUnidade() {
		return codUnidade;
	}

	public void setCodUnidade(Integer codUnidade) {
		this.codUnidade = codUnidade;
	}
}
