package br.gov.al.maceio.sishosp.hosp.model;

import br.gov.al.maceio.sishosp.hosp.enums.SituacaoLaudo;

import java.io.Serializable;
import java.util.Date;

public class LaudoBean implements Serializable {

    private Integer id;
    private Date dataSolicitacao;
    private Integer mesInicio;
    private Integer anoInicio;
    private Integer mesFinal;
    private Integer anoFinal;
    private Integer periodo;
    private String obs;
    private Date vigenciaInicial;
    private Date vigenciaFinal;
    private Date dataAutorizacao;
    private String situacao;

    // HERDADOS
    private PacienteBean paciente;
    private ProcedimentoBean procedimentoPrimario;
    private ProcedimentoBean procedimentoSecundario1;
    private ProcedimentoBean procedimentoSecundario2;
    private ProcedimentoBean procedimentoSecundario3;
    private ProcedimentoBean procedimentoSecundario4;
    private ProcedimentoBean procedimentoSecundario5;
    private CidBean cid1;
    private CidBean cid2;
    private CidBean cid3;
    private RecursoBean recurso;

    public LaudoBean() {
        paciente = new PacienteBean();
        procedimentoPrimario = new ProcedimentoBean();
        procedimentoSecundario1 = new ProcedimentoBean();
        procedimentoSecundario2 = new ProcedimentoBean();
        procedimentoSecundario3 = new ProcedimentoBean();
        procedimentoSecundario4 = new ProcedimentoBean();
        procedimentoSecundario5 = new ProcedimentoBean();
        cid1 = new CidBean();
        cid2 = new CidBean();
        cid3 = new CidBean();
        recurso = new RecursoBean();
        situacao = SituacaoLaudo.PENDENTE.getSigla();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDataSolicitacao() {
        return dataSolicitacao;
    }

    public void setDataSolicitacao(Date dataSolicitacao) {
        this.dataSolicitacao = dataSolicitacao;
    }

    public Integer getMesInicio() {
        return mesInicio;
    }

    public void setMesInicio(Integer mesInicio) {
        this.mesInicio = mesInicio;
    }

    public Integer getAnoInicio() {
        return anoInicio;
    }

    public void setAnoInicio(Integer anoInicio) {
        this.anoInicio = anoInicio;
    }

    public Integer getMesFinal() {
        return mesFinal;
    }

    public void setMesFinal(Integer mesFinal) {
        this.mesFinal = mesFinal;
    }

    public Integer getAnoFinal() {
        return anoFinal;
    }

    public void setAnoFinal(Integer anoFinal) {
        this.anoFinal = anoFinal;
    }

    public Integer getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Integer periodo) {
        this.periodo = periodo;
    }

    public String getObs() {
        return obs;
    }

    public void setObs(String obs) {
        this.obs = obs;
    }

    public PacienteBean getPaciente() {
        return paciente;
    }

    public void setPaciente(PacienteBean paciente) {
        this.paciente = paciente;
    }

    public ProcedimentoBean getProcedimentoPrimario() {
        return procedimentoPrimario;
    }

    public void setProcedimentoPrimario(ProcedimentoBean procedimentoPrimario) {
        this.procedimentoPrimario = procedimentoPrimario;
    }

    public ProcedimentoBean getProcedimentoSecundario1() {
        return procedimentoSecundario1;
    }

    public void setProcedimentoSecundario1(
            ProcedimentoBean procedimentoSecundario1) {
        this.procedimentoSecundario1 = procedimentoSecundario1;
    }

    public ProcedimentoBean getProcedimentoSecundario2() {
        return procedimentoSecundario2;
    }

    public void setProcedimentoSecundario2(
            ProcedimentoBean procedimentoSecundario2) {
        this.procedimentoSecundario2 = procedimentoSecundario2;
    }

    public ProcedimentoBean getProcedimentoSecundario3() {
        return procedimentoSecundario3;
    }

    public void setProcedimentoSecundario3(
            ProcedimentoBean procedimentoSecundario3) {
        this.procedimentoSecundario3 = procedimentoSecundario3;
    }

    public ProcedimentoBean getProcedimentoSecundario4() {
        return procedimentoSecundario4;
    }

    public void setProcedimentoSecundario4(
            ProcedimentoBean procedimentoSecundario4) {
        this.procedimentoSecundario4 = procedimentoSecundario4;
    }

    public ProcedimentoBean getProcedimentoSecundario5() {
        return procedimentoSecundario5;
    }

    public void setProcedimentoSecundario5(
            ProcedimentoBean procedimentoSecundario5) {
        this.procedimentoSecundario5 = procedimentoSecundario5;
    }

    public CidBean getCid1() {
        return cid1;
    }

    public void setCid1(CidBean cid1) {
        this.cid1 = cid1;
    }

    public CidBean getCid2() {
        return cid2;
    }

    public void setCid2(CidBean cid2) {
        this.cid2 = cid2;
    }

    public CidBean getCid3() {
        return cid3;
    }

    public void setCid3(CidBean cid3) {
        this.cid3 = cid3;
    }

    public Date getVigenciaInicial() {
        return vigenciaInicial;
    }

    public void setVigenciaInicial(Date vigenciaInicial) {
        this.vigenciaInicial = vigenciaInicial;
    }

    public Date getVigenciaFinal() {
        return vigenciaFinal;
    }

    public void setVigenciaFinal(Date vigenciaFinal) {
        this.vigenciaFinal = vigenciaFinal;
    }

    public RecursoBean getRecurso() {
        return recurso;
    }

    public void setRecurso(RecursoBean recurso) {
        this.recurso = recurso;
    }

    public Date getDataAutorizacao() {
        return dataAutorizacao;
    }

    public void setDataAutorizacao(Date dataAutorizacao) {
        this.dataAutorizacao = dataAutorizacao;
    }

    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }
}
