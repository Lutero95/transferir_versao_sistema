package br.gov.al.maceio.sishosp.hosp.enums;

public enum TipoProcedimentoPrograma {

    PROCEDIMENTO_PADRAO("P"),
    PROCEDIMENTO_OCUPACAO("O"),
    PROCEDIMENTO_IDADE("I"),
    PROCEDIMENTO_PERMITIDO("PP");

    private String sigla;

    TipoProcedimentoPrograma(String sigla) {
        this.sigla = sigla;
    }

    public String getSigla() {
        return sigla;
    }
}
