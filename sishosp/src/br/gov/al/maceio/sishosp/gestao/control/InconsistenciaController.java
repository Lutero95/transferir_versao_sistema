package br.gov.al.maceio.sishosp.gestao.control;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import br.gov.al.maceio.sishosp.acl.dao.PerfilDAO;
import br.gov.al.maceio.sishosp.acl.model.Perfil;
import br.gov.al.maceio.sishosp.comum.enums.TipoCabecalho;
import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.JSFUtil;
import br.gov.al.maceio.sishosp.comum.util.RedirecionarUtil;
import br.gov.al.maceio.sishosp.gestao.dao.InconsistenciaDAO;
import br.gov.al.maceio.sishosp.gestao.model.InconsistenciaBean;
import br.gov.al.maceio.sishosp.gestao.model.dto.InconsistenciaDTO;

@ManagedBean
@ViewScoped
public class InconsistenciaController {

	private InconsistenciaBean inconsistencia;
	private Integer tipo;
	private String cabecalho;
	private InconsistenciaDAO inconsistenciaDAO;
	private String tipoBusca;
	private String campoBusca;
	private List<InconsistenciaBean> listaInconsistencias;
	private List<Perfil> listaPerfis;
	private List<Perfil> listaPerfisFiltro;
	private List<Perfil> listaPerfisSelecionado;
	private List<Perfil> listaPerfisSelecionadoFiltro;
	private List<InconsistenciaDTO> listaInconsistenciasDTO;
	private InconsistenciaDTO inconsistenciaDTOSelecionada;
	
	private static final String ENDERECO_CADASTRO = "cadastroinconsistencia?faces-redirect=true";
	private static final String ENDERECO_TIPO = "&amp;tipo=";
	private static final String ENDERECO_ID = "&amp;id=";
	private static final String CABECALHO_INCLUSAO = "Inclusão de Inconsistência";
    private static final String CABECALHO_ALTERACAO = "Alteração de Inconsistência";
	
	
	public InconsistenciaController() {
		this.listaInconsistencias = new ArrayList<>();
		this.inconsistencia  = new InconsistenciaBean();
		this.listaPerfis = new ArrayList<>();
		this.listaPerfisFiltro = new ArrayList<>();
		this.listaPerfisSelecionado = new ArrayList<>();
		this.listaPerfisSelecionadoFiltro = new ArrayList<>();
		this.inconsistenciaDAO = new InconsistenciaDAO();
	}

	public void listarInconsistencias() throws ProjetoException {
		listaInconsistencias = inconsistenciaDAO.listarInconsistencias();
	}
	
	public void getEditaInconsistencia() throws ProjetoException, SQLException {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		Map<String, String> params = facesContext.getExternalContext()
				.getRequestParameterMap();
		if (params.get("id") != null) {
			Integer id = Integer.parseInt(params.get("id"));
			tipo = Integer.parseInt(params.get("tipo"));

			this.inconsistencia = inconsistenciaDAO.buscarInconsistenciasPorId(id);
			this.listaPerfisSelecionado.addAll(this.inconsistencia.getListaPerfis());
			this.listaPerfisSelecionadoFiltro.addAll(this.inconsistencia.getListaPerfis());
			listarPerfisNaoAssociadosAhInconsistencia(id);
		} else {
			tipo = Integer.parseInt(params.get("tipo"));
			listarPerfis();
		}
	}
	
	private void listarPerfis() throws ProjetoException {
		this.listaPerfisFiltro.clear();
		this.listaPerfis = new PerfilDAO().listarPerfil();
		this.listaPerfisFiltro.addAll(this.listaPerfis);
	}
	
	private void listarPerfisNaoAssociadosAhInconsistencia(Integer idInconsistencia) throws ProjetoException {
		this.listaPerfisFiltro.clear();
		this.listaPerfis = inconsistenciaDAO.listarPerfisNaoAssociadosAhInconsistencia(idInconsistencia);
		this.listaPerfisFiltro.addAll(this.listaPerfis);
	}
	
	private void limparPerfisSelecionados() {
		this.listaPerfisSelecionado.clear();
		this.listaPerfisSelecionadoFiltro.clear();
	}
	
	public void adicionarFuncionario(Perfil perfilSelecionado) throws ProjetoException, SQLException {
        if (!perfilExisteLista(perfilSelecionado)) {
            this.listaPerfisSelecionado.add(perfilSelecionado);
            this.listaPerfisSelecionadoFiltro.add(perfilSelecionado);
            this.listaPerfis.remove(perfilSelecionado);
            this.listaPerfisFiltro.remove(perfilSelecionado);
        }
    }
    
    private boolean perfilExisteLista(Perfil perfilSelecionado) {
        if(!listaPerfisSelecionado.isEmpty()) {
            for (int i = 0; i < listaPerfisSelecionado.size(); i++) {
                if (listaPerfisSelecionado.get(i).getId().equals(perfilSelecionado.getId())) {
                	JSFUtil.adicionarMensagemErro("Este perfil já foi adicionado", "");
                    return true;
                }
            }
        }
        return false;
    }
    
