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

import org.primefaces.context.RequestContext;

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

}
