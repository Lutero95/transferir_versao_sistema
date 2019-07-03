package br.gov.al.maceio.sishosp.hosp.control;

import br.gov.al.maceio.sishosp.comum.enums.TipoCabecalho;
import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.JSFUtil;
import br.gov.al.maceio.sishosp.comum.util.RedirecionarUtil;
import br.gov.al.maceio.sishosp.hosp.dao.SexoDAO;
import br.gov.al.maceio.sishosp.hosp.model.Sexo;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@ManagedBean(name = "SexoController")
@ViewScoped
public class SexoController implements Serializable {

    private static final long serialVersionUID = 1L;
    private Sexo sexo;
    private Integer tipo;
    private String cabecalho;
    private List<Sexo> listaSexos;
    private SexoDAO sDao = new SexoDAO();

    //CONSTANTES
    private static final String ENDERECO_CADASTRO = "cadastrosexo?faces-redirect=true";
    private static final String ENDERECO_TIPO = "&amp;tipo=";
    private static final String ENDERECO_ID = "&amp;id=";
    private static final String CABECALHO_INCLUSAO = "Inclusão de Sexo";
    private static final String CABECALHO_ALTERACAO = "Alteração de Sexo";

    public SexoController() {
        sexo = new Sexo();
        this.cabecalho = "";
        listaSexos = new ArrayList<>();
    }

    public String redirectEdit() {
        return RedirecionarUtil.redirectEdit(ENDERECO_CADASTRO, ENDERECO_ID, this.sexo.getId(), ENDERECO_TIPO, tipo);
    }

    public String redirectInsert() {
        return RedirecionarUtil.redirectInsert(ENDERECO_CADASTRO, ENDERECO_TIPO, tipo);
    }

    public void getEditSexo() throws ProjetoException {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Map<String, String> params = facesContext.getExternalContext()
                .getRequestParameterMap();
        if (params.get("id") != null) {
            Integer id = Integer.parseInt(params.get("id"));
            tipo = Integer.parseInt(params.get("tipo"));
            this.sexo = sDao.buscaSexoPorId(id);
        } else {
            tipo = Integer.parseInt(params.get("tipo"));

        }

    }

    public void limparDados() {
        sexo = new Sexo();
    }

    public void listarSexos() throws ProjetoException {
        listaSexos = sDao.listarSexos();
    }

    public void gravarSexo() {
        boolean cadastrou = sDao.gravarSexo(sexo);

        if (cadastrou == true) {
            limparDados();
            JSFUtil.adicionarMensagemSucesso("Sexo cadastrado com sucesso!", "Sucesso");
        } else {
            JSFUtil.adicionarMensagemErro("Ocorreu um erro durante o cadastro!", "Erro");
        }
    }

    public void alterarSexo() {

        boolean alterou = sDao.alterarSexo(sexo);
        if (alterou == true) {
            JSFUtil.adicionarMensagemSucesso("Sexo alterado com sucesso!", "Sucesso");
        } else {
            JSFUtil.adicionarMensagemErro("Ocorreu um erro durante a alteração!", "Erro");
        }
    }

    public void excluirSexo() throws ProjetoException {

        boolean excluiu = sDao.excluirSexo(sexo.getId());
        if (excluiu == true) {
            JSFUtil.adicionarMensagemSucesso("Sexo excluído com sucesso!", "Sucesso");
            JSFUtil.fecharDialog("dialogExclusao");
        } else {
            JSFUtil.adicionarMensagemErro("Ocorreu um erro durante a exclusão!", "Erro");
            JSFUtil.fecharDialog("dialogExclusao");
        }
        listarSexos();
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

    public Sexo getSexo() {
        return sexo;
    }

    public void setSexo(Sexo sexo) {
        this.sexo = sexo;
    }

    public Integer getTipo() {
        return tipo;
    }

    public void setTipo(Integer tipo) {
        this.tipo = tipo;
    }

    public List<Sexo> getListaSexos() {
        return listaSexos;
    }

    public void setListaSexos(List<Sexo> listaSexos) {
        this.listaSexos = listaSexos;
    }
}
