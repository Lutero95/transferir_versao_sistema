
package br.gov.al.maceio.sishosp.acl.control;

import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import br.gov.al.maceio.sishosp.acl.model.*;
import br.gov.al.maceio.sishosp.hosp.dao.ProgramaDAO;
import br.gov.al.maceio.sishosp.hosp.model.dto.AtalhosAmbulatorialDTO;
import org.primefaces.event.TransferEvent;
import org.primefaces.model.DualListModel;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.DefaultSubMenu;
import org.primefaces.model.menu.MenuModel;

import br.gov.al.maceio.sishosp.acl.dao.FuncaoDAO;
import br.gov.al.maceio.sishosp.acl.dao.FuncionarioDAO;
import br.gov.al.maceio.sishosp.acl.dao.MenuDAO;
import br.gov.al.maceio.sishosp.acl.dao.PermissaoDAO;
import br.gov.al.maceio.sishosp.acl.enums.DiaSemanaTabelaParametro;
import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.DocumentosUtil;
import br.gov.al.maceio.sishosp.comum.util.JSFUtil;
import br.gov.al.maceio.sishosp.comum.util.RedirecionarUtil;
import br.gov.al.maceio.sishosp.comum.util.SessionUtil;
import br.gov.al.maceio.sishosp.comum.util.VerificadorUtil;
import br.gov.al.maceio.sishosp.hosp.dao.UnidadeDAO;
import br.gov.al.maceio.sishosp.hosp.model.ProgramaBean;
import br.gov.al.maceio.sishosp.hosp.model.UnidadeBean;

