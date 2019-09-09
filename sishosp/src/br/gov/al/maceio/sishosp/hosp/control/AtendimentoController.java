package br.gov.al.maceio.sishosp.hosp.control;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import br.gov.al.maceio.sishosp.comum.util.DataUtil;
import br.gov.al.maceio.sishosp.comum.util.JSFUtil;
import br.gov.al.maceio.sishosp.comum.util.RedirecionarUtil;
import br.gov.al.maceio.sishosp.comum.util.VerificadorUtil;
import br.gov.al.maceio.sishosp.hosp.dao.*;
import br.gov.al.maceio.sishosp.hosp.model.*;
import org.primefaces.event.CellEditEvent;

import br.gov.al.maceio.sishosp.acl.dao.FuncionarioDAO;
import br.gov.al.maceio.sishosp.acl.model.FuncionarioBean;
import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;

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
    private GrupoDAO gDao = new GrupoDAO();
    private Integer idAtendimento1;
    private List<AtendimentoBean> listaEvolucoes;
    private PacienteBean paciente;
    private Pts pts;
    private Boolean renderizarDialogLaudo;
    private ArrayList<InsercaoPacienteBean> listaLaudosVigentes;
    private List<GrupoBean> listaGrupos;
	private String tipoBusca;
	private String campoBusca;

    //CONSTANTES
    private static final String ENDERECO_EQUIPE = "atendimentoEquipe?faces-redirect=true";
    private static final String ENDERECO_PROFISSIONAL = "atendimentoProfissional?faces-redirect=true";
    private static final String ENDERECO_ID = "&amp;id=";

    public AtendimentoController() {
        FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
                .getSessionMap().get("obj_funcionario");

        this.atendimento = new AtendimentoBean();
        this.atendimentoLista = null;
        listAtendimentos = new ArrayList<AtendimentoBean>();
        listAtendimentosEquipe = new ArrayList<AtendimentoBean>();
        funcionario = null;
        procedimento = new ProcedimentoBean();
        listaProcedimentos = new ArrayList<ProcedimentoBean>();
        primeiraVez = true;
        listaEvolucoes = new ArrayList<>();
        atendimento.getUnidade().setId(user_session.getUnidade().getId());
        atendimento.setDataAtendimentoInicio(DataUtil.retornarDataAtual());
        atendimento.setDataAtendimentoFinal(DataUtil.retornarDataAtual());
        paciente = new PacienteBean();
        pts = new Pts();
        renderizarDialogLaudo = false;
        listaGrupos = new ArrayList<>();
    }


    public void consultarAtendimentos() throws ProjetoException {
        if (this.atendimento.getDataAtendimentoInicio() == null
                || this.atendimento.getDataAtendimentoFinal() == null) {
            JSFUtil.adicionarMensagemErro("Selecione as datas para filtrar os atendimentos!", "Erro");
            return;
        }
        listarAtendimentos(campoBusca, tipoBusca);
    }

    public String redirectAtendimento() {
        if (atendimento.getEhEquipe().equals("Sim") || atendimento.getAvaliacao()) {
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

            verificarSeRenderizaDialogDeLaudo();

            listarAtendimentosEquipe();
        }
    }

    private void verificarSeRenderizaDialogDeLaudo() throws ProjetoException {
        if(VerificadorUtil.verificarSeObjetoNuloOuZero(atendimento.getInsercaoPacienteBean().getLaudo().getId()) && (VerificadorUtil.verificarSeObjetoNuloOuZero(atendimento.getAvaliacao()))){
            renderizarDialogLaudo = true;
            listarLaudosVigentesPaciente();
        }
        else{
        	if (atendimento.getInsercaoPacienteBean().getLaudo().getId()!=null)
            carregarDadosLaudo();
        }
    }

    private void carregarDadosLaudo() throws ProjetoException {

    	LaudoDAO laudoDAO = new LaudoDAO();
        atendimento.getInsercaoPacienteBean().setLaudo(laudoDAO.listarLaudosVigentesPorId(atendimento.getInsercaoPacienteBean().getLaudo().getId()));
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

    public void abrirDialogAtendimentoPorEquipe(){
        carregarEvolucaoSelecionada();
        JSFUtil.abrirDialog("dlgEvolucao");
        JSFUtil.atualizarComponente("formEvolucao");
    }

    public void carregarEvolucaoSelecionada(){
        for(int i=0; i<listAtendimentosEquipe.size(); i++){
            if(listAtendimentosEquipe.get(i).getId1() == idAtendimento1){
                atendimento.setEvolucao(listAtendimentosEquipe.get(i).getEvolucao());
            }
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

    public void listarAtendimentos(String campoBusca, String tipo) throws ProjetoException {
        this.listAtendimentos = aDao
                .carregaAtendimentos(atendimento, campoBusca, tipo);
    }

    public void chamarMetodoTabelaAtendimentoEquipe() throws ProjetoException {
        primeiraVez = false;
        listarAtendimentosEquipe();

        //atendimento = new AtendimentoBean();
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

    public void adicionarEvolucaoAtendimentoEquipe() {

        for(int i=0; i<listAtendimentosEquipe.size(); i++){
            if(listAtendimentosEquipe.get(i).getId1() == idAtendimento1){
                listAtendimentosEquipe.get(i).setEvolucao(atendimento.getEvolucao());
            }
        }

        JSFUtil.fecharDialog("dlgEvolucao");
    }

    public void onCellEdit(CellEditEvent event) {

        Object oldValue = event.getOldValue();
        Object newValue = event.getNewValue();

        if (newValue != null && !newValue.equals(oldValue)) {
            JSFUtil.adicionarMensagemAdvertencia("Clique em SALVAR para que a alteração seja gravada!",
                    "Old: " + oldValue + ", New:" + newValue);
        }
    }

    public void listarProcedimentos() throws ProjetoException {
        this.listaProcedimentos = pDao.listarProcedimento();
    }

    private Boolean validarSeEhNecessarioInformarGrupo(){
        Boolean retorno = false;

        if(atendimento.getAvaliacao() && validarDadosDoAtendimentoForamInformados() && VerificadorUtil.verificarSeObjetoNuloOuZero(
                atendimento.getGrupoAvaliacao().getIdGrupo())){
            retorno = true;
        }

        return retorno;
    }

    private Boolean validarDadosDoAtendimentoForamInformados() {

        if(atendimento.getAvaliacao()){
            for (int i = 0; i < listAtendimentosEquipe.size(); i++) {
                if (VerificadorUtil.verificarSeObjetoNuloOuVazio(listAtendimentosEquipe.get(i).getPerfil())) {
                    return false;
                }
            }
        }
        else {
            for (int i = 0; i < listAtendimentosEquipe.size(); i++) {
                if (VerificadorUtil.verificarSeObjetoNuloOuVazio(listAtendimentosEquipe.get(i).getStatus())) {
                    return false;
                }
            }
        }

        return true;
    }

    public void realizarAtendimentoEquipe() throws ProjetoException {
        if(!validarSeEhNecessarioInformarGrupo()) {
            boolean verificou = true; //aDao.verificarSeCboEhDoProfissionalPorEquipe(listAtendimentosEquipe);

            if (verificou) {
                boolean alterou = aDao.realizaAtendimentoEquipe(listAtendimentosEquipe, atendimento.getInsercaoPacienteBean().getLaudo().getId(),
                        atendimento.getGrupoAvaliacao().getIdGrupo());
                if (alterou) {
                    JSFUtil.adicionarMensagemSucesso("Atendimento realizado com sucesso!", "Sucesso");
                } else {
                    JSFUtil.adicionarMensagemErro("Ocorreu um erro durante o atendimento!", "Erro");
                }
            } else {
                String mensagem = aDao.gerarMensagemSeCboNaoEhPermitidoParaProcedimento(listAtendimentosEquipe);
                JSFUtil.adicionarMensagemErro(mensagem, "Erro");
            }
        }
        else{
            JSFUtil.adicionarMensagemErro("Informe o grupo da avaliação!", "Erro!");
        }
    }

    public void carregarEvolucoesPaciente(Integer codPaciente) throws ProjetoException {
        listaEvolucoes = aDao.carregarEvolucoesDoPaciente(codPaciente);
    }

    public void carregarPtsDoPaciente(Integer codPaciente) throws ProjetoException {
        pts = new PtsController().carregarPtsPaciente(codPaciente);
    }

    public void listarLaudosVigentesPaciente()
            throws ProjetoException {
        LaudoDAO laudoDAO = new LaudoDAO();
        listaLaudosVigentes = laudoDAO.listarLaudosVigentesParaPaciente(atendimento.getPaciente().getId_paciente());
    }

    public List<GrupoBean> listaGrupoAutoComplete(String query)
            throws ProjetoException {
        if (atendimento.getPrograma().getIdPrograma() != null) {
            return gDao.listarGruposNoAutoComplete(query,
                    this.atendimento.getPrograma().getIdPrograma());
        } else {
            return null;
        }

    }

    public void listarGruposPorProgramas() throws ProjetoException {
        listaGrupos = gDao.listarGruposPorPrograma(atendimento.getPrograma().getIdPrograma());
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

    public Integer getIdAtendimento1() {
        return idAtendimento1;
    }

    public void setIdAtendimento1(Integer idAtendimento1) {
        this.idAtendimento1 = idAtendimento1;
    }


	public List<ProcedimentoBean> getListaProcedimentos() {
		return listaProcedimentos;
	}


	public void setListaProcedimentos(List<ProcedimentoBean> listaProcedimentos) {
		this.listaProcedimentos = listaProcedimentos;
	}

    public List<AtendimentoBean> getListaEvolucoes() {
        return listaEvolucoes;
    }

    public void setListaEvolucoes(List<AtendimentoBean> listaEvolucoes) {
        this.listaEvolucoes = listaEvolucoes;
    }

    public PacienteBean getPaciente() {
        return paciente;
    }

    public void setPaciente(PacienteBean paciente) {
        this.paciente = paciente;
    }

    public Pts getPts() {
        return pts;
    }

    public void setPts(Pts pts) {
        this.pts = pts;
    }

    public Boolean getRenderizarDialogLaudo() {
        return renderizarDialogLaudo;
    }

    public void setRenderizarDialogLaudo(Boolean renderizarDialogLaudo) {
        this.renderizarDialogLaudo = renderizarDialogLaudo;
    }

    public ArrayList<InsercaoPacienteBean> getListaLaudosVigentes() {
        return listaLaudosVigentes;
    }

    public void setListaLaudosVigentes(ArrayList<InsercaoPacienteBean> listaLaudosVigentes) {
        this.listaLaudosVigentes = listaLaudosVigentes;
    }

    public List<GrupoBean> getListaGrupos() {
        return listaGrupos;
    }

    public void setListaGrupos(List<GrupoBean> listaGrupos) {
        this.listaGrupos = listaGrupos;
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
