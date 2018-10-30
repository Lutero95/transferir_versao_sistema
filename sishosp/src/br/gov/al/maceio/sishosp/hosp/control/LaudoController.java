package br.gov.al.maceio.sishosp.hosp.control;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import br.gov.al.maceio.sishosp.comum.util.JSFUtil;
import br.gov.al.maceio.sishosp.comum.util.RedirecionarUtil;
import br.gov.al.maceio.sishosp.hosp.dao.*;

import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.hosp.model.CidBean;
import br.gov.al.maceio.sishosp.hosp.model.LaudoBean;


@ManagedBean(name = "LaudoController")
@ViewScoped
public class LaudoController implements Serializable {

    private static final long serialVersionUID = 1L;
    private LaudoBean laudo;
    private String cabecalho;
    private int tipo;
    private List<LaudoBean> listaLaudo;
    private LaudoDAO lDao = new LaudoDAO();
    private CidDAO cDao = new CidDAO();

    //CONSTANTES
    private static final String ENDERECO_CADASTRO = "cadastroLaudoDigita?faces-redirect=true";
    private static final String ENDERECO_TIPO = "&amp;tipo=";
    private static final String ENDERECO_ID = "&amp;id=";
    private static final String CABECALHO_INCLUSAO = "Inclusão de Laudo";
    private static final String CABECALHO_ALTERACAO = "Alteração de Laudo";

    public LaudoController() {
        laudo = new LaudoBean();
        this.cabecalho = "";
        listaLaudo = new ArrayList<>();
        listaLaudo = null;
    }

    public String redirectEdit() {
        return RedirecionarUtil.redirectEdit(ENDERECO_CADASTRO, ENDERECO_ID, this.laudo.getId(), ENDERECO_TIPO, tipo);
    }

    public String redirectInsert() {
        return RedirecionarUtil.redirectInsert(ENDERECO_CADASTRO, ENDERECO_TIPO, tipo);
    }

    public void getEditLaudo() throws ProjetoException {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Map<String, String> params = facesContext.getExternalContext()
                .getRequestParameterMap();
        if (params.get("id") != null) {
            Integer id = Integer.parseInt(params.get("id"));
            this.laudo = lDao.buscarLaudosPorId(id);
            tipo = Integer.parseInt(params.get("tipo"));

        } else {

            tipo = Integer.parseInt(params.get("tipo"));

        }

    }

    public void limparDados() {
        laudo = new LaudoBean();
    }

    public void calcularPeriodoLaudo() {

        if (laudo.getPeriodo() != null && laudo.getMes_inicio() != null && laudo.getAno_inicio() != null) {

            int periodo = laudo.getPeriodo() / 30;
            int mes = 0;
            int ano = 0;

            if (laudo.getMes_inicio() + periodo > 12) {
                mes = laudo.getMes_inicio() + periodo - 12;
                ano = laudo.getAno_inicio() + 1;
            } else {
                mes = laudo.getMes_inicio() + periodo;
                ano = laudo.getAno_inicio();
            }

            laudo.setMes_final(mes);
            laudo.setAno_final(ano);


        } else {
            JSFUtil.adicionarMensagemAdvertencia("Informe o período do Laudo!", "Advertência");
        }

    }

    public void gravarLaudo() {
        boolean cadastrou = lDao.cadastrarLaudo(laudo);

        if (cadastrou == true) {
            limparDados();
            JSFUtil.adicionarMensagemSucesso("Laudo cadastrado com sucesso!", "Sucesso");
        } else {
            JSFUtil.adicionarMensagemErro("Ocorreu um erro durante o cadastro!", "Erro");
        }

    }

    public void alterarLaudo() {
        boolean alterou = lDao.alterarLaudo(laudo);

        if (alterou == true) {
            JSFUtil.adicionarMensagemSucesso("Laudo alterado com sucesso!", "Sucesso");
        } else {
            JSFUtil.adicionarMensagemErro("Ocorreu um erro durante a alteração!", "Erro");
        }

    }

    public void excluirLaudo() {
        boolean excluiu = lDao.excluirLaudo(laudo);

        if (excluiu == true) {
            JSFUtil.adicionarMensagemSucesso("Laudo excluído com sucesso!", "Sucesso");
            JSFUtil.fecharDialog("dialogExclusao");
        } else {
            JSFUtil.adicionarMensagemErro("Ocorreu um erro durante a exclusão!", "Erro");
            JSFUtil.fecharDialog("dialogExclusao");
        }

    }

    public List<LaudoBean> listarLaudo() throws ProjetoException {
        return lDao.listaLaudos();
    }

    public List<CidBean> listaCidAutoCompletePorProcedimento(String query)
            throws ProjetoException {
        List<CidBean> result = cDao.listarCidsBuscaPorProcedimentoAutoComplete(query, laudo.getProcedimento_primario().getIdProc());
        return result;
    }

    public List<CidBean> listarCidsPorProcedimento() throws ProjetoException {
        if (laudo.getProcedimento_primario().getIdProc() != null) {
            return cDao.listarCidsBuscaPorProcedimento(laudo.getProcedimento_primario().getIdProc());
        }
        else{
            return null;
        }
    }


    public LaudoBean getLaudo() {
        return laudo;
    }

    public void setLaudo(LaudoBean laudo) {
        this.laudo = laudo;
    }

    public String getCabecalho() {
        if (this.tipo == 2) {
            cabecalho = CABECALHO_ALTERACAO;
        } else {
            cabecalho = CABECALHO_INCLUSAO;
        }
        return cabecalho;
    }

    public void setCabecalho(String cabecalho) {
        this.cabecalho = cabecalho;
    }

    public List<LaudoBean> getListaLaudo() throws ProjetoException {
        if (listaLaudo == null) {
            listaLaudo = lDao.listaLaudos();
        }
        return listaLaudo;
    }

    public void setListaLaudo(List<LaudoBean> listaLaudo) {
        this.listaLaudo = listaLaudo;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

}
