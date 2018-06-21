package br.gov.al.maceio.sishosp.hosp.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import br.gov.al.maceio.sishosp.acl.model.FuncionarioBean;

public class InsercaoPacienteBean implements Serializable {

	// HERDADOS
	private LaudoBean laudo;
	private EquipeBean equipe;
	private ArrayList<ProfissaoBean> listaProfissionais;

	public InsercaoPacienteBean() {
		laudo = new LaudoBean();
		equipe = new EquipeBean();
		listaProfissionais = new ArrayList<ProfissaoBean>();
	}

	public LaudoBean getLaudo() {
		return laudo;
	}

	public void setLaudo(LaudoBean laudo) {
		this.laudo = laudo;
	}

	public EquipeBean getEquipe() {
		return equipe;
	}

	public void setEquipe(EquipeBean equipe) {
		this.equipe = equipe;
	}

	public ArrayList<ProfissaoBean> getListaProfissionais() {
		return listaProfissionais;
	}

	public void setListaProfissionais(
			ArrayList<ProfissaoBean> listaProfissionais) {
		this.listaProfissionais = listaProfissionais;
	}

}
