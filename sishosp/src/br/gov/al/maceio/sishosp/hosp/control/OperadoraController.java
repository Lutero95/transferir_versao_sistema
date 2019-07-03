package br.gov.al.maceio.sishosp.hosp.control;

import br.gov.al.maceio.sishosp.comum.enums.TipoCabecalho;
import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.JSFUtil;
import br.gov.al.maceio.sishosp.comum.util.RedirecionarUtil;
import br.gov.al.maceio.sishosp.comum.util.VerificadorUtil;
import br.gov.al.maceio.sishosp.hosp.dao.OperadoraDAO;
import br.gov.al.maceio.sishosp.hosp.model.Operadora;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@ManagedBean(name = "OperadoraController")
@ViewScoped
public class OperadoraController implements Serializable {

    private static final long serialVersionUID = 1L;
    private Operadora operadora;
    private Integer tipo;
    private String cabecalho;
    private List<Operadora> listaOperadoras;
    private OperadoraDAO oDao = new OperadoraDAO();

    //CONSTANTES
    private static final String ENDERECO_CADASTRO = "cadastrooperadora?faces-redirect=true";
    private static final String ENDERECO_TIPO = "&amp;tipo=";
    private static final String ENDERECO_ID = "&amp;id=";
    private static final String CABECALHO_INCLUSAO = "Inclusão de Operadora";
    private static final String CABECALHO_ALTERACAO = "Alteração de Operadora";

    public OperadoraController() {
        operadora = new Operadora();
        this.cabecalho = "";
        listaOperadoras = new ArrayList<>();
    }

    public String redirectEdit() {
        return RedirecionarUtil.redirectEdit(ENDERECO_CADASTRO, ENDERECO_ID, this.operadora.getId(), ENDERECO_TIPO, tipo);
    }

    public String redirectInsert() {
        return RedirecionarUtil.redirectInsert(ENDERECO_CADASTRO, ENDERECO_TIPO, tipo);
    }

    public void getEditOperadora() throws ProjetoException {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Map<String, String> params = facesContext.getExternalContext()
                .getRequestParameterMap();
        if (params.get("id") != null) {
            Integer id = Integer.parseInt(params.get("id"));
            tipo = Integer.parseInt(params.get("tipo"));
            this.operadora = oDao.buscarOperadoraPorId(id);
        } else {
            tipo = Integer.parseInt(params.get("tipo"));

        }

    }

    public void limparDados() {
        operadora = new Operadora();
    }

    public void listarOperadoras() throws ProjetoException {
        listaOperadoras = oDao.listarOperadoras();
    }

    public List<Operadora> listarTodasAsOperadoras() throws ProjetoException {

        if(!VerificadorUtil.verificarSeListaNuloOuVazia(Collections.singletonList(listaOperadoras))) {
            listaOperadoras = listaOperadoras = oDao.listarOperadoras();
        }

        return listaOperadoras;
    }

    public void gravarOperadora() {
        boolean cadastrou = oDao.gravarOperadora(operadora);

        if (cadastrou == true) {
            limparDados();
            JSFUtil.adicionarMensagemSucesso("Operadora cadastrada com sucesso!", "Sucesso");
        } else {
            JSFUtil.adicionarMensagemErro("Ocorreu um erro durante o cadastro!", "Erro");
        }
    }

    public void alterarOperadora() {

        boolean alterou = oDao.alterarOperadora(operadora);
        if (alterou == true) {
            JSFUtil.adicionarMensagemSucesso("Operadora alterada com sucesso!", "Sucesso");
        } else {
            JSFUtil.adicionarMensagemErro("Ocorreu um erro durante a alteração!", "Erro");
        }
    }

    public void excluirOperadora() throws ProjetoException {

        boolean excluiu = oDao.excluirOperadora(operadora.getId());
        if (excluiu == true) {
            JSFUtil.adicionarMensagemSucesso("Operadora excluída com sucesso!", "Sucesso");
            JSFUtil.fecharDialog("dialogExclusao");
        } else {
            JSFUtil.adicionarMensagemErro("Ocorreu um erro durante a exclusão!", "Erro");
            JSFUtil.fecharDialog("dialogExclusao");
        }
        listarOperadoras();
    }


    public void setCabecalho(String cabecalho) {
        this.cabecalho = cabecalho;
    }

    public String getCabecalho() {
        if (this.tipo.equals(TipoCabecalho.INCLUSAO.getSigla())) {
            cabecalho = CABECALHO_INCLUSAO;
        } else if (this.tipo.equals(TipoCabecalho.ALTERACAO.getSigla())) {
            cabecalho = CABECALHO_ALTERACAO;
        }
        return cabecalho;
    }

    public Operadora getOperadora() {
        return operadora;
    }

    public void setOperadora(Operadora operadora) {
        this.operadora = operadora;
    }

    public Integer getTipo() {
        return tipo;
    }

    public void setTipo(Integer tipo) {
        this.tipo = tipo;
    }

    public List<Operadora> getListaOperadoras() {
        return listaOperadoras;
    }

    public void setListaOperadoras(List<Operadora> listaOperadoras) {
        this.listaOperadoras = listaOperadoras;
    }
}
