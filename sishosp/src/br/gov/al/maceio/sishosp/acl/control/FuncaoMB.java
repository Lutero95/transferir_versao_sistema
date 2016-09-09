package br.gov.al.maceio.sishosp.acl.control;

import br.gov.al.maceio.sishosp.acl.model.Funcao;
import br.gov.al.maceio.sishosp.acl.dao.FuncaoDAO;
import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;

import java.io.Serializable;
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
public class FuncaoMB  implements Serializable{

    private Funcao funcao;
    private List<Funcao> listaFuncoes;

    private String valorBusca;
    private String sisBusca;

    public FuncaoMB() {
        funcao = new Funcao();
        funcao.setAtiva(true);
        listaFuncoes = new ArrayList<>();
        listaFuncoes = null;   
        
        valorBusca = "";
        sisBusca = "0";
    }

    public void cadastrarFuncao() throws ProjetoException {
        
        FuncaoDAO fdao = new FuncaoDAO();
        boolean cadastrou = fdao.cadastrarFuncao(funcao);

        if(cadastrou == true) {

            listaFuncoes = null;

            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
                "Função cadastrada com sucesso!", "Sucesso");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            
            RequestContext.getCurrentInstance().execute("PF('dlgCadFuncao').hide();");
        } else {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                "Ocorreu um erro durante o cadastro!", "Erro");
            FacesContext.getCurrentInstance().addMessage(null, msg);

            RequestContext.getCurrentInstance().execute("PF('dlgCadFuncao').hide();");
        }
    }

    public void alterarFuncao() throws ProjetoException {

        FuncaoDAO fdao = new FuncaoDAO();
        boolean alterou = fdao.alterarFuncao(funcao);

        if(alterou == true) {

            listaFuncoes = null;

            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
                "Função alterada com sucesso!", "Sucesso");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            
            RequestContext.getCurrentInstance().execute("PF('dlgAltFuncao').hide();");
        } else {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                "Ocorreu um erro durante a alteração!", "Erro");
            FacesContext.getCurrentInstance().addMessage(null, msg);

            RequestContext.getCurrentInstance().execute("PF('dlgAltFuncao').hide();");
        }
    }

    public void excluirFuncao() throws ProjetoException {
        
        FuncaoDAO fdao = new FuncaoDAO();
        boolean excluiu = fdao.excluirFuncao(funcao);

        if(excluiu == true) {
            
            listaFuncoes = null;
            
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
                "Função excluida com sucesso!", "Sucesso");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            
            RequestContext.getCurrentInstance().execute("PF('dlgExcFuncao').hide();");
        } else {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                "Ocorreu um erro durante a exclusão!", "Erro");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            
            RequestContext.getCurrentInstance().execute("PF('dlgExcFuncao').hide();");
        }       
    }
    
    public void buscarFuncao() {
        FuncaoDAO fdao = new FuncaoDAO();
        List<Funcao> listaAux = null;
        //List<Funcao> listaAux = fdao.buscarFuncaoDescSis(valorBusca, Integer.parseInt(sisBusca));
        
        if(listaAux != null && listaAux.size() > 0) {
            listaFuncoes = null;
            listaFuncoes = listaAux;
        } else {
            listaFuncoes = null;
            
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN,
                "Função não encontrada!", "Aviso");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } 
    }
    
    public String verificarBolTab(boolean ativo) {
        if(ativo == true) {
            return "../../imgs/status_green.png";
        } else {
            return "../../imgs/status_red.png";
        }
    }
    
    public void limparDados() {
        funcao = new Funcao();
        sisBusca = "0";
    }
    
    public void limparBusca() {
        valorBusca = "";
        sisBusca = "0";
        listaFuncoes = null;
    }
    
    public Funcao getFuncao() {
        return funcao;
    }

    public void setFuncao(Funcao funcao) {
        this.funcao = funcao;
    }
    
    public List<Funcao> getListaFuncoes() throws ProjetoException {
        if (listaFuncoes == null) {
            FuncaoDAO fdao = new FuncaoDAO();
            listaFuncoes = fdao.listarFuncoes();
        }
        return listaFuncoes;
    }

    public void setListaFuncoes(List<Funcao> listaFuncoes) {
        this.listaFuncoes = listaFuncoes;
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
}