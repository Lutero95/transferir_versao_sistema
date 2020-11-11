package br.gov.al.maceio.sishosp.hosp.control;

import java.io.Serializable;
import java.sql.SQLException;
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

import br.gov.al.maceio.sishosp.acl.model.FuncionarioBean;
import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.DataUtil;
import br.gov.al.maceio.sishosp.comum.util.HorarioOuTurnoUtil;
import br.gov.al.maceio.sishosp.comum.util.JSFUtil;
import br.gov.al.maceio.sishosp.comum.util.VerificadorUtil;
import br.gov.al.maceio.sishosp.hosp.dao.AgendaDAO;
import br.gov.al.maceio.sishosp.hosp.dao.AlteracaoPacienteDAO;
import br.gov.al.maceio.sishosp.hosp.dao.EmpresaDAO;
import br.gov.al.maceio.sishosp.hosp.dao.EquipeDAO;
import br.gov.al.maceio.sishosp.hosp.dao.InsercaoPacienteDAO;
import br.gov.al.maceio.sishosp.hosp.dao.LaudoDAO;
import br.gov.al.maceio.sishosp.hosp.dao.RenovacaoPacienteDAO;
import br.gov.al.maceio.sishosp.hosp.enums.DiasDaSemana;
import br.gov.al.maceio.sishosp.hosp.enums.OpcaoAtendimento;
import br.gov.al.maceio.sishosp.hosp.enums.RetornoLaudoRenovacao;
import br.gov.al.maceio.sishosp.hosp.enums.TipoAtendimento;
import br.gov.al.maceio.sishosp.hosp.enums.TipoGravacaoHistoricoPaciente;
import br.gov.al.maceio.sishosp.hosp.model.AgendaBean;
import br.gov.al.maceio.sishosp.hosp.model.GerenciarPacienteBean;
import br.gov.al.maceio.sishosp.hosp.model.HorarioAtendimento;
import br.gov.al.maceio.sishosp.hosp.model.InsercaoPacienteBean;
import br.gov.al.maceio.sishosp.hosp.model.LaudoBean;
import br.gov.al.maceio.sishosp.hosp.model.PacienteBean;
import br.gov.al.maceio.sishosp.hosp.model.ProcedimentoBean;
import br.gov.al.maceio.sishosp.hosp.model.ProgramaBean;

@ManagedBean(name = "RenovarPacienteController")
@ViewScoped
public class RenovacaoPacienteController implements Serializable {

    private static final long serialVersionUID = 1L;
    private RenovacaoPacienteDAO rDao = new RenovacaoPacienteDAO();
    private InsercaoPacienteBean insercao;
    private InsercaoPacienteBean insercaoParaLaudo;
    private String tipo;
    private InsercaoPacienteDAO iDao;
    private ArrayList<String> listaHorarios;
    private List<HorarioAtendimento> listaHorarioFinal = new ArrayList<>();
    private ArrayList<GerenciarPacienteBean> listaDiasProfissional;
    private ArrayList<InsercaoPacienteBean> listaLaudosVigentes;
    private ArrayList<AgendaBean> listAgendamentoProfissional;
    private ArrayList<FuncionarioBean> listaProfissionaisEquipe;
    private ArrayList<FuncionarioBean> listaProfissionaisAdicionados;
    private ArrayList<HorarioAtendimento> listaHorarioAtendimentos;
    private Integer id_paciente_insituicao;
    private String opcaoAtendimento;
    private List<Integer> listaDias;
    private EmpresaDAO empresaDAO = new EmpresaDAO();
    private AgendaDAO agendaDAO = new AgendaDAO();
    private AlteracaoPacienteDAO aDao = new AlteracaoPacienteDAO();
    private FuncionarioBean funcionario;
    private LaudoBean laudoAtual;
    private List<AgendaBean> listaHorariosAgenda;
    private EquipeDAO eDao = new EquipeDAO();
    private List<HorarioAtendimento> listaHorarioAtendimentosAuxiliar;
	private LaudoBean laudo;

