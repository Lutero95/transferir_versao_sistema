package br.gov.al.maceio.sishosp.hosp.control;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.hosp.dao.AgendaDAO;
import br.gov.al.maceio.sishosp.hosp.model.AgendaBean;
import br.gov.al.maceio.sishosp.hosp.model.BloqueioBean;
import br.gov.al.maceio.sishosp.hosp.model.FeriadoBean;
import br.gov.al.maceio.sishosp.hosp.model.TipoAtendimentoBean;

public class AgendaController {

	private AgendaBean agenda;

	// consultas
	private Date dataAtendimentoC;
	private String cnsC;
	private Integer protuarioC;
	private TipoAtendimentoBean tipoC;

	private List<AgendaBean> listaNovosAgendamentos;
	private List<AgendaBean> listaAgendamentosData;
	private List<AgendaBean> listaConsulta;

	private AgendaDAO aDao = new AgendaDAO();

	public AgendaController() {
		this.agenda = new AgendaBean();
		this.listaNovosAgendamentos = new ArrayList<AgendaBean>();
		this.listaAgendamentosData = new ArrayList<AgendaBean>();
		this.listaConsulta = new ArrayList<AgendaBean>();
		this.dataAtendimentoC = null;
		this.cnsC = new String();
		this.protuarioC = null;
		this.tipoC = new TipoAtendimentoBean();
	}

	public void limparDados() {
		this.agenda = new AgendaBean();
		this.listaNovosAgendamentos = new ArrayList<AgendaBean>();
		this.listaAgendamentosData = new ArrayList<AgendaBean>();
		this.listaConsulta = new ArrayList<AgendaBean>();
		this.dataAtendimentoC = null;
		this.cnsC = new String();
		this.protuarioC = null;
		this.tipoC = new TipoAtendimentoBean();
	}

	public List<AgendaBean> getListaNovosAgendamentos() {
		return listaNovosAgendamentos;
	}

	public void setListaNovosAgendamentos(List<AgendaBean> listaNovosAgendamentos) {
		this.listaNovosAgendamentos = listaNovosAgendamentos;
	}

	public AgendaBean getAgenda() {
		return agenda;
	}

	public void setAgenda(AgendaBean agenda) {
		this.agenda = agenda;
	}

	public List<AgendaBean> getListaAgendamentosData() {
		return listaAgendamentosData;
	}

	public void setListaAgendamentosData(List<AgendaBean> listaAgendamentosData) {
		this.listaAgendamentosData = listaAgendamentosData;
	}

	public Date getDataAtendimentoC() {
		return dataAtendimentoC;
	}

	public void setDataAtendimentoC(Date dataAtendimentoC) {
		this.dataAtendimentoC = dataAtendimentoC;
	}

	public String getCnsC() {
		return cnsC;
	}

	public void setCnsC(String cnsC) {
		this.cnsC = cnsC;
	}

	public Integer getProtuarioC() {
		return protuarioC;
	}

	public void setProtuarioC(Integer protuarioC) {
		this.protuarioC = protuarioC;
	}

	public TipoAtendimentoBean getTipoC() {
		return tipoC;
	}

	public void setTipoC(TipoAtendimentoBean tipoC) {
		this.tipoC = tipoC;
	}

	public List<AgendaBean> getListaConsulta() {
		return listaConsulta;
	}

	public void setListaConsulta(List<AgendaBean> listaConsulta) {
		this.listaConsulta = listaConsulta;
	}

	public void verificaDisponibilidadeData() {
		FeriadoBean feriado = aDao.verificarFeriado(this.agenda.getDataAtendimento());
		List<BloqueioBean> bloqueio = new ArrayList<BloqueioBean>();
		if (this.agenda.getProfissional().getIdProfissional() != null) {
			bloqueio = aDao.verificarBloqueioProfissional(this.agenda.getProfissional(),
					this.agenda.getDataAtendimento(), this.agenda.getTurno());
		}
		if (feriado != null) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Data bloqueada! "
					+ feriado.getDescFeriado(), "Feriado");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}

		if (!bloqueio.isEmpty()) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Data bloqueada para este profissional!",
					"Bloqueio");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
	}

	public void verAgenda() throws ProjetoException {
		System.out.println("VAI VER AGENDA");
		boolean dtEspecifica = aDao.buscarDataEspecifica(this.agenda);
		boolean diaSem = aDao.buscarDiaSemana(this.agenda);
		if (dtEspecifica) {
			System.out.println("Eh data especifica");
			listarAgendamentosData();
			this.agenda.setMax(aDao.verQtdMaxAgendaData(this.agenda));
			this.agenda.setQtd(aDao.verQtdAgendadosData(this.agenda));
		} else if (diaSem) {
			System.out.println("Eh dia Semana");
			listarAgendamentosData();
			this.agenda.setMax(aDao.verQtdMaxAgendaEspec(this.agenda));
			this.agenda.setQtd(aDao.verQtdAgendadosEspec(this.agenda));
		} else {
			System.out.println("Eh porra nenhuma");
			listarAgendamentosData();
			this.agenda.setMax(0);
			this.agenda.setQtd(0);
		}

	}

	public void addListaNovosAgendamentos() {
		this.listaNovosAgendamentos.add(this.agenda);
		// this.agenda = new AgendaBean();
	}

	public void listarAgendamentosData() throws ProjetoException {
		this.listaAgendamentosData = aDao.listarAgendamentosData(this.agenda);
	}

	public void gravarAgenda() {
		// verificar se existe algum campo nao preenchido
		if (this.agenda.getPaciente() == null || this.agenda.getPrograma() == null || this.agenda.getGrupo() == null
				|| this.agenda.getTipoAt() == null || this.agenda.getDataAtendimento() == null) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Campo(s) obrigatório(s) em falta!",
					"Erro");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return;
		}

		// verificar as quantidades de vagas
		if (this.agenda.getMax() <= 0) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Quantidade máxima inválida!", "Erro");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return;
		}

		if (this.agenda.getQtd() >= this.agenda.getMax()) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Quantidade de agendamentos está no limite!", "Erro");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return;
		}
		boolean ok = aDao.gravarAgenda(agenda);
		if (ok) {
			limparDados();
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Agenda cadastrada com sucesso!", "Sucesso");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		} else {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ocorreu um erro durante o cadastro!",
					"Erro");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
	}

	public void consultarAgenda() {
		this.listaConsulta = aDao.consultarAgenda(this.dataAtendimentoC, this.protuarioC, this.cnsC, this.tipoC);
	}

	public void excluirAgendamento() {
		boolean ok = aDao.excluirAgendamento(this.agenda);
		if (ok) {
			limparDados();
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Agendamento excluído com sucesso!",
					"Sucesso");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		} else {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ocorreu um erro durante a exclusão!",
					"Erro");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
	}
}
