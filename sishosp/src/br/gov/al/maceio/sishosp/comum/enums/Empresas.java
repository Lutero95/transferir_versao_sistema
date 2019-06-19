package br.gov.al.maceio.sishosp.comum.enums;

public enum Empresas {

    LOCAL(1),
    PESTALOZZI(2),
    ADEFAL(3),
    AAPE(4),
    APAE(5);

    private Integer sigla;

    Empresas(Integer sigla) {
        this.sigla = sigla;
    }

    public Integer getSigla() {
        return sigla;
    }
}
