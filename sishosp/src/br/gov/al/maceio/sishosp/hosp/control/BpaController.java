package br.gov.al.maceio.sishosp.hosp.control;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import br.gov.al.maceio.sishosp.acl.model.FuncionarioBean;
import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.JSFUtil;
import br.gov.al.maceio.sishosp.comum.util.VerificadorUtil;
import br.gov.al.maceio.sishosp.hosp.dao.AtendimentoDAO;
import br.gov.al.maceio.sishosp.hosp.dao.BpaConsolidadoDAO;
import br.gov.al.maceio.sishosp.hosp.dao.BpaIndividualizadoDAO;
import br.gov.al.maceio.sishosp.hosp.dao.ProgramaDAO;
import br.gov.al.maceio.sishosp.hosp.enums.CamposBpaCabecalho;
import br.gov.al.maceio.sishosp.hosp.enums.CamposBpaConsolidado;
import br.gov.al.maceio.sishosp.hosp.enums.CamposBpaIndividualizados;
import br.gov.al.maceio.sishosp.hosp.enums.MesExtensaoArquivoBPA;
import br.gov.al.maceio.sishosp.hosp.model.BpaCabecalhoBean;
import br.gov.al.maceio.sishosp.hosp.model.BpaConsolidadoBean;
import br.gov.al.maceio.sishosp.hosp.model.BpaIndividualizadoBean;
import br.gov.al.maceio.sishosp.hosp.model.ProgramaBean;

@ManagedBean
@ViewScoped
public class BpaController {
	
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
	
	private static final String NOME_ARQUIVO = "BPA_layout_Importacao";
	private static final String PASTA_RAIZ = "/WEB-INF/documentos/";
	private String descricaoArquivo;
	
	private Date dataInicioAtendimento;
	private Date dataFimAtendimento;
	private String competencia;
	private List<String> listaCompetencias;
	private String extensao;
	private FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
			.getSessionMap().get("obj_usuario");
	
