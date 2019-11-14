package br.gov.al.maceio.sishosp.hosp.model.dto;

import br.gov.al.maceio.sishosp.hosp.model.GrupoBean;
import br.gov.al.maceio.sishosp.hosp.model.ProgramaBean;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class BuscaSessaoDTO implements Serializable {

    private ProgramaBean programaBean;
    private GrupoBean grupoBean;
    private Date periodoInicial;
    private Date periodoFinal;
    private String tela;

    public BuscaSessaoDTO() {
        programaBean = new ProgramaBean();
        grupoBean = new GrupoBean();
    }

    public BuscaSessaoDTO(ProgramaBean programaBean, GrupoBean grupoBean, Date periodoInicial, Date periodoFinal, String tela) {
        this.programaBean = programaBean;
        this.grupoBean = grupoBean;
        this.periodoInicial = periodoInicial;
        this.periodoFinal = periodoFinal;
        this.tela = tela;
    }

    public ProgramaBean getProgramaBean() {
        return programaBean;
    }

    public void setProgramaBean(ProgramaBean programaBean) {
        this.programaBean = programaBean;
    }

    public GrupoBean getGrupoBean() {
        return grupoBean;
    }

    public void setGrupoBean(GrupoBean grupoBean) {
        this.grupoBean = grupoBean;
    }

    public Date getPeriodoInicial() {
        return periodoInicial;
    }

    public void setPeriodoInicial(Date periodoInicial) {
        this.periodoInicial = periodoInicial;
    }

    public Date getPeriodoFinal() {
        return periodoFinal;
    }

    public void setPeriodoFinal(Date periodoFinal) {
        this.periodoFinal = periodoFinal;
    }

    public String getTela() {
        return tela;
    }

    public void setTela(String tela) {
        this.tela = tela;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BuscaSessaoDTO that = (BuscaSessaoDTO) o;
        return Objects.equals(programaBean, that.programaBean) &&
                Objects.equals(grupoBean, that.grupoBean) &&
                Objects.equals(periodoInicial, that.periodoInicial) &&
                Objects.equals(periodoFinal, that.periodoFinal) &&
                Objects.equals(tela, that.tela);
    }

    @Override
    public int hashCode() {
        return Objects.hash(programaBean, grupoBean, periodoInicial, periodoFinal, tela);
    }

    @Override
    public String toString() {
        return "BuscaSessaoDTO{" +
                "programaBean=" + programaBean +
                ", grupoBean=" + grupoBean +
                ", periodoInicial=" + periodoInicial +
                ", periodoFinal=" + periodoFinal +
                ", tela='" + tela + '\'' +
                '}';
    }
}
