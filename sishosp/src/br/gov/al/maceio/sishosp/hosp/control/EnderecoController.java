package br.gov.al.maceio.sishosp.hosp.control;

import java.io.Serializable;
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
import br.gov.al.maceio.sishosp.hosp.dao.EnderecoDAO;
import br.gov.al.maceio.sishosp.hosp.model.EnderecoBean;

@ManagedBean(name = "EnderecoController")
@ViewScoped
public class EnderecoController implements Serializable {

    private static final long serialVersionUID = 1L;
    private EnderecoBean endereco;
    private Integer tipo;
    private String cabecalho;
    private String cabecalhoBairro;
    private String bairro;
    private Integer codbairro;
    private List<EnderecoBean> listaMunicipios;
    private List<EnderecoBean> listaBairros;
    private EnderecoDAO eDao = new EnderecoDAO();

    //CONSTANTES
    private static final String ENDERECO_CADASTRO_BAIRRO = "cadastroBairros?faces-redirect=true";
    private static final String CABECALHO_INCLUSAO_BAIRRO = "Inclusão de Bairro";
    private static final String CABECALHO_ALTERACAO_BAIRRO = "Alteração de Bairro";

    private static final String ENDERECO_CADASTRO_MUNICIPIO = "cadastroMunicipios?faces-redirect=true";
    private static final String CABECALHO_INCLUSAO_MUNICIPIO = "Inclusão de Município";
    private static final String CABECALHO_ALTERACAO_MUNICIPIO = "Alteração de Município";

    private static final String ENDERECO_TIPO = "&amp;tipo=";
    private static final String ENDERECO_ID = "&amp;id=";

    public EnderecoController() throws ProjetoException {
        this.endereco = new EnderecoBean();
        listaMunicipios = new ArrayList<>();
        listaMunicipios = null;
        endereco.setUf(eDao.retornarEstadoDaEmpresa());
        listaBairros = new ArrayList<>();
    }

    public void limparDados() {
        endereco = new EnderecoBean();
        bairro = "";

    }

    //BAIRRO

    public String redirectEditBairro() {
        return RedirecionarUtil.redirectEdit(ENDERECO_CADASTRO_BAIRRO, ENDERECO_ID, this.endereco.getCodbairro(), ENDERECO_TIPO, tipo);
    }

    public String redirectInsertBairro() {
        return RedirecionarUtil.redirectInsert(ENDERECO_CADASTRO_BAIRRO, ENDERECO_TIPO, tipo);
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

    public void listarBairros() throws ProjetoException {
        listaBairros = eDao.listaBairros();
    }

    public List<EnderecoBean> listarBairrosPorMunicipio(Integer codMunicipio) throws ProjetoException {
        List<EnderecoBean> listaBairrosPorMunicpio = new ArrayList<>();
        if(codMunicipio != null) {
            listaBairrosPorMunicpio = eDao.listaBairrosPorMunicipio(codMunicipio);
        }

        return listaBairrosPorMunicpio;

    }


    //MUNICÍPIO

    public String redirectEditMunicipio() {
        return RedirecionarUtil.redirectEdit(ENDERECO_CADASTRO_MUNICIPIO, ENDERECO_ID, this.endereco.getCodmunicipio(), ENDERECO_TIPO, tipo);
    }

    public String redirectInsertMunicipio() {
        return RedirecionarUtil.redirectInsert(ENDERECO_CADASTRO_MUNICIPIO, ENDERECO_TIPO, tipo);
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

    public void gravarMunicipios() {

        boolean cadastrou = eDao.cadastrarMunicipio(endereco);

        if (cadastrou == true) {
            limparDados();
            JSFUtil.adicionarMensagemSucesso("Município cadastrado com sucesso!", "Sucesso");
        } else {
            JSFUtil.adicionarMensagemErro("Ocorreu um erro durante o cadastro!", "Erro");
        }

    }

    public void alterarMunicipios() {

        boolean alterou = eDao.alterarMunicipio(endereco);
        if (alterou == true) {
            JSFUtil.adicionarMensagemSucesso("Município alterado com sucesso!", "Sucesso");
        } else {
            JSFUtil.adicionarMensagemErro("Ocorreu um erro durante a alteração!", "Erro");
        }

    }


    public void excluirMunicipios() {

        boolean excluiu = eDao.excluirMunicipio(endereco);

        if (excluiu == true) {
            JSFUtil.adicionarMensagemSucesso("Município excluído com sucesso!", "Sucesso");
            JSFUtil.fecharDialog("dialogExclusao");
        } else {
            JSFUtil.adicionarMensagemErro("Ocorreu um erro durante a exclusão!", "Erro");
            JSFUtil.fecharDialog("dialogExclusao");
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

    @Deprecated
    public List<EnderecoBean> listarMunicipiosPorEstado()
            throws ProjetoException {
        listaMunicipios = eDao.listaMunicipiosPorEstado(endereco.getUf());
        return listaMunicipios;

    }

    public List<EnderecoBean> listarMunicipiosPorEstadoGenerico(String estado)
            throws ProjetoException {
        listaMunicipios = eDao.listaMunicipiosPorEstado(estado);
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
        if (this.tipo.equals(TipoCabecalho.INCLUSAO.getSigla())) {
            cabecalho = CABECALHO_INCLUSAO_MUNICIPIO;
        } else if (this.tipo.equals(TipoCabecalho.ALTERACAO.getSigla())) {
            cabecalho = CABECALHO_ALTERACAO_MUNICIPIO;
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
        if (this.tipo.equals(TipoCabecalho.INCLUSAO.getSigla())) {
            cabecalho = CABECALHO_INCLUSAO_BAIRRO;
        } else if (this.tipo.equals(TipoCabecalho.ALTERACAO.getSigla())) {
            cabecalho = CABECALHO_ALTERACAO_BAIRRO;
        }
        return cabecalho;
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

    public List<EnderecoBean> getListaBairros() {
        return listaBairros;
    }

    public void setListaBairros(List<EnderecoBean> listaBairros) {
        this.listaBairros = listaBairros;
    }


}
