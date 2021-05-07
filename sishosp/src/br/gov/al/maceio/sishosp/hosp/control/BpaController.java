package br.gov.al.maceio.sishosp.hosp.control;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

import br.gov.al.maceio.sishosp.hosp.model.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import br.gov.al.maceio.sishosp.acl.dao.FuncionarioDAO;
import br.gov.al.maceio.sishosp.acl.model.FuncionarioBean;
import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.JSFUtil;
import br.gov.al.maceio.sishosp.comum.util.VerificadorUtil;
import br.gov.al.maceio.sishosp.hosp.dao.AgendaDAO;
import br.gov.al.maceio.sishosp.hosp.dao.AtendimentoDAO;
import br.gov.al.maceio.sishosp.hosp.dao.BpaConsolidadoDAO;
import br.gov.al.maceio.sishosp.hosp.dao.BpaIndividualizadoDAO;
import br.gov.al.maceio.sishosp.hosp.dao.ConfiguracaoProducaoBpaDAO;
import br.gov.al.maceio.sishosp.hosp.dao.ProcedimentoDAO;
import br.gov.al.maceio.sishosp.hosp.dao.ProgramaDAO;
import br.gov.al.maceio.sishosp.hosp.enums.CamposBpaCabecalho;
import br.gov.al.maceio.sishosp.hosp.enums.CamposBpaConsolidado;
import br.gov.al.maceio.sishosp.hosp.enums.CamposBpaIndividualizados;
import br.gov.al.maceio.sishosp.hosp.enums.MesExtensaoArquivoBPA;
import br.gov.al.maceio.sishosp.hosp.enums.MotivoLiberacao;
import br.gov.al.maceio.sishosp.hosp.enums.TipoExportacao;
import br.gov.al.maceio.sishosp.hosp.enums.ValidacaoSenha;

@ManagedBean
@ViewScoped
public class BpaController implements Serializable {

	private static final String TIPO_FOLHA_PARA_GERAR_EXCEL = "Java Books";
	private static final int VALOR_PADRAO_PARA_GERAR_SMT_VRF = 1111;
	private static final int MES_MINIMO_COMPETENCIA_PARA_INE = 8;
	private static final int ANO_MINIMO_COMPETENCIA_PARA_INE = 2015;
	private static final int MAXIMO_DE_REGISTROS_FOLHA_CONSOLIDADO = 20;
	private static final int MAXIMO_DE_REGISTROS_FOLHA_INDIVIDUALIZADO = 99;

	private static final String CBC_FLH = "000003";

	private BpaIndividualizadoDAO bpaIndividualizadoDAO;
	private BpaConsolidadoDAO bpaConsolidadoDAO;
	private BpaCabecalhoBean bpaCabecalho;
	private List<BpaIndividualizadoBean> listaDeBpaIndividualizado;
	private List<BpaConsolidadoBean> listaDeBpaConsolidado;
	private List<String> linhasLayoutImportacao;
	private List<ProcedimentoBean> listaProcedimentos;
	private static final String NOME_ARQUIVO = "BPA_layout_Importacao";
	private static final String PASTA_RAIZ = "/WEB-INF/documentos/";
	private static final String ARQUIVO_EXCEL = ".xlsx";
	private String descricaoArquivo;
	private String sAtributoGenerico1, formatoExportacao, tipoExportacao;
	private Boolean bAtributoGenerico2, informaNumeroFolhaFiltro;
	private Integer numeroFolhaFiltro;
	private Date dataInicioAtendimento;
	private Date dataFimAtendimento;
	private String competencia;
	private List<String> listaCompetencias;
	private String extensao;
	private List<String> listaInconsistencias;
	private FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
			.getSessionMap().get("obj_usuario");
	private Integer idConfiguracaoProducaoBpa = null;
	private List<Integer> listaIdUnidades;
	private List<AtendimentoBean> listaDuplicidades;
	private FuncionarioBean funcionario;
	private FuncionarioBean usuarioLiberacao;
	private AtendimentoBean atendimentoSelecionado;
	private String tipoLiberacao;

	private static final String SENHA_ERRADA_OU_SEM_LIBERAÇÃO = "Funcionário com senha errada ou sem liberação!";

	public BpaController() {
		this.bpaCabecalho = new BpaCabecalhoBean();
		this.bpaConsolidadoDAO = new BpaConsolidadoDAO();
		this.bpaIndividualizadoDAO = new BpaIndividualizadoDAO();
		this.listaDeBpaConsolidado = new ArrayList<BpaConsolidadoBean>();
		this.listaDeBpaIndividualizado = new ArrayList<BpaIndividualizadoBean>();
		listaProcedimentos = new ArrayList<ProcedimentoBean>();
		limparDadosLayoutGerado();
		this.listaDuplicidades = new ArrayList<>();
		this.funcionario = new FuncionarioBean();
	}

	public static void main(String[] args) throws IOException {
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet(TIPO_FOLHA_PARA_GERAR_EXCEL);

		Object[][] bookData = { { "Nome", "Idade"}, { "Walter", "24"},
				{ "Manoel", "22"}};

		int rowCount = -1;

		for (Object[] aBook : bookData) {
			Row row = sheet.createRow(++rowCount);

			int columnCount = -1;

			for (Object field : aBook) {
				Cell cell = row.createCell(++columnCount);
				if (field instanceof String) {
					cell.setCellValue((String) field);
				} else if (field instanceof Integer) {
					cell.setCellValue((Integer) field);
				}
			}
		}

		try (FileOutputStream outputStream = new FileOutputStream("C:\\Users\\henri\\OneDrive\\Documentos\\FREELA\\JavaBooks.xlsx")) {
			workbook.write(outputStream);
		} finally {
			workbook.close();
		}
	}

