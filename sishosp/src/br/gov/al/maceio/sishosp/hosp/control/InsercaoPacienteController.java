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

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.event.SelectEvent;

import br.gov.al.maceio.sishosp.acl.dao.FuncionarioDAO;
import br.gov.al.maceio.sishosp.acl.model.FuncionarioBean;
import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.DataUtil;
import br.gov.al.maceio.sishosp.comum.util.HorarioOuTurnoUtil;
import br.gov.al.maceio.sishosp.comum.util.JSFUtil;
import br.gov.al.maceio.sishosp.comum.util.VerificadorUtil;
import br.gov.al.maceio.sishosp.hosp.abstracts.VetorDiaSemanaAbstract;
import br.gov.al.maceio.sishosp.hosp.dao.AgendaDAO;
import br.gov.al.maceio.sishosp.hosp.dao.CidDAO;
import br.gov.al.maceio.sishosp.hosp.dao.EquipeDAO;
import br.gov.al.maceio.sishosp.hosp.dao.FeriadoDAO;
import br.gov.al.maceio.sishosp.hosp.dao.GrupoDAO;
import br.gov.al.maceio.sishosp.hosp.dao.InsercaoPacienteDAO;
import br.gov.al.maceio.sishosp.hosp.dao.ProcedimentoDAO;
import br.gov.al.maceio.sishosp.hosp.dao.ProgramaDAO;
import br.gov.al.maceio.sishosp.hosp.dao.TipoAtendimentoDAO;
import br.gov.al.maceio.sishosp.hosp.enums.DiasDaSemana;
import br.gov.al.maceio.sishosp.hosp.enums.MotivoLiberacao;
import br.gov.al.maceio.sishosp.hosp.enums.OpcaoAtendimento;
import br.gov.al.maceio.sishosp.hosp.enums.TipoAtendimento;
import br.gov.al.maceio.sishosp.hosp.enums.Turno;
import br.gov.al.maceio.sishosp.hosp.enums.ValidacaoSenha;
import br.gov.al.maceio.sishosp.hosp.model.AgendaBean;
import br.gov.al.maceio.sishosp.hosp.model.CidBean;
import br.gov.al.maceio.sishosp.hosp.model.EquipeBean;
import br.gov.al.maceio.sishosp.hosp.model.GrupoBean;
import br.gov.al.maceio.sishosp.hosp.model.HorarioAtendimento;
import br.gov.al.maceio.sishosp.hosp.model.InsercaoPacienteBean;
import br.gov.al.maceio.sishosp.hosp.model.Liberacao;
import br.gov.al.maceio.sishosp.hosp.model.ProcedimentoBean;
import br.gov.al.maceio.sishosp.hosp.model.ProgramaBean;
import br.gov.al.maceio.sishosp.hosp.model.TipoAtendimentoBean;
import br.gov.al.maceio.sishosp.hosp.model.dto.AvaliacaoInsercaoDTO;

@ManagedBean(name = "InsercaoController")
@ViewScoped
public class InsercaoPacienteController extends VetorDiaSemanaAbstract implements Serializable {

    private static final long serialVersionUID = 1L;
    private InsercaoPacienteBean insercao;
    private InsercaoPacienteDAO iDao = new InsercaoPacienteDAO();
    private EquipeDAO eDao = new EquipeDAO();
    private ProgramaDAO programaDAO = new ProgramaDAO();
    private AgendaDAO agendaDAO = new AgendaDAO();
    private ProgramaBean programaSelecionado;
    private ArrayList<InsercaoPacienteBean> listaLaudosVigentes;
    private ArrayList<FuncionarioBean> listaProfissionaisEquipe;
    private ArrayList<FuncionarioBean> listaProfissionaisAdicionados;
    private List<FuncionarioBean> listaProfissionais;
    private List<GrupoBean> listaGruposProgramas;
    private String tipo;
    private FuncionarioBean funcionario;
    private ArrayList<AgendaBean> listAgendamentoProfissional;
    private List<EquipeBean> listaEquipePorGrupo;
    private String opcaoAtendimento;
    private ArrayList<String> listaHorarios;
    private Boolean todosOsProfissionais;
    private Boolean renderizarAposLaudo;
    private List<AgendaBean> listaHorariosDaEquipe;
    private Boolean liberacao;
    private Boolean ehAvaliacao;
    private ArrayList<Liberacao> listaLiberacao;
    private ArrayList<HorarioAtendimento> listaHorarioAtendimentos;
    private List<HorarioAtendimento> listaHorarioAtendimentosAuxiliar;
    private List<HorarioAtendimento> listaHorarioFinal = new ArrayList<>();
    private List<Integer> listaDias;
	private String tipoBusca;
	private String campoBusca;
	private List<ProcedimentoBean> listaProcedimentos;
	private List<CidBean> listaCids;
	private List<ProcedimentoBean> listaProcedimentosAux;
	private List<CidBean> listaCidsAux;
	private HorarioAtendimento horarioAtendimento;

