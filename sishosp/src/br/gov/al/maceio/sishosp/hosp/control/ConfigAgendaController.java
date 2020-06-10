package br.gov.al.maceio.sishosp.hosp.control;

import java.io.Serializable;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import br.gov.al.maceio.sishosp.comum.util.*;

import br.gov.al.maceio.sishosp.acl.dao.FuncionarioDAO;
import br.gov.al.maceio.sishosp.acl.model.FuncionarioBean;
import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.hosp.dao.*;
import br.gov.al.maceio.sishosp.hosp.enums.OpcaoAtendimento;
import br.gov.al.maceio.sishosp.hosp.enums.OpcaoConfiguracaoAgenda;
import br.gov.al.maceio.sishosp.hosp.model.*;
import br.gov.al.maceio.sishosp.hosp.model.dto.ConfiguracaoAgendaEquipeEspecialidadeDTO;

import static br.gov.al.maceio.sishosp.comum.util.HorarioOuTurnoUtil.gerarHorariosAtendimento;

@ManagedBean(name = "ConfigAgendaController")
@ViewScoped
public class ConfigAgendaController implements Serializable {

    private static final long serialVersionUID = 1L;

    private ConfigAgendaParte1Bean confParte1;
    private ConfigAgendaParte2Bean confParte2;
    private FuncionarioBean prof;
    private EquipeBean equipe;
    private int tipo;
    private String opcaoAtendimento;
    private ConfiguracaoAgendaEquipeEspecialidadeDTO configuracaoAgendaEquipeEspecialidadeDTO;

    private List<ConfigAgendaParte2Bean> listaTipos;
    private List<ConfigAgendaParte1Bean> listaHorariosProfissional;
    private List<ConfigAgendaParte1Bean> listaHorariosEquipe;
    private List<FuncionarioBean> listaProfissionais;
    private List<EquipeBean> listaEquipes;
    private List<EspecialidadeBean> listaEspecialidades;
    private List<ConfiguracaoAgendaEquipeEspecialidadeDTO> listaConfiguracaoAgendaEquipeEspecialidade;

    private List<GrupoBean> listaGruposProgramas;
    private List<TipoAtendimentoBean> listaTipoAtendimentosGrupo;
    private List<ConfigAgendaParte1Bean> listaProfissionalConfiguracaoGeral;
    private List<ConfigAgendaParte1Bean> listaProfissionalConfiguracaoEspecifica;
    private List<ConfigAgendaParte1Bean> listaEquipeConfiguracaoGeral;
    private List<ConfigAgendaParte1Bean> listaEquipeConfiguracaoEspecifica;
    private ArrayList<String> listaHorarios;

    private ConfigAgendaDAO cDao = new ConfigAgendaDAO();
    private FuncionarioDAO fDao = new FuncionarioDAO();
    private EquipeDAO eDao = new EquipeDAO();

    //CONSTANTES
    private static final String ENDERECO_CADASTRO_PROFISSIONAL = "configuracaoAgendaProfissional?faces-redirect=true";
    private static final String ENDERECO_CADASTRO_EQUIPE = "configuracaoAgendaEquipe?faces-redirect=true";
    private static final String ENDERECO_TIPO = "&amp;tipo=";
    private static final String ENDERECO_ID = "&amp;codconfigagenda=";

    public ConfigAgendaController() {
        this.confParte1 = new ConfigAgendaParte1Bean();
        this.confParte2 = new ConfigAgendaParte2Bean();
        this.prof = new FuncionarioBean();
        this.equipe = new EquipeBean();
        this.listaTipos = new ArrayList<ConfigAgendaParte2Bean>();
        this.listaHorariosProfissional = null;
        this.listaHorariosEquipe = null;
        this.listaProfissionais = null;
        this.listaEquipes = null;
        this.listaGruposProgramas = new ArrayList<>();
        this.listaTipoAtendimentosGrupo = new ArrayList<>();
        this.listaProfissionalConfiguracaoGeral = new ArrayList<>();
        this.listaProfissionalConfiguracaoEspecifica = new ArrayList<>();
        this.listaEquipeConfiguracaoGeral = new ArrayList<>();
        this.listaEquipeConfiguracaoEspecifica = new ArrayList<>();
        this.listaHorarios = new ArrayList<>();
        this.listaEspecialidades = new ArrayList<>();
        this.listaConfiguracaoAgendaEquipeEspecialidade = new ArrayList<>();
        this.configuracaoAgendaEquipeEspecialidadeDTO = new ConfiguracaoAgendaEquipeEspecialidadeDTO();
    }

