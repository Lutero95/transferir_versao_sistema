package br.gov.al.maceio.sishosp.hosp.control;

import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.primefaces.event.SelectEvent;

import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.hosp.dao.EscolaridadeDAO;
import br.gov.al.maceio.sishosp.hosp.model.EscolaridadeBean;



public class EscolaridadeController {
	
	private EscolaridadeBean escolaridadegeral;
	private EscolaridadeBean escolaridadeselecionado,escolaridadeselecionadoaux, escolaridadebuscarapida, escolaridadeselecionadocomposicao, escolaridadeSelecionadoExclusao;
	private EscolaridadeBean escolaridadeSuggestion;

	
	public EscolaridadeController(){
		
		
	}
	
	
public void buscaEscolaridadeCod(Integer codescolaridade) throws Exception {
		
		EscolaridadeBean escolaridade = new EscolaridadeBean();
		
		
		Integer in = (Integer)Integer.valueOf(codescolaridade);	
		
		EscolaridadeDAO icdao = new EscolaridadeDAO();
		
		escolaridade=icdao.buscaescolaridadecodigo(Integer.valueOf(codescolaridade)); 
		if (escolaridade.getCodescolaridade()!=null){
		escolaridadeselecionado = escolaridade;
		escolaridadebuscarapida = new EscolaridadeBean();
		
		escolaridadegeral  = escolaridadeselecionado;
		 icdao = new EscolaridadeDAO();
	
		}
		else
		{
			escolaridadebuscarapida = new EscolaridadeBean();
			FacesMessage message = new FacesMessage(
					" Código da Escolaridade incorreto!");
			 FacesContext.getCurrentInstance().addMessage(null, message);
		}
		
		
	}   
	
	public List<EscolaridadeBean> completeText(String query) throws ProjetoException {
		 List<EscolaridadeBean> result = new ArrayList<EscolaridadeBean>();
	        EscolaridadeDAO icdao = new EscolaridadeDAO();
          result = icdao.buscaescolaridade(query);    
	        return result;
	    }
	    public void onItemSelect(SelectEvent event) throws Exception {
	    	
	    	EscolaridadeBean prodsel = new EscolaridadeBean();
	    	prodsel =(EscolaridadeBean) event.getObject();
	    	
	    	EscolaridadeDAO dao = new EscolaridadeDAO();
	    	buscaEscolaridadeCod(prodsel.getCodescolaridade());
	    	escolaridadeSuggestion = new EscolaridadeBean();
	    	escolaridadeSuggestion = null;
	       // FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Item Selected", prodsel.getDescricao()));
	    }


		public EscolaridadeBean getEscolaridadegeral() {
			return escolaridadegeral;
		}


		public void setEscolaridadegeral(EscolaridadeBean escolaridadegeral) {
			this.escolaridadegeral = escolaridadegeral;
		}


		public EscolaridadeBean getEscolaridadeselecionado() {
			return escolaridadeselecionado;
		}


		public void setEscolaridadeselecionado(EscolaridadeBean escolaridadeselecionado) {
			this.escolaridadeselecionado = escolaridadeselecionado;
		}


		public EscolaridadeBean getEscolaridadeselecionadoaux() {
			return escolaridadeselecionadoaux;
		}


		public void setEscolaridadeselecionadoaux(
				EscolaridadeBean escolaridadeselecionadoaux) {
			this.escolaridadeselecionadoaux = escolaridadeselecionadoaux;
		}


		public EscolaridadeBean getEscolaridadebuscarapida() {
			return escolaridadebuscarapida;
		}


		public void setEscolaridadebuscarapida(EscolaridadeBean escolaridadebuscarapida) {
			this.escolaridadebuscarapida = escolaridadebuscarapida;
		}


		public EscolaridadeBean getEscolaridadeselecionadocomposicao() {
			return escolaridadeselecionadocomposicao;
		}


		public void setEscolaridadeselecionadocomposicao(
				EscolaridadeBean escolaridadeselecionadocomposicao) {
			this.escolaridadeselecionadocomposicao = escolaridadeselecionadocomposicao;
		}


		public EscolaridadeBean getEscolaridadeSelecionadoExclusao() {
			return escolaridadeSelecionadoExclusao;
		}


		public void setEscolaridadeSelecionadoExclusao(
				EscolaridadeBean escolaridadeSelecionadoExclusao) {
			this.escolaridadeSelecionadoExclusao = escolaridadeSelecionadoExclusao;
		}


		public EscolaridadeBean getEscolaridadeSuggestion() {
			return escolaridadeSuggestion;
		}


		public void setEscolaridadeSuggestion(EscolaridadeBean escolaridadeSuggestion) {
			this.escolaridadeSuggestion = escolaridadeSuggestion;
		}
	    
	    
}
