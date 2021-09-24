package br.gov.al.maceio.sishosp.hosp.control;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import br.gov.al.maceio.sishosp.acl.model.FuncionarioBean;
import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;
import br.gov.al.maceio.sishosp.comum.util.DataUtil;
import br.gov.al.maceio.sishosp.comum.util.HorarioOuTurnoUtil;
import br.gov.al.maceio.sishosp.comum.util.JSFUtil;
import br.gov.al.maceio.sishosp.comum.util.VerificadorUtil;
import br.gov.al.maceio.sishosp.hosp.dao.AtendimentoDAO;
import br.gov.al.maceio.sishosp.hosp.dao.EquipeDAO;
import br.gov.al.maceio.sishosp.hosp.dao.GrupoDAO;
import br.gov.al.maceio.sishosp.hosp.dao.ProgramaDAO;
import br.gov.al.maceio.sishosp.hosp.dao.RelatorioDAO;
import br.gov.al.maceio.sishosp.hosp.dao.TipoAtendimentoDAO;
import br.gov.al.maceio.sishosp.hosp.dao.UnidadeDAO;
import br.gov.al.maceio.sishosp.hosp.enums.OpcaoAtendimento;
import br.gov.al.maceio.sishosp.hosp.enums.TipoAtendimento;
import br.gov.al.maceio.sishosp.hosp.enums.TipoFiltroRelatorio;
import br.gov.al.maceio.sishosp.hosp.enums.TipoRelatorio;
import br.gov.al.maceio.sishosp.hosp.enums.Turno;
import br.gov.al.maceio.sishosp.hosp.model.*;
import br.gov.al.maceio.sishosp.hosp.model.dto.EquipeGrupoProgramaUnidadeDTO;
import br.gov.al.maceio.sishosp.hosp.model.dto.GrupoProgramaUnidadeDTO;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import org.primefaces.event.SelectEvent;

@ManagedBean(name = "RelatoriosController")
@ViewScoped
public class RelatoriosController implements Serializable {

	private static final String SUBREPORT_DIR = "SUBREPORT_DIR";
	private static final String CAMINHO_PRINCIPAL = "/WEB-INF/relatorios/";
	private static final long serialVersionUID = 1L;
	private ProgramaBean programa;
	private PacienteBean paciente;
	private EspecialidadeBean especialidade;
	private GrupoBean grupo;
	private GerenciarPacienteBean pacienteInstituicao;
	private TipoAtendimentoBean tipoAtendimento;
	private EquipeBean equipe;
	private ProcedimentoBean procedimento;
	private FuncionarioBean prof;
	private List<UnidadeBean> listaUnidades;
	private List<ProgramaBean> listaPrograma;
	private List<GrupoBean> listaGrupos;
	private List<UnidadeBean> listaUnidadesSelecionadas;
	private List<ProgramaBean> listaProgramaSelecionados;
	private List<GrupoProgramaUnidadeDTO> listaGruposProgramaUnidadeDTO;
	private List<GrupoProgramaUnidadeDTO> listaGruposProgramaUnidadeDTOSelecionados;
	private List<EquipeGrupoProgramaUnidadeDTO> listaEquipeGruposProgramaUnidadeDTO;
	private List<EquipeGrupoProgramaUnidadeDTO> listaEquipeGruposProgramaUnidadeDTOSelecionados;
	private List<EquipeBean> listaEquipe;
	private List<TipoAtendimentoBean> listaTipos;
	private String atributoGenerico1;
	private String atributoGenerico2;
	private String atributoGenerico3;
	private String atributoGenerico4;
	private Integer valorGenerico1;
	private Integer valorGenerico2;
	private UnidadeBean unidade;
	private List<GrupoBean> listaGruposProgramas;
	private List<EquipeBean> listaEquipePorTipoAtendimento;
	private AgendaController agendaController;
	private Date dataInicial;
	private Date dataFinal;

	private String recurso;
	private String tipoExameAuditivo;
	private String situacao;
	private String tipoAnalSint;
	private String dataDia;

	private Date dataEspec;
	private Integer diaSemana;
	private Integer mes;
	private Integer ano;
	private Boolean filtrarPorMunicipio;
	private boolean agruparPorUnidade;
	private boolean agruparEquipeDia;

	private Integer idadeMinima;
	private Integer idadeMaxima;
	private ArrayList<String> diasSemana;
	private ArrayList<String> turnos;
	private String turnoSelecionado;
	private Integer idSituacaoAtendimento;
	private List<MunicipioBean> listaMunicipiosDePacienteAtivosSelecionados;
	FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
			.getSessionMap().get("obj_usuario");
	private GrupoDAO grupoDao;
	private List<Integer> listaAnos;
	private boolean filtrarPorQuantidade;
	private String opcaoAtendimento;

	public RelatoriosController() throws ProjetoException {
		this.programa = new ProgramaBean();
		agendaController = new AgendaController();
		this.grupo = new GrupoBean();
		especialidade = new EspecialidadeBean();
		this.tipoAtendimento = new TipoAtendimentoBean();
		this.prof = new FuncionarioBean();
		this.listaGrupos = new ArrayList<GrupoBean>();
		this.listaTipos = new ArrayList<TipoAtendimentoBean>();
		procedimento = new ProcedimentoBean();
		this.dataInicial = null;
		this.dataFinal = null;
		this.tipoExameAuditivo = new String("TODOS");
		this.tipoAnalSint = new String("A");
		this.dataDia = new String("DS");
		this.dataEspec = null;
		pacienteInstituicao = new GerenciarPacienteBean();
		unidade = new UnidadeBean();
		listaGruposProgramas = new ArrayList<>();
		equipe = new EquipeBean();
		listaEquipePorTipoAtendimento = new ArrayList<>();
		this.turnos = new ArrayList<String>();
		listaMunicipiosDePacienteAtivosSelecionados = new ArrayList<MunicipioBean>();
		this.grupoDao = new GrupoDAO();
		this.listaAnos = new ArrayList<>();
		this.listaUnidades = new ArrayList<>();
		this.listaPrograma  = new ArrayList<>();
		this.listaUnidadesSelecionadas = new ArrayList<>();
		this.listaProgramaSelecionados = new ArrayList<>();
		this.listaGruposProgramaUnidadeDTO = new ArrayList<>();
		this.listaGruposProgramaUnidadeDTOSelecionados = new ArrayList<>();
		this.listaEquipeGruposProgramaUnidadeDTO = new ArrayList<>();
		this.listaEquipeGruposProgramaUnidadeDTOSelecionados = new ArrayList<>();
		this.opcaoAtendimento = HorarioOuTurnoUtil.retornarOpcaoAtendimentoUnidade();
	}

	public void limparDados() {
		this.listaGrupos = new ArrayList<GrupoBean>();
		this.listaTipos = new ArrayList<TipoAtendimentoBean>();
		this.programa = new ProgramaBean();
		this.grupo = new GrupoBean();
		this.tipoAtendimento = new TipoAtendimentoBean();
		this.prof = new FuncionarioBean();
		this.dataInicial = null;
		this.dataFinal = null;
		this.tipoExameAuditivo = new String("TODOS");
		this.tipoAnalSint = new String("A");
		this.dataDia = new String("DS");
		this.ano = null;
		this.mes = null;
		this.dataEspec = null;
	}

	public boolean verificarMesesIguais(Date dataInicial, Date dataFinal) {
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		c1.setTime(dataInicial);
		c2.setTime(dataFinal);
		int mes1 = c1.get(Calendar.MONTH);
		int mes2 = c2.get(Calendar.MONTH);
		if (mes1 == mes2)
			return true;
		else
			return false;
	}

	public void preparaRelLaudosVencer() {
		atributoGenerico1 = "N";
		atributoGenerico2 = "P";
	}

	public void preparaRelFrequencia() throws ProjetoException {
		atributoGenerico1 = "P";
	}

	public void preparaRelFrequenciaPreenchida() throws ProjetoException {
		atributoGenerico1 = "P";
		listarAnosAtendimentos();
	}

	public void preparaRelatorioAgendamentos() {
		atributoGenerico1 = "A";
	}

	public void preparaRelatorioAtendimentos() {
		atributoGenerico3 = "P";
	}

	public void setaOpcaoRelLaudoVencer() {
		atributoGenerico2 = "P";
		resetaTipoGeracaoLaudos();
	}

	public void resetaTipoGeracaoLaudos() {
		agendaController.getAgenda().setPrograma(null);
		agendaController.getAgenda().setGrupo(null);
		agendaController.getAgenda().setPaciente(null);
		equipe = null;
	}

	public void preparaRelPacientesPorPrograma() {
		atributoGenerico1 = "I";
		atributoGenerico2 = "A";
		atributoGenerico3 = "G";
	}

	public void preparaRelPendenciasEvolucao() {
		atributoGenerico1 = "PG";
	}

	public void selectPrograma(SelectEvent event) throws ProjetoException {
		this.programa = (ProgramaBean) event.getObject();
		atualizaListaGrupos(programa);
	}

	public void atualizaListaGrupos(ProgramaBean programa) throws ProjetoException {
		this.programa = programa;
		this.listaGruposProgramas = grupoDao.listarGruposPorPrograma(programa.getIdPrograma());

		listaTipos = new ArrayList<>();

	}

	// LISTAS E AUTOCOMPLETES INICIO
	public List<GrupoBean> listaGrupoAutoComplete(String query) throws ProjetoException {

		if (!VerificadorUtil.verificarSeObjetoNuloOuZero(programa.getIdPrograma())) {
			return grupoDao.listarGruposNoAutoComplete(query, programa.getIdPrograma());
		} else {
			return null;
		}

	}

	public void selectGrupo(SelectEvent event) throws ProjetoException {
		this.grupo = (GrupoBean) event.getObject();
		atualizaListaTipos(grupo);
	}

	public void atualizaListaTipos(GrupoBean g) throws ProjetoException {
		this.grupo = g;
		TipoAtendimentoDAO tDao = new TipoAtendimentoDAO();
		this.listaTipos = tDao.listarTipoAtPorGrupo(g.getIdGrupo(), TipoAtendimento.TODOS.getSigla());
	}

	public List<TipoAtendimentoBean> listaTipoAtAutoComplete(String query) throws ProjetoException {
		List<TipoAtendimentoBean> lista = new ArrayList<>();

		if (grupo != null) {
			TipoAtendimentoDAO tDao = new TipoAtendimentoDAO();
			lista = tDao.listarTipoAtAutoComplete(query, this.grupo, TipoAtendimento.TODOS.getSigla());
		} else
			return null;
		return lista;
	}

	public void carregaListaEquipePorTipoAtendimento() throws ProjetoException {
		if (tipoAtendimento != null) {
			if (grupo.getIdGrupo() != null) {
				EquipeDAO eDao = new EquipeDAO();
				listaEquipePorTipoAtendimento = eDao.listarEquipePorGrupo(grupo.getIdGrupo());
			}
		}
	}

	public void listaEquipePorGrupo() throws ProjetoException {
		EquipeDAO eDao = new EquipeDAO();
		listaEquipe = eDao.listarEquipePorGrupo(grupo.getIdGrupo());
	}

	public List<EquipeBean> listaEquipeAutoComplete(String query) throws ProjetoException {
		EquipeDAO eDao = new EquipeDAO();
		List<EquipeBean> result = eDao.listarEquipePorGrupoAutoComplete(query, grupo.getIdGrupo());
		return result;
	}
	
	public void selecionarGrupoLaudoAhVencer(GrupoBean grupo) {
		this.grupo = grupo;
	}

