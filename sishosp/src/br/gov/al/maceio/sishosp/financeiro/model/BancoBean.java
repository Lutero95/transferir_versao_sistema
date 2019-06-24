package br.gov.al.maceio.sishosp.financeiro.model;

import java.io.Serializable;

import br.gov.al.maceio.sishosp.acl.model.FuncionarioBean;

public class BancoBean implements Serializable {

    private Integer id;
    private String codbanco;
    private String banco;
    private String agencia;
    private String conta;
    private String descricao;
    private String ativo;
    private String contaCaixa;
    private FuncionarioBean func;

    public BancoBean() {
        func = new FuncionarioBean();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBanco() {
        return banco;
    }

    public void setBanco(String banco) {
        this.banco = banco;
    }

    public String getAgencia() {
        return agencia;
    }

    public void setAgencia(String agencia) {
        this.agencia = agencia;
    }

    public String getConta() {
        return conta;
    }

    public void setConta(String conta) {
        this.conta = conta;
    }

    public String getAtivo() {
        return ativo;
    }

    public void setAtivo(String ativo) {
        this.ativo = ativo;
    }



    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getCodbanco() {
        return codbanco;
    }

    public void setCodbanco(String codbanco) {
        this.codbanco = codbanco;
    }

    public String getContaCaixa() {
        return contaCaixa;
    }

    public void setContaCaixa(String contaCaixa) {
        this.contaCaixa = contaCaixa;
    }

	public FuncionarioBean getFunc() {
		return func;
	}

	public void setFunc(FuncionarioBean func) {
		this.func = func;
	}
}