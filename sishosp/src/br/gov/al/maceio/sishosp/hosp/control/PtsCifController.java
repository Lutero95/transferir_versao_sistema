package br.gov.al.maceio.sishosp.hosp.control;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import br.gov.al.maceio.sishosp.acl.model.FuncionarioBean;
import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.JSFUtil;
import br.gov.al.maceio.sishosp.comum.util.RedirecionarUtil;
import br.gov.al.maceio.sishosp.comum.util.SessionUtil;
import br.gov.al.maceio.sishosp.comum.util.VerificadorUtil;
import br.gov.al.maceio.sishosp.hosp.dao.PtsCifDAO;
import br.gov.al.maceio.sishosp.hosp.enums.Turno;
import br.gov.al.maceio.sishosp.hosp.model.ObjetivoPtsCifBean;
import br.gov.al.maceio.sishosp.hosp.model.PtsCifBean;

@ManagedBean
@ViewScoped
public class PtsCifController {
		
	private List<PtsCifBean> listaPtsCif;
	private PtsCifBean ptsCif;
	private PtsCifDAO ptsCifDAO;
	private Boolean renderizarBotaoNovo;
	private PtsCifBean ptsCifSelecionado;
    private String filtroTurno;
    private String campoBusca;
    private String tipoBusca;
    private ObjetivoPtsCifBean objetivoPtsCif;
    private boolean existePts;
    private boolean filtrarSemPts;
    private Integer filtroMesVencimento;
    private Integer filtroAnoVencimento;
    private Integer tipo;
    private String cabecalho;
	
	private static final String PTSCIF_ = "ptscif";
	private static final String ENDERECO_PTS = "cadastroptscif?faces-redirect=true";
	private static final String ENDERECO_ID = "&amp;id=";
	private static final String ENDERECO_TIPO = "&amp;tipo=";
	private static final String CABECALHO_INCLUSAO = "Inclusão de PTS CIF";
    private static final String CABECALHO_ALTERACAO = "Alteração de PTS CIF";
	
	public PtsCifController() {
		this.listaPtsCif = new ArrayList<>();
		this.ptsCif = new PtsCifBean();
		this.ptsCifDAO = new PtsCifDAO();
		this.renderizarBotaoNovo = false;
		this.filtroTurno = Turno.AMBOS.getSigla();
		this.objetivoPtsCif = new ObjetivoPtsCifBean();
	}

	public void buscarPtsCif() throws ProjetoException {
		listaPtsCif = ptsCifDAO.buscarPtsPacientesAtivos("", filtroMesVencimento, filtroAnoVencimento, campoBusca, tipoBusca, filtroTurno, filtrarSemPts);
	}
	
    public void verificarSeExistePtsParaPaciente() throws ProjetoException {

        if (!VerificadorUtil.verificarSeObjetoNuloOuZero(ptsCifSelecionado.getGerenciarPaciente().getId())) {
            if (!ptsCifDAO.verificarSeExistePtsPaciente(ptsCifSelecionado))
                renderizarBotaoNovo = true;
            
            else
            	renderizarBotaoNovo = false;
        }
    }
    
    public String redirecionaInsercao() {
        SessionUtil.adicionarNaSessao(ptsCifSelecionado, PTSCIF_);
        return RedirecionarUtil.redirectInsert(ENDERECO_PTS, ENDERECO_TIPO, this.tipo);
    }
    
	public String redirecionaEdicao() {
		SessionUtil.adicionarNaSessao(ptsCifSelecionado, PTSCIF_);
		return RedirecionarUtil.redirectEdit(ENDERECO_PTS, ENDERECO_ID, this.ptsCifSelecionado.getId(), ENDERECO_TIPO, tipo);
	}
    
    public void carregarPts() throws ProjetoException {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Map<String, String> params = facesContext.getExternalContext()
                .getRequestParameterMap();
        if (params.get("id") != null) {
        	this.ptsCif.setId(Integer.valueOf(params.get("id")));
        	this.ptsCif = ptsCifDAO.buscarPtsCifPorId(this.ptsCif.getId());
        	this.existePts = true;
        	cabecalho = CABECALHO_ALTERACAO;
        } else {
        	cabecalho = CABECALHO_INCLUSAO;
        	this.existePts = false;
        	ptsCif = (PtsCifBean) SessionUtil.resgatarDaSessao(PTSCIF_);
        }
    }
    
    public void adicionarAvaliador(FuncionarioBean avaliadorSelecionado) {
    	if(!avaliadorJaFoiAdicionado(avaliadorSelecionado)) {
    		ptsCif.getListaAvaliadores().add(avaliadorSelecionado);
    		JSFUtil.fecharDialog("dlgConsuProf");
    	}
    } 
    
    private boolean avaliadorJaFoiAdicionado(FuncionarioBean avaliadorSelecionado) {
    	for (FuncionarioBean avaliador : ptsCif.getListaAvaliadores()) {
			if(avaliador.getId().equals(avaliadorSelecionado.getId())) {
				JSFUtil.adicionarMensagemAdvertencia("Este avaliador já foi adicionado", "");
				return true;
			}
		}
    	return false;
    }
    
