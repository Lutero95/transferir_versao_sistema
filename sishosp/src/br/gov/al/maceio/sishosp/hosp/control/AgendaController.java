package br.gov.al.maceio.sishosp.hosp.control;

import java.io.Serializable;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import br.gov.al.maceio.sishosp.comum.shared.TelasBuscaSessao;
import br.gov.al.maceio.sishosp.hosp.model.dto.BuscaSessaoDTO;
import br.gov.al.maceio.sishosp.hosp.model.dto.PacientesComInformacaoAtendimentoDTO;

import org.primefaces.event.SelectEvent;
import org.primefaces.event.TransferEvent;
import org.primefaces.model.DualListModel;

import br.gov.al.maceio.sishosp.acl.dao.FuncionarioDAO;
import br.gov.al.maceio.sishosp.acl.model.FuncionarioBean;
import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.DataUtil;
import br.gov.al.maceio.sishosp.comum.util.HorarioOuTurnoUtil;
import br.gov.al.maceio.sishosp.comum.util.JSFUtil;
import br.gov.al.maceio.sishosp.comum.util.SessionUtil;
import br.gov.al.maceio.sishosp.comum.util.VerificadorUtil;
import br.gov.al.maceio.sishosp.hosp.dao.AgendaDAO;
import br.gov.al.maceio.sishosp.hosp.dao.AtendimentoDAO;
import br.gov.al.maceio.sishosp.hosp.dao.BloqueioDAO;
import br.gov.al.maceio.sishosp.hosp.dao.CboDAO;
import br.gov.al.maceio.sishosp.hosp.dao.CidDAO;
import br.gov.al.maceio.sishosp.hosp.dao.EquipeDAO;
import br.gov.al.maceio.sishosp.hosp.dao.FeriadoDAO;
import br.gov.al.maceio.sishosp.hosp.dao.GerenciarPacienteDAO;
import br.gov.al.maceio.sishosp.hosp.dao.GrupoDAO;
import br.gov.al.maceio.sishosp.hosp.dao.LaudoDAO;
import br.gov.al.maceio.sishosp.hosp.dao.PacienteDAO;
import br.gov.al.maceio.sishosp.hosp.dao.ProcedimentoDAO;
import br.gov.al.maceio.sishosp.hosp.dao.ProgramaDAO;
import br.gov.al.maceio.sishosp.hosp.dao.SituacaoAtendimentoDAO;
import br.gov.al.maceio.sishosp.hosp.dao.TipoAtendimentoDAO;
import br.gov.al.maceio.sishosp.hosp.dao.UnidadeDAO;
import br.gov.al.maceio.sishosp.hosp.enums.MotivoLiberacao;
import br.gov.al.maceio.sishosp.hosp.enums.OpcaoAtendimento;
import br.gov.al.maceio.sishosp.hosp.enums.TipoAtendimento;
import br.gov.al.maceio.sishosp.hosp.enums.TipoDataAgenda;
import br.gov.al.maceio.sishosp.hosp.enums.TipoProcedimentoAgenda;
import br.gov.al.maceio.sishosp.hosp.enums.Turno;
import br.gov.al.maceio.sishosp.hosp.enums.ValidacaoSenha;
import br.gov.al.maceio.sishosp.hosp.model.AgendaBean;
import br.gov.al.maceio.sishosp.hosp.model.AtendimentoBean;
import br.gov.al.maceio.sishosp.hosp.model.CboBean;
import br.gov.al.maceio.sishosp.hosp.model.CidBean;
import br.gov.al.maceio.sishosp.hosp.model.ConfigAgendaParte1Bean;
import br.gov.al.maceio.sishosp.hosp.model.EquipeBean;
import br.gov.al.maceio.sishosp.hosp.model.GrupoBean;
import br.gov.al.maceio.sishosp.hosp.model.PacienteBean;
import br.gov.al.maceio.sishosp.hosp.model.ProcedimentoBean;
import br.gov.al.maceio.sishosp.hosp.model.ProgramaBean;
import br.gov.al.maceio.sishosp.hosp.model.SituacaoAtendimentoBean;
import br.gov.al.maceio.sishosp.hosp.model.TipoAtendimentoBean;

import static br.gov.al.maceio.sishosp.comum.shared.DadosSessao.BUSCA_SESSAO;

@ManagedBean(name = "AgendaController")
@ViewScoped
public class AgendaController implements Serializable {

    private static final long serialVersionUID = 1L;
    List<ConfigAgendaParte1Bean> listaConfigAgendaGeral = new ArrayList<>();
    List<ConfigAgendaParte1Bean> listaConfigAgendaMesAtual = new ArrayList<>();
    private AgendaBean agenda;
    private Date dataAtendimentoC;
    private Date dataAtendimentoFinalC;
    private String cnsC;
    private Integer protuarioC;
    private AgendaBean rowBean;
    private String tipoBusca;
    private String campoBusca;

    // Dual Funcionarios
    private DualListModel<FuncionarioBean> listaFuncionariosDual;
    private List<FuncionarioBean> listaFuncionariosSoucer;
    private List<FuncionarioBean> listaFuncionariosSoucerFiltro;
    private List<FuncionarioBean> listaFuncionariosTarget;
    private List<FuncionarioBean> listaFuncionariosTargetFiltro;

    public AgendaBean getRowBean() {
        return rowBean;
    }

    public void setRowBean(AgendaBean rowBean) {
        this.rowBean = rowBean;
    }

    private TipoAtendimentoBean tipoC;
    private ProgramaBean programaSelecionado;
    private List<AgendaBean> listaNovosAgendamentos;
    private List<AgendaBean> listaAgendamentosData;
    private List<GrupoBean> listaGruposProgramas;
    private GrupoBean grupoSelecionado;
    private List<AgendaBean> listaConsulta;
    private List<FuncionarioBean> listaProfissional, listaProfissionalPorGrupo;
    private boolean habilitarDetalhes;
    private List<TipoAtendimentoBean> listaTipos;
    private String opcaoAtendimento;
    private List<EquipeBean> listaEquipePorTipoAtendimento;
    private String tipoData;
    private Boolean temLotado;
    private List<AgendaBean> listaHorariosOcupados;
    private String situacao;
    private FuncionarioDAO fDao = new FuncionarioDAO();
    private TipoAtendimentoDAO tDao = new TipoAtendimentoDAO();
    private AgendaDAO aDao = new AgendaDAO();
    private GrupoDAO gDao = new GrupoDAO();
    private EquipeDAO eDao = new EquipeDAO();
    private Boolean agendamentosConfirmados;
    private String dataAtual;
    private TipoAtendimentoBean tipoAtendimentoSelecionado;
    private List<Date> listaNaoPermitidosIntervaloDeDatas;
    private FuncionarioBean funcionario;
    private List<GrupoBean> listaDeGruposFiltrada;
    private ArrayList<String> listaHorarios;
    private List<PacientesComInformacaoAtendimentoDTO> listaPacientesComInformacaoDTO;
    private List<PacientesComInformacaoAtendimentoDTO> listaPacientesComInformacaoDTOFiltro;
    private List<PacientesComInformacaoAtendimentoDTO> listaPacientesSelecionadosComInformacaoDTO;
    private PacienteDAO pacienteDAO;
    private FuncionarioBean user_session;
    private String mensagemDialogLiberacao;
    private String motivoLiberacao;
    private FuncionarioBean usuarioLiberacao;
    private List<String> listaLiberacoes;
    private FuncionarioBean funcionarioSelecionado;
    private PacientesComInformacaoAtendimentoDTO pacienteSelecionado;
    private List<Long> listaIdFuncionariosComDuplicidadeEspecialidade;
    private boolean incluirProcedimentos;
    private String tipoProcedimento;
    private boolean confirmaAgendamentoComProcedimentos;
    private boolean cidObrigatorio;
    private String tipoConfiguracaoDialog;
    private List<CidBean> listaCids;
    private UnidadeDAO unidadeDAO;
    private boolean unidadeValidaDadosSigtap;
	private List<SituacaoAtendimentoBean> listaSituacoes;
	private SituacaoAtendimentoBean situacaoAtendimentoEvolucaoFalta;
	private String evolucaoFalta;

    private static final String ERRO = "Erro!";
    private static final String SENHA_ERRADA_OU_SEM_LIBERAÇÃO = "Funcionário com senha errada ou sem liberação!";
    private static final String DIALOG_LIBERACAO = "dlgLiberacao";
    private static final String PAGINA_AGENDA_AVULSA_PROFISSIONAL = "agendaavulsacominfoatendimento.faces";
    private static final String PAGINA_AGENDA_AVULSA = "agendaavulsa.faces";
    private static final String PAGINA_AGENDA_MEDICA = "agendaMedica.faces";

    private static final String CONFIGURACAO_AGENDA_MEDICA = "AG";
    private static final String CONFIGURACAO_AGENDA_AVULSA = "AA";
    private static final String CONFIGURACAO_CONSULTAR_AGENDAMENTOS = "CO";


    public AgendaController() throws ProjetoException {
        this.agenda = new AgendaBean();
        grupoSelecionado = new GrupoBean();
        listaGruposProgramas = new ArrayList<GrupoBean>();
        listaTipos = new ArrayList<TipoAtendimentoBean>();
        programaSelecionado = new ProgramaBean();
        this.listaNovosAgendamentos = new ArrayList<AgendaBean>();
        this.listaProfissional = new ArrayList<FuncionarioBean>();
        this.listaProfissionalPorGrupo = new ArrayList<FuncionarioBean>();
        this.listaAgendamentosData = new ArrayList<AgendaBean>();
        this.listaConsulta = new ArrayList<AgendaBean>();
        this.dataAtendimentoC = null;
        this.dataAtendimentoFinalC = null;
        this.cnsC = new String();
        this.protuarioC = null;
        this.tipoC = new TipoAtendimentoBean();
        this.situacao = new String();
        listaEquipePorTipoAtendimento = new ArrayList<EquipeBean>();
        tipoData = TipoDataAgenda.DATA_UNICA.getSigla();
        temLotado = false;
        listaHorariosOcupados = new ArrayList<AgendaBean>();
        agendamentosConfirmados = false;
        dataAtual = DataUtil.mesIhAnoAtual();
        agenda.getUnidade().setId(SessionUtil.recuperarDadosSessao().getUnidade().getId());
        tipoAtendimentoSelecionado = new TipoAtendimentoBean();
        agenda.setTurno(Turno.MANHA.getSigla());
        listaNaoPermitidosIntervaloDeDatas = new ArrayList<>();
        funcionario = new FuncionarioBean();
        listaHorarios = new ArrayList<>();
        listaFuncionariosDual = new DualListModel<FuncionarioBean>();
        this.listaFuncionariosSoucer = new ArrayList<>();
        this.listaFuncionariosTarget = new ArrayList<>();
        this.listaFuncionariosSoucerFiltro = new ArrayList<>();
        this.listaFuncionariosTargetFiltro = new ArrayList<>();
        this.listaPacientesComInformacaoDTO = new ArrayList<>();
        this.listaPacientesComInformacaoDTOFiltro = new ArrayList<>();
        this.listaPacientesSelecionadosComInformacaoDTO = new ArrayList<>();
        this.pacienteDAO = new PacienteDAO();
        this.user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
                .getSessionMap().get("obj_funcionario");
        this.listaLiberacoes = new ArrayList<>();
        this.usuarioLiberacao = new FuncionarioBean();
        this.setEvolucaoFalta("<p>Paciente não compareceu à instituição.</p><p><br></p><p><strong><em>Falta registrada por coordenação.</em></strong></p>");
        this.listaIdFuncionariosComDuplicidadeEspecialidade = new ArrayList<>();
        this.listaCids = new ArrayList<>();
        this.unidadeDAO = new UnidadeDAO();
        verificarUnidadeEstaConfiguradaParaValidarDadosDoSigtap();
        this.cidObrigatorio = user_session.getUnidade().getParametro().isCidAgendaObrigatorio();
        this.setSituacaoAtendimentoEvolucaoFalta(new SituacaoAtendimentoBean());
    }

    public void limparDados() {
        this.agenda = new AgendaBean();
        this.listaNovosAgendamentos = new ArrayList<AgendaBean>();
        this.listaAgendamentosData = new ArrayList<AgendaBean>();
        this.listaConsulta = new ArrayList<AgendaBean>();
        this.dataAtendimentoC = null;
        this.cnsC = new String();
        this.protuarioC = null;
        this.tipoC = new TipoAtendimentoBean();
        this.habilitarDetalhes = false;
        this.situacao = new String();
        agenda.setTurno(new String());
        rowBean = new AgendaBean();
        agenda.getUnidade().setId(SessionUtil.recuperarDadosSessao().getUnidade().getId());
        this.agenda.setProfissional(null);
        this.agenda.setProfissional(new FuncionarioBean());
        this.listaPacientesSelecionadosComInformacaoDTO.clear();
        confirmaAgendamentoComProcedimentos = false;
        incluirProcedimentos = false;
    }

    public void configuraDialogsParaAgendaAvulsa() {
        tipoConfiguracaoDialog = CONFIGURACAO_AGENDA_AVULSA;
    }

    public void configuraDialogsParaAgendaMedica() {
        tipoConfiguracaoDialog = CONFIGURACAO_AGENDA_MEDICA;
    }

    public void configuraDialogsParaConsultaAtendimentos() {
        tipoConfiguracaoDialog = CONFIGURACAO_CONSULTAR_AGENDAMENTOS;
    }

    public void preparaVerificarDisponibilidadeDataECarregarDiasAtendimento() throws ProjetoException, ParseException {
        preparaVerificarDisponibilidadeData();
        listaDiasDeAtendimentoAtuais();
    }

