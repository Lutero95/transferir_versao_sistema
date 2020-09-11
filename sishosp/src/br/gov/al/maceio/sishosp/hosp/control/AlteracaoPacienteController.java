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

import br.gov.al.maceio.sishosp.acl.dao.FuncionarioDAO;
import br.gov.al.maceio.sishosp.acl.model.FuncionarioBean;
import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.DataUtil;
import br.gov.al.maceio.sishosp.comum.util.HorarioOuTurnoUtil;
import br.gov.al.maceio.sishosp.comum.util.JSFUtil;
import br.gov.al.maceio.sishosp.comum.util.VerificadorUtil;
import br.gov.al.maceio.sishosp.hosp.dao.AgendaDAO;
import br.gov.al.maceio.sishosp.hosp.dao.AlteracaoPacienteDAO;
import br.gov.al.maceio.sishosp.hosp.dao.CidDAO;
import br.gov.al.maceio.sishosp.hosp.dao.EmpresaDAO;
import br.gov.al.maceio.sishosp.hosp.dao.EquipeDAO;
import br.gov.al.maceio.sishosp.hosp.dao.GerenciarPacienteDAO;
import br.gov.al.maceio.sishosp.hosp.dao.InsercaoPacienteDAO;
import br.gov.al.maceio.sishosp.hosp.dao.LaudoDAO;
import br.gov.al.maceio.sishosp.hosp.dao.ProcedimentoDAO;
import br.gov.al.maceio.sishosp.hosp.dao.ProgramaDAO;
import br.gov.al.maceio.sishosp.hosp.dao.RenovacaoPacienteDAO;
import br.gov.al.maceio.sishosp.hosp.enums.DiasDaSemana;
import br.gov.al.maceio.sishosp.hosp.enums.OpcaoAtendimento;
import br.gov.al.maceio.sishosp.hosp.enums.RetornoLaudoRenovacao;
import br.gov.al.maceio.sishosp.hosp.enums.TipoAtendimento;
import br.gov.al.maceio.sishosp.hosp.enums.TipoGravacaoHistoricoPaciente;
import br.gov.al.maceio.sishosp.hosp.model.AgendaBean;
import br.gov.al.maceio.sishosp.hosp.model.CidBean;
import br.gov.al.maceio.sishosp.hosp.model.EquipeBean;
import br.gov.al.maceio.sishosp.hosp.model.GerenciarPacienteBean;
import br.gov.al.maceio.sishosp.hosp.model.HorarioAtendimento;
import br.gov.al.maceio.sishosp.hosp.model.InsercaoPacienteBean;
import br.gov.al.maceio.sishosp.hosp.model.LaudoBean;
import br.gov.al.maceio.sishosp.hosp.model.ProcedimentoBean;

@ManagedBean(name = "AlteracaoPacienteController")
@ViewScoped
public class AlteracaoPacienteController implements Serializable {

    private static final long serialVersionUID = 1L;
    private AlteracaoPacienteDAO aDao = new AlteracaoPacienteDAO();
    private static AlteracaoPacienteDAO aDaoDuplicado = new AlteracaoPacienteDAO();
    private InsercaoPacienteBean insercao;
    private static InsercaoPacienteBean insercaoDuplicado;
    private RenovacaoPacienteDAO rDao = new RenovacaoPacienteDAO();
    private InsercaoPacienteBean insercaoParaLaudo;
    private static InsercaoPacienteBean insercaoParaLaudoDuplicado;
    private String tipo;
    private ArrayList<InsercaoPacienteBean> listaLaudosVigentes;
    private static String tipoDuplicado;
    private List<Integer> listaDias;
    private static List<Integer> listaDiasDuplicado;
    private InsercaoPacienteDAO iDao;
    private static InsercaoPacienteDAO iDaoDuplicado =  new InsercaoPacienteDAO();;
    private ArrayList<FuncionarioBean> listaProfissionaisAdicionados;
    private static ArrayList<FuncionarioBean> listaProfissionaisAdicionadosDuplicado;
    private ArrayList<AgendaBean> listAgendamentoProfissional;
    private static ArrayList<AgendaBean> listAgendamentoProfissionalDuplicado;
    private Integer id_paciente_insituicao;
    private static Integer id_paciente_insituicaoDuplicado;
    private String opcaoAtendimento;
    private LaudoBean laudo;
    private ArrayList<FuncionarioBean> listaProfissionaisEquipe;
    private static ArrayList<FuncionarioBean> listaProfissionaisEquipeDuplicado;
    private List<AgendaBean> listaHorariosEquipe;
    private EquipeDAO eDao = new EquipeDAO();
    private ProgramaDAO programaDAO = new ProgramaDAO();
    private FuncionarioBean funcionario;
    private EmpresaDAO empresaDAO = new EmpresaDAO();
    private Boolean todosOsProfissionais;
    private AgendaDAO agendaDAO = new AgendaDAO();
    private static AgendaDAO agendaDAODuplicado = new AgendaDAO();
    private ArrayList<HorarioAtendimento> listaHorarioAtendimentos;
    private ArrayList<String> listaHorarios;
    private List<HorarioAtendimento> listaHorarioAtendimentosAuxiliar;
    private List<HorarioAtendimento> listaHorarioFinal = new ArrayList<>();
	private List<ProcedimentoBean> listaProcedimentos;
	private List<CidBean> listaCids;
	private List<FuncionarioBean> listaProfissionais;

