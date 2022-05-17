package br.gov.al.maceio.sishosp.hosp.model;

import org.apache.poi.ss.formula.functions.T;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;


public class PtsCifBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
	private PacienteBean paciente;
	private GerenciarPacienteBean gerenciarPaciente;

	private List<AvaliadorPtsCifBean> listaAvaliadores;
	private List<ObjetivoPtsCifBean> listaObjetivos;
	private CompetenciaPtsCifBean competencia;

	private Boolean bloqueado;

	private String facilitador;
	private String barreira;

	private String queixaPrincipal;
	private String condicoesSaude;
	private String funcaoEstruturaCorpo;
	private String atividadeParticipacao;
	private String fatoresContextuais;
	private String fatoresPessoais;

	private Timestamp ultimaModificacao;

	public PtsCifBean() {
		this.paciente = new PacienteBean();
		this.gerenciarPaciente = new GerenciarPacienteBean();

		this.listaAvaliadores = new ArrayList<>();
		this.listaObjetivos = new ArrayList<>();
		this.competencia = new CompetenciaPtsCifBean();
		this.ultimaModificacao = new Timestamp(0);
	}

	public PtsCifBean(PtsCifBean original){
		this.paciente = new PacienteBean();
		this.gerenciarPaciente = new GerenciarPacienteBean();

		this.listaAvaliadores = new ArrayList<>();
		this.listaObjetivos = new ArrayList<>();
		this.competencia = new CompetenciaPtsCifBean();
		this.ultimaModificacao = new Timestamp(0);

		this.getGerenciarPaciente().setId(original.getGerenciarPaciente().getId());
		this.getCompetencia().setId(original.getCompetencia().getId());
		this.getCompetencia().setDescricao(original.getCompetencia().getDescricao());
		this.getCompetencia().setDataInicial(original.getCompetencia().getDataInicial());
		this.getCompetencia().setDataFinal(original.getCompetencia().getDataFinal());
		this.getCompetencia().setAtiva(original.getCompetencia().getAtiva());
		this.getUltimaModificacao().setTime(original.getUltimaModificacao().getTime());

		this.setId(original.getId());
		this.setListaAvaliadores(new ArrayList<>(original.getListaAvaliadores()));
		this.setListaObjetivos(new ArrayList<>(original.getListaObjetivos()));
		this.setFacilitador(original.getFacilitador());
		this.setBarreira(original.getBarreira());
		this.setQueixaPrincipal(original.getQueixaPrincipal());
		this.setCondicoesSaude(original.getCondicoesSaude());
		this.setFuncaoEstruturaCorpo(original.getFuncaoEstruturaCorpo());
		this.setAtividadeParticipacao(original.getAtividadeParticipacao());
		this.setFatoresPessoais(original.getFatoresPessoais());
		this.setFatoresContextuais(original.getFatoresContextuais());
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public PacienteBean getPaciente() {
		return paciente;
	}

	public void setPaciente(PacienteBean paciente) {
		this.paciente = paciente;
	}

	public GerenciarPacienteBean getGerenciarPaciente() {
		return gerenciarPaciente;
	}

	public void setGerenciarPaciente(GerenciarPacienteBean gerenciarPaciente) {
		this.gerenciarPaciente = gerenciarPaciente;
	}

	public List<AvaliadorPtsCifBean> getListaAvaliadores() {
		return listaAvaliadores;
	}

	public void setListaAvaliadores(List<AvaliadorPtsCifBean> listaAvaliadores) {
		this.listaAvaliadores = listaAvaliadores;
	}

	public List<ObjetivoPtsCifBean> getListaObjetivos() {
		return listaObjetivos;
	}

	public void setListaObjetivos(List<ObjetivoPtsCifBean> listaObjetivos) {
		this.listaObjetivos = listaObjetivos;
	}

	public CompetenciaPtsCifBean getCompetencia() {
		return competencia;
	}

	public void setCompetencia(CompetenciaPtsCifBean competencia) {
		this.competencia = competencia;
	}

	public Boolean getBloqueado() {
		return bloqueado;
	}

	public void setBloqueado(Boolean bloqueado) {
		this.bloqueado = bloqueado;
	}

	public String getFacilitador() {
		return facilitador;
	}

	public void setFacilitador(String facilitador) {
		this.facilitador = facilitador;
	}

	public String getBarreira() {
		return barreira;
	}

	public void setBarreira(String barreira) {
		this.barreira = barreira;
	}

	public String getQueixaPrincipal() {
		return queixaPrincipal;
	}

	public void setQueixaPrincipal(String queixaPrincipal) {
		this.queixaPrincipal = queixaPrincipal;
	}

	public String getCondicoesSaude() {
		return condicoesSaude;
	}

	public void setCondicoesSaude(String condicoesSaude) {
		this.condicoesSaude = condicoesSaude;
	}

	public String getFuncaoEstruturaCorpo() {
		return funcaoEstruturaCorpo;
	}

	public void setFuncaoEstruturaCorpo(String funcaoEstruturaCorpo) {
		this.funcaoEstruturaCorpo = funcaoEstruturaCorpo;
	}

	public String getAtividadeParticipacao() {
		return atividadeParticipacao;
	}

	public void setAtividadeParticipacao(String atividadeParticipacao) {
		this.atividadeParticipacao = atividadeParticipacao;
	}

	public String getFatoresContextuais() {
		return fatoresContextuais;
	}

	public void setFatoresContextuais(String fatoresContextuais) {
		this.fatoresContextuais = fatoresContextuais;
	}

	public String getFatoresPessoais() {
		return fatoresPessoais;
	}

	public void setFatoresPessoais(String fatoresPessoais) {
		this.fatoresPessoais = fatoresPessoais;
	}

	public Timestamp getUltimaModificacao() {
		return ultimaModificacao;
	}

	public void setUltimaModificacao(Timestamp ultimaModificacao) {
		this.ultimaModificacao = ultimaModificacao;
	}
}
