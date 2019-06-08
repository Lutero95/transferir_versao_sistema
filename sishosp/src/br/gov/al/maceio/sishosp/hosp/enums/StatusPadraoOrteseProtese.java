package br.gov.al.maceio.sishosp.hosp.enums;

public enum StatusPadraoOrteseProtese {

    AUTORIZADO("A"),
    PENDENTE("P");

    private String sigla;

    StatusPadraoOrteseProtese(String sigla) {
        this.sigla = sigla;
    }

    public String getSigla() {
        return sigla;
    }
}
