package br.gov.al.maceio.sishosp.hosp.control;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import net.bootsfaces.component.row.RowBeanInfo;

import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;

import br.gov.al.maceio.sishosp.acl.model.FuncionarioBean;
import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.hosp.dao.CboDAO;
import br.gov.al.maceio.sishosp.hosp.dao.GerenciarPacienteDAO;
import br.gov.al.maceio.sishosp.hosp.dao.GrupoDAO;
import br.gov.al.maceio.sishosp.hosp.dao.LaudoDAO;
import br.gov.al.maceio.sishosp.hosp.dao.ProgramaDAO;
import br.gov.al.maceio.sishosp.hosp.model.CidBean;
import br.gov.al.maceio.sishosp.hosp.model.EquipamentoBean;
import br.gov.al.maceio.sishosp.hosp.model.EquipeBean;
import br.gov.al.maceio.sishosp.hosp.model.FornecedorBean;
import br.gov.al.maceio.sishosp.hosp.model.GerenciarPacienteBean;
import br.gov.al.maceio.sishosp.hosp.model.GrupoBean;
import br.gov.al.maceio.sishosp.hosp.model.LaudoBean;
import br.gov.al.maceio.sishosp.hosp.model.PacienteBean;
import br.gov.al.maceio.sishosp.hosp.model.ProcedimentoBean;
import br.gov.al.maceio.sishosp.hosp.model.ProgramaBean;
import br.gov.al.maceio.sishosp.hosp.model.TipoAtendimentoBean;

@ManagedBean(name = "GerenciarPacienteController")
@ViewScoped
public class GerenciarPacienteController implements Serializable {

	private static final long serialVersionUID = 1L;
	private GerenciarPacienteBean gerenciarpaciente;
	private GerenciarPacienteBean rowBean;
	private List<GerenciarPacienteBean> listaPacientes;
	private GerenciarPacienteDAO gDao = new GerenciarPacienteDAO();
	private String busca = "N";
	private Boolean apenasLeitura;

	public GerenciarPacienteController() {
		gerenciarpaciente = new GerenciarPacienteBean();
		gerenciarpaciente.setPrograma(null);
		gerenciarpaciente.setStatus("T");
		listaPacientes = new ArrayList<GerenciarPacienteBean>();
		apenasLeitura = false;
		rowBean = new GerenciarPacienteBean();
	}

	public void buscarPacientesInstituicao() throws ProjetoException {
		busca = "S";
		carregarPacientesInstituicao();
		apenasLeitura = true;

	}

	public void limparBusca() throws ProjetoException {
		apenasLeitura = false;
	}

	public List<GrupoBean> listaGrupoAutoComplete(String query)
			throws ProjetoException {

		GrupoDAO gDao = new GrupoDAO();

		if (gerenciarpaciente.getPrograma().getIdPrograma() != null) {
			return gDao.listarGruposAutoComplete(query,
					this.gerenciarpaciente.getPrograma());
		} else {
			return null;
		}

	}

	public List<GrupoBean> listaGrupoLista() throws ProjetoException {

		GrupoDAO gDao = new GrupoDAO();
		if (gerenciarpaciente.getPrograma() != null) {
			if (gerenciarpaciente.getPrograma().getIdPrograma() != null) {
				return gDao.listarGruposPorPrograma(this.gerenciarpaciente
						.getPrograma().getIdPrograma());
			} else {
				return null;
			}
		} else {
			return null;
		}

	}

	public void carregarPacientesInstituicao() throws ProjetoException {
		if (busca.equals("N")) {
			listaPacientes = gDao.carregarPacientesInstituicao();
		} else {
			listaPacientes = gDao
					.carregarPacientesInstituicaoBusca(gerenciarpaciente);
		}
	}

	public void onRowSelect(SelectEvent event) throws ProjetoException {
		//IMPLEMENTAR ALGO SE PRECISAR
		//System.out.println("rowbean: " + rowBean.getStatus());
	}

	public GerenciarPacienteBean getGerenciarpaciente() {
		return gerenciarpaciente;
	}

	public void setGerenciarpaciente(GerenciarPacienteBean gerenciarpaciente) {
		this.gerenciarpaciente = gerenciarpaciente;
	}

	public List<GerenciarPacienteBean> getListaPacientes() {
		return listaPacientes;
	}

	public void setListaPacientes(List<GerenciarPacienteBean> listaPacientes) {
		this.listaPacientes = listaPacientes;
	}

	public Boolean getApenasLeitura() {
		return apenasLeitura;
	}

	public void setApenasLeitura(Boolean apenasLeitura) {
		this.apenasLeitura = apenasLeitura;
	}

	public String getBusca() {
		return busca;
	}

	public void setBusca(String busca) {
		this.busca = busca;
	}

	public GerenciarPacienteBean getRowBean() {
		return rowBean;
	}

	public void setRowBean(GerenciarPacienteBean rowBean) {
		this.rowBean = rowBean;
	}

}