    public InsercaoPacienteController() throws ProjetoException {
        this.insercao = new InsercaoPacienteBean();
        listaHorariosDaEquipe = new ArrayList<AgendaBean>();
        programaSelecionado = new ProgramaBean();
        listaLaudosVigentes = new ArrayList<InsercaoPacienteBean>();
        this.tipo = TipoAtendimento.EQUIPE.getSigla();
        funcionario = new FuncionarioBean();
        listaProfissionaisAdicionados = new ArrayList<FuncionarioBean>();
        listAgendamentoProfissional = new ArrayList<AgendaBean>();
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
        listaProfissionaisEquipe = new ArrayList<>();
        listaProfissionaisAdicionados = new ArrayList<>();
        listaProcedimentos = new ArrayList<>();
        listaCids = new ArrayList<>();
    }

    public void carregarHorarioOuTurnoInsercao() throws ProjetoException, ParseException {
        opcaoAtendimento = HorarioOuTurnoUtil.retornarOpcaoAtendimentoUnidade();

        if (opcaoAtendimento.equals(OpcaoAtendimento.SOMENTE_HORARIO.getSigla()) || opcaoAtendimento.equals(OpcaoAtendimento.AMBOS.getSigla())) {
            gerarHorariosAtendimento();
        }
    }

    public String carregarHorarioOuTurno() throws ProjetoException, ParseException {
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
        this.listaProfissionaisAdicionados = new ArrayList<>();
        this.listAgendamentoProfissional = new ArrayList<>();
        limparDias();
    }

    public void limparDias() {
        funcionario.setListaDiasAtendimentoSemana(new ArrayList<HorarioAtendimento>());
    }

