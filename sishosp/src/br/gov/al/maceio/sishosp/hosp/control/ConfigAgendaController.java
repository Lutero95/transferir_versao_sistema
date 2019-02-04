package br.gov.al.maceio.sishosp.hosp.control;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import br.gov.al.maceio.sishosp.comum.util.JSFUtil;
import br.gov.al.maceio.sishosp.comum.util.RedirecionarUtil;

import br.gov.al.maceio.sishosp.acl.dao.FuncionarioDAO;
import br.gov.al.maceio.sishosp.acl.model.FuncionarioBean;
import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.hosp.dao.ConfigAgendaDAO;
import br.gov.al.maceio.sishosp.hosp.dao.EquipeDAO;
import br.gov.al.maceio.sishosp.hosp.dao.GrupoDAO;
import br.gov.al.maceio.sishosp.hosp.dao.TipoAtendimentoDAO;
import br.gov.al.maceio.sishosp.hosp.enums.OpcaoConfiguracaoAgenda;
import br.gov.al.maceio.sishosp.hosp.model.ConfigAgendaParte1Bean;
import br.gov.al.maceio.sishosp.hosp.model.ConfigAgendaParte2Bean;
import br.gov.al.maceio.sishosp.hosp.model.EquipeBean;
import br.gov.al.maceio.sishosp.hosp.model.GrupoBean;
import br.gov.al.maceio.sishosp.hosp.model.TipoAtendimentoBean;

@ManagedBean(name = "ConfigAgendaController")
@ViewScoped
public class ConfigAgendaController implements Serializable {

    private static final long serialVersionUID = 1L;

    private ConfigAgendaParte1Bean confParte1;
    private ConfigAgendaParte2Bean confParte2;
    private FuncionarioBean prof;
    private EquipeBean equipe;
    private int tipo;

    private List<ConfigAgendaParte2Bean> listaTipos;
    private List<ConfigAgendaParte2Bean> listaTiposEditar;
    private List<ConfigAgendaParte1Bean> listaHorarios;
    private List<ConfigAgendaParte1Bean> listaHorariosEquipe;
    private List<FuncionarioBean> listaProfissionais;
    private List<EquipeBean> listaEquipes;

    private List<GrupoBean> listaGruposProgramas;
    private List<TipoAtendimentoBean> listaTipoAtendimentosGrupo;

    private ConfigAgendaDAO cDao = new ConfigAgendaDAO();
    private FuncionarioDAO fDao = new FuncionarioDAO();
    private EquipeDAO eDao = new EquipeDAO();

    //CONSTANTES
    private static final String ENDERECO_CADASTRO = "configuracaoAgenda?faces-redirect=true";
    private static final String ENDERECO_CADASTRO_EQUIPE = "configuracaoAgendaEquipe?faces-redirect=true";
    private static final String ENDERECO_EDITAR_EQUIPE = "editarConfAgendaEquipe?faces-redirect=true";
    private static final String ENDERECO_TIPO = "&amp;tipo=";
    private static final String ENDERECO_ID = "&amp;codconfigagenda=";

    public ConfigAgendaController() {
        this.confParte1 = new ConfigAgendaParte1Bean();
        this.confParte2 = new ConfigAgendaParte2Bean();
        this.prof = new FuncionarioBean();
        this.equipe = new EquipeBean();
        this.listaTipos = new ArrayList<ConfigAgendaParte2Bean>();
        this.listaTiposEditar = new ArrayList<ConfigAgendaParte2Bean>();
        this.listaHorarios = null;
        this.listaHorariosEquipe = null;
        this.listaProfissionais = null;
        this.listaEquipes = null;
        this.listaGruposProgramas = new ArrayList<>();
        this.listaTipoAtendimentosGrupo = new ArrayList<>();

    }

    public void limparDados() {
        this.confParte1 = new ConfigAgendaParte1Bean();
        this.confParte2 = new ConfigAgendaParte2Bean();
        this.listaTipos = new ArrayList<ConfigAgendaParte2Bean>();
        this.listaTiposEditar = new ArrayList<ConfigAgendaParte2Bean>();
        this.listaHorarios = null;
        this.listaHorariosEquipe = null;
        this.listaProfissionais = null;
        this.listaEquipes = null;
    }

    public String redirectEdit() {
        return RedirecionarUtil.redirectEdit(ENDERECO_CADASTRO, ENDERECO_ID, this.confParte1.getIdConfiAgenda(), ENDERECO_TIPO, tipo);
    }

    public String redirectInsert() {
        return RedirecionarUtil.redirectInsert(ENDERECO_CADASTRO, ENDERECO_TIPO, tipo);
    }

    public String redirectInsertEquipe() {
        return RedirecionarUtil.redirectPagina(ENDERECO_CADASTRO_EQUIPE);
    }

    public String redirectEditEquipe() {
        return RedirecionarUtil.redirectEdit(ENDERECO_EDITAR_EQUIPE, ENDERECO_ID, this.confParte1.getIdConfiAgenda(), ENDERECO_TIPO, tipo);
    }

