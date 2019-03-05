package br.gov.al.maceio.sishosp.hosp.enums;

public enum OpcaoAtendimento {

    SOMENTE_TURNO("T"),
    SOMENTE_HORARIO("H"),
    AMBOS("A");

    private String sigla;

    OpcaoAtendimento(String sigla) {
        this.sigla = sigla;
    }

    public String getSigla() {
        return sigla;
    }
}
