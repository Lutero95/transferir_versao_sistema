package br.gov.al.maceio.sishosp.hosp.enums;

public enum LiberacaoRotinas {

    INSERCAO("INSERCAO DE PACIENTES"),
    ALTERACAO("ALTERACAO DE PACIENTES");

    private String sigla;

    LiberacaoRotinas(String sigla) {
        this.sigla = sigla;
    }

    public String getSigla() {
        return sigla;
    }
}
