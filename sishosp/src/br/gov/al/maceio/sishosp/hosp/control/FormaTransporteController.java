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
import br.gov.al.maceio.sishosp.hosp.dao.FormaTransporteDAO;
import br.gov.al.maceio.sishosp.hosp.model.FormaTransporteBean;

@ManagedBean(name = "FormaTransporteController")
@ViewScoped
public class FormaTransporteController implements Serializable {

    private static final long serialVersionUID = 1L;
    private FormaTransporteBean transporte;
    private String cabecalho;
    private Integer tipo;
    private List<FormaTransporteBean> listaFormaTransporte;
    private FormaTransporteDAO fDao = new FormaTransporteDAO();

    //CONSTANTES
    private static final String ENDERECO_CADASTRO = "cadastroFormaTransporte?faces-redirect=true";
    private static final String ENDERECO_TIPO = "&amp;tipo=";
    private static final String ENDERECO_ID = "&amp;id=";
    private static final String CABECALHO_INCLUSAO = "Inclusão de Forma de Transporte";
    private static final String CABECALHO_ALTERACAO = "Alteração de Forma de Transporte";

    public FormaTransporteController() {
        transporte = new FormaTransporteBean();
        listaFormaTransporte = new ArrayList<>();
    }

    public String redirectEdit() {
        return RedirecionarUtil.redirectEdit(ENDERECO_CADASTRO, ENDERECO_ID, this.transporte.getCodformatransporte(), ENDERECO_TIPO, tipo);
    }

    public String redirectInsert() {
        return RedirecionarUtil.redirectInsert(ENDERECO_CADASTRO, ENDERECO_TIPO, tipo);
    }

    public void getEditFormaTransporte() throws ProjetoException {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Map<String, String> params = facesContext.getExternalContext()
                .getRequestParameterMap();
        if (params.get("id") != null) {
            Integer id = Integer.parseInt(params.get("id"));
            tipo = Integer.parseInt(params.get("tipo"));

            this.transporte = fDao.buscatransportecodigo(id);
        } else {

            tipo = Integer.parseInt(params.get("tipo"));

        }
    }

    public void limparDados() {
        transporte = new FormaTransporteBean();
    }

    public void gravarTransporte() throws ProjetoException {
        boolean cadastrou = fDao.cadastrar(transporte);

        if (cadastrou == true) {
            limparDados();
            JSFUtil.adicionarMensagemSucesso("Forma de Transporte cadastrada com sucesso!", "Sucesso");
        } else {
            JSFUtil.adicionarMensagemErro("Ocorreu um erro durante o cadastro!", "Erro");
        }

    }

    public void alterarTransporte() throws ProjetoException {
        boolean alterou = fDao.alterar(transporte);

        if (alterou == true) {
            JSFUtil.adicionarMensagemSucesso("Forma de Transporte alterada com sucesso!", "Sucesso");
        } else {
            JSFUtil.adicionarMensagemErro("Ocorreu um erro durante a alteração!", "Erro");
        }
    }

    public void excluirTransporte() throws ProjetoException {

        boolean excluiu = fDao.excluir(transporte);

        if (excluiu == true) {
            JSFUtil.adicionarMensagemSucesso("Forma de Transporte excluída com sucesso!", "Sucesso");
            JSFUtil.fecharDialog("dialogExclusao");
            listarTransporte();
        } else {
            JSFUtil.adicionarMensagemErro("Ocorreu um erro durante a exclusão!", "Erro");
            JSFUtil.fecharDialog("dialogExclusao");
        }
        listarTransporte();
    }

    public void listarTransporte() throws ProjetoException {
        listaFormaTransporte = fDao.listaTransportes();
    }

    public void atualizarListaFormaTransporte() throws ProjetoException {
        JSFUtil.fecharDialog("dlgcadastroformatransporte");
        listaFormaTransporte = fDao.listaTransportes();
    }

    public FormaTransporteBean getTransporte() {
        return transporte;
    }

    public void setTransporte(FormaTransporteBean transporte) {
        this.transporte = transporte;
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

    public List<FormaTransporteBean> getListaFormaTransporte() {
        return listaFormaTransporte;
    }

    public void setListaFormaTransporte(List<FormaTransporteBean> listaFormaTransporte) {
        this.listaFormaTransporte = listaFormaTransporte;
    }
}
