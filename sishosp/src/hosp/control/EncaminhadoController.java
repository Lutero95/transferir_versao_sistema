package br.gov.al.maceio.sishosp.hosp.control;

import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.primefaces.event.SelectEvent;

import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.hosp.dao.EncaminhadoDAO;
import br.gov.al.maceio.sishosp.hosp.model.EncaminhadoBean;



public class EncaminhadoController {
	
	private EncaminhadoBean encaminhadogeral;
	private EncaminhadoBean encaminhadoselecionado,encaminhadoselecionadoaux, encaminhadobuscarapida, encaminhadoselecionadocomposicao, encaminhadoSelecionadoExclusao;
	private EncaminhadoBean encaminhadoSuggestion;

	
	public EncaminhadoController(){
		
		
	}
	
	
public void buscaEncaminhadoCod(Integer codencaminhado) throws Exception {
		
		EncaminhadoBean encaminhado = new EncaminhadoBean();
		
		
		Integer in = (Integer)Integer.valueOf(codencaminhado);	
		
		EncaminhadoDAO icdao = new EncaminhadoDAO();
		
		encaminhado=icdao.buscaencaminhadocodigo(Integer.valueOf(codencaminhado)); 
		if (encaminhado.getCodencaminhado()!=null){
		encaminhadoselecionado = encaminhado;
		encaminhadobuscarapida = new EncaminhadoBean();
		
		encaminhadogeral  = encaminhadoselecionado;
		 icdao = new EncaminhadoDAO();
	
		}
		else
		{
			encaminhadobuscarapida = new EncaminhadoBean();
			FacesMessage message = new FacesMessage(
					" Código do Encaminhado incorreto!");
			 FacesContext.getCurrentInstance().addMessage(null, message);
		}
		
		
	}   
	
	public List<EncaminhadoBean> completeText(String query) throws ProjetoException {
		 List<EncaminhadoBean> result = new ArrayList<EncaminhadoBean>();
	        EncaminhadoDAO icdao = new EncaminhadoDAO();
          result = icdao.buscaencaminhado(query);    
	        return result;
	    }
	    public void onItemSelect(SelectEvent event) throws Exception {
	    	
	    	EncaminhadoBean prodsel = new EncaminhadoBean();
	    	prodsel =(EncaminhadoBean) event.getObject();
	    	
	    	EncaminhadoDAO dao = new EncaminhadoDAO();
	    	buscaEncaminhadoCod(prodsel.getCodencaminhado());
	    	encaminhadoSuggestion = new EncaminhadoBean();
	    	encaminhadoSuggestion = null;
	       // FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Item Selected", prodsel.getDescricao()));
	    }


		public EncaminhadoBean getEncaminhadogeral() {
			return encaminhadogeral;
		}


		public void setEncaminhadogeral(EncaminhadoBean encaminhadogeral) {
			this.encaminhadogeral = encaminhadogeral;
		}


		public EncaminhadoBean getEncaminhadoselecionado() {
			return encaminhadoselecionado;
		}


		public void setEncaminhadoselecionado(EncaminhadoBean encaminhadoselecionado) {
			this.encaminhadoselecionado = encaminhadoselecionado;
		}


		public EncaminhadoBean getEncaminhadoselecionadoaux() {
			return encaminhadoselecionadoaux;
		}


		public void setEncaminhadoselecionadoaux(
				EncaminhadoBean encaminhadoselecionadoaux) {
			this.encaminhadoselecionadoaux = encaminhadoselecionadoaux;
		}


		public EncaminhadoBean getEncaminhadobuscarapida() {
			return encaminhadobuscarapida;
		}


		public void setEncaminhadobuscarapida(EncaminhadoBean encaminhadobuscarapida) {
			this.encaminhadobuscarapida = encaminhadobuscarapida;
		}


		public EncaminhadoBean getEncaminhadoselecionadocomposicao() {
			return encaminhadoselecionadocomposicao;
		}


		public void setEncaminhadoselecionadocomposicao(
				EncaminhadoBean encaminhadoselecionadocomposicao) {
			this.encaminhadoselecionadocomposicao = encaminhadoselecionadocomposicao;
		}


		public EncaminhadoBean getEncaminhadoSelecionadoExclusao() {
			return encaminhadoSelecionadoExclusao;
		}


		public void setEncaminhadoSelecionadoExclusao(
				EncaminhadoBean encaminhadoSelecionadoExclusao) {
			this.encaminhadoSelecionadoExclusao = encaminhadoSelecionadoExclusao;
		}


		public EncaminhadoBean getEncaminhadoSuggestion() {
			return encaminhadoSuggestion;
		}


		public void setEncaminhadoSuggestion(EncaminhadoBean encaminhadoSuggestion) {
			this.encaminhadoSuggestion = encaminhadoSuggestion;
		}
	    
	    
}
