package br.gov.al.maceio.sishosp.hosp.control;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import br.gov.al.maceio.sishosp.comum.util.JSFUtil;
import br.gov.al.maceio.sishosp.comum.util.RedirecionarUtil;

import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.hosp.dao.EspecialidadeDAO;
import br.gov.al.maceio.sishosp.hosp.model.EspecialidadeBean;

@ManagedBean(name = "EspecialidadeController")
@ViewScoped
public class EspecialidadeController implements Serializable {

    private static final long serialVersionUID = 1L;
    private EspecialidadeBean espec;
    private List<EspecialidadeBean> listaEspecialidade;
    private int tipo;
    private String cabecalho;
    private EspecialidadeDAO eDao = new EspecialidadeDAO();

    //CONSTANTES
    private static final String ENDERECO_CADASTRO = "cadastroEspecialidade?faces-redirect=true";
    private static final String ENDERECO_TIPO = "&amp;tipo=";
    private static final String ENDERECO_ID = "&amp;id=";
    private static final String CABECALHO_INCLUSAO = "Inclusão de Especialidade";
    private static final String CABECALHO_ALTERACAO = "Alteração de Especialidade";

    public EspecialidadeController() {
        this.espec = new EspecialidadeBean();
    }

    public String redirectEdit() {
        return RedirecionarUtil.redirectEdit(ENDERECO_CADASTRO, ENDERECO_ID, this.espec.getCodEspecialidade(), ENDERECO_TIPO, tipo);
    }

    public String redirectInsert() {
        return RedirecionarUtil.redirectInsert(ENDERECO_CADASTRO, ENDERECO_TIPO, tipo);
    }

    public void getEditEspecialidade() throws ProjetoException {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Map<String, String> params = facesContext.getExternalContext()
                .getRequestParameterMap();
        if (params.get("id") != null) {
            Integer id = Integer.parseInt(params.get("id"));
            tipo = Integer.parseInt(params.get("tipo"));

            this.espec = eDao.listarEspecialidadePorId((id));
        } else {

            tipo = Integer.parseInt(params.get("tipo"));

        }

    }

    public void limparDados() throws ProjetoException {
        espec = new EspecialidadeBean();
        this.listaEspecialidade = eDao.listarEspecialidades();
    }

    public void gravarEspecialidade() throws ProjetoException {

        boolean cadastrou = eDao.gravarEspecialidade(espec);

        if (cadastrou == true) {
            limparDados();
            JSFUtil.adicionarMensagemSucesso("Especialidade cadastrado com sucesso!", "Sucesso");
        } else {
            JSFUtil.adicionarMensagemErro("Ocorreu um erro durante o cadastro!", "Erro");
        }
    }

    public void alterarEspecialidade() {
        boolean alterou = eDao.alterarEspecialidade(espec);
        if (alterou == true) {
            JSFUtil.adicionarMensagemSucesso("Especialidade alterada com sucesso!", "Sucesso");
        } else {
            JSFUtil.adicionarMensagemErro("Ocorreu um erro durante a alteração!", "Erro");
        }
    }

    public void excluirEspecialidade() throws ProjetoException {
        boolean excluiu = eDao.excluirEspecialidade(espec);
        if (excluiu == true) {
            JSFUtil.adicionarMensagemSucesso("Especialidade excluída com sucesso!", "Sucesso");
            JSFUtil.fecharDialog("dialogExclusao");
        } else {
            JSFUtil.adicionarMensagemErro("Ocorreu um erro durante a exclusão!", "Erro");
            JSFUtil.fecharDialog("dialogExclusao");
        }
        listarEspecialidades();
    }

    public void listarTodasEspecialidades() throws ProjetoException {
        this.listaEspecialidade = eDao.listarEspecialidades();
    }

    public List<EspecialidadeBean> listarEspecialidades()
            throws ProjetoException {
        this.listaEspecialidade = eDao.listarEspecialidades();
        return listaEspecialidade;
    }

    public List<EspecialidadeBean> listaEspecialidadeAutoComplete(String query)
            throws ProjetoException {
        List<EspecialidadeBean> result = eDao.listarEspecialidadesBusca(query,
                1);
        return result;
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

    public List<EspecialidadeBean> getListaEspecialidade() {
        return listaEspecialidade;
    }

    public EspecialidadeBean getEspec() {
        return espec;
    }

    public void setEspec(EspecialidadeBean espec) {
        this.espec = espec;
    }

    public void setListaEspecialidade(List<EspecialidadeBean> listaEspecialidade) {
        this.listaEspecialidade = listaEspecialidade;
    }
}
