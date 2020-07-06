package br.gov.al.maceio.sishosp.hosp.control;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import br.gov.al.maceio.sishosp.comum.shared.DadosSessao;
import br.gov.al.maceio.sishosp.comum.shared.TelasBuscaSessao;
import br.gov.al.maceio.sishosp.comum.util.*;
import br.gov.al.maceio.sishosp.hosp.dao.*;
import br.gov.al.maceio.sishosp.hosp.enums.ValidacaoSenha;
import br.gov.al.maceio.sishosp.hosp.model.*;
import br.gov.al.maceio.sishosp.hosp.model.dto.BuscaSessaoDTO;
import org.primefaces.event.CellEditEvent;

import br.gov.al.maceio.sishosp.acl.dao.FuncionarioDAO;
import br.gov.al.maceio.sishosp.acl.model.FuncionarioBean;
import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;

import static br.gov.al.maceio.sishosp.comum.shared.DadosSessao.BUSCA_SESSAO;

@ManagedBean(name = "AtendimentoController")
@ViewScoped
public class AtendimentoController implements Serializable {

    private static final long serialVersionUID = 1L;

    private AtendimentoBean atendimento, atendimentoAux;
    private Date periodoInicialEvolucao;
    private Date periodoFinalEvolucao;
    private List<AtendimentoBean> listAtendimentos;
    private List<AtendimentoBean> listAtendimentosEquipe, listAtendimentosEquipeParaExcluir;
    private FuncionarioBean funcionario, funcionarioAux;
    private ProcedimentoBean procedimento;
    private List<ProcedimentoBean> listaProcedimentos;
    private AtendimentoBean atendimentoLista;
    private Boolean primeiraVez;
    private FuncionarioDAO funcionarioDAO = new FuncionarioDAO();
    private AtendimentoDAO atendimentoDAO = new AtendimentoDAO();
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
    private String buscaEvolucao;
    private String buscaTurno;
    private String campoBusca;
    private String opcaoAtendimento;
    private boolean listarEvolucoesPendentes;
    private static final String SIM = "Sim";
    private Integer  idAtendimentos;
    private String ehEquipe;
    private List<SituacaoAtendimentoBean> listaSituacoes;
    private SituacaoAtendimentoDAO situacaoAtendimentoDAO;
    private FuncionarioBean funcionarioLiberacao;
    private AtendimentoBean atendimentoCancelarEvolucao;
    private UnidadeDAO unidadeDAO;

    //CONSTANTES
    private static final String ENDERECO_GERENCIAR_ATENDIMENTOS = "gerenciarAtendimentos?faces-redirect=true";
    //private static final String ENDERECO_PROFISSIONAL_NA_EQUIPE = "atendimentoProfissional01?faces-redirect=true";
    private static final String ENDERECO_PROFISSIONAL = "atendimentoProfissional01?faces-redirect=true";
    private static final String ENDERECO_ATENDIMENTO_PROFISSIONAL = "atendimentoprofissionalnaequipe?faces-redirect=true";
    private static final String ENDERECO_ATENDIMENTO = "atendimento?faces-redirect=true";
    private static final String ENDERECO_ID = "&amp;id=";

    public AtendimentoController() {
        FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
                .getSessionMap().get("obj_funcionario");
        this.periodoInicialEvolucao = null;
        this.periodoFinalEvolucao = null;

        tipoBusca = "nome";
        campoBusca = "";
        this.atendimento = new AtendimentoBean();
        this.atendimentoAux = new AtendimentoBean();
        this.atendimentoLista = null;
        listAtendimentos = new ArrayList<AtendimentoBean>();
        listAtendimentosEquipe = new ArrayList<AtendimentoBean>();
        listAtendimentosEquipeParaExcluir = new ArrayList<AtendimentoBean>();
        funcionario = null;
        funcionarioAux = new FuncionarioBean();
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
        buscaEvolucao = "T";
        buscaTurno = "A";
        this.listaSituacoes = new ArrayList<SituacaoAtendimentoBean>();
        this.situacaoAtendimentoDAO = new SituacaoAtendimentoDAO();
        this.funcionarioLiberacao = new FuncionarioBean();
        unidadeDAO = new UnidadeDAO();
    }

