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
import br.gov.al.maceio.sishosp.comum.util.HorarioOuTurnoUtil;
import br.gov.al.maceio.sishosp.comum.util.JSFUtil;

import br.gov.al.maceio.sishosp.acl.dao.FuncionarioDAO;
import br.gov.al.maceio.sishosp.acl.model.FuncionarioBean;
import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.hosp.abstracts.VetorDiaSemanaAbstract;
import br.gov.al.maceio.sishosp.hosp.dao.*;
import br.gov.al.maceio.sishosp.hosp.enums.DiasDaSemana;
import br.gov.al.maceio.sishosp.hosp.enums.OpcaoAtendimento;
import br.gov.al.maceio.sishosp.hosp.enums.TipoAtendimento;
import br.gov.al.maceio.sishosp.hosp.model.*;
import org.primefaces.event.SelectEvent;

@ManagedBean(name = "InsercaoController")
@ViewScoped
public class InsercaoPacienteController extends VetorDiaSemanaAbstract implements Serializable {

    private static final long serialVersionUID = 1L;
    private InsercaoPacienteBean insercao;
    private InsercaoPacienteDAO iDao = new InsercaoPacienteDAO();
    private EquipeDAO eDao = new EquipeDAO();
    private AgendaDAO agendaDAO = new AgendaDAO();
    private ProgramaBean programaSelecionado;
    private ArrayList<InsercaoPacienteBean> listaLaudosVigentes;
    private ArrayList<FuncionarioBean> listaProfissionaisEquipe;
    private ArrayList<FuncionarioBean> listaProfissionaisAdicionados;
    private List<FuncionarioBean> listaProfissionais;
    private List<GrupoBean> listaGruposProgramas;
    private String tipo;
    private FuncionarioBean funcionario;
    private ArrayList<InsercaoPacienteBean> listAgendamentoProfissional;
    private List<EquipeBean> listaEquipePorGrupo;
    private String opcaoAtendimento;
    private EmpresaDAO empresaDAO = new EmpresaDAO();
    private ArrayList<String> listaHorarios;
    private Boolean todosOsProfissionais;
    private Boolean renderizarAposLaudo;
    private List<AgendaBean> listaHorariosDaEquipe;

    public InsercaoPacienteController() throws ProjetoException {
        this.insercao = new InsercaoPacienteBean();
        listaHorariosDaEquipe = new ArrayList<AgendaBean>();
        programaSelecionado = new ProgramaBean();
        listaLaudosVigentes = new ArrayList<InsercaoPacienteBean>();
        this.tipo = TipoAtendimento.EQUIPE.getSigla();
        funcionario = new FuncionarioBean();
        listaProfissionaisAdicionados = new ArrayList<FuncionarioBean>();
        listAgendamentoProfissional = new ArrayList<InsercaoPacienteBean>();
        listaGruposProgramas = new ArrayList<GrupoBean>();
        listaHorarios = new ArrayList<>();
        todosOsProfissionais = false;
        renderizarAposLaudo = false;
        listaEquipePorGrupo = new ArrayList<>();
        listaProfissionais = new ArrayList<>();
    }

    public void carregarHorarioOuTurnoInsercao() throws ProjetoException, ParseException {
        opcaoAtendimento = HorarioOuTurnoUtil.retornarOpcaoAtendimentoEmpresa();

        if(opcaoAtendimento.equals(OpcaoAtendimento.SOMENTE_HORARIO.getSigla()) || opcaoAtendimento.equals(OpcaoAtendimento.AMBOS.getSigla())){
            gerarHorariosAtendimento();
        }
    }

    public String carregarHorarioOuTurno() throws ProjetoException, ParseException {
        opcaoAtendimento = HorarioOuTurnoUtil.retornarOpcaoAtendimentoEmpresa();

        if(opcaoAtendimento.equals(OpcaoAtendimento.SOMENTE_HORARIO.getSigla()) || opcaoAtendimento.equals(OpcaoAtendimento.AMBOS.getSigla())){
            gerarHorariosAtendimento();
        }

        return opcaoAtendimento;

    }

