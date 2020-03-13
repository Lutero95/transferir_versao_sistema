package br.gov.al.maceio.sishosp.hosp.model.dto;

import br.gov.al.maceio.sishosp.acl.model.FuncionarioBean;
import br.gov.al.maceio.sishosp.administrativo.model.RemocaoProfissionalEquipe;
import br.gov.al.maceio.sishosp.hosp.model.EquipeBean;
import br.gov.al.maceio.sishosp.hosp.model.GrupoBean;
import br.gov.al.maceio.sishosp.hosp.model.ProgramaBean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SubstituicaoProfissionalEquipeDTO implements Serializable {

    private Integer id;
    private FuncionarioBean funcionarioRemovido;
    private EquipeBean equipe;
    private FuncionarioBean funcionarioAssumir;
    private List<Integer> listaAtendimentos1;
    private RemocaoProfissionalEquipe remocaoProfissionalEquipe;
    private List<RemocaoProfissionalEquipe> listaRemocoes;
    private Integer idAtendimentos1;
    private Date dataAtendimento;
    private ProgramaBean programa;
    private GrupoBean grupo;

    public SubstituicaoProfissionalEquipeDTO() {
        funcionarioRemovido = new FuncionarioBean();
        funcionarioAssumir = new FuncionarioBean();
        equipe = new EquipeBean();
        listaAtendimentos1 = new ArrayList<>();
        programa = new ProgramaBean();
        grupo = new GrupoBean();
        remocaoProfissionalEquipe = new RemocaoProfissionalEquipe();
        listaRemocoes = new ArrayList<>();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public FuncionarioBean getFuncionarioRemovido() {
        return funcionarioRemovido;
    }

    public void setFuncionarioRemovido(FuncionarioBean funcionarioRemovido) {
        this.funcionarioRemovido = funcionarioRemovido;
    }

    public EquipeBean getEquipe() {
        return equipe;
    }

    public void setEquipe(EquipeBean equipe) {
        this.equipe = equipe;
    }

    public FuncionarioBean getFuncionarioAssumir() {
        return funcionarioAssumir;
    }

    public void setFuncionarioAssumir(FuncionarioBean funcionarioAssumir) {
        this.funcionarioAssumir = funcionarioAssumir;
    }

    public List<Integer> getListaAtendimentos1() {
        return listaAtendimentos1;
    }

    public void setListaAtendimentos1(List<Integer> listaAtendimentos1) {
        this.listaAtendimentos1 = listaAtendimentos1;
    }

    public RemocaoProfissionalEquipe getRemocaoProfissionalEquipe() {
        return remocaoProfissionalEquipe;
    }

    public void setRemocaoProfissionalEquipe(RemocaoProfissionalEquipe remocaoProfissionalEquipe) {
        this.remocaoProfissionalEquipe = remocaoProfissionalEquipe;
    }

    public List<RemocaoProfissionalEquipe> getListaRemocoes() {
        return listaRemocoes;
    }

    public void setListaRemocoes(List<RemocaoProfissionalEquipe> listaRemocoes) {
        this.listaRemocoes = listaRemocoes;
    }

	public Integer getIdAtendimentos1() {
		return idAtendimentos1;
	}

	public Date getDataAtendimento() {
		return dataAtendimento;
	}

	public void setIdAtendimentos1(Integer idAtendimentos1) {
		this.idAtendimentos1 = idAtendimentos1;
	}

	public void setDataAtendimento(Date dataAtendimento) {
		this.dataAtendimento = dataAtendimento;
	}

	public ProgramaBean getPrograma() {
		return programa;
	}

	public GrupoBean getGrupo() {
		return grupo;
	}

	public void setPrograma(ProgramaBean programa) {
		this.programa = programa;
	}

	public void setGrupo(GrupoBean grupo) {
		this.grupo = grupo;
	}
}
