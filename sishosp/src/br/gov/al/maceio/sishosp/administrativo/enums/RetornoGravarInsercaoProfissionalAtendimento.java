package br.gov.al.maceio.sishosp.administrativo.enums;

public enum RetornoGravarInsercaoProfissionalAtendimento {

    SUCESSO_GRAVACAO("SG"),
    FALHA_GRAVACAO("FG"),
    FALHA_PROFISSIONAL("FP");

    private String sigla;

    RetornoGravarInsercaoProfissionalAtendimento(String sigla) {
        this.sigla = sigla;
    }

    public String getSigla() {
        return sigla;
    }
}
