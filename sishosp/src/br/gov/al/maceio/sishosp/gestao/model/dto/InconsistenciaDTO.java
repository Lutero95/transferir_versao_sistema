package br.gov.al.maceio.sishosp.gestao.model.dto;

import java.util.ArrayList;
import java.util.List;

import br.gov.al.maceio.sishosp.gestao.model.InconsistenciaBean;
import br.gov.al.maceio.sishosp.hosp.model.LaudoBean;
import br.gov.al.maceio.sishosp.hosp.model.PacienteBean;
import br.gov.al.maceio.sishosp.hosp.model.ProcedimentoBean;

public class InconsistenciaDTO {

	private InconsistenciaBean inconsistencia;
	private List<LaudoBean> listaLaudos;
	private List<PacienteBean> listaPacientes;
	private List<ProcedimentoBean> listaProcedimentos;
	
	public InconsistenciaDTO() {
		this.inconsistencia = new InconsistenciaBean();
		this.listaLaudos = new ArrayList<>();
		this.listaPacientes = new ArrayList<>();
		this.listaProcedimentos = new ArrayList<>();	
	}
	
	public InconsistenciaDTO(InconsistenciaBean inconsistencia) {
		this.inconsistencia = inconsistencia;
		this.listaLaudos = new ArrayList<>();
		this.listaPacientes = new ArrayList<>();
		this.listaProcedimentos = new ArrayList<>();	
	}

	public InconsistenciaBean getInconsistencia() {
		return inconsistencia;
	}

	public void setInconsistencia(InconsistenciaBean inconsistencia) {
		this.inconsistencia = inconsistencia;
	}

	public List<LaudoBean> getListaLaudos() {
		return listaLaudos;
	}

	public void setListaLaudos(List<LaudoBean> listaLaudos) {
		this.listaLaudos = listaLaudos;
	}

	public List<PacienteBean> getListaPacientes() {
		return listaPacientes;
	}

	public void setListaPacientes(List<PacienteBean> listaPacientes) {
		this.listaPacientes = listaPacientes;
	}

	public List<ProcedimentoBean> getListaProcedimentos() {
		return listaProcedimentos;
	}

	public void setListaProcedimentos(List<ProcedimentoBean> listaProcedimentos) {
		this.listaProcedimentos = listaProcedimentos;
	}
}
