package br.gov.al.maceio.sishosp.acl.control;

import br.gov.al.maceio.sishosp.acl.model.Menu;
import br.gov.al.maceio.sishosp.acl.model.Permissoes;
import br.gov.al.maceio.sishosp.acl.model.Sistema;

import java.io.IOException;
import java.io.Serializable;
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
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
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
 * @author Thulio e Thiago
 * @since 06/04/2016
 */
@ManagedBean(name="MBUsuarios")
@SessionScoped
public class UsuarioController implements Serializable {

	private static final long serialVersionUID = 1L;
	private UsuarioBean usuario;
	private UsuarioBean novousuario, editausuario;
	private UIData id_tupla;
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

	private List<Sistema> sistemasUsuarioLogado;
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
    	
	public String ultimoDiaMes(Integer Ano, Integer Mes) {
		Calendar cal = new GregorianCalendar(Ano, Mes - 1, 1);
		return toString().valueOf(cal.getActualMaximum(Calendar.DAY_OF_MONTH));
	}

	public String login() throws ProjetoException {
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
				.put("expired", "N");
		permissaoProcessos = "false";

		UsuarioDAO udao = new UsuarioDAO();
		System.out.println("NOME USER: " + usuario.getLogin());
		usuarioLogado = udao.autenticarUsuario(usuario);

		if (usuarioLogado == null) {
			FacesContext fct = FacesContext.getCurrentInstance();
			fct.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Usuario ou senha invalidos!", "Erro"));

			return "";
		} else {
//			FacesContext.getCurrentInstance().getExternalContext()
					//.getSessionMap().put("obj_usuario", usuarioLogado);
			//return "/pages/comum/selecaoSistema.faces?faces-redirect=true";

            FacesContext.getCurrentInstance().getExternalContext()
                .getSessionMap().put("obj_usuario", usuarioLogado);

            nomeSetor = DescSetor;
            permissaoInteressado = permissaoProcessos;

            
            
            // ACL =============================================================
                        
            List<Sistema> sistemas = udao.carregarSistemasUsuario(usuarioLogado);
            
            FacesContext.getCurrentInstance().getExternalContext()
                .getSessionMap().put("perms_usuario_sis", sistemas);
            
            List<Permissoes> permissoes = udao.carregarPermissoes(usuarioLogado);
            
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
            
            for(Sistema s : sistemas) {
                Menu m = new Menu();
                m.setDescricao("Fale Conosco");
                m.setUrl(s.getUrl());
                m.setTipo("injetado");
                Permissoes perms = new Permissoes();
                perms.setMenu(m);
                permissoes.add(perms);
            }
            
            /*Permissoes perms = new Permissoes();
            Menu m2 = new Menu();
            m2.setDescricao("Fale Conosco");
            m2.setUrl("/pages/comum/contato.faces");
            m2.setTipo("injetado");
            perms.setMenu(m2);
            permissoes.add(perms);*/
            
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
            session.setAttribute("User", usuarioLogado.getLogin());
                        
            recoverDataFromSession();
            
            /*if(usuarioLogado.getIdPerfil() == 1) {
                url = "/pages/comum/selecaoSistema.faces?faces-redirect=true";
            } else {   
                for(Sistema sis : sistemas) {
                    if(sis.getId() == 1) {
                        sistemaLogado = sis;
                    } else {
                        
                    }
                }
                url = "/pages/clin/principal.faces?faces-redirect=true";
            }*/
          String  url = "/pages/comum/selecaoSistema.faces?faces-redirect=true";
       
        return url;			

		}
	}

	public String redirecionar(String url) {
		System.out.println("vai redir");
		gerarMenus(sistemaLogado);
		return url;
	}

	public void recSistemaLogado(Sistema sistema) {
		sistemaLogado = sistema;
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
				.put("sistema_logado", sistema);
	}

	public void recoverDataFromSession() {
		usuarioLogado = (UsuarioBean) FacesContext.getCurrentInstance()
				.getExternalContext().getSessionMap().get("obj_usuario");

		sistemasUsuarioLogado = (List<Sistema>) FacesContext
				.getCurrentInstance().getExternalContext().getSessionMap()
				.get("perms_usuario_sis");

		permsUsuarioLogado = (List<Permissoes>) FacesContext
				.getCurrentInstance().getExternalContext().getSessionMap()
				.get("perms_usuario");

	}

	/*
	 * Menu dinâmico
	 * ------------------------------------------------------------
	 * -----------------
	 */
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
	        
