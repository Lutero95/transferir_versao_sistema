package br.gov.al.maceio.sishosp.hosp.control;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import br.gov.al.maceio.sishosp.comum.enums.TipoCabecalho;
import br.gov.al.maceio.sishosp.comum.util.JSFUtil;
import br.gov.al.maceio.sishosp.comum.util.RedirecionarUtil;

import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.hosp.dao.GrupoDAO;
import br.gov.al.maceio.sishosp.hosp.dao.ProgramaDAO;
import br.gov.al.maceio.sishosp.hosp.model.GrupoBean;
import br.gov.al.maceio.sishosp.hosp.model.ProgramaBean;

@ManagedBean(name = "GrupoController")
@ViewScoped
public class GrupoController implements Serializable {

    private static final long serialVersionUID = 1L;
    private GrupoBean grupo;
    private ProgramaBean prog;
    private List<GrupoBean> listaGrupos;
    private List<GrupoBean> listaGruposProgramas;
    private List<ProgramaBean> listaProgramasEGrupos;
    private Integer tipo;
    private String cabecalho;
    private ProgramaBean programaSelecionado;
    private GrupoDAO gDao = new GrupoDAO();

    //CONSTANTES
    private static final String ENDERECO_CADASTRO = "cadastroGrupo?faces-redirect=true";
    private static final String ENDERECO_TIPO = "&amp;tipo=";
    private static final String ENDERECO_ID = "&amp;id=";
    private static final String CABECALHO_INCLUSAO = "Inclusão de Grupo";
    private static final String CABECALHO_ALTERACAO = "Alteração de Grupo";

    public GrupoController() {
        this.grupo = new GrupoBean();
        this.listaGrupos = null;
        this.listaGruposProgramas = new ArrayList<>();
        this.listaProgramasEGrupos = new ArrayList<ProgramaBean>();
        this.cabecalho = "";
        this.programaSelecionado = new ProgramaBean();
    }

    public void limparDados() throws ProjetoException {
        this.grupo = new GrupoBean();
        this.listaGrupos = new ArrayList<>();
        this.listaGruposProgramas = new ArrayList<>();
        listaGrupos = gDao.listarGrupos();
        this.programaSelecionado = new ProgramaBean();
    }

    public String redirectEdit() {
        return RedirecionarUtil.redirectEdit(ENDERECO_CADASTRO, ENDERECO_ID, this.grupo.getIdGrupo(), ENDERECO_TIPO, tipo);
    }

    public String redirectInsert() {
        return RedirecionarUtil.redirectInsert(ENDERECO_CADASTRO, ENDERECO_TIPO, tipo);
    }


    public void getEditGrupo() throws ProjetoException {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Map<String, String> params = facesContext.getExternalContext()
                .getRequestParameterMap();
        if (params.get("id") != null) {
            Integer id = Integer.parseInt(params.get("id"));
            tipo = Integer.parseInt(params.get("tipo"));

            this.grupo = gDao.listarGrupoPorId(id);
        } else {

            tipo = Integer.parseInt(params.get("tipo"));

        }

    }

    public void gravarGrupo() throws ProjetoException {
/*
        if (grupo.getEquipes().isEmpty()) {
            JSFUtil.adicionarMensagemAdvertencia("Escolha ao menos 1 equipe!", "Advertência");
        } else 
        */
        {
            boolean cadastrou = gDao.gravarGrupo(grupo);

            if (cadastrou == true) {
                limparDados();
                JSFUtil.adicionarMensagemSucesso("Grupo cadastrado com sucesso!", "Sucesso");
            } else {
                JSFUtil.adicionarMensagemErro("Ocorreu um erro durante o cadastro!", "Erro");
            }
        }

    }

    public void alterarGrupo() {
        /*
    	if (grupo.getEquipes().isEmpty()) {
            JSFUtil.adicionarMensagemAdvertencia("Escolha ao menos 1 equipe!", "Advertência");
        } else
        */
        {
            boolean alterou = gDao.alterarGrupo(grupo);
            if (alterou == true) {
                JSFUtil.adicionarMensagemSucesso("Grupo alterado com sucesso!", "Sucesso");
            } else {
                JSFUtil.adicionarMensagemErro("Ocorreu um erro durante a alteração!", "Erro");
            }
        }
    }

    public void excluirGrupo() throws ProjetoException {

        boolean excluiu = gDao.excluirGrupo(grupo);
        if (excluiu == true) {
            JSFUtil.adicionarMensagemSucesso("Grupo excluído com sucesso!", "Sucesso");
            JSFUtil.fecharDialog("dialogExclusao");
        } else {
            JSFUtil.adicionarMensagemErro("Ocorreu um erro durante a exclusão!", "Erro");
            JSFUtil.fecharDialog("dialogExclusao");
        }
        listaGrupos = gDao.listarGrupos();
    }

    public void listaTodosGrupos() throws ProjetoException {
        listaGrupos = gDao.listarGrupos();

    }

    public void listarGrupos() throws ProjetoException {
        listaGrupos = gDao.listarGrupos();
    }

    public void listarProgramasEGrupos() throws ProjetoException {
        ProgramaDAO pDao = new ProgramaDAO();
        listaProgramasEGrupos = pDao.listarProgramasEGrupos();

    }

    public void listarProgramasEGruposPorUnidade(int codigoUnidade) throws ProjetoException {
        ProgramaDAO pDao = new ProgramaDAO();
        listaProgramasEGrupos = pDao.listarProgramasEGruposPorUnidade(codigoUnidade);

    }

    public GrupoBean getGrupo() {
        return grupo;
    }

    public void setGrupo(GrupoBean grupo) {
        this.grupo = grupo;
    }

    public void setListaGrupos(List<GrupoBean> listaGrupos) {
        this.listaGrupos = listaGrupos;
    }

    public List<GrupoBean> getListaGruposProgramas() {
        return listaGruposProgramas;
    }

    public void setListaGruposProgramas(List<GrupoBean> listaGruposProgramas) {
        this.listaGruposProgramas = listaGruposProgramas;
    }

    public String getCabecalho() {
        if (this.tipo.equals(TipoCabecalho.INCLUSAO.getSigla())) {
            cabecalho = CABECALHO_INCLUSAO;
        } else if (this.tipo.equals(TipoCabecalho.ALTERACAO.getSigla())) {
            cabecalho = CABECALHO_ALTERACAO;
        }
        return cabecalho;
    }

    public List<GrupoBean> listaGrupoAutoComplete(String query)
            throws ProjetoException {

        if (programaSelecionado.getIdPrograma() != null) {
            return gDao.listarGruposNoAutoComplete(query,
                    this.programaSelecionado.getIdPrograma());
        } else {
            return null;
        }

    }

    public List<GrupoBean> listaGrupoGeralAutoComplete(String query)
            throws ProjetoException {

            return gDao.listarGruposGeralAutoComplete(query);

    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public List<GrupoBean> getListaGrupos() {
        return listaGrupos;
    }

    public ProgramaBean getProg() {
        return prog;
    }

    public void setProg(ProgramaBean prog) {
        this.prog = prog;
    }

	public List<ProgramaBean> getListaProgramasEGrupos() {
		return listaProgramasEGrupos;
	}

	public void setListaProgramasEGrupos(List<ProgramaBean> listaProgramasEGrupos) {
		this.listaProgramasEGrupos = listaProgramasEGrupos;
	}

}
