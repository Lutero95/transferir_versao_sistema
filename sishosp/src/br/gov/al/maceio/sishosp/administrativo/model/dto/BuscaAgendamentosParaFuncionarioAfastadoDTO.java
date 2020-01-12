package br.gov.al.maceio.sishosp.administrativo.model.dto;

import br.gov.al.maceio.sishosp.acl.model.FuncionarioBean;
import br.gov.al.maceio.sishosp.hosp.model.GrupoBean;
import br.gov.al.maceio.sishosp.hosp.model.ProgramaBean;

import java.io.Serializable;
import java.util.Date;

public class BuscaAgendamentosParaFuncionarioAfastadoDTO implements Serializable {

    private FuncionarioBean funcionario;
    private ProgramaBean programa;
    private GrupoBean grupo;
    private Date periodoInicio;
    private Date periodoFinal;
    private String turno;

    public BuscaAgendamentosParaFuncionarioAfastadoDTO() {
        funcionario = new FuncionarioBean();
        programa = new ProgramaBean();
        grupo = new GrupoBean();
    }

    public FuncionarioBean getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(FuncionarioBean funcionario) {
        this.funcionario = funcionario;
    }

    public ProgramaBean getPrograma() {
        return programa;
    }

    public void setPrograma(ProgramaBean programa) {
        this.programa = programa;
    }

    public GrupoBean getGrupo() {
        return grupo;
    }

    public void setGrupo(GrupoBean grupo) {
        this.grupo = grupo;
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

	public String getTurno() {
		return turno;
	}

	public void setTurno(String turno) {
		this.turno = turno;
	}
}
