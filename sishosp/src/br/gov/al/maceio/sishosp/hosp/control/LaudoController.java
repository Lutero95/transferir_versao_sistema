package br.gov.al.maceio.sishosp.hosp.control;

import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;

import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.hosp.dao.LaudoDAO;
import br.gov.al.maceio.sishosp.hosp.model.EquipeBean;
import br.gov.al.maceio.sishosp.hosp.model.FornecedorBean;
import br.gov.al.maceio.sishosp.hosp.model.GrupoBean;
import br.gov.al.maceio.sishosp.hosp.model.LaudoBean;
import br.gov.al.maceio.sishosp.hosp.model.PacienteBean;
import br.gov.al.maceio.sishosp.hosp.model.ProcedimentoBean;
import br.gov.al.maceio.sishosp.hosp.model.ProfissionalBean;
import br.gov.al.maceio.sishosp.hosp.model.ProgramaBean;
import br.gov.al.maceio.sishosp.hosp.model.TipoAtendimentoBean;


public class LaudoController {
	private Integer abaAtiva = 0;
	private LaudoBean laudo;
	private PacienteBean paciente;
	private GrupoBean grupo;
	private ProgramaBean programa;
	private TipoAtendimentoBean tipoAt;
	private ProfissionalBean profissional;
	private EquipeBean equipe;
	private ProcedimentoBean procedimento;
	private FornecedorBean fornecedor;
	
	private String cabecalho;
	
	//LISTAS
	private List<LaudoBean> listaLaudo;
	
	//BUSCAS
	private String tipo;
	private Integer tipoBuscaLaudo;
	private String campoBuscaLaudo;
	private String statusLaudo;
	
	public LaudoController(){
		//CLASSES
		laudo = new LaudoBean();
		this.tipoAt = null;//new TipoAtendimentoBean();
		this.equipe = null;
		 //BUSCA
		tipo ="";
		tipoBuscaLaudo = 1;
		campoBuscaLaudo = "";
		statusLaudo = "P";
		this.cabecalho = "";
		
		 listaLaudo = new ArrayList<>();
		 listaLaudo = null;

	}
	
