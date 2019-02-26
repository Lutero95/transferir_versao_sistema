package br.gov.al.maceio.sishosp.hosp.model;

import java.io.Serializable;

public class ParametroBean implements Serializable {

    private Integer motivoDesligamento;
    private String opcaoAtendimento;
    private Integer quantidadeSimultaneaProfissional;
    private Integer quantidadeSimultaneaEquipe;

    public ParametroBean() {
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
}
