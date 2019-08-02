package br.gov.al.maceio.sishosp.hosp.control;

import br.gov.al.maceio.sishosp.comum.enums.TipoCabecalho;
import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.JSFUtil;
import br.gov.al.maceio.sishosp.comum.util.RedirecionarUtil;
import br.gov.al.maceio.sishosp.comum.util.VerificadorUtil;
import br.gov.al.maceio.sishosp.hosp.dao.ReligiaoDAO;
import br.gov.al.maceio.sishosp.hosp.model.Religiao;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@ManagedBean(name = "ReligiaoController")
@ViewScoped
public class ReligiaoController implements Serializable {

    private static final long serialVersionUID = 1L;
    private Religiao religiao;
    private Integer tipo;
    private String cabecalho;
    private List<Religiao> listaReligioes;
    private ReligiaoDAO rDao = new ReligiaoDAO();

    //CONSTANTES
    private static final String ENDERECO_CADASTRO = "cadastroreligiao?faces-redirect=true";
    private static final String ENDERECO_TIPO = "&amp;tipo=";
    private static final String ENDERECO_ID = "&amp;id=";
    private static final String CABECALHO_INCLUSAO = "Inclusão de Religião";
    private static final String CABECALHO_ALTERACAO = "Alteração de Religião";

    public ReligiaoController() {
        religiao = new Religiao();
        this.cabecalho = "";
        listaReligioes = new ArrayList<>();
    }

    public String redirectEdit() {
        return RedirecionarUtil.redirectEdit(ENDERECO_CADASTRO, ENDERECO_ID, this.religiao.getId(), ENDERECO_TIPO, tipo);
    }

    public String redirectInsert() {
        return RedirecionarUtil.redirectInsert(ENDERECO_CADASTRO, ENDERECO_TIPO, tipo);
    }

    public void getEditReligiao() throws ProjetoException {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Map<String, String> params = facesContext.getExternalContext()
                .getRequestParameterMap();
        if (params.get("id") != null) {
            Integer id = Integer.parseInt(params.get("id"));
            tipo = Integer.parseInt(params.get("tipo"));
            this.religiao = rDao.buscarReligiaoPorId(id);
        } else {
            tipo = Integer.parseInt(params.get("tipo"));

        }

    }

    public void limparDados() {
        religiao = new Religiao();
    }

    public void listarReligioes() throws ProjetoException {
        listaReligioes = rDao.listarReligiao();
    }

    public void atualizarListaDeReligioes() throws ProjetoException {
        JSFUtil.fecharDialog("dlgcadastroreligiao");
        listarTodasAsReligioes();
    }

    public List<Religiao> listarTodasAsReligioes() throws ProjetoException {

        if(!VerificadorUtil.verificarSeListaNuloOuVazia(Collections.singletonList(listaReligioes))) {
            listaReligioes = rDao.listarReligiao();
        }

        return listaReligioes;
    }

    public void gravarReligiao() {
        boolean cadastrou = rDao.gravarReligiao(religiao);

        if (cadastrou == true) {
            limparDados();
            JSFUtil.adicionarMensagemSucesso("Religião cadastrada com sucesso!", "Sucesso");
        } else {
            JSFUtil.adicionarMensagemErro("Ocorreu um erro durante o cadastro!", "Erro");
        }
    }

    public void alterarReligiao() {

        boolean alterou = rDao.alterarReligiao(religiao);
        if (alterou == true) {
            JSFUtil.adicionarMensagemSucesso("Religião alterada com sucesso!", "Sucesso");
        } else {
            JSFUtil.adicionarMensagemErro("Ocorreu um erro durante a alteração!", "Erro");
        }
    }

    public void excluirReligiao() throws ProjetoException {

        boolean excluiu = rDao.excluirReligiao(religiao.getId());
        if (excluiu == true) {
            JSFUtil.adicionarMensagemSucesso("Religião excluída com sucesso!", "Sucesso");
            JSFUtil.fecharDialog("dialogExclusao");
        } else {
            JSFUtil.adicionarMensagemErro("Ocorreu um erro durante a exclusão!", "Erro");
            JSFUtil.fecharDialog("dialogExclusao");
        }
        listarReligioes();
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

    public Religiao getReligiao() {
        return religiao;
    }

    public void setReligiao(Religiao religiao) {
        this.religiao = religiao;
    }

    public Integer getTipo() {
        return tipo;
    }

    public void setTipo(Integer tipo) {
        this.tipo = tipo;
    }

    public List<Religiao> getListaReligioes() {
        return listaReligioes;
    }

    public void setListaReligioes(List<Religiao> listaReligioes) {
        this.listaReligioes = listaReligioes;
    }
}
