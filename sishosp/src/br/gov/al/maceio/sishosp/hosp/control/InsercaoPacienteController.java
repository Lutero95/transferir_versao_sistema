package br.gov.al.maceio.sishosp.hosp.control;

import java.io.Serializable;
import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import br.gov.al.maceio.sishosp.comum.util.DataUtil;
import br.gov.al.maceio.sishosp.comum.util.JSFUtil;

import br.gov.al.maceio.sishosp.acl.dao.FuncionarioDAO;
import br.gov.al.maceio.sishosp.acl.model.FuncionarioBean;
import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.hosp.dao.*;
import br.gov.al.maceio.sishosp.hosp.enums.TipoAtendimento;
import br.gov.al.maceio.sishosp.hosp.model.*;
import org.primefaces.extensions.util.DateUtils;

@ManagedBean(name = "InsercaoController")
@ViewScoped
public class InsercaoPacienteController implements Serializable {

    private static final long serialVersionUID = 1L;
    private InsercaoPacienteBean insercao;
    private InsercaoPacienteDAO iDao = new InsercaoPacienteDAO();
    private EquipeDAO eDao = new EquipeDAO();
    private ArrayList<InsercaoPacienteBean> listaLaudosVigentes;
    private ArrayList<FuncionarioBean> listaProfissionaisEquipe;
    private ArrayList<FuncionarioBean> listaProfissionaisAdicionados;
    private String tipo;
    private FuncionarioBean funcionario;
    private ArrayList<InsercaoPacienteBean> listAgendamentoProfissional;
    private String opcaoAtendimento;
    private EmpresaDAO empresaDAO = new EmpresaDAO();
    private List<String> listaHorarios = new ArrayList<>();

    public InsercaoPacienteController() throws ProjetoException {
        this.insercao = new InsercaoPacienteBean();
        listaLaudosVigentes = new ArrayList<InsercaoPacienteBean>();
        this.tipo = TipoAtendimento.EQUIPE.getSigla();
        funcionario = new FuncionarioBean();
        listaProfissionaisAdicionados = new ArrayList<FuncionarioBean>();
        listAgendamentoProfissional = new ArrayList<InsercaoPacienteBean>();
        opcaoAtendimento = empresaDAO.carregarOpcaoAtendimentoDaEmpresa();
        insercao.setOpcaoAtendimento(!opcaoAtendimento.equals("A") ? opcaoAtendimento : "T");
        listaHorarios = new ArrayList<>();
    }

    public void limparDados() {
        this.insercao = new InsercaoPacienteBean();
    }

    public void limparDias() {
        funcionario.setListDiasSemana(null);
    }

    public ArrayList<InsercaoPacienteBean> listarLaudosVigentes()
            throws ProjetoException {
        return listaLaudosVigentes = iDao.listarLaudosVigentes();
    }

    public void carregarLaudoPaciente() throws ProjetoException {
        int id = insercao.getLaudo().getId();
        limparDados();
        insercao = iDao.carregarLaudoPaciente(id);

    }

    // VALIDAÇÃO DE NÃO REPETIR O PROFISSIONAL
    public void validarAdicionarFuncionario() {
        Boolean existe = false;
        if (listaProfissionaisAdicionados.size() == 0) {
            adicionarFuncionario();
        } else {

            for (int i = 0; i < listaProfissionaisAdicionados.size(); i++) {
                if (listaProfissionaisAdicionados.get(i).getId() == funcionario
                        .getId()) {
                    existe = true;
                }
            }
            if (existe == false) {
                adicionarFuncionario();
            } else {
                JSFUtil.fecharDialog("dlgDiasAtendimento");

                JSFUtil.adicionarMensagemSucesso("Esse profissional já foi adicionado!", "Sucesso");
            }

        }
    }

    public void adicionarFuncionario() {
        String dias = "";

        for (int i = 0; i < funcionario.getListDiasSemana().size(); i++) {
            if (funcionario.getListDiasSemana().get(i).equals("1")) {
                dias = dias + "Domingo";
            }
            if (funcionario.getListDiasSemana().get(i).equals("2")) {
                dias = dias + ", Segunda";
            }
            if (funcionario.getListDiasSemana().get(i).equals("3")) {
                dias = dias + ", Terça";
            }
            if (funcionario.getListDiasSemana().get(i).equals("4")) {
                dias = dias + ", Quarta";
            }
            if (funcionario.getListDiasSemana().get(i).equals("5")) {
                dias = dias + ", Quinta";
            }
            if (funcionario.getListDiasSemana().get(i).equals("6")) {
                dias = dias + ", Sexta";
            }
            if (funcionario.getListDiasSemana().get(i).equals("7")) {
                dias = dias + ", Sábado";
            }
        }
        funcionario.setDiasSemana(dias);
        listaProfissionaisAdicionados.add(funcionario);

        JSFUtil.fecharDialog("dlgDiasAtendimento");
    }

