package br.gov.al.maceio.sishosp.hosp.model;

import java.io.Serializable;
import java.util.Date;

public class PtsBean implements Serializable {

    // HERDADOS
    private Integer id;
    private GrupoBean grupo;
    private ProgramaBean programa;
    private PacienteBean paciente;
    private Date data;
    private String diagnosticoFuncional;
    private String necessidadesIhDesejos;

    public PtsBean() {
        grupo = new GrupoBean();
        programa = new ProgramaBean();
        paciente = new PacienteBean();
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

}
