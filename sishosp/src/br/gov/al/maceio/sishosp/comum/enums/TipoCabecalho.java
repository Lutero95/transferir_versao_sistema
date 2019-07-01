package br.gov.al.maceio.sishosp.comum.enums;

public enum TipoCabecalho {

    INCLUSAO(1),
    ALTERACAO(2);

    private Integer sigla;

    TipoCabecalho(Integer sigla) {
        this.sigla = sigla;
    }

    public Integer getSigla() {
        return sigla;
    }
}
