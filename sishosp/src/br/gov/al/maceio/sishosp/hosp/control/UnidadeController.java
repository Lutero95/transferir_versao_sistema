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
import br.gov.al.maceio.sishosp.comum.util.JSFUtil;
import br.gov.al.maceio.sishosp.comum.util.RedirecionarUtil;
import br.gov.al.maceio.sishosp.comum.util.VerificadorUtil;
import br.gov.al.maceio.sishosp.hosp.dao.GrupoDAO;
import br.gov.al.maceio.sishosp.hosp.dao.UnidadeDAO;
import br.gov.al.maceio.sishosp.hosp.model.EmpresaBean;
import br.gov.al.maceio.sishosp.hosp.model.GrupoBean;
import br.gov.al.maceio.sishosp.hosp.model.UnidadeBean;

@ManagedBean(name = "UnidadeController")
@ViewScoped
public class UnidadeController implements Serializable {

    private static final long serialVersionUID = 1L;
    private UnidadeBean unidade;
    private Integer tipo;
    private String cabecalho;
    private UnidadeDAO eDao = new UnidadeDAO();
    private ArrayList<String> listaEstados;
    private List<GrupoBean> listaGruposProgramas;
    private GrupoDAO gDao = new GrupoDAO();
    private List<UnidadeBean> listaUnidades;

    //CONSTANTES
    private static final String ENDERECO_CADASTRO = "cadastroEmpresa?faces-redirect=true";
    private static final String ENDERECO_TIPO = "&amp;tipo=";
    private static final String ENDERECO_ID = "&amp;id=";
    private static final String CABECALHO_INCLUSAO = "Inclusão de Empresa";
    private static final String CABECALHO_ALTERACAO = "Alteração de Empresa";

    public UnidadeController() {
        this.unidade = new UnidadeBean();
        this.cabecalho = "";
        listaUnidades = new ArrayList<>();
        listaGruposProgramas = new ArrayList<>();
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
        this.unidade = new UnidadeBean();
    }

    public String redirectEdit() {
        return RedirecionarUtil.redirectEdit(ENDERECO_CADASTRO, ENDERECO_ID, this.unidade.getId(), ENDERECO_TIPO, tipo);
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
            this.unidade = eDao.buscarUnidadePorId(id);
        } else {

            tipo = Integer.parseInt(params.get("tipo"));

        }

    }

    public void gravarUnidade() {

        boolean cadastrou = eDao.gravarUnidade(unidade);

        if (cadastrou == true) {
            limparDados();
            JSFUtil.adicionarMensagemSucesso("Empresa cadastrada com sucesso!", "Sucesso");
        } else {
            JSFUtil.adicionarMensagemErro("Ocorreu um erro durante o cadastro!", "Erro");
        }
    }

    public void alterarEmpresa() {
        boolean alterou = eDao.alterarUnidade(unidade);

        if (alterou == true) {
            JSFUtil.adicionarMensagemSucesso("Empresa alterada com sucesso!", "Sucesso");
        } else {
            JSFUtil.adicionarMensagemErro("Ocorreu um erro durante a alteração!", "Erro");
        }
    }

    public void desativarEmpresa() {
        boolean desativou = eDao.desativarUnidade(unidade);

        if (desativou == true) {
            JSFUtil.adicionarMensagemSucesso("Empresa desativada com sucesso!", "Sucesso");
            JSFUtil.fecharDialog("dialogExclusao");
        } else {
            JSFUtil.adicionarMensagemErro("Ocorreu um erro durante a exclusão!", "Erro");
            JSFUtil.fecharDialog("dialogExclusao");
        }
    }

    public void listarEmpresa() throws ProjetoException {
        listaUnidades = eDao.listarUnidade();
    }

    public List<UnidadeBean> listarTodasAsUnidades() throws ProjetoException {
        return eDao.listarUnidade();
    }

    public List<GrupoBean> listaGrupoAutoCompleteComPrograma(String query)
            throws ProjetoException {
        List<GrupoBean> listaGrupo = new ArrayList<>();

        if(verificarProgramaPreenchido(unidade.getParametro().getOrteseProtese().getPrograma().getIdPrograma())) {
            listaGrupo = gDao.listarGruposNoAutoComplete(query, unidade.getParametro().getOrteseProtese().getPrograma().getIdPrograma());
        }

        return listaGrupo;
    }

    public void listaGrupoPorPrograma(Integer codPrograma)
            throws ProjetoException {

        if(verificarProgramaPreenchido(codPrograma)) {
            listaGruposProgramas = gDao.listarGruposPorPrograma(codPrograma);
        }

    }

    private Boolean verificarProgramaPreenchido(Integer idPrograma){
        Boolean retorno = false;

        if(!VerificadorUtil.verificarSeObjetoNulo(idPrograma)) {
            retorno = true;
        }

        return retorno;
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

   

	public UnidadeBean getUnidade() {
		return unidade;
	}

	public UnidadeDAO geteDao() {
		return eDao;
	}

	public GrupoDAO getgDao() {
		return gDao;
	}

	public List<UnidadeBean> getListaUnidades() {
		return listaUnidades;
	}

	public void setUnidade(UnidadeBean unidade) {
		this.unidade = unidade;
	}

	public void setTipo(Integer tipo) {
		this.tipo = tipo;
	}

	public void seteDao(UnidadeDAO eDao) {
		this.eDao = eDao;
	}

	public void setgDao(GrupoDAO gDao) {
		this.gDao = gDao;
	}

	public void setListaUnidades(List<UnidadeBean> listaUnidades) {
		this.listaUnidades = listaUnidades;
	}
}
