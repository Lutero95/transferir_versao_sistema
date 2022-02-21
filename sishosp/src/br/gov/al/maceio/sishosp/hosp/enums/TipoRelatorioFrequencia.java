package br.gov.al.maceio.sishosp.hosp.enums;

public enum TipoRelatorioFrequencia {

    COMPLETO("C"),
    COM_DATA("D"),
    SOMENTE_ASSINATURA("A");

    private String sigla;

    TipoRelatorioFrequencia(String sigla) {
        this.sigla = sigla;
    }

    public String getSigla() {
        return sigla;
    }
}
