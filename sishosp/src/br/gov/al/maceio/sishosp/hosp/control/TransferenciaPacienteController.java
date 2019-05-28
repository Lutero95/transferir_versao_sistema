package br.gov.al.maceio.sishosp.hosp.control;

import br.gov.al.maceio.sishosp.acl.dao.FuncionarioDAO;
import br.gov.al.maceio.sishosp.acl.model.FuncionarioBean;
import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.JSFUtil;
import br.gov.al.maceio.sishosp.comum.util.RedirecionarUtil;
import br.gov.al.maceio.sishosp.hosp.dao.*;
import br.gov.al.maceio.sishosp.hosp.enums.DiasDaSemana;
import br.gov.al.maceio.sishosp.hosp.enums.OpcaoAtendimento;
import br.gov.al.maceio.sishosp.hosp.enums.TipoAtendimento;
import br.gov.al.maceio.sishosp.hosp.model.*;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@ManagedBean(name = "TransferenciaPacienteController")
@ViewScoped
public class TransferenciaPacienteController implements Serializable {

    private static final long serialVersionUID = 1L;
    private TransferenciaPacienteDAO aDao = new TransferenciaPacienteDAO();
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
        listAgendamentoProfissional = new ArrayList<InsercaoPacienteBean>();
        todosOsProfissionais = false;
        listaHorariosAgenda =  new ArrayList<AgendaBean>();
        listaEquipePorGrupo = new ArrayList<>();
    }

    public void carregarTransferencia() throws ProjetoException, ParseException {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Map<String, String> params = facesContext.getExternalContext()
                .getRequestParameterMap();
        if (params.get("id") != null) {
            Integer id = Integer.parseInt(params.get("id"));
            id_paciente_insituicao = id;
            this.insercao = aDao.carregarPacientesInstituicaoAlteracao(id);
            carregarLaudoPaciente();
            InsercaoPacienteController insercaoPacienteController = new InsercaoPacienteController();
            opcaoAtendimento = insercaoPacienteController.carregarHorarioOuTurno();
            tipo = TipoAtendimento.EQUIPE.getSigla();

        } else {
            JSFUtil.adicionarMensagemErro("Ocorreu um erro!", "Erro");
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

    public void excluirFuncionarioIhDiasDeAtendimento() {
        funcionario.getListDiasSemana().remove(funcionario);
        listaProfissionaisAdicionados.remove(funcionario);
    }

    public String gravarAlteracaoPaciente() throws ProjetoException {

        String retorno = "";

        if(validarProfissionaisAdicionados()) {
            Boolean cadastrou = null;

            InsercaoPacienteController insercaoPacienteController = new InsercaoPacienteController();

            ArrayList<InsercaoPacienteBean> listaAgendamentosProfissionalFinal = insercaoPacienteController.validarDatas(
                    listAgendamentoProfissional, insercao.getAgenda().getTurno());

            gerarListaAgendamentosEquipe();

            cadastrou = aDao.gravarTransferenciaEquipe(insercao, insercaoParaLaudo,
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
    
    public void carregaListaEquipePorGrupo()
            throws ProjetoException {
        System.out.println("listaEquipePorTipoAtendimento");
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

    public void adicionarFuncionarioParaEdicao(FuncionarioBean funcionario) {
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

    }

    public List<GrupoBean> listaGrupoAutoComplete(String query)
            throws ProjetoException {

        if (insercao.getPrograma().getIdPrograma() != null) {
            GrupoDAO gDao = new GrupoDAO();
            return gDao.listarGruposAutoComplete(query,
                    this.insercao.getPrograma());
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
}
