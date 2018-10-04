package br.gov.al.maceio.sishosp.acl.control;

import br.gov.al.maceio.sishosp.acl.dao.FuncaoDAO;
import br.gov.al.maceio.sishosp.acl.dao.PermissaoDAO;
import br.gov.al.maceio.sishosp.acl.model.*;

import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.SessionUtil;
import br.gov.al.maceio.sishosp.hosp.model.ProgramaBean;
import br.gov.al.maceio.sishosp.acl.dao.MenuDAO;
import br.gov.al.maceio.sishosp.acl.dao.FuncionarioDAO;

import org.primefaces.context.RequestContext;
import org.primefaces.event.TransferEvent;
import org.primefaces.model.DualListModel;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.DefaultSubMenu;
import org.primefaces.model.menu.MenuModel;

@ManagedBean(name = "MBFuncionarios")
@SessionScoped
public class FuncionarioController implements Serializable {

    private static final long serialVersionUID = 1L;
    private FuncionarioBean usuario;
    private FuncionarioBean profissional;
    private List<FuncionarioBean> listaProfissional;
    private FuncionarioDAO pDao = new FuncionarioDAO();
    private String cabecalho;
    private int tipo;
    private ArrayList<ProgramaBean> listaGruposEProgramasProfissional;

    // Sessão
    private FuncionarioBean usuarioLogado;
    private Sistema sistemaLogado;

    private List<Sistema> sistemasUsuarioLogado;
    private List<Permissoes> permsUsuarioLogado;
    private List<Menu> listaMenus;

    private List<Permissoes> listaPerms;

    private DualListModel<Menu> listaMenusDual;
    private List<Menu> listaMenusSource;
    private List<Menu> listaMenusTarget;

    private DualListModel<Funcao> listaFuncoesDual;
    private List<Funcao> listaFuncoesSource;
    private List<Funcao> listaFuncoesTarget;

    // Menu
    private MenuModel menuModel;

    public FuncionarioController() {

        // Profissional
        listaProfissional = new ArrayList<FuncionarioBean>();
        this.profissional = new FuncionarioBean();
        this.listaGruposEProgramasProfissional = new ArrayList<ProgramaBean>();

        usuario = new FuncionarioBean();

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

        listaFuncoesDual = null;
        listaFuncoesSource = new ArrayList<>();
        listaFuncoesTarget = new ArrayList<>();
    }

    public boolean verificarPermComp(String codigo, Integer idSistema) {
        boolean valida = false;
        for (Permissoes perms : permsUsuarioLogado) {
            if (perms.getIdSistemaFunc() != null
                    && perms.getCodigoFuncao() != null) {
                if (perms.getIdSistemaFunc().equals(idSistema)
                        && perms.getCodigoFuncao().equals(codigo)) {
                    valida = true;
                }
            }
        }
        return valida;
    }

    public String login() throws ProjetoException {
        FuncionarioDAO udao = new FuncionarioDAO();

        FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
                .put("expired", "N");

        usuario.setAtivo(udao.usuarioAtivo(usuario));

        usuarioLogado = udao.autenticarUsuario(usuario);

        if (usuarioLogado == null) {

            FacesMessage msg = new FacesMessage("Usuário ou senha inválido!");
            FacesContext.getCurrentInstance().addMessage("Error", msg);
            RequestContext.getCurrentInstance().update("msgError");
            return null;

        } else {

            FacesContext.getCurrentInstance().getExternalContext()
                    .getSessionMap().put("obj_usuario", usuarioLogado);

            // ACL =============================================================

            List<Sistema> sistemas = udao
                    .carregarSistemasUsuario(usuarioLogado);

            FacesContext.getCurrentInstance().getExternalContext()
                    .getSessionMap().put("perms_usuario_sis", sistemas);

            List<Permissoes> permissoes = udao
                    .carregarPermissoes(usuarioLogado);

            sistemaLogado.setDescricao("Sem Sistema");
            sistemaLogado.setSigla("Sem Sistema");
            FacesContext.getCurrentInstance().getExternalContext()
                    .getSessionMap().put("sistema_logado", sistemaLogado);

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

            FacesContext.getCurrentInstance().getExternalContext()
                    .getSessionMap().put("perms_usuario", permissoes);

            HttpSession session = SessionUtil.getSession();
            session.setAttribute("User", usuarioLogado.getId());

            recoverDataFromSession();

            String url = "/pages/comum/selecaoSistema.faces?faces-redirect=true";

            return url;

        }
    }

