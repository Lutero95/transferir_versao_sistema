package br.gov.al.maceio.sishosp.hosp.control;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import br.gov.al.maceio.sishosp.acl.model.FuncionarioBean;
import br.gov.al.maceio.sishosp.comum.enums.TipoCabecalho;
import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.JSFUtil;
import br.gov.al.maceio.sishosp.comum.util.RedirecionarUtil;
import br.gov.al.maceio.sishosp.comum.util.SessionUtil;
import br.gov.al.maceio.sishosp.comum.util.TratamentoErrosUtil;
import br.gov.al.maceio.sishosp.hosp.dao.GerenciarPacienteDAO;
import br.gov.al.maceio.sishosp.hosp.dao.PacienteDAO;
import br.gov.al.maceio.sishosp.hosp.dao.PtsCifDAO;
import br.gov.al.maceio.sishosp.hosp.model.*;

@ManagedBean
@ViewScoped
public class PtsCifController {

	private static final String GERENCIAMENTOAVALIACAOPTSCIF_FACES = "/pages/sishosp/gerenciamentoavaliacaoptscif.faces";
	private static final String PTSCIF_ = "ptscif";
	private static final String ENDERECO_PTS = "cadastroptscif?faces-redirect=true";
	private static final String ENDERECO_ID = "&amp;id=";
	private static final String ENDERECO_TIPO = "&amp;tipo=";
	private static final String CABECALHO_INCLUSAO = "Inclusão de PTS CIF";
	private static final String CABECALHO_ALTERACAO = "Alteração de PTS CIF";
	private static final String CABECALHO_RENOVACAO = "Renovação de PTS CIF";

	private List<PtsCifBean> listaPtsCif;
	private PtsCifBean ptsCif;
	private PtsCifBean ptsCifOriginal;

	private List<CompetenciaPtsCifBean> listaCompletaCompetenciasPtsCif;
	private List<CompetenciaPtsCifBean> listaDisplayCompetenciasPtsCif;
	private Integer filtroIdCompetenciaPtsCif = -1;

	private List<GerenciarPacienteBean> listaTerapiasPaciente;

	private PtsCifDAO ptsCifDAO;

	private String formatoBusca = "T";

    private String buscaPaciente;
    private String parametroBuscaPaciente = "paciente";

    private boolean buscarPacientesSemPts = false;
	private boolean ptsValidado;

    private FuncionarioBean user_session;

	private ObjetivoPtsCifBean objetivoPtsCif;
	private ArrayList<AvaliadorPtsCifBean> listaAvaliadoresExcluidos;

	private Integer tipo;
	private String cabecalho;

	private String dialogObjetivoTipo = "Adicionar";

	private int idTemporarioObjetivo = 1;

	public PtsCifController() {
		inicializar();
	}

	public void inicializar(){
		this.listaPtsCif = new ArrayList<>();
		this.ptsCif = new PtsCifBean();
		this.ptsCifOriginal = new PtsCifBean();
		this.ptsCifDAO = new PtsCifDAO();
		this.user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("obj_usuario");
		listaAvaliadoresExcluidos = new ArrayList<>();
		listaTerapiasPaciente = new ArrayList<>();
		carregarCompetencias();
	}

	public void carregarCompetencias() {
		try{
			this.listaCompletaCompetenciasPtsCif = ptsCifDAO.listarCompetencias();
		} catch (Exception ex) {
			JSFUtil.adicionarMensagemErro("Erro ao carregar lista de competências do PTS Cif", "");
		}
	}

	public void buscarPtsCif() throws ProjetoException {
		inicializar();
		if(buscarPacientesSemPts){
			listaPtsCif = ptsCifDAO.buscarPacientesSemPts(buscaPaciente, parametroBuscaPaciente);
		} else {
			if(filtroIdCompetenciaPtsCif > 0){
				listaPtsCif = ptsCifDAO.buscarPtsPorCompetencia(buscaPaciente, parametroBuscaPaciente, filtroIdCompetenciaPtsCif);
			} else {
				listaPtsCif = ptsCifDAO.buscarTodosOsPts(buscaPaciente, parametroBuscaPaciente, formatoBusca);
			}
		}
	}

