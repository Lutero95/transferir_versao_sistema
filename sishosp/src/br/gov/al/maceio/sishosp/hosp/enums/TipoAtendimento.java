package br.gov.al.maceio.sishosp.hosp.enums;

public enum TipoAtendimento {

    PROFISSIONAL("P"),
    EQUIPE("E"),
	TODOS("T");

    private String sigla;

    TipoAtendimento(String sigla) {
        this.sigla = sigla;
    }

    public String getSigla() {
        return sigla;
    }
}