    public void carregarGerenciamentoAtendimento() throws ProjetoException{
        BuscaSessaoDTO buscaSessaoDTO = (BuscaSessaoDTO) SessionUtil.resgatarDaSessao(BUSCA_SESSAO);
        if(!VerificadorUtil.verificarSeObjetoNulo(buscaSessaoDTO)) {
            if (buscaSessaoDTO.getTela().equals(TelasBuscaSessao.GERENCIAR_ATENDIMENTO.getSigla())) {
                atendimento.setGrupo(buscaSessaoDTO.getGrupoBean());
                atendimento.setPrograma(buscaSessaoDTO.getProgramaBean());
                atendimento.setDataAtendimentoInicio(buscaSessaoDTO.getPeriodoInicial());
                atendimento.setDataAtendimentoFinal(buscaSessaoDTO.getPeriodoFinal());
            }
        }

        consultarAtendimentos();

    }

    public void carregarGerenciamentoAtendimentoProfissionalNaEquipe() throws ProjetoException{
        BuscaSessaoDTO buscaSessaoDTO = (BuscaSessaoDTO) SessionUtil.resgatarDaSessao(BUSCA_SESSAO);
        if(!VerificadorUtil.verificarSeObjetoNulo(buscaSessaoDTO)) {
            if (buscaSessaoDTO.getTela().equals(TelasBuscaSessao.GERENCIAR_ATENDIMENTO.getSigla())) {
                atendimento.setGrupo(buscaSessaoDTO.getGrupoBean());
                atendimento.setPrograma(buscaSessaoDTO.getProgramaBean());
                atendimento.setDataAtendimentoInicio(buscaSessaoDTO.getPeriodoInicial());
                atendimento.setDataAtendimentoFinal(buscaSessaoDTO.getPeriodoFinal());
            }
        }

        consultarAtendimentosProfissionalNaEquipe();

    }

    public void excluiProfissionalListaAtendimento(AtendimentoBean atendimento) {
        listAtendimentosEquipeParaExcluir.add(atendimento);
        listAtendimentosEquipe.remove(atendimento);
        JSFUtil.fecharDialog("dlgExclusao");
    }

    public void consultarAtendimentos() throws ProjetoException {
        if (this.atendimento.getDataAtendimentoInicio() == null
                || this.atendimento.getDataAtendimentoFinal() == null) {
            JSFUtil.adicionarMensagemErro("Selecione as datas para filtrar os atendimentos!", "Erro");
            return;
        }
        SessionUtil.adicionarBuscaPtsNaSessao(atendimento.getPrograma(), atendimento.getGrupo(),
                atendimento.getDataAtendimentoInicio(), atendimento.getDataAtendimentoFinal(), TelasBuscaSessao.GERENCIAR_ATENDIMENTO.getSigla());
        listarAtendimentos(campoBusca, tipoBusca);
    }

    public void zeraDatasDeAtendimentoQuandoCondicaoListarEvolucoesPendentesEhAlterada() throws ProjetoException {
        this.atendimento.setDataAtendimentoInicio(null);
        this.atendimento.setDataAtendimentoFinal(null);
        if(listarEvolucoesPendentes) {
            carregarGerenciamentoAtendimentoProfissionalNaEquipe();
        }
    }

    public void consultarAtendimentosProfissionalNaEquipe() throws ProjetoException {
        if(!listarEvolucoesPendentes) {
            if (this.atendimento.getDataAtendimentoInicio() == null
                    || this.atendimento.getDataAtendimentoFinal() == null) {
                JSFUtil.adicionarMensagemErro("Selecione as datas para filtrar os atendimentos!", "Erro");
                return;
            }
        }
        SessionUtil.adicionarBuscaPtsNaSessao(atendimento.getPrograma(), atendimento.getGrupo(),
                atendimento.getDataAtendimentoInicio(), atendimento.getDataAtendimentoFinal(), TelasBuscaSessao.GERENCIAR_ATENDIMENTO.getSigla());
        listarAtendimentosProfissionalNaEquipe(campoBusca, tipoBusca);
    }

