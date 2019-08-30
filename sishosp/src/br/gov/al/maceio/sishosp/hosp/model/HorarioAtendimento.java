package br.gov.al.maceio.sishosp.hosp.model;

import br.gov.al.maceio.sishosp.acl.model.FuncionarioBean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class HorarioAtendimento implements Serializable {

    private Integer diaSemana;
    private String horario;
    private FuncionarioBean funcionario;
    private List<FuncionarioBean> listaFuncionarios;

    public HorarioAtendimento() {
        funcionario = new FuncionarioBean();
        listaFuncionarios = new ArrayList<>();
    }

    public Integer getDiaSemana() {
        return diaSemana;
    }

    public void setDiaSemana(Integer diaSemana) {
        this.diaSemana = diaSemana;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public FuncionarioBean getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(FuncionarioBean funcionario) {
        this.funcionario = funcionario;
    }

    public List<FuncionarioBean> getListaFuncionarios() {
        return listaFuncionarios;
    }

    public void setListaFuncionarios(List<FuncionarioBean> listaFuncionarios) {
        this.listaFuncionarios = listaFuncionarios;
    }
}
