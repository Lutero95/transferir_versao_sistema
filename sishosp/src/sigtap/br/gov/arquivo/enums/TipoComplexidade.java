package sigtap.br.gov.arquivo.enums;

public enum TipoComplexidade {
    NAO_SE_APLICA("0"),
    ATENCAO_BASICA_COMPLEXIDADE("1"),
    MEDIA_COMPLEXIDADE("2"),
    ALTA_COMPLEXIDADE("3");

    private String tipoComplexidade;

    TipoComplexidade(String tipoComplexidade) {
        this.tipoComplexidade = tipoComplexidade;
    }

    public String getTipoComplexidade() {
        return tipoComplexidade;
    }

    public void setTipoComplexidade(String tipoComplexidade) {
        this.tipoComplexidade = tipoComplexidade;
    }
}
