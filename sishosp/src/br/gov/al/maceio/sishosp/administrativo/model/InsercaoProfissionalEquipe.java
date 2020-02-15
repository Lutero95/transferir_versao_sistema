package br.gov.al.maceio.sishosp.administrativo.model;

import br.gov.al.maceio.sishosp.acl.model.FuncionarioBean;
import br.gov.al.maceio.sishosp.hosp.enums.Turno;
import br.gov.al.maceio.sishosp.hosp.model.AtendimentoBean;
import br.gov.al.maceio.sishosp.hosp.model.EquipeBean;
import br.gov.al.maceio.sishosp.hosp.model.GrupoBean;
import br.gov.al.maceio.sishosp.hosp.model.ProgramaBean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class InsercaoProfissionalEquipe implements Serializable {

    private Integer id;
    private FuncionarioBean funcionario;
    private ProgramaBean programa;
    private GrupoBean grupo;
    private EquipeBean equipe;
    private AtendimentoBean atendimentoBean;
    private String turno;
    private Date periodoInicio;
    private Date periodoFinal;
    private List<String> diasSemana;

    public InsercaoProfissionalEquipe() {
        funcionario = new FuncionarioBean();
        programa = new ProgramaBean();
        grupo = new GrupoBean();
        equipe = new EquipeBean();
        atendimentoBean = new AtendimentoBean();
        turno = Turno.AMBOS.getSigla();
        diasSemana = new ArrayList<>();
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

    public EquipeBean getEquipe() {
        return equipe;
    }

    public void setEquipe(EquipeBean equipe) {
        this.equipe = equipe;
    }

    public AtendimentoBean getAtendimentoBean() {
        return atendimentoBean;
    }

    public void setAtendimentoBean(AtendimentoBean atendimentoBean) {
        this.atendimentoBean = atendimentoBean;
    }

    public String getTurno() {
        return turno;
    }

    public void setTurno(String turno) {
        this.turno = turno;
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

    public List<String> getDiasSemana() {
        return diasSemana;
    }

    public void setDiasSemana(List<String> diasSemana) {
        this.diasSemana = diasSemana;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InsercaoProfissionalEquipe that = (InsercaoProfissionalEquipe) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(funcionario, that.funcionario) &&
                Objects.equals(programa, that.programa) &&
                Objects.equals(grupo, that.grupo) &&
                Objects.equals(equipe, that.equipe) &&
                Objects.equals(atendimentoBean, that.atendimentoBean) &&
                Objects.equals(turno, that.turno) &&
                Objects.equals(periodoInicio, that.periodoInicio) &&
                Objects.equals(periodoFinal, that.periodoFinal) &&
                Objects.equals(diasSemana, that.diasSemana);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, funcionario, programa, grupo, equipe, atendimentoBean, turno, periodoInicio, periodoFinal, diasSemana);
    }

    @Override
    public String toString() {
        return "InsercaoProfissionalEquipe{" +
                "id=" + id +
                ", funcionario=" + funcionario +
                ", programa=" + programa +
                ", grupo=" + grupo +
                ", equipe=" + equipe +
                ", atendimentoBean=" + atendimentoBean +
                ", turno='" + turno + '\'' +
                ", periodoInicio=" + periodoInicio +
                ", periodoFinal=" + periodoFinal +
                ", diasSemana=" + diasSemana +
                '}';
    }
}
