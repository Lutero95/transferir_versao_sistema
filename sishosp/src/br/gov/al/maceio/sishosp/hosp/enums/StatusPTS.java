package br.gov.al.maceio.sishosp.hosp.enums;

public enum StatusPTS {

    ATIVO("A"),
    CANCELADO("C"),
    RENOVADO("R"),
    DESLIGADO("D"),
    INVALIDADO("I");

    private String sigla;

    StatusPTS(String sigla) {
        this.sigla = sigla;
    }

    public String getSigla() {
        return sigla;
    }
}