    public void gerarListaAgendamentosProfissional() throws ProjetoException {

        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        df.setLenient(false);
        Date d1 = insercao.getData_solicitacao();
        Date d2 = iDao.dataFinalLaudo(insercao.getLaudo().getId());
        Long dt = (d2.getTime() - d1.getTime());

        dt = (dt / 86400000L);

        Calendar c = Calendar.getInstance();
        c.setTime(insercao.getData_solicitacao());

        for (int i = 0; i < dt; i++) {

            if (i > 0) {
                c.add(Calendar.DAY_OF_MONTH, 1);
            }

            int diaSemana = c.get(Calendar.DAY_OF_WEEK);

            if (tipo.equals(TipoAtendimento.PROFISSIONAL.getSigla())) {
                for (int j = 0; j < insercao.getFuncionario()
                        .getListDiasSemana().size(); j++) {

                    if (diaSemana == Integer.parseInt(insercao.getFuncionario()
                            .getListDiasSemana().get(j))) {

                        InsercaoPacienteBean ins = new InsercaoPacienteBean();

                        ins.getAgenda().setPaciente(
                                insercao.getLaudo().getPaciente());

                        ins.getAgenda().setDataMarcacao(c.getTime());

                        listAgendamentoProfissional.add(ins);

                    }
                }

            }

        }

    }

    public void gerarListaAgendamentosEquipe() throws ProjetoException {

        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        df.setLenient(false);
        Date d1 = insercao.getData_solicitacao();
        Date d2 = iDao.dataFinalLaudo(insercao.getLaudo().getId());
        Long dt = (d2.getTime() - d1.getTime());

        dt = (dt / 86400000L);

        Calendar c = Calendar.getInstance();
        c.setTime(insercao.getData_solicitacao());

        for (int i = 0; i < dt; i++) {

            if (i > 0) {
                c.add(Calendar.DAY_OF_MONTH, 1);
            }

            int diaSemana = c.get(Calendar.DAY_OF_WEEK);

            if (tipo.equals(TipoAtendimento.EQUIPE.getSigla())) {
                for (int j = 0; j < listaProfissionaisAdicionados.size(); j++) {
                    for (int h = 0; h < listaProfissionaisAdicionados.get(j).getListDiasSemana().size(); h++) {

                        if (diaSemana == Integer.parseInt(listaProfissionaisAdicionados.get(j).getListDiasSemana().get(h))) {

                            InsercaoPacienteBean ins = new InsercaoPacienteBean();

                            ins.getAgenda().setPaciente(
                                    insercao.getLaudo().getPaciente());

                            ins.getAgenda().setDataMarcacao(c.getTime());

                            ins.getAgenda().setProfissional(listaProfissionaisAdicionados.get(j));

                            listAgendamentoProfissional.add(ins);

                        }
                    }
                }

            }

        }

    }

    public void gravarInsercaoPaciente() throws ProjetoException {

        if (insercao.getLaudo().getId() != null) {

            Boolean cadastrou = null;
            if (tipo.equals(TipoAtendimento.EQUIPE.getSigla())) {

                gerarListaAgendamentosEquipe();

                cadastrou = iDao.gravarInsercaoEquipe(insercao,
                        listaProfissionaisAdicionados, listAgendamentoProfissional);
            }
            if (tipo.equals(TipoAtendimento.PROFISSIONAL.getSigla())) {

                gerarListaAgendamentosProfissional();

                cadastrou = iDao.gravarInsercaoProfissional(insercao,
                        listAgendamentoProfissional);
            }

            if (cadastrou == true) {
                limparDados();

                JSFUtil.adicionarMensagemSucesso("Inserção de Equipe cadastrada com sucesso!", "Sucesso");

            } else {

                JSFUtil.adicionarMensagemErro("Ocorreu um erro durante o cadastro!", "Erro");

            }
        } else {

            JSFUtil.adicionarMensagemAdvertencia("Carregue um laudo primeiro!", "Bloqueio");

        }
    }

