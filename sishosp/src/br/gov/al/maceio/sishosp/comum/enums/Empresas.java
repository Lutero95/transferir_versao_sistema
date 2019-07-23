package br.gov.al.maceio.sishosp.comum.enums;

public enum Empresas {

    LOCAL(1),
    PESTALOZZI(2),
    ADEFAL(3),
    AAPPEMACEIO(4),
    APAEMACEIO(5),
    APAEPALMEIRA(6),
    APAEMARAGOGI(7);

    private Integer sigla;

    Empresas(Integer sigla) {
        this.sigla = sigla;
    }

    public Integer getSigla() {
        return sigla;
    }
}
