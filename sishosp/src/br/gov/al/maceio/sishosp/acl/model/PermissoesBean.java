package br.gov.al.maceio.sishosp.acl.model;

import java.io.Serializable;

public class PermissoesBean implements Serializable{

    private Boolean protocolar;
    private Boolean tramitar;
    private Boolean alterarProtocolo;
    private Boolean alterarNatureza;
    private Boolean incluirParecer;
    private Boolean despachar;
    private Boolean assDigital;
    private Boolean encaminharProtocolo;
    private Boolean docDigital;

    public Boolean getProtocolar() {
        return protocolar;
    }

    public void setProtocolar(Boolean protocolar) {
        this.protocolar = protocolar;
    }

    public Boolean getTramitar() {
        return tramitar;
    }

    public void setTramitar(Boolean tramitar) {
        this.tramitar = tramitar;
    }

    public Boolean getAlterarProtocolo() {
        return alterarProtocolo;
    }

    public void setAlterarProtocolo(Boolean alterarProtocolo) {
        this.alterarProtocolo = alterarProtocolo;
    }

    public Boolean getAlterarNatureza() {
        return alterarNatureza;
    }

    public void setAlterarNatureza(Boolean alterarNatureza) {
        this.alterarNatureza = alterarNatureza;
    }

    public Boolean getIncluirParecer() {
        return incluirParecer;
    }

    public void setIncluirParecer(Boolean incluirParecer) {
        this.incluirParecer = incluirParecer;
    }

    public Boolean getDespachar() {
        return despachar;
    }

    public void setDespachar(Boolean despachar) {
        this.despachar = despachar;
    }

    public Boolean getAssDigital() {
        return assDigital;
    }

    public void setAssDigital(Boolean assDigital) {
        this.assDigital = assDigital;
    }

    public Boolean getEncaminharProtocolo() {
        return encaminharProtocolo;
    }

    public void setEncaminharProtocolo(Boolean encaminharProtocolo) {
        this.encaminharProtocolo = encaminharProtocolo;
    }

    public Boolean getDocDigital() {
        return docDigital;
    }

    public void setDocDigital(Boolean docDigital) {
        this.docDigital = docDigital;
    }
}
