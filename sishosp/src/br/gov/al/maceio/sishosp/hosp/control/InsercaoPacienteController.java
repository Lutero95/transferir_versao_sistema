package br.gov.al.maceio.sishosp.hosp.control;

import java.io.Serializable;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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

import br.gov.al.maceio.sishosp.acl.dao.FuncionalidadeDAO;
import br.gov.al.maceio.sishosp.acl.dao.FuncionarioDAO;
import br.gov.al.maceio.sishosp.acl.model.FuncionarioBean;
import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.hosp.dao.BloqueioDAO;
import br.gov.al.maceio.sishosp.hosp.dao.EquipeDAO;
import br.gov.al.maceio.sishosp.hosp.dao.GrupoDAO;
import br.gov.al.maceio.sishosp.hosp.dao.InsercaoPacienteDAO;
import br.gov.al.maceio.sishosp.hosp.dao.TipoAtendimentoDAO;
import br.gov.al.maceio.sishosp.hosp.model.AgendaBean;
import br.gov.al.maceio.sishosp.hosp.model.BloqueioBean;
import br.gov.al.maceio.sishosp.hosp.model.EquipeBean;
import br.gov.al.maceio.sishosp.hosp.model.GrupoBean;
import br.gov.al.maceio.sishosp.hosp.model.InsercaoPacienteBean;
import br.gov.al.maceio.sishosp.hosp.model.TipoAtendimentoBean;

@ManagedBean(name = "InsercaoController")
@ViewScoped
public class InsercaoPacienteController implements Serializable {

	private static final long serialVersionUID = 1L;
	private InsercaoPacienteBean insercao;
	private InsercaoPacienteDAO iDao = new InsercaoPacienteDAO();
	private EquipeDAO eDao = new EquipeDAO();
	private ArrayList<InsercaoPacienteBean> listaLaudosVigentes;
	private ArrayList<FuncionarioBean> listaProfissionaisEquipe;
	private ArrayList<FuncionarioBean> listaProfissionaisAdicionados;
	private String tipo;
	private FuncionarioBean funcionario;
	private ArrayList<InsercaoPacienteBean> listAgendamentoProfissional;

	public InsercaoPacienteController() {
		this.insercao = new InsercaoPacienteBean();
		listaLaudosVigentes = new ArrayList<InsercaoPacienteBean>();
		this.tipo = "E";
		funcionario = new FuncionarioBean();
		listaProfissionaisAdicionados = new ArrayList<FuncionarioBean>();
		listAgendamentoProfissional = new ArrayList<InsercaoPacienteBean>();
	}

	public void limparDados() {
		this.insercao = new InsercaoPacienteBean();
	}

	public void limparDias() {
		funcionario.setListDiasSemana(null);
	}

	public ArrayList<InsercaoPacienteBean> listarLaudosVigentes()
			throws ProjetoException {
		return listaLaudosVigentes = iDao.listarLaudosVigentes();
	}

	public void carregarLaudoPaciente() throws ProjetoException {
		int id = insercao.getLaudo().getId();
		limparDados();
		insercao = iDao.carregarLaudoPaciente(id);

	}

	// VALIDAÇÃO DE NÃO REPETIR O PROFISSIONAL
	public void validarAdicionarFuncionario() {
		Boolean existe = false;
		if (listaProfissionaisAdicionados.size() == 0) {
			adicionarFuncionario();
		} else {

			for (int i = 0; i < listaProfissionaisAdicionados.size(); i++) {
				if (listaProfissionaisAdicionados.get(i).getId() == funcionario
						.getId()) {
					existe = true;
				}
			}
			if (existe == false) {
				adicionarFuncionario();
			} else {

				RequestContext.getCurrentInstance().execute(
						"PF('dlgDiasAtendimento').hide();");

				FacesMessage msg = new FacesMessage(
						FacesMessage.SEVERITY_ERROR,
						"Esse profissional já foi adicionado!", "Sucesso");
				FacesContext.getCurrentInstance().addMessage(null, msg);
			}

		}
	}

	public void adicionarFuncionario() {
		String dias = "";

		for (int i = 0; i < funcionario.getListDiasSemana().size(); i++) {
			if (funcionario.getListDiasSemana().get(i).equals("1")) {
				dias = dias + "Domingo";
			}
			if (funcionario.getListDiasSemana().get(i).equals("2")) {
				dias = dias + ", Segunda";
			}
			if (funcionario.getListDiasSemana().get(i).equals("3")) {
				dias = dias + ", Terça";
			}
			if (funcionario.getListDiasSemana().get(i).equals("4")) {
				dias = dias + ", Quarta";
			}
			if (funcionario.getListDiasSemana().get(i).equals("5")) {
				dias = dias + ", Quinta";
			}
			if (funcionario.getListDiasSemana().get(i).equals("6")) {
				dias = dias + ", Sexta";
			}
			if (funcionario.getListDiasSemana().get(i).equals("7")) {
				dias = dias + ", Sábado";
			}
		}
		funcionario.setDiasSemana(dias);
		listaProfissionaisAdicionados.add(funcionario);

		RequestContext.getCurrentInstance().execute(
				"PF('dlgDiasAtendimento').hide();");
	}

