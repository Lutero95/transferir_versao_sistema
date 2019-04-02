package br.gov.al.maceio.sishosp.hosp.control;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import br.gov.al.maceio.sishosp.comum.util.JSFUtil;
import br.gov.al.maceio.sishosp.hosp.dao.*;
import br.gov.al.maceio.sishosp.hosp.enums.DiasDaSemana;
import br.gov.al.maceio.sishosp.hosp.enums.OpcaoAtendimento;
import br.gov.al.maceio.sishosp.hosp.enums.TipoAtendimento;

import br.gov.al.maceio.sishosp.acl.dao.FuncionarioDAO;
import br.gov.al.maceio.sishosp.acl.model.FuncionarioBean;
import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.hosp.model.AgendaBean;
import br.gov.al.maceio.sishosp.hosp.model.EquipeBean;
import br.gov.al.maceio.sishosp.hosp.model.GerenciarPacienteBean;
import br.gov.al.maceio.sishosp.hosp.model.InsercaoPacienteBean;

@ManagedBean(name = "AlteracaoPacienteController")
@ViewScoped
public class AlteracaoPacienteController implements Serializable {

    private static final long serialVersionUID = 1L;
    private AlteracaoPacienteDAO aDao = new AlteracaoPacienteDAO();
    private InsercaoPacienteBean insercao;
    private InsercaoPacienteBean insercaoParaLaudo;
    private String tipo;
    private InsercaoPacienteDAO iDao;
    private ArrayList<FuncionarioBean> listaProfissionaisAdicionados;
    private ArrayList<GerenciarPacienteBean> listaDiasProfissional;
    private ArrayList<InsercaoPacienteBean> listAgendamentoProfissional;
    private Integer id_paciente_insituicao;
    private String opcaoAtendimento;
    private ArrayList<FuncionarioBean> listaProfissionaisEquipe;
    private EquipeDAO eDao = new EquipeDAO();
    private FuncionarioBean funcionario;
    private EmpresaDAO empresaDAO = new EmpresaDAO();
    private Boolean todosOsProfissionais;
    private AgendaDAO agendaDAO = new AgendaDAO();

    public AlteracaoPacienteController() {
        insercao = new InsercaoPacienteBean();
        insercaoParaLaudo = new InsercaoPacienteBean();
        listaProfissionaisAdicionados = new ArrayList<FuncionarioBean>();
        funcionario = new FuncionarioBean();
        tipo = "";
        iDao = new InsercaoPacienteDAO();
        listaDiasProfissional = new ArrayList<GerenciarPacienteBean>();
        listAgendamentoProfissional = new ArrayList<InsercaoPacienteBean>();
        todosOsProfissionais = false;
    }

    public void carregaAlteracao() throws ProjetoException {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Map<String, String> params = facesContext.getExternalContext()
                .getRequestParameterMap();
        if (params.get("id") != null) {
            Integer id = Integer.parseInt(params.get("id"));
            id_paciente_insituicao = id;
            this.insercao = aDao.carregarPacientesInstituicaoAlteracao(id);
            carregarLaudoPaciente();
            opcaoAtendimento = empresaDAO.carregarOpcaoAtendimentoDaEmpresa();
            opcaoAtendimento = !opcaoAtendimento.equals(OpcaoAtendimento.AMBOS.getSigla()) ? opcaoAtendimento : OpcaoAtendimento.SOMENTE_TURNO.getSigla();
            if (insercao.getEquipe().getCodEquipe() != null
                    && insercao.getEquipe().getCodEquipe() > 0) {
                tipo = TipoAtendimento.EQUIPE.getSigla();
                listaDiasProfissional = aDao
                        .listarDiasAtendimentoProfissionalEquipe(id);
            }
            if (insercao.getFuncionario().getId() != null
                    && insercao.getFuncionario().getId() > 0) {
                tipo = TipoAtendimento.PROFISSIONAL.getSigla();
                insercao.getFuncionario().setListDiasSemana(
                        aDao.listarDiasAtendimentoProfissional(id));
            }

        } else {
            JSFUtil.adicionarMensagemErro("Ocorreu um erro!", "Erro");
        }

    }