    public void getEditAgenda() throws ProjetoException {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Map<String, String> params = facesContext.getExternalContext()
                .getRequestParameterMap();
        if (params.get("codconfigagenda") != null) {
            Integer id = Integer.parseInt(params.get("codconfigagenda"));
            tipo = Integer.parseInt(params.get("tipo"));

            this.confParte1 = cDao.listarHorariosPorIDProfissionalEdit(id);
        } else {
            tipo = Integer.parseInt(params.get("tipo"));

        }

    }

    public void getEditAgendaEquipe() throws ProjetoException {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Map<String, String> params = facesContext.getExternalContext()
                .getRequestParameterMap();
        if (params.get("codconfigagenda") != null) {
            Integer id = Integer.parseInt(params.get("codconfigagenda"));
            tipo = Integer.parseInt(params.get("tipo"));

            this.confParte1 = cDao.listarHorariosPorIDEquipeEdit(id);
        } else {
            tipo = Integer.parseInt(params.get("tipo"));

        }

    }

    public void addNaLista() {
        if (confParte2.getQtd() == null) {
            JSFUtil.adicionarMensagemErro("Insira a quantidade!", "Erro");
        } else if (confParte2.getPrograma() == null) {
            JSFUtil.adicionarMensagemErro("Insira o programa!", "Erro");
        } else if (confParte2.getGrupo() == null) {
            JSFUtil.adicionarMensagemErro("Insira o grupo!", "Erro");
        } else {
            this.listaTipos.add(confParte2);
            JSFUtil.adicionarMensagemSucesso("Dados inseridos na tabela!", "Sucesso");
        }
        this.confParte2 = new ConfigAgendaParte2Bean();
    }

    public void addNaListaEditar() {
        if (confParte2.getQtd() == null
                || confParte2.getPrograma().getDescPrograma() == null
                || confParte2.getGrupo().getDescGrupo() == null) {
            this.confParte2 = new ConfigAgendaParte2Bean();
            JSFUtil.adicionarMensagemErro("Insira os dados corretamente!", "Erro");
        } else {
            this.listaTiposEditar.add(confParte2);
            JSFUtil.adicionarMensagemSucesso("Dados inseridos na tabela!", "Sucesso");
        }
        this.confParte2 = new ConfigAgendaParte2Bean();
    }

    public void removeNaLista() {
        this.listaTipos.remove(confParte2);

    }

    public void removeNaListaEditar() {
        this.listaTiposEditar.remove(confParte2);

    }

    // GRUPOBEAN

    public List<GrupoBean> selectGrupo() throws ProjetoException {
        GrupoDAO gDao = new GrupoDAO();
        if (confParte2.getPrograma() != null) {
            listaGruposProgramas = gDao.listarGruposPorPrograma(confParte2
                    .getPrograma().getIdPrograma());
        }
        return listaGruposProgramas;
    }

    public List<GrupoBean> listaGrupoAutoComplete(String query)
            throws ProjetoException {
        GrupoDAO gDao = new GrupoDAO();
        if (confParte2.getPrograma() != null) {
            return gDao.listarGruposAutoComplete(query,
                    confParte2.getPrograma());
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
            return tDao.listarTipoAtAutoComplete(query, confParte2.getGrupo());
        } else {
            return null;
        }
    }

