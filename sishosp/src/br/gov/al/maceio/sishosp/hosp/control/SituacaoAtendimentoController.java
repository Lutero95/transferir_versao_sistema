package br.gov.al.maceio.sishosp.hosp.control;

import java.io.Serializable;
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
import br.gov.al.maceio.sishosp.hosp.dao.SituacaoAtendimentoDAO;
import br.gov.al.maceio.sishosp.hosp.model.SituacaoAtendimentoBean;

@ManagedBean
@ViewScoped
public class SituacaoAtendimentoController  implements Serializable {
	

	private static final long serialVersionUID = 1L;
	private static final String ENDERECO_CADASTRO = "cadastroSituacaoAtendimento?faces-redirect=true";
	private static final String ENDERECO_TIPO = "&amp;tipo=";
	private static final String ENDERECO_ID = "&amp;id=";
	private static final String CABECALHO_INCLUSAO = "Inclusão de Situação do Atendimento";
    private static final String CABECALHO_ALTERACAO = "Alteração de Situação do Atendimento";
    private Integer tipo;
	private List<SituacaoAtendimentoBean> listaSituacaoAtendimento;
	private SituacaoAtendimentoBean situacaoAtendimento;
	private SituacaoAtendimentoDAO situacaoAtendimentoDAO;
	private String campoBusca;
	private String cabecalho;
	
	public SituacaoAtendimentoController() {
		this.listaSituacaoAtendimento = new ArrayList<SituacaoAtendimentoBean>();
		this.situacaoAtendimento = new SituacaoAtendimentoBean();
		this.situacaoAtendimentoDAO = new SituacaoAtendimentoDAO();
	}
	
	public String redirectEdit() {
		return RedirecionarUtil.redirectEdit(ENDERECO_CADASTRO, ENDERECO_ID, this.situacaoAtendimento.getId(), ENDERECO_TIPO, tipo);
	}

	public String redirectInsert() {
		this.situacaoAtendimento = new SituacaoAtendimentoBean();
	    return RedirecionarUtil.redirectInsert(ENDERECO_CADASTRO, ENDERECO_TIPO, tipo);
	}
	
	public void listarSituacoes() throws Exception {
		this.listaSituacaoAtendimento = situacaoAtendimentoDAO.listarSituacaoAtendimento();
	}
	
	public void buscarSituacaoAtendimento() throws Exception {
		this.listaSituacaoAtendimento = situacaoAtendimentoDAO.buscarSituacaoAtendimento(this.campoBusca);
	}
	
	public void listarSituacaoAtendimentoFiltroRelatorioAtendimentoProgramaGrupo() throws ProjetoException {
		this.listaSituacaoAtendimento = situacaoAtendimentoDAO.listarSituacaoAtendimentoFiltroRelatorioAtendimentoProgramaGrupo();
	}
	
	public void limparCampoBusca() {
		this.campoBusca = new String();
	}
	
	public void getEditSituacaoAtendimento() throws ProjetoException {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		Map<String, String> params = facesContext.getExternalContext()
				.getRequestParameterMap();
		if (params.get("id") != null) {
			Integer id = Integer.parseInt(params.get("id"));
			tipo = Integer.parseInt(params.get("tipo"));
			this.situacaoAtendimento = situacaoAtendimentoDAO.buscaSituacaoAtendimentoPorId(id);
		} else {
			tipo = Integer.parseInt(params.get("tipo"));
		}
	}
	
	public void gravarSituacaoAtendimento() throws ProjetoException {
		if (!maisDeUmTipoSituacaoFoiMarcado()) {
			if ((this.situacaoAtendimento.getAtendimentoRealizado()) || (this.situacaoAtendimento.isAbonoFalta() && !existeOutraSituacaoComAbonoFalta())
					|| (!this.situacaoAtendimento.getAtendimentoRealizado() && !this.situacaoAtendimento.isAbonoFalta())) {

				if (situacaoAtendimentoDAO.gravarSituacaoAtendimento(this.situacaoAtendimento)) {
					this.situacaoAtendimento = new SituacaoAtendimentoBean();
					JSFUtil.adicionarMensagemSucesso("Situação do Atendimento gravada com sucesso", "");
				}
			}
		}
	}
	
	public void alterarSituacaoAtendimento() throws ProjetoException {
		if (!maisDeUmTipoSituacaoFoiMarcado()) {
			if ((this.situacaoAtendimento.getAtendimentoRealizado()) || (this.situacaoAtendimento.isAbonoFalta() && !existeOutraSituacaoComAbonoFalta())
					|| (!this.situacaoAtendimento.getAtendimentoRealizado() && !this.situacaoAtendimento.isAbonoFalta())) {

				if (situacaoAtendimentoDAO.alterarSituacaoAtendimento(this.situacaoAtendimento))
					JSFUtil.adicionarMensagemSucesso("Situação do Atendimento alterada com sucesso", "");
			}
		}
	}
	
	private Boolean existeOutraSituacaoComAbonoFalta() throws ProjetoException {
		if(situacaoAtendimentoDAO.existeOutraSituacaoComAbonoFalta(this.situacaoAtendimento.getId())) {
			JSFUtil.adicionarMensagemErro("Já existe uma situação para Abono de Falta", "Erro");
			return true;
		}
		return false;
	}

	private Boolean maisDeUmTipoSituacaoFoiMarcado() throws ProjetoException {
		int contador1 = this.situacaoAtendimento.getAtendimentoRealizado()==true ? 1 : 0;
		int contador2 = this.situacaoAtendimento.isAbonoFalta()==true ? 1 : 0;
		int contador3 = this.situacaoAtendimento.isFaltaProfissional()==true ? 1 : 0;
		int contador4 = this.situacaoAtendimento.isPacienteFaltou()==true ? 1 : 0;

		if ((contador1+contador2+contador3+contador4)>1 ) {
			JSFUtil.adicionarMensagemErro("Selecione apenas uma das opções: Atendimento Realizado, Abono de Falta, Falta de Profissional ou Paciente Faltou", "Erro");
			return true;
		}
		return false;
	}
	
	public void excluirSituacaoAtendimento() {
		try {
			if(this.situacaoAtendimentoDAO.excluirSituacaoAtendimento(this.situacaoAtendimento.getId()))
				JSFUtil.adicionarMensagemSucesso("Situação do Atendimento "+situacaoAtendimento.getDescricao()+" excluída com sucesso", "");
			listarSituacoes();
		} catch (Exception e) {
			JSFUtil.adicionarMensagemErro("Erro ao tentar excluir Situação do Atendimento "+e.getMessage(), "Erro");
			e.printStackTrace();
		}			
	}
	
	public List<SituacaoAtendimentoBean> getListaSituacaoAtendimento() {
		return listaSituacaoAtendimento;
	}
	public void setListaSituacaoAtendimento(List<SituacaoAtendimentoBean> listaSituacaoAtendimento) {
		this.listaSituacaoAtendimento = listaSituacaoAtendimento;
	}
	public SituacaoAtendimentoBean getSituacaoAtendimento() {
		return situacaoAtendimento;
	}
	public void setSituacaoAtendimento(SituacaoAtendimentoBean situacaoAtendimento) {
		this.situacaoAtendimento = situacaoAtendimento;
	}

	public String getCampoBusca() {
		return campoBusca;
	}

	public void setCampoBusca(String campoBusca) {
		this.campoBusca = campoBusca;
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
	
}
