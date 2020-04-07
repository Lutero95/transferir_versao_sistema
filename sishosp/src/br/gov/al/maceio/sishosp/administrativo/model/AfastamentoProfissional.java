package br.gov.al.maceio.sishosp.administrativo.model;

import br.gov.al.maceio.sishosp.acl.model.FuncionarioBean;
import br.gov.al.maceio.sishosp.administrativo.enums.TipoAfastamento;

import java.io.Serializable;
import java.util.Date;

public class AfastamentoProfissional implements Serializable {

    private Integer id;
    private FuncionarioBean funcionario;
    private String motivoAfastamento;
    private String motivoAfastamentoPorExtenso;
    private String tipoAfastamento;
    private Date periodoInicio;
    private Date periodoFinal;
    private String turno;

    public AfastamentoProfissional() {
        funcionario = new FuncionarioBean();
        tipoAfastamento = TipoAfastamento.TEMPORÃ.getSigla();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public FuncionarioBean getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(FuncionarioBean funcionario) {
        this.funcionario = funcionario;
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

	public String getTurno() {
		return turno;
	}

	public void setTurno(String turno) {
		this.turno = turno;
	}
}
