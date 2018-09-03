package br.gov.al.maceio.sishosp.hosp.model;

import java.io.Serializable;
import java.util.Date;

import br.gov.al.maceio.sishosp.acl.model.FuncionarioBean;

public class LaudoBean implements Serializable {

    private Integer id;
    private Date data_solicitacao;
    private Integer mes_inicio;
    private Integer ano_inicio;
    private Integer mes_final;
    private Integer ano_final;
    private Integer periodo;
    private String obs;
    private Date vigencia_inicial;
    private Date vigencia_final;

    // HERDADOS
    private PacienteBean paciente;
    private ProcedimentoBean procedimento_primario;
    private ProcedimentoBean procedimento_secundario1;
    private ProcedimentoBean procedimento_secundario2;
    private ProcedimentoBean procedimento_secundario3;
    private ProcedimentoBean procedimento_secundario4;
    private ProcedimentoBean procedimento_secundario5;
    private CidBean cid1;
    private CidBean cid2;
    private CidBean cid3;
    private RecursoBean recurso;

    public LaudoBean() {
        paciente = new PacienteBean();
        procedimento_primario = new ProcedimentoBean();
        procedimento_secundario1 = new ProcedimentoBean();
        procedimento_secundario2 = new ProcedimentoBean();
        procedimento_secundario3 = new ProcedimentoBean();
        procedimento_secundario4 = new ProcedimentoBean();
        procedimento_secundario5 = new ProcedimentoBean();
        cid1 = new CidBean();
        cid2 = new CidBean();
        cid3 = new CidBean();
        recurso = new RecursoBean();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getData_solicitacao() {
        return data_solicitacao;
    }

    public void setData_solicitacao(Date data_solicitacao) {
        this.data_solicitacao = data_solicitacao;
    }

    public Integer getMes_inicio() {
        return mes_inicio;
    }

    public void setMes_inicio(Integer mes_inicio) {
        this.mes_inicio = mes_inicio;
    }

    public Integer getAno_inicio() {
        return ano_inicio;
    }

    public void setAno_inicio(Integer ano_inicio) {
        this.ano_inicio = ano_inicio;
    }

    public Integer getMes_final() {
        return mes_final;
    }

    public void setMes_final(Integer mes_final) {
        this.mes_final = mes_final;
    }

    public Integer getAno_final() {
        return ano_final;
    }

    public void setAno_final(Integer ano_final) {
        this.ano_final = ano_final;
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

    public ProcedimentoBean getProcedimento_primario() {
        return procedimento_primario;
    }

    public void setProcedimento_primario(ProcedimentoBean procedimento_primario) {
        this.procedimento_primario = procedimento_primario;
    }

    public ProcedimentoBean getProcedimento_secundario1() {
        return procedimento_secundario1;
    }

    public void setProcedimento_secundario1(
            ProcedimentoBean procedimento_secundario1) {
        this.procedimento_secundario1 = procedimento_secundario1;
    }

    public ProcedimentoBean getProcedimento_secundario2() {
        return procedimento_secundario2;
    }

    public void setProcedimento_secundario2(
            ProcedimentoBean procedimento_secundario2) {
        this.procedimento_secundario2 = procedimento_secundario2;
    }

    public ProcedimentoBean getProcedimento_secundario3() {
        return procedimento_secundario3;
    }

    public void setProcedimento_secundario3(
            ProcedimentoBean procedimento_secundario3) {
        this.procedimento_secundario3 = procedimento_secundario3;
    }

    public ProcedimentoBean getProcedimento_secundario4() {
        return procedimento_secundario4;
    }

    public void setProcedimento_secundario4(
            ProcedimentoBean procedimento_secundario4) {
        this.procedimento_secundario4 = procedimento_secundario4;
    }

    public ProcedimentoBean getProcedimento_secundario5() {
        return procedimento_secundario5;
    }

    public void setProcedimento_secundario5(
            ProcedimentoBean procedimento_secundario5) {
        this.procedimento_secundario5 = procedimento_secundario5;
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

    public Date getVigencia_inicial() {
        return vigencia_inicial;
    }

    public void setVigencia_inicial(Date vigencia_inicial) {
        this.vigencia_inicial = vigencia_inicial;
    }

    public Date getVigencia_final() {
        return vigencia_final;
    }

    public void setVigencia_final(Date vigencia_final) {
        this.vigencia_final = vigencia_final;
    }

    public RecursoBean getRecurso() {
        return recurso;
    }

    public void setRecurso(RecursoBean recurso) {
        this.recurso = recurso;
    }
}
