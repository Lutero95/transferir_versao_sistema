package br.gov.al.maceio.sishosp.financeiro.model;

import java.io.Serializable;

public class DespesaBean implements Serializable {

    private Integer id;
    private String descricao;
    


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

   

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return descricao;
    }
}