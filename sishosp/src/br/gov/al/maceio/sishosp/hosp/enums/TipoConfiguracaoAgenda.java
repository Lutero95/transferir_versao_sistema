package br.gov.al.maceio.sishosp.hosp.enums;

public enum TipoConfiguracaoAgenda {

    GERAL("G"),
    ESPECIFICA("E");

    private String sigla;

    TipoConfiguracaoAgenda(String sigla) {
        this.sigla = sigla;
    }

    public String getSigla() {
        return sigla;
    }
}