    public void limparDados() {
        this.confParte1 = new ConfigAgendaParte1Bean();
        this.confParte2 = new ConfigAgendaParte2Bean();
        this.listaTipos = new ArrayList<ConfigAgendaParte2Bean>();
        this.listaHorariosProfissional = null;
        this.listaHorariosEquipe = null;
        this.listaProfissionais = null;
        this.listaEquipes = null;
        this.configuracaoAgendaEquipeEspecialidadeDTO = new ConfiguracaoAgendaEquipeEspecialidadeDTO();
        this.listaConfiguracaoAgendaEquipeEspecialidade = new ArrayList<>();
    }

    public String redirectEditProfissional() {
        return RedirecionarUtil.redirectEdit(ENDERECO_CADASTRO_PROFISSIONAL, ENDERECO_ID, this.confParte1.getIdConfiAgenda(), ENDERECO_TIPO, tipo);
    }

    public String redirectInsertProfissional() {
        return RedirecionarUtil.redirectInsert(ENDERECO_CADASTRO_PROFISSIONAL, ENDERECO_TIPO, tipo);
    }

    public String redirectInsertEquipe() {
        return RedirecionarUtil.redirectInsert(ENDERECO_CADASTRO_EQUIPE, ENDERECO_TIPO, tipo);
    }

    public String redirectEditEquipe() {
        return RedirecionarUtil.redirectEdit(ENDERECO_CADASTRO_EQUIPE, ENDERECO_ID, this.confParte1.getIdConfiAgenda(), ENDERECO_TIPO, tipo);
    }

    public void inicializarConfiguracaoAgendaProfissional() throws ProjetoException {
        carregarListaConfiguracaoProfissionalGeral();
        carregarListaConfiguracaoProfissionalEspecifica();
    }

    public void inicializarConfiguracaoAgendaEquipe() throws ProjetoException {
        carregarListaConfiguracaoEquipeGeral();
        carregarListaConfiguracaoEquipeEspecificaa();
    }

    public void getEditAgendaProfissional() throws ProjetoException, ParseException {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Map<String, String> params = facesContext.getExternalContext()
                .getRequestParameterMap();
        carregarHorarioOuTurno();
        if (params.get("codconfigagenda") != null) {
            Integer id = Integer.parseInt(params.get("codconfigagenda"));
            tipo = Integer.parseInt(params.get("tipo"));

            this.confParte1 = cDao.listarHorariosPorIDProfissionalEdit(id);
            listaTipos = cDao.listarTipoAtendimentoConfiguracaoAgendaProfissional(id);

            if (confParte1.getOpcao().equals(OpcaoConfiguracaoAgenda.DIA_DA_SEMANA.getSigla())) {
                confParte1.setDiasSemana(cDao.listarDiasAtendimentoProfissionalPorId(id));
            }

        } else {
            tipo = Integer.parseInt(params.get("tipo"));

        }

    }

    private void carregarHorarioOuTurno() throws ProjetoException, ParseException {
        opcaoAtendimento = HorarioOuTurnoUtil.retornarOpcaoAtendimentoUnidade();

        if (opcaoAtendimento.equals(OpcaoAtendimento.SOMENTE_HORARIO.getSigla())
                || opcaoAtendimento.equals(OpcaoAtendimento.AMBOS.getSigla())) {
            gerarHorariosAtendimento();
        }

    }

    private void gerarHorariosAtendimento() throws ParseException, ProjetoException {
        listaHorarios = HorarioOuTurnoUtil.gerarHorariosAtendimento();
    }

    public void carregarEspecialidadesEquipe() throws ProjetoException {
        EspecialidadeDAO eDao = new EspecialidadeDAO();
        listaEspecialidades = eDao.listarEspecialidadesPorEquipe(confParte1.getEquipe().getCodEquipe());
    }

    private void adicionarEspecialidadeComQuantidade() throws ProjetoException {
        EspecialidadeDAO especialidadeDAO = new EspecialidadeDAO();
        configuracaoAgendaEquipeEspecialidadeDTO.setEspecialidade(especialidadeDAO.listarEspecialidadePorId(configuracaoAgendaEquipeEspecialidadeDTO.getEspecialidade().getCodEspecialidade()));
        listaConfiguracaoAgendaEquipeEspecialidade.add(configuracaoAgendaEquipeEspecialidadeDTO);
        configuracaoAgendaEquipeEspecialidadeDTO = new ConfiguracaoAgendaEquipeEspecialidadeDTO();
    }

