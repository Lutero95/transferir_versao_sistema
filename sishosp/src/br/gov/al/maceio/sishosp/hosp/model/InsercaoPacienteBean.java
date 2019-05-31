package br.gov.al.maceio.sishosp.hosp.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.gov.al.maceio.sishosp.acl.model.FuncionarioBean;

public class InsercaoPacienteBean implements Serializable {

    // HERDADOS
    private Integer id;
    private LaudoBean laudo;
    private GrupoBean grupo;
    private ProgramaBean programa;
    private EquipeBean equipe;
    private String observacao;
    private FuncionarioBean funcionario;
    private AgendaBean agenda;
    private String OpcaoAtendimento;
    private Boolean encaixe;
    private Date dataSolicitacao;

    public InsercaoPacienteBean() {
        laudo = new LaudoBean();
        grupo = new GrupoBean();
        programa = new ProgramaBean();
        equipe = new EquipeBean();
        funcionario = new FuncionarioBean();
        agenda = new AgendaBean();
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

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }


    public FuncionarioBean getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(FuncionarioBean funcionario) {
        this.funcionario = funcionario;
    }

    public AgendaBean getAgenda() {
        return agenda;
    }

    public void setAgenda(AgendaBean agenda) {
        this.agenda = agenda;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOpcaoAtendimento() {
        return OpcaoAtendimento;
    }

    public void setOpcaoAtendimento(String opcaoAtendimento) {
        OpcaoAtendimento = opcaoAtendimento;
    }

    public Boolean getEncaixe() {
        return encaixe;
    }

    public void setEncaixe(Boolean encaixe) {
        this.encaixe = encaixe;
    }

	public Date getDataSolicitacao() {
		return dataSolicitacao;
	}

	public void setDataSolicitacao(Date dataSolicitacao) {
		this.dataSolicitacao = dataSolicitacao;
	}
}