	public void geraLaudoVencer(ProgramaBean programa, GrupoBean grupo, PacienteBean paciente, EquipeBean equipe)
			throws IOException, ParseException, ProjetoException {

		if (atributoGenerico1.equals(null)) {
			JSFUtil.adicionarMensagemErro("Informe o período a vencer!", "Erro!");
		} else {
			String caminho = CAMINHO_PRINCIPAL;
			String relatorio = "";
			if (atributoGenerico1.equals("N"))
				relatorio = caminho + "laudosvencernominal.jasper";
			else
				relatorio = caminho + "laudovencer.jasper";
			Map<String, Object> map = new HashMap<String, Object>();
			if ((atributoGenerico3 != null) && (atributoGenerico3.equals("true")))
				this.atributoGenerico4 = null;
			map.put("periodolaudovencer", this.atributoGenerico4);
			map.put("codunidade", user_session.getUnidade().getId());
			map.put("codempresa", user_session.getUnidade().getCodEmpresa());
			if (programa != null)
				map.put("codprograma", programa.getIdPrograma());

			if (grupo != null)
				map.put("codgrupo", grupo.getIdGrupo());

			if (paciente != null)
				map.put("codpaciente", paciente.getId_paciente());
			
			if(equipe != null)
				map.put("codequipe", equipe.getCodEquipe());
			
			map.put("cod_laudo", null);

			if ((atributoGenerico3 != null) && (atributoGenerico3.equals("true")))
				map.put("mostrarlaudosvencidos", atributoGenerico3);
			else
				map.put("mostrarlaudosvencidos", null);
			this.executeReport(relatorio, map, "relatorio.pdf");
			// this.executeReportNewTab(relatorio, "laudovencer.pdf",
			// map);

		}
	}

	public void geraLaudo(Integer idLaudo) throws IOException, ParseException, ProjetoException {

		String caminho = CAMINHO_PRINCIPAL;
		String relatorio = "";
		relatorio = caminho + "laudo.jasper";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("codempresa", user_session.getUnidade().getCodEmpresa());
		map.put("cod_laudo", idLaudo);
		this.executeReportNewTab(relatorio, "laudo.pdf", map);

	}

	public void gerarRelatorioPacientesAtivosLaudoVencido(ProgramaBean programa, GrupoBean grupo)
			throws IOException, ParseException, ProjetoException, NoSuchAlgorithmException {

		String caminho = CAMINHO_PRINCIPAL;
		String relatorio = "";
		relatorio = caminho + "pacientes_ativos_laudo_vencido.jasper";
		Map<String, Object> map = new HashMap<String, Object>();

		map.put("codunidade", user_session.getUnidade().getId());
		if (pacienteInstituicao.getPrograma() != null)
			map.put("codprograma", pacienteInstituicao.getPrograma().getIdPrograma());

		if (pacienteInstituicao.getGrupo() != null)
			map.put("codgrupo", pacienteInstituicao.getGrupo().getIdGrupo());

		this.executeReport(relatorio, map, "relatorio_pacientes_ativos_laudo_vencido.pdf");
	}


	public void geraFrequenciaPreenchida(PacienteBean paciente, ProgramaBean programa, GrupoBean grupo)
			throws IOException, ParseException, ProjetoException, NoSuchAlgorithmException {

		String caminho = CAMINHO_PRINCIPAL;
		String relatorio = "";
		relatorio = caminho + "frequencia_preenchida.jasper";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("ano", this.ano);
		map.put("mes", this.mes);
		map.put("codunidade", user_session.getUnidade().getId());
		if (!VerificadorUtil.verificarSeObjetoNulo(programa))
			map.put("codprograma", programa.getIdPrograma());

		if (!VerificadorUtil.verificarSeObjetoNulo(grupo))
			map.put("codgrupo", grupo.getIdGrupo());

		if (!VerificadorUtil.verificarSeObjetoNuloOuZero(paciente))
			map.put("codpaciente", paciente.getId_paciente());

		map.put(SUBREPORT_DIR, this.getServleContext().getRealPath(caminho) + File.separator);
		this.executeReport(relatorio, map, "relatorio.pdf");

	}

	public void geraFrequencia(GerenciarPacienteBean pacienteInstituicao, ProgramaBean programa, GrupoBean grupo)
			throws IOException, ParseException, ProjetoException, NoSuchAlgorithmException {
		if (!atributoGenerico1.equals("A")) {
			if (camposvalidos(programa, grupo)) {

				Integer frequencia = grupoDao.buscarFrequencia(programa.getIdPrograma(), grupo.getIdGrupo());
				pacienteInstituicao.setPrograma(programa);
				pacienteInstituicao.setGrupo(grupo);
				int randomico = JSFUtil.geraNumeroRandomico();
				RelatorioDAO rDao = new RelatorioDAO();
				rDao.popularTabelaTemporariaFrequencia(randomico, frequencia);

				if ((pacienteInstituicao.getPrograma() == null) && (pacienteInstituicao.getLaudo().getPaciente() == null)) {
					JSFUtil.adicionarMensagemErro("Informe o Programa ou Paciente obrigatoriamente!", "Erro!");
				} else {
					String caminho = CAMINHO_PRINCIPAL;
					String relatorio = "";

					Map<String, Object> map = new HashMap<String, Object>();
					map.put("chave", randomico);
					map.put("codunidade", user_session.getUnidade().getId());

					ArrayList<Integer> diasSemanaInteger = new ArrayList<Integer>();
					setaDiasSemanaComoListaDeInteiro(diasSemanaInteger);
					map.put("diassemanalista", diasSemanaInteger);

					limparTurno();
					atribuiTurnos();

					map.put("turnoslista", turnos);

					if (!VerificadorUtil.verificarSeObjetoNuloOuZero(pacienteInstituicao.getId()))
						map.put("codpacienteinstituicao", pacienteInstituicao.getId());

					if (!VerificadorUtil.verificarSeObjetoNulo(pacienteInstituicao.getPrograma()))
						map.put("codprograma", pacienteInstituicao.getPrograma().getIdPrograma());

					if (!VerificadorUtil.verificarSeObjetoNulo(pacienteInstituicao.getGrupo()))
						map.put("codgrupo", pacienteInstituicao.getGrupo().getIdGrupo());

					map.put(SUBREPORT_DIR, this.getServleContext().getRealPath(caminho) + File.separator);
					relatorio = caminho + "frequencia.jasper";
					this.executeReport(relatorio, map, "relatorio.pdf");
					rDao.limparTabelaTemporariaFrequencia(randomico);

				}
			}
		}
		else
		{
				Integer frequencia = 20;
				int randomico = JSFUtil.geraNumeroRandomico();
				RelatorioDAO rDao = new RelatorioDAO();
				rDao.popularTabelaTemporariaFrequencia(randomico, frequencia);
					String caminho = CAMINHO_PRINCIPAL;
					String relatorio = "";

					Map<String, Object> map = new HashMap<String, Object>();
					map.put("chave", randomico);
					map.put("codunidade", user_session.getUnidade().getId());
					map.put("codpaciente", paciente.getId_paciente());
					map.put(SUBREPORT_DIR, this.getServleContext().getRealPath(caminho) + File.separator);
					relatorio = caminho + "frequencia_avulsa.jasper";
					this.executeReport(relatorio, map, "frequencia_avulsa.pdf");
					rDao.limparTabelaTemporariaFrequencia(randomico);


		}
	}

	private void listarAnosAtendimentos() throws ProjetoException {
		this.listaAnos = new AtendimentoDAO().listaAnosDeAtendimentos();
	}

	private boolean camposvalidos(ProgramaBean programa, GrupoBean grupo) {

		boolean valido = true;

		if (VerificadorUtil.verificarSeObjetoNulo(programa) || VerificadorUtil.verificarSeObjetoNuloOuZero(programa.getIdPrograma())){
			JSFUtil.adicionarMensagemErro("Programa: Campo obrigatório!", "");
			valido = false;
		}

		if(VerificadorUtil.verificarSeObjetoNulo(grupo) || VerificadorUtil.verificarSeObjetoNuloOuZero(grupo.getIdGrupo())) {
			JSFUtil.adicionarMensagemErro("Grupo: Campo obrigatório!", "");
			valido = false;
		}
		return valido;
	}

	public void limparFiltroPorQuantidade() {
		atributoGenerico2 = "ME";
		valorGenerico1 = null;
		valorGenerico2 = null;
	}

	public void listarUnidadesUsuario() throws ProjetoException {
		listaUnidades = new UnidadeDAO().carregarUnidadesDoFuncionario();
		JSFUtil.abrirDialog("dlgConsulUni");
	}

	public void adicionarUnidadeSelecionada(UnidadeBean unidadeSelecionada) {
		if(!unidadeJaFoiAdicionada(unidadeSelecionada)) {
			listaUnidadesSelecionadas.add(unidadeSelecionada);
			JSFUtil.fecharDialog("dlgConsulUni");
		}
	}

	private boolean unidadeJaFoiAdicionada(UnidadeBean unidadeSelecionada) {
		if(listaUnidadesSelecionadas.contains(unidadeSelecionada)) {
			JSFUtil.adicionarMensagemErro("Está Unidade Já foi Adicionada", "");
			return true;
		}
		return false;
	}

	public void adicionarTodasUnidadesSelecionadas() {
		listaUnidadesSelecionadas.clear();
		listaUnidadesSelecionadas.addAll(listaUnidades);
		JSFUtil.fecharDialog("dlgConsulUni");
	}

	public void removerUnidadeAdicionada(UnidadeBean unidadeSelecionada) {
		if(!existemProgramasAssociadosComEssaUnidade(unidadeSelecionada.getId()))
			listaUnidadesSelecionadas.remove(unidadeSelecionada);
	}

	private boolean existemProgramasAssociadosComEssaUnidade(Integer idUnidade) {

		for (ProgramaBean programa : listaProgramaSelecionados) {
			if(programa.getCodUnidade().equals(idUnidade)) {
				JSFUtil.adicionarMensagemErro
						("Existe(m) programas associados a esta unidade por favor remova-o(s) primeiro", "");
				return true;
			}
		}
		return false;
	}

	public void listarProgramasPorUnidades() throws ProjetoException {
		listaPrograma = new ProgramaDAO().buscaProgramasPorUnidade(listaUnidadesSelecionadas);
		JSFUtil.abrirDialog("dlgConsuProg");
	}

	public void adicionarProgramaSelecionada(ProgramaBean programaSelecionado) {
		if(!programaJaFoiAdicionada(programaSelecionado)) {
			listaProgramaSelecionados.add(programaSelecionado);
			JSFUtil.fecharDialog("dlgConsuProg");
		}
	}

	private boolean programaJaFoiAdicionada(ProgramaBean programaSelecionado) {

		for (ProgramaBean programa : listaProgramaSelecionados) {
			if(programa.getIdPrograma().equals(programaSelecionado.getIdPrograma())) {
				JSFUtil.adicionarMensagemErro("Este Programa Já foi Adicionado", "");
				return true;
			}
		}
		return false;
	}

	public void adicionarTodosProgramaSelecionados() {
		listaProgramaSelecionados.clear();
		listaProgramaSelecionados.addAll(listaPrograma);
		JSFUtil.fecharDialog("dlgConsuProg");
	}

	public void removerProgramaAdicionado(ProgramaBean programaSelecionado) {
		if(!existemGruposAssociadosComEssePrograma(programaSelecionado.getIdPrograma()))
			listaProgramaSelecionados.remove(programaSelecionado);
	}