    public Boolean valdiarEspecialidadeIhQuantidade() {
        Boolean retorno = true;
        if (VerificadorUtil.verificarSeObjetoNulo(configuracaoAgendaEquipeEspecialidadeDTO.getEspecialidade().getCodEspecialidade()) ||
        VerificadorUtil.verificarSeObjetoNuloOuZero(configuracaoAgendaEquipeEspecialidadeDTO.getQuantidade())){
            retorno = false;
        }
        return retorno;
    }

    public void validarAdicionarEspecialidadeComQuantidade() throws ProjetoException {

        if(!valdiarEspecialidadeIhQuantidade()){
            return;
        }

        boolean existe = false;
        if (listaConfiguracaoAgendaEquipeEspecialidade.size() == 0) {
            adicionarEspecialidadeComQuantidade();
        } else {

            for (int i = 0; i < listaConfiguracaoAgendaEquipeEspecialidade.size(); i++) {
                if (listaConfiguracaoAgendaEquipeEspecialidade.get(i).getEspecialidade().getCodEspecialidade() == configuracaoAgendaEquipeEspecialidadeDTO.getEspecialidade().getCodEspecialidade()) {
                    existe = true;
                }
            }
            if (existe == false) {
                adicionarEspecialidadeComQuantidade();
            } else {
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        "Essa especialidade já foi adicionada!", "Erro");
                FacesContext.getCurrentInstance().addMessage(null, msg);
            }

        }

    }

    public void removerEspecialidadeComQuantidade(ConfiguracaoAgendaEquipeEspecialidadeDTO configuracaoAgendaEquipeEspecialidadeDTO){
        listaConfiguracaoAgendaEquipeEspecialidade.remove(configuracaoAgendaEquipeEspecialidadeDTO);
    }

    public void getEditAgendaEquipe() throws ProjetoException, ParseException {

        carregarHorarioOuTurno();

        FacesContext facesContext = FacesContext.getCurrentInstance();
        Map<String, String> params = facesContext.getExternalContext()
                .getRequestParameterMap();
        if (params.get("codconfigagenda") != null) {
            Integer id = Integer.parseInt(params.get("codconfigagenda"));
            tipo = Integer.parseInt(params.get("tipo"));

            this.confParte1 = cDao.listarHorariosPorIDEquipeEdit(id);
            this.confParte2 = cDao.listarHorariosPorIDEquipeEditParte2(id);
            listaTipos = cDao.listarTipoAtendimentoConfiguracaoAgendaEquipe(id);
            listaConfiguracaoAgendaEquipeEspecialidade = cDao.listarQuantidadeEspecialidades(id);

            if (confParte1.getOpcao().equals(OpcaoConfiguracaoAgenda.DIA_DA_SEMANA.getSigla())) {
                confParte1.setDiasSemana(cDao.listarDiasAtendimentoEquipePorId(id));
            }

        } else {
            tipo = Integer.parseInt(params.get("tipo"));

        }

    }


    public List<TipoAtendimentoBean> selectTipoAtendimento(String tipoAtendimento)
            throws ProjetoException {
        TipoAtendimentoDAO tDao = new TipoAtendimentoDAO();
        if (confParte2.getGrupo() != null) {
            listaTipoAtendimentosGrupo = tDao.listarTipoAtPorGrupo(confParte2
                    .getGrupo().getIdGrupo(), tipoAtendimento);
        }
        return listaTipoAtendimentosGrupo;
    }

    public void addNaListaConfigProfissional() {
        if (confParte2.getQtd() == null) {
            JSFUtil.adicionarMensagemErro("Insira a quantidade!", "Erro");
        } else if (confParte2.getPrograma() == null) {
            JSFUtil.adicionarMensagemErro("Insira o programa!", "Erro");
        } else if (confParte2.getGrupo() == null) {
            JSFUtil.adicionarMensagemErro("Insira o grupo!", "Erro");
        } else {
            this.listaTipos.add(confParte2);
        }
        this.confParte2 = new ConfigAgendaParte2Bean();
    }

    public void addNaListaConfigEquipe() {
        ProgramaBean programaSelecionado = new ProgramaBean();
        GrupoBean grupoSelecionado = new GrupoBean();
        if (confParte2.getQtd() == null) {
            JSFUtil.adicionarMensagemErro("Insira a quantidade!", "Erro");
        } else if (confParte2.getPrograma() == null) {
            JSFUtil.adicionarMensagemErro("Insira o programa!", "Erro");
        } else if (confParte2.getGrupo() == null) {
            JSFUtil.adicionarMensagemErro("Insira o grupo!", "Erro");
        } else {
            programaSelecionado = confParte2.getPrograma();
            grupoSelecionado = confParte2.getGrupo();
            this.listaTipos.add(confParte2);
        }
        this.confParte2 = new ConfigAgendaParte2Bean();
        confParte2.setPrograma(programaSelecionado);
        confParte2.setGrupo(grupoSelecionado);
    }

    public void removeNaLista() {
        this.listaTipos.remove(confParte2);
        confParte2 = new ConfigAgendaParte2Bean();
    }

    // GRUPOBEAN

    public void selectGrupo() throws ProjetoException {
        GrupoDAO gDao = new GrupoDAO();
        if (confParte2.getPrograma() != null) {
            listaGruposProgramas = gDao.listarGruposPorPrograma(confParte2
                    .getPrograma().getIdPrograma());
        }

    }

    public List<GrupoBean> listaGrupoAutoComplete(String query)
            throws ProjetoException {
        GrupoDAO gDao = new GrupoDAO();
        if (confParte2.getPrograma() != null) {
            return gDao.listarGruposNoAutoComplete(query,
                    confParte2.getPrograma().getIdPrograma());
        } else {
            return null;
        }

    }

    // FINAL GRUPOBEAN

    // TIPOATENDIMENTOBEAN

    public List<TipoAtendimentoBean> listaTipoAtAutoComplete(String query)
            throws ProjetoException {
        TipoAtendimentoDAO tDao = new TipoAtendimentoDAO();
        if (confParte2.getGrupo() != null) {
            return tDao.listarTipoAtAutoComplete(query, confParte2.getGrupo(), null);
        } else {
            return null;
        }
    }

    public List<TipoAtendimentoBean> listaTipoAtEquipeAutoComplete(String query)
            throws ProjetoException {
        TipoAtendimentoDAO tDao = new TipoAtendimentoDAO();
        if (confParte2.getGrupo() != null) {
            return tDao.listarTipoAtAutoComplete(query, confParte2.getGrupo(), "E");
        } else {
            return null;
        }
    }

    public List<TipoAtendimentoBean> listaTipoAtProfissionalAutoComplete(String query)
            throws ProjetoException {
        TipoAtendimentoDAO tDao = new TipoAtendimentoDAO();
        if (confParte2.getGrupo() != null) {
            return tDao.listarTipoAtAutoComplete(query, confParte2.getGrupo(), "P");
        } else {
            return null;
        }
    }

    public void carregaListaTipoAtendimento(String tipo)
            throws ProjetoException {
        TipoAtendimentoDAO tDao = new TipoAtendimentoDAO();
        if (confParte2.getGrupo() != null) {
            listaTipoAtendimentosGrupo = tDao.listarTipoAtPorGrupo(confParte2
                    .getGrupo().getIdGrupo(), tipo);
        }
    }

    public void carregaListaTipoAtendimentoPorProgramaEGrupo(String tipo)
            throws ProjetoException {
        TipoAtendimentoDAO tDao = new TipoAtendimentoDAO();
        if (confParte2.getGrupo() != null) {
            listaTipoAtendimentosGrupo = tDao.listarTipoAtPorProgramaEGrupo(confParte2
                    .getPrograma().getIdPrograma(), confParte2
                    .getGrupo().getIdGrupo(), tipo);
        }
    }

    // FINAL TIPOATENDIMENTOBEAN

    // EQUIPEBEAN

    public List<EquipeBean> listaEquipeAutoComplete(String query)
            throws ProjetoException {
        int grupoId = 0;
        if (confParte2.getGrupo() == null) {
            grupoId = cDao.carregarGrupoDaEquipe(confParte1.getIdConfiAgenda());
        } else {
            grupoId = confParte2.getGrupo().getIdGrupo();
        }
        List<EquipeBean> result = eDao.listarEquipePorGrupoAutoComplete(query,
                grupoId);
        return result;
    }

    public List<EquipeBean> selectEquipe() throws ProjetoException {
        EquipeDAO eDao = new EquipeDAO();

        if (confParte2.getGrupo() != null) {
            this.listaEquipes = eDao.listarEquipePorGrupo(confParte2.getGrupo()
                    .getIdGrupo());
        } else {
            int grupoId = cDao.carregarGrupoDaEquipe(confParte1.getIdConfiAgenda());
            this.listaEquipes = eDao.listarEquipePorGrupo(grupoId);
        }
        return listaEquipes;

    }

    public void selectEquipeInsercao() throws ProjetoException {
        EquipeDAO eDao = new EquipeDAO();

        if (confParte2.getGrupo() != null) {
            this.listaEquipes = eDao.listarEquipePorGrupo(confParte2.getGrupo()
                    .getIdGrupo());
        }


    }

    // FINAL EQUIPEBEAN

    public void validarConfiguracoesAgendaProfissional() throws ProjetoException, SQLException {
        int somatorio = 0;
        
        if (!opcaoAtendimento.equals("H")) {
        for (ConfigAgendaParte2Bean conf : listaTipos) {
            somatorio += conf.getQtd();
        }

        if ((confParte1.getQtdMax() != null) && (listaTipos.size() > 0)) {
            if (somatorio > confParte1.getQtdMax()) {
                JSFUtil.adicionarMensagemAdvertencia("Quantidade de tipos de atendimento superior a quantidade máxima!", "Advertência");
                return;
            }
        }
        }
        
        if (confParte1.getOpcao().equals(OpcaoConfiguracaoAgenda.DATA_ESPECIFICA.getSigla())
                && this.confParte1.getDataEspecifica() == null) {
            JSFUtil.adicionarMensagemErro("Escolha uma data específica!", "Erro");
            return;
        }

        if (confParte1.getOpcao().equals(OpcaoConfiguracaoAgenda.DIA_DA_SEMANA.getSigla())
                && this.confParte1.getDiasSemana().size() == 0) {
            JSFUtil.adicionarMensagemErro("Escolha no mínimo um dia da semana!", "Erro");
            return;
        }

        if (DataUtil.retornarHorarioEmTime(confParte1.getHorarioInicio()).after
                (DataUtil.retornarHorarioEmTime(confParte1.getHorarioFinal()))
                ||
                DataUtil.retornarHorarioEmTime(confParte1.getHorarioInicio()).equals(DataUtil.retornarHorarioEmTime(confParte1.getHorarioFinal()))) {
            JSFUtil.adicionarMensagemErro("Horário final precisa ser maior que horário inicial!", "Erro");
            return;
        }

        if ((confParte1.getTipo().equals("E")) && confParte1.getOpcao().equals(OpcaoConfiguracaoAgenda.DIA_DA_SEMANA.getSigla()) && this.confParte1.getAno() == null) {
            JSFUtil.adicionarMensagemErro("Ano: Campo obrigatório!", "Erro");
            return;
        } else {
            if (tipo == 1) {
                gravarConfigAgendaProfissional();
            } else {
                alterarConfigAgendaProfissional();
            }
        }

    }

    public void gravarConfigAgendaProfissional() throws ProjetoException, SQLException {
        boolean cadastrou = false;

        cadastrou = cDao.gravarConfiguracaoAgendaProfissionalInicio(confParte1, listaTipos);


        if (cadastrou) {
            JSFUtil.adicionarMensagemSucesso("Configuração cadastrada com sucesso!", "Sucesso");
        } else {
            JSFUtil.adicionarMensagemErro("Ocorreu um erro durante a gravação!", "Erro");
        }

        limparDados();
        JSFUtil.atualizarComponente("formConfiAgenda");
    }

    public void alterarConfigAgendaProfissional() throws ProjetoException, SQLException {
        boolean alterou = false;

        alterou = cDao.alterarConfiguracaoAgendaProfissionalInicio(confParte1, listaTipos);

        if (alterou) {
            JSFUtil.adicionarMensagemSucesso("Configuração cadastrada com sucesso!", "Sucesso");
        } else {
            JSFUtil.adicionarMensagemErro("Ocorreu um erro durante a alteração!!", "Erro");
        }

    }

    public void validarGravarConfigAgendaEquipe() throws ProjetoException, SQLException {
        int somatorio = 0;
        if (!opcaoAtendimento.equals("H")) {
        for (ConfigAgendaParte2Bean conf : listaTipos) {
            somatorio += conf.getQtd();
        }

        if (confParte1.getQtdMax() != null && listaTipos.size() > 0) {
            if (somatorio > confParte1.getQtdMax()) {
                JSFUtil.adicionarMensagemAdvertencia("Quantidade de tipos de atendimento superior a quantidade máxima!", "Advertência");
                return;
            }
        }
        }
        if (confParte1.getOpcao().equals(OpcaoConfiguracaoAgenda.DATA_ESPECIFICA.getSigla())
                && this.confParte1.getDataEspecifica() == null) {
            JSFUtil.adicionarMensagemErro("Escolha uma data específica!", "Erro");
            return;
        }

        if (confParte1.getOpcao().equals(OpcaoConfiguracaoAgenda.DIA_DA_SEMANA.getSigla())
                && this.confParte1.getDiasSemana().size() == 0) {
            JSFUtil.adicionarMensagemErro("Escolha no mínimo um dia da semana!", "Erro");
            return;
        }

        if (DataUtil.retornarHorarioEmTime(confParte1.getHorarioInicio()).after
                (DataUtil.retornarHorarioEmTime(confParte1.getHorarioFinal()))
                ||
                DataUtil.retornarHorarioEmTime(confParte1.getHorarioInicio()).equals(DataUtil.retornarHorarioEmTime(confParte1.getHorarioFinal()))) {
            JSFUtil.adicionarMensagemErro("Horário final precisa ser maior que horário inicial!", "Erro");
            return;
        }

        if ((confParte1.getTipo().equals("E")) && confParte1.getOpcao().equals(OpcaoConfiguracaoAgenda.DIA_DA_SEMANA.getSigla()) && this.confParte1.getAno() == null) {
            JSFUtil.adicionarMensagemErro("Ano: Campo obrigatório!", "Erro");
            return;
        } else {
            if (tipo == 1) {
                gravarConfigAgendaEquipe();
            } else {
                alterarConfigAgendaEquipe();
            }
        }


    }

    public void gravarConfigAgendaEquipe() throws ProjetoException, SQLException {
        boolean gravou = false;

        gravou = cDao.gravarConfiguracaoAgendaEquipeInicio(confParte1, confParte2, listaTipos, listaConfiguracaoAgendaEquipeEspecialidade);

        if (gravou) {
            JSFUtil.adicionarMensagemSucesso("Configuração cadastrada com sucesso!", "Sucesso");
        } else {
            JSFUtil.adicionarMensagemErro("Ocorreu um erro durante a gravação!", "Erro");
        }

        limparDados();
    }

    public void alterarConfigAgendaEquipe() throws SQLException,
            ProjetoException {
        boolean alterou = false;

        alterou = cDao.alterarConfiguracaoAgendaEquipeInicio(confParte1, confParte2, listaTipos, listaConfiguracaoAgendaEquipeEspecialidade);

        if (alterou) {
            JSFUtil.adicionarMensagemSucesso("Configuração cadastrada com sucesso!", "Sucesso");
        } else {
            JSFUtil.adicionarMensagemErro("Ocorreu um erro durante a alteração!", "Erro");
        }

    }

    public void excluirConfigProfissional() throws ProjetoException {
        boolean excluiu = cDao.excluirConfigProfissional(confParte1);

        if (excluiu == true) {
            JSFUtil.adicionarMensagemSucesso("Configuração excluída com sucesso!", "Sucesso");
            JSFUtil.fecharDialog("dialogExclusao");
        } else {
            JSFUtil.adicionarMensagemErro("Ocorreu um erro durante a exclusão!", "Erro");
            JSFUtil.fecharDialog("dialogExclusao");
        }
        this.listaHorariosProfissional = cDao.listarHorariosComFiltrosProfissional(confParte1,
                prof.getId());
    }

    public void excluirConfigEquipe() throws ProjetoException {
        Boolean excluiu = cDao.excluirConfigEquipe(confParte1);

        if (excluiu == true) {
            JSFUtil.adicionarMensagemSucesso("Configuração excluída com sucesso!", "Sucesso");
            JSFUtil.fecharDialog("dialogExclusao");
        } else {
            JSFUtil.adicionarMensagemErro("Ocorreu um erro durante a exclusão!", "Erro");
            JSFUtil.fecharDialog("dialogExclusao");
        }
        this.listaHorariosEquipe = cDao.listarHorariosEquipe();
    }

    public void selectProfissional() throws ProjetoException {
        this.listaHorariosProfissional = cDao.listarHorariosPorIDProfissional(prof
                .getId());
    }

    public void selectEquipeOnRow() throws ProjetoException {
        this.listaHorariosEquipe = cDao.listarHorariosPorIDEquipe(equipe
                .getCodEquipe());
    }

    public void selectProfissionalComFiltros() throws ProjetoException {
        this.listaHorariosProfissional = cDao.listarHorariosComFiltrosProfissional(confParte1,
                prof.getId());
    }

    public void selectEquipeComFiltros() throws ProjetoException {
        this.listaHorariosEquipe = cDao.listarHorariosComFiltrosEquipe(
                confParte1, equipe.getCodEquipe());
    }

    public void limparBuscaPrograma() {
        this.confParte2.setGrupo(null);
        this.confParte2.setTipoAt(null);
    }

    public void limparBuscaGrupo() {
        this.confParte2.setTipoAt(null);
    }

    public ConfigAgendaParte1Bean getConfParte1() {
        return confParte1;
    }

    public void setConfParte1(ConfigAgendaParte1Bean confParte1) {
        this.confParte1 = confParte1;
    }

    public ConfigAgendaParte2Bean getConfParte2() {
        return confParte2;
    }

    public void setConfParte2(ConfigAgendaParte2Bean confParte2) {
        this.confParte2 = confParte2;
    }

    public List<ConfigAgendaParte2Bean> getListaTipos() {
        return listaTipos;
    }

    public void setListaTipos(List<ConfigAgendaParte2Bean> listaTipos) {
        this.listaTipos = listaTipos;
    }

    public List<ConfigAgendaParte1Bean> getListaHorariosProfissional()
            throws ProjetoException {
        if (listaHorariosProfissional == null) {
            return listaHorariosProfissional;
        } else {
            if (this.confParte1.getProfissional() == null) {
            }
            if (this.confParte1.getProfissional().getId() != null) {
                this.listaHorariosProfissional = cDao
                        .listarHorariosPorIDProfissional(this.confParte1
                                .getProfissional().getId());
            }
        }
        return listaHorariosProfissional;
    }

    public void setListaHorariosProfissional(List<ConfigAgendaParte1Bean> listaHorariosProfissional) {
        this.listaHorariosProfissional = listaHorariosProfissional;
    }

    public List<ConfigAgendaParte1Bean> getListaHorariosEquipe()
            throws ProjetoException {
        if (listaHorariosEquipe == null) {
            return listaHorariosEquipe;
        } else {
            if (this.confParte1.getEquipe().getCodEquipe() != null) {
                this.listaHorariosEquipe = cDao
                        .listarHorariosPorIDEquipe(this.confParte1.getEquipe()
                                .getCodEquipe());
            }
        }
        return listaHorariosEquipe;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public void setListaHorariosEquipe(
            List<ConfigAgendaParte1Bean> listaHorariosEquipe) {
        this.listaHorariosEquipe = listaHorariosEquipe;
    }

    public List<FuncionarioBean> getListaProfissionais()
            throws ProjetoException {
        if (this.listaProfissionais == null) {
            this.listaProfissionais = fDao.listarProfissionalAtendimento();
        }
        return listaProfissionais;
    }

    public void setListaProfissionais(List<FuncionarioBean> listaProfissionais) {
        this.listaProfissionais = listaProfissionais;
    }

    public void carregaListaEquipes() throws ProjetoException {
        this.listaEquipes = eDao.listarEquipe();

    }

    private void carregarListaConfiguracaoProfissionalGeral() throws ProjetoException {
        listaProfissionalConfiguracaoGeral = cDao.listarHorariosPorProfissionalGeral();
    }

    private void carregarListaConfiguracaoProfissionalEspecifica() throws ProjetoException {
        listaProfissionalConfiguracaoEspecifica = cDao.listarHorariosPorProfissionalEspecifica();
    }

    private void carregarListaConfiguracaoEquipeGeral() throws ProjetoException {
        listaEquipeConfiguracaoGeral = cDao.listarHorariosPorEquipeGeral();
    }

    private void carregarListaConfiguracaoEquipeEspecificaa() throws ProjetoException {
        listaEquipeConfiguracaoEspecifica = cDao.listarHorariosPorEquipeEspecifica();
    }

    public List<EquipeBean> getListaEquipes() {
        return listaEquipes;
    }

    public void setListaEquipes(List<EquipeBean> listaEquipes) {
        this.listaEquipes = listaEquipes;
    }

    public FuncionarioBean getProf() {
        return prof;
    }

    public void setProf(FuncionarioBean prof) {
        this.prof = prof;
    }

    public EquipeBean getEquipe() {
        return equipe;
    }

    public void setEquipe(EquipeBean equipe) {
        this.equipe = equipe;
    }

    public List<GrupoBean> getListaGruposProgramas() {
        return listaGruposProgramas;
    }

    public void setListaGruposProgramas(List<GrupoBean> listaGruposProgramas) {
        this.listaGruposProgramas = listaGruposProgramas;
    }

    public List<TipoAtendimentoBean> getListaTipoAtendimentosGrupo() {
        return listaTipoAtendimentosGrupo;
    }

    public void setListaTipoAtendimentosGrupo(List<TipoAtendimentoBean> listaTipoAtendimentosGrupo) {
        this.listaTipoAtendimentosGrupo = listaTipoAtendimentosGrupo;
    }

    public List<ConfigAgendaParte1Bean> getListaProfissionalConfiguracaoGeral() {
        return listaProfissionalConfiguracaoGeral;
    }

    public void setListaProfissionalConfiguracaoGeral(List<ConfigAgendaParte1Bean> listaProfissionalConfiguracaoGeral) {
        this.listaProfissionalConfiguracaoGeral = listaProfissionalConfiguracaoGeral;
    }

    public List<ConfigAgendaParte1Bean> getListaProfissionalConfiguracaoEspecifica() {
        return listaProfissionalConfiguracaoEspecifica;
    }

    public void setListaProfissionalConfiguracaoEspecifica(List<ConfigAgendaParte1Bean> listaProfissionalConfiguracaoEspecifica) {
        this.listaProfissionalConfiguracaoEspecifica = listaProfissionalConfiguracaoEspecifica;
    }

    public List<ConfigAgendaParte1Bean> getListaEquipeConfiguracaoGeral() {
        return listaEquipeConfiguracaoGeral;
    }

    public void setListaEquipeConfiguracaoGeral(List<ConfigAgendaParte1Bean> listaEquipeConfiguracaoGeral) {
        this.listaEquipeConfiguracaoGeral = listaEquipeConfiguracaoGeral;
    }

    public List<ConfigAgendaParte1Bean> getListaEquipeConfiguracaoEspecifica() {
        return listaEquipeConfiguracaoEspecifica;
    }

    public void setListaEquipeConfiguracaoEspecifica(List<ConfigAgendaParte1Bean> listaEquipeConfiguracaoEspecifica) {
        this.listaEquipeConfiguracaoEspecifica = listaEquipeConfiguracaoEspecifica;
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

    public List<EspecialidadeBean> getListaEspecialidades() {
        return listaEspecialidades;
    }

    public void setListaEspecialidades(List<EspecialidadeBean> listaEspecialidades) {
        this.listaEspecialidades = listaEspecialidades;
    }

    public List<ConfiguracaoAgendaEquipeEspecialidadeDTO> getListaConfiguracaoAgendaEquipeEspecialidade() {
        return listaConfiguracaoAgendaEquipeEspecialidade;
    }

    public void setListaConfiguracaoAgendaEquipeEspecialidade(List<ConfiguracaoAgendaEquipeEspecialidadeDTO> listaConfiguracaoAgendaEquipeEspecialidade) {
        this.listaConfiguracaoAgendaEquipeEspecialidade = listaConfiguracaoAgendaEquipeEspecialidade;
    }

    public ConfiguracaoAgendaEquipeEspecialidadeDTO getConfiguracaoAgendaEquipeEspecialidadeDTO() {
        return configuracaoAgendaEquipeEspecialidadeDTO;
    }

    public void setConfiguracaoAgendaEquipeEspecialidadeDTO(ConfiguracaoAgendaEquipeEspecialidadeDTO configuracaoAgendaEquipeEspecialidadeDTO) {
        this.configuracaoAgendaEquipeEspecialidadeDTO = configuracaoAgendaEquipeEspecialidadeDTO;
    }
}
