package br.gov.al.maceio.sishosp.administrativo.enums;

public enum MotivoAfastamento {

    FERIAS("FE"),
    LICENCA_MEDICA("LM"),
    FALTA("FA"),
    DESLIGAMENTO("DE");

    private String sigla;

    MotivoAfastamento(String sigla) {
        this.sigla = sigla;
    }

    public String getSigla() {
        return sigla;
    }
}
