package br.gov.al.maceio.sishosp.hosp.model;

import java.io.Serializable;

public class CboBean implements Serializable {

    private Integer codCbo;
    private String descCbo;
    private Integer codEmpresa;
    private String codigo;

    public CboBean() {
    }

    public CboBean(Integer codCbo, String descCbo, Integer codEmpresa) {
        this.codCbo = codCbo;
        this.descCbo = descCbo;
        this.codEmpresa = codEmpresa;
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

    public Integer getCodEmpresa() {
        return codEmpresa;
    }

    public void setCodEmpresa(Integer codEmpresa) {
        this.codEmpresa = codEmpresa;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

}
