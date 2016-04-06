package br.gov.al.maceio.sishosp.hosp.control;

import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.primefaces.event.SelectEvent;

import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.hosp.dao.ProfissaoDAO;
import br.gov.al.maceio.sishosp.hosp.model.ProfissaoBean;



public class ProfissaoController {
	
	private ProfissaoBean profissaogeral;
	private ProfissaoBean profissaoselecionado,profissaoselecionadoaux, profissaobuscarapida, profissaoselecionadocomposicao, profissaoSelecionadoExclusao;
	private ProfissaoBean profissaoSuggestion;

	
	public ProfissaoController(){
		
		
	}
	
	
public void buscaProfissaoCod(Integer codprofissao) throws Exception {
		
		ProfissaoBean profissao = new ProfissaoBean();
		
		
		Integer in = (Integer)Integer.valueOf(codprofissao);	
		
		ProfissaoDAO icdao = new ProfissaoDAO();
		
		profissao=icdao.buscaprofissaocodigo(Integer.valueOf(codprofissao)); 
		if (profissao.getCodprofissao()!=null){
		profissaoselecionado = profissao;
		profissaobuscarapida = new ProfissaoBean();
		
		profissaogeral  = profissaoselecionado;
		 icdao = new ProfissaoDAO();
	
		}
		else
		{
			profissaobuscarapida = new ProfissaoBean();
			FacesMessage message = new FacesMessage(
					" Código da Profissao incorreto!");
			 FacesContext.getCurrentInstance().addMessage(null, message);
		}
		
		
	}   
	
	public List<ProfissaoBean> completeText(String query) throws ProjetoException {
		 List<ProfissaoBean> result = new ArrayList<ProfissaoBean>();
	        ProfissaoDAO icdao = new ProfissaoDAO();
          result = icdao.buscaprofissao(query);    
	        return result;
	    }
	    public void onItemSelect(SelectEvent event) throws Exception {
	    	
	    	ProfissaoBean prodsel = new ProfissaoBean();
	    	prodsel =(ProfissaoBean) event.getObject();
	    	
	    	ProfissaoDAO dao = new ProfissaoDAO();
	    	buscaProfissaoCod(prodsel.getCodprofissao());
	    	profissaoSuggestion = new ProfissaoBean();
	    	profissaoSuggestion = null;
	       // FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Item Selected", prodsel.getDescricao()));
	    }


		public ProfissaoBean getProfissaogeral() {
			return profissaogeral;
		}


		public void setProfissaogeral(ProfissaoBean profissaogeral) {
			this.profissaogeral = profissaogeral;
		}


		public ProfissaoBean getProfissaoselecionado() {
			return profissaoselecionado;
		}


		public void setProfissaoselecionado(ProfissaoBean profissaoselecionado) {
			this.profissaoselecionado = profissaoselecionado;
		}


		public ProfissaoBean getProfissaoselecionadoaux() {
			return profissaoselecionadoaux;
		}


		public void setProfissaoselecionadoaux(ProfissaoBean profissaoselecionadoaux) {
			this.profissaoselecionadoaux = profissaoselecionadoaux;
		}


		public ProfissaoBean getProfissaobuscarapida() {
			return profissaobuscarapida;
		}


		public void setProfissaobuscarapida(ProfissaoBean profissaobuscarapida) {
			this.profissaobuscarapida = profissaobuscarapida;
		}


		public ProfissaoBean getProfissaoselecionadocomposicao() {
			return profissaoselecionadocomposicao;
		}


		public void setProfissaoselecionadocomposicao(
				ProfissaoBean profissaoselecionadocomposicao) {
			this.profissaoselecionadocomposicao = profissaoselecionadocomposicao;
		}


		public ProfissaoBean getProfissaoSelecionadoExclusao() {
			return profissaoSelecionadoExclusao;
		}


		public void setProfissaoSelecionadoExclusao(
				ProfissaoBean profissaoSelecionadoExclusao) {
			this.profissaoSelecionadoExclusao = profissaoSelecionadoExclusao;
		}


		public ProfissaoBean getProfissaoSuggestion() {
			return profissaoSuggestion;
		}


		public void setProfissaoSuggestion(ProfissaoBean profissaoSuggestion) {
			this.profissaoSuggestion = profissaoSuggestion;
		}
	    
	    
}
