package br.gov.al.maceio.sishosp.hosp.control;

import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.primefaces.event.SelectEvent;

import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.hosp.dao.EscolaDAO;
import br.gov.al.maceio.sishosp.hosp.model.EscolaBean;

public class EscolaController {
	
	private EscolaBean escolageral;
	private EscolaBean escolaselecionado,escolaselecionadoaux, escolabuscarapida, escolaselecionadocomposicao, produtoSelecionadoExclusao;
	private EscolaBean escolaSuggestion;
	
	public EscolaController(){
		
		
	}
	
	
	
	public void buscaEscolaCod(Integer codescola) throws Exception {
		
		EscolaBean escola = new EscolaBean();
		
		
		Integer in = (Integer)Integer.valueOf(codescola);	
		
		EscolaDAO icdao = new EscolaDAO();
		
		escola=icdao.buscaescolacodigo(Integer.valueOf(codescola)); 
		if (escola.getCodEscola()!=null){
		escolaselecionado = escola;
		escolabuscarapida = new EscolaBean();
		
		escolageral  = escolaselecionado;
		 icdao = new EscolaDAO();
	
		}
		else
		{
			escolabuscarapida = new EscolaBean();
			FacesMessage message = new FacesMessage(
					" Código da Escola incorreto!");
			 FacesContext.getCurrentInstance().addMessage(null, message);
		}
		
		
	}   
	
	public List<EscolaBean> completeText(String query) throws ProjetoException {
		 List<EscolaBean> result = new ArrayList<EscolaBean>();
	        EscolaDAO icdao = new EscolaDAO();
          result = icdao.buscaescola(query);    
	        return result;
	    }
	    public void onItemSelect(SelectEvent event) throws Exception {
	    	
	    	EscolaBean prodsel = new EscolaBean();
	    	prodsel =(EscolaBean) event.getObject();
	    	
	    	EscolaDAO dao = new EscolaDAO();
	    	buscaEscolaCod(prodsel.getCodEscola());
	    	escolaSuggestion = new EscolaBean();
	    	escolaSuggestion = null;
	       // FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Item Selected", prodsel.getDescricao()));
	    }



		public EscolaBean getEscolateste() {
			return escolageral;
		}



		public void setEscolateste(EscolaBean escolageral) {
			this.escolageral = escolageral;
		}



		public EscolaBean getEscolaselecionado() {
			return escolaselecionado;
		}



		public void setEscolaselecionado(EscolaBean escolaselecionado) {
			this.escolaselecionado = escolaselecionado;
		}



		public EscolaBean getEscolaselecionadoaux() {
			return escolaselecionadoaux;
		}



		public void setEscolaselecionadoaux(EscolaBean escolaselecionadoaux) {
			this.escolaselecionadoaux = escolaselecionadoaux;
		}



		public EscolaBean getEscolabuscarapida() {
			return escolabuscarapida;
		}



		public void setEscolabuscarapida(EscolaBean escolabuscarapida) {
			this.escolabuscarapida = escolabuscarapida;
		}



		public EscolaBean getEscolaselecionadocomposicao() {
			return escolaselecionadocomposicao;
		}



		public void setEscolaselecionadocomposicao(
				EscolaBean escolaselecionadocomposicao) {
			this.escolaselecionadocomposicao = escolaselecionadocomposicao;
		}



		public EscolaBean getProdutoSelecionadoExclusao() {
			return produtoSelecionadoExclusao;
		}



		public void setProdutoSelecionadoExclusao(EscolaBean produtoSelecionadoExclusao) {
			this.produtoSelecionadoExclusao = produtoSelecionadoExclusao;
		}



		public EscolaBean getEscolaSuggestion() {
			return escolaSuggestion;
		}



		public void setEscolaSuggestion(EscolaBean escolaSuggestion) {
			this.escolaSuggestion = escolaSuggestion;
		}
	     
	    
	    

}
