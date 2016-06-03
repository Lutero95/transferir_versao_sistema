package br.gov.al.maceio.sishosp.hosp.control;

import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import br.gov.al.maceio.sishosp.hosp.dao.AgendaDAO;
import br.gov.al.maceio.sishosp.hosp.model.BloqueioBean;
import br.gov.al.maceio.sishosp.hosp.model.EquipeBean;
import br.gov.al.maceio.sishosp.hosp.model.FeriadoBean;
import br.gov.al.maceio.sishosp.hosp.model.GrupoBean;
import br.gov.al.maceio.sishosp.hosp.model.PacienteBean;
import br.gov.al.maceio.sishosp.hosp.model.ProcedimentoBean;
import br.gov.al.maceio.sishosp.hosp.model.ProfissionalBean;
import br.gov.al.maceio.sishosp.hosp.model.ProgramaBean;
import br.gov.al.maceio.sishosp.hosp.model.TipoAtendimentoBean;

public class AgendaController {

	private PacienteBean paciente;
	private ProcedimentoBean procedimento;
	private GrupoBean grupo;
	private ProgramaBean programa;
	private TipoAtendimentoBean tipoAt;
	private ProfissionalBean profissional;
	private EquipeBean equipe;
	private boolean prontoGravar;
	private Date dataAtendimento;
	private String turno;
	private String observacao;

	private AgendaDAO aDao = new AgendaDAO();

	public AgendaController() {
		this.paciente = null;// new PacienteBean();
		this.procedimento = null;
		this.grupo = null;// new GrupoBean();
		this.programa = null;// new ProgramaBean();
		this.tipoAt = null;// new TipoAtendimentoBean();
		this.profissional = null;
		this.equipe = null;
		this.prontoGravar = false;
		this.turno = new String();
		this.observacao = new String();
		this.dataAtendimento = null;
	}

	public void limparDados() {
		grupo = new GrupoBean();
		procedimento = new ProcedimentoBean();
		programa = new ProgramaBean();
		paciente = new PacienteBean();
		tipoAt = new TipoAtendimentoBean();
		this.profissional = new ProfissionalBean();
		this.equipe = new EquipeBean();
		this.observacao = new String();
		this.prontoGravar = false;
		this.dataAtendimento = null;
	}

	public ProfissionalBean getProfissional() {
		return profissional;
	}

	public void setProfissional(ProfissionalBean profissional) {
		this.profissional = profissional;
	}

	public EquipeBean getEquipe() {
		return equipe;
	}

	public void setEquipe(EquipeBean equipe) {
		this.equipe = equipe;
	}

	public GrupoBean getGrupo() {
		return grupo;
	}

	public void setGrupo(GrupoBean grupo) {
		this.grupo = grupo;
	}

	public ProgramaBean getPrograma() {
		return programa;
	}

	public void setPrograma(ProgramaBean programa) {
		this.programa = programa;
	}

	public PacienteBean getPaciente() {
		return paciente;
	}

	public void setPaciente(PacienteBean paciente) {
		this.paciente = paciente;
	}

	public boolean isProntoGravar() {
		return prontoGravar;
	}

	public void setProntoGravar(boolean prontoGravar) {
		this.prontoGravar = prontoGravar;
	}

	public TipoAtendimentoBean getTipoAt() {
		return tipoAt;
	}

	public void setTipoAt(TipoAtendimentoBean tipoAt) {
		this.tipoAt = tipoAt;
	}

	public ProcedimentoBean getProcedimento() {
		return procedimento;
	}

	public void setProcedimento(ProcedimentoBean procedimento) {
		this.procedimento = procedimento;
	}

	public String getTurno() {
		return turno;
	}

	public void setTurno(String turno) {
		this.turno = turno;
	}

	public Date getDataAtendimento() {
		return dataAtendimento;
	}

	public void setDataAtendimento(Date dataAtendimento) {
		this.dataAtendimento = dataAtendimento;
	}
	
	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public void verificaDisponibilidadeData() {
		FeriadoBean feriado = aDao.verificarFeriado(this.dataAtendimento);
		List<BloqueioBean> bloqueio = aDao.verificarBloqueioProfissional(
				this.profissional, this.dataAtendimento, this.turno);
		if (feriado == null) {
			System.out.println("NAO TEM FERIADO");
		} else {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Data bloqueada! " + feriado.getDescFeriado(), "Feriado");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
		
		if(bloqueio.isEmpty()){
			System.out.println("nao tem bloqueio");
		}else{
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Data bloqueada para este profissional!", "Bloqueio");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
	}
}