	public void gerarListaAgendamentos() throws ProjetoException {

		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		df.setLenient(false);
		Date d1 = insercao.getData_solicitacao();
		Date d2 = iDao.dataFinalLaudo(insercao.getLaudo().getId());
		Long dt = (d2.getTime() - d1.getTime());

		dt = (dt / 86400000L);

		Calendar c = Calendar.getInstance();
		c.setTime(insercao.getData_solicitacao());

		for (int i = 0; i < dt; i++) {

			if (i > 0) {
				c.add(Calendar.DAY_OF_MONTH, 1);
			}

			int diaSemana = c.get(Calendar.DAY_OF_WEEK);

			if (tipo.equals("P")) {
				for (int j = 0; j < insercao.getFuncionario()
						.getListDiasSemana().size(); j++) {

					if (diaSemana == Integer.parseInt(insercao.getFuncionario()
							.getListDiasSemana().get(j))) {

						InsercaoPacienteBean ins = new InsercaoPacienteBean();

						ins.getAgenda().setPaciente(
								insercao.getLaudo().getPaciente());

						ins.getAgenda().setDataMarcacao(c.getTime());

						listAgendamentoProfissional.add(ins);

					}
				}

			}

		}

	}

	public void gravarInsercaoPaciente() throws ProjetoException, SQLException {

		if (insercao.getLaudo().getId() != null) {

			Boolean cadastrou = null;
			if (tipo.equals("E")) {
				cadastrou = iDao.gravarInsercaoEquipe(insercao,
						listaProfissionaisAdicionados);
			}
			if (tipo.equals("P")) {

				gerarListaAgendamentos();

				cadastrou = iDao.gravarInsercaoProfissional(insercao,
						listaProfissionaisAdicionados,
						listAgendamentoProfissional);
			}

			if (cadastrou == true) {
				limparDados();
				FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
						"Inserção de Equipe cadastrada com sucesso!", "Sucesso");
				FacesContext.getCurrentInstance().addMessage(null, msg);
			} else {
				FacesMessage msg = new FacesMessage(
						FacesMessage.SEVERITY_ERROR,
						"Ocorreu um erro durante o cadastro!", "Erro");
				FacesContext.getCurrentInstance().addMessage(null, msg);
			}
		} else {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Carregue um laudo primeiro!", "Bloqueio");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
	}

	// AUTOCOMPLETE INÍCIO

	public List<EquipeBean> listaEquipeAutoComplete(String query)
			throws ProjetoException {

		List<EquipeBean> result = eDao.listarEquipePorGrupoAutoComplete(query,
				insercao.getGrupo().getIdGrupo());
		return result;
	}

	public void listarProfissionaisEquipe() throws ProjetoException {
		if (insercao.getLaudo().getId() != null) {
			if (insercao.getEquipe() != null) {
				if (insercao.getEquipe().getCodEquipe() != null) {
					listaProfissionaisEquipe = eDao
							.listarProfissionaisDaEquipe(insercao.getEquipe()
									.getCodEquipe());
				}
			} else {
				FacesMessage msg = new FacesMessage(
						FacesMessage.SEVERITY_ERROR, "Escolha uma equipe!",
						"Bloqueio");
				FacesContext.getCurrentInstance().addMessage(null, msg);
			}
		} else {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Carregue um laudo primeiro!", "Bloqueio");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
	}

	public List<GrupoBean> listaGrupoAutoComplete(String query)
			throws ProjetoException {

		GrupoDAO gDao = new GrupoDAO();

		if (insercao.getPrograma().getIdPrograma() != null) {
			return gDao.listarGruposAutoComplete(query,
					this.insercao.getPrograma());
		} else {
			return null;
		}

	}

	public List<FuncionarioBean> listaProfissionalAutoComplete(String query)
			throws ProjetoException {
		FuncionarioDAO fDao = new FuncionarioDAO();
		List<FuncionarioBean> result = fDao.listarProfissionalBusca(query, 1);
		return result;
	}

	public List<TipoAtendimentoBean> listaTipoAtAutoComplete(String query)
			throws ProjetoException {
		TipoAtendimentoDAO tDao = new TipoAtendimentoDAO();
		return tDao.listarTipoAtBusca(query, 1);

	}

	// AUTOCOMPLETE FIM

	public InsercaoPacienteBean getInsercao() {
		return insercao;
	}

	public void setInsercao(InsercaoPacienteBean insercao) {
		this.insercao = insercao;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public ArrayList<InsercaoPacienteBean> getListaLaudosVigentes() {
		return listaLaudosVigentes;
	}

	public void setListaLaudosVigentes(
			ArrayList<InsercaoPacienteBean> listaLaudosVigentes) {
		this.listaLaudosVigentes = listaLaudosVigentes;
	}

	public ArrayList<FuncionarioBean> getListaProfissionaisEquipe() {
		return listaProfissionaisEquipe;
	}

	public void setListaProfissionaisEquipe(
			ArrayList<FuncionarioBean> listaProfissionaisEquipe) {
		this.listaProfissionaisEquipe = listaProfissionaisEquipe;
	}

	public FuncionarioBean getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(FuncionarioBean funcionario) {
		this.funcionario = funcionario;
	}

	public ArrayList<FuncionarioBean> getListaProfissionaisAdicionados() {
		return listaProfissionaisAdicionados;
	}

	public void setListaProfissionaisAdicionados(
			ArrayList<FuncionarioBean> listaProfissionaisAdicionados) {
		this.listaProfissionaisAdicionados = listaProfissionaisAdicionados;
	}

}
