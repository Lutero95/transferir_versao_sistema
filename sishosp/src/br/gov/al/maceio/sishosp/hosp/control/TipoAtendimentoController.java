package br.gov.al.maceio.sishosp.hosp.control;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.hosp.dao.TipoAtendimentoDAO;
import br.gov.al.maceio.sishosp.hosp.model.GrupoBean;
import br.gov.al.maceio.sishosp.hosp.model.TipoAtendimentoBean;

public class TipoAtendimentoController {
	
	private TipoAtendimentoBean tipo;

	private List<TipoAtendimentoBean> listaTipos;
	
	TipoAtendimentoDAO tDao = new TipoAtendimentoDAO();

	public TipoAtendimentoController() {
		this.tipo = new TipoAtendimentoBean();
		this.listaTipos = new ArrayList<TipoAtendimentoBean>();
	}

	public void limparDados() {
		tipo = new TipoAtendimentoBean();
		listaTipos = new ArrayList<TipoAtendimentoBean>();
	}

	public TipoAtendimentoBean getTipo() {
		return tipo;
	}

	public void setTipo(TipoAtendimentoBean tipo) {
		this.tipo = tipo;
	}

	public List<TipoAtendimentoBean> getListaTipos() {
		return listaTipos;
	}
	
	public void atualizaListaTipos(GrupoBean g){
		System.out.println("VAI ATUALIZAR LISTA DE TIPO");
		this.listaTipos = tDao.listarTipoAtPorGrupo(g.getIdGrupo());
	}

	public void setListaTipos(List<TipoAtendimentoBean> listaTipos) {
		this.listaTipos = listaTipos;
	}
	
	public void gravarTipo() throws ProjetoException, SQLException {
		boolean cadastrou = tDao.gravarTipoAt(tipo);

		if (cadastrou == true) {
			limparDados();
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Tipo de Atendimento cadastrado com sucesso!", "Sucesso");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		} else {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Ocorreu um erro durante o cadastro!", "Erro");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
	}
	
	
}
