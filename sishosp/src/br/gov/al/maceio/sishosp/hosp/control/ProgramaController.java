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
import br.gov.al.maceio.sishosp.comum.util.VerificadorUtil;
import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.hosp.dao.CboDAO;
import br.gov.al.maceio.sishosp.hosp.dao.GrupoDAO;
import br.gov.al.maceio.sishosp.hosp.dao.ProcedimentoDAO;
import br.gov.al.maceio.sishosp.hosp.dao.ProgramaDAO;
import br.gov.al.maceio.sishosp.hosp.model.CboBean;
import br.gov.al.maceio.sishosp.hosp.model.ProcedimentoBean;
import br.gov.al.maceio.sishosp.hosp.model.ProgramaBean;
import br.gov.al.maceio.sishosp.hosp.model.dto.ProcedimentoCboEspecificoDTO;

@ManagedBean(name = "ProgramaController")
@ViewScoped
public class ProgramaController implements Serializable {

    private static final long serialVersionUID = 1L;
    private ProgramaBean prog;
    private List<ProgramaBean> listaProgramas;
    private List<ProgramaBean> buscalistaProgramas;
    private Integer tipo;
    private String cabecalho;
    private ProgramaDAO pDao = new ProgramaDAO();
    private List<ProgramaBean> listaProgramasUsuario, listaFiltradaProgramasusuario;
    private GrupoDAO gDao = new GrupoDAO();
    private List<ProcedimentoBean> listaProcedimentos;
    private List<CboBean> listaCbos;
    private ProcedimentoDAO procedimentoDAO;
    private CboDAO cboDAO;
    private ProcedimentoBean procedimentoSelecionado;
    private CboBean cboSelecionado;

    //CONSTANTES
    private static final String ENDERECO_CADASTRO = "cadastroPrograma?faces-redirect=true";
    private static final String ENDERECO_TIPO = "&amp;tipo=";
    private static final String ENDERECO_ID = "&amp;id=";
    private static final String CABECALHO_INCLUSAO = "Inclusão de Programa";
    private static final String CABECALHO_ALTERACAO = "Alteração de Programa";

    public ProgramaController() {
        this.prog = new ProgramaBean();
        this.listaProgramas = new ArrayList<ProgramaBean>();;
        buscalistaProgramas = new ArrayList<>();
        listaProgramasUsuario = new ArrayList<ProgramaBean>();
        listaFiltradaProgramasusuario = new ArrayList<ProgramaBean>();
        buscalistaProgramas = null;
        this.listaProcedimentos = new ArrayList<>();
        this.listaCbos = new ArrayList<>();
        this.procedimentoDAO = new ProcedimentoDAO();
        this.cboDAO = new CboDAO();
        this.procedimentoSelecionado = new ProcedimentoBean();
        this.cboSelecionado = new CboBean();
    }

    public String redirectEdit() {
        return RedirecionarUtil.redirectEdit(ENDERECO_CADASTRO, ENDERECO_ID, this.prog.getIdPrograma(), ENDERECO_TIPO, tipo);
    }

    public String redirectInsert() {
        return RedirecionarUtil.redirectInsert(ENDERECO_CADASTRO, ENDERECO_TIPO, tipo);
    }


    public void getEditProg() throws ProjetoException {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Map<String, String> params = facesContext.getExternalContext()
                .getRequestParameterMap();
        if (params.get("id") != null) {
            Integer id = Integer.parseInt(params.get("id"));
            tipo = Integer.parseInt(params.get("tipo"));
            this.prog = pDao.listarProgramaPorId(id);
        } else {
            tipo = Integer.parseInt(params.get("tipo"));
        }

    }

    private void limparDados() throws ProjetoException {
        prog = new ProgramaBean();
        listaProgramas = pDao.listarProgramas();
    }

