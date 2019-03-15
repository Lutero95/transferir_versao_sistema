package br.gov.al.maceio.sishosp.hosp.enums;

public enum DiasDaSemana {

    DOMINGO("1"),
    SEGUNDA("2"),
    TERCA("3"),
    QUARTA("4"),
    QUINTA("5"),
    SEXTA("6"),
    SABADO("7");

    private String sigla;

    DiasDaSemana(String sigla) {
        this.sigla = sigla;
    }

    public String getSigla() {
        return sigla;
    }
}
