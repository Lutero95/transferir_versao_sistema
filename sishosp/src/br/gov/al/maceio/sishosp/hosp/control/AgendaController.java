package br.gov.al.maceio.sishosp.hosp.control;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.event.SelectEvent;

import br.gov.al.maceio.sishosp.acl.dao.FuncionarioDAO;
import br.gov.al.maceio.sishosp.acl.model.FuncionarioBean;
import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.DataUtil;
import br.gov.al.maceio.sishosp.comum.util.JSFUtil;
import br.gov.al.maceio.sishosp.comum.util.SessionUtil;
import br.gov.al.maceio.sishosp.hosp.dao.AgendaDAO;
import br.gov.al.maceio.sishosp.hosp.dao.BloqueioDAO;
import br.gov.al.maceio.sishosp.hosp.dao.ConfigAgendaDAO;
import br.gov.al.maceio.sishosp.hosp.dao.EquipeDAO;
import br.gov.al.maceio.sishosp.hosp.dao.FeriadoDAO;
import br.gov.al.maceio.sishosp.hosp.dao.GerenciarPacienteDAO;
import br.gov.al.maceio.sishosp.hosp.dao.GrupoDAO;
import br.gov.al.maceio.sishosp.hosp.dao.ProgramaDAO;
import br.gov.al.maceio.sishosp.hosp.dao.TipoAtendimentoDAO;
import br.gov.al.maceio.sishosp.hosp.enums.TipoDataAgenda;
import br.gov.al.maceio.sishosp.hosp.enums.Turno;
import br.gov.al.maceio.sishosp.hosp.enums.ValidacaoSenhaAgenda;
import br.gov.al.maceio.sishosp.hosp.model.AgendaBean;
import br.gov.al.maceio.sishosp.hosp.model.ConfigAgendaParte1Bean;
import br.gov.al.maceio.sishosp.hosp.model.EquipeBean;
import br.gov.al.maceio.sishosp.hosp.model.GrupoBean;
import br.gov.al.maceio.sishosp.hosp.model.ProgramaBean;
import br.gov.al.maceio.sishosp.hosp.model.TipoAtendimentoBean;

@ManagedBean(name = "AgendaController")
@ViewScoped
public class AgendaController implements Serializable {

    private static final long serialVersionUID = 1L;
    List<ConfigAgendaParte1Bean> listaConfigAgenda = new ArrayList<>();
    private AgendaBean agenda;
    private Date dataAtendimentoC;
    private Date dataAtendimentoFinalC;
    private String cnsC;
    private Integer protuarioC;
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


    public AgendaController() {
    	System.out.println("construtor AgendaController");
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
        agenda.getEmpresa().setCodEmpresa(SessionUtil.recuperarDadosSessao().getEmpresa().getCodEmpresa());
        tipoAtendimentoSelecionado = new TipoAtendimentoBean();
        agenda.setTurno("M");
        listaNaoPermitidosIntervaloDeDatas = new ArrayList<>();
        funcionario = new FuncionarioBean();

    }

    public void limparDados() {
    	System.out.println("limparDados");
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

        agenda.getEmpresa().setCodEmpresa(SessionUtil.recuperarDadosSessao().getEmpresa().getCodEmpresa());
        this.agenda.setProfissional(null);
        this.agenda.setProfissional(new FuncionarioBean());
    }

    public void preparaVerificarDisponibilidadeDataECarregarDiasAtendimento() throws ProjetoException {
    	preparaVerificarDisponibilidadeData();
    	listaDiasDeAtendimentoAtuais();
    }
    
    
    public void preparaVerificarDisponibilidadeData() throws ProjetoException {
    	System.out.println("preparaVerificarDisponibilidadeData");
        if (tipoData.equals(TipoDataAgenda.DATA_UNICA.getSigla())) {
            if (this.agenda.getPrograma().getIdPrograma() == null
                    || this.agenda.getPaciente().getId_paciente() == null
                    || this.agenda.getGrupo().getIdGrupo() == null
                    || this.agenda.getTipoAt().getIdTipo() == null
                    || this.agenda.getDataAtendimento() == null
                    || (this.agenda.getProfissional().getId() == null && this.agenda.getEquipe().getCodEquipe() == null)) {
                return;
            } else {
                verificaDisponibilidadeDataUnica();
            }

        } else if (tipoData.equals(TipoDataAgenda.INTERVALO_DE_DATAS.getSigla())) {
            if (this.agenda.getPrograma() == null
                    || this.agenda.getGrupo() == null
                    || this.agenda.getPaciente() == null
                    || this.agenda.getTipoAt() == null
                    || (this.agenda.getDataAtendimento() == null || this.agenda.getDataAtendimentoFinal() == null)
                    || (this.agenda.getProfissional() == null && this.agenda
                    .getEquipe() == null)) {
                return;
            } else {
                verificaDisponibilidadeDataUnica();
            }
        }
    }

