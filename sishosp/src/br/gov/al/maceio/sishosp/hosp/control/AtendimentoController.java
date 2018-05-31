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
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.SelectEvent;

import br.gov.al.maceio.sishosp.acl.dao.FuncionarioDAO;
import br.gov.al.maceio.sishosp.acl.model.FuncionarioBean;
import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.hosp.dao.AgendaDAO;
import br.gov.al.maceio.sishosp.hosp.dao.AtendimentoDAO;
import br.gov.al.maceio.sishosp.hosp.dao.CboDAO;
import br.gov.al.maceio.sishosp.hosp.dao.EnderecoDAO;
import br.gov.al.maceio.sishosp.hosp.dao.EquipeDAO;
import br.gov.al.maceio.sishosp.hosp.dao.GrupoDAO;
import br.gov.al.maceio.sishosp.hosp.dao.PacienteDAO;
import br.gov.al.maceio.sishosp.hosp.dao.ProcedimentoDAO;
import br.gov.al.maceio.sishosp.hosp.dao.ProgramaDAO;
import br.gov.al.maceio.sishosp.hosp.dao.TipoAtendimentoDAO;
import br.gov.al.maceio.sishosp.hosp.model.AgendaBean;
import br.gov.al.maceio.sishosp.hosp.model.AtendimentoBean;
import br.gov.al.maceio.sishosp.hosp.model.BloqueioBean;
import br.gov.al.maceio.sishosp.hosp.model.CboBean;
import br.gov.al.maceio.sishosp.hosp.model.EquipeBean;
import br.gov.al.maceio.sishosp.hosp.model.FeriadoBean;
import br.gov.al.maceio.sishosp.hosp.model.GrupoBean;
import br.gov.al.maceio.sishosp.hosp.model.ProcedimentoBean;
import br.gov.al.maceio.sishosp.hosp.model.ProgramaBean;
import br.gov.al.maceio.sishosp.hosp.model.TipoAtendimentoBean;

@ManagedBean(name = "AtendimentoController")
@ViewScoped
public class AtendimentoController implements Serializable {

	private static final long serialVersionUID = 1L;

	private AtendimentoBean atendimento;
	private List<AtendimentoBean> listAtendimentos;
	private List<AtendimentoBean> listAtendimentosEquipe;
	AtendimentoDAO aDao = new AtendimentoDAO();
	ProcedimentoDAO pDao = new ProcedimentoDAO();
	private FuncionarioBean funcionario;
	private FuncionarioDAO fDao = new FuncionarioDAO();
	private ProcedimentoBean procedimento;
	private List<ProcedimentoBean> listaProcedimentos;
	private AtendimentoBean atendimentoLista;

