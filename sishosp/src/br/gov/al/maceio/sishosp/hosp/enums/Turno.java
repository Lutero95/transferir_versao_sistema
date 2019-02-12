package br.gov.al.maceio.sishosp.hosp.enums;

public enum Turno {

    MANHA("M"),
    TARDE("T"),
    AMBOS("A");

    private String sigla;

    Turno(String sigla) {
        this.sigla = sigla;
    }

    public String getSigla() {
        return sigla;
    }
}
