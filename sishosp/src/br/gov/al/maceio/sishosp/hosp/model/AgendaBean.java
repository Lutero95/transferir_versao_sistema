package br.gov.al.maceio.sishosp.hosp.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.gov.al.maceio.sishosp.acl.model.FuncionarioBean;

public class AgendaBean implements Serializable {


	private static final long serialVersionUID = 1L;
	private Integer idAgenda;
    private boolean prontoGravar;
    private Date dataAtendimento;
    private Date dataAtendimentoFinal;
    private String turno;
    private String observacao;
    private Integer qtd;
    private Integer max;
    private Date dataMarcacao;
    private String situacao;
    private String ativo;
    private String horario;
    private Integer qtdAtendimentosManha[];
    private Integer qtdAtendimentosTarde[];
    private Boolean encaixe;
    private String presenca;
    private Boolean avaliacao;
    private Boolean avulso;
    private String situacaoAtendimentoInformado;
    private List<String> listaNomeProfissionais;
    
    // HERDADOS
    private PacienteBean paciente;
    private ProcedimentoBean procedimento;
    private GrupoBean grupo;
    private ProgramaBean programa;
    private ProgramaBean programaAvaliacao;
    private TipoAtendimentoBean tipoAt;
    private FuncionarioBean profissional;
    private EquipeBean equipe;
    private UnidadeBean unidade;

    public AgendaBean() {
        this.paciente = new PacienteBean();
        this.procedimento = null;
        this.grupo = new GrupoBean();
        this.programa = new ProgramaBean();
        this.programaAvaliacao = new ProgramaBean();
        this.tipoAt = new TipoAtendimentoBean();
        this.profissional = new FuncionarioBean();
        this.equipe = new EquipeBean();
        this.prontoGravar = false;
        this.turno = new String();
        this.ativo = new String();
        this.situacao = new String();
        this.observacao = new String();
        this.dataAtendimento = null;
        this.dataAtendimentoFinal = null;
        this.dataMarcacao = null;
        this.qtd = null;
        this.max = null;
        this.idAgenda = null;
        this.unidade = new UnidadeBean();
        qtdAtendimentosManha = new Integer [5];
        qtdAtendimentosTarde = new Integer [5];
        encaixe = false;
        avulso = false;
        this.listaNomeProfissionais = new ArrayList<String>();
    }

    public Integer getIdAgenda() {
        return idAgenda;
    }

    public void setIdAgenda(Integer idAgenda) {
        this.idAgenda = idAgenda;
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

    public Date getDataMarcacao() {
        return dataMarcacao;
    }

    public void setDataMarcacao(Date dataMarcacao) {
        this.dataMarcacao = dataMarcacao;
    }

    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public String getAtivo() {
        return ativo;
    }

    public void setAtivo(String ativo) {
        this.ativo = ativo;
    }

    public Date getDataAtendimentoFinal() {
        return dataAtendimentoFinal;
    }

    public void setDataAtendimentoFinal(Date dataAtendimentoFinal) {
        this.dataAtendimentoFinal = dataAtendimentoFinal;
    }

  

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public Integer[] getQtdAtendimentosManha() {
        return qtdAtendimentosManha;
    }

    public void setQtdAtendimentosManha(Integer[] qtdAtendimentosManha) {
        this.qtdAtendimentosManha = qtdAtendimentosManha;
    }

    public Integer[] getQtdAtendimentosTarde() {
        return qtdAtendimentosTarde;
    }

    public void setQtdAtendimentosTarde(Integer[] qtdAtendimentosTarde) {
        this.qtdAtendimentosTarde = qtdAtendimentosTarde;
    }

    public Boolean getEncaixe() {
        return encaixe;
    }

    public void setEncaixe(Boolean encaixe) {
        this.encaixe = encaixe;
    }

	public String getPresenca() {
		return presenca;
	}

	public void setPresenca(String presenca) {
		this.presenca = presenca;
	}

    public Boolean getAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(Boolean avaliacao) {
        this.avaliacao = avaliacao;
    }

    public ProgramaBean getProgramaAvaliacao() {
        return programaAvaliacao;
    }

    public void setProgramaAvaliacao(ProgramaBean programaAvaliacao) {
        this.programaAvaliacao = programaAvaliacao;
    }

	public UnidadeBean getUnidade() {
		return unidade;
	}

	public void setUnidade(UnidadeBean unidade) {
		this.unidade = unidade;
	}

	public Boolean getAvulso() {
		return avulso;
	}

	public void setAvulso(Boolean avulso) {
		this.avulso = avulso;
	}


	public String getSituacaoAtendimentoInformado() {
		return situacaoAtendimentoInformado;
	}

	public void setSituacaoAtendimentoInformado(String situacaoAtendimentoInformado) {
		this.situacaoAtendimentoInformado = situacaoAtendimentoInformado;
	}

	public List<String> getListaNomeProfissionais() {
		return listaNomeProfissionais;
	}

	public void setListaNomeProfissionais(List<String> listaNomeProfissionais) {
		this.listaNomeProfissionais = listaNomeProfissionais;
	}
}