	private boolean existemGruposAssociadosComEssePrograma(Integer idPrograma) {

		for (GrupoProgramaUnidadeDTO grupoProgramaUnidadeDTO : listaGruposProgramaUnidadeDTOSelecionados) {
			if(grupoProgramaUnidadeDTO.getPrograma().getIdPrograma().equals(idPrograma)) {
				JSFUtil.adicionarMensagemErro
						("Existe(m) grupos associados a este programa por favor remova-o(s) primeiro", "");
				return true;
			}
		}
		return false;
	}

	public void listarGruposPorProgramasUnidades() throws ProjetoException {
		listaGruposProgramaUnidadeDTO = grupoDao.buscaProgramasPorUnidade(listaProgramaSelecionados);
		JSFUtil.abrirDialog("dlgConsuGrop");
	}

	public void adicionarGrupoPorProgramaUnidadeSelecionada(GrupoProgramaUnidadeDTO grupoSelecionado) {
		if(!grupoPorProgramaUnidadeJaFoiAdicionada(grupoSelecionado)) {
			listaGruposProgramaUnidadeDTOSelecionados.add(grupoSelecionado);
			JSFUtil.fecharDialog("dlgConsuGrop");
		}
	}

	private boolean grupoPorProgramaUnidadeJaFoiAdicionada(GrupoProgramaUnidadeDTO grupoSelecionado) {
		for (GrupoProgramaUnidadeDTO grupoDTO : listaGruposProgramaUnidadeDTOSelecionados) {
			if(grupoDTO.getGrupo().getIdGrupo().equals(grupoSelecionado.getGrupo().getIdGrupo())
					&& grupoDTO.getPrograma().getIdPrograma().equals(grupoSelecionado.getPrograma().getIdPrograma())) {
				JSFUtil.adicionarMensagemErro("Este Grupo Já foi Adicionado", "");
				return true;
			}
		}
		return false;
	}

	public void adicionarTodosGrupoPorProgramaUnidadeSelecionados() {
		listaGruposProgramaUnidadeDTOSelecionados.clear();
		listaGruposProgramaUnidadeDTOSelecionados.addAll(listaGruposProgramaUnidadeDTO);
		JSFUtil.fecharDialog("dlgConsuGrop");
	}

	public void removerGrupoPorProgramaUnidadeAdicionado(GrupoProgramaUnidadeDTO grupoSelecionado) {
		if(!existemEquipesAssociadasComEsseGrupo(grupoSelecionado.getGrupo().getIdGrupo()))
			listaGruposProgramaUnidadeDTOSelecionados.remove(grupoSelecionado);
	}
	
	private boolean existemEquipesAssociadasComEsseGrupo(Integer idGrupo) {

		for (EquipeGrupoProgramaUnidadeDTO equipeGrupoProgramaUnidadeDTO : listaEquipeGruposProgramaUnidadeDTOSelecionados) {
			if(equipeGrupoProgramaUnidadeDTO.getGrupo().getIdGrupo().equals(idGrupo)) {
				JSFUtil.adicionarMensagemErro
						("Existe(m) equipes associadas a este grupo por favor remove-a(s) primeiro", "");
				return true;
			}
		}
		return false;
	}
	
	public void listarEquipeGruposPorProgramasUnidades() throws ProjetoException {
		listaEquipeGruposProgramaUnidadeDTO = new EquipeDAO().buscaEquipesPorProgramas(listaGruposProgramaUnidadeDTOSelecionados);
		JSFUtil.abrirDialog("dlgConsuEquipe");
	}
	
	public void adicionarTodasEquipesGrupoPorProgramaUnidadeSelecionados() {
		listaEquipeGruposProgramaUnidadeDTOSelecionados.clear();
		listaEquipeGruposProgramaUnidadeDTOSelecionados.addAll(listaEquipeGruposProgramaUnidadeDTO);
		JSFUtil.fecharDialog("dlgConsuEquipe");
	}
	
	public void adicionarEquipeGrupoPorProgramaUnidadeSelecionada(EquipeGrupoProgramaUnidadeDTO equipeSelecionada) {
		if(!equipeGrupoPorProgramaUnidadeJaFoiAdicionada(equipeSelecionada)) {
			listaEquipeGruposProgramaUnidadeDTOSelecionados.add(equipeSelecionada);
			JSFUtil.fecharDialog("dlgConsuEquipe");
		}
	}
	
	private boolean equipeGrupoPorProgramaUnidadeJaFoiAdicionada(EquipeGrupoProgramaUnidadeDTO equipeSelecionada) {
		for (EquipeGrupoProgramaUnidadeDTO equipeDTO : listaEquipeGruposProgramaUnidadeDTOSelecionados) {
			if(equipeDTO.getGrupo().getIdGrupo().equals(equipeSelecionada.getGrupo().getIdGrupo())
					&& equipeDTO.getEquipe().getCodEquipe().equals(equipeSelecionada.getEquipe().getCodEquipe())) {
				JSFUtil.adicionarMensagemErro("Esta Equipe Já foi Adicionada", "");
				return true;
			}
		}
		return false;
	}
	
	public void removerEquipeGrupoPorProgramaUnidadeAdicionado(EquipeGrupoProgramaUnidadeDTO equipeSelecionada) {
		listaEquipeGruposProgramaUnidadeDTOSelecionados.remove(equipeSelecionada);
	}
	
	public void limparAgrupamentoDia() {
		this.agruparEquipeDia = false;
	}

	public void gerarRelatorioAtendimento(GerenciarPacienteBean pacienteInstituicao, ProgramaBean programa, GrupoBean grupo)
			throws IOException, ParseException, ProjetoException, NoSuchAlgorithmException {

		pacienteInstituicao.setPrograma(programa);
		pacienteInstituicao.setGrupo(grupo);
		int randomico = JSFUtil.geraNumeroRandomico();
		RelatorioDAO rDao = new RelatorioDAO();

		String caminho = CAMINHO_PRINCIPAL;
		String relatorio = "";

		if(!validaValorQuantidade())
			return;

		Map<String, Object> map = new HashMap<String, Object>();

		map.put("dt_inicial", dataInicial);
		map.put("dt_final", dataFinal);
		map.put("agruparunidade", agruparPorUnidade);

		if(!listaUnidadesSelecionadas.isEmpty()) {
			map.put("listaunidades", retornaListaIdUnidades(listaUnidadesSelecionadas));
		}

		if(!listaProgramaSelecionados.isEmpty()) {
			map.put("listaprogramas", retornaListaIdProgramas(listaProgramaSelecionados));
		}

		if(!VerificadorUtil.verificarSeObjetoNuloOuZero(this.idSituacaoAtendimento))
			map.put("id_situacao_atendimento", this.idSituacaoAtendimento);

		if(this.turnoSelecionado.equals(Turno.MANHA.getSigla()) || this.turnoSelecionado.equals(Turno.TARDE.getSigla()))
			map.put("turno", this.turnoSelecionado);

		if (!VerificadorUtil.verificarSeObjetoNuloOuZero(paciente))
			map.put("id_paciente", paciente.getId_paciente());

		if (!VerificadorUtil.verificarSeObjetoNuloOuZero(especialidade))
			map.put("codespecialidade", especialidade.getCodEspecialidade());

		ArrayList<Integer> diasSemanaInteger = new ArrayList<Integer>();
		setaDiasSemanaComoListaDeInteiro(diasSemanaInteger);
		map.put("diassemanalista", diasSemanaInteger);

		if (VerificadorUtil.verificarSeObjetoNuloOuZero(idadeMinima))
			map.put("idademinima", 0);
		else
			map.put("idademinima", idadeMinima);

		if (VerificadorUtil.verificarSeObjetoNuloOuZero(idadeMaxima))
			map.put("idademaxima", 200);
		else
			map.put("idademaxima", idadeMaxima);

		if ((!VerificadorUtil.verificarSeObjetoNulo(prof)) && (!VerificadorUtil.verificarSeObjetoNuloOuZero(prof.getId())))
			map.put("codprofissional", this.prof.getId());

		if(!VerificadorUtil.verificarSeObjetoNuloOuVazio(atributoGenerico2)) {
			if (atributoGenerico2.equals("EN")) {
				map.put("qtdatendimentosmenorigual", valorGenerico1);
				map.put("qtdatendimentosmaiorigual", valorGenerico2);
			} else if (atributoGenerico2.equals("MA")) {
				map.put("qtdatendimentosmaiorigual", valorGenerico1);
			} else if (atributoGenerico2.equals("ME")) {
				map.put("qtdatendimentosmenorigual", valorGenerico1);
			}
		}

		map.put(SUBREPORT_DIR, this.getServleContext().getRealPath(caminho) + File.separator);

		if (atributoGenerico1.equalsIgnoreCase(TipoRelatorio.ANALITICO.getSigla())
				&& atributoGenerico3.equalsIgnoreCase(TipoFiltroRelatorio.PROGRAMA.getSigla())) {
			relatorio = caminho + "atendimentosporprograma.jasper";
			this.executeReport(relatorio, map, "relatorio_atendimento_analítico.pdf");

			rDao.limparTabelaTemporariaFrequencia(randomico);
		}

		
		else if (atributoGenerico1.equalsIgnoreCase(TipoRelatorio.ANALITICO.getSigla())
				&& atributoGenerico3.equalsIgnoreCase(TipoFiltroRelatorio.GRUPO.getSigla())) {
			if (!listaGruposProgramaUnidadeDTOSelecionados.isEmpty()) {
				map.put("listagrupos", retornaListaIdGrupos(listaGruposProgramaUnidadeDTOSelecionados));
			}

			relatorio = caminho + "atendimentosporprogramagrupo.jasper";
			this.executeReport(relatorio, map, "relatorio_atendimento_analítico.pdf");

			rDao.limparTabelaTemporariaFrequencia(randomico);
		}
		
		else if (atributoGenerico1.equalsIgnoreCase(TipoRelatorio.ANALITICO.getSigla())
				&& atributoGenerico3.equalsIgnoreCase(TipoFiltroRelatorio.EQUIPE.getSigla())) {
			if (!listaGruposProgramaUnidadeDTOSelecionados.isEmpty()) {
				map.put("listagrupos", retornaListaIdGrupos(listaGruposProgramaUnidadeDTOSelecionados));
			}
			
			if (!listaEquipeGruposProgramaUnidadeDTOSelecionados.isEmpty()) {
				map.put("listaequipes", retornaListaIdEquipes(listaEquipeGruposProgramaUnidadeDTOSelecionados));
			}
			
			map.put("agrupardia", agruparEquipeDia);
			relatorio = caminho + "atendimentosporprogramagrupoequipe.jasper";
			this.executeReport(relatorio, map, "relatorio_atendimento_analítico.pdf");

			rDao.limparTabelaTemporariaFrequencia(randomico);
		}
		
		
		else if (atributoGenerico1.equalsIgnoreCase(TipoRelatorio.SINTETICO.getSigla())
				&&  atributoGenerico3.equalsIgnoreCase(TipoFiltroRelatorio.PROGRAMA.getSigla())) {
			relatorio = caminho + "atendimentosporprogramasintetico.jasper";
			this.executeReport(relatorio, map, "relatorio_atendimento_sintético.pdf");
		}
		
		
		else if (atributoGenerico1.equalsIgnoreCase(TipoRelatorio.SINTETICO.getSigla())
				&&  atributoGenerico3.equalsIgnoreCase(TipoFiltroRelatorio.GRUPO.getSigla())) {
			if (!listaGruposProgramaUnidadeDTOSelecionados.isEmpty()) {
				map.put("listagrupos", retornaListaIdGrupos(listaGruposProgramaUnidadeDTOSelecionados));
			}
			relatorio = caminho + "atendimentosporprogramagruposintetico.jasper";
			this.executeReport(relatorio, map, "relatorio_atendimento_sintético.pdf");
		}
		
		
		else if (atributoGenerico1.equalsIgnoreCase(TipoRelatorio.SINTETICO.getSigla())
				&&  atributoGenerico3.equalsIgnoreCase(TipoFiltroRelatorio.EQUIPE.getSigla())) {
			
			if (!listaGruposProgramaUnidadeDTOSelecionados.isEmpty()) {
				map.put("listagrupos", retornaListaIdGrupos(listaGruposProgramaUnidadeDTOSelecionados));
			}
			
			if (!listaEquipeGruposProgramaUnidadeDTOSelecionados.isEmpty()) {
				map.put("listaequipes", retornaListaIdEquipes(listaEquipeGruposProgramaUnidadeDTOSelecionados));
			}
			map.put("agrupardia", agruparEquipeDia);
			relatorio = caminho + "atendimentosporprogramagrupoequipesintetico.jasper";
			this.executeReport(relatorio, map, "relatorio_atendimento_sintético.pdf");
		}
		
		
		else if (atributoGenerico1.equalsIgnoreCase(TipoRelatorio.QUANTIDADE_ATENDIMENTOS.getSigla()) && 
					(atributoGenerico3.equalsIgnoreCase(TipoFiltroRelatorio.PROGRAMA.getSigla()) 
							|| atributoGenerico3.equalsIgnoreCase(TipoFiltroRelatorio.GRUPO.getSigla()))  ) {

			if (!listaGruposProgramaUnidadeDTOSelecionados.isEmpty()) {
				map.put("listagrupos", retornaListaIdGrupos(listaGruposProgramaUnidadeDTOSelecionados));
			}
			
			relatorio = caminho + "qtdatendimentosporprogramagrupo.jasper";
			this.executeReport(relatorio, map, "relatorio_atendimento.pdf");
		}
		
		else if (atributoGenerico1.equalsIgnoreCase(TipoRelatorio.QUANTIDADE_ATENDIMENTOS.getSigla()) 
				&& atributoGenerico3.equalsIgnoreCase(TipoFiltroRelatorio.EQUIPE.getSigla())) {
			
			if (!listaGruposProgramaUnidadeDTOSelecionados.isEmpty()) {
				map.put("listagrupos", retornaListaIdGrupos(listaGruposProgramaUnidadeDTOSelecionados));
			}
			
			if (!listaEquipeGruposProgramaUnidadeDTOSelecionados.isEmpty()) {
				map.put("listaequipes", retornaListaIdEquipes(listaEquipeGruposProgramaUnidadeDTOSelecionados));
			}
			relatorio = caminho + "qtdatendimentosporprogramagrupoequipe.jasper";
			this.executeReport(relatorio, map, "relatorio_atendimento.pdf");
		}
	}