    public void removerAvaliador(FuncionarioBean avaliadorSelecionado) {
    	ptsCif.getListaAvaliadores().remove(avaliadorSelecionado);
    }
    
    public void limparObjetivoSelecionado() {
    	this.objetivoPtsCif = new ObjetivoPtsCifBean();
    	JSFUtil.abrirDialog("dlgObjetivo");
    }
    
    public void adicionarObjetivo(ObjetivoPtsCifBean objetivoSelecionado) {
    	ptsCif.getListaObjetivos().add(objetivoSelecionado);
    	JSFUtil.fecharDialog("dlgObjetivo");
    }
    
    public void removerObjetivo(ObjetivoPtsCifBean objetivoSelecionado) {
    	ptsCif.getListaObjetivos().remove(objetivoSelecionado);
    }
    
    public void cadastrarPtsCif() throws ProjetoException {
    	if(existeAvaliadores() && existeObjetivos()) {
			boolean cadastrou = ptsCifDAO.cadastrarPtsCif(ptsCif);
			if (cadastrou) {
				ptsCif = new PtsCifBean();
				JSFUtil.adicionarMensagemSucesso("PTS cadastrado com sucesso", "");
			} else {
				JSFUtil.adicionarMensagemErro("Erro ao cadastrar PTS", "");
			}
    	}
    }
    
    public void alterarPtsCif() throws ProjetoException {
    	if(existeAvaliadores() && existeObjetivos()) {
			boolean alterou = ptsCifDAO.alterarPtsCif(ptsCif);
			if (alterou) {
				JSFUtil.adicionarMensagemSucesso("PTS alterado com sucesso", "");
			} else {
				JSFUtil.adicionarMensagemErro("Erro ao alterar PTS", "");
			}
    	}
    }
    
    private boolean existeAvaliadores() {
    	if(ptsCif.getListaAvaliadores().isEmpty()) {
    		JSFUtil.adicionarMensagemAdvertencia("Adicione pelo menos 1 Avaliador", "");
    		return false;
    	}
    	return true;
    }
    
    private boolean existeObjetivos() {
    	if(ptsCif.getListaObjetivos().isEmpty()) {
    		JSFUtil.adicionarMensagemAdvertencia("Adicione pelo menos 1 Objetivo", "");
    		return false;
    	}
    	return true;
    }
    
    public void limparBusca() throws ProjetoException {
    	filtroMesVencimento = null;
    	filtroAnoVencimento = null;
    	campoBusca = "";
    	tipoBusca = "";
    	filtroTurno = Turno.AMBOS.getSigla();
    	filtrarSemPts = false;
    	buscarPtsCif();
    }
	
	public List<PtsCifBean> getListaPtsCif() {
		return listaPtsCif;
	}

	public void setListaPtsCif(List<PtsCifBean> listaPtsCif) {
		this.listaPtsCif = listaPtsCif;
	}

	public PtsCifBean getPtsCif() {
		return ptsCif;
	}

	public void setPtsCif(PtsCifBean ptsCif) {
		this.ptsCif = ptsCif;
	}

	public Boolean getRenderizarBotaoNovo() {
		return renderizarBotaoNovo;
	}

	public void setRenderizarBotaoNovo(Boolean renderizarBotaoNovo) {
		this.renderizarBotaoNovo = renderizarBotaoNovo;
	}

	public PtsCifBean getPtsCifSelecionado() {
		return ptsCifSelecionado;
	}

	public void setPtsCifSelecionado(PtsCifBean ptsCifSelecionado) {
		this.ptsCifSelecionado = ptsCifSelecionado;
	}

	public String getFiltroTurno() {
		return filtroTurno;
	}

	public void setFiltroTurno(String filtroTurno) {
		this.filtroTurno = filtroTurno;
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

	public ObjetivoPtsCifBean getObjetivoPtsCif() {
		return objetivoPtsCif;
	}

	public void setObjetivoPtsCif(ObjetivoPtsCifBean objetivoPtsCif) {
		this.objetivoPtsCif = objetivoPtsCif;
	}

	public boolean isExistePts() {
		return existePts;
	}

	public void setExistePts(boolean existePts) {
		this.existePts = existePts;
	}

	public boolean isFiltrarSemPts() {
		return filtrarSemPts;
	}

	public void setFiltrarSemPts(boolean filtrarSemPts) {
		this.filtrarSemPts = filtrarSemPts;
	}

	public Integer getFiltroMesVencimento() {
		return filtroMesVencimento;
	}

	public void setFiltroMesVencimento(Integer filtroMesVencimento) {
		this.filtroMesVencimento = filtroMesVencimento;
	}

	public Integer getFiltroAnoVencimento() {
		return filtroAnoVencimento;
	}

	public void setFiltroAnoVencimento(Integer filtroAnoVencimento) {
		this.filtroAnoVencimento = filtroAnoVencimento;
	}

	public Integer getTipo() {
		return tipo;
	}

	public void setTipo(Integer tipo) {
		this.tipo = tipo;
	}

	public String getCabecalho() {
		return cabecalho;
	}

	public void setCabecalho(String cabecalho) {
		this.cabecalho = cabecalho;
	}
	
}
