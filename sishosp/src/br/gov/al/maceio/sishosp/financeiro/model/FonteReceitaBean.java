package br.gov.al.maceio.sishosp.financeiro.model;

import java.io.Serializable;

public class FonteReceitaBean implements Serializable {

    private Integer id;
    private String descricao;
    private String codFonteRec;
    
    public FonteReceitaBean() {
    }

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
    
    public String getCodFonteRec() {
        return codFonteRec;
    }

    public void setCodFonteRec(String codFonteRec) {
        this.codFonteRec = codFonteRec;
    }

   
}