package br.gov.al.maceio.sishosp.hosp.control;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import br.gov.al.maceio.sishosp.comum.enums.TipoCabecalho;
import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.CEPUtil;
import br.gov.al.maceio.sishosp.comum.util.JSFUtil;
import br.gov.al.maceio.sishosp.comum.util.RedirecionarUtil;
import br.gov.al.maceio.sishosp.comum.util.VerificadorUtil;
import br.gov.al.maceio.sishosp.hosp.dao.EmpresaDAO;
import br.gov.al.maceio.sishosp.hosp.dao.GrupoDAO;
import br.gov.al.maceio.sishosp.hosp.dao.SituacaoAtendimentoDAO;
import br.gov.al.maceio.sishosp.hosp.model.EmpresaBean;
import br.gov.al.maceio.sishosp.hosp.model.EnderecoBean;
import br.gov.al.maceio.sishosp.hosp.model.GrupoBean;
import br.gov.al.maceio.sishosp.hosp.model.SituacaoAtendimentoBean;

@ManagedBean(name = "EmpresaController")
@ViewScoped
public class EmpresaController implements Serializable {

    private static final long serialVersionUID = 1L;
    private EmpresaBean empresa;
    private Integer tipo;
    private String cabecalho;
    private EmpresaDAO eDao = new EmpresaDAO();
    private ArrayList<String> listaEstados;
    private List<GrupoBean> listaGruposProgramas;
    private GrupoDAO gDao = new GrupoDAO();
    private List<EmpresaBean> listaEmpresas;
    private SituacaoAtendimentoDAO situacaoAtendimentoDAO;
	private List<SituacaoAtendimentoBean> listaSituacoes;
	private String cepAntigo;

    //CONSTANTES
    private static final String ENDERECO_CADASTRO = "cadastroEmpresa?faces-redirect=true";
    private static final String ENDERECO_TIPO = "&amp;tipo=";
    private static final String ENDERECO_ID = "&amp;id=";
    private static final String CABECALHO_INCLUSAO = "Inclusão de Empresa";
    private static final String CABECALHO_ALTERACAO = "Alteração de Empresa";

    public EmpresaController() {
        this.empresa = new EmpresaBean();
        this.cabecalho = "";
        listaEmpresas = new ArrayList<>();
        listaGruposProgramas = new ArrayList<>();
        listaSituacoes  = new ArrayList<>();
        situacaoAtendimentoDAO = new SituacaoAtendimentoDAO();
        listaEstados = new ArrayList<>();
        listaEstados.add("AC");
        listaEstados.add("AL");
        listaEstados.add("AM");
        listaEstados.add("AP");
        listaEstados.add("BA");
        listaEstados.add("CE");
        listaEstados.add("DF");
        listaEstados.add("ES");
        listaEstados.add("GO");
        listaEstados.add("MA");
        listaEstados.add("MG");
        listaEstados.add("MS");
        listaEstados.add("MT");
        listaEstados.add("PA");
        listaEstados.add("PB");
        listaEstados.add("PE");
        listaEstados.add("PI");
        listaEstados.add("PR");
        listaEstados.add("RJ");
        listaEstados.add("RN");
        listaEstados.add("RO");
        listaEstados.add("RR");
        listaEstados.add("RS");
        listaEstados.add("SC");
        listaEstados.add("SE");
        listaEstados.add("SP");
        listaEstados.add("TO");

    }

    public void limparDados() {
        this.empresa = new EmpresaBean();
    }

    public String redirectEdit() {
        return RedirecionarUtil.redirectEdit(ENDERECO_CADASTRO, ENDERECO_ID, this.empresa.getCodEmpresa(), ENDERECO_TIPO, tipo);
    }

    public String redirectInsert() {
        return RedirecionarUtil.redirectInsert(ENDERECO_CADASTRO, ENDERECO_TIPO, tipo);
    }

