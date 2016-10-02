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
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;

import com.lowagie.text.pdf.AcroFields.Item;

import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.hosp.dao.ConfigAgendaDAO;
import br.gov.al.maceio.sishosp.hosp.dao.EquipeDAO;
import br.gov.al.maceio.sishosp.hosp.dao.ProfissionalDAO;
import br.gov.al.maceio.sishosp.hosp.model.ConfigAgendaParte1Bean;
import br.gov.al.maceio.sishosp.hosp.model.ConfigAgendaParte2Bean;
import br.gov.al.maceio.sishosp.hosp.model.EquipeBean;
import br.gov.al.maceio.sishosp.hosp.model.ProfissionalBean;

@ManagedBean(name = "ConfigAgendaController")
@ViewScoped
public class ConfigAgendaController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private ConfigAgendaParte1Bean confParte1;
	private ConfigAgendaParte2Bean confParte2;

	private List<ConfigAgendaParte2Bean> listaTipos;
	private List<ConfigAgendaParte2Bean> listaTiposEditar;
	private List<ConfigAgendaParte1Bean> listaHorarios;
	private List<ConfigAgendaParte1Bean> listaHorariosEquipe;
	private List<ProfissionalBean> listaProfissionais;
	private List<EquipeBean> listaEquipes;
	
	private ConfigAgendaDAO cDao = new ConfigAgendaDAO();
	private ProfissionalDAO pDao = new ProfissionalDAO();
	private EquipeDAO eDao = new EquipeDAO();

	private String nomeBusca;
	private Integer tipoBusca;

	private String tipo;
	private int tipo2;

	private String opcao;

	public ConfigAgendaController() {
		this.confParte1 = new ConfigAgendaParte1Bean();
		this.confParte2 = new ConfigAgendaParte2Bean();
		this.listaTipos = new ArrayList<ConfigAgendaParte2Bean>();
		this.listaTiposEditar = new ArrayList<ConfigAgendaParte2Bean>();
		this.listaHorarios = null;
		this.listaHorariosEquipe = null;
		this.listaProfissionais = null;
		this.listaEquipes = null;
		this.tipo = new String();
		this.opcao = new String("2");
		this.nomeBusca = "";

	}

	public void limparDados() {
		this.confParte1 = new ConfigAgendaParte1Bean();
		this.confParte2 = new ConfigAgendaParte2Bean();
		this.listaTipos = new ArrayList<ConfigAgendaParte2Bean>();
		this.listaTiposEditar = new ArrayList<ConfigAgendaParte2Bean>();
		this.listaHorarios = null;
		this.listaHorariosEquipe = null;
		this.listaProfissionais = null;
		this.listaEquipes = null;
		this.nomeBusca = "";
		this.tipo = "";
		this.opcao = new String("1");
	}

	public ConfigAgendaParte1Bean getConfParte1() {
		return confParte1;
	}

	public void setConfParte1(ConfigAgendaParte1Bean confParte1) {
		this.confParte1 = confParte1;
	}

	public ConfigAgendaParte2Bean getConfParte2() {
		return confParte2;
	}

	public void setConfParte2(ConfigAgendaParte2Bean confParte2) {
		this.confParte2 = confParte2;
	}

	public List<ConfigAgendaParte2Bean> getListaTipos() {
		return listaTipos;
	}

	public void setListaTipos(List<ConfigAgendaParte2Bean> listaTipos) {
		this.listaTipos = listaTipos;
	}

	public List<ConfigAgendaParte1Bean> getListaHorarios() throws ProjetoException {
		if (listaHorarios == null) {
			return listaHorarios;
		} else {
			if (this.confParte1.getProfissional() == null) {
				System.out.println("AHSUDHASDUHASIUHDUAISUHDIUAS");
			}
			if (this.confParte1.getProfissional().getIdProfissional() != null) {
				this.listaHorarios = cDao
						.listarHorariosPorIDProfissional(this.confParte1
								.getProfissional().getIdProfissional());
			}
		}
		return listaHorarios;
	}

	public void setListaHorarios(List<ConfigAgendaParte1Bean> listaHorarios) {
		this.listaHorarios = listaHorarios;
	}

	public List<ConfigAgendaParte1Bean> getListaHorariosEquipe() throws ProjetoException {
		if (listaHorariosEquipe == null) {
			return listaHorariosEquipe;
		} else {
			if (this.confParte1.getEquipe().getCodEquipe() != null) {
				this.listaHorariosEquipe = cDao
						.listarHorariosPorIDEquipe(this.confParte1.getEquipe()
								.getCodEquipe());
			}
		}
		return listaHorariosEquipe;
	}
	//errado = action="/pages/agenda/editarConfAgenda.xhtml?faces-redirect=true">
	public String redirectEdit() {
		System.out.println("edit"+this.confParte1.getProfissional().getIdProfissional());
		return "editarConfAgenda?faces-redirect=true&amp;id=" + this.confParte1.getProfissional().getIdProfissional()+"&amp;tipo="+tipo;
	}	
	
	public void getEditAgenda() throws ProjetoException {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		Map<String,String> params = facesContext.getExternalContext().getRequestParameterMap();
		System.out.println("vai ve se entrar no editar");
		if(params.get("id") != null) {
			System.out.println("entrou no editar");
			Integer id = Integer.parseInt(params.get("id"));
			tipo2 =Integer.parseInt(params.get("tipo"));			
			System.out.println("tipo do walter"+tipo);
			this.confParte1 = (ConfigAgendaParte1Bean) cDao.listarHorariosPorIDProfissional(id);
		}
		else{
			System.out.println("tipo sera"+tipo);
			tipo2 =Integer.parseInt(params.get("tipo"));
			
		}
		
	}

	public void setListaHorariosEquipe(
			List<ConfigAgendaParte1Bean> listaHorariosEquipe) {
		this.listaHorariosEquipe = listaHorariosEquipe;
	}

	public List<ProfissionalBean> getListaProfissionais() throws ProjetoException {
		if (this.listaProfissionais == null) {
			this.listaProfissionais = pDao.listarProfissional();
		}
		return listaProfissionais;
	}

	public void setListaProfissionais(List<ProfissionalBean> listaProfissionais) {
		this.listaProfissionais = listaProfissionais;
	}

	public List<EquipeBean> getListaEquipes() throws ProjetoException {
		if (this.listaEquipes == null) {
			this.listaEquipes = eDao.listarEquipe();
		}
		return listaEquipes;
	}

	public void setListaEquipes(List<EquipeBean> listaEquipes) {
		this.listaEquipes = listaEquipes;
	}

	public String getNomeBusca() {
		return nomeBusca;
	}

	public void setNomeBusca(String nomeBusca) {
		this.nomeBusca = nomeBusca;
	}

	public Integer getTipoBusca() {
		return tipoBusca;
	}

	public void setTipoBusca(Integer tipoBusca) {
		this.tipoBusca = tipoBusca;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getOpcao() {
		return opcao;
	}

	public void setOpcao(String opcao) {
		this.opcao = opcao;
	}

	public List<ConfigAgendaParte2Bean> getListaTiposEditar()
			throws ProjetoException {
		if (this.confParte1.getIdConfiAgenda() != null) {
			this.listaTiposEditar = cDao.listarTiposAgendPorId(this.confParte1
					.getIdConfiAgenda());
		}
		return listaTiposEditar;
	}

	public void setListaTiposEditar(
			List<ConfigAgendaParte2Bean> listaTiposEditar) {
		this.listaTiposEditar = listaTiposEditar;
	}

	public void buscarProfissional() throws ProjetoException {
		if (this.tipoBusca == 0) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Escolha uma op��o de busca v�lida!", "Erro");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		} else {
			this.listaProfissionais = pDao.listarProfissionalBusca(nomeBusca,
					tipoBusca);
			this.listaHorarios = null;
		}
	}

	public void buscarEquipe() throws ProjetoException {
		if (this.tipoBusca == 0) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Escolha uma op��o de busca v�lida!", "Erro");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		} else {
			this.listaEquipes = eDao.listarEquipeBusca(nomeBusca, tipoBusca);
			this.listaHorariosEquipe = null;
		}
	}

	public void addNaLista() {
		//if (confParte2.getPrograma() == null){
			//FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
				//	"Insirar o Programa", "Erro");
			//FacesContext.getCurrentInstance().addMessage(null, msg);
		//}
		//else{
		//ConfigAgendaController.confParte1.qtdMax
		confParte1.setQtdMax(confParte2.getQtd());
		if (confParte2.getQtd() == null
				//|| confParte2.getTipoAt().getDescTipoAt() == null
				|| confParte2.getPrograma().getDescPrograma() == null
				|| confParte2.getGrupo().getDescGrupo() == null) {
			this.confParte2 = new ConfigAgendaParte2Bean();
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Insira os dados corretamente!", "Erro");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		} else {
			this.listaTipos.add(confParte2);
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Dados inseridos na tabela!", "Sucesso");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
		this.confParte2 = new ConfigAgendaParte2Bean();
		//}
	}

	public void gravarConfigAgenda() throws SQLException, ProjetoException {
		boolean ok = false;
		int somatorio = 0;
		System.out.println("ENTROU NO METODO");
		for (ConfigAgendaParte2Bean conf : listaTipos) {
			somatorio += conf.getQtd();
		}
		

		if (confParte1.getQtdMax() != null) {
			if (somatorio != confParte1.getQtdMax()) {
				FacesMessage msg = new FacesMessage(
						FacesMessage.SEVERITY_ERROR,
						"Quantidade m�xima est� divergente!", "Erro");
				FacesContext.getCurrentInstance().addMessage(null, msg);
				ok = false;
				return;
			}
		}else{
			FacesMessage msg = new FacesMessage(
					FacesMessage.SEVERITY_ERROR,
					"Quantidade m�xima obrigat�ria!", "Erro");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return;
		}
		
		if (listaTipos.size() == 0 ){
			FacesMessage msg = new FacesMessage(
					FacesMessage.SEVERITY_ERROR,
					"Adicione na lista!", "Erro");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return;
		}
		System.out.println("PROFISSIONAL: "+confParte1.getProfissional().getIdProfissional());
		if (confParte1.getProfissional().getIdProfissional() == null) {
			FacesMessage msg = new FacesMessage(
					FacesMessage.SEVERITY_ERROR,
					"Escolha um profissional!", "Erro");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return;
		}
		
		System.out.println("DATA ESPECIFICA: "+confParte1.getDataEspecifica());
		System.out.println("this.opcao"+this.opcao);
		System.out.println("confParte1.getDataEspecifica()"+confParte1.getDataEspecifica());
		if (this.opcao.equals("1") && this.confParte1.getDataEspecifica() == null) {
			FacesMessage msg = new FacesMessage(
					FacesMessage.SEVERITY_ERROR,
					"Escolha uma data espec�fica!", "Erro");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return;
		}
		System.out.println("LISTA DOS DIAS DA SEMANA: "+confParte1.getDiasSemana().size());
		//							<!-- update=":formConfiAgenda:msg, :formConfiAgenda"-->
		if(this.opcao.equals("2") && this.confParte1.getDiasSemana().size()==0){
				FacesMessage msg = new FacesMessage(
						FacesMessage.SEVERITY_ERROR,
						"Escolha no m�nimo um dia da semana!", "Erro");
				FacesContext.getCurrentInstance().addMessage(null, msg);
				return;
		}
		
		if(this.opcao.equals("2") && this.confParte1.getAno() == null){
			FacesMessage msg = new FacesMessage(
					FacesMessage.SEVERITY_ERROR,
					"Ano � ebrigat�rio!", "Erro");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return;
		}
		
		
		

		ok = cDao.gravarConfigAgenda(confParte1, confParte2, listaTipos);

		if (ok) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Configura��o gravada com sucesso!", "Sucesso");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		} else {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Insira os dados corretamente!", "Erro");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}

		limparDados();
	}

	public void gravarConfigAgendaEquipe() throws SQLException, ProjetoException {
		boolean ok = false;

		if (this.opcao.equals("1")) {
			this.confParte1.setAno(0);
			this.confParte1.setMes(0);
		}

		ok = cDao.gravarConfigAgendaEquipe(confParte1, confParte2, listaTipos);

		if (ok) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Configura��o gravada com sucesso!", "Sucesso");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		} else {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Insira os dados corretamente!", "Erro");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}

		limparDados();
	}

	public void alterarConfigAgenda() throws SQLException, ProjetoException {
		boolean ok = false;
		int somatorio = 0;

		for (ConfigAgendaParte2Bean conf : listaTiposEditar) {
			somatorio += conf.getQtd();
		}

		if (confParte1.getQtdMax() != null) {
			if (somatorio != confParte1.getQtdMax()) {
				FacesMessage msg = new FacesMessage(
						FacesMessage.SEVERITY_ERROR,
						"Quantidade m�xima est� divergente!", "Erro");
				FacesContext.getCurrentInstance().addMessage(null, msg);
				ok = false;
				return;
			}
		}

		ok = cDao.alterarConfigAgenda(confParte1, confParte2, listaTiposEditar);

		if (ok) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Configura��o alterada com sucesso!", "Sucesso");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		} else {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Insira os dados corretamente!", "Erro");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}

		limparDados();
	}

	public void alterarConfigAgendaEquipe() throws SQLException, ProjetoException {
		boolean ok = false;

		ok = cDao.alterarConfigAgendaEquipe(confParte1, confParte2,
				listaTiposEditar);

		if (ok) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Configura��o alterada com sucesso!", "Sucesso");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		} else {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Insira os dados corretamente!", "Erro");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}

		limparDados();
	}

	public void excluirConfig() throws ProjetoException {
		boolean ok = cDao.excluirConfig(confParte1);

		if (ok == true) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Configura��o excluida com sucesso!", "Sucesso");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			RequestContext.getCurrentInstance().execute(
					"PF('dialogAtencao').hide();");
		} else {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Ocorreu um erro durante a exclusao!", "Erro");
			FacesContext.getCurrentInstance().addMessage(null, msg);

			RequestContext.getCurrentInstance().execute(
					"PF('dialogAtencao').hide();");
		}
		this.listaHorarios = cDao.listarHorarios();
	}

	public void excluirConfigEquipe() throws ProjetoException {
		boolean ok = cDao.excluirConfigEquipe(confParte1);

		if (ok == true) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Configura��o excluida com sucesso!", "Sucesso");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			RequestContext.getCurrentInstance().execute(
					"PF('dialogAtencao').hide();");
		} else {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Ocorreu um erro durante a exclusao!", "Erro");
			FacesContext.getCurrentInstance().addMessage(null, msg);

			RequestContext.getCurrentInstance().execute(
					"PF('dialogAtencao').hide();");
		}
		this.listaHorariosEquipe = cDao.listarHorariosEquipe();
	}

	public void onRowSelect(SelectEvent event) throws ProjetoException {
		ProfissionalBean prof = (ProfissionalBean) event.getObject();
		this.listaHorarios = cDao.listarHorariosPorIDProfissional(prof
				.getIdProfissional());
	}

	public void onRowUnselect(UnselectEvent event) {
		this.listaHorarios = null;
	}

	public void onRowSelectEquipe(SelectEvent event) throws ProjetoException {
		EquipeBean equipe = (EquipeBean) event.getObject();
		this.listaHorariosEquipe = cDao.listarHorariosPorIDEquipe(equipe
				.getCodEquipe());
	}

	public void onRowUnselectEquipe(UnselectEvent event) {
		this.listaHorariosEquipe = null;
	}

	public void limparBuscaPrograma() {
		this.confParte2.setPrograma(null);
		this.confParte2.setGrupo(null);
		this.confParte2.setTipoAt(null);
	}

	public void limparBuscaGrupo() {
		this.confParte2.setGrupo(null);
		this.confParte2.setTipoAt(null);
	}

}
