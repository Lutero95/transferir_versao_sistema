package br.gov.al.maceio.sishosp.comum.control;

import java.io.IOException;
import java.util.Calendar;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

import br.gov.al.maceio.sishosp.acl.model.FuncionarioBean;
import br.gov.al.maceio.sishosp.comum.dao.ToleranciaDAO;
import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.model.HorarioFuncionamento;
import br.gov.al.maceio.sishosp.comum.util.JSFUtil;
import br.gov.al.maceio.sishosp.comum.util.SessionUtil;
import br.gov.al.maceio.sishosp.comum.util.VerificadorUtil;

@ManagedBean
@SessionScoped
public class TolerenciaController {

	private HorarioFuncionamento horarioFuncionamento;
	private ToleranciaDAO toleranciaDAO;
	private Integer minutosTolerancia;
	private boolean buscouHorarioIhTolerancia;
	private Long horarioAtualEmMilisegundos;
	private Long horarioFinalEmMilisegundos;
	private int anoAtual;
	private int mesAtual;
	private int diaDoMesAtual;
	private boolean visualizouDialogTolerancia;
	private final Integer INTERVALO_VERIFICACOES_EM_SEGUNDOS = 30;
	FuncionarioBean user_session;

	public TolerenciaController() {
		this.toleranciaDAO = new ToleranciaDAO();
		this.horarioFuncionamento = new HorarioFuncionamento();
		this.buscouHorarioIhTolerancia = false;
		this.user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
				.getSessionMap().get("obj_usuario");
	}

	public void visualizouDialog() {
		this.visualizouDialogTolerancia = true;
	}

	public void validarHorario() throws ProjetoException, IOException {

		if ((!user_session.getExcecaoBloqueioHorario()) && (user_session.getUnidade().getParametro().getUsaHorarioLimiteParaAcesso())) {
			if (!buscouHorarioIhTolerancia) {
				buscaHorarioFuncionamento();
				buscaMinutosTolerancia();
				buscouHorarioIhTolerancia = true;
			}
			verificaTolerancia();
		}

	}

	private  void verificaTolerancia() throws IOException {
		if(alcancouLimiteHorario()) {

			Calendar calendar = Calendar.getInstance();
			calendar.setTime(horarioFuncionamento.getHorarioFim());
			calendar.set(anoAtual, mesAtual, diaDoMesAtual);

			calendar.add(Calendar.MINUTE, minutosTolerancia);
			this.horarioFinalEmMilisegundos = calendar.getTimeInMillis();

			if(horarioExpirou()) {
				logout();
			}
			else if (!visualizouDialogTolerancia){
				JSFUtil.abrirDialog("dialogTolerancia");
			}
		}
	}

	private void buscaHorarioFuncionamento() throws ProjetoException {
		this.horarioFuncionamento = this.toleranciaDAO.buscaHorarioFuncionamento(user_session);
	}

	private void buscaMinutosTolerancia() throws ProjetoException {
		this.minutosTolerancia = this.toleranciaDAO.buscaMinutosTolerancia(user_session);
	}

	public boolean alcancouLimiteHorario() {

		Calendar calendar = Calendar.getInstance();
		this.horarioAtualEmMilisegundos = calendar.getTimeInMillis();

		this.anoAtual = calendar.get(Calendar.YEAR);
		this.mesAtual = calendar.get(Calendar.MONTH);
		this.diaDoMesAtual = calendar.get(Calendar.DAY_OF_MONTH);

		calendar.setTime(horarioFuncionamento.getHorarioFim());
		calendar.set(anoAtual, mesAtual, diaDoMesAtual);

		this.horarioFinalEmMilisegundos = calendar.getTimeInMillis();

		return horarioExpirou();
	}

	private boolean horarioExpirou() {
		if(horarioAtualEmMilisegundos > horarioFinalEmMilisegundos) {
			return true;
		}
		return false;
	}

	private FacesContext getFacesContext() {
		FacesContext context = FacesContext.getCurrentInstance();
		return context;
	}

	private ServletContext getServleContext() {
		ServletContext scontext = (ServletContext) this.getFacesContext().getExternalContext().getContext();
		return scontext;
	}

	private void logout() throws IOException {
		String path = getServleContext().getContextPath();
		SessionUtil.getSession().invalidate();
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("expired", "S");
		FacesContext.getCurrentInstance().getExternalContext().redirect(path+"/pages/comum/login.faces");

	}

	public HorarioFuncionamento getHorarioFuncionamento() {
		return horarioFuncionamento;
	}

	public void setHorarioFuncionamento(HorarioFuncionamento horarioFuncionamento) {
		this.horarioFuncionamento = horarioFuncionamento;
	}

	public Integer getMinutosTolerancia() {
		return minutosTolerancia;
	}

	public void setMinutosTolerancia(Integer minutosTolerancia) {
		this.minutosTolerancia = minutosTolerancia;
	}

	public Integer getINTERVALO_VERIFICACOES_EM_SEGUNDOS() {
		return INTERVALO_VERIFICACOES_EM_SEGUNDOS;
	}
}
