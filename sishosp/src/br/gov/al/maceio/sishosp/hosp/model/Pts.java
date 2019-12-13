package br.gov.al.maceio.sishosp.hosp.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Pts implements Serializable {

    private Integer id;
    private Date data;
    private String incapacidadesFuncionais;
    private String capacidadesFuncionais;
    private String objetivosFamiliarPaciente;
    private String objetivosGeraisMultidisciplinar;
    private String objetivosGeraisCurtoPrazo;
    private String objetivosGeraisMedioPrazo;
    private String objetivosGeraisLongoPrazo;
    private String analiseDosResultadosDosObjetivosGerias;
    private String novasEstrategiasDeTratamento;
    private String condutaAlta;
    private Timestamp dataHoraOperacao;
    private Date dataVencimento;
    private Date dataDesligamento;
    private String status;
    private String statusPorExtenso;

    // HERDADOS
    private GrupoBean grupo;
    private ProgramaBean programa;
    private PacienteBean paciente;
    private GerenciarPacienteBean gerenciarPaciente;
    private PtsArea ptsArea;
    private List<PtsArea> listaPtsArea;
    private InsercaoPacienteBean insercao;
    private UnidadeBean unidade;

    public Pts() {
        grupo = new GrupoBean();
        programa = new ProgramaBean();
        paciente = new PacienteBean();
        gerenciarPaciente = new GerenciarPacienteBean();
        ptsArea = new PtsArea();
        listaPtsArea = new ArrayList<>();
        insercao = new InsercaoPacienteBean();
        unidade = new UnidadeBean();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getIncapacidadesFuncionais() {
        return incapacidadesFuncionais;
    }

    public void setIncapacidadesFuncionais(String incapacidadesFuncionais) {
        this.incapacidadesFuncionais = incapacidadesFuncionais;
    }

    public String getCapacidadesFuncionais() {
        return capacidadesFuncionais;
    }

    public void setCapacidadesFuncionais(String capacidadesFuncionais) {
        this.capacidadesFuncionais = capacidadesFuncionais;
    }

    public String getObjetivosFamiliarPaciente() {
        return objetivosFamiliarPaciente;
    }

    public void setObjetivosFamiliarPaciente(String objetivosFamiliarPaciente) {
        this.objetivosFamiliarPaciente = objetivosFamiliarPaciente;
    }

    public String getObjetivosGeraisMultidisciplinar() {
        return objetivosGeraisMultidisciplinar;
    }

    public void setObjetivosGeraisMultidisciplinar(String objetivosGeraisMultidisciplinar) {
        this.objetivosGeraisMultidisciplinar = objetivosGeraisMultidisciplinar;
    }

    public String getObjetivosGeraisCurtoPrazo() {
        return objetivosGeraisCurtoPrazo;
    }

    public void setObjetivosGeraisCurtoPrazo(String objetivosGeraisCurtoPrazo) {
        this.objetivosGeraisCurtoPrazo = objetivosGeraisCurtoPrazo;
    }

    public String getObjetivosGeraisMedioPrazo() {
        return objetivosGeraisMedioPrazo;
    }

    public void setObjetivosGeraisMedioPrazo(String objetivosGeraisMedioPrazo) {
        this.objetivosGeraisMedioPrazo = objetivosGeraisMedioPrazo;
    }

    public String getObjetivosGeraisLongoPrazo() {
        return objetivosGeraisLongoPrazo;
    }

    public void setObjetivosGeraisLongoPrazo(String objetivosGeraisLongoPrazo) {
        this.objetivosGeraisLongoPrazo = objetivosGeraisLongoPrazo;
    }

    public String getAnaliseDosResultadosDosObjetivosGerias() {
        return analiseDosResultadosDosObjetivosGerias;
    }

    public void setAnaliseDosResultadosDosObjetivosGerias(String analiseDosResultadosDosObjetivosGerias) {
        this.analiseDosResultadosDosObjetivosGerias = analiseDosResultadosDosObjetivosGerias;
    }

    public String getNovasEstrategiasDeTratamento() {
        return novasEstrategiasDeTratamento;
    }

    public void setNovasEstrategiasDeTratamento(String novasEstrategiasDeTratamento) {
        this.novasEstrategiasDeTratamento = novasEstrategiasDeTratamento;
    }

    public String getCondutaAlta() {
        return condutaAlta;
    }

    public void setCondutaAlta(String condutaAlta) {
        this.condutaAlta = condutaAlta;
    }

    public Timestamp getDataHoraOperacao() {
        return dataHoraOperacao;
    }

    public void setDataHoraOperacao(Timestamp dataHoraOperacao) {
        this.dataHoraOperacao = dataHoraOperacao;
    }

    public Date getDataVencimento() {
        return dataVencimento;
    }

    public void setDataVencimento(Date dataVencimento) {
        this.dataVencimento = dataVencimento;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusPorExtenso() {
        return statusPorExtenso;
    }

    public void setStatusPorExtenso(String statusPorExtenso) {
        this.statusPorExtenso = statusPorExtenso;
    }

    public GrupoBean getGrupo() {
        return grupo;
    }

    public void setGrupo(GrupoBean grupo) {
        this.grupo = grupo;
    }

    public ProgramaBean getPrograma() {
        return programa;
    }

    public void setPrograma(ProgramaBean programa) {
        this.programa = programa;
    }

    public PacienteBean getPaciente() {
        return paciente;
    }

    public void setPaciente(PacienteBean paciente) {
        this.paciente = paciente;
    }

    public GerenciarPacienteBean getGerenciarPaciente() {
        return gerenciarPaciente;
    }

    public void setGerenciarPaciente(GerenciarPacienteBean gerenciarPaciente) {
        this.gerenciarPaciente = gerenciarPaciente;
    }

    public PtsArea getPtsArea() {
        return ptsArea;
    }

    public void setPtsArea(PtsArea ptsArea) {
        this.ptsArea = ptsArea;
    }

    public List<PtsArea> getListaPtsArea() {
        return listaPtsArea;
    }

    public void setListaPtsArea(List<PtsArea> listaPtsArea) {
        this.listaPtsArea = listaPtsArea;
    }

    public InsercaoPacienteBean getInsercao() {
        return insercao;
    }

    public void setInsercao(InsercaoPacienteBean insercao) {
        this.insercao = insercao;
    }

    public Date getDataDesligamento() {
        return dataDesligamento;
    }

    public void setDataDesligamento(Date dataDesligamento) {
        this.dataDesligamento = dataDesligamento;
    }
}