	public AtendimentoController() {
		this.atendimento = new AtendimentoBean();
		this.atendimentoLista = null;
		listAtendimentos = new ArrayList<AtendimentoBean>();
		listAtendimentosEquipe = new ArrayList<AtendimentoBean>();
		funcionario = null;
		procedimento = new ProcedimentoBean();
		listaProcedimentos = new ArrayList<ProcedimentoBean>();
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

	public String redirectAtendimento() throws ProjetoException {
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

	public void getCarregaAtendimentoEquipe() throws ProjetoException,
			SQLException {

		FacesContext facesContext = FacesContext.getCurrentInstance();
		Map<String, String> params = facesContext.getExternalContext()
				.getRequestParameterMap();
		if (params.get("id") != null) {
			Integer id = Integer.parseInt(params.get("id"));
			aDao = new AtendimentoDAO();
			this.atendimento = aDao.listarAtendimentoProfissionalPorId(id);
		}
	}

	public void realizarAtendimentoProfissional() throws ProjetoException,
			SQLException {
		if (funcionario == null) {
			FuncionarioBean user_session = (FuncionarioBean) FacesContext
					.getCurrentInstance().getExternalContext().getSessionMap()
					.get("obj_usuario");
			Integer valor = Integer.valueOf(user_session.getId().toString());
			this.funcionario = fDao.buscarProfissionalPorId(valor);
		}

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

	public void chamarMetodoTabelaAtendimentoEquipe() throws ProjetoException {
		listarAtendimentosEquipe();
	}

	public List<AtendimentoBean> listarAtendimentosEquipe()
			throws ProjetoException {

		if (atendimento.getStatus() != null) {
			if (!atendimento.getStatus().equals("")) {
				System.out.println("STATUS: " + atendimento.getStatus());
				for (int i = 0; i < listAtendimentosEquipe.size(); i++) {

					if (listAtendimentosEquipe.get(i).getId1() == atendimentoLista
							.getId1()) {
						listAtendimentosEquipe.get(i).setStatus(
								atendimento.getStatus());
						;
					}
				}
			}
		}

		if (procedimento.getIdProc() != null) {
			if (procedimento.getIdProc() > 0) {

				for (int i = 0; i < listAtendimentosEquipe.size(); i++) {

					if (listAtendimentosEquipe.get(i).getId1() == atendimentoLista
							.getId1()) {
						listAtendimentosEquipe.get(i).setProcedimento(
								procedimento);
					}
				}
			}
		}

		if (funcionario != null) {

			CboDAO cDao = new CboDAO();
			CboBean cbo = cDao.listarCboPorId(funcionario.getCbo().getCodCbo());

			for (int i = 0; i < listAtendimentosEquipe.size(); i++) {

				if (listAtendimentosEquipe.get(i).getId1() == atendimentoLista
						.getId1()) {
					listAtendimentosEquipe.get(i).setFuncionario(funcionario);
					listAtendimentosEquipe.get(i).setCbo(cbo);
				}
			}

		} else {
			this.listAtendimentosEquipe = aDao
					.carregaAtendimentosEquipe(atendimento.getId());
		}
		return this.listAtendimentosEquipe;

	}

	public List<AtendimentoBean> listarAtendimentosEquipe2()
			throws ProjetoException {

		listAtendimentosEquipe = aDao
				.carregaAtendimentosEquipeProfissional(atendimento.getId1());

		for (int i = 0; i < listAtendimentosEquipe.size(); i++) {
			if (listAtendimentos.get(i).getId1() == atendimento.getId1()) {
				listarAtendimentosEquipe().get(i).getFuncionario()
						.setId(funcionario.getId());
				listarAtendimentosEquipe().get(i).getFuncionario()
						.setNome(funcionario.getNome());
				listarAtendimentosEquipe().get(i).getFuncionario()
						.setCns(funcionario.getCns());
			}
		}

		return this.listAtendimentosEquipe = aDao
				.carregaAtendimentosEquipeProfissional(atendimento.getId1());

	}

	public void onCellEdit(CellEditEvent event) {

		Object oldValue = event.getOldValue();
		Object newValue = event.getNewValue();

		if (newValue != null && !newValue.equals(oldValue)) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Clique em SALVAR para que a alteração seja gravada!",
					"Old: " + oldValue + ", New:" + newValue);
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
	}

	public List<AtendimentoBean> alterarTabelaEquipe() throws ProjetoException {
		return this.listAtendimentosEquipe = aDao
				.carregaAtendimentosEquipeProfissional(atendimento.getId1());

	}

	public List<ProcedimentoBean> listarProcedimentos() throws ProjetoException {
		return this.listaProcedimentos = pDao.listarProcedimento();

	}

	public void realizarAtendimentoEquipe() throws ProjetoException,
			SQLException {
		boolean alterou = aDao.realizaAtendimentoEquipe(listAtendimentosEquipe);

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

	public List<AtendimentoBean> getListAtendimentosEquipe() {
		return listAtendimentosEquipe;
	}

	public void setListAtendimentosEquipe(
			List<AtendimentoBean> listAtendimentosEquipe) {
		this.listAtendimentosEquipe = listAtendimentosEquipe;
	}

	public ProcedimentoBean getProcedimento() {
		return procedimento;
	}

	public void setProcedimento(ProcedimentoBean procedimento) {
		this.procedimento = procedimento;
	}

	public AtendimentoBean getAtendimentoLista() {
		return atendimentoLista;
	}

	public void setAtendimentoLista(AtendimentoBean atendimentoLista) {
		this.atendimentoLista = atendimentoLista;
	}

}
