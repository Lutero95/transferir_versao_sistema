package br.gov.al.maceio.sishosp.hosp.control;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import br.gov.al.maceio.sishosp.comum.util.JSFUtil;
import br.gov.al.maceio.sishosp.comum.util.RedirecionarUtil;
import org.primefaces.context.RequestContext;

import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.hosp.dao.EnderecoDAO;
import br.gov.al.maceio.sishosp.hosp.model.EnderecoBean;

@ManagedBean(name = "EnderecoController")
@ViewScoped
public class EnderecoController implements Serializable {

    private static final long serialVersionUID = 1L;
    private EnderecoBean endereco;
    private int tipo;
    private String cabecalho;
    private String cabecalhoBairro;
    private String bairro;
    private Integer codbairro;
    private List<EnderecoBean> listaMunicipios;
    private EnderecoDAO eDao = new EnderecoDAO();

    //CONSTANTES
    private static final String ENDERECO_CADASTRO_BAIRRO = "cadastroBairros?faces-redirect=true";
    private static final String ENDERECO_TIPO_BAIRRO = "&amp;tipo=";
    private static final String ENDERECO_ID = "&amp;id=";
    private static final String CABECALHO_INCLUSAO_BAIRRO = "Inclusão de Bairro";
    private static final String CABECALHO_ALTERACAO_BAIRRO = "Alteração de Bairro";

    public EnderecoController() {
        this.endereco = new EnderecoBean();
        listaMunicipios = new ArrayList<>();
        listaMunicipios = null;
    }

    public void limparDados() {
        endereco = new EnderecoBean();
        bairro = "";

    }

    //BAIRRO

    public String redirectEditBairro() {
        return RedirecionarUtil.redirectEdit(ENDERECO_CADASTRO_BAIRRO, ENDERECO_ID, this.endereco.getCodbairro(), ENDERECO_TIPO_BAIRRO, tipo);
    }

    public String redirectInsertBairro() {
        return RedirecionarUtil.redirectInsert(ENDERECO_CADASTRO_BAIRRO, ENDERECO_TIPO_BAIRRO, tipo);
    }

    public void getEditBairro() throws ProjetoException {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Map<String, String> params = facesContext.getExternalContext()
                .getRequestParameterMap();
        if (params.get("id") != null) {
            Integer id = Integer.parseInt(params.get("id"));
            tipo = Integer.parseInt(params.get("tipo"));
            this.endereco = eDao.listarBairroPorId(id);
            // foi criado bairro e codbairro porque não estava indo pelos atributos
            // do objeto endereco
            bairro = endereco.getBairro();
            codbairro = endereco.getCodbairro();
        } else {
            tipo = Integer.parseInt(params.get("tipo"));

        }

    }

    public void gravarBairros() {
        // foi criado bairro e codbairro porque não estava indo pelos atributos
        // do objeto endereco
        endereco.setBairro(bairro);

        boolean cadastrou = eDao.cadastrarBairros(endereco);

        if (cadastrou == true) {
            JSFUtil.adicionarMensagemSucesso("Bairro cadastrado com sucesso!", "Sucesso");
            limparDados();
            listaMunicipios = null;
        } else {
            JSFUtil.adicionarMensagemErro("Ocorreu um erro durante o cadastro!", "Erro");
        }

    }

    public void alterarBairros() {
        // foi criado bairro e codbairro porque não estava indo pelos atributos
        // do objeto endereco
        endereco.setBairro(bairro);
        endereco.setCodbairro(codbairro);

        boolean alterou = eDao.alterarBairros(endereco);
        if (alterou == true) {
            JSFUtil.adicionarMensagemSucesso("Bairro alterado com sucesso!", "Sucesso");
        } else {
            JSFUtil.adicionarMensagemErro("Ocorreu um erro durante a alteração!", "Erro");
        }

    }

    public void excluirBairro() throws ProjetoException {

        boolean excluiu = eDao.excluirBairros(endereco);

        if (excluiu == true) {
            JSFUtil.adicionarMensagemSucesso("Bairro excluído com sucesso!", "Sucesso");
            JSFUtil.fecharDialog("dialogExclusao");
            listarMunicipios();
        } else {
            JSFUtil.adicionarMensagemErro("Ocorreu um erro durante a exclusão!", "Erro");
            JSFUtil.fecharDialog("dialogExclusao");
        }

    }

