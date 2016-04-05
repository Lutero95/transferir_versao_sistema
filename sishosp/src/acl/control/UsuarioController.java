package br.gov.al.maceio.sishosp.acl.control;

import br.gov.al.maceio.sishosp.acl.model.Menu;
import br.gov.al.maceio.sishosp.acl.model.Permissoes;
import br.gov.al.maceio.sishosp.acl.model.Sistema;

import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIData;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpSession;

import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.SessionUtil;
import br.gov.al.maceio.sishosp.acl.dao.IUsuarioDAO;
import br.gov.al.maceio.sishosp.acl.dao.UsuarioDAO;
import br.gov.al.maceio.sishosp.acl.model.UsuarioBean;

import javax.faces.component.EditableValueHolder;

import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.DefaultSubMenu;
import org.primefaces.model.menu.MenuModel;

/**
 *
 * @author Emerson Gama & Jerônimo do Nascimento & Arthur Alves
 * @since 26/03/2015
 */
public class UsuarioController {

    private UsuarioBean usuario;
    private UsuarioBean novousuario, editausuario;
    private UIData id_tupla;
    private List listaUserSecretaria;
    private Object objeto1, menuAdm, nomeUserLogado, permissaoInteressado;
    private Object nomeSecretaria, nomeSetor;
    public String DescSetor, permissaoProcessos;
    private UIInput senhaInput;
    private String timeSession;
    private String visibleExpirationMsg;
    
    // ACL =====================================================================
    
    // Sessão
    private UsuarioBean usuarioLogado;
    private Sistema sistemaLogado;
    
    private List<Sistema>sistemasUsuarioLogado;
    private List<Permissoes> permsUsuarioLogado;
    private List<Menu> listaMenus;
    
    private List<Permissoes> listaPerms;
    
    private Integer idSisAux;
    private String descricaoSisAux;
    private String siglaSisAux;
    private String imagemSisAux;
    private String urlSisAux;
    private String versaoSisAux;
    private boolean ativoSisAux;
    
    private String setoresUsuarioLogado;
    
    // Menu
    private MenuModel menuModel;
    
    // SETORES
    private Boolean rendDlgSetores;
    
    // =========================================================================
    
    public UsuarioController() {
        usuario = new UsuarioBean();
        novousuario = new UsuarioBean();
        editausuario = new UsuarioBean();
        visibleExpirationMsg = "N";
             
        // ACL
        usuarioLogado = new UsuarioBean();
        permsUsuarioLogado = new ArrayList<>();       
        sistemasUsuarioLogado = new ArrayList<>();
        sistemaLogado = new Sistema();       
        listaMenus = new ArrayList<>();     
        menuModel = new DefaultMenuModel();
        listaPerms = new ArrayList<>();
        
        rendDlgSetores = false;
    }
    
    public String verificarTamSetores(String setores) {
        
        Integer tamanho = setores.length();
        System.out.println("TAMANHO STRING: " + tamanho);
        
        String setoresAux = "";
        
        if(tamanho > 60) {
            String[] caracteres = setores.split("");
            System.out.println("TAMANHO ARRAY: " + caracteres.length);
            rendDlgSetores = true;
            
            for(int i = 0; i <= 60; i++) {
                setoresAux += caracteres[i];
            }
        } else {
            rendDlgSetores = false;
            setoresAux = setores;
        }
        return setoresAux;
    }

    public void idleListener() {
        FacesContext.getCurrentInstance().addMessage(
                null,
                new FacesMessage(FacesMessage.SEVERITY_WARN,
                        "Your session is closed",
                        "You have been idle for at least 5 seconds"));

        // invalidate session
    }

    public void activeListener() {
        FacesContext.getCurrentInstance().addMessage(
                null,
                new FacesMessage(FacesMessage.SEVERITY_WARN, "Welcome Back",
                        "That's a long coffee break!"));
    }

    public void verificaUserCadastrado(FacesContext context,
            UIComponent toValidate, Object value) throws ProjetoException {

        IUsuarioDAO icdao = new UsuarioDAO();
        String cpf = (String) value;

        String isExist = icdao.verificaUserCadastrado(cpf);

        if (isExist == "S") {

            ((EditableValueHolder) toValidate).setValid(false);
            FacesMessage message = new FacesMessage(
                    " Usuario ja Cadastrado para este CPF!");
            context.addMessage(toValidate.getClientId(context), message);

        }
    }