	private List<Integer> retornaListaIdUnidades(List<UnidadeBean> listaUnidades){
		List<Integer> listaIds = new ArrayList<>();
		for (UnidadeBean unidade : listaUnidades) {
			listaIds.add(unidade.getId());
		}
		return listaIds;
	}

	private List<Integer> retornaListaIdProgramas(List<ProgramaBean> listaProgramas){
		List<Integer> listaIds = new ArrayList<>();
		for (ProgramaBean programa : listaProgramas) {
			listaIds.add(programa.getIdPrograma());
		}
		return listaIds;
	}

	private List<Integer> retornaListaIdGrupos(List<GrupoProgramaUnidadeDTO> listaGrupos){
		List<Integer> listaIds = new ArrayList<>();
		for (GrupoProgramaUnidadeDTO grupo : listaGrupos) {
			listaIds.add(grupo.getGrupo().getIdGrupo());
		}
		return listaIds;
	}

	private List<Integer> retornaListaIdEquipes(List<EquipeGrupoProgramaUnidadeDTO> listaEquipes){
		List<Integer> listaIds = new ArrayList<>();
		for (EquipeGrupoProgramaUnidadeDTO equipe : listaEquipes) {
			if(!listaIds.contains(equipe.getEquipe().getCodEquipe()))
				listaIds.add(equipe.getEquipe().getCodEquipe());
		}
		return listaIds;
	}

	private boolean validaValorQuantidade() {
		if(VerificadorUtil.verificarSeObjetoNuloOuVazio(atributoGenerico2))
			return true;
		else if (atributoGenerico2.equals("EN")) {

			if( (VerificadorUtil.verificarSeObjetoNulo(valorGenerico1)
					&& !VerificadorUtil.verificarSeObjetoNulo(valorGenerico2) )
					|| (VerificadorUtil.verificarSeObjetoNulo(valorGenerico2)
					&& !VerificadorUtil.verificarSeObjetoNulo(valorGenerico1) )) {
				JSFUtil.adicionarMensagemErro("Insira os dois valores válidos para filtrar a quantidade!", "");
				return false;
			}

			else if ( (valorGenerico1 > 0 && valorGenerico2 <= 0)
					|| (valorGenerico2 > 0 && valorGenerico1 <= 0) ) {
				JSFUtil.adicionarMensagemErro("Insira os dois valores válidos para filtrar a quantidade!", "");
				return false;
			}

			else if(valorGenerico1 >= valorGenerico2) {
				JSFUtil.adicionarMensagemErro("Segundo valor da quantidade deve ser maior que o primeiro!", "");
				return false;
			}
		}
		else if(!VerificadorUtil.verificarSeObjetoNulo(valorGenerico1) && valorGenerico1 < 0) {
			JSFUtil.adicionarMensagemErro("Insira um valor válido para filtrar a quantidade!", "");
			return false;
		}
		return true;
	}

	public void gerarRelatorioPresenca(GerenciarPacienteBean pacienteInstituicao, ProgramaBean programa, GrupoBean grupo, PacienteBean paciente)
			throws IOException, ParseException, ProjetoException, NoSuchAlgorithmException {

		pacienteInstituicao.setPrograma(programa);
		pacienteInstituicao.setGrupo(grupo);

		String caminho = CAMINHO_PRINCIPAL;
		String relatorio = caminho + "pacientespresentes.jasper";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("dt_inicial", dataInicial);
		map.put("dt_final", dataFinal);

		if (!VerificadorUtil.verificarSeObjetoNulo(pacienteInstituicao.getPrograma()))
			map.put("cod_programa", pacienteInstituicao.getPrograma().getIdPrograma());

		if (!VerificadorUtil.verificarSeObjetoNuloOuZero(pacienteInstituicao.getGrupo()))
			map.put("cod_grupo", pacienteInstituicao.getGrupo().getIdGrupo());

		if (this.turnoSelecionado.equals(Turno.MANHA.getSigla())
				|| this.turnoSelecionado.equals(Turno.TARDE.getSigla()))
			map.put("turno", this.turnoSelecionado);

		if (!VerificadorUtil.verificarSeObjetoNulo(paciente) && !VerificadorUtil.verificarSeObjetoNuloOuZero(paciente.getId_paciente()))
			map.put("id_paciente", paciente.getId_paciente());

		map.put("codunidade", user_session.getUnidade().getId());

		ArrayList<Integer> diasSemanaInteger = new ArrayList<Integer>();
		setaDiasSemanaComoListaDeInteiro(diasSemanaInteger);
		map.put("diassemanalista", diasSemanaInteger);

		map.put(SUBREPORT_DIR, this.getServleContext().getRealPath(caminho) + File.separator);
		this.executeReport(relatorio, map, "relatorio_presenca.pdf");

	}

	public void gerarRelatorioPresenca(GerenciarPacienteBean pacienteInstituicao, ProgramaBean programa, GrupoBean grupo)
			throws IOException, ParseException, ProjetoException, NoSuchAlgorithmException {

		pacienteInstituicao.setPrograma(programa);
		pacienteInstituicao.setGrupo(grupo);

		String caminho = CAMINHO_PRINCIPAL;
		String relatorio = "";

		if(!validaValorQuantidade())
			return;

		Map<String, Object> map = new HashMap<String, Object>();

		map.put("dt_inicial", dataInicial);
		map.put("dt_final", dataFinal);

		if (!VerificadorUtil.verificarSeObjetoNulo(pacienteInstituicao.getPrograma()))
			map.put("cod_programa", pacienteInstituicao.getPrograma().getIdPrograma());

		if(!VerificadorUtil.verificarSeObjetoNuloOuZero(this.idSituacaoAtendimento))
			map.put("id_situacao_atendimento", this.idSituacaoAtendimento);

		if(this.turnoSelecionado.equals(Turno.MANHA.getSigla()) || this.turnoSelecionado.equals(Turno.TARDE.getSigla()))
			map.put("turno", this.turnoSelecionado);

		if (!VerificadorUtil.verificarSeObjetoNuloOuZero(paciente))
			map.put("id_paciente", paciente.getId_paciente());

		if (!VerificadorUtil.verificarSeObjetoNuloOuZero(especialidade))
			map.put("codespecialidade", especialidade.getCodEspecialidade());

		map.put("codunidade", user_session.getUnidade().getId());
		ArrayList<Integer> diasSemanaInteger = new ArrayList<Integer>();
		setaDiasSemanaComoListaDeInteiro(diasSemanaInteger);
		map.put("diassemanalista", diasSemanaInteger);

		if (VerificadorUtil.verificarSeObjetoNuloOuZero(idadeMinima))
			map.put("idademinima", 0);
		else
			map.put("idademinima", idadeMinima);

		if (VerificadorUtil.verificarSeObjetoNuloOuZero(idadeMaxima))
			map.put("idademaxima", 200);
		else
			map.put("idademaxima", idadeMaxima);

		if ((!VerificadorUtil.verificarSeObjetoNulo(prof)) && (!VerificadorUtil.verificarSeObjetoNuloOuZero(prof.getId())))
			map.put("codprofissional", this.prof.getId());

		if ((atributoGenerico2!=null) && (atributoGenerico2.equals("EN"))) {
			map.put("qtdatendimentosmenorigual", valorGenerico1);
			map.put("qtdatendimentosmaiorigual", valorGenerico2);
		}
		else if ((atributoGenerico2!=null) &&(atributoGenerico2.equals("MA"))) {
			map.put("qtdatendimentosmaiorigual", valorGenerico1);
		}
		else if ((atributoGenerico2!=null) &&(atributoGenerico2.equals("ME"))) {
			map.put("qtdatendimentosmenorigual", valorGenerico1);
		}

		map.put(SUBREPORT_DIR, this.getServleContext().getRealPath(caminho) + File.separator);

//		if (atributoGenerico1.equalsIgnoreCase(TipoRelatorio.ANALITICO.getSigla())
//				&& atributoGenerico3.equalsIgnoreCase(TipoFiltroRelatorio.GRUPO.getSigla())) {
//			if (!VerificadorUtil.verificarSeObjetoNuloOuZero(pacienteInstituicao.getGrupo()))
//				map.put("cod_grupo", pacienteInstituicao.getGrupo().getIdGrupo());
//			relatorio = caminho + "atendimentosporprogramagrupo.jasper";
//			this.executeReport(relatorio, map, "relatorio_atendimento_analítico.pdf");
//		}
//		else if (atributoGenerico1.equalsIgnoreCase(TipoRelatorio.ANALITICO.getSigla())
//				&& atributoGenerico3.equalsIgnoreCase(TipoFiltroRelatorio.PROGRAMA.getSigla())) {
//			relatorio = caminho + "atendimentosporprograma.jasper";
//			this.executeReport(relatorio, map, "relatorio_atendimento_analítico.pdf");
//		}
		/*else*/ if (atributoGenerico1.equalsIgnoreCase(TipoRelatorio.SINTETICO.getSigla())
				&&  atributoGenerico3.equalsIgnoreCase(TipoFiltroRelatorio.PROGRAMA.getSigla())){
			relatorio = caminho + "presencaporprogramasintetico.jasper";
			this.executeReport(relatorio, map, "relatorio_presenca_sintético.pdf");
		}
		else {
			if (!VerificadorUtil.verificarSeObjetoNuloOuZero(pacienteInstituicao.getGrupo()))
				map.put("cod_grupo", pacienteInstituicao.getGrupo().getIdGrupo());
			relatorio = caminho + "presencaporprogramagruposintetico.jasper";
			this.executeReport(relatorio, map, "relatorio_presenca_sintético.pdf");
		}
	}


