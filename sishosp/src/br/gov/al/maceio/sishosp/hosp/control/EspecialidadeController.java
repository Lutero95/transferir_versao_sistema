package br.gov.al.maceio.sishosp.hosp.control;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.hosp.dao.EspecialidadeDAO;
import br.gov.al.maceio.sishosp.hosp.model.EspecialidadeBean;

public class EspecialidadeController {

	private EspecialidadeBean espec;
	private List<EspecialidadeBean> listaEspecialidade;

	EspecialidadeDAO eDao = new EspecialidadeDAO();

	public EspecialidadeController() {
		this.espec = new EspecialidadeBean();
		this.listaEspecialidade = new ArrayList<EspecialidadeBean>();
	}

	public void limparDados() {
		espec = new EspecialidadeBean();
	}

	public EspecialidadeBean getEspec() {
		return espec;
	}

	public void setEspec(EspecialidadeBean espec) {
		this.espec = espec;
	}
	
	public List<EspecialidadeBean> getListaEspecialidade() {
		this.listaEspecialidade = eDao.listarEspecialidades();
		return listaEspecialidade;
	}

	public void gravarEspecialidade() throws ProjetoException, SQLException {

		boolean cadastrou = eDao.gravarEspecialidade(espec);

		if (cadastrou == true) {
			limparDados();
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Especialidade cadastrada com sucesso!", "Sucesso");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		} else {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Ocorreu um erro durante o cadastro!", "Erro");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
	}
	
	
	
}
