package br.gov.al.maceio.sishosp.hosp.control;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import br.gov.al.maceio.sishosp.comum.util.DataUtil;
import br.gov.al.maceio.sishosp.comum.util.JSFUtil;
import br.gov.al.maceio.sishosp.hosp.enums.TipoDataAgenda;
import br.gov.al.maceio.sishosp.hosp.model.*;
import org.primefaces.event.SelectEvent;

import br.gov.al.maceio.sishosp.acl.dao.FuncionarioDAO;
import br.gov.al.maceio.sishosp.acl.model.FuncionarioBean;
import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.hosp.dao.AgendaDAO;
import br.gov.al.maceio.sishosp.hosp.dao.EquipeDAO;
import br.gov.al.maceio.sishosp.hosp.dao.GrupoDAO;
import br.gov.al.maceio.sishosp.hosp.dao.TipoAtendimentoDAO;

@ManagedBean(name = "AgendaController")
@ViewScoped
public class AgendaController implements Serializable {

    private static final long serialVersionUID = 1L;
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
    private List<FuncionarioBean> listaProfissional;
    private boolean habilitarDetalhes;
    private List<TipoAtendimentoBean> listaTipos;
    private List<TipoAtendimentoBean> listaTiposPorGrupo;
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

    public AgendaController() {
        this.agenda = new AgendaBean();
        grupoSelecionado = new GrupoBean();
        listaGruposProgramas = new ArrayList<GrupoBean>();
        listaTipos = new ArrayList<TipoAtendimentoBean>();
        programaSelecionado = new ProgramaBean();
        this.listaNovosAgendamentos = new ArrayList<AgendaBean>();
        this.listaProfissional = new ArrayList<FuncionarioBean>();
        this.listaAgendamentosData = new ArrayList<AgendaBean>();
        this.listaConsulta = new ArrayList<AgendaBean>();
        this.dataAtendimentoC = null;
        this.dataAtendimentoFinalC = null;
        this.cnsC = new String();
        this.protuarioC = null;
        this.tipoC = new TipoAtendimentoBean();
        this.situacao = new String();
        listaTiposPorGrupo = new ArrayList<TipoAtendimentoBean>();
        listaEquipePorTipoAtendimento = new ArrayList<EquipeBean>();
        tipoData = TipoDataAgenda.DATA_UNICA.getSigla();
        temLotado = false;
        listaHorariosOcupados = new ArrayList<AgendaBean>();
        agendamentosConfirmados = false;
        dataAtual = DataUtil.mesIhAnoAtual();
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
    }

    public void verificaDisponibilidadeData() throws ProjetoException {
        if (!agenda.getTipoAt().isEquipe()) {
            FeriadoBean feriado = aDao.verificarFeriado(this.agenda
                    .getDataAtendimento());
            List<BloqueioBean> bloqueio = new ArrayList<BloqueioBean>();
            if (this.agenda.getProfissional().getId() != null) {
                bloqueio = aDao.verificarBloqueioProfissional(
                        this.agenda.getProfissional(),
                        this.agenda.getDataAtendimento(),
                        this.agenda.getTurno());
            }
            if (feriado != null) {
                JSFUtil.adicionarMensagemAdvertencia("Data bloqueada! "
                        + feriado.getDescFeriado(), "Feriado");
            } else if (!bloqueio.isEmpty()) {
                JSFUtil.adicionarMensagemAdvertencia("Data bloqueada para este profissional!", "Bloqueio");
            } else
                verAgenda();

        }

    }

    public void verAgenda() throws ProjetoException {
        if (this.agenda.getPaciente() == null
                || this.agenda.getPrograma() == null
                || this.agenda.getGrupo() == null
                || this.agenda.getTipoAt() == null
                || this.agenda.getDataAtendimento() == null
                || (this.agenda.getProfissional() == null && this.agenda
                .getEquipe() == null)) {
            JSFUtil.adicionarMensagemErro("Campo(s) obrigatório(s) em falta!", "Erro");
            return;
        } else {
            boolean dtEspecifica = aDao.buscarDataEspecifica(this.agenda);
            boolean diaSem = aDao.buscarDiaSemana(this.agenda);
            boolean limitePorTipoAtend = aDao
                    .buscarTabTipoAtendAgenda(this.agenda);
            if (dtEspecifica && limitePorTipoAtend) {
                listarAgendamentosData();
                this.agenda.setMax(aDao.verQtdMaxAgendaData(this.agenda));
                this.agenda.setQtd(aDao.verQtdAgendadosData(this.agenda));
            } else if (diaSem && limitePorTipoAtend) {
                listarAgendamentosData();
                this.agenda.setMax(aDao.verQtdMaxAgendaEspec(this.agenda));
                this.agenda.setQtd(aDao.verQtdAgendadosEspec(this.agenda));
            } else {
                listarAgendamentosData();
                this.agenda.setMax(0);
                this.agenda.setQtd(0);
            }
        }
    }

