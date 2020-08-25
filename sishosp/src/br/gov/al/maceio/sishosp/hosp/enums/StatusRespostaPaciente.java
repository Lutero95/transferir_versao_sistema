package br.gov.al.maceio.sishosp.hosp.enums;

public enum StatusRespostaPaciente {

    TODOS("T"),
    RESPONDIDO("R"),
    NAO_RESPONDIDO("NR");

    private String sigla;

    StatusRespostaPaciente(String sigla) {
        this.sigla = sigla;
    }

    public String getSigla() {
        return sigla;
    }
}
