package br.gov.al.maceio.sishosp.hosp.model;

import java.util.Date;

public class AgendaBean {
	
	private PacienteBean paciente;
	private ProcedimentoBean procedimento;
	private GrupoBean grupo;
	private ProgramaBean programa;
	private TipoAtendimentoBean tipoAt;
	private ProfissionalBean profissional;
	private EquipeBean equipe;
	private boolean prontoGravar;
	private Date dataAtendimento;
	private String turno;
	private String observacao;
	private Integer qtd;
	private Integer max;
	
	public AgendaBean() {
		this.paciente = null;// new PacienteBean();
		this.procedimento = null;
		this.grupo = null;// new GrupoBean();
		this.programa = null;// new ProgramaBean();
		this.tipoAt = null;// new TipoAtendimentoBean();
		this.profissional = new ProfissionalBean();
		this.equipe = new EquipeBean();;
		this.prontoGravar = false;
		this.turno = new String();
		this.observacao = new String();
		this.dataAtendimento = null;
		this.qtd = null;
		this.max = null;
	}

	public PacienteBean getPaciente() {
		return paciente;
	}

	public void setPaciente(PacienteBean paciente) {
		this.paciente = paciente;
	}

	public ProcedimentoBean getProcedimento() {
		return procedimento;
	}

	public void setProcedimento(ProcedimentoBean procedimento) {
		this.procedimento = procedimento;
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

	public TipoAtendimentoBean getTipoAt() {
		return tipoAt;
	}

	public void setTipoAt(TipoAtendimentoBean tipoAt) {
		this.tipoAt = tipoAt;
	}

	public ProfissionalBean getProfissional() {
		return profissional;
	}

	public void setProfissional(ProfissionalBean profissional) {
		this.profissional = profissional;
	}

	public EquipeBean getEquipe() {
		return equipe;
	}

	public void setEquipe(EquipeBean equipe) {
		this.equipe = equipe;
	}

	public boolean isProntoGravar() {
		return prontoGravar;
	}

	public void setProntoGravar(boolean prontoGravar) {
		this.prontoGravar = prontoGravar;
	}

	public Date getDataAtendimento() {
		return dataAtendimento;
	}

	public void setDataAtendimento(Date dataAtendimento) {
		this.dataAtendimento = dataAtendimento;
	}

	public String getTurno() {
		return turno;
	}

	public void setTurno(String turno) {
		this.turno = turno;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public Integer getQtd() {
		return qtd;
	}

	public void setQtd(Integer qtd) {
		this.qtd = qtd;
	}

	public Integer getMax() {
		return max;
	}

	public void setMax(Integer max) {
		this.max = max;
	}
	
}
