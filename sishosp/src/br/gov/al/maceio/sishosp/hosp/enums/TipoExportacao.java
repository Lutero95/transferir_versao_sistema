package br.gov.al.maceio.sishosp.hosp.enums;

public enum TipoExportacao {

    INDIVIDUALIZADO("I"),
    CONSOLIDADO("CS"),
    COMPLETO("CP");

    private String sigla;

    TipoExportacao(String sigla) {
        this.sigla = sigla;
    }

    public String getSigla() {
        return sigla;
    }
}