    public String redirectGerenciarAtendimentos(String ehEquipe) {
        setaIdEquipeNaSessao(ehEquipe);
        return RedirecionarUtil.redirectEditSemTipo(ENDERECO_GERENCIAR_ATENDIMENTOS, ENDERECO_ID, this.atendimento.getId());
    }

    private void setaIdEquipeNaSessao(String ehEquipe) {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        Map<String, Object> sessionMap = externalContext.getSessionMap();
        sessionMap.put("ehEquipe", ehEquipe);
    }

    public String redirectAtendimentoProfissional(Boolean atendimentoRealizado) {
        if(this.atendimento.getUnidade().getParametro().isBloqueiaPorPendenciaEvolucaoAnterior()) {

            if(quantidadePendenciasEvolucaoAnteriorEhMenorQueUm())
                return RedirecionarUtil.redirectEditSemTipo(ENDERECO_PROFISSIONAL, ENDERECO_ID, this.atendimento.getId());
            return null;
        }
        else {
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("atendimento_realizado", atendimentoRealizado);
            return RedirecionarUtil.redirectEditSemTipo(ENDERECO_PROFISSIONAL, ENDERECO_ID, this.atendimento.getId());
        }
    }

    private boolean quantidadePendenciasEvolucaoAnteriorEhMenorQueUm() {
        FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
                .getSessionMap().get("obj_usuario");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(atendimento.getDataAtendimentoInicio());
        LocalDate dataAtendimento =  LocalDate.of(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).plusMonths(1);
        LocalDate dataAtual = LocalDate.now();
        try {
            Integer quantidadePendenciaEvolucaoAnterior = atendimentoDAO.retornaQuantidadeDePendenciasAnterioresDeEvolucao(
                    atendimento.getUnidade().getId(), user_session.getId());

            if (quantidadePendenciaEvolucaoAnterior == 0)
                return true;
            else
            if ((dataAtendimento.isAfter(dataAtual)) || (dataAtendimento.isEqual(dataAtual)))
                JSFUtil.abrirDialog("dlgErroBloqueioPorPendenciaAnterior");
            else
                return true;

        } catch (ProjetoException e) {
            JSFUtil.adicionarMensagemErro(e.getMessage(), "Erro!");
            e.printStackTrace();
        }
        return false;
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
            this.atendimento = atendimentoDAO.listarAtendimentoProfissionalPaciente(id);
            Boolean atendimentoRealizado = (Boolean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("atendimento_realizado");
            this.atendimento.getSituacaoAtendimento().setAtendimentoRealizado(atendimentoRealizado);
            this.funcionario = funcionarioDAO.buscarProfissionalPorId(valor);
        }
    }

    public void getCarregaGerenciarAtendimentos() throws ProjetoException {

        FacesContext facesContext = FacesContext.getCurrentInstance();
        Map<String, String> params = facesContext.getExternalContext()
                .getRequestParameterMap();
        if ((params.get("id") != null) || (idAtendimentos!=null)) {
            if (idAtendimentos==null)
                idAtendimentos = Integer.parseInt(params.get("id"));
            opcaoAtendimento = HorarioOuTurnoUtil.retornarOpcaoAtendimentoUnidade();
            this.atendimento = atendimentoDAO.listarAtendimentoProfissionalPorId(idAtendimentos);
            listAtendimentosEquipeParaExcluir = new ArrayList<AtendimentoBean>();
            verificarSeRenderizaDialogDeLaudo();
            recuperaIdEquipeDaSessao();
            listarAtendimentosEquipe();
        }
    }

    public void buscarSituacoes() throws ProjetoException {
        this.listaSituacoes = situacaoAtendimentoDAO.listarSituacaoAtendimento();
    }

