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
import org.primefaces.event.CellEditEvent;

import br.gov.al.maceio.sishosp.acl.dao.FuncionarioDAO;
import br.gov.al.maceio.sishosp.acl.model.FuncionarioBean;
import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.hosp.dao.AtendimentoDAO;
import br.gov.al.maceio.sishosp.hosp.dao.CboDAO;
import br.gov.al.maceio.sishosp.hosp.dao.ProcedimentoDAO;
import br.gov.al.maceio.sishosp.hosp.model.AtendimentoBean;
import br.gov.al.maceio.sishosp.hosp.model.CboBean;
import br.gov.al.maceio.sishosp.hosp.model.ProcedimentoBean;

@ManagedBean(name = "AtendimentoController")
@ViewScoped
public class AtendimentoController implements Serializable {

    private static final long serialVersionUID = 1L;

    private AtendimentoBean atendimento;
    private List<AtendimentoBean> listAtendimentos;
    private List<AtendimentoBean> listAtendimentosEquipe;
    private FuncionarioBean funcionario;
    private ProcedimentoBean procedimento;
    private List<ProcedimentoBean> listaProcedimentos;
    private AtendimentoBean atendimentoLista;
    private Boolean primeiraVez;
    private FuncionarioDAO fDao = new FuncionarioDAO();
    private AtendimentoDAO aDao = new AtendimentoDAO();
    private ProcedimentoDAO pDao = new ProcedimentoDAO();

    //CONSTANTES
    private static final String ENDERECO_EQUIPE = "atendimentoEquipe?faces-redirect=true";
    private static final String ENDERECO_PROFISSIONAL = "atendimentoProfissional?faces-redirect=true";
    private static final String ENDERECO_ID = "&amp;id=";

    public AtendimentoController() {
        this.atendimento = new AtendimentoBean();
        this.atendimentoLista = null;
        listAtendimentos = new ArrayList<AtendimentoBean>();
        listAtendimentosEquipe = new ArrayList<AtendimentoBean>();
        funcionario = null;
        procedimento = new ProcedimentoBean();
        listaProcedimentos = new ArrayList<ProcedimentoBean>();
        primeiraVez = true;

    }

    public void consultarAtendimentos() throws ProjetoException {
        if (this.atendimento.getDataAtendimentoInicio() == null
                || this.atendimento.getDataAtendimentoFinal() == null) {
            JSFUtil.adicionarMensagemErro("Selecione as datas para filtrar os atendimentos!", "Erro");
            return;
        }
        listarAtendimentos();
    }

    public String redirectAtendimento() {
        if (atendimento.getEhEquipe().equals("Sim")) {
            return RedirecionarUtil.redirectEditSemTipo(ENDERECO_EQUIPE, ENDERECO_ID, this.atendimento.getId());
        } else {
            return RedirecionarUtil.redirectEditSemTipo(ENDERECO_PROFISSIONAL, ENDERECO_ID, this.atendimento.getId());
        }
    }

    public void getCarregaAtendimentoProfissional() throws ProjetoException {
        FuncionarioBean user_session = (FuncionarioBean) FacesContext
                .getCurrentInstance().getExternalContext().getSessionMap()
                .get("obj_usuario");

        FacesContext facesContext = FacesContext.getCurrentInstance();
        Map<String, String> params = facesContext.getExternalContext()
                .getRequestParameterMap();
        if (params.get("id") != null) {
            Integer id = Integer.parseInt(params.get("id"));

            Integer valor = Integer.valueOf(user_session.getId().toString());
            this.atendimento = aDao.listarAtendimentoProfissionalPorId(id);
            this.funcionario = fDao.buscarProfissionalPorId(valor);
        }
    }

    public void getCarregaAtendimentoEquipe() throws ProjetoException {

        FacesContext facesContext = FacesContext.getCurrentInstance();
        Map<String, String> params = facesContext.getExternalContext()
                .getRequestParameterMap();
        if (params.get("id") != null) {
            Integer id = Integer.parseInt(params.get("id"));

            this.atendimento = aDao.listarAtendimentoProfissionalPorId(id);
        }
    }

    public void realizarAtendimentoProfissional() throws ProjetoException {
        if (funcionario == null) {
            FuncionarioBean user_session = (FuncionarioBean) FacesContext
                    .getCurrentInstance().getExternalContext().getSessionMap()
                    .get("obj_usuario");
            Integer valor = Integer.valueOf(user_session.getId().toString());
            this.funcionario = fDao.buscarProfissionalPorId(valor);
        }

        boolean verificou = aDao.verificarSeCboEhDoProfissionalPorProfissional(atendimento.getFuncionario().getId(), atendimento.getProcedimento().getIdProc());

        if (verificou) {

            boolean alterou = aDao.realizaAtendimentoProfissional(funcionario,
                    atendimento);

            if (alterou == true) {
                JSFUtil.adicionarMensagemSucesso("Atendimento realizado com sucesso!", "Sucesso");
            } else {
                JSFUtil.adicionarMensagemErro("Ocorreu um erro durante o atendimento!", "Erro");
            }
        } else {
            JSFUtil.adicionarMensagemErro("Esse procedimento não pode ser atendido por um profissional com esse CBO!", "Erro");
        }
    }

