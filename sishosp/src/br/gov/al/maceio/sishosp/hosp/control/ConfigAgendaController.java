package br.gov.al.maceio.sishosp.hosp.control;

import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import br.gov.al.maceio.sishosp.hosp.model.ConfigAgendaParte1Bean;
import br.gov.al.maceio.sishosp.hosp.model.ConfigAgendaParte2Bean;

public class ConfigAgendaController {

	private ConfigAgendaParte1Bean confParte1;
	private ConfigAgendaParte2Bean confParte2;

	private List<ConfigAgendaParte2Bean> listaTipos;

	public ConfigAgendaController() {
		this.confParte1 = new ConfigAgendaParte1Bean();
		this.confParte2 = new ConfigAgendaParte2Bean();
		this.listaTipos = new ArrayList<>();
	}

	public void limparDados() {
		System.out.println("LIMPAR");
		this.confParte1 = new ConfigAgendaParte1Bean();
		this.confParte2 = new ConfigAgendaParte2Bean();
		this.listaTipos = new ArrayList<>();
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

}
