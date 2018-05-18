package br.gov.al.maceio.sishosp.hosp.control;

import java.io.Serializable;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
import br.gov.al.maceio.sishosp.hosp.dao.EnderecoDAO;
import br.gov.al.maceio.sishosp.hosp.dao.EquipeDAO;
import br.gov.al.maceio.sishosp.hosp.dao.GrupoDAO;
import br.gov.al.maceio.sishosp.hosp.dao.PacienteDAO;
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
	private FuncionarioBean funcionario;
	private FuncionarioDAO fDao = new FuncionarioDAO();

	public AtendimentoController() {
		this.atendimento = new AtendimentoBean();
		listAtendimentos = new ArrayList<AtendimentoBean>();
		funcionario = new FuncionarioBean();
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

	public String redirectAtendimento() {
		if (atendimento.getEhEquipe().equals("Sim")) {
			return "atendimentoEquipe?faces-redirect=true&amp;id="
					+ this.atendimento.getId();
		} else {
			return "atendimentoProfissional?faces-redirect=true&amp;id="
					+ this.atendimento.getId();
		}
	}

	public void getCarregaAtendimentoProfissional() throws ProjetoException,
			SQLException {
		FuncionarioBean user_session = (FuncionarioBean) FacesContext
				.getCurrentInstance().getExternalContext().getSessionMap()
				.get("obj_usuario");

		FacesContext facesContext = FacesContext.getCurrentInstance();
		Map<String, String> params = facesContext.getExternalContext()
				.getRequestParameterMap();
		if (params.get("id") != null) {
			Integer id = Integer.parseInt(params.get("id"));
			aDao = new AtendimentoDAO();

			Integer valor = Integer.valueOf(user_session.getId().toString());
			this.atendimento = aDao.listarAtendimentoProfissionalPorId(id);
			this.funcionario = fDao.buscarProfissionalPorId(valor);
		}
	}

	public void realizarAtendimentoProfissional() throws ProjetoException {
		boolean alterou = aDao.realizaAtendimentoProfissional(funcionario,
				atendimento);

		if (alterou == true) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Atendimento realizado com sucesso!", "Sucesso");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		} else {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Ocorreu um erro durante o atendimento!", "Erro");
			FacesContext.getCurrentInstance().addMessage(null, msg);

		}
	}

	public void limparAtendimentoProfissional() throws ProjetoException {

		boolean alterou = aDao.limpaAtendimentoProfissional(atendimento);

		if (alterou == true) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Atendimento limpo com sucesso!", "Sucesso");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		} else {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Ocorreu um erro durante o atendimento!", "Erro");
			FacesContext.getCurrentInstance().addMessage(null, msg);

		}
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

	public FuncionarioBean getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(FuncionarioBean funcionario) {
		this.funcionario = funcionario;
	}

}
