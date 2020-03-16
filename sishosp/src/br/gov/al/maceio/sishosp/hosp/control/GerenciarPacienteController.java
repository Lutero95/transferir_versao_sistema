package br.gov.al.maceio.sishosp.hosp.control;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.shared.TelasBuscaSessao;
import br.gov.al.maceio.sishosp.comum.util.*;
import br.gov.al.maceio.sishosp.hosp.dao.GerenciarPacienteDAO;
import br.gov.al.maceio.sishosp.hosp.dao.GrupoDAO;
import br.gov.al.maceio.sishosp.hosp.dao.InsercaoPacienteDAO;
import br.gov.al.maceio.sishosp.hosp.dao.LaudoDAO;
import br.gov.al.maceio.sishosp.hosp.model.GerenciarPacienteBean;
import br.gov.al.maceio.sishosp.hosp.model.GrupoBean;
import br.gov.al.maceio.sishosp.hosp.model.InsercaoPacienteBean;
import br.gov.al.maceio.sishosp.hosp.model.LaudoBean;
import br.gov.al.maceio.sishosp.hosp.model.dto.BuscaSessaoDTO;

import static br.gov.al.maceio.sishosp.comum.shared.DadosSessao.BUSCA_SESSAO;

@ManagedBean(name = "GerenciarPacienteController")
@ViewScoped
public class GerenciarPacienteController implements Serializable {

	private static final long serialVersionUID = 1L;
	private GerenciarPacienteBean gerenciarpaciente;
	private GerenciarPacienteBean rowBean;
	private List<GerenciarPacienteBean> listaPacientes;
	private List<GrupoBean> listaGrupos;
	private GerenciarPacienteDAO gDao = new GerenciarPacienteDAO();
	private String busca = "N";
	private Boolean apenasLeitura;
	private InsercaoPacienteBean insercao;
	private String tipo;
	private ArrayList<InsercaoPacienteBean> listaLaudosVigentes;
	private InsercaoPacienteDAO iDao;
	private ArrayList<GerenciarPacienteBean> listaDiasProfissional;
	private String tipoBusca;
	private String campoBusca;

	// CONSTANTES
	private static final String ENDERECO_RENOVACAO = "renovacaoPaciente?faces-redirect=true";
	private static final String ENDERECO_TRANSFERENCIA = "transferenciaPaciente?faces-redirect=true";
	private static final String ENDERECO_ALTERACAO = "alteracaoPaciente?faces-redirect=true";
	private static final String ENDERECO_GERENCIAMENTO = "gerenciamentoPacientes?faces-redirect=true";
	private static final String ENDERECO_PTS = "pts?faces-redirect=true";
	private static final String ENDERECO_ID = "&amp;id=";

	public GerenciarPacienteController() {
		gerenciarpaciente = new GerenciarPacienteBean();
		gerenciarpaciente.setPrograma(null);
		listaGrupos = new ArrayList();
		gerenciarpaciente.setStatus("A");
		listaPacientes = new ArrayList<GerenciarPacienteBean>();
		apenasLeitura = false;
		rowBean = new GerenciarPacienteBean();
		insercao = new InsercaoPacienteBean();
		tipo = "";
		listaLaudosVigentes = new ArrayList<InsercaoPacienteBean>();
		iDao = new InsercaoPacienteDAO();
		listaDiasProfissional = new ArrayList<GerenciarPacienteBean>();
	}

	public void carregarBuscaGerenciamentoPaciente() {
		BuscaSessaoDTO buscaSessaoDTO = (BuscaSessaoDTO) SessionUtil.resgatarDaSessao(BUSCA_SESSAO);
		if (!VerificadorUtil.verificarSeObjetoNulo(buscaSessaoDTO)) {
			if (buscaSessaoDTO.getTela().equals(TelasBuscaSessao.GERENCIAMENTO_PACIENTES.getSigla())) {
				gerenciarpaciente.setGrupo(buscaSessaoDTO.getGrupoBean());
				gerenciarpaciente.setPrograma(buscaSessaoDTO.getProgramaBean());
			}
		}

	}

	public void buscarPacientesInstituicao() throws ProjetoException {
		busca = "S";
		SessionUtil.adicionarBuscaPtsNaSessao(gerenciarpaciente.getPrograma(), gerenciarpaciente.getGrupo(), null, null,
				TelasBuscaSessao.GERENCIAMENTO_PACIENTES.getSigla());
		carregarPacientesInstituicao();
		apenasLeitura = true;

	}

	public void limparBusca() throws ProjetoException {
		apenasLeitura = false;
		gerenciarpaciente = new GerenciarPacienteBean();
		busca = "N";
		carregarPacientesInstituicao();
	}

	public List<GrupoBean> listaGrupoAutoComplete(String query) throws ProjetoException {
		GrupoDAO grDao = new GrupoDAO();

		if (gerenciarpaciente.getPrograma().getIdPrograma() != null) {
			return grDao.listarGruposNoAutoComplete(query, this.gerenciarpaciente.getPrograma().getIdPrograma());
		} else {
			return null;
		}

	}

	public void carregaGruposDoPrograma() throws ProjetoException {
		GrupoDAO grDao = new GrupoDAO();
		if (gerenciarpaciente.getPrograma() != null) {
			if (gerenciarpaciente.getPrograma().getIdPrograma() != null) {
				listaGrupos = grDao.listarGruposPorPrograma(this.gerenciarpaciente.getPrograma().getIdPrograma());
			}
		}

	}

	public void inicioDesligar() {
		gerenciarpaciente = new GerenciarPacienteBean();
		JSFUtil.abrirDialog("dlgDeslPac");
	}

