package br.gov.al.maceio.sishosp.hosp.enums;

public enum ValidacaoSenhaAgenda {

    LIBERACAO("L"),
    ENCAIXE("E"),
    ADICIONAR_AREA_PTS("P");

    private String sigla;

    ValidacaoSenhaAgenda(String sigla) {
        this.sigla = sigla;
    }

    public String getSigla() {
        return sigla;
    }
}
