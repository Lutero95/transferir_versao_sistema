package br.gov.al.maceio.sishosp.hosp.enums;

public enum StatusMovimentacaoOrteseProtese {

    INSERCAO_DE_SOLICITACAO("IS"),
    ENCAMINHAMENTO_FORNECEDOR("EF"),
    ENCAMINHAMENTO_CANCELADO("EC"),
    MEDICAO_EFETUADA("ME"),
    MEDICAO_CANCELADA("MC"),
    EQUIPAMENTO_RECEBIDO("ER"),
    EQUIPAMENTO_ENTREGUE("EE");

    private String sigla;

    StatusMovimentacaoOrteseProtese(String sigla) {
        this.sigla = sigla;
    }

    public String getSigla() {
        return sigla;
    }
}