	public void atualizarDisplayCompetencia(String contexto){
		listaDisplayCompetenciasPtsCif = new ArrayList<>(listaCompletaCompetenciasPtsCif);
		if(contexto.equals("cadastro")){
			switch (tipo) {
				case 1: case 3:
					listaDisplayCompetenciasPtsCif.removeIf(c -> !c.getAtiva());
					break;
				default:
					listaDisplayCompetenciasPtsCif = new ArrayList<>();
					listaDisplayCompetenciasPtsCif.add(ptsCif.getCompetencia());
					break;
			}
		} else if(contexto.equals("gerenciamento")){
			switch (formatoBusca) {
				case "T":
					CompetenciaPtsCifBean todas = new CompetenciaPtsCifBean();
					todas.setId(-1);
					todas.setDescricao("Todas");
					listaDisplayCompetenciasPtsCif.add(0, todas);
					break;
				case "SA":
					listaDisplayCompetenciasPtsCif.removeIf(c -> !c.getAtiva());
					break;
				case "A":
					listaDisplayCompetenciasPtsCif.removeIf(c -> c.getAtiva());
					break;
			}
		}
	}

	public boolean verificaAlteracaoInformacoesPts(){
		if(ptsCif != null && ptsCifOriginal != null){
			if(!ptsCif.getQueixaPrincipal().equals(ptsCifOriginal.getQueixaPrincipal())) return true;
			if(!ptsCif.getCondicoesSaude().equals(ptsCifOriginal.getCondicoesSaude())) return true;
			if(!ptsCif.getFuncaoEstruturaCorpo().equals(ptsCifOriginal.getFuncaoEstruturaCorpo())) return true;
			if(!ptsCif.getAtividadeParticipacao().equals(ptsCifOriginal.getAtividadeParticipacao())) return true;
			if(!ptsCif.getFatoresPessoais().equals(ptsCifOriginal.getFatoresPessoais())) return true;
			if(!ptsCif.getFatoresContextuais().equals(ptsCifOriginal.getFatoresContextuais())) return true;
		}

		return false;
	}

	public boolean verificaAlteracaoFatoresAmbientais(){
		if(ptsCif != null && ptsCifOriginal != null){
			if(!ptsCif.getFacilitador().equals(ptsCifOriginal.getFacilitador())) return true;
			if(!ptsCif.getBarreira().equals(ptsCifOriginal.getBarreira()))  return true;
		}
		return false;
	}

	public void limparBusca() throws ProjetoException {
		buscaPaciente = "";
		parametroBuscaPaciente = "";
		buscarPacientesSemPts = false;
		buscarPtsCif();
	}

    public String redirecionaInsercao() {
        SessionUtil.adicionarNaSessao(ptsCif, PTSCIF_);
        return RedirecionarUtil.redirectInsert(ENDERECO_PTS, ENDERECO_TIPO, 1);
    }
    
	public String redirecionaEdicao() {
		SessionUtil.adicionarNaSessao(ptsCif, PTSCIF_);
		return RedirecionarUtil.redirectEdit(ENDERECO_PTS, ENDERECO_ID, this.ptsCif.getId(), ENDERECO_TIPO, 2);
	}

	public String redirecionaRenovacao() {
		SessionUtil.adicionarNaSessao(ptsCif, PTSCIF_);
		return RedirecionarUtil.redirectEdit(ENDERECO_PTS, ENDERECO_ID, this.ptsCif.getId(), ENDERECO_TIPO, 3);
	}

	public String redirecionaValidacao() {
		SessionUtil.adicionarNaSessao(ptsCif, PTSCIF_);
		return RedirecionarUtil.redirectEdit(ENDERECO_PTS, ENDERECO_ID, this.ptsCif.getId(), ENDERECO_TIPO, 4);
	}



