package br.gov.al.maceio.sishosp.hosp.control;

import java.sql.SQLException;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;

import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.hosp.dao.TipoAtendimentoDAO;
import br.gov.al.maceio.sishosp.hosp.model.GrupoBean;
import br.gov.al.maceio.sishosp.hosp.model.TipoAtendimentoBean;

public class TipoAtendimentoController {
	
	private TipoAtendimentoBean tipo;
	private List<TipoAtendimentoBean> listaTipos;
	private Integer tipoBuscar;
	private String descricaoBusca;
	private String tipoS;
	private GrupoBean grupoSelecionado;
	private String cabecalho;
	
	private Integer abaAtiva = 0;
	
	TipoAtendimentoDAO tDao = new TipoAtendimentoDAO();

	public TipoAtendimentoController() {
		this.tipo = new TipoAtendimentoBean();
		this.listaTipos = null;
		this.descricaoBusca = new String();
		this.tipoS = new String();
		this.grupoSelecionado = new GrupoBean();
	}

	public void limparDados() {
		this.tipo = new TipoAtendimentoBean();
		this.listaTipos = null;
		this.descricaoBusca = new String();
		this.listaTipos = tDao.listarTipoAt();
		this.grupoSelecionado = new GrupoBean();
	}

	public TipoAtendimentoBean getTipo() {
		return tipo;
	}

	public void setTipo(TipoAtendimentoBean tipo) {
		this.tipo = tipo;
	}

	public List<TipoAtendimentoBean> getListaTipos() {
		if (listaTipos == null) {
			listaTipos = tDao.listarTipoAt();
		}
		return listaTipos;
	}
	
	public void atualizaListaTipos(GrupoBean g){
		this.grupoSelecionado = g;
		this.listaTipos = tDao.listarTipoAtPorGrupo(g.getIdGrupo());
	}
	
	public void buscarTipoAt() {
		this.listaTipos = tDao.listarTipoAtBusca(descricaoBusca, tipoBuscar);
	}
	
	public void listarTodosTipos() {
		this.listaTipos = tDao.listarTipoAt();
	}

	public void setListaTipos(List<TipoAtendimentoBean> listaTipos) {
		this.listaTipos = listaTipos;
	}
	
	public void gravarTipo() throws ProjetoException, SQLException {
		boolean cadastrou = tDao.gravarTipoAt(tipo);

		if (cadastrou == true) {
			limparDados();
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Tipo de Atendimento cadastrado com sucesso!", "Sucesso");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		} else {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Ocorreu um erro durante o cadastro!", "Erro");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
		listaTipos = tDao.listarTipoAt();
	}
	
	public String alterarTipo() throws ProjetoException {
        boolean alterou = tDao.alterarTipo(this.tipo);
        if(alterou == true) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
                "Tipo de atendimento alterado com sucesso!", "Sucesso");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            listaTipos = tDao.listarTipoAt();
            return "/pages/sishosp/gerenciarTipoAtendimento.faces?faces-redirect=true";
        } else {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                "Ocorreu um erro durante o cadastro!", "Erro");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return "";
        }
		
	}
	
	public void excluirTipo() throws ProjetoException {
        boolean ok = tDao.excluirTipo(this.tipo);
        if(ok == true) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
                "Tipo de atendimento excluido com sucesso!", "Sucesso");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().execute("PF('dialogAtencao').hide();");
        } else {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                "Ocorreu um erro durante a exclusao!", "Erro");
            FacesContext.getCurrentInstance().addMessage(null, msg);

            RequestContext.getCurrentInstance().execute("PF('dialogAtencao').hide();");
        }
		listaTipos = tDao.listarTipoAt();
	}

	public Integer getTipoBuscar() {
		return tipoBuscar;
	}

	public void setTipoBuscar(Integer tipoBuscar) {
		this.tipoBuscar = tipoBuscar;
	}

	public String getDescricaoBusca() {
		return descricaoBusca;
	}

	public void setDescricaoBusca(String descricaoBusca) {
		this.descricaoBusca = descricaoBusca;
	}

	public String getTipoS() {
		return tipoS;
	}

	public void setTipoS(String tipoS) {
		this.tipoS = tipoS;
	}

	public Integer getAbaAtiva() {
		return abaAtiva;
	}

	public void setAbaAtiva(Integer abaAtiva) {
		this.abaAtiva = abaAtiva;
	}
	
	public String getCabecalho() {
		if(this.tipoS.equals("I")){
			cabecalho = "CADASTRO DE TIPO ATENDIMENTO";
		}else if(this.tipoS.equals("A")){
			cabecalho = "ALTERAR TIPO ATENDIMENTO";
		}
		return cabecalho;
	}

	public void setCabecalho(String cabecalho) {
		this.cabecalho = cabecalho;
	}
	
	public List<TipoAtendimentoBean> listaTipoAtAutoComplete(String query)
			throws ProjetoException {
		return tDao.listarTipoAtAutoComplete(query,this.grupoSelecionado);
	}
	
	public void selectGrupo(SelectEvent event) {
		this.grupoSelecionado = (GrupoBean) event.getObject();
		atualizaListaTipos(grupoSelecionado);
	}
	
	public List<TipoAtendimentoBean> listaTipoAtAutoCompleteTodos(String query)
			throws ProjetoException {
		return tDao.listarTipoAtBusca(query, 1);
	}
}
;