	        for(Permissoes p : permsUsuarioLogado) {         
	            // Gerar menu pai.
	            if((p.getMenu().getIndice() == null) && (p.getMenu().getUrl() == null) 
	                && (p.getMenu().getTipo().equals("menuPai"))
	                && (p.getIdSistema().equals(sistema.getId())) 
	                && (p.getMenu().isAtivo() == true)) {           
	                
	                DefaultSubMenu mp = new DefaultSubMenu();
	                mp.setLabel(p.getMenu().getDescricao());//o nome(descri��o) que vai aparecer na interface
	                mp.setIcon(p.getMenu().getCodigo()); // faz a associa��o em que menu ele vai ficar
	                menuPai.add(mp); //adiciona o menu
	            }
	           
	           
	            // Gerar submenu.
	            if((p.getMenu().getIndice() != null) && (p.getMenu().getUrl() == null) 
	                && (p.getMenu().getTipo().equals("submenu")) //compara o tipo para verificar se � submenu
	                && (p.getIdSistema().equals(sistema.getId()))  //compara os ids
	                && (p.getMenu().isAtivo() == true)) { //se for ativo
	                
	                DefaultSubMenu sb = new DefaultSubMenu();//inicia a cria��o do submenu
	                sb.setLabel(p.getMenu().getDescricao());
	                sb.setIcon(p.getMenu().getCodigo());
	                sb.setId(p.getMenu().getIndice());
	                System.out.println("Gerar SubMenu - Label: "+sb.getLabel());
	                System.out.println("Gerar SubMenu - Icon: "+sb.getIcon());
	                System.out.println("Gerar SubMenu - Id: "+sb.getId());
	                submenu.add(sb);//cria o submenu
	            }
	            
	            // Gerar menu item com url.
	            if((p.getMenu().getIndice() != null) && (p.getMenu().getUrl() != null) 
	                && (p.getMenu().getTipo().equals("menuItem"))//compara o tipo 
	                && (p.getIdSistema().equals(sistema.getId())) 
	                && (p.getMenu().isAtivo() == true)) {
	                //System.out.println("gerar menu item com url, p.getIdSistema: "+p.getIdSistema());
	                //System.out.println("gerar menu item com url, sistema.getId: "+sistema.getId());
	                Menu mi = p.getMenu();
	                //System.out.println("gerar menu item com url, p.getMenu: "+p.getMenu());
	                DefaultMenuItem item = new DefaultMenuItem();
	                item.setValue(mi.getDescricao());
	                System.out.println("Gerar menuitem com url, descri��o: "+mi.getDescricao());
	                System.out.println("Gerar menuitem com url, URL: "+mi.getUrl());	                
	                item.setUrl(mi.getUrl());
	                item.setIcon(p.getMenu().getCodigo());//coloca o submenu dentro do menu
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
	            DefaultSubMenu submenuAux = sb; //submenu que � elemento da lista recebe sb(submenu)
	            System.out.println("TESTE FORA DO FOR, ICON: "+sb.getIcon());
	            for(DefaultMenuItem mi : menuItem) {
	                if(sb.getIcon().equals(mi.getId())) { 
	                	System.out.println("1 - Menu item com submenu, sb.getIcon: "+sb.getIcon());
	                    mi.setIcon(null);
	                    submenuAux.addElement(mi);
	                    System.out.println("1 - Menu item com submenu, mi.getId: "+mi.getValue());
	                }
	            }
	            menuItemAssSubmenu.add(submenuAux);
	            
	        }
	        
	     // Associar submenu com submenu.
	        for(DefaultSubMenu sb2 : submenu) { 
	          
	            
	            for(DefaultSubMenu sb : menuItemAssSubmenu) {
	            	System.out.println("ENTROU NO SUB MENU"+sb2.getLabel());
	            	System.out.println("sb2.getIcon "+sb.getIcon());
	            	System.out.println("sb2.getId()"+sb2.getId());
	            	if(sb2.getIcon().equals(sb.getId())) {
	            		System.out.println("Procurar id: "+sb.getId());
	                	System.out.println("SUBMENU OK");
	                	System.out.println("LABEL COM ERRO: "+sb.getLabel());
	                  //  sb.setIcon(null);
	                   
	                    sb2.addElement(sb);//esse auxiliar recebe o submenu e depois mais embaixo adiciona ele na lista
	                }
	            }
	            subsubAssSubMenu.add(sb2);
	        }
	        
	        // Associar submenu com menu pai.
	        for(DefaultSubMenu mp : menuPai) { 
	            DefaultSubMenu menuPaiAux = mp;
	            
	            for(DefaultSubMenu sb : subsubAssSubMenu) {
	                if(mp.getIcon().equals(sb.getId())) {
	                	System.out.println("2 - Submneu com meunupai, sb.getId: "+sb.getId());
	                	System.out.println("2 - Submneu com meunupai, sb.getIcon: "+sb.getIcon());
	                    sb.setIcon(null);
	                    menuPaiAux.addElement(sb);//esse auxiliar recebe o submenu e depois mais embaixo adiciona ele na lista
	                    System.out.println("2-  Submenu com menupai, menuPaiAux.getLabel: "+menuPaiAux.getLabel());
	                }
	            }
	            submenuAssSubMenuPai.add(menuPaiAux);
	        }
	        
	        // Associar menu item com menu pai.    
	        for(DefaultSubMenu sa : submenuAssSubMenuPai) { 
	            DefaultSubMenu menuPaiAux = sa;
	            //Argemiro
	            for(DefaultMenuItem mi : menuItem) {           	
	                if(sa.getIcon().equals(mi.getId())) {
	                    mi.setIcon(null);
	                    menuPaiAux.addElement(mi);
	                    System.out.println("3 - Menuitem com menupai, icon: "+sa.getIcon());
	                	System.out.println("3 - Menuitem com menupai, id: "+mi.getId());
	                    System.out.println("3 - Menuitem com menupai, menuPaiAux.getLabel: "+menuPaiAux.getLabel());
	                    System.out.println("3 - Menuitem com menupai, mi.getId: "+mi.getId());
	                    
	                    
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
	        //item2.setUrl("/pages/comum/contato.faces");
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
	        item4.setCommand("#{MBUsuarios.logout()}");
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