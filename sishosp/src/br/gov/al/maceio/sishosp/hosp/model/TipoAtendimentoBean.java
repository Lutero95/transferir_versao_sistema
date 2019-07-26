package br.gov.al.maceio.sishosp.hosp.model;

import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.JSFUtil;
import br.gov.al.maceio.sishosp.comum.util.VerificadorUtil;
import br.gov.al.maceio.sishosp.hosp.dao.TipoAtendimentoDAO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TipoAtendimentoBean implements Serializable {

    private Integer idTipo;
    private String descTipoAt;
    private Boolean equipe;
    private Boolean profissional;
    private boolean primeiroAt;
    private Integer intervaloMinimo;


    // LISTAS
    private List<GrupoBean> grupo;
    private List<GrupoBean> grupoNovo;
    private List<ProgramaBean> listaPrograma;
    private List<ProgramaBean> programaNovo;

    // HERDADOS
    private GrupoBean grupoParaAdd;
    private ProgramaBean programaParaAdd;
    private Integer codUnidade;
    
    public TipoAtendimentoBean() {
        this.grupo = new ArrayList<GrupoBean>();
        this.grupoNovo = new ArrayList<GrupoBean>();
        this.descTipoAt = new String();
        this.equipe = null;
        this.primeiroAt = false;
        this.grupoParaAdd = new GrupoBean();
        this.idTipo = null;
        this.profissional = false;
        programaParaAdd = new ProgramaBean();
        listaPrograma = new ArrayList<>();

    }

    public List<GrupoBean> getGrupo() {
        return grupo;
    }

    public void setGrupo(List<GrupoBean> grupo) {
        this.grupo = grupo;
    }

    public String getDescTipoAt() {
        return descTipoAt;
    }

    public void setDescTipoAt(String descTipoAt) {
        this.descTipoAt = descTipoAt;
    }

    public boolean isPrimeiroAt() {
        return primeiroAt;
    }

    public void setPrimeiroAt(boolean primeiroAt) {
        this.primeiroAt = primeiroAt;
    }

    public Integer getIdTipo() {
        return idTipo;
    }

    public void setIdTipo(Integer idTipo) {
        this.idTipo = idTipo;
    }



    public GrupoBean getGrupoParaAdd() {
        return grupoParaAdd;
    }

    public void setGrupoParaAdd(GrupoBean grupoParaAdd) {
        this.grupoParaAdd = grupoParaAdd;
    }

    public void addGrupoLista() {
        boolean existe = false;
        if (grupo.size() == 0) {
            this.grupo.add(grupoParaAdd);
        } else {
            for (int i = 0; i < grupo.size(); i++) {
                if (grupo.get(i).getIdGrupo() == grupoParaAdd.getIdGrupo()) {
                    existe = true;
                }
            }
            if (existe == false) {
                this.grupo.add(grupoParaAdd);
            } else {
                JSFUtil.adicionarMensagemAdvertencia("Esse grupo já foi adicionado!", "Advertência");
            }
        }
    }

    public void rmvGrupoLista() {
        this.grupo.remove(grupoParaAdd);
    }

    public void rmvProgramaLista() {
        this.listaPrograma.remove(programaParaAdd);
    }

    public List<GrupoBean> getGrupoNovo() {
        this.grupoNovo = this.grupo;
        return grupoNovo;
    }

    public void setGrupoNovo(List<GrupoBean> grupoNovo) {
        this.grupoNovo = grupoNovo;
    }

    public Integer getIntervaloMinimo() {
        return intervaloMinimo;
    }

    public void setIntervaloMinimo(Integer intervaloMinimo) {
        this.intervaloMinimo = intervaloMinimo;
    }

    public Boolean getEquipe() {
        return equipe;
    }

    public void setEquipe(Boolean equipe) {
        this.equipe = equipe;
    }

    public Boolean getProfissional() {
        return profissional;
    }

    public void setProfissional(Boolean profissional) {
        this.profissional = profissional;
    }

    public List<ProgramaBean> getListaPrograma() {
        return listaPrograma;
    }

    public void setListaPrograma(List<ProgramaBean> listaPrograma) {
        this.listaPrograma = listaPrograma;
    }

    public List<ProgramaBean> getProgramaNovo() {
        return programaNovo;
    }

    public void setProgramaNovo(List<ProgramaBean> programaNovo) {
        this.programaNovo = programaNovo;
    }

    public ProgramaBean getProgramaParaAdd() {
        return programaParaAdd;
    }

    public void setProgramaParaAdd(ProgramaBean programaParaAdd) {
        this.programaParaAdd = programaParaAdd;
    }

    public void addProgramaLista() throws ProjetoException {
        boolean existe = false;
        TipoAtendimentoDAO tipoAtendimentoDAO = new TipoAtendimentoDAO();
        if (!VerificadorUtil.verificarSeObjetoNuloOuZero(tipoAtendimentoDAO.listarTipoDeAtendimentoDoPrograma(programaParaAdd.getIdPrograma()))) {
            JSFUtil.adicionarMensagemAdvertencia("Esse programa já está relacionado a outro tipo de atendimento!", "Advertência");
        } else {
            if (listaPrograma.size() == 0) {
                this.listaPrograma.add(programaParaAdd);
            } else {
                for (int i = 0; i < listaPrograma.size(); i++) {
                    if (listaPrograma.get(i).getIdPrograma() == programaParaAdd.getIdPrograma()) {
                        existe = true;
                    }
                }
                if (existe == false) {
                    this.listaPrograma.add(programaParaAdd);
                } else {
                    JSFUtil.adicionarMensagemAdvertencia("Esse programa já foi adicionado!", "Advertência");
                }
            }
        }
    }

	public Integer getCodUnidade() {
		return codUnidade;
	}

	public void setCodUnidade(Integer codUnidade) {
		this.codUnidade = codUnidade;
	}

	
}
