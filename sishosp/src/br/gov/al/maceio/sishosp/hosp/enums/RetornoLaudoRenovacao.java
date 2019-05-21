package br.gov.al.maceio.sishosp.hosp.enums;

public enum RetornoLaudoRenovacao {

    DATA_ATUAL_MAIOR_QUE_NOVA_DATA("DM"),
    DATA_OK("OK"),
    MAIS_DE_UM_MES("MM");

    private String sigla;

    RetornoLaudoRenovacao(String sigla) {
        this.sigla = sigla;
    }

    public String getSigla() {
        return sigla;
    }
}