    public void removerFuncionario(Perfil perfilSelecionado) throws ProjetoException, SQLException {
        this.listaPerfisSelecionado.remove(perfilSelecionado);
        this.listaPerfisSelecionadoFiltro.remove(perfilSelecionado);
        this.listaPerfis.add(perfilSelecionado);
        this.listaPerfisFiltro.add(perfilSelecionado);
    }
    
    public void gravarInconsistencia() throws ProjetoException {
    	inconsistencia.setListaPerfis(listaPerfisSelecionado);
    	if(inconsistenciaDAO.gravarInconsistencia(inconsistencia)) {
    		JSFUtil.adicionarMensagemSucesso("Inconsistência cadastrada com sucesso", "");
    		inconsistencia = new InconsistenciaBean();
    		listarPerfis();
    		limparPerfisSelecionados();
    	}
    }
    
    public void alterarInconsistencia() throws ProjetoException {
    	inconsistencia.setListaPerfis(listaPerfisSelecionado);
    	if(inconsistenciaDAO.alterarInconsistencia(inconsistencia))
    		JSFUtil.adicionarMensagemSucesso("Inconsistência alterada com sucesso", "");
    }
    
    public void excluirInconsistencia() throws ProjetoException {
    	if(inconsistenciaDAO.excluirInconsistencia(inconsistencia.getId())) {
    		JSFUtil.adicionarMensagemSucesso("Inconsistência excluída com sucesso", "");
    		listarInconsistencias();
    		JSFUtil.fecharDialog("dialogExclusao");
    	}
    }
    
    public void listarInconsistenciasPerfil() throws ProjetoException {
		this.listaInconsistenciasDTO = inconsistenciaDAO.listarInconsistenciasPeloPerfil();
		if(!this.listaInconsistenciasDTO.isEmpty())
			JSFUtil.abrirDialog("dlgInconsistencias");
    }
	
	public void buscarInconsistencias() throws ProjetoException {
		this.listaInconsistencias = inconsistenciaDAO.buscarInconsistencias(campoBusca, tipoBusca);
	}

	public String redirecionaInsercao() {
	    return RedirecionarUtil.redirectInsert(ENDERECO_CADASTRO, ENDERECO_TIPO, tipo);
	}
	
	public String redirecionaEdicao() {
		return RedirecionarUtil.redirectEdit(ENDERECO_CADASTRO, ENDERECO_ID, this.inconsistencia.getId(), ENDERECO_TIPO, tipo);
	}
	
	public void selecionarInconsistenciaDTO(InconsistenciaDTO inconsistenciaDTOSelecionada) {
		this.inconsistenciaDTOSelecionada = inconsistenciaDTOSelecionada;
	}
	
	public List<InconsistenciaBean> getListaInconsistencias() {
		return listaInconsistencias;
	}

	public void setListaInconsistencias(List<InconsistenciaBean> listaInconsistencias) {
		this.listaInconsistencias = listaInconsistencias;
	}

	public InconsistenciaBean getInconsistencia() {
		return inconsistencia;
	}


	public void setInconsistencia(InconsistenciaBean inconsistencia) {
		this.inconsistencia = inconsistencia;
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
	
    public String getTipoBusca() {
		return tipoBusca;
	}

	public void setTipoBusca(String tipoBusca) {
		this.tipoBusca = tipoBusca;
	}

	public String getCampoBusca() {
		return campoBusca;
	}

	public void setCampoBusca(String campoBusca) {
		this.campoBusca = campoBusca;
	}

    
	public void setCabecalho(String cabecalho) {
		this.cabecalho = cabecalho;
	}

	public List<Perfil> getListaPerfis() {
		return listaPerfis;
	}

	public void setListaPerfis(List<Perfil> listaPerfis) {
		this.listaPerfis = listaPerfis;
	}

	public List<Perfil> getListaPerfisFiltro() {
		return listaPerfisFiltro;
	}

	public void setListaPerfisFiltro(List<Perfil> listaPerfisFiltro) {
		this.listaPerfisFiltro = listaPerfisFiltro;
	}

	public List<Perfil> getListaPerfisSelecionado() {
		return listaPerfisSelecionado;
	}

	public void setListaPerfisSelecionado(List<Perfil> listaPerfisSelecionado) {
		this.listaPerfisSelecionado = listaPerfisSelecionado;
	}

	public List<Perfil> getListaPerfisSelecionadoFiltro() {
		return listaPerfisSelecionadoFiltro;
	}

	public void setListaPerfisSelecionadoFiltro(List<Perfil> listaPerfisSelecionadoFiltro) {
		this.listaPerfisSelecionadoFiltro = listaPerfisSelecionadoFiltro;
	}

	public List<InconsistenciaDTO> getListaInconsistenciasDTO() {
		return listaInconsistenciasDTO;
	}

	public void setListaInconsistenciasDTO(List<InconsistenciaDTO> listaInconsistenciasDTO) {
		this.listaInconsistenciasDTO = listaInconsistenciasDTO;
	}

	public InconsistenciaDTO getInconsistenciaDTOSelecionada() {
		return inconsistenciaDTOSelecionada;
	}

	public void setInconsistenciaDTOSelecionada(InconsistenciaDTO inconsistenciaDTOSelecionada) {
		this.inconsistenciaDTOSelecionada = inconsistenciaDTOSelecionada;
	}

}
