package br.gov.al.maceio.sishosp.hosp.control;

import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.JSFUtil;
import br.gov.al.maceio.sishosp.comum.util.RedirecionarUtil;
import br.gov.al.maceio.sishosp.hosp.dao.ParentescoDAO;
import br.gov.al.maceio.sishosp.hosp.model.Parentesco;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@ManagedBean(name = "ParentescoController")
@ViewScoped
public class ParentescoController implements Serializable {

    private static final long serialVersionUID = 1L;
    private Parentesco parentesco;
    private List<Parentesco> listaParentescos;
    private String cabecalho;
    private int tipo;
    private ParentescoDAO pDao = new ParentescoDAO();

    //CONSTANTES
    private static final String ENDERECO_CADASTRO = "cadastroParentesco?faces-redirect=true";
    private static final String ENDERECO_TIPO = "&amp;tipo=";
    private static final String ENDERECO_ID = "&amp;id=";
    private static final String CABECALHO_INCLUSAO = "Inclusão de Parentesco";
    private static final String CABECALHO_ALTERACAO = "Alteração de Parentesco";

    public ParentescoController() {
        parentesco = new Parentesco();
        listaParentescos = new ArrayList<>();
        listaParentescos = null;
    }

    public String redirectEdit() {
        return RedirecionarUtil.redirectEdit(ENDERECO_CADASTRO, ENDERECO_ID, this.parentesco.getCodParentesco(), ENDERECO_TIPO, tipo);
    }

    public String redirectInsert() {
        return RedirecionarUtil.redirectInsert(ENDERECO_CADASTRO, ENDERECO_TIPO, tipo);
    }

    public void getEditParentesco() throws ProjetoException {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Map<String, String> params = facesContext.getExternalContext()
                .getRequestParameterMap();
        if (params.get("id") != null) {
            Integer id = Integer.parseInt(params.get("id"));
            tipo = Integer.parseInt(params.get("tipo"));
            this.parentesco = pDao.buscaParentesCocodigo(id);
        } else {
            tipo = Integer.parseInt(params.get("tipo"));
        }

    }

    public void gravarParentesco() {

        boolean cadastrou = pDao.cadastrar(parentesco);

        if (cadastrou == true) {
            limparDados();
            JSFUtil.adicionarMensagemSucesso("Parentesco cadastrado com sucesso!", "Sucesso");
        } else {
            JSFUtil.adicionarMensagemErro("Ocorreu um erro durante o cadastro!", "Erro");
        }
    }

    public void alterarParentesco() {

        boolean alterou = pDao.alterar(parentesco);
        listaParentescos = null;
        if (alterou == true) {
            JSFUtil.adicionarMensagemSucesso("Parentesco alterado com sucesso!", "Sucesso");
        } else {
            JSFUtil.adicionarMensagemErro("Ocorreu um erro durante a alteração!", "Erro");
        }

    }

    public void excluirParentesco() throws ProjetoException {

        boolean excluiu = pDao.excluir(parentesco);

        if (excluiu == true) {
            JSFUtil.adicionarMensagemSucesso("Parentesco excluído com sucesso!", "Sucesso");
            JSFUtil.fecharDialog("dialogExclusao");
        } else {
            JSFUtil.adicionarMensagemErro("Ocorreu um erro durante a exclusão!", "Erro");
            JSFUtil.fecharDialog("dialogExclusao");
        }
        listarParentescos();
    }

    public void limparDados() {
        parentesco = new Parentesco();

    }

    public List<Parentesco> listarParentescos() throws ProjetoException {
        return pDao.listaParentescos();
    }

    public void buscarParentesco() throws ProjetoException {
        listaParentescos = pDao.listaParentescos();
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

    public Parentesco getParentesco() {
        return parentesco;
    }

    public void setParentesco(Parentesco parentesco) {
        this.parentesco = parentesco;
    }

    public List<Parentesco> getListaParentescos() {
        return listaParentescos;
    }

    public void setListaParentescos(List<Parentesco> listaParentescos) {
        this.listaParentescos = listaParentescos;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }
}
