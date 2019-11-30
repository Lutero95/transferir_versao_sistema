package br.gov.al.maceio.sishosp.hosp.control;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import br.gov.al.maceio.sishosp.acl.dao.FuncionarioDAO;
import br.gov.al.maceio.sishosp.acl.model.FuncionarioBean;
import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.HorarioOuTurnoUtil;
import br.gov.al.maceio.sishosp.comum.util.JSFUtil;
import br.gov.al.maceio.sishosp.comum.util.RedirecionarUtil;
import br.gov.al.maceio.sishosp.hosp.dao.AgendaDAO;
import br.gov.al.maceio.sishosp.hosp.dao.EquipeDAO;
import br.gov.al.maceio.sishosp.hosp.dao.GrupoDAO;
import br.gov.al.maceio.sishosp.hosp.dao.InsercaoPacienteDAO;
import br.gov.al.maceio.sishosp.hosp.dao.TransferenciaPacienteDAO;
import br.gov.al.maceio.sishosp.hosp.enums.DiasDaSemana;
import br.gov.al.maceio.sishosp.hosp.enums.OpcaoAtendimento;
import br.gov.al.maceio.sishosp.hosp.enums.TipoAtendimento;
import br.gov.al.maceio.sishosp.hosp.model.AgendaBean;
import br.gov.al.maceio.sishosp.hosp.model.EquipeBean;
import br.gov.al.maceio.sishosp.hosp.model.GerenciarPacienteBean;
import br.gov.al.maceio.sishosp.hosp.model.GrupoBean;
import br.gov.al.maceio.sishosp.hosp.model.HorarioAtendimento;
import br.gov.al.maceio.sishosp.hosp.model.InsercaoPacienteBean;


@ManagedBean(name = "TransferenciaPacienteController")
@ViewScoped
public class TransferenciaPacienteController implements Serializable {

    private static final long serialVersionUID = 1L;
    private TransferenciaPacienteDAO aDao = new TransferenciaPacienteDAO();
    private InsercaoPacienteBean insercao;
    private InsercaoPacienteBean insercaoParaLaudo;
    private ArrayList<String> listaHorarios;
    private String tipo;
    private InsercaoPacienteDAO iDao;
    private ArrayList<HorarioAtendimento> listaHorarioAtendimentos;
    private ArrayList<FuncionarioBean> listaProfissionaisAdicionados;
    private ArrayList<GerenciarPacienteBean> listaDiasProfissional;
    private ArrayList<AgendaBean> listAgendamentoProfissional;
    private Integer id_paciente_insituicao;
    private List<HorarioAtendimento> listaHorarioFinal = new ArrayList<>();
    private String opcaoAtendimento;
    private List<Integer> listaDias;
    private List<HorarioAtendimento> listaHorarioAtendimentosAuxiliar;
    private ArrayList<FuncionarioBean> listaProfissionaisEquipe;
    private EquipeDAO eDao = new EquipeDAO();
    private FuncionarioBean funcionario;
    private Boolean todosOsProfissionais;
    private AgendaDAO agendaDAO = new AgendaDAO();
    private List<AgendaBean> listaHorariosAgenda;
    private List<EquipeBean> listaEquipePorGrupo;

    public TransferenciaPacienteController() {
        insercao = new InsercaoPacienteBean();
        insercaoParaLaudo = new InsercaoPacienteBean();
        listaProfissionaisAdicionados = new ArrayList<FuncionarioBean>();
        funcionario = new FuncionarioBean();
        tipo = "";
        iDao = new InsercaoPacienteDAO();
        listaDiasProfissional = new ArrayList<GerenciarPacienteBean>();
        listAgendamentoProfissional = new ArrayList<AgendaBean>();
        todosOsProfissionais = false;
        listaHorariosAgenda =  new ArrayList<AgendaBean>();
        listaEquipePorGrupo = new ArrayList<>();
        listaHorarioAtendimentos = new ArrayList<>();
        listaHorarios = new ArrayList<>();
        listaHorarioAtendimentosAuxiliar = new ArrayList<>();

    }
    public void carregarTransferencia() throws ProjetoException, ParseException {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Map<String, String> params = facesContext.getExternalContext()
                .getRequestParameterMap();
        listaDias = new ArrayList<>();
        if (params.get("id") != null) {
            Integer id = Integer.parseInt(params.get("id"));
            id_paciente_insituicao = id;
            this.insercao = aDao.carregarPacientesInstituicaoAlteracao(id);
            if (insercao.getLaudo().getId()!=null)
            carregarLaudoPaciente();
            InsercaoPacienteController insercaoPacienteController = new InsercaoPacienteController();
            opcaoAtendimento = insercaoPacienteController.carregarHorarioOuTurno();
            tipo = TipoAtendimento.EQUIPE.getSigla();

        } else {
            JSFUtil.adicionarMensagemErro("Ocorreu um erro!", "Erro");
        }

    }

