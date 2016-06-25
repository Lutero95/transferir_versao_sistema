package br.gov.al.maceio.sishosp.hosp.model;

import java.util.ArrayList;
import java.util.List;

public class EquipeBean {

	private Integer codEquipe;
	private String descEquipe;
	private Integer codEmpresa;
	private List<ProfissionalBean> profissionais;
	private ProfissionalBean prof;

	public EquipeBean() {
		this.profissionais = new ArrayList<ProfissionalBean>();
		this.prof = new ProfissionalBean();
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

	public ProfissionalBean getProf() {
		return prof;
	}
	
	public void setProf(ProfissionalBean prof) {
		this.prof = prof;
	}

	public void addProfList(){
		this.profissionais.add(prof);
		this.prof = new ProfissionalBean();
	}
	public void removeProfList(){
		System.out.println("asda " + this.prof.getDescricaoProf());
		this.profissionais.remove(this.prof);
		this.prof = new ProfissionalBean();
	}
}
