package br.gov.al.maceio.sishosp.hosp.log.enums;

public enum Rotina {
    PACIENTE("PACIENTE"),
    LAUDO("LAUDO");

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