    public void gerarListaAgendamentosEquipeTurno() throws ProjetoException {
    	Integer codPaciente = null;
    	if ((insercao.getLaudo() != null) && (insercao.getLaudo().getId() != null)) 
    			codPaciente = insercao.getLaudo().getPaciente().getId_paciente();
    	
    	if  ((insercao.getPaciente() != null) && (insercao.getPaciente().getId_paciente() != null))
    		codPaciente = insercao.getPaciente().getId_paciente();
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        df.setLenient(false);
        GerenciarPacienteController gerenciarPacienteController = new GerenciarPacienteController();
        //Date periodoInicial = gerenciarPacienteController.ajustarDataDeSolicitacao(insercao.getDataSolicitacao(), insercao.getLaudo().getId(), insercao.getPaciente().getId_paciente());
        //Date d1 = periodoInicial;
        //Date d2 = iDao.dataFinalLaudo(insercao.getLaudo().getId());
        Date periodoInicial = gerenciarPacienteController.ajustarDataDeSolicitacao(insercao.getDataSolicitacao(), insercao.getLaudo().getId(), insercao.getPaciente().getId_paciente(), insercao.getPrograma().getIdPrograma(), insercao.getGrupo().getIdGrupo());
        Date d1 = periodoInicial;
        Date d2 =null;
        if ((insercao.getLaudo().getId()!=null) && (insercao.getLaudo().getId()!=0))
         d2 = iDao.dataFinalLaudo(insercao.getLaudo().getId());
        else
             d2 = iDao.dataFinalPacienteSemLaudo(insercao, codPaciente);
        Long dt = (d2.getTime() - d1.getTime());

        dt = (dt / 86400000L);

        Calendar c = Calendar.getInstance();
        c.setTime(periodoInicial);

        for (int i = 0; i <= dt; i++) {

            if (i > 0) {
                c.add(Calendar.DAY_OF_MONTH, 1);
            }

            int diaSemana = c.get(Calendar.DAY_OF_WEEK);
            ArrayList<Date> listaDatasDeAtendimento = new ArrayList<Date>();
            if (tipo.equals(TipoAtendimento.EQUIPE.getSigla())) {
                for (int j = 0; j < listaProfissionaisAdicionados.size(); j++) {
                    for (int h = 0; h < listaProfissionaisAdicionados.get(j).getListaDiasAtendimentoSemana().size(); h++) {
                        if (!listaDatasDeAtendimento.contains(c.getTime())) {
                            if (diaSemana ==listaProfissionaisAdicionados.get(j).getListaDiasAtendimentoSemana().get(h).getDiaSemana()) {

                                AgendaBean agenda = new AgendaBean();

                                agenda.setPaciente(
                                        insercao.getLaudo().getPaciente());

                                agenda.setDataMarcacao(c.getTime());

                                agenda.setProfissional(listaProfissionaisAdicionados.get(j));

                                listAgendamentoProfissional.add(agenda);
                                listaDatasDeAtendimento.add(c.getTime());

                            }
                        }
                    }
                }

            }

        }

    }