    public void gravarPrograma() throws ProjetoException {
       /*
    	if (this.prog.getGrupo().isEmpty()) {
            JSFUtil.adicionarMensagemAdvertencia("É necessário ao menos um grupo!", "Advertência");
        } else {
        	*/
        	if(procedimentoPadraoNaoEhNulo()) {
				boolean cadastrou = pDao.gravarPrograma(this.prog);
				if (cadastrou == true) {
					limparDados();
					JSFUtil.adicionarMensagemSucesso("Programa cadastrado com sucesso!", "Sucesso");
				} else {
					JSFUtil.adicionarMensagemErro("Ocorreu um erro durante o cadastro!", "Erro");
				}
				listaProgramas = pDao.listarProgramas();
        	}
       // }
    }
    
    private boolean procedimentoPadraoNaoEhNulo() {
    	if(VerificadorUtil.verificarSeObjetoNuloOuZero(this.prog.getProcedimento().getIdProc())) {
    		JSFUtil.adicionarMensagemAdvertencia("Selecione um procedimento padrão", "");
    		return false;
    	}
    	return true;
    }

    public void alterarPrograma() throws ProjetoException {
        if (this.prog.getGrupo().isEmpty()) {
            JSFUtil.adicionarMensagemAdvertencia("É necessário ao menos um grupo!", "Advertência");
        } else {
            boolean alterou = pDao.alterarPrograma(this.prog);
            if (alterou == true) {
                JSFUtil.adicionarMensagemSucesso("Programa alterado com sucesso!", "Sucesso");
            } else {
                JSFUtil.adicionarMensagemErro("Ocorreu um erro durante a alteração!", "Erro");
            }
        }
    }

    public void excluirPrograma() throws ProjetoException {
        boolean excluiu = pDao.excluirPrograma(prog);
        if (excluiu == true) {
            JSFUtil.adicionarMensagemSucesso("Programa excluído com sucesso!", "Sucesso");
            JSFUtil.fecharDialog("dialogExclusao");
        } else {
            JSFUtil.adicionarMensagemErro("Ocorreu um erro durante a exclusão!", "Erro");
            JSFUtil.fecharDialog("dialogExclusao");
        }
        listaProgramas = pDao.listarProgramas();
    }

    public void carregaListaProgramas() throws ProjetoException {
    	listaProgramas = pDao.listarProgramas();
    }

    public List<ProgramaBean> listaProgramaAutoComplete(String query)
            throws ProjetoException {
        List<ProgramaBean> result = pDao.listarProgramasBusca(query, 1);
        return result;
    }

    public List<ProgramaBean> listaProgramaAutoCompleteUsuario(String query)
            throws ProjetoException {
        List<ProgramaBean> result = pDao.listarProgramasBuscaUsuario(query, 1);
        return result;
    }

    public void carregaListaProgramasUsuario()
            throws ProjetoException {
       listaProgramasUsuario = pDao.listarProgramasUsuario();
    }

    public ProgramaBean getProg() {
        return prog;
    }

    public void setProg(ProgramaBean prog) {
        this.prog = prog;
    } 

    public List<ProgramaBean> getListaProgramas() {
		return listaProgramas;
	}

	public void setListaProgramas(List<ProgramaBean> listaProgramas) {
		this.listaProgramas = listaProgramas;
	}

    public String getCabecalho() {
        if (this.tipo.equals(TipoCabecalho.INCLUSAO.getSigla())) {
            cabecalho = CABECALHO_INCLUSAO;
        } else if (this.tipo.equals(TipoCabecalho.ALTERACAO.getSigla())) {
            cabecalho = CABECALHO_ALTERACAO;
        }
        return cabecalho;
    }
    
    public void listarProcedimentosIhCbos() throws ProjetoException {
    	listarProcedimentos();
    	listarCbo();
    }
    
    private void listarProcedimentos() throws ProjetoException {
    	this.listaProcedimentos = this.procedimentoDAO.listarProcedimento();
    }
    
    private void listarCbo() throws ProjetoException {
    	this.listaCbos = this.cboDAO.listarCbo();
    }
    
