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
import br.gov.al.maceio.sishosp.comum.util.JSFUtil;
import br.gov.al.maceio.sishosp.hosp.dao.AgendaDAO;
import br.gov.al.maceio.sishosp.hosp.dao.AlteracaoPacienteDAO;
import br.gov.al.maceio.sishosp.hosp.dao.EmpresaDAO;
import br.gov.al.maceio.sishosp.hosp.dao.EquipeDAO;
import br.gov.al.maceio.sishosp.hosp.dao.InsercaoPacienteDAO;
import br.gov.al.maceio.sishosp.hosp.enums.DiasDaSemana;
import br.gov.al.maceio.sishosp.hosp.enums.OpcaoAtendimento;
import br.gov.al.maceio.sishosp.hosp.enums.TipoAtendimento;
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
    private List<Integer> listaDias;
    private InsercaoPacienteDAO iDao;
    private ArrayList<FuncionarioBean> listaProfissionaisAdicionados;
    private ArrayList<GerenciarPacienteBean> listaDiasProfissional;
    private ArrayList<AgendaBean> listAgendamentoProfissional;
    private Integer id_paciente_insituicao;
    private String opcaoAtendimento;
    private ArrayList<FuncionarioBean> listaProfissionaisEquipe;
    private List<AgendaBean> listaHorariosEquipe;
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
        listAgendamentoProfissional = new ArrayList<AgendaBean>();
        listaHorariosEquipe = new ArrayList<AgendaBean>();
        todosOsProfissionais = false;
    }

    public void carregaAlteracao() throws ProjetoException, ParseException {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Map<String, String> params = facesContext.getExternalContext()
                .getRequestParameterMap();
        if (params.get("id") != null) {
            Integer id = Integer.parseInt(params.get("id"));
            id_paciente_insituicao = id;
            this.insercao = aDao.carregarPacientesInstituicaoAlteracao(id);
            if (insercao.getLaudo().getId()!=null)
            carregarLaudoPaciente();
            InsercaoPacienteController insercaoPacienteController = new InsercaoPacienteController();
            opcaoAtendimento = insercaoPacienteController.carregarHorarioOuTurno();
            if (insercao.getEquipe().getCodEquipe() != null
                    && insercao.getEquipe().getCodEquipe() > 0) {
                listaProfissionaisEquipe = agendaDAO.listaProfissionaisAtendimetoParaPacienteInstituicao(id);
                tipo = TipoAtendimento.EQUIPE.getSigla();
                listaDiasProfissional = aDao
                        .listarDiasAtendimentoProfissionalEquipe(id);
                listarProfissionaisEquipe();

                List<FuncionarioBean> listaFuncionarioAuxiliar = agendaDAO.listaProfissionaisIhDiasIhHorariosAtendimetoParaPacienteInstituicao(id);
                for(int i=0; i<listaFuncionarioAuxiliar.size(); i++){
                    adicionarFuncionarioParaEdicao(listaFuncionarioAuxiliar);
                }
            }
            if (insercao.getFuncionario().getId() != null
                    && insercao.getFuncionario().getId() > 0) {
                funcionario.setListDiasSemana(agendaDAO.listaDiasDeAtendimetoParaPacienteInstituicao(id));
                tipo = TipoAtendimento.PROFISSIONAL.getSigla();
                insercao.getFuncionario().setListDiasSemana(
                        aDao.listarDiasAtendimentoProfissional(id));
            }

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
        Date periodoInicial = gerenciarPacienteController.ajustarDataDeSolicitacao(insercao.getDataSolicitacao(), insercao.getLaudo().getId(), insercao.getPaciente().getId_paciente(), insercao.getPrograma().getIdPrograma(), insercao.getGrupo().getIdGrupo());
        Date d1 = periodoInicial;
        Date d2 =null;
        if ((insercao.getLaudo().getId()!=null) && (insercao.getLaudo().getId()!=0))
         d2 = iDao.dataFinalLaudo(insercao.getLaudo().getId());
        else
        {
             Date dataFinalSemLaudo = iDao.dataFinalPacienteSemLaudo(insercao, codPaciente);
             Calendar cal = Calendar.getInstance();
             cal.setTime(dataFinalSemLaudo);
             cal.add(Calendar.MONTH, 2);
             cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
             d2 = cal.getTime();
        }
        

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
                    for (int h = 0; h < listaProfissionaisAdicionados.get(j).getListDiasSemana().size(); h++) {
                        if (!listaDatasDeAtendimento.contains(c.getTime())) {
                            if (diaSemana == Integer.parseInt(listaProfissionaisAdicionados.get(j).getListDiasSemana().get(h))) {

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

    public void excluirFuncionarioIhDiasDeAtendimento(){
        funcionario.getListDiasSemana().remove(funcionario);
        listaProfissionaisAdicionados.remove(funcionario);
    }

    public void gerarListaAgendamentosProfissional() throws ProjetoException {

        Boolean temAtendimento = false;
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        df.setLenient(false);
        Date d1 = insercao.getDataSolicitacao();

        if (aDao.listaAtendimentos(id_paciente_insituicao).size() > 0
                && aDao.listaAtendimentos(id_paciente_insituicao).get(0)
                .getDataAtendimento() != null) {
            for (int i = 0; i < aDao.listaAtendimentos(id_paciente_insituicao)
                    .size(); i++) {
                d1 = aDao.listaAtendimentos(id_paciente_insituicao).get(i)
                        .getDataAtendimento();
                insercao.setDataSolicitacao(d1);
                temAtendimento = true;
            }
        }

        Date d2 = iDao.dataFinalLaudo(insercaoParaLaudo.getLaudo().getId());

        Long dt = (d2.getTime() - d1.getTime());

        dt = (dt / 86400000L);

        Calendar c = Calendar.getInstance();

        // INICIA O ATENDIMENTO 1 DIA APÓS O ÚLTIMO
        if (temAtendimento) {
            c.setTime(insercao.getDataSolicitacao());
            c.add(Calendar.DAY_OF_MONTH, 1);
        } else {
            c.setTime(insercao.getDataSolicitacao());
        }

        for (int i = 0; i <= dt; i++) {
            if (i > 0) {
                c.add(Calendar.DAY_OF_MONTH, 1);
            }

            int diaSemana = c.get(Calendar.DAY_OF_WEEK);

            if (tipo.equals(TipoAtendimento.PROFISSIONAL.getSigla())) {
                for (int j = 0; j < funcionario.getListDiasSemana().size(); j++) {
                    if (diaSemana == Integer.parseInt(funcionario
                            .getListDiasSemana().get(j))) {

                        AgendaBean agenda = new AgendaBean();

                        agenda.setPaciente(
                                insercao.getLaudo().getPaciente());

                        agenda.setDataMarcacao(c.getTime());

                        listAgendamentoProfissional.add(agenda);

                    }
                }
            }

        }

    }

    public void gravarAlteracaoPaciente() throws ProjetoException {

        Boolean cadastrou = null;

        InsercaoPacienteController insercaoPacienteController = new InsercaoPacienteController();

        GerenciarPacienteController gerenciarPacienteController = new GerenciarPacienteController();
        Date dataSolicitacaoCorreta = gerenciarPacienteController.ajustarDataDeSolicitacao(insercao.getDataSolicitacao(), insercao.getLaudo().getId(), insercao.getPaciente().getId_paciente(), insercao.getPrograma().getIdPrograma(), insercao.getGrupo().getIdGrupo());
        insercao.setDataSolicitacao(dataSolicitacaoCorreta);

        ArrayList<AgendaBean> listaAgendamentosProfissionalFinal = new ArrayList<AgendaBean>();
        
        if (tipo.equals(TipoAtendimento.EQUIPE.getSigla())) {

            if (opcaoAtendimento.equals(OpcaoAtendimento.SOMENTE_TURNO.getSigla())){
            	gerarListaAgendamentosEquipeTurno();	
            }
            
            if (opcaoAtendimento.equals(OpcaoAtendimento.SOMENTE_HORARIO.getSigla())) {
            	gerarListaAgendamentosEquipeDiaHorario();
            }
            
            listaAgendamentosProfissionalFinal = insercaoPacienteController.validarDatas(
                    listAgendamentoProfissional, insercao.getTurno());
            
            if (opcaoAtendimento.equals(OpcaoAtendimento.SOMENTE_HORARIO.getSigla()))
            	cadastrou = aDao.gravarAlteracaoEquipeDiaHorario(insercao, insercaoParaLaudo,
                        listaAgendamentosProfissionalFinal, id_paciente_insituicao, listaProfissionaisAdicionados);
            	else
                    cadastrou = aDao.gravarAlteracaoEquipeTurno(insercao, 
                            listaAgendamentosProfissionalFinal, id_paciente_insituicao, listaProfissionaisAdicionados);


        }
        if (tipo.equals(TipoAtendimento.PROFISSIONAL.getSigla())) {

            gerarListaAgendamentosProfissional();

            cadastrou = aDao.gravarAlteracaoProfissional(insercao,
                    insercaoParaLaudo, listaAgendamentosProfissionalFinal,
                    id_paciente_insituicao);
        }

        if (cadastrou) {
            JSFUtil.adicionarMensagemSucesso("Alteração de Equipe cadastrada com sucesso!", "Sucesso");
            JSFUtil.abrirDialog("dlgAlteracaoEfetuada");
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

    public void adicionarFuncionarioParaEdicao(List<FuncionarioBean> listaFuncionarioAuxiliar) {
        String dias = "";
        /*
        for (int i = 0; i < funcionario.getListDiasSemana().size(); i++) {
            if (funcionario.getListDiasSemana().get(i).equals(DiasDaSemana.DOMINGO.getSigla())) {
                dias = dias + "Domingo "+" "+funcionario.getListDiasSemana().get(i).get;

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
        
        */
        
        for (int i = 0; i < listaFuncionarioAuxiliar.size(); i++) {
        	
            if (listaFuncionarioAuxiliar.get(i).getDiaSemana().equals(DiasDaSemana.DOMINGO.getSigla())) {
                dias = dias + "Domingo "+" "+listaFuncionarioAuxiliar.get(i).getHorarioAtendimento();

              //  if(funcionario.getListDiasSemana().size() > 1 && funcionario.getListDiasSemana().size()!=i+1){
              //      dias = dias + ", ";
               // }
            }
            
            if (listaFuncionarioAuxiliar.get(i).getDiaSemana().equals(DiasDaSemana.SEGUNDA.getSigla())) {
                dias = dias + "Segunda "+" "+listaFuncionarioAuxiliar.get(i).getHorarioAtendimento();

              //  if(funcionario.getListDiasSemana().size() > 1 && funcionario.getListDiasSemana().size()!=i+1){
              //      dias = dias + ", ";
               // }
            }

            if (listaFuncionarioAuxiliar.get(i).getDiaSemana().equals(DiasDaSemana.TERCA.getSigla())) {
                dias = dias + "Terça "+" "+listaFuncionarioAuxiliar.get(i).getHorarioAtendimento();

              //  if(funcionario.getListDiasSemana().size() > 1 && funcionario.getListDiasSemana().size()!=i+1){
              //      dias = dias + ", ";
               // }
            }

            if (listaFuncionarioAuxiliar.get(i).getDiaSemana().equals(DiasDaSemana.QUARTA.getSigla())) {
                dias = dias + "Quarta "+" "+listaFuncionarioAuxiliar.get(i).getHorarioAtendimento();

              //  if(funcionario.getListDiasSemana().size() > 1 && funcionario.getListDiasSemana().size()!=i+1){
              //      dias = dias + ", ";
               // }
            }
            
            if (listaFuncionarioAuxiliar.get(i).getDiaSemana().equals(DiasDaSemana.QUINTA.getSigla())) {
                dias = dias + "Quinta"+" "+listaFuncionarioAuxiliar.get(i).getHorarioAtendimento();

              //  if(funcionario.getListDiasSemana().size() > 1 && funcionario.getListDiasSemana().size()!=i+1){
              //      dias = dias + ", ";
               // }
            }
            
            if (listaFuncionarioAuxiliar.get(i).getDiaSemana().equals(DiasDaSemana.SEXTA.getSigla())) {
                dias = dias + "Sexta "+" "+listaFuncionarioAuxiliar.get(i).getHorarioAtendimento();

              //  if(funcionario.getListDiasSemana().size() > 1 && funcionario.getListDiasSemana().size()!=i+1){
              //      dias = dias + ", ";
               // }
            }
            
            if (listaFuncionarioAuxiliar.get(i).getDiaSemana().equals(DiasDaSemana.SABADO.getSigla())) {
                dias = dias + "Sábado "+" "+listaFuncionarioAuxiliar.get(i).getHorarioAtendimento();

              //  if(funcionario.getListDiasSemana().size() > 1 && funcionario.getListDiasSemana().size()!=i+1){
              //      dias = dias + ", ";
               // }
            }
            dias = dias + ".";

            funcionario.setDiasSemana(dias);
            listaProfissionaisAdicionados.add(funcionario);

        }
        
       

    }

    public void listarProfissionaisEquipe() throws ProjetoException {
        if ((insercao.getLaudo().getId() != null) || (insercao.getPaciente().getId_paciente() != null)) {
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
            JSFUtil.adicionarMensagemErro("Carregue um laudo primeiro ou Selecione um Paciente!", "Bloqueio");

        }
    }

    public void visualizarHorariosEquipe() {
    	listaHorariosEquipe = agendaDAO.quantidadeDeAgendamentosDaEquipePorTurno();
         
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

	public List<AgendaBean> getListaHorariosEquipe() {
		return listaHorariosEquipe;
	}

	public void setListaHorariosEquipe(ArrayList<AgendaBean> listaHorariosEquipe) {
		this.listaHorariosEquipe = listaHorariosEquipe;
	}
}