    public List<EnderecoBean> listarBairros() throws ProjetoException {
        return eDao.listaBairros();
    }


    //MUNICÍPIO

    public String redirectEdit() {
        return "cadastroMunicipios?faces-redirect=true&amp;id="
                + this.endereco.getCodmunicipio() + "&amp;tipo=" + tipo;
    }

    public String redirectInsert() {
        return "cadastroMunicipios?faces-redirect=true&amp;tipo=" + tipo;
    }

    public void getEditMunicipio() throws ProjetoException {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Map<String, String> params = facesContext.getExternalContext()
                .getRequestParameterMap();
        if (params.get("id") != null) {
            Integer id = Integer.parseInt(params.get("id"));
            tipo = Integer.parseInt(params.get("tipo"));
            this.endereco = eDao.listarMunicipioPorId(id);
        } else {
            tipo = Integer.parseInt(params.get("tipo"));

        }

    }

    public void gravarMunicipios() throws ProjetoException {

        boolean cadastrou = eDao.cadastrarMunicipio(endereco);

        if (cadastrou == true) {
            limparDados();
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Município cadastrado com sucesso!", "Sucesso");
            FacesContext.getCurrentInstance().addMessage(null, msg);

            listaMunicipios = null;

        } else {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Ocorreu um erro durante o cadastro!", "Erro");
            FacesContext.getCurrentInstance().addMessage(null, msg);

        }

    }

    public void alterarMunicipios() throws ProjetoException {

        boolean alterou = eDao.alterarMunicipio(endereco);
        listaMunicipios = null;
        if (alterou == true) {
            // limparDados();
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Município alterado com sucesso!", "Sucesso");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            // return
            // "/pages/sishosp/gerenciarMunicipio.faces?faces-redirect=true";
            // RequestContext.getCurrentInstance().execute("dlgAltMenu.hide();");
        } else {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Ocorreu um erro durante o cadastro!", "Erro");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            // return "";
            // RequestContext.getCurrentInstance().execute("dlgAltMenu.hide();");
        }

    }


    public void excluirMunicipios() throws ProjetoException {

        boolean excluio = eDao.excluirMunicipio(endereco);

        if (excluio == true) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Município excluído com sucesso!", "Sucesso");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            listarMunicipios();
            RequestContext.getCurrentInstance().execute(
                    "PF('dialogAtencao').hide();");
        } else {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Ocorreu um erro durante a exclusao!", "Erro");
            FacesContext.getCurrentInstance().addMessage(null, msg);

            RequestContext.getCurrentInstance().execute(
                    "PF('dialogAtencao').hide();");
        }

    }


    public List<EnderecoBean> listaMunicipioAutoComplete(String query)
            throws ProjetoException {
        List<EnderecoBean> result = eDao.buscaMunicipioAutoComplete(query);
        return result;
    }


    public void listarMunicipios() throws ProjetoException {
        listaMunicipios = eDao.listaMunicipios();

    }

    public List<EnderecoBean> listarMunicipiosCadastro()
            throws ProjetoException {
        listaMunicipios = eDao.listaMunicipios();
        return listaMunicipios;

    }

    public void setListaMunicipios(List<EnderecoBean> listaMunicipios) {
        this.listaMunicipios = listaMunicipios;
    }


    //GETTERS E SETTERS

    public EnderecoBean getEndereco() {
        return endereco;
    }

    public void setEndereco(EnderecoBean endereco) {
        this.endereco = endereco;
    }

    public String getCabecalho() {
        if (this.tipo == 1) {
            cabecalho = "Inclusão de Município";
        } else if (this.tipo == 2) {
            cabecalho = "Alteração de Município";
        }
        return cabecalho;
    }

    public void setCabecalho(String cabecalho) {
        this.cabecalho = cabecalho;
    }

    public List<EnderecoBean> getListaMunicipios() {
        return listaMunicipios;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public String getCabecalhoBairro() {
        if (this.tipo == 1) {
            cabecalhoBairro = CABECALHO_INCLUSAO_BAIRRO;
        } else if (this.tipo == 2) {
            cabecalhoBairro = CABECALHO_ALTERACAO_BAIRRO;
        }
        return cabecalhoBairro;
    }

    public void setCabecalhoBairro(String cabecalhoBairro) {
        this.cabecalhoBairro = cabecalhoBairro;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

}
