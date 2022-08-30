package br.gov.al.maceio.sishosp.hosp.enums;

public enum TipoRelatorioLaudoEmBranco {
    ANTIGO("A"),
    MODERNO("M");

    private String sigla;

    TipoRelatorioLaudoEmBranco(String sigla) {
        this.sigla = sigla;
    }

    public String getSigla() {
        return sigla;
    }
}