	public void atualizaListaMunicipiosDePacientesAtivosSelecionados(MunicipioBean municipioSelecionado) {
		List<Integer> idMunicipios = retornaIdDosMunicipiosSelecionados();

		if(!this.listaMunicipiosDePacienteAtivosSelecionados.isEmpty() && idMunicipios.contains(municipioSelecionado.getId()))
			JSFUtil.adicionarMensagemAdvertencia("O município "+municipioSelecionado.getNome()+" já adicionado", "Atenção");
		else
			this.listaMunicipiosDePacienteAtivosSelecionados.add(municipioSelecionado);
	}

	private List<Integer> retornaIdDosMunicipiosSelecionados() {
		List<Integer> idMunicipios = new ArrayList<Integer>();
		for (MunicipioBean municipioBean : listaMunicipiosDePacienteAtivosSelecionados) {
			idMunicipios.add(municipioBean.getId());
		}
		return idMunicipios;
	}

	public void removerMunicipioSelecionado(MunicipioBean municipio) {
		listaMunicipiosDePacienteAtivosSelecionados.remove(municipio);
	}

	public void gerarPacientesAtivos() throws IOException, ParseException, ProjetoException {
		String caminho = CAMINHO_PRINCIPAL;
		String relatorio = "";
		Map<String, Object> map = new HashMap<String, Object>();

		if (atributoGenerico1.equals("A")) {
			idadeMaxima = 200;
		}
		List<Integer> idMunicipiosSelecionados = retornaIdDosMunicipiosSelecionados();

		if(atributoGenerico3.equals("E")) {
			relatorio = caminho + "pacientes_ativos_por_programa_grupo_equipe.jasper";
			if (!VerificadorUtil.verificarSeObjetoNulo(grupo))
				map.put("codgrupo", grupo.getIdGrupo());
			if (!VerificadorUtil.verificarSeObjetoNulo(equipe))
				map.put("codequipe", equipe.getCodEquipe());
		}
		else if (atributoGenerico3.equals("G")) {
			relatorio = caminho + "pacientes_ativos_por_programa_grupo.jasper";
			if (!VerificadorUtil.verificarSeObjetoNulo(grupo))
				map.put("codgrupo", grupo.getIdGrupo());
		}
		else if (atributoGenerico3.equals("P")) {
			relatorio = caminho + "pacientes_ativos_por_programa.jasper";
		}

		map.put("codunidade", user_session.getUnidade().getId());
		map.put("filtromunicipio", idMunicipiosSelecionados);
		map.put("sexo", this.atributoGenerico2);
		if (!VerificadorUtil.verificarSeObjetoNulo(programa))
			map.put("codprograma", programa.getIdPrograma());

		if (idadeMinima == null)
			map.put("idademinima", 0);
		else
			map.put("idademinima", idadeMinima);

		if (idadeMaxima == null)
			map.put("idademaxima", 200);
		else
			map.put("idademaxima", idadeMaxima);
		ArrayList<Integer> diasSemanaInteger = new ArrayList<Integer>();
		setaDiasSemanaComoListaDeInteiro(diasSemanaInteger);
		map.put("diassemanalista", diasSemanaInteger);

		limparTurno();
		atribuiTurnos();

		if(this.opcaoAtendimento.equals(OpcaoAtendimento.SOMENTE_TURNO.getSigla())) {
			map.put("turnoslista", turnos);
		}
		this.executeReport(relatorio, map, "relatoriopacientesativos.pdf");

	}

	public void limparGrupoEquipe() {
		if(atributoGenerico3.equals("P")) {
			this.grupo = new GrupoBean();
			this.equipe = new EquipeBean();
		}
		else if(atributoGenerico3.equals("P") ||
				atributoGenerico3.equals("G")) {
			this.equipe = new EquipeBean();
		}
	}

	private void setaDiasSemanaComoListaDeInteiro(ArrayList<Integer> diasSemanaInteger) {
		for (String dia : diasSemana) {
			diasSemanaInteger.add(Integer.valueOf(dia));
		}
	}

	private void atribuiTurnos() {
		if(turnoSelecionado.equals(Turno.AMBOS.getSigla())) {
			this.turnos.add(Turno.MANHA.getSigla());
			this.turnos.add(Turno.TARDE.getSigla());
		}
		else if(turnoSelecionado.equals(Turno.MANHA.getSigla()))
			this.turnos.add(Turno.MANHA.getSigla());
		else if(turnoSelecionado.equals(Turno.TARDE.getSigla()))
			this.turnos.add(Turno.TARDE.getSigla());
	}

	private void limparTurno() {
		this.turnos = new ArrayList<String>();
	}

	public void gerarPacientesAtivosSemEvolucao() throws IOException, ParseException, ProjetoException {

		if (atributoGenerico1.equals("A")) {
			idadeMaxima = 200;
		}
		List<Integer> idMunicipiosSelecionados = retornaIdDosMunicipiosSelecionados();
		String caminho = CAMINHO_PRINCIPAL;
		String relatorio = "";
		relatorio = caminho + "pacientes_ativos_sem_evolucao.jasper";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("codunidade", user_session.getUnidade().getId());
		map.put("filtromunicipio", idMunicipiosSelecionados);
		map.put("sexo", this.atributoGenerico2);
		if (programa != null)
			map.put("codprograma", programa.getIdPrograma());

		if (grupo != null)
			map.put("codgrupo", grupo.getIdGrupo());

		if (idadeMinima == null)
			map.put("idademinima", 0);
		else
			map.put("idademinima", idadeMinima);

		if (idadeMaxima == null)
			map.put("idademaxima", 200);
		else
			map.put("idademaxima", idadeMaxima);
		ArrayList<Integer> diasSemanaInteger = new ArrayList<Integer>();
		setaDiasSemanaComoListaDeInteiro(diasSemanaInteger);
		map.put("diassemanalista", diasSemanaInteger);

		limparTurno();
		atribuiTurnos();

		map.put("turnoslista", turnos);
		this.executeReport(relatorio, map, "relatoriopacientessemevolucao.pdf");
	}

	public void gerarPendenciasEvolucaoPorProgramaEGrupo(ArrayList<ProgramaBean> listaProgramasGrupos)
			throws IOException, ParseException, ProjetoException {
		String caminho = CAMINHO_PRINCIPAL;

		String relatorio = retornaTipoDoRelatorioPendenciasEvolucao(caminho);

		ArrayList<Integer> listaProgramas = new ArrayList<>();
		ArrayList<Integer> listaGrupos = new ArrayList<>();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("codunidade", user_session.getUnidade().getId());
		map.put("data_inicio_atendimento", this.dataInicial);
		map.put("data_fim_atendimento", this.dataFinal);
		for (ProgramaBean programaGrupo : listaProgramasGrupos) {
			listaProgramas.add(programaGrupo.getIdPrograma());
			listaGrupos.add(programaGrupo.getGrupoBean().getIdGrupo());

		}

		map.put("codgrupolista", listaProgramas);
		map.put("codgrupolista", listaGrupos);
		this.executeReport(relatorio, map, retornaNomeDoRelatorioPendenciasEvolucaoPeloTipo());

	}

	private String retornaTipoDoRelatorioPendenciasEvolucao(String caminho) {
		if(atributoGenerico1.equalsIgnoreCase("PG"))
			caminho += "pendencias_evolucao_por_programa_grupo.jasper";
		else
			caminho += "pendencias_evolucao_por_profissional_programa_grupo.jasper";
		return caminho;
	}

	private String retornaNomeDoRelatorioPendenciasEvolucaoPeloTipo() {
		String nomeRelatorio;
		if(atributoGenerico1.equalsIgnoreCase("PG"))
			nomeRelatorio = "relatorio_pendencias_evolucao_por_progama.pdf";
		else
			nomeRelatorio = "relatorio_pendencias_evolucao_por_profissional_progama.pdf";
		return nomeRelatorio;
	}

	public void geraPtsApaeMaceio(ProgramaBean programa, GrupoBean grupo)
			throws IOException, ParseException, ProjetoException, NoSuchAlgorithmException {
		{
			String caminho = CAMINHO_PRINCIPAL;
			String relatorio = "";
			relatorio = caminho + "pts_apae_maceio.jasper";
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("codunidade", user_session.getUnidade().getId());
			if (programa != null)
				map.put("codprograma", programa.getIdPrograma());

			if (grupo != null)
				map.put("codgrupo", grupo.getIdGrupo());

			map.put(SUBREPORT_DIR, this.getServleContext().getRealPath(caminho) + File.separator);
			this.executeReport(relatorio, map, "Pts.pdf");

		}
	}

	public void geraPtsPendenciasEspecialidades(ProgramaBean programa, GrupoBean grupo)
			throws IOException, ParseException, ProjetoException, NoSuchAlgorithmException {
		{
			String caminho = CAMINHO_PRINCIPAL;
			String relatorio = "";
			relatorio = caminho + "pts_pendentes_por_especialidade.jasper";
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("codunidade", user_session.getUnidade().getId());
			if (programa != null)
				map.put("codprograma", programa.getIdPrograma());

			if (grupo != null)
				map.put("codgrupo", grupo.getIdGrupo());

			this.executeReport(relatorio, map, "Pts_Especialidades_Pendentes.pdf");

		}
	}

	public void geraPtsPacientesFaltando(ProgramaBean programa, GrupoBean grupo)
			throws IOException, ParseException, ProjetoException, NoSuchAlgorithmException {
		{
			String caminho = CAMINHO_PRINCIPAL;
			String relatorio = "";
			relatorio = caminho + "pts_pacientes_faltando.jasper";
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("codunidade", user_session.getUnidade().getId());
			if (programa != null)
				map.put("codprograma", programa.getIdPrograma());

			if (grupo != null)
				map.put("codgrupo", grupo.getIdGrupo());

			this.executeReport(relatorio, map, "Pts_Pacientes_Faltando.pdf");

		}
	}
	
	public void imprimirPts(Integer idPts) throws IOException, ParseException, ProjetoException {

		String relatorio = "";
		relatorio = CAMINHO_PRINCIPAL + "pts.jasper";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id_pts", idPts);
		map.put(SUBREPORT_DIR, this.getServleContext().getRealPath(CAMINHO_PRINCIPAL) + File.separator);
		this.executeReportNewTab(relatorio, "pts.pdf", map);
	}

	public void gerarRelatorioEncaminhamentoOrteseProtese(OrteseProtese orteseProtese)
			throws IOException, ParseException, ProjetoException, NoSuchAlgorithmException{
		String caminho = CAMINHO_PRINCIPAL;
		String relatorio = caminho + "encaminhamentoopm.jasper";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("codunidade", user_session.getUnidade().getId());
		map.put("id_encaminhamento", orteseProtese.getIdEncaminhamento());
		map.put(SUBREPORT_DIR, this.getServleContext().getRealPath(caminho) + File.separator);
		this.executeReport(relatorio, map, "encaminhamento_ortese_protese.pdf");
	}

