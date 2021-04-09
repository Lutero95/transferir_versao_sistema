package br.gov.al.maceio.sishosp.hosp.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.gov.al.maceio.sishosp.acl.model.FuncionarioBean;


public class PtsCifBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
	private PacienteBean paciente;
	private List<FuncionarioBean> listaAvaliadores;
	private String queixaPrincipal;
	private String condicoesSaude;
	private String funcaoIhEstruturaCorpo;
	private String atividadeIhParticipacao;
	private String fatoresContextuais;
	private FatoresAmbientaisPtcCifBean fatoresAmbientais;
	private String fatoresPessoais;
	private List<ObjetivoPtsCifBean> listaObjetivos;
	private GerenciarPacienteBean gerenciarPaciente;
	private Integer idadePaciente;
	private Date dataAvaliacao;
	private Date dataVencimento;
	
	public PtsCifBean() {
		this.paciente = new PacienteBean();
		this.listaAvaliadores = new ArrayList<>();
		this.fatoresAmbientais = new FatoresAmbientaisPtcCifBean();
		this.listaObjetivos = new ArrayList<>();
		this.gerenciarPaciente = new GerenciarPacienteBean();
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

	public List<FuncionarioBean> getListaAvaliadores() {
		return listaAvaliadores;
	}

	public void setListaAvaliadores(List<FuncionarioBean> listaAvaliadores) {
		this.listaAvaliadores = listaAvaliadores;
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

	public String getFuncaoIhEstruturaCorpo() {
		return funcaoIhEstruturaCorpo;
	}

	public void setFuncaoIhEstruturaCorpo(String funcaoIhEstruturaCorpo) {
		this.funcaoIhEstruturaCorpo = funcaoIhEstruturaCorpo;
	}

	public String getAtividadeIhParticipacao() {
		return atividadeIhParticipacao;
	}

	public void setAtividadeIhParticipacao(String atividadeIhParticipacao) {
		this.atividadeIhParticipacao = atividadeIhParticipacao;
	}

	public String getFatoresContextuais() {
		return fatoresContextuais;
	}

	public void setFatoresContextuais(String fatoresContextuais) {
		this.fatoresContextuais = fatoresContextuais;
	}

	public FatoresAmbientaisPtcCifBean getFatoresAmbientais() {
		return fatoresAmbientais;
	}

	public void setFatoresAmbientais(FatoresAmbientaisPtcCifBean fatoresAmbientais) {
		this.fatoresAmbientais = fatoresAmbientais;
	}

	public String getFatoresPessoais() {
		return fatoresPessoais;
	}

	public void setFatoresPessoais(String fatoresPessoais) {
		this.fatoresPessoais = fatoresPessoais;
	}

	public List<ObjetivoPtsCifBean> getListaObjetivos() {
		return listaObjetivos;
	}

	public void setListaObjetivos(List<ObjetivoPtsCifBean> listaObjetivos) {
		this.listaObjetivos = listaObjetivos;
	}

	public GerenciarPacienteBean getGerenciarPaciente() {
		return gerenciarPaciente;
	}

	public void setGerenciarPaciente(GerenciarPacienteBean gerenciarPaciente) {
		this.gerenciarPaciente = gerenciarPaciente;
	}

	public Integer getIdadePaciente() {
		return idadePaciente;
	}

	public void setIdadePaciente(Integer idadePaciente) {
		this.idadePaciente = idadePaciente;
	}

	public Date getDataAvaliacao() {
		return dataAvaliacao;
	}

	public void setDataAvaliacao(Date dataAvaliacao) {
		this.dataAvaliacao = dataAvaliacao;
	}

	public Date getDataVencimento() {
		return dataVencimento;
	}

	public void setDataVencimento(Date dataVencimento) {
		this.dataVencimento = dataVencimento;
	}
}