	public void carregarPts() throws ProjetoException {
		GerenciarPacienteDAO gerenciarPacienteDAO = new GerenciarPacienteDAO();
		GerenciarPacienteBean buscaGP = new GerenciarPacienteBean();
		PacienteDAO pDao = new PacienteDAO();

        FacesContext facesContext = FacesContext.getCurrentInstance();
        Map<String, String> params = facesContext.getExternalContext().getRequestParameterMap();
        tipo = TipoCabecalho.INCLUSAO.getSigla();
        if (params.get("id") != null) {
        	ptsCif.setId(Integer.valueOf(params.get("id")));
        	ptsCif = ptsCifDAO.buscarPtsCifPorId(ptsCif.getId());

			ptsCifOriginal = new PtsCifBean(ptsCif);

			ptsCif.setPaciente(pDao.listarPacientePorID(ptsCif.getPaciente().getId_paciente()));

        	cabecalho = CABECALHO_ALTERACAO;
        	tipo = Integer.valueOf(params.get("tipo"));

			//filtrando por id
			buscaGP.setId(ptsCif.getGerenciarPaciente().getId());
			listaTerapiasPaciente = gerenciarPacienteDAO.carregarPacientesInstituicaoBusca(buscaGP, "", "");

			carregarCompetencias();
		} else {
        	cabecalho = CABECALHO_INCLUSAO;
        	ptsCif = (PtsCifBean) SessionUtil.resgatarDaSessao(PTSCIF_);

			ptsCif.setPaciente(pDao.listarPacientePorID(ptsCif.getPaciente().getId_paciente()));

			if(params.containsKey("tipo")){
				tipo = Integer.valueOf(params.get("tipo"));

				if(tipo == 1){
					buscaGP.setStatus("A"); //ativo

					listaTerapiasPaciente = gerenciarPacienteDAO.carregarPacientesInstituicaoBusca(buscaGP, ptsCif.getPaciente().getId_paciente().toString(), "prontpaciente");
				}
			}

        }
    }
    
    public void adicionarAvaliador(FuncionarioBean funcionario) {
    	AvaliadorPtsCifBean avaliadorSelecionado = new AvaliadorPtsCifBean();
    	avaliadorSelecionado.setAvaliador(funcionario);
    	
    	if(!avaliadorJaFoiAdicionado(avaliadorSelecionado)) {
    		ptsCif.getListaAvaliadores().add(avaliadorSelecionado);
			listaAvaliadoresExcluidos.removeIf(avaliador -> avaliador.getAvaliador().getId().equals(funcionario.getId()));
    		JSFUtil.fecharDialog("dlgConsuProf");
    	}
    } 
    
    private boolean avaliadorJaFoiAdicionado(AvaliadorPtsCifBean avaliadorSelecionado) {
    	for (AvaliadorPtsCifBean avaliador : ptsCif.getListaAvaliadores()) {
			if(avaliador.getAvaliador().getId().equals(avaliadorSelecionado.getAvaliador().getId())) {
				JSFUtil.adicionarMensagemAdvertencia("Este avaliador já foi adicionado", "");
				return true;
			}
		}
    	return false;
    }
    
    public void removerAvaliador(AvaliadorPtsCifBean avaliadorSelecionado) {
    	ptsCif.getListaAvaliadores().remove(avaliadorSelecionado);
    	listaAvaliadoresExcluidos.add(avaliadorSelecionado);
    }
    
    public void limparObjetivoSelecionado() {
    	this.objetivoPtsCif = new ObjetivoPtsCifBean();
		dialogObjetivoTipo = "Adicionar";
    	JSFUtil.abrirDialog("dlgObjetivo");
    }
    
    public void adicionarObjetivo(ObjetivoPtsCifBean objetivoSelecionado) throws ProjetoException{
		if(objetivoSelecionado.getId() == null){
			objetivoSelecionado.setId(++idTemporarioObjetivo);
		}
		ptsCif.getListaObjetivos().removeIf(obj -> obj.getId().equals(objetivoSelecionado.getId()));

		//novo objeto para registrar como mudança e habilitar botão de alteração
		ObjetivoPtsCifBean novo = new ObjetivoPtsCifBean();
		novo.setId(objetivoSelecionado.getId());
		novo.setObjetivo(objetivoSelecionado.getObjetivo());
		novo.setFormaAvaliacao(objetivoSelecionado.getFormaAvaliacao());
		novo.setIntervencao(objetivoSelecionado.getIntervencao());

		ptsCif.getListaObjetivos().add(novo);
    	JSFUtil.fecharDialog("dlgObjetivo");
    }

