package br.gov.al.maceio.sishosp.hosp.control;

import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;

import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.hosp.dao.EnderecoDAO;
import br.gov.al.maceio.sishosp.hosp.model.EnderecoBean;



public class EnderecoController {

	//CLASSES HERDADAS
	private EnderecoBean endereco;
	
	//LISTAS
	private List<EnderecoBean> listaMunicipios;
	private List<EnderecoBean> listaBairros;
	
	public EnderecoController(){
		endereco = new EnderecoBean();
		
		  //LISTAS
		  listaMunicipios = new ArrayList<>();
	      listaMunicipios = null;
	      listaBairros = new ArrayList<>();
	      listaBairros = null;
	}
	
	public void gravarLogradouro() throws ProjetoException {
        EnderecoDAO udao = new EnderecoDAO();

                boolean cadastrou = udao.cadastrarLogradouro(endereco);

                if(cadastrou == true) {
                	limparDados();
                    FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
                        "Logradouro cadastrado com sucesso!", "Sucesso");
                    FacesContext.getCurrentInstance().addMessage(null, msg);

                    

                } else {
                    FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        "Ocorreu um erro durante o cadastro!", "Erro");
                    FacesContext.getCurrentInstance().addMessage(null, msg);

 
                }
            
    }
	
	public void gravarMunicipios() throws ProjetoException {
        EnderecoDAO udao = new EnderecoDAO();

                boolean cadastrou = udao.cadastrarMunicipio(endereco);

                if(cadastrou == true) {
                	limparDados();
                    FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
                        "Municipio cadastrado com sucesso!", "Sucesso");
                    FacesContext.getCurrentInstance().addMessage(null, msg);

                    

                } else {
                    FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        "Ocorreu um erro durante o cadastro!", "Erro");
                    FacesContext.getCurrentInstance().addMessage(null, msg);

 
                }
            
    }
	public void alterarMunicipios() throws ProjetoException {
		
		EnderecoDAO udao = new EnderecoDAO();
         boolean alterou = udao.alterarMunicipio(endereco);

         if(alterou == true) {

             FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
                 "Municipio alterado com sucesso!", "Sucesso");
             FacesContext.getCurrentInstance().addMessage(null, msg);

             //RequestContext.getCurrentInstance().execute("dlgAltMenu.hide();");
         } else {
             FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                 "Ocorreu um erro durante o cadastro!", "Erro");
             FacesContext.getCurrentInstance().addMessage(null, msg);

             //RequestContext.getCurrentInstance().execute("dlgAltMenu.hide();");
         }
		
	}
	
	
	public void excluirLogradourou() throws ProjetoException {
		EnderecoDAO udao = new EnderecoDAO();
        System.out.println("excluio");
        boolean excluio = udao.excluirLogradouro(endereco);

        if(excluio == true) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
                "Logradouro excluido com sucesso!", "Sucesso");
            FacesContext.getCurrentInstance().addMessage(null, msg);

            RequestContext.getCurrentInstance().execute("PF('dialogAtencao').hide();");
        } else {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                "Ocorreu um erro durante a exclusao!", "Erro");
            FacesContext.getCurrentInstance().addMessage(null, msg);

            RequestContext.getCurrentInstance().execute("PF('dialogAtencao').hide();");
        }
    
}
	
	public void excluirMunicipios() throws ProjetoException {
		EnderecoDAO udao = new EnderecoDAO();
        System.out.println("excluio");
        boolean excluio = udao.excluirMunicipio(endereco);

        if(excluio == true) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
                "Municipio excluido com sucesso!", "Sucesso");
            FacesContext.getCurrentInstance().addMessage(null, msg);

            RequestContext.getCurrentInstance().execute("PF('dialogAtencao').hide();");
        } else {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                "Ocorreu um erro durante a exclusao!", "Erro");
            FacesContext.getCurrentInstance().addMessage(null, msg);

            RequestContext.getCurrentInstance().execute("PF('dialogAtencao').hide();");
        }
    
}
	
	public void limparDados(){
		endereco = new EnderecoBean();
		
		
	}
	
	public EnderecoBean getEndereco() {
		return endereco;
	}

	public void setEndereco(EnderecoBean endereco) {
		this.endereco = endereco;
	}

	public List<EnderecoBean> getListaMunicipios() {
		 if(listaMunicipios == null) {
				
	            EnderecoDAO fdao = new EnderecoDAO();
	            listaMunicipios = fdao.listaMunicipios();
	      
	        }
		return listaMunicipios;
	}



	public void setListaMunicipios(List<EnderecoBean> listaMunicipios) {
		this.listaMunicipios = listaMunicipios;
	}

	public List<EnderecoBean> getListaBairros() {
		 if(listaBairros == null) {
				
	            EnderecoDAO fdao = new EnderecoDAO();
	            listaBairros= fdao.listaBairros();
	      
	        }
		return listaBairros;
	}

	public void setListaBairros(List<EnderecoBean> listaBairros) {
		this.listaBairros = listaBairros;
	}
	
	
	
}