    public void carregaListaFuncionariosDual() throws ProjetoException {
        listaFuncionariosSoucer.clear();
        listaFuncionariosTarget.clear();
        listaFuncionariosSoucerFiltro.clear();
        listaFuncionariosTargetFiltro.clear();
        listaLiberacoes.clear();
        listaFuncionariosSoucer();
    }

    public List<FuncionarioBean> listaFuncionariosSoucer() throws ProjetoException {
        if (listaFuncionariosSoucer.isEmpty()) {
            FuncionarioDAO udao = new FuncionarioDAO();
            listaFuncionariosSoucer = udao.listarProfissionalAtendimento();
        }
        listaFuncionariosSoucerFiltro.addAll(listaFuncionariosSoucer);
        return listaFuncionariosSoucer;
    }

    public void validaFuncionarioParaAdicionar(FuncionarioBean funcionarioSelecionado) throws ProjetoException, SQLException {
        agenda.setDataAtendimento(dataAtendimentoC);
        if(camposValidosParaValidarEspecialidadePaciente(agenda.getPaciente(), agenda.getDataAtendimento())
                && !existeEspecialidaAgendaAvulsaNaInsercaoFuncionario(agenda, funcionarioSelecionado)) {
            adicionarFuncionario(funcionarioSelecionado);
        }
    }

    private void adicionarFuncionario(FuncionarioBean funcionarioSelecionado) throws ProjetoException, SQLException {
        if (!funcionarioExisteLista(funcionarioSelecionado)) {
            this.listaFuncionariosTarget.add(funcionarioSelecionado);
            this.listaFuncionariosTargetFiltro.add(funcionarioSelecionado);
            this.listaFuncionariosSoucer.remove(funcionarioSelecionado);
            this.listaFuncionariosSoucerFiltro.remove(funcionarioSelecionado);
            this.funcionarioSelecionado = null;
        }
    }

    public void adicionarTodosFuncionarios() {
        if(this.listaFuncionariosSoucerFiltro.isEmpty()) {
            this.listaFuncionariosTarget.removeAll(this.listaFuncionariosSoucer);
            this.listaFuncionariosTargetFiltro.removeAll(this.listaFuncionariosSoucer);

            this.listaFuncionariosTarget.addAll(this.listaFuncionariosSoucer);
            this.listaFuncionariosTargetFiltro.addAll(this.listaFuncionariosSoucer);

            this.listaFuncionariosSoucer.clear();
        }
        else {
            this.listaFuncionariosTarget.removeAll(this.listaFuncionariosSoucerFiltro);
            this.listaFuncionariosTargetFiltro.removeAll(this.listaFuncionariosSoucerFiltro);

            this.listaFuncionariosTarget.addAll(this.listaFuncionariosSoucerFiltro);
            this.listaFuncionariosTargetFiltro.addAll(this.listaFuncionariosSoucerFiltro);

            this.listaFuncionariosSoucer.removeAll(this.listaFuncionariosSoucerFiltro);
            this.listaFuncionariosSoucerFiltro.clear();
        }
    }

    private boolean funcionarioExisteLista(FuncionarioBean funcionarioSelecionado) {
        if(!listaFuncionariosTarget.isEmpty()) {
            for (int i = 0; i < listaFuncionariosTarget.size(); i++) {
                if (listaFuncionariosTarget.get(i).getId().equals(funcionarioSelecionado.getId()))
                    return true;
            }
        }
        return false;
    }

    public void removerFuncionario(FuncionarioBean funcionarioSelecionado) throws ProjetoException, SQLException {
        this.listaFuncionariosTarget.remove(funcionarioSelecionado);
        this.listaFuncionariosTargetFiltro.remove(funcionarioSelecionado);
        this.listaFuncionariosSoucer.add(funcionarioSelecionado);
        this.listaFuncionariosSoucerFiltro.add(funcionarioSelecionado);

        if(listaIdFuncionariosComDuplicidadeEspecialidade.contains(funcionarioSelecionado.getId()))
            this.listaLiberacoes.remove(MotivoLiberacao.DUPLICIDADE_ESPECIALIDADE.getTitulo());
        if(listaLiberacoes.isEmpty())
            usuarioLiberacao = new FuncionarioBean();
    }

    public void removerTodosFuncionarios() {
        this.listaFuncionariosSoucer.addAll(this.listaFuncionariosTargetFiltro);
        this.listaFuncionariosSoucerFiltro.addAll(this.listaFuncionariosTargetFiltro);
        this.listaFuncionariosTarget.removeAll(this.listaFuncionariosTargetFiltro);
        this.listaFuncionariosTargetFiltro.clear();

        listaLiberacoes.clear();
        usuarioLiberacao = new FuncionarioBean();
    }

    public void agendaAvulsaInit() throws ProjetoException, ParseException {
        carregarHorarioOuTurno();
        carregaListaFuncionariosDual();
    }


    public void carregarHorarioOuTurno() throws ProjetoException, ParseException {
        opcaoAtendimento = HorarioOuTurnoUtil.retornarOpcaoAtendimentoUnidade();

        if (opcaoAtendimento.equals(OpcaoAtendimento.SOMENTE_HORARIO.getSigla())
                || opcaoAtendimento.equals(OpcaoAtendimento.AMBOS.getSigla())) {
            gerarHorariosAtendimento();
        }

    }

    public void limparTurno() {
        this.agenda.setTurno(new String());
    }

    public void carregarTipoDeAtendimentoParaProgramaAvaliacao() throws ProjetoException {
        TipoAtendimentoDAO tDao = new TipoAtendimentoDAO();
        Integer tipoAtendimento = tDao.listarTipoDeAtendimentoDoPrograma(agenda.getProgramaAvaliacao().getIdPrograma());
        if (tipoAtendimento != null) {
            agenda.setTipoAt(tDao.listarTipoPorId(tipoAtendimento));
            agenda.getTipoAt().setProfissional(false);
            agenda.getTipoAt().setPrimeiroAt(true);
            validarTipoAtendimentoNaAgenda(null);
        } else
            JSFUtil.adicionarMensagemAdvertencia(
                    "Não existe tipo de atendimento do tipo 'Avaliação' associado ao Programa selecionado! Verifique o Cadastro.",
                    "ATENÇÃO");
    }

    public void validarTipoAtendimentoNaAgenda(SelectEvent event) throws ProjetoException {
        if (!VerificadorUtil.verificarSeObjetoNulo(event)) {
            this.tipoAtendimentoSelecionado = (TipoAtendimentoBean) event.getObject();

            agenda.setTipoAt(new TipoAtendimentoDAO()
                    .listarInformacoesTipoAtendimentoEquieProgramaPorId(tipoAtendimentoSelecionado.getIdTipo()));

        }

        if (agenda.getTipoAt().getIntervaloMinimo() > 0) {

            Boolean intervalo = aDao.retornarIntervaloUltimoAgendamento(agenda.getPaciente().getId_paciente(),
                    agenda.getTipoAt().getIdTipo(), agenda.getTipoAt().getIntervaloMinimo());

            if (!intervalo) {
                JSFUtil.adicionarMensagemErro(
                        "Paciente tem agendamento inferior ao mínimo para esse tipo de atendimento!", "Erro");

                agenda.setTipoAt(new TipoAtendimentoBean());
            }
        }
    }

    public void gerarHorarioAtendimentoAnalisandoHorariosCheios() throws ParseException, ProjetoException {
        if((!VerificadorUtil.verificarSeObjetoNuloOuZero(agenda.getEquipe().getCodEquipe()) || !VerificadorUtil.verificarSeObjetoNuloOuZero(agenda.getProfissional().getId()))
                && !VerificadorUtil.verificarSeObjetoNulo(agenda.getDataAtendimento())){
            gerarHorariosAtendimento();
            JSFUtil.atualizarComponente("opHorario");
        }
    }

    private void gerarHorariosAtendimento() throws ParseException, ProjetoException {
        listaHorarios = HorarioOuTurnoUtil.gerarHorariosAtendimentoOlhandoHorariosCheios(agenda.getEquipe().getCodEquipe(), agenda.getProfissional().getId(), agenda.getDataAtendimento());
    }

    public void preparaVerificarDisponibilidadeData() throws ProjetoException, ParseException {
        if (VerificadorUtil.verificarSeObjetoNuloOuVazio(agenda.getTurno()) && opcaoAtendimento.equals(OpcaoAtendimento.SOMENTE_TURNO.getSigla())) {
            JSFUtil.adicionarMensagemAdvertencia("Selecione o turno para o sistema verificar a disponibilidade de vagas", "");
            agenda.setDataAtendimento(null);
        }

        if (VerificadorUtil.verificarSeObjetoNuloOuVazio(agenda.getHorario()) && opcaoAtendimento.equals(OpcaoAtendimento.SOMENTE_HORARIO.getSigla())) {
            JSFUtil.adicionarMensagemAdvertencia("Selecione o horário para o sistema verificar a disponibilidade de vagas", "");
            agenda.setDataAtendimento(null);
        }

        else {
            if (tipoData.equals(TipoDataAgenda.DATA_UNICA.getSigla())) {
                if ((this.agenda.getAvaliacao() == null || this.agenda.getAvaliacao() == false)
                        && this.agenda.getPrograma().getIdPrograma() == null
                        || (this.agenda.getAvaliacao() != null && this.agenda.getAvaliacao() == true)
                        && this.agenda.getProgramaAvaliacao().getIdPrograma() == null
                        // || this.agenda.getPaciente().getId_paciente() == null
                        || ((this.agenda.getAvaliacao() == null || this.agenda.getAvaliacao() == false)
                        && this.agenda.getGrupo().getIdGrupo() == null)
                        || this.agenda.getTipoAt().getIdTipo() == null || this.agenda.getDataAtendimento() == null
                        || ((this.agenda.getAvaliacao() == null || this.agenda.getAvaliacao() == false)
                        && this.agenda.getProfissional().getId() == null
                        && this.agenda.getEquipe().getCodEquipe() == null)) {
                    return;
                } else {
                    verificaDisponibilidadeDataUnica();
                }

            } else if (tipoData.equals(TipoDataAgenda.INTERVALO_DE_DATAS.getSigla())) {
                if (this.agenda.getPrograma() == null || this.agenda.getGrupo() == null
                        || this.agenda.getPaciente() == null || this.agenda.getTipoAt() == null
                        || (this.agenda.getDataAtendimento() == null || this.agenda.getDataAtendimentoFinal() == null)
                        || (this.agenda.getProfissional() == null && this.agenda.getEquipe() == null)) {
                    return;
                } else {
                    verificaDisponibilidadeDataUnica();
                }
            }

            gerarHorarioAtendimentoAnalisandoHorariosCheios();
        }
    }

    public void setarQuantidadeIhMaximoComoNulos() {
        agenda.setQtd(null);
        agenda.setMax(null);
    }

    public void verificaDisponibilidadeDataUnica() throws ProjetoException {
        if (verificarSeEhFeriadoDataUnica()) {
            JSFUtil.adicionarMensagemErro("Data com feriado, não é permitido fazer agendamento!", "Erro");
            setarQuantidadeIhMaximoComoNulos();

        } else if (agenda.getProfissional().getId() != null && verificarSeTemBloqueioDataUnica()) {
            JSFUtil.adicionarMensagemErro("Data com bloqueio, não é permitido fazer agendamento!", "Erro");
            setarQuantidadeIhMaximoComoNulos();

        } else if (verificarSeAtingiuLimiteTipoDeAtendimentoDataUnica() && !agenda.getEncaixe()) {
            JSFUtil.adicionarMensagemErro(
                    "Atingiu o limite máximo para esse tipo de atendimento e Profissional/Equipe!", "Erro");
            setarQuantidadeIhMaximoComoNulos();
        } else {
            agenda.setQtd(0);
            agenda.setMax(0);
            verConfigAgenda();
        }

    }

    public Boolean verificarSeAtingiuLimiteTipoDeAtendimentoDataUnica() throws ProjetoException {
        Boolean retorno = false;

        if ((agenda.getTipoAt().getProfissional()) && (agenda.getProfissional().getId() != null)) {
            if (verificarSeExisteTipoAtendimentoEspecificoProfissional()) {

                if (verificarSeAtingiuLimitePorTipoDeAtendimentoPorProfissional()) {
                    retorno = true;
                }
            }
        }

        if ((agenda.getTipoAt().getEquipe()) && (agenda.getEquipe().getCodEquipe() != null)) {
            if (verificarSeExisteTipoAtendimentoEspecificoEquipe()) {

                if (verificarSeAtingiuLimitePorTipoDeAtendimentoPorEquipe()) {
                    retorno = true;
                }
            }
        }

        if ((agenda.getTipoAt().getPrimeiroAt()) && (agenda.getEquipe().getCodEquipe() != null)) {
            if (verificarSeExisteTipoAtendimentoEspecificoAvaliacao()) {

                if (verificarSeAtingiuLimitePorTipoDeAtendimentoPorEquipe()) {
                    retorno = true;
                }
            }
        }

        return retorno;
    }

    public void verificarDisponibilidadeDataEspecifica(Integer quantidade, Integer maxima) {
        if (quantidade >= maxima) {
            JSFUtil.adicionarMensagemErro("Já atingiu a quantidade máxima para essa data específica!", ERRO);
        }

    }

    public Boolean verificarSeEhFeriadoDataUnica() throws ProjetoException {
        Boolean retorno = false;

        retorno = new FeriadoDAO().verificarSeEhFeriadoDataUnica(this.agenda.getDataAtendimento());

        return retorno;
    }

    public Boolean verificarSeTemBloqueioDataUnica() throws ProjetoException {
        Boolean retorno = false;

        retorno = new BloqueioDAO().verificarBloqueioProfissionalDataUnica(this.agenda.getProfissional().getId(),
                this.agenda.getDataAtendimento(), this.agenda.getTurno());

        return retorno;
    }

