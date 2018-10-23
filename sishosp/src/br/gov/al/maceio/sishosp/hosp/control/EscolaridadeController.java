package br.gov.al.maceio.sishosp.hosp.control;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import br.gov.al.maceio.sishosp.comum.util.JSFUtil;

import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.RedirecionarUtil;
import br.gov.al.maceio.sishosp.hosp.dao.EscolaridadeDAO;
import br.gov.al.maceio.sishosp.hosp.model.EscolaridadeBean;

@ManagedBean(name = "EscolaridadeController")
@ViewScoped
public class EscolaridadeController implements Serializable {

    private static final long serialVersionUID = 1L;
    private EscolaridadeBean escolaridade;
    private String cabecalho;
    private int tipo;
    private List<EscolaridadeBean> listaEscolaridade;
    private EscolaridadeDAO eDao = new EscolaridadeDAO();

    //CONSTANTES
    private static final String ENDERECO_CADASTRO = "cadastroEscolaridade?faces-redirect=true";
    private static final String ENDERECO_TIPO = "&amp;tipo=";
    private static final String ENDERECO_ID = "&amp;id=";
    private static final String CABECALHO_INCLUSAO = "Inclusão de Escolaridade";
    private static final String CABECALHO_ALTERACAO = "Alteração de Escolaridade";

    public EscolaridadeController() {
        escolaridade = new EscolaridadeBean();
        listaEscolaridade = new ArrayList<>();
    }

    public String redirectEdit() {
        return RedirecionarUtil.redirectEdit(ENDERECO_CADASTRO, ENDERECO_ID, this.escolaridade.getCodescolaridade(), ENDERECO_TIPO, tipo);
    }

    public String redirectInsert() {
        return RedirecionarUtil.redirectInsert(ENDERECO_CADASTRO, ENDERECO_TIPO, tipo);
    }

    public void getEditEscolaridade() throws ProjetoException {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Map<String, String> params = facesContext.getExternalContext()
                .getRequestParameterMap();
        if (params.get("id") != null) {
            Integer id = Integer.parseInt(params.get("id"));
            tipo = Integer.parseInt(params.get("tipo"));

            this.escolaridade = eDao.buscaescolaridadecodigo(id);
        } else {

            tipo = Integer.parseInt(params.get("tipo"));

        }

    }

    public void limparDados() {
        escolaridade = new EscolaridadeBean();
    }

    public void gravarEscolaridade() {

        boolean cadastrou = eDao.cadastrar(escolaridade);

        if (cadastrou == true) {
            limparDados();
            JSFUtil.adicionarMensagemSucesso("Escolaridade cadastrada com sucesso!", "Sucesso");
        } else {
            JSFUtil.adicionarMensagemErro("Ocorreu um erro durante o cadastro!", "Erro");
        }

    }

    public void alterarEscolaridade() {

        boolean alterou = eDao.alterar(escolaridade);

        if (alterou == true) {
            JSFUtil.adicionarMensagemSucesso("Escolaridade alterada com sucesso!", "Sucesso");
        } else {
            JSFUtil.adicionarMensagemErro("Ocorreu um erro durante a alteração!", "Erro");
        }

    }

    public void excluirEscolaridade() throws ProjetoException {

        boolean excluiu = eDao.excluir(escolaridade);

        if (excluiu == true) {
            JSFUtil.adicionarMensagemSucesso("Escolaridade excluída com sucesso!", "Sucesso");
            JSFUtil.fecharDialog("dialogExclusao");
            listaEscolaridade = null;
            buscarEscolaridades();
        } else {
            JSFUtil.adicionarMensagemErro("Ocorreu um erro durante a exclusão!", "Erro");
            JSFUtil.fecharDialog("dialogExclusao");
        }
        listarEscolaridades();
    }

    public List<EscolaridadeBean> listarEscolaridades() throws ProjetoException {
        listaEscolaridade = eDao.listaEscolaridade();

        return listaEscolaridade;
    }

    public void buscarEscolaridades() throws ProjetoException {
        listaEscolaridade = eDao.listaEscolaridade();
    }

    public EscolaridadeBean getEscolaridade() {
        return escolaridade;
    }

    public void setEscolaridade(EscolaridadeBean escolaridade) {
        this.escolaridade = escolaridade;
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

}
