package br.gov.al.maceio.sishosp.hosp.control;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;

import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.hosp.dao.ConfigAgendaDAO;
import br.gov.al.maceio.sishosp.hosp.dao.ProfissionalDAO;
import br.gov.al.maceio.sishosp.hosp.model.ConfigAgendaParte1Bean;
import br.gov.al.maceio.sishosp.hosp.model.ConfigAgendaParte2Bean;
import br.gov.al.maceio.sishosp.hosp.model.ProfissionalBean;

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
	private List<ProfissionalBean> listaProfissionais;

	private ConfigAgendaDAO cDao = new ConfigAgendaDAO();
	private ProfissionalDAO pDao = new ProfissionalDAO();

	private String nomeBusca;
	private Integer tipoBusca;

	private String tipo;
	
	private String opcao;

	public ConfigAgendaController() {
		this.confParte1 = new ConfigAgendaParte1Bean();
		this.confParte2 = new ConfigAgendaParte2Bean();
		this.listaTipos = new ArrayList<ConfigAgendaParte2Bean>();
		this.listaTiposEditar = new ArrayList<ConfigAgendaParte2Bean>();
		this.listaHorarios = new ArrayList<ConfigAgendaParte1Bean>();
		this.listaProfissionais = null;
		this.tipo = new String();
		this.opcao = new String();

	}

	public void limparDados() {
		System.out.println("LIMPAR");
		this.confParte1 = new ConfigAgendaParte1Bean();
		this.confParte2 = new ConfigAgendaParte2Bean();
		this.listaTipos = new ArrayList<ConfigAgendaParte2Bean>();
		this.listaTiposEditar = new ArrayList<ConfigAgendaParte2Bean>();
		this.listaHorarios = new ArrayList<ConfigAgendaParte1Bean>();
		this.listaProfissionais = null;
		this.nomeBusca = new String();
		this.tipo = new String();
		this.opcao = new String();
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

	public List<ConfigAgendaParte1Bean> getListaHorarios() {
		if(listaHorarios==null){
			return listaHorarios;
		}else{
			if (this.confParte1.getProfissional().getIdProfissional() != null) {
				this.listaHorarios = cDao.listarHorariosPorIDProfissional(this.confParte1
								.getProfissional().getIdProfissional());
			}
		}
		return listaHorarios;
	}

	public void setListaHorarios(List<ConfigAgendaParte1Bean> listaHorarios) {
		this.listaHorarios = listaHorarios;
	}

	public List<ProfissionalBean> getListaProfissionais() {
		if (this.listaProfissionais == null) {
			this.listaProfissionais = pDao.listarProfissional();
		}
		return listaProfissionais;
	}

	public void setListaProfissionais(List<ProfissionalBean> listaProfissionais) {
		this.listaProfissionais = listaProfissionais;
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

	public List<ConfigAgendaParte2Bean> getListaTiposEditar() {
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

	public void buscarProfissional() {
		if (this.tipoBusca == 0) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Escolha uma op��o de busca v�lida!", "Erro");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		} else {
			this.listaProfissionais = pDao.listarProfissionalBusca(nomeBusca,
					tipoBusca);
		}
	}

	public void addNaLista() {
		if (confParte2.getQtd() == null
				|| confParte2.getTipoAt().getDescTipoAt() == null
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

	}

	public void gravarConfigAgenda() throws SQLException {
		boolean ok = false;
		int somatorio = 0;

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
	
	public void alterarConfigAgenda() throws SQLException {
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

	public void onRowSelect(SelectEvent event) {
		ProfissionalBean prof = (ProfissionalBean) event.getObject();
		this.listaHorarios = cDao.listarHorariosPorIDProfissional(prof
				.getIdProfissional());
	}

	public void onRowUnselect(UnselectEvent event) {
		this.listaHorarios = null;
	}

}