    public void setarQuantidadeIhMaximoComoNulos() {
    	System.out.println("setarQuantidadeIhMaximoComoNulos");
        agenda.setQtd(null);
        agenda.setMax(null);
    }

    public void verificaDisponibilidadeDataUnica() throws ProjetoException {
    	System.out.println("verificaDisponibilidadeDataUnica");

        if (verificarSeEhFeriadoDataUnica()) {
            JSFUtil.adicionarMensagemErro("Data com feriado, não é permitido fazer agendamento!", "Erro");
            setarQuantidadeIhMaximoComoNulos();

        } else if (agenda.getProfissional().getId() != null && verificarSeTemBloqueioDataUnica()) {
            JSFUtil.adicionarMensagemErro("Data com bloqueio, não é permitido fazer agendamento!", "Erro");
            setarQuantidadeIhMaximoComoNulos();

        } else if (verificarTipoDeAtendimentoDataUnica() && !agenda.getEncaixe()) {
            JSFUtil.adicionarMensagemErro("Atingiu o limite máximo para esse tipo de atendimento e profissional!", "Erro");
            setarQuantidadeIhMaximoComoNulos();
        } else {
            verAgenda();
        }

    }

    public Boolean verificarTipoDeAtendimentoDataUnica() throws ProjetoException {
    	System.out.println("verificarTipoDeAtendimentoDataUnica");
        Boolean retorno = false;

        if (agenda.getProfissional().getId() != null) {
            if (verificarSeExisteTipoAtendimentoEspecificoDataUnica()) {

                if (verificarSeAtingiuLimitePorTipoDeAtendimento()) {
                    retorno = true;
                }
            }
        }

        return retorno;
    }

    public void verificarDisponibilidadeDataEspecifica(Integer quantidade, Integer maxima) throws ProjetoException {
System.out.println("verificarDisponibilidadeDataEspecifica");
        if (quantidade >= maxima) {
            JSFUtil.adicionarMensagemErro("Já atingiu a quantidade máxima para essa data específica!", "Erro!");
        }

    }

    public Boolean verificarSeEhFeriadoDataUnica() throws ProjetoException {
    	System.out.println("verificarSeEhFeriadoDataUnica");
        Boolean retorno = false;

        retorno = new FeriadoDAO().verificarSeEhFeriadoDataUnica(this.agenda.getDataAtendimento());

        return retorno;
    }

    public Boolean verificarSeTemBloqueioDataUnica() throws ProjetoException {
    	System.out.println("verificarSeTemBloqueioDataUnica");
        Boolean retorno = false;

        retorno = new BloqueioDAO().verificarBloqueioProfissionalDataUnica(this.agenda.getProfissional().getId(), this.agenda.getDataAtendimento(),
                this.agenda.getTurno());

        return retorno;
    }

    public Boolean verificarSeExisteTipoAtendimentoEspecificoDataUnica() throws ProjetoException {
    	System.out.println("verificarSeExisteTipoAtendimentoEspecificoDataUnica");
        Boolean retorno = false;

        retorno = new ConfigAgendaDAO().verificarSeExisteTipoAtendimentoEspecificoDataUnica(this.agenda.getProfissional().getId(), this.agenda.getDataAtendimento(),
                this.agenda.getTurno());

        return retorno;
    }

    public Boolean verificarSeAtingiuLimitePorTipoDeAtendimento() throws ProjetoException {
    	System.out.println("verificarSeAtingiuLimitePorTipoDeAtendimento");

        Boolean retorno = false;

        Integer limite = aDao.contarAtendimentosPorTipoAtendimentoPorProfissionalDataUnica(this.agenda, this.agenda.getDataAtendimento());

        Integer maximo = 0;

        maximo = aDao.verQtdMaxAgendaEspecDataEspecifica(this.agenda);

        if (maximo == 0) {
            aDao.verQtdMaxAgendaEspec(this.agenda);
        }

        if (limite >= maximo && limite > 0) {
            retorno = true;
        }

        return retorno;
    }

