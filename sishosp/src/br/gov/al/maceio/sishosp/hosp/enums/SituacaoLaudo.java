package br.gov.al.maceio.sishosp.hosp.enums;

public enum SituacaoLaudo {

    AUTORIZADO("A"),
    PENDENTE("P");

    private String sigla;

    SituacaoLaudo(String sigla) {
        this.sigla = sigla;
    }

    public String getSigla() {
        return sigla;
    }
}
