package br.gov.al.maceio.sishosp.hosp.control;

import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import br.gov.al.maceio.sishosp.hosp.dao.AgendaDAO;
import br.gov.al.maceio.sishosp.hosp.model.AgendaBean;
import br.gov.al.maceio.sishosp.hosp.model.BloqueioBean;
import br.gov.al.maceio.sishosp.hosp.model.FeriadoBean;

public class AgendaController {

	private AgendaBean agenda;

	private List<AgendaBean> listaNovosAgendamentos;

	private AgendaDAO aDao = new AgendaDAO();

	public AgendaController() {
		this.agenda = new AgendaBean();
		this.listaNovosAgendamentos = new ArrayList<AgendaBean>();
	}

	public void limparDados() {
		this.agenda = new AgendaBean();
		this.listaNovosAgendamentos = new ArrayList<AgendaBean>();
	}

	public List<AgendaBean> getListaNovosAgendamentos() {
		return listaNovosAgendamentos;
	}

	public void setListaNovosAgendamentos(
			List<AgendaBean> listaNovosAgendamentos) {
		this.listaNovosAgendamentos = listaNovosAgendamentos;
	}

	public AgendaBean getAgenda() {
		return agenda;
	}

	public void setAgenda(AgendaBean agenda) {
		this.agenda = agenda;
	}

	public void verificaDisponibilidadeData() {
		FeriadoBean feriado = aDao.verificarFeriado(this.agenda.getDataAtendimento());
		List<BloqueioBean> bloqueio = new ArrayList<BloqueioBean>();
		if(this.agenda.getProfissional()!=null){
			bloqueio = aDao.verificarBloqueioProfissional(
					this.agenda.getProfissional(), this.agenda.getDataAtendimento(), this.agenda.getTurno());
		}
		if (feriado == null) {
			System.out.println("NAO TEM FERIADO");
		} else {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Data bloqueada! " + feriado.getDescFeriado(), "Feriado");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
		
		if(bloqueio.isEmpty()){
			System.out.println("nao tem bloqueio");
		}else{
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Data bloqueada para este profissional!", "Bloqueio");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
	}

	public void verAgenda() {
		System.out.println("VAI VER AGENDA");
		this.agenda.setMax(aDao.verQtdMaxAgenda(this.agenda));
		this.agenda.setQtd(aDao.verQtdAgendados(this.agenda));
	}

	public void addListaNovosAgendamentos() {
		this.listaNovosAgendamentos.add(this.agenda);
		// this.agenda = new AgendaBean();
	}

	public void gravarAgenda() {
		boolean ok = aDao.gravarAgenda(agenda);
		if (ok) {
			limparDados();
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Agenda cadastrada com sucesso!", "Sucesso");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		} else {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Ocorreu um erro durante o cadastro!", "Erro");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
	}
}
