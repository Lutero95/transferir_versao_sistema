package br.gov.al.maceio.sishosp.hosp.enums;

public enum TipoProcedimentoAgenda {

	PROCEDIMENTO_PRINCIPAL("P"),
    PROCEDIMENTO_SECUNDARIO("S");

    private String sigla;

    TipoProcedimentoAgenda(String sigla) {
        this.sigla = sigla;
    }

    public String getSigla() {
        return sigla;
    }
}
