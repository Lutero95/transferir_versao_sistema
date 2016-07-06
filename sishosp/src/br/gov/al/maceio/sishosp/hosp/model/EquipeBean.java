package br.gov.al.maceio.sishosp.hosp.model;

import java.util.ArrayList;
import java.util.List;

public class EquipeBean {

	private Integer codEquipe;
	private String descEquipe;
	private Integer codEmpresa;
	private List<ProfissionalBean> profissionais;
	private List<ProfissionalBean> profissionaisNovo;
	private ProfissionalBean profAdd;
	private ProfissionalBean profRmv;

	public EquipeBean() {
		this.profissionais = new ArrayList<ProfissionalBean>();
		this.profissionaisNovo = new ArrayList<ProfissionalBean>();
		this.profAdd = new ProfissionalBean();
		this.profRmv = new ProfissionalBean();

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

	public ProfissionalBean getProfRmv() {
		return profRmv;
	}

	public void setProfAdd(ProfissionalBean profAdd) {
		this.profAdd = profAdd;
	}

	public void setProfRmv(ProfissionalBean profRmv) {
		this.profRmv = profRmv;
	}

	public void addProfList() {
		this.profissionais.add(this.profAdd);
	}

	public void removeProfList() {
		this.profissionais.remove(this.profRmv);
	}
}
