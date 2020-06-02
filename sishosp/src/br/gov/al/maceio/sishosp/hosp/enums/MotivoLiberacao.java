package br.gov.al.maceio.sishosp.hosp.enums;

public enum MotivoLiberacao {

    SEM_PERFIL_AVALIACAO("INSERÇÃO DE PACIENTE EM TERAPIA INTENSIVA"),
	ALTERAR_DATA_PTS("LIBERAÇÃO PARA ALTERAR A DATA DO PTS"),
	CANCELAR_EVOLUCAO("CANCELAMENTO DE EVOLUÇÃO");

    private String sigla;

    MotivoLiberacao(String sigla) {
        this.sigla = sigla;
    }

    public String getSigla() {
        return sigla;
    }
}