    public void gerarListaAgendamentosEquipeDiaHorario() throws ProjetoException {

        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        df.setLenient(false);
        GerenciarPacienteController gerenciarPacienteController = new GerenciarPacienteController();
        Date periodoInicial = gerenciarPacienteController.ajustarDataDeSolicitacao(insercao.getDataSolicitacao(), insercao.getLaudo().getId(), insercao.getPaciente().getId_paciente(), insercao.getPrograma().getIdPrograma(), insercao.getGrupo().getIdGrupo());
        Date d1 = periodoInicial;
        Date d2 = iDao.dataFinalLaudo(insercao.getLaudo().getId());
        Long dt = (d2.getTime() - d1.getTime());

        dt = (dt / 86400000L);

        Calendar c = Calendar.getInstance();
        c.setTime(periodoInicial);

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

                            AgendaBean agenda = new AgendaBean();

                            agenda.setPaciente(
                                    insercao.getLaudo().getPaciente());

                            agenda.setDataMarcacao(c.getTime());

                            listAgendamentoProfissional.add(agenda);
                            listaDatasDeAtendimento.add(c.getTime());

                        }
                    }
                }
            }

        }


    }


    public void excluirFuncionarioIhDiasDeAtendimento() {
        funcionario.getListaDiasAtendimentoSemana().remove(funcionario);
        listaProfissionaisAdicionados.remove(funcionario);
    }

    public String gravarAlteracaoPaciente() throws ProjetoException {

        String retorno = "";

        if(validarProfissionaisAdicionados()) {
            Boolean cadastrou = null;

            InsercaoPacienteController insercaoPacienteController = new InsercaoPacienteController();


            GerenciarPacienteController gerenciarPacienteController = new GerenciarPacienteController();
            Date dataSolicitacaoCorreta = gerenciarPacienteController.ajustarDataDeSolicitacao(insercao.getDataSolicitacao(), insercao.getLaudo().getId(), insercao.getPaciente().getId_paciente(), insercao.getPrograma().getIdPrograma(), insercao.getGrupo().getIdGrupo());
            insercao.setDataSolicitacao(dataSolicitacaoCorreta);

            if (opcaoAtendimento.equals(OpcaoAtendimento.SOMENTE_TURNO.getSigla())){
            	gerarListaAgendamentosEquipeTurno();	
            }
            
            if (opcaoAtendimento.equals(OpcaoAtendimento.SOMENTE_HORARIO.getSigla())) {
            	gerarListaAgendamentosEquipeDiaHorario();
            }

            ArrayList<AgendaBean> listaAgendamentosProfissionalFinal = insercaoPacienteController.validarDatas(
                    listAgendamentoProfissional, insercao.getTurno());

            
        	if (opcaoAtendimento.equals(OpcaoAtendimento.SOMENTE_HORARIO.getSigla()))
                cadastrou = aDao.gravarTransferenciaEquipeDiaHorario(insercao,insercaoParaLaudo, listaAgendamentosProfissionalFinal, id_paciente_insituicao, listaProfissionaisAdicionados);
            	else
                    cadastrou = aDao.gravarTransferenciaEquipeTurno(insercao,insercaoParaLaudo,
                    		listaAgendamentosProfissionalFinal, id_paciente_insituicao, listaProfissionaisAdicionados);



            if (cadastrou) {
                JSFUtil.adicionarMensagemSucesso("Transferência realizada com sucesso!", "Sucesso");
                final String ENDERECO_PAGINA_GERENCIAMENTO = "gerenciamentoPacientes?faces-redirect=true";
                retorno = RedirecionarUtil.redirectPagina(ENDERECO_PAGINA_GERENCIAMENTO);
            } else {
                JSFUtil.adicionarMensagemErro("Ocorreu um erro durante o cadastro!", "Erro");
            }
        }
        else{
            JSFUtil.adicionarMensagemErro("Adicione ao menos um profissional com seus dias de atendimento!", "Erro");
        }

        return retorno;
    }

    private Boolean validarProfissionaisAdicionados(){
        if(listaProfissionaisAdicionados.size() == 0){
            return false;
        }
        else{
            return true;
        }
    }

    public void carregarLaudoPaciente() throws ProjetoException {
        insercaoParaLaudo = iDao.carregarLaudoPaciente(insercao.getLaudo()
                .getId());
        insercao = aDao
                .carregarPacientesInstituicaoAlteracao(id_paciente_insituicao);

    }

    public void abrirDialog() {
        if (opcaoAtendimento.equals(OpcaoAtendimento.SOMENTE_TURNO.getSigla())) {
            //funcionario.setListDiasSemana(new ArrayList<>());
            JSFUtil.atualizarComponente("formDiasAtendimentoTurno");
            JSFUtil.abrirDialog("dlgDiasAtendimentoTurno");
        } else {
            limparHorariosProfissional();
            JSFUtil.abrirDialog("dlgDiasAtendimentoHorario");
        }
    }
    
    private void limparHorariosProfissional() {
        insercao.setHorarioAtendimento(new HorarioAtendimento());
        listaHorarioAtendimentos = new ArrayList<>();
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
    
    public void carregaListaEquipePorGrupo()
            throws ProjetoException {
        if (insercao.getGrupo().getIdGrupo() != null) {
            listaEquipePorGrupo = eDao
                    .listarEquipePorGrupo(insercao.getGrupo().getIdGrupo());
        }
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
        	HorarioAtendimento horarioAtendimento = new HorarioAtendimento();
        	horarioAtendimento.setDiaSemana(Integer.parseInt(funcionario.getListDiasSemana().get(i)));
            	funcionario.getListaDiasAtendimentoSemana().add(horarioAtendimento);
            
        }

        for (int i = 0; i < funcionario.getListaDiasAtendimentoSemana().size(); i++) {
            if (funcionario.getListaDiasAtendimentoSemana().get(i).getDiaSemana().toString().equals(DiasDaSemana.DOMINGO.getSigla())) {
                dias = dias + "Domingo";

                if (funcionario.getListaDiasAtendimentoSemana().size() > 1 && funcionario.getListaDiasAtendimentoSemana().size() != i + 1) {
                    dias = dias + ", ";
                }
            }

            if (funcionario.getListaDiasAtendimentoSemana().get(i).getDiaSemana().toString().equals(DiasDaSemana.SEGUNDA.getSigla())) {
                dias = dias + "Segunda";
                if (funcionario.getListaDiasAtendimentoSemana().size() > 1 && funcionario.getListaDiasAtendimentoSemana().size() != i + 1) {
                    dias = dias + ", ";
                }
            }

            if (funcionario.getListaDiasAtendimentoSemana().get(i).getDiaSemana().toString().equals(DiasDaSemana.TERCA.getSigla())) {
                dias = dias + "Terça";
                if (funcionario.getListaDiasAtendimentoSemana().size() > 1 && funcionario.getListaDiasAtendimentoSemana().size() != i + 1) {
                    dias = dias + ", ";
                }
            }

            if (funcionario.getListaDiasAtendimentoSemana().get(i).getDiaSemana().toString().equals(DiasDaSemana.QUARTA.getSigla())) {
                dias = dias + "Quarta";
                if (funcionario.getListaDiasAtendimentoSemana().size() > 1 && funcionario.getListaDiasAtendimentoSemana().size() != i + 1) {
                    dias = dias + ", ";
                }
            }

            if (funcionario.getListaDiasAtendimentoSemana().get(i).getDiaSemana().toString().equals(DiasDaSemana.QUINTA.getSigla())) {
                dias = dias + "Quinta";
                if (funcionario.getListaDiasAtendimentoSemana().size() > 1 && funcionario.getListaDiasAtendimentoSemana().size() != i + 1) {
                    dias = dias + ", ";
                }
            }

            if (funcionario.getListaDiasAtendimentoSemana().get(i).getDiaSemana().toString().equals(DiasDaSemana.SEXTA.getSigla())) {
                dias = dias + "Sexta";
                if (funcionario.getListaDiasAtendimentoSemana().size() > 1 && funcionario.getListaDiasAtendimentoSemana().size() != i + 1) {
                    dias = dias + ", ";
                }
            }

            if (funcionario.getListaDiasAtendimentoSemana().get(i).getDiaSemana().toString().equals(DiasDaSemana.SABADO.getSigla())) {
                dias = dias + "Sábado";
                if (funcionario.getListaDiasAtendimentoSemana().size() > 1 && funcionario.getListaDiasAtendimentoSemana().size() != i + 1) {
                    dias = dias + ", ";
                }
            }

        }
        dias = dias + ".";

        funcionario.setDiasSemana(dias);
        listaProfissionaisAdicionados.add(funcionario);

        JSFUtil.fecharDialog("dlgDiasAtendimento");
    }

    public void adicionarFuncionarioParaEdicao(FuncionarioBean funcionario) {
        String dias = "";

        for (int i = 0; i < funcionario.getListaDiasAtendimentoSemana().size(); i++) {
            if (funcionario.getListaDiasAtendimentoSemana().get(i).getDiaSemana().toString().equals(DiasDaSemana.DOMINGO.getSigla())) {
                dias = dias + "Domingo";

                if (funcionario.getListaDiasAtendimentoSemana().size() > 1 && funcionario.getListaDiasAtendimentoSemana().size() != i + 1) {
                    dias = dias + ", ";
                }
            }

            if (funcionario.getListaDiasAtendimentoSemana().get(i).getDiaSemana().toString().equals(DiasDaSemana.SEGUNDA.getSigla())) {
                dias = dias + "Segunda";
                if (funcionario.getListaDiasAtendimentoSemana().size() > 1 && funcionario.getListaDiasAtendimentoSemana().size() != i + 1) {
                    dias = dias + ", ";
                }
            }

            if (funcionario.getListaDiasAtendimentoSemana().get(i).getDiaSemana().toString().equals(DiasDaSemana.TERCA.getSigla())) {
                dias = dias + "Terça";
                if (funcionario.getListaDiasAtendimentoSemana().size() > 1 && funcionario.getListaDiasAtendimentoSemana().size() != i + 1) {
                    dias = dias + ", ";
                }
            }

            if (funcionario.getListaDiasAtendimentoSemana().get(i).getDiaSemana().toString().equals(DiasDaSemana.QUARTA.getSigla())) {
                dias = dias + "Quarta";
                if (funcionario.getListaDiasAtendimentoSemana().size() > 1 && funcionario.getListaDiasAtendimentoSemana().size() != i + 1) {
                    dias = dias + ", ";
                }
            }

            if (funcionario.getListaDiasAtendimentoSemana().get(i).getDiaSemana().toString().equals(DiasDaSemana.QUINTA.getSigla())) {
                dias = dias + "Quinta";
                if (funcionario.getListaDiasAtendimentoSemana().size() > 1 && funcionario.getListaDiasAtendimentoSemana().size() != i + 1) {
                    dias = dias + ", ";
                }
            }

            if (funcionario.getListaDiasAtendimentoSemana().get(i).getDiaSemana().toString().equals(DiasDaSemana.SEXTA.getSigla())) {
                dias = dias + "Sexta";
                if (funcionario.getListaDiasAtendimentoSemana().size() > 1 && funcionario.getListaDiasAtendimentoSemana().size() != i + 1) {
                    dias = dias + ", ";
                }
            }

            if (funcionario.getListaDiasAtendimentoSemana().get(i).getDiaSemana().toString().equals(DiasDaSemana.SABADO.getSigla())) {
                dias = dias + "Sábado";
                if (funcionario.getListaDiasAtendimentoSemana().size() > 1 && funcionario.getListaDiasAtendimentoSemana().size() != i + 1) {
                    dias = dias + ", ";
                }
            }

        }
        dias = dias + ".";

        funcionario.setDiasSemana(dias);
        listaProfissionaisAdicionados.add(funcionario);

    }

    public List<GrupoBean> listaGrupoAutoComplete(String query)
            throws ProjetoException {

        if (insercao.getPrograma().getIdPrograma() != null) {
            GrupoDAO gDao = new GrupoDAO();
            return gDao.listarGruposNoAutoComplete(query,
                    this.insercao.getPrograma().getIdPrograma());
        } else {
            return null;
        }

    }

    public void listarProfissionaisEquipe() throws ProjetoException {
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
    
    public void verificarAdicionarFuncionarioHorarioDia() {
        if (listaHorarioAtendimentos.size() == 0) {
            JSFUtil.adicionarMensagemAdvertencia("Adicione ao menos 1 dia e horário!", "Advertência");
        } else if (verificarSeHorariosDoFuncionarioJaForamAdicionados()) {
            JSFUtil.adicionarMensagemAdvertencia("Esse funcionário já teve seus horários adicionados!", "Advertência");
        } else {
            adicionarFuncionarioHorarioDia();
        }
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
        funcionario.setListaDiasAtendimentoSemana(listaHorarioAtendimentos);
        listaProfissionaisAdicionados.add(funcionario);

        listaHorarioAtendimentosAuxiliar = new ArrayList<>();
        /*
        for (int i = 0; i < listaHorarioAtendimentos.size(); i++) {
            listaHorarioAtendimentosAuxiliar.add(listaHorarioAtendimentos.get(i));
        }
*/
        for (int i = 0; i < listaHorarioAtendimentos.size(); i++) {
        	listaHorarioFinal.add(listaHorarioAtendimentos.get(i));
        }
        adicionarProfissionalIhHorarioNaLista();
        

        JSFUtil.fecharDialog("dlgDiasAtendimentoHorario");
    }
    
    private void adicionarProfissionalIhHorarioNaLista() {
    	/*
    	        if (listaHorarioFinal.size() == 0) {
    	            for (int i = 0; i < listaHorarioAtendimentosAuxiliar.size(); i++) {
    	                listaHorarioFinal.add(listaHorarioAtendimentosAuxiliar.get(i));
    	            }
    	            for (int i = 0; i < listaHorarioFinal.size(); i++) {
    	                listaHorarioFinal.get(i).setFuncionario(funcionario);
    	            }

    	        } else {
    	            List<HorarioAtendimento> listaClonada = new ArrayList<>();
    	            for (int i = 0; i < listaHorarioFinal.size(); i++) {
    	                listaClonada.add(listaHorarioFinal.get(i));
    	            }

    	            for (int i = 0; i < listaHorarioAtendimentosAuxiliar.size(); i++) {

    	            	if (!verificaSeExisteDiaSemanaEHorario (listaHorarioAtendimentosAuxiliar, listaHorarioAtendimentosAuxiliar.get(i).getDiaSemana(), listaHorarioAtendimentosAuxiliar.get(i).getHorario())) {
    	                    listaClonada.add(listaHorarioAtendimentosAuxiliar.get(i));

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
    	                        if (!listaHorarioFinal.get(j).getFuncionario().getId().contains(listaHorarioAtendimentosAuxiliar.get(i).getFuncionario().getId())) {
    	                            listaHorarioFinal.get(j).setFuncionario(listaHorarioAtendimentosAuxiliar.get(i).getFuncionario());
    	                        }

    	                    }
    	                }
    	            }
    	        }

    	*/
    	        for (int i = 0; i < listaHorarioFinal.size(); i++) {
    	            if (!listaDias.contains(listaHorarioFinal.get(i).getDiaSemana())) {
    	                listaDias.add(listaHorarioFinal.get(i).getDiaSemana());
    	            }
    	        }
    	        
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
    
    private Boolean validarAdicionarFuncionarioDiaHorario() {
        Boolean retorno = false;
        for (int i = 0; i < listaHorarioAtendimentos.size(); i++) {
            if (listaHorarioAtendimentos.get(i).getDiaSemana() == insercao.getHorarioAtendimento().getDiaSemana()) {
                return false;
            }
        }

        return true;
    }
    
    private void gerarHorariosAtendimento() throws ParseException {
        listaHorarios = HorarioOuTurnoUtil.gerarHorariosAtendimento();
    }

    public void selecionarPrograma(){
        insercao.setGrupo(new GrupoBean());
        insercao.setEquipe(new EquipeBean());
    }

    public void selecionarGrupo(){
        insercao.setEquipe(new EquipeBean());
    }

    public void carregaHorariosEquipe() {
        listaHorariosAgenda =  agendaDAO.quantidadeDeAgendamentosDaEquipePorTurno();
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

	public List<AgendaBean> getListaHorariosAgenda() {
		return listaHorariosAgenda;
	}

	public void setListaHorariosAgenda(List<AgendaBean> listaHorariosAgenda) {
		this.listaHorariosAgenda = listaHorariosAgenda;
	}

	public List<EquipeBean> getListaEquipePorGrupo() {
		return listaEquipePorGrupo;
	}

	public void setListaEquipePorGrupo(List<EquipeBean> listaEquipePorGrupo) {
		this.listaEquipePorGrupo = listaEquipePorGrupo;
	}

	public ArrayList<HorarioAtendimento> getListaHorarioAtendimentos() {
		return listaHorarioAtendimentos;
	}

	public void setListaHorarioAtendimentos(ArrayList<HorarioAtendimento> listaHorarioAtendimentos) {
		this.listaHorarioAtendimentos = listaHorarioAtendimentos;
	}

	public ArrayList<String> getListaHorarios() throws ParseException {
		gerarHorariosAtendimento();
		return listaHorarios;
	}

	public void setListaHorarios(ArrayList<String> listaHorarios) {
		this.listaHorarios = listaHorarios;
	}
}
