package br.gov.al.maceio.sishosp.hosp.control;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;

import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.hosp.dao.MotivoDesligamentoDAO;
import br.gov.al.maceio.sishosp.hosp.model.MotivoDesligamentoBean;

@ManagedBean(name = "MotivoController")
@ViewScoped
public class MotivoDesligamentoController implements Serializable {

	private static final long serialVersionUID = 1L;
	private MotivoDesligamentoBean motivo;
	private List<MotivoDesligamentoBean> listaMotivos;
	private Integer tipoBuscar;
	private String descricaoBusca;
	private int tipo;
	private String cabecalho;

	private Integer abaAtiva = 0;

	public MotivoDesligamentoController() {
		motivo = new MotivoDesligamentoBean();
		listaMotivos = new ArrayList<>();
		listaMotivos = null;
		this.descricaoBusca = new String();
		this.cabecalho = "";

	}

	public String redirectEdit() {
		return "cadastroMotivoDesligamento?faces-redirect=true&amp;id="
				+ this.motivo.getId_motivo() + "&amp;tipo=" + tipo;
	}

	public String redirectInsert() {
		return "cadastroMotivoDesligamento?faces-redirect=true&amp;tipo=" + tipo;
	}

	public void limparDados() {
		motivo = new MotivoDesligamentoBean();
		listaMotivos = new ArrayList<>();
		this.descricaoBusca = new String();

	}

	public void gravarMotivo() throws ProjetoException, SQLException {
		MotivoDesligamentoDAO gDao = new MotivoDesligamentoDAO();
		boolean cadastrou = gDao.gravarMotivo(motivo);

		if (cadastrou == true) {
			limparDados();
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Motivo de Desligamento cadastrado com sucesso!", "Sucesso");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		} else {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Ocorreu um erro durante o cadastro!", "Erro");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
	}

	public void alterarMotivo() throws ProjetoException, SQLException {
		MotivoDesligamentoDAO gDao = new MotivoDesligamentoDAO();
		boolean alterou = gDao.alterarMotivo(motivo);
		if (alterou == true) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Motivo de Desligamento alterado com sucesso!", "Sucesso");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		} else {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Ocorreu um erro durante o cadastro!", "Erro");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
		listaMotivos = gDao.listarMotivos();

	}

	public void excluirMotivo() throws ProjetoException, SQLException {
		MotivoDesligamentoDAO gDao = new MotivoDesligamentoDAO();
		boolean ok = gDao.excluirMotivo(motivo);
		if (ok == true) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Motivo de Desligamento excluído com sucesso!", "Sucesso");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			RequestContext.getCurrentInstance().execute(
					"PF('dialogAtencao').hide();");
		} else {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Ocorreu um erro durante a exclusao!", "Erro");
			FacesContext.getCurrentInstance().addMessage(null, msg);

			RequestContext.getCurrentInstance().execute(
					"PF('dialogAtencao').hide();");
		}
		listaMotivos = gDao.listarMotivos();
	}

	public void getEditMotivo() throws ProjetoException {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		Map<String, String> params = facesContext.getExternalContext()
				.getRequestParameterMap();
		if (params.get("id") != null) {
			Integer id = Integer.parseInt(params.get("id"));
			tipo = Integer.parseInt(params.get("tipo"));
			MotivoDesligamentoDAO cDao = new MotivoDesligamentoDAO();
			this.motivo = cDao.buscaMotivoPorId(id);
		} else {
			tipo = Integer.parseInt(params.get("tipo"));

		}

	}

	public MotivoDesligamentoBean getMotivo() {
		return motivo;
	}

	public void setMotivo(MotivoDesligamentoBean motivo) {
		this.motivo = motivo;
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

	public Integer getAbaAtiva() {
		return abaAtiva;
	}

	public void setAbaAtiva(Integer abaAtiva) {
		this.abaAtiva = abaAtiva;
	}

	public int getTipo() {
		return tipo;
	}

	public void setTipo(int tipo) {
		this.tipo = tipo;
	}

	public void setCabecalho(String cabecalho) {
		this.cabecalho = cabecalho;
	}

	public String getCabecalho() {
		if (this.tipo == 1) {
			cabecalho = "Inclusão de Motivo de Desligamento";
		} else if (this.tipo == 2) {
			cabecalho = "Alteração de Motivo de Desligamento";
		}
		return cabecalho;
	}

	public List<MotivoDesligamentoBean> getListaMotivos() throws SQLException,
			ProjetoException {
		if (listaMotivos == null) {

			MotivoDesligamentoDAO fdao = new MotivoDesligamentoDAO();
			listaMotivos = fdao.listarMotivos();
		}
		return listaMotivos;
	}

	public void setListaMotivos(List<MotivoDesligamentoBean> listaMotivos) {
		this.listaMotivos = listaMotivos;
	}

}
