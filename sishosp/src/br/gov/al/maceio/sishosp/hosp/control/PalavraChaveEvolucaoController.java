package br.gov.al.maceio.sishosp.hosp.control;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import br.gov.al.maceio.sishosp.comum.enums.TipoCabecalho;
import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.JSFUtil;
import br.gov.al.maceio.sishosp.comum.util.RedirecionarUtil;
import br.gov.al.maceio.sishosp.hosp.dao.PalavraChaveEvolucaoDAO;
import br.gov.al.maceio.sishosp.hosp.model.PalavraChaveEvolucaoBean;

@ManagedBean
@ViewScoped
public class PalavraChaveEvolucaoController {

	private PalavraChaveEvolucaoBean palavraChaveEvolucao;
	private List<PalavraChaveEvolucaoBean> listaPalavraChaveEvolucao;
	private PalavraChaveEvolucaoDAO palavraChaveEvolucaoDAO;
	private Integer tipo;
	private String cabecalho;
	private String campoBusca;
	
	//CONSTANTES
	private static final String ENDERECO_CADASTRO = "cadastropalavrachaveevolucao?faces-redirect=true";
	private static final String ENDERECO_TIPO = "&amp;tipo=";
	private static final String ENDERECO_ID = "&amp;id=";
	private static final String CABECALHO_INCLUSAO = "Inclusão de Palavra Chave para Evolução";
    private static final String CABECALHO_ALTERACAO = "Alteração de Palavra Chave para Evolução";
	
	public PalavraChaveEvolucaoController() {
		this.palavraChaveEvolucao = new PalavraChaveEvolucaoBean();
		this.listaPalavraChaveEvolucao = new ArrayList<>();
		this.palavraChaveEvolucaoDAO = new PalavraChaveEvolucaoDAO();
	}
	
	public String redirecionaEditar() {
		return RedirecionarUtil.redirectEdit(ENDERECO_CADASTRO, ENDERECO_ID, this.palavraChaveEvolucao.getId(), ENDERECO_TIPO, tipo);
	}

	public String redirecioInsercao() {
	    return RedirecionarUtil.redirectInsert(ENDERECO_CADASTRO, ENDERECO_TIPO, tipo);
	}

	public void getEditaPalavraChave() throws ProjetoException {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		Map<String, String> params = facesContext.getExternalContext()
				.getRequestParameterMap();
		if (params.get("id") != null) {
			Integer id = Integer.parseInt(params.get("id"));
			tipo = Integer.parseInt(params.get("tipo"));

			this.palavraChaveEvolucao = palavraChaveEvolucaoDAO.buscarPalavraChaveEvolucao(id);
		} else {
			tipo = Integer.parseInt(params.get("tipo"));
		}
	}
	
	public void listarPalavrasChave() throws ProjetoException {
		this.listaPalavraChaveEvolucao = palavraChaveEvolucaoDAO.listarPalavraChaveEvolucao();
	} 
	
	public void gravarPalavraChave() throws ProjetoException {
		boolean cadastrou = palavraChaveEvolucaoDAO.gravarPalavraChaveEvolucao(palavraChaveEvolucao);
		if(cadastrou) {
			JSFUtil.adicionarMensagemSucesso("Palavra-Chave cadastrada com sucesso!", "");
			palavraChaveEvolucao = new PalavraChaveEvolucaoBean();
		}
	}
	
	public void alterarPalavraChave() throws ProjetoException {
		boolean alterou = palavraChaveEvolucaoDAO.alterarPalavraChaveEvolucao(palavraChaveEvolucao);
		if(alterou) {
			JSFUtil.adicionarMensagemSucesso("Palavra-Chave alterada com sucesso!", "");
		}
	}
	
	public void excluirPalavraChave(Integer id) throws ProjetoException {
		boolean excluiu = palavraChaveEvolucaoDAO.excluirPalavraChaveEvolucao(id);
		if(excluiu) {
			JSFUtil.adicionarMensagemSucesso("Palavra-Chave excluída", "");
			JSFUtil.fecharDialog("dialogExclusao");
			listarPalavrasChave();
		}
	}
	
	public void buscarPalavrasChave() throws ProjetoException {
		listaPalavraChaveEvolucao = palavraChaveEvolucaoDAO.listarPalavraChaveEvolucaoFiltro(campoBusca);
	}
	
	public void limparCampoBusca() {
		campoBusca = new String();
	}
	
	public PalavraChaveEvolucaoBean getPalavraChaveEvolucao() {
		return palavraChaveEvolucao;
	}

	public void setPalavraChaveEvolucao(PalavraChaveEvolucaoBean palavraChaveEvolucao) {
		this.palavraChaveEvolucao = palavraChaveEvolucao;
	}

	public List<PalavraChaveEvolucaoBean> getListaPalavraChaveEvolucao() {
		return listaPalavraChaveEvolucao;
	}

	public void setListaPalavraChaveEvolucao(List<PalavraChaveEvolucaoBean> listaPalavraChaveEvolucao) {
		this.listaPalavraChaveEvolucao = listaPalavraChaveEvolucao;
	}

	public Integer getTipo() {
		return tipo;
	}

	public void setTipo(Integer tipo) {
		this.tipo = tipo;
	}
	
	public String getCabecalho() {
		if (this.tipo.equals(TipoCabecalho.INCLUSAO.getSigla())) {
			cabecalho = CABECALHO_INCLUSAO;
		} else if (this.tipo.equals(TipoCabecalho.ALTERACAO.getSigla())) {
			cabecalho = CABECALHO_ALTERACAO;
		}
		return cabecalho;
	}

	public void setCabecalho(String cabecalho) {
		this.cabecalho = cabecalho;
	}

	public String getCampoBusca() {
		return campoBusca;
	}

	public void setCampoBusca(String campoBusca) {
		this.campoBusca = campoBusca;
	}
}
