package br.gov.al.maceio.sishosp.hosp.control;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;

import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.hosp.dao.FeriadoDAO;
import br.gov.al.maceio.sishosp.hosp.model.FeriadoBean;

public class FeriadoController {

	private FeriadoBean feriado;
	private List<FeriadoBean> listaFeriados;
	private String tipo;
	private String descricaoBusca;
	private Integer tipoBuscar;
	private Date dataBuscar;
	private String cabecalho;

	private Integer abaAtiva = 0;

	private FeriadoDAO fDao = new FeriadoDAO();

	public FeriadoController() {
		this.feriado = new FeriadoBean();
		this.listaFeriados = null;
		this.tipo = new String();
		this.descricaoBusca = new String();
		this.dataBuscar = null;
		this.tipoBuscar = 1;
	}

	public void limparDados() {
		this.feriado = new FeriadoBean();
		this.listaFeriados = null;
		this.descricaoBusca = new String();
		this.dataBuscar = null;
		this.tipoBuscar = 1;
	}

	public FeriadoBean getFeriado() {
		return feriado;
	}

	public void setFeriado(FeriadoBean feriado) {
		this.feriado = feriado;
	}

	public List<FeriadoBean> getListaFeriados() {
		if (this.listaFeriados == null) {
			this.listaFeriados = fDao.listarFeriado();
		}
		return listaFeriados;
	}

	public void setListaFeriados(List<FeriadoBean> listaFeriados) {
		this.listaFeriados = listaFeriados;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getDescricaoBusca() {
		return descricaoBusca;
	}

	public void setDescricaoBusca(String descricaoBusca) {
		this.descricaoBusca = descricaoBusca;
	}

	public Integer getTipoBuscar() {
		return tipoBuscar;
	}

	public void setTipoBuscar(Integer tipoBuscar) {
		this.tipoBuscar = tipoBuscar;
	}

	public Integer getAbaAtiva() {
		return abaAtiva;
	}

	public void setAbaAtiva(Integer abaAtiva) {
		this.abaAtiva = abaAtiva;
	}

	public String getCabecalho() {
		if (this.tipo.equals("I")) {
			cabecalho = "CADASTRO DE FERIADO";
		} else if (this.tipo.equals("A")) {
			cabecalho = "ALTERAR FERIADO";
		}

		return cabecalho;
	}

	public void setCabecalho(String cabecalho) {
		this.cabecalho = cabecalho;
	}

	public void buscarFeriado() {
		this.listaFeriados = fDao
				.listarFeriadoBusca(descricaoBusca, tipoBuscar, dataBuscar);
	}

	public Date getDataBuscar() {
		return dataBuscar;
	}

	public void setDataBuscar(Date dataBuscar) {
		this.dataBuscar = dataBuscar;
	}

	public void gravarFeriado() throws ProjetoException, SQLException {
		boolean cadastrou = fDao.gravarFeriado(feriado);

		if (cadastrou == true) {
			limparDados();
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Feriado cadastrado com sucesso!", "Sucesso");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		} else {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Ocorreu um erro durante o cadastro!", "Erro");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
	}

	public String alterarFeriado() throws ProjetoException {
		boolean alterou = fDao.alterarFeriado(feriado);
		if (alterou == true) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Feriado alterado com sucesso!", "Sucesso");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			this.listaFeriados = fDao.listarFeriado();
			return "/pages/sishosp/gerenciarFeriados.xhtml?faces-redirect=true";
		} else {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Ocorreu um erro durante o cadastro!", "Erro");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return "";
		}

	}

	public void excluirFeriado() throws ProjetoException {
		boolean ok = fDao.excluirFeriado(feriado);
		if (ok == true) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Cbo excluido com sucesso!", "Sucesso");
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
		this.listaFeriados = fDao.listarFeriado();
	}
}
