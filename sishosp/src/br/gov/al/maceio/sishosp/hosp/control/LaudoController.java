package br.gov.al.maceio.sishosp.hosp.control;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import br.gov.al.maceio.sishosp.comum.enums.TipoCabecalho;
import br.gov.al.maceio.sishosp.comum.util.DataUtil;
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
    private Integer tipo;
    private List<LaudoBean> listaLaudo;
    private List<CidBean> listaCids;
    private LaudoDAO lDao = new LaudoDAO();
    private CidDAO cDao = new CidDAO();
    private Boolean renderizarDataAutorizacao;
    private Integer idLaudoGerado = null;
	private String tipoBusca;
	private String campoBusca;

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
        if ((params.get("id") != null)) {
            Integer id = Integer.parseInt(params.get("id"));
            tipo = Integer.parseInt(params.get("tipo"));
            this.laudo = lDao.buscarLaudosPorId(id);
            
            renderizarDadosDeAutorizacao();

        } else {

            tipo = Integer.parseInt(params.get("tipo"));

        }

    }

    public void setaValidadeProcPrimLaudo(Integer validade) {
        laudo.setPeriodo(validade);
        calcularPeriodoLaudo();
    }

    public void renderizarDadosDeAutorizacao() {
        if (laudo.getSituacao().equals(SituacaoLaudo.AUTORIZADO.getSigla())) {
            renderizarDataAutorizacao = true;
        }
    }

    public void limparDados() {
        laudo = new LaudoBean();
    }

    public void calcularPeriodoLaudo() {
        laudo.setMesInicio(null);
        laudo.setMesFinal(null);
        if (laudo.getDataSolicitacao() != null) {
            laudo.setMesInicio(DataUtil.extrairMesDeData(laudo.getDataSolicitacao()));
            laudo.setAnoInicio(DataUtil.extrairAnoDeData(laudo.getDataSolicitacao()));
        }
        if (laudo.getPeriodo() != null && laudo.getPeriodo() != 0 && laudo.getMesInicio() != null && laudo.getAnoInicio() != null) {

            int periodo = (laudo.getPeriodo() / 30) - 1; // o periodo do laudo considera o mes atual, por isso a inclusao do -1
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


        }

    }

    public void gravarLaudo() {
        idLaudoGerado = null;
        idLaudoGerado = lDao.cadastrarLaudo(laudo);

        if (idLaudoGerado != null) {
            limparDados();
            JSFUtil.adicionarMensagemSucesso("Laudo cadastrado com sucesso!", "Sucesso");
            JSFUtil.abrirDialog("dlgImprimir");
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

    public void listarLaudo(String situacao, String campoBusca, String tipoBusca) throws ProjetoException {
        listaLaudo = lDao.listaLaudos(situacao, campoBusca, tipoBusca);
    }

    public List<CidBean> listaCidAutoCompletePorProcedimento(String query)
            throws ProjetoException {
        List<CidBean> result = cDao.listarCidsBuscaPorProcedimentoAutoComplete(query);
        return result;
    }

    public void listarCids(String campoBusca) throws ProjetoException {
        listaCids = cDao.listarCidsBusca(campoBusca);
    }

    public LaudoBean getLaudo() {
        return laudo;
    }

    public void setLaudo(LaudoBean laudo) {
        this.laudo = laudo;
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

    public List<LaudoBean> getListaLaudo() {
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

    public Integer getIdLaudoGerado() {
        return idLaudoGerado;
    }

    public void setIdLaudoGerado(Integer idLaudoGerado) {
        this.idLaudoGerado = idLaudoGerado;
    }

	public String getTipoBusca() {
		return tipoBusca;
	}

	public void setTipoBusca(String tipoBusca) {
		this.tipoBusca = tipoBusca;
	}

	public String getCampoBusca() {
		return campoBusca;
	}

	public void setCampoBusca(String campoBusca) {
		this.campoBusca = campoBusca;
	}
}