    public Boolean verificarSeExisteTipoAtendimentoEspecificoProfissional() throws ProjetoException {
        Boolean retorno = false;

        retorno = aDao.verificarSeExisteConfigAgendaProfissionalPorTipoAtendimento(
                this.agenda.getProfissional().getId(), this.agenda.getDataAtendimento(), this.agenda.getTurno());

        return retorno;
    }

    public Boolean verificarSeExisteTipoAtendimentoEspecificoEquipe() throws ProjetoException {
        Boolean retorno = false;

        retorno = aDao.verificarSeExisteConfigAgendaEquipePorTipoAtendimento(this.agenda.getEquipe().getCodEquipe(),
                this.agenda.getDataAtendimento(), this.agenda.getTurno());

        return retorno;
    }

    public Boolean verificarSeExisteTipoAtendimentoEspecificoAvaliacao() throws ProjetoException {
        Boolean retorno = false;

        retorno = aDao.verificarSeExisteConfigAgendaEquipeAvaliacao(this.agenda.getEquipe().getCodEquipe(),
                this.agenda.getDataAtendimento(), this.agenda.getTurno());

        return retorno;
    }

    public Boolean verificarSeAtingiuLimitePorTipoDeAtendimentoPorProfissional() throws ProjetoException {
        Boolean retorno = false;

        Integer limite = aDao.contarAtendimentosPorTipoAtendimentoPorProfissionalDataUnica(this.agenda,
                this.agenda.getDataAtendimento());

        Integer maximo = 0;

        // primeiro vai verificar se existe quantidade por tipo de atendimento para o
        // mes atual, caso nao existe prossegue
        // com a verificacao da quantidade por tipo de atendimento geral
        maximo = aDao.contaConfigEspecificaQtdMaxTipoAtendimentoProfissional(this.agenda.getProfissional().getId(),
                this.agenda.getDataAtendimento(), this.agenda.getTurno(), this.agenda.getTipoAt().getIdTipo());
        if (maximo == 0)
            maximo = aDao.contaConfigGeralQtdMaxTipoAtendimentoProfissional(this.agenda.getProfissional().getId(),
                    this.agenda.getTurno(), this.agenda.getTipoAt().getIdTipo());
        maximo = aDao.verQtdMaxAgendaEspecDataEspecifica(this.agenda);

        if (maximo != null && limite >= maximo && limite > 0) {
            retorno = true;
        }

        return retorno;
    }

    public Boolean verificarSeAtingiuLimitePorTipoDeAtendimentoPorEquipe() throws ProjetoException {
        Boolean retorno = false;

        Integer limite = aDao.contarAtendimentosPorTipoAtendimentoPorEquipeDataUnica(this.agenda,
                this.agenda.getDataAtendimento());

        Integer maximo = 0;

        // primeiro vai verificar se existe quantidade por tipo de atendimento para o
        // mes atual, caso nao existe prossegue
        // com a verificacao da quantidade por tipo de atendimento geral
        maximo = aDao.contaConfigEspecificaQtdMaxTipoAtendimentoEquipe(this.agenda.getEquipe().getCodEquipe(),
                this.agenda.getDataAtendimento(), this.agenda.getTurno(), this.agenda.getTipoAt().getIdTipo());
        if ((maximo != null) && (maximo == 0))
            maximo = aDao.contaConfigGeralQtdMaxTipoAtendimentoEquipe(this.agenda.getEquipe().getCodEquipe(),
                    this.agenda.getTurno(), this.agenda.getTipoAt().getIdTipo());
        maximo = aDao.verQtdMaxAgendaEspecDataEspecifica(this.agenda);

        if (maximo != null && limite >= maximo && limite > 0) {
            retorno = true;
        }

        return retorno;
    }

    public Boolean verificarSeExisteTipoAtendimentoEspecificoIntervaloDeDatas(Date data) throws ProjetoException {
        Boolean retorno = false;

        // retorno =
        // aDao.verificarSeExisteTipoAtendimentoEspecificoDataUnica(this.agenda.getProfissional().getId(),
        // data,
        // this.agenda.getTurno());

        return retorno;
    }

    public Boolean verificarSeAtingiuLimitePorTipoDeAtendimentoIntervaloDeDatas(Date data) throws ProjetoException {

        Boolean retorno = false;

        Integer limite = aDao.contarAtendimentosPorTipoAtendimentoPorProfissionalDataUnica(this.agenda, data);
//VERIFICAR ABAIXO WALTER
        // Integer maximo = aDao.verQtdMaxAgendaEspec(this.agenda);
        Integer maximo = aDao.verQtdMaxAgendaEspecDataEspecifica(this.agenda);

        if (limite >= maximo) {
            retorno = true;
        }

        return retorno;
    }

    public Boolean podeAdicionarAposVerificarTipoDeAtendimentoIntervaloDeDatas(Date data) throws ProjetoException {
        Boolean retorno = true;

        if (verificarSeExisteTipoAtendimentoEspecificoIntervaloDeDatas(data)) {
            retorno = verificarSeAtingiuLimitePorTipoDeAtendimentoIntervaloDeDatas(data);
        }

        return retorno;
    }

    public void verConfigAgenda() throws ProjetoException {
        Boolean dtEspecifica = aDao.buscarDataEspecifica(this.agenda);
        Boolean diaSemEspecifico = aDao.buscarDiaSemanaMesAnoEspecifico(this.agenda);
        if (dtEspecifica) {
            listarAgendamentosData();
            if (aDao.verificarSeExisteConfigAgendaDataEspecificaPorTipoAtendimento(this.agenda)) {
                this.agenda.setMax(aDao.verQtdMaxAgendaDataEspecificaPorTipoAtendimento(this.agenda));
            } else
                this.agenda.setMax(aDao.verQtdMaxAgendaDataEspecifica(this.agenda));
            this.agenda.setQtd(aDao.verQtdAgendadosData(this.agenda));

            verificarDisponibilidadeDataEspecifica(agenda.getQtd(), agenda.getMax());

        } else if (diaSemEspecifico) {
            listarAgendamentosData();
            if (aDao.verificarSeExisteConfigAgendaDiaSemanaEspecificaPorTipoAtendimento(this.agenda))
                this.agenda.setMax(aDao.verQtdMaxConfigAgendaDiaSemanaEspecificaPorTipoAtendimento(this.agenda));
            else
                this.agenda.setMax(aDao.verQtdMaxConfigAgendaDiaSemanaEspecifica(this.agenda));
            this.agenda.setQtd(aDao.verQtdAgendadosEspec(this.agenda));
        } else {
            listarAgendamentosData();
            if (aDao.verificarSeExisteConfigAgendaDiaSemanaGeralPorTipoAtendimento(this.agenda))
                this.agenda.setMax(aDao.verQtdMaxConfigAgendaDiaSemanaGeralPorTipoAtendimento(this.agenda));
            else
                this.agenda.setMax(aDao.verQtdMaxConfigAgendaDiaSemanaGeral(this.agenda));
            this.agenda.setQtd(aDao.verQtdAgendadosData(this.agenda));
        }

    }

    public void validarParaConfirmar() throws ProjetoException {
        if (verificarEncaixe()) {
            preparaConfirmar();
        } else {
            JSFUtil.abrirDialog("dlgSenhaEncaixe");
        }
    }

    public Boolean verificarSeFoiAdicionadoODiaNaLista() {
        for (int i = 0; i < listaNovosAgendamentos.size(); i++) {
            if (agenda == listaNovosAgendamentos.get(i)) {
                return true;
            }
        }
        return false;
    }

    public void imprime(){
        System.out.println(agenda.getProfissional().getNome());
    }

    public Boolean verificarSeDiaFoiAdicionadoNosNovosAgendamentos(){

        for(int i = 0; i < listaNovosAgendamentos.size(); i++){
            if(listaNovosAgendamentos.get(i).getDataAtendimento().equals(agenda.getDataAtendimento())){
                return false;
            }
        }

        return true;
    }

    public void preparaConfirmar() throws ProjetoException {
        if(!verificarSeDiaFoiAdicionadoNosNovosAgendamentos()){
            JSFUtil.adicionarMensagemErro("Essa data já foi adicionada a lista de agendamentos", "Erro");
            return;
        }

        if (tipoData.equals(TipoDataAgenda.DATA_UNICA.getSigla())) {
            if ((agenda.getAvaliacao() == true) && (agenda.getTipoAt().getIdTipo() == null)) {
                JSFUtil.adicionarMensagemErro(
                        "Não existe tipo de atendimento relacionado ao Programa! Verifique o Cadastro!!", "Erro");
            } else if ((agenda.getMax() == null || agenda.getQtd() == null) && !agenda.getEncaixe()) {
                JSFUtil.adicionarMensagemErro("Não existe disponibilidade de vaga para este dia!!", "Erro");
            } else if ((VerificadorUtil.verificarSeObjetoNuloOuZero(agenda.getMax())
                    || agenda.getQtd() >= agenda.getMax()) && !agenda.getEncaixe()) {
                JSFUtil.adicionarMensagemErro("Não existe disponibilidade de vaga para este dia!!", "Erro");
            } else {
                addListaNovosAgendamentos();
                agendamentosConfirmados = true;
            }
        }
        if (tipoData.equals(TipoDataAgenda.INTERVALO_DE_DATAS.getSigla())) {

            List<Date> listaAgendamentoPermitidos = new ArrayList<>();
            Date data1 = agenda.getDataAtendimento();
            Date data2 = agenda.getDataAtendimentoFinal();

            if (verAgendaIntervalo()) {

                if (agenda.getTipoAt().getProfissional()) {
                    listaNaoPermitidosIntervaloDeDatas = listarDatasBloqueadas(data1, data2);
                }

                listaNaoPermitidosIntervaloDeDatas = listarDatasComFeriado(listaNaoPermitidosIntervaloDeDatas, data1,
                        data2);

                while (data1.before(data2) || data1.equals(data2)) {
                    int contador = 0;

                    for (int i = 0; i < listaNaoPermitidosIntervaloDeDatas.size(); i++) {
                        if (data1.equals(listaNaoPermitidosIntervaloDeDatas.get(i))) {
                            contador++;
                        }
                    }

                    if (contador == 0) {

                        if (agenda.getTipoAt().getProfissional()) {
                            if (podeAdicionarAposVerificarTipoDeAtendimentoIntervaloDeDatas(data1)) {
                                listaAgendamentoPermitidos.add(data1);
                            } else {
                                listaNaoPermitidosIntervaloDeDatas.add(data1);
                            }
                        }
                        if (agenda.getTipoAt().getEquipe()) {
                            listaAgendamentoPermitidos.add(data1);
                        }

                    } else {
                        listaNaoPermitidosIntervaloDeDatas.add(data1);
                    }
                    data1 = DataUtil.adicionarDiasAData(data1, 1);
                }
            }
            this.listaNovosAgendamentos = new ArrayList<>();
            agendamentosConfirmados = true;

            for (int j = 0; j < listaAgendamentoPermitidos.size(); j++) {
                agenda.setDataAtendimento(listaAgendamentoPermitidos.get(j));
                listaNovosAgendamentos.add(agenda);
            }
        }
    }

    public List<Date> listarDatasBloqueadas(Date dataInicio, Date dataFinal) throws ProjetoException {
        BloqueioDAO bloqueioDAO = new BloqueioDAO();

        return bloqueioDAO.verificarBloqueioProfissionalIntervaloDeDatas(agenda.getProfissional().getId(), dataInicio,
                dataFinal, agenda.getTurno());
    }

    public List<Date> listarDatasComFeriado(List<Date> listaDatasBloqueadas, Date dataInicio, Date dataFinal)
            throws ProjetoException {
        FeriadoDAO feriadoDAO = new FeriadoDAO();

        List<Date> listaFeriado = feriadoDAO.verificarSeEhFeriadoIntervaloDeDatas(dataInicio, dataFinal);

        for (int i = 0; i < listaFeriado.size(); i++) {
            listaDatasBloqueadas.add(listaFeriado.get(i));
        }

        return listaDatasBloqueadas;
    }

    public Boolean verAgendaIntervalo() throws ProjetoException {
        if (this.agenda.getPaciente() == null || this.agenda.getPrograma() == null || this.agenda.getGrupo() == null
                || this.agenda.getTipoAt() == null || this.agenda.getDataAtendimento() == null
                || this.agenda.getDataAtendimentoFinal() == null
                || (this.agenda.getProfissional() == null && this.agenda.getEquipe() == null)) {
            JSFUtil.adicionarMensagemErro("Campo(s) obrigatório(s) em falta!", "Erro");
            return false;
        } else {
            Boolean retorno = false;

            boolean dtEspecifica = false;
            boolean diaSem = false;
            boolean temData = false;
            int j = 0;

            DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            df.setLenient(false);
            Date d1 = agenda.getDataAtendimento();
            Date d2 = agenda.getDataAtendimentoFinal();
            Long dt = (d2.getTime() - d1.getTime());

            dt = (dt / 86400000L);
            for (int i = 0; i <= dt; i++) {
                temData = false;
                Calendar c = Calendar.getInstance();
                c.setTime(agenda.getDataAtendimento());

                if (i > 0) {
                    c.add(Calendar.DAY_OF_MONTH, 1);
                }

                agenda.setDataAtendimento(c.getTime());
                dtEspecifica = aDao.buscarDataEspecifica(this.agenda);
                diaSem = aDao.buscarDiaSemanaMesAnoEspecifico(this.agenda);

                if (dtEspecifica == true || diaSem == true) {
                    temData = true;
                    if (diaSem) {
                        this.agenda.setMax(aDao.verQtdMaxAgendaEspecDataEspecifica(this.agenda));
                        this.agenda.setQtd(aDao.verQtdAgendadosEspec(this.agenda));
                    }
                    if (dtEspecifica) {
                        this.agenda.setMax(aDao.verQtdMaxAgendaDataEspecifica(this.agenda));
                        this.agenda.setQtd(aDao.verQtdAgendadosData(this.agenda));
                    }
                }
                AgendaBean agendaAux = new AgendaBean();
                agendaAux.setDataAtendimento(agenda.getDataAtendimento());
                agendaAux.setPaciente(agenda.getPaciente());
                agendaAux.setPrograma(agenda.getPrograma());
                agendaAux.setGrupo(agenda.getGrupo());
                agendaAux.setTipoAt(agenda.getTipoAt());
                agendaAux.setProfissional(agenda.getProfissional());
                agendaAux.setEquipe(agenda.getEquipe());
                agendaAux.setMax(agenda.getMax());
                agendaAux.setQtd(agenda.getQtd());
                if (temData) {
                    if (agenda.getMax() == agenda.getQtd()) {

                        listaHorariosOcupados.add(agendaAux);

                    } else {

                        listaNovosAgendamentos.add(agendaAux);

                        j++;
                    }

                }
            }
            if (listaHorariosOcupados.size() > 0) {
                temLotado = true;
                JSFUtil.adicionarMensagemErro(
                        "Não foi possível agendar, pois tem horários lotados! Clique em Visualizar Ocupados para ver.",
                        "Erro");
            } else {
                retorno = true;
            }

            return retorno;

        }
    }

