package br.gov.al.maceio.sishosp.hosp.control;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;

import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.hosp.dao.FornecedorDAO;
import br.gov.al.maceio.sishosp.hosp.model.FornecedorBean;




public class FornecedorController {
	
	private Integer abaAtiva = 0;
	private FornecedorBean fornecedor;
	private String cabecalho;
	
	
	
	//BUSCAS
	private String tipo;
	private Integer tipoBuscaFornecedor;
	private String campoBuscaFornecedor;
	private String statusFornecedor;
	private List<FornecedorBean> listaFornecedor;
	public FornecedorController(){
		
		fornecedor = new FornecedorBean();
		
		 //BUSCA
		tipo ="";
		tipoBuscaFornecedor = 1;
		campoBuscaFornecedor = "";
		statusFornecedor = "P";
		
		 listaFornecedor = new ArrayList<>();
		 listaFornecedor = null;
	}
	
	public void gravarFornecedor() throws ProjetoException, SQLException {
		FornecedorDAO udao = new FornecedorDAO();    
                boolean cadastrou = udao.gravarFornecedor(fornecedor);

                if(cadastrou == true) {
                	limparDados();
                    FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
                        "Fornecedor cadastrado com sucesso!", "Sucesso");
                    FacesContext.getCurrentInstance().addMessage(null, msg);

                    listaFornecedor = null;

                } else {
                    FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        "Ocorreu um erro durante o cadastro!", "Erro");
                    FacesContext.getCurrentInstance().addMessage(null, msg);

 
                }
            
    }
	
	public String alterarFornecedor() throws ProjetoException {

		FornecedorDAO rdao = new FornecedorDAO();
         boolean alterou = rdao.alterarFornecedor(fornecedor);

         if(alterou == true) {
             limparDados();
             FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
                 "Fornecedor alterado com sucesso!", "Sucesso");
             FacesContext.getCurrentInstance().addMessage(null, msg);
             return "/pages/sishosp/gerenciarFornecedor.faces?faces-redirect=true";
             //RequestContext.getCurrentInstance().execute("dlgAltMenu.hide();");
         } else {
             FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                 "Ocorreu um erro durante o cadastro!", "Erro");
             FacesContext.getCurrentInstance().addMessage(null, msg);
             return "";
             //RequestContext.getCurrentInstance().execute("dlgAltMenu.hide();");
         }
		
	}
	
	public void excluirFornecedor() throws ProjetoException {
		FornecedorDAO udao = new FornecedorDAO();

        boolean excluio = udao.excluirFornecedor(fornecedor);

        if(excluio == true) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
                "Fornecedor excluido com sucesso!", "Sucesso");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            listaFornecedor = null;
            RequestContext.getCurrentInstance().execute("PF('dialogAtencao').hide();");
        } else {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                "Ocorreu um erro durante a exclusao!", "Erro");
            FacesContext.getCurrentInstance().addMessage(null, msg);

            RequestContext.getCurrentInstance().execute("PF('dialogAtencao').hide();");
        }
	}
	
	public void buscarFornecedores() {

		List<FornecedorBean> listaAux = null;
		listaFornecedor = new ArrayList<>();

		FornecedorDAO adao = new FornecedorDAO();

		listaAux = adao.listarFornecedorBusca(campoBuscaFornecedor,tipoBuscaFornecedor);

		if (listaAux != null && listaAux.size() > 0) {
			// listaAss = null;
			listaFornecedor = listaAux;
		} else {
			// listaAss = null;
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN,
					"Nenhum Fornecedor encontrada.", "Aviso");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}

	}
	
	public void limparBuscaDados() {
		tipoBuscaFornecedor = 1;
		campoBuscaFornecedor = "";
		statusFornecedor = "P";
		listaFornecedor = null;
	}

	public void limparDados(){
		fornecedor = new FornecedorBean();
		
	}
	

	public Integer getAbaAtiva() {
		return abaAtiva;
	}

	public void setAbaAtiva(Integer abaAtiva) {
		this.abaAtiva = abaAtiva;
	}

	public FornecedorBean getFornecedor() {
		return fornecedor;
	}

	public void setFornecedor(FornecedorBean fornecedor) {
		this.fornecedor = fornecedor;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public Integer getTipoBuscaFornecedor() {
		return tipoBuscaFornecedor;
	}

	public void setTipoBuscaFornecedor(Integer tipoBuscaFornecedor) {
		this.tipoBuscaFornecedor = tipoBuscaFornecedor;
	}

	public String getCampoBuscaFornecedor() {
		return campoBuscaFornecedor;
	}

	public void setCampoBuscaFornecedor(String campoBuscaFornecedor) {
		this.campoBuscaFornecedor = campoBuscaFornecedor;
	}

	public String getStatusFornecedor() {
		return statusFornecedor;
	}

	public void setStatusFornecedor(String statusFornecedor) {
		this.statusFornecedor = statusFornecedor;
	}

	public List<FornecedorBean> getListaFornecedor() {
		if (listaFornecedor == null) {

			FornecedorDAO fdao = new FornecedorDAO();
			listaFornecedor = fdao.listarFornecedores();

		}
		return listaFornecedor;
	}

	public void setListaFornecedor(List<FornecedorBean> listaFornecedor) {
		this.listaFornecedor = listaFornecedor;
	}

	
	public String getCabecalho() {
		if(this.tipo.equals("I")){
			cabecalho = "CADASTRO DE FORNECEDOR";
		}else if(this.tipo.equals("A")){
			cabecalho = "ALTERAR FORNECEDOR";
		}
		return cabecalho;
	}

	public void setCabecalho(String cabecalho) {
		this.cabecalho = cabecalho;
	}
	    
	    
}
