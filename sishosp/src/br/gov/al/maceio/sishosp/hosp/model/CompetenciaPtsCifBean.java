package br.gov.al.maceio.sishosp.hosp.model;

import java.sql.Timestamp;
import java.util.Date;

public class CompetenciaPtsCifBean {

    private Integer id;
    private String descricao;
    private Timestamp dataInicial;
    private Timestamp dataFinal;
    private Boolean ativa;

    public CompetenciaPtsCifBean(){ }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Timestamp getDataInicial() {
        return dataInicial;
    }

    public void setDataInicial(Timestamp dataInicial) {
        this.dataInicial = dataInicial;
    }

    public Timestamp getDataFinal() {
        return dataFinal;
    }

    public void setDataFinal(Timestamp dataFinal) {
        this.dataFinal = dataFinal;
    }

    public Boolean getAtiva() {
        return ativa;
    }

    public void setAtiva(Boolean ativa) {
        this.ativa = ativa;
    }
}
