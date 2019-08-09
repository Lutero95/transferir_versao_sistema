package br.gov.al.maceio.sishosp.hosp.control;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import br.gov.al.maceio.sishosp.comum.enums.TipoCabecalho;
import br.gov.al.maceio.sishosp.comum.util.JSFUtil;
import br.gov.al.maceio.sishosp.comum.util.RedirecionarUtil;
import br.gov.al.maceio.sishosp.hosp.dao.RecursoDAO;
import br.gov.al.maceio.sishosp.hosp.model.RecursoBean;

import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.hosp.dao.ProcedimentoDAO;
import br.gov.al.maceio.sishosp.hosp.model.CboBean;
import br.gov.al.maceio.sishosp.hosp.model.CidBean;
import br.gov.al.maceio.sishosp.hosp.model.ProcedimentoBean;

@ManagedBean(name = "ProcedimentoController")
@ViewScoped
public class ProcedimentoController implements Serializable {

    private static final long serialVersionUID = 1L;
    private ProcedimentoBean proc;
    private List<ProcedimentoBean> listaProcedimentos;
    private Integer tipo;
    private String cabecalho;
    private CidBean cid;
    private CboBean cbo;
    private RecursoBean recurso;
    private ProcedimentoDAO pDao = new ProcedimentoDAO();
    private String tipoBusca;
    private String campoBusca;

    //CONSTANTES
    private static final String ENDERECO_CADASTRO = "cadastroProcedimento?faces-redirect=true";
    private static final String ENDERECO_TIPO = "&amp;tipo=";
    private static final String ENDERECO_ID = "&amp;id=";
    private static final String CABECALHO_INCLUSAO = "Inclusão de Procedimento";
    private static final String CABECALHO_ALTERACAO = "Alteração de Procedimento";

    public ProcedimentoController() {
        this.proc = new ProcedimentoBean();
        this.listaProcedimentos = null;
        cid = new CidBean();
        cbo = new CboBean();
        recurso = new RecursoBean();
    }

    public String redirectEdit() {
        return RedirecionarUtil.redirectEdit(ENDERECO_CADASTRO, ENDERECO_ID, this.proc.getIdProc(), ENDERECO_TIPO, tipo);
    }

    public String redirectInsert() {
        return RedirecionarUtil.redirectInsert(ENDERECO_CADASTRO, ENDERECO_TIPO, tipo);
    }

    public void getEditProcedimento() throws ProjetoException {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Map<String, String> params = facesContext.getExternalContext()
                .getRequestParameterMap();
        if (params.get("id") != null) {
            Integer id = Integer.parseInt(params.get("id"));
            tipo = Integer.parseInt(params.get("tipo"));
            this.proc = pDao.listarProcedimentoPorId(id);
            proc.setListaCid(pDao.listarCid(id));
            proc.setListaCbo(pDao.listarCbo(id));
            RecursoDAO rDao = new RecursoDAO();
            proc.setListaRecurso(rDao.listaRecursosPorProcedimento(id));
        } else {
            tipo = Integer.parseInt(params.get("tipo"));

        }

    }

    public void limparDados() throws ProjetoException {
        proc = new ProcedimentoBean();
        listaProcedimentos = new ArrayList<ProcedimentoBean>();
        listaProcedimentos = pDao.listarProcedimento();
        cid = new CidBean();
        cbo = new CboBean();
        recurso = new RecursoBean();
    }

    public void gravarProcedimento() throws ProjetoException, SQLException {

        boolean cadastrou = pDao.gravarProcedimento(proc);

        if (cadastrou == true) {
            limparDados();
            JSFUtil.adicionarMensagemSucesso("Procedimento cadastrado com sucesso!", "Sucesso");
        } else {
            JSFUtil.adicionarMensagemErro("Ocorreu um erro durante o cadastro!", "Erro");
        }
    }

    public void alterarProcedimento() throws ProjetoException {

        boolean alterou = pDao.alterarProcedimento(proc);

        if (alterou == true) {
            JSFUtil.adicionarMensagemSucesso("Procedimento alterado com sucesso!", "Sucesso");
            listaProcedimentos = pDao.listarProcedimento();
        } else {
            JSFUtil.adicionarMensagemErro("Ocorreu um erro durante a alteração!", "Erro");
        }

    }

    public void excluirProcedimento() throws ProjetoException {

        boolean excluiu = pDao.excluirProcedimento(proc);

        if (excluiu == true) {
            JSFUtil.adicionarMensagemSucesso("Procedimento excluído com sucesso!", "Sucesso");
            JSFUtil.fecharDialog("dialogExclusao");
        } else {
            JSFUtil.adicionarMensagemErro("Ocorreu um erro durante a exclusão!", "Erro");
            JSFUtil.fecharDialog("dialogExclusao");
        }
        listaProcedimentos = pDao.listarProcedimento();
    }

