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
import br.gov.al.maceio.sishosp.hosp.dao.EncaminhadoDAO;
import br.gov.al.maceio.sishosp.hosp.model.EncaminhadoBean;

@ManagedBean(name = "EncaminhadoController")
@ViewScoped
public class EncaminhadoController implements Serializable {

    private static final long serialVersionUID = 1L;
    private EncaminhadoBean encaminhado;
    private Integer tipo;
    private String cabecalho;
    private List<EncaminhadoBean> listaTiposEncaminhamento;
    private EncaminhadoDAO eDao = new EncaminhadoDAO();

    //CONSTANTES
    private static final String ENDERECO_CADASTRO = "cadastroTipoEncaminhamento?faces-redirect=true";
    private static final String ENDERECO_TIPO = "&amp;tipo=";
    private static final String ENDERECO_ID = "&amp;id=";
    private static final String CABECALHO_INCLUSAO = "Inclusão de Tipo de Encaminhamento";
    private static final String CABECALHO_ALTERACAO = "Alteração de Encaminhamento";

    public EncaminhadoController() {
        encaminhado = new EncaminhadoBean();
        listaTiposEncaminhamento = new ArrayList<>();
    }

    public void limparDados() {
        encaminhado = new EncaminhadoBean();
    }

    public String redirectEdit() {
        return RedirecionarUtil.redirectEdit(ENDERECO_CADASTRO, ENDERECO_ID, this.encaminhado.getCodencaminhado(), ENDERECO_TIPO, tipo);
    }

    public String redirectInsert() {
        return RedirecionarUtil.redirectInsert(ENDERECO_CADASTRO, ENDERECO_TIPO, tipo);
    }

    public void getEditTipoEncaminhamento() throws ProjetoException {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Map<String, String> params = facesContext.getExternalContext()
                .getRequestParameterMap();
        if (params.get("id") != null) {
            Integer id = Integer.parseInt(params.get("id"));
            tipo = Integer.parseInt(params.get("tipo"));
            this.encaminhado = eDao.buscaencaminhadocodigo(id);
        } else {
            tipo = Integer.parseInt(params.get("tipo"));

        }

    }

    public void gravarEncaminhado() throws ProjetoException {

        boolean cadastrou = eDao.cadastrar(encaminhado);

        if (cadastrou == true) {
        	encaminhado = new EncaminhadoBean();
            JSFUtil.adicionarMensagemSucesso("Tipo de Encaminhamento cadastrado com sucesso!", "Sucesso");
        } else {
            JSFUtil.adicionarMensagemErro("Ocorreu um erro durante o cadastro!", "Erro");
        }

    }

    public void alterarEncaminhado() throws ProjetoException {

        boolean alterou = eDao.alterar(encaminhado);

        if (alterou == true) {
            JSFUtil.adicionarMensagemSucesso("Tipo de Encaminhamento alterado com sucesso!", "Sucesso");
        } else {
            JSFUtil.adicionarMensagemErro("Ocorreu um erro durante a alteração!", "Erro");

        }
    }

    public void excluirEncaminhado() throws ProjetoException {

        boolean excluiu = eDao.excluir(encaminhado);

        if (excluiu == true) {
        	listarTiposDeEncaminhamento();
            JSFUtil.adicionarMensagemSucesso("Tipo de Encaminhamento excluído com sucesso!", "Sucesso");
            JSFUtil.fecharDialog("dialogExclusao");
        } else {
            JSFUtil.adicionarMensagemErro("Ocorreu um erro durante a exclusão!", "Erro");
            JSFUtil.fecharDialog("dialogExclusao");
        }
    }

    public void listarTiposDeEncaminhamento() throws ProjetoException {
        listaTiposEncaminhamento = eDao.listaEncaminhados();
    }

    public void atualizarListaDeTiposDeEncaminhamento() throws ProjetoException {
        JSFUtil.fecharDialog("dlgcadastrotipoencaminhamento");
        listaTiposEncaminhamento = eDao.listaEncaminhados();
    }

    public EncaminhadoBean getEncaminhado() {
        return encaminhado;
    }

    public void setEncaminhado(EncaminhadoBean encaminhado) {
        this.encaminhado = encaminhado;
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

    public List<EncaminhadoBean> getListaTiposEncaminhamento() {
        return listaTiposEncaminhamento;
    }

    public void setListaTiposEncaminhamento(List<EncaminhadoBean> listaTiposEncaminhamento) {
        this.listaTiposEncaminhamento = listaTiposEncaminhamento;
    }
}