    public String redirecionar(String url) {
        gerarMenus(sistemaLogado);
        return url;
    }

    public void recSistemaLogado(Sistema sistema) {
        sistemaLogado = sistema;
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
                .put("sistema_logado", sistema);
    }

    public void recoverDataFromSession() {
        usuarioLogado = (FuncionarioBean) FacesContext.getCurrentInstance()
                .getExternalContext().getSessionMap().get("obj_usuario");

        sistemasUsuarioLogado = (List<Sistema>) FacesContext
                .getCurrentInstance().getExternalContext().getSessionMap()
                .get("perms_usuario_sis");

        permsUsuarioLogado = (List<Permissoes>) FacesContext
                .getCurrentInstance().getExternalContext().getSessionMap()
                .get("perms_usuario");

    }

    private void limparMenuModel() {
        menuModel = null;
        menuModel = new DefaultMenuModel();
    }

    public void gerarMenus(Sistema sistema) {

        limparMenuModel();
        List<DefaultSubMenu> menuPai = new ArrayList<>();
        List<DefaultSubMenu> submenu = new ArrayList<>();
        // List<DefaultSubMenu> submenu2 = new ArrayList<>();
        List<DefaultMenuItem> menuItem = new ArrayList<>();
        List<DefaultSubMenu> subsubAssSubMenu = new ArrayList<>();
        List<DefaultSubMenu> menuItemAssSubmenu = new ArrayList<>();
        List<DefaultSubMenu> submenuAssSubMenuPai = new ArrayList<>();
        List<DefaultSubMenu> menusAssociados = new ArrayList<>();

        // Gerar menu início.
        DefaultMenuItem item1 = new DefaultMenuItem();
        item1.setValue("Início");
        item1.setUrl(sistema.getUrl().replace("?faces-redirect=true", ""));
        menuModel.addElement(item1);

        for (Permissoes p : permsUsuarioLogado) {
            // Gerar menu pai.
            if ((p.getMenu().getIndice() == null)
                    && (p.getMenu().getUrl() == null)
                    && (p.getMenu().getTipo().equals("menuPai"))
                    && (p.getIdSistema().equals(sistema.getId()))
                    && (p.getMenu().isAtivo() == true)) {

                DefaultSubMenu mp = new DefaultSubMenu();
                mp.setLabel(p.getMenu().getDescricao());// o nome(descri��o) que
                // vai aparecer na
                // interface
                mp.setIcon(p.getMenu().getCodigo()); // faz a associa��o em que
                // menu ele vai ficar
                menuPai.add(mp); // adiciona o menu
            }

            // Gerar submenu.
            if ((p.getMenu().getIndice() != null)
                    && (p.getMenu().getUrl() == null)
                    && (p.getMenu().getTipo().equals("submenu")) // compara o
                    // tipo para
                    // verificar
                    // se �
                    // submenu
                    && (p.getIdSistema().equals(sistema.getId())) // compara os
                    // ids
                    && (p.getMenu().isAtivo() == true)) { // se for ativo

                DefaultSubMenu sb = new DefaultSubMenu();// inicia a cria��o do
                // submenu
                sb.setLabel(p.getMenu().getDescricao());
                sb.setIcon(p.getMenu().getCodigo());
                sb.setId(p.getMenu().getIndice());
                submenu.add(sb);// cria o submenu
            }

            // Gerar menu item com url.
            if ((p.getMenu().getIndice() != null)
                    && (p.getMenu().getUrl() != null)
                    && (p.getMenu().getTipo().equals("menuItem"))// compara o
                    // tipo
                    && (p.getIdSistema().equals(sistema.getId()))
                    && (p.getMenu().isAtivo() == true)) {
                Menu mi = p.getMenu();
                DefaultMenuItem item = new DefaultMenuItem();
                item.setValue(mi.getDescricao());
                item.setUrl(mi.getUrl());
                item.setIcon(p.getMenu().getCodigo());// coloca o submenu dentro
                // do menu
                item.setId(mi.getIndice());
                menuItem.add(item);
            }

            // Gerar menu item com action/onclick.
            if ((p.getMenu().getIndice() != null)
                    && (p.getMenu().getUrl() == null)
                    && (p.getMenu().getTipo().equals("menuItemRel"))
                    && (p.getIdSistema().equals(sistema.getId()))
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
        item2.setUrl(sistema.getUrl().replace("?faces-redirect=true", ""));
        menuModel.addElement(item2);

        // Gerar menu sistemas.
        DefaultMenuItem item3 = new DefaultMenuItem();
        item3.setValue("Sistemas");
        item3.setUrl("/pages/comum/selecaoSistema.faces");
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
            FacesContext.getCurrentInstance().getExternalContext()
                    .getSessionMap().put("expired", "S");
        } else {
            FacesContext.getCurrentInstance().getExternalContext()
                    .redirect("/pages/comum/login.faces");
        }
    }

    public String logout() {
        SessionUtil.getSession().invalidate();
        return "/pages/comum/login.faces?faces-redirect=true";
    }

    // PROFISSIONAL INÍCIO
    public void limparDados() throws ProjetoException {
        this.profissional = new FuncionarioBean();
        this.listaProfissional = pDao.listarProfissional();
        this.listaGruposEProgramasProfissional = new ArrayList<ProgramaBean>();
    }

    public void gravarProfissional() throws SQLException, ProjetoException {

        if (profissional.getRealizaAtendimento()) {
            if (listaGruposEProgramasProfissional.size() == 0) {
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        "Deve ser informado pelo menos um Programa e um Grupo!",
                        "Campos obrigatórios!");
                FacesContext.getCurrentInstance().addMessage(null, msg);
            }
        }

        List<Long> permissoes = new ArrayList<>();
        List<Menu> listaMenusAux = listaMenusDual.getTarget();
        List<Funcao> listaFuncoesAux = listaFuncoesDual.getTarget();

        MenuDAO mdao = new MenuDAO();
        List<Menu> menusPerfil = mdao.listarMenusPerfil((profissional.getPerfil().getId()));

        MenuMB mmb = new MenuMB();
        List<Menu> listaFiltrada = mmb.filtrarListaMenu(listaMenusAux);

        for (Menu mp : menusPerfil) {
            for (Menu mf : listaFiltrada) {
                if (mp.getCodigo().equals(mf.getCodigo())) {
                    listaFiltrada.remove(mf);
                }
            }
        }

        PermissaoDAO pmdao = new PermissaoDAO();
        for (Menu m : listaFiltrada) {
            permissoes.add(pmdao.recIdPermissoesMenu(m.getId()));
        }

        for (Funcao f : listaFuncoesAux) {
            permissoes.add(pmdao.recIdPermissoesFuncao(f.getId()));
        }

        profissional.setListaIdPermissoes(permissoes);


        boolean cadastrou = pDao.gravarProfissional(profissional,
                listaGruposEProgramasProfissional);

        if (cadastrou == true) {
            limparDados();
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Profissional cadastrado com sucesso!", "Sucesso");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } else {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Ocorreu um erro durante o cadastro!", "Erro");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
        this.listaProfissional = pDao.listarProfissional();
    }

