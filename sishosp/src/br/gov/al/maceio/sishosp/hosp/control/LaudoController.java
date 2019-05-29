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
import br.gov.al.maceio.sishosp.hosp.enums.SituacaoLaudo;
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
    private List<CidBean> listaCids;
    private LaudoDAO lDao = new LaudoDAO();
    private CidDAO cDao = new CidDAO();
    private Boolean renderizarDataAutorizacao;

    //CONSTANTES
    private static final String ENDERECO_CADASTRO = "cadastroLaudoDigita?faces-redirect=true";
    private static final String ENDERECO_TIPO = "&amp;tipo=";
    private static final String ENDERECO_ID = "&amp;id=";
    private static final String CABECALHO_INCLUSAO = "Inclusão de Laudo";
    private static final String CABECALHO_ALTERACAO = "Alteração de Laudo";

    public LaudoController() {
        this.laudo = new LaudoBean();
        this.cabecalho = "";
        listaLaudo = new ArrayList<>();
        listaCids = new ArrayList<>();
        renderizarDataAutorizacao = false;
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

    public void renderizarDadosDeAutorizacao(){
        if(laudo.getSituacao().equals(SituacaoLaudo.AUTORIZADO.getSigla())){
            renderizarDataAutorizacao = true;
        }
    }

    public void limparDados() {
        laudo = new LaudoBean();
    }

    public void calcularPeriodoLaudo() {

        if (laudo.getPeriodo() != null && laudo.getMesInicio() != null && laudo.getAnoInicio() != null) {

            int periodo = (laudo.getPeriodo() / 30)-1; // o periodo do laudo considera o mes atual, por isso a inclusao do -1
            int mes = 0;
            int ano = 0;

            if (laudo.getMesInicio() + periodo > 12) {
                mes = laudo.getMesInicio() + periodo - 12;
                ano = laudo.getAnoInicio() + 1;
            } else {
                mes = laudo.getMesInicio() + periodo;
                ano = laudo.getAnoInicio();
            }

            laudo.setMesFinal(mes);
            laudo.setAnoFinal(ano);


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
        List<CidBean> result = cDao.listarCidsBuscaPorProcedimentoAutoComplete(query, laudo.getProcedimentoPrimario().getIdProc());
        return result;
    }

    public void listarCidsPorProcedimento() throws ProjetoException {
        if (laudo.getProcedimentoPrimario().getIdProc() != null) {
            listaCids =  cDao.listarCidsBuscaPorProcedimento(laudo.getProcedimentoPrimario().getIdProc());
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

	public List<CidBean> getListaCids() {
		return listaCids;
	}

	public void setListaCids(List<CidBean> listaCids) {
		this.listaCids = listaCids;
	}

    public Boolean getRenderizarDataAutorizacao() {
        return renderizarDataAutorizacao;
    }

    public void setRenderizarDataAutorizacao(Boolean renderizarDataAutorizacao) {
        this.renderizarDataAutorizacao = renderizarDataAutorizacao;
    }
}