    public void limparAtendimentoProfissional() throws ProjetoException {

        boolean alterou = aDao.limpaAtendimentoProfissional(atendimento);

        if (alterou == true) {
            JSFUtil.adicionarMensagemSucesso("Atendimento limpo com sucesso!", "Sucesso");
        } else {
            JSFUtil.adicionarMensagemErro("Ocorreu um erro durante o atendimento!", "Erro");
        }
    }

    public void listarAtendimentos() throws ProjetoException {
        this.listAtendimentos = aDao.carregaAtendimentos(atendimento);
    }

    public void chamarMetodoTabelaAtendimentoEquipe() throws ProjetoException {
        primeiraVez = false;
        listarAtendimentosEquipe();

        atendimento = new AtendimentoBean();
        procedimento = new ProcedimentoBean();
        funcionario = null;

        JSFUtil.fecharDialog("dlgConsultProfi");
        JSFUtil.fecharDialog("dlgConsulProc1");
    }

    public List<AtendimentoBean> listarAtendimentosEquipe()
            throws ProjetoException {

        if (atendimento.getStatus() != null) {
            if (!atendimento.getStatus().equals("")) {

                for (int i = 0; i < listAtendimentosEquipe.size(); i++) {

                    if (listAtendimentosEquipe.get(i).getId1() == atendimentoLista
                            .getId1()) {
                        listAtendimentosEquipe.get(i).setStatus(
                                atendimento.getStatus());
                        ;
                    }
                }
            }
        }

        if (procedimento.getIdProc() != null) {
            if (procedimento.getIdProc() > 0) {

                for (int i = 0; i < listAtendimentosEquipe.size(); i++) {

                    if (listAtendimentosEquipe.get(i).getId1() == atendimentoLista
                            .getId1()) {
                        listAtendimentosEquipe.get(i).setProcedimento(
                                procedimento);
                    }
                }
            }
        }

        if (funcionario != null) {

            CboDAO cDao = new CboDAO();
            CboBean cbo = cDao.listarCboPorId(funcionario.getCbo().getCodCbo());

            for (int i = 0; i < listAtendimentosEquipe.size(); i++) {

                if (listAtendimentosEquipe.get(i).getId1() == atendimentoLista
                        .getId1()) {
                    listAtendimentosEquipe.get(i).setFuncionario(funcionario);
                    listAtendimentosEquipe.get(i).setCbo(cbo);
                }
            }

        } else {
            if (primeiraVez) {
                this.listAtendimentosEquipe = aDao
                        .carregaAtendimentosEquipe(atendimento.getId());
            }
        }
        return this.listAtendimentosEquipe;

    }

    public void onCellEdit(CellEditEvent event) {

        Object oldValue = event.getOldValue();
        Object newValue = event.getNewValue();

        if (newValue != null && !newValue.equals(oldValue)) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Clique em SALVAR para que a alteração seja gravada!",
                    "Old: " + oldValue + ", New:" + newValue);
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }

    public List<ProcedimentoBean> listarProcedimentos() throws ProjetoException {
        return this.listaProcedimentos = pDao.listarProcedimento();

    }

    public void realizarAtendimentoEquipe() throws ProjetoException {
        boolean verificou = aDao.verificarSeCboEhDoProfissionalPorEquipe(listAtendimentosEquipe);

        if (verificou) {
            boolean alterou = aDao.realizaAtendimentoEquipe(listAtendimentosEquipe);
            if (alterou == true) {
                JSFUtil.adicionarMensagemSucesso("Atendimento realizado com sucesso!", "Sucesso");
            } else {
                JSFUtil.adicionarMensagemErro("Ocorreu um erro durante o atendimento!", "Erro");
            }
        } else {
            JSFUtil.adicionarMensagemErro("Esse procedimento não pode ser atendido por um profissional com esse CBO!", "Erro");
        }
    }


    public AtendimentoBean getAtendimento() {
        return atendimento;
    }

    public void setAtendimento(AtendimentoBean atendimento) {
        this.atendimento = atendimento;
    }

    public List<AtendimentoBean> getListAtendimentos() {
        return listAtendimentos;
    }

    public void setListAtendimentos(List<AtendimentoBean> listAtendimentos) {
        this.listAtendimentos = listAtendimentos;
    }

    public FuncionarioBean getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(FuncionarioBean funcionario) {
        this.funcionario = funcionario;
    }

    public List<AtendimentoBean> getListAtendimentosEquipe() {
        return listAtendimentosEquipe;
    }

    public void setListAtendimentosEquipe(
            List<AtendimentoBean> listAtendimentosEquipe) {
        this.listAtendimentosEquipe = listAtendimentosEquipe;
    }

    public ProcedimentoBean getProcedimento() {
        return procedimento;
    }

    public void setProcedimento(ProcedimentoBean procedimento) {
        this.procedimento = procedimento;
    }

    public AtendimentoBean getAtendimentoLista() {
        return atendimentoLista;
    }

    public void setAtendimentoLista(AtendimentoBean atendimentoLista) {
        this.atendimentoLista = atendimentoLista;
    }

}
