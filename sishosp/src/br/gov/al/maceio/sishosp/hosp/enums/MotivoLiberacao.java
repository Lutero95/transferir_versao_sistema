package br.gov.al.maceio.sishosp.hosp.enums;

public enum MotivoLiberacao {
	
    ENCAIXE("ENCAIXE", "EN"),
    AVALIACAO("INSERCAO DE PACIENTES: LIBERACAO PARA AGENDAMENTO EM EQUIPE DE UM PACIENTE QUE NAO FAZ PARTE DA MESMA", "AV"),
    SEM_PERFIL_AVALIACAO("INSERÇÃO DE PACIENTE EM TERAPIA INTENSIVA", "SPAV"),
	ALTERAR_DATA_PTS("LIBERAÇÃO PARA ALTERAR A DATA DO PTS", "ALT_DATA_PTS"),
	DUPLICIDADE_AGENDA_AVULSA("DUPLICIDADE DE AGENDA AVULSA", "DUPLI_AG_AVULSA"),
	RENOVAO_PTS_ANTES_VENCIMENTO("RENOVAÇÃO PTS ANTES DA DATA DE VENCIMENTO", "REN_PTS_ANTES_VENC"),
	CANCELAR_EVOLUCAO("CANCELAMENTO DE EVOLUÇÃO", "CANC_EVOL"),
	DUPLICIDADE_ESPECIALIDADE("DUPLICIDADE DE ESPECIALIDADE PARA O PACIENTE NA DATA DE AGENDA", "DUPLI_ESP"),
	EXCLUSAO_AGENDAMENTO_PACIENTE("EXCLUSAO AGENDAMENTO PACIENTE", "EXC_AG_PACI"),
	AGENDA_AVULSA_COM_PROCEDIMENTO("CADASTRO DE AGENDA AVULSA COM PROCEDIMENTO", "AG_AVULSA_PROC"),
	AGENDA_COM_PROCEDIMENTO("CADASTRO DE AGENDA COM PROCEDIMENTO", "AG_PROC");

    private String titulo;
    private String sigla;

    MotivoLiberacao(String titulo, String sigla) {
        this.titulo = titulo;
        this.sigla = sigla;
    }

    public String getTitulo() {
        return titulo;
    }

	public String getSigla() {
		return sigla;
	}
}