    public Boolean verificarSeExisteTipoAtendimentoEspecificoIntervaloDeDatas(Date data) throws ProjetoException {
    	System.out.println("verificarSeExisteTipoAtendimentoEspecificoIntervaloDeDatas");
        Boolean retorno = false;

        retorno = new ConfigAgendaDAO().verificarSeExisteTipoAtendimentoEspecificoDataUnica(this.agenda.getProfissional().getId(), data,
                this.agenda.getTurno());

        return retorno;
    }

    public Boolean verificarSeAtingiuLimitePorTipoDeAtendimentoIntervaloDeDatas(Date data) throws ProjetoException {
    	System.out.println("verificarSeAtingiuLimitePorTipoDeAtendimentoIntervaloDeDatas");

        Boolean retorno = false;

        Integer limite = aDao.contarAtendimentosPorTipoAtendimentoPorProfissionalDataUnica(this.agenda, data);

        Integer maximo = aDao.verQtdMaxAgendaEspec(this.agenda);

        if (limite >= maximo) {
            retorno = true;
        }

        return retorno;
    }

    public Boolean podeAdicionarAposVerificarTipoDeAtendimentoIntervaloDeDatas(Date data) throws ProjetoException {
    	System.out.println("podeAdicionarAposVerificarTipoDeAtendimentoIntervaloDeDatas");
        Boolean retorno = true;

        if (verificarSeExisteTipoAtendimentoEspecificoIntervaloDeDatas(data)) {
            retorno = verificarSeAtingiuLimitePorTipoDeAtendimentoIntervaloDeDatas(data);
        }

        return retorno;
    }

    public void verAgenda() throws ProjetoException {
    	System.out.println("verAgenda");

        Boolean dtEspecifica = aDao.buscarDataEspecifica(this.agenda);
        Boolean diaSem = aDao.buscarDiaSemana(this.agenda);

        if (dtEspecifica) {
            listarAgendamentosData();
            this.agenda.setMax(aDao.verQtdMaxAgendaData(this.agenda));
            this.agenda.setQtd(aDao.verQtdAgendadosData(this.agenda));

            verificarDisponibilidadeDataEspecifica(agenda.getQtd(), agenda.getMax());

        } else if (diaSem) {
            listarAgendamentosData();
            this.agenda.setMax(aDao.verQtdMaxAgendaEspec(this.agenda));
            this.agenda.setQtd(aDao.verQtdAgendadosEspec(this.agenda));
        } else {
            listarAgendamentosData();
            this.agenda.setMax(aDao.verQtdMaxAgendaGeral(this.agenda));
            this.agenda.setQtd(aDao.verQtdAgendadosData(this.agenda));
        }

    }

    public void validarParaConfirmar() throws ProjetoException {
    	System.out.println("validarParaConfirmar");
        if(verificarEncaixe()){
            preparaConfirmar();
        }
        else{
            JSFUtil.abrirDialog("dlgSenhaEncaixe");
        }
    }

    public Boolean verificarSeFoiAdicionadoODiaNaLista() {
        for(int i=0; i<listaNovosAgendamentos.size(); i++){
            if(agenda == listaNovosAgendamentos.get(i)){
                return true;
            }
        }
        return false;
    }