    public void carregarListaOcupados() {
        JSFUtil.abrirDialog("dlgOcupados");
        getListaHorariosOcupados();
    }

    public void addListaNovosAgendamentos() {
        this.listaNovosAgendamentos.add(this.agenda);
    }

    public void listarAgendamentosData() throws ProjetoException {
        this.listaAgendamentosData = aDao.listarAgendamentosData(this.agenda);
    }

    public void verificarPacienteAtivoInstituicao() throws ProjetoException {
        GerenciarPacienteDAO gerenciarPacienteDAO = new GerenciarPacienteDAO();

        if (procedimentoValido()) {
            Boolean pacienteAtivo = gerenciarPacienteDAO.verificarPacienteAtivoInstituicao(
                    agenda.getPaciente().getId_paciente(), agenda.getPrograma().getIdPrograma(),
                    agenda.getGrupo().getIdGrupo());

            if (VerificadorUtil.verificarSeObjetoNulo(agenda.getMax())
                    && VerificadorUtil.verificarSeObjetoNulo(agenda.getQtd())) {
                zerarValoresAgendaMaximoIhQuantidade();
            }

            if ((agenda.getEquipe().getCodEquipe() != null) && (agenda.getAvaliacao() == false)) {

                if (pacienteAtivo) {
                    gravarAgenda(new FuncionarioBean());
                } else {
                    funcionario = new FuncionarioBean();
                    JSFUtil.abrirDialog("dlgSenha");
                }
            } else if ((agenda.getProfissional().getId() != null) || (agenda.getAvaliacao() == true)) {
                gravarAgenda(new FuncionarioBean());
            }
        }
    }

    public void preparaGravarAgendaAvulsa() throws ProjetoException {

        limpaDadosDialogLiberacao();

        // verificar se existe algum campo nao preenchido
        if (this.agenda.getPaciente() == null || this.agenda.getPrograma() == null || this.agenda.getGrupo() == null
                || (this.agenda.getTipoAt() == null) || this.agenda.getDataAtendimento() == null
                || VerificadorUtil.verificarSeObjetoNuloOuZero(this.agenda.getGrupo().getIdGrupo())
                || VerificadorUtil.verificarSeObjetoNuloOuZero(this.agenda.getTipoAt().getIdTipo())) {
            JSFUtil.adicionarMensagemErro("Campo(s) obrigatório(s) em falta!", "Erro");
            return;
        }

        if (this.listaFuncionariosTarget.isEmpty()) {
            JSFUtil.adicionarMensagemErro("Informe o(s) Profissional(is) do Agendamento !", "Erro");
            return;
        }
        
        if (verificarSeEhFeriadoDataUnica()) {
            JSFUtil.adicionarMensagemErro("A data selecionada é um feriado!", "Erro");
            return;
        }

        if (procedimentoValido() && pacienteValido(agenda.getPaciente())) {

            if (VerificadorUtil.verificarSeObjetoNulo(agenda.getMax())
                    && VerificadorUtil.verificarSeObjetoNulo(agenda.getQtd())) {
                zerarValoresAgendaMaximoIhQuantidade();
            }

            if(!cidObrigatorio) {
                insereAutomaticamenteIdCidPrimarioEmAgendaAvulsa();
            }

            limpaDadosDialogLiberacao();
            boolean existeDuplicidadeAgendaAvulsa = existeDuplicidadeAgendaAvulsa();
            boolean permiteDuplicidade = user_session.getUnidade().getParametro().isPermiteAgendamentoDuplicidade();

            if ((existeDuplicidadeAgendaAvulsa && permiteDuplicidade)
                    || !existeDuplicidadeAgendaAvulsa && !permiteDuplicidade
                    || !existeDuplicidadeAgendaAvulsa && permiteDuplicidade) {
                gravarAgendaAvulsa(usuarioLiberacao);
            }
        }
    }

    private boolean procedimentoValido() {
        if(!incluirProcedimentos)
            return true;
        else if(incluirProcedimentos && (VerificadorUtil.verificarSeObjetoNulo(agenda.getProcedimento())
                || VerificadorUtil.verificarSeObjetoNuloOuZero(agenda.getProcedimento().getIdProc())) ) {
            JSFUtil.adicionarMensagemErro("Escolha o procedimento principal!", "");
            agenda.setProcedimento(new ProcedimentoBean());
            return false;
        }
        return true;
    }

    private void insereAutomaticamenteIdCidPrimarioEmAgendaAvulsa() throws ProjetoException {
        Integer idCid = aDao.retornaIdCidDoLaudo(this.agenda, user_session.getUnidade().getId(),
                agenda.getPaciente().getId_paciente());
        this.agenda.getCid().setIdCid(idCid);
    }

    private boolean pacienteValido(PacienteBean paciente) throws ProjetoException {

        if (user_session.getUnidade().getParametro().isAgendaAvulsaValidaPacienteAtivo()) {

            GerenciarPacienteDAO gerenciarPacienteDAO = new GerenciarPacienteDAO();
            Boolean pacienteAtivo = gerenciarPacienteDAO.verificarPacienteAtivoInstituicao(paciente.getId_paciente(),
                    agenda.getPrograma().getIdPrograma(), agenda.getGrupo().getIdGrupo());

            agenda.getTipoAt().setAgendaAvulsaValidaPacienteAtivo(
                    gerenciarPacienteDAO.tipoAtendimentoValidaPacienteAtivo(agenda.getTipoAt().getIdTipo()));

            if (!pacienteAtivo && agenda.getTipoAt().isAgendaAvulsaValidaPacienteAtivo()) {
                JSFUtil.adicionarMensagemErro("Paciente selecionado não está ativo neste programa e grupo", "");
                JSFUtil.adicionarMensagemErro("Este tipo de atendimento não permite pacientes inativos", "");
                return false;
            } else if (!pacienteAtivo && !agenda.getTipoAt().isAgendaAvulsaValidaPacienteAtivo())
                return true;

            return pacienteAtivo;
        }
        return true;
    }

    private boolean existeDuplicidadeAgendaAvulsa() throws ProjetoException {
        Boolean existeDuplicidade = aDao.verificarDuplicidadeAgendaAvulsa(agenda);

        if ((!user_session.getUnidade().getParametro().isPermiteAgendamentoDuplicidade()) && (existeDuplicidade)) {
            mensagemDialogLiberacao = "Duplicidade De Agenda Avulsa: Você está tentando realizar um agendamento avulso que já possui cadastro nesta \n"
                    + "data para este programa, grupo e tipo de atendimento! \n"
                    + "Digite o CPF e senha para liberação!";

            this.motivoLiberacao = MotivoLiberacao.DUPLICIDADE_AGENDA_AVULSA.getSigla();
            JSFUtil.abrirDialog(DIALOG_LIBERACAO);
            limpaDadosDialogLiberacao();
        }

        return existeDuplicidade;
    }

    public void validarSenhaLiberacaoAgendaAvulsa() throws ProjetoException {
        FuncionarioDAO funcionarioDAO = new FuncionarioDAO();

        usuarioLiberacao = funcionarioDAO.validarCpfIhSenha(funcionario.getCpf(), funcionario.getSenha(),
                ValidacaoSenha.LIBERACAO.getSigla());

        if (!VerificadorUtil.verificarSeObjetoNulo(usuarioLiberacao)) {
            listaLiberacoes.add(MotivoLiberacao.DUPLICIDADE_AGENDA_AVULSA.getTitulo());
            gravarAgendaAvulsa(usuarioLiberacao);
            if(!incluirProcedimentos)
                JSFUtil.fecharDialog(DIALOG_LIBERACAO);
        } else {
            JSFUtil.adicionarMensagemErro(SENHA_ERRADA_OU_SEM_LIBERAÇÃO, ERRO);
        }
    }

    public void validarSenhaLiberacaoEspecialidadeAgendaAvulsaPaciente() throws ProjetoException, SQLException {
        FuncionarioDAO funcionarioDAO = new FuncionarioDAO();

        usuarioLiberacao = funcionarioDAO.validarCpfIhSenha(funcionario.getCpf(), funcionario.getSenha(),
                ValidacaoSenha.LIBERACAO.getSigla());

        if(adicionarLiberacaoEspecialidade()) {
        	listaIdFuncionariosComDuplicidadeEspecialidade.add(this.funcionarioSelecionado.getId());
        	adicionarFuncionario(this.funcionarioSelecionado);
        }
    }

    public void validarSenhaLiberacaoAgendaComProcedimento() throws ProjetoException {
        FuncionarioDAO funcionarioDAO = new FuncionarioDAO();

        usuarioLiberacao = funcionarioDAO.validarCpfIhSenha(funcionario.getCpf(), funcionario.getSenha(),
                ValidacaoSenha.LIBERACAO.getSigla());

        if (!VerificadorUtil.verificarSeObjetoNulo(usuarioLiberacao)) {
            listaLiberacoes.add(MotivoLiberacao.AGENDA_AVULSA_COM_PROCEDIMENTO.getTitulo());
            confirmaAgendamentoComProcedimentos = true;
            String pagina = retornaPagina();

            if(pagina.contains(PAGINA_AGENDA_AVULSA))
                gravarAgendaAvulsa(usuarioLiberacao);
            else if(pagina.contains(PAGINA_AGENDA_AVULSA_PROFISSIONAL))
                gravarAgendaAvulsaPorProfissional(usuarioLiberacao);
            else if(pagina.contains(PAGINA_AGENDA_MEDICA))
                gravarAgenda(usuarioLiberacao);
            JSFUtil.fecharDialog(DIALOG_LIBERACAO);
        } else {
            JSFUtil.adicionarMensagemErro(SENHA_ERRADA_OU_SEM_LIBERAÇÃO, ERRO);
        }
    }

    private String retornaPagina() {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        return request.getRequestURL().toString();
    }

    private boolean adicionarLiberacaoEspecialidade() throws ProjetoException, SQLException {
        if (!VerificadorUtil.verificarSeObjetoNulo(usuarioLiberacao)) {
            listaLiberacoes.add(MotivoLiberacao.DUPLICIDADE_ESPECIALIDADE.getTitulo());
            JSFUtil.fecharDialog(DIALOG_LIBERACAO);
            return true;
        }
        return false;
    }

    public void validarSenhaLiberacaoEspecialidadeAgendaAvulsaProfissional() throws ProjetoException, SQLException {
        FuncionarioDAO funcionarioDAO = new FuncionarioDAO();

        usuarioLiberacao = funcionarioDAO.validarCpfIhSenha(funcionario.getCpf(), funcionario.getSenha(),
                ValidacaoSenha.LIBERACAO.getSigla());
        if(adicionarLiberacaoEspecialidade())
        	adicionarPacienteSelecionado(pacienteSelecionado);
    }

    private void limpaDadosDialogLiberacao() {
        this.funcionario.setCpf(new String());
        this.funcionario.setSenha(new String());
    }

    public void zerarValoresAgendaMaximoIhQuantidade() {
        agenda.setQtd(0);
        agenda.setMax(0);
    }

    public void abrirDialogEvolucaoFaltosos() {
    	usuarioLiberacao = new FuncionarioBean();
    	JSFUtil.abrirDialog("dlgEvolucaoFaltosos"); 
    }
    
    public boolean verificaSePerfilRealizaEvolucaoFalta(){
    	boolean realizaEvolucaoFalta = false;
    	if(user_session != null) {
    		if(user_session.getPerfil() != null){
    			FuncionarioDAO fDAO = new FuncionarioDAO();
    			try {
    				realizaEvolucaoFalta = fDAO.verificaSeRealizaEvolucaoFalta(user_session.getId());
				} catch (ProjetoException e) { /*e.printStackTrace();*/ }
    		}
    	}
    	return realizaEvolucaoFalta;
    }
    
    public void listarSituacoesAtendimentoEvolucaoFaltosos() {
    	//System.out.println("Listando Evolução");
    	SituacaoAtendimentoDAO situacaoAtendimentoDAO = new SituacaoAtendimentoDAO();
    	try {
    		List<SituacaoAtendimentoBean> l = situacaoAtendimentoDAO.listarSituacaoAtendimentoFaltaPaciente();
			this.setListaSituacoes(l);
		} catch (ProjetoException e) {
			// e.printStackTrace();
		}
    }
    
