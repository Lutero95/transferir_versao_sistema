package br.gov.al.maceio.sishosp.hosp.model;

import java.io.Serializable;
import java.util.Date;

import br.gov.al.maceio.sishosp.acl.model.FuncionarioBean;

public class AgendaBean implements Serializable {

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

    // HERDADOS
    private PacienteBean paciente;
    private ProcedimentoBean procedimento;
    private GrupoBean grupo;
    private ProgramaBean programa;
    private TipoAtendimentoBean tipoAt;
    private FuncionarioBean profissional;
    private EquipeBean equipe;
    private EmpresaBean empresa;

    public AgendaBean() {
        this.paciente = new PacienteBean();
        this.procedimento = null;
        this.grupo = new GrupoBean();
        this.programa = new ProgramaBean();
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
        this.empresa = new EmpresaBean();
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

    public EmpresaBean getEmpresa() {
        return empresa;
    }

    public void setEmpresa(EmpresaBean empresa) {
        this.empresa = empresa;
    }

}