    public void gerarListaAgendamentosEquipe() throws ProjetoException {

        Boolean temAtendimento = false;
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        df.setLenient(false);
        Date d1 = insercao.getData_solicitacao();

        if (aDao.listaAtendimentos(id_paciente_insituicao).size() > 0) {

            for (int i = 0; i < aDao.listaAtendimentos(id_paciente_insituicao)
                    .size(); i++) {

                d1 = aDao.listaAtendimentos(id_paciente_insituicao).get(i)
                        .getDataAtendimento();
                insercao.setData_solicitacao(d1);
                temAtendimento = true;
            }
        }

        Date d2 = iDao.dataFinalLaudo(insercaoParaLaudo.getLaudo().getId());
        Long dt = (d2.getTime() - d1.getTime());

        dt = (dt / 86400000L);

        Calendar c = Calendar.getInstance();

        // INICIA O ATENDIMENTO 1 DIA APÓS O ÚLTIMO
        if (temAtendimento) {

            c.setTime(insercao.getData_solicitacao());
            c.add(Calendar.DAY_OF_MONTH, 1);
        } else {

            c.setTime(insercao.getData_solicitacao());
        }

        for (int i = 0; i < dt; i++) {

            if (i > 0) {

                c.add(Calendar.DAY_OF_MONTH, 1);
            }

            int diaSemana = c.get(Calendar.DAY_OF_WEEK);

            if (tipo.equals(TipoAtendimento.EQUIPE.getSigla())) {
                for (int j = 0; j < listaProfissionaisAdicionados.size(); j++) {
                    for (int h = 0; h < listaProfissionaisAdicionados.get(j).getListDiasSemana().size(); h++) {

                        if (diaSemana == Integer
                                .parseInt(listaProfissionaisAdicionados.get(j)
                                        .getListDiasSemana().get(h))) {

                            InsercaoPacienteBean ins = new InsercaoPacienteBean();

                            ins.getAgenda().setPaciente(
                                    insercao.getLaudo().getPaciente());

                            ins.getAgenda().setDataMarcacao(c.getTime());

                            ins.getAgenda().setProfissional(
                                    listaProfissionaisAdicionados.get(j));

                            listAgendamentoProfissional.add(ins);

                        }
                    }

                }
            }
        }

    }

    public void excluirFuncionarioIhDiasDeAtendimento(){
        funcionario.getListDiasSemana().remove(funcionario);
        listaProfissionaisAdicionados.remove(funcionario);
    }

