package br.gov.al.maceio.sishosp.hosp.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Pts implements Serializable {

    private Integer id;
    private Date data;
    private String diagnosticoFuncional;
    private String necessidadesIhDesejos;
    private Timestamp dataHoraOperacao;

    // HERDADOS
    private GrupoBean grupo;
    private ProgramaBean programa;
    private PacienteBean paciente;
    private GerenciarPacienteBean gerenciarPaciente;
    private PtsArea ptsArea;
    private List<PtsArea> listaPtsArea;

    public Pts() {
        grupo = new GrupoBean();
        programa = new ProgramaBean();
        paciente = new PacienteBean();
        gerenciarPaciente = new GerenciarPacienteBean();
        ptsArea = new PtsArea();
        listaPtsArea = new ArrayList<>();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getDiagnosticoFuncional() {
        return diagnosticoFuncional;
    }

    public void setDiagnosticoFuncional(String diagnosticoFuncional) {
        this.diagnosticoFuncional = diagnosticoFuncional;
    }

    public String getNecessidadesIhDesejos() {
        return necessidadesIhDesejos;
    }

    public void setNecessidadesIhDesejos(String necessidadesIhDesejos) {
        this.necessidadesIhDesejos = necessidadesIhDesejos;
    }

    public GerenciarPacienteBean getGerenciarPaciente() {
        return gerenciarPaciente;
    }

    public void setGerenciarPaciente(GerenciarPacienteBean gerenciarPaciente) {
        this.gerenciarPaciente = gerenciarPaciente;
    }

    public Timestamp getDataHoraOperacao() {
        return dataHoraOperacao;
    }

    public void setDataHoraOperacao(Timestamp dataHoraOperacao) {
        this.dataHoraOperacao = dataHoraOperacao;
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

}
