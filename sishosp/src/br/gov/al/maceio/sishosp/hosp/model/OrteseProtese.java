package br.gov.al.maceio.sishosp.hosp.model;

import java.io.Serializable;

public class OrteseProtese implements Serializable {

    private Integer id;
    private String statusMovimentacao;
    private String statusPadrao;
    private String notaFiscal;

    //HERDADOS
    private ProgramaBean programa;
    private GrupoBean grupo;
    private EquipamentoBean equipamento;
    private LaudoBean laudo;
    private FornecedorBean fornecedor;

    public OrteseProtese() {
        programa = new ProgramaBean();
        grupo = new GrupoBean();
        equipamento = new EquipamentoBean();
        laudo = new LaudoBean();
        fornecedor = new FornecedorBean();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ProgramaBean getPrograma() {
        return programa;
    }

    public void setPrograma(ProgramaBean programa) {
        this.programa = programa;
    }

    public GrupoBean getGrupo() {
        return grupo;
    }

    public void setGrupo(GrupoBean grupo) {
        this.grupo = grupo;
    }

    public EquipamentoBean getEquipamento() {
        return equipamento;
    }

    public void setEquipamento(EquipamentoBean equipamento) {
        this.equipamento = equipamento;
    }

    public LaudoBean getLaudo() {
        return laudo;
    }

    public void setLaudo(LaudoBean laudo) {
        this.laudo = laudo;
    }

    public String getStatusMovimentacao() {
        return statusMovimentacao;
    }

    public void setStatusMovimentacao(String statusMovimentacao) {
        this.statusMovimentacao = statusMovimentacao;
    }

    public String getNotaFiscal() {
        return notaFiscal;
    }

    public void setNotaFiscal(String notaFiscal) {
        this.notaFiscal = notaFiscal;
    }

    public FornecedorBean getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(FornecedorBean fornecedor) {
        this.fornecedor = fornecedor;
    }

    public String getStatusPadrao() {
        return statusPadrao;
    }

    public void setStatusPadrao(String statusPadrao) {
        this.statusPadrao = statusPadrao;
    }
}
