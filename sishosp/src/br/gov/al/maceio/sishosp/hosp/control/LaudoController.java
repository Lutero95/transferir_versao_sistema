package br.gov.al.maceio.sishosp.hosp.control;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import br.gov.al.maceio.sishosp.hosp.dao.*;
import org.primefaces.context.RequestContext;

import br.gov.al.maceio.sishosp.acl.model.FuncionarioBean;
import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.hosp.model.CidBean;
import br.gov.al.maceio.sishosp.hosp.model.EquipamentoBean;
import br.gov.al.maceio.sishosp.hosp.model.EquipeBean;
import br.gov.al.maceio.sishosp.hosp.model.FornecedorBean;
import br.gov.al.maceio.sishosp.hosp.model.GrupoBean;
import br.gov.al.maceio.sishosp.hosp.model.LaudoBean;
import br.gov.al.maceio.sishosp.hosp.model.PacienteBean;
import br.gov.al.maceio.sishosp.hosp.model.ProcedimentoBean;
import br.gov.al.maceio.sishosp.hosp.model.ProgramaBean;
import br.gov.al.maceio.sishosp.hosp.model.TipoAtendimentoBean;

@ManagedBean(name = "LaudoController")
@ViewScoped
public class LaudoController implements Serializable {

    private static final long serialVersionUID = 1L;
    private LaudoBean laudo;
    private String cabecalho;
    private int tipo;

    private List<LaudoBean> listaLaudo;
    private List<LaudoBean> listaLaudoDigita;

    private LaudoDAO lDao = new LaudoDAO();

    public LaudoController() {
        laudo = new LaudoBean();
        this.cabecalho = "";
        listaLaudo = new ArrayList<>();
        listaLaudo = null;
        listaLaudoDigita = new ArrayList<>();
        listaLaudoDigita = null;

    }

    public String redirectInsert() {
        return "cadastroLaudoDigita?faces-redirect=true&amp;tipo=" + this.tipo;
    }

    public String redirectEdit() {
        return "cadastroLaudoDigita?faces-redirect=true&amp;id="
                + this.laudo.getId() + "&amp;tipo=" + tipo;
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
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN,
                    "Informe o período do Laudo.", "Aviso");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }

    }

    public void gravarLaudo() throws ProjetoException {
        boolean cadastrou = lDao.cadastrarLaudo(laudo);

        if (cadastrou == true) {
            limparDados();
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Laudo cadastrado com sucesso!", "Sucesso");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            listaLaudo = null;

        } else {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Ocorreu um erro durante o cadastro!", "Erro");
            FacesContext.getCurrentInstance().addMessage(null, msg);

        }

    }

    public void alterarLaudo() throws ProjetoException {
        boolean alterou = lDao.alterarLaudo(laudo);

        if (alterou == true) {
            limparDados();
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Laudo Alterado com sucesso!", "Sucesso");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            listaLaudo = null;

        } else {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Ocorreu um erro durante o cadastro!", "Erro");
            FacesContext.getCurrentInstance().addMessage(null, msg);

        }

    }

    public void excluirLaudo() throws ProjetoException {
        boolean excluiu = lDao.excluirLaudo(laudo);

        if (excluiu == true) {
            limparDados();
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Laudo Excluído com sucesso!", "Sucesso");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            listaLaudoDigita = null;
            getListaLaudoDigita();
            RequestContext.getCurrentInstance().execute(
                    "PF('dialogAtencao').hide();");

        } else {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Ocorreu um erro durante o cadastro!", "Erro");
            FacesContext.getCurrentInstance().addMessage(null, msg);

        }

    }

    public void listarLaudo() throws ProjetoException {

        LaudoDAO fdao = new LaudoDAO();
        listaLaudo = fdao.listaLaudos();

    }

    public List<CidBean> listaCidAutoCompletePorProcedimento(String query)
            throws ProjetoException {
        CidDAO gDao = new CidDAO();
        List<CidBean> result = gDao.listarCidsBuscaPorProcedimento(query, laudo.getProcedimento_primario().getIdProc());
        return result;
    }


    public LaudoBean getLaudo() {
        return laudo;
    }

    public void setLaudo(LaudoBean laudo) {
        this.laudo = laudo;
    }

    public String getCabecalho() {
        if (this.tipo == 2) {
            cabecalho = "Alterar Laudo";
        } else {
            cabecalho = "Cadastro de Laudo";
        }
        return cabecalho;
    }

    public void setCabecalho(String cabecalho) {
        this.cabecalho = cabecalho;
    }

    public List<LaudoBean> getListaLaudo() throws ProjetoException {
        if (listaLaudo == null) {

            LaudoDAO fdao = new LaudoDAO();
            listaLaudo = fdao.listaLaudos();
        }
        return listaLaudo;
    }

    public void setListaLaudo(List<LaudoBean> listaLaudo) {
        this.listaLaudo = listaLaudo;
    }

    public List<LaudoBean> getListaLaudoDigita() throws ProjetoException {
        if (listaLaudoDigita == null) {

            LaudoDAO fdao = new LaudoDAO();
            listaLaudoDigita = fdao.listaLaudos();

        }
        return listaLaudoDigita;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

}
