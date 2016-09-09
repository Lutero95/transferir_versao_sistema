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
import br.gov.al.maceio.sishosp.hosp.model.CboBean;
import br.gov.al.maceio.sishosp.acl.dao.UsuarioDAO;
import br.gov.al.maceio.sishosp.acl.model.PessoaBean;
import br.gov.al.maceio.sishosp.acl.model.UsuarioBean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;

import org.primefaces.context.RequestContext;
import org.primefaces.event.CloseEvent;
import org.primefaces.event.TransferEvent;
import org.primefaces.model.DualListModel;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.MenuModel;

@ManagedBean(name = "MBUsuarioCadastro")
@SessionScoped
//@ViewScoped
public class UsuarioCadastroControll implements Serializable {

    private UsuarioBean usuario;
    private UsuarioBean usuarioEdit;
    private String login;
    private String senha;
    private String idUsuario = "0";
    private UIInput senhaInput;
    private UIInput senhaInputEdit;


    // busca personalizada
    private String valor;
	private String tipoBusca;
    private String rendererSetor;
    private PessoaBean pessoaselecionada;
    private List<PessoaBean> buscapessoa;    
    private List<UsuarioBean> listaUsuario;
    
    //LISTAS
    
    // Dual Sistema
    private DualListModel<Sistema> listaSistemasDual;
    private List<Sistema> listaSistemasSoucer;
    private List<Sistema> listaSistemasTarget;
    private List<Integer> codigoSistema;
    
    private DualListModel<Permissao> listaPermissoesDual;
    private List<Permissao> listaPermissoesSource;
    private List<Permissao> listaPermissoesTarget;

    private DualListModel<Sistema> listaSistemasDualAlt;
    private List<Sistema> listaSistemasSoucerAlt;
    private List<Sistema> listaSistemasTargetAlt;
    private List<Integer> codigoSistemaAlt;

    private String usuarioSelecionado = "0";

    private DualListModel<Permissao> listaPermissoesDualAlt;
    private List<Permissao> listaPermissoesSourceAlt;
    private List<Permissao> listaPermissoesTargetAlt;

    private Integer abaAtiva = 0;
    
    private Perfil pf;
    
    private DualListModel<Menu> listaMenusDual;
    private List<Menu> listaMenusSource;
    private List<Menu> listaMenusTarget;
    
    private DualListModel<Funcao> listaFuncoesDual;
    private List<Funcao> listaFuncoesSource;
    private List<Funcao> listaFuncoesTarget;
    
    private DualListModel<Menu> listaMenusDualEdit;
    private List<Menu> listaMenusSourceEdit;
    private List<Menu> listaMenusTargetEdit;
    
    private DualListModel<Funcao> listaFuncoesDualEdit;
    private List<Funcao> listaFuncoesSourceEdit;
    private List<Funcao> listaFuncoesTargetEdit;
    
    private String perfilSelecionado = "0";
    
    private Integer abaAtivaV2 = 0;




    public UsuarioCadastroControll() {

        // ACL
        usuario = new UsuarioBean();
        usuarioEdit = new UsuarioBean();

        listaUsuario = new ArrayList<UsuarioBean>();
        listaUsuario = null;
        
        listaSistemasDual = null;
        listaSistemasSoucer = new ArrayList<>();
        listaSistemasTarget = new ArrayList<>();
        codigoSistema = new ArrayList<>();

        listaPermissoesDual = null;
        listaPermissoesSource = new ArrayList<>();
        listaPermissoesTarget = new ArrayList<>();
        perfilSelecionado = null;

        listaSistemasDualAlt = null;
        listaSistemasSoucerAlt = new ArrayList<>();
        listaSistemasTargetAlt = new ArrayList<>();
        codigoSistemaAlt = new ArrayList<>();

        listaPermissoesDualAlt = null;
        listaPermissoesSourceAlt = new ArrayList<>();
        listaPermissoesTargetAlt = new ArrayList<>();

        buscapessoa = new ArrayList<>();
        
        pf = new Perfil();

        listaMenusDual = null;
        listaMenusSource = new ArrayList<>();
        listaMenusTarget = new ArrayList<>();
        
        listaFuncoesDual = null;
        listaFuncoesSource = new ArrayList<>();
        listaFuncoesTarget = new ArrayList<>();
        
        listaMenusDualEdit = null;
        listaMenusSourceEdit = new ArrayList<>();
        listaMenusTargetEdit = new ArrayList<>();
        
        listaFuncoesDualEdit = null;
        listaFuncoesSourceEdit = new ArrayList<>();
        listaFuncoesTargetEdit = new ArrayList<>();

    }
    