    public void gerarListaAgendamentosProfissional() throws ProjetoException {

        Boolean temAtendimento = false;
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        df.setLenient(false);
        Date d1 = insercao.getData_solicitacao();

        if (aDao.listaAtendimentos(id_paciente_insituicao).size() > 0
                && aDao.listaAtendimentos(id_paciente_insituicao).get(0)
                .getDataAtendimento() != null) {
            for (int i = 0; i < aDao.listaAtendimentos(id_paciente_insituicao)
                    .size(); i++) {
                d1 = aDao.listaAtendimentos(id_paciente_insituicao).get(i)
                        .getDataAtendimento();
                insercao.setData_solicitacao(d1);
                temAtendimento = true;
            }
        }

        Date d2 = iDao.dataFinalLaudo(insercaoParaLaudo.getLaudo().getId());

        Long dt = (d2.getTime() - d1.getTime());

        dt = (dt / 86400000L);

        Calendar c = Calendar.getInstance();

        // INICIA O ATENDIMENTO 1 DIA APÓS O ÚLTIMO
        if (temAtendimento) {
            c.setTime(insercao.getData_solicitacao());
            c.add(Calendar.DAY_OF_MONTH, 1);
        } else {
            c.setTime(insercao.getData_solicitacao());
        }

        for (int i = 0; i < dt; i++) {
            if (i > 0) {
                c.add(Calendar.DAY_OF_MONTH, 1);
            }

            int diaSemana = c.get(Calendar.DAY_OF_WEEK);

            if (tipo.equals(TipoAtendimento.PROFISSIONAL.getSigla())) {
                for (int j = 0; j < funcionario.getListDiasSemana().size(); j++) {
                    if (diaSemana == Integer.parseInt(funcionario
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

    public void gravarAlteracaoPaciente() throws ProjetoException {

        Boolean cadastrou = null;

        InsercaoPacienteController insercaoPacienteController = new InsercaoPacienteController();

        if (tipo.equals(TipoAtendimento.EQUIPE.getSigla())) {

            gerarListaAgendamentosEquipe();

            ArrayList<InsercaoPacienteBean> listaAgendamentosProfissionalFinal = insercaoPacienteController.validarDatas(listAgendamentoProfissional, insercao.getAgenda().getTurno());

            cadastrou = aDao.gravarAlteracaoEquipe(insercao, insercaoParaLaudo,
                    listaAgendamentosProfissionalFinal, id_paciente_insituicao, listaProfissionaisAdicionados);
        }
        if (tipo.equals(TipoAtendimento.PROFISSIONAL.getSigla())) {

            gerarListaAgendamentosProfissional();

            ArrayList<InsercaoPacienteBean> listaAgendamentosProfissionalFinal = insercaoPacienteController.validarDatas(listAgendamentoProfissional, insercao.getAgenda().getTurno());

            cadastrou = aDao.gravarAlteracaoProfissional(insercao,
                    insercaoParaLaudo, listaAgendamentosProfissionalFinal,
                    id_paciente_insituicao);
        }

        if (cadastrou) {
            JSFUtil.adicionarMensagemSucesso("Alteração de Equipe cadastrada com sucesso!", "Sucesso");
        } else {
            JSFUtil.adicionarMensagemErro("Ocorreu um erro durante o cadastro!", "Erro");
        }
    }

    public void carregarLaudoPaciente() throws ProjetoException {
        insercaoParaLaudo = iDao.carregarLaudoPaciente(insercao.getLaudo()
                .getId());
        insercao = aDao
                .carregarPacientesInstituicaoAlteracao(id_paciente_insituicao);

    }

    public void abrirDialog(){
        JSFUtil.abrirDialog("dlgDiasAtendimento");
    }

    public List<FuncionarioBean> listaProfissionalAutoComplete(String query)
            throws ProjetoException {
        FuncionarioDAO fDao = new FuncionarioDAO();
        List<FuncionarioBean> result = fDao.listarProfissionalBusca(query, 1);
        return result;
    }

    public List<EquipeBean> listaEquipeAutoComplete(String query)
            throws ProjetoException {

        List<EquipeBean> result = eDao.listarEquipePorGrupoAutoComplete(query,
                insercao.getGrupo().getIdGrupo());
        return result;
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
        //Retirado para análise futura, retirei na véspera da apresentação para a funcionalidade ficar ok. Data: 26/03/2019
        //limparDias();
    }

    public void adicionarFuncionario() {
        String dias = "";

        for (int i = 0; i < funcionario.getListDiasSemana().size(); i++) {
            if (funcionario.getListDiasSemana().get(i).equals(DiasDaSemana.DOMINGO.getSigla())) {
                dias = dias + "Domingo";

                if(funcionario.getListDiasSemana().size() > 1 && funcionario.getListDiasSemana().size()!=i+1){
                    dias = dias + ", ";
                }
            }

            if (funcionario.getListDiasSemana().get(i).equals(DiasDaSemana.SEGUNDA.getSigla())) {
                dias = dias + "Segunda";
                if(funcionario.getListDiasSemana().size() > 1 && funcionario.getListDiasSemana().size()!=i+1){
                    dias = dias + ", ";
                }
            }

            if (funcionario.getListDiasSemana().get(i).equals(DiasDaSemana.TERCA.getSigla())) {
                dias = dias + "Terça";
                if(funcionario.getListDiasSemana().size() > 1 && funcionario.getListDiasSemana().size()!=i+1){
                    dias = dias + ", ";
                }
            }

            if (funcionario.getListDiasSemana().get(i).equals(DiasDaSemana.QUARTA.getSigla())) {
                dias = dias + "Quarta";
                if(funcionario.getListDiasSemana().size() > 1 && funcionario.getListDiasSemana().size()!=i+1){
                    dias = dias + ", ";
                }
            }

            if (funcionario.getListDiasSemana().get(i).equals(DiasDaSemana.QUINTA.getSigla())) {
                dias = dias + "Quinta";
                if(funcionario.getListDiasSemana().size() > 1 && funcionario.getListDiasSemana().size()!=i+1){
                    dias = dias + ", ";
                }
            }

            if (funcionario.getListDiasSemana().get(i).equals(DiasDaSemana.SEXTA.getSigla())) {
                dias = dias + "Sexta";
                if(funcionario.getListDiasSemana().size() > 1 && funcionario.getListDiasSemana().size()!=i+1){
                    dias = dias + ", ";
                }
            }

            if (funcionario.getListDiasSemana().get(i).equals(DiasDaSemana.SABADO.getSigla())) {
                dias = dias + "Sábado";
                if(funcionario.getListDiasSemana().size() > 1 && funcionario.getListDiasSemana().size()!=i+1){
                    dias = dias + ", ";
                }
            }

        }
        dias = dias + ".";

        funcionario.setDiasSemana(dias);
        listaProfissionaisAdicionados.add(funcionario);

        JSFUtil.fecharDialog("dlgDiasAtendimento");
    }

    public void listarProfissionaisEquipe() throws ProjetoException {
        if (insercao.getLaudo().getId() != null) {
            if (insercao.getEquipe() != null) {
                if (insercao.getEquipe().getCodEquipe() != null) {
                    listaProfissionaisEquipe = eDao
                            .listarProfissionaisDaEquipeInsercao(insercao.getEquipe()
                                    .getCodEquipe(), todosOsProfissionais);
                }
            } else {
                JSFUtil.adicionarMensagemErro("Escolha uma equipe!",
                        "Bloqueio");
            }
        } else {
            JSFUtil.adicionarMensagemErro("Carregue um laudo primeiro!", "Bloqueio");

        }
    }

    public List<AgendaBean> visualizarHorariosEquipe() {
        return agendaDAO.quantidadeDeAgendamentosDaEquipePorTurno();
    }

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

    public ArrayList<GerenciarPacienteBean> getListaDiasProfissional() {
        return listaDiasProfissional;
    }

    public void setListaDiasProfissional(
            ArrayList<GerenciarPacienteBean> listaDiasProfissional) {
        this.listaDiasProfissional = listaDiasProfissional;
    }

    public InsercaoPacienteBean getInsercaoParaLaudo() {
        return insercaoParaLaudo;
    }

    public void setInsercaoParaLaudo(InsercaoPacienteBean insercaoParaLaudo) {
        this.insercaoParaLaudo = insercaoParaLaudo;
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

    public Boolean getTodosOsProfissionais() {
        return todosOsProfissionais;
    }

    public void setTodosOsProfissionais(Boolean todosOsProfissionais) {
        this.todosOsProfissionais = todosOsProfissionais;
    }
}
