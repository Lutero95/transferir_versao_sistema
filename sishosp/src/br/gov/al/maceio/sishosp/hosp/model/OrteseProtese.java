package br.gov.al.maceio.sishosp.hosp.model;

import br.gov.al.maceio.sishosp.comum.util.DataUtil;

import java.io.Serializable;
import java.util.Date;

public class OrteseProtese implements Serializable {

    private Integer id;
    private Integer idEncaminhamento;
    private String statusMovimentacao;
    private String statusPadrao;
    private String notaFiscal;
    private String especificacao;
    private Date dataEncaminhamento;
    private Date dataCancelamento;
    private String medicao;
    private Date dataMedicao;
    private UnidadeBean unidade;

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
        dataEncaminhamento = DataUtil.retornarDataAtual();
        unidade = new UnidadeBean();
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

    public String getEspecificacao() {
        return especificacao;
    }

    public void setEspecificacao(String especificacao) {
        this.especificacao = especificacao;
    }

    public Date getDataEncaminhamento() {
        return dataEncaminhamento;
    }

    public void setDataEncaminhamento(Date dataEncaminhamento) {
        this.dataEncaminhamento = dataEncaminhamento;
    }

    public Date getDataCancelamento() {
        return dataCancelamento;
    }

    public void setDataCancelamento(Date dataCancelamento) {
        this.dataCancelamento = dataCancelamento;
    }

    public Integer getIdEncaminhamento() {
        return idEncaminhamento;
    }

    public void setIdEncaminhamento(Integer idEncaminhamento) {
        this.idEncaminhamento = idEncaminhamento;
    }

    public String getMedicao() {
        return medicao;
    }

    public void setMedicao(String medicao) {
        this.medicao = medicao;
    }

    public Date getDataMedicao() {
        return dataMedicao;
    }

    public void setDataMedicao(Date dataMedicao) {
        this.dataMedicao = dataMedicao;
    }
}
