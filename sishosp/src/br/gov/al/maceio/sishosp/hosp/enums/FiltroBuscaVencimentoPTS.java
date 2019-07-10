package br.gov.al.maceio.sishosp.hosp.enums;

public enum FiltroBuscaVencimentoPTS {

    TODOS("T"),
    VINGENTES("VI"),
    VENCIDOS("VE");

    private String sigla;

    FiltroBuscaVencimentoPTS(String sigla) {
        this.sigla = sigla;
    }

    public String getSigla() {
        return sigla;
    }
}