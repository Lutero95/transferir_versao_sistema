package br.gov.al.maceio.sishosp.hosp.enums;

public enum TipoBuscaPaciente {

	CNS("cns"),
	CPF("cpf"),
	MATRICULA("matricula"),
    NOME("nome"),
    PRONTUARIO("prontuario"),
    PRONTUARIO_ANTIGO("prontuario_antigo");

    private String sigla;

    TipoBuscaPaciente(String sigla) {
        this.sigla = sigla;
    }

    public String getSigla() {
        return sigla;
    }
}