    public List<String> gerarHorariosAtendimento() throws ParseException {
        ParametroBean parametroBean = new ParametroBean();
        EmpresaDAO empresaDAO = new EmpresaDAO();

        parametroBean = empresaDAO.carregarDetalhesAtendimentoDaEmpresa();

        java.sql.Time novahora = (Time) parametroBean.getHorarioInicial();
        String horario = null;

        while(parametroBean.getHorarioFinal().after(novahora)) {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

            GregorianCalendar gc = new GregorianCalendar();
            String timeAux = sdf.format(novahora);

            gc.setTime(sdf.parse(timeAux));

            gc.add(Calendar.MINUTE, parametroBean.getIntervalo());

            novahora = new java.sql.Time(gc.getTime().getTime());

            horario = DataUtil.retornarHorarioEmString(novahora);

            listaHorarios.add(horario);

        }

        return listaHorarios;
    }

    // AUTOCOMPLETE INÍCIO

    public List<EquipeBean> listaEquipeAutoComplete(String query)
            throws ProjetoException {

        List<EquipeBean> result = eDao.listarEquipePorGrupoAutoComplete(query,
                insercao.getGrupo().getIdGrupo());
        return result;
    }

    public void listarProfissionaisEquipe() throws ProjetoException {
        if (insercao.getLaudo().getId() != null) {
            if (insercao.getEquipe() != null) {
                if (insercao.getEquipe().getCodEquipe() != null) {
                    listaProfissionaisEquipe = eDao
                            .listarProfissionaisDaEquipe(insercao.getEquipe()
                                    .getCodEquipe());
                }
            } else {
                JSFUtil.adicionarMensagemErro("Escolha uma equipe!",
                        "Bloqueio");
            }
        } else {
            JSFUtil.adicionarMensagemErro("Carregue um laudo primeiro!", "Bloqueio");

        }
    }

    public List<GrupoBean> listaGrupoAutoComplete(String query)
            throws ProjetoException {

        GrupoDAO gDao = new GrupoDAO();

        if (insercao.getPrograma().getIdPrograma() != null) {
            return gDao.listarGruposAutoComplete(query,
                    this.insercao.getPrograma());
        } else {
            return null;
        }

    }

    public List<FuncionarioBean> listaProfissionalAutoComplete(String query)
            throws ProjetoException {
        FuncionarioDAO fDao = new FuncionarioDAO();
        List<FuncionarioBean> result = fDao.listarProfissionalBusca(query, 1);
        return result;
    }

    public List<TipoAtendimentoBean> listaTipoAtAutoComplete(String query)
            throws ProjetoException {
        TipoAtendimentoDAO tDao = new TipoAtendimentoDAO();
        return tDao.listarTipoAtBusca(query, 1);

    }

    // AUTOCOMPLETE FIM

    public InsercaoPacienteBean getInsercao() {
        return insercao;
    }

    public void setInsercao(InsercaoPacienteBean insercao) {
        this.insercao = insercao;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public ArrayList<InsercaoPacienteBean> getListaLaudosVigentes() {
        return listaLaudosVigentes;
    }

    public void setListaLaudosVigentes(
            ArrayList<InsercaoPacienteBean> listaLaudosVigentes) {
        this.listaLaudosVigentes = listaLaudosVigentes;
    }

    public ArrayList<FuncionarioBean> getListaProfissionaisEquipe() {
        return listaProfissionaisEquipe;
    }

    public void setListaProfissionaisEquipe(
            ArrayList<FuncionarioBean> listaProfissionaisEquipe) {
        this.listaProfissionaisEquipe = listaProfissionaisEquipe;
    }

    public FuncionarioBean getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(FuncionarioBean funcionario) {
        this.funcionario = funcionario;
    }

    public ArrayList<FuncionarioBean> getListaProfissionaisAdicionados() {
        return listaProfissionaisAdicionados;
    }

    public void setListaProfissionaisAdicionados(
            ArrayList<FuncionarioBean> listaProfissionaisAdicionados) {
        this.listaProfissionaisAdicionados = listaProfissionaisAdicionados;
    }

    public String getOpcaoAtendimento() {
        return opcaoAtendimento;
    }

    public void setOpcaoAtendimento(String opcaoAtendimento) {
        this.opcaoAtendimento = opcaoAtendimento;
    }

    public List<String> getListaHorarios() {
        return listaHorarios;
    }

    public void setListaHorarios(List<String> listaHorarios) {
        this.listaHorarios = listaHorarios;
    }
}
