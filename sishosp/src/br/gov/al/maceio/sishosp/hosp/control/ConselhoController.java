package br.gov.al.maceio.sishosp.hosp.control;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.JSFUtil;
import br.gov.al.maceio.sishosp.comum.util.RedirecionarUtil;
import br.gov.al.maceio.sishosp.hosp.dao.ConselhoDAO;
import br.gov.al.maceio.sishosp.hosp.model.ConselhoBean;


@ManagedBean
@ViewScoped
public class ConselhoController {

	private static final String ENDERECO_CADASTRO = "cadastroconselho?faces-redirect=true";
	private static final String ENDERECO_TIPO = "&amp;tipo=";
	private static final String ENDERECO_ID = "&amp;id=";
	private static final String CABECALHO_INCLUSAO = "Inclusão de Conselho";
    private static final String CABECALHO_ALTERACAO = "Alteração de Conselho";
	
	private List<ConselhoBean> listaConselhos;
	private ConselhoBean conselho;
	private ConselhoDAO conselhoDAO;
	private Integer tipo;
	private String campoBusca;
	private String tipoBusca;
	private String cabecalho;
	
	public ConselhoController() {
		listaConselhos = new ArrayList<>();
		conselho = new ConselhoBean();
		conselhoDAO = new ConselhoDAO();
	}
	
	public void listarConselhos() throws ProjetoException {
		listaConselhos = conselhoDAO.listaConselho();
	}
	
    public List<ConselhoBean> listaConselhoAutoComplete(String query)
            throws ProjetoException {
        List<ConselhoBean> result = conselhoDAO.buscaConselhoFiltro(query);
        return result;
    }
	
	public void buscarConselhosFiltro() throws ProjetoException {
		listaConselhos = conselhoDAO.buscaConselhoFiltro(campoBusca);
	}
	
	public void limparCamposBusca() {
		campoBusca = "";
	}
	
	public String redirecionaEdicao() {
		return RedirecionarUtil.redirectEdit(ENDERECO_CADASTRO, ENDERECO_ID, this.conselho.getId(), ENDERECO_TIPO, tipo);
	}

	public String redirecionaInsercao() {
	    return RedirecionarUtil.redirectInsert(ENDERECO_CADASTRO, ENDERECO_TIPO, tipo);
	}

	public void getEditaConselho() throws ProjetoException {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		Map<String, String> params = facesContext.getExternalContext()
				.getRequestParameterMap();
		if (params.get("id") != null) {
			Integer id = Integer.parseInt(params.get("id"));
			tipo = Integer.parseInt(params.get("tipo"));
			this.conselho = conselhoDAO.buscaConselhoPorId(id);
		} else {
			tipo = Integer.parseInt(params.get("tipo"));
		}
	}
	
   
    public void cadastrarConselho() throws ProjetoException {
    	
		boolean cadastrado = conselhoDAO.gravarConselho(conselho);
		if (cadastrado) {
			limparDados();
			JSFUtil.adicionarMensagemSucesso("Conselho cadastrado com sucesso", "");
		} else {
			JSFUtil.adicionarMensagemErro("Erro ao cadastrar conselho", "");
		}    	
    }
    
    public void alterarConselho() throws ProjetoException {
    	
		boolean alterado = conselhoDAO.alterarConselho(conselho);
		if (alterado) {
			JSFUtil.adicionarMensagemSucesso("Conselho alterado com sucesso", "");
		} else {
			JSFUtil.adicionarMensagemErro("Erro ao alterar conselho", "");
		}    	
    }
    
    private void limparDados() {
    	conselho = new ConselhoBean();
    }
   
	
	public void excluirConselho() throws ProjetoException {
		boolean excluido = conselhoDAO.excluiConselho(conselho);
		if(excluido) {
			JSFUtil.adicionarMensagemSucesso("Conselho "+conselho.getDescricao()+" excluído com sucesso", "");
			JSFUtil.fecharDialog("dialogExclusao");
			listarConselhos();
		}
	}
	

	public List<ConselhoBean> getListaConselhos() {
		return listaConselhos;
	}

	public void setListaConselhos(List<ConselhoBean> listaConselhos) {
		this.listaConselhos = listaConselhos;
	}

	public ConselhoBean getConselho() {
		return conselho;
	}

	public void setConselho(ConselhoBean conselho) {
		this.conselho = conselho;
	}

	public Integer getTipo() {
		return tipo;
	}

	public void setTipo(Integer tipo) {
		this.tipo = tipo;
	}

	public String getCampoBusca() {
		return campoBusca;
	}

	public void setCampoBusca(String campoBusca) {
		this.campoBusca = campoBusca;
	}

	public String getTipoBusca() {
		return tipoBusca;
	}

	public void setTipoBusca(String tipoBusca) {
		this.tipoBusca = tipoBusca;
	}

	public String getCabecalho() {
		return cabecalho;
	}

	public void setCabecalho(String cabecalho) {
		this.cabecalho = cabecalho;
	}
}