    public RenovacaoPacienteController() {
        insercao = new InsercaoPacienteBean();
        insercaoParaLaudo = new InsercaoPacienteBean();
        listaProfissionaisAdicionados = new ArrayList<FuncionarioBean>();
        listaLaudosVigentes = new ArrayList<InsercaoPacienteBean>();
        tipo = "";
        iDao = new InsercaoPacienteDAO();
        listaDiasProfissional = new ArrayList<GerenciarPacienteBean>();
        listAgendamentoProfissional = new ArrayList<AgendaBean>();
        funcionario = new FuncionarioBean();
        laudoAtual = new LaudoBean();
        listaHorariosAgenda =  new ArrayList<AgendaBean>();
        listaHorarios = new ArrayList<>();
        listaHorarioAtendimentos = new ArrayList<>();
        listaHorarioAtendimentosAuxiliar = new ArrayList<>();
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
    
    public void adicionarFuncionarioDiaHorario() {
        if (validarAdicionarFuncionarioDiaHorario()) {
            insercao.getHorarioAtendimento().setFuncionario(funcionario);
            listaHorarioAtendimentos.add(insercao.getHorarioAtendimento());
            insercao.setHorarioAtendimento(new HorarioAtendimento());
        } else {
            JSFUtil.adicionarMensagemAdvertencia("Esse dia já foi adicionado!", "Advertência");
        }
    }

    public void setaRenovacaoPacienteComOuSemLaudo() {
    	if (insercaoParaLaudo.isInsercaoPacienteSemLaudo()==true) {
    		insercaoParaLaudo.getLaudo().setId(null);
    		insercaoParaLaudo.setPaciente(laudoAtual.getPaciente());
    		insercaoParaLaudo.setProcedimentoPrimarioSemLaudo(laudoAtual.getProcedimentoPrimario());
    		insercaoParaLaudo.setProcedimentoSecundario1SemLaudo(laudoAtual.getProcedimentoSecundario1());
    		insercaoParaLaudo.setProcedimentoSecundario2SemLaudo(laudoAtual.getProcedimentoSecundario2());
    		insercaoParaLaudo.setProcedimentoSecundario3SemLaudo(laudoAtual.getProcedimentoSecundario3());
    		insercaoParaLaudo.setProcedimentoSecundario4SemLaudo(laudoAtual.getProcedimentoSecundario4());
    		insercaoParaLaudo.setProcedimentoSecundario5SemLaudo(laudoAtual.getProcedimentoSecundario5());
    	}
    	else{
    		insercaoParaLaudo.setPaciente(null);
    		insercaoParaLaudo.setProcedimentoPrimarioSemLaudo(new ProcedimentoBean());
    		insercaoParaLaudo.setProcedimentoSecundario1SemLaudo(new ProcedimentoBean());
    		insercaoParaLaudo.setProcedimentoSecundario2SemLaudo(new ProcedimentoBean());
    		insercaoParaLaudo.setProcedimentoSecundario3SemLaudo(new ProcedimentoBean());
    		insercaoParaLaudo.setProcedimentoSecundario4SemLaudo(new ProcedimentoBean());
    		insercaoParaLaudo.setProcedimentoSecundario5SemLaudo(new ProcedimentoBean());
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
                dias = dias + "Domingo " +  (listaHorarioAtendimentos.get(i).getHorario() !=null ? listaHorarioAtendimentos.get(i).getHorario() : "") ;

                if (listaHorarioAtendimentos.size() > 1 && listaHorarioAtendimentos.size() != i + 1) {
                    dias = dias + ", ";
                }
            }

            if (listaHorarioAtendimentos.get(i).getDiaSemana().toString().equals(DiasDaSemana.SEGUNDA.getSigla())) {
                dias = dias + "Segunda " + (listaHorarioAtendimentos.get(i).getHorario() !=null ? listaHorarioAtendimentos.get(i).getHorario() : "") ;
                if (listaHorarioAtendimentos.size() > 1 && listaHorarioAtendimentos.size() != i + 1) {
                    dias = dias + ", ";
                }
            }

            if (listaHorarioAtendimentos.get(i).getDiaSemana().toString().equals(DiasDaSemana.TERCA.getSigla())) {
                dias = dias + "Terça " +  (listaHorarioAtendimentos.get(i).getHorario() !=null ? listaHorarioAtendimentos.get(i).getHorario() : "") ;
                if (listaHorarioAtendimentos.size() > 1 && listaHorarioAtendimentos.size() != i + 1) {
                    dias = dias + ", ";
                }
            }

            if (listaHorarioAtendimentos.get(i).getDiaSemana().toString().equals(DiasDaSemana.QUARTA.getSigla())) {
                dias = dias + "Quarta " + (listaHorarioAtendimentos.get(i).getHorario() !=null ? listaHorarioAtendimentos.get(i).getHorario() : "") ;
                if (listaHorarioAtendimentos.size() > 1 && listaHorarioAtendimentos.size() != i + 1) {
                    dias = dias + ", ";
                }
            }

            if (listaHorarioAtendimentos.get(i).getDiaSemana().toString().equals(DiasDaSemana.QUINTA.getSigla())) {
                dias = dias + "Quinta " +  (listaHorarioAtendimentos.get(i).getHorario() !=null ? listaHorarioAtendimentos.get(i).getHorario() : "") ;
                if (listaHorarioAtendimentos.size() > 1 && listaHorarioAtendimentos.size() != i + 1) {
                    dias = dias + ", ";
                }
            }

            if (listaHorarioAtendimentos.get(i).getDiaSemana().toString().equals(DiasDaSemana.SEXTA.getSigla())) {
                dias = dias + "Sexta " + (listaHorarioAtendimentos.get(i).getHorario() !=null ? listaHorarioAtendimentos.get(i).getHorario() : "") ;
                if (listaHorarioAtendimentos.size() > 1 && listaHorarioAtendimentos.size() != i + 1) {
                    dias = dias + ", ";
                }
            }

            if (listaHorarioAtendimentos.get(i).getDiaSemana().toString().equals(DiasDaSemana.SABADO.getSigla())) {
                dias = dias + "Sábado " + (listaHorarioAtendimentos.get(i).getHorario() !=null ? listaHorarioAtendimentos.get(i).getHorario() : "") ;
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
    

    
    
    public void carregaRenovacao() throws ProjetoException, ParseException {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Map<String, String> params = facesContext.getExternalContext()
                .getRequestParameterMap();
        listaDias = new ArrayList<>();
        if (params.get("id") != null) {
            Integer id = Integer.parseInt(params.get("id"));
            id_paciente_insituicao = id;
            this.insercao = aDao.carregarPacientesInstituicaoAlteracao(id);
            this.laudo = this.insercao.getLaudo();
            //vou setar nulo para a data de solicitacao, pois numa nova renovacao nao é pra carregar a data de solicitacao,
            //pois a mesma é informada na hora do lancamento pelo profissional. 
            //Esta acao é para nao criar um novo metodo especifico para carregar os dados de gerenc. paciente
            insercao.setDataSolicitacao(null);
            InsercaoPacienteController insercaoPacienteController = new InsercaoPacienteController();
            opcaoAtendimento = insercaoPacienteController.carregarHorarioOuTurno();
            laudoAtual = new LaudoDAO().buscarLaudosPorId(insercao.getLaudo().getId());
            if (insercao.getEquipe().getCodEquipe() != null
                    && insercao.getEquipe().getCodEquipe() > 0) {
            	listarProfissionaisEquipe() ;
                tipo = TipoAtendimento.EQUIPE.getSigla();
                listaDiasProfissional = aDao
                        .listarDiasAtendimentoProfissionalEquipe(id);

                List<FuncionarioBean> listaFuncionarioAuxiliar = agendaDAO.listaProfissionaisIhDiasIhHorariosAtendimetoParaPacienteInstituicao(id);
                for(int i=0; i<listaFuncionarioAuxiliar.size(); i++){
                    adicionarFuncionarioParaRenovacao(listaFuncionarioAuxiliar.get(i));
                }
            }
            if (insercao.getFuncionario().getId() != null
                    && insercao.getFuncionario().getId() > 0) {
                funcionario.setListaDiasAtendimentoSemana(agendaDAO.listaDiasDeAtendimetoParaPacienteInstituicao(id));
                tipo = TipoAtendimento.PROFISSIONAL.getSigla();
                insercao.getFuncionario().setListaDiasAtendimentoSemana(
                        aDao.listarDiasAtendimentoProfissional(id));
            }

        } else {
            JSFUtil.adicionarMensagemErro("Ocorreu um erro!", "Erro");
        }

    }
    
    public void listarProfissionaisEquipe() throws ProjetoException {
        if ((insercao.getLaudo().getId() != null) || (insercao.getPaciente().getId_paciente() != null)) {
            if (insercao.getEquipe() != null) {
                if (insercao.getEquipe().getCodEquipe() != null) {
                    listaProfissionaisEquipe = eDao
                            .listarProfissionaisDaEquipeInsercao(insercao.getEquipe()
                                    .getCodEquipe(), false);
                }
            } else {
                JSFUtil.adicionarMensagemErro("Escolha uma equipe!",
                        "Bloqueio");
            }
        } else {
            JSFUtil.adicionarMensagemErro("Carregue um laudo primeiro ou Selecione um Paciente!", "Bloqueio");

        }
    }
    
    public void gerarListaAgendamentosEquipeTurno() throws ProjetoException {
    	Integer codPaciente = null;
    	if ((insercaoParaLaudo.getLaudo() != null) && (insercaoParaLaudo.getLaudo().getId() != null))
    			codPaciente = insercaoParaLaudo.getLaudo().getPaciente().getId_paciente();
    	
    	if  ((insercaoParaLaudo.getPaciente() != null) && (insercaoParaLaudo.getPaciente().getId_paciente() != null))
    		codPaciente = insercaoParaLaudo.getPaciente().getId_paciente();
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        df.setLenient(false);
        GerenciarPacienteController gerenciarPacienteController = new GerenciarPacienteController();
        Date periodoInicial = gerenciarPacienteController.ajustarDataDeSolicitacao(insercao.getDataSolicitacao(), insercaoParaLaudo.getLaudo().getId(), codPaciente, insercao.getPrograma().getIdPrograma(), insercao.getGrupo().getIdGrupo());
        Date d1 = periodoInicial;
        Date d2 =null;
        if ((insercaoParaLaudo.getLaudo().getId()!=null) && (insercaoParaLaudo.getLaudo().getId()!=0))
         d2 = iDao.dataFinalLaudo(insercaoParaLaudo.getLaudo().getId());
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
                    for (int h = 0; h < listaProfissionaisAdicionados.get(j).getListaDiasAtendimentoSemana().size(); h++) {
                        if (!listaDatasDeAtendimento.contains(c.getTime())) {
                            if (diaSemana == listaProfissionaisAdicionados.get(j).getListaDiasAtendimentoSemana().get(h).getDiaSemana()) {

                                AgendaBean agenda = new AgendaBean();

                                agenda.setPaciente(
                                        insercao.getLaudo().getPaciente());

                                agenda.setDataAtendimento(c.getTime());

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
    
    public Boolean verificaPeriodoValidoRenovacaoLaudo() throws ProjetoException {
    	Boolean rst = false;
    	Integer codPaciente = null;
    	if ((insercaoParaLaudo.getLaudo() != null) && (insercaoParaLaudo.getLaudo().getId() != null)) 
    			codPaciente = insercaoParaLaudo.getLaudo().getPaciente().getId_paciente();
    	
    	if  ((insercaoParaLaudo.getPaciente() != null) && (insercaoParaLaudo.getPaciente().getId_paciente() != null))
    		codPaciente = insercaoParaLaudo.getPaciente().getId_paciente();
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        df.setLenient(false);
        GerenciarPacienteController gerenciarPacienteController = new GerenciarPacienteController();
        Date periodoInicial = gerenciarPacienteController.ajustarDataDeSolicitacao(insercao.getDataSolicitacao(), insercaoParaLaudo.getLaudo().getId(), codPaciente, insercao.getPrograma().getIdPrograma(), insercao.getGrupo().getIdGrupo());
        Date d1 = periodoInicial;
        Date d2 =null;
        if ((insercaoParaLaudo.getLaudo().getId()!=null) && (insercaoParaLaudo.getLaudo().getId()!=0))
         d2 = iDao.dataFinalLaudo(insercaoParaLaudo.getLaudo().getId());
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
        
        if (dt<0) {
        	rst = false;
        	JSFUtil.adicionarMensagemErro("Não é possível renovar este Paciente! /n A data de renovação está fora do intervalo do Laudo", "Atenção");
        }
        else
        {
        	rst = true;
        }

        return rst;

    }



    public void gerarListaAgendamentosEquipeDiaHorario() throws ProjetoException {
    	Integer codPaciente = null;
    	if ((insercaoParaLaudo.getLaudo() != null) && (insercaoParaLaudo.getLaudo().getId() != null)) 
    			codPaciente = insercaoParaLaudo.getLaudo().getPaciente().getId_paciente();
    	
    	if  ((insercaoParaLaudo.getPaciente() != null) && (insercaoParaLaudo.getPaciente().getId_paciente() != null))
    		codPaciente = insercaoParaLaudo.getPaciente().getId_paciente();
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        df.setLenient(false);
        GerenciarPacienteController gerenciarPacienteController = new GerenciarPacienteController();
        Date periodoInicial = gerenciarPacienteController.ajustarDataDeSolicitacao(insercao.getDataSolicitacao(), insercaoParaLaudo.getLaudo().getId(), codPaciente, insercao.getPrograma().getIdPrograma(), insercao.getGrupo().getIdGrupo());
        Date d1 = periodoInicial;
        Date d2 =null;
        if ((insercaoParaLaudo.getLaudo().getId()!=null) && (insercaoParaLaudo.getLaudo().getId()!=0))
            d2 = iDao.dataFinalLaudo(insercaoParaLaudo.getLaudo().getId());
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
                for (int h = 0; h < listaDias.size(); h++) {
                    if (!listaDatasDeAtendimento.contains(c.getTime())) {
                        if (diaSemana == listaDias.get(h)) {

                            AgendaBean agenda = new AgendaBean();

                            agenda.setPaciente(
                                    insercao.getLaudo().getPaciente());

                            agenda.setDataAtendimento(c.getTime());

                            listAgendamentoProfissional.add(agenda);
                            listaDatasDeAtendimento.add(c.getTime());

                        }
                    }
                }
            }

        }


    }
    



    public void gerarListaAgendamentosProfissional() throws ProjetoException {

        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        df.setLenient(false);
        Date d1 = insercao.getDataSolicitacao();
        Date d2 = iDao.dataFinalLaudo(insercaoParaLaudo.getLaudo().getId());
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
                        .getListaDiasAtendimentoSemana().size(); j++) {

                    if (diaSemana == insercao.getFuncionario()
                            .getListaDiasAtendimentoSemana().get(j).getDiaSemana()) {

                        AgendaBean agenda = new AgendaBean();

                        agenda.setPaciente(
                                insercao.getLaudo().getPaciente());

                        agenda.setDataAtendimento(c.getTime());

                        listAgendamentoProfissional.add(agenda);

                    }
                }

            }

        }

    }

    public void abrirDialog() throws ParseException, ProjetoException { 
        if (opcaoAtendimento.equals(OpcaoAtendimento.SOMENTE_TURNO.getSigla())) {
            funcionario.setListDiasSemana(new ArrayList<>());
            JSFUtil.atualizarComponente("formDiasAtendimentoTurno");
            JSFUtil.abrirDialog("dlgDiasAtendimentoTurno");
        } else {
        	gerarHorariosAtendimento();
            limparHorariosProfissional();
            JSFUtil.abrirDialog("dlgDiasAtendimentoHorario");
        }
    }
    
    private void limparHorariosProfissional() {
        insercao.setHorarioAtendimento(new HorarioAtendimento());
        listaHorarioAtendimentos = new ArrayList<>();
    }
    
    private void gerarHorariosAtendimento() throws ParseException, ProjetoException {
        listaHorarios = HorarioOuTurnoUtil.gerarHorariosAtendimento();
    }
    
    public void excluirDiasHorariosDoFuncionario(HorarioAtendimento horarioAtendimento) {
        listaHorarioAtendimentos.remove(horarioAtendimento);
    }

    public void adicionarFuncionarioParaRenovacao(FuncionarioBean funcionario) {
        String dias = "";

        for (int i = 0; i < funcionario.getListaDiasAtendimentoSemana().size(); i++) {
            if (funcionario.getListaDiasAtendimentoSemana().get(i).getDiaSemana().toString().equals(DiasDaSemana.DOMINGO.getSigla())) {
                dias = dias + "Domingo";

                if(funcionario.getListaDiasAtendimentoSemana().size() > 1 && funcionario.getListaDiasAtendimentoSemana().size()!=i+1){
                    dias = dias + ", ";
                }
            }

            if (funcionario.getListaDiasAtendimentoSemana().get(i).getDiaSemana().toString().equals(DiasDaSemana.SEGUNDA.getSigla())) {
                dias = dias + "Segunda";
                if(funcionario.getListaDiasAtendimentoSemana().size() > 1 && funcionario.getListaDiasAtendimentoSemana().size()!=i+1){
                    dias = dias + ", ";
                }
            }

            if (funcionario.getListaDiasAtendimentoSemana().get(i).getDiaSemana().toString().equals(DiasDaSemana.TERCA.getSigla())) {
                dias = dias + "Terça";
                if(funcionario.getListaDiasAtendimentoSemana().size() > 1 && funcionario.getListaDiasAtendimentoSemana().size()!=i+1){
                    dias = dias + ", ";
                }
            }

            if (funcionario.getListaDiasAtendimentoSemana().get(i).getDiaSemana().toString().equals(DiasDaSemana.QUARTA.getSigla())) {
                dias = dias + "Quarta";
                if(funcionario.getListaDiasAtendimentoSemana().size() > 1 && funcionario.getListaDiasAtendimentoSemana().size()!=i+1){
                    dias = dias + ", ";
                }
            }

            if (funcionario.getListaDiasAtendimentoSemana().get(i).getDiaSemana().toString().equals(DiasDaSemana.QUINTA.getSigla())) {
                dias = dias + "Quinta";
                if(funcionario.getListaDiasAtendimentoSemana().size() > 1 && funcionario.getListaDiasAtendimentoSemana().size()!=i+1){
                    dias = dias + ", ";
                }
            }

            if (funcionario.getListaDiasAtendimentoSemana().get(i).getDiaSemana().toString().equals(DiasDaSemana.SEXTA.getSigla())) {
                dias = dias + "Sexta";
                if(funcionario.getListaDiasAtendimentoSemana().size() > 1 && funcionario.getListaDiasAtendimentoSemana().size()!=i+1){
                    dias = dias + ", ";
                }
            }

            if (funcionario.getListaDiasAtendimentoSemana().get(i).getDiaSemana().toString().equals(DiasDaSemana.SABADO.getSigla())) {
                dias = dias + "Sábado";
                if(funcionario.getListaDiasAtendimentoSemana().size() > 1 && funcionario.getListaDiasAtendimentoSemana().size()!=i+1){
                    dias = dias + ", ";
                }
            }

        }
        dias = dias + ".";

        funcionario.setDiasSemana(dias);
        listaProfissionaisAdicionados.add(funcionario);
        
        for (int i = 0; i < funcionario.getListaDiasAtendimentoSemana().size(); i++) {
            if (!listaDias.contains(funcionario.getListaDiasAtendimentoSemana().get(i).getDiaSemana())) {
                listaDias.add(funcionario.getListaDiasAtendimentoSemana().get(i).getDiaSemana());
            }
        }

    }

    public void excluirFuncionarioIhDiasDeAtendimento(){
        funcionario.getListaDiasAtendimentoSemana().remove(funcionario);
        listaProfissionaisAdicionados.remove(funcionario);
    }

    public void gravarRenovacaoPaciente() throws ProjetoException, SQLException {
        listAgendamentoProfissional = new ArrayList<AgendaBean>();
		InsercaoPacienteController insercaoPacienteController = new InsercaoPacienteController();
		
        if (((insercaoParaLaudo.getLaudo() != null) && (insercaoParaLaudo.getLaudo().getId() != null)) || ((insercaoParaLaudo.getPaciente() != null) && (insercaoParaLaudo.getPaciente().getId_paciente() != null))) {
        	
        	PacienteBean pacienteAux;
        	if(insercaoParaLaudo.isInsercaoPacienteSemLaudo())
        		pacienteAux = insercaoParaLaudo.getPaciente();
        	else
        		pacienteAux = insercaoParaLaudo.getLaudo().getPaciente();
        	
			if (verificaPeriodoValidoRenovacaoLaudo() && 
					insercaoPacienteController.procedimentoValido(retornaProcedimentoLaudo(), insercao.getPrograma(), insercao.getGrupo(), pacienteAux)) {
																					// Verifica se a data de renovacao
																					// está dentro do periodo do laudo
																					// ou da nova solicitacao sem laudo
				Integer codPaciente = null;

				if ((insercaoParaLaudo.getLaudo() != null) && (insercaoParaLaudo.getLaudo().getId() != null)) {
					codPaciente = insercaoParaLaudo.getLaudo().getPaciente().getId_paciente();
				}

				if ((insercaoParaLaudo.getPaciente() != null)
						&& (insercaoParaLaudo.getPaciente().getId_paciente() != null)) {
					codPaciente = insercaoParaLaudo.getPaciente().getId_paciente();
				}

				Boolean cadastrou = null;

				
				if (dataInclusaoPacienteEstaEntreDataInicialIhFinalDoLaudo()) {
					GerenciarPacienteController gerenciarPacienteController = new GerenciarPacienteController();
					Date dataSolicitacaoCorreta = gerenciarPacienteController.ajustarDataDeSolicitacao(
							insercao.getDataSolicitacao(), insercaoParaLaudo.getLaudo().getId(), codPaciente,
							insercao.getPrograma().getIdPrograma(), insercao.getGrupo().getIdGrupo());
					insercao.setDataSolicitacao(dataSolicitacaoCorreta);

					ArrayList<AgendaBean> listaAgendamentosProfissionalFinal = insercaoPacienteController
							.validarDatas(listAgendamentoProfissional, insercao.getTurno());

					if (tipo.equals(TipoAtendimento.EQUIPE.getSigla())) {

						if (opcaoAtendimento.equals(OpcaoAtendimento.SOMENTE_TURNO.getSigla())) {
							gerarListaAgendamentosEquipeTurno();
							cadastrou = rDao.gravarRenovacaoEquipeTurno(insercao, insercaoParaLaudo,
									listaProfissionaisAdicionados, listaAgendamentosProfissionalFinal);
						}

						if (opcaoAtendimento.equals(OpcaoAtendimento.SOMENTE_HORARIO.getSigla())) {
							gerarListaAgendamentosEquipeDiaHorario();
							cadastrou = rDao.gravarRenovacaoEquipeDiaHorario(insercao, insercaoParaLaudo,
									listaAgendamentosProfissionalFinal, listaProfissionaisAdicionados);
						}
					}
					if (tipo.equals(TipoAtendimento.PROFISSIONAL.getSigla())) {
						gerarListaAgendamentosProfissional();
						cadastrou = rDao.gravarInsercaoProfissional(insercao, insercaoParaLaudo,
								listaAgendamentosProfissionalFinal);
					}

					if (cadastrou == true) {
						JSFUtil.adicionarMensagemSucesso("Renovação de Paciente cadastrada com sucesso!", "Sucesso");
						JSFUtil.abrirDialog("dlgRenovacaoEfetuada");
					} else {
						JSFUtil.adicionarMensagemErro("Ocorreu um erro durante o cadastro!", "Erro");
					}
				}
			}
        } else {
            JSFUtil.adicionarMensagemAdvertencia("Carregue um laudo ou selecione um Paciente!", "Bloqueio");
        }
    }
    
    private boolean dataInclusaoPacienteEstaEntreDataInicialIhFinalDoLaudo() throws ProjetoException {
        boolean dataValida = true;
        
		if ((!VerificadorUtil.verificarSeObjetoNulo(insercaoParaLaudo.getLaudo()))
				&& (!VerificadorUtil.verificarSeObjetoNulo(insercaoParaLaudo.getLaudo().getId()))
				&& (!insercaoParaLaudo.isInsercaoPacienteSemLaudo())) {
	
			dataValida = iDao.dataInclusaoPacienteEstaEntreDataInicialIhFinalDoLaudo
					(insercaoParaLaudo.getLaudo().getId(), insercao.getDataSolicitacao());
		} else if ((insercaoParaLaudo.isInsercaoPacienteSemLaudo())) {
			dataValida = validarDataLaudoPacienteSemLaudo();
		} else
			dataValida = true;
        if(!dataValida)
            JSFUtil.adicionarMensagemErro("Data de Inclusão não está dentro do intervalo da data do laudo", "Erro!");
        return dataValida;
    }
    
    private boolean validarDataLaudoPacienteSemLaudo() throws ProjetoException {
        boolean resultado = false;
        Date dataSolicitacaoPacienteTerapia = insercao.getDataSolicitacao();
        Date dataVigenciaFinalLaudo = insercao.getLaudo().getVigenciaFinal();

        if (dataVigenciaFinalLaudo.before(dataSolicitacaoPacienteTerapia))
            JSFUtil.adicionarMensagemErro("A data do laudo selecionado é menor que a data de inclusão do paciente na Terapia", "Erro!");
        else
            resultado = true;
        return resultado;
    }

	private ProcedimentoBean retornaProcedimentoLaudo() {
		ProcedimentoBean procedimentoLaudo = null;
		if(insercaoParaLaudo.isInsercaoPacienteSemLaudo())
			procedimentoLaudo = insercaoParaLaudo.getProcedimentoPrimarioSemLaudo();
		else
			procedimentoLaudo = insercaoParaLaudo.getLaudo().getProcedimentoPrimario();
		return procedimentoLaudo;
	}

    public void listarLaudosVigentes()
            throws ProjetoException {
        LaudoDAO laudoDAO = new LaudoDAO();
        listaLaudosVigentes = laudoDAO.listarLaudosVigentesParaPaciente(insercao.getLaudo().getPaciente().getId_paciente());
    }

    public void carregaHorariosEquipe() throws ProjetoException {
        listaHorariosAgenda =  agendaDAO.quantidadeDeAgendamentosDaEquipePorTurno();
    }


    public void carregarLaudoPaciente() throws ProjetoException {

//        String condicao_datas_laudo = compararDatasLaudo(laudoAtual.getMesFinal(), laudoAtual.getAnoFinal(),
//                insercao.getLaudo().getMesInicio(), insercao.getLaudo().getAnoInicio());
        
        String condicao_datas_laudo = compararDatasLaudo(laudoAtual.getMesFinal(), laudoAtual.getAnoFinal(),
                insercao.getLaudo().getVigenciaInicial());

        if(condicao_datas_laudo.equals(RetornoLaudoRenovacao.DATA_ATUAL_MAIOR_QUE_NOVA_DATA.getSigla())){
            JSFUtil.adicionarMensagemErro("A data do novo laudo é menor que a data do laudo atual", "Erro!");
        }
        else if(condicao_datas_laudo.equals(RetornoLaudoRenovacao.MAIS_DE_UM_MES.getSigla())){
            JSFUtil.adicionarMensagemAdvertencia("O novo laudo tem início com mais de 30 dias a mais que o término do que o lado atual!", "Observação!");
            insercaoParaLaudo = iDao.carregarLaudoPaciente(insercao.getLaudo()
                    .getId());
            insercao = rDao
                    .carregarPacientesInstituicaoRenovacao(id_paciente_insituicao);
        }
        else{
            insercaoParaLaudo = iDao.carregarLaudoPaciente(insercao.getLaudo().getId());
            insercao = rDao.carregarPacientesInstituicaoRenovacao(id_paciente_insituicao);
        }
    }

    public String compararDatasLaudo(Integer mesAtual, Integer anoAtual, Date dataLaudoNovo){

        String retorno = "";

        //final Boolean INICIO_MES = true;
        final Boolean FIM_MES = false;

        Date dataLaudoAtual = DataUtil.adicionarMesIhAnoEmDate(mesAtual, anoAtual, FIM_MES);
        //Date dataLaudoNovo = DataUtil.adicionarMesIhAnoEmDate(mesNovo, anoNovo, INICIO_MES);

        long totalDiasDataLaudoAtual = DataUtil.calcularQuantidadeDeDiasDeUmaData(dataLaudoAtual);
        long totalDiasDataLaudoNovo = DataUtil.calcularQuantidadeDeDiasDeUmaData(dataLaudoNovo);

        long totalDiasGeral = totalDiasDataLaudoNovo - totalDiasDataLaudoAtual;

        if(totalDiasGeral <= 0){
            retorno = RetornoLaudoRenovacao.DATA_ATUAL_MAIOR_QUE_NOVA_DATA.getSigla();
        }
        else if(totalDiasGeral > 30){
            retorno = RetornoLaudoRenovacao.MAIS_DE_UM_MES.getSigla();
        }
        else{
            retorno = RetornoLaudoRenovacao.DATA_OK.getSigla();
        }

        return retorno;

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
                JSFUtil.fecharDialog("dlgDiasAtendimentoTurno");

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
                dias = dias + "Domingo "+(funcionario.getListaDiasAtendimentoSemana().get(i).getHorario()!=null ? funcionario.getListaDiasAtendimentoSemana().get(i).getHorario() :"") ;

                if(funcionario.getListaDiasAtendimentoSemana().size() > 1 && funcionario.getListaDiasAtendimentoSemana().size()!=i+1){
                    dias = dias + ", ";
                }
            }

            if (funcionario.getListaDiasAtendimentoSemana().get(i).getDiaSemana().toString().equals(DiasDaSemana.SEGUNDA.getSigla())) {
                dias = dias + "Segunda "+(funcionario.getListaDiasAtendimentoSemana().get(i).getHorario()!=null ? funcionario.getListaDiasAtendimentoSemana().get(i).getHorario() :"");
                if(funcionario.getListaDiasAtendimentoSemana().size() > 1 && funcionario.getListaDiasAtendimentoSemana().size()!=i+1){
                    dias = dias + ", ";
                }
            }

            if (funcionario.getListaDiasAtendimentoSemana().get(i).getDiaSemana().toString().equals(DiasDaSemana.TERCA.getSigla())) {
                dias = dias + "Terça "+(funcionario.getListaDiasAtendimentoSemana().get(i).getHorario()!=null ? funcionario.getListaDiasAtendimentoSemana().get(i).getHorario() :"");
                if(funcionario.getListaDiasAtendimentoSemana().size() > 1 && funcionario.getListaDiasAtendimentoSemana().size()!=i+1){
                    dias = dias + ", ";
                }
            }

            if (funcionario.getListaDiasAtendimentoSemana().get(i).getDiaSemana().toString().equals(DiasDaSemana.QUARTA.getSigla())) {
                dias = dias + "Quarta "+(funcionario.getListaDiasAtendimentoSemana().get(i).getHorario()!=null ? funcionario.getListaDiasAtendimentoSemana().get(i).getHorario() :"");
                if(funcionario.getListaDiasAtendimentoSemana().size() > 1 && funcionario.getListaDiasAtendimentoSemana().size()!=i+1){
                    dias = dias + ", ";
                }
            }

            if (funcionario.getListaDiasAtendimentoSemana().get(i).getDiaSemana().toString().equals(DiasDaSemana.QUINTA.getSigla())) {
                dias = dias + "Quinta "+(funcionario.getListaDiasAtendimentoSemana().get(i).getHorario()!=null ? funcionario.getListaDiasAtendimentoSemana().get(i).getHorario() :"");
                if(funcionario.getListaDiasAtendimentoSemana().size() > 1 && funcionario.getListaDiasAtendimentoSemana().size()!=i+1){
                    dias = dias + ", ";
                }
            }

            if (funcionario.getListaDiasAtendimentoSemana().get(i).getDiaSemana().toString().equals(DiasDaSemana.SEXTA.getSigla())) {
                dias = dias + "Sexta "+(funcionario.getListaDiasAtendimentoSemana().get(i).getHorario()!=null ? funcionario.getListaDiasAtendimentoSemana().get(i).getHorario() :"");
                if(funcionario.getListaDiasAtendimentoSemana().size() > 1 && funcionario.getListaDiasAtendimentoSemana().size()!=i+1){
                    dias = dias + ", ";
                }
            }

            if (funcionario.getListaDiasAtendimentoSemana().get(i).getDiaSemana().toString().equals(DiasDaSemana.SABADO.getSigla())) {
                dias = dias + "Sábado "+(funcionario.getListaDiasAtendimentoSemana().get(i).getHorario()!=null ? funcionario.getListaDiasAtendimentoSemana().get(i).getHorario() :"");
                if(funcionario.getListaDiasAtendimentoSemana().size() > 1 && funcionario.getListaDiasAtendimentoSemana().size()!=i+1){
                    dias = dias + ", ";
                }
            }

        }
        dias = dias + ".";

        funcionario.setDiasSemana(dias);
        listaProfissionaisAdicionados.add(funcionario);
        JSFUtil.fecharDialog("dlgDiasAtendimentoTurno");

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

    public String getOpcaoAtendimento() {
        return opcaoAtendimento;
    }

    public void setOpcaoAtendimento(String opcaoAtendimento) {
        this.opcaoAtendimento = opcaoAtendimento;
    }

    public ArrayList<FuncionarioBean> getListaProfissionaisEquipe() {
        return listaProfissionaisEquipe;
    }

    public void setListaProfissionaisEquipe(ArrayList<FuncionarioBean> listaProfissionaisEquipe) {
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

    public void setListaProfissionaisAdicionados(ArrayList<FuncionarioBean> listaProfissionaisAdicionados) {
        this.listaProfissionaisAdicionados = listaProfissionaisAdicionados;
    }

    public ArrayList<InsercaoPacienteBean> getListaLaudosVigentes() {
        return listaLaudosVigentes;
    }

    public void setListaLaudosVigentes(ArrayList<InsercaoPacienteBean> listaLaudosVigentes) {
        this.listaLaudosVigentes = listaLaudosVigentes;
    }

	public List<AgendaBean> getListaHorariosAgenda() {
		return listaHorariosAgenda;
	}

	public void setListaHorariosAgenda(List<AgendaBean> listaHorariosAgenda) {
		this.listaHorariosAgenda = listaHorariosAgenda;
	}

	public ArrayList<String> getListaHorarios() throws ParseException, ProjetoException {
		return listaHorarios;
	}

	public void setListaHorarios(ArrayList<String> listaHorarios) {
		this.listaHorarios = listaHorarios;
	}

	public ArrayList<HorarioAtendimento> getListaHorarioAtendimentos() {
		return listaHorarioAtendimentos;
	}

	public void setListaHorarioAtendimentos(ArrayList<HorarioAtendimento> listaHorarioAtendimentos) {
		this.listaHorarioAtendimentos = listaHorarioAtendimentos;
	}


}