    public List<TipoAtendimentoBean> selectTipoAtendimento()
            throws ProjetoException {
        TipoAtendimentoDAO tDao = new TipoAtendimentoDAO();
        if (confParte2.getGrupo() != null) {
            listaTipoAtendimentosGrupo = tDao.listarTipoAtPorGrupo(confParte2
                    .getGrupo().getIdGrupo());
        }
        return listaTipoAtendimentosGrupo;
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

    public List<EquipeBean> selectEquipeInsercao() throws ProjetoException {
        EquipeDAO eDao = new EquipeDAO();

        if (confParte2.getGrupo() != null) {
            this.listaEquipes = eDao.listarEquipePorGrupo(confParte2.getGrupo()
                    .getIdGrupo());
        }

        return listaEquipes;

    }

    // FINAL EQUIPEBEAN

    public void gravarConfigAgenda() throws ProjetoException, SQLException {
        boolean cadastrou = false;
        int somatorio = 0;
        for (ConfigAgendaParte2Bean conf : listaTipos) {
            somatorio += conf.getQtd();
        }

        if (confParte1.getQtdMax() != null && listaTipos.size() > 0) {
            if (somatorio != confParte1.getQtdMax()) {
                JSFUtil.adicionarMensagemAdvertencia("Quantidade máxima está divergente!", "Advertência");
                cadastrou = false;
                return;
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

        if (confParte1.getOpcao().equals(OpcaoConfiguracaoAgenda.DIA_DA_SEMANA.getSigla()) && this.confParte1.getAno() == null) {
            JSFUtil.adicionarMensagemErro("Ano: Campo obrigatório!", "Erro");
            return;
        }


        cadastrou = cDao.gravarConfiguracaoAgendaProfissionalInicio(confParte1, listaTipos);


        if (cadastrou) {
            JSFUtil.adicionarMensagemSucesso("Configuração cadastrada com sucesso!", "Sucesso");
        } else {
            JSFUtil.adicionarMensagemErro("Insira os dados corretamente!", "Erro");
        }

        limparDados();
        JSFUtil.atualizarComponente("formConfiAgenda");
    }

    public void gravarConfigAgendaEquipe() {
        boolean gravou = false;

        if (confParte1.getOpcao().equals(OpcaoConfiguracaoAgenda.DATA_ESPECIFICA.getSigla())) {
            this.confParte1.setAno(0);
            this.confParte1.setMes(0);
        }

        gravou = cDao.gravarConfigAgendaEquipe(confParte1, confParte2);

        if (gravou) {
            JSFUtil.adicionarMensagemSucesso("Configuração cadastrada com sucesso!", "Sucesso");
        } else {
            JSFUtil.adicionarMensagemErro("Insira os dados corretamente!", "Erro");
        }

        limparDados();
    }

    public void alterarConfigAgenda() {
        boolean alterou = false;

        //SEM USO POR ENQUANTO
        /*
        int somatorio = 0;

        for (ConfigAgendaParte2Bean conf : listaTiposEditar) {
            somatorio += conf.getQtd();
        }


        if (confParte1.getQtdMax() != null) {
            if (somatorio != confParte1.getQtdMax()) {
                JSFUtil.adicionarMensagemAdvertencia("Quantidade máxima está divergente!", "Advertência");
                alterou = false;
                return;
            }
        }
        */

        alterou = cDao.alterarTurno(confParte1, listaTiposEditar);

        if (alterou) {
            JSFUtil.adicionarMensagemSucesso("Configuração cadastrada com sucesso!", "Sucesso");
        } else {
            JSFUtil.adicionarMensagemErro("Insira os dados corretamente!", "Erro");
        }

    }

    public void alterarConfigAgendaEquipe() throws SQLException,
            ProjetoException {
        boolean alterou = false;

        alterou = cDao.alterarTurnoEquipe(confParte1);

        if (alterou) {
            JSFUtil.adicionarMensagemSucesso("Configuração cadastrada com sucesso!", "Sucesso");
        } else {
            JSFUtil.adicionarMensagemErro("Insira os dados corretamente!", "Erro");
        }

    }

    public void excluirConfig() throws ProjetoException {
        boolean excluiu = cDao.excluirConfig(confParte1);

        if (excluiu == true) {
            JSFUtil.adicionarMensagemSucesso("Configuração excluída com sucesso!", "Sucesso");
            JSFUtil.fecharDialog("dialogExclusao");
        } else {
            JSFUtil.adicionarMensagemErro("Ocorreu um erro durante a exclusão!", "Erro");
            JSFUtil.fecharDialog("dialogExclusao");
        }
        this.listaHorarios = cDao.listarHorariosComFiltros(confParte1,
                prof.getId());
    }

    public void excluirConfigEquipe() throws ProjetoException {
        boolean excluiu = cDao.excluirConfigEquipe(confParte1);

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
        this.listaHorarios = cDao.listarHorariosPorIDProfissional(prof
                .getId());
    }

    public void selectEquipeOnRow() throws ProjetoException {
        this.listaHorariosEquipe = cDao.listarHorariosPorIDEquipe(equipe
                .getCodEquipe());
    }

    public void selectProfissionalComFiltros() throws ProjetoException {
        this.listaHorarios = cDao.listarHorariosComFiltros(confParte1,
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

    public List<ConfigAgendaParte1Bean> getListaHorarios()
            throws ProjetoException {
        if (listaHorarios == null) {
            return listaHorarios;
        } else {
            if (this.confParte1.getProfissional() == null) {
            }
            if (this.confParte1.getProfissional().getId() != null) {
                this.listaHorarios = cDao
                        .listarHorariosPorIDProfissional(this.confParte1
                                .getProfissional().getId());
            }
        }
        return listaHorarios;
    }

    public void setListaHorarios(List<ConfigAgendaParte1Bean> listaHorarios) {
        this.listaHorarios = listaHorarios;
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
            this.listaProfissionais = fDao.listarProfissional();
        }
        return listaProfissionais;
    }

    public void setListaProfissionais(List<FuncionarioBean> listaProfissionais) {
        this.listaProfissionais = listaProfissionais;
    }

    public List<EquipeBean> getListaEquipes() throws ProjetoException {
        if (this.listaEquipes == null) {
            this.listaEquipes = eDao.listarEquipe();
        }
        return listaEquipes;
    }

    public void setListaEquipes(List<EquipeBean> listaEquipes) {
        this.listaEquipes = listaEquipes;
    }

    public List<ConfigAgendaParte2Bean> getListaTiposEditar() {
        return listaTiposEditar;
    }

    public void setListaTiposEditar(
            List<ConfigAgendaParte2Bean> listaTiposEditar) {
        this.listaTiposEditar = listaTiposEditar;
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

}