	public void removerObjetivo(ObjetivoPtsCifBean objetivoSelecionado) throws ProjetoException {
		ptsCif.getListaObjetivos().removeIf(o -> o.getId() == objetivoSelecionado.getId());
	}
    
    public void editarObjetivo(ObjetivoPtsCifBean objetivoSelecionado) {
		objetivoPtsCif = objetivoSelecionado;
		dialogObjetivoTipo = "Alterar";
		JSFUtil.abrirDialog("dlgObjetivo");
    }
    
    public void cadastrarPtsCif() throws ProjetoException {
    	if(existeAvaliadores() && existeObjetivos()) {
			boolean cadastrou = ptsCifDAO.cadastrarPtsCif(ptsCif, user_session);
			for(ObjetivoPtsCifBean objetivo : ptsCif.getListaObjetivos()){
				Integer id = ptsCifDAO.cadastrarObjetivoPtsCif(objetivo, ptsCif);
				objetivo.setId(id);
			}
			for(AvaliadorPtsCifBean avaliador : ptsCif.getListaAvaliadores()){
				ptsCifDAO.cadastrarAvaliadorPtsCif(avaliador, ptsCif);
			}
			if (cadastrou) {
				ptsCif = new PtsCifBean();
				JSFUtil.adicionarMensagemSucesso("PTS cadastrado com sucesso", "");
				JSFUtil.abrirDialog("dlgPtsGravado");
			} else {
				JSFUtil.adicionarMensagemErro("Erro ao cadastrar PTS", "");
			}
    	}
    }
    
    public void alterarPtsCif() throws ProjetoException {
    	if(existeAvaliadores() && existeObjetivos()) {
			boolean alterou = ptsCifDAO.alterarPtsCif(ptsCif, user_session);
			if (alterou) {
				JSFUtil.adicionarMensagemSucesso("PTS alterado com sucesso", "");
			} else {
				JSFUtil.adicionarMensagemErro("Erro ao alterar PTS", "");
			}
    	}
    }

	public void alterarInformacoesPtsCif() throws ProjetoException {
		boolean alterou = ptsCifDAO.alterarInformacoesPtsCif(ptsCif);
		if (alterou) {
			JSFUtil.adicionarMensagemSucesso("Informações alteradas com sucesso", "");
			ptsCifOriginal.getCompetencia().setId(ptsCif.getCompetencia().getId());
			ptsCifOriginal.getCompetencia().setDescricao(ptsCif.getCompetencia().getDescricao());
			ptsCifOriginal.getCompetencia().setDataInicial(ptsCif.getCompetencia().getDataInicial());
			ptsCifOriginal.getCompetencia().setDataFinal(ptsCif.getCompetencia().getDataFinal());
			ptsCifOriginal.getCompetencia().setAtiva(ptsCif.getCompetencia().getAtiva());

			ptsCifOriginal.setQueixaPrincipal(ptsCif.getQueixaPrincipal());
			ptsCifOriginal.setCondicoesSaude(ptsCif.getCondicoesSaude());
			ptsCifOriginal.setFuncaoEstruturaCorpo(ptsCif.getFuncaoEstruturaCorpo());
			ptsCifOriginal.setAtividadeParticipacao(ptsCif.getAtividadeParticipacao());
			ptsCifOriginal.setFatoresPessoais(ptsCif.getFatoresPessoais());
			ptsCifOriginal.setFatoresContextuais(ptsCif.getFatoresContextuais());
		} else {
			JSFUtil.adicionarMensagemErro("Erro ao alterar PTS", "");
		}
	}

	public void alterarFatoresAmbientaisPtsCif() throws ProjetoException {
		boolean alterou = ptsCifDAO.alterarFatoresAmbientaisPtsCif(ptsCif);
		if (alterou) {
			JSFUtil.adicionarMensagemSucesso("Fatores Ambientais alterados com sucesso", "");

			ptsCifOriginal.setFacilitador(ptsCif.getFacilitador());
			ptsCifOriginal.setBarreira(ptsCif.getBarreira());
		} else {
			JSFUtil.adicionarMensagemErro("Erro ao alterar PTS", "");
		}
	}

