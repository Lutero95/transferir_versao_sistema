package br.gov.al.maceio.sishosp.hosp.control;

import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;

import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.hosp.dao.EnderecoDAO;
import br.gov.al.maceio.sishosp.hosp.dao.ProfissaoDAO;
import br.gov.al.maceio.sishosp.hosp.model.EnderecoBean;
import br.gov.al.maceio.sishosp.hosp.model.ProfissaoBean;



public class EnderecoController {
	private Integer abaAtiva = 0;
	//CLASSES HERDADAS
	private EnderecoBean endereco;
	
	//LISTAS
	private List<EnderecoBean> listaMunicipios;
	private List<EnderecoBean> listaBairros;
	
	//BUSCAS
			private String tipo;
			private Integer tipoBuscaMunicipio;
			private String campoBuscaMunicipio;
			private String statusMunicipio;
	
	public EnderecoController(){
		endereco = new EnderecoBean();
		
		 //BUSCA
			tipo ="";
			tipoBuscaMunicipio = 1;
			campoBuscaMunicipio = "";
			statusMunicipio = "P";
		
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
                listaMunicipios = null;
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
         listaMunicipios = null;
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
        listaMunicipios = null;
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
	
	public void buscarMunicipios() {

		List<EnderecoBean> listaAux = null;
		listaMunicipios = new ArrayList<>();

		EnderecoDAO adao = new EnderecoDAO();

		listaAux = adao.buscarTipoMunicipio(campoBuscaMunicipio,tipoBuscaMunicipio);

		if (listaAux != null && listaAux.size() > 0) {
			// listaAss = null;
			listaMunicipios = listaAux;
		} else {
			// listaAss = null;
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN,
					"Nenhum Municipio encontrado.", "Aviso");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}

	}
	
	
	
	public void limparBuscaDados() {
		tipoBuscaMunicipio = 1;
		campoBuscaMunicipio = "";
		statusMunicipio = "P";
		listaMunicipios = null;
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

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public Integer getTipoBuscaMunicipio() {
		return tipoBuscaMunicipio;
	}

	public void setTipoBuscaMunicipio(Integer tipoBuscaMunicipio) {
		this.tipoBuscaMunicipio = tipoBuscaMunicipio;
	}

	public String getCampoBuscaMunicipio() {
		return campoBuscaMunicipio;
	}

	public void setCampoBuscaMunicipio(String campoBuscaMunicipio) {
		this.campoBuscaMunicipio = campoBuscaMunicipio;
	}

	public String getStatusMunicipio() {
		return statusMunicipio;
	}

	public void setStatusMunicipio(String statusMunicipio) {
		this.statusMunicipio = statusMunicipio;
	}

	public Integer getAbaAtiva() {
		return abaAtiva;
	}

	public void setAbaAtiva(Integer abaAtiva) {
		this.abaAtiva = abaAtiva;
	}
	
	
	
}
