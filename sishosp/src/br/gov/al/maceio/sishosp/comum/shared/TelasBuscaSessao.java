package br.gov.al.maceio.sishosp.comum.shared;

public enum TelasBuscaSessao {

    PTS("pts"),
    CONSULTAR_AGENDAMENTO ("consultarAgendamento");

    private String sigla;

    TelasBuscaSessao(String sigla) {
        this.sigla = sigla;
    }

    public String getSigla() {
        return sigla;
    }

}
