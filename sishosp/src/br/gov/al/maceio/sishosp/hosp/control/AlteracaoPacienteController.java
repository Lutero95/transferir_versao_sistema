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

import net.bootsfaces.component.row.RowBeanInfo;

import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;

import br.gov.al.maceio.sishosp.acl.dao.FuncionarioDAO;
import br.gov.al.maceio.sishosp.acl.model.FuncionarioBean;
import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.hosp.dao.AlteracaoPacienteDAO;
import br.gov.al.maceio.sishosp.hosp.dao.CboDAO;
import br.gov.al.maceio.sishosp.hosp.dao.EnderecoDAO;
import br.gov.al.maceio.sishosp.hosp.dao.GerenciarPacienteDAO;
import br.gov.al.maceio.sishosp.hosp.dao.GrupoDAO;
import br.gov.al.maceio.sishosp.hosp.dao.InsercaoPacienteDAO;
import br.gov.al.maceio.sishosp.hosp.dao.LaudoDAO;
import br.gov.al.maceio.sishosp.hosp.dao.ProgramaDAO;
import br.gov.al.maceio.sishosp.hosp.dao.RenovacaoPacienteDAO;
import br.gov.al.maceio.sishosp.hosp.model.CidBean;
import br.gov.al.maceio.sishosp.hosp.model.EquipamentoBean;
import br.gov.al.maceio.sishosp.hosp.model.EquipeBean;
import br.gov.al.maceio.sishosp.hosp.model.FornecedorBean;
import br.gov.al.maceio.sishosp.hosp.model.GerenciarPacienteBean;
import br.gov.al.maceio.sishosp.hosp.model.GrupoBean;
import br.gov.al.maceio.sishosp.hosp.model.InsercaoPacienteBean;
import br.gov.al.maceio.sishosp.hosp.model.LaudoBean;
import br.gov.al.maceio.sishosp.hosp.model.PacienteBean;
import br.gov.al.maceio.sishosp.hosp.model.ProcedimentoBean;
import br.gov.al.maceio.sishosp.hosp.model.ProgramaBean;
import br.gov.al.maceio.sishosp.hosp.model.TipoAtendimentoBean;

@ManagedBean(name = "AlteracaoPacienteController")
@ViewScoped
public class AlteracaoPacienteController implements Serializable {

	private static final long serialVersionUID = 1L;
	private AlteracaoPacienteDAO aDao = new AlteracaoPacienteDAO();
	private InsercaoPacienteBean insercao;
	private InsercaoPacienteBean insercaoParaLaudo;
	private String tipo;
	private InsercaoPacienteDAO iDao;
	private ArrayList<GerenciarPacienteBean> listaDiasProfissional;
	private ArrayList<InsercaoPacienteBean> listAgendamentoProfissional;
	private Integer id_paciente_insituicao;

	public AlteracaoPacienteController() {
		insercao = new InsercaoPacienteBean();
		insercaoParaLaudo = new InsercaoPacienteBean();
		tipo = "";
		iDao = new InsercaoPacienteDAO();
		listaDiasProfissional = new ArrayList<GerenciarPacienteBean>();
		listAgendamentoProfissional = new ArrayList<InsercaoPacienteBean>();
	}

