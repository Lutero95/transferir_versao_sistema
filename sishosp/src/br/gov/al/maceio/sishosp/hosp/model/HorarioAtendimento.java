package br.gov.al.maceio.sishosp.hosp.model;

import br.gov.al.maceio.sishosp.acl.model.FuncionarioBean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class HorarioAtendimento implements Serializable {

    private Integer diaSemana;
    private String horario;
    private FuncionarioBean funcionario;
    private String turno;
    private String diaNome;

    public HorarioAtendimento() {
        funcionario = new FuncionarioBean();
    }
    
    public HorarioAtendimento(Integer diaSemana, String turno) {
        funcionario = new FuncionarioBean();
        this.diaSemana = diaSemana;
        this.turno = turno;
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

	public String getTurno() {
		return turno;
	}

	public void setTurno(String turno) {
		this.turno = turno;
	}

	public String getDiaNome() {
		return diaNome;
	}

	public void setDiaNome(String diaNome) {
		this.diaNome = diaNome;
	}

}
