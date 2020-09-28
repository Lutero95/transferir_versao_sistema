package br.gov.al.maceio.sishosp.gestao.enums;

public enum TipoInconsistencia {

    PACIENTE(""),
    PROCEDIMENTO(""),
    LAUDO(""),
    ;

    private String sigla;

    TipoInconsistencia(String sigla) {
        this.sigla = sigla;
    }

    public String getSigla() {
        return sigla;
    }
}
