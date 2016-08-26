package br.gov.al.maceio.sishosp.hosp.control;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;

import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.hosp.dao.EncaminhadoDAO;
import br.gov.al.maceio.sishosp.hosp.dao.EncaminhamentoDAO;
import br.gov.al.maceio.sishosp.hosp.dao.EnderecoDAO;
import br.gov.al.maceio.sishosp.hosp.dao.EscolaDAO;
import br.gov.al.maceio.sishosp.hosp.dao.EscolaridadeDAO;
import br.gov.al.maceio.sishosp.hosp.dao.FormaTransporteDAO;
import br.gov.al.maceio.sishosp.hosp.dao.PacienteDAO;
import br.gov.al.maceio.sishosp.hosp.dao.ProfissaoDAO;
import br.gov.al.maceio.sishosp.hosp.dao.RacaDAO;
import br.gov.al.maceio.sishosp.hosp.model.ConvenioBean;
import br.gov.al.maceio.sishosp.hosp.model.EncaminhadoBean;
import br.gov.al.maceio.sishosp.hosp.model.EncaminhamentoBean;
import br.gov.al.maceio.sishosp.hosp.model.EnderecoBean;
import br.gov.al.maceio.sishosp.hosp.model.EscolaBean;
import br.gov.al.maceio.sishosp.hosp.model.EscolaridadeBean;
import br.gov.al.maceio.sishosp.hosp.model.EspecialidadeBean;
import br.gov.al.maceio.sishosp.hosp.model.FormaTransporteBean;
import br.gov.al.maceio.sishosp.hosp.model.PacienteBean;
import br.gov.al.maceio.sishosp.hosp.model.ProfissaoBean;
import br.gov.al.maceio.sishosp.hosp.model.RacaBean;