	public BpaController() {
		this.bpaCabecalho = new BpaCabecalhoBean();
		this.bpaConsolidadoDAO = new BpaConsolidadoDAO();
		this.bpaIndividualizadoDAO = new BpaIndividualizadoDAO();
		this.listaDeBpaConsolidado = new ArrayList<BpaConsolidadoBean>();
		this.listaDeBpaIndividualizado = new ArrayList<BpaIndividualizadoBean>();
		limparDadosLayoutGerado();
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
	
	public void gerarLayoutBpaImportacao() throws ProjetoException, ParseException, SQLException {
		try {
			limparDadosLayoutGerado();
			this.competencia = formataCompetenciaParaBanco();
			setaDataInicioIhFimAtendimento(this.competencia);
			executaMetodosParaGerarBpaConsolidado();
			executaMetodosParaGerarBpaIndividualizado();
			executaMetodosParaGerarBpaCabecalho();
			
			if (!existeInconsistencias()) {
				adicionarCabecalho();
				adicionarLinhasBpaConsolidado();
				adicionarLinhasBpaIndividualizado();
				this.extensao = gerarExtensaoArquivo(this.competencia);
				this.descricaoArquivo = NOME_ARQUIVO + extensao;
				String caminhoIhArquivo = PASTA_RAIZ + NOME_ARQUIVO + extensao;

				Path file = Paths.get(this.getServleContext().getRealPath(caminhoIhArquivo) + File.separator);
				Files.write(file, this.linhasLayoutImportacao, StandardCharsets.UTF_8).getFileSystem();
			}
		} catch (IOException ioe) {
			JSFUtil.adicionarMensagemErro(ioe.getMessage(), "Erro");
			ioe.printStackTrace();
		} catch (ProjetoException pe) {
			JSFUtil.adicionarMensagemErro(pe.getMessage(), "");
		}
	}
	
	private boolean existeInconsistencias() throws SQLException, ProjetoException {
		if (verificarInconsistenciaPrograma() || verificarInconsistenciaQuantidadeAtendimentos())
			return true;
		return false;
	}
	
	private boolean verificarInconsistenciaPrograma() throws SQLException, ProjetoException {
		ProgramaBean programa = new ProgramaDAO().retornarProgramaInconsistente();
		if(!VerificadorUtil.verificarSeObjetoNulo(programa)) {
			if(VerificadorUtil.verificarSeObjetoNuloOuZero(programa.getIdServico())
					&& VerificadorUtil.verificarSeObjetoNuloOuZero(programa.getIdClassificacao()) ) {
				JSFUtil.adicionarMensagemErro
					( "O programa " +programa.getDescPrograma()+" não possui serviço e classificação corrija essa inconsistência", "Erro");
			}
			else if (VerificadorUtil.verificarSeObjetoNuloOuZero(programa.getIdServico())) {
				JSFUtil.adicionarMensagemErro ( "O programa " +programa.getDescPrograma()+" não possui serviço corrija essa inconsistência", "Erro");
			}
			else {
				JSFUtil.adicionarMensagemErro ( "O programa " +programa.getDescPrograma()+" não possui classificação corrija essa inconsistência", "Erro");
			}
			return true;
		}
		return false;
	}
	
	private boolean verificarInconsistenciaQuantidadeAtendimentos() throws ProjetoException {
		Integer totalAtendimentos = new AtendimentoDAO().retornaTotalAtendimentosDeUmPeriodo(this.dataInicioAtendimento, this.dataFimAtendimento);
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
		this.bpaCabecalho = new BpaCabecalhoBean();
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

	private void executaMetodosParaGerarBpaCabecalho() throws ProjetoException {
		gerarCabecalho();
		adicionaCaracteresEmCamposBpaCabecalhoOndeTamanhoNaoEhValido();
	}

	private void adicionarCabecalho() {
		this.linhasLayoutImportacao.add(bpaCabecalho.toString());
	}

	private void gerarCabecalho() throws ProjetoException {
		this.bpaCabecalho.setCbcMvm(this.competencia);
		
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

	private void executaMetodosParaGerarBpaConsolidado() throws ProjetoException {
		buscaBpasConsolidadosDoProcedimento(this.dataInicioAtendimento, this.dataFimAtendimento, this.competencia);
		geraNumeroDaFolhaConsolidado();
		geraNumeroDaLinhaDaFolhaConsolidado();
		adicionaCaracteresEmCamposBpaConsolidadoOndeTamanhoNaoEhValido();
	}

	private void buscaBpasConsolidadosDoProcedimento(Date dataInicio, Date dataFim, String competencia) throws ProjetoException {
		this.listaDeBpaConsolidado = bpaConsolidadoDAO.carregaDadosBpaConsolidado(dataInicio, dataFim, competencia);
	}
	
	public void geraNumeroDaFolhaConsolidado() {
		List<String> listaCbos = retornaUnicamenteCbosParaBpaConsolidado();
		for (String cbo : listaCbos) {
			Integer quantidadeRegistrosPorFolha = 0;
			Integer numeroFolhaBpaConsolidado = 1;
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
                if (this.listaDeBpaConsolidado.get(i).getPrdCbo().equals(cboAuxiliador)
                        && this.listaDeBpaConsolidado.get(i).getPrdFlh().equals(numeroFolhaAuxiliador)) {
                    numeroLinha++;
                    this.listaDeBpaConsolidado.get(i).setPrdSeq(numeroLinha.toString());
                } else {
                    cboAuxiliador = this.listaDeBpaConsolidado.get(i).getPrdCbo();
                    numeroFolhaAuxiliador = this.listaDeBpaConsolidado.get(i).getPrdFlh();
                    numeroLinha = 1;
                    this.listaDeBpaConsolidado.get(i).setPrdSeq(numeroLinha.toString());
                }
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

	private void executaMetodosParaGerarBpaIndividualizado() throws ProjetoException {
		buscaBpasIndividualizadosDoProcedimento(this.dataInicioAtendimento, this.dataFimAtendimento, this.competencia);
		geraNumeroDaFolhaIndividualizado();
		geraNumeroDaLinhaDaFolhaIndividualizado();
		adicionaCaracteresEmCamposBpaIndividualizadoOndeTamanhoNaoEhValido();
	}
	
	public void buscaBpasIndividualizadosDoProcedimento(Date dataInicio, Date dataFim, String competencia) throws ProjetoException {
		this.listaDeBpaIndividualizado = bpaIndividualizadoDAO.carregaDadosBpaIndividualizado(dataInicio, dataFim, competencia);
	}
	
	public void geraNumeroDaFolhaIndividualizado() {
		if ((listaDeBpaIndividualizado!=null) && (listaDeBpaIndividualizado.size()>0)) {
            List<String> listaCnsDosMedicos = retornaUnicamenteCnsParaBpaIndividualizado();
            for (String cnsMedico : listaCnsDosMedicos) {
                Integer quantidadeRegistrosPorFolha = 0;
                Integer numeroFolhaBpaIndividualizado = 1;
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
        }
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
	
}
