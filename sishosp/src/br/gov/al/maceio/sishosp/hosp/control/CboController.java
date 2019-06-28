package br.gov.al.maceio.sishosp.hosp.control;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import br.gov.al.maceio.sishosp.comum.util.RedirecionarUtil;
import br.gov.al.maceio.sishosp.comum.util.JSFUtil;

import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.hosp.dao.CboDAO;
import br.gov.al.maceio.sishosp.hosp.model.CboBean;

@ManagedBean(name = "CboController")
@ViewScoped
public class CboController implements Serializable {

    private static final long serialVersionUID = 1L;
    private CboBean cbo;
    private int tipo;
    private String cabecalho;
    private CboDAO cDao = new CboDAO();
    private List<CboBean> listaCbo;

    //CONSTANTES
    private static final String ENDERECO_CADASTRO = "cadastroCbo?faces-redirect=true";
    private static final String ENDERECO_TIPO = "&amp;tipo=";
    private static final String ENDERECO_ID = "&amp;id=";
    private static final String CABECALHO_INCLUSAO = "Inclusão de CBO";
    private static final String CABECALHO_ALTERACAO = "Alteração de CBO";

    public CboController() {
        this.cbo = new CboBean();
        listaCbo = new ArrayList<>();
    }

    public String redirectEdit() {
        return RedirecionarUtil.redirectEdit(ENDERECO_CADASTRO, ENDERECO_ID, this.cbo.getCodCbo(), ENDERECO_TIPO, tipo);
    }

    public String redirectInsert() {
        return RedirecionarUtil.redirectInsert(ENDERECO_CADASTRO, ENDERECO_TIPO, tipo);
    }

    public void getEditCbo() throws ProjetoException {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Map<String, String> params = facesContext.getExternalContext()
                .getRequestParameterMap();
        if (params.get("id") != null) {
            Integer id = Integer.parseInt(params.get("id"));
            tipo = Integer.parseInt(params.get("tipo"));
            this.cbo = cDao.listarCboPorId(id);
        } else {
            tipo = Integer.parseInt(params.get("tipo"));

        }

    }

    public void limparDados() {
        cbo = new CboBean();

    }

    public List<CboBean> listaCboAutoComplete(String query)
            throws ProjetoException {
        List<CboBean> result = cDao.listarCboBusca(query);
        return result;
    }

    public void gravarCbo() {
        boolean cadastrou = cDao.gravarCBO(this.cbo);

        if (cadastrou == true) {
            limparDados();
            JSFUtil.adicionarMensagemSucesso("CBO cadastrado com sucesso!", "Sucesso");
        } else {
            JSFUtil.adicionarMensagemErro("Ocorreu um erro durante o cadastro", "Erro");
        }
    }

    public void alterarCbo() {
        boolean alterou = cDao.alterarCbo(cbo);
        if (alterou == true) {
            JSFUtil.adicionarMensagemSucesso("CBO alterado com sucesso!", "Sucesso");
        } else {
            JSFUtil.adicionarMensagemErro("Ocorreu um erro durante a alteração", "Erro");
        }
    }

    public void excluirCbo() throws ProjetoException {
        boolean excluiu = cDao.excluirCbo(cbo);
        if (excluiu == true) {
            JSFUtil.adicionarMensagemSucesso("CBO excluído com sucesso!", "Sucesso");
            JSFUtil.fecharDialog("dlgExclusao");
            listarCbos();
        } else {
            JSFUtil.adicionarMensagemErro("Ocorreu um erro durante a exclusão", "Erro");
            JSFUtil.fecharDialog("dlgExclusao");
        }
    }

    public void listarCbos() throws ProjetoException {
        listaCbo = cDao.listarCbo();
    }

    public CboBean getCbo() {
        return cbo;
    }

    public void setCbo(CboBean cbo) {
        this.cbo = cbo;
    }

    public String getCabecalho() {
        if (this.tipo == 1) {
            cabecalho = CABECALHO_INCLUSAO;
        } else if (this.tipo == 2) {
            cabecalho = CABECALHO_ALTERACAO;
        }
        return cabecalho;
    }

    public void setCabecalho(String cabecalho) {
        this.cabecalho = cabecalho;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public List<CboBean> getListaCbo() {
        return listaCbo;
    }

    public void setListaCbo(List<CboBean> listaCbo) {
        this.listaCbo = listaCbo;
    }
}
