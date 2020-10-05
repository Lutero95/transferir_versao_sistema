package br.gov.al.maceio.sishosp.hosp.log.enums;

public enum Rotina {
    ALTERACAO_PACIENTE("ALTERACAO PACIENTE"),
    EXCLUSAO_PACIENTE("EXCLUSAO PACIENTE"),
    ALTERACAO_LAUDO("ALTERACAO LAUDO");

    private String sigla;

    Rotina(String sigla) {
        this.sigla = sigla;
    }

    public String getSigla() {
        return sigla;
    }

    public void setSigla(String tipoComplexidade) {
        this.sigla = tipoComplexidade;
    }
}
