package br.gov.al.maceio.sishosp.administrativo.model;

import java.util.Date;

import br.gov.al.maceio.sishosp.acl.model.FuncionarioBean;
import br.gov.al.maceio.sishosp.hosp.model.EquipeBean;
import br.gov.al.maceio.sishosp.hosp.model.GrupoBean;
import br.gov.al.maceio.sishosp.hosp.model.PacienteBean;
import br.gov.al.maceio.sishosp.hosp.model.ProgramaBean;

public class GestaoAbonoFaltaPaciente {
	
	private Integer id;
	private Date dataHoraOperacao;
	private FuncionarioBean funcionario;
	private ProgramaBean programa;
	private GrupoBean grupo;
	private EquipeBean equipe;
	private Date dataAbono;
	private String turno;
	private PacienteBean paciente;
	private String justificativa;
	
	public GestaoAbonoFaltaPaciente() {
		funcionario = new FuncionarioBean();
		programa = new ProgramaBean();
		grupo = new GrupoBean();
		equipe = new EquipeBean();
		paciente = new PacienteBean();
	}
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Date getDataHoraOperacao() {
		return dataHoraOperacao;
	}
	public void setDataHoraOperacao(Date dataHoraOperacao) {
		this.dataHoraOperacao = dataHoraOperacao;
	}
	public FuncionarioBean getFuncionario() {
		return funcionario;
	}
	public void setFuncionario(FuncionarioBean funcionario) {
		this.funcionario = funcionario;
	}
	public ProgramaBean getPrograma() {
		return programa;
	}
	public void setPrograma(ProgramaBean programa) {
		this.programa = programa;
	}
	public GrupoBean getGrupo() {
		return grupo;
	}
	public void setGrupo(GrupoBean grupo) {
		this.grupo = grupo;
	}
	public EquipeBean getEquipe() {
		return equipe;
	}
	public void setEquipe(EquipeBean equipe) {
		this.equipe = equipe;
	}
	public Date getDataAbono() {
		return dataAbono;
	}
	public void setDataAbono(Date dataAbono) {
		this.dataAbono = dataAbono;
	}
	public String getTurno() {
		return turno;
	}
	public void setTurno(String turno) {
		this.turno = turno;
	}
	public PacienteBean getPaciente() {
		return paciente;
	}
	public void setPaciente(PacienteBean paciente) {
		this.paciente = paciente;
	}
	public String getJustificativa() {
		return justificativa;
	}
	public void setJustificativa(String justificativa) {
		this.justificativa = justificativa;
	}
	
		
}
