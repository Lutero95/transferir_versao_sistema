package br.gov.al.maceio.sishosp.financeiro.control;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.JSFUtil;
import br.gov.al.maceio.sishosp.financeiro.dao.DespesaDAO;
import br.gov.al.maceio.sishosp.financeiro.model.DespesaBean;

@ManagedBean
@ViewScoped
public class DespesaController implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private DespesaBean despesa;
    private DespesaBean despesaSelecionada;
    private ArrayList<DespesaBean> listaDespesas;

    public DespesaController() {
        despesa = new DespesaBean();
        despesaSelecionada = null;
        listaDespesas = new ArrayList<DespesaBean>();
        listaDespesas = null;
    }
    
    public void cadastrarDespesa() throws ProjetoException {
        
        DespesaDAO ddao = new DespesaDAO();
        boolean cadastrou = ddao.cadastrarDespesa(despesa);
        
        if(cadastrou == true) {
            
            despesaSelecionada = null;
            listaDespesas = null;
            
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
                "Despesa cadastrada com sucesso!", "Sucesso");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            
            JSFUtil.fecharDialog("dlgCadDespesa");
        } else {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                "Erro ao cadastrarPaciente!", "Erro");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }
    
    public void alterarDespesa() throws ProjetoException {
        
        DespesaDAO ddao = new DespesaDAO();
        boolean alterou = ddao.alterarDespesa(despesaSelecionada);
        
        if(alterou == true) {
            
            despesaSelecionada = null;
            listaDespesas = null;
            
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
                "Despesa alterada com sucesso!", "Sucesso");
            FacesContext.getCurrentInstance().addMessage(null, msg);

            JSFUtil.fecharDialog("dlgAltDespesa");
        } else {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                "Erro ao alterarPaciente!", "Erro");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }
    
    public void excluirDespesa() {
        
        DespesaDAO ddao = new DespesaDAO();
        
        boolean excluiu = false;
        try {
            excluiu = ddao.excluirDespesa(despesaSelecionada);
        } catch(ProjetoException pex) {
            pex.getMessage();
        }
        
        if(excluiu == true) {
            
            despesaSelecionada = null;
            listaDespesas = null;
            
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
                "Despesa excluída com sucesso!", "Sucesso");
            FacesContext.getCurrentInstance().addMessage(null, msg);

            JSFUtil.fecharDialog("dlgExcDespesa");
        } else {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                "Erro ao excluir!", "Erro");
            FacesContext.getCurrentInstance().addMessage(null, msg);

            JSFUtil.fecharDialog("dlgExcDespesa");
        }
    }
    
    public ArrayList<DespesaBean> listaDespesasMtd() throws ProjetoException {

		if (listaDespesas == null) {
			DespesaDAO ddao = new DespesaDAO();
			listaDespesas = ddao.listarDespesas();
		}

		return listaDespesas;

	}
    
    public void limparDados() {
        despesa = new DespesaBean();
    }
    
    public void onRowSelect(DespesaBean desp) {
    	despesaSelecionada = desp;
    	JSFUtil.atualizarComponente("formPrincipal:growl");
        JSFUtil.atualizarComponente("formPrincipal:outBotoes");
    }

    public DespesaBean getDespesa() {
        return despesa;
    }

    public void setDespesa(DespesaBean despesa) {
        this.despesa = despesa;
    }

    public DespesaBean getDespesaSelecionada() {
        return despesaSelecionada;
    }

    public void setDespesaSelecionada(DespesaBean despesaSelecionada) {
        this.despesaSelecionada = despesaSelecionada;
    }

    public List<DespesaBean> getListaDespesas() throws ProjetoException {
        if(listaDespesas == null) {
            DespesaDAO ddao = new DespesaDAO();
            listaDespesas = ddao.listarDespesas();
        }
        return listaDespesas;
    }

    public void setListaDespesas(ArrayList<DespesaBean> listaDespesas) {
        this.listaDespesas = listaDespesas;
    }
}