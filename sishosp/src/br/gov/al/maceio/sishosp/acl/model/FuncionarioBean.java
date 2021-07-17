package br.gov.al.maceio.sishosp.acl.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import br.gov.al.maceio.sishosp.hosp.model.*;

public class FuncionarioBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private String nome;
    private String senha;
    private String novaSenha;
    private String confirmacaoNovaSenha;
    private Date datacriacao;
    private String ativo;
    private String email;
    private String cpf;
    private boolean primeiroAcesso;
    private Boolean realizaAtendimento;
    private Boolean realizaLiberacoes;
    private Boolean realizaAuditoria;
    private Boolean realizaEncaixes;
    private Boolean permiteAutorizacaoLaudo;
    private String cns;
    private String diasSemana;
    private ArrayList<HorarioAtendimento> listaDiasAtendimentoSemana;
    private Integer diaSemana;
    private UnidadeBean unidadeExtra;
    private String nomeBancoAcesso;
    private String horarioAtendimento;
    private ConselhoBean conselho;
    
    /* TODO Remover essa propriedade depois que o ajuste de turno por dia da semana for implementado na agenda normal*/
    private ArrayList<String> listDiasSemana;
    
    
    private Integer codigoDaUnidadeSelecionada; 

    // LISTAS
    private List<ProgramaBean> programa;
    private List<ProgramaBean> programaNovo;
    private List<GrupoBean> grupo;
    private List<GrupoBean> grupoNovo;
    private List<UnidadeBean> listaUnidades;

    // HERDADOS
    private EspecialidadeBean especialidade;
    private ProcedimentoBean proc1;
    private ProcedimentoBean proc2;
    private ProgramaBean progAdd;
    private GrupoBean grupoAdd;
    private ProgramaBean progRmv;
    private GrupoBean grupoRmv;
    private PermissoesBean permissao;
    private UnidadeBean unidade;
    private List<CboBean> listaCbos;

    // ACL
    private Perfil perfil;
    private Long id; // id usuário para o acl.
    private boolean usuarioAtivo;
    private List<Integer> listaIdSistemas;
    private List<Long> listaIdMenus;
    private List<Long> listaIdPermissoes;
    private Boolean excecaoBloqueioHorario;

    private List<SecretariaBean> listaSecreFolha;

    public FuncionarioBean() {
        permissao = new PermissoesBean();
        this.programa = new ArrayList<ProgramaBean>();
        this.programaNovo = new ArrayList<ProgramaBean>();
        this.grupo = new ArrayList<GrupoBean>();
        this.grupoNovo = new ArrayList<GrupoBean>();
        this.especialidade = new EspecialidadeBean();
        this.proc1 = new ProcedimentoBean();
        this.progAdd = new ProgramaBean();
        this.grupoAdd = new GrupoBean();
        this.progRmv = new ProgramaBean();
        this.grupoRmv = new GrupoBean();
        this.unidade = new UnidadeBean();
        this.perfil = new Perfil();
        listaDiasAtendimentoSemana = new ArrayList<HorarioAtendimento>();
        this.realizaAtendimento = false;
        this.realizaLiberacoes = false;
        listaUnidades = new ArrayList<>();
        unidadeExtra = new UnidadeBean();
        senha = "";
        novaSenha = "";
        confirmacaoNovaSenha = "";
        listDiasSemana = new ArrayList<String>();
        this.listaCbos = new ArrayList<CboBean>();
        this.conselho = new ConselhoBean();
    }


    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Date getDatacriacao() {
        return datacriacao;
    }

    public void setDatacriacao(Date datacriacao) {
        this.datacriacao = datacriacao;
    }

    public boolean isPrimeiroAcesso() {
        return primeiroAcesso;
    }

    public void setPrimeiroAcesso(boolean primeiroAcesso) {
        this.primeiroAcesso = primeiroAcesso;
    }

    public String getAtivo() {
        return ativo;
    }

    public void setAtivo(String ativo) {
        this.ativo = ativo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public PermissoesBean getPermissao() {
        return permissao;
    }

    public void setPermissao(PermissoesBean permissao) {
        this.permissao = permissao;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Integer> getListaIdSistemas() {
        return listaIdSistemas;
    }

    public void setListaIdSistemas(List<Integer> listaIdSistemas) {
        this.listaIdSistemas = listaIdSistemas;
    }

    public List<Long> getListaIdMenus() {
        return listaIdMenus;
    }

    public void setListaIdMenus(List<Long> listaIdMenus) {
        this.listaIdMenus = listaIdMenus;
    }

    public List<Long> getListaIdPermissoes() {
        return listaIdPermissoes;
    }

    public void setListaIdPermissoes(List<Long> listaIdPermissoes) {
        this.listaIdPermissoes = listaIdPermissoes;
    }

    public boolean isUsuarioAtivo() {
        return usuarioAtivo;
    }

    public void setUsuarioAtivo(boolean usuarioAtivo) {
        this.usuarioAtivo = usuarioAtivo;
    }

    public List<SecretariaBean> getListaSecreFolha() {
        return listaSecreFolha;
    }

    public void setListaSecreFolha(List<SecretariaBean> listaSecreFolha) {
        this.listaSecreFolha = listaSecreFolha;
    }

    public List<ProgramaBean> getProgramaNovo() {
        this.programaNovo = this.programa;
        return programaNovo;
    }

    public void setProgramaNovo(List<ProgramaBean> programaNovo) {
        this.programaNovo = programaNovo;
    }

    public List<GrupoBean> getGrupoNovo() {
        this.grupoNovo = this.grupo;
        return grupoNovo;
    }

    public void setGrupoNovo(List<GrupoBean> grupoNovo) {
        this.grupoNovo = grupoNovo;
    }

    public List<GrupoBean> getGrupo() {
        return grupo;
    }

    public void setGrupo(List<GrupoBean> grupo) {
        this.grupo = grupo;
    }

    public List<ProgramaBean> getPrograma() {
        return programa;
    }

    public void setPrograma(List<ProgramaBean> programa) {
        this.programa = programa;
    }

    public EspecialidadeBean getEspecialidade() {
        return especialidade;
    }

    public void setEspecialidade(EspecialidadeBean especialidade) {
        this.especialidade = especialidade;
    }

    public ProcedimentoBean getProc1() {
        return proc1;
    }

    public void setProc1(ProcedimentoBean proc1) {
        this.proc1 = proc1;
    }

    public ProcedimentoBean getProc2() {
        return proc2;
    }

    public void setProc2(ProcedimentoBean proc2) {
        this.proc2 = proc2;
    }

    public ProgramaBean getProgAdd() {
        return progAdd;
    }

    public void setProgAdd(ProgramaBean progAdd) {
        this.progAdd = progAdd;
    }

    public GrupoBean getGrupoAdd() {
        return grupoAdd;
    }

    public void setGrupoAdd(GrupoBean grupoAdd) {
        this.grupoAdd = grupoAdd;
    }

    public ProgramaBean getProgRmv() {
        return progRmv;
    }

    public void setProgRmv(ProgramaBean progRmv) {
        this.progRmv = progRmv;
    }

    public GrupoBean getGrupoRmv() {
        return grupoRmv;
    }

    public void setGrupoRmv(GrupoBean grupoRmv) {
        this.grupoRmv = grupoRmv;
    }

    public void addGrupoLista() {
        boolean existe = false;
        if (grupo.size() == 0) {
            this.grupo.add(grupoAdd);
        } else {
            for (int i = 0; i < grupo.size(); i++) {
                if (grupo.get(i).getIdGrupo() == grupoAdd.getIdGrupo()) {
                    existe = true;
                }
            }
            if (existe == false) {
                this.grupo.add(grupoAdd);
            } else {
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
                        "Esse grupo já foi adicionado!", "Sucesso");
                FacesContext.getCurrentInstance().addMessage(null, msg);
            }
        }
    }

    public void addProgLista() {
        boolean existe = false;
        if (programa.size() == 0) {
            this.programa.add(progAdd);
        } else {
            for (int i = 0; i < programa.size(); i++) {
                if (programa.get(i).getIdPrograma() == progAdd.getIdPrograma()) {
                    existe = true;
                }
            }
            if (existe == false) {
                this.programa.add(progAdd);
            } else {
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
                        "Esse programa já foi adicionado!", "Sucesso");
                FacesContext.getCurrentInstance().addMessage(null, msg);
            }
        }

    }

    public void removeProgLista() {
        this.programa.remove(this.progRmv);
    }

    public void removeGrupoLista() {
        this.grupo.remove(this.grupoRmv);
    }

    public Boolean getRealizaAtendimento() {
        return realizaAtendimento;
    }

    public void setRealizaAtendimento(Boolean realizaAtendimento) {
        this.realizaAtendimento = realizaAtendimento;
    }

    public String getCns() {
        return cns;
    }

    public void setCns(String cns) {
        this.cns = cns;
    }

    public String getDiasSemana() {
        return diasSemana;
    }

    public void setDiasSemana(String diasSemana) {
        this.diasSemana = diasSemana;
    }

  

    public Integer getDiaSemana() {
        return diaSemana;
    }

    public void setDiaSemana(Integer diaSemana) {
        this.diaSemana = diaSemana;
    }

    public Perfil getPerfil() {
        return perfil;
    }

    public void setPerfil(Perfil perfil) {
        this.perfil = perfil;
    }

    public Boolean getRealizaLiberacoes() {
        return realizaLiberacoes;
    }

    public void setRealizaLiberacoes(Boolean realizaLiberacoes) {
        this.realizaLiberacoes = realizaLiberacoes;
    }

    public Boolean getRealizaEncaixes() {
        return realizaEncaixes;
    }

    public void setRealizaEncaixes(Boolean realizaEncaixes) {
        this.realizaEncaixes = realizaEncaixes;
    }
    
	public Boolean getPermiteAutorizacaoLaudo() {
		return permiteAutorizacaoLaudo;
	}

	public void setPermiteAutorizacaoLaudo(Boolean permiteAutorizacaoLaudo) {
		this.permiteAutorizacaoLaudo = permiteAutorizacaoLaudo;
	}

	public String getNomeBancoAcesso() {
		return nomeBancoAcesso;
	}

	public void setNomeBancoAcesso(String nomeBancoAcesso) {
		this.nomeBancoAcesso = nomeBancoAcesso;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public UnidadeBean getUnidadeExtra() {
		return unidadeExtra;
	}

	public void setUnidadeExtra(UnidadeBean unidadeExtra) {
		this.unidadeExtra = unidadeExtra;
	}

	public List<UnidadeBean> getListaUnidades() {
		return listaUnidades;
	}

	public void setListaUnidades(List<UnidadeBean> listaUnidades) {
		this.listaUnidades = listaUnidades;
	}

	public UnidadeBean getUnidade() {
		return unidade;
	}

	public void setUnidade(UnidadeBean unidade) {
		this.unidade = unidade;
	}

    public String getNovaSenha() {
        return novaSenha;
    }

    public void setNovaSenha(String novaSenha) {
        this.novaSenha = novaSenha;
    }

    public String getConfirmacaoNovaSenha() {
        return confirmacaoNovaSenha;
    }

    public void setConfirmacaoNovaSenha(String confirmacaoNovaSenha) {
        this.confirmacaoNovaSenha = confirmacaoNovaSenha;
    }



	public String getHorarioAtendimento() {
		return horarioAtendimento;
	}


	public void setHorarioAtendimento(String horarioAtendimento) {
		this.horarioAtendimento = horarioAtendimento;
	}


	public ArrayList<HorarioAtendimento> getListaDiasAtendimentoSemana() {
		return listaDiasAtendimentoSemana;
	}


	public void setListaDiasAtendimentoSemana(ArrayList<HorarioAtendimento> listaDiasAtendimentoSemana) {
		this.listaDiasAtendimentoSemana = listaDiasAtendimentoSemana;
	}


	public ArrayList<String> getListDiasSemana() {
		return listDiasSemana;
	}


	public void setListDiasSemana(ArrayList<String> listDiasSemana) {
		this.listDiasSemana = listDiasSemana;
	}
	    public Integer getCodigoDaUnidadeSelecionada() {
        return codigoDaUnidadeSelecionada;
    }

    public void setCodigoDaUnidadeSelecionada(Integer codigoDaUnidadeSelecionada) {
        this.codigoDaUnidadeSelecionada = codigoDaUnidadeSelecionada;
    }


	public Boolean getExcecaoBloqueioHorario() {
		return excecaoBloqueioHorario;
	}


	public void setExcecaoBloqueioHorario(Boolean excecaoBloqueioHorario) {
		this.excecaoBloqueioHorario = excecaoBloqueioHorario;
	}

    public Boolean getRealizaAuditoria() {
        return realizaAuditoria;
    }

    public void setRealizaAuditoria(Boolean realizaAuditoria) {
        this.realizaAuditoria = realizaAuditoria;
    }
    
    public List<CboBean> getListaCbos() {
		return listaCbos;
	}

	public void setListaCbos(List<CboBean> listaCbos) {
		this.listaCbos = listaCbos;
	}

	public ConselhoBean getConselho() {
		return conselho;
	}

	public void setConselho(ConselhoBean conselho) {
		this.conselho = conselho;
	}


	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FuncionarioBean)) return false;
        FuncionarioBean that = (FuncionarioBean) o;
        return isPrimeiroAcesso() == that.isPrimeiroAcesso() &&
                isUsuarioAtivo() == that.isUsuarioAtivo() &&
                Objects.equals(getNome(), that.getNome()) &&
                Objects.equals(getSenha(), that.getSenha()) &&
                Objects.equals(getNovaSenha(), that.getNovaSenha()) &&
                Objects.equals(getConfirmacaoNovaSenha(), that.getConfirmacaoNovaSenha()) &&
                Objects.equals(getDatacriacao(), that.getDatacriacao()) &&
                Objects.equals(getAtivo(), that.getAtivo()) &&
                Objects.equals(getEmail(), that.getEmail()) &&
                Objects.equals(getCpf(), that.getCpf()) &&
                Objects.equals(getRealizaAtendimento(), that.getRealizaAtendimento()) &&
                Objects.equals(getRealizaLiberacoes(), that.getRealizaLiberacoes()) &&
                Objects.equals(getRealizaEncaixes(), that.getRealizaEncaixes()) &&
                Objects.equals(getCns(), that.getCns()) &&
                Objects.equals(getDiasSemana(), that.getDiasSemana()) &&
                Objects.equals(getListaDiasAtendimentoSemana(), that.getListaDiasAtendimentoSemana()) &&
                Objects.equals(getDiaSemana(), that.getDiaSemana()) &&
                Objects.equals(getUnidadeExtra(), that.getUnidadeExtra()) &&
                Objects.equals(getNomeBancoAcesso(), that.getNomeBancoAcesso()) &&
                Objects.equals(getHorarioAtendimento(), that.getHorarioAtendimento()) &&
                Objects.equals(getListDiasSemana(), that.getListDiasSemana()) &&
                Objects.equals(getCodigoDaUnidadeSelecionada(), that.getCodigoDaUnidadeSelecionada()) &&
                Objects.equals(getPrograma(), that.getPrograma()) &&
                Objects.equals(getProgramaNovo(), that.getProgramaNovo()) &&
                Objects.equals(getGrupo(), that.getGrupo()) &&
                Objects.equals(getGrupoNovo(), that.getGrupoNovo()) &&
                Objects.equals(getListaUnidades(), that.getListaUnidades()) &&
                Objects.equals(getEspecialidade(), that.getEspecialidade()) &&
                Objects.equals(getProc1(), that.getProc1()) &&
                Objects.equals(getProc2(), that.getProc2()) &&
                Objects.equals(getProgAdd(), that.getProgAdd()) &&
                Objects.equals(getGrupoAdd(), that.getGrupoAdd()) &&
                Objects.equals(getProgRmv(), that.getProgRmv()) &&
                Objects.equals(getGrupoRmv(), that.getGrupoRmv()) &&
                Objects.equals(getPermissao(), that.getPermissao()) &&
                Objects.equals(getUnidade(), that.getUnidade()) &&
                Objects.equals(getPerfil(), that.getPerfil()) &&
                Objects.equals(getId(), that.getId()) &&
                Objects.equals(getListaIdSistemas(), that.getListaIdSistemas()) &&
                Objects.equals(getListaIdMenus(), that.getListaIdMenus()) &&
                Objects.equals(getListaIdPermissoes(), that.getListaIdPermissoes()) &&
                Objects.equals(getExcecaoBloqueioHorario(), that.getExcecaoBloqueioHorario()) &&
                Objects.equals(getListaSecreFolha(), that.getListaSecreFolha()) &&
                Objects.equals(getPermiteAutorizacaoLaudo(), that.getPermiteAutorizacaoLaudo()) &&
                Objects.equals(getListaCbos(), that.getListaCbos()) && 
                Objects.equals(getConselho(), that.getConselho());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getNome(), getSenha(), getNovaSenha(), getConfirmacaoNovaSenha(), getDatacriacao(), getAtivo(), getEmail(), getCpf(), isPrimeiroAcesso(), getRealizaAtendimento(), 
        		getRealizaLiberacoes(), getRealizaEncaixes(), getCns(), getDiasSemana(), getListaDiasAtendimentoSemana(), getDiaSemana(), getUnidadeExtra(), getNomeBancoAcesso(), 
        		getHorarioAtendimento(), getListDiasSemana(), getCodigoDaUnidadeSelecionada(), getPrograma(), getProgramaNovo(), getGrupo(), getGrupoNovo(), getListaUnidades(), getEspecialidade(), 
        		getProc1(), getProc2(), getProgAdd(), getGrupoAdd(), getProgRmv(), getGrupoRmv(), getPermissao(), getUnidade(), getPerfil(), getId(), isUsuarioAtivo(), getListaIdSistemas(), 
        		getListaIdMenus(), getListaIdPermissoes(), getExcecaoBloqueioHorario(), getListaSecreFolha(), getPermiteAutorizacaoLaudo(), getListaCbos(), getConselho());
    }
}