    public void verificaLoginCadastrado(FacesContext context,
            UIComponent toValidate, Object value) throws ProjetoException {
        IUsuarioDAO icdao = new UsuarioDAO();
        String login = (String) value;

        String isExist = icdao.verificaLoginCadastrado(login);

        if (isExist == "S") {
            ((EditableValueHolder) toValidate).setValid(false);
            FacesMessage message = new FacesMessage(" Login já em uso");
            context.addMessage(toValidate.getClientId(context), message);

        }
    }

    // ** controle de usuário -----------------------
    public boolean verificarProtocolar() {

        UsuarioBean user_session = (UsuarioBean) FacesContext
                .getCurrentInstance().getExternalContext().getSessionMap()
                .get("obj_usuario");

        return user_session.getPermissao().getProtocolar();
    }

    public boolean verificarEditarNatureza() {

        UsuarioBean user_session = (UsuarioBean) FacesContext
                .getCurrentInstance().getExternalContext().getSessionMap()
                .get("obj_usuario");
        
        return user_session.getPermissao().getAlterarNatureza();
    }

    public boolean verificarTramite() {

        UsuarioBean user_session = (UsuarioBean) FacesContext
                .getCurrentInstance().getExternalContext().getSessionMap()
                .get("obj_usuario");

        return user_session.getPermissao().getTramitar();

    }

    public boolean verificarManterProcesso() {

        UsuarioBean user_session = (UsuarioBean) FacesContext
                .getCurrentInstance().getExternalContext().getSessionMap()
                .get("obj_usuario");

        return user_session.getPermissao().getAlterarProtocolo();

    }

    public boolean adm() {

        UsuarioBean user_session = (UsuarioBean) FacesContext
                .getCurrentInstance().getExternalContext().getSessionMap()
                .get("obj_usuario");

        return user_session.isAdministrador();

    }

    // **FIM controle de usuário -----------------------
    public void validateSenha(FacesContext context, UIComponent toValidate,
            Object value) {

        String confirmaSenha = (String) value;
        String senha = (String) this.senhaInput.getLocalValue();

        if (senha != null) {
            if (!confirmaSenha.equals(senha)) {
                ((EditableValueHolder) toValidate).setValid(false);

                FacesMessage message = new FacesMessage(
                        "As senhas digitadas diferem.");
                context.addMessage(toValidate.getClientId(context), message);
            }
        }
    }

    public String ultimoDiaMes(Integer Ano, Integer Mes) {
        Calendar cal = new GregorianCalendar(Ano, Mes - 1, 1);
        return toString().valueOf(cal.getActualMaximum(Calendar.DAY_OF_MONTH));
    }

    public String login() throws ProjetoException {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("expired", "N");
        permissaoProcessos = "false";

        UsuarioBean user = null;
        UsuarioDAO udao = new UsuarioDAO();
        user = udao.autenticarUsuario(usuario);

        if(user == null) {
            FacesContext fct = FacesContext.getCurrentInstance();
            fct.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                "Usuário ou senha inválidos!", "Erro"));

