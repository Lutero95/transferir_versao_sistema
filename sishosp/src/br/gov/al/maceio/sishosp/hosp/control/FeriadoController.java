package br.gov.al.maceio.sishosp.hosp.control;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import br.gov.al.maceio.sishosp.comum.enums.TipoCabecalho;
import br.gov.al.maceio.sishosp.comum.util.JSFUtil;
import br.gov.al.maceio.sishosp.comum.util.RedirecionarUtil;

import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.hosp.dao.FeriadoDAO;
import br.gov.al.maceio.sishosp.hosp.model.FeriadoBean;

@ManagedBean(name = "FeriadoController")
@ViewScoped
public class FeriadoController implements Serializable {

    private static final long serialVersionUID = 1L;
    private FeriadoBean feriado;
    private List<FeriadoBean> listaFeriados;
    private Integer tipo;
    private String cabecalho;
    private FeriadoDAO fDao = new FeriadoDAO();

    //CONSTANTES
    private static final String ENDERECO_CADASTRO = "cadastroFeriado?faces-redirect=true";
    private static final String ENDERECO_TIPO = "&amp;tipo=";
    private static final String ENDERECO_ID = "&amp;id=";
    private static final String CABECALHO_INCLUSAO = "Inclusão de Feriado";
    private static final String CABECALHO_ALTERACAO = "Alteração de Feriado";

    public FeriadoController() {
        this.feriado = new FeriadoBean();
        this.listaFeriados = null;
    }

    public void limparDados() {
        this.feriado = new FeriadoBean();
        this.listaFeriados = null;
    }

    public String redirectEdit() {
        return RedirecionarUtil.redirectEdit(ENDERECO_CADASTRO, ENDERECO_ID, this.feriado.getCodFeriado(), ENDERECO_TIPO, tipo);
    }

    public String redirectInsert() {
        return RedirecionarUtil.redirectInsert(ENDERECO_CADASTRO, ENDERECO_TIPO, tipo);
    }

    public void getEditFeriado() throws ProjetoException {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Map<String, String> params = facesContext.getExternalContext()
                .getRequestParameterMap();
        if (params.get("id") != null) {
            Integer id = Integer.parseInt(params.get("id"));
            tipo = Integer.parseInt(params.get("tipo"));
            this.feriado = fDao.listarFeriadoPorId(id);
        } else {
            tipo = Integer.parseInt(params.get("tipo"));
        }

    }

    public void gravarFeriado() {
        boolean cadastrou = fDao.gravarFeriado(feriado);

        if (cadastrou == true) {
            limparDados();
            JSFUtil.adicionarMensagemSucesso("Feriado cadastrado com sucesso!", "Sucesso");
        } else {
            JSFUtil.adicionarMensagemErro("Ocorreu um erro durante o cadastro!", "Erro");
        }
    }

    public void alterarFeriado() {
        boolean alterou = fDao.alterarFeriado(feriado);
        if (alterou == true) {
            JSFUtil.adicionarMensagemSucesso("Feriado alterado com sucesso!", "Sucesso");
        } else {
            JSFUtil.adicionarMensagemErro("Ocorreu um erro durante a alteração!", "Erro");
        }

    }

    public void excluirFeriado() throws ProjetoException {
        boolean excluiu = fDao.excluirFeriado(feriado);
        if (excluiu == true) {
            JSFUtil.adicionarMensagemSucesso("Feriado excluído com sucesso!", "Sucesso");
            JSFUtil.fecharDialog("dialogExclusao");
        } else {
            JSFUtil.adicionarMensagemErro("Ocorreu um erro durante a exclusão!", "Erro");
            JSFUtil.fecharDialog("dialogExclusao");
        }
       listarFeriados();
    }

    public void listarFeriados() throws ProjetoException {
        this.listaFeriados = fDao.listarFeriado();
    }

    public FeriadoBean getFeriado() {
        return feriado;
    }

    public void setFeriado(FeriadoBean feriado) {
        this.feriado = feriado;
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

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public List<FeriadoBean> getListaFeriados() {
        return listaFeriados;
    }

    public void setListaFeriados(List<FeriadoBean> listaFeriados) {
        this.listaFeriados = listaFeriados;
    }
}
