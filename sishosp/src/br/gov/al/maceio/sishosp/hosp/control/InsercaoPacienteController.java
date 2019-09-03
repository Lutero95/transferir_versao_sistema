package br.gov.al.maceio.sishosp.hosp.control;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import br.gov.al.maceio.sishosp.comum.util.DataUtil;
import br.gov.al.maceio.sishosp.hosp.enums.*;
import br.gov.al.maceio.sishosp.hosp.model.*;
import br.gov.al.maceio.sishosp.hosp.model.dto.AvaliacaoInsercaoDTO;
import org.primefaces.event.SelectEvent;

import br.gov.al.maceio.sishosp.acl.dao.FuncionarioDAO;
import br.gov.al.maceio.sishosp.acl.model.FuncionarioBean;
import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.HorarioOuTurnoUtil;
import br.gov.al.maceio.sishosp.comum.util.JSFUtil;
import br.gov.al.maceio.sishosp.hosp.abstracts.VetorDiaSemanaAbstract;
import br.gov.al.maceio.sishosp.hosp.dao.AgendaDAO;
import br.gov.al.maceio.sishosp.hosp.dao.BloqueioDAO;
import br.gov.al.maceio.sishosp.hosp.dao.EquipeDAO;
import br.gov.al.maceio.sishosp.hosp.dao.FeriadoDAO;
import br.gov.al.maceio.sishosp.hosp.dao.GrupoDAO;
import br.gov.al.maceio.sishosp.hosp.dao.InsercaoPacienteDAO;
import br.gov.al.maceio.sishosp.hosp.dao.TipoAtendimentoDAO;

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
    private ArrayList<String> listaHorarios;
    private Boolean todosOsProfissionais;
    private Boolean renderizarAposLaudo;
    private List<AgendaBean> listaHorariosDaEquipe;
    private Boolean liberacao;
    private Boolean ehAvaliacao;
    private ArrayList<Liberacao> listaLiberacao;
    private List<HorarioAtendimento> listaHorarioAtendimentos;
    private List<HorarioAtendimento> listaHorarioAtendimentosAuxiliar;
    private List<HorarioAtendimento> listaHorarioFinal = new ArrayList<>();
    private List<Integer> listaDias;
	private String tipoBusca;
	private String campoBusca;

    public InsercaoPacienteController() throws ProjetoException {
    	System.out.println("construror InsercaoPacienteController");
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
        liberacao = false;
        ehAvaliacao = false;
        listaLiberacao = new ArrayList<>();
        listaHorarioAtendimentos = new ArrayList<>();
        listaHorarioAtendimentosAuxiliar = new ArrayList<>();
        listaDias = new ArrayList<>();
    }

    public void carregarHorarioOuTurnoInsercao() throws ProjetoException, ParseException {
    	System.out.println("insercao carregarHorarioOuTurnoInsercao");
        opcaoAtendimento = HorarioOuTurnoUtil.retornarOpcaoAtendimentoUnidade();

        if (opcaoAtendimento.equals(OpcaoAtendimento.SOMENTE_HORARIO.getSigla()) || opcaoAtendimento.equals(OpcaoAtendimento.AMBOS.getSigla())) {
            gerarHorariosAtendimento();
        }
    }

    public String carregarHorarioOuTurno() throws ProjetoException, ParseException {
    	System.out.println("isnercao carregarHorarioOuTurno");
        opcaoAtendimento = HorarioOuTurnoUtil.retornarOpcaoAtendimentoUnidade();

        if (opcaoAtendimento.equals(OpcaoAtendimento.SOMENTE_HORARIO.getSigla()) || opcaoAtendimento.equals(OpcaoAtendimento.AMBOS.getSigla())) {
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
    	System.out.println("insercao limparDias");
        funcionario.setListDiasSemana(new ArrayList<String>());
    }

    public void listarLaudosVigentes(String campoBusca, String tipoBusca)
            throws ProjetoException {
    	System.out.println("insercao listarLaudosVigentes");
        listaLaudosVigentes = iDao.listarLaudosVigentes(campoBusca, tipoBusca);
    }

    public void limparNaBuscaPrograma() {
    	System.out.println("insercao");
        this.insercao.setGrupo(new GrupoBean());
        this.insercao.setEquipe(new EquipeBean());
    }

    public void selectPrograma(SelectEvent event) throws ProjetoException {
    	System.out.println("insercao selectPrograma");
        this.programaSelecionado = (ProgramaBean) event.getObject();
        atualizaListaGrupos(programaSelecionado);
        limparNaBuscaPrograma();
    }

    public void atualizaListaGrupos(ProgramaBean p) throws ProjetoException {
    	System.out.println("isnercao atualizaListaGrupos");
        GrupoDAO gDao = new GrupoDAO();
        this.programaSelecionado = p;
        this.listaGruposProgramas = gDao.listarGruposPorPrograma(p
                .getIdPrograma());
        for (GrupoBean g : listaGruposProgramas) {
        }

    }

    public void carregaListaEquipePorTipoAtendimento()
            throws ProjetoException {
    	System.out.println("insercao carregaListaEquipePorTipoAtendimento");
        if (insercao.getGrupo().getIdGrupo() != null) {
            listaEquipePorGrupo = eDao
                    .listarEquipePorGrupo(insercao.getGrupo().getIdGrupo());
        }
    }


    public void validarCarregarLaudoPaciente() throws ProjetoException {
    	System.out.println("insercao validarCarregarLaudoPaciente");
        int id = insercao.getLaudo().getId();

        if (validarCarregamentoDoLaudo(id)) {
            carregarLaudoPaciente(id);
        }

    }

    private void carregarLaudoPaciente(int id) throws ProjetoException {
    	System.out.println("isnercao carregarLaudoPaciente");
        limparDados();
        insercao = iDao.carregarLaudoPaciente(id);
        renderizarAposLaudo = true;

        if (ehAvaliacao) {
            carregarDadosAvaliacao();
        }
    }

    private void carregarDadosAvaliacao(){
    	System.out.println("insercao carregarDadosAvaliacao");
        AvaliacaoInsercaoDTO avaliacaoInsercaoDTO = iDao.carregarAtendimentoAvaliacao(insercao.getLaudo().getId());
        insercao.setPrograma(avaliacaoInsercaoDTO.getProgramaBean());
        insercao.setGrupo(avaliacaoInsercaoDTO.getGrupoBean());
        insercao.setEquipe(avaliacaoInsercaoDTO.getEquipeBean());
        listaProfissionaisEquipe = avaliacaoInsercaoDTO.getListaProfissionais();
    }

    public Boolean validarCarregamentoDoLaudo(Integer idLaudo) throws ProjetoException {
    	System.out.println("insercao validarCarregamentoDoLaudo");
        Boolean retorno = true;

        if (iDao.verificarSeLaudoConstaNoAtendimento(idLaudo)) {
            ehAvaliacao = true;
            if (iDao.verificarSeAlgumAtendimentoNaoFoiLancadoPerfil(idLaudo)) {
                JSFUtil.adicionarMensagemErro(
                        "Avaliação incompleta, preencha totalmente a avaliação que esse laudo está relacionada para poder selecionar esse laudo na inserção!",
                        "Erro");
                retorno = false;
            } else {
                if (!iDao.verificarSeAlgumAtendimentoDoLaudoTemPerfilAvaliacao(idLaudo)) {
                    JSFUtil.abrirDialog("dlgSenha");
                    retorno = false;
                }
            }
        }

        return retorno;
    }

    private Boolean validarAdicionarFuncionarioDiaHorario() {
        Boolean retorno = false;
        for (int i = 0; i < listaHorarioAtendimentos.size(); i++) {
            if (listaHorarioAtendimentos.get(i).getDiaSemana() == insercao.getHorarioAtendimento().getDiaSemana()) {
                return false;
            }
        }

        return true;
    }

    public void validarSenhaIhRealizarLiberacao() throws ProjetoException {
    	System.out.println("insercao validarSenhaIhRealizarLiberacao");
        FuncionarioDAO funcionarioDAO = new FuncionarioDAO();

        Integer idFuncionario = funcionarioDAO.validarCpfIhSenha(funcionario.getCpf(), funcionario.getSenha(), ValidacaoSenha.LIBERACAO_PACIENTES_SEM_PERFIL.getSigla());

        if (idFuncionario > 0) {
            liberacao = true;
            JSFUtil.fecharDialog("dlgSenha");
            adicionarLiberacaoNaLista(montarLiberacao(LiberacaoMotivos.AVALIACAO.getSigla(), idFuncionario, LiberacaoRotinas.INSERCAO.getSigla()));
            carregarLaudoPaciente(insercao.getLaudo().getId());
            listarProfissionaisEquipe();
        } else {
            JSFUtil.adicionarMensagemErro("Funcionário com senha errada ou sem liberação!", "Erro!");
        }
    }

    public Liberacao montarLiberacao(String motivo, Integer idUsuarioLiberacao, String rotina){
    	System.out.println("insercao montarLiberacao");
        Liberacao liberacao = null;
        return liberacao = new Liberacao(motivo, idUsuarioLiberacao, DataUtil.retornarDataIhHoraAtual(), rotina);
    }

    public void adicionarLiberacaoNaLista(Liberacao liberacao){
    	System.out.println("isnercao adicionarLiberacaoNaLista");
        listaLiberacao.add(liberacao);
    }

    public void adicionarFuncionarioDiaHorario() {
        if (validarAdicionarFuncionarioDiaHorario()) {
            insercao.getHorarioAtendimento().setFuncionario(funcionario);
            listaHorarioAtendimentos.add(insercao.getHorarioAtendimento());
            insercao.setHorarioAtendimento(new HorarioAtendimento());
        } else {
            JSFUtil.adicionarMensagemAdvertencia("Esse dia já foi adicionado!", "Advertência");
        }
    }

    public void verificarAdicionarFuncionarioHorarioDia() {
        if (listaHorarioAtendimentos.size() == 0) {
            JSFUtil.adicionarMensagemAdvertencia("Adicione ao menos 1 dia e horário!", "Advertência");
        } else if (verificarSeHorariosDoFuncionarioJaForamAdicionados()) {
            JSFUtil.adicionarMensagemAdvertencia("Esse funcionário já teve seus horários adicionados!", "Advertência");
        } else {
            adicionarFuncionarioHorarioDia();
        }
    }

    private Boolean verificarSeHorariosDoFuncionarioJaForamAdicionados() {
        if (listaProfissionaisAdicionados.size() == 0) {
            return false;
        } else {
            for (int i = 0; i < listaProfissionaisAdicionados.size(); i++) {
                if (listaProfissionaisAdicionados.get(i).getId().equals(funcionario.getId())) {
                    return true;
                }
            }
        }

        return false;
    }

    private void adicionarFuncionarioHorarioDia() {

        String dias = "";

        for (int i = 0; i < listaHorarioAtendimentos.size(); i++) {
            if (listaHorarioAtendimentos.get(i).getDiaSemana().toString().equals(DiasDaSemana.DOMINGO.getSigla())) {
                dias = dias + "Domingo " + listaHorarioAtendimentos.get(i).getHorario();

                if (listaHorarioAtendimentos.size() > 1 && listaHorarioAtendimentos.size() != i + 1) {
                    dias = dias + ", ";
                }
            }

            if (listaHorarioAtendimentos.get(i).getDiaSemana().toString().equals(DiasDaSemana.SEGUNDA.getSigla())) {
                dias = dias + "Segunda " + listaHorarioAtendimentos.get(i).getHorario();
                if (listaHorarioAtendimentos.size() > 1 && listaHorarioAtendimentos.size() != i + 1) {
                    dias = dias + ", ";
                }
            }

            if (listaHorarioAtendimentos.get(i).getDiaSemana().toString().equals(DiasDaSemana.TERCA.getSigla())) {
                dias = dias + "Terça " + listaHorarioAtendimentos.get(i).getHorario();
                if (listaHorarioAtendimentos.size() > 1 && listaHorarioAtendimentos.size() != i + 1) {
                    dias = dias + ", ";
                }
            }

            if (listaHorarioAtendimentos.get(i).getDiaSemana().toString().equals(DiasDaSemana.QUARTA.getSigla())) {
                dias = dias + "Quarta " + listaHorarioAtendimentos.get(i).getHorario();
                if (listaHorarioAtendimentos.size() > 1 && listaHorarioAtendimentos.size() != i + 1) {
                    dias = dias + ", ";
                }
            }

            if (listaHorarioAtendimentos.get(i).getDiaSemana().toString().equals(DiasDaSemana.QUINTA.getSigla())) {
                dias = dias + "Quinta " + listaHorarioAtendimentos.get(i).getHorario();
                if (listaHorarioAtendimentos.size() > 1 && listaHorarioAtendimentos.size() != i + 1) {
                    dias = dias + ", ";
                }
            }

            if (listaHorarioAtendimentos.get(i).getDiaSemana().toString().equals(DiasDaSemana.SEXTA.getSigla())) {
                dias = dias + "Sexta " + listaHorarioAtendimentos.get(i).getHorario();
                if (listaHorarioAtendimentos.size() > 1 && listaHorarioAtendimentos.size() != i + 1) {
                    dias = dias + ", ";
                }
            }

            if (listaHorarioAtendimentos.get(i).getDiaSemana().toString().equals(DiasDaSemana.SABADO.getSigla())) {
                dias = dias + "Sábado " + listaHorarioAtendimentos.get(i).getHorario();
                if (listaHorarioAtendimentos.size() > 1 && listaHorarioAtendimentos.size() != i + 1) {
                    dias = dias + ", ";
                }
            }

        }
        dias = dias + ".";

        funcionario.setDiasSemana(dias);
        listaProfissionaisAdicionados.add(funcionario);

        listaHorarioAtendimentosAuxiliar = new ArrayList<>();
        for (int i = 0; i < listaHorarioAtendimentos.size(); i++) {
            listaHorarioAtendimentosAuxiliar.add(listaHorarioAtendimentos.get(i));
        }

        adicionarProfissionalIhHorarioNaLista();

        JSFUtil.fecharDialog("dlgDiasAtendimento");
    }

    public void excluirDiasHorariosDoFuncionario(HorarioAtendimento horarioAtendimento) {
        listaHorarioAtendimentos.remove(horarioAtendimento);
    }

    private void adicionarProfissionalIhHorarioNaLista() {

        if (listaHorarioFinal.size() == 0) {
            for (int i = 0; i < listaHorarioAtendimentosAuxiliar.size(); i++) {
                listaHorarioFinal.add(listaHorarioAtendimentosAuxiliar.get(i));
            }
            for (int i = 0; i < listaHorarioFinal.size(); i++) {
                listaHorarioFinal.get(i).getListaFuncionarios().add(funcionario);
            }

        } else {
            List<HorarioAtendimento> listaClonada = new ArrayList<>();
            for (int i = 0; i < listaHorarioFinal.size(); i++) {
                listaClonada.add(listaHorarioFinal.get(i));
            }

            for (int j = 0; j < listaHorarioFinal.size(); j++) {
                for (int i = 0; i < listaHorarioAtendimentosAuxiliar.size(); i++) {

                    if (listaHorarioFinal.get(j).getDiaSemana().equals(listaHorarioAtendimentosAuxiliar.get(i).getDiaSemana())
                            && listaHorarioFinal.get(j).getHorario().equals(listaHorarioAtendimentosAuxiliar.get(i).getHorario())) {

                    } else {
                        listaClonada.add(listaHorarioAtendimentosAuxiliar.get(i));


                    }
                }
            }

            listaHorarioFinal = new ArrayList<>();
            listaHorarioFinal = listaClonada;

            for (int j = 0; j < listaHorarioFinal.size(); j++) {
                for (int i = 0; i < listaHorarioAtendimentosAuxiliar.size(); i++) {

                    if (listaHorarioFinal.get(j).getDiaSemana().equals(listaHorarioAtendimentosAuxiliar.get(i).getDiaSemana())
                            && listaHorarioFinal.get(j).getHorario().equals(listaHorarioAtendimentosAuxiliar.get(i).getHorario())
                    ) {

                    } else {
                        if (!listaHorarioFinal.get(j).getListaFuncionarios().contains(listaHorarioAtendimentosAuxiliar.get(i).getFuncionario().getId())) {
                            listaHorarioFinal.get(j).getListaFuncionarios().add(listaHorarioAtendimentosAuxiliar.get(i).getFuncionario());
                        }

                    }
                }
            }
        }


        for (int i = 0; i < listaHorarioFinal.size(); i++) {
            if (!listaDias.contains(listaHorarioFinal.get(i).getDiaSemana())) {
                listaDias.add(listaHorarioFinal.get(i).getDiaSemana());
            }
        }
    }

    // VALIDAÇÃO DE NÃO REPETIR O PROFISSIONAL
    public void validarAdicionarFuncionario() {
    	System.out.println("insercao validarAdicionarFuncionario");
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
        Date d1 = insercao.getDataSolicitacao();
        Date d2 = iDao.dataFinalLaudo(insercao.getLaudo().getId());
        Long dt = (d2.getTime() - d1.getTime());

        dt = (dt / 86400000L);

        Calendar c = Calendar.getInstance();
        c.setTime(insercao.getDataSolicitacao());

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
        Date d1 = insercao.getDataSolicitacao();
        Date d2 = iDao.dataFinalLaudo(insercao.getLaudo().getId());
        Long dt = (d2.getTime() - d1.getTime());

        dt = (dt / 86400000L);

        Calendar c = Calendar.getInstance();
        c.setTime(insercao.getDataSolicitacao());

        for (int i = 0; i <= dt; i++) {

            if (i > 0) {
                c.add(Calendar.DAY_OF_MONTH, 1);
            }

            int diaSemana = c.get(Calendar.DAY_OF_WEEK);
            ArrayList<Date> listaDatasDeAtendimento = new ArrayList<Date>();
            if (tipo.equals(TipoAtendimento.EQUIPE.getSigla())) {
                for (int h = 0; h < listaDias.size(); h++) {
                    if (!listaDatasDeAtendimento.contains(c.getTime())) {
                        if (diaSemana == listaDias.get(h)) {

                            InsercaoPacienteBean ins = new InsercaoPacienteBean();

                            ins.getAgenda().setPaciente(
                                    insercao.getLaudo().getPaciente());

                            ins.getAgenda().setDataMarcacao(c.getTime());

                            listAgendamentoProfissional.add(ins);
                            listaDatasDeAtendimento.add(c.getTime());

                        }
                    }
                }
            }

        }


    }

    public void validarInsercaoPaciente() throws ProjetoException {
    	System.out.println("insercao validarInsercaoPaciente");
        GerenciarPacienteController gerenciarPacienteController = new GerenciarPacienteController();
        Date dataSolicitacaoCorreta = gerenciarPacienteController.ajustarDataDeSolicitacao(insercao.getDataSolicitacao(), insercao.getLaudo().getId());
        insercao.setDataSolicitacao(dataSolicitacaoCorreta);

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
    	System.out.println("insercao validarDatas");
        ArrayList<InsercaoPacienteBean> listaAgendamentosAux = new ArrayList<>();

        for (int i = 0; i < listaAgendamentos.size(); i++) {

            if (verificarSeEhFeriadoData(listaAgendamentos.get(i).getAgenda().getDataMarcacao())) {
                listaAgendamentosAux.add(listaAgendamentos.get(i));
            }
            //else if (verificarSeTemBloqueioData(listaAgendamentos.get(i), turno)) {
              //  listaAgendamentosAux.add(listaAgendamentos.get(i));
            //}
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
    	System.out.println("insercao verificarSeTemBloqueioData");
        Boolean retorno = false;

        retorno = new BloqueioDAO().verificarBloqueioProfissionalDataUnica
                (insercaoBean.getAgenda().getProfissional().getId(), insercaoBean.getAgenda().getDataMarcacao(), turno);

        return retorno;
    }

    public void gravarInsercaoPaciente() throws ProjetoException {
    	System.out.println("insercao gravarInsercaoPaciente");
        if (insercao.getLaudo().getId() != null) {

            Boolean cadastrou = null;

            gerarListaAgendamentosEquipe();

            ArrayList<InsercaoPacienteBean> listaAgendamentosProfissionalFinal = validarDatas(listAgendamentoProfissional, insercao.getAgenda().getTurno());

            if (tipo.equals(TipoAtendimento.EQUIPE.getSigla())) {

                //gerarListaAgendamentosEquipe();

                cadastrou = iDao.gravarInsercaoEquipe(insercao, listaAgendamentosProfissionalFinal, listaLiberacao, listaHorarioFinal);
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
        listaHorariosDaEquipe = agendaDAO.quantidadeDeAgendamentosDaEquipePorTurno();
    }

    // AUTOCOMPLETE INÃ�CIO

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
            return gDao.listarGruposNoAutoComplete(query,
                    this.insercao.getPrograma().getIdPrograma());
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

    public ArrayList<String> getListaHorarios() throws ParseException {
        gerarHorariosAtendimento();
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

    public Boolean getEhAvaliacao() {
        return ehAvaliacao;
    }

    public void setEhAvaliacao(Boolean ehAvaliacao) {
        this.ehAvaliacao = ehAvaliacao;
    }

    public List<HorarioAtendimento> getListaHorarioAtendimentos() {
        return listaHorarioAtendimentos;
    }

    public void setListaHorarioAtendimentos(List<HorarioAtendimento> listaHorarioAtendimentos) {
        this.listaHorarioAtendimentos = listaHorarioAtendimentos;
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
}
