package br.gov.al.maceio.sishosp.hosp.model;

import java.io.Serializable;
import java.sql.Time;
import java.util.Date;

public class ParametroBean implements Serializable {

    private Integer motivoDesligamento;
    private String opcaoAtendimento;
    private Integer quantidadeSimultaneaProfissional;
    private Integer quantidadeSimultaneaEquipe;
    private Date horarioInicial;
    private Date horarioFinal;
    private Integer intervalo;
    private TipoAtendimentoBean tipoAtendimento;
    private ProgramaBean programaOrteseIhProtese;
    private GrupoBean grupoOrteseIhProtese;

    public ParametroBean() {
        tipoAtendimento = new TipoAtendimentoBean();
        programaOrteseIhProtese = new ProgramaBean();
        grupoOrteseIhProtese = new GrupoBean();
    }

    public Integer getMotivoDesligamento() {
        return motivoDesligamento;
    }

    public void setMotivoDesligamento(Integer motivoDesligamento) {
        this.motivoDesligamento = motivoDesligamento;
    }

    public String getOpcaoAtendimento() {
        return opcaoAtendimento;
    }

    public void setOpcaoAtendimento(String opcaoAtendimento) {
        this.opcaoAtendimento = opcaoAtendimento;
    }

    public Integer getQuantidadeSimultaneaProfissional() {
        return quantidadeSimultaneaProfissional;
    }

    public void setQuantidadeSimultaneaProfissional(Integer quantidadeSimultaneaProfissional) {
        this.quantidadeSimultaneaProfissional = quantidadeSimultaneaProfissional;
    }

    public Integer getQuantidadeSimultaneaEquipe() {
        return quantidadeSimultaneaEquipe;
    }

    public void setQuantidadeSimultaneaEquipe(Integer quantidadeSimultaneaEquipe) {
        this.quantidadeSimultaneaEquipe = quantidadeSimultaneaEquipe;
    }

    public Date getHorarioInicial() {
        return horarioInicial;
    }

    public void setHorarioInicial(Date horarioInicial) {
        this.horarioInicial = horarioInicial;
    }

    public Date getHorarioFinal() {
        return horarioFinal;
    }

    public void setHorarioFinal(Date horarioFinal) {
        this.horarioFinal = horarioFinal;
    }

    public Integer getIntervalo() {
        return intervalo;
    }

    public void setIntervalo(Integer intervalo) {
        this.intervalo = intervalo;
    }

    public TipoAtendimentoBean getTipoAtendimento() {
        return tipoAtendimento;
    }

    public void setTipoAtendimento(TipoAtendimentoBean tipoAtendimento) {
        this.tipoAtendimento = tipoAtendimento;
    }

    public ProgramaBean getProgramaOrteseIhProtese() {
        return programaOrteseIhProtese;
    }

    public void setProgramaOrteseIhProtese(ProgramaBean programaOrteseIhProtese) {
        this.programaOrteseIhProtese = programaOrteseIhProtese;
    }

    public GrupoBean getGrupoOrteseIhProtese() {
        return grupoOrteseIhProtese;
    }

    public void setGrupoOrteseIhProtese(GrupoBean grupoOrteseIhProtese) {
        this.grupoOrteseIhProtese = grupoOrteseIhProtese;
    }
}
