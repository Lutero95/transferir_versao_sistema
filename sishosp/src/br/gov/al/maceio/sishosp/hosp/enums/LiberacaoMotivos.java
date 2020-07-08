package br.gov.al.maceio.sishosp.hosp.enums;

public enum LiberacaoMotivos {

    ENCAIXE("ENCAIXE"),
    AVALIACAO("LIBERACAO PARA AGENDAMENTO EM EQUIPE DE UM PACIENTE QUE NAO FAZ PARTE DA MESMA");

    private String sigla;

    LiberacaoMotivos(String sigla) {
        this.sigla = sigla;
    }

    public String getSigla() {
        return sigla;
    }
}
