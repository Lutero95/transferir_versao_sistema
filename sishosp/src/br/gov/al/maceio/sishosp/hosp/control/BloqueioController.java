package br.gov.al.maceio.sishosp.hosp.control;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import br.gov.al.maceio.sishosp.comum.util.JSFUtil;
import br.gov.al.maceio.sishosp.comum.util.RedirecionarUtil;

import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.hosp.dao.BloqueioDAO;
import br.gov.al.maceio.sishosp.hosp.model.BloqueioBean;

@ManagedBean(name = "BloqueioController")
@ViewScoped
public class BloqueioController implements Serializable {

    private static final long serialVersionUID = 1L;
    private BloqueioBean bloqueio;
    private List<BloqueioBean> listaBloqueio;
    private BloqueioDAO bDao = new BloqueioDAO();

    //CONSTANTES
    private static final String ENDERECO_CADASTRO = "bloqueio?faces-redirect=true";
    private static final String ENDERECO_ALTERACAO = "editarBloqueio?faces-redirect=true";
    private static final String ENDERECO_ID = "&amp;id=";

    public BloqueioController() {
        this.bloqueio = new BloqueioBean();
    }

    public void limparDados() {
        this.bloqueio = new BloqueioBean();
    }

    public String redirectEdit() {
        return RedirecionarUtil.redirectEditSemTipo(ENDERECO_ALTERACAO, ENDERECO_ID, this.bloqueio.getIdBloqueio());
    }

    public String redirectInsert() {
        return RedirecionarUtil.redirectInsertSemTipo(ENDERECO_CADASTRO);
    }


    public void getEditBloqueio() throws ProjetoException {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Map<String, String> params = facesContext.getExternalContext()
                .getRequestParameterMap();
        if (params.get("id") != null) {
            Integer id = Integer.parseInt(params.get("id"));
            this.bloqueio = bDao.listarBloqueioPorId(id);
        } else {
        }

    }

    public Boolean validarBloqueio(){
        if (bloqueio.getDataInicio().after(bloqueio.getDataFim()) == false) {
            return true;
        }
        else{
            return false;
        }
    }

    public void gravarBloqueio() throws ProjetoException {

        if(validarBloqueio()) {
            Boolean cadastrou = bDao.gravarBloqueioInicio(bloqueio);

            if (cadastrou == true) {
                limparDados();
                JSFUtil.adicionarMensagemSucesso("Bloqueio cadastrado com sucesso!", "Sucesso");
            } else {
                JSFUtil.adicionarMensagemErro("Ocorreu um erro durante o cadastro!", "Erro");
            }
        }
        else{
            JSFUtil.adicionarMensagemErro("Data inicial tem que ser anterior a data final!", "Erro");
        }
    }

    public void alterarBloqueio() throws ProjetoException {

        Boolean alterou = bDao.alterarBloqueio(bloqueio);

        if (alterou == true) {
            JSFUtil.adicionarMensagemSucesso("Bloqueio alterado com sucesso!", "Sucesso");
        } else {
            JSFUtil.adicionarMensagemErro("Ocorreu um erro durante a alteração!", "Erro");
        }
    }

    public void excluirBloqueio() throws ProjetoException {

        Boolean excluiu = bDao.excluirBloqueio(bloqueio);

        if (excluiu == true) {
            JSFUtil.adicionarMensagemSucesso("Bloqueio excluído com sucesso!", "Sucesso");
            JSFUtil.fecharDialog("dialogExclusao");
        } else {
            JSFUtil.adicionarMensagemErro("Ocorreu um erro durante a exclusão!", "Erro");
            JSFUtil.fecharDialog("dialogExclusao");
        }
        listarBloqueios();
    }

    public void listarBloqueios() throws ProjetoException {
        listaBloqueio = bDao.listarBloqueio();
    }

    public BloqueioBean getBloqueio() {
        return bloqueio;
    }

    public void setBloqueio(BloqueioBean bloqueio) {
        this.bloqueio = bloqueio;
    }

    public List<BloqueioBean> getListaBloqueio() {
        return listaBloqueio;
    }

    public void setListaBloqueio(List<BloqueioBean> listaBloqueio) {
        this.listaBloqueio = listaBloqueio;
    }
}