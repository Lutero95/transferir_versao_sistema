package br.gov.al.maceio.sishosp.hosp.enums;

public enum TipoDataAgenda {

    DATA_UNICA("U"),
    INTERVALO_DE_DATAS("I"),
    A_PARTIR_DA_DATA("A");

    private String sigla;

    TipoDataAgenda(String sigla) {
        this.sigla = sigla;
    }

    public String getSigla() {
        return sigla;
    }
}
