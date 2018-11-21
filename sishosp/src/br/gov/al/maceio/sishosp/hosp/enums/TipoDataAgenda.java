package br.gov.al.maceio.sishosp.hosp.enums;

public enum TipoDataAgenda {

    DATA_UNICA("U"),
    INTERVALO_DE_DATAS("I");

    private String sigla;

    TipoDataAgenda(String sigla) {
        this.sigla = sigla;
    }

    public String getSigla() {
        return sigla;
    }
}
