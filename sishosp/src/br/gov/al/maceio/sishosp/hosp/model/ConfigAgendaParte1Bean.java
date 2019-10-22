package br.gov.al.maceio.sishosp.hosp.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.gov.al.maceio.sishosp.acl.model.FuncionarioBean;

public class ConfigAgendaParte1Bean implements Serializable {

    private Integer idConfiAgenda;
    private Integer mes;
    private Integer ano;
    private FuncionarioBean profissional;
    private EquipeBean equipe;
    private ArrayList<String> diasSemana;
    private Date dataEspecifica;
    private String turno;
    private String horarioInicio;
    private String horarioFinal;
    private Integer qtdMax;
    private Integer diaDaSemana;
    private String opcao;
    private String tipo;
    private String diaSemana;
    private UnidadeBean unidade;
    private String diasPorExtenso;

    public ConfigAgendaParte1Bean() {
        this.profissional = new FuncionarioBean();
        this.equipe = new EquipeBean();
        this.diasSemana = new ArrayList<String>();
        opcao = "2";
        tipo = "G"; 
        unidade = new UnidadeBean();
        turno = "";
    }

    public Integer getMes() {
        return mes;
    }

    public void setMes(Integer mes) {
        this.mes = mes;
    }

    public Integer getAno() {
        return ano;
    }

    public void setAno(Integer ano) {
        this.ano = ano;
    }

    public FuncionarioBean getProfissional() {
        return profissional;
    }

    public void setProfissional(FuncionarioBean profissional) {
        this.profissional = profissional;
    }

    public ArrayList<String> getDiasSemana() {
        return diasSemana;
    }

    public void setDiasSemana(ArrayList<String> diasSemana) {
        this.diasSemana = diasSemana;
    }

    public Date getDataEspecifica() {
        return dataEspecifica;
    }

    public void setDataEspecifica(Date dataEspecifica) {
        this.dataEspecifica = dataEspecifica;
    }

    public String getTurno() {
        return turno;
    }

    public void setTurno(String turno) {
        this.turno = turno;
    }

    public Integer getQtdMax() {
        return qtdMax;
    }

    public void setQtdMax(Integer qtdMax) {
        this.qtdMax = qtdMax;
    }

    public Integer getIdConfiAgenda() {
        return idConfiAgenda;
    }

    public void setIdConfiAgenda(Integer idConfiAgenda) {
        this.idConfiAgenda = idConfiAgenda;
    }

    public Integer getDiaDaSemana() {
        return diaDaSemana;
    }

    public void setDiaDaSemana(Integer diaDaSemana) {
        this.diaDaSemana = diaDaSemana;
    }

    public EquipeBean getEquipe() {
        return equipe;
    }

    public void setEquipe(EquipeBean equipe) {
        this.equipe = equipe;
    }

    public String getOpcao() {
        return opcao;
    }

    public void setOpcao(String opcao) {
        this.opcao = opcao;
    }

    public String getDiaSemana() {
        return diaSemana;
    }

    public void setDiaSemana(String diaSemana) {
        this.diaSemana = diaSemana;
    }

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public UnidadeBean getUnidade() {
		return unidade;
	}

	public void setUnidade(UnidadeBean unidade) {
		this.unidade = unidade;
	}

    public String getDiasPorExtenso() {
        return diasPorExtenso;
    }

    public void setDiasPorExtenso(String diasPorExtenso) {
        this.diasPorExtenso = diasPorExtenso;
    }

    public String getHorarioInicio() {
        return horarioInicio;
    }

    public void setHorarioInicio(String horarioInicio) {
        this.horarioInicio = horarioInicio;
    }

    public String getHorarioFinal() {
        return horarioFinal;
    }

    public void setHorarioFinal(String horarioFinal) {
        this.horarioFinal = horarioFinal;
    }
}
