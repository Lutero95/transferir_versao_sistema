package br.gov.al.maceio.sishosp.hosp.control;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;

import br.gov.al.maceio.sishosp.acl.model.FuncionarioBean;
import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.hosp.dao.BloqueioDAO;
import br.gov.al.maceio.sishosp.hosp.dao.EquipeDAO;
import br.gov.al.maceio.sishosp.hosp.dao.GrupoDAO;
import br.gov.al.maceio.sishosp.hosp.dao.InsercaoPacienteDAO;
import br.gov.al.maceio.sishosp.hosp.model.BloqueioBean;
import br.gov.al.maceio.sishosp.hosp.model.EquipeBean;
import br.gov.al.maceio.sishosp.hosp.model.GrupoBean;
import br.gov.al.maceio.sishosp.hosp.model.InsercaoPacienteBean;

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

	public InsercaoPacienteController() {
		this.insercao = new InsercaoPacienteBean();
		listaLaudosVigentes = new ArrayList<InsercaoPacienteBean>();
		this.tipo = "E";
		funcionario = new FuncionarioBean();
		listaProfissionaisAdicionados = new ArrayList<FuncionarioBean>();
	}

	public void limparDados() {
		this.insercao = new InsercaoPacienteBean();
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

	public void adicionarFuncionario() {
		String dias = "";
		for(int i=0; i<insercao.getDiasSemana().size(); i++){
			System.out.println("Dia: "+insercao.getDiasSemana().get(i));
			if (insercao.getDiasSemana().get(i).equals("1")){
				dias = dias+"Domingo";
			}
			if (insercao.getDiasSemana().get(i).equals("1")){
				dias = dias+", Segunda";
			}
			if (insercao.getDiasSemana().get(i).equals("1")){
				dias = dias+", Terça";
			}
			if (insercao.getDiasSemana().get(i).equals("1")){
				dias = dias+", Quarta";
			}
			if (insercao.getDiasSemana().get(i).equals("1")){
				dias = dias+", Quinta";
			}
			if (insercao.getDiasSemana().get(i).equals("1")){
				dias = dias+", Sexta";
			}
			if (insercao.getDiasSemana().get(i).equals("1")){
				dias = dias+", Sábado";
			}
		}
		funcionario.setDiasSemana(dias);
		listaProfissionaisAdicionados.add(funcionario);
		RequestContext.getCurrentInstance()
		.execute("PF('dlgDiasAtendimento').hide();");
	}

	// AUTOCOMPLETE INÍCIO

	public List<EquipeBean> listaEquipeAutoComplete(String query)
			throws ProjetoException {

		List<EquipeBean> result = eDao.listarEquipePorGrupoAutoComplete(query,
				insercao.getGrupo().getIdGrupo());
		return result;
	}

	public void listarProfissionaisEquipe() throws ProjetoException {

		if (insercao.getEquipe() != null) {
			if (insercao.getEquipe().getCodEquipe() != null) {
				listaProfissionaisEquipe = eDao
						.listarProfissionaisDaEquipe(insercao.getEquipe()
								.getCodEquipe());
			}
		} else {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Escolha uma equipe!", "Bloqueio");
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
