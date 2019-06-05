package br.gov.al.maceio.sishosp.hosp.model;

import java.io.Serializable;

public class OrteseProtese implements Serializable {

    private Integer id;
    private ProgramaBean programa;
    private GrupoBean grupo;

    public OrteseProtese() {
        programa = new ProgramaBean();
        grupo = new GrupoBean();
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
}
