package br.gov.al.maceio.sishosp.hosp.model.dto;

import br.gov.al.maceio.sishosp.acl.model.FuncionarioBean;
import br.gov.al.maceio.sishosp.hosp.model.*;

import java.util.ArrayList;

public class AvaliacaoInsercaoDTO {

    private ProgramaBean programaBean;
    private GrupoBean grupoBean;
    private EquipeBean equipeBean;
    private ArrayList<FuncionarioBean> listaProfissionais;

    public AvaliacaoInsercaoDTO() {
        programaBean = new ProgramaBean();
        grupoBean = new GrupoBean();
        equipeBean = new EquipeBean();
        listaProfissionais = new ArrayList<>();
    }

    public ProgramaBean getProgramaBean() {
        return programaBean;
    }

    public void setProgramaBean(ProgramaBean programaBean) {
        this.programaBean = programaBean;
    }

    public GrupoBean getGrupoBean() {
        return grupoBean;
    }

    public void setGrupoBean(GrupoBean grupoBean) {
        this.grupoBean = grupoBean;
    }

    public EquipeBean getEquipeBean() {
        return equipeBean;
    }

    public void setEquipeBean(EquipeBean equipeBean) {
        this.equipeBean = equipeBean;
    }

    public ArrayList<FuncionarioBean> getListaProfissionais() {
        return listaProfissionais;
    }

    public void setListaProfissionais(ArrayList<FuncionarioBean> listaProfissionais) {
        this.listaProfissionais = listaProfissionais;
    }
}
