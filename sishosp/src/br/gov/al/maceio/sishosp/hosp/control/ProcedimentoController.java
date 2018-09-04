package br.gov.al.maceio.sishosp.hosp.control;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import br.gov.al.maceio.sishosp.hosp.dao.RecursoDAO;
import br.gov.al.maceio.sishosp.hosp.model.RecursoBean;
import org.primefaces.context.RequestContext;

import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.hosp.dao.ProcedimentoDAO;
import br.gov.al.maceio.sishosp.hosp.model.CboBean;
import br.gov.al.maceio.sishosp.hosp.model.CidBean;
import br.gov.al.maceio.sishosp.hosp.model.ProcedimentoBean;

@ManagedBean(name = "ProcedimentoController")
@ViewScoped
public class ProcedimentoController implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private ProcedimentoBean proc;
    private List<ProcedimentoBean> listaProcedimentos;
    private Integer tipoBuscar;
    private String descricaoBusca;
    private int tipo;
    private String cabecalho;
    private CidBean cid;
    private CboBean cbo;
    private RecursoBean recurso;
    ProcedimentoDAO pDao = new ProcedimentoDAO();

    public ProcedimentoController() {
        this.proc = new ProcedimentoBean();
        this.listaProcedimentos = null;
        this.descricaoBusca = new String();
        cid = new CidBean();
        cbo = new CboBean();
        recurso = new RecursoBean();
    }

    public void limparDados() throws ProjetoException {
        proc = new ProcedimentoBean();
        listaProcedimentos = new ArrayList<ProcedimentoBean>();
        this.descricaoBusca = new String();
        listaProcedimentos = pDao.listarProcedimento();
        cid = new CidBean();
        cbo = new CboBean();
        recurso = new RecursoBean();
    }

    public void buscarProcedimento() throws ProjetoException {
        this.listaProcedimentos = pDao.listarProcedimentoBusca(descricaoBusca,
                tipoBuscar);
    }

    public void gravarProcedimento() throws ProjetoException, SQLException {

        if (this.proc.getCodProc() == null || this.proc.getNomeProc() == null) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Código e descrição obrigatórios!", "Campos Obrigatórios.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return;
        }

        boolean cadastrou = pDao.gravarProcedimento(proc);

        if (cadastrou == true) {
            limparDados();
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Procedimento cadastrado com sucesso!", "Sucesso");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } else {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Ocorreu um erro durante o cadastro!", "Erro");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }

    public void alterarProcedimento() throws ProjetoException {
        if (this.proc.getCodProc() == null || this.proc.getNomeProc() == null) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Código e descrição obrigatórios!", "Campos Obrigatórios.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            // return "";
        }

        boolean alterou = pDao.alterarProcedimento(proc);
        if (alterou == true) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Procedimento alterado com sucesso!", "Sucesso");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            listaProcedimentos = pDao.listarProcedimento();
            // return
            // "/pages/sishosp/gerenciarProcedimento.faces?faces-redirect=true";
        } else {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Ocorreu um erro durante o cadastro!", "Erro");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            // return "";
        }

    }

    public void excluirProcedimento() throws ProjetoException {
        boolean ok = pDao.excluirProcedimento(proc);
        if (ok == true) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Procedimento excluído com sucesso!", "Sucesso");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().execute(
                    "PF('dialogAtencao').hide();");
        } else {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Ocorreu um erro durante a exclusao!", "Erro");
            FacesContext.getCurrentInstance().addMessage(null, msg);

            RequestContext.getCurrentInstance().execute(
                    "PF('dialogAtencao').hide();");
        }
        listaProcedimentos = pDao.listarProcedimento();
    }

    public void addCid() {
        boolean existe = false;
        if (proc.getListaCid().size() == 0) {
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
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        "Essa CID já foi adicionado!", "Erro");
                FacesContext.getCurrentInstance().addMessage(null, msg);
            }
        }
        cid = new CidBean();
    }

    public void removerCid() {
        proc.getListaCid().remove(cid);
    }

    public void addCbo() {
        boolean existe = false;
        if (proc.getListaCbo().size() == 0) {
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
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        "Essa CBO já foi adicionado!", "Erro");
                FacesContext.getCurrentInstance().addMessage(null, msg);
            }
        }
        cbo = new CboBean();
    }

    public void removerCbo() {
        proc.getListaCbo().remove(cbo);
    }

    public String getCabecalho() {
        if (this.tipo == 1) {
            cabecalho = "Inclusão de Procedimento";
        } else if (this.tipo == 2) {
            cabecalho = "Alteração de Procedimento";
        }
        return cabecalho;
    }

    public void addRecurso() {
        boolean existe = false;
        if (proc.getListaRecurso().size() == 0) {
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
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        "Esse Recurso já foi adicionado!", "Erro");
                FacesContext.getCurrentInstance().addMessage(null, msg);
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

    public List<ProcedimentoBean> listaProcedimentoAutoComplete(String query)
            throws ProjetoException {
        List<ProcedimentoBean> result = pDao.listarProcedimentoBusca(query, 1);
        return result;
    }

    public String redirectInsert() {
        return "cadastroProcedimento?faces-redirect=true&amp;tipo=" + this.tipo;
    }

    public String redirectEdit() {
        return "cadastroProcedimento?faces-redirect=true&amp;id="
                + this.proc.getIdProc() + "&amp;tipo=" + tipo;
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

    public List<ProcedimentoBean> getListaProcedimentos() {
        return listaProcedimentos;
    }

    public ProcedimentoBean getProc() {
        return proc;
    }

    public void setProc(ProcedimentoBean proc) {
        this.proc = proc;
    }

    public void listarProcedimentos() throws ProjetoException {
        this.listaProcedimentos = pDao.listarProcedimento();

    }

    public void setListaProcedimentos(List<ProcedimentoBean> listaProcedimentos) {
        this.listaProcedimentos = listaProcedimentos;
    }

    public Integer getTipoBuscar() {
        return tipoBuscar;
    }

    public void setTipoBuscar(Integer tipoBuscar) {
        this.tipoBuscar = tipoBuscar;
    }

    public String getDescricaoBusca() {
        return descricaoBusca;
    }

    public void setDescricaoBusca(String descricaoBusca) {
        this.descricaoBusca = descricaoBusca;
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
}