    public void evoluirFalta() {
    	JSFUtil.fecharDialog("dlgEvolucaoFaltosos");
    	AtendimentoDAO atDao = new AtendimentoDAO();
    	AgendaDAO agDao = new AgendaDAO();
    	
    	FuncionarioBean coordenador = user_session;
    	AgendaBean agendamento = rowBean;
    	//System.out.println(agendamento.getIdAgenda());
    	try {
			List<AtendimentoBean> atendimentos = atDao.listarAtendimentos1PorIdAtendimento(agendamento.getIdAgenda());
			
			AtendimentoBean atendimentoBase = atendimentos.get(0);
			AtendimentoBean atendimentoCoordenador = new AtendimentoBean();
			atendimentoCoordenador.setId(agendamento.getIdAgenda());
	        atendimentoCoordenador.setPaciente(agendamento.getPaciente());
	        if(coordenador.getListaCbos() != null && coordenador.getListaCbos().size() > 0) {	        	
	        	atendimentoCoordenador.setCbo(coordenador.getListaCbos().get(0));
	        } else {
	        	CboDAO cboDao = new CboDAO();
	        	CboBean cboCoordenadorDefault = cboDao.listarCboPorId(7);
	        	atendimentoCoordenador.setCbo(cboCoordenadorDefault);
	        }
	        atendimentoCoordenador.setDataAtendimento(agendamento.getDataAtendimento());
	        atendimentoCoordenador.setGrupo(agendamento.getGrupo());
	        atendimentoCoordenador.setPrograma(agendamento.getPrograma());
	        atendimentoCoordenador.setProcedimento(atendimentoBase.getProcedimento());
	        atendimentoCoordenador.setCidPrimario(atendimentoBase.getCidPrimario());
	        atendimentoCoordenador.setFuncionario(coordenador);
	        atendimentoCoordenador.setSituacaoAtendimento(situacaoAtendimentoEvolucaoFalta);
	        atendimentoCoordenador.setEvolucao(evolucaoFalta);

	        atDao.adicionarAtendimentoParaEvolucaoDeFalta(atendimentoCoordenador);
			for(AtendimentoBean at : atendimentos) {
				atDao.excluirAtendimentoParaEvolucaoDeFalta(at.getId1(), coordenador.getId());
			}
			agDao.finalizarAgendamento(agendamento.getIdAgenda());
			
		} catch (ProjetoException e) {
			// e.printStackTrace();
		}
    }
    
    //FUNÇÃO NÃO SERÁ UTILIZADA
    /*public void validarSenhaCoordenacao() throws ProjetoException {
        FuncionarioDAO funcionarioDAO = new FuncionarioDAO();

        FuncionarioBean usuarioLiberacao = funcionarioDAO.validarCpfIhSenha(coordenacao.getCpf(), coordenacao.getSenha(),
                ValidacaoSenha.EVOLUCAO_FALTA.getSigla());

        if (usuarioLiberacao != null) {
            JSFUtil.fecharDialog("dlgSenhaCoordenacao");
            System.out.println("COORD PERM AUTORIZADA");
            evoluirComoCoordenacao(usuarioLiberacao);
        } else {
            JSFUtil.adicionarMensagemErro(SENHA_COORD_ERRADA, ERRO);
            System.out.println("COORD PERM NEGADA");
        }
    }*/

    public void validarSenhaLiberacao() throws ProjetoException {
        FuncionarioDAO funcionarioDAO = new FuncionarioDAO();

        FuncionarioBean usuarioLiberacao = funcionarioDAO.validarCpfIhSenha(funcionario.getCpf(), funcionario.getSenha(),
                ValidacaoSenha.LIBERACAO.getSigla());

        if (usuarioLiberacao != null) {
            JSFUtil.fecharDialog("dlgSenha");
            gravarAgenda(usuarioLiberacao);
        } else {
            JSFUtil.adicionarMensagemErro(SENHA_ERRADA_OU_SEM_LIBERAÇÃO, ERRO);
        }
    }

    public void validarSenhaEncaixe() throws ProjetoException {
        FuncionarioDAO funcionarioDAO = new FuncionarioDAO();

        FuncionarioBean func = funcionarioDAO.validarCpfIhSenha(funcionario.getCpf(), funcionario.getSenha(),
                ValidacaoSenha.ENCAIXE.getSigla());

        if (func != null) {
            JSFUtil.fecharDialog("dlgSenhaEncaixe");
            preparaConfirmar();
        } else {
            JSFUtil.adicionarMensagemErro(SENHA_ERRADA_OU_SEM_LIBERAÇÃO, ERRO);
        }
    }

    public Boolean verificarEncaixe() {
        Boolean retorno = false;

        if (agenda.getEncaixe()) {
            if (SessionUtil.recuperarDadosSessao().getRealizaEncaixes()) {
                retorno = true;
            } else {
                JSFUtil.adicionarMensagemAdvertencia("Usuário não tem permissão para fazer encaixe!", "Atenção");
            }
        } else {
            retorno = true;
        }

        return retorno;
    }


    public void gravarAgenda(FuncionarioBean funcionarioLiberacao) throws ProjetoException {
        // verificar se existe algum campo nao preenchido
        if (this.agenda.getPaciente() == null || this.agenda.getPrograma() == null || this.agenda.getGrupo() == null
                || ((this.agenda.getTipoAt() == null) && (this.agenda.getAvaliacao() == false))
                || this.agenda.getDataAtendimento() == null) {
            JSFUtil.adicionarMensagemErro("Campo(s) obrigatório(s) em falta!", "Erro");
            return;
        }

        // verificar as quantidades de vagas
        if ((this.agenda.getMax() <= 0) && (!agenda.getEncaixe())) {
            JSFUtil.adicionarMensagemErro("Quantidade máxima inválida!", "Erro");
            return;
        }

        // verificar a quantidade de agendamentos
        if (this.agenda.getQtd() >= this.agenda.getMax() && !agenda.getEncaixe()) {
            JSFUtil.adicionarMensagemErro("Quantidade de agendamentos está no limite!", "Erro");
            return;
        }

        if (this.listaNovosAgendamentos.isEmpty()) {
            JSFUtil.adicionarMensagemErro("A lista de novos agendamentos está vazia!", "Erro");
            return;
        }

        if( (incluirProcedimentos && confirmaAgendamentoComProcedimentos)
                || !incluirProcedimentos) {
            boolean cadastrou = false;

            List<String> listaLiberacoesFiltradas = retornaLiberacoesNaoRepetidas();
            cadastrou = aDao.gravarAgenda(this.agenda, this.listaNovosAgendamentos, funcionarioLiberacao, listaLiberacoesFiltradas);

            if (cadastrou) {
                limparDados();
                this.agenda.setTurno(Turno.MANHA.getSigla());
                JSFUtil.adicionarMensagemSucesso("Agenda cadastrada com sucesso!", "Sucesso");
            } else {
                JSFUtil.adicionarMensagemErro("Ocorreu um erro durante o cadastro!", "Erro");
            }
            this.agenda.setTurno(Turno.MANHA.getSigla());
        }
        else if (incluirProcedimentos && !confirmaAgendamentoComProcedimentos) {
            configuraDialogLiberacaoParaAgendaComProcedimento();
        }
    }

    private void gravarAgendaAvulsa(FuncionarioBean usuarioLiberacao) throws ProjetoException {
        boolean cadastrou = false;

        List<String> listaLiberacoesFiltradas = retornaLiberacoesNaoRepetidas();
        cadastrou = aDao.gravarAgendaAvulsa(this.agenda, this.listaFuncionariosTarget, usuarioLiberacao,
                listaLiberacoesFiltradas);

        if (cadastrou) {
            limparDados();
            carregaListaFuncionariosDual();
            JSFUtil.fecharDialog(DIALOG_LIBERACAO);
            JSFUtil.adicionarMensagemSucesso("Agenda cadastrada com sucesso!", "Sucesso");
        } else {
            JSFUtil.adicionarMensagemErro("Ocorreu um erro durante o cadastro!", "Erro");
        }
    }

    private List<String> retornaLiberacoesNaoRepetidas() {
        List<String> listaLiberacoesFiltradas = new ArrayList<>();
        for (String liberacao : listaLiberacoes) {
            if (!listaLiberacoesFiltradas.contains(liberacao))
                listaLiberacoesFiltradas.add(liberacao);
        }
        return listaLiberacoesFiltradas;
    }

    public void validarAgendamentosInformandoAtendimento() throws ProjetoException {

        if (procedimentoValido() && existemPacientesAdicionados() && todosPacienteSelecionadoSaoAtivos()) {
            insereIdCidPrimarioEmPacientesSelecionados();

            limpaDadosDialogLiberacao();
            boolean existeDuplicidadeAgendaAvulsa = existeDuplicidadeAgendaAvulsa();
            boolean permiteDuplicidade = user_session.getUnidade().getParametro().isPermiteAgendamentoDuplicidade();

            if ((existeDuplicidadeAgendaAvulsa && permiteDuplicidade)
                    || !existeDuplicidadeAgendaAvulsa && !permiteDuplicidade
                    || !existeDuplicidadeAgendaAvulsa && permiteDuplicidade) {
                gravarAgendaAvulsaPorProfissional(usuarioLiberacao);
            }
        }
    }

    private void gravarAgendaAvulsaPorProfissional(FuncionarioBean usuarioLiberacao) throws ProjetoException {
        if( (incluirProcedimentos && confirmaAgendamentoComProcedimentos && cidValido(agenda.getCid()))
                || (!incluirProcedimentos && cidValido(agenda.getCid())) ) {

            boolean cadastrou = false;

            List<String> listaLiberacoesFiltradas = retornaLiberacoesNaoRepetidas();
            cadastrou = aDao.gravarAgendamentosInformandoAtendimento(this.agenda,
                    this.listaPacientesSelecionadosComInformacaoDTO, usuarioLiberacao, listaLiberacoesFiltradas);

            if (cadastrou) {
                limparDados();
                listarPacientes();
                JSFUtil.fecharDialog(DIALOG_LIBERACAO);
                JSFUtil.adicionarMensagemSucesso("Agendamentos cadastrados com sucesso!", "Sucesso");
            } else {
                JSFUtil.adicionarMensagemErro("Ocorreu um erro durante o cadastro!", "Erro");
            }
        } else if (incluirProcedimentos && !confirmaAgendamentoComProcedimentos) {
            configuraDialogLiberacaoParaAgendaComProcedimento();
        }
    }

    private void configuraDialogLiberacaoParaAgendaComProcedimento() {
        limpaDadosDialogLiberacao();
        JSFUtil.fecharDialog(DIALOG_LIBERACAO);
        mensagemDialogLiberacao = "Liberação de Agendamento com Procedimento:\n Você está tentando gravar um agendamento do qual você está informando o(s) procedimento(s) utilizado(s).\r\n" +
                "					Você está ciente de que quaisquer erros em relação ao sigtap ou outras validações são de sua responsabilidade?";
        motivoLiberacao = MotivoLiberacao.AGENDA_AVULSA_COM_PROCEDIMENTO.getSigla();
        JSFUtil.abrirDialog(DIALOG_LIBERACAO);
    }

    public void validarSenhaLiberacaoAgendamentosInformandoAtendimento() throws ProjetoException {
        FuncionarioDAO funcionarioDAO = new FuncionarioDAO();

        usuarioLiberacao = funcionarioDAO.validarCpfIhSenha(funcionario.getCpf(), funcionario.getSenha(),
                ValidacaoSenha.LIBERACAO.getSigla());

        if (!VerificadorUtil.verificarSeObjetoNulo(usuarioLiberacao)) {
            listaLiberacoes.add(MotivoLiberacao.DUPLICIDADE_AGENDA_AVULSA.getTitulo());
            gravarAgendaAvulsaPorProfissional(usuarioLiberacao);
            if(!incluirProcedimentos)
                JSFUtil.fecharDialog(DIALOG_LIBERACAO);
        } else {
            JSFUtil.adicionarMensagemErro(SENHA_ERRADA_OU_SEM_LIBERAÇÃO, ERRO);
        }
    }

    private boolean todosPacienteSelecionadoSaoAtivos() throws ProjetoException {
        GerenciarPacienteDAO gerenciarPacienteDAO = new GerenciarPacienteDAO();

        Boolean retorno = true;
        if (user_session.getUnidade().getParametro().isAgendaAvulsaValidaPacienteAtivo()) {
            for (PacientesComInformacaoAtendimentoDTO pacienteDTO : listaPacientesSelecionadosComInformacaoDTO) {
                Boolean pacienteAtivo = gerenciarPacienteDAO.verificarPacienteAtivoInstituicao(
                        pacienteDTO.getPaciente().getId_paciente(), agenda.getPrograma().getIdPrograma(),
                        agenda.getGrupo().getIdGrupo());

                if (!pacienteAtivo) {
                    JSFUtil.adicionarMensagemErro("Paciente " + pacienteDTO.getPaciente().getNome()
                            + " não está ativo neste programa e grupo", "");
                    retorno = false;
                }
            }
        }
        return retorno;
    }

    private boolean existemPacientesAdicionados() {
        if (this.listaPacientesSelecionadosComInformacaoDTO.isEmpty()) {
            JSFUtil.adicionarMensagemErro("Adicione pelo menos um paciente", "");
            return false;
        }
        return true;
    }

    public void listarPacientes() throws ProjetoException {
        this.listaPacientesSelecionadosComInformacaoDTO.clear();
        this.listaPacientesComInformacaoDTOFiltro.clear();
        this.listaPacientesComInformacaoDTO = pacienteDAO.listaPacientesComInformacaoDTO();
        this.listaPacientesComInformacaoDTOFiltro.addAll(listaPacientesComInformacaoDTO);
        this.listaLiberacoes.clear();
    }

