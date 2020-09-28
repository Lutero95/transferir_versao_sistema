package br.gov.al.maceio.sishosp.gestao.control;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import br.gov.al.maceio.sishosp.gestao.model.InconsistenciaBean;

@ManagedBean
@ViewScoped
public class InconsistenciaController {
	
	private List<InconsistenciaBean> listaInconsistencias;
	
	
	public InconsistenciaController() {
		this.listaInconsistencias = new ArrayList<>();
	}

	public void listarIncosistencias() {
		
	}
	
	
	public List<InconsistenciaBean> getListaInconsistencias() {
		return listaInconsistencias;
	}

	public void setListaInconsistencias(List<InconsistenciaBean> listaInconsistencias) {
		this.listaInconsistencias = listaInconsistencias;
	}
}
