package br.gov.al.maceio.sishosp.hosp.control;

import java.io.Serializable;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.event.SelectEvent;

import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.hosp.dao.AgendaDAO;
import br.gov.al.maceio.sishosp.hosp.dao.EquipeDAO;
import br.gov.al.maceio.sishosp.hosp.dao.GrupoDAO;
import br.gov.al.maceio.sishosp.hosp.dao.ProfissionalDAO;
import br.gov.al.maceio.sishosp.hosp.dao.ProgramaDAO;
import br.gov.al.maceio.sishosp.hosp.dao.TipoAtendimentoDAO;
import br.gov.al.maceio.sishosp.hosp.model.AgendaBean;
import br.gov.al.maceio.sishosp.hosp.model.BloqueioBean;
import br.gov.al.maceio.sishosp.hosp.model.EquipeBean;
import br.gov.al.maceio.sishosp.hosp.model.FeriadoBean;
import br.gov.al.maceio.sishosp.hosp.model.GrupoBean;
import br.gov.al.maceio.sishosp.hosp.model.ProfissionalBean;
import br.gov.al.maceio.sishosp.hosp.model.ProgramaBean;
import br.gov.al.maceio.sishosp.hosp.model.TipoAtendimentoBean;

@ManagedBean(name = "AgendaController")
@ViewScoped
public class AgendaController implements Serializable {

	private static final long serialVersionUID = 1L;

	private AgendaBean agenda;

	private Date dataAtendimentoC;
	private String cnsC;
	private Integer protuarioC;
	private ProfissionalDAO pDao = new ProfissionalDAO();
	private TipoAtendimentoBean tipoC;
	private ProgramaBean programaSelecionado;
	private List<AgendaBean> listaNovosAgendamentos;
	private List<AgendaBean> listaAgendamentosData;
	private List<GrupoBean> listaGruposProgramas;
	private GrupoBean grupoSelecionado;
	private List<AgendaBean> listaConsulta;
	private List<ProfissionalBean> listaProfissional;
	private boolean habilitarDetalhes;
	private List<TipoAtendimentoBean> listaTipos;
	TipoAtendimentoDAO tDao = new TipoAtendimentoDAO();

	private String situacao;

	private AgendaDAO aDao = new AgendaDAO();

	public AgendaController() {
		this.agenda = new AgendaBean();
		grupoSelecionado = new GrupoBean();
		listaGruposProgramas = new ArrayList<GrupoBean>();
		listaTipos = new ArrayList<TipoAtendimentoBean>();
		programaSelecionado = new ProgramaBean();
		this.listaNovosAgendamentos = new ArrayList<AgendaBean>();
		this.listaProfissional = new ArrayList<ProfissionalBean>();
		this.listaAgendamentosData = new ArrayList<AgendaBean>();
		this.listaConsulta = new ArrayList<AgendaBean>();
		this.dataAtendimentoC = null;
		this.cnsC = new String();
		this.protuarioC = null;
		this.tipoC = new TipoAtendimentoBean();
		this.situacao = new String();
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
		this.habilitarDetalhes = false;
		this.situacao = new String();
	}

	public void verificaDisponibilidadeData() throws ProjetoException {
		if (!agenda.getTipoAt().isEquipe()) {
			FeriadoBean feriado = aDao.verificarFeriado(this.agenda
					.getDataAtendimento());
			List<BloqueioBean> bloqueio = new ArrayList<BloqueioBean>();
			if (this.agenda.getProfissional().getIdProfissional() != null) {
				bloqueio = aDao.verificarBloqueioProfissional(
						this.agenda.getProfissional(),
						this.agenda.getDataAtendimento(),
						this.agenda.getTurno());
			}
			if (feriado != null) {
				FacesMessage msg = new FacesMessage(
						FacesMessage.SEVERITY_ERROR, "Data bloqueada! "
								+ feriado.getDescFeriado(), "Feriado");
				FacesContext.getCurrentInstance().addMessage(null, msg);
			} else

			if (!bloqueio.isEmpty()) {
				FacesMessage msg = new FacesMessage(
						FacesMessage.SEVERITY_ERROR,
						"Data bloqueada para este profissional!", "Bloqueio");
				FacesContext.getCurrentInstance().addMessage(null, msg);
			} else
				verAgenda();

		}

	}