    public void getEditEmpresa() throws ProjetoException {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Map<String, String> params = facesContext.getExternalContext()
                .getRequestParameterMap();
        if (params.get("id") != null) {
            Integer id = Integer.parseInt(params.get("id"));
            tipo = Integer.parseInt(params.get("tipo"));
            this.empresa = eDao.buscarEmpresaPorId(id);
            cepAntigo = this.empresa.getCep(); 
        } else {

            tipo = Integer.parseInt(params.get("tipo"));

        }

    }

    public void gravarEmpresa() throws ProjetoException {

        boolean cadastrou = eDao.gravarEmpresa(empresa);

        if (cadastrou == true) {
            limparDados();
            JSFUtil.adicionarMensagemSucesso("Empresa cadastrada com sucesso!", "Sucesso");
        } else {
            JSFUtil.adicionarMensagemErro("Ocorreu um erro durante o cadastro!", "Erro");
        }
    }

    public void alterarEmpresa() throws ProjetoException {
        boolean alterou = eDao.alterarEmpresa(empresa);

        if (alterou == true) {
            JSFUtil.adicionarMensagemSucesso("Empresa alterada com sucesso!", "Sucesso");
        } else {
            JSFUtil.adicionarMensagemErro("Ocorreu um erro durante a alteração!", "Erro");
        }
    }

    public void desativarEmpresa() throws ProjetoException {
        boolean desativou = eDao.desativarEmpresa(empresa);

        if (desativou == true) {
            JSFUtil.adicionarMensagemSucesso("Empresa desativada com sucesso!", "Sucesso");
            JSFUtil.fecharDialog("dialogExclusao");
        } else {
            JSFUtil.adicionarMensagemErro("Ocorreu um erro durante a exclusão!", "Erro");
            JSFUtil.fecharDialog("dialogExclusao");
        }
    }

    public void listarEmpresa() throws ProjetoException {
        listaEmpresas = eDao.listarEmpresa();
    }
    
    public void carregarDadosPeloCep(String cep) throws ProjetoException {
    	if(!VerificadorUtil.verificarSeObjetoNuloOuVazio(cepAntigo)
    			&& cep.replaceAll("\\D", "").equals(cepAntigo.replaceAll("\\D", ""))) {
    		return;
    	}
    	
    	EnderecoBean endereco = CEPUtil.encontraCEP(cep);
    	this.empresa.setBairro(endereco.getBairro());
    	this.empresa.setRua(endereco.getLogradouro());
    	this.empresa.setCidade(endereco.getMunicipio());
    	this.empresa.setEstado(endereco.getUf());
    	cepAntigo = this.empresa.getCep();
    }

    public List<EmpresaBean> listarTodasAsEmpresa() throws ProjetoException {
        return eDao.listarEmpresa();
    }

    private Boolean verificarProgramaPreenchido(Integer idPrograma){
        Boolean retorno = false;

        if(!VerificadorUtil.verificarSeObjetoNulo(idPrograma)) {
            retorno = true;
        }

        return retorno;
    }
    
    public void buscarSituacoes() throws ProjetoException {
        this.listaSituacoes = situacaoAtendimentoDAO.listarSituacaoAtendimento();
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

    public EmpresaBean getEmpresa() {
        return empresa;
    }

    public void setEmpresa(EmpresaBean empresa) {
        this.empresa = empresa;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public ArrayList<String> getListaEstados() {
        return listaEstados;
    }

    public void setListaEstados(ArrayList<String> listaEstados) {
        this.listaEstados = listaEstados;
    }

    public List<GrupoBean> getListaGruposProgramas() {
        return listaGruposProgramas;
    }

    public void setListaGruposProgramas(List<GrupoBean> listaGruposProgramas) {
        this.listaGruposProgramas = listaGruposProgramas;
    }

    public List<EmpresaBean> getListaEmpresas() {
        return listaEmpresas;
    }

    public void setListaEmpresas(List<EmpresaBean> listaEmpresas) {
        this.listaEmpresas = listaEmpresas;
    }

	public List<SituacaoAtendimentoBean> getListaSituacoes() {
		return listaSituacoes;
	}

	public void setListaSituacoes(List<SituacaoAtendimentoBean> listaSituacoes) {
		this.listaSituacoes = listaSituacoes;
	}
    
}