    public AlteracaoPacienteController() throws ProjetoException, ParseException {
        insercao = new InsercaoPacienteBean();
        insercaoDuplicado = new InsercaoPacienteBean();
        insercaoParaLaudo = new InsercaoPacienteBean();
        insercaoParaLaudoDuplicado = new InsercaoPacienteBean();
        listaProfissionaisAdicionados = new ArrayList<FuncionarioBean>();
        listaProfissionaisAdicionadosDuplicado = new ArrayList<FuncionarioBean>();
        funcionario = new FuncionarioBean();
        listaLaudosVigentes = new ArrayList<InsercaoPacienteBean>();
        tipo = "";
        iDao = new InsercaoPacienteDAO();
        iDaoDuplicado = new InsercaoPacienteDAO();
        listAgendamentoProfissional = new ArrayList<AgendaBean>();
        listaHorariosEquipe = new ArrayList<AgendaBean>();
        todosOsProfissionais = false;
        listaHorarioAtendimentos = new ArrayList<>();
        InsercaoPacienteController insercaoPacienteController = new InsercaoPacienteController();
        opcaoAtendimento = insercaoPacienteController.carregarHorarioOuTurno();
        listaHorarios = new ArrayList<>();
        laudo = new LaudoBean();
        listaHorarioAtendimentosAuxiliar = new ArrayList<>();
    	listaProcedimentos = new ArrayList<>();
    	listaCids = new ArrayList<>();
    	listaProfissionais = new ArrayList<>();
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
    
    public void botaoCarga() throws ProjetoException, ParseException {
    	GerenciarPacienteDAO gDao = new GerenciarPacienteDAO();
    	ArrayList<Integer> lista = new ArrayList<>();
    	lista = gDao.carregarPacientesInstituicaoDuplicado();
    	for (int i = 0; i < lista.size(); i++) {
    		listaProfissionaisAdicionados = new ArrayList<FuncionarioBean>();
    		listAgendamentoProfissionalDuplicado = new ArrayList<>();
    		listaDiasDuplicado = new ArrayList<>();
    		listaProfissionaisAdicionadosDuplicado = new ArrayList<>();
    		carregaAlteracaoDuplicado(lista.get(i));
    		gravarAlteracaoPacienteDuplicado();
    	}
    	JSFUtil.adicionarMensagemAdvertencia("Acabou carga", "Advertência");
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
    
    public void alteraDiaAtendimentoProfissionaisNaLista(Integer novoDiaSemana) {
    	String dias = "";
    	listaDias = new ArrayList<>();
    	if (verificaPermissaoParaAlterarDiaAtendimentoProfissionaisNaLista()) {
        for (int i = 0; i < listaProfissionaisAdicionados.size(); i++) {
        	dias = "";
        	listaProfissionaisAdicionados.get(i).setDiaSemana(novoDiaSemana);
        	for (int h = 0; h < listaProfissionaisAdicionados.get(i).getListaDiasAtendimentoSemana().size(); h++) {
        		listaProfissionaisAdicionados.get(h).setDiaSemana(novoDiaSemana);
        		 listaProfissionaisAdicionados.get(i).getListaDiasAtendimentoSemana().get(h).setDiaSemana(novoDiaSemana);
         	   if (listaProfissionaisAdicionados.get(i).getDiaSemana().toString().equals(DiasDaSemana.DOMINGO.getSigla())) {
                   dias = dias + "Domingo " +  (listaProfissionaisAdicionados.get(i).getListaDiasAtendimentoSemana().get(h).getHorario() !=null ? listaProfissionaisAdicionados.get(i).getListaDiasAtendimentoSemana().get(h).getHorario() : "") ;

                   if (listaProfissionaisAdicionados.get(i).getListaDiasAtendimentoSemana().size() > 1 && listaProfissionaisAdicionados.get(i).getListaDiasAtendimentoSemana().size() != i + 1) {
                       dias = dias + ", ";
                   }
               }

         	  if (listaProfissionaisAdicionados.get(i).getDiaSemana().toString().equals(DiasDaSemana.SEGUNDA.getSigla())) {
                   dias = dias + "Segunda " +  (listaProfissionaisAdicionados.get(i).getListaDiasAtendimentoSemana().get(h).getHorario() !=null ? listaProfissionaisAdicionados.get(i).getListaDiasAtendimentoSemana().get(h).getHorario() : "") ;
                   if (listaProfissionaisAdicionados.get(i).getListaDiasAtendimentoSemana().size() > 1 && listaProfissionaisAdicionados.get(i).getListaDiasAtendimentoSemana().size() != i + 1) {
                       dias = dias + ", ";
                   }
               }

         	 if (listaProfissionaisAdicionados.get(i).getDiaSemana().toString().equals(DiasDaSemana.TERCA.getSigla())) {
                   dias = dias + "Terça " +   (listaProfissionaisAdicionados.get(i).getListaDiasAtendimentoSemana().get(h).getHorario() !=null ? listaProfissionaisAdicionados.get(i).getListaDiasAtendimentoSemana().get(h).getHorario() : "") ;
                   if (listaProfissionaisAdicionados.get(i).getListaDiasAtendimentoSemana().size() > 1 && listaProfissionaisAdicionados.get(i).getListaDiasAtendimentoSemana().size() != i + 1) {
                       dias = dias + ", ";
                   }
               }

         	if (listaProfissionaisAdicionados.get(i).getDiaSemana().toString().equals(DiasDaSemana.QUARTA.getSigla())) {
                   dias = dias + "Quarta " +  (listaProfissionaisAdicionados.get(i).getListaDiasAtendimentoSemana().get(h).getHorario() !=null ? listaProfissionaisAdicionados.get(i).getListaDiasAtendimentoSemana().get(h).getHorario() : "") ;
                   if (listaProfissionaisAdicionados.get(i).getListaDiasAtendimentoSemana().size() > 1 && listaProfissionaisAdicionados.get(i).getListaDiasAtendimentoSemana().size() != i + 1) {
                       dias = dias + ", ";
                   }
               }

         	if (listaProfissionaisAdicionados.get(i).getDiaSemana().toString().equals(DiasDaSemana.QUINTA.getSigla())) {
                   dias = dias + "Quinta " +  (listaProfissionaisAdicionados.get(i).getListaDiasAtendimentoSemana().get(h).getHorario() !=null ? listaProfissionaisAdicionados.get(i).getListaDiasAtendimentoSemana().get(h).getHorario() : "") ;
                   if (listaProfissionaisAdicionados.get(i).getListaDiasAtendimentoSemana().size() > 1 && listaProfissionaisAdicionados.get(i).getListaDiasAtendimentoSemana().size() != i + 1) {
                       dias = dias + ", ";
                   }
               }

         	if (listaProfissionaisAdicionados.get(i).getDiaSemana().toString().equals(DiasDaSemana.SEXTA.getSigla())) {
                   dias = dias + "Sexta " +  (listaProfissionaisAdicionados.get(i).getListaDiasAtendimentoSemana().get(h).getHorario() !=null ? listaProfissionaisAdicionados.get(i).getListaDiasAtendimentoSemana().get(h).getHorario() : "") ;
                   if (listaProfissionaisAdicionados.get(i).getListaDiasAtendimentoSemana().size() > 1 && listaProfissionaisAdicionados.get(i).getListaDiasAtendimentoSemana().size() != i + 1) {
                       dias = dias + ", ";
                   }
               }

         	if (listaProfissionaisAdicionados.get(i).getDiaSemana().toString().equals(DiasDaSemana.SABADO.getSigla())) {
                   dias = dias + "Sábado " +  (listaProfissionaisAdicionados.get(i).getListaDiasAtendimentoSemana().get(h).getHorario() !=null ? listaProfissionaisAdicionados.get(i).getListaDiasAtendimentoSemana().get(h).getHorario() : "") ;
                   if (listaProfissionaisAdicionados.get(i).getListaDiasAtendimentoSemana().size() > 1 && listaProfissionaisAdicionados.get(i).getListaDiasAtendimentoSemana().size() != i + 1) {
                       dias = dias + ", ";
                   }
               } 
        	}
        	
        	listaProfissionaisAdicionados.get(i).setDiasSemana(dias); 
       	
        }
        
        for (int i = 0; i < listaProfissionaisAdicionados.size(); i++) {
        	dias = "";
        	listaProfissionaisAdicionados.get(i).setDiaSemana(novoDiaSemana);
        	for (int h = 0; h < listaProfissionaisAdicionados.get(i).getListaDiasAtendimentoSemana().size(); h++) {
                if (!listaDias.contains(listaProfissionaisAdicionados.get(i).getListaDiasAtendimentoSemana().get(h).getDiaSemana())) {
                    listaDias.add(listaProfissionaisAdicionados.get(i).getListaDiasAtendimentoSemana().get(h).getDiaSemana());
                }
        		
        	}
        }
        
        
        JSFUtil.fecharDialog("dlgAlteraDiaAtendimento");
    	}
    	else
    	{
    		JSFUtil.adicionarMensagemAdvertencia("Apenas é possível alterar o dia de atendimento quando os profissionais atendem em apenas um dia!", "Atenção!");
    	}
    }  
    
    public void abreDialogAlteraDiasAtendimentoProfissionais() {
    	insercao.getHorarioAtendimento().setDiaSemana(null);
    	JSFUtil.abrirDialog("dlgAlteraDiaAtendimento");
    }
    

    public Boolean verificaPermissaoParaAlterarDiaAtendimentoProfissionaisNaLista() {
    	Integer primeirodiaSemanaNaLista = null;
    	Boolean rst = true;
        for (int i = 0; i < listaProfissionaisAdicionados.size(); i++) {
        	for (int h = 0; h < listaProfissionaisAdicionados.get(i).getListaDiasAtendimentoSemana().size(); h++) {
        		if (primeirodiaSemanaNaLista==null) 
        			primeirodiaSemanaNaLista = listaProfissionaisAdicionados.get(i).getListaDiasAtendimentoSemana().get(h).getDiaSemana();
        		if (primeirodiaSemanaNaLista!= listaProfissionaisAdicionados.get(i).getListaDiasAtendimentoSemana().get(h).getDiaSemana()) {
        			rst = false;
        			break;
        		}
        	}
        }
        return rst;
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
   
    
    private void gerarHorariosAtendimento() throws ParseException, ProjetoException {
        listaHorarios = HorarioOuTurnoUtil.gerarHorariosAtendimento();
    }

    public void carregaAlteracao() throws ProjetoException, ParseException {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Map<String, String> params = facesContext.getExternalContext()
                .getRequestParameterMap();
        if (params.get("id") != null) {
            if (opcaoAtendimento.equals(OpcaoAtendimento.SOMENTE_HORARIO.getSigla()) || opcaoAtendimento.equals(OpcaoAtendimento.AMBOS.getSigla())) {
                gerarHorariosAtendimento();
            }
            listaDias = new ArrayList<>();
            Integer id = Integer.parseInt(params.get("id"));
            id_paciente_insituicao = id;
            this.insercao = aDao.carregarPacientesInstituicaoAlteracao(id);
            this.laudo = this.insercao.getLaudo();
            if (insercao.getLaudo().getId()!=null) {
            	carregarLaudoPaciente();
            }
            
            
            if (insercao.getEquipe().getCodEquipe() != null
                    && insercao.getEquipe().getCodEquipe() > 0) {
                listaProfissionaisEquipe = agendaDAO.listaProfissionaisAtendimetoParaPacienteInstituicao(id);
                tipo = TipoAtendimento.EQUIPE.getSigla();
                listarProfissionaisEquipe();

                List<FuncionarioBean> listaFuncionarioAuxiliar = agendaDAO.listaProfissionaisIhDiasIhHorariosAtendimetoParaPacienteInstituicao(id);
                adicionarFuncionarioParaEdicao(listaFuncionarioAuxiliar);
                
            }
            if (insercao.getFuncionario().getId() != null
                    && insercao.getFuncionario().getId() > 0) {
                tipo = TipoAtendimento.PROFISSIONAL.getSigla();
            }

        } else {
            JSFUtil.adicionarMensagemErro("Ocorreu um erro!", "Erro");
        }

    }
    
    public static void  carregaAlteracaoDuplicado(Integer idPacienteInstituicao) throws ProjetoException, ParseException {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Map<String, String> params = facesContext.getExternalContext()
                .getRequestParameterMap();
            listaDiasDuplicado = new ArrayList<>();
            Integer id = idPacienteInstituicao;
            id_paciente_insituicaoDuplicado = id;
            insercaoDuplicado = aDaoDuplicado.carregarPacientesInstituicaoAlteracao(id);
            if (insercaoDuplicado.getLaudo().getId()!=null)
            carregarLaudoPacienteDuplicado();
            
            if (insercaoDuplicado.getEquipe().getCodEquipe() != null
                    && insercaoDuplicado.getEquipe().getCodEquipe() > 0) {
                listaProfissionaisEquipeDuplicado = agendaDAODuplicado.listaProfissionaisAtendimetoParaPacienteInstituicao(id);
                tipoDuplicado = TipoAtendimento.EQUIPE.getSigla();


                List<FuncionarioBean> listaFuncionarioAuxiliar = agendaDAODuplicado.listaProfissionaisIhDiasIhHorariosAtendimetoParaPacienteInstituicao(id);
                adicionarFuncionarioParaEdicaoDuplicado(listaFuncionarioAuxiliar);
                
            }
            if (insercaoDuplicado.getFuncionario().getId() != null
                    && insercaoDuplicado.getFuncionario().getId() > 0) {
              //  funcionario.setListDiasSemana(agendaDAO.listaDiasDeAtendimetoParaPacienteInstituicao(id));
                tipoDuplicado = TipoAtendimento.PROFISSIONAL.getSigla();
            //    insercao.getFuncionario().setListDiasSemana(
              //          aDao.listarDiasAtendimentoProfissional(id));
            }

      

    }    

    public void gerarListaAgendamentosEquipeTurno() throws ProjetoException {
    	Integer codPaciente = null;
    	if ((insercao.getLaudo() != null) && (insercao.getLaudo().getId() != null)) 
    			codPaciente = insercao.getLaudo().getPaciente().getId_paciente();
    	
    	if  ((insercao.getPaciente() != null) && (insercao.getPaciente().getId_paciente() != null))
    		codPaciente = insercao.getPaciente().getId_paciente();
    	if(!VerificadorUtil.verificarSeObjetoNuloOuZero(insercaoParaLaudo.getLaudo().getPaciente().getId_paciente()))
    		codPaciente = insercaoParaLaudo.getLaudo().getPaciente().getId_paciente();
    	
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        df.setLenient(false);
        GerenciarPacienteController gerenciarPacienteController = new GerenciarPacienteController();
        Date periodoInicial = gerenciarPacienteController.ajustarDataDeSolicitacao(insercao.getDataSolicitacao(), insercaoParaLaudo.getLaudo().getId(), insercao.getPaciente().getId_paciente(), insercao.getPrograma().getIdPrograma(), insercao.getGrupo().getIdGrupo());
        Date d1 = periodoInicial;
        Date d2 =null;
        if (!VerificadorUtil.verificarSeObjetoNuloOuZero(insercao.getLaudo().getId()))
        	d2 = iDao.dataFinalLaudo(insercao.getLaudo().getId());
        else {
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
    
    public static void main(String[] args) throws ProjetoException, ParseException {
		// TODO Auto-generated method stub
    	GerenciarPacienteDAO gDao = new GerenciarPacienteDAO();
    	ArrayList<Integer> lista = new ArrayList<>();
    	lista = gDao.carregarPacientesInstituicaoDuplicado();
    	for (int i = 0; i < lista.size(); i++) {
    		carregaAlteracaoDuplicado(lista.get(i));
    		gravarAlteracaoPacienteDuplicado();
    	}
	}

    public void gerarListaAgendamentosEquipeDiaHorario() throws ProjetoException {

        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        df.setLenient(false);
        GerenciarPacienteController gerenciarPacienteController = new GerenciarPacienteController();
        Date periodoInicial = gerenciarPacienteController.ajustarDataDeSolicitacao(insercao.getDataSolicitacao(), insercaoParaLaudo.getLaudo().getId(), insercao.getPaciente().getId_paciente(), insercao.getPrograma().getIdPrograma(), insercao.getGrupo().getIdGrupo());
        Date d1 = periodoInicial;
        
        Date d2 = null;
        
        if(!VerificadorUtil.verificarSeObjetoNuloOuZero(insercao.getLaudo().getId()))
        		d2 = iDao.dataFinalLaudo(insercao.getLaudo().getId());
        else {
            Date dataFinalSemLaudo = iDao.dataFinalPacienteSemLaudo(insercao, insercao.getPaciente().getId_paciente());
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
    
    public static void gerarListaAgendamentosEquipeDiaHorarioDuplicado() throws ProjetoException {

        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        df.setLenient(false);
        GerenciarPacienteController gerenciarPacienteController = new GerenciarPacienteController();
        Date periodoInicial = gerenciarPacienteController.ajustarDataDeSolicitacao(insercaoDuplicado.getDataSolicitacao(), insercaoDuplicado.getLaudo().getId(), insercaoDuplicado.getPaciente().getId_paciente(), insercaoDuplicado.getPrograma().getIdPrograma(), insercaoDuplicado.getGrupo().getIdGrupo());
        Date d1 = periodoInicial;
        Date d2 = iDaoDuplicado.dataFinalLaudo(insercaoDuplicado.getLaudo().getId());
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
            if (tipoDuplicado.equals(TipoAtendimento.EQUIPE.getSigla())) {
                for (int h = 0; h < listaDiasDuplicado.size(); h++) {
                    if (!listaDatasDeAtendimento.contains(c.getTime())) {
                        if (diaSemana == listaDiasDuplicado.get(h)) {

                            AgendaBean agenda = new AgendaBean();

                            agenda.setPaciente(
                                    insercaoDuplicado.getLaudo().getPaciente());

                            agenda.setDataAtendimento(c.getTime());

                            listAgendamentoProfissionalDuplicado.add(agenda);
                            listaDatasDeAtendimento.add(c.getTime());

                        }
                    }
                }
            }

        }


    }

    public void excluirFuncionarioIhDiasDeAtendimento(){
        funcionario.setDiasSemana("");
        funcionario.setListDiasSemana(new ArrayList<>());
        funcionario.setListaDiasAtendimentoSemana(new ArrayList<>());
        listaProfissionaisAdicionados.remove(funcionario);
        funcionario =  new FuncionarioBean();
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
                for (int j = 0; j < funcionario.getListaDiasAtendimentoSemana().size(); j++) {
                    if (diaSemana == funcionario
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
    
    public boolean dataInclusaoPacienteEstaEntreDataInicialIhFinalDoLaudo() throws ProjetoException {
    	Boolean dataValida = null;
    	
    	if ((!VerificadorUtil.verificarSeObjetoNulo(insercaoParaLaudo.getLaudo())) && (!VerificadorUtil.verificarSeObjetoNulo(insercaoParaLaudo.getLaudo().getId())) && (insercao.isInsercaoPacienteSemLaudo()==false)) {
    		if(ehNovoLaudo())
    			dataValida = iDao.dataInclusaoPacienteEstaEntreDataInicialIhFinalDoLaudo(insercaoParaLaudo.getLaudo().getId(), insercao.getDataSolicitacao());
    		else
    			dataValida = aDao.dataInclusaoPacienteEstaEntreDataInicialIhFinalDoLaudo(insercaoParaLaudo.getLaudo().getId(), insercao.getDataSolicitacao());
    	}
    	else if ((insercao.isInsercaoPacienteSemLaudo())) {
    		dataValida = validarDataLaudoPacienteSemLaudo();
    	}
    	else
    		dataValida = true;
    	if(!dataValida)
    		JSFUtil.adicionarMensagemErro("Data de Inclusão não está dentro do intervalo da data do laudo", "Erro!");
    	return dataValida; 
    }
    
    private boolean ehNovoLaudo() {
    	return VerificadorUtil.verificarSeObjetoNuloOuZero(this.laudo.getId());
    }

    public void gravarAlteracaoPaciente() throws ProjetoException {
    	
    	if(dataInclusaoPacienteEstaEntreDataInicialIhFinalDoLaudo()) {
			Boolean cadastrou = false;
			listAgendamentoProfissional = new ArrayList<AgendaBean>();

			InsercaoPacienteController insercaoPacienteController = new InsercaoPacienteController();

			GerenciarPacienteController gerenciarPacienteController = new GerenciarPacienteController();
			Date dataSolicitacaoCorreta = gerenciarPacienteController.ajustarDataDeSolicitacao(
					insercao.getDataSolicitacao(), insercaoParaLaudo.getLaudo().getId(), insercao.getPaciente().getId_paciente(),
					insercao.getPrograma().getIdPrograma(), insercao.getGrupo().getIdGrupo());
			insercao.setDataSolicitacao(dataSolicitacaoCorreta);

			ArrayList<AgendaBean> listaAgendamentosProfissionalFinal = new ArrayList<AgendaBean>();

			if (tipo.equals(TipoAtendimento.EQUIPE.getSigla())) {

				if (!listaProfissionaisAdicionadosEstaVazia()) {
					if (opcaoAtendimento.equals(OpcaoAtendimento.SOMENTE_TURNO.getSigla())) {
						gerarListaAgendamentosEquipeTurno();
					}

					if (opcaoAtendimento.equals(OpcaoAtendimento.SOMENTE_HORARIO.getSigla())) {
						gerarListaAgendamentosEquipeDiaHorario();
					}

					listaAgendamentosProfissionalFinal = insercaoPacienteController
							.validarDatas(listAgendamentoProfissional, insercao.getTurno());

					if (opcaoAtendimento.equals(OpcaoAtendimento.SOMENTE_HORARIO.getSigla())) {
						cadastrou = aDao.gravarAlteracaoEquipeDiaHorario(insercao, insercaoParaLaudo,
								listaAgendamentosProfissionalFinal, id_paciente_insituicao,
								listaProfissionaisAdicionados);
					}
					else {
						cadastrou = aDao.gravarAlteracaoEquipeTurno(insercao, insercaoParaLaudo,
								listaAgendamentosProfissionalFinal,
								id_paciente_insituicao, listaProfissionaisAdicionados);
					}
				}
			}
			if (tipo.equals(TipoAtendimento.PROFISSIONAL.getSigla())) {

				gerarListaAgendamentosProfissional();

				cadastrou = aDao.gravarAlteracaoProfissional(insercao, insercaoParaLaudo,
						listaAgendamentosProfissionalFinal, id_paciente_insituicao);
			}

			if (cadastrou) {
				JSFUtil.adicionarMensagemSucesso("Alteração de Equipe cadastrada com sucesso!", "Sucesso");
				JSFUtil.abrirDialog("dlgAlteracaoEfetuada");
			} else {
				JSFUtil.adicionarMensagemErro("Ocorreu um erro durante o cadastro!", "Erro");
			}
    	}
    }
    
    private boolean listaProfissionaisAdicionadosEstaVazia() {
    	if(this.listaProfissionaisAdicionados.isEmpty()) {
    		JSFUtil.adicionarMensagemAdvertencia("Por favor adicione pelo menos um profissional", "Atenção");
    		return true;
    	}
    	return false;
    }
    
    public static void gravarAlteracaoPacienteDuplicado() throws ProjetoException {

        Boolean cadastrou = null;

        InsercaoPacienteController insercaoPacienteController = new InsercaoPacienteController();

        GerenciarPacienteController gerenciarPacienteController = new GerenciarPacienteController();
        Date dataSolicitacaoCorreta = gerenciarPacienteController.ajustarDataDeSolicitacao(insercaoDuplicado.getDataSolicitacao(), insercaoDuplicado.getLaudo().getId(), insercaoDuplicado.getPaciente().getId_paciente(), insercaoDuplicado.getPrograma().getIdPrograma(), insercaoDuplicado.getGrupo().getIdGrupo());
        insercaoDuplicado.setDataSolicitacao(dataSolicitacaoCorreta);

        ArrayList<AgendaBean> listaAgendamentosProfissionalFinal = new ArrayList<AgendaBean>();
        
        if (tipoDuplicado.equals(TipoAtendimento.EQUIPE.getSigla())) {
            	gerarListaAgendamentosEquipeDiaHorarioDuplicado();
            
            
            listaAgendamentosProfissionalFinal = insercaoPacienteController.validarDatas(
                    listAgendamentoProfissionalDuplicado, insercaoDuplicado.getTurno());
            	cadastrou = aDaoDuplicado.gravarAlteracaoEquipeDiaHorarioDuplicado(insercaoDuplicado, insercaoParaLaudoDuplicado,
                        listaAgendamentosProfissionalFinal, id_paciente_insituicaoDuplicado, listaProfissionaisAdicionadosDuplicado);
        }
    }
    
    public void atualizaarDataSolicitacao(Date dataSolicitacaoNova) {
    	insercao.setDataSolicitacao(dataSolicitacaoNova);
    }
    
    public void validaSelecaoLaudoPacienteSemLaudo() throws ProjetoException {
    	if(validarDataLaudoPacienteSemLaudo())
    		validaDataLaudoComDataAnteriorPacienteSemLaudo();
    }    
    
    private boolean validarDataLaudoPacienteSemLaudo() throws ProjetoException {
    	boolean resultado = false;
    	Date dataSolicitacaoPacienteTerapia = insercao.getDataSolicitacao();
    	Date dataVigenciaFinalLaudo = insercao.getLaudo().getVigenciaFinal();
    	
    	if (dataVigenciaFinalLaudo.before(dataSolicitacaoPacienteTerapia)) {
            JSFUtil.adicionarMensagemErro("A data do laudo selecionado é menor que a data de inclusão do paciente na Terapia", "Erro!");
            insercao.setLaudo(this.laudo);
            insercaoParaLaudo.setLaudo(new LaudoBean());
        }
    	else 
    		resultado = true;
        return resultado;
    }
    
    private void validaDataLaudoComDataAnteriorPacienteSemLaudo() throws ProjetoException {
    	
    	String condicaoDatasLaudo = retornaCondicaoLaudo();

        if(condicaoDatasLaudo.equals(RetornoLaudoRenovacao.DATA_ATUAL_MAIOR_QUE_NOVA_DATA.getSigla())){
            JSFUtil.adicionarMensagemErro("A data do novo laudo é menor que a data do laudo atual", "Erro!");
            insercao.setLaudo(this.laudo);
        }
        else if(condicaoDatasLaudo.equals(RetornoLaudoRenovacao.MAIS_DE_UM_MES.getSigla())){
            JSFUtil.adicionarMensagemAdvertencia("O novo laudo tem início com mais de 30 dias a mais que o término do que o lado atual!", "Observação!");
            Integer idNovoLaudo = insercao.getLaudo().getId();
            insercaoParaLaudo = iDao.carregarLaudoPaciente(idNovoLaudo);
            insercao = rDao.carregarPacientesInstituicaoRenovacao(id_paciente_insituicao);
            insercao.setPaciente(insercaoParaLaudo.getLaudo().getPaciente());
        }
        else {    	
            Integer idNovoLaudo = insercao.getLaudo().getId();
            insercaoParaLaudo = iDao.carregarLaudoPaciente(idNovoLaudo);
            insercao = rDao.carregarPacientesInstituicaoRenovacao(id_paciente_insituicao);
            insercao.setPaciente(insercaoParaLaudo.getLaudo().getPaciente());
        }
    }
    
    private String retornaCondicaoLaudo() {
    	
    	Integer mesLaudoAtual;
    	Integer anoLaudoAtual;
    	boolean vigenciaInicial;
    	
    	if(VerificadorUtil.verificarSeObjetoNuloOuZero(laudo.getId())) {
    		Calendar calendar = Calendar.getInstance();
    		calendar.setTime(insercao.getDataSolicitacao());
    		mesLaudoAtual = calendar.get(Calendar.MONTH)+1;
    		anoLaudoAtual = calendar.get(Calendar.YEAR);
    		vigenciaInicial = true;
    	}
    	else {
    		mesLaudoAtual = laudo.getMesFinal();
    		anoLaudoAtual = laudo.getAnoFinal();
    		vigenciaInicial = false;
    	}
    	
    	return compararDatasLaudo(mesLaudoAtual, anoLaudoAtual,
                insercao.getLaudo().getMesInicio(), insercao.getLaudo().getAnoInicio(), vigenciaInicial);
    }
    
    
    private String compararDatasLaudo(Integer mesAtual, Integer anoAtual, Integer mesNovo, Integer anoNovo, boolean vigenciaInicial){

        String retorno = "";

        final Boolean INICIO_MES = true;

        Date dataLaudoAtual = DataUtil.adicionarMesIhAnoEmDate(mesAtual, anoAtual, vigenciaInicial);
        Date dataLaudoNovo = DataUtil.adicionarMesIhAnoEmDate(mesNovo, anoNovo, INICIO_MES);

        long totalDiasDataLaudoAtual = DataUtil.calcularQuantidadeDeDiasDeUmaData(dataLaudoAtual);
        long totalDiasDataLaudoNovo = DataUtil.calcularQuantidadeDeDiasDeUmaData(dataLaudoNovo);

        long totalDiasGeral = totalDiasDataLaudoNovo - totalDiasDataLaudoAtual;

        if(totalDiasGeral < 0){
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
    
    
    public void carregarLaudoPaciente() throws ProjetoException {
		insercaoParaLaudo = iDao.carregarLaudoPaciente(insercao.getLaudo().getId());
		insercao = aDao.carregarPacientesInstituicaoAlteracao(id_paciente_insituicao);
		insercao.setPaciente(insercaoParaLaudo.getLaudo().getPaciente());
    }
    
    public static void carregarLaudoPacienteDuplicado() throws ProjetoException {
        insercaoParaLaudoDuplicado = iDaoDuplicado.carregarLaudoPaciente(insercaoDuplicado.getLaudo()
                .getId());
        insercaoDuplicado = aDaoDuplicado
                .carregarPacientesInstituicaoAlteracao(id_paciente_insituicaoDuplicado);

    }
    
    private void limparHorariosProfissional() {
        insercao.setHorarioAtendimento(new HorarioAtendimento());
        listaHorarioAtendimentos = new ArrayList<>();
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
                

                JSFUtil.adicionarMensagemSucesso("Esse profissional já foi adicionado!", "Sucesso");
            }

        }
        JSFUtil.fecharDialog("dlgDiasAtendimentoTurno");
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

        
    }
    
    public void listarLaudosVigentes()
            throws ProjetoException {
        LaudoDAO laudoDAO = new LaudoDAO();
        listaLaudosVigentes = laudoDAO.listarLaudosVigentesParaPaciente(insercao.getPaciente().getId_paciente());
    }
    
    


    public void adicionarFuncionarioParaEdicao(List<FuncionarioBean> listaFuncionarioAuxiliar) {
        
        
        for (int i = 0; i < listaFuncionarioAuxiliar.size(); i++) {
        	String dias = "";
        	for (int j = 0; j < listaFuncionarioAuxiliar.get(i).getListaDiasAtendimentoSemana().size(); j++) {
            if (listaFuncionarioAuxiliar.get(i).getListaDiasAtendimentoSemana().get(j).getDiaSemana()==Integer.valueOf(DiasDaSemana.DOMINGO.getSigla())) {
                dias = dias + "Domingo "+" "+(listaFuncionarioAuxiliar.get(i).getListaDiasAtendimentoSemana().get(j).getHorario()!=null  ? listaFuncionarioAuxiliar.get(i).getListaDiasAtendimentoSemana().get(0).getHorario() : "" );

                if(listaFuncionarioAuxiliar.get(i).getListaDiasAtendimentoSemana().size() > 1 && funcionario.getListaDiasAtendimentoSemana().size()!=i+1){
                    dias = dias + ", ";
                }
            }

            if (listaFuncionarioAuxiliar.get(i).getListaDiasAtendimentoSemana().get(j).getDiaSemana()==(Integer.valueOf(DiasDaSemana.SEGUNDA.getSigla()))) {
                dias = dias + "Segunda"+" "+(listaFuncionarioAuxiliar.get(i).getListaDiasAtendimentoSemana().get(j).getHorario()!=null  ? listaFuncionarioAuxiliar.get(i).getListaDiasAtendimentoSemana().get(0).getHorario() : "" );
                if(listaFuncionarioAuxiliar.get(i).getListaDiasAtendimentoSemana().size()  > 1 && listaFuncionarioAuxiliar.get(i).getListaDiasAtendimentoSemana().size()!=i+1){
                    dias = dias + ", ";
                }
            }

            if (listaFuncionarioAuxiliar.get(i).getListaDiasAtendimentoSemana().get(j).getDiaSemana()==Integer.valueOf(DiasDaSemana.TERCA.getSigla())) {
                dias = dias + "Terça"+" "+(listaFuncionarioAuxiliar.get(i).getListaDiasAtendimentoSemana().get(j).getHorario()!=null  ? listaFuncionarioAuxiliar.get(i).getListaDiasAtendimentoSemana().get(0).getHorario() : "" );
                if(listaFuncionarioAuxiliar.get(i).getListaDiasAtendimentoSemana().size()  > 1 && listaFuncionarioAuxiliar.get(i).getListaDiasAtendimentoSemana().size()!=i+1){
                    dias = dias + ", ";
                }
            }

            if (listaFuncionarioAuxiliar.get(i).getListaDiasAtendimentoSemana().get(j).getDiaSemana()==Integer.valueOf(DiasDaSemana.QUARTA.getSigla())) {
                dias = dias + "Quarta"+" "+(listaFuncionarioAuxiliar.get(i).getListaDiasAtendimentoSemana().get(j).getHorario()!=null  ? listaFuncionarioAuxiliar.get(i).getListaDiasAtendimentoSemana().get(0).getHorario() : "" );
                if(listaFuncionarioAuxiliar.get(i).getListaDiasAtendimentoSemana().size()  > 1 && listaFuncionarioAuxiliar.get(i).getListaDiasAtendimentoSemana().size()!=i+1){
                    dias = dias + ", ";
                }
            }

            if (listaFuncionarioAuxiliar.get(i).getListaDiasAtendimentoSemana().get(j).getDiaSemana()==Integer.valueOf(DiasDaSemana.QUINTA.getSigla())) {
                dias = dias + "Quinta"+" "+(listaFuncionarioAuxiliar.get(i).getListaDiasAtendimentoSemana().get(j).getHorario()!=null  ? listaFuncionarioAuxiliar.get(i).getListaDiasAtendimentoSemana().get(0).getHorario() : "" );
                if(listaFuncionarioAuxiliar.get(i).getListaDiasAtendimentoSemana().size()  > 1 && listaFuncionarioAuxiliar.get(i).getListaDiasAtendimentoSemana().size()!=i+1){
                    dias = dias + ", ";
                }
            }

            if (listaFuncionarioAuxiliar.get(i).getListaDiasAtendimentoSemana().get(j).getDiaSemana()==Integer.valueOf(DiasDaSemana.SEXTA.getSigla())) {
                dias = dias + "Sexta"+" "+(listaFuncionarioAuxiliar.get(i).getListaDiasAtendimentoSemana().get(j).getHorario()!=null  ? listaFuncionarioAuxiliar.get(i).getListaDiasAtendimentoSemana().get(0).getHorario() : "" );
                if(listaFuncionarioAuxiliar.get(i).getListaDiasAtendimentoSemana().size()  > 1 && listaFuncionarioAuxiliar.get(i).getListaDiasAtendimentoSemana().size()!=i+1){
                    dias = dias + ", ";
                }
            }

            if (listaFuncionarioAuxiliar.get(i).getListaDiasAtendimentoSemana().get(j).getDiaSemana()==Integer.valueOf(DiasDaSemana.SABADO.getSigla())) {
                dias = dias + "Sábado"+" "+(listaFuncionarioAuxiliar.get(i).getListaDiasAtendimentoSemana().get(j).getHorario()!=null  ? listaFuncionarioAuxiliar.get(i).getListaDiasAtendimentoSemana().get(0).getHorario() : "" );;
                if(listaFuncionarioAuxiliar.get(i).getListaDiasAtendimentoSemana().size()  > 1 && listaFuncionarioAuxiliar.get(i).getListaDiasAtendimentoSemana().size()!=i+1){
                    dias = dias + ", ";
                }
            }


        }
            listaFuncionarioAuxiliar.get(i).setDiasSemana(dias);
            listaProfissionaisAdicionados.add(listaFuncionarioAuxiliar.get(i));
        }
        
        for (int i = 0; i < listaFuncionarioAuxiliar.size(); i++) {
        	String dias = "";
        	for (int j = 0; j < listaFuncionarioAuxiliar.get(i).getListaDiasAtendimentoSemana().size(); j++) {
                if (!listaDias.contains(listaFuncionarioAuxiliar.get(i).getListaDiasAtendimentoSemana().get(0).getDiaSemana())) {
                    listaDias.add(listaFuncionarioAuxiliar.get(i).getListaDiasAtendimentoSemana().get(0).getDiaSemana());
                }        		
        		
        	}
        	
        }
        
    
        
        /*
        
        for (int i = 0; i < listaFuncionarioAuxiliar.size(); i++) {
        FuncionarioBean func = new FuncionarioBean();
        dias = "";
        func.setId(listaFuncionarioAuxiliar.get(i).getId());
        func.setNome(listaFuncionarioAuxiliar.get(i).getNome());
        func.setDiaSemana(listaFuncionarioAuxiliar.get(i).getDiaSemana());
        func.setListaDiasAtendimentoSemana(listaFuncionarioAuxiliar.get(i).getListaDiasAtendimentoSemana());
        func.setHorarioAtendimento(listaFuncionarioAuxiliar.get(i).getHorarioAtendimento());
            if (listaFuncionarioAuxiliar.get(i).getListaDiasAtendimentoSemana().get(0).getDiaSemana().toString().equals(DiasDaSemana.DOMINGO.getSigla())) {
                dias = dias + "Domingo "+" "+  (listaFuncionarioAuxiliar.get(i).getListaDiasAtendimentoSemana().get(0).getHorario()!=null  ? listaFuncionarioAuxiliar.get(i).getListaDiasAtendimentoSemana().get(0).getHorario() : "" );

              //  if(funcionario.getListDiasSemana().size() > 1 && funcionario.getListDiasSemana().size()!=i+1){
              //      dias = dias + ", ";
               // }
            }
            
            if (listaFuncionarioAuxiliar.get(i).getListaDiasAtendimentoSemana().get(0).getDiaSemana().toString().equals(DiasDaSemana.SEGUNDA.getSigla())) {
                dias = dias + "Segunda "+" "+ (listaFuncionarioAuxiliar.get(i).getListaDiasAtendimentoSemana().get(0).getHorario()!=null  ? listaFuncionarioAuxiliar.get(i).getListaDiasAtendimentoSemana().get(0).getHorario() : "" );

              //  if(funcionario.getListDiasSemana().size() > 1 && funcionario.getListDiasSemana().size()!=i+1){
              //      dias = dias + ", ";
               // }
            }

            if (listaFuncionarioAuxiliar.get(i).getListaDiasAtendimentoSemana().get(0).getDiaSemana().toString().equals(DiasDaSemana.TERCA.getSigla())) {
                dias = dias + "Terça "+" "+ (listaFuncionarioAuxiliar.get(i).getListaDiasAtendimentoSemana().get(0).getHorario()!=null  ? listaFuncionarioAuxiliar.get(i).getListaDiasAtendimentoSemana().get(0).getHorario() : "" );

              //  if(funcionario.getListDiasSemana().size() > 1 && funcionario.getListDiasSemana().size()!=i+1){
              //      dias = dias + ", ";
               // }
            }

            if (listaFuncionarioAuxiliar.get(i).getListaDiasAtendimentoSemana().get(0).getDiaSemana().toString().equals(DiasDaSemana.QUARTA.getSigla())) {
                dias = dias + "Quarta "+" "+ (listaFuncionarioAuxiliar.get(i).getListaDiasAtendimentoSemana().get(0).getHorario()!=null  ? listaFuncionarioAuxiliar.get(i).getListaDiasAtendimentoSemana().get(0).getHorario() : "" );

              //  if(funcionario.getListDiasSemana().size() > 1 && funcionario.getListDiasSemana().size()!=i+1){
              //      dias = dias + ", ";
               // }
            }
            
            if (listaFuncionarioAuxiliar.get(i).getListaDiasAtendimentoSemana().get(0).getDiaSemana().toString().equals(DiasDaSemana.QUINTA.getSigla())) {
                dias = dias + "Quinta"+" "+  (listaFuncionarioAuxiliar.get(i).getListaDiasAtendimentoSemana().get(0).getHorario()!=null  ? listaFuncionarioAuxiliar.get(i).getListaDiasAtendimentoSemana().get(0).getHorario() : "" );

              //  if(funcionario.getListDiasSemana().size() > 1 && funcionario.getListDiasSemana().size()!=i+1){
              //      dias = dias + ", ";
               // }
            }
            
            if (listaFuncionarioAuxiliar.get(i).getListaDiasAtendimentoSemana().get(0).getDiaSemana().toString().equals(DiasDaSemana.SEXTA.getSigla())) {
                dias = dias + "Sexta "+" "+  (listaFuncionarioAuxiliar.get(i).getListaDiasAtendimentoSemana().get(0).getHorario()!=null  ? listaFuncionarioAuxiliar.get(i).getListaDiasAtendimentoSemana().get(0).getHorario() : "" );

              //  if(funcionario.getListDiasSemana().size() > 1 && funcionario.getListDiasSemana().size()!=i+1){
              //      dias = dias + ", ";
               // }
            }
            
            if (listaFuncionarioAuxiliar.get(i).getListaDiasAtendimentoSemana().get(0).getDiaSemana().toString().equals(DiasDaSemana.SABADO.getSigla())) {
                dias = dias + "Sábado "+" "+  (listaFuncionarioAuxiliar.get(i).getListaDiasAtendimentoSemana().get(0).getHorario()!=null  ? listaFuncionarioAuxiliar.get(i).getListaDiasAtendimentoSemana().get(0).getHorario() : "" );

              //  if(funcionario.getListDiasSemana().size() > 1 && funcionario.getListDiasSemana().size()!=i+1){
              //      dias = dias + ", ";
               // }
            }
            dias = dias + ".";

            func.setDiasSemana(dias);
            listaProfissionaisAdicionados.add(func);

        }
        
        for (int i = 0; i < listaProfissionaisAdicionados.size(); i++) {
            if (!listaDias.contains(listaProfissionaisAdicionados.get(i).getDiaSemana())) {
                listaDias.add(listaProfissionaisAdicionados.get(i).getDiaSemana());
            }
        }
       
       */

    }
    

        
    
    public static void adicionarFuncionarioParaEdicaoDuplicado(List<FuncionarioBean> listaFuncionarioAuxiliar) {
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
        FuncionarioBean func = new FuncionarioBean();
        dias = "";
        func.setId(listaFuncionarioAuxiliar.get(i).getId());
        func.setNome(listaFuncionarioAuxiliar.get(i).getNome());
        func.setDiaSemana(listaFuncionarioAuxiliar.get(i).getDiaSemana());
        func.setListaDiasAtendimentoSemana(listaFuncionarioAuxiliar.get(i).getListaDiasAtendimentoSemana());
        func.setHorarioAtendimento(listaFuncionarioAuxiliar.get(i).getHorarioAtendimento());
            if (listaFuncionarioAuxiliar.get(i).getListaDiasAtendimentoSemana().get(0).getDiaSemana().toString().equals(DiasDaSemana.DOMINGO.getSigla())) {
                dias = dias + "Domingo "+" "+listaFuncionarioAuxiliar.get(i).getListaDiasAtendimentoSemana().get(0).getHorario();

              //  if(funcionario.getListDiasSemana().size() > 1 && funcionario.getListDiasSemana().size()!=i+1){
              //      dias = dias + ", ";
               // }
            }
            
            if (listaFuncionarioAuxiliar.get(i).getListaDiasAtendimentoSemana().get(0).getDiaSemana().toString().equals(DiasDaSemana.SEGUNDA.getSigla())) {
                dias = dias + "Segunda "+" "+listaFuncionarioAuxiliar.get(i).getListaDiasAtendimentoSemana().get(0).getHorario();

              //  if(funcionario.getListDiasSemana().size() > 1 && funcionario.getListDiasSemana().size()!=i+1){
              //      dias = dias + ", ";
               // }
            }

            if (listaFuncionarioAuxiliar.get(i).getListaDiasAtendimentoSemana().get(0).getDiaSemana().toString().equals(DiasDaSemana.TERCA.getSigla())) {
                dias = dias + "Terça "+" "+listaFuncionarioAuxiliar.get(i).getListaDiasAtendimentoSemana().get(0).getHorario();

              //  if(funcionario.getListDiasSemana().size() > 1 && funcionario.getListDiasSemana().size()!=i+1){
              //      dias = dias + ", ";
               // }
            }

            if (listaFuncionarioAuxiliar.get(i).getListaDiasAtendimentoSemana().get(0).getDiaSemana().toString().equals(DiasDaSemana.QUARTA.getSigla())) {
                dias = dias + "Quarta "+" "+listaFuncionarioAuxiliar.get(i).getListaDiasAtendimentoSemana().get(0).getHorario();

              //  if(funcionario.getListDiasSemana().size() > 1 && funcionario.getListDiasSemana().size()!=i+1){
              //      dias = dias + ", ";
               // }
            }
            
            if (listaFuncionarioAuxiliar.get(i).getListaDiasAtendimentoSemana().get(0).getDiaSemana().toString().equals(DiasDaSemana.QUINTA.getSigla())) {
                dias = dias + "Quinta"+" "+listaFuncionarioAuxiliar.get(i).getListaDiasAtendimentoSemana().get(0).getHorario();

              //  if(funcionario.getListDiasSemana().size() > 1 && funcionario.getListDiasSemana().size()!=i+1){
              //      dias = dias + ", ";
               // }
            }
            
            if (listaFuncionarioAuxiliar.get(i).getListaDiasAtendimentoSemana().get(0).getDiaSemana().toString().equals(DiasDaSemana.SEXTA.getSigla())) {
                dias = dias + "Sexta "+" "+listaFuncionarioAuxiliar.get(i).getListaDiasAtendimentoSemana().get(0).getHorario();

              //  if(funcionario.getListDiasSemana().size() > 1 && funcionario.getListDiasSemana().size()!=i+1){
              //      dias = dias + ", ";
               // }
            }
            
            if (listaFuncionarioAuxiliar.get(i).getListaDiasAtendimentoSemana().get(0).getDiaSemana().toString().equals(DiasDaSemana.SABADO.getSigla())) {
                dias = dias + "Sábado "+" "+listaFuncionarioAuxiliar.get(i).getListaDiasAtendimentoSemana().get(0).getHorario();

              //  if(funcionario.getListDiasSemana().size() > 1 && funcionario.getListDiasSemana().size()!=i+1){
              //      dias = dias + ", ";
               // }
            }
            dias = dias + ".";

            func.setDiasSemana(dias);
            listaProfissionaisAdicionadosDuplicado.add(func);

        }
        
        for (int i = 0; i < listaProfissionaisAdicionadosDuplicado.size(); i++) {
        	for (int j = 0; j < listaProfissionaisAdicionadosDuplicado.get(i).getListaDiasAtendimentoSemana().size(); j++) {
                if (!listaDiasDuplicado.contains(listaProfissionaisAdicionadosDuplicado.get(i).getListaDiasAtendimentoSemana().get(0).getDiaSemana())) {
                	listaDiasDuplicado.add(listaProfissionaisAdicionadosDuplicado.get(i).getListaDiasAtendimentoSemana().get(0).getDiaSemana());
                }        		
        		
        	}
        	
        }
        
        /*
        for (int i = 0; i < listaProfissionaisAdicionadosDuplicado.size(); i++) {
            if (!listaDiasDuplicado.contains(listaProfissionaisAdicionadosDuplicado.get(i).getDiaSemana())) {
            	listaDiasDuplicado.add(listaProfissionaisAdicionadosDuplicado.get(i).getDiaSemana());
            }
        }
        */
       

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

    public void visualizarHorariosEquipe() throws ProjetoException {
    	listaHorariosEquipe = agendaDAO.quantidadeDeAgendamentosDaEquipePorTurno();
    }
    
    public void carregaAlteracaoInsercaoSemLaudo() throws ProjetoException, ParseException, SQLException {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Map<String, String> params = facesContext.getExternalContext()
                .getRequestParameterMap();
        if (params.get("id") != null) {
            
            listaDias = new ArrayList<>();
            Integer id = Integer.parseInt(params.get("id"));
            id_paciente_insituicao = id;
            this.insercao = aDao.carregarPacientesInstituicaoAlteracao(id);
            
            listaDadosPermitidosDoPaciente(id);
            listarDadosPermitidosDoPrograma(insercao.getPrograma().getIdPrograma());
            
            List<FuncionarioBean> listaFuncionarioAuxiliar = agendaDAO.listaProfissionaisIhDiasIhHorariosAtendimetoParaPacienteInstituicao(id);
            adicionarFuncionarioParaEdicao(listaFuncionarioAuxiliar);
        } else {
            JSFUtil.adicionarMensagemErro("Ocorreu um erro!", "Erro");
        }
    }


	private void listaDadosPermitidosDoPaciente(Integer id) throws ProjetoException, SQLException {
		GerenciarPacienteDAO gerenciarPacienteDAO = new GerenciarPacienteDAO();
		this.insercao.getPrograma().setListaCidsPermitidos(gerenciarPacienteDAO.listaCidsPacienteInstituicao(id));
		this.insercao.getPrograma().setListaProcedimentosPermitidos(gerenciarPacienteDAO.listaProcedimentosPacienteInstituicao(id));
	}
    
    private void listarDadosPermitidosDoPrograma(Integer idPrograma) throws ProjetoException {
        this.listaProcedimentos = programaDAO.listarProcedimentosPermitidos(idPrograma);
        this.listaCids = programaDAO.listarCidsPermitidos(idPrograma);
		this.listaProfissionais = programaDAO.listarProfissionaisInsercaoSemlaudo
				(insercao.getPrograma().getIdPrograma(), insercao.getGrupo().getIdGrupo());
    }
    
    public void abrirDialogTurno() {
        JSFUtil.atualizarComponente("formDiasAtendimentoTurno");
        JSFUtil.abrirDialog("dlgDiasAtendimentoTurno");
    }
    
    public void validarAdicionarFuncionarioTurno() {
        Boolean existe = false;
        
        if (listaProfissionaisAdicionados.isEmpty()) {
        	adicionarFuncionarioTurno();
        } else {

            for (FuncionarioBean profissional :  listaProfissionaisAdicionados) {
                if (profissional.getId() == funcionario.getId()) {
                    existe = true;
                }
            }
            if (existe == false) {
            	adicionarFuncionarioTurno();
            } else {
                JSFUtil.fecharDialog("dlgDiasAtendimentoTurno");
                JSFUtil.adicionarMensagemSucesso("Esse profissional já foi adicionado!", "Sucesso");
            }
        }
    }
    
    public void adicionarFuncionarioTurno() {
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

        JSFUtil.fecharDialog("dlgDiasAtendimentoTurno");
    }
    
    public void validarProcedimentoParaAdicionar(ProcedimentoBean procedimentoSelecionado) {
    	for (ProcedimentoBean procedimento : insercao.getPrograma().getListaProcedimentosPermitidos()) {
			if(procedimento.getIdProc().equals(procedimentoSelecionado.getIdProc())) {
				JSFUtil.adicionarMensagemSucesso("Este procedimento já foi adicionado", "");
				return;
			}
		}
    	insercao.getPrograma().getListaProcedimentosPermitidos().add(procedimentoSelecionado);
    	//listaProcedimentosAux = insercao.getPrograma().getListaProcedimentosPermitidos();
    }
    
    public void removerProcedimento(ProcedimentoBean procedimentoSelecionado) {
    	insercao.getPrograma().getListaProcedimentosPermitidos().remove(procedimentoSelecionado);
    }
    
    public void validarCidParaAdicionar(CidBean cidSelecionado) {
    	for (CidBean cid : insercao.getPrograma().getListaCidsPermitidos()) {
			if(cid.getIdCid().equals(cidSelecionado.getIdCid())) {
				JSFUtil.adicionarMensagemSucesso("Este CID já foi adicionado", "");
				return;
			}
		}
    	insercao.getPrograma().getListaCidsPermitidos().add(cidSelecionado);
    }
    
    public void removerCid(CidBean cidSelecionado) {
    	insercao.getPrograma().getListaCidsPermitidos().remove(cidSelecionado);
    }
    
	public void alterarPacienteInseridoSemLaudo() throws ProjetoException {
		InsercaoPacienteController insercaoController = new InsercaoPacienteController();
		if(!insercaoController.listaProfissionaisAdicionadosEstaVazia(this.listaProfissionaisAdicionados) 
    			&& !insercaoController.listaProcedimentosPermitidosEstaVazia(this.insercao.getPrograma().getListaProcedimentosPermitidos()) 
    			&& !insercaoController.listaCidsPermitidosEstaVazia(this.insercao.getPrograma().getListaCidsPermitidos())) {
			
			listAgendamentoProfissional = insercaoController.gerarListaAgendamentosTurnoSemLaudo(this.insercao, this.listaProfissionaisAdicionados, listAgendamentoProfissional);
			
			if(aDao.gravarAlteracaoTurnoSemLaudo(insercao, listAgendamentoProfissional, id_paciente_insituicao, this.listaProfissionaisAdicionados)) {
				JSFUtil.adicionarMensagemSucesso("Paciente Alterado com Sucesso", "");
			}
		}
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

	public ArrayList<HorarioAtendimento> getListaHorarioAtendimentos() {
		return listaHorarioAtendimentos;
	}

	public void setListaHorarioAtendimentos(ArrayList<HorarioAtendimento> listaHorarioAtendimentos) {
		this.listaHorarioAtendimentos = listaHorarioAtendimentos;
	}



	public ArrayList<String> getListaHorarios() {
		return listaHorarios;
	}



	public void setListaHorarios(ArrayList<String> listaHorarios) {
		this.listaHorarios = listaHorarios;
	}


	public List<HorarioAtendimento> getListaHorarioAtendimentosAuxiliar() {
		return listaHorarioAtendimentosAuxiliar;
	}


	public void setListaHorarioAtendimentosAuxiliar(List<HorarioAtendimento> listaHorarioAtendimentosAuxiliar) {
		this.listaHorarioAtendimentosAuxiliar = listaHorarioAtendimentosAuxiliar;
	}


	public List<HorarioAtendimento> getListaHorarioFinal() {
		return listaHorarioFinal;
	}


	public void setListaHorarioFinal(List<HorarioAtendimento> listaHorarioFinal) {
		this.listaHorarioFinal = listaHorarioFinal;
	}


	public ArrayList<InsercaoPacienteBean> getListaLaudosVigentes() {
		return listaLaudosVigentes;
	}

	public void setListaLaudosVigentes(ArrayList<InsercaoPacienteBean> listaLaudosVigentes) {
		this.listaLaudosVigentes = listaLaudosVigentes;
	}


	public List<ProcedimentoBean> getListaProcedimentos() {
		return listaProcedimentos;
	}


	public void setListaProcedimentos(List<ProcedimentoBean> listaProcedimentos) {
		this.listaProcedimentos = listaProcedimentos;
	}


	public List<CidBean> getListaCids() {
		return listaCids;
	}


	public void setListaCids(List<CidBean> listaCids) {
		this.listaCids = listaCids;
	}


	public List<FuncionarioBean> getListaProfissionais() {
		return listaProfissionais;
	}


	public void setListaProfissionais(List<FuncionarioBean> listaProfissionais) {
		this.listaProfissionais = listaProfissionais;
	}
}
