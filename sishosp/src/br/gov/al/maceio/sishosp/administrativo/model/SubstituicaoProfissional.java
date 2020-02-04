package br.gov.al.maceio.sishosp.administrativo.model;

import br.gov.al.maceio.sishosp.acl.model.FuncionarioBean;
import br.gov.al.maceio.sishosp.hosp.model.AtendimentoBean;

import java.io.Serializable;
import java.util.Date;

public class SubstituicaoProfissional implements Serializable {

    private Integer id;
    private AfastamentoProfissional afastamentoProfissional;
    private FuncionarioBean funcionario;
    private AtendimentoBean atendimento;
    private Integer idAtendimentos1;
    private String motivo;
    private Date dataAtendimento;
    private FuncionarioBean usuarioAcao;
    private Date dataHoraAcao;
    
    public SubstituicaoProfissional() {
        afastamentoProfissional = new AfastamentoProfissional();
        funcionario = new FuncionarioBean();
        usuarioAcao = new FuncionarioBean();
        atendimento = new AtendimentoBean();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

	public Date getDataAtendimento() {
		return dataAtendimento;
	}

	public void setDataAtendimento(Date dataAtendimento) {
		this.dataAtendimento = dataAtendimento;
	}

	public Integer getIdAtendimentos1() {
		return idAtendimentos1;
	}

	public void setIdAtendimentos1(Integer idAtendimentos1) {
		this.idAtendimentos1 = idAtendimentos1;
	}

	public FuncionarioBean getUsuarioAcao() {
		return usuarioAcao;
	}

	public void setUsuarioAcao(FuncionarioBean usuarioAcao) {
		this.usuarioAcao = usuarioAcao;
	}

	public Date getDataHoraAcao() {
		return dataHoraAcao;
	}

	public void setDataHoraAcao(Date dataHoraAcao) {
		this.dataHoraAcao = dataHoraAcao;
	}

	public AfastamentoProfissional getAfastamentoProfissional() {
		return afastamentoProfissional;
	}

	public void setAfastamentoProfissional(AfastamentoProfissional afastamentoProfissional) {
		this.afastamentoProfissional = afastamentoProfissional;
	}

	
}