@ManagedBean(name = "MBFuncionarios")
@SessionScoped
public class FuncionarioController implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final String TRANSPARENTE = "transparent !important";
	private static final String VERMELHO = "#ff8f8f !important";
	private static final String VERDE = "#dcffb8 !important";
	private FuncionarioBean usuario;
	private FuncionarioBean profissional;
	private List<FuncionarioBean> listaProfissional;
	private String cabecalho;
	private int tipo;
	private ArrayList<ProgramaBean> listaGruposEProgramasProfissional;
	private FuncionarioDAO fDao = new FuncionarioDAO();
	private UnidadeBean unidadeBean;
	private AtalhosAmbulatorialDTO atalhosAmbulatorialDTO;
	private List<UnidadeBean> unidadesDoUsuarioLogado;
	private Integer codigoDaUnidadeSelecionada = null;
	private List<Empresa> listaEmpresaFuncionario;
	private Empresa empresa;
	private List<UnidadeBean> listaUnidadesDoUsuario;
	private UnidadeBean unidadeParaProgramasIhGrupos;

	// SESSÃO
	private FuncionarioBean usuarioLogado;
	private Sistema sistemaLogado;

	private List<Sistema> sistemasUsuarioLogado;
	private List<Permissoes> permsUsuarioLogado;
	private List<Menu> listaMenus;

	private List<Permissoes> listaPerms;

	private DualListModel<Menu> listaMenusDual;
	private List<Menu> listaMenusSource;
	private List<Menu> listaMenusTarget;

	private DualListModel<Menu> listaMenusDualEdit;
	private List<Menu> listaMenusSourceEdit;
	private List<Menu> listaMenusTargetEdit;

	private DualListModel<Funcao> listaFuncoesDual;
	private List<Funcao> listaFuncoesSource;
	private List<Funcao> listaFuncoesTarget;

	private DualListModel<Funcao> listaFuncoesDualEdit;
	private List<Funcao> listaFuncoesSourceEdit;
	private List<Funcao> listaFuncoesTargetEdit;

	private Boolean renderizarPermissoes;
	private UnidadeDAO unidadeDAO;

	// Dual Sistema
	private DualListModel<Sistema> listaSistemasDual;
	private List<Sistema> listaSistemasSoucer;
	private List<Sistema> listaSistemasTarget;
	private List<Integer> codigoSistema;
	private String corEvoluido;
	private String corNaoEvoluido;

	// MENU
	private MenuModel menuModel;

	// CONSTANTES
	private static final String ENDERECO_CADASTRO = "cadastroProfissional?faces-redirect=true";
	private static final String ENDERECO_TIPO = "&amp;tipo=";
	private static final String ENDERECO_ID = "&amp;id=";
	private static final String CABECALHO_INCLUSAO = "Inclusão de Profissional";
	private static final String CABECALHO_ALTERACAO = "Alteração de Profissional";

	public FuncionarioController() {
		
		// Profissional
		listaProfissional = new ArrayList<FuncionarioBean>();
		this.profissional = null;
		this.profissional = new FuncionarioBean();
		this.listaGruposEProgramasProfissional = new ArrayList<ProgramaBean>();
		usuario = new FuncionarioBean();
		renderizarPermissoes = false;
		unidadesDoUsuarioLogado = new ArrayList<>();
		listaEmpresaFuncionario = new ArrayList<>();
		empresa = new Empresa();
		listaUnidadesDoUsuario = new ArrayList<>();
		unidadeParaProgramasIhGrupos = new UnidadeBean();

		// ACL
		usuarioLogado = new FuncionarioBean();
		permsUsuarioLogado = new ArrayList<>();
		sistemasUsuarioLogado = new ArrayList<>();
		sistemaLogado = new Sistema();
		listaMenus = new ArrayList<>();
		menuModel = new DefaultMenuModel();
		listaPerms = new ArrayList<>();

		listaMenusDual = null;
		listaMenusSource = new ArrayList<>();
		listaMenusTarget = new ArrayList<>();

		listaMenusDualEdit = null;
		listaMenusDualEdit = new DualListModel<>();
		listaMenusSourceEdit = new ArrayList<>();
		listaMenusTargetEdit = new ArrayList<>();

		listaSistemasDual = new DualListModel<Sistema>();
		listaSistemasSoucer = new ArrayList<>();
		listaSistemasTarget = new ArrayList<>();
		codigoSistema = new ArrayList<>();

		listaFuncoesDual = null;
		listaFuncoesSource = new ArrayList<>();
		listaFuncoesTarget = new ArrayList<>();

		listaFuncoesDualEdit = null;
		listaFuncoesSourceEdit = new ArrayList<>();
		listaFuncoesTargetEdit = new ArrayList<>();
		
		unidadeDAO = new UnidadeDAO();
	}

	public boolean verificarPermComp(String codigo, Integer idSistema) {
		boolean valida = false;
		for (Permissoes perms : permsUsuarioLogado) {
			if (perms.getIdSistemaFunc() != null && perms.getCodigoFuncao() != null) {
				if (perms.getIdSistemaFunc().equals(idSistema) && perms.getCodigoFuncao().equals(codigo)) {
					valida = true;
				}
			}
		}
		return valida;
	}

	public void onTransferSistema(TransferEvent event) {
		StringBuilder builder = new StringBuilder();
	for (Object item : event.getItems()) {
			builder.append(((Sistema) item).getId());
			if (listaSistemasTarget.contains(item)) {
				listaSistemasTarget.remove(item);
			} else {
				listaSistemasTarget.add((Sistema) item);
			}
		}
	}

	public String login() throws ProjetoException {

		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("expired", "N");

		String nomeBancoAcesso = fDao.autenticarUsuarioInicialNomeBancoAcesso(usuario);

		String retorno = "";

		if (VerificadorUtil.verificarSeObjetoNuloOuZero(nomeBancoAcesso)) {
			JSFUtil.adicionarMensagemErro("Usuário ou senha Inválida!!", "Erro");
			return null;

		} else {

			Integer quantidadeEmpresas = fDao.verificarSeTrabalhaEmMaisDeUmaEmpresa(usuario.getCpf());

			if(quantidadeEmpresas > 1){
				listaEmpresaFuncionario = fDao.carregarEmpresasDoFuncionario(usuario.getCpf());
				JSFUtil.abrirDialog("selecaoEmpresa");
			}

			else{
				retorno = autenticarUsuario();

			}

			return retorno;
		}
	}

	public Boolean verificarSeTemHorarioLimieIhSeHorarioEhPermitidoPorUsuario(String cpf) throws ProjetoException{
		return fDao.verificarSeTemHorarioLimieIhSeHorarioEhPermitidoPorUsuario(usuario.getCpf());
	}

	public Boolean verificarSeTemHorarioLimiteIhSeHorarioEhPermitidoPorUnidade(Integer codigoUnidade) throws ProjetoException{
		return fDao.verificarSeTemHorarioLimiteIhSeHorarioEhPermitidoPorUnidade(codigoUnidade);
	}

	public String autenticarUsuario() throws ProjetoException {

		if(!VerificadorUtil.verificarSeObjetoNuloOuVazio(empresa.getBancoAcesso())) {
			SessionUtil.adicionarNaSessao(empresa.getBancoAcesso(), "nomeBancoAcesso");
		}

		usuarioLogado = fDao.autenticarUsuario(usuario);
		
		if (usuarioLogado == null) {
			JSFUtil.adicionarMensagemErro("Usuário ou senha Inválida!!", "Erro");
			return null;

		} else {
			
			if(usuarioPodeAcessarSistemaHoje()) {			
				unidadesDoUsuarioLogado = fDao.listarTodasAsUnidadesDoUsuario(usuarioLogado.getId());
				if (unidadesDoUsuarioLogado.size() > 1) {
					JSFUtil.abrirDialog("selecaoUnidade");

					return null;
				} else {
					if ((!usuarioLogado.getExcecaoBloqueioHorario())
							&& (!verificarSeTemHorarioLimieIhSeHorarioEhPermitidoPorUsuario(usuario.getCpf()))) {
						JSFUtil.adicionarMensagemErro("Acesso bloqueado a esta unidade nesse horário!", "");
						return null;
					} else {
						String url = carregarSistemasDoUsuarioLogadoIhJogarUsuarioNaSessao();
						return url;
					}
				}
			}
			return null;
		}
	}
	
	private boolean usuarioPodeAcessarSistemaHoje() throws ProjetoException {
		if(usuarioLogado.getExcecaoBloqueioHorario())
			return true;
		
		String diaAtual = retornaDiaAtual();
		boolean acessoPermitidoHoje = fDao.verificarSeTemPermissaoAcessoHoje(usuarioLogado, diaAtual);
		if(!acessoPermitidoHoje)
			JSFUtil.adicionarMensagemErro("Você só pode acessar o Sistema nos dias permitidos", "Erro");
		return acessoPermitidoHoje;
	}
	
	private String retornaDiaAtual() {
		Calendar calendar = Calendar.getInstance();
		int diaAtual = calendar.get(Calendar.DAY_OF_WEEK);
		
		if(diaAtual == 1)
			return DiaSemanaTabelaParametro.DOMINGO.getSigla();
		else if (diaAtual == 2)
			return DiaSemanaTabelaParametro.SEGUNDA.getSigla();
		else if (diaAtual == 3)
			return DiaSemanaTabelaParametro.TERCA.getSigla();
		else if (diaAtual == 4)
			return DiaSemanaTabelaParametro.QUARTA.getSigla();
		else if (diaAtual == 5)
			return DiaSemanaTabelaParametro.QUINTA.getSigla();
		else if (diaAtual == 6)
			return DiaSemanaTabelaParametro.SEXTA.getSigla();
		else
			return DiaSemanaTabelaParametro.SABADO.getSigla();
	}

	public String associarUnidadeSelecionadaAoUsuarioDaSessaoIhRealizarLogin() throws ProjetoException {
		UnidadeBean unidade = unidadeDAO.buscarUnidadePorId(codigoDaUnidadeSelecionada);
		usuarioLogado.setUnidade(unidade);

		if ((!usuarioLogado.getExcecaoBloqueioHorario()) &&  (!verificarSeTemHorarioLimiteIhSeHorarioEhPermitidoPorUnidade(unidade.getId()))){
			JSFUtil.adicionarMensagemErro("Acesso bloqueado a esta unidade nesse horário!", "");
			return null;
		}
		else {
			FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("obj_funcionario", usuarioLogado);
			return carregarSistemasDoUsuarioLogadoIhJogarUsuarioNaSessao();
		}
	}

	public void associarUnidadeSelecionadaAoUsuarioDaSessao() throws ProjetoException, IOException {
		UnidadeBean unidade = unidadeDAO.buscarUnidadePorId(codigoDaUnidadeSelecionada);
		usuarioLogado.setUnidade(unidade);
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("obj_usuario", usuarioLogado);
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("obj_funcionario",
				usuarioLogado);
		String path = getServleContext().getContextPath();
		FacesContext.getCurrentInstance().getExternalContext().redirect(path+"/pages/comum/selecaoSistema.faces");
	}

	public void abrirDialogDeSelecaoDeUnidade() throws ProjetoException {

		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("expired", "N");
		String nomeBancoAcesso = fDao.autenticarUsuarioInicialNomeBancoAcesso(usuario);

		if (VerificadorUtil.verificarSeObjetoNuloOuZero(nomeBancoAcesso)) {
			JSFUtil.adicionarMensagemErro("Usuário ou senha Inválida!!", "Erro");
		} else {

			usuarioLogado = fDao.autenticarUsuario(usuario);

			if (usuarioLogado == null) {
				JSFUtil.adicionarMensagemErro("Usuário ou senha Inválida!!", "Erro");
			} else {
				List<UnidadeBean> unidadesDoUsuario = fDao.listarTodasAsUnidadesDoUsuario(usuarioLogado.getId());
				if(unidadesDoUsuario.size() > 1){
					JSFUtil.abrirDialog("selecaoUnidade");
				}
			}
		}
	}


	private FacesContext getFacesContext() {
		FacesContext context = FacesContext.getCurrentInstance();
		return context;
	}

	private ServletContext getServleContext() {
		ServletContext scontext = (ServletContext) this.getFacesContext().getExternalContext().getContext();
		return scontext;
	}


	private String carregarSistemasDoUsuarioLogadoIhJogarUsuarioNaSessao() throws ProjetoException {
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("obj_usuario",
				usuarioLogado);
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("obj_funcionario",
				usuarioLogado);
		String root =  getServleContext().getContextPath();
		// ACL =============================================================

		List<Sistema> sistemas = fDao.carregarSistemasUsuario(usuarioLogado);

		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("perms_usuario_sis",
				sistemas);

		List<Permissoes> permissoes = fDao.carregarPermissoes(usuarioLogado);

		carregarAtalhosPaginaInicial(permissoes);

		sistemaLogado.setDescricao("Sem Sistema");
		sistemaLogado.setSigla("Sem Sistema");
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("sistema_logado",
				sistemaLogado);

		for (Sistema s : sistemas) {
			Menu m = new Menu();
			m.setDescricao("Principal");
			m.setUrl(s.getUrl());
			m.setTipo("injetado");
			Permissoes perms = new Permissoes();
			perms.setMenu(m);
			permissoes.add(perms);
		}

		for (Sistema s : sistemas) {
			Menu m = new Menu();
			m.setDescricao("Fale Conosco");
			m.setUrl(s.getUrl());
			m.setTipo("injetado");
			Permissoes perms = new Permissoes();
			perms.setMenu(m);
			permissoes.add(perms);
		}

		Permissoes perms2 = new Permissoes();
		Menu m3 = new Menu();
		m3.setDescricao("Primeiro Acesso");
		m3.setUrl("/pages/comum/primeiroAcesso.faces");
		m3.setTipo("injetado");
		perms2.setMenu(m3);
		permissoes.add(perms2);

		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("perms_usuario", permissoes);

		HttpSession session = SessionUtil.getSession();
		session.setAttribute("User", usuarioLogado.getId());

		recoverDataFromSession();

		String url = "";


		if (sistemas.size() > 1) {
			url = "/pages/comum/selecaoSistema.faces?faces-redirect=true";
		}
		else {
			recSistemaLogado(sistemas.get(0));
			gerarMenus(sistemaLogado);
			url = root+sistemas.get(0).getUrl() + "?faces-redirect=true";
		}

		return url;


	}

	public void carregarAtalhosPaginaInicial(List<Permissoes> permissoes){
		atalhosAmbulatorialDTO = new AtalhosAmbulatorialDTO();

		for(int i=0; i<permissoes.size(); i++){

			if(!VerificadorUtil.verificarSeObjetoNuloOuVazio(permissoes.get(i).getMenu().getUrl())) {

				if (permissoes.get(i).getMenu().getUrl().contains("cadastroPaciente")) {
					atalhosAmbulatorialDTO.setAtalhoCadastroPaciente(true);
				}
				if (permissoes.get(i).getMenu().getUrl().contains("insercaoPaciente")) {
					atalhosAmbulatorialDTO.setAtalhoInsercaoPaciente(true);
				}
				if (permissoes.get(i).getMenu().getUrl().contains("cadastroLaudoDigita")) {
					atalhosAmbulatorialDTO.setAtalhoLaudo(true);
				}
				if (permissoes.get(i).getMenu().getUrl().contains("agendaMedica")) {
					atalhosAmbulatorialDTO.setAtalhoAgenda(true);
				}
				if (permissoes.get(i).getMenu().getUrl().contains("consAgendamentos")) {
					atalhosAmbulatorialDTO.setAtalhoConsultarAgendamentos(true);
				}
				if (permissoes.get(i).getMenu().getDescPagina().equals("atendimento")) {
					atalhosAmbulatorialDTO.setAtalhoAtendimentos(true);
				}
			}

		}

	}

	public void carregaListaSistemasDualInsercao() throws ProjetoException {
		listaSistemasSoucer = null;
		listaSistemasTarget = new ArrayList<>();
		listaSistemasSoucerNaInclusao();
		listaSistemasDual = new DualListModel<>(listaSistemasSoucer, listaSistemasTarget);
	}

	public void carregaListaSistemasDualAlteracao() throws ProjetoException {
		listaSistemasSoucer = null;
		listaSistemasTarget = new ArrayList<>();
		listaSistemasSoucerNaAlteracao();
		listaSistemasTargetNaAlteracao();
		listaSistemasDual = new DualListModel<>(listaSistemasSoucer, listaSistemasTarget);
	}

	public void setListaSistemasDual(DualListModel<Sistema> listaSistemasDual) {
		this.listaSistemasDual = listaSistemasDual;
	}

	public String redirecionar(String url) {
		gerarMenus(sistemaLogado);
		return url;
	}

	public void recSistemaLogado(Sistema sistema) {
		sistemaLogado = sistema;
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("sistema_logado", sistema);
	}

	public void recoverDataFromSession() {
		usuarioLogado = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
				.get("obj_usuario");

		sistemasUsuarioLogado = (List<Sistema>) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
				.get("perms_usuario_sis");

		permsUsuarioLogado = (List<Permissoes>) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
				.get("perms_usuario");

	}

	private void limparMenuModel() {
		menuModel = null;
		menuModel = new DefaultMenuModel();
	}

	public void gerarMenus(Sistema sistema) {
		// FacesContext fc = FacesContext.getCurrentInstance();
		// HttpServletRequest request = (HttpServletRequest)
		// fc.getExternalContext().getRequest();
		// String contextPath = request.getServletContext().getContextPath();
		String root =  getServleContext().getContextPath();
		limparMenuModel();
		List<DefaultSubMenu> menuPai = new ArrayList<>();
		List<DefaultSubMenu> submenu = new ArrayList<>();
		// List<DefaultSubMenu> submenu2 = new ArrayList<>();
		List<DefaultMenuItem> menuItem = new ArrayList<>();
		List<DefaultSubMenu> subsubAssSubMenu = new ArrayList<>();
		List<DefaultSubMenu> menuItemAssSubmenu = new ArrayList<>();
		List<DefaultSubMenu> submenuAssSubMenuPai = new ArrayList<>();
		List<DefaultSubMenu> menusAssociados = new ArrayList<>();

		// Gerar menu inicio.
		DefaultMenuItem item1 = new DefaultMenuItem();
		item1.setValue("Início");
		// contextPath+
		item1.setUrl(root+sistema.getUrl().replace("?faces-redirect=true", ""));
		// item1.setStyle("overflow: auto; !important");
		menuModel.addElement(item1);

		for (Permissoes p : permsUsuarioLogado) {
			// Gerar menu pai.
			if ((p.getMenu().getIndice() == null) && (p.getMenu().getUrl() == null)
					&& (p.getMenu().getTipo().equals("menuPai")) && (p.getIdSistema().equals(sistema.getId()))
					&& (p.getMenu().isAtivo() == true)) {

				DefaultSubMenu mp = new DefaultSubMenu();
				/*
				 * mp.setStyle("display: block;\n" + "    z-index: 1017;\n" + "    left: 0px;\n"
				 * + "    top: 54px;\n" + "    overflow: auto;\n" + "    height: 312px;");
				 */
				mp.setLabel(p.getMenu().getDescricao());// o nome(descricao) que
				// vai aparecer na
				// interface
				mp.setIcon(p.getMenu().getCodigo()); // faz a associacao em que
				// menu ele vai ficar
				menuPai.add(mp);

			}

			// Gerar submenu.
			if ((p.getMenu().getIndice() != null) && (p.getMenu().getUrl() == null)
					&& (p.getMenu().getTipo().equals("submenu")) // compara o
					// tipo para
					// verificar
					// se
					// submenu
					&& (p.getIdSistema().equals(sistema.getId())) // compara os
					// ids
					&& (p.getMenu().isAtivo() == true)) { // se for ativo

				DefaultSubMenu sb = new DefaultSubMenu();
				/*
				 * sb.setStyle("display: block;\n" + "    z-index: 1017;\n" + "    left: 0px;\n"
				 * + "    top: 54px;\n" + "    overflow: auto;\n" + "    height: 312px;");
				 */
				// inicia a criação do
				// submenu
				sb.setLabel(p.getMenu().getDescricao());
				sb.setIcon(p.getMenu().getCodigo());
				sb.setId(p.getMenu().getIndice());
				submenu.add(sb);// cria o submenu
			}

			// Gerar menu item com url.
			if ((p.getMenu().getIndice() != null) && (p.getMenu().getUrl() != null)
					&& (p.getMenu().getTipo().equals("menuItem"))// compara o
					// tipo
					&& (p.getIdSistema().equals(sistema.getId())) && (p.getMenu().isAtivo() == true)) {
				Menu mi = p.getMenu();
				DefaultMenuItem item = new DefaultMenuItem();
				item.setValue(mi.getDescricao());
				item.setUrl(root+mi.getUrl());
				item.setIcon(p.getMenu().getCodigo());// coloca o submenu dentro
				// do menu
				item.setId(mi.getIndice());
				menuItem.add(item);
			}

			// Gerar menu item com action/onclick.
			if ((p.getMenu().getIndice() != null) && (p.getMenu().getUrl() == null)
					&& (p.getMenu().getTipo().equals("menuItemRel")) && (p.getIdSistema().equals(sistema.getId()))
					&& (p.getMenu().isAtivo() == true)) {

				Menu mi = p.getMenu();

				DefaultMenuItem item = new DefaultMenuItem();
				item.setValue(mi.getDescricao());

				if (mi.getAction() != null) {
					item.setCommand(mi.getAction());
					item.setAjax(false);
				} else {
					item.setOnclick(mi.getOnclick());
				}

				item.setIcon(p.getMenu().getCodigo());
				item.setId(mi.getIndice());
				menuItem.add(item);
			}
		}

		// Associar menu item com submenu.
		for (DefaultSubMenu sb : submenu) {
			DefaultSubMenu submenuAux = sb; // submenu que é elemento da lista
			// recebe sb(submenu)
			for (DefaultMenuItem mi : menuItem) {
				if (sb.getIcon().equals(mi.getId())) {

					mi.setIcon(null);
					submenuAux.addElement(mi);

				}
			}
			menuItemAssSubmenu.add(submenuAux);

		}

		// Associar submenu com submenu.
		for (DefaultSubMenu sb2 : submenu) {

			for (DefaultSubMenu sb : menuItemAssSubmenu) {
				if (sb2.getIcon().equals(sb.getId())) {
					// sb.setIcon(null);

					sb2.addElement(sb);// esse auxiliar recebe o submenu e
					// depois mais embaixo adiciona ele na
					// lista
				}
			}
			subsubAssSubMenu.add(sb2);
		}

		// Associar submenu com menu pai.
		for (DefaultSubMenu mp : menuPai) {
			DefaultSubMenu menuPaiAux = mp;

			for (DefaultSubMenu sb : subsubAssSubMenu) {
				if (mp.getIcon().equals(sb.getId())) {

					sb.setIcon(null);
					menuPaiAux.addElement(sb);// esse auxiliar recebe o submenu
					// e depois mais embaixo
					// adiciona ele na lista

				}
			}
			submenuAssSubMenuPai.add(menuPaiAux);
		}

		// Associar menu item com menu pai.
		for (DefaultSubMenu sa : submenuAssSubMenuPai) {
			DefaultSubMenu menuPaiAux = sa;
			// Argemiro
			for (DefaultMenuItem mi : menuItem) {
				if (sa.getIcon().equals(mi.getId())) {
					mi.setIcon(null);
					menuPaiAux.addElement(mi);

				}
			}
			menuPaiAux.setIcon(null);
			menusAssociados.add(menuPaiAux);

		}

		// Preencher menu model.
		for (DefaultSubMenu menuAss : menusAssociados) {
			menuModel.addElement(menuAss);
		}

		// Gerar menu fale conosco.
		DefaultMenuItem item2 = new DefaultMenuItem();
		item2.setValue("Fale Conosco");
		// item2.setUrl("/pages/comum/contato.faces");
		item2.setUrl(root+sistema.getUrl().replace("?faces-redirect=true", ""));
		menuModel.addElement(item2);

		// Gerar menu sistemas.
		DefaultMenuItem item3 = new DefaultMenuItem();
		item3.setValue("Sistemas");
		item3.setUrl(root+"/pages/comum/selecaoSistema.faces");
		menuModel.addElement(item3);

		// Gerar menu sair.
		DefaultMenuItem item4 = new DefaultMenuItem();
		item4.setValue("Sair");
		item4.setCommand("#{MBFuncionarios.logout()}");
		menuModel.addElement(item4);
	}

	public String verificarBolTab(boolean ativo, String tipo) {
		if (ativo == true) {
			return "../../imgs/status_green.png";
		} else if (ativo == false && tipo.equals("injetado")) {
			return "../../imgs/status_yellow.png";
		} else {
			return "../../imgs/status_red.png";
		}
	}

	public static void timeOut() throws IOException {
		if (SessionUtil.getSession() != null) {
			SessionUtil.getSession().invalidate();
			FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("expired", "S");
		} else {
			FacesContext.getCurrentInstance().getExternalContext().redirect("/pages/comum/login.faces");
		}
	}

	public String logout() {
		SessionUtil.getSession().invalidate();
		return "/pages/comum/login.faces?faces-redirect=true";
	}

	// PROFISSIONAL INICIO
	public void limparDados() throws ProjetoException {
		this.profissional = new FuncionarioBean();
		this.listaProfissional = fDao.listarProfissionalAtendimento();
		this.listaGruposEProgramasProfissional = new ArrayList<ProgramaBean>();
		renderizarPermissoes = false;
	}

	public Boolean validaSeTodosOsProgramasPossuemUnidadeAssociada() {
		List<Integer> listaIdUnidadesDaListaDeGruposIhProgramas = retornaIdUnidadesDaListaDeGruposIhProgramaDeFormaUnica();

		if(!listaIdUnidadesDaListaDeGruposIhProgramas.isEmpty() &&
				!listaIdUnidadesDaListaDeGruposIhProgramas.contains(profissional.getUnidade().getId())) {
			JSFUtil.adicionarMensagemErro("A unidade principal"+
					" foi alterada, por favor remova o(s) programa(s)/grupo(o) associados a ela", "");
			return false;
		}

		else if(this.profissional.getListaUnidades().isEmpty() && listaIdUnidadesDaListaDeGruposIhProgramas.size() > 1) {
			JSFUtil.adicionarMensagemErro("Existe Programa/Grupo associado que não tem correlação com as unidades associadas a este funcionário", "");
			return false;
		}

		else {
			for (UnidadeBean unidade : this.profissional.getListaUnidades()) {
				if (!listaIdUnidadesDaListaDeGruposIhProgramas.contains(unidade.getId())) {
					JSFUtil.adicionarMensagemErro(
							"Existe Programa/Grupo associado que não tem correlação com as unidades associadas a este funcionário","");
					return false;
				}
			}
		}
		return true;
	}

	private List<Integer> retornaIdUnidadesDaListaDeGruposIhProgramaDeFormaUnica() {
		List<Integer> idUnidadesDaListaDeGruposIhProgramas = new ArrayList<Integer>();
		for (ProgramaBean grupoIhPrograma : listaGruposEProgramasProfissional) {
			if(!idUnidadesDaListaDeGruposIhProgramas.contains(grupoIhPrograma.getCodUnidade()))
				idUnidadesDaListaDeGruposIhProgramas.add(grupoIhPrograma.getCodUnidade());
		}
		return idUnidadesDaListaDeGruposIhProgramas;
	}

	public void gravarProfissional() throws ProjetoException {

		if (profissional.getRealizaAtendimento() == true && listaGruposEProgramasProfissional.isEmpty()) {
			JSFUtil.adicionarMensagemAdvertencia("Deve ser informado pelo menos um Programa e um Grupo!",
					"Campos obrigatórios!");
			return;
		}

		else if(!validaSeTodosOsProgramasPossuemUnidadeAssociada())
			return;
		
		else if (verificaUnidadePadraoFoiAdicionadaLista(profissional.getUnidade().getId()))
			return;

		else

		if (listaSistemasDual.getTarget().size() == 0) {
			JSFUtil.adicionarMensagemAdvertencia("Deve ser informado pelo menos um Sistema!", "Campo obrigatório!");
		} else {
			if (validaCboProfissional()) {
				List<Integer> listaSis = new ArrayList<>();
				List<Long> permissoes = new ArrayList<>();
				List<Menu> listaMenusAux = listaMenusDual.getTarget();
				List<Funcao> listaFuncoesAux = listaFuncoesDual.getTarget();

				MenuDAO mdao = new MenuDAO();
				List<Menu> menusPerfil = mdao.listarMenusPerfil((profissional.getPerfil().getId()));

				MenuMB mmb = new MenuMB();
				List<Menu> listaFiltrada = mmb.filtrarListaMenu(listaMenusAux);
				List<Menu> listaFiltradaaux = mmb.filtrarListaMenu(listaMenusAux);
				for (Menu mp : menusPerfil) {
					for (Menu mf : listaFiltradaaux) {
						if (mp.getCodigo().equals(mf.getCodigo())) {
							listaFiltrada.remove(mf);
						}
					}
				}

				for (Sistema s : listaSistemasDual.getTarget()) {
					listaSis.add(s.getId());
				}

				PermissaoDAO pmdao = new PermissaoDAO();
				for (Menu m : listaFiltrada) {
					permissoes.add(pmdao.recIdPermissoesMenu(m.getId()));
				}

				for (Funcao f : listaFuncoesAux) {
					permissoes.add(pmdao.recIdPermissoesFuncao(f.getId()));
				}

				profissional.setListaIdPermissoes(permissoes);
				profissional.setListaIdSistemas(listaSis);

				boolean cadastrou = fDao.gravarProfissional(profissional, listaGruposEProgramasProfissional);

				if (cadastrou == true) {
					limparDados();
					JSFUtil.adicionarMensagemSucesso("Profissional cadastrado com sucesso!", "Sucesso");
				} else {
					JSFUtil.adicionarMensagemErro("Ocorreu um erro durante o cadastro!", "Erro");
				}
				this.listaProfissional = fDao.listarProfissionalAtendimento();
			}
		}
	}

	private Boolean validaCboProfissional() {
		if(this.profissional.getRealizaAtendimento() == true) {
			if (VerificadorUtil.verificarSeObjetoNuloOuVazio(this.profissional.getCbo().getCodigo())) {
				JSFUtil.adicionarMensagemAdvertencia("Campo CBO é obrigatório", "");
				return false;
			}
		}
		return true;
	}

	public void excluirProfissional() throws ProjetoException {

		boolean excluiu = fDao.excluirProfissional(profissional);
		if (excluiu == true) {
			JSFUtil.adicionarMensagemSucesso("Profissional excluído com sucesso!", "Sucesso");
			JSFUtil.fecharDialog("dialogExclusao");
		} else {
			JSFUtil.adicionarMensagemErro("Ocorreu um erro durante a exclusão!", "Erro");
			JSFUtil.fecharDialog("dialogExclusao");
		}
		this.listaProfissional = fDao.listarProfissionalAtendimento();
	}

	public void alterarProfissional() throws ProjetoException {

		if (profissional.getRealizaAtendimento() == true && listaGruposEProgramasProfissional.size() == 0) {
			JSFUtil.adicionarMensagemAdvertencia("Deve ser informado pelo menos um Programa e um Grupo!",
					"Campos obrigatórios!");
			return;
		}

		else if(!validaSeTodosOsProgramasPossuemUnidadeAssociada())
			return;
		
		else if (verificaUnidadePadraoFoiAdicionadaLista(profissional.getUnidade().getId()))
			return;
			
		else
		if (listaSistemasDual.getTarget().size() == 0) {
			JSFUtil.adicionarMensagemAdvertencia("Deve ser informado pelo menos um Sistema!", "Campo obrigatório!");
		}
		else {
			if (validaCboProfissional()) {
				List<Integer> listaSis = new ArrayList<>();
				List<Long> permissoes = new ArrayList<>();
				List<Menu> listaMenusAux = new ArrayList<>();
				List<Funcao> listaFuncoesAux = new ArrayList<>();
				listaMenusAux = (tipo == 1) ? listaMenusDual.getTarget() : listaMenusDualEdit.getTarget();
				listaFuncoesAux = (tipo == 1) ? listaFuncoesDual.getTarget() : listaFuncoesDualEdit.getTarget();

				MenuDAO mdao = new MenuDAO();
				List<Menu> menusPerfil = mdao.listarMenusPerfil((profissional.getPerfil().getId()));

				MenuMB mmb = new MenuMB();
				List<Menu> listaFiltrada = mmb.filtrarListaMenu(listaMenusAux);

				List<Menu> listaFiltradaaux = mmb.filtrarListaMenu(listaMenusAux);

				for (Menu mp : menusPerfil) {
					for (Menu mf : listaFiltradaaux) {
						if (mp.getCodigo().equals(mf.getCodigo())) {
							listaFiltrada.remove(mf);
						}
					}
				}

				PermissaoDAO pmdao = new PermissaoDAO();
				for (Menu m : listaFiltrada) {
					if (!permissoes.contains(pmdao.recIdPermissoesMenu(m.getId()))) {
						permissoes.add(pmdao.recIdPermissoesMenu(m.getId()));
					}
				}

				for (Sistema s : listaSistemasDual.getTarget()) {
					listaSis.add(s.getId());
				}

				for (Funcao f : listaFuncoesAux) {
					permissoes.add(pmdao.recIdPermissoesFuncao(f.getId()));
				}

				profissional.setListaIdPermissoes(permissoes);
				profissional.setListaIdSistemas(listaSis);

				boolean alterou = fDao.alterarProfissional(profissional, listaGruposEProgramasProfissional);

				if (alterou == true) {
					JSFUtil.adicionarMensagemSucesso("Funcionário alterado com sucesso!", "Sucesso");

				} else {
					JSFUtil.adicionarMensagemErro("Ocorreu um erro durante a alteração!", "Erro");
				}
			}
		}
	}
	
	private boolean verificaUnidadePadraoFoiAdicionadaLista(Integer IdUnidadePadrao) {
		for (UnidadeBean unidade : profissional.getListaUnidades()) {
			if(unidade.getId().equals(IdUnidadePadrao)) {
				JSFUtil.adicionarMensagemErro("Unidade padrão não pode ser adicionada na lista", "");
				return true;
			}
		}
		return false;
	}

	public void validaCns(String s) {
		if (!DocumentosUtil.validaCNS(s)) {
			JSFUtil.adicionarMensagemAdvertencia("Esse número de CNS não é valido", "ADvertência");
			profissional.setCns("");
		}
	}

	public void listarProfissionais() throws ProjetoException {
		listaProfissional = fDao.listarTodosOsProfissional();
	}

	public void listarProfissionaisConfigAgenda() throws ProjetoException {
		listaProfissional = fDao.listarProfissionalAtendimento();
	}

	public void addListaGruposEProgramasProfissional() {
		Boolean existe = false;
		if (!listaGruposEProgramasProfissional.isEmpty()) {

			for (int i = 0; i < listaGruposEProgramasProfissional.size(); i++) {

				if (listaGruposEProgramasProfissional.get(i).getIdPrograma() == profissional.getProgAdd()
						.getIdPrograma()
						&& (listaGruposEProgramasProfissional.get(i).getGrupoBean().getIdGrupo() == profissional
						.getProgAdd().getGrupoBean().getIdGrupo())) {
					existe = true;

				}
			}
			if (existe == true) {
				JSFUtil.adicionarMensagemAdvertencia("Esse grupo já foi adicionado!", "ADvertência");
			} else {
				listaGruposEProgramasProfissional.add(profissional.getProgAdd());
			}
		} else {
			listaGruposEProgramasProfissional.add(profissional.getProgAdd());
		}
	}

	public void removeListaGruposEProgramasProfissional() {
		listaGruposEProgramasProfissional.remove(profissional.getProgAdd());
	}

	public String getCabecalho() {
		if (this.tipo == 1) {
			cabecalho = CABECALHO_INCLUSAO;
		} else if (this.tipo == 2) {
			cabecalho = CABECALHO_ALTERACAO;
		}
		return cabecalho;
	}

	public String redirectEdit() {
		long num = this.profissional.getId();
		int codigo = (int) num;
		return RedirecionarUtil.redirectEdit(ENDERECO_CADASTRO, ENDERECO_ID, codigo, ENDERECO_TIPO, tipo);
	}

	public String redirectInsert() throws ProjetoException {
		limparDados();
		return "cadastroProfissional?faces-redirect=true&amp;tipo=" + this.tipo;
	}

	public void setCabecalho(String cabecalho) {
		this.cabecalho = cabecalho;
	}

	public List<FuncionarioBean> listaProfissionalAutoComplete(String query) throws ProjetoException {
		List<FuncionarioBean> result = fDao.listarProfissionalBusca(query, 1);
		return result;
	}

	public List<FuncionarioBean> listaTodosProfissionaisAutoComplete(String query) throws ProjetoException {
		List<FuncionarioBean> result = fDao.listarProfissionalBusca(query, 2);
		return result;
	}

	public void rendermedico() {
		if (profissional.getRealizaAtendimento() == true) {
			profissional.setRealizaAtendimento(true);
		}
		else {
			profissional.setEspecialidade(null);
			profissional.setCbo(null);
			profissional.setProc1(null);
			profissional.setCns(null);
		}

	}

	// PROFISSIONAL FIM

	// PROFISSIONAL GETTERS E SETTERS INICIO

	public List<FuncionarioBean> getListaProfissional() {
		return listaProfissional;
	}

	public void getEditProfissional() throws ProjetoException, SQLException {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		Map<String, String> params = facesContext.getExternalContext().getRequestParameterMap();
		if (params.get("id") != null) {
			Integer id = Integer.parseInt(params.get("id"));
			tipo = Integer.parseInt(params.get("tipo"));
			this.profissional = fDao.buscarProfissionalPorId(id);
			profissional.setListaUnidades(fDao.listarUnidadesUsuarioVisualiza(id));
			listaGruposEProgramasProfissional = fDao.carregaProfissionalProgramaEGrupos(id);
			carregaListaSistemasDualAlteracao();
			if (profissional.getPerfil().getId() > 0) {
				renderizarPermissoes = true;
			} else {
				renderizarPermissoes = false;
			}
		} else {
			renderizarPermissoes = false;
			tipo = Integer.parseInt(params.get("tipo"));
			carregaListaSistemasDualInsercao();
		}

	}

	public void contalistafunctarget(){
		System.out.println(this.listaSistemasTarget.size());
	}

	public void onTransferMenu(TransferEvent event) {
		StringBuilder builder = new StringBuilder();

		for (Object item : event.getItems()) {
			builder.append(((Menu) item).getId());
			if (listaMenusTarget.contains(item)) {
				listaMenusTarget.remove(item);
			} else {
				listaMenusTarget.add((Menu) item);
			}
		}
	}

	public void onTransferFuncao(TransferEvent event) {
		StringBuilder builder = new StringBuilder();

		for (Object item : event.getItems()) {
			builder.append(((Funcao) item).getId());
			if (listaFuncoesTarget.contains(item)) {
				listaFuncoesTarget.remove(item);
			} else {
				listaFuncoesTarget.add((Funcao) item);
			}
		}
	}

	public void limparDualCad() {

		if (profissional.getPerfil().getId() > 0) {
			renderizarPermissoes = true;
		} else {
			renderizarPermissoes = false;
		}

		listaMenusDual = null;
		listaMenusTarget = null;
		listaMenusTarget = new ArrayList<>();

		listaFuncoesDual = null;
		listaFuncoesTarget = null;
		listaFuncoesTarget = new ArrayList<>();

	}

	public List<UnidadeBean> listarUnidadesDoFuncionario() throws ProjetoException {
		return fDao.listarUnidadesDoFuncionario();
	}

	public void addUnidadeExtra() throws ProjetoException {
		if (!VerificadorUtil.verificarSeObjetoNuloOuZero(profissional.getUnidadeExtra().getId())) {
			if (profissional.getListaUnidades().isEmpty()) {
				UnidadeBean unidadeBean1 = unidadeDAO.buscarUnidadePorId(profissional.getUnidadeExtra().getId());
				if(!unidadePadraoFoiadicionada())
					profissional.getListaUnidades().add(unidadeBean1);
			} else {
				Boolean existe = false;
				for (int i = 0; i < profissional.getListaUnidades().size(); i++) {
					if (profissional.getListaUnidades().get(i).getId() == profissional.getUnidadeExtra().getId()) {
						existe = true;
					}
				}
				
				if(unidadePadraoFoiadicionada())
					existe = true;
				
				if (existe == false) {
					UnidadeBean unidadeBean1 = unidadeDAO.buscarUnidadePorId(profissional.getUnidadeExtra().getId());
					profissional.getListaUnidades().add(unidadeBean1);
				} else {
					JSFUtil.adicionarMensagemErro("Essa unidade já foi adicionada!", "Erro!");
				}
			}
		} else {
			JSFUtil.adicionarMensagemErro("Escolha uma unidade para adicionar", "Erro!");
		}
	}
	
	private boolean unidadePadraoFoiadicionada() {
		if(profissional.getUnidadeExtra().getId() == profissional.getUnidade().getId()) {
			JSFUtil.adicionarMensagemErro("Unidade padrão já foi adicionada", "");
			return true;
		}
		return false;
	}

	public void removerUnidadeExtra() {
		profissional.getListaUnidades().remove(unidadeBean);
	}

	public void montarListaDeUnidadesDoUsuario() throws ProjetoException {
		listaUnidadesDoUsuario = new ArrayList<>();
		listaUnidadesDoUsuario.add(unidadeDAO.buscarUnidadePorId(profissional.getUnidade().getId()));

		for (int i=0; i<profissional.getListaUnidades().size(); i++){
			listaUnidadesDoUsuario.add(profissional.getListaUnidades().get(i));
		}

	}

	public void acoesAposEscolherUnidadeParaCarregarProgramasIhGrupos(){
		JSFUtil.fecharDialog("dlgUnidadesParaProgramaGrupo");
		JSFUtil.abrirDialog("dlgConsuGrupos");
	}

	public FuncionarioBean getProfissional() {
		return profissional;
	}

	public void setProfissional(FuncionarioBean profissional) {
		this.profissional = profissional;
	}

	public int getTipo() {
		return tipo;
	}

	public void setTipo(int tipo) {
		this.tipo = tipo;
	}

	public void ListarTodosProfissionais() throws ProjetoException {
		listaProfissional = fDao.listarProfissionalAtendimento();

	}

	public void setListaProfissional(List<FuncionarioBean> listaProfissional) {
		this.listaProfissional = listaProfissional;
	}

	public ArrayList<ProgramaBean> getListaGruposEProgramasProfissional() {
		return listaGruposEProgramasProfissional;
	}

	public void setListaGruposEProgramasProfissional(ArrayList<ProgramaBean> listaGruposEProgramasProfissional) {
		this.listaGruposEProgramasProfissional = listaGruposEProgramasProfissional;
	}

	public void abrirDialogAlterarSenha() {
		limparCamposSenha();
		JSFUtil.abrirDialog("dlgAlterarSenha");
		JSFUtil.atualizarComponente("frmAlterarSenha");
	}

	public void abrirDialogTrocarDeUnidade(){
		JSFUtil.abrirDialog("selecaoUnidade");
	}

	public void limparCamposSenha(){
		usuario.setSenha(null);
		usuario.setNovaSenha(null);
		usuario.setConfirmacaoNovaSenha(null);
	}

	public void alterarSenhaFuncionario() throws ProjetoException {

		if (validarSeNovasSenhasSãoIguais()) {

			Integer idFuncionario = fDao.validarIdIhSenha(usuario.getSenha());

			if (idFuncionario > 0) {
				realizarAlteracaoSenha();
			} else {
				JSFUtil.adicionarMensagemErro("Senha errada!", "Erro!");
			}
		} else {
			JSFUtil.adicionarMensagemErro("A nova senha e a confirmação de nova senha estão diferentes!", "Erro");
		}
	}

	private void realizarAlteracaoSenha() throws ProjetoException {
		Boolean alterou = fDao.alterarSenha(usuario);
		if (alterou) {
			JSFUtil.adicionarMensagemSucesso("Senha alterada com sucesso!", "Sucesso!");
			JSFUtil.fecharDialog("dlgAlterarSenha");
			usuario = new FuncionarioBean();
		} else {
			JSFUtil.adicionarMensagemErro("Erro ao alterar a senha!", "Erro!");
		}
	}

	public Boolean validarSeNovasSenhasSãoIguais() {
		boolean retorno = false;

		if (usuario.getNovaSenha().equals(usuario.getConfirmacaoNovaSenha())) {
			retorno = true;
		}

		return retorno;
	}

	// PROFISSIONAL GETTES E SETTERS FIM

	public FuncionarioBean getUsuario() {
		return usuario;
	}

	public void setUsuario(FuncionarioBean usuario) {
		this.usuario = usuario;
	}

	public FuncionarioBean getFuncionario() {
		return usuario;
	}

	// ACL
	public FuncionarioBean getUsuarioLogado() {
		return usuarioLogado;
	}

	public void setUsuarioLogado(FuncionarioBean usuarioLogado) {
		this.usuarioLogado = usuarioLogado;
	}

	public Sistema getSistemaLogado() {
		return sistemaLogado;
	}

	public void setSistemaLogado(Sistema sistemaLogado) {
		this.sistemaLogado = sistemaLogado;
	}

	public List<Sistema> getSistemasUsuarioLogado() {
		return sistemasUsuarioLogado;
	}

	public void setSistemasUsuarioLogado(List<Sistema> sistemasUsuarioLogado) {
		this.sistemasUsuarioLogado = sistemasUsuarioLogado;
	}

	public List<Permissoes> getPermsUsuarioLogado() {
		return permsUsuarioLogado;
	}

	public void setPermsUsuarioLogado(List<Permissoes> permsUsuarioLogado) {
		this.permsUsuarioLogado = permsUsuarioLogado;
	}

	public List<Menu> getListaMenus() {
		return listaMenus;
	}

	public void setListaMenus(List<Menu> listaMenus) {
		this.listaMenus = listaMenus;
	}

	public MenuModel getMenuModel() {
		return menuModel;
	}

	public void setMenuModel(MenuModel menuModel) {
		this.menuModel = menuModel;
	}

	public DualListModel<Menu> getListaMenusDual() throws ProjetoException, SQLException {
		if (listaMenusDual == null) {
			listaMenusSource = null;
			listaMenusTarget = new ArrayList<>();
			getListaMenusSource();
			listaMenusDual = new DualListModel<>(listaMenusSource, listaMenusTarget);
		}
		return listaMenusDual;
	}

	public DualListModel<Menu> getListaMenusDualEdit()
			throws NumberFormatException, ProjetoException {
		//if (listaMenusDualEdit == null) {
		listaMenusSourceEdit = null;
		listaMenusTargetEdit = null;
		getListaMenusSourceEdit();
		getListaMenusTargetEdit();
		listaMenusDualEdit = new DualListModel<>(listaMenusSourceEdit,
				listaMenusTargetEdit);
		//}
		return listaMenusDualEdit;
	}

	public void setListaMenusDual(DualListModel<Menu> listaMenusDual) {
		this.listaMenusDual = listaMenusDual;
	}

	public List<Menu> getListaMenusSource() throws ProjetoException {
		if (listaMenusSource == null) {
			MenuDAO mdao = new MenuDAO();
			listaMenusSource = mdao.listarMenuItemSourcerUser(profissional.getPerfil().getId());
		}
		return listaMenusSource;
	}

	public DualListModel<Funcao> getListaFuncoesDual() throws ProjetoException {
		if (listaFuncoesDual == null) {
			listaFuncoesSource = null;
			listaFuncoesTarget = new ArrayList<>();
			getListaFuncoesSource();
			listaFuncoesDual = new DualListModel<>(listaFuncoesSource, listaFuncoesTarget);
		}
		return listaFuncoesDual;
	}

	public void setListaFuncoesDual(DualListModel<Funcao> listaFuncoesDual) {
		this.listaFuncoesDual = listaFuncoesDual;
	}

	public List<Funcao> getListaFuncoesSource() throws ProjetoException {
		if (listaFuncoesSource == null) {
			FuncaoDAO fdao = new FuncaoDAO();
			listaFuncoesSource = fdao.listarFuncaoItemSourcerUser(profissional.getPerfil().getId());
		}
		return listaFuncoesSource;
	}

	public void setListaMenusDualEdit(DualListModel<Menu> listaMenusDualEdit) {
		this.listaMenusDualEdit = listaMenusDualEdit;
	}

	public List<Menu> getListaMenusSourceEdit() throws NumberFormatException,
			ProjetoException {
		if (listaMenusSourceEdit == null) {
			MenuDAO mdao = new MenuDAO();
			listaMenusSourceEdit = mdao.listarMenuItemSourcerEdit(profissional.getPerfil().getId(), profissional.getId());
		}
		return listaMenusSourceEdit;
	}

	public void setListaMenusSourceEdit(List<Menu> listaMenusSourceEdit) {
		this.listaMenusSourceEdit = listaMenusSourceEdit;
	}

	public List<Menu> getListaMenusTargetEdit() throws NumberFormatException,
			ProjetoException {
		if (listaMenusTargetEdit == null) {
			MenuDAO mdao = new MenuDAO();
			listaMenusTargetEdit = mdao.listarMenuItemTargetEdit(profissional.getId().intValue());
		}
		return listaMenusTargetEdit;
	}

	public void setListaMenusTargetEdit(List<Menu> listaMenusTargetEdit) {
		this.listaMenusTargetEdit = listaMenusTargetEdit;
	}

	public DualListModel<Funcao> getListaFuncoesDualEdit()
			throws NumberFormatException, ProjetoException {
		if (listaFuncoesDualEdit == null) {
			listaFuncoesSourceEdit = null;
			listaFuncoesTargetEdit = null;
			getListaFuncoesSourceEdit();
			getListaFuncoesTargetEdit();
			listaFuncoesDualEdit = new DualListModel<>(listaFuncoesSourceEdit,
					listaFuncoesTargetEdit);
		}
		return listaFuncoesDualEdit;
	}

	public void setListaFuncoesDualEdit(
			DualListModel<Funcao> listaFuncoesDualEdit) {
		this.listaFuncoesDualEdit = listaFuncoesDualEdit;
	}

	public List<Funcao> getListaFuncoesSourceEdit()
			throws NumberFormatException, ProjetoException {
		if (listaFuncoesSourceEdit == null) {
			FuncaoDAO fdao = new FuncaoDAO();
			listaFuncoesSourceEdit = fdao.listarFuncoesSourceEdit(profissional.getPerfil().getId());
		}
		return listaFuncoesSourceEdit;
	}

	public void setListaFuncoesSourceEdit(List<Funcao> listaFuncoesSourceEdit) {
		this.listaFuncoesSourceEdit = listaFuncoesSourceEdit;
	}

	public List<Funcao> getListaFuncoesTargetEdit()
			throws NumberFormatException, ProjetoException {
		if (listaFuncoesTargetEdit == null) {
			FuncaoDAO fdao = new FuncaoDAO();
			listaFuncoesTargetEdit = fdao.listarFuncoesTargetEdit(profissional.getPerfil().getId());
		}
		return listaFuncoesTargetEdit;
	}

	public void setListaFuncoesTargetEdit(List<Funcao> listaFuncoesTargetEdit) {
		this.listaFuncoesTargetEdit = listaFuncoesTargetEdit;
	}
	
	public void atribuiCoresTabelaAtendimentoProfissionalNaEquipe() {
		if(usuarioLogado.getUnidade().getParametro().isAtribuirCorTabelaTelaEvolucaoProfissional()) {
			this.corEvoluido = VERDE;
			this.corNaoEvoluido = VERMELHO;
		}
		else {
			this.corEvoluido = TRANSPARENTE; 
			this.corNaoEvoluido = TRANSPARENTE;		
		}
	}

	public void setListaFuncoesSource(List<Funcao> listaFuncoesSource) {
		this.listaFuncoesSource = listaFuncoesSource;
	}

	public List<Funcao> getListaFuncoesTarget() {
		return listaFuncoesTarget;
	}

	public void setListaFuncoesTarget(List<Funcao> listaFuncoesTarget) {
		this.listaFuncoesTarget = listaFuncoesTarget;
	}

	public void setListaMenusSource(List<Menu> listaMenusSource) {
		this.listaMenusSource = listaMenusSource;
	}

	public List<Menu> getListaMenusTarget() {
		return listaMenusTarget;
	}

	public void setListaMenusTarget(List<Menu> listaMenusTarget) {
		this.listaMenusTarget = listaMenusTarget;
	}

	public Boolean getRenderizarPermissoes() {
		return renderizarPermissoes;
	}

	public void setRenderizarPermissoes(Boolean renderizarPermissoes) {
		this.renderizarPermissoes = renderizarPermissoes;
	}

	public UnidadeBean getUnidadeBean() {
		return unidadeBean;
	}

	public void setUnidadeBean(UnidadeBean unidadeBean) {
		this.unidadeBean = unidadeBean;
	}

	public FuncionarioDAO getfDao() {
		return fDao;
	}

	public void setfDao(FuncionarioDAO fDao) {
		this.fDao = fDao;
	}

	public List<Permissoes> getListaPerms() {
		return listaPerms;
	}

	public void setListaPerms(List<Permissoes> listaPerms) {
		this.listaPerms = listaPerms;
	}

	public List<Sistema> listaSistemasSoucerNaInclusao() throws ProjetoException {
		if (listaSistemasSoucer == null) {
			FuncionarioDAO udao = new FuncionarioDAO();
			listaSistemasSoucer = udao.carregaListaSistemasSoucerInclusao();
		}
		return listaSistemasSoucer;
	}

	public List<Sistema> listaSistemasSoucerNaAlteracao() throws ProjetoException {
		FuncionarioDAO udao = new FuncionarioDAO();
		listaSistemasSoucer = udao.carregaSistemasListaSourceAlteracao(profissional.getId());
		return listaSistemasSoucer;
	}

	public List<Sistema> listaSistemasTargetNaAlteracao() throws ProjetoException {
		FuncionarioDAO udao = new FuncionarioDAO();
		listaSistemasTarget = udao.carregaListaSistemasTargetAlteracao(profissional.getId());
		return listaSistemasTarget;
	}

	public void setListaSistemasSoucer(List<Sistema> listaSistemasSoucer) {
		this.listaSistemasSoucer = listaSistemasSoucer;
	}

	public void setListaSistemasTarget(List<Sistema> listaSistemasTarget) {
		this.listaSistemasTarget = listaSistemasTarget;
	}

	public List<Integer> getCodigoSistema() {
		return codigoSistema;
	}

	public void setCodigoSistema(List<Integer> codigoSistema) {
		this.codigoSistema = codigoSistema;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public static String getEnderecoCadastro() {
		return ENDERECO_CADASTRO;
	}

	public static String getEnderecoTipo() {
		return ENDERECO_TIPO;
	}

	public static String getEnderecoId() {
		return ENDERECO_ID;
	}

	public static String getCabecalhoInclusao() {
		return CABECALHO_INCLUSAO;
	}

	public static String getCabecalhoAlteracao() {
		return CABECALHO_ALTERACAO;
	}

	public DualListModel<Sistema> getListaSistemasDual() {
		return listaSistemasDual;
	}

	public List<Sistema> getListaSistemasSoucer() {
		return listaSistemasSoucer;
	}

	public List<Sistema> getListaSistemasTarget() {
		return listaSistemasTarget;
	}

	public AtalhosAmbulatorialDTO getAtalhosAmbulatorialDTO() {
		return atalhosAmbulatorialDTO;
	}

	public void setAtalhosAmbulatorialDTO(AtalhosAmbulatorialDTO atalhosAmbulatorialDTO) {
		this.atalhosAmbulatorialDTO = atalhosAmbulatorialDTO;
	}
	public List<UnidadeBean> getUnidadesDoUsuarioLogado() {
		return unidadesDoUsuarioLogado;
	}

	public void setUnidadesDoUsuarioLogado(List<UnidadeBean> unidadesDoUsuarioLogado) {
		this.unidadesDoUsuarioLogado = unidadesDoUsuarioLogado;
	}

	public Integer getCodigoDaUnidadeSelecionada() {
		return codigoDaUnidadeSelecionada;
	}

	public void setCodigoDaUnidadeSelecionada(Integer codigoDaUnidadeSelecionada) {
		this.codigoDaUnidadeSelecionada = codigoDaUnidadeSelecionada;
	}

	public String retornaTextoDaUnidadeAtual() throws ProjetoException{
		return fDao.retornaNomeDaUnidadeAtual(usuarioLogado.getCodigoDaUnidadeSelecionada());
	}

	public List<Empresa> getListaEmpresaFuncionario() {
		return listaEmpresaFuncionario;
	}

	public void setListaEmpresaFuncionario(List<Empresa> listaEmpresaFuncionario) {
		this.listaEmpresaFuncionario = listaEmpresaFuncionario;
	}

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	public List<UnidadeBean> getListaUnidadesDoUsuario() {
		return listaUnidadesDoUsuario;
	}

	public void setListaUnidadesDoUsuario(List<UnidadeBean> listaUnidadesDoUsuario) {
		this.listaUnidadesDoUsuario = listaUnidadesDoUsuario;
	}

	public UnidadeBean getUnidadeParaProgramasIhGrupos() {
		return unidadeParaProgramasIhGrupos;
	}

	public void setUnidadeParaProgramasIhGrupos(UnidadeBean unidadeParaProgramasIhGrupos) {
		this.unidadeParaProgramasIhGrupos = unidadeParaProgramasIhGrupos;
	}

	public String getCorEvoluido() {
		return corEvoluido;
	}

	public void setCorEvoluido(String corEvoluido) {
		this.corEvoluido = corEvoluido;
	}

	public String getCorNaoEvoluido() {
		return corNaoEvoluido;
	}

	public void setCorNaoEvoluido(String corNaoEvoluido) {
		this.corNaoEvoluido = corNaoEvoluido;
	}
}