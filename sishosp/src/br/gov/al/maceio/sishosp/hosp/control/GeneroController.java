package br.gov.al.maceio.sishosp.hosp.control;

import br.gov.al.maceio.sishosp.comum.enums.TipoCabecalho;
import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.JSFUtil;
import br.gov.al.maceio.sishosp.comum.util.RedirecionarUtil;
import br.gov.al.maceio.sishosp.comum.util.VerificadorUtil;
import br.gov.al.maceio.sishosp.hosp.dao.GeneroDAO;
import br.gov.al.maceio.sishosp.hosp.model.Genero;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@ManagedBean(name = "GeneroController")
@ViewScoped
public class GeneroController implements Serializable {

    private static final long serialVersionUID = 1L;
    private Genero genero;
    private Integer tipo;
    private String cabecalho;
    private List<Genero> listaGeneros;
    private GeneroDAO sDao = new GeneroDAO();

    //CONSTANTES
    private static final String ENDERECO_CADASTRO = "cadastrogenero?faces-redirect=true";
    private static final String ENDERECO_TIPO = "&amp;tipo=";
    private static final String ENDERECO_ID = "&amp;id=";
    private static final String CABECALHO_INCLUSAO = "Inclusão de Gênero";
    private static final String CABECALHO_ALTERACAO = "Alteração de Gênero";

    public GeneroController() {
        genero = new Genero();
        this.cabecalho = "";
        listaGeneros = new ArrayList<>();
    }

    public String redirectEdit() {
        return RedirecionarUtil.redirectEdit(ENDERECO_CADASTRO, ENDERECO_ID, this.genero.getId(), ENDERECO_TIPO, tipo);
    }

    public String redirectInsert() {
        return RedirecionarUtil.redirectInsert(ENDERECO_CADASTRO, ENDERECO_TIPO, tipo);
    }

    public void getEditGenero() throws ProjetoException {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Map<String, String> params = facesContext.getExternalContext()
                .getRequestParameterMap();
        if (params.get("id") != null) {
            Integer id = Integer.parseInt(params.get("id"));
            tipo = Integer.parseInt(params.get("tipo"));
            this.genero = sDao.buscaGeneroPorId(id);
        } else {
            tipo = Integer.parseInt(params.get("tipo"));

        }

    }

    public void limparDados() {
        genero = new Genero();
    }

    public void listarGeneros() throws ProjetoException {
        listaGeneros = sDao.listarGeneros();
    }

    public void atualizarListaDeGeneros() throws ProjetoException {
        JSFUtil.fecharDialog("dlgcadastrogenero");
       listarTodosOsGeneros();
    }

    public List<Genero> listarTodosOsGeneros() throws ProjetoException {

        if(!VerificadorUtil.verificarSeListaNuloOuVazia(Collections.singletonList(listaGeneros))) {
            listaGeneros = sDao.listarGeneros();
        }

        return listaGeneros;
    }

    public void gravarGenero() throws ProjetoException {
        boolean cadastrou = sDao.gravarGenero(genero);

        if (cadastrou == true) {
            limparDados();
            JSFUtil.adicionarMensagemSucesso("Gênero cadastrado com sucesso!", "Sucesso");
        } else {
            JSFUtil.adicionarMensagemErro("Ocorreu um erro durante o cadastro!", "Erro");
        }
    }

    public void alterarGenero() throws ProjetoException {

        boolean alterou = sDao.alterarGenero(genero);
        if (alterou == true) {
            JSFUtil.adicionarMensagemSucesso("Gênero alterado com sucesso!", "Sucesso");
        } else {
            JSFUtil.adicionarMensagemErro("Ocorreu um erro durante a alteração!", "Erro");
        }
    }

    public void excluirGenero() throws ProjetoException {

        boolean excluiu = sDao.excluirGenero(genero.getId());
        if (excluiu == true) {
            JSFUtil.adicionarMensagemSucesso("Gênero excluído com sucesso!", "Sucesso");
            JSFUtil.fecharDialog("dialogExclusao");
        } else {
            JSFUtil.adicionarMensagemErro("Ocorreu um erro durante a exclusão!", "Erro");
            JSFUtil.fecharDialog("dialogExclusao");
        }
        listarGeneros();
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

    public Genero getGenero() {
        return genero;
    }

    public void setGenero(Genero genero) {
        this.genero = genero;
    }

    public Integer getTipo() {
        return tipo;
    }

    public void setTipo(Integer tipo) {
        this.tipo = tipo;
    }

    public List<Genero> getListaGeneros() {
        return listaGeneros;
    }

    public void setListaGeneros(List<Genero> listaGeneros) {
        this.listaGeneros = listaGeneros;
    }
}
