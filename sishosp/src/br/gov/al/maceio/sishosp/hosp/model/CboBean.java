package br.gov.al.maceio.sishosp.hosp.model;

import java.io.Serializable;

public class CboBean implements Serializable {

    private Integer codCbo;
    private String descCbo;
    private String codigo;

    public CboBean() {
    }

    public CboBean(Integer codCbo, String descCbo, Integer codUnidade) {
        this.codCbo = codCbo;
        this.descCbo = descCbo;
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
