package br.gov.al.maceio.sishosp.hosp.model;

import java.io.Serializable;

public class CboBean implements Serializable {

    private Integer codCbo;
    private String descCbo;
    private String codigo;

    public CboBean() {
    }


    public CboBean(Integer codCbo, String descCbo, String codigo) {
        this.codCbo = codCbo;
        this.descCbo = descCbo;
        this.codigo = codigo;
    }

    public Integer getCodCbo() {
        return codCbo;
    }

    public void setCodCbo(Integer codCbo) {
        this.codCbo = codCbo;
    }

    public String getDescCbo() {
        return descCbo;
    }

    public void setDescCbo(String descCbo) {
        this.descCbo = descCbo;
    }


    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

	

}
