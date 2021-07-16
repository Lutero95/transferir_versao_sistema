package br.gov.al.maceio.sishosp.hosp.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.gov.al.maceio.sishosp.acl.model.FuncionarioBean;
import br.gov.al.maceio.sishosp.hosp.model.dto.ProcedimentoCidDTO;

public class AtendimentoBean implements Serializable {

    
	private static final long serialVersionUID = 1L;
	private Integer id;
    private Integer id1;
    private Date dataAtendimento;
    private Date dataAtendimentoInicio;
    private Date dataAtendimentoFinal;
    private Date dataAtendido;
    private String turno;
    private String situacao;
    private String ehEquipe;
    private String evolucao;
    private Boolean avaliacao;
    private String perfil;
    private String horarioAtendimento;
    private String presenca;
    private Boolean avulso; 
    private List<String> listaNomeProfissionais;
    private boolean validadoPeloSigtapAnterior;
    // HERDADOS
    private PacienteBean paciente;
    private ProcedimentoBean procedimento;
    private ProgramaBean programa;
    private GrupoBean grupo;
    private TipoAtendimentoBean tipoAt;
    private FuncionarioBean funcionario;
    private EquipeBean equipe;
    private CboBean cbo;
    private UnidadeBean unidade;
    private InsercaoPacienteBean insercaoPacienteBean;
    private GrupoBean grupoAvaliacao;
    private SituacaoAtendimentoBean situacaoAtendimento;
    private SituacaoAtendimentoBean situacaoAtendimentoAnterior;
    private CidBean cidPrimario;
    private List<ProcedimentoCidDTO> listaProcedimentoCid;

