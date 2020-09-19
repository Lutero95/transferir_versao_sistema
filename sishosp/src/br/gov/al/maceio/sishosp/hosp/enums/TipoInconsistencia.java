package br.gov.al.maceio.sishosp.hosp.enums;

public enum TipoInconsistencia {

	EVOLUCAO_PROGRAMA_GRUPO("EVOLUCAO PROGRAMA GRUPO");

    private String sigla;

    TipoInconsistencia(String sigla) {
        this.sigla = sigla;
    }

    public String getSigla() {
        return sigla;
    }
}
