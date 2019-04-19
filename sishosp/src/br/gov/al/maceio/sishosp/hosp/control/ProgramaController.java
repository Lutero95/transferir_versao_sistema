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
import br.gov.al.maceio.sishosp.hosp.dao.GrupoDAO;
import br.gov.al.maceio.sishosp.hosp.dao.ProgramaDAO;
import br.gov.al.maceio.sishosp.hosp.model.ProgramaBean;

@ManagedBean(name = "ProgramaController")
@ViewScoped
public class ProgramaController implements Serializable {

    private static final long serialVersionUID = 1L;
    private ProgramaBean prog;
    private List<ProgramaBean> listaProgramas;
    private List<ProgramaBean> buscalistaProgramas;
    private int tipo;
    private String cabecalho;
    private ProgramaDAO pDao = new ProgramaDAO();
    private List<ProgramaBean> listaProgramasUsuario;
    private GrupoDAO gDao = new GrupoDAO();

    //CONSTANTES
    private static final String ENDERECO_CADASTRO = "cadastroPrograma?faces-redirect=true";
    private static final String ENDERECO_TIPO = "&amp;tipo=";
    private static final String ENDERECO_ID = "&amp;id=";
    private static final String CABECALHO_INCLUSAO = "Inclusão de Programa";
    private static final String CABECALHO_ALTERACAO = "Alteração de Programa";

    public ProgramaController() {
    	System.out.println("construror ProgramaController");
        this.prog = new ProgramaBean();
        this.listaProgramas = new ArrayList<ProgramaBean>();;
        buscalistaProgramas = new ArrayList<>();
        listaProgramasUsuario = new ArrayList<ProgramaBean>();
        buscalistaProgramas = null;
    }

    public String redirectEdit() {
        return RedirecionarUtil.redirectEdit(ENDERECO_CADASTRO, ENDERECO_ID, this.prog.getIdPrograma(), ENDERECO_TIPO, tipo);
    }

    public String redirectInsert() {
        return RedirecionarUtil.redirectInsert(ENDERECO_CADASTRO, ENDERECO_TIPO, tipo);
    }


    public void getEditProg() throws ProjetoException {
    	System.out.println("getEditProg");
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Map<String, String> params = facesContext.getExternalContext()
                .getRequestParameterMap();
        if (params.get("id") != null) {
            Integer id = Integer.parseInt(params.get("id"));
            tipo = Integer.parseInt(params.get("tipo"));
            this.prog = pDao.listarProgramaPorId(id);
            prog.setGrupo(gDao.listarGruposDoPrograma(prog.getIdPrograma()));
        } else {
            tipo = Integer.parseInt(params.get("tipo"));
        }

    }

    public void limparDados() throws ProjetoException {
    	System.out.println("limparDados programacontroller");
        prog = new ProgramaBean();
        listaProgramas = pDao.listarProgramas();
    }

    public void gravarPrograma() throws ProjetoException {
    	System.out.println("gravarPrograma");
        if (this.prog.getGrupo().isEmpty()) {
            JSFUtil.adicionarMensagemAdvertencia("É necessário ao menos um grupo!", "Advertência");
        } else {
            boolean cadastrou = pDao.gravarPrograma(prog);

            if (cadastrou == true) {
                limparDados();
                JSFUtil.adicionarMensagemSucesso("Programa cadastrado com sucesso!", "Sucesso");
            } else {
                JSFUtil.adicionarMensagemErro("Ocorreu um erro durante o cadastro!", "Erro");
            }
            listaProgramas = pDao.listarProgramas();
        }
    }

    public void alterarPrograma() {
    	System.out.println("alterarPrograma");
        if (this.prog.getGrupo().isEmpty()) {
            JSFUtil.adicionarMensagemAdvertencia("É necessário ao menos um grupo!", "Advertência");
        } else {
            boolean alterou = pDao.alterarPrograma(prog);
            if (alterou == true) {
                JSFUtil.adicionarMensagemSucesso("Programa alterado com sucesso!", "Sucesso");
            } else {
                JSFUtil.adicionarMensagemErro("Ocorreu um erro durante a alteração!", "Erro");
            }
        }
    }

    public void excluirPrograma() throws ProjetoException {
    	System.out.println("excluirPrograma");
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
    	System.out.println("listarProgramas");
    	listaProgramas = pDao.listarProgramas();
    }

    public List<ProgramaBean> listaProgramaAutoComplete(String query)
            throws ProjetoException {
    	System.out.println("listaProgramaAutoComplete");
        List<ProgramaBean> result = pDao.listarProgramasBusca(query, 1);
        return result;
    }

    public List<ProgramaBean> listaProgramaAutoCompleteUsuario(String query)
            throws ProjetoException {
    	System.out.println("listaProgramaAutoCompleteUsuario");
        List<ProgramaBean> result = pDao.listarProgramasBuscaUsuario(query, 1);
        return result;
    }

    public void carregaListaProgramasUsuario()
            throws ProjetoException {
    	System.out.println("carregaListaProgramasUsuario");
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
        if (this.tipo == 1) {
            cabecalho = CABECALHO_INCLUSAO;
        } else if (this.tipo == 2) {
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

    public List<ProgramaBean> getBuscalistaProgramas() throws ProjetoException {
    	System.out.println("getBuscalistaProgramas");
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

}
