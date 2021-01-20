package br.gov.al.maceio.sishosp.hosp.enums;

public enum TipoRelatorio {

	ANALITICO("A"),
	SINTETICO("S"),
	QUANTIDADE_ATENDIMENTOS("Q");

    private String sigla;

    TipoRelatorio(String sigla) {
        this.sigla = sigla;
    }

    public String getSigla() {
        return sigla;
    }
}