    public AtendimentoBean() {
        this.paciente = new PacienteBean();
        this.procedimento = new ProcedimentoBean();
        this.programa = new ProgramaBean();
        this.tipoAt = new TipoAtendimentoBean();
        this.funcionario = new FuncionarioBean();
        this.equipe = new EquipeBean();
        this.cbo = new CboBean();
        this.unidade = new UnidadeBean();
        this.insercaoPacienteBean = new InsercaoPacienteBean();
        this.grupoAvaliacao = new GrupoBean();
        this.grupo = new GrupoBean();
        this.situacaoAtendimento = new SituacaoAtendimentoBean();
        this.situacaoAtendimentoAnterior = new SituacaoAtendimentoBean();
        this.cidPrimario = new CidBean();
        this.listaProcedimentoCid = new ArrayList<>();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDataAtendimentoInicio() {
        return dataAtendimentoInicio;
    }

    public void setDataAtendimentoInicio(Date dataAtendimentoInicio) {
        this.dataAtendimentoInicio = dataAtendimentoInicio;
    }

    public Date getDataAtendimentoFinal() {
        return dataAtendimentoFinal;
    }

    public void setDataAtendimentoFinal(Date dataAtendimentoFinal) {
        this.dataAtendimentoFinal = dataAtendimentoFinal;
    }
    

    public Date getDataAtendido() {
		return dataAtendido;
	}

	public void setDataAtendido(Date dataAtendido) {
		this.dataAtendido = dataAtendido;
	}

	public String getTurno() {
        return turno;
    }

    public void setTurno(String turno) {
        this.turno = turno;
    }

    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public String getEhEquipe() {
        return ehEquipe;
    }

    public void setEhEquipe(String ehEquipe) {
        this.ehEquipe = ehEquipe;
    }

    public PacienteBean getPaciente() {
        return paciente;
    }

    public void setPaciente(PacienteBean paciente) {
        this.paciente = paciente;
    }

    public ProcedimentoBean getProcedimento() {
        return procedimento;
    }

    public void setProcedimento(ProcedimentoBean procedimento) {
        this.procedimento = procedimento;
    }

    public ProgramaBean getPrograma() {
        return programa;
    }

    public void setPrograma(ProgramaBean programa) {
        this.programa = programa;
    }

    public TipoAtendimentoBean getTipoAt() {
        return tipoAt;
    }

    public void setTipoAt(TipoAtendimentoBean tipoAt) {
        this.tipoAt = tipoAt;
    }

    public FuncionarioBean getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(FuncionarioBean funcionario) {
        this.funcionario = funcionario;
    }

    public EquipeBean getEquipe() {
        return equipe;
    }

    public void setEquipe(EquipeBean equipe) {
        this.equipe = equipe;
    }

    public Integer getId1() {
        return id1;
    }

    public void setId1(Integer id1) {
        this.id1 = id1;
    }

    public CboBean getCbo() {
        return cbo;
    }

    public void setCbo(CboBean cbo) {
        this.cbo = cbo;
    }

    public String getEvolucao() {
        return evolucao;
    }

    public void setEvolucao(String evolucao) {
        this.evolucao = evolucao;
    }

    public Boolean getAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(Boolean avaliacao) {
        this.avaliacao = avaliacao;
    }

    public InsercaoPacienteBean getInsercaoPacienteBean() {
        return insercaoPacienteBean;
    }

    public void setInsercaoPacienteBean(InsercaoPacienteBean insercaoPacienteBean) {
        this.insercaoPacienteBean = insercaoPacienteBean;
    }

    public String getPerfil() {
        return perfil;
    }

    public void setPerfil(String perfil) {
        this.perfil = perfil;
    }

    public GrupoBean getGrupoAvaliacao() {
        return grupoAvaliacao;
    }

    public void setGrupoAvaliacao(GrupoBean grupoAvaliacao) {
        this.grupoAvaliacao = grupoAvaliacao;
    }

	public UnidadeBean getUnidade() {
		return unidade;
	}

	public void setUnidade(UnidadeBean unidade) {
		this.unidade = unidade;
	}

	public GrupoBean getGrupo() {
		return grupo;
	}

	public void setGrupo(GrupoBean grupo) {
		this.grupo = grupo;
	}

	public String getHorarioAtendimento() {
		return horarioAtendimento;
	}

	public void setHorarioAtendimento(String horarioAtendimento) {
		this.horarioAtendimento = horarioAtendimento;
	}

	public String getPresenca() {
		return presenca;
	}

	public void setPresenca(String presenca) {
		this.presenca = presenca;
	}

	public SituacaoAtendimentoBean getSituacaoAtendimento() {
		return situacaoAtendimento;
	}

	public void setSituacaoAtendimento(SituacaoAtendimentoBean situacaoAtendimento) {
		this.situacaoAtendimento = situacaoAtendimento;
	}

	public SituacaoAtendimentoBean getSituacaoAtendimentoAnterior() {
		return situacaoAtendimentoAnterior;
	}

	public void setSituacaoAtendimentoAnterior(SituacaoAtendimentoBean situacaoAtendimentoAnterior) {
		this.situacaoAtendimentoAnterior = situacaoAtendimentoAnterior;
	}

    public Date getDataAtendimento() {
        return dataAtendimento;
    }

    public void setDataAtendimento(Date dataAtendimento) {
        this.dataAtendimento = dataAtendimento;
    }

	public boolean getAvulso() {
		return avulso;
	}

	public void setAvulso(Boolean avulso) {
		this.avulso = avulso;
	}

	public List<String> getListaNomeProfissionais() {
		return listaNomeProfissionais;
	}

	public void setListaNomeProfissionais(List<String> listaNomeProfissionais) {
		this.listaNomeProfissionais = listaNomeProfissionais;
	}

	public boolean isValidadoPeloSigtapAnterior() {
		return validadoPeloSigtapAnterior;
	}

	public void setValidadoPeloSigtapAnterior(boolean validadoPeloSigtapAnterior) {
		this.validadoPeloSigtapAnterior = validadoPeloSigtapAnterior;
	}

	public CidBean getCidPrimario() {
		return cidPrimario;
	}

	public void setCidPrimario(CidBean cidPrimario) {
		this.cidPrimario = cidPrimario;
	}

	public List<ProcedimentoCidDTO> getListaProcedimentoCid() {
		return listaProcedimentoCid;
	}

	public void setListaProcedimentoCid(List<ProcedimentoCidDTO> listaProcedimentoCid) {
		this.listaProcedimentoCid = listaProcedimentoCid;
	}
	
}
