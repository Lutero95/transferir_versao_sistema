package br.gov.al.maceio.sishosp.hosp.control;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import br.gov.al.maceio.sishosp.comum.shared.DadosSessao;
import br.gov.al.maceio.sishosp.comum.shared.TelasBuscaSessao;
import br.gov.al.maceio.sishosp.comum.util.*;
import br.gov.al.maceio.sishosp.hosp.dao.*;
import br.gov.al.maceio.sishosp.hosp.enums.MotivoLiberacao;
import br.gov.al.maceio.sishosp.hosp.enums.TipoBuscaPaciente;
import br.gov.al.maceio.sishosp.hosp.enums.ValidacaoSenha;
import br.gov.al.maceio.sishosp.hosp.model.*;
import br.gov.al.maceio.sishosp.hosp.model.dto.BuscaSessaoDTO;
import br.gov.al.maceio.sishosp.hosp.model.dto.PendenciaEvolucaoProgramaGrupoDTO;
import br.gov.al.maceio.sishosp.hosp.model.dto.ProcedimentoCidDTO;

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
    private List<AtendimentoBean> listAtendimentos, listaAtendimentosFiltro, listaAtendimentosSelecionados;
    private List<AtendimentoBean> listAtendimentosEquipe, listAtendimentosEquipeParaExcluir;
    private FuncionarioBean funcionario, funcionarioAux;
    private ProcedimentoBean procedimento;
    private List<ProcedimentoBean> listaProcedimentos;
    private AtendimentoBean atendimentoLista;
    private Boolean primeiraVez;
    private FuncionarioDAO funcionarioDAO = new FuncionarioDAO();
    private AtendimentoDAO atendimentoDAO = new AtendimentoDAO();
    private ProcedimentoDAO procedimentoDAO = new ProcedimentoDAO();
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
    private String campoBusca, campoBuscaCid;
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
    private boolean unidadeValidaDadosSigtap;
    private Boolean existeAlgumaCargaSigtap;
    private Boolean podeEditarEvolucaoNestaData;
    private Boolean semCids;
    private CidDAO cidDao;
    private List<CidBean> listaCids;
    private Date dataAtende;
    private Boolean existeCargaSigtapParaEsteMesOuAnterior;
    private EspecialidadeBean especialidade;
    private List<PendenciaEvolucaoProgramaGrupoDTO> listaPendenciasEvolucaoProgramaGrupo;
    private String visualizouDialogPendencias;
    private Integer totalPendenciaEvolucao;
    private String descricaoEvolucaoPadrao;
    private ProcedimentoCidDTO procedimentoCidSelecionado;
    private boolean alteracaoEmMassa;
    private CidBean cid;
    private String confirmacaoDialog;
    private String palavrasChaveDivergentes;
    private CboBean cboSelecionado;
    private List<CboBean> listaCbos;
    private boolean selecionarTodos;
    private List<EquipeBean> listaEquipe;
    private String textoEvolucaoAuxiliar;

    // os ultimos atributos foram adicionados - Martinho

    private EspecialidadeDAO eDao = new EspecialidadeDAO();
    private List<EspecialidadeBean> listaEspecialidadesEquipe;
    private GerenciarPacienteDAO gPDao = new GerenciarPacienteDAO();
    private String identificadorEspecialidade;

    private Date dataFiltroEspecialidade;

    //CONSTANTES
    private static final String ENDERECO_GERENCIAR_ATENDIMENTOS = "gerenciarAtendimentos?faces-redirect=true";
    //private static final String ENDERECO_PROFISSIONAL_NA_EQUIPE = "atendimentoProfissional01?faces-redirect=true";
    private static final String ENDERECO_PROFISSIONAL = "atendimentoProfissional01?faces-redirect=true";
    private static final String ENDERECO_ATENDIMENTO_PROFISSIONAL = "atendimentoprofissionalnaequipe?faces-redirect=true";
    private static final String ENDERECO_ATENDIMENTO = "atendimento?faces-redirect=true";
    private static final String ENDERECO_ID = "&amp;id=";
    private static final String EH_EVOLUCAO = "ehEvolucao";
    private static final String NAO_EH_EVOLUCAO = "naoEhEvolucao";

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
        this.procedimento = new ProcedimentoBean();
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
        this.semCids = false;
        this.cidDao = new CidDAO();
        this.listaCids = new ArrayList<>();
        especialidade = new EspecialidadeBean();
        this.listaPendenciasEvolucaoProgramaGrupo = new ArrayList<>();
        this.visualizouDialogPendencias = new String();
        this.procedimentoCidSelecionado = new ProcedimentoCidDTO();
        this.listaAtendimentosFiltro = new ArrayList<>();
        this.listaAtendimentosSelecionados = new ArrayList<>();
        this.confirmacaoDialog = "";
        this.palavrasChaveDivergentes = "";
        this.cboSelecionado = new CboBean();
        this.listaCbos = new ArrayList<>();
    }

    public void carregarGerenciamentoAtendimento() throws ProjetoException{
        BuscaSessaoDTO buscaSessaoDTO = (BuscaSessaoDTO) SessionUtil.resgatarDaSessao(BUSCA_SESSAO);
        if(!VerificadorUtil.verificarSeObjetoNulo(buscaSessaoDTO)) {
            if (buscaSessaoDTO.getTela().equals(TelasBuscaSessao.GERENCIAR_ATENDIMENTO.getSigla())) {
                atendimento.setGrupo(buscaSessaoDTO.getGrupoBean());
                atendimento.setPrograma(buscaSessaoDTO.getProgramaBean());
                atendimento.setEquipe(buscaSessaoDTO.getEquipeBean());
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
                atendimento.setEquipe(buscaSessaoDTO.getEquipeBean());
                atendimento.setDataAtendimentoInicio(buscaSessaoDTO.getPeriodoInicial());
                atendimento.setDataAtendimentoFinal(buscaSessaoDTO.getPeriodoFinal());
                if  (buscaSessaoDTO.getCampoBusca()!=null)
                    this.campoBusca = buscaSessaoDTO.getCampoBusca();
                if ( buscaSessaoDTO.getTipoBusca()!=null)
                    this.tipoBusca = buscaSessaoDTO.getTipoBusca();
                this.buscaEvolucao = buscaSessaoDTO.getBuscaEvolucao();
                this.listarEvolucoesPendentes = buscaSessaoDTO.isListarEvolucoesPendentes();
            }
        }

        consultarAtendimentosProfissionalNaEquipe();

    }

    public void excluiProfissionalListaAtendimento(AtendimentoBean atendimento) throws ProjetoException {
    	if(!atendimentoPossuiSituacaoAtendimento(atendimento.getId1())
    		|| (VerificadorUtil.verificarSeObjetoNuloOuZero(atendimento.getId1()))) {
    		
    		listAtendimentosEquipeParaExcluir.add(atendimento);
    		listAtendimentosEquipe.remove(atendimento);
    		JSFUtil.fecharDialog("dlgExclusao");
    	}
    }
    
    private boolean atendimentoPossuiSituacaoAtendimento(Integer idAtendimento1) throws ProjetoException {
    	if(!VerificadorUtil.verificarSeObjetoNuloOuZero(idAtendimento1) &&
    			atendimentoDAO.atendimentoPossuiSituacaoAtendimento(idAtendimento1)) {
    			JSFUtil.adicionarMensagemErro
    			("Não é possível excluir este registro pois já foi informada alguma situação de Atendimento", "");
    		return true;
    	}
    	return false;
    }

    public void consultarAtendimentos() throws ProjetoException {
        if (this.atendimento.getDataAtendimentoInicio() == null
                || this.atendimento.getDataAtendimentoFinal() == null) {
            JSFUtil.adicionarMensagemErro("Selecione as datas para filtrar os atendimentos!", "Erro");
            return;
        }
        SessionUtil.adicionarBuscaPtsNaSessao(atendimento.getPrograma(), atendimento.getGrupo(), atendimento.getEquipe(),
                atendimento.getDataAtendimentoInicio(), atendimento.getDataAtendimentoFinal(), TelasBuscaSessao.GERENCIAR_ATENDIMENTO.getSigla());
        listarAtendimentos(campoBusca, tipoBusca);
    }

    public void zeraDadosQuandoCondicaoListarEvolucoesPendentesEhAlterada() throws ProjetoException {
        this.atendimento.setDataAtendimentoInicio(null);
        this.atendimento.setDataAtendimentoFinal(null);
        this.atendimento.setPrograma(null);
        this.atendimento.setGrupo(null);
        this. buscaEvolucao = "T";
        this. buscaTurno = "A";
        this. campoBusca = "";
        SessionUtil.removerDaSessao(BUSCA_SESSAO);
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
        SessionUtil.adicionarBuscaPtsNaSessao(atendimento.getPrograma(), atendimento.getGrupo(), atendimento.getEquipe(),
                atendimento.getDataAtendimentoInicio(), atendimento.getDataAtendimentoFinal(),
                TelasBuscaSessao.GERENCIAR_ATENDIMENTO.getSigla(), this.campoBusca, this.tipoBusca,
                this.buscaEvolucao, this.listarEvolucoesPendentes);

        listarAtendimentosProfissionalNaEquipe(campoBusca, tipoBusca);
    }

    public String redirectGerenciarAtendimentos(String ehEquipe) {
        //setaIdEquipeNaSessao(ehEquipe);
    	setaIdEquipeNaSessao(SIM);
        return RedirecionarUtil.redirectEditSemTipo(ENDERECO_GERENCIAR_ATENDIMENTOS, ENDERECO_ID, this.atendimento.getId());
    }

    private void setaIdEquipeNaSessao(String ehEquipe) {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        Map<String, Object> sessionMap = externalContext.getSessionMap();
        sessionMap.put("ehEquipe", ehEquipe);
    }

    public String redirectAtendimentoProfissional(Boolean atendimentoRealizado) {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("atendimento_realizado", atendimentoRealizado);
        FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
                .getSessionMap().get("obj_usuario");
        
        if(!ehAbonoFalta(atendimento)) {
            if (this.atendimento.getUnidade().getParametro().isBloqueiaPorPendenciaEvolucaoAnterior()
            		&& !user_session.isExcecaoEvolucaoComPendencia()) {
            	
                if (quantidadePendenciasEvolucaoAnteriorEhMenorQueUm()) {
                    return RedirecionarUtil.redirectEditSemTipo(ENDERECO_PROFISSIONAL, ENDERECO_ID,
                            this.atendimento.getId());
                }
                JSFUtil.adicionarMensagemAdvertencia("Acesso bloqueado por pendências de evolução anterior", "");
                return null;
            } else {
                return RedirecionarUtil.redirectEditSemTipo(ENDERECO_PROFISSIONAL, ENDERECO_ID,
                        this.atendimento.getId());
            }
        }
        return null;
    }

    private boolean ehAbonoFalta(AtendimentoBean atendimento) {
        if(atendimento.getSituacaoAtendimento().isAbonoFalta()) {
            JSFUtil.adicionarMensagemErro("Não é possível Evoluir um Atendimento Abonado por Falta", "");
            return true;
        }
        return false;
    }

    private boolean quantidadePendenciasEvolucaoAnteriorEhMenorQueUm() {
        FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
                .getSessionMap().get("obj_usuario");

        try {
            Integer quantidadePendenciaEvolucaoAnterior = atendimentoDAO.retornaQuantidadeDePendenciasAnterioresDeEvolucao(
                    atendimento.getUnidade().getId(), user_session.getId(), atendimento.getDataAtendimentoInicio());

            if (quantidadePendenciaEvolucaoAnterior == 0)
                return true;
            else {
                JSFUtil.abrirDialog("dlgErroBloqueioPorPendenciaAnterior");
                carregarGerenciamentoAtendimentoProfissionalNaEquipe();
            }

        } catch (ProjetoException e) {
            JSFUtil.adicionarMensagemErro(e.getMessage(), "Erro!");
            //comentado walter erro log ex.printStackTrace();
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
        verificarUnidadeEstaConfiguradaParaValidarDadosDoSigtap();
        verificaSeExisteAlgumaCargaSigtap();

        if (params.get("id") != null) {
            Integer id = Integer.parseInt(params.get("id"));

            Integer valor = Integer.valueOf(user_session.getId().toString());
            this.atendimento = atendimentoDAO.listarAtendimentoProfissionalPaciente(id);

            this.dataAtende = this.atendimento.getDataAtendimentoInicio();

            if(this.unidadeValidaDadosSigtap) {
                dataAtende = setaAtendimentoValidadoPeloSigtapAnterior(dataAtende);
                existeCargaSigtapParaEsteMesOuAnterior(dataAtende, EH_EVOLUCAO);
            }

            Boolean atendimentoRealizado = (Boolean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("atendimento_realizado");
            this.atendimento.getSituacaoAtendimento().setAtendimentoRealizado(atendimentoRealizado);
            this.funcionario = funcionarioDAO.buscarProfissionalPorId(valor);
            listarTodosTiposProcedimentos();
            evolucaoFoiRealizadaNaDataAnterior(atendimento);
            listaEspecialidadesEquipe = eDao.listarEspecialidadesEvolucaoEmTerapia();
        }
    }

    public void getCarregaGerenciarAtendimentos() throws ProjetoException {

        FacesContext facesContext = FacesContext.getCurrentInstance();
        Map<String, String> params = facesContext.getExternalContext().getRequestParameterMap();
        verificarUnidadeEstaConfiguradaParaValidarDadosDoSigtap();
        verificaSeExisteAlgumaCargaSigtap();

        if ((params.get("id") != null) || (idAtendimentos!=null)) {
            if (idAtendimentos==null)
                idAtendimentos = Integer.parseInt(params.get("id"));
            opcaoAtendimento = HorarioOuTurnoUtil.retornarOpcaoAtendimentoUnidade();
            this.atendimento = atendimentoDAO.listarAtendimentoProfissionalPorId(idAtendimentos);

            this.dataAtende = this.atendimento.getDataAtendimentoInicio();

            if(this.unidadeValidaDadosSigtap) {
                dataAtende = setaAtendimentoValidadoPeloSigtapAnterior(dataAtende);
                existeCargaSigtapParaEsteMesOuAnterior(dataAtende, EH_EVOLUCAO);
            }

            listAtendimentosEquipeParaExcluir = new ArrayList<AtendimentoBean>();
            verificarSeRenderizaDialogDeLaudo();
            recuperaIdEquipeDaSessao();
            listarAtendimentosEquipe();
        }
    }

    public Date getDataAtende() {
        return dataAtende;
    }

    public void setDataAtende(Date dataAtende) {
        this.dataAtende = dataAtende;
    }

    public void executaMetodosInicializadoresAjustesAtendimentos() throws ProjetoException {
        verificarUnidadeEstaConfiguradaParaValidarDadosDoSigtap();
        verificaSeExisteAlgumaCargaSigtap();
        this.atendimentoAux.setDataAtendimentoInicio(this.atendimento.getDataAtendimentoInicio());
        this.atendimentoAux.setDataAtendimentoFinal(this.atendimento.getDataAtendimentoFinal());
    }

    private void verificarUnidadeEstaConfiguradaParaValidarDadosDoSigtap() throws ProjetoException {
        this.unidadeValidaDadosSigtap = unidadeDAO.verificarUnidadeEstaConfiguradaParaValidarDadosDoSigtap();
    }

    private void verificaSeExisteAlgumaCargaSigtap() {
        if(this.unidadeValidaDadosSigtap) {
            this.existeAlgumaCargaSigtap = procedimentoDAO.verificaSeExisteAlgumaCargaSigtap();
            if (!this.existeAlgumaCargaSigtap)
                JSFUtil.adicionarMensagemAdvertencia("NÃO É POSSÍVEL GRAVAR OS DADOS DO ATENDIMENTO POIS NENHUMA CARGA DO SIGTAP FOI REALIZADA", "");
        }
        else
            this.existeAlgumaCargaSigtap = true;
    }

    private void existeCargaSigtapParaEsteMesOuAnterior(Date dataAtende, String ehEvolucao) throws ProjetoException {
        this.existeCargaSigtapParaEsteMesOuAnterior = procedimentoDAO.houveCargaDoSigtapEsteMes(DataUtil.extrairMesDeData(dataAtende), DataUtil.extrairAnoDeData(dataAtende));
        if(!existeCargaSigtapParaEsteMesOuAnterior) {
            if(ehEvolucao.equals(EH_EVOLUCAO))
                JSFUtil.adicionarMensagemAdvertencia("Não é possível evoluir o atendimento pois não existem Dados do Sigtap para o mês/ano do atendimento", "");
            else
                JSFUtil.adicionarMensagemAdvertencia("Não é possível alterar o cid do atendimento pois não existem Dados do Sigtap para o mês/ano do atendimento", "");
        }
    }

    public void buscarSituacoes() throws ProjetoException {
        this.listaSituacoes = situacaoAtendimentoDAO.listarSituacaoAtendimento();
    }

    public void buscarSituacoesFiltroAtendimentoRealizado() throws ProjetoException {
        this.listaSituacoes = situacaoAtendimentoDAO.listarSituacaoAtendimentoFiltro
                (this.atendimento.getSituacaoAtendimento().getAtendimentoRealizado());
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
    	
    	this.palavrasChaveDivergentes = "";
    	String textoEvolucaoFormatado = retornaTextoEvolucaoSemVirgulaIhEspacoDuplo();
    	List<String> palavrasIncompativeis = new PalavraChaveEvolucaoDAO().existePalavraDigitadaDeOutroTipoDeAtendimento
    			(atendimento.getSituacaoAtendimento().getId());
    	boolean existePalavrasDivergentes = verificaExisteTextoIncompativelEvolucao(textoEvolucaoFormatado, palavrasIncompativeis);
    	
    	if(this.confirmacaoDialog.equals("N")) {
    		JSFUtil.fecharDialog("dlgPalavraChave");
    		this.confirmacaoDialog = "";
    		return null;
    	}
    	
    	else if(existePalavrasDivergentes && 
    			VerificadorUtil.verificarSeObjetoNuloOuVazio(this.confirmacaoDialog)) {
    		this.confirmacaoDialog = "";
    		JSFUtil.abrirDialog("dlgPalavraChave");
    		return null;
    	} 
    	
    	else {
    		this.confirmacaoDialog = "";
			if (funcionario == null) {
				FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
						.getSessionMap().get("obj_usuario");
				Integer valor = Integer.valueOf(user_session.getId().toString());
				this.funcionario = funcionarioDAO.buscarProfissionalPorId(valor);
			}

			buscarDadosProcedimentoPorId();
			validarDadosSigtap();
			buscarDadosCidPorId();
			validarCidSigtap();
			validarProcedimentoCidsSecundariosSigtap();
			if(evolucaoAtendimentoEhPermitida(atendimento)) {
				boolean alterou = atendimentoDAO.realizaAtendimentoProfissional(funcionario, atendimento,
						palavrasChaveDivergentes);
				if (alterou == true) {
					JSFUtil.adicionarMensagemSucesso("Atendimento realizado com sucesso!", "Sucesso");
					JSFUtil.fecharDialog("dlgPalavraChave");
					return RedirecionarUtil.redirectPagina(ENDERECO_ATENDIMENTO_PROFISSIONAL);
				} else {
					JSFUtil.adicionarMensagemErro("Ocorreu um erro durante o atendimento!", "Erro");
					return null;
				}
			}
			return null;
    	}
    }
    
    private boolean evolucaoAtendimentoEhPermitida(AtendimentoBean atendimento) throws ProjetoException {
    	FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
    			.getSessionMap().get("obj_usuario");
    	
    	if(user_session.getUnidade().getParametro().isVerificaPeriodoInicialEvolucaoPrograma() 
    			&& !atendimentoDAO.verificaEvolucaoAtendimentoEhPermitida(atendimento)) {
    		JSFUtil.adicionarMensagemAdvertencia
    			("As configurações de evolução não permitem a evolução de atendimentos neste período", "");
    		return false;
    	}
    	else if(!user_session.getUnidade().getParametro().isVerificaPeriodoInicialEvolucaoPrograma()
    			&& atendimento.getDataAtendimento().before(user_session.getUnidade().getParametro().getInicioEvolucaoUnidade())) {
    		JSFUtil.adicionarMensagemAdvertencia
    				("As configurações de evolução não permitem a evolução de atendimentos neste período", "");
    		return false;    		
    	}
    	return true;
    }
    
    private boolean evolucaoAtendimentoEhPermitida(List<AtendimentoBean> listaAtendimento) throws ProjetoException {
    	FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
    			.getSessionMap().get("obj_usuario");
    	
    	for (AtendimentoBean atendimentoEquipe : listaAtendimento) {
    		if(user_session.getUnidade().getParametro().isVerificaPeriodoInicialEvolucaoPrograma() 
    				&& !atendimentoDAO.verificaEvolucaoAtendimentoEhPermitida(atendimentoEquipe)) {
    			JSFUtil.adicionarMensagemAdvertencia
    			("As configurações de evolução não permitem a evolução do atendimento do profissional "
    			+atendimentoEquipe.getFuncionario().getNome()+" neste período", "");
    			return false;
    		}
        	else if(!user_session.getUnidade().getParametro().isVerificaPeriodoInicialEvolucaoPrograma()
        			&& atendimentoEquipe.getDataAtendimento().before(user_session.getUnidade().getParametro().getInicioEvolucaoUnidade())) {
    			JSFUtil.adicionarMensagemAdvertencia
    			("As configurações de evolução não permitem a evolução do atendimento do profissional "
    			+atendimentoEquipe.getFuncionario().getNome()+" neste período", "");
        		return false;    		
        	}
		}
    	return true;
    }

	private String retornaTextoEvolucaoSemVirgulaIhEspacoDuplo() {
        String textoEvolucao=  atendimento.getEvolucao().replace(",", "").replaceAll("\\s+", " ").replace("<p>", "").replace("</p>", "");
        textoEvolucao= org.apache.commons.lang3.StringUtils.stripAccents(textoEvolucao);
        return textoEvolucao;
	}
	
	private boolean verificaExisteTextoIncompativelEvolucao(String textoEvolucao, List<String> palavrasChaveIncompativeis) {
		
		boolean existe = false;
		
		for (String palavraChave : palavrasChaveIncompativeis) {
			palavraChave = palavraChave.replace(",", "").replaceAll("\\s+", " ");
			if(textoEvolucao.toUpperCase().contains(palavraChave.toUpperCase())) {
				palavrasChaveDivergentes += palavraChave+=", ";
				existe = true;
			}
		}
		
		return existe;
	}

    public void alterarSituacaoDeAtendimentoPorProfissional() {
        try {

            if (VerificadorUtil.verificarSeObjetoNuloOuVazio(this.ehEquipe) || !this.ehEquipe.equalsIgnoreCase(SIM)) {
            	if(evolucaoAtendimentoEhPermitida(atendimento)) {
					if (atendimentoDAO.alteraSituacaoDeAtendimentoPorProfissional(
							this.listAtendimentosEquipe.get(0).getSituacaoAtendimento().getId(), this.atendimento)) {
						JSFUtil.adicionarMensagemSucesso("Situação de atendimento alterada com sucesso!", "Sucesso");
						listarAtendimentosEquipe();
					}
            	}
            }
        } catch (ProjetoException e) {
            JSFUtil.adicionarMensagemErro("Não foi possível atualizar o atendimento, erro: "+e.getMessage(), "");
            //comentado walter erro log ex.printStackTrace();
        }
    }

    private void validarDadosSigtap() throws ProjetoException {
        if(this.unidadeValidaDadosSigtap) {
            Date dataSolicitacaoPeloSigtap = dataAtende;

            if(!existeCargaSigtapParaDataSolicitacao(dataAtende)) {
                dataAtende= DataUtil.retornaDataComMesAnterior(atendimento.getDataAtendimento());
            }

            LaudoController laudoController = new LaudoController();
            laudoController.idadeValida(dataAtende, this.atendimento.getPaciente(), this.atendimento.getProcedimento().getCodProc(), true);
            laudoController.validaSexoDoPacienteProcedimentoSigtap(dataAtende, this.atendimento.getProcedimento().getCodProc(), this.atendimento.getPaciente(), true);
            laudoController.validaCboDoProfissionalLaudo(dataAtende, this.atendimento.getProcedimento().getCodProc(), this.atendimento.getCbo(), true);
        }
    }
    
    private void validarProcedimentoCidsSecundariosSigtap() throws ProjetoException {
        if(this.unidadeValidaDadosSigtap) {
            LaudoController laudoController = new LaudoController();
            
            for (ProcedimentoCidDTO procedimentoCid : atendimento.getListaProcedimentoCid()) {
            	laudoController.idadeValida(dataAtende, this.atendimento.getPaciente(), procedimentoCid.getProcedimento().getCodProc(), true);
            	laudoController.validaSexoDoPacienteProcedimentoSigtap(dataAtende, procedimentoCid.getProcedimento().getCodProc(), this.atendimento.getPaciente(), true);
            	laudoController.validaCboDoProfissionalLaudo(dataAtende, procedimentoCid.getProcedimento().getCodProc(), this.atendimento.getCbo(), true);
            	laudoController.validarCidPorProcedimento(procedimentoCid.getCid(), dataAtende, procedimentoCid.getProcedimento().getCodProc(), paciente, true);
			}
        }
    }

    private void validarCidSigtap() throws ProjetoException {
        if(this.unidadeValidaDadosSigtap && this.atendimento.getPrograma().isPermiteAlteracaoCidNaEvolucao()) {
            LaudoController laudoController = new LaudoController();
            laudoController.validarCidPorProcedimento(atendimento.getCidPrimario(), dataAtende, atendimento.getProcedimento().getCodProc(), paciente, true);
        }
    }

    private Date setaAtendimentoValidadoPeloSigtapAnterior(Date dataAtende) {
        if (!existeCargaSigtapParaDataAtende(dataAtende)) {
            dataAtende = DataUtil.retornaDataComMesAnterior(dataAtende);
            this.atendimento.setValidadoPeloSigtapAnterior(true);
        }
        else {
            this.atendimento.setValidadoPeloSigtapAnterior(false);
        }
        return dataAtende;
    }

    public boolean existeCargaSigtapParaDataAtende(Date dataAtende) {
        boolean existeCargaSigtapParaDataSolicitacao = true;
        if(this.unidadeValidaDadosSigtap) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dataAtende);
            int mesAtendimento = calendar.get(Calendar.MONTH);
            mesAtendimento++;
            int anoAtendimento = calendar.get(Calendar.YEAR);
            existeCargaSigtapParaDataSolicitacao = this.procedimentoDAO.verificaExisteCargaSigtapParaData(mesAtendimento, anoAtendimento);
        }
        return existeCargaSigtapParaDataSolicitacao;
    }