    public void validaPacienteParaAdicionar(PacientesComInformacaoAtendimentoDTO pacienteSelecionado)
            throws ProjetoException, SQLException {
        agenda.setDataAtendimento(dataAtendimentoC);
        if (camposValidosParaValidarEspecialidadeProfissional(agenda.getProfissional(), agenda.getDataAtendimento())
                && !existeEspecialidaAgendaAvulsaNaInsercaoPaciente(agenda, pacienteSelecionado)) {
            adicionarPacienteSelecionado(pacienteSelecionado);
        }
    }

    public void adicionarPacienteSelecionado(PacientesComInformacaoAtendimentoDTO pacienteSelecionado) {
        if (!pacienteFoiAdicionado(pacienteSelecionado))
            this.listaPacientesSelecionadosComInformacaoDTO.add(pacienteSelecionado);
    }

    private boolean pacienteFoiAdicionado(PacientesComInformacaoAtendimentoDTO pacienteSelecionado) {
        for (PacientesComInformacaoAtendimentoDTO paciente : listaPacientesSelecionadosComInformacaoDTO) {
            if (paciente.getPaciente().getId_paciente().equals(pacienteSelecionado.getPaciente().getId_paciente())) {
                JSFUtil.adicionarMensagemAdvertencia("Este paciente já foi adicionado", "");
                return true;
            }
        }
        return false;
    }

    public void removerPacienteSelecionado(PacientesComInformacaoAtendimentoDTO pacienteSelecionado)
            throws ProjetoException, SQLException {

        int tamanhoLista = listaPacientesSelecionadosComInformacaoDTO.size();

        for (int i = 0; i < tamanhoLista; i++) {
            if (listaPacientesSelecionadosComInformacaoDTO.get(i).getPaciente().getId_paciente()
                    .equals(pacienteSelecionado.getPaciente().getId_paciente())) {
                this.listaPacientesSelecionadosComInformacaoDTO.remove(i);
                this.listaLiberacoes.remove(MotivoLiberacao.DUPLICIDADE_ESPECIALIDADE.getTitulo());
                break;
            }
        }

        if (this.listaLiberacoes.isEmpty())
            usuarioLiberacao = new FuncionarioBean();

    }

    private void insereIdCidPrimarioEmPacientesSelecionados() throws ProjetoException {
        for (int i = 0; i < listaPacientesSelecionadosComInformacaoDTO.size(); i++) {
            Integer idCid = aDao.retornaIdCidDoLaudo(this.agenda, user_session.getUnidade().getId(),
                    this.listaPacientesSelecionadosComInformacaoDTO.get(i).getPaciente().getId_paciente());
            this.listaPacientesSelecionadosComInformacaoDTO.get(i).setIdCidPrimario(idCid);
        }
    }

    public void contalistafunctarget() {
        System.out.println(this.listaFuncionariosTarget.size());
    }

    public void onTransferFuncionario(TransferEvent event) {

        StringBuilder builder = new StringBuilder();

        for (Object item : event.getItems()) {
            FuncionarioBean funcionario = (FuncionarioBean) item;
            builder.append(funcionario.getId());

            if (listaFuncionariosTarget.isEmpty()) {
                listaFuncionariosTarget.add(funcionario);
            }

            else {
                List<FuncionarioBean> listaFuncionariosTargetAux = new ArrayList<>();
                listaFuncionariosTargetAux.addAll(listaFuncionariosTarget);

                for (Integer i = 0; i < listaFuncionariosTargetAux.size(); i++) {
                    if (listaFuncionariosTarget.get(i).getId().equals(funcionario.getId())) {
                        listaFuncionariosTarget.remove(i.intValue());
                        break;
                    } else {
                        listaFuncionariosTarget.add(funcionario);
                    }
                }
            }
        }
    }

    public void consultarAgenda(String situacao) throws ProjetoException {
        SessionUtil.adicionarBuscaPtsNaSessao
                (agenda.getPrograma(), agenda.getGrupo(), agenda.getEquipe(), dataAtendimentoC, dataAtendimentoFinalC,
                        TelasBuscaSessao.CONSULTAR_AGENDAMENTO.getSigla());
        /*
         * if (this.dataAtendimentoC == null) {
         * JSFUtil.adicionarMensagemErro("Selecione uma data de atendimento!", "Erro");
         * return; }
         */
        this.listaConsulta = aDao.consultarAgenda(this.dataAtendimentoC, dataAtendimentoFinalC,
                agenda, situacao, campoBusca, tipoBusca);
    }

    public void carregarBuscaConsultaAgendamento() {
        BuscaSessaoDTO buscaSessaoDTO = (BuscaSessaoDTO) SessionUtil.resgatarDaSessao(BUSCA_SESSAO);
        if (!VerificadorUtil.verificarSeObjetoNulo(buscaSessaoDTO)) {
            if (buscaSessaoDTO.getTela().equals(TelasBuscaSessao.CONSULTAR_AGENDAMENTO.getSigla())) {
                dataAtendimentoC = buscaSessaoDTO.getPeriodoInicial();
                dataAtendimentoFinalC = buscaSessaoDTO.getPeriodoFinal();
                agenda.setPrograma(buscaSessaoDTO.getProgramaBean());
                agenda.setGrupo(buscaSessaoDTO.getGrupoBean());
                agenda.setEquipe(buscaSessaoDTO.getEquipeBean());
            }
        } else {
            dataAtendimentoC = new Date();
            dataAtendimentoFinalC = new Date();
            agenda.setPrograma(new ProgramaBean());
            agenda.setGrupo(new GrupoBean());
            agenda.setEquipe(new EquipeBean());
        }

    }

    public void resetaParametrosConsultaAgenda() {
        agenda.setPresenca("T");
    }

    public void iniciarPaginaConsultarAgendamentos() {
        carregarBuscaConsultaAgendamento();
        resetaParametrosConsultaAgenda();
    }

    // SEM USO NO MOMENTO
    public void excluirAgendamento() throws ProjetoException {
        boolean excluiu = aDao.excluirAgendamento(this.agenda);
        if (excluiu) {
            limparDados();
            JSFUtil.adicionarMensagemSucesso("Agendamento excluído com sucesso!", "Sucesso");
        } else {
            JSFUtil.adicionarMensagemErro("Ocorreu um erro durante a exclusão!", "Erro");
        }
        limparDados();
    }

    public void mudaStatusPresenca(AgendaBean agendaSelecionada) throws ProjetoException {
        if (!atendimentoFoiEvoluido(agendaSelecionada)) {
            boolean mudouStatusPresenca = aDao.mudaStatusPresenca(agendaSelecionada);
            if (mudouStatusPresenca) {
                if (agendaSelecionada.getPresenca().equals("S"))
                    rowBean.setPresenca("N");
                else if (agendaSelecionada.getPresenca().equals("N"))
                    rowBean.setPresenca("S");
                rowBean.getPresenca();
                AgendaBean linhaCapturada = rowBean;
                consultarAgenda(agenda.getPresenca());
                rowBean = linhaCapturada;
                // rowBean = new AgendaBean();
                JSFUtil.adicionarMensagemSucesso("ação concluída com sucesso!", "Sucesso");
            } else {
                JSFUtil.adicionarMensagemErro("Ocorreu um erro durante a ação!", "Erro");
            }
        }

    }

    private boolean atendimentoFoiEvoluido(AgendaBean agenda) throws ProjetoException {
        if (aDao.verificarSeAtendimentoFoiEvoluido(agenda.getIdAgenda())) {
            JSFUtil.adicionarMensagemAdvertencia("Não é possível alterar a presença de um atendimento evoluído", "Atenção");
            return true;
        }
        return false;
    }

    public void limparNaBuscaPaciente() {
        this.agenda.setTipoAt(null);
        this.agenda.setProfissional(new FuncionarioBean());
        this.agenda.setEquipe(new EquipeBean());
        this.agenda.setObservacao(new String());
        this.agenda.setDataAtendimento(null);
        this.agenda.setQtd(null);
        this.agenda.setMax(null);
    }

    public void limparNaBuscaPrograma() {
        this.agenda.setGrupo(new GrupoBean());
        this.agenda.setTipoAt(new TipoAtendimentoBean());
        this.agenda.setProfissional(new FuncionarioBean());
        this.agenda.setEquipe(new EquipeBean());
        this.agenda.setObservacao(new String());
        this.agenda.setDataAtendimento(null);
        this.agenda.setDataAtendimentoFinal(null);
        this.agenda.setQtd(null);
        this.agenda.setMax(null);
        this.agenda.getTipoAt().setEquipe(false);
        this.agenda.getTipoAt().setProfissional(false);
    }

    public void limparNaBuscaAvaliacao() {
        this.agenda.setPrograma(new ProgramaBean());
        this.agenda.setGrupo(new GrupoBean());
        this.agenda.setTipoAt(new TipoAtendimentoBean());
        this.agenda.setProfissional(new FuncionarioBean());
        this.agenda.setEquipe(new EquipeBean());
        this.agenda.setObservacao(new String());
        this.agenda.setDataAtendimento(null);
        this.agenda.setDataAtendimentoFinal(null);
        this.agenda.setQtd(null);
        this.agenda.setMax(null);
        this.agenda.getTipoAt().setEquipe(false);
        this.agenda.getTipoAt().setProfissional(false);
    }

    public void limparNaBuscaGrupo() {
        this.agenda.setTipoAt(null);
        this.agenda.setProfissional(new FuncionarioBean());
        this.agenda.setEquipe(new EquipeBean());
        this.agenda.setObservacao(new String());
        this.agenda.setDataAtendimento(null);
        this.agenda.setQtd(null);
        this.agenda.setMax(null);
    }

    public void limparNaBuscaTipo() {
        this.agenda.setProfissional(new FuncionarioBean());
        this.agenda.setEquipe(new EquipeBean());
        this.agenda.setObservacao(new String());
        this.agenda.setDataAtendimento(null);
        this.agenda.setQtd(null);
        this.agenda.setMax(null);
    }

    public void limparNaBuscaEquipeProf() throws ProjetoException {
        this.agenda.setObservacao(new String());
        // this.agenda.setDataAtendimento(null);
        this.agenda.setQtd(null);
        this.agenda.setMax(null);
        carregaListaProfissionalPorGrupo();
    }

    public void selectPrograma(SelectEvent event) throws ProjetoException {
        this.programaSelecionado = (ProgramaBean) event.getObject();
        atualizaListaGrupos(programaSelecionado);
        limparNaBuscaPrograma();
    }

    public void atualizaListaGrupos(ProgramaBean p) throws ProjetoException {
        this.programaSelecionado = p;
        this.listaGruposProgramas = gDao.listarGruposPorPrograma(p.getIdPrograma());

        listaTipos = new ArrayList<>();

    }

    public void atualizaListaTipos(GrupoBean g) throws ProjetoException {
        this.grupoSelecionado = g;
        this.listaTipos = tDao.listarTipoAtPorGrupo(g.getIdGrupo(), TipoAtendimento.TODOS.getSigla());
    }

    // LISTAS E AUTOCOMPLETES INICIO
    public List<GrupoBean> listaGrupoAutoComplete(String query) throws ProjetoException {

        if (agenda.getPrograma().getIdPrograma() != null) {
            return gDao.listarGruposNoAutoComplete(query, this.agenda.getPrograma().getIdPrograma());
        } else {
            return null;
        }

    }

    public void carregaListaGruposPorPrograma() throws ProjetoException {
        if (agenda.getPrograma() != null) {
            if (agenda.getPrograma().getIdPrograma() != null) {
                listaGruposProgramas = gDao.listarGruposPorPrograma(agenda.getPrograma().getIdPrograma());
            }
        }

    }

    public List<EquipeBean> listaEquipeAutoComplete(String query) throws ProjetoException {
        List<EquipeBean> result = eDao.listarEquipePorGrupoAutoComplete(query, agenda.getGrupo().getIdGrupo());
        return result;
    }

    public List<EquipeBean> listaEquipeAvaliacaoAutoComplete(String query) throws ProjetoException {
        List<EquipeBean> result = eDao.listarEquipeAvaliacaoPorProgramaAutoComplete(query,
                agenda.getProgramaAvaliacao().getIdPrograma());
        return result;
    }

    public void carregaListaEquipe() throws ProjetoException {
        if (agenda.getGrupo().getIdGrupo() != null) {
            listaEquipePorTipoAtendimento = eDao.listarEquipePorGrupo(agenda.getGrupo().getIdGrupo());
        }
    }

    public void carregaListaEquipePorTipoAtendimento() throws ProjetoException {
        if (agenda.getTipoAt() != null) {
            if (agenda.getGrupo().getIdGrupo() != null) {
                listaEquipePorTipoAtendimento = eDao.listarEquipePorGrupo(agenda.getGrupo().getIdGrupo());
            }
        }
    }

    public void carregaListaEquipeAvaliacao() throws ProjetoException {
        if (agenda.getTipoAt() != null) {
            if (agenda.getProgramaAvaliacao().getIdPrograma() != null) {
                listaEquipePorTipoAtendimento = eDao
                        .listarEquipeAvaliacaoPorPrograma(agenda.getProgramaAvaliacao().getIdPrograma());
            }
        }
    }

    public List<FuncionarioBean> listaProfissionalPorGrupoAutoComplete(String query) throws ProjetoException {

        List<FuncionarioBean> result = fDao.listarProfissionalBuscaPorGrupo(query, agenda.getGrupo().getIdGrupo());
        return result;
    }

    public void carregaListaProfissionalPorGrupo() throws ProjetoException {
        listaProfissionalPorGrupo = new ArrayList<FuncionarioBean>();
        if (agenda.getGrupo() != null) {
            if (agenda.getGrupo().getIdGrupo() != null) {
                listaProfissionalPorGrupo = fDao.listarProfissionalPorGrupo(agenda.getGrupo().getIdGrupo());
            }
        }
    }

