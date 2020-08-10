package br.gov.al.maceio.sishosp.hosp.enums;

public enum TipoBuscaLaudo {

    NOME_PACIENTE("paciente"),
    PRONTUARIO_PACIENTE("prontpaciente"),
    PRONTUARIO_PACIENTE_ANTIGO("prontpacienteant"),
    MATRICULA("matpaciente"),
    CODIGO_PROCEDIMENTO("codproc");

    private String sigla;

    TipoBuscaLaudo(String sigla) {
        this.sigla = sigla;
    }

    public String getSigla() {
        return sigla;
    }
}
