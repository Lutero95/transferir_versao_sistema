package br.gov.al.maceio.sishosp.financeiro.model;

import java.io.Serializable;

public class ImpostoBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;
    
    private Integer duplicata;
    private String descImposto;
    
    private Double pcRentencao;
    private Double valorRetencao;
    private Double valorBase;
    
    private Integer indice;

    public ImpostoBean() {
        
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getDuplicata() {
        return duplicata;
    }

    public void setDuplicata(Integer duplicata) {
        this.duplicata = duplicata;
    }

    public String getDescImposto() {
        return descImposto;
    }

    public void setDescImposto(String descImposto) {
        this.descImposto = descImposto;
    }

    public Double getPcRentencao() {
        return pcRentencao;
    }

    public void setPcRentencao(Double pcRentencao) {
        this.pcRentencao = pcRentencao;
    }

    public Double getValorRetencao() {
        return valorRetencao;
    }

    public void setValorRetencao(Double valorRetencao) {
        this.valorRetencao = valorRetencao;
    }

    public Double getValorBase() {
        return valorBase;
    }

    public void setValorBase(Double valorBase) {
        this.valorBase = valorBase;
    }

    public Integer getIndice() {
        return indice;
    }

    public void setIndice(Integer indice) {
        this.indice = indice;
    }
}