	public void carregaAlteracao() throws ProjetoException {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		Map<String, String> params = facesContext.getExternalContext()
				.getRequestParameterMap();
		if (params.get("id") != null) {
			Integer id = Integer.parseInt(params.get("id"));
			id_paciente_insituicao = id;
			this.insercao = aDao.carregarPacientesInstituicaoAlteracao(id);
			carregarLaudoPaciente();
			if (insercao.getEquipe().getCodEquipe() != null
					&& insercao.getEquipe().getCodEquipe() > 0) {
				tipo = "E";
				listaDiasProfissional = aDao
						.listarDiasAtendimentoProfissionalEquipe(id);
			}
			if (insercao.getFuncionario().getId() != null
					&& insercao.getFuncionario().getId() > 0) {
				tipo = "P";
				insercao.getFuncionario().setListDiasSemana(
						aDao.listarDiasAtendimentoProfissional(id));
			}

		} else {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Ocorreu um erro!", "Erro");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}

	}

	public void gerarListaAgendamentosEquipe() throws ProjetoException {

		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		df.setLenient(false);
		Date d1 = insercao.getData_solicitacao();
		Date d2 = iDao.dataFinalLaudo(insercaoParaLaudo.getLaudo().getId());
		Long dt = (d2.getTime() - d1.getTime());

		dt = (dt / 86400000L);

		Calendar c = Calendar.getInstance();
		c.setTime(insercao.getData_solicitacao());

		for (int i = 0; i < dt; i++) {

			if (i > 0) {
				c.add(Calendar.DAY_OF_MONTH, 1);
			}

			int diaSemana = c.get(Calendar.DAY_OF_WEEK);

			if (tipo.equals("E")) {
				for (int j = 0; j < listaDiasProfissional.size(); j++) {

					if (diaSemana == listaDiasProfissional.get(j)
							.getFuncionario().getDiaSemana()) {

						InsercaoPacienteBean ins = new InsercaoPacienteBean();

						ins.getAgenda().setPaciente(
								insercao.getLaudo().getPaciente());

						ins.getAgenda().setDataMarcacao(c.getTime());

						ins.getAgenda().setProfissional(
								listaDiasProfissional.get(j).getFuncionario());

						listAgendamentoProfissional.add(ins);

					}
				}

			}

		}

	}

	public void gerarListaAgendamentosProfissional() throws ProjetoException {

		Boolean temAtendimento = false;
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		df.setLenient(false);
		Date d1 = insercao.getData_solicitacao();

		if (aDao.listaAtendimentos(id_paciente_insituicao).size() > 0) {
			for (int i = 0; i < aDao.listaAtendimentos(id_paciente_insituicao)
					.size(); i++) {
				d1 = aDao.listaAtendimentos(id_paciente_insituicao).get(i)
						.getDataAtendimento();
				insercao.setData_solicitacao(d1);
				temAtendimento = true;
			}
		}

		Date d2 = iDao.dataFinalLaudo(insercaoParaLaudo.getLaudo().getId());
		Long dt = (d2.getTime() - d1.getTime());

		dt = (dt / 86400000L);

		Calendar c = Calendar.getInstance();

		//INICIA O ATENDIMENTO 1 DIA APÓS O ÚLTIMO
		if (temAtendimento) {
			c.setTime(insercao.getData_solicitacao());
			c.add(Calendar.DAY_OF_MONTH, 1);
		} 
		else {
			c.setTime(insercao.getData_solicitacao());
		}

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

	public void gravarAlteracaoPaciente() throws ProjetoException {

		Boolean cadastrou = null;
		if (tipo.equals("E")) {

			gerarListaAgendamentosEquipe();

			cadastrou = aDao.gravarAlteracaoEquipe(insercao, insercaoParaLaudo,
					listAgendamentoProfissional);
		}
		if (tipo.equals("P")) {

			gerarListaAgendamentosProfissional();

			cadastrou = aDao.gravarAlteracaoProfissional(insercao,
					insercaoParaLaudo, listAgendamentoProfissional);
		}

		if (cadastrou) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Inserção de Equipe cadastrada com sucesso!", "Sucesso");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		} else {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Ocorreu um erro durante o cadastro!", "Erro");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
	}

	public ArrayList<InsercaoPacienteBean> listarLaudosVigentes()
			throws ProjetoException {
		return iDao.listarLaudosVigentes();
	}

	public void carregarLaudoPaciente() throws ProjetoException {
		insercaoParaLaudo = iDao.carregarLaudoPaciente(insercao.getLaudo()
				.getId());
		insercao = aDao
				.carregarPacientesInstituicaoAlteracao(id_paciente_insituicao);

	}

	public List<FuncionarioBean> listaProfissionalAutoComplete(String query)
			throws ProjetoException {
		FuncionarioDAO fDao = new FuncionarioDAO();
		List<FuncionarioBean> result = fDao.listarProfissionalBusca(query, 1);
		return result;
	}

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

	public ArrayList<GerenciarPacienteBean> getListaDiasProfissional() {
		return listaDiasProfissional;
	}

	public void setListaDiasProfissional(
			ArrayList<GerenciarPacienteBean> listaDiasProfissional) {
		this.listaDiasProfissional = listaDiasProfissional;
	}

	public InsercaoPacienteBean getInsercaoParaLaudo() {
		return insercaoParaLaudo;
	}

	public void setInsercaoParaLaudo(InsercaoPacienteBean insercaoParaLaudo) {
		this.insercaoParaLaudo = insercaoParaLaudo;
	}

}
