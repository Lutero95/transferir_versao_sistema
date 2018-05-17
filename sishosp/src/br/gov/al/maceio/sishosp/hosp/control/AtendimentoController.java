package br.gov.al.maceio.sishosp.hosp.control;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;

import br.gov.al.maceio.sishosp.acl.dao.FuncionarioDAO;
import br.gov.al.maceio.sishosp.acl.model.FuncionarioBean;
import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.hosp.dao.AgendaDAO;
import br.gov.al.maceio.sishosp.hosp.dao.AtendimentoDAO;
import br.gov.al.maceio.sishosp.hosp.dao.EquipeDAO;
import br.gov.al.maceio.sishosp.hosp.dao.GrupoDAO;
import br.gov.al.maceio.sishosp.hosp.dao.ProgramaDAO;
import br.gov.al.maceio.sishosp.hosp.dao.TipoAtendimentoDAO;
import br.gov.al.maceio.sishosp.hosp.model.AgendaBean;
import br.gov.al.maceio.sishosp.hosp.model.AtendimentoBean;
import br.gov.al.maceio.sishosp.hosp.model.BloqueioBean;
import br.gov.al.maceio.sishosp.hosp.model.EquipeBean;
import br.gov.al.maceio.sishosp.hosp.model.FeriadoBean;
import br.gov.al.maceio.sishosp.hosp.model.GrupoBean;
import br.gov.al.maceio.sishosp.hosp.model.ProgramaBean;
import br.gov.al.maceio.sishosp.hosp.model.TipoAtendimentoBean;

@ManagedBean(name = "AtendimentoController")
@ViewScoped
public class AtendimentoController implements Serializable {

	private static final long serialVersionUID = 1L;

	private AtendimentoBean atendimento;
	private List<AtendimentoBean> listAtendimentos;
	AtendimentoDAO aDao = new AtendimentoDAO();

	public AtendimentoController() {
		this.atendimento = new AtendimentoBean();
		listAtendimentos = new ArrayList<AtendimentoBean>();
	}

	public void limparDados() {

	}

	public void consultarAtendimentos() throws ProjetoException {
		if (this.atendimento.getDataAtendimentoInicio() == null
				|| this.atendimento.getDataAtendimentoFinal() == null) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Selecione as datas para filtrar os atendimentos!", "Erro");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return;
		}
		listarAtendimentos();
	}

	public void listarAtendimentos() throws ProjetoException {
		this.listAtendimentos = aDao.carregaAtendimentos(atendimento);
	}

	public AtendimentoBean getAtendimento() {
		return atendimento;
	}

	public void setAtendimento(AtendimentoBean atendimento) {
		this.atendimento = atendimento;
	}

	public List<AtendimentoBean> getListAtendimentos() {
		return listAtendimentos;
	}

	public void setListAtendimentos(List<AtendimentoBean> listAtendimentos) {
		this.listAtendimentos = listAtendimentos;
	}

}
