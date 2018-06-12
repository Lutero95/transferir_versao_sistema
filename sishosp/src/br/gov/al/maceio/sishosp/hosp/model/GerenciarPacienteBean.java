package br.gov.al.maceio.sishosp.hosp.model;

import java.io.Serializable;
import java.util.Date;

import br.gov.al.maceio.sishosp.acl.model.FuncionarioBean;

public class GerenciarPacienteBean implements Serializable {

	private Integer id;
	private String status;
	private Date data_operacao;
	private Date data_solicitacao;
	private Date data_cadastro;
	private String observacao;
	private String motivo_desligamento;
	private String tipo;

	// HERDADOS
	private GrupoBean grupo;
	private ProgramaBean programa;
	private PacienteBean paciente;
	private FuncionarioBean funcionario;
	private EquipamentoBean equipe;
	private LaudoBean laudo;

	public GerenciarPacienteBean() {
		grupo = new GrupoBean();
		programa = new ProgramaBean();
		paciente = new PacienteBean();
		funcionario = new FuncionarioBean();
		equipe = new EquipamentoBean();
		laudo = new LaudoBean();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getData_operacao() {
		return data_operacao;
	}

	public void setData_operacao(Date data_operacao) {
		this.data_operacao = data_operacao;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public String getMotivo_desligamento() {
		return motivo_desligamento;
	}

	public void setMotivo_desligamento(String motivo_desligamento) {
		this.motivo_desligamento = motivo_desligamento;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
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

	public PacienteBean getPaciente() {
		return paciente;
	}

	public void setPaciente(PacienteBean paciente) {
		this.paciente = paciente;
	}

	public FuncionarioBean getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(FuncionarioBean funcionario) {
		this.funcionario = funcionario;
	}

	public EquipamentoBean getEquipe() {
		return equipe;
	}

	public void setEquipe(EquipamentoBean equipe) {
		this.equipe = equipe;
	}

	public LaudoBean getLaudo() {
		return laudo;
	}

	public void setLaudo(LaudoBean laudo) {
		this.laudo = laudo;
	}

	public Date getData_solicitacao() {
		return data_solicitacao;
	}

	public void setData_solicitacao(Date data_solicitacao) {
		this.data_solicitacao = data_solicitacao;
	}

	public Date getData_cadastro() {
		return data_cadastro;
	}

	public void setData_cadastro(Date data_cadastro) {
		this.data_cadastro = data_cadastro;
	}

}