    public void limparDados() {
        this.insercao = new InsercaoPacienteBean();
        renderizarAposLaudo = false;
        listaProfissionaisEquipe = new ArrayList<>();
        listaProfissionaisAdicionados = new ArrayList<>();
        limparDias();
    }

    public void limparDias() {
        funcionario.setListDiasSemana(new ArrayList<String>());
    }

    public void listarLaudosVigentes()
            throws ProjetoException {
        listaLaudosVigentes = iDao.listarLaudosVigentes();
    }

    public void limparNaBuscaPrograma() {
        this.insercao.setGrupo(new GrupoBean());
        this.insercao.setEquipe(new EquipeBean());
    }

    public void selectPrograma(SelectEvent event) throws ProjetoException {
        this.programaSelecionado = (ProgramaBean) event.getObject();
        atualizaListaGrupos(programaSelecionado);
        limparNaBuscaPrograma();
    }

    public void atualizaListaGrupos(ProgramaBean p) throws ProjetoException {
        GrupoDAO gDao = new GrupoDAO();
        this.programaSelecionado = p;
        this.listaGruposProgramas = gDao.listarGruposPorPrograma(p
                .getIdPrograma());
        for (GrupoBean g : listaGruposProgramas) {
        }

    }

    public void carregaListaEquipePorTipoAtendimento()
            throws ProjetoException {
        System.out.println("listaEquipePorTipoAtendimento");
        if (insercao.getGrupo().getIdGrupo() != null) {
            listaEquipePorGrupo = eDao
                    .listarEquipePorGrupo(insercao.getGrupo().getIdGrupo());
        }
    }