    public void preparaConfirmar() throws ProjetoException {
    	System.out.println("preparaConfirmar");
        if (tipoData.equals(TipoDataAgenda.DATA_UNICA.getSigla())) {
            if (agenda.getMax() == null || agenda.getQtd() == null) {
                JSFUtil.adicionarMensagemErro("Não existe disponibilidade de vaga para este dia!!", "Erro");
            } else if ((agenda.getMax() == 0 || agenda.getQtd() >= agenda.getMax()) && !agenda.getEncaixe()) {
                JSFUtil.adicionarMensagemErro("Não existe disponibilidade de vaga para este dia!!", "Erro");
            } else {
                addListaNovosAgendamentos();
                agendamentosConfirmados = true;
            }
        }
        agenda.getTipoAt().getProfissional();
        if (tipoData.equals(TipoDataAgenda.INTERVALO_DE_DATAS.getSigla())) {

            List<Date> listaAgendamentoPermitidos = new ArrayList<>();
            Date data1 = agenda.getDataAtendimento();
            Date data2 = agenda.getDataAtendimentoFinal();

            if (verAgendaIntervalo()) {

                if (agenda.getTipoAt().getProfissional()) {
                    listaNaoPermitidosIntervaloDeDatas = listarDatasBloqueadas(data1, data2);
                }

                listaNaoPermitidosIntervaloDeDatas = listarDatasComFeriado(listaNaoPermitidosIntervaloDeDatas, data1, data2);

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
    	System.out.println("listarDatasBloqueadas");
        BloqueioDAO bloqueioDAO = new BloqueioDAO();

        return bloqueioDAO.verificarBloqueioProfissionalIntervaloDeDatas(
                agenda.getProfissional().getId(), dataInicio, dataFinal, agenda.getTurno());
    }

    public List<Date> listarDatasComFeriado(List<Date> listaDatasBloqueadas, Date dataInicio, Date dataFinal) throws ProjetoException {
    	System.out.println("listarDatasComFeriado");
        FeriadoDAO feriadoDAO = new FeriadoDAO();

        List<Date> listaFeriado = feriadoDAO.verificarSeEhFeriadoIntervaloDeDatas(dataInicio, dataFinal);

        for (int i = 0; i < listaFeriado.size(); i++) {
            listaDatasBloqueadas.add(listaFeriado.get(i));
        }

        return listaDatasBloqueadas;
    }

    public Boolean verAgendaIntervalo() throws ProjetoException {
    	System.out.println("verAgendaIntervalo");
        if (this.agenda.getPaciente() == null
                || this.agenda.getPrograma() == null
                || this.agenda.getGrupo() == null
                || this.agenda.getTipoAt() == null
                || this.agenda.getDataAtendimento() == null
                || this.agenda.getDataAtendimentoFinal() == null
                || (this.agenda.getProfissional() == null && this.agenda
                .getEquipe() == null)) {
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
                diaSem = aDao.buscarDiaSemana(this.agenda);

                if (dtEspecifica == true || diaSem == true) {
                    temData = true;
                    if (diaSem) {
                        this.agenda.setMax(aDao
                                .verQtdMaxAgendaEspec(this.agenda));
                        this.agenda.setQtd(aDao
                                .verQtdAgendadosEspec(this.agenda));
                    }
                    if (dtEspecifica) {
                        this.agenda.setMax(aDao
                                .verQtdMaxAgendaData(this.agenda));
                        this.agenda.setQtd(aDao
                                .verQtdAgendadosData(this.agenda));
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
                JSFUtil.adicionarMensagemErro("Não foi possível agendar, pois tem horários lotados! Clique em Visualizar Ocupados para ver.",
                        "Erro");
            } else {
                retorno = true;
            }

            return retorno;

        }
    }

    public void carregarListaOcupados() {
    	System.out.println("carregarListaOcupados");
        JSFUtil.abrirDialog("dlgOcupados");
        getListaHorariosOcupados();
    }

    public void addListaNovosAgendamentos() {
    	System.out.println("addListaNovosAgendamentos");
        this.listaNovosAgendamentos.add(this.agenda);
    }

    public void listarAgendamentosData() throws ProjetoException {
    	System.out.println("listarAgendamentosData");
        this.listaAgendamentosData = aDao.listarAgendamentosData(this.agenda);
    }

    public void verificarPacienteAtivoInstituicao() throws ProjetoException {
    	System.out.println("verificarPacienteAtivoInstituicao");
        GerenciarPacienteDAO gerenciarPacienteDAO = new GerenciarPacienteDAO();

        Boolean pacienteAtivo = gerenciarPacienteDAO.verificarPacienteAtivoInstituicao(agenda.getPaciente().getId_paciente());

        if (agenda.getEquipe().getCodEquipe() != null) {

            if (pacienteAtivo) {
                gravarAgenda(SEM_FUNCIONARIO_LIBERACAO);
            } else {
                funcionario = new FuncionarioBean();
                JSFUtil.abrirDialog("dlgSenha");
            }
        }
        if (agenda.getProfissional().getId() != null) {
            gravarAgenda(SEM_FUNCIONARIO_LIBERACAO);
        }
    }

    public void validarSenhaLiberacao() throws ProjetoException {
    	System.out.println("validarSenhaLiberacao");
        FuncionarioDAO funcionarioDAO = new FuncionarioDAO();

        Integer idFuncionario = funcionarioDAO.validarCpfIhSenha(funcionario.getCpf(), funcionario.getSenha(), ValidacaoSenhaAgenda.LIBERACAO.getSigla());

        if (idFuncionario > 0) {
            JSFUtil.fecharDialog("dlgSenha");
            gravarAgenda(idFuncionario);
        } else {
            JSFUtil.adicionarMensagemErro("Funcionário com senha errada ou sem liberação!", "Erro!");
        }
    }

    public void validarSenhaEncaixe() throws ProjetoException {
    	System.out.println("validarSenhaEncaixe");
        FuncionarioDAO funcionarioDAO = new FuncionarioDAO();

        Integer idFuncionario = funcionarioDAO.validarCpfIhSenha(funcionario.getCpf(), funcionario.getSenha(), ValidacaoSenhaAgenda.ENCAIXE.getSigla());

        if (idFuncionario > 0) {
            JSFUtil.fecharDialog("dlgSenhaEncaixe");
            preparaConfirmar();
        } else {
            JSFUtil.adicionarMensagemErro("Funcionário com senha errada ou sem liberação!", "Erro!");
        }
    }

    public Boolean verificarEncaixe() {
    	System.out.println("verificarEncaixe");
        Boolean retorno = false;

        if (agenda.getEncaixe()) {
            if (SessionUtil.recuperarDadosSessao().getRealizaEncaixes()) {
                retorno = true;
            }
            else
            {
            	JSFUtil.adicionarMensagemAdvertencia("Usuário não tem permissão para fazer encaixe!", "Atenção");
            }
        }
        else{
            retorno = true;
        }

        return retorno;
    }

    public void gravarAgenda(Integer funcionarioLiberacao) {
    	System.out.println("gravarAgenda");
        // verificar se existe algum campo nao preenchido
        if (this.agenda.getPaciente() == null
                || this.agenda.getPrograma() == null
                || this.agenda.getGrupo() == null
                || this.agenda.getTipoAt() == null
                || this.agenda.getDataAtendimento() == null) {
            JSFUtil.adicionarMensagemErro("Campo(s) obrigatório(s) em falta!", "Erro");
            return;
        }

        // verificar as quantidades de vagas
        if ((this.agenda.getMax() <= 0) && ( !agenda.getEncaixe())) {
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

    public void consultarAgenda() throws ProjetoException {
    	System.out.println("consultarAgenda");
        if (this.dataAtendimentoC == null) {
            JSFUtil.adicionarMensagemErro("Selecione uma data de atendimento!", "Erro");
            return;
        }
        this.listaConsulta = aDao.consultarAgenda(this.dataAtendimentoC,
                dataAtendimentoFinalC, agenda.getEmpresa().getCodEmpresa());
    }

    //SEM USO NO MOMENTO
    public void excluirAgendamento() {
    	System.out.println("excluirAgendamento");
        boolean excluiu = aDao.excluirAgendamento(this.agenda);
        if (excluiu) {
            limparDados();
            JSFUtil.adicionarMensagemSucesso("Agendamento excluído com sucesso!", "Sucesso");
        } else {
            JSFUtil.adicionarMensagemErro("Ocorreu um erro durante a exclusão!", "Erro");
        }
        limparDados();
    }

    public void limparNaBuscaPaciente() {
    	System.out.println("limparNaBuscaPaciente");
        this.agenda.setTipoAt(null);
        this.agenda.setProfissional(new FuncionarioBean());
        this.agenda.setEquipe(new EquipeBean());
        this.agenda.setObservacao(new String());
        this.agenda.setDataAtendimento(null);
        this.agenda.setQtd(null);
        this.agenda.setMax(null);
    }

    public void limparNaBuscaPrograma() {
    	System.out.println("limparNaBuscaPrograma");
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
    	System.out.println("limparNaBuscaGrupo");
        this.agenda.setTipoAt(null);
        this.agenda.setProfissional(new FuncionarioBean());
        this.agenda.setEquipe(new EquipeBean());
        this.agenda.setObservacao(new String());
        this.agenda.setDataAtendimento(null);
        this.agenda.setQtd(null);
        this.agenda.setMax(null);
    }

    public void limparNaBuscaTipo() {
    	System.out.println("limparNaBuscaTipo");
        this.agenda.setProfissional(new FuncionarioBean());
        this.agenda.setEquipe(new EquipeBean());
        this.agenda.setObservacao(new String());
        this.agenda.setDataAtendimento(null);
        this.agenda.setQtd(null);
        this.agenda.setMax(null);
    }

    public void limparNaBuscaEquipeProf() throws ProjetoException {
    	System.out.println("limparNaBuscaEquipeProf");
        this.agenda.setObservacao(new String());
        //this.agenda.setDataAtendimento(null);
        this.agenda.setQtd(null);
        this.agenda.setMax(null);
        carregaListaProfissionalPorGrupo();
    }

    public void selectPrograma(SelectEvent event) throws ProjetoException {
    	System.out.println("selectPrograma");
        this.programaSelecionado = (ProgramaBean) event.getObject();
        atualizaListaGrupos(programaSelecionado);
        limparNaBuscaPrograma();
    }

    public void atualizaListaGrupos(ProgramaBean p) throws ProjetoException {
    	System.out.println("atualizaListaGrupos");
        this.programaSelecionado = p;
        this.listaGruposProgramas = gDao.listarGruposPorPrograma(p
                .getIdPrograma());

        listaTipos = new ArrayList<>();

    }

    public void atualizaListaTipos(GrupoBean g) throws ProjetoException {
    	System.out.println("atualizaListaTipos");
        this.grupoSelecionado = g;
        this.listaTipos = tDao.listarTipoAtPorGrupo(g.getIdGrupo());
    }

    // LISTAS E AUTOCOMPLETES INÍCIO
    public List<GrupoBean> listaGrupoAutoComplete(String query)
            throws ProjetoException {

        if (agenda.getPrograma().getIdPrograma() != null) {
        	System.out.println("listaGrupoAutoComplete");
            return gDao.listarGruposAutoComplete(query,
                    this.agenda.getPrograma());
        } else {
            return null;
        }

    }

    public void carregaListaGruposPorPrograma() throws ProjetoException {
    	System.out.println("listaGruposPorPrograma");
        if (agenda.getPrograma() != null) {
            if (agenda.getPrograma().getIdPrograma() != null) {
                listaGruposProgramas = gDao.listarGruposPorPrograma(agenda
                        .getPrograma().getIdPrograma());
            }
        }

    }

    public List<EquipeBean> listaEquipeAutoComplete(String query)
            throws ProjetoException {
    	System.out.println("listaEquipeAutoComplete");
        List<EquipeBean> result = eDao.listarEquipePorGrupoAutoComplete(query,
                agenda.getGrupo().getIdGrupo());
        return result;
    }

    public void carregaListaEquipePorTipoAtendimento()
            throws ProjetoException {
    	System.out.println("listaEquipePorTipoAtendimento");
        if (agenda.getTipoAt() != null) {
            if (agenda.getGrupo().getIdGrupo() != null) {
                listaEquipePorTipoAtendimento = eDao
                        .listarEquipePorGrupo(agenda.getGrupo().getIdGrupo());
            }
        }
    }

    public List<FuncionarioBean> listaProfissionalPorGrupoAutoComplete(
            String query) throws ProjetoException {
    	System.out.println("listaProfissionalPorGrupoAutoComplete");
        List<FuncionarioBean> result = fDao.listarProfissionalBuscaPorGrupo(
                query, agenda.getGrupo().getIdGrupo());
        return result;
    }

    public void carregaListaProfissionalPorGrupo()
            throws ProjetoException {
    	listaProfissionalPorGrupo = new ArrayList<FuncionarioBean>();
    	System.out.println("listaProfissionalPorGrupo");
        if (agenda.getGrupo() != null) {
            if (agenda.getGrupo().getIdGrupo() != null) {
            	listaProfissionalPorGrupo = fDao
                        .listarProfissionalPorGrupo(agenda.getGrupo()
                                .getIdGrupo());
            }
        }

    }

    public List<TipoAtendimentoBean> listaTipoAtAutoComplete(String query)
            throws ProjetoException {
    	System.out.println("listaTipoAtAutoComplete");
        List<TipoAtendimentoBean> lista = new ArrayList<>();

        if (agenda.getGrupo() != null) {
            lista = tDao.listarTipoAtAutoComplete(query, this.agenda.getGrupo());
        } else
            return null;
        return lista;
    }

    public void validarTipoAtendimentoNaAgenda(SelectEvent event) throws ProjetoException {
    	System.out.println("validarTipoAtendimentoNaAgenda");
        this.tipoAtendimentoSelecionado = (TipoAtendimentoBean) event.getObject();

        agenda.setTipoAt(new TipoAtendimentoDAO().listarInformacoesTipoAtendimentoEquieProgramaPorId(tipoAtendimentoSelecionado.getIdTipo()));

        if (agenda.getTipoAt().getIntervaloMinimo() > 0) {

            Boolean intervalo = aDao.retornarIntervaloUltimoAgendamento(agenda.getPaciente().getId_paciente(), agenda.getTipoAt().getIdTipo(), agenda.getTipoAt().getIntervaloMinimo());

            if (!intervalo) {
                JSFUtil.adicionarMensagemErro("Paciente tem agendamento inferior ao mínimo para esse tipo de atendimento!", "Erro");

                agenda.setTipoAt(new TipoAtendimentoBean());
            }
        }
    }

    public void listaDiasDeAtendimentoAtuais() throws ProjetoException {
        System.out.println("listaDiasDeAtendimentoAtuais");
        if (agenda.getTipoAt() != null) {
            if (agenda.getTipoAt().getIdTipo() != null) {
                if (agenda.getProfissional().getId() != null) {
                    listaConfigAgenda = aDao.retornarDiaAtendimentoProfissional(agenda.getProfissional().getId());
                } else {
                    if (agenda.getEquipe().getCodEquipe() != null) {
                        listaConfigAgenda = aDao.retornarDiaAtendimentoEquipe(agenda.getEquipe().getCodEquipe());
                    }
                }
            }
        }
    }

    public List<ProgramaBean> listaProgramaAutoCompleteUsuarioOutraUnidade(String query)
            throws ProjetoException {
    	System.out.println("listaProgramaAutoCompleteUsuarioOutraUnidade");
        ProgramaDAO pDao = new ProgramaDAO();
        List<ProgramaBean> result = pDao.listarProgramasBuscaUsuarioOutraUnidade(query, agenda.getEmpresa().getCodEmpresa());
        return result;
    }

    // LISTAS E AUTOCOMPLETES FINAL

    public void selectGrupo(SelectEvent event) throws ProjetoException {
    	System.out.println("selectGrupo");
        this.grupoSelecionado = (GrupoBean) event.getObject();
        atualizaListaTipos(grupoSelecionado);
        atualizaListaProfPorGrupo();
        limparNaBuscaGrupo();
    }

    public void atualizaListaProfPorGrupo() throws ProjetoException {
    	System.out.println("atualizaListaProfPorGrupo");
        this.listaProfissional = fDao
                .listarProfissionalPorGrupo(this.grupoSelecionado.getIdGrupo());
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

    public void setListaNovosAgendamentos(
            List<AgendaBean> listaNovosAgendamentos) {
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

	public FuncionarioDAO getfDao() {
		return fDao;
	}

	public void setfDao(FuncionarioDAO fDao) {
		this.fDao = fDao;
	}

	public TipoAtendimentoDAO gettDao() {
		return tDao;
	}

	public void settDao(TipoAtendimentoDAO tDao) {
		this.tDao = tDao;
	}

	public AgendaDAO getaDao() {
		return aDao;
	}

	public void setaDao(AgendaDAO aDao) {
		this.aDao = aDao;
	}

	public GrupoDAO getgDao() {
		return gDao;
	}

	public void setgDao(GrupoDAO gDao) {
		this.gDao = gDao;
	}

	public EquipeDAO geteDao() {
		return eDao;
	}

	public void seteDao(EquipeDAO eDao) {
		this.eDao = eDao;
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

	public List<ConfigAgendaParte1Bean> getListaConfigAgenda() {
		return listaConfigAgenda;
	}

	public void setListaConfigAgenda(List<ConfigAgendaParte1Bean> listaConfigAgenda) {
		this.listaConfigAgenda = listaConfigAgenda;
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
}