	public void gerarRelatorioTermoCompromissoOrteseProtese(OrteseProtese orteseProtese)
			throws IOException, ParseException, ProjetoException, NoSuchAlgorithmException{
		String caminho = CAMINHO_PRINCIPAL;
		String relatorio = caminho + "termocompromisso_opm.jasper";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("codunidade", user_session.getUnidade().getId());
		map.put("opm_id", orteseProtese.getId());
		this.executeReport(relatorio, map, "termo_de_compromisso_ortese_protese.pdf");
	}

	public void gerarMapaLaudoOrteseProtese() throws IOException, ParseException, ProjetoException {

		if (this.dataFinal == null || this.dataInicial == null || this.programa == null || this.grupo == null) {
			JSFUtil.adicionarMensagemErro("Todos os campos devem ser preenchidos.", "Campos inválidos!");
			return;
		}

		if (!verificarMesesIguais(this.dataInicial, this.dataFinal)) {
			JSFUtil.adicionarMensagemErro("As datas devem possuir o mesmo mês.", "Datas Inválidas!");
			return;
		}

		String caminho = CAMINHO_PRINCIPAL;
		String relatorio = "";
		Map<String, Object> map = new HashMap<String, Object>();
		if (this.grupo.isAuditivo()) {
			relatorio = caminho + "mapaLaudoOrteseProteseAuditivo.jasper";
			map.put("dt_inicial", this.dataInicial);
			map.put("dt_final", this.dataFinal);
			map.put("cod_programa", this.programa.getIdPrograma());
			map.put("cod_grupo", this.grupo.getIdGrupo());
			map.put("recurso", this.recurso);
			map.put("tipo_exame_auditivo", this.tipoExameAuditivo);
		} else {
			relatorio = caminho + "mapaLaudoOrteseProteseNormal.jasper";
			map.put("dt_inicial", this.dataInicial);
			map.put("dt_final", this.dataFinal);
			map.put("cod_programa", this.programa.getIdPrograma());
			map.put("cod_grupo", this.grupo.getIdGrupo());
			map.put("recurso", this.recurso);
		}

		map.put(SUBREPORT_DIR, this.getServleContext().getRealPath(caminho) + File.separator);
		this.executeReport(relatorio, map, "relatorio.pdf");
// limparDados();
	}

	public void gerarMapaLaudo(Integer idPrograma) throws IOException, ParseException, ProjetoException {
		/*
		 * if (!verificarMesesIguais(this.dataInicial, this.dataFinal)) { FacesMessage
		 * msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
		 * "As datas devem possuir o mesmo mês.", "Datas Inválidas!");
		 * FacesContext.getCurrentInstance().addMessage(null, msg); return; }
		 */
		final Boolean INICIO_MES = true;
		final Boolean FIM_MES = false;

		dataInicial = DataUtil.adicionarMesIhAnoEmDate(mes, ano, INICIO_MES);
		dataFinal = DataUtil.adicionarMesIhAnoEmDate(mes, ano, FIM_MES);

		String caminho = CAMINHO_PRINCIPAL;
		String relatorio = "";
		Map<String, Object> map = new HashMap<String, Object>();
		relatorio = caminho + "mapalaudo.jasper";
		map.put("dt_inicial", this.dataInicial);
		map.put("dt_final", this.dataFinal);
		map.put("codunidade", user_session.getUnidade().getId());
		map.put("id_programa", idPrograma);
		if(!VerificadorUtil.verificarSeObjetoNulo(procedimento)
				&& !VerificadorUtil.verificarSeObjetoNuloOuZero(procedimento.getIdProc())) {
			map.put("id_procedimento", procedimento.getIdProc());
		}
		
		this.executeReport(relatorio, map, "mapalaudo.pdf");
// limparDados();
	}

	public void gerarFinanceiroOrteseProtese() throws IOException, ParseException, ProjetoException {

		if (this.dataFinal == null || this.dataInicial == null || this.programa == null || this.grupo == null) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Todos os campos devem ser preenchidos.",
					"Campos inválidos!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return;
		}

