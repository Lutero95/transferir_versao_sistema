package br.gov.al.maceio.sishosp.hosp.control;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import br.gov.al.maceio.sishosp.comum.util.JSFUtil;
import br.gov.al.maceio.sishosp.comum.util.RedirecionarUtil;

import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.hosp.dao.RecursoDAO;
import br.gov.al.maceio.sishosp.hosp.model.RecursoBean;

@ManagedBean(name = "RecursoController")
@ViewScoped
public class RecursoController implements Serializable {

    private RecursoBean recurso;
    private int tipo;
    private String cabecalho;
    private RecursoDAO rDao = new RecursoDAO();

    //CONSTANTES
    private static final String ENDERECO_CADASTRO = "cadastroRecurso?faces-redirect=true";
    private static final String ENDERECO_TIPO = "&amp;tipo=";
    private static final String ENDERECO_ID = "&amp;id=";
    private static final String CABECALHO_INCLUSAO = "Inclusão de Recurso";
    private static final String CABECALHO_ALTERACAO = "Alteração de Recurso";

    public RecursoController() {
        recurso = new RecursoBean();
    }

    public String redirectEdit() {
        return RedirecionarUtil.redirectEdit(ENDERECO_CADASTRO, ENDERECO_ID, this.recurso.getIdRecurso(), ENDERECO_TIPO, tipo);
    }

    public String redirectInsert() {
        return RedirecionarUtil.redirectInsert(ENDERECO_CADASTRO, ENDERECO_TIPO, tipo);
    }

    public void getEditRecurso() throws ProjetoException {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Map<String, String> params = facesContext.getExternalContext()
                .getRequestParameterMap();
        if (params.get("id") != null) {
            Integer id = Integer.parseInt(params.get("id"));
            tipo = Integer.parseInt(params.get("tipo"));

            this.recurso = rDao.buscaRecursoCodigo(id);
        } else {

            tipo = Integer.parseInt(params.get("tipo"));

        }

    }

    public void gravarRecurso() throws ProjetoException {

        boolean cadastrou = rDao.cadastrar(recurso);

        if (cadastrou == true) {
            limparDados();
            JSFUtil.adicionarMensagemSucesso("Recurso cadastrado com sucesso!", "Sucesso");
        } else {
            JSFUtil.adicionarMensagemErro("Ocorreu um erro durante o cadastro!", "Erro");
        }
    }

    public void alterarRecurso() throws ProjetoException {

        boolean alterou = rDao.alterar(recurso);
        if (alterou == true) {
            JSFUtil.adicionarMensagemSucesso("Recurso alterado com sucesso!", "Sucesso");
        } else {
            JSFUtil.adicionarMensagemErro("Ocorreu um erro durante a alteração!", "Erro");
        }

    }

    public void excluirRecurso() throws ProjetoException {

        boolean excluir = rDao.excluir(recurso);

        if (excluir == true) {
            JSFUtil.adicionarMensagemSucesso("Recurso excluído com sucesso!", "Sucesso");
            JSFUtil.fecharDialog("dialogExclusao");
            listarRecursos();
        } else {
            JSFUtil.adicionarMensagemErro("Ocorreu um erro durante a exclusão!", "Erro");
            JSFUtil.fecharDialog("dialogExclusao");
        }
    }

    public void limparDados() {
        recurso = new RecursoBean();

    }

    public List<RecursoBean> listarRecursos() throws ProjetoException {
        return rDao.listaRecursos();
    }

    public ArrayList<RecursoBean> listarRecurso(Integer id_proc) throws ProjetoException {
    	System.out.println("listarRecurso");
        if (id_proc != null) {
            return rDao.listaRecursosPorProcedimento(id_proc);
        } else {
            return null;
        }
    }

    public String getCabecalho() {
        if (this.tipo == 1) {
            cabecalho = CABECALHO_INCLUSAO;
        } else if (this.tipo == 2) {
            cabecalho = CABECALHO_ALTERACAO;
        }
        return cabecalho;
    }

    public List<RecursoBean> listaRecursoAutoComplete(String query)
            throws ProjetoException {
        return rDao.buscaRecursoAutoComplete(query);
    }

    public void setCabecalho(String cabecalho) {
        this.cabecalho = cabecalho;
    }

    public RecursoBean getRecurso() {
        return recurso;
    }

    public void setRecurso(RecursoBean recurso) {
        this.recurso = recurso;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

}
