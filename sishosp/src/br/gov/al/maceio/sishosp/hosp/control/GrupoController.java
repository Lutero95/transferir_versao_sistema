package br.gov.al.maceio.sishosp.hosp.control;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.hosp.dao.GrupoDAO;
import br.gov.al.maceio.sishosp.hosp.model.GrupoBean;

public class GrupoController {

	private GrupoBean grupo;
	private List<GrupoBean> listaGrupos;

	public GrupoController() {
		this.grupo = new GrupoBean();
		this.listaGrupos = new ArrayList<>();

	}

	public void limparDados() {
		grupo = new GrupoBean();

	}

	public void gravarGrupo() throws ProjetoException, SQLException {
		GrupoDAO gDao = new GrupoDAO();
		boolean cadastrou = gDao.gravarGrupo(grupo);

		if (cadastrou == true) {
			limparDados();
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Grupo cadastrado com sucesso!", "Sucesso");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		} else {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ocorreu um erro durante o cadastro!",
					"Erro");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
	}

	public GrupoBean getGrupo() {
		return grupo;
	}

	public void setGrupo(GrupoBean grupo) {
		this.grupo = grupo;
	}

	public List<GrupoBean> getListaGrupos() {
		GrupoDAO gDao = new GrupoDAO();
		listaGrupos = gDao.listarGrupos();
		return listaGrupos;
	}

	public void setListaGrupos(List<GrupoBean> listaGrupos) {
		this.listaGrupos = listaGrupos;
	}

}
