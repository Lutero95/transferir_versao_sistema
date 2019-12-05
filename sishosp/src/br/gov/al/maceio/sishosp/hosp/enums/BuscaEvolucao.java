package br.gov.al.maceio.sishosp.hosp.enums;

public enum BuscaEvolucao {

    TODOS("T"),
    COM_EVOLUCAO("CE"),
    SEM_EVOLUCAO("SE");

    private String sigla;

    BuscaEvolucao(String sigla) {
        this.sigla = sigla;
    }

    public String getSigla() {
        return sigla;
    }
}
