package br.gov.al.maceio.sishosp.hosp.enums;

public enum TipoGravacaoHistoricoPaciente {

	ALTERACAO("A"),
    INSERCAO("I"),
    INSERCAO_VIA_TRANSFERENCIA("IT"),
    DESLIGAMENTO("D"),
	RENOVACAO("R"),
	TRANSFERENCIA("T");

    private String sigla;

    TipoGravacaoHistoricoPaciente(String sigla) {
        this.sigla = sigla;
    }

    public String getSigla() {
        return sigla;
    }
}