		if (!verificarMesesIguais(this.dataInicial, this.dataFinal)) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "As datas devem possuir o mesmo mês.",
					"Datas InvÃ¡lidas!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return;
		}

		String caminho = CAMINHO_PRINCIPAL;
		String relatorio = "";
		Map<String, Object> map = new HashMap<String, Object>();
		if (this.grupo.isAuditivo()) {
			relatorio = caminho + "mapaFinanceiroOrteseProtese.jasper";
			map.put("img_adefal", this.getServleContext().getRealPath("/WEB-INF/relatorios/adefal.png"));
			map.put("dt_inicial", this.dataInicial);
			map.put("dt_final", this.dataFinal);
			map.put("cod_programa", this.programa.getIdPrograma());
			map.put("cod_grupo", this.grupo.getIdGrupo());
			map.put("recurso", this.recurso);
			map.put("tipo_exame_auditivo", this.tipoExameAuditivo);

		} else {
			relatorio = caminho + "mapaFinanceiroOrteseProtese.jasper";
			map.put("img_adefal", this.getServleContext().getRealPath("/WEB-INF/relatorios/adefal.png"));
			map.put("dt_inicial", this.dataInicial);
			map.put("dt_final", this.dataFinal);
			map.put("cod_programa", this.programa.getIdPrograma());
			map.put("cod_grupo", this.grupo.getIdGrupo());
			map.put("recurso", this.recurso);
		}

		map.put(SUBREPORT_DIR, this.getServleContext().getRealPath(caminho) + File.separator);
		this.executeReport(relatorio, map, "relatorio.pdf");
		// limparDados();
	}
	
	public void preparaRelatorioAgendamentosPorProfissional() {
		this.atributoGenerico1 = "T";
	}

	public void gerarAgendamentosPorProfissional() throws IOException, ParseException, ProjetoException {

		if (this.dataFinal == null || this.dataInicial == null) {
			JSFUtil.adicionarMensagemErro("Informe o Período Inicial e Final do Agendamento.", "Campos inválidos!");
			return;
		}



		String caminho = CAMINHO_PRINCIPAL;
		String relatorio = caminho + "agendamentosPorProfissional.jasper";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("dt_inicial", this.dataInicial);
		map.put("dt_final", this.dataFinal);
		if ((prof != null) && (prof.getId() != null))
			map.put("cod_profissional", this.prof.getId());
		if ((tipoAtendimento != null) && (tipoAtendimento.getIdTipo() != null))
			map.put("cod_tipo_atend", this.tipoAtendimento.getIdTipo());
		map.put("codunidade", user_session.getUnidade().getId());
		if ((programa != null) && (programa.getIdPrograma() != null))
			map.put("codprograma", this.programa.getIdPrograma());
		if ((grupo != null) && (grupo.getIdGrupo() != null))
			map.put("codgrupo", this.grupo.getIdGrupo());
		
		if (!atributoGenerico1.equals("T")) {
			map.put("presenca", atributoGenerico1);
		}
		
		this.executeReport(relatorio, map, "agendamentos_profissional.pdf");
		// limparDados();
	}

	public void gerarAtendimentosPorProfissional() throws IOException, ParseException, ProjetoException {

		if (this.dataFinal == null || this.dataInicial == null || this.programa == null) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Datas e programa devem ser preenchidos.",
					"Campos inválidos!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return;
		}

		String caminho = CAMINHO_PRINCIPAL;
		String relatorio = caminho + "atendPorProfissional.jasper";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("img_adefal", this.getServleContext().getRealPath("/WEB-INF/relatorios/adefal.png"));
		map.put("dt_inicial", this.dataInicial);
		map.put("dt_final", this.dataFinal);
		map.put("cod_programa", this.programa.getIdPrograma());
		if (this.prof == null) {
			map.put("cod_medico", null);
		} else {
			map.put("cod_medico", this.prof.getId());
		}
		map.put(SUBREPORT_DIR, this.getServleContext().getRealPath(caminho) + File.separator);

		this.executeReport(relatorio, map, "relatorio.pdf");
		// limparDados();
	}

	public void gerarAtendimentosPorPrograma() throws IOException, ParseException, ProjetoException {

		if (this.dataFinal == null || this.dataInicial == null || this.programa == null) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Datas e programa devem ser preenchidos.",
					"Campos inválidos!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return;
		}

		String caminho = CAMINHO_PRINCIPAL;
		String relatorio = caminho + "atendimentosPorPrograma.jasper";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("dt_inicial", this.dataInicial);
		map.put("dt_final", this.dataFinal);
		map.put("cod_programa", this.programa.getIdPrograma());
		if (this.grupo == null) {
			map.put("cod_grupo", null);
		} else {
			map.put("cod_grupo", this.grupo.getIdGrupo());
		}
		if (this.situacao.equals("T")) {
			map.put("situacao", null);
		} else {
			map.put("situacao", this.situacao);
		}
		map.put(SUBREPORT_DIR, this.getServleContext().getRealPath(caminho) + File.separator);

		this.executeReport(relatorio, map, "relatorio.pdf");
		// limparDados();
	}

	public void gerarAtendimentosPorProcedimento() throws IOException, ParseException, ProjetoException {

		if (this.dataFinal == null || this.dataInicial == null) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Datas e programa devem ser preenchidos.",
					"Campos inválidos!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return;
		}

		String caminho = CAMINHO_PRINCIPAL;
		String relatorio = "";
		if (this.tipoAnalSint.equals("A")) {
			relatorio = caminho + "atendPorProcedimentosAnalitico.jasper";
		} else if (this.tipoAnalSint.equals("S")) {
			relatorio = caminho + "atendPorProcedimentosSintetico.jasper";
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("dt_inicial", this.dataInicial);
		map.put("dt_final", this.dataFinal);
		map.put(SUBREPORT_DIR, this.getServleContext().getRealPath(caminho) + File.separator);

		this.executeReport(relatorio, map, "relatorio.pdf");
		// limparDados();
	}

	public void gerarAtendimentosPorProcedimentoGrupo() throws IOException, ParseException, ProjetoException {
		if (this.dataInicial == null || this.dataFinal == null || this.programa == null) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Datas e programa devem ser preenchidos.",
					"Campos inválidos!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		} else {

			String caminho = CAMINHO_PRINCIPAL;
			String relatorio = "";
			if (this.tipoAnalSint.equals("A")) {
				relatorio = caminho + "atendPorProcedimentosGrupoAnalitico.jasper";
			} else if (this.tipoAnalSint.equals("S")) {
				relatorio = caminho + "atendPorProcedimentosGrupoSintetico.jasper";
			}
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("dt_inicial", this.dataInicial);
			map.put("dt_final", this.dataFinal);
			map.put("cod_programa", this.programa.getIdPrograma());

			if (this.listaGrupos.isEmpty()) {
				map.put("cod_grupo", null);
			} else {
				map.put("cod_grupo", strIdGrupos());
			}

			map.put(SUBREPORT_DIR, this.getServleContext().getRealPath(caminho) + File.separator);

			this.executeReport(relatorio, map, "relatorio.pdf");
			// limparDados();
		}
	}

	public void gerarAtendimentosPorProcedimentoTipoAt() throws IOException, ParseException, ProjetoException {
		if (this.dataInicial == null || this.dataFinal == null || this.programa == null || this.grupo == null) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Datas, programa e grupo devem ser preenchidos.", "Campos inválidos!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		} else {

			String caminho = CAMINHO_PRINCIPAL;
			String relatorio = "";
			if (this.tipoAnalSint.equals("A")) {
				relatorio = caminho + "atendPorProcedimentosTipoAtAnalitico.jasper";
			} else if (this.tipoAnalSint.equals("S")) {
				relatorio = caminho + "atendPorProcedimentosTipoAtSintetico.jasper";
			}
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("dt_inicial", this.dataInicial);
			map.put("dt_final", this.dataFinal);
			map.put("cod_programa", this.programa.getIdPrograma());
			map.put("cod_grupo", this.grupo.getIdGrupo());

			if (this.listaTipos.isEmpty()) {
				map.put("cod_tipoatendimento", null);
			} else {
				map.put("cod_tipoatendimento", strIdTipos());
			}

			map.put(SUBREPORT_DIR, this.getServleContext().getRealPath(caminho) + File.separator);

			this.executeReport(relatorio, map, "relatorio.pdf");
			// limparDados();
		}
	}

	public void gerarConfigAgendaProfissional() throws IOException, ParseException, ProjetoException {

		if (this.grupo == null || this.programa == null) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Programa e Grupo devem ser preenchidos.",
					"Campos inválidos!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return;
		}

		Map<String, Object> map = new HashMap<String, Object>();
		String caminho = CAMINHO_PRINCIPAL;
		String relatorio = "";
		if (this.dataDia.equals("DS")) {
			if (this.mes == null || this.ano == null) {
				FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Mês e Ano devem ser preenchidos.",
						"Campos inválidos!");
				FacesContext.getCurrentInstance().addMessage(null, msg);
				return;
			}
			relatorio = caminho + "configAgenda-DS.jasper";
			if (this.diaSemana == 0)
				map.put("dia_semana", null);
			else
				map.put("dia_semana", this.diaSemana);
			map.put("mes", this.mes);
			map.put("ano", this.ano);
		} else {
			if (this.dataEspec == null) {
				FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Data especÃ­fica deve ser preenchida.", "Campo invÃ¡lido!");
				FacesContext.getCurrentInstance().addMessage(null, msg);
				return;
			}
			relatorio = caminho + "configAgenda-DE.jasper";
			map.put("dt_espec", this.dataEspec);
		}
		map.put("cod_programa", this.programa.getIdPrograma());
		map.put("cod_grupo", this.grupo.getIdGrupo());

		if (this.prof == null)
			map.put("cod_medico", null);
		else
			map.put("cod_medico", this.prof.getId());

		map.put(SUBREPORT_DIR, this.getServleContext().getRealPath(caminho) + File.separator);
		this.executeReport(relatorio, map, "relatorio.pdf");
		limparDados();
	}

	public void executeReportNewTab(String caminhoRelatorio, String nomeRelatorio, Map parametros)
			throws ProjetoException {

		Connection conexao = null;
		ServletOutputStream outputStream = null;
		FacesContext context = FacesContext.getCurrentInstance();

		HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
		InputStream relatorioStream = context.getExternalContext().getResourceAsStream(caminhoRelatorio);

		try {
			conexao = ConnectionFactory.getConnection();
			JasperPrint print = JasperFillManager.fillReport(relatorioStream, parametros, conexao);

			JRExporter exportador = new JRPdfExporter();
			exportador.setParameter(JRExporterParameter.OUTPUT_STREAM, response.getOutputStream());
			exportador.setParameter(JRExporterParameter.JASPER_PRINT, print);

			response.setContentType("application/pdf");
			response.setHeader("Content-disposition", "inline; filename=arquivo.pdf");
			ServletOutputStream servletOutputStream = response.getOutputStream();

			exportador.exportReport();
			servletOutputStream.flush();
			servletOutputStream.close();
			context.renderResponse();
			context.responseComplete();
		} catch (JRException ex) {
			throw new RuntimeException(ex);
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		} finally {
			try {
				conexao.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				//comentado walter erro log ex.printStackTrace();
			}
		}

	}

	private void executeReport(String relatorio, Map<String, Object> map, String filename)
			throws IOException, ParseException, ProjetoException {
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
		InputStream reportStream = context.getExternalContext().getResourceAsStream(relatorio);

		response.setContentType("application/pdf");
		response.setHeader("Content-disposition", "attachment;filename=" + filename);

		ServletOutputStream servletOutputStream = response.getOutputStream();

		try {
			Connection connection = ConnectionFactory.getConnection();

			JasperRunManager.runReportToPdfStream(reportStream, response.getOutputStream(), map, connection);

			if (connection != null)
				connection.close();
		} catch (JRException e) {
			//comentado walter erro log ex.printStackTrace();
		} catch (SQLException e) {
			//comentado walter erro log ex.printStackTrace();
		}
		this.getFacesContext().responseComplete();
		servletOutputStream.flush();
		servletOutputStream.close();
	}

	public void gerarFaltasPacientes() throws IOException, ParseException, ProjetoException {

		if (this.dataFinal == null || this.dataInicial == null ) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Informe o período de Atendimento",
					"Campos inválidos!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return;
		}

		String caminho = CAMINHO_PRINCIPAL;
		String relatorio = caminho + "faltaspacientes.jasper";

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("codunidade", user_session.getUnidade().getId());
		map.put("dt_inicial", this.dataInicial);
		map.put("dt_final", this.dataFinal);
		if (programa != null)
			map.put("codprograma", programa.getIdPrograma());

		if (grupo != null)
			map.put("codgrupo", grupo.getIdGrupo());

		if (paciente != null)
			map.put("id_paciente", paciente.getId_paciente());

		if(this.turnoSelecionado.equals(Turno.MANHA.getSigla()) || this.turnoSelecionado.equals(Turno.TARDE.getSigla()))
			map.put("turno", this.turnoSelecionado);

		this.executeReport(relatorio, map, "faltaspacientes.pdf");
		limparDados();
	}

	public void addGrupoLst() {
		this.listaGrupos.add(this.grupo);
	}

	public void rmvGrupoLst() {
		this.listaGrupos.remove(this.grupo);
	}

	public void addTipoLst() {
		this.listaTipos.add(this.tipoAtendimento);
	}

	public void rmvTipoLst() {
		this.listaTipos.remove(this.tipoAtendimento);
	}

	public String strIdGrupos() {
		int si = this.listaGrupos.size();
		int i = 1;
		String str = new String();
		for (GrupoBean grupo : listaGrupos) {

			if (i < si) {
				str += String.valueOf(grupo.getIdGrupo()) + ", ";
			} else {
				str += String.valueOf(grupo.getIdGrupo());
			}
			i++;
		}
		return str;
	}

	public String strIdTipos() {
		int si = this.listaTipos.size();
		int i = 1;
		String str = new String();
		for (TipoAtendimentoBean tipos : listaTipos) {

			if (i < si) {
				str += String.valueOf(tipos.getIdTipo()) + ", ";
			} else {
				str += String.valueOf(tipos.getIdTipo());
			}
			i++;
		}
		return str;
	}

	public void gerarAgendamentosPorEquipe() throws IOException, ParseException, ProjetoException {

		String caminho = CAMINHO_PRINCIPAL;
		String relatorio = "";
		Map<String, Object> map = new HashMap<String, Object>();

		relatorio = caminho + "agendamentosEquipe.jasper";
		map.put("dt_inicial", this.dataInicial);
		map.put("dt_final", this.dataFinal);
		if ((tipoAtendimento != null) && (tipoAtendimento.getIdTipo() != null))
			map.put("cod_tipo_atend", this.tipoAtendimento.getIdTipo());
		if ((equipe != null) && (equipe.getCodEquipe() != null))
			map.put("cod_equipe", this.equipe.getCodEquipe());
		map.put("codunidade", user_session.getUnidade().getId());
		if ((programa != null) && (programa.getIdPrograma() != null))
			map.put("codprograma", this.programa.getIdPrograma());
		if ((grupo != null) && (grupo.getIdGrupo() != null))
			map.put("codgrupo", this.grupo.getIdGrupo());

		this.executeReport(relatorio, map, "Agendamentos por Equipe.pdf");
	}

	public void adicionaDiasDaSemanaPadrao() {
		this.diasSemana = new ArrayList<String>();
		this.diasSemana.add("0");
		this.diasSemana.add("1");
		this.diasSemana.add("2");
		this.diasSemana.add("3");
		this.diasSemana.add("4");
		this.diasSemana.add("5");
		this.diasSemana.add("6");

	}

	public void adicionaDiasDaSemanaPadraoFiltroTipoInteiro() {
		this.diasSemana = new ArrayList<String>();
		this.diasSemana.add("1");
		this.diasSemana.add("2");
		this.diasSemana.add("3");
		this.diasSemana.add("4");
		this.diasSemana.add("5");
		this.diasSemana.add("6");
		this.diasSemana.add("7");

	}

	public void setaTurnoPadrao() {
		this.turnoSelecionado = Turno.AMBOS.getSigla();
	}

	public void gerarRelatorioEvolucao(GerenciarPacienteBean pacienteInstituicao, ProgramaBean programa, GrupoBean grupo)
			throws IOException, ParseException, ProjetoException, NoSuchAlgorithmException {

		pacienteInstituicao.setPrograma(programa);
		pacienteInstituicao.setGrupo(grupo);

		String caminho = CAMINHO_PRINCIPAL;
		String relatorio = "";

		Map<String, Object> map = new HashMap<String, Object>();

		map.put("dt_inicial", dataInicial);
		map.put("dt_final", dataFinal);
		map.put("codunidade", user_session.getUnidade().getId());

		if (!VerificadorUtil.verificarSeObjetoNulo(pacienteInstituicao.getPrograma()))
			map.put("cod_programa", pacienteInstituicao.getPrograma().getIdPrograma());

		if (!VerificadorUtil.verificarSeObjetoNuloOuZero(paciente))
			map.put("id_paciente", paciente.getId_paciente());

		map.put(SUBREPORT_DIR, this.getServleContext().getRealPath(caminho) + File.separator);

		if (atributoGenerico3.equalsIgnoreCase(TipoFiltroRelatorio.GRUPO.getSigla())) {
			if (!VerificadorUtil.verificarSeObjetoNuloOuZero(pacienteInstituicao.getGrupo()))
				map.put("cod_grupo", pacienteInstituicao.getGrupo().getIdGrupo());
		}

		relatorio = caminho + "evolucaoatendimentos.jasper";
		this.executeReport(relatorio, map, "relatorio_evolucao_atendimentos.pdf");
	}
	
	public void gerarRelatorioDesligamento() throws IOException, ParseException, ProjetoException {


		String caminho = CAMINHO_PRINCIPAL;
		String relatorio = "";
		relatorio = caminho + "desligamento.jasper";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("codunidade", user_session.getUnidade().getId());
		map.put("dt_inicial", dataInicial);
		map.put("dt_final", dataFinal);
		if (valorGenerico1 != null)
		map.put("idmotivodesligamento", valorGenerico1);
		
		if (programa != null)
			map.put("codprograma", programa.getIdPrograma());

		if (grupo != null)
			map.put("codgrupo", grupo.getIdGrupo());

		if (equipe != null)
			map.put("codequipe", equipe.getCodEquipe());

		this.executeReport(relatorio, map, "relatoriodesligamentopacientes.pdf");
	}

	private FacesContext getFacesContext() {
		FacesContext context = FacesContext.getCurrentInstance();
		return context;
	}

	private ServletContext getServleContext() {
		ServletContext scontext = (ServletContext) this.getFacesContext().getExternalContext().getContext();
		return scontext;
	}

	public ProgramaBean getPrograma() {
		return programa;
	}

	public void setPrograma(ProgramaBean programa) {
		this.programa = programa;
	}

	public GrupoBean getGrupo() {
		return grupo;
	}

	public void setGrupo(GrupoBean grupo) {
		this.grupo = grupo;
	}

	public Date getDataInicial() {
		return dataInicial;
	}

	public void setDataInicial(Date dataInicial) {
		this.dataInicial = dataInicial;
	}

	public Date getDataFinal() {
		return dataFinal;
	}

	public void setDataFinal(Date dataFinal) {
		this.dataFinal = dataFinal;
	}

	public String getRecurso() {
		return recurso;
	}

	public void setRecurso(String recurso) {
		this.recurso = recurso;
	}

	public String getTipoExameAuditivo() {
		return tipoExameAuditivo;
	}

	public void setTipoExameAuditivo(String tipoExameAuditivo) {
		this.tipoExameAuditivo = tipoExameAuditivo;
	}

	public FuncionarioBean getProf() {
		return prof;
	}

	public void setProf(FuncionarioBean prof) {
		this.prof = prof;
	}

	public String getSituacao() {
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	public String getTipoAnalSint() {
		return tipoAnalSint;
	}

	public void setTipoAnalSint(String tipoAnalSint) {
		this.tipoAnalSint = tipoAnalSint;
	}

	public String getDataDia() {
		return dataDia;
	}

	public void setDataDia(String dataDia) {
		this.dataDia = dataDia;
	}

	public Date getDataEspec() {
		return dataEspec;
	}

	public Integer getDiaSemana() {
		return diaSemana;
	}

	public Integer getMes() {
		return mes;
	}

	public Integer getAno() {
		return ano;
	}

	public void setDataEspec(Date dataEspec) {
		this.dataEspec = dataEspec;
	}

	public void setDiaSemana(Integer diaSemana) {
		this.diaSemana = diaSemana;
	}

	public void setMes(Integer mes) {
		this.mes = mes;
	}

	public void setAno(Integer ano) {
		this.ano = ano;
	}

	public List<GrupoBean> getListaGrupos() {
		return listaGrupos;
	}

	public void setListaGrupos(List<GrupoBean> listaGrupos) {
		this.listaGrupos = listaGrupos;
	}

	public List<TipoAtendimentoBean> getListaTipos() {
		return listaTipos;
	}

	public void setListaTipos(List<TipoAtendimentoBean> listaTipos) {
		this.listaTipos = listaTipos;
	}

	public String getAtributoGenerico1() {
		return atributoGenerico1;
	}

	public void setAtributoGenerico1(String atributoGenerico1) {
		this.atributoGenerico1 = atributoGenerico1;
	}

	public String getAtributoGenerico2() {
		return atributoGenerico2;
	}

	public void setAtributoGenerico2(String atributoGenerico2) {
		this.atributoGenerico2 = atributoGenerico2;
	}

	public ProcedimentoBean getProcedimento() {
		return procedimento;
	}

	public void setProcedimento(ProcedimentoBean procedimento) {
		this.procedimento = procedimento;
	}

	public PacienteBean getPaciente() {
		return paciente;
	}

	public GerenciarPacienteBean getPacienteInstituicao() {
		return pacienteInstituicao;
	}

	public void setPaciente(PacienteBean paciente) {
		this.paciente = paciente;
	}

	public void setPacienteInstituicao(GerenciarPacienteBean pacienteInstituicao) {
		this.pacienteInstituicao = pacienteInstituicao;
	}

	public UnidadeBean getUnidade() {
		return unidade;
	}

	public void setUnidade(UnidadeBean unidade) {
		this.unidade = unidade;
	}

	public List<GrupoBean> getListaGruposProgramas() {
		return listaGruposProgramas;
	}

	public void setListaGruposProgramas(List<GrupoBean> listaGruposProgramas) {
		this.listaGruposProgramas = listaGruposProgramas;
	}

	public EquipeBean getEquipe() {
		return equipe;
	}

	public void setEquipe(EquipeBean equipe) {
		this.equipe = equipe;
	}

	public List<EquipeBean> getListaEquipePorTipoAtendimento() {
		return listaEquipePorTipoAtendimento;
	}

	public void setListaEquipePorTipoAtendimento(List<EquipeBean> listaEquipePorTipoAtendimento) {
		this.listaEquipePorTipoAtendimento = listaEquipePorTipoAtendimento;
	}

	public Integer getIdadeMinima() {
		return idadeMinima;
	}

	public void setIdadeMinima(Integer idadeMinima) {
		this.idadeMinima = idadeMinima;
	}

	public Integer getIdadeMaxima() {
		return idadeMaxima;
	}

	public void setIdadeMaxima(Integer idadeMaxima) {
		this.idadeMaxima = idadeMaxima;
	}

	public AgendaController getAgendaController() {
		return agendaController;
	}

	public void setAgendaController(AgendaController agendaController) {
		this.agendaController = agendaController;
	}

	public TipoAtendimentoBean getTipoAtendimento() {
		return tipoAtendimento;
	}

	public void setTipoAtendimento(TipoAtendimentoBean tipoAtendimento) {
		this.tipoAtendimento = tipoAtendimento;
	}

	public ArrayList<String> getDiasSemana() {
		return diasSemana;
	}

	public void setDiasSemana(ArrayList<String> diasSemana) {
		this.diasSemana = diasSemana;
	}

	public String getTurnoSelecionado() {
		return turnoSelecionado;
	}

	public void setTurnoSelecionado(String turnoSelecionado) {
		this.turnoSelecionado = turnoSelecionado;
	}
	public Boolean getFiltrarPorMunicipio() {
		return filtrarPorMunicipio;
	}

	public void setFiltrarPorMunicipio(Boolean filtrarPorMunicipio) {
		this.filtrarPorMunicipio = filtrarPorMunicipio;
	}

	public Integer getIdSituacaoAtendimento() {
		return idSituacaoAtendimento;
	}

	public void setIdSituacaoAtendimento(Integer idSituacaoAtendimento) {
		this.idSituacaoAtendimento = idSituacaoAtendimento;
	}

	public List<MunicipioBean> getListaMunicipiosDePacienteAtivosSelecionados() {
		return listaMunicipiosDePacienteAtivosSelecionados;
	}

	public void setListaMunicipiosDePacienteAtivosSelecionados(
			List<MunicipioBean> listaMunicipiosDePacienteAtivosSelecionados) {
		this.listaMunicipiosDePacienteAtivosSelecionados = listaMunicipiosDePacienteAtivosSelecionados;
	}

	public List<Integer> getListaAnos() {
		return listaAnos;
	}

	public void setListaAnos(List<Integer> listaAnos) {
		this.listaAnos = listaAnos;
	}

	public String getAtributoGenerico3() {
		return atributoGenerico3;
	}

	public void setAtributoGenerico3(String atributoGenerico3) {
		this.atributoGenerico3 = atributoGenerico3;
	}

	public String getAtributoGenerico4() {
		return atributoGenerico4;
	}

	public void setAtributoGenerico4(String atributoGenerico4) {
		this.atributoGenerico4 = atributoGenerico4;
	}

	public EspecialidadeBean getEspecialidade() {
		return especialidade;
	}

	public void setEspecialidade(EspecialidadeBean especialidade) {
		this.especialidade = especialidade;
	}

	public List<EquipeBean> getListaEquipe() {
		return listaEquipe;
	}

	public void setListaEquipe(List<EquipeBean> listaEquipe) {
		this.listaEquipe = listaEquipe;
	}

	public Integer getValorGenerico1() {
		return valorGenerico1;
	}

	public void setValorGenerico1(Integer valorGenerico1) {
		this.valorGenerico1 = valorGenerico1;
	}

	public Integer getValorGenerico2() {
		return valorGenerico2;
	}

	public void setValorGenerico2(Integer valorGenerico2) {
		this.valorGenerico2 = valorGenerico2;
	}

	public boolean isFiltrarPorQuantidade() {
		return filtrarPorQuantidade;
	}

	public void setFiltrarPorQuantidade(boolean filtrarPorQuantidade) {
		this.filtrarPorQuantidade = filtrarPorQuantidade;
	}

	public List<UnidadeBean> getListaUnidades() {
		return listaUnidades;
	}

	public void setListaUnidades(List<UnidadeBean> listaUnidades) {
		this.listaUnidades = listaUnidades;
	}

	public List<ProgramaBean> getListaPrograma() {
		return listaPrograma;
	}

	public void setListaPrograma(List<ProgramaBean> listaPrograma) {
		this.listaPrograma = listaPrograma;
	}

	public List<UnidadeBean> getListaUnidadesSelecionadas() {
		return listaUnidadesSelecionadas;
	}

	public void setListaUnidadesSelecionadas(List<UnidadeBean> listaUnidadesSelecionadas) {
		this.listaUnidadesSelecionadas = listaUnidadesSelecionadas;
	}

	public List<ProgramaBean> getListaProgramaSelecionados() {
		return listaProgramaSelecionados;
	}

	public void setListaProgramaSelecionados(List<ProgramaBean> listaProgramaSelecionados) {
		this.listaProgramaSelecionados = listaProgramaSelecionados;
	}

	public List<GrupoProgramaUnidadeDTO> getListaGruposProgramaUnidadeDTO() {
		return listaGruposProgramaUnidadeDTO;
	}

	public void setListaGruposProgramaUnidadeDTO(List<GrupoProgramaUnidadeDTO> listaGruposProgramaUnidadeDTO) {
		this.listaGruposProgramaUnidadeDTO = listaGruposProgramaUnidadeDTO;
	}

	public List<GrupoProgramaUnidadeDTO> getListaGruposProgramaUnidadeDTOSelecionados() {
		return listaGruposProgramaUnidadeDTOSelecionados;
	}

	public void setListaGruposProgramaUnidadeDTOSelecionados(
			List<GrupoProgramaUnidadeDTO> listaGruposProgramaUnidadeDTOSelecionados) {
		this.listaGruposProgramaUnidadeDTOSelecionados = listaGruposProgramaUnidadeDTOSelecionados;
	}

	public boolean isAgruparPorUnidade() {
		return agruparPorUnidade;
	}

	public void setAgruparPorUnidade(boolean agruparPorUnidade) {
		this.agruparPorUnidade = agruparPorUnidade;
	}

	public List<EquipeGrupoProgramaUnidadeDTO> getListaEquipeGruposProgramaUnidadeDTOSelecionados() {
		return listaEquipeGruposProgramaUnidadeDTOSelecionados;
	}

	public void setListaEquipeGruposProgramaUnidadeDTOSelecionados(
			List<EquipeGrupoProgramaUnidadeDTO> listaEquipeGruposProgramaUnidadeDTOSelecionados) {
		this.listaEquipeGruposProgramaUnidadeDTOSelecionados = listaEquipeGruposProgramaUnidadeDTOSelecionados;
	}

	public List<EquipeGrupoProgramaUnidadeDTO> getListaEquipeGruposProgramaUnidadeDTO() {
		return listaEquipeGruposProgramaUnidadeDTO;
	}

	public void setListaEquipeGruposProgramaUnidadeDTO(
			List<EquipeGrupoProgramaUnidadeDTO> listaEquipeGruposProgramaUnidadeDTO) {
		this.listaEquipeGruposProgramaUnidadeDTO = listaEquipeGruposProgramaUnidadeDTO;
	}

	public boolean isAgruparEquipeDia() {
		return agruparEquipeDia;
	}

	public void setAgruparEquipeDia(boolean agruparEquipeDia) {
		this.agruparEquipeDia = agruparEquipeDia;
	}

	public String getOpcaoAtendimento() {
		return opcaoAtendimento;
	}

	public void setOpcaoAtendimento(String opcaoAtendimento) {
		this.opcaoAtendimento = opcaoAtendimento;
	}
	
}
