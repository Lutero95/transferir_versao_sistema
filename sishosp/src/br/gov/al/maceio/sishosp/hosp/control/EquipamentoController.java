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

import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.hosp.dao.EquipamentoDAO;
import br.gov.al.maceio.sishosp.hosp.model.EquipamentoBean;

@ManagedBean(name = "EquipamentoController")
@ViewScoped
public class EquipamentoController implements Serializable {

    private static final long serialVersionUID = 1L;
    private EquipamentoBean equipamento;
    private List<EquipamentoBean> listaEquipamentos;
    private Integer tipoBuscar;
    private int tipo;
    private String cabecalho;
    private EquipamentoDAO eDao = new EquipamentoDAO();

    //CONSTANTES
    private static final String ENDERECO_CADASTRO = "cadastroEquipamento?faces-redirect=true";
    private static final String ENDERECO_TIPO = "&amp;tipo=";
    private static final String ENDERECO_ID = "&amp;id=";
    private static final String CABECALHO_INCLUSAO = "Inclusão de Equipamento";
    private static final String CABECALHO_ALTERACAO = "Alteração de Equipamento";

    public EquipamentoController() {
        equipamento = new EquipamentoBean();
        listaEquipamentos = new ArrayList<>();
        listaEquipamentos = null;
        this.cabecalho = "";
    }

    public String redirectEdit() {
        return RedirecionarUtil.redirectEdit(ENDERECO_CADASTRO, ENDERECO_ID, this.equipamento.getId_equipamento(), ENDERECO_TIPO, tipo);
    }

    public String redirectInsert() {
        return RedirecionarUtil.redirectInsert(ENDERECO_CADASTRO, ENDERECO_TIPO, tipo);
    }


    public void limparDados() {
        equipamento = new EquipamentoBean();
        listaEquipamentos = new ArrayList<>();
    }

    public void gravarEquipamento(){
        boolean cadastrou = eDao.gravarEquipamento(equipamento);

        if (cadastrou == true) {
            limparDados();
            JSFUtil.adicionarMensagemSucesso("Equipamento cadastrado com sucesso!", "Sucesso");
        } else {
            JSFUtil.adicionarMensagemErro("Ocorreu um erro durante o cadastro!", "Erro");
        }
    }

    public void alterarEquipamento() throws ProjetoException {
        boolean alterou = eDao.alterarEquipamento(equipamento);

        if (alterou == true) {
            JSFUtil.adicionarMensagemSucesso("Equipamento alterado com sucesso!", "Sucesso");
        } else {
            JSFUtil.adicionarMensagemErro("Ocorreu um erro durante a alteração!", "Erro");
        }
        listaEquipamentos = eDao.listarEquipamentos();

    }

    public void excluirEquipamento() throws ProjetoException {
        boolean excluiu = eDao.excluirEquipamento(equipamento);

        if (excluiu == true) {
            JSFUtil.adicionarMensagemSucesso("Equipamento excluído com sucesso!", "Sucesso");
            JSFUtil.fecharDialog("dialogExclusao");
        } else {
            JSFUtil.adicionarMensagemErro("Ocorreu um erro durante a exclusão!", "Erro");
            JSFUtil.fecharDialog("dialogExclusao");
        }
        listaEquipamentos = eDao.listarEquipamentos();
    }

    public void getEditEquipamento() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Map<String, String> params = facesContext.getExternalContext()
                .getRequestParameterMap();
        if (params.get("id") != null) {
            Integer id = Integer.parseInt(params.get("id"));
            tipo = Integer.parseInt(params.get("tipo"));
            this.equipamento = eDao.buscaEquipamentoPorId(id);
        } else {
            tipo = Integer.parseInt(params.get("tipo"));

        }

    }

    public List<EquipamentoBean> listarEquipamentos()
            throws ProjetoException{
        listaEquipamentos = eDao.listarEquipamentos();

        return listaEquipamentos;
    }

    public List<EquipamentoBean> listaEquipamentoAutoComplete(String query){
        List<EquipamentoBean> result = eDao.listarEquipamentoAutoComplete(query);

        return result;
    }

    public EquipamentoBean getEquipamento() {
        return equipamento;
    }

    public void setEquipamento(EquipamentoBean equipamento) {
        this.equipamento = equipamento;
    }

    public Integer getTipoBuscar() {
        return tipoBuscar;
    }

    public void setTipoBuscar(Integer tipoBuscar) {
        this.tipoBuscar = tipoBuscar;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public void setCabecalho(String cabecalho) {
        this.cabecalho = cabecalho;
    }

    public String getCabecalho() {
        if (this.tipo == 1) {
            cabecalho = CABECALHO_INCLUSAO;
        } else if (this.tipo == 2) {
            cabecalho = CABECALHO_ALTERACAO;
        }
        return cabecalho;
    }

    public List<EquipamentoBean> getListaEquipamentos() {
        return listaEquipamentos;
    }

    public void setListaEquipamentos(List<EquipamentoBean> listaEquipamentos) {
        this.listaEquipamentos = listaEquipamentos;
    }
}
