package br.gov.al.maceio.sishosp.hosp.model.dto;

import br.gov.al.maceio.sishosp.acl.model.FuncionarioBean;
import br.gov.al.maceio.sishosp.hosp.model.EquipeBean;
import br.gov.al.maceio.sishosp.hosp.model.ProcedimentoBean;

public class ProcedimentoProfissionalEquipeEspecificoDTO {

	private ProcedimentoBean procedimento;
	private FuncionarioBean profissional;
	private EquipeBean equipe;
	
	public ProcedimentoProfissionalEquipeEspecificoDTO() {
		procedimento = new ProcedimentoBean();
		profissional = new FuncionarioBean();
		equipe = new EquipeBean();
	}
	
	public ProcedimentoBean getProcedimento() {
		return procedimento;
	}
	public void setProcedimento(ProcedimentoBean procedimento) {
		this.procedimento = procedimento;
	}
	public FuncionarioBean getProfissional() {
		return profissional;
	}
	public void setProfissional(FuncionarioBean profissional) {
		this.profissional = profissional;
	}
	public EquipeBean getEquipe() {
		return equipe;
	}
	public void setEquipe(EquipeBean equipe) {
		this.equipe = equipe;
	}
}