	public void alterarAvaliadoresPtsCif() throws ProjetoException {
		boolean alterou = true;

		for ( AvaliadorPtsCifBean avaliador : ptsCif.getListaAvaliadores()) {
			alterou = ptsCifDAO.adicionarAvaliadorPtsCif(ptsCif.getId(), avaliador);
		}

		for ( AvaliadorPtsCifBean avaliador : listaAvaliadoresExcluidos){
			if(!ptsCif.getListaAvaliadores().contains(avaliador)){
				ptsCifDAO.removerAvaliadorPtsCif(ptsCif.getId(), avaliador, user_session);
			}
		}

		if (alterou) {
			JSFUtil.adicionarMensagemSucesso("Avaliadores alterados com sucesso", "");
		} else {
			JSFUtil.adicionarMensagemErro("Erro ao alterar Avaliadores do PTS Cif", "");
		}
	}

	public void alterarObjetivosPtsCif() throws ProjetoException {
		boolean alterou = true;

		for ( ObjetivoPtsCifBean objetivo : ptsCif.getListaObjetivos()) {
			alterou = ptsCifDAO.alterarObjetivoPtsCif(objetivo);
			if(!alterou) break;
		}

		if (alterou) {
			JSFUtil.adicionarMensagemSucesso("Avaliadores alterados com sucesso", "");
		} else {
			JSFUtil.adicionarMensagemErro("Erro ao alterar Avaliadores do PTS Cif", "");
		}
	}
    
    private boolean existeAvaliadores() {
    	if(ptsCif.getListaAvaliadores().isEmpty()) {
    		JSFUtil.adicionarMensagemAdvertencia("Adicione pelo menos 1 Avaliador", "");
    		return false;
    	}
    	return true;
    }
    
    private boolean existeObjetivos() {
    	if(ptsCif.getListaObjetivos().isEmpty()) {
    		JSFUtil.adicionarMensagemAdvertencia("Adicione pelo menos 1 Objetivo", "");
    		return false;
    	}
    	return true;
    }
    
	public void buscarPtsCifAvaliador() throws ProjetoException {
		listaPtsCif = ptsCifDAO.buscarPtsAvaliador(user_session.getId());
	}
	
    public void gravarValidacaoAvaliador() throws ProjetoException, IOException {
    	
    	if(usuarioConfirmouAvaliacao()) {    	
			boolean alterou = ptsCifDAO.gravarValidacaoAvaliador(user_session.getId(), ptsCif.getId());
			if (alterou) {
				String path = RedirecionarUtil.getServleContext().getContextPath();
				FacesContext.getCurrentInstance().getExternalContext()
						.redirect(path + GERENCIAMENTOAVALIACAOPTSCIF_FACES);
				JSFUtil.adicionarMensagemSucesso("PTS validado com sucesso", "");
			} else {
				JSFUtil.adicionarMensagemErro("Erro ao validado PTS", "");
			}
    	}
    }
    
    private boolean usuarioConfirmouAvaliacao() {
    	if(!ptsValidado) JSFUtil.adicionarMensagemAdvertencia("Confirme os dados da avaliação para gravar", "");
    	return ptsValidado;
    }

	public List<PtsCifBean> getListaPtsCif() {
		return listaPtsCif;
	}

	public void setListaPtsCif(List<PtsCifBean> listaPtsCif) {
		this.listaPtsCif = listaPtsCif;
	}

	public PtsCifBean getPtsCif() {
		return ptsCif;
	}

	public void setPtsCif(PtsCifBean ptsCif) {
		this.ptsCif = ptsCif;
	}

	public String getBuscaPaciente() {
		return buscaPaciente;
	}

	public void setBuscaPaciente(String buscaPaciente) {
		this.buscaPaciente = buscaPaciente;
	}

	public String getParametroBuscaPaciente() {
		return parametroBuscaPaciente;
	}

	public void setParametroBuscaPaciente(String parametroBuscaPaciente) {
		this.parametroBuscaPaciente = parametroBuscaPaciente;
	}

	public boolean isBuscarPacientesSemPts() {
		return buscarPacientesSemPts;
	}