	public void addMessage() {
		String summary =  "Checked";
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(summary));
	}
	public void listarCompetencias() throws ProjetoException {
		this.listaCompetencias = bpaIndividualizadoDAO.listarCompetencias();
	}

	private ServletContext getServleContext() {
		ServletContext scontext = (ServletContext) this.getFacesContext().getExternalContext().getContext();
		return scontext;
	}

	private FacesContext getFacesContext() {
		FacesContext context = FacesContext.getCurrentInstance();
		return context;
	}

	public void limparTipoLayout() {
		tipoExportacao = new String();
	}

	public void adicionarProcedimentoFiltro(ProcedimentoBean procedimento) {
		if (!existeProcedimentoPermitido(procedimento)) {
			listaProcedimentos.add(procedimento);
			JSFUtil.fecharDialog("dlgConsulProc");
		}
	}

	public void removerProcedimentoFiltro(ProcedimentoBean procedimentoSelecionado) {
		listaProcedimentos.remove(procedimentoSelecionado);
	}

	private boolean existeProcedimentoPermitido(ProcedimentoBean procedimentoSelecionado) {
		for (ProcedimentoBean procedimento : listaProcedimentos) {
			if(procedimentoSelecionado.getIdProc().equals(procedimento.getIdProc())) {
				JSFUtil.adicionarMensagemAdvertencia("Não é possível adicionar o procedimento novamente", "");
				return true;
			}
		}
		return false;
	}

	public void gerarLayoutBpaExcel() throws ProjetoException, ParseException, SQLException {
		limparDadosLayoutGerado();
		listarIdUnidadesConfiguracaoBpa();
		this.competencia = formataCompetenciaParaBanco();
		setaDataInicioIhFimAtendimento(this.competencia);

		if(tipoExportacao.equals(TipoExportacao.INDIVIDUALIZADO.getSigla())) {
			buscaBpasIndividualizadosDoProcedimento(this.dataInicioAtendimento, this.dataFimAtendimento, this.competencia, sAtributoGenerico1, listaProcedimentos);
			atualizaListaInconsistenciasIndividualizado();
			if(!existeProcedimentosForaDaCarga(listaDeBpaIndividualizado, listaDeBpaConsolidado,  this.competencia)
					&& (!existeInconsistencias(sAtributoGenerico1)) && (listaInconsistencias.size()==0)) {
				gerarLayoutBpaIndividualizadoExcel();
			}
		}
		else if(tipoExportacao.equals(TipoExportacao.CONSOLIDADO.getSigla())) {
			buscaBpasConsolidadosDoProcedimento(this.dataInicioAtendimento, this.dataFimAtendimento, this.competencia, sAtributoGenerico1, listaProcedimentos);
			if(!existeProcedimentosForaDaCarga(listaDeBpaIndividualizado, listaDeBpaConsolidado,  this.competencia)
					&& (!existeInconsistencias(sAtributoGenerico1)) && (listaInconsistencias.size()==0)) {
				gerarLayoutBpaConsolidadoExcel();
			}
		}
		else {
			buscaBpasIndividualizadosDoProcedimento(this.dataInicioAtendimento, this.dataFimAtendimento, this.competencia, sAtributoGenerico1, listaProcedimentos);
			buscaBpasConsolidadosDoProcedimento(this.dataInicioAtendimento, this.dataFimAtendimento, this.competencia, sAtributoGenerico1, listaProcedimentos);
			atualizaListaInconsistenciasIndividualizado();
			if(!existeProcedimentosForaDaCarga(listaDeBpaIndividualizado, listaDeBpaConsolidado,  this.competencia)
					&& (!existeInconsistencias(sAtributoGenerico1)) && (listaInconsistencias.size()==0)) {
				gerarLayoutBpaCompletoExcel();
			}
		}
	}

	private void atualizaListaInconsistenciasIndividualizado() {
		listaInconsistencias = new ArrayList<>();
		for (BpaIndividualizadoBean bpaIndividualizado : listaDeBpaIndividualizado) {
			if (bpaIndividualizado.getListaInconsistencias().size()>0){
				for (String inconsistencia : bpaIndividualizado.getListaInconsistencias()) {
					listaInconsistencias.add(inconsistencia);
				}
			}
		}
	}

	private void gerarLayoutBpaIndividualizadoExcel() {
		try {
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet(TIPO_FOLHA_PARA_GERAR_EXCEL);

			int contadorLinha = -1;
			int contadorColuna = -1;

			montarLayoutBpaIndividualizado(sheet, contadorLinha, contadorColuna);

			this.extensao = ARQUIVO_EXCEL;
			String caminhoIhArquivo = PASTA_RAIZ + NOME_ARQUIVO + this.extensao;

			try (FileOutputStream outputStream = new FileOutputStream(this.getServleContext().getRealPath(caminhoIhArquivo))) {
				this.descricaoArquivo = NOME_ARQUIVO + this.extensao;
				workbook.write(outputStream);
			} finally {
				workbook.close();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void montarLayoutBpaIndividualizado(XSSFSheet sheet, int contadorLinha, int contadorColuna)
			throws ProjetoException {

		Row row = sheet.createRow(++contadorLinha);
		row = sheet.createRow(++contadorLinha);
		Cell cell;
		cell = row.createCell(++contadorColuna);
		cell.setCellValue("BPA INDIVIDUALIZADO");
		cell.setCellStyle(retornaEstiloColunaPrimaria(sheet));
		contadorColuna = -1;

		String cnsAnterior = "";

		for (BpaIndividualizadoBean individualizado : listaDeBpaIndividualizado) {

			CellStyle cellStyle = retornaEstiloColunaSecundaria(sheet);

			if(VerificadorUtil.verificarSeObjetoNuloOuVazio(cnsAnterior) || !cnsAnterior.equals(individualizado.getPrdCnsmed())) {
				cnsAnterior = individualizado.getPrdCnsmed();
				FuncionarioBean funcionario = new FuncionarioDAO().buscarProfissionalPorCns(individualizado.getPrdCnsmed());

				if(!VerificadorUtil.verificarSeObjetoNuloOuZero(contadorLinha))
					row = sheet.createRow(++contadorLinha);

				contadorColuna = gerarColunasFuncionarioBpaIndividualizado(contadorColuna, cellStyle, row,
						funcionario);

				row = sheet.createRow(++contadorLinha);
				contadorColuna = -1;

				contadorColuna = gerarColunasAtendimentoBpaIndividualizado(contadorColuna, cellStyle, row);

				row = sheet.createRow(++contadorLinha);
			}
			contadorColuna = -1;
			contadorColuna = gerarLinhasAtendimentoBpaIndividualizado(contadorColuna, individualizado, row);
			contadorColuna = -1;
			row = sheet.createRow(++contadorLinha);
		}
	}

	private int gerarColunasFuncionarioBpaIndividualizado(int contadorColuna, CellStyle cellStyle, Row row,
														  FuncionarioBean funcionario) {
		Cell cell;
		cell = row.createCell(++contadorColuna);
		cell.setCellValue("Profissional: " + funcionario.getNome());
		cell.setCellStyle(cellStyle);

		cell = row.createCell(++contadorColuna);
		cell.setCellValue("CNS: " + funcionario.getCns());
		cell.setCellStyle(cellStyle);

		cell = row.createCell(++contadorColuna);
		cell.setCellValue("Especialidade: " + funcionario.getEspecialidade().getDescEspecialidade());
		cell.setCellStyle(cellStyle);
		return contadorColuna;
	}

	private int gerarColunasAtendimentoBpaIndividualizado(int contadorColuna, CellStyle cellStyle, Row row) {
		Cell cell;
		cell = row.createCell(++contadorColuna);
		cell.setCellValue("Data Atendimento");
		cell.setCellStyle(cellStyle);

		cell = row.createCell(++contadorColuna);
		cell.setCellValue("Código procedimento");
		cell.setCellStyle(cellStyle);

		cell = row.createCell(++contadorColuna);
		cell.setCellValue("Paciente");
		cell.setCellStyle(cellStyle);

		cell = row.createCell(++contadorColuna);
		cell.setCellValue("CNS");
		cell.setCellStyle(cellStyle);

		cell = row.createCell(++contadorColuna);
		cell.setCellValue("Endereço");
		cell.setCellStyle(cellStyle);

		cell = row.createCell(++contadorColuna);
		cell.setCellValue("Código Tipo Logradouro");
		cell.setCellStyle(cellStyle);

		cell = row.createCell(++contadorColuna);
		cell.setCellValue("Número");
		cell.setCellStyle(cellStyle);

		cell = row.createCell(++contadorColuna);
		cell.setCellValue("Bairro");
		cell.setCellStyle(cellStyle);

		cell = row.createCell(++contadorColuna);
		cell.setCellValue("IBG Muni.");
		cell.setCellStyle(cellStyle);

		cell = row.createCell(++contadorColuna);
		cell.setCellValue("Sexo");
		cell.setCellStyle(cellStyle);

		cell = row.createCell(++contadorColuna);
		cell.setCellValue("CBO");
		cell.setCellStyle(cellStyle);

		cell = row.createCell(++contadorColuna);
		cell.setCellValue("CID");
		cell.setCellStyle(cellStyle);

		cell = row.createCell(++contadorColuna);
		cell.setCellValue("Data de Nascimento");
		cell.setCellStyle(cellStyle);

		cell = row.createCell(++contadorColuna);
		cell.setCellValue("Idade");
		cell.setCellStyle(cellStyle);

		cell = row.createCell(++contadorColuna);
		cell.setCellValue("Telefone");
		cell.setCellStyle(cellStyle);

		cell = row.createCell(++contadorColuna);
		cell.setCellValue("Email");
		cell.setCellStyle(cellStyle);

		cell = row.createCell(++contadorColuna);
		cell.setCellValue("Raça / Cor");
		cell.setCellStyle(cellStyle);

		cell = row.createCell(++contadorColuna);
		cell.setCellValue("Nacionalidade");
		cell.setCellStyle(cellStyle);

		cell = row.createCell(++contadorColuna);
		cell.setCellValue("QTD de Procedimento Produzidos");
		cell.setCellStyle(cellStyle);

		cell = row.createCell(++contadorColuna);
		cell.setCellValue("Caracter de atendimento");
		cell.setCellStyle(cellStyle);

		cell = row.createCell(++contadorColuna);
		cell.setCellValue("Origem das Informações");
		cell.setCellStyle(cellStyle);

		cell = row.createCell(++contadorColuna);
		cell.setCellValue("Cod Serviço");
		cell.setCellStyle(cellStyle);

		cell = row.createCell(++contadorColuna);
		cell.setCellValue("Cod Classificação");
		cell.setCellStyle(cellStyle);

		cell = row.createCell(++contadorColuna);
		cell.setCellValue("Cod da Sequência Equipe");
		cell.setCellStyle(cellStyle);

		cell = row.createCell(++contadorColuna);
		cell.setCellValue("Cod da Área Equipe");
		cell.setCellStyle(cellStyle);

		cell = row.createCell(++contadorColuna);
		cell.setCellValue("CNPJ");
		cell.setCellStyle(cellStyle);
		return contadorColuna;
	}

	private int gerarLinhasAtendimentoBpaIndividualizado(int contadorColuna, BpaIndividualizadoBean individualizado,
														 Row row) {
		Cell cell;
		cell = row.createCell(++contadorColuna);
		cell.setCellValue(formataDataBpaParaDataDoBrasil(individualizado.getPrdDtaten()));
		cell = row.createCell(++contadorColuna);
		cell.setCellValue(individualizado.getPrdPa());
		cell = row.createCell(++contadorColuna);
		cell.setCellValue(individualizado.getPrdNmpac());
		cell = row.createCell(++contadorColuna);
		cell.setCellValue(individualizado.getPrdCnspac());
		cell = row.createCell(++contadorColuna);
		cell.setCellValue(individualizado.getPrdEndPcnte());
		cell = row.createCell(++contadorColuna);
		cell.setCellValue(individualizado.getPrdLogradPcnte());
		cell = row.createCell(++contadorColuna);
		cell.setCellValue(individualizado.getPrdNumPcnte());
		cell = row.createCell(++contadorColuna);
		cell.setCellValue(individualizado.getPrdBairroPcnte());
		cell = row.createCell(++contadorColuna);
		cell.setCellValue(individualizado.getPrdIbge());
		cell = row.createCell(++contadorColuna);
		cell.setCellValue(individualizado.getPrdSexo());
		cell = row.createCell(++contadorColuna);
		cell.setCellValue(individualizado.getPrdCbo());
		cell = row.createCell(++contadorColuna);
		cell.setCellValue(individualizado.getPrdCid());
		cell = row.createCell(++contadorColuna);
		cell.setCellValue(formataDataBpaParaDataDoBrasil(individualizado.getPrdDtnasc()));
		cell = row.createCell(++contadorColuna);
		cell.setCellValue(individualizado.getPrdIdade());
		cell = row.createCell(++contadorColuna);
		cell.setCellValue(individualizado.getPrdDDtelPcnte());
		cell = row.createCell(++contadorColuna);
		cell.setCellValue(individualizado.getPrdEmailPcnte());
		cell = row.createCell(++contadorColuna);
		cell.setCellValue(individualizado.getPrdRaca());
		cell = row.createCell(++contadorColuna);
		cell.setCellValue(individualizado.getPrdNac());
		cell = row.createCell(++contadorColuna);
		cell.setCellValue(individualizado.getPrdQt());
		cell = row.createCell(++contadorColuna);
		cell.setCellValue(individualizado.getPrdCaten());
		cell = row.createCell(++contadorColuna);
		cell.setCellValue(individualizado.getPrdOrg());
		cell = row.createCell(++contadorColuna);
		cell.setCellValue(individualizado.getPrdSrv());
		cell = row.createCell(++contadorColuna);
		cell.setCellValue(individualizado.getPrdClf());
		cell = row.createCell(++contadorColuna);
		cell.setCellValue(individualizado.getPrdEquipeSeq());
		cell = row.createCell(++contadorColuna);
		cell.setCellValue(individualizado.getPrdEquipeArea());
		cell = row.createCell(++contadorColuna);
		cell.setCellValue(individualizado.getPrdCnpj());
		return contadorColuna;
	}

	private void gerarLayoutBpaConsolidadoExcel() {
		try {
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet(TIPO_FOLHA_PARA_GERAR_EXCEL);

			int contadorLinha = -1;
			int contadorColuna = -1;

			montarLayoutBpaConsolidado(sheet, contadorLinha, contadorColuna);

			this.extensao = ARQUIVO_EXCEL;
			String caminhoIhArquivo = PASTA_RAIZ + NOME_ARQUIVO + this.extensao;

			try (FileOutputStream outputStream = new FileOutputStream(this.getServleContext().getRealPath(caminhoIhArquivo))) {
				this.descricaoArquivo = NOME_ARQUIVO + this.extensao;
				workbook.write(outputStream);
			} finally {
				workbook.close();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private int montarLayoutBpaConsolidado(XSSFSheet sheet, int contadorLinha, int contadorColuna) {
		CellStyle cellStyle = retornaEstiloColunaSecundaria(sheet);
		Row row = sheet.createRow(++contadorLinha);

		Cell cell;
		cell = row.createCell(++contadorColuna);
		cell.setCellValue("BPA CONSOLIDADO");
		cell.setCellStyle(retornaEstiloColunaPrimaria(sheet));
		contadorColuna = -1;

		row = sheet.createRow(++contadorLinha);
		contadorColuna = gerarColunasBpaConsolidado(contadorColuna, cellStyle, row);

		for (BpaConsolidadoBean consolidado : listaDeBpaConsolidado) {

			row = sheet.createRow(++contadorLinha);
			contadorColuna = -1;

			contadorColuna = gerarLinhasBpaConsolidado(contadorColuna, consolidado, row);
			contadorColuna = -1;
		}
		return contadorLinha;
	}

	private int gerarColunasBpaConsolidado(int contadorColuna, CellStyle cellStyle, Row row) {
		Cell cell;
		cell = row.createCell(++contadorColuna);
		cell.setCellValue("CNES");
		cell.setCellStyle(cellStyle);

		cell = row.createCell(++contadorColuna);
		cell.setCellValue("Competencia");
		cell.setCellStyle(cellStyle);

		cell = row.createCell(++contadorColuna);
		cell.setCellValue("CBO");
		cell.setCellStyle(cellStyle);

		cell = row.createCell(++contadorColuna);
		cell.setCellValue("Procedimento");
		cell.setCellStyle(cellStyle);

		cell = row.createCell(++contadorColuna);
		cell.setCellValue("Idade");
		cell.setCellStyle(cellStyle);

		cell = row.createCell(++contadorColuna);
		cell.setCellValue("QTD de Procedimento Produzidos");
		cell.setCellStyle(cellStyle);

		cell = row.createCell(++contadorColuna);
		cell.setCellValue("Origem das Informações");
		cell.setCellStyle(cellStyle);

		return contadorColuna;
	}

	private int gerarLinhasBpaConsolidado(int contadorColuna, BpaConsolidadoBean consolidado, Row row) {
		Cell cell;
		cell = row.createCell(++contadorColuna);
		cell.setCellValue(consolidado.getPrdCnes());
		cell = row.createCell(++contadorColuna);
		cell.setCellValue(consolidado.getPrdCmp());
		cell = row.createCell(++contadorColuna);
		cell.setCellValue(consolidado.getPrdCbo());
		cell = row.createCell(++contadorColuna);
		cell.setCellValue(consolidado.getPrdPa());
		cell = row.createCell(++contadorColuna);
		cell.setCellValue(consolidado.getPrdIdade());
		cell = row.createCell(++contadorColuna);
		cell.setCellValue(consolidado.getPrdQt());
		cell = row.createCell(++contadorColuna);
		cell.setCellValue(consolidado.getPrdOrg());
		cell = row.createCell(++contadorColuna);
		return contadorColuna;
	}

	private void gerarLayoutBpaCompletoExcel() {
		try {
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet(TIPO_FOLHA_PARA_GERAR_EXCEL);

			int contadorLinha = -1;
			int contadorColuna = -1;

			contadorLinha = montarLayoutBpaConsolidado(sheet, contadorLinha, contadorColuna);

			contadorColuna = -1;
			sheet.createRow(++contadorLinha);

			montarLayoutBpaIndividualizado(sheet, contadorLinha, contadorColuna);

			this.extensao = ARQUIVO_EXCEL;
			String caminhoIhArquivo = PASTA_RAIZ + NOME_ARQUIVO + this.extensao;

			try (FileOutputStream outputStream = new FileOutputStream(this.getServleContext().getRealPath(caminhoIhArquivo))) {
				this.descricaoArquivo = NOME_ARQUIVO + this.extensao;
				workbook.write(outputStream);
			} finally {
				workbook.close();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private CellStyle retornaEstiloColunaPrimaria(Sheet sheet) {
		CellStyle cellStyle = sheet.getWorkbook().createCellStyle();
		Font font = sheet.getWorkbook().createFont();
		font.setBold(true);
		font.setFontHeightInPoints((short) 14);
		cellStyle.setFont(font);
		return cellStyle;
	}

	private CellStyle retornaEstiloColunaSecundaria(Sheet sheet) {
		CellStyle cellStyle = sheet.getWorkbook().createCellStyle();
		Font font = sheet.getWorkbook().createFont();
		font.setBold(true);
		font.setFontHeightInPoints((short) 12);
		cellStyle.setFont(font);
		return cellStyle;
	}

	private String formataDataBpaParaDataDoBrasil(String data) {
		String ano = data.substring(0, 4);
		String mes = data.substring(4, 6);
		String dia = data.substring(6, 8);
		String competenciaFormatada = dia+"/"+mes+"/"+ano;
		return competenciaFormatada;
	}

	public void gerarLayoutBpaImportacao() throws ProjetoException, ParseException, SQLException {
		try {
			listaDuplicidades.clear();
			limparDadosLayoutGerado();
			listarIdUnidadesConfiguracaoBpa();
			this.competencia = formataCompetenciaParaBanco();
			setaDataInicioIhFimAtendimento(this.competencia);
			executaMetodosParaGerarBpaConsolidado(sAtributoGenerico1, this.competencia);
			executaMetodosParaGerarBpaIndividualizado(sAtributoGenerico1, this.competencia);
			executaMetodosParaGerarBpaCabecalho(this.competencia);
			atualizaListaInconsistenciasIndividualizado();
			listarPossiveisDuplicidades();

			if(listaDuplicidades.size() <= 1) {
				executaMetodosParaGerarBpaCabecalho(competencia);
				listaInconsistencias = new ArrayList<>();
				for (BpaIndividualizadoBean bpaIndividualizado : listaDeBpaIndividualizado) {
					if (bpaIndividualizado.getListaInconsistencias().size() > 0) {
						for (String inconsistencia : bpaIndividualizado.getListaInconsistencias()) {
							listaInconsistencias.add(inconsistencia);
						}
					}

				}

				if ( !existeProcedimentosForaDaCarga(listaDeBpaIndividualizado, listaDeBpaConsolidado,  this.competencia)
						&& (!existeInconsistencias(sAtributoGenerico1)) && (listaInconsistencias.size()==0)) {
					adicionarCabecalho();
					adicionarLinhasBpaConsolidado();
					adicionarLinhasBpaIndividualizado();
					this.extensao = gerarExtensaoArquivo(this.competencia);
					this.descricaoArquivo = NOME_ARQUIVO + extensao;
					String caminhoIhArquivo = PASTA_RAIZ + NOME_ARQUIVO + extensao;

					Path file = Paths.get(this.getServleContext().getRealPath(caminhoIhArquivo) + File.separator);
					Files.write(file, this.linhasLayoutImportacao, StandardCharsets.UTF_8).getFileSystem();
				}
				if (listaInconsistencias.size() > 0)
					JSFUtil.adicionarMensagemErro(
							listaInconsistencias.size()
									+ " Inconsistência(s) encontrada(s). Resolva para poder gerar o arquivo BPA", "ERRO");
				JSFUtil.atualizarComponente("frmPrincipal:tabelainconsistencia");
			} else {
				JSFUtil.abrirDialog("dlgDuplicidade");
			}
		} catch (IOException ioe) {
			JSFUtil.adicionarMensagemErro(ioe.getMessage(), "Erro");
			ioe.printStackTrace();
		} catch (ProjetoException pe) {
			JSFUtil.adicionarMensagemErro(pe.getMessage(), "");
		}
	}

	private boolean existeProcedimentosForaDaCarga
			(List<BpaIndividualizadoBean> listaBpaIndividualizado, List<BpaConsolidadoBean> listaBpaConsolidado, String competencia)
			throws ProjetoException {

		boolean existeProcedimentoSemCarga = false;

		List<String> listaCodigoProcedimentosBpa = new ArrayList<>();

		List<String> listaCodigoProcedimentos = new AtendimentoDAO().retornaCodigoProcedimentoDeAtendimentosOuAgendamentosDeUmPeriodo
				(this.dataInicioAtendimento, this.dataFimAtendimento, sAtributoGenerico1, listaProcedimentos, listaIdUnidades, competencia);

		for (BpaIndividualizadoBean individual : listaBpaIndividualizado) {
			if(!listaCodigoProcedimentosBpa.contains(individual.getPrdPa())) {
				listaCodigoProcedimentosBpa.add(individual.getPrdPa());
			}
		}

		for (BpaConsolidadoBean consolidado : listaBpaConsolidado) {
			if(!listaCodigoProcedimentosBpa.contains(consolidado.getPrdPa())) {
				listaCodigoProcedimentosBpa.add(consolidado.getPrdPa());
			}
		}

		String codigoProcedimentosSemCarga = "";
		if(!listaCodigoProcedimentosBpa.isEmpty()) {
			for (String codigo : listaCodigoProcedimentos) {
				if (!listaCodigoProcedimentosBpa.contains(codigo)) {
					codigoProcedimentosSemCarga += codigo + "; ";
					existeProcedimentoSemCarga = true;
				}
			}
		}

		if(existeProcedimentoSemCarga) {
			JSFUtil.adicionarMensagemAdvertencia
					("Não é possivel fazer a exportação do BPA pois há atendimentos com procedimento(s) sem carga do SIGTAP no periodo escolhido", "");
			JSFUtil.adicionarMensagemAdvertencia("Procedimento(s): "+codigoProcedimentosSemCarga, "");
		}

		return existeProcedimentoSemCarga;
	}

	private void listarIdUnidadesConfiguracaoBpa() throws ProjetoException, SQLException {
		if(!VerificadorUtil.verificarSeObjetoNuloOuZero(idConfiguracaoProducaoBpa))
			this.listaIdUnidades = new ConfiguracaoProducaoBpaDAO().listaIdUnidadesDaConfiguracoesBpa(idConfiguracaoProducaoBpa);
	}

	private boolean existeInconsistencias(String tipoGeracao) throws SQLException, ProjetoException {
		if ((verificarInconsistenciaProcedimento()) || (verificarInconsistenciaQuantidadeAtendimentosOuAgendamentos(tipoGeracao)))
			return true;
		return false;
	}

	private boolean verificarInconsistenciaProcedimento() throws SQLException, ProjetoException {
		ProcedimentoBean procedimento = new ProcedimentoDAO().retornarProcedimentoInconsistente(this.competencia, this.dataInicioAtendimento, this.dataFimAtendimento);
		if(!VerificadorUtil.verificarSeObjetoNulo(procedimento)) {
			if(VerificadorUtil.verificarSeObjetoNuloOuZero(procedimento.getServico().getId())
					&& VerificadorUtil.verificarSeObjetoNuloOuZero(procedimento.getClassificacao().getId()) ) {
				JSFUtil.adicionarMensagemErro
						( "O procedimento " +procedimento.getNomeProc()+" não possui serviço e classificação corrija essa inconsistência", "Erro");
			}
			else if (VerificadorUtil.verificarSeObjetoNuloOuZero(procedimento.getServico().getId())) {
				JSFUtil.adicionarMensagemErro ( "O procedimento " +procedimento.getNomeProc()+" não possui serviço corrija essa inconsistência", "Erro");
			}
			else {
				JSFUtil.adicionarMensagemErro ( "O procedimento " +procedimento.getNomeProc()+" não possui classificação corrija essa inconsistência", "Erro");
			}
			return true;
		}
		return false;
	}

	private boolean verificarInconsistenciaQuantidadeAtendimentosOuAgendamentos(String tipoGeracao) throws ProjetoException {
		Integer totalAtendimentos = new AtendimentoDAO().retornaTotalAtendimentosOuAgendamentosDeUmPeriodo(this.dataInicioAtendimento, this.dataFimAtendimento, tipoGeracao, listaProcedimentos,  this.idConfiguracaoProducaoBpa, this.competencia);
		Integer totalAtendimentoGeradoBPA = calculaTotalAtendimentoBPA();
		if(totalAtendimentoGeradoBPA < totalAtendimentos) {
			JSFUtil.adicionarMensagemErro("O total de atendimentos no arquivo do BPA é  menor do que o total de atendimentos do sistema", "Erro");
			return true;
		}
		else if (totalAtendimentoGeradoBPA > totalAtendimentos) {
			JSFUtil.adicionarMensagemErro("O total de atendimentos no arquivo do BPA é  maior do que o total de atendimentos do sistema", "Erro");
			return true;
		}

		return false;
	}

	private Integer calculaTotalAtendimentoBPA() {
		Integer totalAtendimentos = 0;
		for (BpaConsolidadoBean bpaConsolidado : listaDeBpaConsolidado) {
			totalAtendimentos += Integer.valueOf(bpaConsolidado.getPrdQt());
		}

		for (BpaIndividualizadoBean bpaIndividualizado : listaDeBpaIndividualizado) {
			totalAtendimentos += Integer.valueOf(bpaIndividualizado.getPrdQt());
		}
		return totalAtendimentos;
	}

	private void limparDadosLayoutGerado() {
		this.linhasLayoutImportacao = new ArrayList<String>();
		this.listaDeBpaConsolidado = new ArrayList<BpaConsolidadoBean>();
		this.listaDeBpaIndividualizado = new ArrayList<BpaIndividualizadoBean>();
		this.extensao = null;
		this.descricaoArquivo = null;
		this.bpaCabecalho = new BpaCabecalhoBean();
		listaInconsistencias = new ArrayList<>();
		this.listaIdUnidades = new ArrayList<>();
	}

	public StreamedContent download() throws IOException, ProjetoException {
		StreamedContent file = null;
		if(VerificadorUtil.verificarSeObjetoNuloOuVazio(this.descricaoArquivo)) {
			return file;
		}

		InputStream stream = new FileInputStream(
				this.getServleContext().getRealPath(PASTA_RAIZ) + File.separator + this.descricaoArquivo);

		file = new DefaultStreamedContent(stream, "application/"+this.extensao, this.descricaoArquivo);
		return file;
	}

	private void executaMetodosParaGerarBpaCabecalho(String competencia) throws ProjetoException {
		gerarCabecalho(competencia);
		adicionaCaracteresEmCamposBpaCabecalhoOndeTamanhoNaoEhValido();
	}

	private void adicionarCabecalho() {
		this.linhasLayoutImportacao.add(bpaCabecalho.toString());
	}

	private void gerarCabecalho(String competencia) throws ProjetoException {
		this.bpaCabecalho.setCbcMvm(competencia);

		Integer cbcLin = (this.listaDeBpaConsolidado.size() + this.listaDeBpaIndividualizado.size() + 1);
		this.bpaCabecalho.setCbcLin(cbcLin.toString());

		this.bpaCabecalho.setCbcFlh(CBC_FLH);
		this.bpaCabecalho.setCbcSmtVrf(geraNumeroSmtVrf());

		this.bpaCabecalho.setCbcRsp(user_session.getUnidade().getParametro().getOrgaoOrigemResponsavelPelaInformacao());
		this.bpaCabecalho.setCbcSgl(user_session.getUnidade().getParametro().getSiglaOrgaoOrigemResponsavelPelaDigitacao());
		this.bpaCabecalho.setCbcCgccpf(user_session.getUnidade().getParametro().getCgcCpfPrestadorOuOrgaoPublico());
		this.bpaCabecalho.setCbcDst(user_session.getUnidade().getParametro().getOrgaoDestinoInformacao());
		this.bpaCabecalho.setCbcDstIn(user_session.getUnidade().getParametro().getIndicadorOrgaoDestinoInformacao());
		this.bpaCabecalho.setCbcVersao(user_session.getUnidade().getParametro().getVersaoSistema());
	}

	private String geraNumeroSmtVrf() {

		Long somaDosCodigosDeProcedimentos = 0L;
		for (BpaConsolidadoBean bpaConsolidado : this.listaDeBpaConsolidado) {
			somaDosCodigosDeProcedimentos += Long.valueOf(bpaConsolidado.getPrdPa());
			somaDosCodigosDeProcedimentos += Long.valueOf(bpaConsolidado.getPrdQt());
		}

		for (BpaIndividualizadoBean bpaIndividualizado : this.listaDeBpaIndividualizado) {
			somaDosCodigosDeProcedimentos += Long.valueOf(bpaIndividualizado.getPrdPa());
			somaDosCodigosDeProcedimentos += Long.valueOf(bpaIndividualizado.getPrdQt());
		}

		Long restoDivisao = (somaDosCodigosDeProcedimentos % VALOR_PADRAO_PARA_GERAR_SMT_VRF);
		Long smtVrf = (restoDivisao + VALOR_PADRAO_PARA_GERAR_SMT_VRF);
		return smtVrf.toString();
	}

	private void adicionaCaracteresEmCamposBpaCabecalhoOndeTamanhoNaoEhValido() throws ProjetoException {
		this.bpaCabecalho.setCbcMvm(CamposBpaCabecalho.CBC_MVM.preencheCaracteresRestantes(this.bpaCabecalho.getCbcMvm()));
		this.bpaCabecalho.setCbcLin(CamposBpaCabecalho.CBC_LIN.preencheCaracteresRestantes(this.bpaCabecalho.getCbcLin()));
		this.bpaCabecalho.setCbcFlh(CamposBpaCabecalho.CBC_FLH.preencheCaracteresRestantes(this.bpaCabecalho.getCbcFlh()));
		this.bpaCabecalho.setCbcSmtVrf(CamposBpaCabecalho.CBC_SMT_VRF.preencheCaracteresRestantes(this.bpaCabecalho.getCbcSmtVrf()));
		this.bpaCabecalho.setCbcRsp(CamposBpaCabecalho.CBC_RSP.preencheCaracteresRestantes(this.bpaCabecalho.getCbcRsp()));
		this.bpaCabecalho.setCbcSgl(CamposBpaCabecalho.CBC_SGL.preencheCaracteresRestantes(this.bpaCabecalho.getCbcSgl()));
		this.bpaCabecalho.setCbcCgccpf(CamposBpaCabecalho.CBC_CGCCPF.preencheCaracteresRestantes(this.bpaCabecalho.getCbcCgccpf()));
		this.bpaCabecalho.setCbcDst(CamposBpaCabecalho.CBC_DST.preencheCaracteresRestantes(this.bpaCabecalho.getCbcDst()));
		this.bpaCabecalho.setCbcDstIn(CamposBpaCabecalho.CBC_DST_IN.preencheCaracteresRestantes(this.bpaCabecalho.getCbcDstIn()));
		this.bpaCabecalho.setCbcVersao(CamposBpaCabecalho.CBC_VERSAO.preencheCaracteresRestantes(this.bpaCabecalho.getCbcVersao()));
	}

	private void setaDataInicioIhFimAtendimento(String competencia) throws ParseException {
		Integer ano = Integer.valueOf(competencia.substring(0, 4));
		Integer mes = Integer.valueOf(competencia.substring(4, 6));
		String competenciaFormatada = ano+"/"+mes;

		Date data = new SimpleDateFormat("yyyy/MM").parse(competenciaFormatada);

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(data);
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));

		Integer primeiraDataMes = calendar.getMinimum(Calendar.DAY_OF_MONTH);
		Integer ultimaDataMes = calendar.getActualMaximum(Calendar.DATE);

		String dataInicial = ano+"/"+mes+"/"+primeiraDataMes;
		String dataFinal = ano+"/"+mes+"/"+ultimaDataMes;

		try {
			this.dataInicioAtendimento = new SimpleDateFormat("yyyy/MM/dd").parse(dataInicial);
			this.dataFimAtendimento = new SimpleDateFormat("yyyy/MM/dd").parse(dataFinal);
		} catch (ParseException e) {
			JSFUtil.adicionarMensagemErro(e.getMessage(), "Erro");
			e.printStackTrace();
		}
	}

	private String formataCompetenciaParaBanco() {
		String diaCompetencia = competencia.substring(0, 2);
		String anoCompetencia = competencia.substring(3, 7);
		String competenciaFormatada = anoCompetencia+diaCompetencia;
		return competenciaFormatada;
	}

	private void executaMetodosParaGerarBpaConsolidado(String tipoGeracao, String competencia) throws ProjetoException {
		buscaBpasConsolidadosDoProcedimento(this.dataInicioAtendimento, this.dataFimAtendimento, competencia, tipoGeracao, listaProcedimentos);
		geraNumeroDaFolhaConsolidado();
		geraNumeroDaLinhaDaFolhaConsolidado();
		adicionaCaracteresEmCamposBpaConsolidadoOndeTamanhoNaoEhValido();
	}

	private void buscaBpasConsolidadosDoProcedimento(Date dataInicio, Date dataFim, String competencia, String tipoGeracao, List<ProcedimentoBean> listaProcedimentosFiltro) throws ProjetoException {
		this.listaDeBpaConsolidado = bpaConsolidadoDAO.carregaDadosBpaConsolidado(dataInicio, dataFim, competencia, tipoGeracao, listaProcedimentosFiltro, listaIdUnidades, this.idConfiguracaoProducaoBpa);
	}

	public void geraNumeroDaFolhaConsolidado() {
		List<String> listaCbos = retornaUnicamenteCbosParaBpaConsolidado();
		for (String cbo : listaCbos) {
			Integer quantidadeRegistrosPorFolha = 0;
			Integer numeroFolhaBpaConsolidado = null;
			if (informaNumeroFolhaFiltro)
				numeroFolhaBpaConsolidado = numeroFolhaFiltro;
			else
				numeroFolhaBpaConsolidado = 1;
			for(int i = 0; i < this.listaDeBpaConsolidado.size(); i++) {
				if(this.listaDeBpaConsolidado.get(i).getPrdCbo().equals(cbo)) {
					if(quantidadeRegistrosPorFolha < MAXIMO_DE_REGISTROS_FOLHA_CONSOLIDADO) {
						this.listaDeBpaConsolidado.get(i).setPrdFlh(numeroFolhaBpaConsolidado.toString());
						quantidadeRegistrosPorFolha++;
					}
					else {
						numeroFolhaBpaConsolidado++;
						this.listaDeBpaConsolidado.get(i).setPrdFlh(numeroFolhaBpaConsolidado.toString());
						quantidadeRegistrosPorFolha = 1;
					}
				}
			}
		}
	}

	private List<String> retornaUnicamenteCbosParaBpaConsolidado() {
		List<String> listaCbos = new ArrayList<String>();
		for(BpaConsolidadoBean consolidado : this.listaDeBpaConsolidado) {
			if(!listaCbos.contains(consolidado.getPrdCbo()))
				listaCbos.add(consolidado.getPrdCbo());
		}
		return listaCbos;
	}

	public void geraNumeroDaLinhaDaFolhaConsolidado() {
		if ((listaDeBpaConsolidado!=null) && (listaDeBpaConsolidado.size()>0)) {
			String cboAuxiliador = this.listaDeBpaConsolidado.get(0).getPrdCbo();
			String numeroFolhaAuxiliador = this.listaDeBpaConsolidado.get(0).getPrdFlh();
			Integer numeroLinha = 0;
			for (int i = 0; i < this.listaDeBpaConsolidado.size(); i++) {
             /*   if (this.listaDeBpaConsolidado.get(i).getPrdCbo().equals(cboAuxiliador)
                        && this.listaDeBpaConsolidado.get(i).getPrdFlh().equals(numeroFolhaAuxiliador)) {
                  */
				numeroLinha++;
				this.listaDeBpaConsolidado.get(i).setPrdSeq(numeroLinha.toString());
               /* } else {
                    cboAuxiliador = this.listaDeBpaConsolidado.get(i).getPrdCbo();
                    numeroFolhaAuxiliador = this.listaDeBpaConsolidado.get(i).getPrdFlh();
                    numeroLinha = 1;
                    this.listaDeBpaConsolidado.get(i).setPrdSeq(numeroLinha.toString());
                }
                */
			}
		}
	}

	public void adicionaCaracteresEmCamposBpaConsolidadoOndeTamanhoNaoEhValido() throws ProjetoException {
		if ((listaDeBpaConsolidado!=null) && (listaDeBpaConsolidado.size()>0)) {
			List<BpaConsolidadoBean> listaBpaConsolidadoAuxiliar = new ArrayList<BpaConsolidadoBean>();
			listaBpaConsolidadoAuxiliar.addAll(this.listaDeBpaConsolidado);
			this.listaDeBpaConsolidado = new ArrayList<BpaConsolidadoBean>();

			for (BpaConsolidadoBean consolidado : listaBpaConsolidadoAuxiliar) {
				consolidado.setPrdCnes(CamposBpaConsolidado.PRD_CNES.preencheCaracteresRestantes(consolidado.getPrdCnes()));
				consolidado.setPrdCmp(CamposBpaConsolidado.PRD_CMP.preencheCaracteresRestantes(consolidado.getPrdCmp()));
				consolidado.setPrdCbo(CamposBpaConsolidado.PRD_CBO.preencheCaracteresRestantes(consolidado.getPrdCbo()));
				consolidado.setPrdFlh(CamposBpaConsolidado.PRD_FLH.preencheCaracteresRestantes(consolidado.getPrdFlh()));
				consolidado.setPrdSeq(CamposBpaConsolidado.PRD_SEQ.preencheCaracteresRestantes(consolidado.getPrdSeq()));
				consolidado.setPrdPa(CamposBpaConsolidado.PRD_PA.preencheCaracteresRestantes(org.apache.commons.lang3.StringUtils.stripAccents(consolidado.getPrdPa())));
				consolidado.setPrdIdade(CamposBpaConsolidado.PRD_IDADE.preencheCaracteresRestantes(consolidado.getPrdIdade()));
				consolidado.setPrdQt(CamposBpaConsolidado.PRD_QT.preencheCaracteresRestantes(consolidado.getPrdQt()));
				this.listaDeBpaConsolidado.add(consolidado);
			}
		}
	}

	private void adicionarLinhasBpaConsolidado() {
		for (BpaConsolidadoBean bpaConsolidado : this.listaDeBpaConsolidado) {
			this.linhasLayoutImportacao.add(bpaConsolidado.toString());
		}
	}

	private void executaMetodosParaGerarBpaIndividualizado(String tipoGeracao, String competencia) throws ProjetoException {
		buscaBpasIndividualizadosDoProcedimento(this.dataInicioAtendimento, this.dataFimAtendimento, competencia, tipoGeracao, listaProcedimentos);
		geraNumeroDaFolhaIndividualizado();
		geraNumeroDaLinhaDaFolhaIndividualizado();
		adicionaCaracteresEmCamposBpaIndividualizadoOndeTamanhoNaoEhValido();
	}

	public void buscaBpasIndividualizadosDoProcedimento(Date dataInicio, Date dataFim, String competencia, String tipoGeracao, List<ProcedimentoBean> listaProcedimentosFiltro) throws ProjetoException {
		this.listaDeBpaIndividualizado = bpaIndividualizadoDAO.carregaDadosBpaIndividualizado(dataInicio, dataFim, competencia, tipoGeracao, listaProcedimentosFiltro, this.idConfiguracaoProducaoBpa);
	}

	public void listarPossiveisDuplicidades() throws ProjetoException {
		List<AtendimentoBean> listaDuplicidadesAux = new ArrayList<>();
		for (BpaIndividualizadoBean bpa : listaDeBpaIndividualizado) {
			if(Integer.valueOf(bpa.getPrdQt()) > 1) {
				listaDuplicidadesAux = bpaIndividualizadoDAO.listaPossiveisDuplicidades(bpa, this.idConfiguracaoProducaoBpa);
				listaDuplicidades.addAll(listaDuplicidadesAux);
			}
		}
	}

	public void geraNumeroDaFolhaIndividualizado() {
		if ((listaDeBpaIndividualizado!=null) && (listaDeBpaIndividualizado.size()>0)) {
			List<String> listaCnsDosMedicos = retornaUnicamenteCnsParaBpaIndividualizado();
			for (String cnsMedico : listaCnsDosMedicos) {
				Integer quantidadeRegistrosPorFolha = 0;

				Integer numeroFolhaBpaIndividualizado = null;
				if (informaNumeroFolhaFiltro)
					numeroFolhaBpaIndividualizado = numeroFolhaFiltro;
				else
					numeroFolhaBpaIndividualizado = 1;
				for (int i = 0; i < this.listaDeBpaIndividualizado.size(); i++) {
					if (this.listaDeBpaIndividualizado.get(i).getPrdCnsmed().equals(cnsMedico)) {
						if (quantidadeRegistrosPorFolha < MAXIMO_DE_REGISTROS_FOLHA_INDIVIDUALIZADO) {
							this.listaDeBpaIndividualizado.get(i).setPrdFlh(numeroFolhaBpaIndividualizado.toString());
							quantidadeRegistrosPorFolha++;
						} else {
							numeroFolhaBpaIndividualizado++;
							this.listaDeBpaIndividualizado.get(i).setPrdFlh(numeroFolhaBpaIndividualizado.toString());
							quantidadeRegistrosPorFolha = 1;
						}
					}
				}
			}
		}
	}

	private List<String> retornaUnicamenteCnsParaBpaIndividualizado() {
		List<String> listaCnsDosMedicos = new ArrayList<String>();
		for(BpaIndividualizadoBean individualizado : this.listaDeBpaIndividualizado) {
			if(!listaCnsDosMedicos.contains(individualizado.getPrdCnsmed()))
				listaCnsDosMedicos.add(individualizado.getPrdCnsmed());
		}
		return listaCnsDosMedicos;
	}

	public void setaOpcaoGeracaoProducao(){
		sAtributoGenerico1 = "A";
		informaNumeroFolhaFiltro = false;
		bAtributoGenerico2 = false;
		formatoExportacao = "T";
	}

	public void geraNumeroDaLinhaDaFolhaIndividualizado() {
		if ((listaDeBpaIndividualizado!=null) && (listaDeBpaIndividualizado.size()>0)) {
			String cnsMedicoAuxiliador = this.listaDeBpaIndividualizado.get(0).getPrdCnsmed();
			String numeroFolhaAuxiliador = this.listaDeBpaIndividualizado.get(0).getPrdFlh();
			Integer numeroLinha = 0;


			for (int i = 0; i < this.listaDeBpaIndividualizado.size(); i++) {
				if (this.listaDeBpaIndividualizado.get(i).getPrdCnsmed().equals(cnsMedicoAuxiliador)
						&& this.listaDeBpaIndividualizado.get(i).getPrdFlh()!=null
						&& this.listaDeBpaIndividualizado.get(i).getPrdFlh().equals(numeroFolhaAuxiliador)) {
					numeroLinha++;
					this.listaDeBpaIndividualizado.get(i).setPrdSeq(numeroLinha.toString());
				} else {
					cnsMedicoAuxiliador = this.listaDeBpaIndividualizado.get(i).getPrdCnsmed();
					numeroFolhaAuxiliador = this.listaDeBpaIndividualizado.get(i).getPrdFlh();
					numeroLinha = 1;
					this.listaDeBpaIndividualizado.get(i).setPrdSeq(numeroLinha.toString());
				}
			}

			ordenarSequenciaBpaIndividualizado();
		}
	}

	private void ordenarSequenciaBpaIndividualizado() {
		List<BpaIndividualizadoBean> aux = new ArrayList<>();

		Integer numeroLinha = 1;
		while(aux.size() < this.listaDeBpaIndividualizado.size()) {

			for (int i = 0; i < this.listaDeBpaIndividualizado.size(); i++) {
				if(this.listaDeBpaIndividualizado.get(i).getPrdSeq().equals(numeroLinha.toString())) {
					aux.add(listaDeBpaIndividualizado.get(i));
				}
			}
			numeroLinha++;

		}

		this.listaDeBpaIndividualizado.clear();
		this.listaDeBpaIndividualizado.addAll(aux);
	}

	public void adicionaCaracteresEmCamposBpaIndividualizadoOndeTamanhoNaoEhValido() throws ProjetoException {
		if ((listaDeBpaIndividualizado!=null) && (listaDeBpaIndividualizado.size()>0)) {
			List<BpaIndividualizadoBean> listaBpaIndividualizadoAuxiliar = new ArrayList<BpaIndividualizadoBean>();
			listaBpaIndividualizadoAuxiliar.addAll(this.listaDeBpaIndividualizado);

			this.listaDeBpaIndividualizado = new ArrayList<BpaIndividualizadoBean>();

			for (BpaIndividualizadoBean individualizado : listaBpaIndividualizadoAuxiliar) {
				individualizado.setPrdCnes(CamposBpaIndividualizados.PRD_CNES.preencheCaracteresRestantes(individualizado.getPrdCnes()));
				individualizado.setPrdCmp(CamposBpaIndividualizados.PRD_CMP.preencheCaracteresRestantes(individualizado.getPrdCmp()));
				individualizado.setPrdCnsmed(CamposBpaIndividualizados.PRD_CNSMED.preencheCaracteresRestantes(individualizado.getPrdCnsmed()));
				individualizado.setPrdCbo(CamposBpaIndividualizados.PRD_CBO.preencheCaracteresRestantes(individualizado.getPrdCbo()));
				individualizado.setPrdDtaten(CamposBpaIndividualizados.PRD_DTATEN.preencheCaracteresRestantes(individualizado.getPrdDtaten()));
				individualizado.setPrdFlh(CamposBpaIndividualizados.PRD_FLH.preencheCaracteresRestantes(individualizado.getPrdFlh()));
				individualizado.setPrdSeq(CamposBpaIndividualizados.PRD_SEQ.preencheCaracteresRestantes(individualizado.getPrdSeq()));
				individualizado.setPrdPa(CamposBpaIndividualizados.PRD_PA.preencheCaracteresRestantes(individualizado.getPrdPa()));
				individualizado.setPrdCnspac(CamposBpaIndividualizados.PRD_CNSPAC.preencheCaracteresRestantes(individualizado.getPrdCnspac()));
				individualizado.setPrdSexo(CamposBpaIndividualizados.PRD_SEXO.preencheCaracteresRestantes(individualizado.getPrdSexo()));
				individualizado.setPrdIbge(CamposBpaIndividualizados.PRD_IBGE.preencheCaracteresRestantes(individualizado.getPrdIbge()));
				individualizado.setPrdCid(CamposBpaIndividualizados.PRD_CID.preencheCaracteresRestantes(individualizado.getPrdCid()));
				individualizado.setPrdIdade(CamposBpaIndividualizados.PRD_IDADE.preencheCaracteresRestantes(individualizado.getPrdIdade()));
				individualizado.setPrdQt(CamposBpaIndividualizados.PRD_QT.preencheCaracteresRestantes(individualizado.getPrdQt()));
				individualizado.setPrdCaten(CamposBpaIndividualizados.PRD_CATEN.preencheCaracteresRestantes(individualizado.getPrdCaten()));
				individualizado.setPrdNaut(CamposBpaIndividualizados.PRD_NAUT.preencheCaracteresRestantes(individualizado.getPrdNaut()));
				individualizado.setPrdNmpac(CamposBpaIndividualizados.PRD_NMPAC.preencheCaracteresRestantes(org.apache.commons.lang3.StringUtils.stripAccents(individualizado.getPrdNmpac())));
				individualizado.setPrdDtnasc(CamposBpaIndividualizados.PRD_DTNASC.preencheCaracteresRestantes(individualizado.getPrdDtnasc()));
				individualizado.setPrdRaca(CamposBpaIndividualizados.PRD_RACA.preencheCaracteresRestantes(individualizado.getPrdRaca()));
				individualizado.setPrdEtnia(CamposBpaIndividualizados.PRD_ETNIA.preencheCaracteresRestantes(individualizado.getPrdEtnia()));
				individualizado.setPrdNac(CamposBpaIndividualizados.PRD_NAC.preencheCaracteresRestantes(individualizado.getPrdNac()));
				individualizado.setPrdSrv(CamposBpaIndividualizados.PRD_SRV.preencheCaracteresRestantes(individualizado.getPrdSrv()));
				individualizado.setPrdClf(CamposBpaIndividualizados.PRD_CLF.preencheCaracteresRestantes(individualizado.getPrdClf()));
				individualizado.setPrdEquipeSeq(CamposBpaIndividualizados.PRD_EQUIPE_SEQ.preencheCaracteresRestantes(individualizado.getPrdEquipeSeq()));
				individualizado.setPrdEquipeArea(CamposBpaIndividualizados.PRD_EQUIPE_AREA.preencheCaracteresRestantes(individualizado.getPrdEquipeArea()));
				individualizado.setPrdCnpj(CamposBpaIndividualizados.PRD_CNPJ.preencheCaracteresRestantes(individualizado.getPrdCnpj()));
				individualizado.setPrdCepPcnte(CamposBpaIndividualizados.PRD_CEP_PCNTE.preencheCaracteresRestantes(individualizado.getPrdCepPcnte()));
				individualizado.setPrdLogradPcnte(CamposBpaIndividualizados.PRD_LOGRAD_PCNTE.preencheCaracteresRestantes(org.apache.commons.lang3.StringUtils.stripAccents(individualizado.getPrdLogradPcnte())));
				individualizado.setPrdEndPcnte(CamposBpaIndividualizados.PRD_END_PCNTE.preencheCaracteresRestantes(org.apache.commons.lang3.StringUtils.stripAccents(individualizado.getPrdEndPcnte())));
				individualizado.setPrdComplPcnte(CamposBpaIndividualizados.PRD_COMPL_PCNTE.preencheCaracteresRestantes(org.apache.commons.lang3.StringUtils.stripAccents(individualizado.getPrdComplPcnte())));
				individualizado.setPrdNumPcnte(CamposBpaIndividualizados.PRD_NUM_PCNTE.preencheCaracteresRestantes(org.apache.commons.lang3.StringUtils.stripAccents(individualizado.getPrdNumPcnte())));
				individualizado.setPrdBairroPcnte(CamposBpaIndividualizados.PRD_BAIRRO_PCNTE.preencheCaracteresRestantes(org.apache.commons.lang3.StringUtils.stripAccents(individualizado.getPrdBairroPcnte())));
				individualizado.setPrdDDtelPcnte(CamposBpaIndividualizados.PRD_DDTEL_PCNTE.preencheCaracteresRestantes(org.apache.commons.lang3.StringUtils.stripAccents(individualizado.getPrdDDtelPcnte())));
				individualizado.setPrdEmailPcnte(CamposBpaIndividualizados.PRD_EMAIL_PCNTE.preencheCaracteresRestantes(org.apache.commons.lang3.StringUtils.stripAccents(individualizado.getPrdEmailPcnte())));
				individualizado.setPrdIne(validaCompetenciaParaPreencherINE(individualizado.getPrdIne(), individualizado.getPrdCmp()));

				this.listaDeBpaIndividualizado.add(individualizado);
			}
		}

	}

	public String validaCompetenciaParaPreencherINE(String ine, String competencia) throws ProjetoException {
		Integer anoCompetencia = Integer.valueOf(competencia.substring(0, 4));
		Integer mesCompetencia = Integer.valueOf(competencia.substring(4, 6));
		if (anoCompetencia < ANO_MINIMO_COMPETENCIA_PARA_INE || (anoCompetencia == ANO_MINIMO_COMPETENCIA_PARA_INE
				&& mesCompetencia < MES_MINIMO_COMPETENCIA_PARA_INE)) {
			Integer tamanho = 10;
			ine = new String();
			while (ine.length() < tamanho) {
				ine += " ";
			}
			return ine;
		} else
			return CamposBpaIndividualizados.PRD_INE.preencheCaracteresRestantes(ine);
	}

	public void adicionarLinhasBpaIndividualizado() {
		for (BpaIndividualizadoBean bpaIndividualizado : this.listaDeBpaIndividualizado) {
			this.linhasLayoutImportacao.add(bpaIndividualizado.toString());
		}
	}

	private String gerarExtensaoArquivo(String competencia) {

		String numeroMesString = competencia.substring(4, 6);
		String extensao = "";

		if(numeroMesString.equals(MesExtensaoArquivoBPA.JANEIRO.getSigla()))
			extensao = MesExtensaoArquivoBPA.JANEIRO.getExtensao();
		else if(numeroMesString.equals(MesExtensaoArquivoBPA.FEVEREIRO.getSigla()))
			extensao = MesExtensaoArquivoBPA.FEVEREIRO.getExtensao();
		else if(numeroMesString.equals(MesExtensaoArquivoBPA.MARCO.getSigla()))
			extensao = MesExtensaoArquivoBPA.MARCO.getExtensao();
		else if(numeroMesString.equals(MesExtensaoArquivoBPA.ABRIL.getSigla()))
			extensao = MesExtensaoArquivoBPA.ABRIL.getExtensao();
		else if(numeroMesString.equals(MesExtensaoArquivoBPA.MAIO.getSigla()))
			extensao = MesExtensaoArquivoBPA.MAIO.getExtensao();
		else if(numeroMesString.equals(MesExtensaoArquivoBPA.JUNHO.getSigla()))
			extensao = MesExtensaoArquivoBPA.JUNHO.getExtensao();
		else if(numeroMesString.equals(MesExtensaoArquivoBPA.JULHO.getSigla()))
			extensao = MesExtensaoArquivoBPA.JULHO.getExtensao();
		else if(numeroMesString.equals(MesExtensaoArquivoBPA.AGOSTO.getSigla()))
			extensao = MesExtensaoArquivoBPA.AGOSTO.getExtensao();
		else if(numeroMesString.equals(MesExtensaoArquivoBPA.SETEMBRO.getSigla()))
			extensao = MesExtensaoArquivoBPA.SETEMBRO.getExtensao();
		else if(numeroMesString.equals(MesExtensaoArquivoBPA.OUTUBRO.getSigla()))
			extensao = MesExtensaoArquivoBPA.OUTUBRO.getExtensao();
		else if(numeroMesString.equals(MesExtensaoArquivoBPA.NOVEMBRO.getSigla()))
			extensao = MesExtensaoArquivoBPA.NOVEMBRO.getExtensao();
		else if(numeroMesString.equals(MesExtensaoArquivoBPA.DEZEMBRO.getSigla()))
			extensao = MesExtensaoArquivoBPA.DEZEMBRO.getExtensao();
		return extensao;
	}

	public void limparDialogLiberacao() {
		funcionario = new FuncionarioBean();
		JSFUtil.abrirDialog("dlgLiberacao");
	}

	public void validarSenhaLiberacaoExclusaoDuplicidade() throws ProjetoException, ParseException, SQLException {
		FuncionarioDAO funcionarioDAO = new FuncionarioDAO();

		usuarioLiberacao = funcionarioDAO.validarCpfIhSenha(funcionario.getCpf(), funcionario.getSenha(),
				ValidacaoSenha.LIBERACAO.getSigla());

		if (!VerificadorUtil.verificarSeObjetoNulo(usuarioLiberacao)) {
			new AgendaDAO().excluirAgendamentoPaciente
					(atendimentoSelecionado, usuarioLiberacao, MotivoLiberacao.EXCLUSAO_ATENDIMENTO_DUPLICIDADE.getTitulo());
			listaDuplicidades.remove(atendimentoSelecionado);
			JSFUtil.adicionarMensagemSucesso("Exclusão de duplicidade feita com sucesso", "");
			JSFUtil.fecharDialog("dlgLiberacao");
			if(listaDuplicidades.size() <= 1) {
				JSFUtil.fecharDialog("dlgDuplicidade");
			}
		} else {
			JSFUtil.adicionarMensagemErro(SENHA_ERRADA_OU_SEM_LIBERAÇÃO, "Erro!");
		}
	}

	public void validarSenhaLiberacaoParaIgnorarDuplicidades() throws ProjetoException, ParseException, SQLException {
		FuncionarioDAO funcionarioDAO = new FuncionarioDAO();

		usuarioLiberacao = funcionarioDAO.validarCpfIhSenha(funcionario.getCpf(), funcionario.getSenha(),
				ValidacaoSenha.LIBERACAO.getSigla());

		if (!VerificadorUtil.verificarSeObjetoNulo(usuarioLiberacao)) {
			bpaIndividualizadoDAO.inserirAtendimentoParaIgnorarDuplicidades
					(atendimentoSelecionado, usuarioLiberacao, MotivoLiberacao.MARCACAO_ATENDIMENTO_COMO_SEM_DUPLICIDADE.getTitulo());
			listaDuplicidades.remove(atendimentoSelecionado);
			JSFUtil.adicionarMensagemSucesso("Marcado como sem duplicidade com sucesso", "");
			JSFUtil.fecharDialog("dlgLiberacao");
			if(listaDuplicidades.size() <= 1) {
				JSFUtil.fecharDialog("dlgDuplicidade");
			}
		} else {
			JSFUtil.adicionarMensagemErro(SENHA_ERRADA_OU_SEM_LIBERAÇÃO, "Erro!");
		}
	}

	public String getCompetencia() {
		return competencia;
	}

	public void setCompetencia(String competencia) {
		this.competencia = competencia;
	}

	public List<String> getListaCompetencias() {
		return listaCompetencias;
	}

	public void setListaCompetencias(List<String> listaCompetencias) {
		this.listaCompetencias = listaCompetencias;
	}

	public List<ProcedimentoBean> getListaProcedimentos() {
		return listaProcedimentos;
	}

	public void setListaProcedimentos(List<ProcedimentoBean> listaProcedimentos) {
		this.listaProcedimentos = listaProcedimentos;
	}

	public String getsAtributoGenerico1() {
		return sAtributoGenerico1;
	}

	public void setsAtributoGenerico1(String sAtributoGenerico1) {
		this.sAtributoGenerico1 = sAtributoGenerico1;
	}

	public Boolean getbAtributoGenerico2() {
		return bAtributoGenerico2;
	}

	public void setbAtributoGenerico2(Boolean bAtributoGenerico2) {
		this.bAtributoGenerico2 = bAtributoGenerico2;
	}


	public Integer getNumeroFolhaFiltro() {
		return numeroFolhaFiltro;
	}

	public void setNumeroFolhaFiltro(Integer numeroFolhaFiltro) {
		this.numeroFolhaFiltro = numeroFolhaFiltro;
	}

	public Boolean getInformaNumeroFolhaFiltro() {
		return informaNumeroFolhaFiltro;
	}

	public void setInformaNumeroFolhaFiltro(Boolean informaNumeroFolhaFiltro) {
		this.informaNumeroFolhaFiltro = informaNumeroFolhaFiltro;
	}

	public List<String> getListaInconsistencias() {
		return listaInconsistencias;
	}

	public void setListaInconsistencias(List<String> listaInconsistencias) {
		this.listaInconsistencias = listaInconsistencias;
	}

	public String getFormatoExportacao() {
		return formatoExportacao;
	}

	public void setFormatoExportacao(String formatoExportacao) {
		this.formatoExportacao = formatoExportacao;
	}

	public String getTipoExportacao() {
		return tipoExportacao;
	}

	public void setTipoExportacao(String tipoExportacao) {
		this.tipoExportacao = tipoExportacao;
	}

	public Integer getIdConfiguracaoProducaoBpa() {
		return idConfiguracaoProducaoBpa;
	}

	public void setIdConfiguracaoProducaoBpa(Integer idConfiguracaoProducaoBpa) {
		this.idConfiguracaoProducaoBpa = idConfiguracaoProducaoBpa;
	}

	public List<AtendimentoBean> getListaDuplicidades() {
		return listaDuplicidades;
	}

	public void setListaDuplicidades(List<AtendimentoBean> listaDuplicidades) {
		this.listaDuplicidades = listaDuplicidades;
	}

	public FuncionarioBean getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(FuncionarioBean funcionario) {
		this.funcionario = funcionario;
	}

	public AtendimentoBean getAtendimentoSelecionado() {
		return atendimentoSelecionado;
	}

	public void setAtendimentoSelecionado(AtendimentoBean atendimentoSelecionado) {
		this.atendimentoSelecionado = atendimentoSelecionado;
	}

	public String getTipoLiberacao() {
		return tipoLiberacao;
	}

	public void setTipoLiberacao(String tipoLiberacao) {
		this.tipoLiberacao = tipoLiberacao;
	}

}
