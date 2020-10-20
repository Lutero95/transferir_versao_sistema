package br.gov.al.maceio.sishosp.hosp.enums;

public enum TipoFiltroRelatorio {

	PROGRAMA("P"),
	GRUPO("G"),
	EQUIPE("E");

    private String sigla;

    TipoFiltroRelatorio(String sigla) {
        this.sigla = sigla;
    }

    public String getSigla() {
        return sigla;
    }
}