    public void limparProcedimentoIhCboSelecionado() {
    	this.procedimentoSelecionado = new ProcedimentoBean();
    	this.cboSelecionado = new CboBean();
    }
    
    public void adicionarProcedimentoCboEspecifico() {
    	ProcedimentoCboEspecificoDTO procedimentoCboEspecifico = new ProcedimentoCboEspecificoDTO();
    	procedimentoCboEspecifico.setProcedimento(this.procedimentoSelecionado);
    	procedimentoCboEspecifico.setCbo(this.cboSelecionado);
    	
    	if(!existeProcedimentoComEsteCbo(procedimentoCboEspecifico)) {
    		this.prog.getListaProcedimentoCboEspecificoDTO().add(procedimentoCboEspecifico);
    		JSFUtil.fecharDialog("dlgConsulProcOcup");
    	}
    	else
    		JSFUtil.adicionarMensagemAdvertencia("Já exite um CBO inserido!", "");
    }
    
    private boolean existeProcedimentoComEsteCbo(ProcedimentoCboEspecificoDTO procedimentoCboEspecifico) {
    	for (ProcedimentoCboEspecificoDTO procedimentoIhCbo : this.prog.getListaProcedimentoCboEspecificoDTO()) {
    		if(procedimentoIhCbo.getCbo().getCodCbo() == procedimentoCboEspecifico.getCbo().getCodCbo()) {
    			return true;    			
    		}
		}
    	return false;
    }
    
    public void removerProcedimentoCboEspecifico(ProcedimentoCboEspecificoDTO procedimentoCboEspecifico) {
    	this.prog.getListaProcedimentoCboEspecificoDTO().remove(procedimentoCboEspecifico);
    }
    
    public void selecionarProcedimento(ProcedimentoBean procedimento) {
    	this.procedimentoSelecionado = procedimento;
    }
    
    public void selecionarCbo(CboBean cbo) {
    	this.cboSelecionado = cbo;
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

    public List<ProgramaBean> getBuscalistaProgramas() throws ProjetoException {
        if (buscalistaProgramas == null) {

            buscalistaProgramas = pDao.BuscalistarProgramas();
        }
        return buscalistaProgramas;
    }

    public void setBuscalistaProgramas(List<ProgramaBean> buscalistaProgramas) {
        this.buscalistaProgramas = buscalistaProgramas;
    }

	public List<ProgramaBean> getListaProgramasUsuario() {
		return listaProgramasUsuario;
	}

	public void setListaProgramasUsuario(List<ProgramaBean> listaProgramasUsuario) {
		this.listaProgramasUsuario = listaProgramasUsuario;
	}

	public List<ProgramaBean> getListaFiltradaProgramasusuario() {
		return listaFiltradaProgramasusuario;
	}

	public void setListaFiltradaProgramasusuario(List<ProgramaBean> listaFiltradaProgramasusuario) {
		this.listaFiltradaProgramasusuario = listaFiltradaProgramasusuario;
	}

	public List<ProcedimentoBean> getListaProcedimentos() {
		return listaProcedimentos;
	}

	public void setListaProcedimentos(List<ProcedimentoBean> listaProcedimentos) {
		this.listaProcedimentos = listaProcedimentos;
	}

	public List<CboBean> getListaCbos() {
		return listaCbos;
	}

	public void setListaCbos(List<CboBean> listaCbos) {
		this.listaCbos = listaCbos;
	}

	public ProcedimentoBean getProcedimentoSelecionado() {
		return procedimentoSelecionado;
	}

	public void setProcedimentoSelecionado(ProcedimentoBean procedimentoSelecionado) {
		this.procedimentoSelecionado = procedimentoSelecionado;
	}

	public CboBean getCboSelecionado() {
		return cboSelecionado;
	}

	public void setCboSelecionado(CboBean cboSelecionado) {
		this.cboSelecionado = cboSelecionado;
	}
}
