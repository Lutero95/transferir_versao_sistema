package br.gov.al.maceio.sishosp.hosp.control;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import br.gov.al.maceio.sishosp.acl.model.FuncionarioBean;
import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.DataUtil;
import br.gov.al.maceio.sishosp.comum.util.JSFUtil;
import br.gov.al.maceio.sishosp.hosp.dao.*;
import br.gov.al.maceio.sishosp.hosp.enums.DiasDaSemana;
import br.gov.al.maceio.sishosp.hosp.enums.OpcaoAtendimento;
import br.gov.al.maceio.sishosp.hosp.enums.RetornoLaudoRenovacao;
import br.gov.al.maceio.sishosp.hosp.enums.TipoAtendimento;
import br.gov.al.maceio.sishosp.hosp.model.AgendaBean;
import br.gov.al.maceio.sishosp.hosp.model.GerenciarPacienteBean;
import br.gov.al.maceio.sishosp.hosp.model.HorarioAtendimento;
import br.gov.al.maceio.sishosp.hosp.model.InsercaoPacienteBean;
import br.gov.al.maceio.sishosp.hosp.model.LaudoBean;

@ManagedBean(name = "RenovarPacienteController")
@ViewScoped
public class RenovacaoPacienteController implements Serializable {

    private static final long serialVersionUID = 1L;
    private RenovacaoPacienteDAO rDao = new RenovacaoPacienteDAO();
    private InsercaoPacienteBean insercao;
    private InsercaoPacienteBean insercaoParaLaudo;
    private String tipo;
    private InsercaoPacienteDAO iDao;
    private List<HorarioAtendimento> listaHorarioFinal = new ArrayList<>();
    private ArrayList<GerenciarPacienteBean> listaDiasProfissional;
    private ArrayList<InsercaoPacienteBean> listaLaudosVigentes;
    private ArrayList<AgendaBean> listAgendamentoProfissional;
    private ArrayList<FuncionarioBean> listaProfissionaisEquipe;
    private ArrayList<FuncionarioBean> listaProfissionaisAdicionados;
    private Integer id_paciente_insituicao;
    private String opcaoAtendimento;
    private List<Integer> listaDias;
    private EmpresaDAO empresaDAO = new EmpresaDAO();
    private AgendaDAO agendaDAO = new AgendaDAO();
    private AlteracaoPacienteDAO aDao = new AlteracaoPacienteDAO();
    private FuncionarioBean funcionario;
    private LaudoBean laudo;
    private List<AgendaBean> listaHorariosAgenda;

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
        laudo = new LaudoBean();
        listaHorariosAgenda =  new ArrayList<AgendaBean>();
    }

    public void setaRenovacaoPacienteComOuSemLaudo() {
    	if (insercaoParaLaudo.isInsercaoPacienteSemLaudo()==true) {
    		insercaoParaLaudo.setPaciente(insercao.getLaudo().getPaciente());
    	}
    	else
    	{
    		insercaoParaLaudo.setPaciente(null);
    	}
    }
    
    public void carregaRenovacao() throws ProjetoException, ParseException {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Map<String, String> params = facesContext.getExternalContext()
                .getRequestParameterMap();
        if (params.get("id") != null) {
            Integer id = Integer.parseInt(params.get("id"));
            id_paciente_insituicao = id;
            this.insercao = aDao.carregarPacientesInstituicaoAlteracao(id);
            //vou setar nulo para a data de solicitacao, pois numa nova renovacao nao é pra carregar a data de solicitacao,
            //pois a mesma é informada na hora do lancamento pelo profissional. 
            //Esta acao é para nao criar um novo metodo especifico para carregar os dados de gerenc. paciente
            insercao.setDataSolicitacao(null);
            InsercaoPacienteController insercaoPacienteController = new InsercaoPacienteController();
            opcaoAtendimento = insercaoPacienteController.carregarHorarioOuTurno();
            laudo = new LaudoDAO().buscarLaudosPorId(insercao.getLaudo().getId());
            if (insercao.getEquipe().getCodEquipe() != null
                    && insercao.getEquipe().getCodEquipe() > 0) {
                listaProfissionaisEquipe = agendaDAO.listaProfissionaisAtendimetoParaPacienteInstituicao(id);
                tipo = TipoAtendimento.EQUIPE.getSigla();
                listaDiasProfissional = aDao
                        .listarDiasAtendimentoProfissionalEquipe(id);

                List<FuncionarioBean> listaFuncionarioAuxiliar = agendaDAO.listaProfissionaisIhDiasAtendimetoParaPacienteInstituicao(id);
                for(int i=0; i<listaFuncionarioAuxiliar.size(); i++){
                    adicionarFuncionarioParaRenovacao(listaFuncionarioAuxiliar.get(i));
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
        Date d2 = iDao.dataFinalLaudo(insercaoParaLaudo.getLaudo().getId());
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
                        .getListDiasSemana().size(); j++) {

                    if (diaSemana == Integer.parseInt(insercao.getFuncionario()
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

    public void abrirDialog(){
        JSFUtil.abrirDialog("dlgDiasAtendimento");
    }

    public void adicionarFuncionarioParaRenovacao(FuncionarioBean funcionario) {
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

    }

    public void excluirFuncionarioIhDiasDeAtendimento(){
        funcionario.getListDiasSemana().remove(funcionario);
        listaProfissionaisAdicionados.remove(funcionario);
    }

    public void gravarRenovacaoPaciente() throws ProjetoException {

        if (((insercaoParaLaudo.getLaudo() != null) && (insercaoParaLaudo.getLaudo().getId() != null)) || ((insercaoParaLaudo.getPaciente() != null) && (insercaoParaLaudo.getPaciente().getId_paciente() != null))) {
        	Integer codPaciente = null;
        	if ((insercaoParaLaudo.getLaudo() != null) && (insercaoParaLaudo.getLaudo().getId() != null)) 
        			codPaciente = insercaoParaLaudo.getLaudo().getPaciente().getId_paciente();
        	
        	if  ((insercaoParaLaudo.getPaciente() != null) && (insercaoParaLaudo.getPaciente().getId_paciente() != null))
        		codPaciente = insercaoParaLaudo.getPaciente().getId_paciente();

            InsercaoPacienteController insercaoPacienteController = new InsercaoPacienteController();

            Boolean cadastrou = null;

            GerenciarPacienteController gerenciarPacienteController = new GerenciarPacienteController();
            Date dataSolicitacaoCorreta = gerenciarPacienteController.ajustarDataDeSolicitacao(insercao.getDataSolicitacao(), insercaoParaLaudo.getLaudo().getId(),codPaciente, insercao.getPrograma().getIdPrograma(), insercao.getGrupo().getIdGrupo());
            insercao.setDataSolicitacao(dataSolicitacaoCorreta);

            ArrayList<AgendaBean> listaAgendamentosProfissionalFinal = insercaoPacienteController.validarDatas(listAgendamentoProfissional, insercao.getTurno());

            if (tipo.equals(TipoAtendimento.EQUIPE.getSigla())) {

                if (opcaoAtendimento.equals(OpcaoAtendimento.SOMENTE_TURNO.getSigla())){
                	gerarListaAgendamentosEquipeTurno();	
                }
                
                if (opcaoAtendimento.equals(OpcaoAtendimento.SOMENTE_HORARIO.getSigla())) {
                	gerarListaAgendamentosEquipeDiaHorario();
                }

            	if (opcaoAtendimento.equals(OpcaoAtendimento.SOMENTE_HORARIO.getSigla()))
                    cadastrou = rDao.gravarRenovacaoEquipeDiaHorario(insercao,insercaoParaLaudo, listaAgendamentosProfissionalFinal,  listaHorarioFinal);
                	else
                        cadastrou = rDao.gravarRenovacaoEquipeTurno(insercao,insercaoParaLaudo,
                                listaProfissionaisAdicionados, listaAgendamentosProfissionalFinal);
                
            }
            if (tipo.equals(TipoAtendimento.PROFISSIONAL.getSigla())) {

                gerarListaAgendamentosProfissional();

                cadastrou = rDao.gravarInsercaoProfissional(insercao,
                        insercaoParaLaudo, listaAgendamentosProfissionalFinal);
            }

            if (cadastrou == true) {
                JSFUtil.adicionarMensagemSucesso("Renovação de Paciente cadastrada com sucesso!", "Sucesso");
                JSFUtil.abrirDialog("dlgRenovacaoEfetuada");
            } else {
                JSFUtil.adicionarMensagemErro("Ocorreu um erro durante o cadastro!", "Erro");
            }
        } else {
            JSFUtil.adicionarMensagemAdvertencia("Carregue um laudo ou selecione um Paciente!", "Bloqueio");
        }
    }

    public void listarLaudosVigentes()
            throws ProjetoException {
        LaudoDAO laudoDAO = new LaudoDAO();
        listaLaudosVigentes = laudoDAO.listarLaudosVigentesParaPaciente(insercao.getLaudo().getPaciente().getId_paciente());
    }

    public void carregaHorariosEquipe() {
        listaHorariosAgenda =  agendaDAO.quantidadeDeAgendamentosDaEquipePorTurno();
    }


    public void carregarLaudoPaciente() throws ProjetoException {

        String condicao_datas_laudo = compararDatasLaudo(laudo.getMesFinal(), laudo.getAnoFinal(),
                insercao.getLaudo().getMesInicio(), insercao.getLaudo().getAnoInicio());

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
            insercaoParaLaudo = iDao.carregarLaudoPaciente(insercao.getLaudo()
                    .getId());
            insercao = rDao
                    .carregarPacientesInstituicaoRenovacao(id_paciente_insituicao);
        }
    }

    public String compararDatasLaudo(Integer mesAtual, Integer anoAtual, Integer mesNovo, Integer anoNovo){

        String retorno = "";

        final Boolean INICIO_MES = true;
        final Boolean FIM_MES = false;

        Date dataLaudoAtual = DataUtil.adicionarMesIhAnoEmDate(mesAtual, anoAtual, FIM_MES);
        Date dataLaudoNovo = DataUtil.adicionarMesIhAnoEmDate(mesNovo, anoNovo, INICIO_MES);

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


}