    public List<TipoAtendimentoBean> listaTipoAtAutoComplete(String query) throws ProjetoException {
        List<TipoAtendimentoBean> lista = new ArrayList<>();

        if (agenda.getGrupo() != null) {
            lista = tDao.listarTipoAtAutoComplete(query, this.agenda.getGrupo(), TipoAtendimento.TODOS.getSigla());
        } else
            return null;
        return lista;
    }

    public void listaDiasDeAtendimentoAtuais() throws ProjetoException {
        if (agenda.getTipoAt() != null) {

            if (agenda.getTipoAt().getIdTipo() != null) {
                // if (agenda.getProfissional().getId() != null) {
                listaConfigAgendaGeral = aDao.retornarDiaAtendimentoGeral(agenda);
                listaConfigAgendaMesAtual = aDao.retornarDiaAtendimentoMesAtual(agenda);
                // } else {
                // if (agenda.getEquipe().getCodEquipe() != null) {
//                        listaConfigAgendaGeral = aDao.retornarDiaAtendimentoEquipe(agenda.getEquipe().getCodEquipe());
                // }
                // }
            }
        }
    }

    public List<ProgramaBean> listaProgramaAutoCompleteUsuarioOutraUnidade(String query) throws ProjetoException {
        ProgramaDAO pDao = new ProgramaDAO();
        List<ProgramaBean> result = pDao.listarProgramasBuscaUsuarioOutraUnidade(query, agenda.getUnidade().getId());
        return result;
    }

    // LISTAS E AUTOCOMPLETES FINAL

    public void selectGrupo(SelectEvent event) throws ProjetoException {
        this.grupoSelecionado = (GrupoBean) event.getObject();
        atualizaListaTipos(grupoSelecionado);
        atualizaListaProfPorGrupo();
        limparNaBuscaGrupo();
    }

    public void atualizaListaProfPorGrupo() throws ProjetoException {
        this.listaProfissional = fDao.listarProfissionalPorGrupo(this.grupoSelecionado.getIdGrupo());
    }

    public void verificarSeAtendimentoFoiRealizado() throws ProjetoException{
        Boolean atendimentoRealizado = aDao.verificarSeAtendimentoFoiRealizado(rowBean.getIdAgenda());

        if(atendimentoRealizado){
            JSFUtil.adicionarMensagemAdvertencia("Não é possível cancelar o agendamento pois existe atendimento informado!", "Aviso");
            return;
        }
        else{
            JSFUtil.abrirDialog("dialogCancelamento");
        }
    }

    public void cancelarAgendamento() throws ProjetoException{
        Boolean cancelado = aDao.cancelarAgendamento(rowBean.getIdAgenda());

        if(cancelado){
            JSFUtil.adicionarMensagemSucesso("Cancelamento realizado com sucesso!", "Sucesso");
            consultarAgenda("T");
        }
        else{
            JSFUtil.adicionarMensagemErro("Erro ao realizar cancelamento!", "Erro");
        }

        rowBean = null;
        rowBean = new AgendaBean();
        JSFUtil.fecharDialog("dialogCancelamento");
    }

    private boolean camposValidosParaValidarEspecialidadePaciente(PacienteBean paciente, Date dataAtende) {
        boolean valido = true;
        valido = validaPaciente(paciente, valido);
        valido = validaData(dataAtende, valido);

        return valido;
    }

    private boolean camposValidosParaValidarEspecialidadeProfissional(FuncionarioBean funcionario, Date dataAtende) {
        boolean valido = true;
        valido = validaProfissional(funcionario, valido);
        valido = validaData(dataAtende, valido);

        return valido;
    }

    private boolean validaPaciente(PacienteBean paciente, boolean valido) {
        if(VerificadorUtil.verificarSeObjetoNulo(paciente)
                || VerificadorUtil.verificarSeObjetoNuloOuZero(paciente.getId_paciente())) {
            JSFUtil.adicionarMensagemErro("Adicione um paciente", "Erro");
            valido = false;
        }
        return valido;
    }

    private boolean validaProfissional(FuncionarioBean funcionario, boolean valido) {
        if(VerificadorUtil.verificarSeObjetoNulo(funcionario)
                || VerificadorUtil.verificarSeObjetoNuloOuZero(funcionario.getId())) {
            JSFUtil.adicionarMensagemErro("Adicione um profissional", "Erro");
            valido = false;
        }
        return valido;
    }

    private boolean validaData(Date dataAtende, boolean valido) {
        if(VerificadorUtil.verificarSeObjetoNulo(dataAtende)) {
            JSFUtil.adicionarMensagemErro("Adicione a data", "Erro");
            valido = false;
        }
        return valido;
    }

    private boolean existeEspecialidaAgendaAvulsaNaInsercaoFuncionario(AgendaBean agenda, FuncionarioBean funcionario)
            throws ProjetoException, SQLException {

        this.motivoLiberacao = "";
        if(aDao.verificaExisteEspecialidadeNestaData
                (agenda.getPaciente().getId_paciente(), agenda.getDataAtendimento(), funcionario.getEspecialidade().getCodEspecialidade())) {
            mensagemDialogLiberacao = "Duplicidade De Agenda Avulsa: Não é possível adicionar o profissional "+funcionario.getNome()+
                    ".\n Já existe um agendamento nessa data na mesma especialidade para o paciente";
            this.motivoLiberacao = MotivoLiberacao.DUPLICIDADE_ESPECIALIDADE.getSigla();
        }

        else {
            for (FuncionarioBean funcionarioAdicionado : this.listaFuncionariosTarget) {
                if(funcionarioAdicionado.getEspecialidade().getCodEspecialidade().
                        equals(funcionario.getEspecialidade().getCodEspecialidade())) {
                    mensagemDialogLiberacao = "Duplicidade De Agenda Avulsa: Não é possível adicionar o profissional "+funcionario.getNome()+
                            "\n Já existe um agendamento nessa data na mesma especialidade para o paciente";
                    this.motivoLiberacao = MotivoLiberacao.DUPLICIDADE_ESPECIALIDADE.getSigla();
                }
            }
        }

        if(this.motivoLiberacao.equals(MotivoLiberacao.DUPLICIDADE_ESPECIALIDADE.getSigla())) {
            funcionarioSelecionado = funcionario;
            limpaDadosDialogLiberacao();
            JSFUtil.abrirDialog(DIALOG_LIBERACAO);
            return true;
        }
        return false;
    }

    private boolean existeEspecialidaAgendaAvulsaNaInsercaoPaciente(AgendaBean agenda, PacientesComInformacaoAtendimentoDTO paciente)
            throws ProjetoException, SQLException {
        this.motivoLiberacao = "";

        if(aDao.verificaExisteEspecialidadeNestaData
                (paciente.getPaciente().getId_paciente(), agenda.getDataAtendimento(), agenda.getProfissional().getEspecialidade().getCodEspecialidade())) {
            mensagemDialogLiberacao = "Duplicidade De Agenda Avulsa: Não é possível adicionar o paciente "+paciente.getPaciente().getNome()+
                    ".\n Já existe um agendamento nessa data na mesma especialidade para este paciente. Para continuar, digite seu CPF e senha";
            this.motivoLiberacao = MotivoLiberacao.DUPLICIDADE_ESPECIALIDADE.getSigla();
        }

        if(this.motivoLiberacao.equals(MotivoLiberacao.DUPLICIDADE_ESPECIALIDADE.getSigla())) {
            pacienteSelecionado = paciente;
            pacienteSelecionado.setDuplicidadeEspecialidade(true);
            limpaDadosDialogLiberacao();
            JSFUtil.abrirDialog(DIALOG_LIBERACAO);
            return true;
        }
        return false;
    }

    //METODOS CRIADOS PARA SUPLIR O NÃO PROCESSAMENTO DA VARIAVEL NO BOTÃO DE ADICIONAR PACIENTE/PROFISSIONAL DAS AGENDAS AVULSAS
    public void setaDataAtendeParaValidacaoPaciente(Date data) throws ProjetoException {
        this.dataAtendimentoC = data;
        carregaListaFuncionariosDual();
    }

    public void setaDataAtendeParaValidacaoProfissional(Date data) throws ProjetoException {
        this.dataAtendimentoC = data;
        listarPacientes();
    }

    public void limparProcedimentos() {
        this.agenda.setProcedimento(new ProcedimentoBean());
        this.agenda.setListaProcedimentosSecundarios(new ArrayList<>());
    }

    public void configuraDialogProcedimentoPrincipal() {
        this.tipoProcedimento = TipoProcedimentoAgenda.PROCEDIMENTO_PRINCIPAL.getSigla();
        JSFUtil.abrirDialog("dlgConsulProc");
    }

    public void configuraDialogProcedimentoSecundario() {
        this.tipoProcedimento = TipoProcedimentoAgenda.PROCEDIMENTO_SECUNDARIO.getSigla();
        JSFUtil.abrirDialog("dlgConsulProc");
    }

    public void selecionarProcedimentoPrincipal(ProcedimentoBean procedimentoSelecionado) {
        if(!procedimentoSelecionadoEhIgualAlgumSecundario(procedimentoSelecionado)) {
            this.agenda.setProcedimento(procedimentoSelecionado);
            JSFUtil.fecharDialog("dlgConsulProc");
        }
    }

    private boolean procedimentoSelecionadoEhIgualAlgumSecundario(ProcedimentoBean procedimentoSelecionado) {
        for (ProcedimentoBean procedimento : this.agenda.getListaProcedimentosSecundarios()) {
            if(procedimentoSelecionado.getIdProc().equals(procedimento.getIdProc())) {
                JSFUtil.adicionarMensagemErro("Procedimento já foi adicionado como um secundário", "");
                return true;
            }
        }
        return false;
    }

    public void adicionarProcedimentoSecundario(ProcedimentoBean procedimentoSelecionado) {
        if ((!this.agenda.getPrograma().isPermiteDuplicarProcedimentoAtendimento()) ) {
            if (procedimentoSelecionadoEhIgualPrincipal(procedimentoSelecionado))
            return;
        }
        if(this.agenda.getListaProcedimentosSecundarios().isEmpty()) {
            this.agenda.getListaProcedimentosSecundarios().add(procedimentoSelecionado);
            JSFUtil.fecharDialog("dlgConsulProc");
        }
        else
            if ((this.agenda.getPrograma().isPermiteDuplicarProcedimentoAtendimento()) || ((!this.agenda.getPrograma().isPermiteDuplicarProcedimentoAtendimento()) && (!procedimentoSelecionadoEhIgualAlgumSecundario(procedimentoSelecionado)))) {
            this.agenda.getListaProcedimentosSecundarios().add(procedimentoSelecionado);
            JSFUtil.fecharDialog("dlgConsulProc");
        }
    }

    private boolean procedimentoSelecionadoEhIgualPrincipal(ProcedimentoBean procedimentoSelecionado) {
        if(procedimentoSelecionado.getIdProc().equals(this.agenda.getProcedimento().getIdProc())) {
            JSFUtil.adicionarMensagemErro("Procedimento já foi adicionado como o principal", "");
            return true;
        }
        return false;
    }

    public void removerProcedimentoSecundario(ProcedimentoBean procedimentoSelecionado) {
        List<ProcedimentoBean> listaAux = new ArrayList<>();
        listaAux.addAll(this.agenda.getListaProcedimentosSecundarios());

        for (ProcedimentoBean procedimento : listaAux) {
            if(procedimentoSelecionado.getIdProc().equals(procedimento.getIdProc()))
                this.agenda.getListaProcedimentosSecundarios().remove(procedimento);
        }
    }

    private void verificarUnidadeEstaConfiguradaParaValidarDadosDoSigtap() throws ProjetoException {
        this.unidadeValidaDadosSigtap = unidadeDAO.verificarUnidadeEstaConfiguradaParaValidarDadosDoSigtap();
    }

    private boolean cidValido(CidBean cid) throws ProjetoException {

        LaudoDAO laudoDAO = new LaudoDAO();

        if(!unidadeValidaDadosSigtap || !cidObrigatorio)
            return true;

        else if(cidObrigatorio && (VerificadorUtil.verificarSeObjetoNulo(agenda.getCid())
                || VerificadorUtil.verificarSeObjetoNuloOuMenorQueZero(agenda.getCid().getIdCid())) ) {
            JSFUtil.adicionarMensagemErro("CID: Campo Obrigatório ", "");
            return false;
        }

        else {
            List<ProcedimentoBean> listaProcedimentosCompativeisPrograma
                    = new ProgramaDAO().listarProcedimentosPermitidosIhPadrao(agenda.getPrograma().getIdPrograma());

            Date dataAtendimento = agenda.getDataAtendimento();
            if (!existeCargaSigtapParaDataAgenda(dataAtendimento)) {
                dataAtendimento = DataUtil.retornaDataComMesAnterior(dataAtendimento);
            }

            for (ProcedimentoBean procedimento : listaProcedimentosCompativeisPrograma) {
                boolean cidValido =
                        laudoDAO.validaCodigoCidEmLaudo(cid.getCid(), dataAtendimento, procedimento.getCodProc());
                if(cidValido)
                    return true;
            }
            JSFUtil.adicionarMensagemAdvertencia("CID inválido com procedimentos do programa", "");
        }
        return false;
    }