	public void gravarLaudo() throws ProjetoException {
		LaudoDAO udao = new LaudoDAO();    
                boolean cadastrou = udao.cadastrarLaudo(laudo);
               
                if(cadastrou == true) {
                	limparDados();
                    FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
                        "Laudo cadastrado com sucesso!", "Sucesso");
                    FacesContext.getCurrentInstance().addMessage(null, msg);
                    listaLaudo = null;
                    

                } else {
                    FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        "Ocorreu um erro durante o cadastro!", "Erro");
                    FacesContext.getCurrentInstance().addMessage(null, msg);

 
                }
            
    }
	
	public String alterarLaudo() throws ProjetoException {

		LaudoDAO rdao = new LaudoDAO();
         boolean alterou = rdao.alterarLaudo(laudo);
         listaLaudo = null;
         if(alterou == true) {
             limparDados();
             FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
                 "Laudo alterado com sucesso!", "Sucesso");
             FacesContext.getCurrentInstance().addMessage(null, msg);
             return "/pages/sishosp/gerenciarLaudo.faces?faces-redirect=true";
             //RequestContext.getCurrentInstance().execute("dlgAltMenu.hide();");
         } else {
             FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                 "Ocorreu um erro durante o cadastro!", "Erro");
             FacesContext.getCurrentInstance().addMessage(null, msg);
             return "";
             //RequestContext.getCurrentInstance().execute("dlgAltMenu.hide();");
         }
		
	}
	
	public void excluirLaudo() throws ProjetoException {
		LaudoDAO udao = new LaudoDAO();

        boolean excluio = udao.excluirLaudo(laudo);
        
        if(excluio == true) {
        	
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
                "Laudo excluido com sucesso!", "Sucesso");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            listaLaudo = null;
            RequestContext.getCurrentInstance().execute("PF('dialogAtencao').hide();");
        } else {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                "Ocorreu um erro durante a exclusao!", "Erro");
            FacesContext.getCurrentInstance().addMessage(null, msg);

            RequestContext.getCurrentInstance().execute("PF('dialogAtencao').hide();");
        }
	}
	
	public void gravarLaudoApac() throws ProjetoException {
		LaudoDAO udao = new LaudoDAO();    
                boolean cadastrou = udao.cadastrarLaudoApac(laudo);
               
                if(cadastrou == true) {
                	limparDados();
                    FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
                        "Laudo Apac cadastrado com sucesso!", "Sucesso");
                    FacesContext.getCurrentInstance().addMessage(null, msg);
                    listaLaudo = null;
                    

                } else {
                    FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        "Ocorreu um erro durante o cadastro!", "Erro");
                    FacesContext.getCurrentInstance().addMessage(null, msg);

 
                }
            
    }
	
	public String alterarLaudoApac() throws ProjetoException {

		LaudoDAO rdao = new LaudoDAO();
         boolean alterou = rdao.alterarLaudoApac(laudo);
         listaLaudo = null;
         if(alterou == true) {
             limparDados();
             FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
                 "Laudo Apac alterado com sucesso!", "Sucesso");
             FacesContext.getCurrentInstance().addMessage(null, msg);
             return "/pages/sishosp/gerenciarEscola.faces?faces-redirect=true";
             //RequestContext.getCurrentInstance().execute("dlgAltMenu.hide();");
         } else {
             FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                 "Ocorreu um erro durante o cadastro!", "Erro");
             FacesContext.getCurrentInstance().addMessage(null, msg);
             return "";
             //RequestContext.getCurrentInstance().execute("dlgAltMenu.hide();");
         }
		
	}
	
	public void excluirLaudoApac() throws ProjetoException {
		LaudoDAO udao = new LaudoDAO();

        boolean excluio = udao.excluirLaudoApac(laudo);
        
        if(excluio == true) {
        	
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
                "Laudo Apac excluido com sucesso!", "Sucesso");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            listaLaudo = null;
            RequestContext.getCurrentInstance().execute("PF('dialogAtencao').hide();");
        } else {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                "Ocorreu um erro durante a exclusao!", "Erro");
            FacesContext.getCurrentInstance().addMessage(null, msg);

            RequestContext.getCurrentInstance().execute("PF('dialogAtencao').hide();");
        }
	}
	
	public void gravarLaudoBpi() throws ProjetoException {
		LaudoDAO udao = new LaudoDAO();  
		
                boolean cadastrou = udao.cadastrarLaudoBpi(laudo);
                
                if(cadastrou == true) {
                	limparDados();
                    FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
                        "Laudo Bpi cadastrado com sucesso!", "Sucesso");
                    FacesContext.getCurrentInstance().addMessage(null, msg);

                    

                } else {
                    FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        "Ocorreu um erro durante o cadastro!", "Erro");
                    FacesContext.getCurrentInstance().addMessage(null, msg);

 
                }
            
    }
	
	public void alterarLaudoBpi() throws ProjetoException {

		LaudoDAO rdao = new LaudoDAO();
         boolean alterou = rdao.alterarLaudoBpi(laudo);

         if(alterou == true) {

             FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
                 "Laudo Bpi alterado com sucesso!", "Sucesso");
             FacesContext.getCurrentInstance().addMessage(null, msg);

             //RequestContext.getCurrentInstance().execute("dlgAltMenu.hide();");
         } else {
             FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                 "Ocorreu um erro durante o cadastro!", "Erro");
             FacesContext.getCurrentInstance().addMessage(null, msg);

             //RequestContext.getCurrentInstance().execute("dlgAltMenu.hide();");
         }
		
	}
	
	public void excluirLaudoBpi() throws ProjetoException {
		LaudoDAO udao = new LaudoDAO();

        boolean excluio = udao.excluirLaudoBpi(laudo);

        if(excluio == true) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
                "Laudo Bpi excluido com sucesso!", "Sucesso");
            FacesContext.getCurrentInstance().addMessage(null, msg);

            //RequestContext.getCurrentInstance().execute("PF('dialogAtencao').hide();");
        } else {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                "Ocorreu um erro durante a exclusao!", "Erro");
            FacesContext.getCurrentInstance().addMessage(null, msg);

            //RequestContext.getCurrentInstance().execute("PF('dialogAtencao').hide();");
        }
	}
	
	public void buscarLaudo() {

		List<LaudoBean> listaAux = null;
		listaLaudo = new ArrayList<>();

		LaudoDAO adao = new LaudoDAO();

		listaAux = adao.buscarTipoLaudo(campoBuscaLaudo,tipoBuscaLaudo);

		if (listaAux != null && listaAux.size() > 0) {
			// listaAss = null;
			listaLaudo = listaAux;
		} else {
			// listaAss = null;
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN,
					"Nenhum Laudo encontrada.", "Aviso");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}

	}
	
	public void limparBuscaDados() {
		tipoBuscaLaudo = 1;
		campoBuscaLaudo = "";
		statusLaudo = "P";
		listaLaudo = null;
	}
	
	public void limparDados(){
		laudo = new LaudoBean();
		paciente = new PacienteBean();
		grupo = new GrupoBean();
		programa = new ProgramaBean();
		profissional = new ProfissionalBean();
		equipe = new EquipeBean();
		procedimento = new ProcedimentoBean();
		
	
	}

	public Integer getAbaAtiva() {
		return abaAtiva;
	}

	public void setAbaAtiva(Integer abaAtiva) {
		this.abaAtiva = abaAtiva;
	}

	public LaudoBean getLaudo() {
		return laudo;
	}

	public void setLaudo(LaudoBean laudo) {
		this.laudo = laudo;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public Integer getTipoBuscaLaudo() {
		return tipoBuscaLaudo;
	}

	public void setTipoBuscaLaudo(Integer tipoBuscaLaudo) {
		this.tipoBuscaLaudo = tipoBuscaLaudo;
	}

	public String getCampoBuscaLaudo() {
		return campoBuscaLaudo;
	}

	public void setCampoBuscaLaudo(String campoBuscaLaudo) {
		this.campoBuscaLaudo = campoBuscaLaudo;
	}

	public String getStatusLaudo() {
		return statusLaudo;
	}

	public void setStatusLaudo(String statusLaudo) {
		this.statusLaudo = statusLaudo;
	}
	
	public PacienteBean getPaciente() {
		return paciente;
	}

	public void setPaciente(PacienteBean paciente) {
		this.paciente = paciente;
	}

	public GrupoBean getGrupo() {
		return grupo;
	}

	public void setGrupo(GrupoBean grupo) {
		this.grupo = grupo;
	}

	public ProgramaBean getPrograma() {
		return programa;
	}

	public void setPrograma(ProgramaBean programa) {
		this.programa = programa;
	}

	public TipoAtendimentoBean getTipoAt() {
		return tipoAt;
	}

	public void setTipoAt(TipoAtendimentoBean tipoAt) {
		this.tipoAt = tipoAt;
	}

	public ProfissionalBean getProfissional() {
		return profissional;
	}

	public void setProfissional(ProfissionalBean profissional) {
		this.profissional = profissional;
	}

	public EquipeBean getEquipe() {
		return equipe;
	}

	public void setEquipe(EquipeBean equipe) {
		this.equipe = equipe;
	}

	public ProcedimentoBean getProcedimento() {
		return procedimento;
	}

	public void setProcedimento(ProcedimentoBean procedimento) {
		this.procedimento = procedimento;
	}

	public String getCabecalho() {
		if(this.tipo.equals("I")){
			cabecalho = "CADASTRO DE LAUDO";
		}else if(this.tipo.equals("A")){
			cabecalho = "ALTERAR LAUDO";
		}
		return cabecalho;
	}

	public void setCabecalho(String cabecalho) {
		this.cabecalho = cabecalho;
	}

	public FornecedorBean getFornecedor() {
		return fornecedor;
	}

	public void setFornecedor(FornecedorBean fornecedor) {
		this.fornecedor = fornecedor;
	}

	public List<LaudoBean> getListaLaudo() {
		if (listaLaudo == null) {

			LaudoDAO fdao = new LaudoDAO();
			listaLaudo = fdao.listaLaudos();
		}
		return listaLaudo;
	}

	public void setListaLaudo(List<LaudoBean> listaLaudo) {
		this.listaLaudo = listaLaudo;
	}

	    
	    

}
