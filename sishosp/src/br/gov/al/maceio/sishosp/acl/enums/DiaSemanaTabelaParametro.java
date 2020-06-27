package br.gov.al.maceio.sishosp.acl.enums;

public enum DiaSemanaTabelaParametro {

	DOMINGO("acesso_permitido_domingo"),
    SEGUNDA("acesso_permitido_segunda"),
    TERCA("acesso_permitido_terca"),
    QUARTA("acesso_permitido_quarta"),
    QUINTA("acesso_permitido_quinta"),
    SEXTA("acesso_permitido_sexta"),
    SABADO("acesso_permitido_sabado");

    private String sigla;

    DiaSemanaTabelaParametro(String sigla) {
        this.sigla = sigla;
    }

    public String getSigla() {
        return sigla;
    }
}