@ManagedBean(name="PacienteController")
@ViewScoped
public class PacienteController implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer abaAtiva = 0;
	private Integer SelecionadoRaca;
	private String tipoBuscar;
	private String descricaoParaBuscar;
	private String cabecalho;
	private String cabecalho2;

	// CLASSES HERDADAS
	private PacienteBean paciente;
	private PacienteBean pacienteBuscado;
	private EnderecoBean endereco;
	private EscolaBean escola;
	private EscolaridadeBean escolaridade;
	private EspecialidadeBean especialidade;
	private ConvenioBean convenio;
	private EncaminhamentoBean encaminhamento;
	private ProfissaoBean profissao;
	private EncaminhadoBean encaminhado;
	private FormaTransporteBean transporte;
	private RacaBean raca;

	// BUSCAS
	private int tipo;
	private Integer tipoBuscaPaciente;
	private String campoBuscaPaciente;
	private String statusPaciente;

	private Integer tipoBuscaRaca;
	private String campoBuscaRaca;
	private String statusRaca;

	// AUTO COMPLETE
	private PacienteBean pacienteSelecionado;
	private EscolaBean escolageral;
	private EscolaBean escolaselecionado, escolaselecionadoaux,
			escolabuscarapida, escolaselecionadocomposicao,
			escolaSelecionadoExclusao;
	private EscolaBean escolaSuggestion;

	private EscolaridadeBean escolaridadegeral;
	private EscolaridadeBean escolaridadeselecionado,
			escolaridadeselecionadoaux, escolaridadebuscarapida,
			escolaridadeselecionadocomposicao, escolaridadeSelecionadoExclusao;
	private EscolaridadeBean escolaridadeSuggestion;

	private EncaminhamentoBean encaminhamentogeral;
	private EncaminhamentoBean encaminhamentoselecionado,
			encaminhamentoselecionadoaux, encaminhamentobuscarapida,
			encaminhamentoselecionadocomposicao,
			encaminhamentoSelecionadoExclusao;
	private EncaminhamentoBean encaminhamentoSuggestion;

	private EncaminhadoBean encaminhadogeral;
	private EncaminhadoBean encaminhadoselecionado, encaminhadoselecionadoaux,
			encaminhadobuscarapida, encaminhadoselecionadocomposicao,
			encaminhadoSelecionadoExclusao;
	private EncaminhadoBean encaminhadoSuggestion;

	private FormaTransporteBean transportegeral;
	private FormaTransporteBean transporteselecionado,
			transporteselecionadoaux, transportebuscarapida,
			transporteselecionadocomposicao, transporteSelecionadoExclusao;
	private FormaTransporteBean transporteSuggestion;

	private ProfissaoBean profissaogeral;
	private ProfissaoBean profissaoselecionado, profissaoselecionadoaux,
			profissaobuscarapida, profissaoselecionadocomposicao,
			profissaoSelecionadoExclusao;
	private ProfissaoBean profissaoSuggestion;

	// LISTAS
	private List<PacienteBean> listaPacientes;
	private List<PacienteBean> listaPacientesParaAgenda;
	private List<RacaBean> listaRaca;
	private List<EscolaBean> listaEscolas;
	private List<EscolaridadeBean> listaEscolaridade;
	private List<ProfissaoBean> listaProfissao;
	private List<EncaminhadoBean> listaEncaminhado;
	private List<FormaTransporteBean> listaTransporte;
	private List<EnderecoBean> listaMunicipios;

	public PacienteController() {
		paciente = new PacienteBean();
		pacienteBuscado = new PacienteBean();
		endereco = new EnderecoBean();
		escola = new EscolaBean();
		escolaridade = new EscolaridadeBean();
		especialidade = new EspecialidadeBean();
		encaminhamento = new EncaminhamentoBean();
		encaminhado = new EncaminhadoBean();
		profissao = new ProfissaoBean();
		transporte = new FormaTransporteBean();
		raca = new RacaBean();

		this.pacienteSelecionado = new PacienteBean();
		escolageral = new EscolaBean();
		escolaselecionado = new EscolaBean();
		escolaselecionadoaux = new EscolaBean();
		escolabuscarapida = new EscolaBean();
		escolaselecionadocomposicao = new EscolaBean();
		escolaSelecionadoExclusao = new EscolaBean();
		escolaSuggestion = new EscolaBean();

		transporteSuggestion = null;

		// LISTAS
		listaPacientes = new ArrayList<>();
		listaPacientes = null;
		listaPacientesParaAgenda = new ArrayList<>();
		listaPacientesParaAgenda = null;
		listaRaca = new ArrayList<>();
		listaRaca = null;
		listaEscolas = new ArrayList<>();
		listaEscolas = null;
		listaEscolaridade = new ArrayList<>();
		listaEscolaridade = null;
		listaProfissao = new ArrayList<>();
		listaProfissao = null;
		listaEncaminhado = new ArrayList<>();
		listaEncaminhado = null;
		listaTransporte = new ArrayList<>();
		listaTransporte = null;
		listaMunicipios = new ArrayList<>();
		listaMunicipios = null;

		// BUSCA
		tipoBuscaPaciente = 1;
		campoBuscaPaciente = "";
		statusPaciente = "P";

		tipoBuscaRaca = 1;
		campoBuscaRaca = "";
		statusRaca = "P";
	}

	public List<PacienteBean> getListaPacientesParaAgenda() {

		return listaPacientesParaAgenda;
	}

	public void setListaPacientesParaAgenda(
			List<PacienteBean> listaPacientesParaAgenda) {
		this.listaPacientesParaAgenda = listaPacientesParaAgenda;
	}
	
	
	public String redirectEdit() {
		return "cadastroPaciente?faces-redirect=true&amp;id=" + this.paciente.getId_paciente()+"&amp;tipo="+tipo;
	}	
	
	
	public String redirectInsert() {
		return "cadastroPaciente?faces-redirect=true&amp;tipo="+tipo;
	}		
	
	public void getEditPaciente() throws ProjetoException, SQLException {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		Map<String,String> params = facesContext.getExternalContext().getRequestParameterMap();
		System.out.println("vai ve se entrar no editar");
		if(params.get("id") != null) {
			System.out.println("entrou no editar");
			Integer id = Integer.parseInt(params.get("id"));
			tipo =Integer.parseInt(params.get("tipo"));			
			System.out.println("tipo do walter"+tipo);
			PacienteDAO cDao = new PacienteDAO();
			this.paciente = cDao.listarPacientePorID(id);
		}
		else{
			System.out.println("tipo sera"+tipo);
			tipo =Integer.parseInt(params.get("tipo"));
			
		}
		
	}



	public void gravarPaciente() throws ProjetoException {
		PacienteDAO udao = new PacienteDAO();
		if (escolaSuggestion!=null)
		paciente.getEscola().setCodEscola(escolaSuggestion.getCodEscola());
		else
		paciente.getEscola().setCodEscola(null);
		if (escolaridadeSuggestion!=null)
		paciente.getEscolaridade().setCodescolaridade(
				escolaridadeSuggestion.getCodescolaridade());
		else
			paciente.getEscolaridade().setCodescolaridade(null);
		if (profissaoSuggestion!=null) 
		paciente.getProfissao().setCodprofissao(
				profissaoSuggestion.getCodprofissao());
		else
			paciente.getProfissao().setCodprofissao(null);			
		
		if (encaminhadoSuggestion!=null)
		paciente.getEncaminhado().setCodencaminhado(
				encaminhadoSuggestion.getCodencaminhado());
		else
			paciente.getEncaminhado().setCodencaminhado(null);			
		if (transporteSuggestion!=null)
		paciente.getFormatransporte().setCodformatransporte(
				transporteSuggestion.getCodformatransporte());
		else
			paciente.getFormatransporte().setCodformatransporte(null);			
		boolean cadastrou = udao.cadastrar(paciente);

		if (cadastrou == true) {
			limparDados();
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Paciente cadastrado com sucesso!", "Sucesso");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			listaPacientes = null;

		} else {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Ocorreu um erro durante o cadastro!", "Erro");
			FacesContext.getCurrentInstance().addMessage(null, msg);

		}

	}

	public void buscarPaciente() throws ProjetoException, SQLException {
		PacienteDAO udao = new PacienteDAO();
		if (tipoBuscar.equals("VAZIO") || descricaoParaBuscar.isEmpty()) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Escolha uma op��o v�lida e insira uma descri��o!",
					"Insira os dados");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		} else {
			this.listaPacientesParaAgenda = new ArrayList<PacienteBean>();
			this.listaPacientesParaAgenda = udao.buscarPacienteAgenda(
					tipoBuscar, descricaoParaBuscar);

			if (this.listaPacientesParaAgenda.isEmpty()) {
				FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
						"N�o existe paciente com essa descri��o!",
						"Paciente n�o encontrado");
				FacesContext.getCurrentInstance().addMessage(null, msg);
			}
		}

	}

	public String alterarPaciente() throws ProjetoException {
		paciente.getEscola().setCodEscola(escolaSuggestion.getCodEscola());
		paciente.getEscolaridade().setCodescolaridade(
				escolaridadeSuggestion.getCodescolaridade());
		paciente.getProfissao().setCodprofissao(
				profissaoSuggestion.getCodprofissao());
		paciente.getEncaminhado().setCodencaminhado(
				encaminhadoSuggestion.getCodencaminhado());
		paciente.getFormatransporte().setCodformatransporte(
				transporteSuggestion.getCodformatransporte());

		PacienteDAO mdao = new PacienteDAO();
		boolean alterou = mdao.alterar(paciente);

		if (alterou == true) {
			limparDados();
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Paciente alterado com sucesso!", "Sucesso");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return "/pages/sishosp/gerenciarPaciente.faces?faces-redirect=true";
			// RequestContext.getCurrentInstance().execute("dlgAltMenu.hide();");
		} else {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Ocorreu um erro durante o cadastro!", "Erro");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return "";
			// RequestContext.getCurrentInstance().execute("dlgAltMenu.hide();");
		}

	}

	public void excluirPaciente() throws ProjetoException {
		PacienteDAO udao = new PacienteDAO();

		boolean excluio = udao.excluir(paciente);

		if (excluio == true) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Paciente excluido com sucesso!", "Sucesso");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			listaPacientes = null;
			RequestContext.getCurrentInstance().execute(
					"PF('dialogAtencao').hide();");
		} else {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Ocorreu um erro durante a exclusao!", "Erro");
			FacesContext.getCurrentInstance().addMessage(null, msg);

			RequestContext.getCurrentInstance().execute(
					"PF('dialogAtencao').hide();");
		}

	}

	public void gravarRaca() throws ProjetoException {
		RacaDAO udao = new RacaDAO();
		boolean cadastrou = udao.cadastrar(raca);

		if (cadastrou == true) {
			limparDados();
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Cor/Ra�a cadastrado com sucesso!", "Sucesso");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			listaRaca = null;

		} else {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Ocorreu um erro durante o cadastro!", "Erro");
			FacesContext.getCurrentInstance().addMessage(null, msg);

		}

	}

	public String alterarRaca() throws ProjetoException {

		RacaDAO rdao = new RacaDAO();
		boolean alterou = rdao.alterar(raca);

		if (alterou == true) {
			limparDados();
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Cor/Ra�a alterado com sucesso!", "Sucesso");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return "/pages/sishosp/gerenciarRaca.faces?faces-redirect=true";
			// RequestContext.getCurrentInstance().execute("dlgAltMenu.hide();");
		} else {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Ocorreu um erro durante o cadastro!", "Erro");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return "";
			// RequestContext.getCurrentInstance().execute("dlgAltMenu.hide();");
		}

	}

	public void excluirRaca() throws ProjetoException {
		RacaDAO udao = new RacaDAO();

		boolean excluio = udao.excluir(raca);

		if (excluio == true) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Cor/Ra�a excluido com sucesso!", "Sucesso");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			listaRaca = null;
			RequestContext.getCurrentInstance().execute(
					"PF('dialogAtencao').hide();");
		} else {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Ocorreu um erro durante a exclusao!", "Erro");
			FacesContext.getCurrentInstance().addMessage(null, msg);

			RequestContext.getCurrentInstance().execute(
					"PF('dialogAtencao').hide();");
		}

	}

	public void onRowSelect(SelectEvent event) {
		Integer codpaciente;
		codpaciente = ((PacienteBean) event.getObject()).getId_paciente();

	}

	public void buscaProfissaoCod(Integer codprofissao) throws Exception {

		ProfissaoBean profissao = new ProfissaoBean();

		Integer in = (Integer) Integer.valueOf(codprofissao);

		ProfissaoDAO icdao = new ProfissaoDAO();

		profissao = icdao.buscaprofissaocodigo(Integer.valueOf(codprofissao));
		if (profissao.getCodprofissao() != null) {
			profissaoselecionado = profissao;
			profissaobuscarapida = new ProfissaoBean();

			profissaogeral = profissaoselecionado;
			icdao = new ProfissaoDAO();

		} else {
			profissaobuscarapida = new ProfissaoBean();
			FacesMessage message = new FacesMessage(
					" C�digo da Profissao incorreto!");
			FacesContext.getCurrentInstance().addMessage(null, message);
		}

	}

	public List<ProfissaoBean> completeText6(String query)
			throws ProjetoException {
		List<ProfissaoBean> result = new ArrayList<ProfissaoBean>();
		ProfissaoDAO icdao = new ProfissaoDAO();
		result = icdao.buscaprofissao(query);
		return result;
	}

	public void onItemSelect6(SelectEvent event) throws Exception {

		ProfissaoBean prodsel = new ProfissaoBean();
		prodsel = (ProfissaoBean) event.getObject();

		ProfissaoDAO dao = new ProfissaoDAO();
		buscaProfissaoCod(prodsel.getCodprofissao());
		profissaoSuggestion = new ProfissaoBean();
		profissaoSuggestion = null;
		// FacesContext.getCurrentInstance().addMessage(null, new
		// FacesMessage("Item Selected", prodsel.getDescricao()));
	}

	public void buscaTransporteCod(Integer codformatransporte) throws Exception {

		FormaTransporteBean transporte = new FormaTransporteBean();

		Integer in = (Integer) Integer.valueOf(codformatransporte);

		FormaTransporteDAO icdao = new FormaTransporteDAO();

		transporte = icdao.buscatransportecodigo(Integer
				.valueOf(codformatransporte));
		if (transporte.getCodformatransporte() != null) {
			transporteselecionado = transporte;
			transportebuscarapida = new FormaTransporteBean();

			transportegeral = transporteselecionado;
			icdao = new FormaTransporteDAO();

		} else {
			transportebuscarapida = new FormaTransporteBean();
			FacesMessage message = new FacesMessage(
					" C�digo do Encaminhado incorreto!");
			FacesContext.getCurrentInstance().addMessage(null, message);
		}

	}

	public List<FormaTransporteBean> completeText5(String query)
			throws ProjetoException {
		List<FormaTransporteBean> result = new ArrayList<FormaTransporteBean>();
		FormaTransporteDAO icdao = new FormaTransporteDAO();
		result = icdao.buscatransporte(query);
		return result;
	}

	public void onItemSelect5(SelectEvent event) throws Exception {
		System.out.println("Marce:"
				+ transporteSuggestion.getCodformatransporte());
		FormaTransporteBean prodsel = new FormaTransporteBean();
		prodsel = (FormaTransporteBean) event.getObject();

		FormaTransporteDAO dao = new FormaTransporteDAO();
		buscaTransporteCod(prodsel.getCodformatransporte());
		transporteSuggestion = new FormaTransporteBean();
		transporteSuggestion = null;
		// FacesContext.getCurrentInstance().addMessage(null, new
		// FacesMessage("Item Selected", prodsel.getDescricao()));
	}

	public void buscaEncaminhadoCod(Integer codencaminhado) throws Exception {

		EncaminhadoBean encaminhado = new EncaminhadoBean();

		Integer in = (Integer) Integer.valueOf(codencaminhado);

		EncaminhadoDAO icdao = new EncaminhadoDAO();

		encaminhado = icdao.buscaencaminhadocodigo(Integer
				.valueOf(codencaminhado));
		if (encaminhado.getCodencaminhado() != null) {
			encaminhadoselecionado = encaminhado;
			encaminhadobuscarapida = new EncaminhadoBean();

			encaminhadogeral = encaminhadoselecionado;
			icdao = new EncaminhadoDAO();

		} else {
			encaminhadobuscarapida = new EncaminhadoBean();
			FacesMessage message = new FacesMessage(
					" C�digo do Encaminhado incorreto!");
			FacesContext.getCurrentInstance().addMessage(null, message);
		}

	}

	public List<EncaminhadoBean> completeText4(String query)
			throws ProjetoException {
		List<EncaminhadoBean> result = new ArrayList<EncaminhadoBean>();
		EncaminhadoDAO icdao = new EncaminhadoDAO();
		result = icdao.buscaencaminhado(query);
		return result;
	}

	public void onItemSelect4(SelectEvent event) throws Exception {
		System.out.println("NATH:" + encaminhadoSuggestion.getCodencaminhado());
		EncaminhadoBean prodsel = new EncaminhadoBean();
		prodsel = (EncaminhadoBean) event.getObject();

		EncaminhadoDAO dao = new EncaminhadoDAO();
		buscaEncaminhadoCod(prodsel.getCodencaminhado());
		encaminhadoSuggestion = new EncaminhadoBean();
		encaminhadoSuggestion = null;
		// FacesContext.getCurrentInstance().addMessage(null, new
		// FacesMessage("Item Selected", prodsel.getDescricao()));
	}

	public void buscaEncaminhamentoCod(Integer codencaminhamento)
			throws Exception {

		EncaminhamentoBean encaminhamento = new EncaminhamentoBean();

		Integer in = (Integer) Integer.valueOf(codencaminhamento);

		EncaminhamentoDAO icdao = new EncaminhamentoDAO();

		encaminhamento = icdao.buscaencaminhamentocodigo(Integer
				.valueOf(codencaminhamento));
		if (encaminhamento.getCod() != null) {
			encaminhamentoselecionado = encaminhamento;
			encaminhamentobuscarapida = new EncaminhamentoBean();

			encaminhamentogeral = encaminhamentoselecionado;
			icdao = new EncaminhamentoDAO();

		} else {
			encaminhamentobuscarapida = new EncaminhamentoBean();
			FacesMessage message = new FacesMessage(
					" C�digo do Encaminhado incorreto!");
			FacesContext.getCurrentInstance().addMessage(null, message);
		}

	}

	public List<EncaminhamentoBean> completeText3(String query)
			throws ProjetoException {
		List<EncaminhamentoBean> result = new ArrayList<EncaminhamentoBean>();
		EncaminhamentoDAO icdao = new EncaminhamentoDAO();
		result = icdao.buscaencaminhamento(query);
		return result;
	}

	public void onItemSelect3(SelectEvent event) throws Exception {

		EncaminhamentoBean prodsel = new EncaminhamentoBean();
		prodsel = (EncaminhamentoBean) event.getObject();

		EncaminhamentoDAO dao = new EncaminhamentoDAO();
		buscaEncaminhamentoCod(prodsel.getCod());
		encaminhamentoSuggestion = new EncaminhamentoBean();
		encaminhamentoSuggestion = null;
		// FacesContext.getCurrentInstance().addMessage(null, new
		// FacesMessage("Item Selected", prodsel.getDescricao()));
	}

	public void buscaEscolaridadeCod(Integer codescolaridade) throws Exception {

		EscolaridadeBean escolaridade = new EscolaridadeBean();

		Integer in = (Integer) Integer.valueOf(codescolaridade);

		EscolaridadeDAO icdao = new EscolaridadeDAO();

		escolaridade = icdao.buscaescolaridadecodigo(Integer
				.valueOf(codescolaridade));
		if (escolaridade.getCodescolaridade() != null) {
			escolaridadeselecionado = escolaridade;
			escolaridadebuscarapida = new EscolaridadeBean();

			escolaridadegeral = escolaridadeselecionado;
			icdao = new EscolaridadeDAO();

		} else {
			escolaridadebuscarapida = new EscolaridadeBean();
			FacesMessage message = new FacesMessage(
					" C�digo da Escolaridade incorreto!");
			FacesContext.getCurrentInstance().addMessage(null, message);
		}

	}

	public List<EscolaridadeBean> completeText2(String query)
			throws ProjetoException {
		List<EscolaridadeBean> result = new ArrayList<EscolaridadeBean>();
		EscolaridadeDAO icdao = new EscolaridadeDAO();
		result = icdao.buscaescolaridade(query);
		return result;
	}
	
	public List<PacienteBean> listaPacienteAutoComplete(String query)
			throws ProjetoException {
		PacienteDAO pDao = new PacienteDAO();
		List<PacienteBean> result = pDao.buscaPacienteAutoComplete(query);
		return result;
	}

	public void onItemSelect2(SelectEvent event) throws Exception {

		EscolaridadeBean prodsel = new EscolaridadeBean();
		prodsel = (EscolaridadeBean) event.getObject();

		EscolaridadeDAO dao = new EscolaridadeDAO();
		buscaEscolaridadeCod(prodsel.getCodescolaridade());
		escolaridadeSuggestion = new EscolaridadeBean();
		escolaridadeSuggestion = null;
		// FacesContext.getCurrentInstance().addMessage(null, new
		// FacesMessage("Item Selected", prodsel.getDescricao()));
	}
	
	public void onItemSelectPaciente(SelectEvent event) throws Exception {

		PacienteBean prodsel = new PacienteBean();
		prodsel = (PacienteBean) event.getObject();

		EscolaridadeDAO dao = new EscolaridadeDAO();
		//buscaEscolaridadeCod(prodsel.getCodescolaridade());
		escolaridadeSuggestion = new EscolaridadeBean();
		escolaridadeSuggestion = null;
		// FacesContext.getCurrentInstance().addMessage(null, new
		// FacesMessage("Item Selected", prodsel.getDescricao()));
	}

	public void buscaEscolaCod(Integer codescola) throws Exception {

		EscolaBean escola = new EscolaBean();

		Integer in = (Integer) Integer.valueOf(codescola);

		EscolaDAO icdao = new EscolaDAO();

		escola = icdao.buscaescolacodigo(Integer.valueOf(codescola));
		if (escola.getCodEscola() != null) {
			escolaselecionado = escola;
			escolabuscarapida = new EscolaBean();

			escolageral = escolaselecionado;
			icdao = new EscolaDAO();

		} else {
			escolabuscarapida = new EscolaBean();
			FacesMessage message = new FacesMessage(
					" C�digo da Escola incorreto!");
			FacesContext.getCurrentInstance().addMessage(null, message);
		}

	}

	public List<EscolaBean> completeText(String query) throws ProjetoException {
		List<EscolaBean> result = new ArrayList<EscolaBean>();
		EscolaDAO icdao = new EscolaDAO();
		result = icdao.buscaescola(query);
		return result;
	}

	public void onItemSelect(SelectEvent event) throws Exception {
		System.out.println("THULIO" + escolaSuggestion.getCodEscola());

		EscolaBean prodsel = new EscolaBean();
		prodsel = (EscolaBean) event.getObject();

		EscolaDAO dao = new EscolaDAO();
		buscaEscolaCod(prodsel.getCodEscola());

		escolaSuggestion = new EscolaBean();
		escolaSuggestion = null;

		// FacesContext.getCurrentInstance().addMessage(null, new
		// FacesMessage("Item Selected", prodsel.getDescricao()));
	}

	public void limparBuscaDados() {
		tipoBuscaPaciente = 1;
		campoBuscaPaciente = "";
		statusPaciente = "P";
		listaPacientes = null;
	}

	public void limparBuscaDadosRaca() {
		tipoBuscaRaca = 1;
		campoBuscaRaca = "";
		statusRaca = "P";
		listaRaca = null;
	}

	public void limparDados() {
		transporteSuggestion = new FormaTransporteBean();
		encaminhadoSuggestion = new EncaminhadoBean();
		profissaoSuggestion = new ProfissaoBean();
		escolaridadeSuggestion = new EscolaridadeBean();
		escolaSuggestion = new EscolaBean();
		paciente = new PacienteBean();
		pacienteBuscado = new PacienteBean();
		endereco = new EnderecoBean();
		escola = new EscolaBean();
		escolaridade = new EscolaridadeBean();
		especialidade = new EspecialidadeBean();
		encaminhamento = new EncaminhamentoBean();
		encaminhado = new EncaminhadoBean();
		profissao = new ProfissaoBean();
		transporte = new FormaTransporteBean();
		raca = new RacaBean();
		this.pacienteSelecionado = new PacienteBean();

		tipoBuscar = "";
		descricaoParaBuscar = "";
		listaPacientesParaAgenda = new ArrayList<>();

	}

	public void buscarPacientes() throws ProjetoException {

		List<PacienteBean> listaAux = null;
		listaPacientes = new ArrayList<>();

		PacienteDAO adao = new PacienteDAO();

		listaAux = adao.buscarTipoPaciente(campoBuscaPaciente,
				tipoBuscaPaciente);

		if (listaAux != null && listaAux.size() > 0) {
			// listaAss = null;
			listaPacientes = listaAux;
		} else {
			// listaAss = null;
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN,
					"Nenhum Paciente encontrado.", "Aviso");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}

	}

	public void buscarRacas() throws ProjetoException {

		List<RacaBean> listaAux = null;
		listaRaca = new ArrayList<>();

		RacaDAO adao = new RacaDAO();

		listaAux = adao.buscarTipoRaca(campoBuscaRaca, tipoBuscaRaca);

		if (listaAux != null && listaAux.size() > 0) {
			// listaAss = null;
			listaRaca = listaAux;
		} else {
			// listaAss = null;
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN,
					"Nenhuma Ra�a encontrada.", "Aviso");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}

	}

	public void buscarescolaridade() {

	}

	public Integer getAbaAtiva() {
		return abaAtiva;
	}

	public void setAbaAtiva(Integer abaAtiva) {
		this.abaAtiva = abaAtiva;
	}

	public PacienteBean getPaciente() {
		return paciente;
	}

	public void setPaciente(PacienteBean paciente) {
		this.paciente = paciente;
	}

	public EnderecoBean getEndereco() {
		return endereco;
	}

	public void setEndereco(EnderecoBean endereco) {
		this.endereco = endereco;
	}

	public EscolaBean getEscola() {
		return escola;
	}

	public void setEscola(EscolaBean escola) {
		this.escola = escola;
	}

	public EscolaridadeBean getEscolaridade() {
		return escolaridade;
	}

	public void setEscolaridade(EscolaridadeBean escolaridade) {
		this.escolaridade = escolaridade;
	}

	public EspecialidadeBean getEspecialidade() {
		return especialidade;
	}

	public void setEspecialidade(EspecialidadeBean especialidade) {
		this.especialidade = especialidade;
	}

	public ConvenioBean getConvenio() {
		return convenio;
	}

	public void setConvenio(ConvenioBean convenio) {
		this.convenio = convenio;
	}

	public EncaminhamentoBean getEncaminhamento() {
		return encaminhamento;
	}

	public void setEncaminhamento(EncaminhamentoBean encaminhamento) {
		this.encaminhamento = encaminhamento;
	}

	public ProfissaoBean getProfissao() {
		return profissao;
	}

	public void setProfissao(ProfissaoBean profissao) {
		this.profissao = profissao;
	}

	public List<EscolaBean> getListaEscolas() throws ProjetoException {
		if (listaEscolas == null) {

			EscolaDAO fdao = new EscolaDAO();
			listaEscolas = fdao.listaEscolas();

		}
		return listaEscolas;
	}

	public void setListaEscolas(List<EscolaBean> listaEscolas) {
		this.listaEscolas = listaEscolas;
	}

	public List<EscolaridadeBean> getListaEscolaridade() throws ProjetoException {
		if (listaEscolaridade == null) {

			EscolaridadeDAO fdao = new EscolaridadeDAO();
			listaEscolaridade = fdao.listaEscolaridade();

		}
		return listaEscolaridade;
	}

	public void setListaEscolaridade(List<EscolaridadeBean> listaEscolaridade) {
		this.listaEscolaridade = listaEscolaridade;
	}

	public List<ProfissaoBean> getListaProfissao() throws ProjetoException {
		if (listaProfissao == null) {

			ProfissaoDAO fdao = new ProfissaoDAO();
			listaProfissao = fdao.listaProfissoes();

		}
		return listaProfissao;
	}

	public void setListaProfissao(List<ProfissaoBean> listaProfissao) {
		this.listaProfissao = listaProfissao;
	}

	public EncaminhadoBean getEncaminhado() {
		return encaminhado;
	}

	public void setEncaminhado(EncaminhadoBean encaminhado) {
		this.encaminhado = encaminhado;
	}

	public void listarPacientes() throws ProjetoException {
			PacienteDAO fdao = new PacienteDAO();
			listaPacientes = fdao.listaPacientes();
	}

	public void setListaPacientes(List<PacienteBean> listaPacientes) {
		this.listaPacientes = listaPacientes;
	}

	public List<EncaminhadoBean> getListaEncaminhado() throws ProjetoException {
		if (listaEncaminhado == null) {

			EncaminhadoDAO fdao = new EncaminhadoDAO();
			listaEncaminhado = fdao.listaEncaminhados();

		}
		return listaEncaminhado;
	}

	public void setListaEncaminhado(List<EncaminhadoBean> listaEncaminhado) {
		this.listaEncaminhado = listaEncaminhado;
	}

	public FormaTransporteBean getTransporte() {
		return transporte;
	}

	public void setTransporte(FormaTransporteBean transporte) {
		this.transporte = transporte;
	}

	public List<FormaTransporteBean> getListaTransporte() throws ProjetoException {
		if (listaTransporte == null) {

			FormaTransporteDAO fdao = new FormaTransporteDAO();
			listaTransporte = fdao.listaTransportes();

		}
		return listaTransporte;
	}

	public void setListaTransporte(List<FormaTransporteBean> listaTransporte) {
		this.listaTransporte = listaTransporte;
	}

	public List<RacaBean> getListaRaca() throws ProjetoException {
		if (listaRaca == null) {

			PacienteDAO fdao = new PacienteDAO();
			listaRaca = fdao.listaCor();

		}
		return listaRaca;
	}

	public void setListaRaca(List<RacaBean> listaRaca) {
		this.listaRaca = listaRaca;
	}

	public Integer getSelecionadoRaca() {
		return SelecionadoRaca;
	}

	public void setSelecionadoRaca(Integer selecionadoRaca) {
		SelecionadoRaca = selecionadoRaca;
	}

	public EscolaBean getEscolageral() {
		return escolageral;
	}

	public void setEscolageral(EscolaBean escolageral) {
		this.escolageral = escolageral;
	}

	public EscolaBean getEscolaselecionado() {
		return escolaselecionado;
	}

	public void setEscolaselecionado(EscolaBean escolaselecionado) {
		this.escolaselecionado = escolaselecionado;
	}

	public EscolaBean getEscolaselecionadoaux() {
		return escolaselecionadoaux;
	}

	public void setEscolaselecionadoaux(EscolaBean escolaselecionadoaux) {
		this.escolaselecionadoaux = escolaselecionadoaux;
	}

	public EscolaBean getEscolabuscarapida() {
		return escolabuscarapida;
	}

	public void setEscolabuscarapida(EscolaBean escolabuscarapida) {
		this.escolabuscarapida = escolabuscarapida;
	}

	public EscolaBean getEscolaselecionadocomposicao() {
		return escolaselecionadocomposicao;
	}

	public void setEscolaselecionadocomposicao(
			EscolaBean escolaselecionadocomposicao) {
		this.escolaselecionadocomposicao = escolaselecionadocomposicao;
	}

	public EscolaBean getEscolaSelecionadoExclusao() {
		return escolaSelecionadoExclusao;
	}

	public void setEscolaSelecionadoExclusao(
			EscolaBean escolaSelecionadoExclusao) {
		this.escolaSelecionadoExclusao = escolaSelecionadoExclusao;
	}

	public EscolaBean getEscolaSuggestion() {
		return escolaSuggestion;
	}

	public void setEscolaSuggestion(EscolaBean escolaSuggestion) {
		this.escolaSuggestion = escolaSuggestion;
	}

	public EscolaridadeBean getEscolaridadegeral() {
		return escolaridadegeral;
	}

	public void setEscolaridadegeral(EscolaridadeBean escolaridadegeral) {
		this.escolaridadegeral = escolaridadegeral;
	}

	public EscolaridadeBean getEscolaridadeselecionado() {
		return escolaridadeselecionado;
	}

	public void setEscolaridadeselecionado(
			EscolaridadeBean escolaridadeselecionado) {
		this.escolaridadeselecionado = escolaridadeselecionado;
	}

	public EscolaridadeBean getEscolaridadeselecionadoaux() {
		return escolaridadeselecionadoaux;
	}

	public void setEscolaridadeselecionadoaux(
			EscolaridadeBean escolaridadeselecionadoaux) {
		this.escolaridadeselecionadoaux = escolaridadeselecionadoaux;
	}

	public EscolaridadeBean getEscolaridadebuscarapida() {
		return escolaridadebuscarapida;
	}

	public void setEscolaridadebuscarapida(
			EscolaridadeBean escolaridadebuscarapida) {
		this.escolaridadebuscarapida = escolaridadebuscarapida;
	}

	public EscolaridadeBean getEscolaridadeselecionadocomposicao() {
		return escolaridadeselecionadocomposicao;
	}

	public void setEscolaridadeselecionadocomposicao(
			EscolaridadeBean escolaridadeselecionadocomposicao) {
		this.escolaridadeselecionadocomposicao = escolaridadeselecionadocomposicao;
	}

	public EscolaridadeBean getEscolaridadeSelecionadoExclusao() {
		return escolaridadeSelecionadoExclusao;
	}

	public void setEscolaridadeSelecionadoExclusao(
			EscolaridadeBean escolaridadeSelecionadoExclusao) {
		this.escolaridadeSelecionadoExclusao = escolaridadeSelecionadoExclusao;
	}

	public EscolaridadeBean getEscolaridadeSuggestion() {
		return escolaridadeSuggestion;
	}

	public void setEscolaridadeSuggestion(
			EscolaridadeBean escolaridadeSuggestion) {
		this.escolaridadeSuggestion = escolaridadeSuggestion;
	}

	public EncaminhamentoBean getEncaminhamentogeral() {
		return encaminhamentogeral;
	}

	public void setEncaminhamentogeral(EncaminhamentoBean encaminhamentogeral) {
		this.encaminhamentogeral = encaminhamentogeral;
	}

	public EncaminhamentoBean getEncaminhamentoselecionado() {
		return encaminhamentoselecionado;
	}

	public void setEncaminhamentoselecionado(
			EncaminhamentoBean encaminhamentoselecionado) {
		this.encaminhamentoselecionado = encaminhamentoselecionado;
	}

	public EncaminhamentoBean getEncaminhamentoselecionadoaux() {
		return encaminhamentoselecionadoaux;
	}

	public void setEncaminhamentoselecionadoaux(
			EncaminhamentoBean encaminhamentoselecionadoaux) {
		this.encaminhamentoselecionadoaux = encaminhamentoselecionadoaux;
	}

	public EncaminhamentoBean getEncaminhamentobuscarapida() {
		return encaminhamentobuscarapida;
	}

	public void setEncaminhamentobuscarapida(
			EncaminhamentoBean encaminhamentobuscarapida) {
		this.encaminhamentobuscarapida = encaminhamentobuscarapida;
	}

	public EncaminhamentoBean getEncaminhamentoselecionadocomposicao() {
		return encaminhamentoselecionadocomposicao;
	}

	public void setEncaminhamentoselecionadocomposicao(
			EncaminhamentoBean encaminhamentoselecionadocomposicao) {
		this.encaminhamentoselecionadocomposicao = encaminhamentoselecionadocomposicao;
	}

	public EncaminhamentoBean getEncaminhamentoSelecionadoExclusao() {
		return encaminhamentoSelecionadoExclusao;
	}

	public void setEncaminhamentoSelecionadoExclusao(
			EncaminhamentoBean encaminhamentoSelecionadoExclusao) {
		this.encaminhamentoSelecionadoExclusao = encaminhamentoSelecionadoExclusao;
	}

	public EncaminhamentoBean getEncaminhamentoSuggestion() {
		return encaminhamentoSuggestion;
	}

	public void setEncaminhamentoSuggestion(
			EncaminhamentoBean encaminhamentoSuggestion) {
		this.encaminhamentoSuggestion = encaminhamentoSuggestion;
	}

	public EncaminhadoBean getEncaminhadogeral() {
		return encaminhadogeral;
	}

	public void setEncaminhadogeral(EncaminhadoBean encaminhadogeral) {
		this.encaminhadogeral = encaminhadogeral;
	}

	public EncaminhadoBean getEncaminhadoselecionado() {
		return encaminhadoselecionado;
	}

	public void setEncaminhadoselecionado(EncaminhadoBean encaminhadoselecionado) {
		this.encaminhadoselecionado = encaminhadoselecionado;
	}

	public EncaminhadoBean getEncaminhadoselecionadoaux() {
		return encaminhadoselecionadoaux;
	}

	public void setEncaminhadoselecionadoaux(
			EncaminhadoBean encaminhadoselecionadoaux) {
		this.encaminhadoselecionadoaux = encaminhadoselecionadoaux;
	}

	public EncaminhadoBean getEncaminhadobuscarapida() {
		return encaminhadobuscarapida;
	}

	public void setEncaminhadobuscarapida(EncaminhadoBean encaminhadobuscarapida) {
		this.encaminhadobuscarapida = encaminhadobuscarapida;
	}

	public EncaminhadoBean getEncaminhadoselecionadocomposicao() {
		return encaminhadoselecionadocomposicao;
	}

	public void setEncaminhadoselecionadocomposicao(
			EncaminhadoBean encaminhadoselecionadocomposicao) {
		this.encaminhadoselecionadocomposicao = encaminhadoselecionadocomposicao;
	}

	public EncaminhadoBean getEncaminhadoSelecionadoExclusao() {
		return encaminhadoSelecionadoExclusao;
	}

	public void setEncaminhadoSelecionadoExclusao(
			EncaminhadoBean encaminhadoSelecionadoExclusao) {
		this.encaminhadoSelecionadoExclusao = encaminhadoSelecionadoExclusao;
	}

	public EncaminhadoBean getEncaminhadoSuggestion() {
		return encaminhadoSuggestion;
	}

	public void setEncaminhadoSuggestion(EncaminhadoBean encaminhadoSuggestion) {
		this.encaminhadoSuggestion = encaminhadoSuggestion;
	}

	public FormaTransporteBean getTransportegeral() {
		return transportegeral;
	}

	public void setTransportegeral(FormaTransporteBean transportegeral) {
		this.transportegeral = transportegeral;
	}

	public FormaTransporteBean getTransporteselecionado() {
		return transporteselecionado;
	}

	public void setTransporteselecionado(
			FormaTransporteBean transporteselecionado) {
		this.transporteselecionado = transporteselecionado;
	}

	public FormaTransporteBean getTransporteselecionadoaux() {
		return transporteselecionadoaux;
	}

	public void setTransporteselecionadoaux(
			FormaTransporteBean transporteselecionadoaux) {
		this.transporteselecionadoaux = transporteselecionadoaux;
	}

	public FormaTransporteBean getTransportebuscarapida() {
		return transportebuscarapida;
	}

	public void setTransportebuscarapida(
			FormaTransporteBean transportebuscarapida) {
		this.transportebuscarapida = transportebuscarapida;
	}

	public FormaTransporteBean getTransporteselecionadocomposicao() {
		return transporteselecionadocomposicao;
	}

	public void setTransporteselecionadocomposicao(
			FormaTransporteBean transporteselecionadocomposicao) {
		this.transporteselecionadocomposicao = transporteselecionadocomposicao;
	}

	public FormaTransporteBean getTransporteSelecionadoExclusao() {
		return transporteSelecionadoExclusao;
	}

	public void setTransporteSelecionadoExclusao(
			FormaTransporteBean transporteSelecionadoExclusao) {
		this.transporteSelecionadoExclusao = transporteSelecionadoExclusao;
	}

	public FormaTransporteBean getTransporteSuggestion() {
		return transporteSuggestion;
	}

	public void setTransporteSuggestion(FormaTransporteBean transporteSuggestion) {
		this.transporteSuggestion = transporteSuggestion;
	}

	public ProfissaoBean getProfissaogeral() {
		return profissaogeral;
	}

	public void setProfissaogeral(ProfissaoBean profissaogeral) {
		this.profissaogeral = profissaogeral;
	}

	public ProfissaoBean getProfissaoselecionado() {
		return profissaoselecionado;
	}

	public void setProfissaoselecionado(ProfissaoBean profissaoselecionado) {
		this.profissaoselecionado = profissaoselecionado;
	}

	public ProfissaoBean getProfissaoselecionadoaux() {
		return profissaoselecionadoaux;
	}

	public void setProfissaoselecionadoaux(ProfissaoBean profissaoselecionadoaux) {
		this.profissaoselecionadoaux = profissaoselecionadoaux;
	}

	public ProfissaoBean getProfissaobuscarapida() {
		return profissaobuscarapida;
	}

	public void setProfissaobuscarapida(ProfissaoBean profissaobuscarapida) {
		this.profissaobuscarapida = profissaobuscarapida;
	}

	public ProfissaoBean getProfissaoselecionadocomposicao() {
		return profissaoselecionadocomposicao;
	}

	public void setProfissaoselecionadocomposicao(
			ProfissaoBean profissaoselecionadocomposicao) {
		this.profissaoselecionadocomposicao = profissaoselecionadocomposicao;
	}

	public ProfissaoBean getProfissaoSelecionadoExclusao() {
		return profissaoSelecionadoExclusao;
	}

	public void setProfissaoSelecionadoExclusao(
			ProfissaoBean profissaoSelecionadoExclusao) {
		this.profissaoSelecionadoExclusao = profissaoSelecionadoExclusao;
	}

	public ProfissaoBean getProfissaoSuggestion() {
		return profissaoSuggestion;
	}

	public void setProfissaoSuggestion(ProfissaoBean profissaoSuggestion) {
		this.profissaoSuggestion = profissaoSuggestion;
	}

	public List<EnderecoBean> getListaMunicipios() throws ProjetoException {
		if (listaMunicipios == null) {

			EnderecoDAO fdao = new EnderecoDAO();
			listaMunicipios = fdao.listaMunicipios();

		}
		return listaMunicipios;
	}

	public void setListaMunicipios(List<EnderecoBean> listaMunicipios) {
		this.listaMunicipios = listaMunicipios;
	}

	public RacaBean getRaca() {
		return raca;
	}

	public void setRaca(RacaBean raca) {
		this.raca = raca;
	}

	public String getDescricaoParaBuscar() {
		return descricaoParaBuscar;
	}

	public void setDescricaoParaBuscar(String descricaoParaBuscar) {
		this.descricaoParaBuscar = descricaoParaBuscar;
	}

	public String getTipoBuscar() {
		return tipoBuscar;
	}

	public void setTipoBuscar(String tipoBuscar) {
		this.tipoBuscar = tipoBuscar;
	}

	public Integer getTipoBuscaPaciente() {
		return tipoBuscaPaciente;
	}

	public void setTipoBuscaPaciente(Integer tipoBuscaPaciente) {
		this.tipoBuscaPaciente = tipoBuscaPaciente;
	}

	public String getCampoBuscaPaciente() {
		return campoBuscaPaciente;
	}

	public void setCampoBuscaPaciente(String campoBuscaPaciente) {
		this.campoBuscaPaciente = campoBuscaPaciente;
	}

	public String getStatusPaciente() {
		return statusPaciente;
	}

	public void setStatusPaciente(String statusPaciente) {
		this.statusPaciente = statusPaciente;
	}

	
	public Integer getTipoBuscaRaca() {
		return tipoBuscaRaca;
	}

	public void setTipoBuscaRaca(Integer tipoBuscaRaca) {
		this.tipoBuscaRaca = tipoBuscaRaca;
	}

	public String getCampoBuscaRaca() {
		return campoBuscaRaca;
	}

	public void setCampoBuscaRaca(String campoBuscaRaca) {
		this.campoBuscaRaca = campoBuscaRaca;
	}

	public String getStatusRaca() {
		return statusRaca;
	}

	public void setStatusRaca(String statusRaca) {
		this.statusRaca = statusRaca;
	}

	public String getCabecalho() {
		if (this.tipo==1) {
			cabecalho = "Inclusão de Paciente";
		} else if (this.tipo==2) {
			cabecalho = "Alteração de Paciente";
		}
		return cabecalho;
	}

	public void setCabecalho(String cabecalho) {
		this.cabecalho = cabecalho;
	}

	public PacienteBean getPacienteBuscado() {
		return pacienteBuscado;
	}

	public void setPacienteBuscado(PacienteBean pacienteBuscado) {
		this.pacienteBuscado = pacienteBuscado;
	}

	public String getCabecalho2() {
		if (this.tipo==1) {
			cabecalho2 = "Inclusão de Raça";
		} else if (this.tipo==2) {
			cabecalho2 = "Alteração de Raça";
		}
		return cabecalho2;
	}

	public void setCabecalho2(String cabecalho2) {
		this.cabecalho2 = cabecalho2;
	}
	
	
	public void selectPaciente(SelectEvent event) {
		this.pacienteSelecionado = (PacienteBean) event.getObject();
	}

	public PacienteBean getPacienteSelecionado() {
		return pacienteSelecionado;
	}

	public void setPacienteSelecionado(PacienteBean pacienteSelecionado) {
		this.pacienteSelecionado = pacienteSelecionado;
	}

	public int getTipo() {
		return tipo;
	}

	public void setTipo(int tipo) {
		this.tipo = tipo;
	}

	public List<PacienteBean> getListaPacientes() {
		return listaPacientes;
	}
	
	

}
