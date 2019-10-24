package br.gov.al.maceio.sishosp.hosp.model.dto;

import br.gov.al.maceio.sishosp.hosp.model.EspecialidadeBean;

public class ConfiguracaoAgendaEquipeEspecialidadeDTO {

    private EspecialidadeBean especialidade;
    private Integer quantidade;

    public ConfiguracaoAgendaEquipeEspecialidadeDTO() {
        especialidade = new EspecialidadeBean();
    }

    public EspecialidadeBean getEspecialidade() {
        return especialidade;
    }

    public void setEspecialidade(EspecialidadeBean especialidade) {
        this.especialidade = especialidade;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }
}
