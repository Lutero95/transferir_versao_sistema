package br.gov.al.maceio.sishosp.acl.control;

import br.gov.al.maceio.sishosp.acl.model.Menu;
import br.gov.al.maceio.sishosp.acl.model.Sistema;
import br.gov.al.maceio.sishosp.acl.dao.MenuDAO;
import br.gov.al.maceio.sishosp.acl.dao.SistemaDAO;

import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;
import org.primefaces.event.TransferEvent;
import org.primefaces.model.DualListModel;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.DefaultSubMenu;
import org.primefaces.model.menu.MenuModel;

/**
 *
 * @author Arthur Alves, Emerson Gama & Jerônimo do Nascimento 
 * @since 26/03/2015
 */
@ManagedBean
@ViewScoped
public class MenuMB {

    private Menu menu;
    private List<Menu> listaMenusGeral;
    private List<String> listaExtensoesPag;
    private List<Sistema> listaDiretorios;
    
    private Integer abaAtiva;    
    private String rotinaSelecionada = "0";
    
    private String idMenuAss;
    private List<Menu> listaMenus;
    private List<Menu> listaMenusPag;
    private boolean statusMenu;
   
    private DualListModel<Sistema> listaSistemasDual;
    private List<Sistema> listaSistemasSoucer;
    private List<Sistema> listaSistemasTarget;

    private DualListModel<Sistema> listaSistemasDualAlt;
    private List<Sistema> listaSistemasSoucerAlt;
    private List<Sistema> listaSistemasTargetAlt;
    
    private String idMenuAlt = "0";
    private String indiceAux = "null";    
    private String tipoMenuRel = "A";
    
    private String valorBusca;

    public MenuMB() {
        menu = new Menu();
        menu.setTipo("menuPai");
        menu.getRotina().setExtensao(".faces");

        listaMenus = new ArrayList<>();
        listaMenus = null;

        listaMenusGeral = new ArrayList<>();
        listaMenusGeral = null;
        
        listaMenusPag = new ArrayList<>();
        listaMenusPag = null;

        listaExtensoesPag = new ArrayList<>();
        listaExtensoesPag.add(".faces");
        listaExtensoesPag.add(".jsf");
        listaExtensoesPag.add(".xhtml");
        
        listaDiretorios = new ArrayList<>();
        listaDiretorios = null;
        
        listaSistemasDual = null;
        listaSistemasSoucer = new ArrayList<>();
        listaSistemasTarget = new ArrayList<>();
        
        listaSistemasDualAlt = null;
        listaSistemasSoucerAlt = new ArrayList<>();
        listaSistemasTargetAlt = new ArrayList<>(); 
        
        valorBusca = "";
    }

    public void cadastrarMenu() {
                
        if(listaSistemasDual.getTarget().size() > 0) {
            List<Integer> listaSis = new ArrayList<>();
            
            for (Sistema s : listaSistemasDual.getTarget()) {
                listaSis.add(s.getId());
            }
            
            if(menu.getTipo().equals("menuPai")) {
                menu.getRotina().setDescPagina(null);
                menu.getRotina().setDiretorio(null);
                menu.getRotina().setExtensao(null);
                menu.setIndice(null);
            }
            
            if(menu.getTipo().equals("submenu")) {
                menu.getRotina().setDescPagina(null);
                menu.getRotina().setDiretorio(null);
                menu.getRotina().setExtensao(null);
            }
            
            if(menu.getTipo().equals("menuItemRel")) {
                if(tipoMenuRel.equals("A")) {
                    menu.getRotina().setDescPagina(null);
                    menu.getRotina().setDiretorio(null);
                    menu.getRotina().setExtensao(null);
                    menu.setAction("#{" + menu.getAction() + "}");
                    menu.setOnclick(null);
                } else {
                    menu.getRotina().setDescPagina(null);
                    menu.getRotina().setDiretorio(null);
                    menu.getRotina().setExtensao(null);
                    menu.setAction(null);
                    menu.setOnclick(menu.getOnclick() + ".show();");
                }
            }            
            menu.setAtivo(statusMenu);
            menu.setListaSistemas(listaSis);
            menu.setIdRotina(Integer.parseInt(rotinaSelecionada));

            MenuDAO mdao = new MenuDAO();
            boolean cadastrou = mdao.cadastrar(menu);

            if(cadastrou == true) {

                listaMenusGeral = null;

                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Menu cadastrado com sucesso!", "Sucesso");
                FacesContext.getCurrentInstance().addMessage(null, msg);

                RequestContext.getCurrentInstance().execute("dlgCadMenu.hide();");
            } else {
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Ocorreu um erro durante o cadastro!", "Erro");
                FacesContext.getCurrentInstance().addMessage(null, msg);

                RequestContext.getCurrentInstance().execute("dlgCadMenu.hide();");
            }
        } else {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                "Selecione pelo menos um sistema", "Erro");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }
    