    public void listarLaudosVigentes(String campoBusca, String tipoBusca)
            throws ProjetoException {
        listaLaudosVigentes = iDao.listarLaudosVigentes(campoBusca, tipoBusca);
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
    
    public void listarDadosPermitidosDoPrograma(Integer idPrograma) throws ProjetoException {
        this.listaProcedimentos = programaDAO.listarProcedimentosPermitidos(idPrograma);
        this.listaCids = programaDAO.listarCidsPermitidos(idPrograma);
    }

    public void atualizaListaGrupos(ProgramaBean programa) throws ProjetoException {
        GrupoDAO gDao = new GrupoDAO();
        this.programaSelecionado = programa;
        this.listaGruposProgramas = gDao.listarGruposPorPrograma(programa.getIdPrograma());
        for (GrupoBean g : listaGruposProgramas) {
        }

    }
    
    public void selectGrupo(SelectEvent event) throws ProjetoException {
        this.insercao.setGrupo((GrupoBean) event.getObject());
        limparEquipeSelecionada();
    }
    
    public void limparEquipeSelecionada() {
    	limparTabelasProfissionais();
        this.insercao.setEquipe(new EquipeBean());
    }

    public void carregaListaEquipePorTipoAtendimento()
            throws ProjetoException {
        if (insercao.getGrupo().getIdGrupo() != null) {
            listaEquipePorGrupo = eDao
                    .listarEquipePorGrupo(this.insercao.getGrupo().getIdGrupo());
        }
    }


    public void validarCarregarLaudoPaciente() throws ProjetoException {
        int id = insercao.getLaudo().getId();

        if (validarCarregamentoDoLaudo(id)) {
            carregarLaudoPaciente(id);
        }

    }

    private void carregarLaudoPaciente(int id) throws ProjetoException {
        limparDados();
        insercao = iDao.carregarLaudoPaciente(id);
        renderizarAposLaudo = true;

        if (ehAvaliacao) {
            carregarDadosAvaliacao();
        }
    }

    private void carregarDadosAvaliacao() throws ProjetoException{
        AvaliacaoInsercaoDTO avaliacaoInsercaoDTO = iDao.carregarAtendimentoAvaliacao(insercao.getLaudo().getId());
        insercao.setPrograma(avaliacaoInsercaoDTO.getProgramaBean());
        insercao.setGrupo(avaliacaoInsercaoDTO.getGrupoBean());
        insercao.setEquipe(avaliacaoInsercaoDTO.getEquipeBean());
        listaProfissionaisEquipe = avaliacaoInsercaoDTO.getListaProfissionais();
    }

    public Boolean validarCarregamentoDoLaudo(Integer idLaudo) throws ProjetoException {
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
        FuncionarioDAO funcionarioDAO = new FuncionarioDAO();

        FuncionarioBean func = funcionarioDAO.validarCpfIhSenha(funcionario.getCpf(), funcionario.getSenha(), ValidacaoSenha.LIBERACAO_PACIENTES_SEM_PERFIL.getSigla());

        if (func!=null) {
            liberacao = true;
            JSFUtil.fecharDialog("dlgSenha");
            adicionarLiberacaoNaLista(montarLiberacao(MotivoLiberacao.AVALIACAO.getSigla(), func.getId()));
            carregarLaudoPaciente(insercao.getLaudo().getId());
            listarProfissionaisEquipe();
        } else {
            JSFUtil.adicionarMensagemErro("Funcionário com senha errada ou sem liberação!", "Erro!");
        }
    }

    public Liberacao montarLiberacao(String motivo, Long idUsuarioLiberacao){
        Liberacao liberacao = null;
        return liberacao = new Liberacao(motivo, idUsuarioLiberacao.intValue(), DataUtil.retornarDataIhHoraAtual());
    }

    public void adicionarLiberacaoNaLista(Liberacao liberacao){
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
                dias = dias + "Domingo " + (listaHorarioAtendimentos.get(i).getHorario()!=null? listaHorarioAtendimentos.get(i).getHorario():"");

                if (listaHorarioAtendimentos.size() > 1 && listaHorarioAtendimentos.size() != i + 1) {
                    dias = dias + ", ";
                }
            }

            if (listaHorarioAtendimentos.get(i).getDiaSemana().toString().equals(DiasDaSemana.SEGUNDA.getSigla())) {
                dias = dias + "Segunda " +(listaHorarioAtendimentos.get(i).getHorario()!=null? listaHorarioAtendimentos.get(i).getHorario():"");
                if (listaHorarioAtendimentos.size() > 1 && listaHorarioAtendimentos.size() != i + 1) {
                    dias = dias + ", ";
                }
            }

            if (listaHorarioAtendimentos.get(i).getDiaSemana().toString().equals(DiasDaSemana.TERCA.getSigla())) {
                dias = dias + "Terça " + (listaHorarioAtendimentos.get(i).getHorario()!=null? listaHorarioAtendimentos.get(i).getHorario():"");
                if (listaHorarioAtendimentos.size() > 1 && listaHorarioAtendimentos.size() != i + 1) {
                    dias = dias + ", ";
                }
            }

            if (listaHorarioAtendimentos.get(i).getDiaSemana().toString().equals(DiasDaSemana.QUARTA.getSigla())) {
                dias = dias + "Quarta " + (listaHorarioAtendimentos.get(i).getHorario()!=null? listaHorarioAtendimentos.get(i).getHorario():"");
                if (listaHorarioAtendimentos.size() > 1 && listaHorarioAtendimentos.size() != i + 1) {
                    dias = dias + ", ";
                }
            }

            if (listaHorarioAtendimentos.get(i).getDiaSemana().toString().equals(DiasDaSemana.QUINTA.getSigla())) {
                dias = dias + "Quinta " +(listaHorarioAtendimentos.get(i).getHorario()!=null? listaHorarioAtendimentos.get(i).getHorario():"");
                if (listaHorarioAtendimentos.size() > 1 && listaHorarioAtendimentos.size() != i + 1) {
                    dias = dias + ", ";
                }
            }

            if (listaHorarioAtendimentos.get(i).getDiaSemana().toString().equals(DiasDaSemana.SEXTA.getSigla())) {
                dias = dias + "Sexta " + (listaHorarioAtendimentos.get(i).getHorario()!=null? listaHorarioAtendimentos.get(i).getHorario():"");
                if (listaHorarioAtendimentos.size() > 1 && listaHorarioAtendimentos.size() != i + 1) {
                    dias = dias + ", ";
                }
            }

            if (listaHorarioAtendimentos.get(i).getDiaSemana().toString().equals(DiasDaSemana.SABADO.getSigla())) {
                dias = dias + "Sábado " + (listaHorarioAtendimentos.get(i).getHorario()!=null? listaHorarioAtendimentos.get(i).getHorario():"");
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
    
    /*
    public boolean verificaSeExisteDiaSemanaEHorario(List<HorarioAtendimento> lista, int diaSemana, String horario) {
    	boolean rst = false;
        for (int j = 0; j < listaHorarioFinal.size(); j++) {
            for (int i = 0; i < lista.size(); i++) {

                if (listaHorarioFinal.get(j).getDiaSemana().equals(diaSemana)
                        && listaHorarioFinal.get(j).getHorario().equals(horario)) {
                	rst = true;
                }
                
            }
            
        }	
        return rst;
    }
    */


    public void excluirDiasHorariosDoFuncionario(HorarioAtendimento horarioAtendimento) {
        listaHorarioAtendimentos.remove(horarioAtendimento);
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
    
    public void validarDiaTurno() {
    	if(this.funcionario.getListaDiasAtendimentoSemana().isEmpty())
    		JSFUtil.adicionarMensagemErro("Insira pelo menos um dia e turno", "");
    	else
    		validarAdicionarFuncionarioTurno();
    }

    // VALIDAÇÃO DE NÃO REPETIR O PROFISSIONAL
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
        //Retirado para análise futura, retirei na véspera da apresentação para a funcionalidade ficar ok. Data: 26/03/2019
        //limparDias();
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

    public void excluirFuncionarioIhDiasDeAtendimento() {
        //funcionario.getListaDiasAtendimentoSemana().remove(funcionario);

        funcionario.setDiasSemana("");
        funcionario.setListDiasSemana(new ArrayList<>());
        funcionario.setListaDiasAtendimentoSemana(new ArrayList<>());
        listaProfissionaisAdicionados.remove(funcionario);
        funcionario =  new FuncionarioBean();
    }

    public void adicionarFuncionarioTurno() {
        String dias = "";

        for (int i = 0; i < funcionario.getListDiasSemana().size(); i++) {
        	HorarioAtendimento horarioAtendimento = new HorarioAtendimento();
        	horarioAtendimento.setDiaSemana(Integer.parseInt(funcionario.getListDiasSemana().get(i)));
            	funcionario.getListaDiasAtendimentoSemana().add(horarioAtendimento);
            
        }

        for (int i = 0; i < funcionario.getListaDiasAtendimentoSemana().size(); i++) {
            if (funcionario.getListaDiasAtendimentoSemana().get(i).getDiaSemana().toString().equals(DiasDaSemana.DOMINGO.getSigla())
            		&& !dias.contains("Domingo")) {
                dias = dias + "Domingo";

                if (funcionario.getListaDiasAtendimentoSemana().size() > 1 && funcionario.getListaDiasAtendimentoSemana().size() != i + 1) {
                    dias = dias + ", ";
                }
            }

            if (funcionario.getListaDiasAtendimentoSemana().get(i).getDiaSemana().toString().equals(DiasDaSemana.SEGUNDA.getSigla())
            		&& !dias.contains("Segunda")) {
                dias = dias + "Segunda";
                if (funcionario.getListaDiasAtendimentoSemana().size() > 1 && funcionario.getListaDiasAtendimentoSemana().size() != i + 1) {
                    dias = dias + ", ";
                }
            }

            if (funcionario.getListaDiasAtendimentoSemana().get(i).getDiaSemana().toString().equals(DiasDaSemana.TERCA.getSigla())
            		&& !dias.contains("Terça")) {
                dias = dias + "Terça";
                if (funcionario.getListaDiasAtendimentoSemana().size() > 1 && funcionario.getListaDiasAtendimentoSemana().size() != i + 1) {
                    dias = dias + ", ";
                }
            }

            if (funcionario.getListaDiasAtendimentoSemana().get(i).getDiaSemana().toString().equals(DiasDaSemana.QUARTA.getSigla())
            		&& !dias.contains("Quarta")) {
                dias = dias + "Quarta";
                if (funcionario.getListaDiasAtendimentoSemana().size() > 1 && funcionario.getListaDiasAtendimentoSemana().size() != i + 1) {
                    dias = dias + ", ";
                }
            }

            if (funcionario.getListaDiasAtendimentoSemana().get(i).getDiaSemana().toString().equals(DiasDaSemana.QUINTA.getSigla())
            		&& !dias.contains("Quinta")) {
                dias = dias + "Quinta";
                if (funcionario.getListaDiasAtendimentoSemana().size() > 1 && funcionario.getListaDiasAtendimentoSemana().size() != i + 1) {
                    dias = dias + ", ";
                }
            }

            if (funcionario.getListaDiasAtendimentoSemana().get(i).getDiaSemana().toString().equals(DiasDaSemana.SEXTA.getSigla())
            		&& !dias.contains("Sexta")) {
                dias = dias + "Sexta";
                if (funcionario.getListaDiasAtendimentoSemana().size() > 1 && funcionario.getListaDiasAtendimentoSemana().size() != i + 1) {
                    dias = dias + ", ";
                }
            }

            if (funcionario.getListaDiasAtendimentoSemana().get(i).getDiaSemana().toString().equals(DiasDaSemana.SABADO.getSigla())
            		&& !dias.contains("Sábado")) {
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
        JSFUtil.fecharDialog("dlgDiaTurno");
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
                        .getListaDiasAtendimentoSemana().size(); j++) {

                    if (diaSemana ==insercao.getFuncionario()
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
    

    public void gerarListaAgendamentosEquipeTurno() throws ProjetoException {

        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        df.setLenient(false);
        GerenciarPacienteController gerenciarPacienteController = new GerenciarPacienteController();
        Date periodoInicial = gerenciarPacienteController.ajustarDataDeSolicitacao(insercao.getDataSolicitacao(), insercao.getLaudo().getId(), insercao.getPaciente().getId_paciente(), insercao.getPrograma().getIdPrograma(), insercao.getGrupo().getIdGrupo());
        Date d1 = periodoInicial;
        Date d2 = iDao.dataFinalLaudo(insercao.getLaudo().getId());
        
        int dt = DataUtil.calculaQuantidadeDiasEntreDuasDatas(d2.getTime() , d1.getTime());
      

        Calendar c = Calendar.getInstance();
        c.setTime(periodoInicial);
        
        for (int i = 0; i <= dt; i++) {

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
            c.add(Calendar.DAY_OF_MONTH, 1);

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

                            agenda.setDataAtendimento(c.getTime());

                            listAgendamentoProfissional.add(agenda);
                            listaDatasDeAtendimento.add(c.getTime());

                        }
                    }
                }
            }

        }


    }
    
    public boolean dataInclusaoPacienteEstaEntreDataInicialIhFinalDoLaudo() throws ProjetoException {
    	boolean dataValida =  iDao.dataInclusaoPacienteEstaEntreDataInicialIhFinalDoLaudo(insercao.getLaudo().getId(), insercao.getDataSolicitacao()); 
    	if(!dataValida)
    		JSFUtil.adicionarMensagemErro("Data de Inclusão não está dentro do intervalo da data do laudo", "Erro!");
    	return dataValida; 
    }
    
    public boolean listaProfissionaisAdicionadosEstaVazia(List<FuncionarioBean> listaFuncionariosAdicionados) {
    	if(listaFuncionariosAdicionados.isEmpty()) {
    		JSFUtil.adicionarMensagemAdvertencia("Por favor adicione pelo menos um profissional", "Atenção");
    		return true;
    	}
    	return false;
    }
    
    public void validarInsercaoPaciente() throws ProjetoException, SQLException {
    	if(dataInclusaoPacienteEstaEntreDataInicialIhFinalDoLaudo() && 
    			procedimentoValido(insercao.getLaudo().getProcedimentoPrimario(), insercao.getPrograma(), insercao.getGrupo())) {
			
//			  GerenciarPacienteController gerenciarPacienteController = new
//			  GerenciarPacienteController(); 
//			  Date dataSolicitacaoCorreta =
//			  gerenciarPacienteController.ajustarDataDeSolicitacao(
//			  insercao.getDataSolicitacao(), insercao.getLaudo().getId(),
//			  insercao.getPaciente().getId_paciente(),
//			  insercao.getPrograma().getIdPrograma(), insercao.getGrupo().getIdGrupo());
			 
			// insercao.setDataSolicitacao(dataSolicitacaoCorreta);
			if (VerificadorUtil.verificarSeObjetoNulo(insercao.getTurno())) {
				JSFUtil.adicionarMensagemErro("Turno do Atendimento é obrigatório", "Erro");
			} else if (!VerificadorUtil.verificarSeObjetoNuloOuZero(insercao.getPrograma().getIdPrograma()) && !VerificadorUtil.verificarSeObjetoNuloOuZero(insercao.getGrupo().getIdGrupo())) {
				if (iDao.verificarSeExisteLaudoAtivoParaProgramaIhGrupo(insercao.getPrograma().getIdPrograma(),
						insercao.getGrupo().getIdGrupo(), insercao.getLaudo().getPaciente().getId_paciente())) {
					JSFUtil.adicionarMensagemErro("Paciente já está ativo neste Programa/Grupo", "Erro");
				}
				
				else if (insercao.getEncaixe()) {
					gravarInsercaoPaciente();
				} 
				
				else if (tipo.equals(TipoAtendimento.EQUIPE.getSigla())) {
					if (agendaDAO.numeroAtendimentosEquipe(insercao)) {
						gravarInsercaoPaciente();
					} else {
						JSFUtil.adicionarMensagemErro(
								"Quantidade de agendamentos para essa equipe já antigiu o máximo para esse dia!", "Erro");
					}
				} 
			}
			
			else {
				if (insercao.getEncaixe()) {
					gravarInsercaoPaciente();
				}
				else if (agendaDAO.numeroAtendimentosProfissional(insercao)) {
					gravarInsercaoPaciente();
				} else {
					JSFUtil.adicionarMensagemErro(
							"Quantidade de agendamentos para esse profissional já antigiu o máximo para esse horário e dia!",
							"Erro");
				}
			}
    	}
    }
    
    public boolean procedimentoValido(ProcedimentoBean procedimentoLaudo, ProgramaBean programa, GrupoBean grupo) throws ProjetoException, SQLException {
    	ProgramaDAO pDAo = new ProgramaDAO();
    	ProcedimentoBean procedimentoPadraoGrupo = pDAo.retornaProcedimentoPadraoDoGrupoNoPrograma(programa, grupo);
        if((!procedimentoLaudo.getIdProc().equals(programa.getProcedimento().getIdProc())) && ((procedimentoPadraoGrupo!=null) && (!procedimentoLaudo.getIdProc().equals(procedimentoPadraoGrupo.getIdProc())))) {
    		JSFUtil.adicionarMensagemErro("Procedimento do Laudo é Incompatível com o Procedimento do Programa/Grupo Selecionado", "");
    		return false;
    	}
    	return true;
    }

    public ArrayList<AgendaBean> validarDatas(ArrayList<AgendaBean> listaAgendamentos, String turno) throws ProjetoException {
        ArrayList<AgendaBean> listaAgendamentosAux = new ArrayList<>();

        for (int i = 0; i < listaAgendamentos.size(); i++) {
            if (verificarSeEhFeriadoData(listaAgendamentos.get(i).getDataAtendimento())) {
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

    /*
    public Boolean verificarSeTemBloqueioData(InsercaoPacienteBean insercaoBean, String turno) throws ProjetoException {
        Boolean retorno = false;

        retorno = new BloqueioDAO().verificarBloqueioProfissionalDataUnica
                (insercaoBean.getAgenda().getProfissional().getId(), insercaoBean.getAgenda().getDataMarcacao(), turno);

        return retorno;
    } */

    public void gravarInsercaoPaciente() throws ProjetoException {
            if (insercao.getLaudo().getId() != null) {

                Boolean cadastrou = false;

                if (opcaoAtendimento.equals(OpcaoAtendimento.SOMENTE_TURNO.getSigla())) {
                    gerarListaAgendamentosEquipeTurno();
                }

                if (opcaoAtendimento.equals(OpcaoAtendimento.SOMENTE_HORARIO.getSigla())) {
                    gerarListaAgendamentosEquipeDiaHorario();
                }

                ArrayList<AgendaBean> listaAgendamentosProfissionalFinal = validarDatas(listAgendamentoProfissional, insercao.getTurno());

                if (tipo.equals(TipoAtendimento.EQUIPE.getSigla())) {
                	if(!listaProfissionaisAdicionadosEstaVazia(this.listaProfissionaisAdicionados)) {
                		// gerarListaAgendamentosEquipe();
                		if (opcaoAtendimento.equals(OpcaoAtendimento.SOMENTE_HORARIO.getSigla()))
                			cadastrou = iDao.gravarInsercaoEquipeDiaHorario(insercao, listaAgendamentosProfissionalFinal,
								listaLiberacao, listaProfissionaisAdicionados);
                		else
                			cadastrou = iDao.gravarInsercaoEquipeTurno(insercao, listaProfissionaisAdicionados,
								listaAgendamentosProfissionalFinal, listaLiberacao);
                    }
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

    private void gerarHorariosAtendimento() throws ParseException, ProjetoException {
        listaHorarios = HorarioOuTurnoUtil.gerarHorariosAtendimento();
    }

    public void visualizarHorariosEquipe() throws ProjetoException {
        listaHorariosDaEquipe = agendaDAO.quantidadeDeAgendamentosDaEquipePorTurno();
    }

    // AUTOCOMPLETE INÍCIO

    public List<EquipeBean> listaEquipeAutoComplete(String query)
            throws ProjetoException {

        List<EquipeBean> result = eDao.listarEquipePorGrupoAutoComplete(query,
                this.insercao.getGrupo().getIdGrupo());
        return result;
    }

    public void listarProfissionaisEquipe() throws ProjetoException {
    	limparTabelasProfissionais();
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
        turnoInsercaoReceberValorTurnoEquipe();
    }
    
    public void turnoInsercaoReceberValorTurnoEquipe(){
        insercao.setTurno(insercao.getEquipe().getTurno());
    }

    public void zerarValorTurnoInsercao() throws ProjetoException {
        if(tipo.equals(TipoAtendimento.PROFISSIONAL.getSigla())) {
            insercao.setTurno("");
        }
        else{
            listarProfissionaisEquipe();
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
    
    private void limparTabelasProfissionais() {
    	this.listaProfissionaisEquipe.clear();
    	this.listaProfissionaisAdicionados.clear();
    }
    
    public void abrirDialogTurno() {
        //funcionario.setListDiasSemana(new ArrayList<>());
        JSFUtil.atualizarComponente("formDiasAtendimentoTurno");
        JSFUtil.abrirDialog("dlgDiasAtendimentoTurno");
    }

    // AUTOCOMPLETE FIM
    
    public void selectGrupoInsercaoSemLaudo(SelectEvent event) throws ProjetoException {
        this.insercao.setGrupo((GrupoBean) event.getObject());
    }
    
    public void listarProfissionaisSemLaudo() throws ProjetoException {
    	limparTabelasProfissionais();
		listaProfissionais = programaDAO.listarProfissionaisInsercaoSemlaudo
				(insercao.getPrograma().getIdPrograma(), insercao.getGrupo().getIdGrupo());
    }
    
    public void validarProcedimentoParaAdicionar(ProcedimentoBean procedimentoSelecionado) {
    	for (ProcedimentoBean procedimento : insercao.getPrograma().getListaProcedimentosPermitidos()) {
			if(procedimento.getIdProc().equals(procedimentoSelecionado.getIdProc())) {
				JSFUtil.adicionarMensagemSucesso("Este procedimento já foi adicionado", "");
				return;
			}
		}
    	insercao.getPrograma().getListaProcedimentosPermitidos().add(procedimentoSelecionado);
    	listaProcedimentosAux = insercao.getPrograma().getListaProcedimentosPermitidos();
    }
    
    public void removerProcedimento(ProcedimentoBean procedimentoSelecionado) {
    	insercao.getPrograma().getListaProcedimentosPermitidos().remove(procedimentoSelecionado);
    	listaProcedimentosAux = insercao.getPrograma().getListaProcedimentosPermitidos();
    }
    
    public void validarCidParaAdicionar(CidBean cidSelecionado) {
        if (insercao.getPrograma().getListaCidsPermitidos()!=null)
    	for (CidBean cid : insercao.getPrograma().getListaCidsPermitidos()) {
			if(cid.getIdCid().equals(cidSelecionado.getIdCid())) {
				JSFUtil.adicionarMensagemSucesso("Este CID já foi adicionado", "");
				return;
			}
		}
    	insercao.getPrograma().getListaCidsPermitidos().add(cidSelecionado);
    	listaCidsAux = insercao.getPrograma().getListaCidsPermitidos(); 
    }
    
    public void removerCid(CidBean cidSelecionado) {
    	insercao.getPrograma().getListaCidsPermitidos().remove(cidSelecionado);
    	listaCidsAux = insercao.getPrograma().getListaCidsPermitidos();
    }
    
    public void inserirPacienteSemLaudo() throws ProjetoException {
        if ((listaCidsAux!=null) && (!listaCidsAux.isEmpty()))
    	insercao.getPrograma().setListaCidsPermitidos(listaCidsAux);
        if ((listaProcedimentosAux!=null) && (!listaProcedimentosAux.isEmpty()))
    	insercao.getPrograma().setListaProcedimentosPermitidos(listaProcedimentosAux);
    	if(!listaProfissionaisAdicionadosEstaVazia(this.listaProfissionaisAdicionados) 
    			&& !listaProcedimentosPermitidosEstaVazia(this.insercao.getPrograma().getListaProcedimentosPermitidos()) 
    			&& !listaCidsPermitidosEstaVazia(this.insercao.getPrograma().getListaCidsPermitidos())) {
    		
    		listAgendamentoProfissional = gerarListaAgendamentosTurnoSemLaudo(this.insercao, this.listaProfissionaisAdicionados, listAgendamentoProfissional);
    		
    		if(iDao.gravarInsercaoTurnoSemLaudo(insercao, listaProfissionaisAdicionados, listAgendamentoProfissional)) {
    			JSFUtil.adicionarMensagemSucesso("Paciente Inserido com Sucesso", "");
    			limparDadosInsercaoSemLaudo();
    		}
    	}
    }
    
    public boolean listaProcedimentosPermitidosEstaVazia(List<ProcedimentoBean> listaProcedimentosPermitidos) {
    	if ((listaProcedimentosPermitidos==null) ||  (listaProcedimentosPermitidos.isEmpty())) {
    		JSFUtil.adicionarMensagemAdvertencia("Por favor adicione pelo menos um procedimento", "Atenção");
    		return true;
    	}
    	return false;
    }
    
    public boolean listaCidsPermitidosEstaVazia(List<CidBean> listaCidsPermitidos) {
    	if ((listaCidsPermitidos==null) || (listaCidsPermitidos.isEmpty())) {
    		JSFUtil.adicionarMensagemAdvertencia("Por favor adicione pelo menos um CID", "Atenção");
    		return true;
    	}
    	return false;
    }
    
    public ArrayList<AgendaBean> gerarListaAgendamentosTurnoSemLaudo(InsercaoPacienteBean insercao, 
    		List<FuncionarioBean> listaProfissionaisAdicionados, ArrayList<AgendaBean> listAgendamentoProfissional) 
    		throws ProjetoException {

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dateFormat.setLenient(false);
        
        Date dataInicial = insercao.getDataSolicitacao();
        
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dataInicial);
        calendar.add(Calendar.DAY_OF_MONTH, insercao.getPrograma().getDiasPacienteSemLaudoAtivo());
        
        Date dataFinal = calendar.getTime();
        
        int dt = DataUtil.calculaQuantidadeDiasEntreDuasDatas(dataFinal.getTime() , dataInicial.getTime());

        calendar = Calendar.getInstance();
        calendar.setTime(dataInicial);
        
        for (int i = 0; i <= dt; i++) {

            int diaSemana = calendar.get(Calendar.DAY_OF_WEEK);
            String turnoAnterior = "";
            ArrayList<Date> listaDatasDeAtendimento = new ArrayList<Date>();
            if (tipo.equals(TipoAtendimento.EQUIPE.getSigla())) {
                for (int j = 0; j < listaProfissionaisAdicionados.size(); j++) {
                    for (int h = 0; h < listaProfissionaisAdicionados.get(j).getListaDiasAtendimentoSemana().size(); h++) {
						if (diaSemana == listaProfissionaisAdicionados.get(j).getListaDiasAtendimentoSemana().get(h)
								.getDiaSemana()) {
							
							if(!listaProfissionaisAdicionados.get(j).getListaDiasAtendimentoSemana().get(h).getTurno().equals(turnoAnterior)) {
								AgendaBean agenda = new AgendaBean();

								agenda.setPaciente(insercao.getLaudo().getPaciente());

								agenda.setDataAtendimento(calendar.getTime());
								agenda.setProfissional(listaProfissionaisAdicionados.get(j));
								agenda.setTurno(listaProfissionaisAdicionados.get(j).getListaDiasAtendimentoSemana()
										.get(h).getTurno());

								listAgendamentoProfissional.add(agenda);
								listaDatasDeAtendimento.add(calendar.getTime());
								turnoAnterior = listaProfissionaisAdicionados.get(j).getListaDiasAtendimentoSemana().get(h).getTurno();
							}
						}
                    }
                }

            }
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        
        return listAgendamentoProfissional;
    }
    
    private void limparDadosInsercaoSemLaudo() {
    	this.insercao = new InsercaoPacienteBean();
    	this.listaProfissionaisAdicionados.clear();
    	this.listaProcedimentosAux.clear();
    	this.listaCidsAux.clear();
    	this.listAgendamentoProfissional.clear();
    }
    
    public void limparHorario() {
    	this.horarioAtendimento = new HorarioAtendimento();
    }
    
    public void adicionarDiaSemanaTurno(HorarioAtendimento horarioAtendimento) {
    	
    	List<HorarioAtendimento> listaHorarioAtendimentos = new ArrayList<>();
    	if(horarioAtendimento.getTurno().equals(Turno.AMBOS.getSigla())) {
    		listaHorarioAtendimentos.add(new HorarioAtendimento(horarioAtendimento.getDiaSemana(), Turno.MANHA.getSigla()));
    		listaHorarioAtendimentos.add(new HorarioAtendimento(horarioAtendimento.getDiaSemana(), Turno.TARDE.getSigla()));
    	} else {
    		listaHorarioAtendimentos.add(horarioAtendimento);
    	}
    	
    	for (HorarioAtendimento horario : listaHorarioAtendimentos) {
    		String diaSemanaString = horario.getDiaSemana().toString();
    		setaDiaSemanaNome(horario, diaSemanaString);
    		
    		if(!diaTurnoJaExiste(horario)) {
    			this.funcionario.getListaDiasAtendimentoSemana().add(horario);
    			JSFUtil.adicionarMensagemSucesso("Dia e Turno de Atendimento Adicionado com Sucesso!", "");
    			JSFUtil.fecharDialog("dlgDiaTurno");
    		}			
		}
    }
    
    public void removerDiaSemanaTurno(HorarioAtendimento horarioAtendimento) {
    	this.funcionario.getListaDiasAtendimentoSemana().remove(horarioAtendimento);
    }
    
    private boolean diaTurnoJaExiste(HorarioAtendimento horarioAtendimento) {
    	for (HorarioAtendimento horario : this.funcionario.getListaDiasAtendimentoSemana()) {
    		if(horario.getDiaSemana().equals(horarioAtendimento.getDiaSemana())
    				&& horario.getTurno().equals(horarioAtendimento.getTurno())) {
    			JSFUtil.adicionarMensagemErro("Este dia da semana e turno já foi adicionado", "");
    			return true;
    		}
		}
    	return false;
    }

	private void setaDiaSemanaNome(HorarioAtendimento horarioAtendimento, String diaSemanaString) {
		if(diaSemanaString.equals(DiasDaSemana.DOMINGO.getSigla()))
    		horarioAtendimento.setDiaNome("Domingo");
    	if(diaSemanaString.equals(DiasDaSemana.SEGUNDA.getSigla()))
    		horarioAtendimento.setDiaNome("Segunda");
    	if(diaSemanaString.equals(DiasDaSemana.TERCA.getSigla()))
    		horarioAtendimento.setDiaNome("Terça");
    	if(diaSemanaString.equals(DiasDaSemana.QUARTA.getSigla()))
    		horarioAtendimento.setDiaNome("Quarta");
    	if(diaSemanaString.equals(DiasDaSemana.QUINTA.getSigla()))
    		horarioAtendimento.setDiaNome("Quinta");
    	if(diaSemanaString.equals(DiasDaSemana.SEXTA.getSigla()))
    		horarioAtendimento.setDiaNome("Sexta");
    	if(diaSemanaString.equals(DiasDaSemana.SABADO.getSigla()))
    		horarioAtendimento.setDiaNome("Sábado");
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

    public ArrayList<String> getListaHorarios() throws ParseException, ProjetoException {
    	if (opcaoAtendimento.equals(OpcaoAtendimento.SOMENTE_HORARIO.getSigla()) || opcaoAtendimento.equals(OpcaoAtendimento.AMBOS.getSigla())) 
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

    public ArrayList<HorarioAtendimento> getListaHorarioAtendimentos() {
        return listaHorarioAtendimentos;
    }

    public void setListaHorarioAtendimentos(ArrayList<HorarioAtendimento> listaHorarioAtendimentos) {
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

	public HorarioAtendimento getHorarioAtendimento() {
		return horarioAtendimento;
	}

	public void setHorarioAtendimento(HorarioAtendimento horarioAtendimento) {
		this.horarioAtendimento = horarioAtendimento;
	}
	
}