    public boolean existeCargaSigtapParaDataAgenda(Date dataAgenda) {
        boolean existeCargaSigtapParaDataSolicitacao = true;
        if(this.unidadeValidaDadosSigtap) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dataAgenda);
            int mesAtendimento = calendar.get(Calendar.MONTH);
            mesAtendimento++;
            int anoAtendimento = calendar.get(Calendar.YEAR);
            existeCargaSigtapParaDataSolicitacao
                    = new ProcedimentoDAO().verificaExisteCargaSigtapParaData(mesAtendimento, anoAtendimento);
        }
        return existeCargaSigtapParaDataSolicitacao;
    }

    public List<CidBean> listaCidAutoComplete(String query) throws ProjetoException {
        return new CidDAO().listarCidsAutoComplete(query);
    }

    public void listarCids(String campoBusca) throws ProjetoException {
        listaCids = new CidDAO().listarCidsBusca(campoBusca);
    }

    public ProgramaBean getProgramaSelecionado() {
        return programaSelecionado;
    }

    public void setProgramaSelecionado(ProgramaBean programaSelecionado) {
        this.programaSelecionado = programaSelecionado;
    }

    public List<GrupoBean> getListaGruposProgramas() {
        return listaGruposProgramas;
    }

    public void setListaGruposProgramas(List<GrupoBean> listaGruposProgramas) {
        this.listaGruposProgramas = listaGruposProgramas;
    }

    public GrupoBean getGrupoSelecionado() {
        return grupoSelecionado;
    }

    public void setGrupoSelecionado(GrupoBean grupoSelecionado) {
        this.grupoSelecionado = grupoSelecionado;
    }

    public List<FuncionarioBean> getListaProfissional() {
        return listaProfissional;
    }

    public void setListaProfissional(List<FuncionarioBean> listaProfissional) {
        this.listaProfissional = listaProfissional;
    }

    public List<TipoAtendimentoBean> getListaTipos() {
        return listaTipos;
    }

    public void setListaTipos(List<TipoAtendimentoBean> listaTipos) {
        this.listaTipos = listaTipos;
    }

    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public List<AgendaBean> getListaNovosAgendamentos() {
        return listaNovosAgendamentos;
    }

    public void setListaNovosAgendamentos(List<AgendaBean> listaNovosAgendamentos) {
        this.listaNovosAgendamentos = listaNovosAgendamentos;
    }

    public AgendaBean getAgenda() {
        return agenda;
    }

    public void setAgenda(AgendaBean agenda) {
        this.agenda = agenda;
    }

    public List<AgendaBean> getListaAgendamentosData() {
        return listaAgendamentosData;
    }

    public void setListaAgendamentosData(List<AgendaBean> listaAgendamentosData) {
        this.listaAgendamentosData = listaAgendamentosData;
    }

    public Date getDataAtendimentoC() {
        return dataAtendimentoC;
    }

    public void setDataAtendimentoC(Date dataAtendimentoC) {
        this.dataAtendimentoC = dataAtendimentoC;
    }

    public String getCnsC() {
        return cnsC;
    }

    public void setCnsC(String cnsC) {
        this.cnsC = cnsC;
    }

    public Integer getProtuarioC() {
        return protuarioC;
    }

    public void setProtuarioC(Integer protuarioC) {
        this.protuarioC = protuarioC;
    }

    public TipoAtendimentoBean getTipoC() {
        return tipoC;
    }

    public void setTipoC(TipoAtendimentoBean tipoC) {
        this.tipoC = tipoC;
    }

    public List<AgendaBean> getListaConsulta() {
        return listaConsulta;
    }

    public void setListaConsulta(List<AgendaBean> listaConsulta) {
        this.listaConsulta = listaConsulta;
    }

    public boolean isHabilitarDetalhes() {
        return habilitarDetalhes;
    }

    public void setHabilitarDetalhes(boolean habilitarDetalhes) {
        this.habilitarDetalhes = habilitarDetalhes;
    }

    public String getTipoData() {
        return tipoData;
    }

    public void setTipoData(String tipoData) {
        this.tipoData = tipoData;
    }

    public List<AgendaBean> getListaHorariosOcupados() {
        return listaHorariosOcupados;
    }

    public void setListaHorariosOcupados(List<AgendaBean> listaHorariosOcupados) {
        this.listaHorariosOcupados = listaHorariosOcupados;
    }

    public Boolean getTemLotado() {
        return temLotado;
    }

    public void setTemLotado(Boolean temLotado) {
        this.temLotado = temLotado;
    }

    public Date getDataAtendimentoFinalC() {
        return dataAtendimentoFinalC;
    }

    public void setDataAtendimentoFinalC(Date dataAtendimentoFinalC) {
        this.dataAtendimentoFinalC = dataAtendimentoFinalC;
    }

    public Boolean getAgendamentosConfirmados() {
        return agendamentosConfirmados;
    }

    public void setAgendamentosConfirmados(Boolean agendamentosConfirmados) {
        this.agendamentosConfirmados = agendamentosConfirmados;
    }

    public String getDataAtual() {
        return dataAtual;
    }

    public void setDataAtual(String dataAtual) {
        this.dataAtual = dataAtual;
    }

    public List<Date> getListaNaoPermitidosIntervaloDeDatas() {
        return listaNaoPermitidosIntervaloDeDatas;
    }

    public void setListaNaoPermitidosIntervaloDeDatas(List<Date> listaNaoPermitidosIntervaloDeDatas) {
        this.listaNaoPermitidosIntervaloDeDatas = listaNaoPermitidosIntervaloDeDatas;
    }

    public FuncionarioBean getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(FuncionarioBean funcionario) {
        this.funcionario = funcionario;
    }

    public List<EquipeBean> getListaEquipePorTipoAtendimento() {
        return listaEquipePorTipoAtendimento;
    }

    public void setListaEquipePorTipoAtendimento(List<EquipeBean> listaEquipePorTipoAtendimento) {
        this.listaEquipePorTipoAtendimento = listaEquipePorTipoAtendimento;
    }

    public TipoAtendimentoBean getTipoAtendimentoSelecionado() {
        return tipoAtendimentoSelecionado;
    }

    public void setTipoAtendimentoSelecionado(TipoAtendimentoBean tipoAtendimentoSelecionado) {
        this.tipoAtendimentoSelecionado = tipoAtendimentoSelecionado;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public List<GrupoBean> getListaDeGruposFiltrada() {
        return listaDeGruposFiltrada;
    }

    public void setListaDeGruposFiltrada(List<GrupoBean> listaDeGruposFiltrada) {
        this.listaDeGruposFiltrada = listaDeGruposFiltrada;
    }

    public List<FuncionarioBean> getListaProfissionalPorGrupo() {
        return listaProfissionalPorGrupo;
    }

    public void setListaProfissionalPorGrupo(List<FuncionarioBean> listaProfissionalPorGrupo) {
        this.listaProfissionalPorGrupo = listaProfissionalPorGrupo;
    }

    public String getOpcaoAtendimento() {
        return opcaoAtendimento;
    }

    public void setOpcaoAtendimento(String opcaoAtendimento) {
        this.opcaoAtendimento = opcaoAtendimento;
    }

    public ArrayList<String> getListaHorarios() {
        return listaHorarios;
    }

    public void setListaHorarios(ArrayList<String> listaHorarios) {
        this.listaHorarios = listaHorarios;
    }

    public List<ConfigAgendaParte1Bean> getListaConfigAgendaGeral() {
        return listaConfigAgendaGeral;
    }

    public List<ConfigAgendaParte1Bean> getListaConfigAgendaMesAtual() {
        return listaConfigAgendaMesAtual;
    }

    public void setListaConfigAgendaGeral(List<ConfigAgendaParte1Bean> listaConfigAgendaGeral) {
        this.listaConfigAgendaGeral = listaConfigAgendaGeral;
    }

    public void setListaConfigAgendaMesAtual(List<ConfigAgendaParte1Bean> listaConfigAgendaMesAtual) {
        this.listaConfigAgendaMesAtual = listaConfigAgendaMesAtual;
    }

    public String getTipoBusca() {
        return tipoBusca;
    }

    public String getCampoBusca() {
        return campoBusca;
    }

    public void setTipoBusca(String tipoBusca) {
        this.tipoBusca = tipoBusca;
    }

    public void setCampoBusca(String campoBusca) {
        this.campoBusca = campoBusca;
    }

    public DualListModel<FuncionarioBean> getListaFuncionariosDual() {
        return listaFuncionariosDual;
    }

    public List<FuncionarioBean> getListaFuncionariosSoucer() {
        return listaFuncionariosSoucer;
    }

    public List<FuncionarioBean> getListaFuncionariosTarget() {
        return listaFuncionariosTarget;
    }

    public void setListaFuncionariosDual(DualListModel<FuncionarioBean> listaFuncionariosDual) {
        this.listaFuncionariosDual = listaFuncionariosDual;
    }

    public void setListaFuncionariosSoucer(List<FuncionarioBean> listaFuncionariosSoucer) {
        this.listaFuncionariosSoucer = listaFuncionariosSoucer;
    }

    public void setListaFuncionariosTarget(List<FuncionarioBean> listaFuncionariosTarget) {
        this.listaFuncionariosTarget = listaFuncionariosTarget;
    }

    public List<FuncionarioBean> getListaFuncionariosSoucerFiltro() {
        return listaFuncionariosSoucerFiltro;
    }

    public void setListaFuncionariosSoucerFiltro(List<FuncionarioBean> listaFuncionariosSoucerFiltro) {
        this.listaFuncionariosSoucerFiltro = listaFuncionariosSoucerFiltro;
    }

    public List<FuncionarioBean> getListaFuncionariosTargetFiltro() {
        return listaFuncionariosTargetFiltro;
    }

    public void setListaFuncionariosTargetFiltro(List<FuncionarioBean> listaFuncionariosTargetFiltro) {
        this.listaFuncionariosTargetFiltro = listaFuncionariosTargetFiltro;
    }

    public List<PacientesComInformacaoAtendimentoDTO> getListaPacientesComInformacaoDTO() {
        return listaPacientesComInformacaoDTO;
    }

    public void setListaPacientesComInformacaoDTO(
            List<PacientesComInformacaoAtendimentoDTO> listaPacientesComInformacaoDTO) {
        this.listaPacientesComInformacaoDTO = listaPacientesComInformacaoDTO;
    }

    public List<PacientesComInformacaoAtendimentoDTO> getListaPacientesComInformacaoDTOFiltro() {
        return listaPacientesComInformacaoDTOFiltro;
    }

    public void setListaPacientesComInformacaoDTOFiltro(
            List<PacientesComInformacaoAtendimentoDTO> listaPacientesComInformacaoDTOFiltro) {
        this.listaPacientesComInformacaoDTOFiltro = listaPacientesComInformacaoDTOFiltro;
    }

    public List<PacientesComInformacaoAtendimentoDTO> getListaPacientesSelecionadosComInformacaoDTO() {
        return listaPacientesSelecionadosComInformacaoDTO;
    }

    public void setListaPacientesSelecionadosComInformacaoDTO(
            List<PacientesComInformacaoAtendimentoDTO> listaPacientesSelecionadosComInformacaoDTO) {
        this.listaPacientesSelecionadosComInformacaoDTO = listaPacientesSelecionadosComInformacaoDTO;
    }

    public String getMensagemDialogLiberacao() {
        return mensagemDialogLiberacao;
    }

    public void setMensagemDialogLiberacao(String mensagemDialogLiberacao) {
        this.mensagemDialogLiberacao = mensagemDialogLiberacao;
    }

    public boolean isIncluirProcedimentos() {
        return incluirProcedimentos;
    }

    public void setIncluirProcedimentos(boolean incluirProcedimentos) {
        this.incluirProcedimentos = incluirProcedimentos;
    }

    public String getTipoProcedimento() {
        return tipoProcedimento;
    }

    public void setTipoProcedimento(String tipoProcedimento) {
        this.tipoProcedimento = tipoProcedimento;
    }

    public boolean isConfirmaAgendamentoComProcedimentos() {
        return confirmaAgendamentoComProcedimentos;
    }

    public void setConfirmaAgendamentoComProcedimentos(boolean confirmaAgendamentoComProcedimentos) {
        this.confirmaAgendamentoComProcedimentos = confirmaAgendamentoComProcedimentos;
    }

    public String getMotivoLiberacao() {
        return motivoLiberacao;
    }

    public void setMotivoLiberacao(String motivoLiberacao) {
        this.motivoLiberacao = motivoLiberacao;
    }

    public String getTipoConfiguracaoDialog() {
        return tipoConfiguracaoDialog;
    }

    public void setTipoConfiguracaoDialog(String tipoConfiguracaoDialog) {
        this.tipoConfiguracaoDialog = tipoConfiguracaoDialog;
    }

    public boolean isCidObrigatorio() {
        return cidObrigatorio;
    }

    public void setCidObrigatorio(boolean cidObrigatorio) {
        this.cidObrigatorio = cidObrigatorio;
    }

    public List<CidBean> getListaCids() {
        return listaCids;
    }

    public void setListaCids(List<CidBean> listaCids) {
        this.listaCids = listaCids;
    }

	public List<SituacaoAtendimentoBean> getListaSituacoes() {
		return listaSituacoes;
	}

	public void setListaSituacoes(List<SituacaoAtendimentoBean> listaSituacoes) {
		this.listaSituacoes = listaSituacoes;
	}

	public SituacaoAtendimentoBean getSituacaoAtendimentoEvolucaoFalta() {
		return situacaoAtendimentoEvolucaoFalta;
	}

	public void setSituacaoAtendimentoEvolucaoFalta(SituacaoAtendimentoBean situacaoAtendimentoEvolucaoFalta) {
		this.situacaoAtendimentoEvolucaoFalta = situacaoAtendimentoEvolucaoFalta;
	}

	public String getEvolucaoFalta() {
		return evolucaoFalta;
	}

	public void setEvolucaoFalta(String evolucaoFalta) {
		this.evolucaoFalta = evolucaoFalta;
	}
}
