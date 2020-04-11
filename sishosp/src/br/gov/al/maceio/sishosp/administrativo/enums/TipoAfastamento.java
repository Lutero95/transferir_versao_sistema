package br.gov.al.maceio.sishosp.administrativo.enums;

public enum TipoAfastamento {

    TEMPOR("T");

    private String sigla;

    TipoAfastamento(String sigla) {
        this.sigla = sigla;
    }

    public String getSigla() {
        return sigla;
    }
}
