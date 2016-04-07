package br.gov.al.maceio.sishosp.acl.control;


import br.gov.al.maceio.sishosp.acl.dao.FuncaoDAO;
import br.gov.al.maceio.sishosp.acl.dao.MenuDAO;
import br.gov.al.maceio.sishosp.acl.dao.PermissaoDAO;
import br.gov.al.maceio.sishosp.acl.dao.SistemaDAO;
import br.gov.al.maceio.sishosp.acl.model.Funcao;
import br.gov.al.maceio.sishosp.acl.model.Menu;
import br.gov.al.maceio.sishosp.acl.model.Perfil;
import br.gov.al.maceio.sishosp.acl.model.Permissao;
import br.gov.al.maceio.sishosp.acl.model.Sistema;
import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.acl.dao.IUsuarioDAO;
import br.gov.al.maceio.sishosp.acl.dao.PessoaDAO;
import br.gov.al.maceio.sishosp.acl.dao.UsuarioDAO;
import br.gov.al.maceio.sishosp.acl.model.PessoaBean;
import br.gov.al.maceio.sishosp.acl.model.UsuarioBean;
import br.gov.al.maceio.sishosp.acl.model.SecretariaBean;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;

import org.primefaces.context.RequestContext;
import org.primefaces.event.TransferEvent;
import org.primefaces.model.DualListModel;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.MenuModel;

@ManagedBean(name = "MBUsuarioCadastro")
@ViewScoped
public class UsuarioCadastroControll implements Serializable {

    private UsuarioBean usuario;
    private String login;
    private String senha;


    // busca personalizada
    private String valor;
    private String tipoBusca;
    private String rendererSetor;
    private PessoaBean pessoaselecionada;
    private List<PessoaBean> buscapessoa;    
    

    private Integer abaAtiva = 0;


    private Integer abaAtivaV2 = 0;


    public UsuarioCadastroControll() {

        // ACL
     
        
       
        usuario = new UsuarioBean();
        

    }
        
    

 
 
    
    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public UsuarioBean getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioBean usuario) {
        this.usuario = usuario;
    }

    public Integer getAbaAtiva() {
        return abaAtiva;
    }

    public void setAbaAtiva(Integer abaAtiva) {
        this.abaAtiva = abaAtiva;
    }

    
    public Integer getAbaAtivaV2() {
        return abaAtivaV2;
    }

    public void setAbaAtivaV2(Integer abaAtivaV2) {
        this.abaAtivaV2 = abaAtivaV2;
    } 
}