    public void excluirProfissional() throws ProjetoException {
        boolean ok = pDao.excluirProfissional(profissional);
        if (ok == true) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Funcionário excluído com sucesso!", "Sucesso");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().execute(
                    "PF('dialogAtencao').hide();");
        } else {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Ocorreu um erro durante a exclusao!", "Erro");
            FacesContext.getCurrentInstance().addMessage(null, msg);

            RequestContext.getCurrentInstance().execute(
                    "PF('dialogAtencao').hide();");
        }
        this.listaProfissional = pDao.listarProfissional();
    }

    public void alterarProfissional() throws ProjetoException {
        if (this.profissional.getCbo() == null
                // || this.profissional.getCns().isEmpty()
                || this.profissional.getNome().isEmpty()
            // || this.profissional.getEspecialidade() == null
        ) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "CBO, CNS, especialidade e descrição obrigatórios!",
                    "Campos obrigatórios!");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            // return "";
        }

        if (profissional.getRealizaAtendimento() == true
                && listaGruposEProgramasProfissional.size() == 0) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Deve ser informado pelo menos um Programa e um Grupo!",
                    "Campos obrigatórios!");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }

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
        listaFiltrada = listaFiltradaaux;
        PermissaoDAO pmdao = new PermissaoDAO();
        for (Menu m : listaFiltradaaux) {
            permissoes.add(pmdao.recIdPermissoesMenu(m.getId()));
        }

        for (Funcao f : listaFuncoesAux) {
            permissoes.add(pmdao.recIdPermissoesFuncao(f.getId()));
        }

        profissional.setListaIdPermissoes(permissoes);

        boolean alterou = pDao.alterarProfissional(profissional,
                listaGruposEProgramasProfissional);

        if (alterou == true) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Funcionário alterado com sucesso!", "Sucesso");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            this.listaProfissional = pDao.listarProfissional();
            // return
            // "/pages/sishosp/gerenciarProfissional.faces?faces-redirect=true";
        } else {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Ocorreu um erro durante o cadastro!", "Erro");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            // return "";
        }

    }

    public void listarProfissionais() throws ProjetoException {
        FuncionarioDAO prDao = new FuncionarioDAO();
        listaProfissional = prDao.listarProfissional();

    }

    public List<FuncionarioBean> listarProfissionaisConfigAgenda()
            throws ProjetoException {
        FuncionarioDAO prDao = new FuncionarioDAO();
        listaProfissional = prDao.listarProfissional();

        return listaProfissional;
    }

    public void addListaGruposEProgramasProfissional() {
        Boolean existe = false;
        if (listaGruposEProgramasProfissional.size() >= 1) {

            for (int i = 0; i < listaGruposEProgramasProfissional.size(); i++) {

                if (listaGruposEProgramasProfissional.get(i).getIdPrograma() == profissional
                        .getProgAdd().getIdPrograma()
                        && (listaGruposEProgramasProfissional.get(i)
                        .getGrupoBean().getIdGrupo() == profissional
                        .getProgAdd().getGrupoBean().getIdGrupo())) {
                    existe = true;

                }
            }
            if (existe == true) {
                FacesMessage msg = new FacesMessage(
                        FacesMessage.SEVERITY_ERROR,
                        "Esse grupo já foi adicionado!", "Erro");
                FacesContext.getCurrentInstance().addMessage(null, msg);
            } else {
                listaGruposEProgramasProfissional
                        .add(profissional.getProgAdd());
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
            cabecalho = "Cadastro de Funcionário";
        } else if (this.tipo == 2) {
            cabecalho = "Alterar Funcionário";
        }
        return cabecalho;
    }

    public String redirectInsert() throws ProjetoException {
        limparDados();
        return "cadastroProfissional?faces-redirect=true&amp;tipo=" + this.tipo;
    }

    public String redirectEdit() {
        return "cadastroProfissional?faces-redirect=true&amp;id="
                + this.profissional.getId() + "&amp;tipo=" + tipo;
    }

    public void setCabecalho(String cabecalho) {
        this.cabecalho = cabecalho;
    }

    public List<FuncionarioBean> listaProfissionalAutoComplete(String query)
            throws ProjetoException {
        List<FuncionarioBean> result = pDao.listarProfissionalBusca(query, 1);
        return result;
    }

    public void rendermedico() {
        if (profissional.getRealizaAtendimento() == true) {
            profissional.setRealizaAtendimento(true);
        }

    }

    // PROFISSIONAL FIM

    // PROFISSIONAL GETTERS E SETTERS INÍCIO

    public List<FuncionarioBean> getListaProfissional() {
        return listaProfissional;
    }

    public void getEditProfissional() throws ProjetoException, SQLException {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Map<String, String> params = facesContext.getExternalContext()
                .getRequestParameterMap();
        if (params.get("id") != null) {
            Integer id = Integer.parseInt(params.get("id"));
            tipo = Integer.parseInt(params.get("tipo"));
            this.profissional = pDao.buscarProfissionalPorId(id);
            listaGruposEProgramasProfissional = pDao
                    .carregaProfissionalProgramaEGrupos(id);
        } else {
            tipo = Integer.parseInt(params.get("tipo"));

        }

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
        listaMenusDual = null;
        listaMenusTarget = null;
        listaMenusTarget = new ArrayList<>();

        listaFuncoesDual = null;
        listaFuncoesTarget = null;
        listaFuncoesTarget = new ArrayList<>();

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
        listaProfissional = pDao.listarProfissional();

    }

    public void setListaProfissional(List<FuncionarioBean> listaProfissional) {
        this.listaProfissional = listaProfissional;
    }

    public ArrayList<ProgramaBean> getListaGruposEProgramasProfissional() {
        return listaGruposEProgramasProfissional;
    }

    public void setListaGruposEProgramasProfissional(
            ArrayList<ProgramaBean> listaGruposEProgramasProfissional) {
        this.listaGruposEProgramasProfissional = listaGruposEProgramasProfissional;
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

    public DualListModel<Menu> getListaMenusDual() throws ProjetoException {
        if (listaMenusDual == null) {
            listaMenusSource = null;
            listaMenusTarget = new ArrayList<>();
            getListaMenusSource();
            listaMenusDual = new DualListModel<>(listaMenusSource, listaMenusTarget);
        }
        return listaMenusDual;
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

}