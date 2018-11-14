package br.gov.al.maceio.sishosp.hosp.enums;

public enum OpcaoConfiguracaoAgenda {

    DATA_ESPECIFICA("1"),
    DIA_DA_SEMANA("2");

    private String sigla;

    OpcaoConfiguracaoAgenda(String sigla) {
        this.sigla = sigla;
    }

    public String getSigla() {
        return sigla;
    }
}
