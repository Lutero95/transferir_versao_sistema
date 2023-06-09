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
import br.gov.al.maceio.sishosp.hosp.dao.EscolaDAO;
import br.gov.al.maceio.sishosp.hosp.model.EscolaBean;

@ManagedBean(name = "EscolaController")
@ViewScoped
public class EscolaController implements Serializable {

    private static final long serialVersionUID = 1L;
    private EscolaBean escola;
    private List<EscolaBean> listaTipoEscola;
    private List<EscolaBean> listaEscolas;
    private String cabecalho;
    private Integer tipo;
    private EscolaDAO eDao = new EscolaDAO();

    //CONSTANTES
    private static final String ENDERECO_CADASTRO = "cadastroEscola?faces-redirect=true";
    private static final String ENDERECO_TIPO = "&amp;tipo=";
    private static final String ENDERECO_ID = "&amp;id=";
    private static final String CABECALHO_INCLUSAO = "Inclusão de Escola";
    private static final String CABECALHO_ALTERACAO = "Alteração de Escola";

    public EscolaController() {
        escola = new EscolaBean();
        listaEscolas = new ArrayList<>();
        listaEscolas = null;
        listaTipoEscola = new ArrayList<>();
        listaTipoEscola = null;
    }

    public String redirectEdit() {
        return RedirecionarUtil.redirectEdit(ENDERECO_CADASTRO, ENDERECO_ID, this.escola.getCodEscola(), ENDERECO_TIPO, tipo);
    }

    public String redirectInsert() {
        return RedirecionarUtil.redirectInsert(ENDERECO_CADASTRO, ENDERECO_TIPO, tipo);
    }

    public void getEditEscola() throws ProjetoException {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Map<String, String> params = facesContext.getExternalContext()
                .getRequestParameterMap();
        if (params.get("id") != null) {
            Integer id = Integer.parseInt(params.get("id"));
            tipo = Integer.parseInt(params.get("tipo"));
            this.escola = eDao.buscaescolacodigo(id);
        } else {
            tipo = Integer.parseInt(params.get("tipo"));
        }

    }

    public void gravarEscola() throws ProjetoException {

        boolean cadastrou = eDao.cadastrar(escola);

        if (cadastrou == true) {
            limparDados();
            JSFUtil.adicionarMensagemSucesso("Escola cadastrada com sucesso!", "Sucesso");
        } else {
            JSFUtil.adicionarMensagemErro("Ocorreu um erro durante o cadastro!", "Erro");
        }
    }

    public void alterarEscola() throws ProjetoException {

        boolean alterou = eDao.alterar(escola);
        listaEscolas = null;
        if (alterou == true) {
            JSFUtil.adicionarMensagemSucesso("Escola alterada com sucesso!", "Sucesso");
        } else {
            JSFUtil.adicionarMensagemErro("Ocorreu um erro durante a alteração!", "Erro");
        }

    }

    public void excluirEscola() throws ProjetoException {

        boolean excluiu = eDao.excluir(escola);

        if (excluiu == true) {
            JSFUtil.adicionarMensagemSucesso("Escola excluída com sucesso!", "Sucesso");
            JSFUtil.fecharDialog("dialogExclusao");
        } else {
            JSFUtil.adicionarMensagemErro("Ocorreu um erro durante a exclusão!", "Erro");
            JSFUtil.fecharDialog("dialogExclusao");
        }
        buscarEscola();
    }

    public void limparDados() {
        escola = new EscolaBean();

    }

    public void buscarEscola() throws ProjetoException {
        listaEscolas = eDao.listaEscolas();
    }

    public void atualizarListaDeEscolas() throws ProjetoException {
        JSFUtil.fecharDialog("dlgcadastroescola");
        listaEscolas = eDao.listaEscolas();
    }


    public EscolaBean getEscola() {
        return escola;
    }

    public void setEscola(EscolaBean escola) {
        this.escola = escola;
    }

    public List<EscolaBean> getListaTipoEscola() {
        return listaTipoEscola;
    }

    public void setListaTipoEscola(List<EscolaBean> listaTipoEscola) {
        this.listaTipoEscola = listaTipoEscola;
    }

    public void setListaEscolas(List<EscolaBean> listaEscolas) {
        this.listaEscolas = listaEscolas;
    }

    public String getCabecalho() {
        if (this.tipo.equals(TipoCabecalho.INCLUSAO.getSigla())) {
            cabecalho = CABECALHO_INCLUSAO;
        } else if (this.tipo.equals(TipoCabecalho.ALTERACAO.getSigla())) {
            cabecalho = CABECALHO_ALTERACAO;
        }
        return cabecalho;
    }

    public void setCabecalho(String cabecalho) {
        this.cabecalho = cabecalho;
    }

    public List<EscolaBean> getListaEscolas() {
        return listaEscolas;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }
}