    public void alterarMenu() {
    
        if(listaSistemasDualAlt.getTarget().size() > 0) {
            List<Integer> listaSis = new ArrayList<>();
            
            for(Sistema s : listaSistemasDualAlt.getTarget()) {
                listaSis.add(s.getId());
            }
            menu.setAtivo(statusMenu);
            menu.setListaSistemas(listaSis);
            menu.setIdRotina(Integer.parseInt(rotinaSelecionada));
            menu.setIndiceAux(indiceAux);

            MenuDAO mdao = new MenuDAO();
            boolean alterou = mdao.alterar(menu);

            if(alterou == true) {

                listaMenusGeral = null;

                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Menu alterado com sucesso!", "Sucesso");
                FacesContext.getCurrentInstance().addMessage(null, msg);

                RequestContext.getCurrentInstance().execute("dlgAltMenu.hide();");
            } else {
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Ocorreu um erro durante o cadastro!", "Erro");
                FacesContext.getCurrentInstance().addMessage(null, msg);

                RequestContext.getCurrentInstance().execute("dlgAltMenu.hide();");
            }
        } else {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                "Selecione pelo menos um sistema", "Erro");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }       
    } 
    
    public void excluirMenu() {
        
        MenuDAO mdao = new MenuDAO();
        boolean excluiu = mdao.excluirMenu(menu);

        if(excluiu == true) {
            
            listaMenusGeral = null;
            
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
                "Menu excluido com sucesso!", "Sucesso");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            
            RequestContext.getCurrentInstance().execute("dlgExcMenu.hide();");
        } else {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
                "Ocorreu um erro durante a exclusão!", "Sucesso");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            
            RequestContext.getCurrentInstance().execute("dlgExcMenu.hide();");
        }        
    }
    
    public void buscarMenu() {
        MenuDAO mdao = new MenuDAO();
        List<Menu> listaAux = mdao.buscarMenuDesc(valorBusca);
        
        if(listaAux != null && listaAux.size() > 0) {
            listaMenusGeral = null;
            listaMenusGeral = listaAux;
        } else {
            listaMenusGeral = null;
            
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN,
                "Menu não encontrado!", "Aviso");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } 
    }
    
    public List<Menu> filtrarListaMenu(List<Menu> lista) {
        
        List<Menu> listaMenusAux = lista;
        List<Menu> listaVerificada = new ArrayList<>();

        MenuDAO mdao = new MenuDAO();
        List<Menu> listaMenusPI = mdao.listarMenuPaiSubmenuComSis();
 
        for(Menu mp : listaMenusAux) {
            for(Menu mn1 : listaMenusPI) {
                Menu menuAux = new Menu();
                if(mp.getIndice().equals(mn1.getCodigo()) 
                    && mp.getIdSistema().equals(mn1.getIdSistema())) {                    
                    
                    if(!listaVerificada.contains(mn1)) {
                        listaVerificada.add(mn1);
                    }
                    listaVerificada.add(mp);
                    
                    menuAux = mn1;
                    if(menuAux.getIndice() != null) {
                        for(Menu mn2 : listaMenusPI) {
                            if(menuAux.getIndice().equals(mn2.getCodigo()) 
                                && menuAux.getIdSistema().equals(mn2.getIdSistema())) {
                                
                                if(!listaVerificada.contains(mn2)) {
                                    listaVerificada.add(mn2);
                                }
                            }                        
                        }
                    }
                }   
            }
        }
        return listaVerificada;
    }
    
    public List<Menu> filtrarPreMenu(String tipo, List<Menu> listaCad, List<Menu> listaAlt) {
        List<Menu> listaMenusAux = new ArrayList<>();
        List<Menu> listaVerificada = new ArrayList<>();
        
        if(tipo.equals("CM")) {
            for(Menu m : listaCad){
                listaMenusAux.add(m);
            }
        }
        
        if(tipo.equals("AM")) {
            for(Menu m : listaAlt){
                listaMenusAux.add(m);
            }
        }

        MenuDAO mdao = new MenuDAO();
        List<Menu> listaMenus = mdao.listarMenuPaiSubmenuComSis();
 
        for(Menu mp : listaMenusAux) {
            for(Menu mn1 : listaMenus) {
                Menu menuAux = new Menu();
                if(mp.getIndice().equals(mn1.getCodigo()) 
                    && mp.getIdSistema().equals(mn1.getIdSistema())) {                    
                    
                    if(!listaVerificada.contains(mn1)) {
                        listaVerificada.add(mn1);
                    }
                    listaVerificada.add(mp);
                    
                    menuAux = mn1;
                    if(menuAux.getIndice() != null) {
                        for(Menu mn2 : listaMenus) {
                            if(menuAux.getIndice().equals(mn2.getCodigo()) 
                                && menuAux.getIdSistema().equals(mn2.getIdSistema())) {
                                
                                if(!listaVerificada.contains(mn2)) {
                                    listaVerificada.add(mn2);
                                }
                            }                        
                        }
                    }
                }   
            }
        }  
        return listaVerificada;
    }
    
    public MenuModel gerarMenusPreview(List<Menu> listaMenusPreview, Integer idSistema) {
        
        MenuModel model = new DefaultMenuModel();
        
        List<DefaultSubMenu> menuPai = new ArrayList<>();
        List<DefaultSubMenu> submenu = new ArrayList<>();
        List<DefaultMenuItem> menuItem = new ArrayList<>();         
        List<DefaultSubMenu> menuItemAssSubmenu = new ArrayList<>();
        List<DefaultSubMenu> submenuAssSubMenuPai = new ArrayList<>();
        List<DefaultSubMenu> menusAssociados = new ArrayList<>();
        
        List<Menu> lista = listaMenusPreview;
        
        // Gerar menu início.
        DefaultMenuItem item1 = new DefaultMenuItem();
        item1.setValue("Início");
        model.addElement(item1);
        
        for(Menu menu : lista) {
            // Gerar menu pai.
            if((menu.getIndice() == null) && (menu.getUrl() == null) 
                && (menu.getTipo().equals("menuPai"))
                && (menu.getIdSistema().equals(idSistema)) 
                && (menu.isAtivo() == true)) {
                
                DefaultSubMenu mp = new DefaultSubMenu();
                mp.setLabel(menu.getDescricao());
                mp.setIcon(menu.getCodigo());
                menuPai.add(mp);
            }

            // Gerar submenu.
            if((menu.getIndice() != null) && (menu.getUrl() == null) 
                && (menu.getTipo().equals("submenu"))
                && (menu.getIdSistema().equals(idSistema)) 
                && (menu.isAtivo() == true)) {
                
                DefaultSubMenu sb = new DefaultSubMenu();
                sb.setLabel(menu.getDescricao());
                sb.setIcon(menu.getCodigo());
                sb.setId(menu.getIndice());
                submenu.add(sb);
            }

            // Gerar menu item com url.
            if((menu.getIndice() != null) && (menu.getUrl() != null) 
                && (menu.getTipo().equals("menuItem")) 
                && (menu.getIdSistema().equals(idSistema)) 
                && (menu.isAtivo() == true)) {
                
                Menu mi = menu;
                
                DefaultMenuItem item = new DefaultMenuItem();
                item.setValue(mi.getDescricao());
                item.setIcon(menu.getCodigo());
                item.setId(mi.getIndice());
                menuItem.add(item);
            }

            // Gerar menu item com action/onclick.
            if((menu.getIndice() != null) && (menu.getUrl() == null) 
                && (menu.getTipo().equals("menuItemRel"))
                && (menu.getIdSistema().equals(idSistema)) 
                && (menu.isAtivo() == true)) {
                
                Menu mi = menu;

                DefaultMenuItem item = new DefaultMenuItem();
                item.setValue(mi.getDescricao());
                item.setIcon(menu.getCodigo());
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
            model.addElement(menuAss);
        }

        // Gerar menu fale conosco.        
        DefaultMenuItem item2 = new DefaultMenuItem();
        item2.setValue("Fale Conosco");
        model.addElement(item2);

        // Gerar menu sistemas.
        DefaultMenuItem item3 = new DefaultMenuItem();
        item3.setValue("Sistemas");
        model.addElement(item3);

        // Gerar menu sair.
        DefaultMenuItem item4 = new DefaultMenuItem();
        item4.setValue("Sair");
        model.addElement(item4);

        return model;
    }
    
    public String verificarTipoRel(String descricao, String tipo) {
        if(tipo.equals("menuItemRel")) {
            return "Relatório " + descricao;
        } else {
            return descricao;
        }
    }
    
    private void recActionOnclick() {
        
        if(menu.getTipo().equals("menuItemRel")) {
            if(menu.getAction() != null && menu.getOnclick() == null) {
                tipoMenuRel = "A";
                menu.setAction(menu.getAction().replace("#{", "").replace("}", ""));
            }

            if(menu.getAction() == null && menu.getOnclick() != null) {
                tipoMenuRel = "D";
                menu.setOnclick(menu.getOnclick().replace(".show();", ""));
            }
        }
    }
    
    public String verificarBolTab(boolean ativo) {
        if(ativo == true) {
            return "../../imgs/status_green.png";
        } else {
            return "../../imgs/status_red.png";
        }
    }
    
    public void onTransferMenuSis(TransferEvent event) {
        StringBuilder builder = new StringBuilder();

        for(Object item : event.getItems()) {
            builder.append(((Sistema) item).getId());
            if(listaSistemasTarget.contains(item)) {
                listaSistemasTarget.remove(item);
            } else {
                listaSistemasTarget.add((Sistema) item);
            }
        }
    }
    
    public void limparDados() {
        abaAtiva = 0;
        menu = new Menu();
        menu.setTipo("menuPai");
        menu.getRotina().setExtensao(".faces");
        tipoMenuRel = "A";
        
        listaMenus = null;
        listaDiretorios = null;
        
        rotinaSelecionada = "0";
        statusMenu = true;
    }
    
    public void limparBusca() {
        valorBusca = "";
        listaMenusGeral = null;
    }
    
    public void limparDualMenuSis() {
        listaSistemasDual = null;
        
        listaSistemasTarget = null;
        listaSistemasTarget = new ArrayList<>();
    }
    
    public void limparDualMenuSisAlt() {
        listaSistemasDualAlt = null;
        
        listaSistemasTargetAlt = null;
        listaSistemasTargetAlt = new ArrayList<>();
        
        recActionOnclick();
    }

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    public String getIdMenuAss() {
        return idMenuAss;
    }

    public void setIdMenuAss(String idMenuAss) {
        this.idMenuAss = idMenuAss;
    }

    public List<Menu> getListaMenus() {
        if (listaMenus == null) {
            MenuDAO mdao = new MenuDAO();
            listaMenus = mdao.listarMenusPaiSubmenus();
        }
        return listaMenus;
    }
    public List<Menu> getListaMenusGeral() {
        if (listaMenusGeral == null) {
            MenuDAO mdao = new MenuDAO();
            listaMenusGeral = mdao.listarMenuGeral();
        }
        return listaMenusGeral;
    }

    public void setListaMenus(List<Menu> listaMenus) {
        this.listaMenus = listaMenus;
    }

    public List<Menu> getListaMenusPag() {
        if (listaMenusPag == null) {
            MenuDAO mdao = new MenuDAO();
            listaMenusPag = mdao.listarMenuItem();
        }
        return listaMenusPag;
    }

    public void setListaMenusPag(List<Menu> listaMenusPag) {
        this.listaMenusPag = listaMenusPag;
    }

    public List<String> getListaExtensoesPag() {
        return listaExtensoesPag;
    }

    public void setListaExtensoesPag(List<String> listaExtensoesPag) {
        this.listaExtensoesPag = listaExtensoesPag;
    }

    public List<Sistema> getListaDiretorios() {
        if(listaDiretorios == null) {
            SistemaDAO sdao = new SistemaDAO();
            listaDiretorios = sdao.listarSiglas();
        }
        return listaDiretorios;
    }

    public void setListaDiretorios(List<Sistema> listaDiretorios) {
        this.listaDiretorios = listaDiretorios;
    }

    public DualListModel<Sistema> getListaSistemasDual() {
        if(listaSistemasDual == null) {
            listaSistemasSoucer = null;
            listaSistemasTarget = new ArrayList<>();
            getListaSistemasSoucer();
            listaSistemasDual = new DualListModel<>(listaSistemasSoucer, listaSistemasTarget);
        }
        return listaSistemasDual;
    }

    public void setListaSistemasDual(DualListModel<Sistema> listaSistemasDual) {
        this.listaSistemasDual = listaSistemasDual;
    }

    public List<Sistema> getListaSistemasSoucer() {
        if(listaSistemasSoucer == null) {
            SistemaDAO sdao = new SistemaDAO();
            listaSistemasSoucer = sdao.listarSistemasSource();
        }
        return listaSistemasSoucer;
    }

    public void setListaSistemasSoucer(List<Sistema> listaSistemasSoucer) {
        this.listaSistemasSoucer = listaSistemasSoucer;
    }

    public List<Sistema> getListaSistemasTarget() {
        return listaSistemasTarget;
    }

    public void setListaSistemasTarget(List<Sistema> listaSistemasTarget) {
        this.listaSistemasTarget = listaSistemasTarget;
    }

    public Integer getAbaAtiva() {
        return abaAtiva;
    }

    public void setAbaAtiva(Integer abaAtiva) {
        this.abaAtiva = abaAtiva;
    }

    public String getRotinaSelecionada() {
        return rotinaSelecionada;
    }

    public void setRotinaSelecionada(String rotinaSelecionada) {
        this.rotinaSelecionada = rotinaSelecionada;
    }

    public DualListModel<Sistema> getListaSistemasDualAlt() {       
        if(listaSistemasDualAlt == null) {
            listaSistemasSoucerAlt = null;
            listaSistemasTargetAlt = null;
            getListaSistemasSoucerAlt();
            getListaSistemasTargetAlt();
            listaSistemasDualAlt = new DualListModel<>(listaSistemasSoucerAlt, listaSistemasTargetAlt);
        }
        return listaSistemasDualAlt;
    }

    public void setListaSistemasDualAlt(DualListModel<Sistema> listaSistemasDualAlt) {
        this.listaSistemasDualAlt = listaSistemasDualAlt;
    }

    public List<Sistema> getListaSistemasSoucerAlt() {
        if(listaSistemasSoucerAlt == null)  {
            MenuDAO mdao = new MenuDAO();
            listaSistemasSoucerAlt = mdao.listarSisAssNaoMenuSource(Long.valueOf(idMenuAlt));           
        }
        return listaSistemasSoucerAlt;
    }

    public void setListaSistemasSoucerAlt(List<Sistema> listaSistemasSoucerAlt) {
        this.listaSistemasSoucerAlt = listaSistemasSoucerAlt;
    }

    public List<Sistema> getListaSistemasTargetAlt() {
        
        if(listaSistemasTargetAlt == null) {
            MenuDAO mdao = new MenuDAO();
            listaSistemasTargetAlt = mdao.listarSisAssMenuTarget(Long.valueOf(idMenuAlt));
        }
        return listaSistemasTargetAlt;
    }

    public void setListaSistemasTargetAlt(List<Sistema> listaSistemasTargetAlt) {
        this.listaSistemasTargetAlt = listaSistemasTargetAlt;
    }

    public String getIdMenuAlt() {
        return idMenuAlt;
    }

    public void setIdMenuAlt(String idMenuAlt) {
        this.idMenuAlt = idMenuAlt;
    }

    public String getIndiceAux() {
        return indiceAux;
    }

    public void setIndiceAux(String indiceAux) {
        this.indiceAux = indiceAux;
    }

    public boolean isStatusMenu() {
        return statusMenu;
    }

    public void setStatusMenu(boolean statusMenu) {
        this.statusMenu = statusMenu;
    }

    public String getTipoMenuRel() {
        return tipoMenuRel;
    }

    public void setTipoMenuRel(String tipoMenuRel) {
        this.tipoMenuRel = tipoMenuRel;
    }

    public String getValorBusca() {
        return valorBusca;
    }

    public void setValorBusca(String valorBusca) {
        this.valorBusca = valorBusca;
    }
}