    public void preparaConfirmar() throws ProjetoException {
        if (this.agenda.getPaciente() == null
                || this.agenda.getPrograma() == null
                || this.agenda.getGrupo() == null
                || this.agenda.getTipoAt() == null
                || this.agenda.getDataAtendimento() == null
                || (tipoData.equals(TipoDataAgenda.INTERVALO_DE_DATAS.getSigla()) && this.agenda
                .getDataAtendimentoFinal() == null)
                || (this.agenda.getProfissional() == null && this.agenda
                .getEquipe() == null)) {
            JSFUtil.adicionarMensagemErro("Campo(s) obrigatório(s) em falta!", "Erro");
        } else {
            if (tipoData.equals(TipoDataAgenda.DATA_UNICA.getSigla())) {
                addListaNovosAgendamentos();
            }
            if (tipoData.equals(TipoDataAgenda.INTERVALO_DE_DATAS.getSigla())) {
                verAgendaIntervalo();
            }
            agendamentosConfirmados = true;
        }
    }

    public void verAgendaIntervalo() throws ProjetoException {
        if (this.agenda.getPaciente() == null
                || this.agenda.getPrograma() == null
                || this.agenda.getGrupo() == null
                || this.agenda.getTipoAt() == null
                || this.agenda.getDataAtendimento() == null
                || this.agenda.getDataAtendimentoFinal() == null
                || (this.agenda.getProfissional() == null && this.agenda
                .getEquipe() == null)) {
            JSFUtil.adicionarMensagemErro("Campo(s) obrigatório(s) em falta!", "Erro");
            return;
        } else {

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
            }

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

    public void gravarAgenda() {
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
        if (this.agenda.getMax() <= 0) {
            JSFUtil.adicionarMensagemErro("Quantidade máxima inválida!", "Erro");
            return;
        }

        // verificar a quantidade de agendamentos
        if (this.agenda.getQtd() >= this.agenda.getMax()) {
            JSFUtil.adicionarMensagemErro("Quantidade de agendamentos está no limite!", "Erro");
            return;
        }

        if (this.listaNovosAgendamentos.isEmpty()) {
            JSFUtil.adicionarMensagemErro("A lista de novos agendamentos está vazia!", "Erro");
            return;
        }

        boolean cadastrou = false;

        cadastrou = aDao.gravarAgenda(this.agenda, this.listaNovosAgendamentos);

        if (cadastrou) {
            limparDados();
            JSFUtil.adicionarMensagemSucesso("Agenda cadastrada com sucesso!", "Sucesso");
        } else {
            JSFUtil.adicionarMensagemErro("Ocorreu um erro durante o cadastro!", "Erro");
        }
        limparDados();
    }

    public void consultarAgenda() throws ProjetoException {
        if (this.dataAtendimentoC == null) {
            JSFUtil.adicionarMensagemErro("Selecione uma data de atendimento!", "Erro");
            return;
        }
        this.listaConsulta = aDao.consultarAgenda(this.dataAtendimentoC,
                dataAtendimentoFinalC);
    }

    //SEM USO NO MOMENTO
    public void excluirAgendamento() {
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
        this.agenda.setPrograma(null);
        this.agenda.setGrupo(null);
        this.agenda.setTipoAt(null);
        this.agenda.setProfissional(new FuncionarioBean());
        this.agenda.setEquipe(new EquipeBean());
        this.agenda.setObservacao(new String());
        this.agenda.setDataAtendimento(null);
        this.agenda.setQtd(null);
        this.agenda.setMax(null);
    }

    public void limparNaBuscaPrograma() {
        this.agenda.setGrupo(null);
        this.agenda.setTipoAt(null);
        this.agenda.setProfissional(new FuncionarioBean());
        this.agenda.setEquipe(new EquipeBean());
        this.agenda.setObservacao(new String());
        this.agenda.setDataAtendimento(null);
        this.agenda.setQtd(null);
        this.agenda.setMax(null);
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

    public void limparNaBuscaEquipeProf() {
        this.agenda.setObservacao(new String());
        this.agenda.setDataAtendimento(null);
        this.agenda.setQtd(null);
        this.agenda.setMax(null);
    }

    public void selectPrograma(SelectEvent event) throws ProjetoException {
        this.programaSelecionado = (ProgramaBean) event.getObject();
        atualizaListaGrupos(programaSelecionado);
        limparNaBuscaPrograma();
    }

    public void atualizaListaGrupos(ProgramaBean p) throws ProjetoException {
        this.programaSelecionado = p;
        this.listaGruposProgramas = gDao.listarGruposPorPrograma(p
                .getIdPrograma());
        for (GrupoBean g : listaGruposProgramas) {
        }

    }

    public void atualizaListaTipos(GrupoBean g) throws ProjetoException {
        this.grupoSelecionado = g;
        this.listaTipos = tDao.listarTipoAtPorGrupo(g.getIdGrupo());
    }

    // LISTAS E AUTOCOMPLETES INÍCIO
    public List<GrupoBean> listaGrupoAutoComplete(String query)
            throws ProjetoException {

        if (agenda.getPrograma().getIdPrograma() != null) {
            return gDao.listarGruposAutoComplete(query,
                    this.agenda.getPrograma());
        } else {
            return null;
        }

    }

    public List<GrupoBean> listaGruposPorPrograma() throws ProjetoException {
        if (agenda.getPrograma() != null) {
            if (agenda.getPrograma().getIdPrograma() != null) {
                listaGruposProgramas = gDao.listarGruposPorPrograma(agenda
                        .getPrograma().getIdPrograma());
            }
        }
        return listaGruposProgramas;
    }

    public List<EquipeBean> listaEquipeAutoComplete(String query)
            throws ProjetoException {

        List<EquipeBean> result = eDao.listarEquipePorGrupoAutoComplete(query,
                agenda.getGrupo().getIdGrupo());
        return result;
    }

    public List<EquipeBean> listaEquipePorTipoAtendimento()
            throws ProjetoException {

        if (agenda.getTipoAt() != null) {
            if (agenda.getGrupo().getIdGrupo() != null) {
                listaEquipePorTipoAtendimento = eDao
                        .listarEquipePorGrupo(agenda.getGrupo().getIdGrupo());
            }
        }
        return listaEquipePorTipoAtendimento;
    }

    public List<FuncionarioBean> listaProfissionalPorGrupoAutoComplete(
            String query) throws ProjetoException {
        List<FuncionarioBean> result = fDao.listarProfissionalBuscaPorGrupo(
                query, agenda.getGrupo().getIdGrupo());
        return result;
    }

    public List<FuncionarioBean> listaProfissionalPorGrupo()
            throws ProjetoException {
        if (agenda.getGrupo() != null) {
            if (agenda.getGrupo().getIdGrupo() != null) {
                List<FuncionarioBean> result = fDao
                        .listarProfissionalPorGrupo(agenda.getGrupo()
                                .getIdGrupo());
                return result;
            } else {
                return null;
            }

        } else
            return null;

    }

    public List<TipoAtendimentoBean> listaTipoAtAutoComplete(String query)
            throws ProjetoException {
        if (agenda.getGrupo() != null) {
            return tDao.listarTipoAtAutoComplete(query, this.agenda.getGrupo());
        } else
            return null;
    }


    public void validarTipoAtendimentoNaAgenda() throws ProjetoException {
        if (agenda.getTipoAt().getIntervaloMinimo() > 0) {
            Boolean intervalo = aDao.retornarIntervaloUltimoAgendamento(agenda.getPaciente().getId_paciente(), agenda.getTipoAt().getIdTipo(), agenda.getTipoAt().getIntervaloMinimo());

            if (!intervalo) {
                JSFUtil.adicionarMensagemErro("Paciente tem agendamento inferior ao mínimo para esse tipo de atendimento!", "Erro");

                agenda.setTipoAt(null);
            }
        }
    }

    public List<TipoAtendimentoBean> listaTipoAtendimentoPorGrupo()
            throws ProjetoException {

        if (agenda.getGrupo() != null) {
            if (agenda.getGrupo().getIdGrupo() != null) {
                listaTiposPorGrupo = tDao.listarTipoAtPorGrupo(agenda
                        .getGrupo().getIdGrupo());
            }
        }
        return listaTiposPorGrupo;
    }

    public List<ConfigAgendaParte1Bean> listaDiasDeAtendimentoAtuais() throws ProjetoException {
        List<ConfigAgendaParte1Bean> listaConfigAgenda = new ArrayList<>();
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
        return listaConfigAgenda;
    }

    // LISTAS E AUTOCOMPLETES FINAL

    public void selectGrupo(SelectEvent event) throws ProjetoException {
        this.grupoSelecionado = (GrupoBean) event.getObject();
        atualizaListaTipos(grupoSelecionado);
        atualizaListaProfPorGrupo();
        limparNaBuscaGrupo();
    }

    public void atualizaListaProfPorGrupo() throws ProjetoException {
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

}
