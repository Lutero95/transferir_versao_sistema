package br.gov.al.maceio.sishosp.questionario.model;

import java.io.Serializable;

public class ComposicaoFamiliar implements Serializable {
    private String nome;
    private String sexo;
    private Integer idade;
    private Integer parentesco;
    private String trabalha;
    private Double valor;
    private String pcd;
    private String benef;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public Integer getIdade() {
        return idade;
    }

    public void setIdade(Integer idade) {
        this.idade = idade;
    }

    public Integer getParentesco() {
        return parentesco;
    }

    public void setParentesco(Integer parentesco) {
        this.parentesco = parentesco;
    }

    public String getTrabalha() {
        return trabalha;
    }

    public void setTrabalha(String trabalha) {
        this.trabalha = trabalha;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public String getPcd() {
        return pcd;
    }

    public void setPcd(String pcd) {
        this.pcd = pcd;
    }

    public String getBenef() {
        return benef;
    }

    public void setBenef(String benef) {
        this.benef = benef;
    }
}
