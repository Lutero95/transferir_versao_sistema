package br.gov.al.maceio.sishosp.financeiro.control;

import java.io.Serializable;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;

import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.financeiro.dao.BancoDAO;
import br.gov.al.maceio.sishosp.financeiro.model.BancoBean;

@ManagedBean
@ViewScoped
public class BancoController implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private BancoBean banco;    
    private BancoBean bancoSelecionado;    
    private List<BancoBean> listaBancos;
    private List<BancoBean> listaBancosAtivos;
    
    private Integer rendTipoConta;

    public BancoController() {
        banco = new BancoBean();
        bancoSelecionado = null;
        listaBancos = null;
        listaBancosAtivos = null;
        rendTipoConta = 0;
    }
    
    public void cadastrarBanco() throws ProjetoException {
        
        BancoDAO bdao = new BancoDAO();
        boolean cadastrou = bdao.cadastrarBanco(banco);
        
        if(cadastrou == true) {
            
            bancoSelecionado = null;
            listaBancos = null;
            
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
                "Banco cadastrado com sucesso!", "Sucesso");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            
            RequestContext.getCurrentInstance().execute("PF('dlgCadBanco').hide();");
        } else {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                "Erro ao cadastrarPaciente!", "Erro");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }
    
    public void alterarBanco() throws ProjetoException {
        
        BancoDAO bdao = new BancoDAO();
        boolean alterou = bdao.alterarBanco(bancoSelecionado);
        
        if(alterou == true) {
            
            bancoSelecionado = null;
            listaBancos = null;
            
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
                "Banco alterado com sucesso!", "Sucesso");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            
            RequestContext.getCurrentInstance().execute("PF('dlgAltBanco').hide();");
        } else {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                "Erro ao alterarPaciente!", "Erro");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }
    
    public void excluirBanco() {
    	
    	BancoDAO bdao = new BancoDAO();
        
        boolean excluiu = false;
        try {
            excluiu = bdao.excluirBanco(bancoSelecionado);
        } catch(ProjetoException pex) {
            pex.getMessage();
        }
        
        if(excluiu == true) {
            
            bancoSelecionado = null;
            listaBancos = null;
            
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
                "Banco excluído com sucesso!", "Sucesso");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            
            RequestContext.getCurrentInstance().execute("PF('dlgExcBanco').hide();");
        } else {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                "Erro ao excluir!", "Erro");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            
            RequestContext.getCurrentInstance().execute("PF('dlgExcBanco').hide();");
        }
    }
    
    public void verificaContaCaixa() {
        
        if(this.banco.getContaCaixa().equals("S")) {
            rendTipoConta = 0;
        } else {
            rendTipoConta = 1;
        }
        
    }
    
    public void verificaContaCaixaAlt() {
        if(this.bancoSelecionado.getContaCaixa()!=null) {
        if(this.bancoSelecionado.getContaCaixa().equals("S")) {
            rendTipoConta = 0;
        } else {
            rendTipoConta = 1;
        }
        }
    }
    
    public void onRowSelect(BancoBean bancoEsc) {
    	if(bancoEsc != null) {
    		bancoSelecionado = bancoEsc;
    	}
    	else {
    		bancoSelecionado = null;
    	}
    	
    	RequestContext.getCurrentInstance().update("formPrincipal:outBotoes");
    	RequestContext.getCurrentInstance().update("formPrincipal:growl");
    }
    
    public void limparDados() {
        banco = new BancoBean();
        rendTipoConta = 0;
    }

    public BancoBean getBanco() {
        return banco;
    }

    public void setBanco(BancoBean banco) {
        this.banco = banco;
    }

    public List<BancoBean> getListaBancos() throws ProjetoException {
        if(listaBancos == null) {
            BancoDAO icdao = new BancoDAO();
            listaBancos = icdao.listarBancos();
        }
        return listaBancos;
    }

    public void setListaBancos(List<BancoBean> listaBancos) {
        this.listaBancos = listaBancos;
    }

    public List<BancoBean> getListaBancosAtivos() throws ProjetoException {
        if(listaBancosAtivos == null) {
            BancoDAO icdao = new BancoDAO();
            listaBancosAtivos = icdao.listarBancosAtivos();
        }
        return listaBancosAtivos;
    }

    public void setListaBancosAtivos(List<BancoBean> listaBancosAtivos) {
        this.listaBancosAtivos = listaBancosAtivos;
    }

    public BancoBean getBancoSelecionado() {
        return bancoSelecionado;
    }

    public void setBancoSelecionado(BancoBean bancoSelecionado) {
        this.bancoSelecionado = bancoSelecionado;
    }

    public Integer getRendTipoConta() {
        return rendTipoConta;
    }

    public void setRendTipoConta(Integer rendTipoConta) {
        this.rendTipoConta = rendTipoConta;
    }
}