    public void addCid() {
        boolean existe = false;
        if (proc.getListaCid().size() == 0 && cid.getIdCid() > 0) {
            proc.getListaCid().add(cid);
        } else {
            for (int i = 0; i < proc.getListaCid().size(); i++) {
                if (proc.getListaCid().get(i).getIdCid() == cid.getIdCid()) {
                    existe = true;
                }
            }
            if (existe == false) {
                this.proc.getListaCid().add(this.cid);
            } else {
                JSFUtil.adicionarMensagemAdvertencia("Essa CID já foi adicionado!", "Advertência");
            }
        }
        cid = new CidBean();
    }

    public void removerCid() {
        proc.getListaCid().remove(cid);
    }

    public void addCbo() {
        boolean existe = false;
        if (proc.getListaCbo().size() == 0 && cbo.getCodCbo() > 0) {
            proc.getListaCbo().add(cbo);
        } else {
            for (int i = 0; i < proc.getListaCbo().size(); i++) {
                if (proc.getListaCbo().get(i).getCodCbo() == cbo.getCodCbo()) {
                    existe = true;
                }
            }
            if (existe == false) {
                this.proc.getListaCbo().add(this.cbo);
            } else {
                JSFUtil.adicionarMensagemAdvertencia("Essa CBO já foi adicionado!", "Advertência");
            }
        }
        cbo = new CboBean();
    }

    public void removerCbo() {
        proc.getListaCbo().remove(cbo);
    }

    public String getCabecalho() {
        if (this.tipo.equals(TipoCabecalho.INCLUSAO.getSigla())) {
            cabecalho = CABECALHO_INCLUSAO;
        } else if (this.tipo.equals(TipoCabecalho.ALTERACAO.getSigla())) {
            cabecalho = CABECALHO_ALTERACAO;
        }
        return cabecalho;
    }

    public void addRecurso() {
        boolean existe = false;
        if (proc.getListaRecurso().size() == 0 && recurso.getIdRecurso() > 0) {
            proc.getListaRecurso().add(recurso);
        } else {
            for (int i = 0; i < proc.getListaRecurso().size(); i++) {
                if (proc.getListaRecurso().get(i).getIdRecurso() == recurso.getIdRecurso()) {
                    existe = true;
                }
            }
            if (existe == false) {
                this.proc.getListaRecurso().add(this.recurso);
            } else {
                JSFUtil.adicionarMensagemAdvertencia("Essa Recurso já foi adicionado!", "Advertência");
            }
        }
        recurso = new RecursoBean();
    }

    public void removerRecurso() {
        proc.getListaRecurso().remove(recurso);
    }

    public void setCabecalho(String cabecalho) {
        this.cabecalho = cabecalho;
    }
    
    public List<ProcedimentoBean> listaProcedimentoQueGeramLaudoAutoComplete(String query)
            throws ProjetoException {
        List<ProcedimentoBean> result = pDao.listarProcedimentoQueGeramLaudoBusca(query, 1);
        return result;
    }

    public List<ProcedimentoBean> listaProcedimentoAutoComplete(String query)
            throws ProjetoException {
        List<ProcedimentoBean> result = pDao.listarProcedimentoBusca(query, 1);
        return result;
    }

    public void buscarProcedimentos() throws ProjetoException {
        this.listaProcedimentos = pDao.listarProcedimento();

    }

    public void listarProcedimentos() throws ProjetoException {
        this.listaProcedimentos = pDao.listarProcedimento();
        
    }

    public void buscarProcedimentosPorTipo() throws ProjetoException {
        this.listaProcedimentos = pDao.buscarProcedimento(campoBusca, tipoBusca);

    }

    public void limparCampoBusca(){
        campoBusca = "";
    }
    
    public void listarProcedimentosQueGeramLaudo() throws ProjetoException {
        this.listaProcedimentos = pDao.listarProcedimentoLaudo();
        
    }

    public List<ProcedimentoBean> getListaProcedimentos() {
        return listaProcedimentos;
    }

    public ProcedimentoBean getProc() {
        return proc;
    }

    public void setProc(ProcedimentoBean proc) {
        this.proc = proc;
    }

    public void setListaProcedimentos(List<ProcedimentoBean> listaProcedimentos) {
        this.listaProcedimentos = listaProcedimentos;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public CidBean getCid() {
        return cid;
    }

    public void setCid(CidBean cid) {
        this.cid = cid;
    }

    public CboBean getCbo() {
        return cbo;
    }

    public void setCbo(CboBean cbo) {
        this.cbo = cbo;
    }

    public RecursoBean getRecurso() {
        return recurso;
    }

    public void setRecurso(RecursoBean recurso) {
        this.recurso = recurso;
    }

    public String getTipoBusca() {
        return tipoBusca;
    }

    public void setTipoBusca(String tipoBusca) {
        this.tipoBusca = tipoBusca;
    }

    public String getCampoBusca() {
        return campoBusca;
    }

    public void setCampoBusca(String campoBusca) {
        this.campoBusca = campoBusca;
    }
}
