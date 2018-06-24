package br.gov.al.maceio.sishosp.hosp.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.gov.al.maceio.sishosp.acl.model.FuncionarioBean;

public class InsercaoPacienteBean implements Serializable {

	// HERDADOS
	private LaudoBean laudo;
	private GrupoBean grupo;
	private ProgramaBean programa;
	private EquipeBean equipe;
	private ArrayList<String> diasSemana;

	public InsercaoPacienteBean() {
		laudo = new LaudoBean();
		grupo = new GrupoBean();
		programa = new ProgramaBean();
		equipe = new EquipeBean();
		diasSemana = new ArrayList<String>();
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

	public GrupoBean getGrupo() {
		return grupo;
	}

	public void setGrupo(GrupoBean grupo) {
		this.grupo = grupo;
	}

	public ProgramaBean getPrograma() {
		return programa;
	}

	public void setPrograma(ProgramaBean programa) {
		this.programa = programa;
	}

	public ArrayList<String> getDiasSemana() {
		return diasSemana;
	}

	public void setDiasSemana(ArrayList<String> diasSemana) {
		this.diasSemana = diasSemana;
	}

}
