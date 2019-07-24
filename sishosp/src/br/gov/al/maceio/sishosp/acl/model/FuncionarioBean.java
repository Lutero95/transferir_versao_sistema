package br.gov.al.maceio.sishosp.acl.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import br.gov.al.maceio.sishosp.hosp.model.CboBean;
import br.gov.al.maceio.sishosp.hosp.model.EspecialidadeBean;
import br.gov.al.maceio.sishosp.hosp.model.GrupoBean;
import br.gov.al.maceio.sishosp.hosp.model.ProcedimentoBean;
import br.gov.al.maceio.sishosp.hosp.model.ProgramaBean;
import br.gov.al.maceio.sishosp.hosp.model.UnidadeBean;

public class FuncionarioBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private Integer codigo;
    private String nome;
    private String senha;
    private Date datacriacao;
    private String ativo;
    private String email;
    private String cpf;
    private boolean primeiroAcesso;
    private Boolean realizaAtendimento;
    private Boolean realizaLiberacoes;
    private Boolean realizaEncaixes;
    private String cns;
    private String diasSemana;
    private ArrayList<String> listDiasSemana;
    private Integer diaSemana;
    private UnidadeBean unidadeExtra;
    private String nomeBancoAcesso;

    // LISTAS
    private List<ProgramaBean> programa;
    private List<ProgramaBean> programaNovo;
    private List<GrupoBean> grupo;
    private List<GrupoBean> grupoNovo;
    private List<UnidadeBean> listaUnidades;

    // HERDADOS
    private EspecialidadeBean especialidade;
    private CboBean cbo;
    private ProcedimentoBean proc1;
    private ProcedimentoBean proc2;
    private ProgramaBean progAdd;
    private GrupoBean grupoAdd;
    private ProgramaBean progRmv;
    private GrupoBean grupoRmv;
    private PermissoesBean permissao;
    private UnidadeBean unidade;

    // ACL
    private Perfil perfil;
    private Long id; // id usuário para o acl.
    private boolean usuarioAtivo;
    private List<Integer> listaIdSistemas;
    private List<Long> listaIdMenus;
    private List<Long> listaIdPermissoes;

    private List<SecretariaBean> listaSecreFolha;

    public FuncionarioBean() {
        permissao = new PermissoesBean();
        this.programa = new ArrayList<ProgramaBean>();
        this.programaNovo = new ArrayList<ProgramaBean>();
        this.grupo = new ArrayList<GrupoBean>();
        this.grupoNovo = new ArrayList<GrupoBean>();
        this.especialidade = new EspecialidadeBean();
        this.cbo = new CboBean();
        this.proc1 = new ProcedimentoBean();
        this.progAdd = new ProgramaBean();
        this.grupoAdd = new GrupoBean();
        this.progRmv = new ProgramaBean();
        this.grupoRmv = new GrupoBean();
        this.unidade = new UnidadeBean();
        this.perfil = new Perfil();
        listDiasSemana = new ArrayList<String>();
        this.realizaAtendimento = false;
        this.realizaLiberacoes = false;
        listaUnidades = new ArrayList<>();
        unidadeExtra = new UnidadeBean();
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
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

    public CboBean getCbo() {
        return cbo;
    }

    public void setCbo(CboBean cbo) {
        this.cbo = cbo;
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

    public ArrayList<String> getListDiasSemana() {
        return listDiasSemana;
    }

    public void setListDiasSemana(ArrayList<String> listDiasSemana) {
        this.listDiasSemana = listDiasSemana;
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
}