	public void verAgenda() throws ProjetoException {
		if (this.agenda.getPaciente() == null
				|| this.agenda.getPrograma() == null
				|| this.agenda.getGrupo() == null
				|| this.agenda.getTipoAt() == null
				|| this.agenda.getDataAtendimento() == null) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Campo(s) obrigatório(s) em falta!", "Erro");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return;
		}
		boolean dtEspecifica = aDao.buscarDataEspecifica(this.agenda);
		boolean diaSem = aDao.buscarDiaSemana(this.agenda);
		boolean limitePorTipoAtend = aDao.buscarTabTipoAtendAgenda(this.agenda);
		if (dtEspecifica && limitePorTipoAtend) {
			listarAgendamentosData();
			this.agenda.setMax(aDao.verQtdMaxAgendaData(this.agenda));
			this.agenda.setQtd(aDao.verQtdAgendadosData(this.agenda));
		} else if (diaSem && limitePorTipoAtend) {
			listarAgendamentosData();
			this.agenda.setMax(aDao.verQtdMaxAgendaEspec(this.agenda));
			this.agenda.setQtd(aDao.verQtdAgendadosEspec(this.agenda));
		} else {
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

	public void gravarAgenda() throws ProjetoException {
		// verificar se existe algum campo nao preenchido
		if (this.agenda.getPaciente() == null
				|| this.agenda.getPrograma() == null
				|| this.agenda.getGrupo() == null
				|| this.agenda.getTipoAt() == null
				|| this.agenda.getDataAtendimento() == null) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Campo(s) obrigatório(s) em falta!", "Erro");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return;
		}

		// verificar as quantidades de vagas
		if (this.agenda.getMax() <= 0) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Quantidade máxima inválida!", "Erro");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return;
		}

