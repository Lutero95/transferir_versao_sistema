package br.gov.al.maceio.sishosp.hosp.control;

import java.io.Serializable;
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

import br.gov.al.maceio.sishosp.acl.control.FuncionarioController;
import br.gov.al.maceio.sishosp.comum.shared.TelasBuscaSessao;
import br.gov.al.maceio.sishosp.hosp.model.dto.BuscaSessaoDTO;
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
import br.gov.al.maceio.sishosp.hosp.dao.BloqueioDAO;
import br.gov.al.maceio.sishosp.hosp.dao.EquipeDAO;
import br.gov.al.maceio.sishosp.hosp.dao.FeriadoDAO;
import br.gov.al.maceio.sishosp.hosp.dao.GerenciarPacienteDAO;
import br.gov.al.maceio.sishosp.hosp.dao.GrupoDAO;
import br.gov.al.maceio.sishosp.hosp.dao.ProgramaDAO;
import br.gov.al.maceio.sishosp.hosp.dao.TipoAtendimentoDAO;
import br.gov.al.maceio.sishosp.hosp.enums.OpcaoAtendimento;
import br.gov.al.maceio.sishosp.hosp.enums.TipoAtendimento;
import br.gov.al.maceio.sishosp.hosp.enums.TipoDataAgenda;
import br.gov.al.maceio.sishosp.hosp.enums.Turno;
import br.gov.al.maceio.sishosp.hosp.enums.ValidacaoSenha;
import br.gov.al.maceio.sishosp.hosp.model.AgendaBean;
import br.gov.al.maceio.sishosp.hosp.model.CidBean;
import br.gov.al.maceio.sishosp.hosp.model.ConfigAgendaParte1Bean;
import br.gov.al.maceio.sishosp.hosp.model.EquipeBean;
import br.gov.al.maceio.sishosp.hosp.model.GrupoBean;
import br.gov.al.maceio.sishosp.hosp.model.ProgramaBean;
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
    private static final Integer SEM_FUNCIONARIO_LIBERACAO = 0;
    private List<GrupoBean> listaDeGruposFiltrada;
    private ArrayList<String> listaHorarios;

    public AgendaController() {
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
        agenda.setTurno("M");
        listaNaoPermitidosIntervaloDeDatas = new ArrayList<>();
        funcionario = new FuncionarioBean();
        listaHorarios = new ArrayList<>();
        this.listaFuncionariosSoucer = new ArrayList<>();
        this.listaFuncionariosTarget = new ArrayList<>();
        this.listaFuncionariosSoucerFiltro = new ArrayList<>();
        this.listaFuncionariosTargetFiltro = new ArrayList<>();
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
        agenda.setTurno(Turno.MANHA.getSigla());
        rowBean = new AgendaBean();
        agenda.getUnidade().setId(SessionUtil.recuperarDadosSessao().getUnidade().getId());
        this.agenda.setProfissional(null);
        this.agenda.setProfissional(new FuncionarioBean());
        
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
    
    public void adicionarFuncionario(FuncionarioBean funcionarioSelecionado) {
    	if(!funcionarioExisteLista(funcionarioSelecionado)) {
    		this.listaFuncionariosTarget.add(funcionarioSelecionado);
    		this.listaFuncionariosTargetFiltro.add(funcionarioSelecionado);
    	}
    	this.listaFuncionariosSoucer.remove(funcionarioSelecionado);
    	this.listaFuncionariosSoucerFiltro.remove(funcionarioSelecionado);
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
    
    public void removerFuncionario(FuncionarioBean funcionarioSelecionado) {
    	this.listaFuncionariosTarget.remove(funcionarioSelecionado);
    	this.listaFuncionariosTargetFiltro.remove(funcionarioSelecionado);
    	this.listaFuncionariosSoucer.add(funcionarioSelecionado);
    	this.listaFuncionariosSoucerFiltro.add(funcionarioSelecionado);
    }
    
    public void removerTodosFuncionarios() {
    	this.listaFuncionariosSoucer.addAll(this.listaFuncionariosTargetFiltro);
    	this.listaFuncionariosSoucerFiltro.addAll(this.listaFuncionariosTargetFiltro);
    	this.listaFuncionariosTarget.removeAll(this.listaFuncionariosTargetFiltro);
    	this.listaFuncionariosTargetFiltro.clear();
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
            if (this.agenda.getPrograma() == null || this.agenda.getGrupo() == null || this.agenda.getPaciente() == null
                    || this.agenda.getTipoAt() == null
                    || (this.agenda.getDataAtendimento() == null || this.agenda.getDataAtendimentoFinal() == null)
                    || (this.agenda.getProfissional() == null && this.agenda.getEquipe() == null)) {
                return;
            } else {
                verificaDisponibilidadeDataUnica();
            }
        }

        gerarHorarioAtendimentoAnalisandoHorariosCheios();
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
            JSFUtil.adicionarMensagemErro("Já atingiu a quantidade máxima para essa data específica!", "Erro!");
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
        System.out.println(agenda.getProfissional().getCbo().getCodigo());
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

        Boolean pacienteAtivo = gerenciarPacienteDAO
                .verificarPacienteAtivoInstituicao(agenda.getPaciente().getId_paciente());

        if (VerificadorUtil.verificarSeObjetoNulo(agenda.getMax())
                && VerificadorUtil.verificarSeObjetoNulo(agenda.getQtd())) {
            zerarValoresAgendaMaximoIhQuantidade();
        }

        if ((agenda.getEquipe().getCodEquipe() != null) && (agenda.getAvaliacao() == false)) {

            if (pacienteAtivo) {
                gravarAgenda(SEM_FUNCIONARIO_LIBERACAO);
            } else {
                funcionario = new FuncionarioBean();
                JSFUtil.abrirDialog("dlgSenha");
            }
        } else if ((agenda.getProfissional().getId() != null) || (agenda.getAvaliacao() == true)) {
            gravarAgenda(SEM_FUNCIONARIO_LIBERACAO);
        }
    }

    public void preparaGravarAgendaAvulsa() throws ProjetoException {

    	if(pacienteEstaAtivo()) {
            FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
                    .getSessionMap().get("obj_funcionario");
            
			if (VerificadorUtil.verificarSeObjetoNulo(agenda.getMax())
					&& VerificadorUtil.verificarSeObjetoNulo(agenda.getQtd())) {
				zerarValoresAgendaMaximoIhQuantidade();
			}
			
			insereIdCidPrimarioEmAgendaAvulsa(user_session);
			
			boolean existeDuplicidadeAgendaAvulsa = existeDuplicidadeAgendaAvulsa();
			boolean permiteDuplicidade = user_session.getUnidade().getParametro().isPermiteAgendamentoDuplicidade();

			if((existeDuplicidadeAgendaAvulsa && permiteDuplicidade)
					|| !existeDuplicidadeAgendaAvulsa && !permiteDuplicidade
					|| !existeDuplicidadeAgendaAvulsa && permiteDuplicidade) {
				gravarAgendaAvulsa(new FuncionarioBean());
			}
    	}
    }

	private void insereIdCidPrimarioEmAgendaAvulsa(FuncionarioBean user_session) throws ProjetoException {
		Integer idCid = aDao.retornaIdCidDoLaudo(this.agenda, user_session.getUnidade().getId());
		this.agenda.setIdCidPrimario(idCid);;
	}
    
    private boolean pacienteEstaAtivo() throws ProjetoException {
    	GerenciarPacienteDAO gerenciarPacienteDAO = new GerenciarPacienteDAO();
    	
        Boolean pacienteAtivo = gerenciarPacienteDAO
                .verificarPacienteAtivoInstituicao(agenda.getPaciente().getId_paciente());
        
        if(!pacienteAtivo)
        	JSFUtil.adicionarMensagemErro("Paciente selecionado não está ativo", "");
        
        return pacienteAtivo;	
    }
    
    private boolean existeDuplicidadeAgendaAvulsa() throws ProjetoException {
    	Boolean existeDuplicidade = aDao.verificarDuplicidadeAgendaAvulsa(agenda);
        FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
                .getSessionMap().get("obj_funcionario");
    	if((!user_session.getUnidade().getParametro().isPermiteAgendamentoDuplicidade()) && (existeDuplicidade)) {
    		JSFUtil.abrirDialog("dlgLiberacao");
        	limpaDadosDialogLiberacao();
    	}
    	
    	return existeDuplicidade;
    }
    
    public void validarSenhaLiberacaoAgendaAvulsa() throws ProjetoException {
        FuncionarioDAO funcionarioDAO = new FuncionarioDAO();

        FuncionarioBean funcionarioLiberacao = funcionarioDAO.validarCpfIhSenha(funcionario.getCpf(), funcionario.getSenha(),
                ValidacaoSenha.LIBERACAO.getSigla());

        if (!VerificadorUtil.verificarSeObjetoNulo(funcionarioLiberacao)) {
        	gravarAgendaAvulsa(funcionarioLiberacao);
    		JSFUtil.fecharDialog("dlgLiberacao");
        } else {
            JSFUtil.adicionarMensagemErro("Funcionário com senha errada ou sem liberação!", "Erro!");
        }
    }

	private void limpaDadosDialogLiberacao() {
		this.funcionario.setCpf(new String());
		this.funcionario.setSenha(new String());
	}

    public void zerarValoresAgendaMaximoIhQuantidade() {
        agenda.setQtd(0);
        agenda.setMax(0);
    }
    
    public void validarSenhaLiberacao() throws ProjetoException {
        FuncionarioDAO funcionarioDAO = new FuncionarioDAO();

        FuncionarioBean func = funcionarioDAO.validarCpfIhSenha(funcionario.getCpf(), funcionario.getSenha(),
                ValidacaoSenha.LIBERACAO.getSigla());

        if (func != null) {
            JSFUtil.fecharDialog("dlgSenha");
            gravarAgenda(func.getId().intValue());
        } else {
            JSFUtil.adicionarMensagemErro("Funcionário com senha errada ou sem liberação!", "Erro!");
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
            JSFUtil.adicionarMensagemErro("Funcionário com senha errada ou sem liberação!", "Erro!");
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

    public void gravarAgenda(Integer funcionarioLiberacao) throws ProjetoException {
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

        boolean cadastrou = false;

        cadastrou = aDao.gravarAgenda(this.agenda, this.listaNovosAgendamentos, funcionarioLiberacao);

        if (cadastrou) {
            limparDados();
            JSFUtil.adicionarMensagemSucesso("Agenda cadastrada com sucesso!", "Sucesso");
        } else {
            JSFUtil.adicionarMensagemErro("Ocorreu um erro durante o cadastro!", "Erro");
        }
        limparDados();
    }

    private void gravarAgendaAvulsa(FuncionarioBean usuarioLiberacao) throws ProjetoException {
        // verificar se existe algum campo nao preenchido
        if (this.agenda.getPaciente() == null || this.agenda.getPrograma() == null || this.agenda.getGrupo() == null
                || (this.agenda.getTipoAt() == null)
                || this.agenda.getDataAtendimento() == null) {
            JSFUtil.adicionarMensagemErro("Campo(s) obrigatório(s) em falta!", "Erro");
            return;
        }

        if (this.listaFuncionariosTarget.size() == 0) {
            JSFUtil.adicionarMensagemErro("Informe o(s) Profissional(is) do Agendamento !", "Erro");
            return;
        }

        boolean cadastrou = false;

        cadastrou = aDao.gravarAgendaAvulsa(this.agenda, this.listaFuncionariosTarget, usuarioLiberacao);

        if (cadastrou) {
            limparDados();
            carregaListaFuncionariosDual();
            JSFUtil.adicionarMensagemSucesso("Agenda cadastrada com sucesso!", "Sucesso");
        } else {
            JSFUtil.adicionarMensagemErro("Ocorreu um erro durante o cadastro!", "Erro");
        }
        limparDados();
    }

    public void contalistafunctarget(){
       System.out.println(this.listaFuncionariosTarget.size());
    }

    public void onTransferFuncionario(TransferEvent event) {
    	
        StringBuilder builder = new StringBuilder();

        for (Object item : event.getItems()) {
        	FuncionarioBean funcionario = (FuncionarioBean) item;
            builder.append(funcionario.getId());
            
            if(listaFuncionariosTarget.isEmpty()) {
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
        SessionUtil.adicionarBuscaPtsNaSessao(null, null, dataAtendimentoC,
                dataAtendimentoFinalC, TelasBuscaSessao.CONSULTAR_AGENDAMENTO.getSigla());
        /*
         * if (this.dataAtendimentoC == null) {
         * JSFUtil.adicionarMensagemErro("Selecione uma data de atendimento!", "Erro");
         * return; }
         */
        this.listaConsulta = aDao.consultarAgenda(this.dataAtendimentoC, dataAtendimentoFinalC,
                agenda.getUnidade().getId(), situacao, campoBusca, tipoBusca);
    }

    public void carregarBuscaConsultaAgendamento() {
        BuscaSessaoDTO buscaSessaoDTO = (BuscaSessaoDTO) SessionUtil.resgatarDaSessao(BUSCA_SESSAO);
        if (!VerificadorUtil.verificarSeObjetoNulo(buscaSessaoDTO)) {
            if (buscaSessaoDTO.getTela().equals(TelasBuscaSessao.CONSULTAR_AGENDAMENTO.getSigla())) {
                dataAtendimentoC = buscaSessaoDTO.getPeriodoInicial();
                dataAtendimentoFinalC = buscaSessaoDTO.getPeriodoFinal();
            }
        }
        else
        {
        	dataAtendimentoC = new Date();
        	dataAtendimentoFinalC = new Date();
        	
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
        boolean mudouStatusPresenca = aDao.mudaStatusPresenca(agendaSelecionada);
        if (mudouStatusPresenca) {
        	 if (agendaSelecionada.getPresenca().equals("S"))
                 rowBean.setPresenca("N");
                 else
        	 if (agendaSelecionada.getPresenca().equals("N"))
                 rowBean.setPresenca("S");
        	 String presenca =""; rowBean.getPresenca();
        	 AgendaBean linhaCapturada = rowBean;
            consultarAgenda(agenda.getPresenca());
            rowBean = linhaCapturada;
            //rowBean = new AgendaBean();
            JSFUtil.adicionarMensagemSucesso("ação concluída com sucesso!", "Sucesso");
        } else {
            JSFUtil.adicionarMensagemErro("Ocorreu um erro durante a ação!", "Erro");
        }

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

    public static Integer getSemFuncionarioLiberacao() {
        return SEM_FUNCIONARIO_LIBERACAO;
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

    public List<FuncionarioBean> getListaFuncionariosSoucer() {
        return listaFuncionariosSoucer;
    }

    public List<FuncionarioBean> getListaFuncionariosTarget() {
        return listaFuncionariosTarget;
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
    
}