	public void setBuscarPacientesSemPts(boolean buscarPacientesSemPts) {
		this.buscarPacientesSemPts = buscarPacientesSemPts;
	}

	public List<CompetenciaPtsCifBean> getListaCompletaCompetenciasPtsCif() {
		return listaCompletaCompetenciasPtsCif;
	}

	public void setListaCompletaCompetenciasPtsCif(List<CompetenciaPtsCifBean> listaCompletaCompetenciasPtsCif) {
		this.listaCompletaCompetenciasPtsCif = listaCompletaCompetenciasPtsCif;
	}

	public Integer getFiltroIdCompetenciaPtsCif() {
		return filtroIdCompetenciaPtsCif;
	}

	public void setFiltroIdCompetenciaPtsCif(Integer filtroIdCompetenciaPtsCif) {
		this.filtroIdCompetenciaPtsCif = filtroIdCompetenciaPtsCif;
	}

	public String getFormatoBusca() {
		return formatoBusca;
	}

	public void setFormatoBusca(String formatoBusca) {
		this.formatoBusca = formatoBusca;
	}

	public ObjetivoPtsCifBean getObjetivoPtsCif() {
		return objetivoPtsCif;
	}

	public void setObjetivoPtsCif(ObjetivoPtsCifBean objetivoPtsCif) {
		this.objetivoPtsCif = objetivoPtsCif;
	}

	public ArrayList<AvaliadorPtsCifBean> getListaAvaliadoresExcluidos() {
		return listaAvaliadoresExcluidos;
	}

	public void setListaAvaliadoresExcluidos(ArrayList<AvaliadorPtsCifBean> listaAvaliadoresExcluidos) {
		this.listaAvaliadoresExcluidos = listaAvaliadoresExcluidos;
	}

	public List<CompetenciaPtsCifBean> getListaDisplayCompetenciasPtsCif() {
		return listaDisplayCompetenciasPtsCif;
	}

	public void setListaDisplayCompetenciasPtsCif(List<CompetenciaPtsCifBean> listaDisplayCompetenciasPtsCif) {
		this.listaDisplayCompetenciasPtsCif = listaDisplayCompetenciasPtsCif;
	}

	public Integer getTipo() {
		return tipo;
	}

	public void setTipo(Integer tipo) {
		this.tipo = tipo;
	}

	public String getCabecalho() {
		return cabecalho;
	}

	public void setCabecalho(String cabecalho) {
		this.cabecalho = cabecalho;
	}

	public PtsCifBean getPtsCifOriginal() {
		return ptsCifOriginal;
	}

	public void setPtsCifOriginal(PtsCifBean ptsCifOriginal) {
		this.ptsCifOriginal = ptsCifOriginal;
	}

	public PtsCifDAO getPtsCifDAO() {
		return ptsCifDAO;
	}

	public void setPtsCifDAO(PtsCifDAO ptsCifDAO) {
		this.ptsCifDAO = ptsCifDAO;
	}

	public boolean isPtsValidado() {
		return ptsValidado;
	}

	public void setPtsValidado(boolean ptsValidado) {
		this.ptsValidado = ptsValidado;
	}

	public FuncionarioBean getUser_session() {
		return user_session;
	}

	public void setUser_session(FuncionarioBean user_session) {
		this.user_session = user_session;
	}

	public String getDialogObjetivoTipo() {
		return dialogObjetivoTipo;
	}

	public void setDialogObjetivoTipo(String dialogObjetivoTipo) {
		this.dialogObjetivoTipo = dialogObjetivoTipo;
	}

	public List<GerenciarPacienteBean> getListaTerapiasPaciente() {
		return listaTerapiasPaciente;
	}

	public void setListaTerapiasPaciente(List<GerenciarPacienteBean> listaTerapiasPaciente) {
		this.listaTerapiasPaciente = listaTerapiasPaciente;
	}

	public int getIdTemporarioObjetivo() {
		return idTemporarioObjetivo;
	}

	public void setIdTemporarioObjetivo(int idTemporarioObjetivo) {
		this.idTemporarioObjetivo = idTemporarioObjetivo;
	}
}
