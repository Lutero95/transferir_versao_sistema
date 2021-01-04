package br.gov.al.maceio.sishosp.hosp.model;

import java.io.Serializable;

public class CboBean implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer codCbo;
    private String descCbo;
    private String codigo;
    private ConselhoBean conselho;

    public CboBean() {
    	this.conselho = new ConselhoBean();
    }


    public CboBean(Integer codCbo, String descCbo, String codigo) {
        this.codCbo = codCbo;
        this.descCbo = descCbo;
        this.codigo = codigo;
        this.conselho = new ConselhoBean();
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


	public ConselhoBean getConselho() {
		return conselho;
	}


	public void setConselho(ConselhoBean conselho) {
		this.conselho = conselho;
	}
}
