package br.gov.al.maceio.sishosp.hosp.enums;

public enum ValidacaoSenha {

    LIBERACAO("L"),
    LIBERACAO_PACIENTES_SEM_PERFIL("S"),
    LIBERACAO_ALTERAR_DATA_PTS("D"),
    ENCAIXE("E"),
    ADICIONAR_AREA_PTS("P");

    private String sigla;

    ValidacaoSenha(String sigla) {
        this.sigla = sigla;
    }

    public String getSigla() {
        return sigla;
    }
}
