package br.gov.al.maceio.sishosp.hosp.enums;

public enum MotivoLiberacao {

    SEM_PERFIL_AVALIACAO("INSERÇÃO DE PACIENTE EM TERAPIA INTENSIVA");

    private String sigla;

    MotivoLiberacao(String sigla) {
        this.sigla = sigla;
    }

    public String getSigla() {
        return sigla;
    }
}