//    public void verificaSeCboProfissionalEhValidoParaProcedimento() throws ProjetoException, SQLException {
//    	
//        if(this.unidadeValidaDadosSigtap) {
//            for (AtendimentoBean atendimento : this.listAtendimentosEquipe) {
//            	
//            	if(!existeCargaSigtapParaDataSolicitacao(atendimento.getDataAtendimento())) {
//            		atendimento.setDataAtendimento(DataUtil.retornaDataComMesAnterior(atendimento.getDataAtendimento()));
//            	}
//            	
//            	atendimento.getFuncionario().setCbo(funcionarioDAO.buscaCboCompativelComProcedimento(	
//            			atendimento.getDataAtendimento(), atendimento.getProcedimento().getIdProc(), 
//            			atendimento.getFuncionario().getId(), null));
//            	
//            	if(VerificadorUtil.verificarSeObjetoNuloOuZero(atendimento.getFuncionario().getCbo())) {
//            		throw new ProjetoException("O profissional " + atendimento.getFuncionario().getNome()
//                            + " não possui um CBO válido para este procedimento");
//            	}
//            		
//                if (!procedimentoDAO.validaCboProfissionalParaProcedimento(atendimento.getProcedimento().getIdProc(),
//                        atendimento.getFuncionario().getId(), atendimento.getDataAtendimento())) {
//                    throw new ProjetoException("O profissional " + atendimento.getFuncionario().getNome()
//                            + " não possui um CBO válido para este procedimento");
//                }
//            }
//        }
//
//    }
    
    private void validarDadosSigtapDeListaDeAtendimentos(List<AtendimentoBean> listaAtendimento) throws ProjetoException {
        if(this.unidadeValidaDadosSigtap) {
            for (AtendimentoBean atendimento : listaAtendimento) {
            	dataAtende = atendimento.getDataAtendimento();
            	if(!existeCargaSigtapParaDataSolicitacao(dataAtende)) {
            		dataAtende= DataUtil.retornaDataComMesAnterior(atendimento.getDataAtendimento());
            	}
            	
            	LaudoController laudoController = new LaudoController();
            	laudoController.idadeValida(dataAtende, atendimento.getPaciente(), atendimento.getProcedimento().getCodProc(), true);
            	laudoController.validaSexoDoPacienteProcedimentoSigtap(dataAtende, atendimento.getProcedimento().getCodProc(), atendimento.getPaciente(), true);
            	laudoController.validaCboDoProfissionalLaudo(dataAtende, atendimento.getProcedimento().getCodProc(), atendimento.getCbo(), true);				
			}
        }
    }
    
    private boolean existeCargaSigtapParaDataSolicitacao(Date dataAtendimento) {
        if(this.unidadeValidaDadosSigtap) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dataAtendimento);
            int mesSolicitacao = calendar.get(Calendar.MONTH);
            mesSolicitacao++;
            int anoSolicitacao = calendar.get(Calendar.YEAR);
            return procedimentoDAO.verificaExisteCargaSigtapParaData(mesSolicitacao, anoSolicitacao);
        }
        return true;
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

    public void insereProfissionalParaRealizarAtendimentoNaEquipe(CboBean cbo) throws ProjetoException {
        AtendimentoBean atendimentoAux = new AtendimentoBean();
        //boolean gravou = aDao.insereProfissionalParaRealizarAtendimentoNaEquipe(atendimento, funcionarioAux);

        atendimentoAux = listAtendimentosEquipe.get(0);

        cbo = new CboDAO().listarCboPorId(cbo.getCodCbo());
        
        AtendimentoBean aux = new AtendimentoBean();
        aux.setId(atendimentoAux.getId());
        aux.setPaciente(atendimento.getPaciente());
        aux.setCbo(cbo);
        aux.setDataAtendimento(atendimentoAux.getDataAtendimento());
        aux.setGrupo((atendimentoAux.getGrupo()));
        aux.setPrograma((atendimentoAux.getPrograma()));
        aux.setProcedimento(atendimento.getProcedimento());
        aux.setFuncionario(funcionarioAux);
        listAtendimentosEquipe.add(aux);
        JSFUtil.adicionarMensagemSucesso("Profissional inserido com sucesso!", "Sucesso");
        JSFUtil.fecharDialog("dlgincprof");
    }

    public void limpaInclusaoProfissionalAtendimento() {
        funcionarioAux = new FuncionarioBean();
        cboSelecionado = new CboBean();
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
        if (!VerificadorUtil.verificarSeObjetoNuloOuZero(procedimento.getIdProc())) {
			for (int i = 0; i < listAtendimentosEquipe.size(); i++) {
				if (listAtendimentosEquipe.get(i).getId1() == atendimentoLista.getId1()) {
					listAtendimentosEquipe.get(i).setProcedimento(procedimento);
				}
			}
        }

        if (funcionario != null) {
            
            for (int i = 0; i < listAtendimentosEquipe.size(); i++) {

                if (listAtendimentosEquipe.get(i).getId1() == atendimentoLista
                        .getId1()) {
                    listAtendimentosEquipe.get(i).setFuncionario(funcionario);
                    listAtendimentosEquipe.get(i).setCbo(atendimentoDAO.retornaCboProfissionalNoAtendimento(idAtendimentos, funcionario.getId()));
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
        this.listaProcedimentos = procedimentoDAO.listarProcedimento();
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
        
        validarDadosSigtapDeListaDeAtendimentos(listAtendimentosEquipe);
        
        if(!validarSeEhNecessarioInformarGrupo()) {
            if(!validarSeEhNecessarioInformarLaudo()) {

				if (evolucaoAtendimentoEhPermitida(listAtendimentosEquipe)) {
					boolean alterou = atendimentoDAO.realizaAtendimentoEquipe(listAtendimentosEquipe,
							atendimento.getInsercaoPacienteBean().getLaudo().getId(),
							atendimento.getGrupoAvaliacao().getIdGrupo(), listAtendimentosEquipeParaExcluir,
							atendimento.getId(), atendimento.isValidadoPeloSigtapAnterior(), atendimento.getTurno());
					if (alterou) {
						getCarregaGerenciarAtendimentos();
						JSFUtil.adicionarMensagemSucesso("Atendimento Gravado com sucesso!", "Sucesso");
					} else {
						JSFUtil.adicionarMensagemErro("Ocorreu um erro durante o atendimento!", "Erro");
					}
				}
            }  else {
				JSFUtil.adicionarMensagemErro("Informe o Laudo da avaliação!", "Erro!");
			}
        }
        else{
            JSFUtil.adicionarMensagemErro("Informe o grupo da avaliação!", "Erro!");
        }
    }

    public void carregarTodasAsEvolucoesDoPaciente(Integer codPaciente) throws ProjetoException {

        if(VerificadorUtil.verificarSeObjetoNuloOuZero(codPaciente)) {
            if ((!VerificadorUtil.verificarSeObjetoNulo(periodoFinalEvolucao)) || (!VerificadorUtil.verificarSeObjetoNulo(periodoInicialEvolucao))) {
                if ((VerificadorUtil.verificarSeObjetoNulo(periodoFinalEvolucao)) || (VerificadorUtil.verificarSeObjetoNulo(periodoInicialEvolucao))) {
                    JSFUtil.adicionarMensagemAdvertencia("Informe o Período Inicial e Final da Evolução", "Atenção!");
                }
                else
                    listaEvolucoes = atendimentoDAO.carregarTodasAsEvolucoesDoPaciente(codPaciente, periodoInicialEvolucao, periodoFinalEvolucao, especialidade);
            }
            else {
                JSFUtil.adicionarMensagemAdvertencia("Informe o Paciente ou o Período Inicial e Final da Evolução", "Atenção!");
            }
        }

        else if ((VerificadorUtil.verificarSeObjetoNulo(periodoFinalEvolucao)) && (VerificadorUtil.verificarSeObjetoNulo(periodoInicialEvolucao))) {
            if(VerificadorUtil.verificarSeObjetoNuloOuZero(codPaciente)) {
                JSFUtil.adicionarMensagemAdvertencia("Informe o Paciente ", "Atenção!");
            }
            else
                listaEvolucoes = atendimentoDAO.carregarTodasAsEvolucoesDoPaciente(codPaciente, periodoInicialEvolucao, periodoFinalEvolucao, especialidade);
        }

        else if(!VerificadorUtil.verificarSeObjetoNuloOuZero(codPaciente) &&
                ((VerificadorUtil.verificarSeObjetoNulo(periodoFinalEvolucao)) || (VerificadorUtil.verificarSeObjetoNulo(periodoInicialEvolucao))) ) {
            JSFUtil.adicionarMensagemAdvertencia("Informe o Período Inicial e Final da Evolução", "Atenção!");
        }

        else
            listaEvolucoes = atendimentoDAO.carregarTodasAsEvolucoesDoPaciente(codPaciente, periodoInicialEvolucao, periodoFinalEvolucao, especialidade);
    }

    public void carregaEvolucoesPacienteProfissional(Integer codPaciente) throws ProjetoException {
        //Adicionei o valor null, pois modifiquei a função de busca no banco.
        this.listaEvolucoes = atendimentoDAO.carregarEvolucoesDoPaciente(codPaciente, null);
    }

    //As funções getListaEspecialidadesEquipe() e listarEvolucoesPorEspecialidade() foram adicionadas - Martinho
    public List<EspecialidadeBean> getListaEspecialidadesEquipe() {
        return listaEspecialidadesEquipe;
    }

    public void listarEvolucoesPorEspecialidade() throws ProjetoException{

        if(identificadorEspecialidade.equals("geral")){
            this.listaEvolucoes = atendimentoDAO.carregarEvolucoesDoPaciente(atendimento.getPaciente().getId_paciente(), getDataFiltroEspecialidade());
        }else{
            Integer codEspecialidade = Integer.parseInt(identificadorEspecialidade);
            this.listaEvolucoes = atendimentoDAO.carregarEvolucoesPorEspecialidade(atendimento.getPaciente().getId_paciente(), codEspecialidade, getDataFiltroEspecialidade());
        }
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
    
	public List<EquipeBean> listaEquipeAutoComplete(String query) throws ProjetoException {
		return new EquipeDAO().listarEquipePorGrupoAutoComplete(query, this.atendimento.getGrupo().getIdGrupo());
	}
	
    public void carregaListaEquipePorGrupo() throws ProjetoException {
           listaEquipe = new EquipeDAO().listarEquipePorGrupo(this.atendimento.getGrupo().getIdGrupo());
    }

    public void listarGruposPorProgramas() throws ProjetoException {
        listaGrupos = gDao.listarGruposPorPrograma(atendimento.getPrograma().getIdPrograma());
    }
    
    public void limparGrupoIhEquipe() {
    	atendimento.setGrupo(new GrupoBean());
    	atendimento.setEquipe(new EquipeBean());
    }
    
    public void limparEquipe() {
    	atendimento.setEquipe(new EquipeBean());
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

    public void listaAtendimentos1Ajustes() throws ProjetoException {
        this.listAtendimentos = atendimentoDAO.listaAtendimentos1FiltroAjustes(atendimentoAux, this.semCids, this.campoBusca, this.tipoBusca);
    }
    
    public void listaAtendimentos1AjustesEmMassa() throws ProjetoException {
        this.listaAtendimentosFiltro = atendimentoDAO.listaAtendimentos1FiltroAjustes(atendimento, this.semCids, atendimento.getPaciente().getNome(), TipoBuscaPaciente.PACIENTE.getSigla());
        this.listaAtendimentosSelecionados.clear();
        this.selecionarTodos = false;
    }
    
    public void adicionarOuRemoverAtendimentoSelecionado(AtendimentoBean atendimento) {
    	if(atendimentoFoiAdicionado(atendimento))
    		this.listaAtendimentosSelecionados.remove(atendimento);
    	else
    		this.listaAtendimentosSelecionados.add(atendimento);
    }
    
    public void selecionarOuRemoverTodosAtendimentos(List<AtendimentoBean> lista) {
    	if(!selecionarTodos)
    		this.listaAtendimentosSelecionados.clear();
    	else
    		this.listaAtendimentosSelecionados.addAll(lista);
    }
    
    
    public boolean atendimentoFoiAdicionado(AtendimentoBean atendimento) {
    	return this.listaAtendimentosSelecionados.contains(atendimento);
    }

    public void listarCids1() throws ProjetoException {
        if(this.unidadeValidaDadosSigtap) {
            Date dataAtende = setaAtendimentoValidadoPeloSigtapAnterior(this.atendimento.getDataAtendimento());
            existeCargaSigtapParaEsteMesOuAnterior(dataAtende, NAO_EH_EVOLUCAO);

            if(existeCargaSigtapParaEsteMesOuAnterior) {
                listaCids = cidDao.listarCidsBuscaPorProcedimento(campoBuscaCid,
                        this.atendimento.getProcedimento().getCodProc(), dataAtende);
            }
        }
        else
            listaCids = cidDao.listarCidsBusca(campoBuscaCid);
    }

    public void limparDialogBuscaCid() {
        this.listaCids.clear();
        this.campoBuscaCid = new String();
        JSFUtil.abrirDialog("dlgConsulCid1");
    }

    public void atualizaCidDeAtendimento() throws ProjetoException {
        boolean alterou = atendimentoDAO.atualizaCidDeAtendimento(atendimento, cid);
        if (alterou) {
            JSFUtil.adicionarMensagemSucesso("CID alterado no atendimento com sucesso!", "");
            listaAtendimentos1Ajustes();
        }
    }
    
    public void atualizaCboDeAtendimento() throws ProjetoException {
    	buscaCboPorId();
        boolean alterou = atendimentoDAO.atualizaCboDeAtendimento(atendimento, cboSelecionado);
        if (alterou) {
            JSFUtil.adicionarMensagemSucesso("CBO alterado no atendimento com sucesso!", "");
            listaAtendimentos1Ajustes();
        }
    }

    public void atualizaProcedimentoDoAtendimento() throws ProjetoException {
      //  parei aqui walter
        if(this.unidadeValidaDadosSigtap) {
            Date dataSolicitacaoPeloSigtap = atendimento.getDataAtendimento();

            if(!existeCargaSigtapParaDataSolicitacao(dataSolicitacaoPeloSigtap)) {
                dataSolicitacaoPeloSigtap= DataUtil.retornaDataComMesAnterior(atendimento.getDataAtendimento());
            }
            LaudoController laudoController = new LaudoController();
            laudoController.validaCboDoProfissionalLaudo(dataSolicitacaoPeloSigtap, procedimento.getCodProc(),
            		this.atendimento.getCbo(), true);
        }
        boolean alterou = atendimentoDAO.atualizaProcedimentoDoAtendimento(atendimento, procedimento);
        if (alterou) {
            JSFUtil.adicionarMensagemSucesso("Procedimento alterado no atendimento com sucesso!", "");
            JSFUtil.fecharDialog("dlgConsulProc");
            JSFUtil.fecharDialog("dlgConfirmacaoProc");
            listaAtendimentos1Ajustes();
        }
    }

    public void adicionarTextoEvolucaoPadraoNoCampoEvolucao() {

        String  novoTexto = new String();
        String textoEvolucao = textoEvolucaoAuxiliar; 
        
        if(VerificadorUtil.verificarSeObjetoNuloOuVazio(textoEvolucao)) {
        	novoTexto = this.descricaoEvolucaoPadrao + this.atendimento.getEvolucao();
        	this.atendimento.setEvolucao(novoTexto);
        }
        else if(!textoEvolucao.contains(this.descricaoEvolucaoPadrao)) {
        	novoTexto = this.descricaoEvolucaoPadrao += textoEvolucao;
        	this.atendimento.setEvolucao(novoTexto);
        }
    }
    
    public void atualizaTextoEvolucaoAuxiliar(String texto) {
    	textoEvolucaoAuxiliar = texto;
    }

    private void listarTodosTiposProcedimentos() throws ProjetoException {
        this.listaProcedimentos = procedimentoDAO.listarTodosProcedimentosDoPrograma(this.atendimento);
        if(VerificadorUtil.verificarSeObjetoNuloOuZero(this.atendimento.getCidPrimario().getIdCid()))
        	buscarCidAtendimento(this.listaProcedimentos.get(0).getIdProc(), this.atendimento.getId());
    }
    
    private void evolucaoFoiRealizadaNaDataAnterior(AtendimentoBean atendimento) throws ProjetoException {
        FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
                .getSessionMap().get("obj_funcionario");
        
        boolean unidadeBloqueiaEditarEvolucao = user_session.getUnidade().getParametro().isBloquearEdicaoEvolucao();
        
    	if(VerificadorUtil.verificarSeObjetoNulo(atendimento.getDataAtendido()))
    		podeEditarEvolucaoNestaData = true;
    	else {
    		if(!atendimentoDAO.verificaSePodeEditarEvolucao(atendimento) && unidadeBloqueiaEditarEvolucao)
    			podeEditarEvolucaoNestaData = false;
    		else
    			podeEditarEvolucaoNestaData = true;
    			
    		if(!podeEditarEvolucaoNestaData)
    			JSFUtil.adicionarMensagemAdvertencia("Não é possível editar a evolução", "");
    	}
    }

    private void buscarDadosProcedimentoPorId() throws ProjetoException {
        this.atendimento.setProcedimento(procedimentoDAO.listarProcedimentoPorId(this.atendimento.getProcedimento().getIdProc()));
    }

    private void buscarDadosCidPorId() throws ProjetoException {
        if (!VerificadorUtil.verificarSeObjetoNuloOuZero(atendimento.getCidPrimario().getIdCid()))
            this.atendimento.setCidPrimario(cidDao.buscaCidPorId(atendimento.getCidPrimario().getIdCid()));
    }

    public void marcarDialogPendenciasComoVisualizado() {
        JSFUtil.fecharDialog("dlgPendencias");
        this.visualizouDialogPendencias = SIM;
        SessionUtil.adicionarNaSessao(this.visualizouDialogPendencias, "visualizouPendencias");
    }

    public void listarTotalPendencias() throws ProjetoException {
        FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
                .getSessionMap().get("obj_usuario");

        this.visualizouDialogPendencias = (String) FacesContext.getCurrentInstance().getExternalContext()
                .getSessionMap().get("visualizouPendencias");

        if(user_session.getRealizaAtendimento() &&
                (VerificadorUtil.verificarSeObjetoNuloOuVazio(this.visualizouDialogPendencias) || !this.visualizouDialogPendencias.equals(SIM)) ) {
            this.listaPendenciasEvolucaoProgramaGrupo = atendimentoDAO.retornaTotalDePendenciasDeEvolucaoDoUsuarioLogado();
            setaTotalPendenciasEvolucao(this.listaPendenciasEvolucaoProgramaGrupo);
            if (listaPendenciasEvolucaoProgramaGrupo.size()>0)
                JSFUtil.abrirDialog("dlgPendencias");
        }
    }

    private void setaTotalPendenciasEvolucao(List<PendenciaEvolucaoProgramaGrupoDTO> listaPendenciaEvolucaoProgramaGrupo) {
        this.totalPendenciaEvolucao = 0;
        for (PendenciaEvolucaoProgramaGrupoDTO pendenciaEvolucaoProgramaGrupoDTO : listaPendenciaEvolucaoProgramaGrupo) {
            this.totalPendenciaEvolucao += pendenciaEvolucaoProgramaGrupoDTO.getTotalPendencia();
        }
    }
    
    public void abrirDialogProcedimentos() throws ProjetoException {
    	this.procedimentoCidSelecionado = new ProcedimentoCidDTO();
    	JSFUtil.abrirDialog("dlgConsultaProcedimentos");
    }
    
    public void buscarCidAtendimento(Integer idProcedimento, Integer idAtendimento) throws ProjetoException {
    	this.atendimento.setCidPrimario (cidDao.buscarCidAtendimento(idProcedimento, idAtendimento));
    }
    
    public void buscarCidParaProcedimentoSecundario(Integer idProcedimento, Integer idAtendimento) throws ProjetoException {
    	this.procedimentoCidSelecionado.setCid(cidDao.buscarCidAtendimento(idProcedimento, idAtendimento));
    }
    
    public void adicionarProcedimentoSecundario() throws ProjetoException {
    	Integer idProcedimento = procedimentoCidSelecionado.getProcedimento().getIdProc();
    	Integer idCid = procedimentoCidSelecionado.getCid().getIdCid();
    	
    	if(!procedimentoCidJaFoiAdicionado(idProcedimento)) {
    		procedimentoCidSelecionado.setProcedimento(procedimentoDAO.listarProcedimentoPorId(idProcedimento));
    		procedimentoCidSelecionado.setCid(cidDao.buscaCidPorId(idCid));
    		this.atendimento.getListaProcedimentoCid().add(procedimentoCidSelecionado);
    		JSFUtil.fecharDialog("dlgConsultaProcedimentos");
    	}
    }
    
    private boolean procedimentoCidJaFoiAdicionado(Integer idProcedimento) {
    	
    	if(atendimento.getProcedimento().getIdProc().equals(idProcedimento)) {
			JSFUtil.adicionarMensagemErro("Não é possível adicionar o procedimento principal novamente", "");
			return true;    		
    	}
    	
    	for (ProcedimentoCidDTO procedimentoCid : atendimento.getListaProcedimentoCid()) {
			if(procedimentoCid.getProcedimento().getIdProc().equals(idProcedimento)) {
				JSFUtil.adicionarMensagemErro("Este Procedimento já foi adicionado", "");
				return true;
			}
		}
    	return false;
    }
    
    public void removerProcedimentoCidSecundario(ProcedimentoCidDTO procedimentoCidDTO) {
    	this.atendimento.getListaProcedimentoCid().remove(procedimentoCidDTO);
    }
    
    public void preparaDialogParaExcluirAgendamentoProfissional() throws ProjetoException {
    	limpaDadosDialogLiberacao();
    	JSFUtil.abrirDialog("dlgLiberacao");
    }
    
    private void limpaDadosDialogLiberacao() {
    	this.funcionario = new FuncionarioBean();
	}

	public void validarSenhaLiberacaoExclusaoAgendamento() throws ProjetoException {
        FuncionarioDAO funcionarioDAO = new FuncionarioDAO();

        FuncionarioBean func = funcionarioDAO.validarCpfIhSenha(funcionario.getCpf(), funcionario.getSenha(),
                ValidacaoSenha.LIBERACAO.getSigla());

        if (func != null) {
            excluirAgendamentoPaciente(func, atendimento);
            JSFUtil.fecharDialog("dlgLiberacao");
            JSFUtil.fecharDialog("dialogExclusao");
            listaAtendimentos1Ajustes();
        } else {
            JSFUtil.adicionarMensagemErro("Funcionário com senha errada ou sem liberação!", "Erro!");
        }
    }
    
    private void excluirAgendamentoPaciente(FuncionarioBean funcionarioLiberacao, AtendimentoBean atendimento) throws ProjetoException {
    	boolean agendamentoExcluido = new AgendaDAO().excluirAgendamentoPaciente
    				(atendimento, funcionarioLiberacao, MotivoLiberacao.EXCLUSAO_AGENDAMENTO_PACIENTE.getTitulo());
    	if(agendamentoExcluido) {
    		JSFUtil.adicionarMensagemSucesso("Agendamento Excluído com Sucesso", "");
    	}
    }
    
    public void preparaAbrirDialogAjustesEmMassa() {
    	this.semCids = false;
    	this.atendimento = new AtendimentoBean();
    	this.listaAtendimentosFiltro.clear();
    	this.listaAtendimentosSelecionados.clear();
    	JSFUtil.abrirDialog("dlgAjustes");
    }
    
    public void configuraDialogsParaAlteracaoUnica() {
    	this.alteracaoEmMassa = false;
    }
    
    public void configuraDialogsParaAlteracaoEmMassa() {
    	this.cid = new CidBean();
    	this.procedimento = new ProcedimentoBean();
    	this.alteracaoEmMassa = true;
    }
    
    public void selecionarCid(CidBean cidSelecionado) {
    	this.cid = cidSelecionado;
    }
    
    public void selecionarProcedimento(ProcedimentoBean procedimentoSelecionado) {
    	this.procedimento = procedimentoSelecionado;
    }
    
    public void listarCidsAlteracaoEmMassa() throws ProjetoException {
    	listaCids = cidDao.listarCidsBusca(campoBuscaCid);
    }
    
    public void gravarAlteracaoCidEmMassa() throws ProjetoException {
    	if(this.unidadeValidaDadosSigtap) {
    		listaAtendimentosSelecionados = setaAtendimentoValidadoPeloSigtapAnteriorParaAtendimentosEmMassa(listaAtendimentosSelecionados);
    		
    		LaudoController laudoController = new LaudoController();
    		for (AtendimentoBean atendimento : listaAtendimentosSelecionados) {
    			laudoController.validarCidPorProcedimento(cid, atendimento.getDataAtendimento(), atendimento.getProcedimento().getCodProc(), atendimento.getPaciente(), true);
			}
    	}
    	
    	boolean alterou = atendimentoDAO.atualizaCidDeVariosAtendimento(listaAtendimentosSelecionados, cid);
    	if(alterou) {
    		JSFUtil.adicionarMensagemSucesso("Atendimentos Alterados com Sucesso!", "");
    		JSFUtil.fecharDialog("dlgConsulCid1");
    		JSFUtil.fecharDialog("dlgConfirmacaoCid2");
    		this.campoBusca = new String();
    		listaAtendimentos1AjustesEmMassa();
    	}
    }
    
    public void gravarAlteracaoProcedimentoEmMassa() throws ProjetoException {
    	if(this.unidadeValidaDadosSigtap) {
    		listaAtendimentosSelecionados = setaAtendimentoValidadoPeloSigtapAnteriorParaAtendimentosEmMassa(listaAtendimentosSelecionados);
    		
    		LaudoController laudoController = new LaudoController();
    		for (AtendimentoBean atendimento : listaAtendimentosSelecionados) {
    			laudoController.idadeValida(atendimento.getDataAtendimento(), atendimento.getPaciente(), procedimento.getCodProc(), true);
            	laudoController.validaSexoDoPacienteProcedimentoSigtap(atendimento.getDataAtendimento(), procedimento.getCodProc(), atendimento.getPaciente(), true);
            	laudoController.validaCboDoProfissionalLaudo(atendimento.getDataAtendimento(), procedimento.getCodProc(), atendimento.getCbo() ,true);
                if(laudoController.procedimentoPossuiCidsAssociados(atendimento.getDataAtendimento(), procedimento.getCodProc()))
                	laudoController.validarCidPorProcedimento(atendimento.getCidPrimario(), atendimento.getDataAtendimento(), procedimento.getCodProc(), atendimento.getPaciente(), true);
    		}
    	}
    	
    	boolean alterou = atendimentoDAO.atualizaProcedimentoVariosAtendimento(listaAtendimentosSelecionados, procedimento);
    	if(alterou) {
    		JSFUtil.adicionarMensagemSucesso("Atendimentos Alterados com Sucesso!", "");
    		JSFUtil.fecharDialog("dlgConsulProc");
    		JSFUtil.fecharDialog("dlgConfirmacaoProc2");
    		listaAtendimentos1AjustesEmMassa();
    	}
    	else {
    		JSFUtil.fecharDialog("dlgConsulProc");
    		JSFUtil.fecharDialog("dlgConfirmacaoProc2");
    	}
    }
    
    
    private List<AtendimentoBean> setaAtendimentoValidadoPeloSigtapAnteriorParaAtendimentosEmMassa(List<AtendimentoBean> listaAtendimento) {
    	for (int i = 0; i < listaAtendimento.size(); i++) {
    		if (!existeCargaSigtapParaDataAtende(listaAtendimento.get(i).getDataAtendimento())) {
    			listaAtendimento.get(i).setDataAtendimento(DataUtil.retornaDataComMesAnterior(listaAtendimento.get(i).getDataAtendimento()));
    			listaAtendimento.get(i).setValidadoPeloSigtapAnterior(true);
    		}
    		else {
    			listaAtendimento.get(i).setValidadoPeloSigtapAnterior(false);
    		}
		}
        return listaAtendimento;
    }
    
	public void listarTodosCbos() throws ProjetoException {
		this.cboSelecionado = new CboBean();
		this.listaCbos = new CboDAO().listarCbo();
	}
	
	public void buscaCboPorId() throws ProjetoException {
		this.cboSelecionado = new CboDAO().listarCboPorId(this.cboSelecionado.getCodCbo());
	}
	
    public void gravarAlteracaoCboEmMassa() throws ProjetoException {
    	if(this.unidadeValidaDadosSigtap) {
    		listaAtendimentosSelecionados = setaAtendimentoValidadoPeloSigtapAnteriorParaAtendimentosEmMassa(listaAtendimentosSelecionados);
    		List<FuncionarioBean> listaProfissionais = filtraProfissionaisRepetidosDaLista(listaAtendimentosSelecionados);
    		
    		if (!todosProfissionaisPossuemOhCboSelecionado(listaProfissionais)) {
    			JSFUtil.fecharDialog("dlgConfirmacaoCbo2");
    			return;
    		}
    		
    		LaudoController laudoController = new LaudoController();
    		for (AtendimentoBean atendimento : listaAtendimentosSelecionados) {
            	laudoController.validaCboDoProfissionalLaudo(atendimento.getDataAtendimento(), atendimento.getProcedimento().getCodProc(), cboSelecionado ,true);
    		}
    	}
    	
    	boolean alterou = atendimentoDAO.atualizaCboVariosAtendimentos(listaAtendimentosSelecionados, cboSelecionado);
    	if(alterou) {
    		JSFUtil.adicionarMensagemSucesso("Atendimentos Alterados com Sucesso!", "");
    		JSFUtil.fecharDialog("dlgcbo");
    		JSFUtil.fecharDialog("dlgConfirmacaoCbo2");
    		listaAtendimentos1AjustesEmMassa();
    	}
    	else {
    		JSFUtil.fecharDialog("dlgcbo");
    		JSFUtil.fecharDialog("dlgConfirmacaoCbo2");
    	}
    }

	private boolean todosProfissionaisPossuemOhCboSelecionado(List<FuncionarioBean> listaProfissionais)
			throws ProjetoException {
		for (FuncionarioBean profissional : listaProfissionais) {
			if(!funcionarioDAO.cboCompativelComProfissional(cboSelecionado.getCodCbo(), profissional.getId())) {
				JSFUtil.adicionarMensagemErro
				("Erro o profissional " +profissional.getNome()+ " não possui este CBO cadastrado", "");
				return false;
			}
		}
		return true;
	}

	private List<FuncionarioBean> filtraProfissionaisRepetidosDaLista(List<AtendimentoBean> listaAtendimentos) {
		List<FuncionarioBean> listaProfissionais = new ArrayList<>();
		List<Long> ids = new ArrayList<>();
		for (AtendimentoBean atendimento : listaAtendimentos) {
			
			if(!ids.contains(atendimento.getFuncionario().getId())) {
				ids.add(atendimento.getFuncionario().getId());
				listaProfissionais.add(atendimento.getFuncionario());
			}
		}
		return listaProfissionais;
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

    public Boolean getExisteAlgumaCargaSigtap() {
        return existeAlgumaCargaSigtap;
    }

    public void setExisteAlgumaCargaSigtap(Boolean existeAlgumaCargaSigtap) {
        this.existeAlgumaCargaSigtap = existeAlgumaCargaSigtap;
    }

    public Boolean getExisteCargaSigtapParaEsteMesOuAnterior() {
        return existeCargaSigtapParaEsteMesOuAnterior;
    }

    public void setExisteCargaSigtapParaEsteMesOuAnterior(Boolean existeCargaSigtapParaEsteMesOuAnterior) {
        this.existeCargaSigtapParaEsteMesOuAnterior = existeCargaSigtapParaEsteMesOuAnterior;
    }
    public Boolean getSemCids() {
        return semCids;
    }

    public void setSemCids(Boolean semCids) {
        this.semCids = semCids;
    }

    public List<CidBean> getListaCids() {
        return listaCids;
    }

    public void setListaCids(List<CidBean> listaCids) {
        this.listaCids = listaCids;
    }

    public EspecialidadeBean getEspecialidade() {
        return especialidade;
    }

    public void setEspecialidade(EspecialidadeBean especialidade) {
        this.especialidade = especialidade;
    }

    public List<PendenciaEvolucaoProgramaGrupoDTO> getListaPendenciasEvolucaoProgramaGrupo() {
        return listaPendenciasEvolucaoProgramaGrupo;
    }

    public void setListaPendenciasEvolucaoProgramaGrupo(
            List<PendenciaEvolucaoProgramaGrupoDTO> listaPendenciasEvolucaoProgramaGrupo) {
        this.listaPendenciasEvolucaoProgramaGrupo = listaPendenciasEvolucaoProgramaGrupo;
    }

    public String getVisualizouDialogPendencias() {
        return visualizouDialogPendencias;
    }

    public void setVisualizouDialogPendencias(String visualizouDialogPendencias) {
        this.visualizouDialogPendencias = visualizouDialogPendencias;
    }

    public Integer getTotalPendenciaEvolucao() {
        return totalPendenciaEvolucao;
    }

    public String getDescricaoEvolucaoPadrao() {
        return descricaoEvolucaoPadrao;
    }

    public void setDescricaoEvolucaoPadrao(String descricaoEvolucaoPadrao) {
        this.descricaoEvolucaoPadrao = descricaoEvolucaoPadrao;
    }

	public ProcedimentoCidDTO getProcedimentoCidSelecionado() {
		return procedimentoCidSelecionado;
	}

	public void setProcedimentoCidSelecionado(ProcedimentoCidDTO procedimentoCidSelecionado) {
		this.procedimentoCidSelecionado = procedimentoCidSelecionado;
	}

	public List<AtendimentoBean> getListaAtendimentosFiltro() {
		return listaAtendimentosFiltro;
	}

	public void setListaAtendimentosFiltro(List<AtendimentoBean> listaAtendimentosFiltro) {
		this.listaAtendimentosFiltro = listaAtendimentosFiltro;
	}

	public List<AtendimentoBean> getListaAtendimentosSelecionados() {
		return listaAtendimentosSelecionados;
	}

	public void setListaAtendimentosSelecionados(List<AtendimentoBean> listaAtendimentosSelecionados) {
		this.listaAtendimentosSelecionados = listaAtendimentosSelecionados;
	}

	public boolean isAlteracaoEmMassa() {
		return alteracaoEmMassa;
	}

	public void setAlteracaoEmMassa(boolean alteracaoEmMassa) {
		this.alteracaoEmMassa = alteracaoEmMassa;
	}

	public CidBean getCid() {
		return cid;
	}

	public void setCid(CidBean cid) {
		this.cid = cid;
	}

	public String getCampoBuscaCid() {
		return campoBuscaCid;
	}

	public void setCampoBuscaCid(String campoBuscaCid) {
		this.campoBuscaCid = campoBuscaCid;
	}

	public String getConfirmacaoDialog() {
		return confirmacaoDialog;
	}

	public void setConfirmacaoDialog(String confirmacaoDialog) {
		this.confirmacaoDialog = confirmacaoDialog;
	}

	public String getPalavrasChaveDivergentes() {
		return palavrasChaveDivergentes;
	}

	public CboBean getCboSelecionado() {
		return cboSelecionado;
	}

	public void setCboSelecionado(CboBean cboSelecionado) {
		this.cboSelecionado = cboSelecionado;
	}

	public List<CboBean> getListaCbos() {
		return listaCbos;
	}

	public void setListaCbos(List<CboBean> listaCbos) {
		this.listaCbos = listaCbos;
	}

	public boolean isSelecionarTodos() {
		return selecionarTodos;
	}

	public void setSelecionarTodos(boolean selecionarTodos) {
		this.selecionarTodos = selecionarTodos;
	}

	public List<EquipeBean> getListaEquipe() {
		return listaEquipe;
	}

	public void setListaEquipe(List<EquipeBean> listaEquipe) {
		this.listaEquipe = listaEquipe;
	}

	public Boolean getPodeEditarEvolucaoNestaData() {
		return podeEditarEvolucaoNestaData;
	}

	public void setPodeEditarEvolucaoNestaData(Boolean podeEditarEvolucaoNestaData) {
		this.podeEditarEvolucaoNestaData = podeEditarEvolucaoNestaData;
	}

	public String getTextoEvolucaoAuxiliar() {
		return textoEvolucaoAuxiliar;
	}

	public void setTextoEvolucaoAuxiliar(String textoEvolucaoAuxiliar) {
		this.textoEvolucaoAuxiliar = textoEvolucaoAuxiliar;
	}

    //Adicionado os dois métodos especiais para o atributo indentificador de especialidade

    public String getIdentificadorEspecialidade() {
        return identificadorEspecialidade;
    }

    public void setIdentificadorEspecialidade(String identificadorEspecialidade) {
        this.identificadorEspecialidade = identificadorEspecialidade;
    }

    public Date getDataFiltroEspecialidade() {
        return dataFiltroEspecialidade;
    }

    public void setDataFiltroEspecialidade(Date dataFiltroEspecialidade) {
        this.dataFiltroEspecialidade = dataFiltroEspecialidade;
    }
}