		// verificar a quantidade de agendamentos
		if (this.agenda.getQtd() >= this.agenda.getMax()) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Quantidade de agendamentos está no limite!", "Erro");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return;
		}

		if (this.listaNovosAgendamentos.isEmpty()) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"A lista de novos agendamentos está vazia!", "Erro");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return;
		}
		boolean ok = aDao
				.gravarAgenda(this.agenda, this.listaNovosAgendamentos);
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
		limparDados();
	}

	public void consultarAgenda() throws ProjetoException {
		if (this.dataAtendimentoC == null) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Selecione uma data de atendimento!", "Erro");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return;
		}
		this.listaConsulta = aDao.consultarAgenda(this.dataAtendimentoC,
				this.protuarioC, this.cnsC, this.tipoC);
	}

	public void excluirAgendamento() throws ProjetoException {
		boolean ok = aDao.excluirAgendamento(this.agenda);
		if (ok) {
			limparDados();
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Agendamento excluído com sucesso!", "Sucesso");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		} else {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Ocorreu um erro durante a exclusão!", "Erro");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
		limparDados();
	}

	public void limparNaBuscaPaciente() {
		this.agenda.setPrograma(null);
		this.agenda.setGrupo(null);
		this.agenda.setTipoAt(null);
		this.agenda.setProfissional(new ProfissionalBean());
		this.agenda.setEquipe(new EquipeBean());
		this.agenda.setObservacao(new String());
		this.agenda.setDataAtendimento(null);
		this.agenda.setQtd(null);
		this.agenda.setMax(null);
	}

	public void limparNaBuscaPrograma() {
		this.agenda.setGrupo(null);
		this.agenda.setTipoAt(null);
		this.agenda.setProfissional(new ProfissionalBean());
		this.agenda.setEquipe(new EquipeBean());
		this.agenda.setObservacao(new String());
		this.agenda.setDataAtendimento(null);
		this.agenda.setQtd(null);
		this.agenda.setMax(null);
	}

	public void limparNaBuscaGrupo() {
		this.agenda.setTipoAt(null);
		this.agenda.setProfissional(new ProfissionalBean());
		this.agenda.setEquipe(new EquipeBean());
		this.agenda.setObservacao(new String());
		this.agenda.setDataAtendimento(null);
		this.agenda.setQtd(null);
		this.agenda.setMax(null);
	}

	public void limparNaBuscaTipo() {
		this.agenda.setProfissional(new ProfissionalBean());
		this.agenda.setEquipe(new EquipeBean());
		this.agenda.setObservacao(new String());
		this.agenda.setDataAtendimento(null);
		this.agenda.setQtd(null);
		this.agenda.setMax(null);
	}

	public void limparNaBuscaEquipeProf() {
		this.agenda.setObservacao(new String());
		this.agenda.setDataAtendimento(null);
		this.agenda.setQtd(null);
		this.agenda.setMax(null);
	}

	public void habilitarDetalhes() {
		if (this.habilitarDetalhes == false) {
			this.habilitarDetalhes = true;
		}
	}

	public void confirmarAtendimento() throws ProjetoException {
		boolean ok = aDao.confirmarAtendimento(this.agenda, situacao);
		if (ok) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Atendimento Confirmado", "");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		} else {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Ocorreu um erro ao confirmar este atendimento!", "Erro");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
	}

	public void selectPrograma(SelectEvent event) throws ProjetoException {
		this.programaSelecionado = (ProgramaBean) event.getObject();
		atualizaListaGrupos(programaSelecionado);
		limparNaBuscaPrograma();
	}

	public void atualizaListaGrupos(ProgramaBean p) throws ProjetoException {
		GrupoDAO gDao = new GrupoDAO();
		this.programaSelecionado = p;
		this.listaGruposProgramas = gDao.listarGruposPorPrograma(p
				.getIdPrograma());
		for (GrupoBean g : listaGruposProgramas) {
		}

	}

	public void atualizaListaTipos(GrupoBean g) throws ProjetoException {
		this.grupoSelecionado = g;
		this.listaTipos = tDao.listarTipoAtPorGrupo(g.getIdGrupo());
	}

	public List<GrupoBean> listaGrupoAutoComplete(String query)
			throws ProjetoException {

		GrupoDAO gDao = new GrupoDAO();

		if (agenda.getPrograma().getIdPrograma() != null) {
			return gDao.listarGruposAutoComplete(query,
					this.agenda.getPrograma());
		} else {
			return null;
		}

	}
	
	public List<EquipeBean> listaEquipeAutoComplete(String query)
			throws ProjetoException {
		EquipeDAO eDao = new EquipeDAO();
		
		List<EquipeBean> result = eDao.listarEquipePorGrupoAutoComplete(query,
				agenda.getGrupo().getIdGrupo());
		return result;
	}
	
	public List<ProfissionalBean> listaProfissionalPorGrupoAutoComplete(String query)
			throws ProjetoException {
		List<ProfissionalBean> result = pDao.listarProfissionalBuscaPorGrupo(query, agenda.getGrupo().getIdGrupo());
		return result;
	}

	public List<TipoAtendimentoBean> listaTipoAtAutoComplete(String query)
			throws ProjetoException {
		if (agenda.getGrupo() != null) {
			return tDao.listarTipoAtAutoComplete(query, this.agenda.getGrupo());
		} else
			return null;
	}

	public void selectGrupo(SelectEvent event) throws ProjetoException {
		this.grupoSelecionado = (GrupoBean) event.getObject();
		atualizaListaTipos(grupoSelecionado);
		atualizaListaProfPorGrupo();
		limparNaBuscaGrupo();
	}
	
	

	public void atualizaListaProfPorGrupo() throws ProjetoException {
		this.listaProfissional = pDao
				.listarProfissionalPorGrupo(this.grupoSelecionado.getIdGrupo());
	}

	/**
	 * @return the programaSelecionado
	 */
	public ProgramaBean getProgramaSelecionado() {
		return programaSelecionado;
	}

	/**
	 * @param programaSelecionado
	 *            the programaSelecionado to set
	 */
	public void setProgramaSelecionado(ProgramaBean programaSelecionado) {
		this.programaSelecionado = programaSelecionado;
	}

	/**
	 * @return the listaGruposProgramas
	 */
	public List<GrupoBean> getListaGruposProgramas() {
		return listaGruposProgramas;
	}

	/**
	 * @param listaGruposProgramas
	 *            the listaGruposProgramas to set
	 */
	public void setListaGruposProgramas(List<GrupoBean> listaGruposProgramas) {
		this.listaGruposProgramas = listaGruposProgramas;
	}

	/**
	 * @return the grupoSelecionado
	 */
	public GrupoBean getGrupoSelecionado() {
		return grupoSelecionado;
	}

	/**
	 * @param grupoSelecionado
	 *            the grupoSelecionado to set
	 */
	public void setGrupoSelecionado(GrupoBean grupoSelecionado) {
		this.grupoSelecionado = grupoSelecionado;
	}

	/**
	 * @return the listaProfissional
	 */
	public List<ProfissionalBean> getListaProfissional() {
		return listaProfissional;
	}

	/**
	 * @param listaProfissional
	 *            the listaProfissional to set
	 */
	public void setListaProfissional(List<ProfissionalBean> listaProfissional) {
		this.listaProfissional = listaProfissional;
	}

	/**
	 * @return the listaTipos
	 */
	public List<TipoAtendimentoBean> getListaTipos() {
		return listaTipos;
	}

	/**
	 * @param listaTipos
	 *            the listaTipos to set
	 */
	public void setListaTipos(List<TipoAtendimentoBean> listaTipos) {
		this.listaTipos = listaTipos;
	}

	public String getSituacao() {
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
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

	public boolean isHabilitarDetalhes() {
		return habilitarDetalhes;
	}

	public void setHabilitarDetalhes(boolean habilitarDetalhes) {
		this.habilitarDetalhes = habilitarDetalhes;
	}

}
