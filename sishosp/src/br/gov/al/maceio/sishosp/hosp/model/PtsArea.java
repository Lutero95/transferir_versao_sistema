package br.gov.al.maceio.sishosp.hosp.model;

import br.gov.al.maceio.sishosp.acl.model.FuncionarioBean;

import java.io.Serializable;
import java.sql.Timestamp;

public class PtsArea implements Serializable {

    private Integer id;
    private String objetivoCurto;
    private String objetivoMedio;
    private String objetivoLongo;
    private String planoDeIntervencoesCurto;
    private String planoDeIntervencoesMedio;
    private String planoDeIntervencoesLongo;
    private Timestamp dataHoraOperacao;

    // HERDADOS
    private EspecialidadeBean area;
    private FuncionarioBean funcionario;

    public PtsArea() {
        area = new EspecialidadeBean();
        funcionario = new FuncionarioBean();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public EspecialidadeBean getArea() {
        return area;
    }

    public void setArea(EspecialidadeBean area) {
        this.area = area;
    }

    public String getObjetivoCurto() {
        return objetivoCurto;
    }

    public void setObjetivoCurto(String objetivoCurto) {
        this.objetivoCurto = objetivoCurto;
    }

    public String getObjetivoMedio() {
        return objetivoMedio;
    }

    public void setObjetivoMedio(String objetivoMedio) {
        this.objetivoMedio = objetivoMedio;
    }

    public String getObjetivoLongo() {
        return objetivoLongo;
    }

    public void setObjetivoLongo(String objetivoLongo) {
        this.objetivoLongo = objetivoLongo;
    }

    public String getPlanoDeIntervencoesCurto() {
        return planoDeIntervencoesCurto;
    }

    public void setPlanoDeIntervencoesCurto(String planoDeIntervencoesCurto) {
        this.planoDeIntervencoesCurto = planoDeIntervencoesCurto;
    }

    public String getPlanoDeIntervencoesMedio() {
        return planoDeIntervencoesMedio;
    }

    public void setPlanoDeIntervencoesMedio(String planoDeIntervencoesMedio) {
        this.planoDeIntervencoesMedio = planoDeIntervencoesMedio;
    }

    public String getPlanoDeIntervencoesLongo() {
        return planoDeIntervencoesLongo;
    }

    public void setPlanoDeIntervencoesLongo(String planoDeIntervencoesLongo) {
        this.planoDeIntervencoesLongo = planoDeIntervencoesLongo;
    }

    public Timestamp getDataHoraOperacao() {
        return dataHoraOperacao;
    }

    public void setDataHoraOperacao(Timestamp dataHoraOperacao) {
        this.dataHoraOperacao = dataHoraOperacao;
    }

    public FuncionarioBean getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(FuncionarioBean funcionario) {
        this.funcionario = funcionario;
    }

}
