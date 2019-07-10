package br.gov.al.maceio.sishosp.hosp.control;

import java.io.Serializable;
import java.sql.SQLException;
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
import br.gov.al.maceio.sishosp.hosp.dao.TipoAtendimentoDAO;
import br.gov.al.maceio.sishosp.hosp.model.GrupoBean;
import br.gov.al.maceio.sishosp.hosp.model.TipoAtendimentoBean;

@ManagedBean(name = "TipoAtendimentoController")
@ViewScoped
public class TipoAtendimentoController implements Serializable {

    private static final long serialVersionUID = 1L;
    private TipoAtendimentoBean tipoAtendimento;
    private List<TipoAtendimentoBean> listaTipos;
    private Integer tipo;
    private GrupoBean grupoSelecionado;
    private String cabecalho;
    private List<TipoAtendimentoBean> listaTiposAtendimento;
    private TipoAtendimentoDAO tDao = new TipoAtendimentoDAO();
    private String mensagemAvisoGrupoOuProgramaFaltando;

    //CONSTANTES
    private static final String ENDERECO_CADASTRO = "cadastroTipoAtendimento?faces-redirect=true";
    private static final String ENDERECO_TIPO = "&amp;tipo=";
    private static final String ENDERECO_ID = "&amp;id=";
    private static final String CABECALHO_INCLUSAO = "Inclusão de Tipo de Atendimento";
    private static final String CABECALHO_ALTERACAO = "Alteração de Tipo de Atendimento";

    public TipoAtendimentoController() {
        this.tipoAtendimento = new TipoAtendimentoBean();
        this.listaTipos = null;
        this.grupoSelecionado = new GrupoBean();
        listaTiposAtendimento = new ArrayList<>();
    }

    public void limparDados() throws ProjetoException {
        this.tipoAtendimento = new TipoAtendimentoBean();
        this.listaTipos = null;
        this.listaTipos = tDao.listarTipoAt();
        this.grupoSelecionado = new GrupoBean();
    }

    public String redirectEdit() {
        return RedirecionarUtil.redirectEdit(ENDERECO_CADASTRO, ENDERECO_ID, this.tipoAtendimento.getIdTipo(), ENDERECO_TIPO, tipo);
    }

    public String redirectInsert() {
        return RedirecionarUtil.redirectInsert(ENDERECO_CADASTRO, ENDERECO_TIPO, tipo);
    }

    public void getEditTipoAtend() throws ProjetoException {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Map<String, String> params = facesContext.getExternalContext()
                .getRequestParameterMap();
        if (params.get("id") != null) {
            Integer id = Integer.parseInt(params.get("id"));

            tipo = Integer.parseInt(params.get("tipo"));
            this.tipoAtendimento = tDao.listarTipoPorId(id);
        } else {
            tipo = Integer.parseInt(params.get("tipo"));

        }

    }

    public void gravarTipo() throws ProjetoException {
        if (!validarGrupoOuPrograma()) {
            JSFUtil.adicionarMensagemAdvertencia(mensagemAvisoGrupoOuProgramaFaltando, "Campos obrigatórios!");
        } else {
            boolean cadastrou = tDao.gravarTipoAt(tipoAtendimento);

            if (cadastrou == true) {
                JSFUtil.adicionarMensagemSucesso("Tipo de Atendimento cadastrado com sucesso!", "Sucesso");
                limparDados();
            } else {
                JSFUtil.adicionarMensagemErro("Ocorreu um erro durante o cadastro!", "Erro");
            }
        }
    }

    public Boolean validarGrupoOuPrograma() {
        Boolean retorno = false;

        if (tipoAtendimento.isPrimeiroAt()) {
            if (!tipoAtendimento.getListaPrograma().isEmpty()) {
                retorno = true;
            } else {
                mensagemAvisoGrupoOuProgramaFaltando = "É necessário no mínimo informar um programa!";
            }
        } else {
            if (!tipoAtendimento.getGrupo().isEmpty()) {
                retorno = true;
            } else {
                mensagemAvisoGrupoOuProgramaFaltando = "É necessário no mínimo informar um grupo!";
            }
        }

        return retorno;
    }

    public void alterarTipo() {
        if (!validarGrupoOuPrograma()) {
            JSFUtil.adicionarMensagemAdvertencia(mensagemAvisoGrupoOuProgramaFaltando, "Campos obrigatórios!");
        } else {
            boolean alterou = tDao.alterarTipo(this.tipoAtendimento);
            if (alterou == true) {
                JSFUtil.adicionarMensagemSucesso("Tipo de Atendimento alterado com sucesso!", "Sucesso");
            } else {
                JSFUtil.adicionarMensagemErro("Ocorreu um erro durante a alteração!", "Erro");
            }
        }

    }

    public void excluirTipo() throws ProjetoException {

        boolean excluiu = tDao.excluirTipo(this.tipoAtendimento);

        if (excluiu == true) {
            JSFUtil.adicionarMensagemSucesso("Tipo de Atendimento excluído com sucesso!", "Sucesso");
            JSFUtil.fecharDialog("dialogExclusao");
        } else {
            JSFUtil.adicionarMensagemErro("Ocorreu um erro durante a exclusão!", "Erro");
            JSFUtil.fecharDialog("dialogExclusao");
        }
        listarTipos();
    }

    public List<TipoAtendimentoBean> listaTipoAtAutoComplete(String query)
            throws ProjetoException {
        return tDao.listarTipoAtAutoComplete(query, this.grupoSelecionado);
    }

    public void listarTipos() throws ProjetoException {
        listaTiposAtendimento = listaTipos = tDao.listarTipoAt();

    }

    public TipoAtendimentoBean getTipoAtendimento() {
        return tipoAtendimento;
    }

    public void setTipoAtendimento(TipoAtendimentoBean tipoAtendimento) {
        this.tipoAtendimento = tipoAtendimento;
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


    public List<TipoAtendimentoBean> getListaTipos() {
        return listaTipos;
    }

    public void setListaTipos(List<TipoAtendimentoBean> listaTipos) {
        this.listaTipos = listaTipos;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public List<TipoAtendimentoBean> getListaTiposAtendimento() {
        return listaTiposAtendimento;
    }

    public void setListaTiposAtendimento(List<TipoAtendimentoBean> listaTiposAtendimento) {
        this.listaTiposAtendimento = listaTiposAtendimento;
    }
}
