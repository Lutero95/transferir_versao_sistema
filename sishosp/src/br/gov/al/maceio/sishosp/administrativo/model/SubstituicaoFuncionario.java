package br.gov.al.maceio.sishosp.administrativo.model;

import br.gov.al.maceio.sishosp.acl.model.FuncionarioBean;
import br.gov.al.maceio.sishosp.hosp.model.AtendimentoBean;

import java.io.Serializable;

public class SubstituicaoFuncionario implements Serializable {

    private Integer id;
    private AfastamentoTemporario afastamentoTemporario;
    private FuncionarioBean funcionario;
    private AtendimentoBean atendimento;
    private String motivo;

    public SubstituicaoFuncionario() {
        afastamentoTemporario = new AfastamentoTemporario();
        funcionario = new FuncionarioBean();
        atendimento = new AtendimentoBean();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public AfastamentoTemporario getAfastamentoTemporario() {
        return afastamentoTemporario;
    }

    public void setAfastamentoTemporario(AfastamentoTemporario afastamentoTemporario) {
        this.afastamentoTemporario = afastamentoTemporario;
    }

    public FuncionarioBean getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(FuncionarioBean funcionario) {
        this.funcionario = funcionario;
    }

    public AtendimentoBean getAtendimento() {
        return atendimento;
    }

    public void setAtendimento(AtendimentoBean atendimento) {
        this.atendimento = atendimento;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }
}
