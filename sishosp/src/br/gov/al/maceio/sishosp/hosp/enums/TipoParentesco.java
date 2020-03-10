package br.gov.al.maceio.sishosp.hosp.enums;

public enum TipoParentesco {

    MAE("M"),
    PAI("P"),
	RESPONSAVEL("R"),
	OUTROS("O");

    private String sigla;

    TipoParentesco(String sigla) {
        this.sigla = sigla;
    }

    public String getSigla() {
        return sigla;
    }
}
