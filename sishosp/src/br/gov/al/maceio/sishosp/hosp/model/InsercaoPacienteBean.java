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
    private boolean insercaoPacienteSemLaudo;
    private GrupoBean grupoAtual;
    private ProgramaBean programaAtual;
    private EquipeBean equipeAtual;
    private PacienteBean paciente;
    
    private String observacao;
    private FuncionarioBean funcionario;
    private String OpcaoAtendimento;
    private Boolean encaixe;
    private Date dataSolicitacao;
    private HorarioAtendimento horarioAtendimento;
    private List<HorarioAtendimento> listaHorarioAtendimento;
    private String turno;
    private String horario;

    public InsercaoPacienteBean() {
        laudo = new LaudoBean();
        grupo = new GrupoBean();
        programa = new ProgramaBean();
        equipe = new EquipeBean();
        grupoAtual = new GrupoBean();
        programaAtual = new ProgramaBean();
        equipeAtual = new EquipeBean();
        funcionario = new FuncionarioBean();
        horarioAtendimento = new HorarioAtendimento();
        listaHorarioAtendimento = new ArrayList<>();
        paciente = new PacienteBean();
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

    public HorarioAtendimento getHorarioAtendimento() {
        return horarioAtendimento;
    }

    public void setHorarioAtendimento(HorarioAtendimento horarioAtendimento) {
        this.horarioAtendimento = horarioAtendimento;
    }

    public List<HorarioAtendimento> getListaHorarioAtendimento() {
        return listaHorarioAtendimento;
    }

    public void setListaHorarioAtendimento(List<HorarioAtendimento> listaHorarioAtendimento) {
        this.listaHorarioAtendimento = listaHorarioAtendimento;
    }

	public GrupoBean getGrupoAtual() {
		return grupoAtual;
	}

	public ProgramaBean getProgramaAtual() {
		return programaAtual;
	}

	public EquipeBean getEquipeAtual() {
		return equipeAtual;
	}

	public void setGrupoAtual(GrupoBean grupoAtual) {
		this.grupoAtual = grupoAtual;
	}

	public void setProgramaAtual(ProgramaBean programaAtual) {
		this.programaAtual = programaAtual;
	}

	public void setEquipeAtual(EquipeBean equipeAtual) {
		this.equipeAtual = equipeAtual;
	}

	public boolean isInsercaoPacienteSemLaudo() {
		return insercaoPacienteSemLaudo;
	}

	public void setInsercaoPacienteSemLaudo(boolean insercaoPacienteSemLaudo) {
		this.insercaoPacienteSemLaudo = insercaoPacienteSemLaudo;
	}

	public PacienteBean getPaciente() {
		return paciente;
	}

	public void setPaciente(PacienteBean paciente) {
		this.paciente = paciente;
	}

	public String getTurno() {
		return turno;
	}

	public void setTurno(String turno) {
		this.turno = turno;
	}

	public String getHorario() {
		return horario;
	}

	public void setHorario(String horario) {
		this.horario = horario;
	}
}
