package br.gov.al.maceio.sishosp.administrativo.model;

import br.gov.al.maceio.sishosp.acl.model.FuncionarioBean;
import br.gov.al.maceio.sishosp.administrativo.enums.TipoAfastamento;

import java.io.Serializable;
import java.util.Date;

public class AfastamentoTemporario implements Serializable {

    private Integer id;
    private FuncionarioBean funcionarioBean;
    private String motivoAfastamento;
    private String motivoAfastamentoPorExtenso;
    private String tipoAfastamento;
    private Date periodoInicio;
    private Date periodoFinal;

    public AfastamentoTemporario() {
        funcionarioBean = new FuncionarioBean();
        tipoAfastamento = TipoAfastamento.TEMPORÁRIO.getSigla();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public FuncionarioBean getFuncionarioBean() {
        return funcionarioBean;
    }

    public void setFuncionarioBean(FuncionarioBean funcionarioBean) {
        this.funcionarioBean = funcionarioBean;
    }

    public String getMotivoAfastamento() {
        return motivoAfastamento;
    }

    public void setMotivoAfastamento(String motivoAfastamento) {
        this.motivoAfastamento = motivoAfastamento;
    }

    public Date getPeriodoInicio() {
        return periodoInicio;
    }

    public void setPeriodoInicio(Date periodoInicio) {
        this.periodoInicio = periodoInicio;
    }

    public Date getPeriodoFinal() {
        return periodoFinal;
    }

    public void setPeriodoFinal(Date periodoFinal) {
        this.periodoFinal = periodoFinal;
    }

    public String getTipoAfastamento() {
        return tipoAfastamento;
    }

    public void setTipoAfastamento(String tipoAfastamento) {
        this.tipoAfastamento = tipoAfastamento;
    }

    public String getMotivoAfastamentoPorExtenso() {
        return motivoAfastamentoPorExtenso;
    }

    public void setMotivoAfastamentoPorExtenso(String motivoAfastamentoPorExtenso) {
        this.motivoAfastamentoPorExtenso = motivoAfastamentoPorExtenso;
    }
}
