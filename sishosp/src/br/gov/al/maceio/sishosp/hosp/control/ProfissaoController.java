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
import br.gov.al.maceio.sishosp.hosp.dao.ProfissaoDAO;
import br.gov.al.maceio.sishosp.hosp.model.ProfissaoBean;

@ManagedBean(name = "ProfissaoController")
@ViewScoped
public class ProfissaoController implements Serializable {

    private static final long serialVersionUID = 1L;
    private ProfissaoBean profissao;
    private String cabecalho;
    private Integer tipo;
    private List<ProfissaoBean> listaProfissoes;
    private ProfissaoDAO pDao = new ProfissaoDAO();

    //CONSTANTES
    private static final String ENDERECO_CADASTRO = "cadastroProfissoes?faces-redirect=true";
    private static final String ENDERECO_TIPO = "&amp;tipo=";
    private static final String ENDERECO_ID = "&amp;id=";
    private static final String CABECALHO_INCLUSAO = "Inclusão de Profissão";
    private static final String CABECALHO_ALTERACAO = "Alteração de Profissão";

    public ProfissaoController() {
        profissao = new ProfissaoBean();
        listaProfissoes = new ArrayList<>();
    }

    public String redirectEdit() {
        return RedirecionarUtil.redirectEdit(ENDERECO_CADASTRO, ENDERECO_ID, this.profissao.getCodprofissao(), ENDERECO_TIPO, tipo);
    }

    public String redirectInsert() {
        return RedirecionarUtil.redirectInsert(ENDERECO_CADASTRO, ENDERECO_TIPO, tipo);
    }

    public void getEditProfissao() throws ProjetoException {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Map<String, String> params = facesContext.getExternalContext()
                .getRequestParameterMap();
        if (params.get("id") != null) {
            ProfissaoDAO pDao = new ProfissaoDAO();
            Integer id = Integer.parseInt(params.get("id"));
            tipo = Integer.parseInt(params.get("tipo"));
            this.profissao = pDao.buscaprofissaocodigo(id);
        } else {
            tipo = Integer.parseInt(params.get("tipo"));

        }

    }

    public void gravarProfissao() throws ProjetoException {

        boolean cadastrou = pDao.cadastrar(profissao);
        if (cadastrou == true) {
            limparDados();
            JSFUtil.adicionarMensagemSucesso("Profissão cadastrada com sucesso!", "Sucesso");
        } else {
            JSFUtil.adicionarMensagemErro("Ocorreu um erro durante o cadastro!", "Erro");
        }

    }

    public void alterarProfissao() throws ProjetoException {

        boolean alterou = pDao.alterar(profissao);
        if (alterou == true) {
            JSFUtil.adicionarMensagemSucesso("Profissão alterada com sucesso!", "Sucesso");
        } else {
            JSFUtil.adicionarMensagemErro("Ocorreu um erro durante a alteração!", "Erro");
        }

    }

    public void excluirProfissao() throws ProjetoException {

        boolean excluiu = pDao.excluir(profissao);

        if (excluiu == true) {
            JSFUtil.adicionarMensagemSucesso("Profissão excluída com sucesso!", "Sucesso");
            JSFUtil.fecharDialog("dialogExclusao");
        } else {
            JSFUtil.adicionarMensagemErro("Ocorreu um erro durante a exclusão!", "Erro");
            JSFUtil.fecharDialog("dialogExclusao");
        }
        listarProfissoes();
    }

    public void limparDados() {
        profissao = new ProfissaoBean();

    }

    public void listarProfissoes() throws ProjetoException {
        listaProfissoes = pDao.listaProfissoes();

    }

    public void atualizarListaDeProfissoes() throws ProjetoException {
        JSFUtil.fecharDialog("dlgcadastroprofissao");
        listaProfissoes = pDao.listaProfissoes();

    }

    public ProfissaoBean getProfissao() {
        return profissao;
    }

    public void setProfissao(ProfissaoBean profissao) {
        this.profissao = profissao;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
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

    public List<ProfissaoBean> getListaProfissoes() {
        return listaProfissoes;
    }

    public void setListaProfissoes(List<ProfissaoBean> listaProfissoes) {
        this.listaProfissoes = listaProfissoes;
    }
}