	public void carregarPacientesInstituicao() throws ProjetoException {
		listaPacientes = gDao.carregarPacientesInstituicaoBusca(gerenciarpaciente, campoBusca, tipoBusca);
	}

	public void desligarPaciente() throws ProjetoException {
		Boolean cadastrou = false;

		cadastrou = gDao.desligarPaciente(rowBean, gerenciarpaciente);

		if (cadastrou) {

			JSFUtil.fecharDialog("dlgDeslPac");

			listaPacientes = gDao.carregarPacientesInstituicaoBusca(gerenciarpaciente, campoBusca, tipoBusca);

			rowBean = new GerenciarPacienteBean();

			JSFUtil.adicionarMensagemSucesso("Paciente desligado com sucesso!", "Sucesso");

		} else {
			JSFUtil.adicionarMensagemErro("Ocorreu um erro durante o desligamento!", "Erro");
		}

	}

	public void gerarEncaminhamento() throws ProjetoException {
		Boolean cadastrou = false;

		cadastrou = gDao.encaminharPaciente(rowBean, gerenciarpaciente);

		if (cadastrou) {

			listaPacientes = gDao.carregarPacientesInstituicaoBusca(gerenciarpaciente, campoBusca, tipoBusca);

			JSFUtil.adicionarMensagemSucesso("Encaminhamento gerado com sucesso!", "Sucesso");

		} else {
			JSFUtil.adicionarMensagemErro("Ocorreu um erro durante o encaminhamento!", "Erro");
		}

	}

	public Date ajustarDataDeSolicitacao(Date dataSolicitacao, Integer codLaudo, Integer codPaciente, Integer codPrograma, Integer codGrupo) throws ProjetoException {
		//Caso nao venha o codigo do laudo é porque a renovacao será sem laudo, logo devera buscar o periodo da agenda pelo ultimo laudo do paciente
		LaudoDAO laudoDAO = new LaudoDAO();
		Date dataInicioLaudo = null;
		if (codLaudo != null) {
			LaudoBean laudoBean = laudoDAO.recuperarPeriodoInicioLaudo(codLaudo);
			dataInicioLaudo = DataUtil.montarDataCompleta(1, laudoBean.getMesInicio(), laudoBean.getAnoInicio());
		} else {
			LaudoBean laudoBean = laudoDAO.recuperarUltimoLaudoPaciente(codPaciente, codPrograma, codGrupo);
			dataInicioLaudo = DataUtil.montarDataCompleta(1, laudoBean.getMesFinal(), laudoBean.getAnoFinal());
			LocalDate dataAtual = LocalDate.now();
			dataAtual =  LocalDate.now().withDayOfMonth(1).withMonth(laudoBean.getMesFinal()).withYear(laudoBean.getAnoFinal()).plusMonths(1);
			dataInicioLaudo = Date.from(dataAtual.atStartOfDay(ZoneId.systemDefault()).toInstant());
		}
		if ((dataSolicitacao.after(dataInicioLaudo))) {// || (dataSolicitacao.equals(dataInicioLaudo))){
			return dataSolicitacao;
		} else {
			return dataInicioLaudo;
		}

	}
	
	public String redirectRenovacao() {
		return RedirecionarUtil.redirectEditSemTipo(ENDERECO_RENOVACAO, ENDERECO_ID, this.rowBean.getId());
	}

	public String redirectTransferencia() {
		return RedirecionarUtil.redirectEditSemTipo(ENDERECO_TRANSFERENCIA, ENDERECO_ID, this.rowBean.getId());
	}

	public String redirectAlteracao() {
		return RedirecionarUtil.redirectEditSemTipo(ENDERECO_ALTERACAO, ENDERECO_ID, this.rowBean.getId());
	}

	public String redirectGerenciamento() {
		return RedirecionarUtil.redirectPagina(ENDERECO_GERENCIAMENTO);
	}

	public GerenciarPacienteBean getGerenciarpaciente() {
		return gerenciarpaciente;
	}

	public void setGerenciarpaciente(GerenciarPacienteBean gerenciarpaciente) {
		this.gerenciarpaciente = gerenciarpaciente;
	}

	public List<GerenciarPacienteBean> getListaPacientes() {
		return listaPacientes;
	}

	public void setListaPacientes(List<GerenciarPacienteBean> listaPacientes) {
		this.listaPacientes = listaPacientes;
	}

	public Boolean getApenasLeitura() {
		return apenasLeitura;
	}

	public void setApenasLeitura(Boolean apenasLeitura) {
		this.apenasLeitura = apenasLeitura;
	}

	public String getBusca() {
		return busca;
	}

	public void setBusca(String busca) {
		this.busca = busca;
	}

	public GerenciarPacienteBean getRowBean() {
		return rowBean;
	}

	public void setRowBean(GerenciarPacienteBean rowBean) {
		this.rowBean = rowBean;
	}

	public InsercaoPacienteBean getInsercao() {
		return insercao;
	}

	public void setInsercao(InsercaoPacienteBean insercao) {
		this.insercao = insercao;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public List<GrupoBean> getListaGrupos() {
		return listaGrupos;
	}

	public void setListaGrupos(List<GrupoBean> listaGrupos) {
		this.listaGrupos = listaGrupos;
	}

	public String getTipoBusca() {
		return tipoBusca;
	}

	public String getCampoBusca() {
		return campoBusca;
	}

	public void setTipoBusca(String tipoBusca) {
		this.tipoBusca = tipoBusca;
	}

	public void setCampoBusca(String campoBusca) {
		this.campoBusca = campoBusca;
	}

}
