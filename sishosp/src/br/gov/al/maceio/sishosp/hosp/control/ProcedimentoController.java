package br.gov.al.maceio.sishosp.hosp.control;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.hosp.dao.ProcedimentoDAO;
import br.gov.al.maceio.sishosp.hosp.model.ProcedimentoBean;

public class ProcedimentoController {
	
	private ProcedimentoBean proc;
	private List<ProcedimentoBean> listaProcedimentos;

	ProcedimentoDAO pDao = new ProcedimentoDAO();

	public ProcedimentoController() {
		this.proc = new ProcedimentoBean();
		this.listaProcedimentos = new ArrayList<ProcedimentoBean>();
	}

	public void limparDados() {
		proc = new ProcedimentoBean();
		listaProcedimentos = new ArrayList<ProcedimentoBean>();
	}

	public ProcedimentoBean getProc() {
		return proc;
	}

	public void setProc(ProcedimentoBean proc) {
		this.proc = proc;
	}
	
	public List<ProcedimentoBean> getListaProcedimentos() {
		this.listaProcedimentos = pDao.listarProcedimento();
		return listaProcedimentos;
	}

	public void setListaProcedimentos(List<ProcedimentoBean> listaProcedimentos) {
		this.listaProcedimentos = listaProcedimentos;
	}

	public void gravarProcedimento() throws ProjetoException, SQLException {

		boolean cadastrou = pDao.gravarProcedimento(proc);

		if (cadastrou == true) {
			limparDados();
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Procedimento cadastrado com sucesso!", "Sucesso");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		} else {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Ocorreu um erro durante o cadastro!", "Erro");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
	}
}
