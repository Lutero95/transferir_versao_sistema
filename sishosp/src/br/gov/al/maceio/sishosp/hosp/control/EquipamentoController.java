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
import br.gov.al.maceio.sishosp.hosp.dao.EncaminhadoDAO;
import br.gov.al.maceio.sishosp.hosp.dao.EquipamentoDAO;
import br.gov.al.maceio.sishosp.hosp.model.EquipamentoBean;

@ManagedBean(name = "EquipamentoController")
@ViewScoped
public class EquipamentoController implements Serializable {

	private static final long serialVersionUID = 1L;
	private EquipamentoBean equipamento;
	private List<EquipamentoBean> listaEquipamentos;
	private Integer tipoBuscar;
	private String descricaoBusca;
	private int tipo;
	private String cabecalho;

	private Integer abaAtiva = 0;

	public EquipamentoController() {
		equipamento = new EquipamentoBean();

		listaEquipamentos = new ArrayList<>();
		listaEquipamentos = null;
		this.descricaoBusca = new String();
		this.cabecalho = "";

	}

	public String redirectEdit() {
		return "cadastroEquipamento?faces-redirect=true&amp;id="
				+ this.equipamento.getId_equipamento() + "&amp;tipo=" + tipo;
	}

	public String redirectInsert() {
		return "cadastroEquipamento?faces-redirect=true&amp;tipo=" + tipo;
	}

	public void limparDados() {
		equipamento = new EquipamentoBean();
		listaEquipamentos = new ArrayList<>();
		this.descricaoBusca = new String();

	}

	public void gravarEquipamento() throws ProjetoException, SQLException {
		EquipamentoDAO gDao = new EquipamentoDAO();
		boolean cadastrou = gDao.gravarEquipamento(equipamento);

		if (cadastrou == true) {
			limparDados();
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Equipamento cadastrado com sucesso!", "Sucesso");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		} else {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Ocorreu um erro durante o cadastro!", "Erro");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
	}

	public void alterarEquipamento() throws ProjetoException, SQLException {
		EquipamentoDAO gDao = new EquipamentoDAO();
		boolean alterou = gDao.alterarEquipamento(equipamento);
		if (alterou == true) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Equipamento alterado com sucesso!", "Sucesso");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		} else {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Ocorreu um erro durante o cadastro!", "Erro");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
		listaEquipamentos = gDao.listarEquipamentos();

	}

	public void excluirEquipamento() throws ProjetoException, SQLException {
		EquipamentoDAO gDao = new EquipamentoDAO();
		boolean ok = gDao.excluirEquipamento(equipamento);
		if (ok == true) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Equipamento excluído com sucesso!", "Sucesso");
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
		listaEquipamentos = gDao.listarEquipamentos();
	}

	public void getEditEquipamento() throws ProjetoException {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		Map<String, String> params = facesContext.getExternalContext()
				.getRequestParameterMap();
		if (params.get("id") != null) {
			Integer id = Integer.parseInt(params.get("id"));
			tipo = Integer.parseInt(params.get("tipo"));
			EquipamentoDAO cDao = new EquipamentoDAO();
			this.equipamento = cDao.buscaEquipamentoPorId(id);
		} else {
			tipo = Integer.parseInt(params.get("tipo"));

		}

	}

	public EquipamentoBean getEquipamento() {
		return equipamento;
	}

	public void setEquipamento(EquipamentoBean equipamento) {
		this.equipamento = equipamento;
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
			cabecalho = "Inclusão de Equipamento";
		} else if (this.tipo == 2) {
			cabecalho = "Alteração de Equipamento";
		}
		return cabecalho;
	}

	public List<EquipamentoBean> getListaEquipamentos() throws SQLException,
			ProjetoException {
		if (listaEquipamentos == null) {

			EquipamentoDAO fdao = new EquipamentoDAO();
			listaEquipamentos = fdao.listarEquipamentos();
		}
		return listaEquipamentos;
	}

	public void setListaEquipamentos(List<EquipamentoBean> listaEquipamentos) {
		this.listaEquipamentos = listaEquipamentos;
	}

}
