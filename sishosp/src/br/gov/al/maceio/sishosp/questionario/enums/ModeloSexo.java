package br.gov.al.maceio.sishosp.questionario.enums;

public enum ModeloSexo {

    MASCULINO("M"),
    FEMININO("F");

    private String sigla;

    ModeloSexo(String sigla) {
        this.sigla = sigla;
    }

    public String getSigla() {
        return sigla;
    }
}
