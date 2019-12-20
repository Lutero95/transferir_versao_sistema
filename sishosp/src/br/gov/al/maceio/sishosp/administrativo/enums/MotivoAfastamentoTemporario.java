package br.gov.al.maceio.sishosp.administrativo.enums;

public enum MotivoAfastamentoTemporario {

    FERIAS("FE"),
    LICENCA_MEDICA("LM");

    private String sigla;

    MotivoAfastamentoTemporario(String sigla) {
        this.sigla = sigla;
    }

    public String getSigla() {
        return sigla;
    }
}
