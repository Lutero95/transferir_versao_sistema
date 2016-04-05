package br.gov.al.maceio.sishosp.acl.control;

import br.gov.al.maceio.sishosp.acl.model.Menu;
import br.gov.al.maceio.sishosp.acl.dao.MenuDAO;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Emerson Devel
 */
public class Teste {
    
    public static void main(String[] args) {
        Teste t = new Teste();
        t.testa();
    }
    
    public void testa() {
        
        Menu m = new Menu();
        m.setId(Long.valueOf("117"));
        m.setDescricao("Editar Servidor");
        m.setCodigo("MN-117");
        m.setIndice("MN-1");
        m.setTipo("menuItem");
        m.setAtivo(true);         
        m.setIndiceAux("MN-117");
        m.setIdSistema(1);
        
        List<Menu> listaMenusAux = new ArrayList<>();
        listaMenusAux.add(m);
        
        List<Menu> listaFiltrada = filtrarListaMenu(listaMenusAux);
            
        for(Menu mlf : listaFiltrada) {
            //System.out.println("listafiltrada p1: " + mlf.getDescricao());
        }
    }
    
    public List<Menu> filtrarListaMenu(List<Menu> lista) {
        
        List<Menu> listaMenusAux = lista;
        List<Menu> listaVerificada = new ArrayList<>();
        
        for(Menu mlf : listaMenusAux) {
            //System.out.println("listamenuaux: " + mlf.getDescricao());
        }

        MenuDAO mdao = new MenuDAO();
        List<Menu> listaMenusPI = mdao.listarMenuPaiSubmenuComSis();
        
        for(Menu mlf : listaMenusPI) {
            //System.out.println("listamenupaisub: " + mlf.getDescricao());
        }
 
        for(Menu mp : listaMenusAux) {
            for(Menu mn1 : listaMenusPI) {
                Menu menuAux = new Menu();
                if(mp.getIndice().equals(mn1.getCodigo()) 
                    && mp.getIdSistema().equals(mn1.getIdSistema())) {                    
                    
                    if(!listaVerificada.contains(mn1)) {
                        //System.out.println("pai: " + mn1.getDescricao());
                        listaVerificada.add(mn1);
                    }
                    listaVerificada.add(mp);
                    
                    menuAux = mn1;
                    if(menuAux.getIndice() != null) {
                        for(Menu mn2 : listaMenusPI) {
                            if(menuAux.getIndice().equals(mn2.getCodigo()) 
                                && menuAux.getIdSistema().equals(mn2.getIdSistema())) {
                                
                                if(!listaVerificada.contains(mn2)) {
                                    //System.out.println("paipai: " + mn1.getDescricao());
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
}