package br.gov.al.maceio.sishosp.acl.control;

import br.gov.al.maceio.sishosp.acl.model.Menu;
import br.gov.al.maceio.sishosp.acl.model.Rotina;
import br.gov.al.maceio.sishosp.acl.model.Sistema;
import br.gov.al.maceio.sishosp.acl.dao.MenuDAO;
import br.gov.al.maceio.sishosp.acl.dao.RotinaDAO;
import br.gov.al.maceio.sishosp.acl.dao.SistemaDAO;

import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;

/**
 *
 * @author Arthur Alves, Emerson Gama & Jerônimo do Nascimento 
 * @since 17/03/2015
 */
@ManagedBean
@ViewScoped
public class RotinaMB {

    private Rotina rotina;
    private List<Rotina> listaRotinas;
    private String sistemaSelecionado;

    private Menu menu;
    private List<Menu> listaMenusGeral;
    private List<String> listaExtensoesPag;
    private List<Sistema> listaDiretorios;
    
     
    private String rotinaSelecionada = "0";
    
    private List<Menu> listaMenus;
    private List<Menu> listaMenusPag; 
    private String tipoMenuRel = "A";
    
	private String valorBusca;
    private String sisBusca;

    public RotinaMB() {
        rotina = new Rotina();
        listaRotinas = new ArrayList<>();
        listaRotinas = null;
        sistemaSelecionado = "0";
        valorBusca = "";
        sisBusca = "0";
        
        menu = new Menu();
        menu.setTipo("menuPai");
        rotina.setExtensao(".faces");

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
        
    }

    public void cadastrarRotina() {
 
    	/*
            if(menu.getTipo().equals("menuPai")) {
                rotina.setDescPagina(null);
                rotina.setDiretorio(null);
                rotina.setExtensao(null);
            }
            if(menu.getTipo().equals("submenu")) {
                rotina.setDescPagina(null);
                rotina.setDiretorio(null);
                rotina.setExtensao(null);
            }
            if(menu.getTipo().equals("menuItemRel")) {
                if(tipoMenuRel.equals("A")) {
                    rotina.setDescPagina(null);
                    rotina.setDiretorio(null);
                    rotina.setExtensao(null);
      
                } else {
                    rotina.setDescPagina(null);
                    rotina.setDiretorio(null);
                    rotina.setExtensao(null);
                }
            }  */
         
            //rotina.setAtivo(statusMenu);
            //rotina.setIdSistema(Integer.parseInt(rotinaSelecionada));
            RotinaDAO rdao = new RotinaDAO();
            boolean cadastrou = rdao.cadastrarRotina(rotina);
            
        if(cadastrou == true) {
            
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
                "Rotina cadastrada com sucesso!", "Sucesso");
            FacesContext.getCurrentInstance().addMessage(null, msg);

            RequestContext.getCurrentInstance().execute("dlgCadRotina.hide();");
        } 
        else {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                "Ocorreu um erro durante o cadastro!", "Erro");
            FacesContext.getCurrentInstance().addMessage(null, msg);

            RequestContext.getCurrentInstance().execute("dlgCadRotina.hide();");
        }
    }

    public void alterarRotina() {
    	
        RotinaDAO rdao = new RotinaDAO();
        boolean alterou = rdao.alterarRotina(rotina);

        if(alterou == true) {
            
            listaRotinas = null;
            
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
                "Rotina alterada com sucesso!", "Sucesso");
            FacesContext.getCurrentInstance().addMessage(null, msg);

            RequestContext.getCurrentInstance().execute("dlgAltRotina.hide();");
        } else {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                "Ocorreu um erro durante a alteração!", "Erro");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            
            RequestContext.getCurrentInstance().execute("dlgAltRotina.hide();");
        }       
    }

    public void excluirRotina() {
        
        RotinaDAO rdao = new RotinaDAO();       
        boolean excluiu = rdao.excluirRotina(rotina);

        if(excluiu == true) {
            
            listaRotinas = null;
            
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
                "Rotina excluida com sucesso!", "Sucesso");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            
            RequestContext.getCurrentInstance().execute("dlgExcRotina.hide();");
        } else {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                "Ocorreu um erro durante a exclusão!", "Erro");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            
            RequestContext.getCurrentInstance().execute("dlgExcRotina.hide();");
        }       
    }
    
    public void buscarRotina() {
        RotinaDAO rdao = new RotinaDAO();
        List<Rotina> listaAux = rdao.buscarRotinaDescSis(valorBusca, Integer.parseInt(sisBusca));
        
        if(listaAux != null && listaAux.size() > 0) {
            listaRotinas = null;
            listaRotinas = listaAux;
        } else {
            listaRotinas = null;
            
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN,
                "Rotina não encontrada!", "Aviso");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } 
    }
    /*
    public void recDescricao() {
        String aux = rotina.getDescricao().replaceFirst("ROT-", "");
        rotina.setDescricao(aux);
    }*/

    public void limparDados() {
        rotina = new Rotina();
        sisBusca = "0";
        sistemaSelecionado = "0";
    }
    
    public void limparBusca() {
        valorBusca = "";
        sisBusca = "0";
        listaRotinas = null;
    }

    public Rotina getRotina() {
        return rotina;
    }

    public void setRotina(Rotina rotina) {
        this.rotina = rotina;
    }

    public List<Rotina> getListaRotinas() {
        if (listaRotinas == null) {
            RotinaDAO rdao = new RotinaDAO();
            listaRotinas = rdao.listarRotinas();
        }
        return listaRotinas;
    }

    public void setListaRotinas(List<Rotina> listaRotinas) {
        this.listaRotinas = listaRotinas;
    }

    public String getSistemaSelecionado() {
        return sistemaSelecionado;
    }

    public void setSistemaSelecionado(String sistemaSelecionado) {
        this.sistemaSelecionado = sistemaSelecionado;
    }

    public String getValorBusca() {
        return valorBusca;
    }

    public void setValorBusca(String valorBusca) {
        this.valorBusca = valorBusca;
    }

    public String getSisBusca() {
        return sisBusca;
    }

    public void setSisBusca(String sisBusca) {
        this.sisBusca = sisBusca;
    }
    public Menu getMenu() {
		return menu;
	}

	public void setMenu(Menu menu) {
		this.menu = menu;
	}

	public List<Menu> getListaMenusGeral() {
		 if (listaMenusGeral == null) {
	            MenuDAO mdao = new MenuDAO();
	            listaMenusGeral = mdao.listarMenuGeral();
	        }
		return listaMenusGeral;
	}

	public void setListaMenusGeral(List<Menu> listaMenusGeral) {
		this.listaMenusGeral = listaMenusGeral;
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

	public String getRotinaSelecionada() {
		return rotinaSelecionada;
	}

	public void setRotinaSelecionada(String rotinaSelecionada) {
		this.rotinaSelecionada = rotinaSelecionada;
	}

	public List<Menu> getListaMenus() {
		return listaMenus;
	}

	public void setListaMenus(List<Menu> listaMenus) {
		this.listaMenus = listaMenus;
	}

	public List<Menu> getListaMenusPag() {
		return listaMenusPag;
	}

	public void setListaMenusPag(List<Menu> listaMenusPag) {
		this.listaMenusPag = listaMenusPag;
	}

	public String getTipoMenuRel() {
		return tipoMenuRel;
	}

	public void setTipoMenuRel(String tipoMenuRel) {
		this.tipoMenuRel = tipoMenuRel;
	}
}