    public void carregarLaudoPaciente() throws ProjetoException {
        int id = insercao.getLaudo().getId();
        limparDados();
        insercao = iDao.carregarLaudoPaciente(id);
        renderizarAposLaudo = true;

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

    public void abrirDialog() {
        JSFUtil.abrirDialog("dlgDiasAtendimento");
    }

    public void excluirFuncionarioIhDiasDeAtendimento() {
        funcionario.getListDiasSemana().remove(funcionario);
        listaProfissionaisAdicionados.remove(funcionario);
    }

    public void adicionarFuncionario() {
        String dias = "";

        for (int i = 0; i < funcionario.getListDiasSemana().size(); i++) {
            if (funcionario.getListDiasSemana().get(i).equals(DiasDaSemana.DOMINGO.getSigla())) {
                dias = dias + "Domingo";

                if (funcionario.getListDiasSemana().size() > 1 && funcionario.getListDiasSemana().size() != i + 1) {
                    dias = dias + ", ";
                }
            }

            if (funcionario.getListDiasSemana().get(i).equals(DiasDaSemana.SEGUNDA.getSigla())) {
                dias = dias + "Segunda";
                if (funcionario.getListDiasSemana().size() > 1 && funcionario.getListDiasSemana().size() != i + 1) {
                    dias = dias + ", ";
                }
            }

            if (funcionario.getListDiasSemana().get(i).equals(DiasDaSemana.TERCA.getSigla())) {
                dias = dias + "Terça";
                if (funcionario.getListDiasSemana().size() > 1 && funcionario.getListDiasSemana().size() != i + 1) {
                    dias = dias + ", ";
                }
            }

            if (funcionario.getListDiasSemana().get(i).equals(DiasDaSemana.QUARTA.getSigla())) {
                dias = dias + "Quarta";
                if (funcionario.getListDiasSemana().size() > 1 && funcionario.getListDiasSemana().size() != i + 1) {
                    dias = dias + ", ";
                }
            }

            if (funcionario.getListDiasSemana().get(i).equals(DiasDaSemana.QUINTA.getSigla())) {
                dias = dias + "Quinta";
                if (funcionario.getListDiasSemana().size() > 1 && funcionario.getListDiasSemana().size() != i + 1) {
                    dias = dias + ", ";
                }
            }

            if (funcionario.getListDiasSemana().get(i).equals(DiasDaSemana.SEXTA.getSigla())) {
                dias = dias + "Sexta";
                if (funcionario.getListDiasSemana().size() > 1 && funcionario.getListDiasSemana().size() != i + 1) {
                    dias = dias + ", ";
                }
            }

            if (funcionario.getListDiasSemana().get(i).equals(DiasDaSemana.SABADO.getSigla())) {
                dias = dias + "Sábado";
                if (funcionario.getListDiasSemana().size() > 1 && funcionario.getListDiasSemana().size() != i + 1) {
                    dias = dias + ", ";
                }
            }

        }
        dias = dias + ".";

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

        for (int i = 0; i <= dt; i++) {

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

        for (int i = 0; i <= dt; i++) {

            if (i > 0) {
                c.add(Calendar.DAY_OF_MONTH, 1);
            }

            int diaSemana = c.get(Calendar.DAY_OF_WEEK);
            ArrayList<Date> listaDatasDeAtendimento = new ArrayList<Date>();
            if (tipo.equals(TipoAtendimento.EQUIPE.getSigla())) {
                for (int j = 0; j < listaProfissionaisAdicionados.size(); j++) {
                    for (int h = 0; h < listaProfissionaisAdicionados.get(j).getListDiasSemana().size(); h++) {
                        if (!listaDatasDeAtendimento.contains(c.getTime())) {
                            if (diaSemana == Integer.parseInt(listaProfissionaisAdicionados.get(j).getListDiasSemana().get(h))) {

                                InsercaoPacienteBean ins = new InsercaoPacienteBean();

                                ins.getAgenda().setPaciente(
                                        insercao.getLaudo().getPaciente());

                                ins.getAgenda().setDataMarcacao(c.getTime());

                                ins.getAgenda().setProfissional(listaProfissionaisAdicionados.get(j));

                                listAgendamentoProfissional.add(ins);
                                listaDatasDeAtendimento.add(c.getTime());

                            }
                        }
                    }
                }

            }

        }

    }

    public void validarInsercaoPaciente() throws ProjetoException {

        if (insercao.getEncaixe()) {
            gravarInsercaoPaciente();
        } else if (tipo.equals(TipoAtendimento.EQUIPE.getSigla())) {
            if (agendaDAO.numeroAtendimentosEquipe(insercao)) {
                gravarInsercaoPaciente();
            } else {
                JSFUtil.adicionarMensagemErro("Quantidade de agendamentos para esse profissional já antigiu o máximo para esse horário e dia!",
                        "Erro");
            }
        } else {
            if (agendaDAO.numeroAtendimentosProfissional(insercao)) {
                gravarInsercaoPaciente();
            } else {
                JSFUtil.adicionarMensagemErro("Quantidade de agendamentos para essa equipe já antigiu o máximo para esse dia!",
                        "Erro");
            }
        }
    }

    public ArrayList<InsercaoPacienteBean> validarDatas(ArrayList<InsercaoPacienteBean> listaAgendamentos, String turno) throws ProjetoException {

        ArrayList<InsercaoPacienteBean> listaAgendamentosAux = new ArrayList<>();

        for (int i = 0; i < listaAgendamentos.size(); i++) {

            if (verificarSeEhFeriadoData(listaAgendamentos.get(i).getAgenda().getDataMarcacao())) {
                listaAgendamentosAux.add(listaAgendamentos.get(i));
            } else if (verificarSeTemBloqueioData(listaAgendamentos.get(i), turno)) {
                listaAgendamentosAux.add(listaAgendamentos.get(i));
            }
        }

        //Adicionei esse FOR pois não estava indo com remove nas condições acima, então adicionei em uma lista e depois removi nesse for abaixo.
        for (int i = 0; i < listaAgendamentosAux.size(); i++) {
            listaAgendamentos.remove(listaAgendamentosAux.get(i));
        }

        return listaAgendamentos;
    }

    public Boolean verificarSeEhFeriadoData(Date data) throws ProjetoException {
        Boolean retorno = false;

        retorno = new FeriadoDAO().verificarSeEhFeriadoDataUnica(data);

        return retorno;
    }

    public Boolean verificarSeTemBloqueioData(InsercaoPacienteBean insercaoBean, String turno) throws ProjetoException {
        Boolean retorno = false;

        retorno = new BloqueioDAO().verificarBloqueioProfissionalDataUnica
                (insercaoBean.getAgenda().getProfissional().getId(), insercaoBean.getAgenda().getDataMarcacao(), turno);

        return retorno;
    }

    public void gravarInsercaoPaciente() throws ProjetoException {

        if (insercao.getLaudo().getId() != null) {

            Boolean cadastrou = null;

            ArrayList<InsercaoPacienteBean> listaAgendamentosProfissionalFinal = validarDatas(listAgendamentoProfissional, insercao.getAgenda().getTurno());

            if (tipo.equals(TipoAtendimento.EQUIPE.getSigla())) {

                gerarListaAgendamentosEquipe();


                cadastrou = iDao.gravarInsercaoEquipe(insercao,
                        listaProfissionaisAdicionados, listaAgendamentosProfissionalFinal);
            }
            if (tipo.equals(TipoAtendimento.PROFISSIONAL.getSigla())) {

                gerarListaAgendamentosProfissional();

                cadastrou = iDao.gravarInsercaoProfissional(insercao,
                        listaAgendamentosProfissionalFinal);
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

    private void gerarHorariosAtendimento() throws ParseException {
        listaHorarios = HorarioOuTurnoUtil.gerarHorariosAtendimento();
    }

    public void visualizarHorariosEquipe() {
    	listaHorariosDaEquipe=  agendaDAO.quantidadeDeAgendamentosDaEquipePorTurno();
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

    public void listarProfissionais() throws ProjetoException {
        FuncionarioDAO fDao = new FuncionarioDAO();
        listaProfissionais = fDao.listarProfissionalAtendimento();
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

    public ArrayList<String> getListaHorarios() {
        return listaHorarios;
    }

    public void setListaHorarios(ArrayList<String> listaHorarios) {
        this.listaHorarios = listaHorarios;
    }

    public Boolean getTodosOsProfissionais() {
        return todosOsProfissionais;
    }

    public void setTodosOsProfissionais(Boolean todosOsProfissionais) {
        this.todosOsProfissionais = todosOsProfissionais;
    }

    public Boolean getRenderizarAposLaudo() {
        return renderizarAposLaudo;
    }

    public void setRenderizarAposLaudo(Boolean renderizarAposLaudo) {
        this.renderizarAposLaudo = renderizarAposLaudo;
    }

    public List<GrupoBean> getListaGruposProgramas() {
        return listaGruposProgramas;
    }

    public void setListaGruposProgramas(List<GrupoBean> listaGruposProgramas) {
        this.listaGruposProgramas = listaGruposProgramas;
    }

    public List<EquipeBean> getListaEquipePorGrupo() {
        return listaEquipePorGrupo;
    }

    public void setListaEquipePorGrupo(List<EquipeBean> listaEquipePorGrupo) {
        this.listaEquipePorGrupo = listaEquipePorGrupo;
    }

    public List<FuncionarioBean> getListaProfissionais() {
        return listaProfissionais;
    }

    public void setListaProfissionais(List<FuncionarioBean> listaProfissionais) {
        this.listaProfissionais = listaProfissionais;
    }

	public List<AgendaBean> getListaHorariosDaEquipe() {
		return listaHorariosDaEquipe;
	}

	public void setListaHorariosDaEquipe(List<AgendaBean> listaHorariosDaEquipe) {
		this.listaHorariosDaEquipe = listaHorariosDaEquipe;
	}
}
