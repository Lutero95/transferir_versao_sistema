package br.gov.al.maceio.sishosp.hosp.control;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.primefaces.event.SelectEvent;

import br.gov.al.maceio.sishosp.hosp.dao.ConfigAgendaDAO;
import br.gov.al.maceio.sishosp.hosp.dao.ProfissionalDAO;
import br.gov.al.maceio.sishosp.hosp.model.ConfigAgendaParte1Bean;
import br.gov.al.maceio.sishosp.hosp.model.ConfigAgendaParte2Bean;
import br.gov.al.maceio.sishosp.hosp.model.ProfissionalBean;

public class ConfigAgendaController implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private ConfigAgendaParte1Bean confParte1;
	private ConfigAgendaParte2Bean confParte2;

	private List<ConfigAgendaParte2Bean> listaTipos;
	private List<ConfigAgendaParte1Bean> listaHorarios;
	private List<ProfissionalBean> listaProfissionais;

	private ConfigAgendaDAO cDao = new ConfigAgendaDAO();
	private ProfissionalDAO pDao = new ProfissionalDAO();
	
	private String nomeBusca;
	private Integer tipoBusca;

	public ConfigAgendaController() {
		this.confParte1 = new ConfigAgendaParte1Bean();
		this.confParte2 = new ConfigAgendaParte2Bean();
		this.listaTipos = new ArrayList<ConfigAgendaParte2Bean>();
		this.listaHorarios = new ArrayList<ConfigAgendaParte1Bean>();
		this.listaProfissionais = null;
		
	}

	public void limparDados() {
		System.out.println("LIMPAR");
		this.confParte1 = new ConfigAgendaParte1Bean();
		this.confParte2 = new ConfigAgendaParte2Bean();
		this.listaTipos = new ArrayList<ConfigAgendaParte2Bean>();
		this.listaHorarios = new ArrayList<ConfigAgendaParte1Bean>();
		this.listaProfissionais = null;
		this.nomeBusca= new String();
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
		if(listaHorarios == null){
			this.listaHorarios = cDao.listarHorarios();
		}
		return listaHorarios;
	}

	public void setListaHorarios(List<ConfigAgendaParte1Bean> listaHorarios) {
		this.listaHorarios = listaHorarios;
	}
	
	
	public List<ProfissionalBean> getListaProfissionais() {
		if(this.listaProfissionais == null){
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
	
	public void buscarProfissional() {
		if(this.tipoBusca==0){
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
	                "Escolha uma opção de busca válida!", "Erro");
	            FacesContext.getCurrentInstance().addMessage(null, msg);
		}else{
			this.listaProfissionais = pDao.listarProfissionalBusca(nomeBusca, tipoBusca);
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

		if(confParte1.getQtdMax()!=null){
			if (somatorio != confParte1.getQtdMax()) {
				FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Quantidade máxima está divergente!", "Erro");
				FacesContext.getCurrentInstance().addMessage(null, msg);
				ok = false;
				return;
			}
		}
		
		ok = cDao.gravarConfigAgenda(confParte1, confParte2);
		
		if (ok) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Configuração gravada com sucesso!", "Sucesso");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		} else {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Insira os dados corretamente!", "Erro");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}

		limparDados();
	}
	
	public void onRowSelect(SelectEvent event) {
		ProfissionalBean prof = (ProfissionalBean) event.getObject();
		this.listaHorarios = cDao.listarHorariosPorIDProfissional(prof.getIdProfissional());
    }
 
}