    public void gravarUsuario() throws ProjetoException {
    	
        UsuarioDAO udao = new UsuarioDAO();
             
        if(listaSistemasDual.getTarget().size() > 0
            && Integer.parseInt(perfilSelecionado) > 0) {
            
            if(usuario.getCodSetor().size() > 0 && usuario.getCodSetor() != null) {
            
                List<Integer> listaSis = new ArrayList<>();

                List<Long> permissoes = new ArrayList<>();
                List<Menu> listaMenusAux = listaMenusDual.getTarget();
                List<Funcao> listaFuncoesAux = listaFuncoesDual.getTarget();

                MenuDAO mdao = new MenuDAO();
                List<Menu> menusPerfil = mdao.listarMenusPerfil(Integer.parseInt(perfilSelecionado));

                MenuMB mmb = new MenuMB();
                List<Menu> listaFiltrada = mmb.filtrarListaMenu(listaMenusAux);

                for(Menu mp : menusPerfil) {
                    for(Menu mf : listaFiltrada) {
                        if(mp.getCodigo().equals(mf.getCodigo())) {
                            listaFiltrada.remove(mf);
                        }
                    }
                }

                PermissaoDAO pmdao = new PermissaoDAO();
                for(Menu m : listaFiltrada) {
                    permissoes.add(pmdao.recIdPermissoesMenu(m.getId()));
                }

                for(Funcao f : listaFuncoesAux) {
                    permissoes.add(pmdao.recIdPermissoesFuncao(f.getId()));
                }

                for(Sistema s : listaSistemasDual.getTarget()) {
                    listaSis.add(s.getId());
                }


                usuario.setIdPerfil(Integer.parseInt(perfilSelecionado));
                usuario.setListaIdSistemas(listaSis);
                usuario.setListaIdPermissoes(permissoes);

                boolean cadastrou = udao.cadastrar(usuario);

                if(cadastrou) {
                    FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
                        "Usuario cadastrado com sucesso!", "Sucesso");
                    FacesContext.getCurrentInstance().addMessage(null, msg);

                    listaUsuario = null;

                    RequestContext.getCurrentInstance().execute("dlgNovoUsuario.hide();");
                } else {
                    FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        "Ocorreu um erro durante o cadastro!", "Erro");
                    FacesContext.getCurrentInstance().addMessage(null, msg);

                    RequestContext.getCurrentInstance().execute("dlgNovoUsuario.hide();");
                }
            } 
        } else {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                "Selecione um perfil e pelo menos um sistema", "Erro");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }
    
    
    public void alterarUsuario() throws ProjetoException {
        UsuarioDAO udao = new UsuarioDAO();

        if(listaSistemasDualAlt.getTarget().size() > 0
            && Integer.parseInt(perfilSelecionado) > 0) {
            
            if(usuarioEdit.getCodSetor().size() > 0 && usuarioEdit.getCodSetor() != null) {

                List<Integer> listaSis = new ArrayList<>();        
                List<Long> listaPerms = new ArrayList<>();
                List<Menu> listaMenusAux = listaMenusDualEdit.getTarget();
                List<Funcao> listaFuncoesAux = listaFuncoesDualEdit.getTarget();

                MenuDAO mdao = new MenuDAO();
                List<Menu> menusPerfil = mdao.listarMenusPerfil(Integer.parseInt(perfilSelecionado));
                MenuMB mmb = new MenuMB();
                List<Menu> listaFiltrada = mmb.filtrarListaMenu(listaMenusAux);
                List<Menu> listaFiltradaaux = mmb.filtrarListaMenu(listaMenusAux);
                for(Menu mp : menusPerfil) {
                    for(Menu mf : listaFiltradaaux) {
                        if(mp.getCodigo().equals(mf.getCodigo())) {
                            listaFiltrada.remove(mf);
                        }
                    }
                }

                PermissaoDAO pmdao = new PermissaoDAO();
                for(Menu m : listaFiltrada) {
                    listaPerms.add(pmdao.recIdPermissoesMenu(m.getId()));
                }

                for(Funcao f : listaFuncoesAux) {
                    listaPerms.add(pmdao.recIdPermissoesFuncao(f.getId()));
                }

                boolean alterou = false;

                for(Sistema s : listaSistemasDualAlt.getTarget()) {
                    listaSis.add(s.getId());
                }

                if(listaPerms.size() > 0) {
                    usuarioEdit.setIdPerfil(Integer.parseInt(perfilSelecionado));
                    usuarioEdit.setListaIdSistemas(listaSis);
                    usuarioEdit.setListaIdPermissoes(listaPerms);
                    alterou = udao.alterar(usuarioEdit);

                } else {
                    usuarioEdit.setIdPerfil(Integer.parseInt(perfilSelecionado));
                    usuarioEdit.setListaIdSistemas(listaSis);
                    alterou = udao.alterarSemPerm(usuarioEdit);
                }

                if(alterou == true) {
                    FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
                        "Dados alterados com sucesso!", "Sucesso");
                    FacesContext.getCurrentInstance().addMessage(null, msg);
	
                    listaUsuario = null;

                    RequestContext.getCurrentInstance().execute("altUsuario.hide();");
                } else {
                    FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        "Ocorreu um erro durante a alteracao!", "Erro");
                    FacesContext.getCurrentInstance().addMessage(null, msg);

                    RequestContext.getCurrentInstance().execute("altUsuario.hide();");
                }
            } 
        } else {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                "Selecione um perfil e um sistema.", "Erro");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }
    
    public void validateSenha(FacesContext context, UIComponent toValidate,
            Object value) {

        String confirmaSenha = (String) value;
        String senha = (String) this.senhaInput.getLocalValue();

        if (senha != null) {
            if (!confirmaSenha.equals(senha)) {
                ((EditableValueHolder) toValidate).setValid(false);

                FacesMessage message = new FacesMessage(
                        " As senhas digitadas diferem!");
                context.addMessage(toValidate.getClientId(context), message);
            }
        }
    }

    public void validateSenhaEdit(FacesContext context, UIComponent toValidate,
            Object value) {

        String confirmaSenha = (String) value;
        String senha = (String) this.senhaInputEdit.getLocalValue();

        if (senha != null) {
            if (!confirmaSenha.equals(senha)) {
                ((EditableValueHolder) toValidate).setValid(false);

                FacesMessage message = new FacesMessage(
                        " As senhas digitadas diferem!");
                context.addMessage(toValidate.getClientId(context), message);
            }
        }
    }
    
    public void alterarSenha() throws ProjetoException {
        UsuarioDAO udao = new UsuarioDAO();
        boolean alterou = udao.alterarSenha(usuarioEdit);

        if (alterou == true) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Senha alterada com sucesso!", "Sucesso");
            FacesContext.getCurrentInstance().addMessage(null, msg);

            RequestContext.getCurrentInstance().execute("altSenha.hide();");
        } else {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Ocorreu um erro durante a alteraÃ§Ã£o!", "Erro");
            FacesContext.getCurrentInstance().addMessage(null, msg);

            RequestContext.getCurrentInstance().execute("altSenha.hide();");
        }
    }
    
    public void verificaLoginCadastrado(FacesContext context,
            UIComponent toValidate, Object value) throws ProjetoException {

       UsuarioDAO icdao = new UsuarioDAO();
        String login = (String) value;

        String isExist = icdao.verificaLoginCadastrado(login);

        if (isExist == "S") {
            ((EditableValueHolder) toValidate).setValid(false);
            FacesMessage message = new FacesMessage(" Login ja em uso");
            context.addMessage(toValidate.getClientId(context), message);

        }
    }
    
    public void onTransferMenu(TransferEvent event) {
        StringBuilder builder = new StringBuilder();

        for(Object item : event.getItems()) {
            builder.append(((Menu) item).getId());
            if(listaMenusTarget.contains(item)) {
                listaMenusTarget.remove(item);
            } else {
                listaMenusTarget.add((Menu) item);
            }
        }        
    }
    
    public void onTransferFuncao(TransferEvent event) {
        StringBuilder builder = new StringBuilder();

        for(Object item : event.getItems()) {
            builder.append(((Funcao) item).getId());
            if(listaFuncoesTarget.contains(item)) {
                listaFuncoesTarget.remove(item);
            } else {
                listaFuncoesTarget.add((Funcao) item);
            }
        }        
    }
    
    public void onTransferMenuEdit(TransferEvent event) {
        StringBuilder builder = new StringBuilder();

        for(Object item : event.getItems()) {
            builder.append(((Menu) item).getId());
            if(listaMenusTargetEdit.contains(item)) {
                listaMenusTargetEdit.remove(item);
            } else {
                listaMenusTargetEdit.add((Menu) item);
            }
        }        
    }
    
    public void onTransferFuncaoEdit(TransferEvent event) {
        StringBuilder builder = new StringBuilder();

        for(Object item : event.getItems()) {
            builder.append(((Funcao) item).getId());
            if(listaFuncoesTargetEdit.contains(item)) {
                listaFuncoesTargetEdit.remove(item);
            } else {
                listaFuncoesTargetEdit.add((Funcao) item);
            }
        }        
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

    public void onTransferPerm(TransferEvent event) {
        StringBuilder builder = new StringBuilder();

        for (Object item : event.getItems()) {
            builder.append(((Permissao) item).getIdAux());
            if (listaSistemasTarget.contains(item)) {
                listaSistemasTarget.remove(item);
            } else {
                listaPermissoesTarget.add((Permissao) item);
            }
        }
    }
    

    
    public void close(CloseEvent event) {
      	usuario = new UsuarioBean();
            usuarioEdit = new UsuarioBean();
            limparCampos(event.getComponent());
      }
      
      public void limparCampos(UIComponent component) {
          if (component instanceof EditableValueHolder) {
              EditableValueHolder evh = (EditableValueHolder) component;
              evh.setSubmittedValue(null);
              evh.setValue(null);
              evh.setLocalValueSet(false);
              evh.setValid(true);
          }
          if (component.getChildCount() > 0) {
              for (UIComponent child : component.getChildren()) {
                  limparCampos(child);
              }
          }
      }

        public void limparDados() {
            usuario = new UsuarioBean();
            usuarioEdit = new UsuarioBean();
            buscapessoa = new ArrayList<>();
            perfilSelecionado = null;
            abaAtiva = 0;
            abaAtivaV2 = 0;
        }
        
        public void limparbuscapessoa() {
            pessoaselecionada = null;
            buscapessoa = new ArrayList<>();
        }
        
        public void limparDualCad() {        
            listaMenusDual = null;
            listaMenusTarget = null;
            listaMenusTarget = new ArrayList<>();
            
            listaFuncoesDual = null;
            listaFuncoesTarget = null;
            listaFuncoesTarget = new ArrayList<>();
            
            listaSistemasDual = null;

            listaSistemasTarget = null;
            listaSistemasTarget = new ArrayList<>();

            codigoSistema = null;
            codigoSistema = new ArrayList<>();
  
        }
        
        public void limparDualAlt() {
            listaMenusDualEdit = null;
            listaMenusSourceEdit = new ArrayList<>();
            listaMenusTargetEdit = new ArrayList<>();
            
            listaFuncoesDualEdit = null;
            listaFuncoesSourceEdit = new ArrayList<>();
            listaFuncoesTargetEdit = new ArrayList<>();
            
            
            listaSistemasDualAlt = null;

            listaSistemasTargetAlt = null;
            listaSistemasTargetAlt = new ArrayList<>();
            
        }

        public void limparDualPerm() {
            listaPermissoesDual = null;

            listaPermissoesTarget = null;
            listaPermissoesTarget = new ArrayList<>();
        }

    

    public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	public String getTipoBusca() {
		return tipoBusca;
	}

	public void setTipoBusca(String tipoBusca) {
		this.tipoBusca = tipoBusca;
	}

	public String getRendererSetor() {
		return rendererSetor;
	}

	public void setRendererSetor(String rendererSetor) {
		this.rendererSetor = rendererSetor;
	}

	public PessoaBean getPessoaselecionada() {
		return pessoaselecionada;
	}

	public void setPessoaselecionada(PessoaBean pessoaselecionada) {
		this.pessoaselecionada = pessoaselecionada;
	}

	public List<PessoaBean> getBuscapessoa() {
		return buscapessoa;
	}

	public void setBuscapessoa(List<PessoaBean> buscapessoa) {
		this.buscapessoa = buscapessoa;
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


	public UsuarioBean getUsuarioEdit() {
		return usuarioEdit;
	}


	public void setUsuarioEdit(UsuarioBean usuarioEdit) {
		this.usuarioEdit = usuarioEdit;
	} 
    
 // ACL
    public DualListModel<Sistema> getListaSistemasDual() throws ProjetoException {
        if (listaSistemasDual == null) {
            listaSistemasSoucer = null;
            listaSistemasTarget = new ArrayList<>();
            getListaSistemasSoucer();
            getListaSistemasTarget();
            listaSistemasDual = new DualListModel<>(listaSistemasSoucer, listaSistemasTarget);
        }
        return listaSistemasDual;
    }

    public void setListaSistemasDual(DualListModel<Sistema> listaSistemasDual) {
        this.listaSistemasDual = listaSistemasDual;
    }

    public List<Sistema> getListaSistemasSoucer() throws ProjetoException {
        if (listaSistemasSoucer == null) {
            UsuarioDAO udao = new UsuarioDAO();
            listaSistemasSoucer = udao.recListaSisSoucerCad();
        }
        return listaSistemasSoucer;
    }

    public void setListaSistemasSoucer(List<Sistema> listaSistemasSoucer) {
        this.listaSistemasSoucer = listaSistemasSoucer;
    }

    public List<Sistema> getListaSistemasTarget() throws NumberFormatException, ProjetoException {
        if (listaSistemasTarget == null) {
            UsuarioDAO udao = new UsuarioDAO();
            listaSistemasSoucer = udao.recListaSisTarget(Long.valueOf(idUsuario));
        }
        return listaSistemasTarget;
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

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public DualListModel<Permissao> getListaPermissoesDual() throws NumberFormatException, ProjetoException {
        if (listaPermissoesDual == null) {
            listaPermissoesSource = null;
            listaPermissoesTarget = new ArrayList<>();
            getListaPermissoesSource();
            getListaPermissoesTarget();
            listaPermissoesDual = new DualListModel<>(listaPermissoesSource, listaPermissoesTarget);
        }
        return listaPermissoesDual;
    }

    public void setListaPermissoesDual(DualListModel<Permissao> listaPermissoesDual) {
        this.listaPermissoesDual = listaPermissoesDual;
    }

    public List<Permissao> getListaPermissoesSource() throws NumberFormatException, ProjetoException {
        if (listaPermissoesSource == null) {
            UsuarioDAO udao = new UsuarioDAO();
            listaPermissoesSource = udao.listarPermSemPerfSource(Integer.parseInt(perfilSelecionado));
        }
        return listaPermissoesSource;
    }

    public void setListaPermissoesSource(List<Permissao> listaPermissoesSource) {
        this.listaPermissoesSource = listaPermissoesSource;
    }

    public List<Permissao> getListaPermissoesTarget() {
        return listaPermissoesTarget;
    }

    public void setListaPermissoesTarget(List<Permissao> listaPermissoesTarget) {
        this.listaPermissoesTarget = listaPermissoesTarget;
    }

    public DualListModel<Sistema> getListaSistemasDualAlt() throws NumberFormatException, ProjetoException {
        if (listaSistemasDualAlt == null) {
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

    public void setListaSistemasSoucerAlt(List<Sistema> listaSistemasSoucerAlt) {
        this.listaSistemasSoucerAlt = listaSistemasSoucerAlt;
    }

    public List<Sistema> getListaSistemasTargetAlt() throws NumberFormatException, ProjetoException {
        if (listaSistemasTargetAlt == null) {
            UsuarioDAO udao = new UsuarioDAO();
            listaSistemasTargetAlt = udao.recListaSisTarget(Long.valueOf(usuarioSelecionado));
        }
        return listaSistemasTargetAlt;
    }

    public void setListaSistemasTargetAlt(List<Sistema> listaSistemasTargetAlt) {
        this.listaSistemasTargetAlt = listaSistemasTargetAlt;
    }

    public List<Integer> getCodigoSistemaAlt() {
        return codigoSistemaAlt;
    }

    public void setCodigoSistemaAlt(List<Integer> codigoSistemaAlt) {
        this.codigoSistemaAlt = codigoSistemaAlt;
    }

    public List<Sistema> getListaSistemasSoucerAlt() throws NumberFormatException, ProjetoException {
        if (listaSistemasSoucerAlt == null) {
            UsuarioDAO udao = new UsuarioDAO();
            listaSistemasSoucerAlt = udao.recListaSisSoucer(Long.valueOf(usuarioSelecionado));
        }
        return listaSistemasSoucerAlt;
    }

    public String getUsuarioSelecionado() {
        return usuarioSelecionado;
    }

    public void setUsuarioSelecionado(String usuarioSelecionado) {
        this.usuarioSelecionado = usuarioSelecionado;
    }

    public DualListModel<Permissao> getListaPermissoesDualAlt() throws NumberFormatException, ProjetoException {
        if (listaPermissoesDualAlt == null) {
            listaPermissoesSourceAlt = null;
            listaPermissoesTargetAlt = null;
            getListaPermissoesSourceAlt();
            getListaPermissoesTargetAlt();
            listaPermissoesDualAlt = new DualListModel<>(listaPermissoesSourceAlt, listaPermissoesTargetAlt);
        }
        return listaPermissoesDualAlt;
    }

    public void setListaPermissoesDualAlt(DualListModel<Permissao> listaPermissoesDualAlt) {
        this.listaPermissoesDualAlt = listaPermissoesDualAlt;
    }

    public List<Permissao> getListaPermissoesSourceAlt() throws NumberFormatException, ProjetoException {
        if (listaPermissoesSourceAlt == null) {
            UsuarioDAO udao = new UsuarioDAO();
            listaPermissoesSourceAlt = udao.listarPermSemPerfAssSource(
                    Integer.parseInt(perfilSelecionado), Integer.parseInt(usuarioSelecionado));
        }
        return listaPermissoesSourceAlt;
    }

    public void setListaPermissoesSourceAlt(List<Permissao> listaPermissoesSourceAlt) {
        this.listaPermissoesSourceAlt = listaPermissoesSourceAlt;
    }

    public List<Permissao> getListaPermissoesTargetAlt() throws NumberFormatException, ProjetoException {
        if (listaPermissoesTargetAlt == null) {
            UsuarioDAO udao = new UsuarioDAO();
            listaPermissoesTargetAlt = udao.listarPermSemPerfAssTarget(Integer.parseInt(usuarioSelecionado));
        }
        return listaPermissoesTargetAlt;
    }

    public void setListaPermissoesTargetAlt(List<Permissao> listaPermissoesTargetAlt) {
        this.listaPermissoesTargetAlt = listaPermissoesTargetAlt;
    }
    
    public DualListModel<Menu> getListaMenusDual() throws NumberFormatException, ProjetoException {
        if(listaMenusDual == null) {
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

    public List<Menu> getListaMenusSource() throws NumberFormatException, ProjetoException {
        if(listaMenusSource == null) {
            MenuDAO mdao = new MenuDAO();
            listaMenusSource = mdao.listarMenuItemSourcerUser(Integer.parseInt(perfilSelecionado));
        }
        return listaMenusSource;
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

    public DualListModel<Funcao> getListaFuncoesDual() throws NumberFormatException, ProjetoException {
        if(listaFuncoesDual == null) {
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

    public List<Funcao> getListaFuncoesSource() throws NumberFormatException, ProjetoException {
        if(listaFuncoesSource == null) {
            FuncaoDAO fdao = new FuncaoDAO();
            listaFuncoesSource = fdao.listarFuncaoItemSourcerUser(Integer.parseInt(perfilSelecionado));
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
    
    public DualListModel<Menu> getListaMenusDualEdit() throws NumberFormatException, ProjetoException {
        if(listaMenusDualEdit == null) {
            listaMenusSourceEdit = null;
            listaMenusTargetEdit = null;
            getListaMenusSourceEdit();
            getListaMenusTargetEdit();
            listaMenusDualEdit = new DualListModel<>(listaMenusSourceEdit, listaMenusTargetEdit);
        }
        return listaMenusDualEdit;
    }

    public void setListaMenusDualEdit(DualListModel<Menu> listaMenusDualEdit) {
        this.listaMenusDualEdit = listaMenusDualEdit;
    }

    public List<Menu> getListaMenusSourceEdit() throws NumberFormatException, ProjetoException {
        if(listaMenusSourceEdit == null) {
            MenuDAO mdao = new MenuDAO();
            listaMenusSourceEdit = mdao.listarMenuItemSourcerEditUser(
                Integer.parseInt(perfilSelecionado), Integer.parseInt(usuarioSelecionado));
        }
        return listaMenusSourceEdit;
    }

    public void setListaMenusSourceEdit(List<Menu> listaMenusSourceEdit) {
        this.listaMenusSourceEdit = listaMenusSourceEdit;
    }

    public List<Menu> getListaMenusTargetEdit() throws NumberFormatException, ProjetoException {
        if(listaMenusTargetEdit == null) {
            MenuDAO mdao = new MenuDAO();
            listaMenusTargetEdit = mdao.listarMenuItemTargetEditUser(Integer.parseInt(usuarioSelecionado));
        }
        return listaMenusTargetEdit;
    }

    public void setListaMenusTargetEdit(List<Menu> listaMenusTargetEdit) {
        this.listaMenusTargetEdit = listaMenusTargetEdit;
    }

    public DualListModel<Funcao> getListaFuncoesDualEdit() throws NumberFormatException, ProjetoException {
        if(listaFuncoesDualEdit == null) {
            listaFuncoesSourceEdit = null;
            listaFuncoesTargetEdit = null;
            getListaFuncoesSourceEdit();
            getListaFuncoesTargetEdit();
            listaFuncoesDualEdit = new DualListModel<>(listaFuncoesSourceEdit, listaFuncoesTargetEdit);
        }
        return listaFuncoesDualEdit;
    }

    public void setListaFuncoesDualEdit(DualListModel<Funcao> listaFuncoesDualEdit) {
        this.listaFuncoesDualEdit = listaFuncoesDualEdit;
    }

    public List<Funcao> getListaFuncoesSourceEdit() throws NumberFormatException, ProjetoException {
        if(listaFuncoesSourceEdit == null) {
            FuncaoDAO fdao = new FuncaoDAO();
            listaFuncoesSourceEdit = fdao.listarFuncaoItemSourcerUserEdit(
                Integer.parseInt(perfilSelecionado), Integer.parseInt(usuarioSelecionado));
        }
        return listaFuncoesSourceEdit;
    }

    public void setListaFuncoesSourceEdit(List<Funcao> listaFuncoesSourceEdit) {
        this.listaFuncoesSourceEdit = listaFuncoesSourceEdit;
    }

    public List<Funcao> getListaFuncoesTargetEdit() throws NumberFormatException, ProjetoException {
        if(listaFuncoesTargetEdit == null) {
            FuncaoDAO fdao = new FuncaoDAO();
            listaFuncoesTargetEdit = fdao.listarFuncaoItemTargetUserEdit(Integer.parseInt(usuarioSelecionado));
        }
        return listaFuncoesTargetEdit;
    }

    public void setListaFuncoesTargetEdit(List<Funcao> listaFuncoesTargetEdit) {
        this.listaFuncoesTargetEdit = listaFuncoesTargetEdit;
    }

    public String getPerfilSelecionado() {
        return perfilSelecionado;
    }

    public void setPerfilSelecionado(String perfilSelecionado) {
        this.perfilSelecionado = perfilSelecionado;
    }
    public UIInput getSenhaInput() {
        return senhaInput;
    }

    public void setSenhaInput(UIInput senhaInput) {
        this.senhaInput = senhaInput;
    }

    public UIInput getSenhaInputEdit() {
        return senhaInputEdit;
    }

    public void setSenhaInputEdit(UIInput senhaInputEdit) {
        this.senhaInputEdit = senhaInputEdit;
    }

	public List<UsuarioBean> getListaUsuario() throws ProjetoException {
		UsuarioDAO uDao = new UsuarioDAO();
		listaUsuario = uDao.buscaUsuarios();
		return listaUsuario;
	}

	public void setListaUsuario(List<UsuarioBean> listaUsuario) {
		this.listaUsuario = listaUsuario;
	}

	public Perfil getPf() {
		return pf;
	}

	public void setPf(Perfil pf) {
		this.pf = pf;
	}
    
    
}