            return "";
        } else {
            nomeSetor = DescSetor;           
            
            // ACL =============================================================
            
            FacesContext.getCurrentInstance().getExternalContext()
                .getSessionMap().put("obj_usuario", user);
            
            List<Sistema> sistemas = udao.carregarSistemasUsuario(user);
            
            FacesContext.getCurrentInstance().getExternalContext()
                .getSessionMap().put("perms_usuario_sis", sistemas);
            
            List<Permissoes> permissoes = udao.carregarPermissoes(user);
            
            sistemaLogado.setDescricao("Sem Sistema");
            sistemaLogado.setSigla("Sem Sistema");
            FacesContext.getCurrentInstance().getExternalContext()
                .getSessionMap().put("sistema_logado", sistemaLogado);
            
            for(Sistema s : sistemas) {
                Menu m = new Menu();
                m.setDescricao("Principal");
                m.setUrl(s.getUrl());
                m.setTipo("injetado");
                Permissoes perms = new Permissoes();
                perms.setMenu(m);
                permissoes.add(perms);
            }
            
            Permissoes perms = new Permissoes();
            Menu m2 = new Menu();
            m2.setDescricao("Fale Conosco");
            m2.setUrl("/pages/comum/contato.faces");
            m2.setTipo("injetado");
            perms.setMenu(m2);
            permissoes.add(perms);
            
            Permissoes perms2 = new Permissoes();            
            Menu m3 = new Menu();
            m3.setDescricao("Primeiro Acesso");
            m3.setUrl("/pages/comum/primeiroAcesso.faces");
            m3.setTipo("injetado");
            perms2.setMenu(m3);
            permissoes.add(perms2);
            
            /*Permissoes perms2 = new Permissoes();            
            Menu m = new Menu();
            m.setDescricao("Primeiro Acesso");
            m.setUrl("/pages/comum/primeiroAcesso.faces");
            m.setTipo("injetado");
            perms2.setMenu(m);
            permissoes.add(perms2);
            
            Permissoes perms3 = new Permissoes();
            Menu m2 = new Menu();
            m2.setDescricao("Fale Conosco");
            m2.setUrl("/pages/comum/contato.faces");
            m2.setTipo("injetado");
            perms3.setMenu(m2);
            permissoes.add(perms3);*/
            
            FacesContext.getCurrentInstance().getExternalContext()
                .getSessionMap().put("perms_usuario", permissoes);
                        
            recoverDataFromSession();
            
            // =================================================================

            if (user.isPrimeiroAcesso() == true) {

                return "/pages/comum/primeiroAcesso.faces?faces-redirect=true";
            } else {
                HttpSession session = SessionUtil.getSession();
                session.setAttribute("User", user.getLogin());
                
                return "/pages/comum/selecaoSistema.faces?faces-redirect=true";
            }
        }
    }
    
    public boolean verificarPermComp(String codigo, Integer idSistema) {
        boolean valida = false;       
        for(Permissoes perms : permsUsuarioLogado) {            
            if(perms.getIdSistemaFunc() != null && perms.getCodigoFuncao() != null) {
                if(perms.getIdSistemaFunc().equals(idSistema) 
                    && perms.getCodigoFuncao().equals(codigo)) {               
                    valida = true;
                }  
            }                     
        }
        return valida;
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
        usuarioLogado = (UsuarioBean) FacesContext
            .getCurrentInstance().getExternalContext().getSessionMap()
            .get("obj_usuario");
        
        sistemasUsuarioLogado = (List<Sistema>) FacesContext
            .getCurrentInstance().getExternalContext().getSessionMap()
            .get("perms_usuario_sis");
        
        permsUsuarioLogado = (List<Permissoes>) FacesContext
            .getCurrentInstance().getExternalContext().getSessionMap()
            .get("perms_usuario");
        
        setoresUsuarioLogado = (String) FacesContext
            .getCurrentInstance().getExternalContext().getSessionMap()
            .get("setores_usuario");
    }
    
    /* Menu dinâmico
    -----------------------------------------------------------------------------*/    
    private void limparMenuModel() {
        menuModel = null;
        menuModel = new DefaultMenuModel();
    }
    
    public void gerarMenus(Sistema sistema) {       
        limparMenuModel();
        List<DefaultSubMenu> menuPai = new ArrayList<>();
        List<DefaultSubMenu> submenu = new ArrayList<>();
        List<DefaultMenuItem> menuItem = new ArrayList<>();         
        List<DefaultSubMenu> menuItemAssSubmenu = new ArrayList<>();
        List<DefaultSubMenu> submenuAssSubMenuPai = new ArrayList<>();
        List<DefaultSubMenu> menusAssociados = new ArrayList<>();
        
        // Gerar menu início.
        DefaultMenuItem item1 = new DefaultMenuItem();
        item1.setValue("Início");
        item1.setUrl(sistema.getUrl().replace("?faces-redirect=true", ""));
        menuModel.addElement(item1);
        
        for(Permissoes p : permsUsuarioLogado) {         
            // Gerar menu pai.
            if((p.getMenu().getIndice() == null) && (p.getMenu().getUrl() == null) 
                && (p.getMenu().getTipo().equals("menuPai"))
                && (p.getIdSistema().equals(sistema.getId())) 
                && (p.getMenu().isAtivo() == true)) {           
                
                DefaultSubMenu mp = new DefaultSubMenu();
                mp.setLabel(p.getMenu().getDescricao());
                mp.setIcon(p.getMenu().getCodigo());
                menuPai.add(mp);
            }
            
            // Gerar submenu.
            if((p.getMenu().getIndice() != null) && (p.getMenu().getUrl() == null) 
                && (p.getMenu().getTipo().equals("submenu"))
                && (p.getIdSistema().equals(sistema.getId())) 
                && (p.getMenu().isAtivo() == true)) {
                
                DefaultSubMenu sb = new DefaultSubMenu();
                sb.setLabel(p.getMenu().getDescricao());
                sb.setIcon(p.getMenu().getCodigo());
                sb.setId(p.getMenu().getIndice());
                submenu.add(sb);
            }
            
            // Gerar menu item com url.
            if((p.getMenu().getIndice() != null) && (p.getMenu().getUrl() != null) 
                && (p.getMenu().getTipo().equals("menuItem")) 
                && (p.getIdSistema().equals(sistema.getId())) 
                && (p.getMenu().isAtivo() == true)) {
                
                Menu mi = p.getMenu();
                
                DefaultMenuItem item = new DefaultMenuItem();
                item.setValue(mi.getDescricao());
                item.setUrl(mi.getUrl());
                item.setIcon(p.getMenu().getCodigo());
                item.setId(mi.getIndice());
                menuItem.add(item);  
            }
            
            
            // Gerar menu item com action/onclick.
            if((p.getMenu().getIndice() != null) && (p.getMenu().getUrl() == null) 
                && (p.getMenu().getTipo().equals("menuItemRel"))
                && (p.getIdSistema().equals(sistema.getId())) 
                && (p.getMenu().isAtivo() == true)) {
                
                Menu mi = p.getMenu();
                
                DefaultMenuItem item = new DefaultMenuItem();
                item.setValue(mi.getDescricao());
                
                if(mi.getAction() != null) {
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
        for(DefaultSubMenu sb : submenu) { 
            DefaultSubMenu submenuAux = sb;
            
            for(DefaultMenuItem mi : menuItem) {
                if(sb.getIcon().equals(mi.getId())) {
                    mi.setIcon(null);
                    submenuAux.addElement(mi);
                }
            }
            menuItemAssSubmenu.add(submenuAux);
        }
        
        // Associar submenu com menu pai.
        for(DefaultSubMenu mp : menuPai) { 
            DefaultSubMenu menuPaiAux = mp;
            
            for(DefaultSubMenu sb : menuItemAssSubmenu) {
                if(mp.getIcon().equals(sb.getId())) {
                    sb.setIcon(null);
                    menuPaiAux.addElement(sb);
                }
            }
            submenuAssSubMenuPai.add(menuPaiAux);
        }
        
        // Associar menu item com menu pai.    
        for(DefaultSubMenu sa : submenuAssSubMenuPai) { 
            DefaultSubMenu menuPaiAux = sa;
            
            for(DefaultMenuItem mi : menuItem) {
                if(sa.getIcon().equals(mi.getId())) {
                    mi.setIcon(null);
                    menuPaiAux.addElement(mi);
                }
            }
            menuPaiAux.setIcon(null);
            menusAssociados.add(menuPaiAux);
        }
                
        // Preencher menu model.
        for(DefaultSubMenu menuAss : menusAssociados) {
            menuModel.addElement(menuAss);
        }
        
        // Gerar menu fale conosco.        
        DefaultMenuItem item2 = new DefaultMenuItem();
        item2.setValue("Fale Conosco");
        item2.setUrl("/pages/comum/contato.faces");
        menuModel.addElement(item2);
        
        // Gerar menu sistemas.
        DefaultMenuItem item3 = new DefaultMenuItem();
        item3.setValue("Sistemas");
        item3.setUrl("/pages/comum/selecaoSistema.faces");
        menuModel.addElement(item3);
        
        // Gerar menu sair.
        DefaultMenuItem item4 = new DefaultMenuItem();
        item4.setValue("Sair");
        item4.setCommand("#{MBUsuarios.logout()}");
        menuModel.addElement(item4);
    }
    
    public String verificarBolTab(boolean ativo, String tipo) {
        if(ativo == true) {
            return "../../imgs/status_green.png";
        } else if(ativo == false && tipo.equals("injetado")) {
            return "../../imgs/status_yellow.png";
        } else {
            return "../../imgs/status_red.png";
        }
    }

    public static void timeOut() throws IOException {
        if(SessionUtil.getSession() != null) {
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

    public List getListaUsuarioSecretaria() throws ProjetoException {
        if (listaUserSecretaria == null) {
            IUsuarioDAO icdao = new UsuarioDAO();

            listaUserSecretaria = icdao.buscaUsuariosSecretaria();
        }
        return listaUserSecretaria;
    }

    public void alterSenha() throws ProjetoException {

        IUsuarioDAO icdao = new UsuarioDAO();
        icdao.updateEditSenha(editausuario);

    }

    public void alterUsuario() throws ProjetoException {

        IUsuarioDAO icdao = new UsuarioDAO();
        boolean resultado = false;
        resultado = icdao.updateEditUsuario(editausuario);
        if (resultado == true) {

            FacesContext context = FacesContext.getCurrentInstance();

            context.addMessage(null, new FacesMessage(
                    "Usuário alterado com sucesso"));

        } else {
            FacesContext context = FacesContext.getCurrentInstance();

            context.addMessage(null, new FacesMessage(
                    "Falha na alteração dos dados do usuário!"));

        }

    }

    public void record() throws ProjetoException {

        IUsuarioDAO icdao = new UsuarioDAO();
        icdao.newUser(novousuario);
        novousuario = new UsuarioBean();

    }

    public void desativarusuario() throws ProjetoException {

        IUsuarioDAO icdao = new UsuarioDAO();
        icdao.desativarUser();
        listaUserSecretaria = null;

    }

    public void ativarusuario() throws ProjetoException {
        boolean pode_ativar = false;
        IUsuarioDAO icdao = new UsuarioDAO();

        pode_ativar = icdao.existeUsuarioAtivo(editausuario.getCpf());
        if (pode_ativar) {
            icdao.ativarUser();
            listaUserSecretaria = null;
        } else {
            FacesContext context = FacesContext.getCurrentInstance();

            context.addMessage(
                    null,
                    new FacesMessage(
                            "Ativação não permitida! Usuário já possui cadastro ativo!"));
        }

    }

    public String alteraSenha() throws ProjetoException {
        IUsuarioDAO icdao = new UsuarioDAO();
        String alteracao = icdao.alteraSenha(editausuario);

        if (alteracao.equals("success")) {
            UsuarioBean user_session = (UsuarioBean) FacesContext
                    .getCurrentInstance().getExternalContext().getSessionMap()
                    .get("obj_usuario");
            HttpSession session = SessionUtil.getSession();
            session.setAttribute("User", user_session.getLogin());

            return "selecaoSistema.faces?faces-redirect=true";
        } else {
            return "";
        }
    }

    public void verificaUltimoAdm(FacesContext context, UIComponent toValidate,
            Object value) throws ProjetoException {
        IUsuarioDAO icdao = new UsuarioDAO();
        String alteracao = String.valueOf(value);

        Integer I = icdao.verificaUltimoAdm(editausuario);

        if ((I == 1)) {
            if (alteracao.equals("false")) {
                ((EditableValueHolder) toValidate).setValid(false);
                FacesMessage message = new FacesMessage(
                        " Operacao nao permitida! ultimo Administrador do Sistema");
                context.addMessage(toValidate.getClientId(context), message);
            }
        }
    }

    public UsuarioBean getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioBean usuario) {
        this.usuario = usuario;
    }

    public UIData getId_tupla() {
        return id_tupla;
    }

    public void setId_tupla(UIData id_tupla) {
        this.id_tupla = id_tupla;
    }

    @Override
    public String toString() {

        return "" + nomeSecretaria + "/" + nomeSetor + "\n";
    }

    public String encrypt(String senha) throws NoSuchAlgorithmException,
            InvalidKeyException, SignatureException {

        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA");
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        md.update(senha.getBytes());
        BigInteger hash = new BigInteger(1, md.digest());
        String retornaSenha = hash.toString(32);

        return retornaSenha;

    }

    public String encryptLogin(String senha) {

        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA");
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        md.update(senha.getBytes());
        BigInteger hash = new BigInteger(1, md.digest());
        String retornaSenha = hash.toString(32);

        return retornaSenha;

    }

    public void getAtualizar() {

        UsuarioDAO dao = new UsuarioDAO();

        ArrayList<UsuarioBean> list = new ArrayList<>();
        ArrayList<UsuarioBean> list2 = new ArrayList<>();

        try {
            list = dao.listUsuario();
        } catch (ProjetoException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA");
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        for (int i = 0; i < list.size(); i++) {
            UsuarioBean user = new UsuarioBean();
            md.update(list.get(i).getSenha().toUpperCase().getBytes());
            BigInteger hash = new BigInteger(1, md.digest());
            user.setCodigo(list.get(i).getCodigo());
            user.setSenha(hash.toString(32));

            list2.add(user);

        }

        try {
            dao.updateSenha(list2);
        } catch (ProjetoException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public String getTimeSession() {
        return timeSession;
    }

    public void setTimeSession(String timeSession) {
        this.timeSession = timeSession;
    }

    public String getVisibleExpirationMsg() {
        visibleExpirationMsg = (String) FacesContext.getCurrentInstance()
                .getExternalContext().getSessionMap().get("expired");
        return visibleExpirationMsg;
    }

    public void setVisibleExpirationMsg(String visibleExpirationMsg) {
        this.visibleExpirationMsg = visibleExpirationMsg;
    }
    
    public UIInput getSenhaInput() {
        return senhaInput;
    }

    public String getPermissaoProcessos() {
        return permissaoProcessos;
    }

    public void setPermissaoProcessos(String permissaoProcessos) {
        permissaoProcessos = permissaoProcessos;
    }

    public void setSenhaInput(UIInput senhaInput) {
        this.senhaInput = senhaInput;
    }

    public Object getNomeUserLogado() {
        return nomeUserLogado;
    }

    public void setNomeUserLogado(Object nomeUserLogado) {
        this.nomeUserLogado = nomeUserLogado;
    }
    
    public void getCodUser(ActionEvent e) {
        editausuario = new UsuarioBean();
        this.editausuario = (UsuarioBean) id_tupla.getRowData();
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
                .put("obj_cod_user", editausuario);
    }

    public void setarCodusuario() {

        FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
                .put("obj_cod_user", editausuario);
    }

    public Object getMenuAdm() {
        return menuAdm;
    }

    public void setMenuAdm(Object menuAdm) {
        this.menuAdm = menuAdm;
    }

    public Object getPermissaoInteressado() {
        return permissaoInteressado;
    }

    public void setPermissaoInteressado(Object permissaoInteressado) {
        this.permissaoInteressado = permissaoInteressado;
    }

    public UsuarioBean getFuncionario() {
        return usuario;
    }

    public UsuarioBean getNovoUsuario() {
        return novousuario;
    }

    public UsuarioBean getEditausuario() {
        return editausuario;
    }

    public void setEditausuario(UsuarioBean editausuario) {
        this.editausuario = editausuario;
    }

    public Object getNomeSetor() {
        return nomeSetor;
    }

    public void setNomeSetor(Object nomeSetor) {
        this.nomeSetor = nomeSetor;
    }

    public Object getObjeto1() {
        return objeto1;
    }

    public void setObjeto1(Object objeto1) {
        this.objeto1 = objeto1;
    }

    public String getDescSetor() {
        return DescSetor;
    }

    public void setDescSetor(String descSetor) {
        DescSetor = descSetor;
    }

    public Object getNomeSecretaria() {
        return nomeSecretaria;
    }

    public void setNomeSecretaria(Object nomeSecretaria) {
        this.nomeSecretaria = nomeSecretaria;
    }
    
    // ACL
    public UsuarioBean getUsuarioLogado() {
        return usuarioLogado;
    }

    public void setUsuarioLogado(UsuarioBean usuarioLogado) {
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

    public Integer getIdSisAux() {
        return idSisAux;
    }

    public void setIdSisAux(Integer idSisAux) {
        this.idSisAux = idSisAux;
    }

    public String getDescricaoSisAux() {
        return descricaoSisAux;
    }

    public void setDescricaoSisAux(String descricaoSisAux) {
        this.descricaoSisAux = descricaoSisAux;
    }

    public String getSiglaSisAux() {
        return siglaSisAux;
    }

    public void setSiglaSisAux(String siglaSisAux) {
        this.siglaSisAux = siglaSisAux;
    }

    public String getImagemSisAux() {
        return imagemSisAux;
    }

    public void setImagemSisAux(String imagemSisAux) {
        this.imagemSisAux = imagemSisAux;
    }

    public String getUrlSisAux() {
        return urlSisAux;
    }

    public void setUrlSisAux(String urlSisAux) {
        this.urlSisAux = urlSisAux;
    }

    public String getVersaoSisAux() {
        return versaoSisAux;
    }

    public void setVersaoSisAux(String versaoSisAux) {
        this.versaoSisAux = versaoSisAux;
    }

    public boolean isAtivoSisAux() {
        return ativoSisAux;
    }

    public void setAtivoSisAux(boolean ativoSisAux) {
        this.ativoSisAux = ativoSisAux;
    }

    public String getSetoresUsuarioLogado() {
        return setoresUsuarioLogado;
    }

    public void setSetoresUsuarioLogado(String setoresUsuarioLogado) {
        this.setoresUsuarioLogado = setoresUsuarioLogado;
    }

    public Boolean getRendDlgSetores() {
        return rendDlgSetores;
    }

    public void setRendDlgSetores(Boolean rendDlgSetores) {
        this.rendDlgSetores = rendDlgSetores;
    }
}