    public void buscarSituacoesFiltroAtendimentoRealizado() throws ProjetoException {
        this.listaSituacoes = situacaoAtendimentoDAO.listarSituacaoAtendimentoFiltro
                (this.atendimento.getSituacaoAtendimento().getAtendimentoRealizado());
        if ((this.atendimento.getSituacaoAtendimento().getAtendimentoRealizado() && (listaSituacoes.size()==1
        ))){
            atendimento.setSituacaoAtendimento(listaSituacoes.get(0));
        }
    }


    private void recuperaIdEquipeDaSessao() {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        Map<String, Object> sessionMap = externalContext.getSessionMap();
        this.ehEquipe = (String) sessionMap.get("ehEquipe");
    }

    private void verificarSeRenderizaDialogDeLaudo() throws ProjetoException {
        if(VerificadorUtil.verificarSeObjetoNuloOuZero(atendimento.getInsercaoPacienteBean().getLaudo().getId()) && ((atendimento.getAvaliacao()!=null) &&(atendimento.getAvaliacao()==true))){
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
        atendimento.getInsercaoPacienteBean().setLaudo(laudoDAO.buscarLaudosPorId(atendimento.getInsercaoPacienteBean().getLaudo().getId()));
    }

    public String realizarAtendimentoProfissional() throws ProjetoException {
        if (funcionario == null) {
            FuncionarioBean user_session = (FuncionarioBean) FacesContext
                    .getCurrentInstance().getExternalContext().getSessionMap()
                    .get("obj_usuario");
            Integer valor = Integer.valueOf(user_session.getId().toString());
            this.funcionario = funcionarioDAO.buscarProfissionalPorId(valor);
        }

        //comentado enquanto nao tiver a integracao com o datasus boolean verificou = aDao.verificarSeCboEhDoProfissionalPorProfissional(atendimento.getFuncionario().getId(), atendimento.getProcedimento().getIdProc());

        //comentado enquanto nao tiver a integracao com o datasus    if (verificou) {

        boolean alterou = atendimentoDAO.realizaAtendimentoProfissional(funcionario, atendimento);

        if (alterou == true) {
            JSFUtil.adicionarMensagemSucesso("Atendimento realizado com sucesso!", "Sucesso");
            return RedirecionarUtil.redirectPagina(ENDERECO_ATENDIMENTO_PROFISSIONAL);
        } else {
            JSFUtil.adicionarMensagemErro("Ocorreu um erro durante o atendimento!", "Erro");
            return null;
        }
        //comentado enquanto nao tiver a integracao com o datasus    } else {
        //comentado enquanto nao tiver a integracao com o datasus       JSFUtil.adicionarMensagemErro("Esse procedimento não pode ser atendido por um profissional com esse CBO!", "Erro");
        //comentado enquanto nao tiver a integracao com o datasus   }
    }

    public void alterarSituacaoDeAtendimentoPorProfissional() {
        try {
            if(verificarUnidadeEstaConfiguradaParaValidarDadosDoSigtap())
                verificaSeCboProfissionalEhValidoParaProcedimento();
            if (!this.ehEquipe.equalsIgnoreCase(SIM)) {
                if (atendimentoDAO.alteraSituacaoDeAtendimentoPorProfissional
                        (this.listAtendimentosEquipe.get(0).getSituacaoAtendimento().getId(), this.atendimento.getId())) {
                    JSFUtil.adicionarMensagemSucesso("Situação de atendimento alterada com sucesso!", "Sucesso");
                    this.listAtendimentosEquipe.get(0).getSituacaoAtendimentoAnterior().setId(this.listAtendimentosEquipe.get(0).getSituacaoAtendimento().getId());
                }
            }
        } catch (ProjetoException e) {
            JSFUtil.adicionarMensagemErro("Não foi possível atualizar o atendimento, erro: "+e.getMessage(), "");
            e.printStackTrace();
        }
    }

    public Boolean verificarUnidadeEstaConfiguradaParaValidarDadosDoSigtap() throws ProjetoException {
        return unidadeDAO.verificarUnidadeEstaConfiguradaParaValidarDadosDoSigtap();
    }

    public void verificaSeCboProfissionalEhValidoParaProcedimento() throws ProjetoException {
        for(AtendimentoBean atendimento: this.listAtendimentosEquipe) {
            if(!pDao.validaCboProfissionalParaProcedimento
                    (atendimento.getProcedimento().getIdProc(), atendimento.getFuncionario().getId(), atendimento.getDataAtendimento())){
                throw new ProjetoException("O profissional " +
                        atendimento.getFuncionario().getNome()+ " não possui um CBO válido para este procedimento");
            }
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

    public void adicionarPacienteLaudoNaSessao(){
        SessionUtil.adicionarNaSessao(atendimento.getPaciente(), DadosSessao.PACIENTE_LAUDO);
    }

    public void limparAtendimentoProfissional() throws ProjetoException {

        boolean alterou = atendimentoDAO.cancelarAtendimentoProfissional(atendimento);

        if (alterou == true) {
            JSFUtil.adicionarMensagemSucesso("Atendimento limpo com sucesso!", "Sucesso");
        } else {
            JSFUtil.adicionarMensagemErro("Ocorreu um erro durante o atendimento!", "Erro");
        }
    }

    public void insereProfissionalParaRealizarAtendimentoNaEquipe() throws ProjetoException {
        AtendimentoBean atendimentoAux = new AtendimentoBean();
        //boolean gravou = aDao.insereProfissionalParaRealizarAtendimentoNaEquipe(atendimento, funcionarioAux);
        
        atendimentoAux = listAtendimentosEquipe.get(0);

        AtendimentoBean aux = new AtendimentoBean();
        aux.setId(atendimentoAux.getId());
        aux.setCbo(atendimentoAux.getCbo());
        aux.setDataAtendimento(atendimentoAux.getDataAtendimento());
        aux.setProcedimento(atendimento.getProcedimento());
        aux.setFuncionario(funcionarioAux);
        listAtendimentosEquipe.add(aux);
        JSFUtil.adicionarMensagemSucesso("Profissional inserido com sucesso!", "Sucesso");
        JSFUtil.fecharDialog("dlgincprof");
    }

    public void limpaInclusaoProfissionalAtendimento() {
        funcionarioAux = new FuncionarioBean();
    }

    public void listarAtendimentos(String campoBusca, String tipo) throws ProjetoException {
        this.listAtendimentos = atendimentoDAO
                .carregaAtendimentos(atendimento, campoBusca, tipo);
    }

    public void listarAtendimentosProfissionalNaEquipe(String campoBusca, String tipo) throws ProjetoException {
        this.listAtendimentos = atendimentoDAO
                .carregaAtendimentosDoProfissionalNaEquipe(atendimento, campoBusca, tipo, buscaEvolucao, buscaTurno, listarEvolucoesPendentes);
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
            CboBean cbo = new CboBean();
            if (!VerificadorUtil.verificarSeObjetoNuloOuZero(funcionario.getCbo().getCodCbo()))
                cbo = cDao.listarCboPorId(funcionario.getCbo().getCodCbo());

            for (int i = 0; i < listAtendimentosEquipe.size(); i++) {

                if (listAtendimentosEquipe.get(i).getId1() == atendimentoLista
                        .getId1()) {
                    listAtendimentosEquipe.get(i).setFuncionario(funcionario);
                    listAtendimentosEquipe.get(i).setCbo(cbo);
                }
            }

        } else {
            if (primeiraVez) {
                this.listAtendimentosEquipe = atendimentoDAO
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


    private Boolean validarSeEhNecessarioInformarLaudo(){
        Boolean retorno = false;

        if(atendimento.getAvaliacao() && validarDadosDoAtendimentoForamInformados() && VerificadorUtil.verificarSeObjetoNuloOuZero(atendimento.getInsercaoPacienteBean().getLaudo().getId())){
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
                if (VerificadorUtil.verificarSeObjetoNuloOuZero(listAtendimentosEquipe.get(i).getSituacaoAtendimento().getId())) {
                    return false;
                }
            }
        }

        return true;
    }

    public void realizarAtendimentoEquipe() throws ProjetoException {
        if(verificarUnidadeEstaConfiguradaParaValidarDadosDoSigtap())
            verificaSeCboProfissionalEhValidoParaProcedimento();
        if(!validarSeEhNecessarioInformarGrupo()) {
            if(!validarSeEhNecessarioInformarLaudo()) {
                boolean verificou = true; //aDao.verificarSeCboEhDoProfissionalPorEquipe(listAtendimentosEquipe);

                if (verificou) {
                    boolean alterou = atendimentoDAO.realizaAtendimentoEquipe(listAtendimentosEquipe, atendimento.getInsercaoPacienteBean().getLaudo().getId(),
                            atendimento.getGrupoAvaliacao().getIdGrupo(), listAtendimentosEquipeParaExcluir, atendimento.getId());
                    if (alterou) {
                        getCarregaGerenciarAtendimentos();
                        JSFUtil.adicionarMensagemSucesso("Atendimento Gravado com sucesso!", "Sucesso");
                    } else {
                        JSFUtil.adicionarMensagemErro("Ocorreu um erro durante o atendimento!", "Erro");
                    }
                } else {
                    String mensagem = atendimentoDAO.gerarMensagemSeCboNaoEhPermitidoParaProcedimento(listAtendimentosEquipe);
                    JSFUtil.adicionarMensagemErro(mensagem, "Erro");
                }
            }
            else{
                JSFUtil.adicionarMensagemErro("Informe o Laudo da avaliação!", "Erro!");
            }
        }
        else{
            JSFUtil.adicionarMensagemErro("Informe o grupo da avaliação!", "Erro!");
        }
    }

    public void carregarTodasAsEvolucoesDoPaciente(Integer codPaciente) throws ProjetoException {
        if ((periodoFinalEvolucao!=null) || (periodoInicialEvolucao!=null)) {
            if ((periodoFinalEvolucao==null) || (periodoInicialEvolucao==null)) {
                JSFUtil.adicionarMensagemAdvertencia("Informe o período Inicial e Final da Evolução", "Atenção!");
                return;
            }
            else
                listaEvolucoes = atendimentoDAO.carregarTodasAsEvolucoesDoPaciente(codPaciente, periodoInicialEvolucao, periodoFinalEvolucao);
        }
        else
            listaEvolucoes = atendimentoDAO.carregarTodasAsEvolucoesDoPaciente(codPaciente, periodoInicialEvolucao, periodoFinalEvolucao);
    }

    public void carregaEvolucoesPacienteProfissional(Integer codPaciente) throws ProjetoException {
        this.listaEvolucoes = atendimentoDAO.carregarEvolucoesDoPaciente(codPaciente);
    }

    public void carregaEvolucoesPacienteEquipe(Integer idAtendimento) throws ProjetoException {
        this.listaEvolucoes = atendimentoDAO.carregarEvolucoesDoPacientePorEquipe(idAtendimento);
    }

    public void carregarPtsDoPaciente(Integer codPrograma, Integer codGrupo,Integer codPaciente) throws ProjetoException {
        pts = new PtsController().carregarPtsPaciente(codPrograma, codGrupo, codPaciente);
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

    public void limparFuncionarioLiberacao() {
        this.funcionarioLiberacao = new FuncionarioBean();
    }

    public String validarSenhaLiberacao() throws ProjetoException {

        FuncionarioBean funcionario = funcionarioDAO.validarCpfIhSenha(this.funcionarioLiberacao.getCpf(), this.funcionarioLiberacao.getSenha(),
                ValidacaoSenha.LIBERACAO.getSigla());

        if (!VerificadorUtil.verificarSeObjetoNulo(funcionario)) {
            atendimentoDAO.cancelarEvolucaoAtendimentoPorProfissional(atendimentoCancelarEvolucao, funcionario);

            List<AtendimentoBean> listaAtendimentoProfissionalNaEquipeAux = new ArrayList<AtendimentoBean>();
            listaAtendimentoProfissionalNaEquipeAux.addAll(this.listAtendimentos);

            this.listAtendimentos.remove(atendimentoCancelarEvolucao);

            if(this.listAtendimentosEquipe.isEmpty() && listaAtendimentoProfissionalNaEquipeAux.isEmpty())
                return RedirecionarUtil.redirectPagina(ENDERECO_ATENDIMENTO);

            else if (!this.listAtendimentosEquipe.isEmpty())
                this.listAtendimentosEquipe = atendimentoDAO.carregaAtendimentosEquipe(idAtendimentos);

            JSFUtil.fecharDialog("dlgLiberacao");
            JSFUtil.adicionarMensagemSucesso("Evolução cancelada", "Sucesso");
        }
        else
            JSFUtil.adicionarMensagemErro("Funcionário com senha errada ou sem permissão!", "Erro!");
        return null;
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

    public String getOpcaoAtendimento() {
        return opcaoAtendimento;
    }

    public void setOpcaoAtendimento(String opcaoAtendimento) {
        this.opcaoAtendimento = opcaoAtendimento;
    }

    public String getBuscaEvolucao() {
        return buscaEvolucao;
    }

    public void setBuscaEvolucao(String buscaEvolucao) {
        this.buscaEvolucao = buscaEvolucao;
    }

    public String getBuscaTurno() {
        return buscaTurno;
    }

    public void setBuscaTurno(String buscaTurno) {
        this.buscaTurno = buscaTurno;
    }

    public FuncionarioBean getFuncionarioAux() {
        return funcionarioAux;
    }

    public void setFuncionarioAux(FuncionarioBean funcionarioAux) {
        this.funcionarioAux = funcionarioAux;
    }

    public AtendimentoBean getAtendimentoAux() {
        return atendimentoAux;
    }

    public void setAtendimentoAux(AtendimentoBean atendimentoAux) {
        this.atendimentoAux = atendimentoAux;
    }

    public Date getDataAtendimentoC() {
        return periodoInicialEvolucao;
    }

    public Date getDataAtendimentoFinalC() {
        return periodoFinalEvolucao;
    }

    public void setDataAtendimentoC(Date dataAtendimentoC) {
        this.periodoInicialEvolucao = dataAtendimentoC;
    }

    public void setDataAtendimentoFinalC(Date dataAtendimentoFinalC) {
        this.periodoFinalEvolucao = dataAtendimentoFinalC;
    }

    public Date getPeriodoInicialEvolucao() {
        return periodoInicialEvolucao;
    }

    public Date getPeriodoFinalEvolucao() {
        return periodoFinalEvolucao;
    }

    public void setPeriodoInicialEvolucao(Date periodoInicialEvolucao) {
        this.periodoInicialEvolucao = periodoInicialEvolucao;
    }

    public void setPeriodoFinalEvolucao(Date periodoFinalEvolucao) {
        this.periodoFinalEvolucao = periodoFinalEvolucao;
    }

    public boolean getListarEvolucoesPendentes() {
        return listarEvolucoesPendentes;
    }

    public void setListarEvolucoesPendentes(boolean listarEvolucoesPendentes) {
        this.listarEvolucoesPendentes = listarEvolucoesPendentes;
    }

    public String getEhEquipe() {
        return ehEquipe;
    }

    public void setEhEquipe(String ehEquipe) {
        this.ehEquipe = ehEquipe;
    }

    public List<SituacaoAtendimentoBean> getListaSituacoes() {
        return listaSituacoes;
    }

    public void setListaSituacoes(List<SituacaoAtendimentoBean> listaSituacoes) {
        this.listaSituacoes = listaSituacoes;
    }

    public FuncionarioBean getFuncionarioLiberacao() {
        return funcionarioLiberacao;
    }

    public void setFuncionarioLiberacao(FuncionarioBean funcionarioLiberacao) {
        this.funcionarioLiberacao = funcionarioLiberacao;
    }

    public AtendimentoBean getAtendimentoCancelarEvolucao() {
        return atendimentoCancelarEvolucao;
    }

    public void setAtendimentoCancelarEvolucao(AtendimentoBean atendimentoCancelarEvolucao) {
        this.atendimentoCancelarEvolucao = atendimentoCancelarEvolucao;
    }
}
