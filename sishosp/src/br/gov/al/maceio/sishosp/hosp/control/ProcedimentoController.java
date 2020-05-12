package br.gov.al.maceio.sishosp.hosp.control;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.math.BigInteger;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.xml.ws.soap.SOAPFaultException;
import org.primefaces.model.UploadedFile;

import br.gov.al.maceio.sishosp.acl.model.FuncionarioBean;
import br.gov.al.maceio.sishosp.comum.enums.TipoCabecalho;
import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.JSFUtil;
import br.gov.al.maceio.sishosp.comum.util.RedirecionarUtil;
import br.gov.al.maceio.sishosp.comum.util.VerificadorUtil;
import br.gov.al.maceio.sishosp.hosp.dao.ProcedimentoDAO;
import br.gov.al.maceio.sishosp.hosp.dao.RecursoDAO;
import br.gov.al.maceio.sishosp.hosp.enums.DocumentosRLImportacaoSigtap;
import br.gov.al.maceio.sishosp.hosp.enums.DocumentosTBImportacaoSigtap;
import br.gov.al.maceio.sishosp.hosp.model.CboBean;
import br.gov.al.maceio.sishosp.hosp.model.CidBean;
import br.gov.al.maceio.sishosp.hosp.model.HistoricoSigtapBean;
import br.gov.al.maceio.sishosp.hosp.model.ProcedimentoBean;
import br.gov.al.maceio.sishosp.hosp.model.RecursoBean;
import br.gov.al.maceio.sishosp.hosp.model.dto.DescricaoProcedimentoDTO;
import br.gov.al.maceio.sishosp.hosp.model.dto.GravarProcedimentoMensalDTO;
import br.gov.al.maceio.sishosp.hosp.model.dto.PropriedadeDeProcedimentoMensalExistenteDTO;
import br.gov.al.maceio.sishosp.hosp.model.dto.RelacaoObjetoComProcedimentoDTO;
import sigtap.br.gov.al.maceio.sigtapclient.util.ProcedimentoUtil;
import sigtap.br.gov.saude.servicos.schema.cbo.v1.cbo.CBOType;
import sigtap.br.gov.saude.servicos.schema.sigtap.procedimento.financiamento.v1.tipofinanciamento.TipoFinanciamentoType;
import sigtap.br.gov.saude.servicos.schema.sigtap.procedimento.nivelagregacao.v1.formaorganizacao.FormaOrganizacaoType;
import sigtap.br.gov.saude.servicos.schema.sigtap.procedimento.nivelagregacao.v1.grupo.GrupoType;
import sigtap.br.gov.saude.servicos.schema.sigtap.procedimento.nivelagregacao.v1.subgrupo.SubgrupoType;
import sigtap.br.gov.saude.servicos.schema.sigtap.procedimento.renases.v1.renases.RENASESType;
import sigtap.br.gov.saude.servicos.schema.sigtap.procedimento.servicoclassificacao.v1.servico.ServicoType;
import sigtap.br.gov.saude.servicos.schema.sigtap.procedimento.servicoclassificacao.v1.servicoclassificacao.ServicoClassificacaoType;
import sigtap.br.gov.saude.servicos.schema.sigtap.procedimento.v1.instrumentoregistro.InstrumentoRegistroType;
import sigtap.br.gov.saude.servicos.schema.sigtap.procedimento.v1.modalidadeatendimento.ModalidadeAtendimentoType;
import sigtap.br.gov.saude.servicos.schema.sigtap.procedimento.v1.procedimento.ProcedimentoType.CBOsVinculados;
import sigtap.br.gov.saude.servicos.schema.sigtap.procedimento.v1.procedimento.ProcedimentoType.CIDsVinculados;
import sigtap.br.gov.saude.servicos.schema.sigtap.procedimento.v1.procedimento.ProcedimentoType.CIDsVinculados.CIDVinculado;
import sigtap.br.gov.saude.servicos.schema.sigtap.procedimento.v1.procedimento.ProcedimentoType.InstrumentosRegistro;
import sigtap.br.gov.saude.servicos.schema.sigtap.procedimento.v1.procedimento.ProcedimentoType.ModalidadesAtendimento;
import sigtap.br.gov.saude.servicos.schema.sigtap.procedimento.v1.procedimento.ProcedimentoType.RENASESVinculadas;
import sigtap.br.gov.saude.servicos.schema.sigtap.procedimento.v1.procedimento.ProcedimentoType.ServicosClassificacoesVinculados;
import sigtap.br.gov.saude.servicos.schema.sigtap.procedimento.v1.procedimento.ProcedimentoType;
import sigtap.br.gov.saude.servicos.sigtap.v1.procedimentoservice.RequestDetalharProcedimento;
import sigtap.br.gov.saude.servicos.sigtap.v1.procedimentoservice.RequestDetalharProcedimento.DetalhesAdicionais;
import sigtap.br.gov.saude.servicos.sigtap.v1.procedimentoservice.SIGTAPFault;
import sigtap.br.gov.saude.servicos.wsdl.mensageria.sigtap.v1.detalheadicional.CategoriaDetalheAdicionalType;
import sigtap.br.gov.saude.servicos.wsdl.mensageria.sigtap.v1.detalheadicional.DetalheAdicionalType;
import sigtap.br.gov.saude.servicos.wsdl.mensageria.v1.paginacao.PaginacaoType;
import sigtap.br.gov.saude.servicos.wsdl.mensageria.v1r0.resultadosdetalhaprocedimentos.ResultadosDetalhaProcedimentosType;

@ManagedBean(name = "ProcedimentoController")
@ViewScoped
public class ProcedimentoController implements Serializable {

	private static final long serialVersionUID = 1L;
    private ProcedimentoBean proc;
    private List<ProcedimentoBean> listaProcedimentos;
    private Integer tipo;
    private String cabecalho;
    private CidBean cid;
    private CboBean cbo;
    private RecursoBean recurso;
    private ProcedimentoDAO procedimentoDao = new ProcedimentoDAO();
    private String tipoBusca;
    private String campoBusca;
    
    //CONSTANTES
    private static final String ENDERECO_CADASTRO = "cadastroProcedimento?faces-redirect=true";
    private static final String ENDERECO_TIPO = "&amp;tipo=";
    private static final String ENDERECO_ID = "&amp;id=";
    private static final String CABECALHO_INCLUSAO = "Inclus�o de Procedimento";
    private static final String CABECALHO_ALTERACAO = "Altera��o de Procedimento";
    //SIGTAP
    private static final String VALOR_PADRAO_PARAMETRO_NAO_USADO = "0";
    private static final Integer QUANTIDADE_FILTROS_DETALHAR_PROCEDIMENTO = 5;
    private static final Integer QUANTIDADE_MAXIMA_REGISTROS_POR_BUSCA = 20; 
    private static final BigInteger REGISTRO_INICIAL = BigInteger.ONE;
    private static final Integer VALOR_ZERO = 0;
    
    private List<PropriedadeDeProcedimentoMensalExistenteDTO> listaModalidadeAtendimentoExistente;
    private List<PropriedadeDeProcedimentoMensalExistenteDTO> listaInstrumentosRegistroExistentes;
    private List<PropriedadeDeProcedimentoMensalExistenteDTO> listaCBOsExistentes;
    private List<PropriedadeDeProcedimentoMensalExistenteDTO> listaCidsExistentes;
    private List<PropriedadeDeProcedimentoMensalExistenteDTO> listaRenasesExistentes;
    private List<PropriedadeDeProcedimentoMensalExistenteDTO> listaFormasDeOrganizacaoExistentes;
    private List<PropriedadeDeProcedimentoMensalExistenteDTO> listaTipoFinanciamentoExistente;
    
    private List<GravarProcedimentoMensalDTO> listaGravarProcedimentosMensaisDTO;
    private GravarProcedimentoMensalDTO gravarProcedimentoMensalDTO;
    
    private List<ModalidadeAtendimentoType> listaModalidadeAtendimentoNaoGravadosNoBanco; //*
    private List<InstrumentoRegistroType> listaInstrumentosRegistroNaoGravadosNoBanco;
    private List<CBOType> listaCBOsNaoGravadosNoBanco;
    private List<CIDVinculado> listaCidsNaoGravadosNoBanco;
    private FormaOrganizacaoType formaOrganizacaoNaoGravadaNoBanco;
    private List<RENASESType> listaRenasesNaoGravadosNoBanco;
    private TipoFinanciamentoType tipoFinanciamentoNaoGravadoNoBanco;
    private List<ServicoClassificacaoType> listaServicosClassificacao;
    private String descricaoProcedimentoMensal;
    
    private List<Integer> listaIdModalidadeAtendimentoExistente;
    private List<Integer> listaIdInstrumentosRegistroExistente;
    private List<Integer> listaIdCBOsExistente;
    private List<Integer> listaIdCidsExistente;
    private List<Integer> listaIdRenasesExistente;
    private Integer idFormasDeOrganizacaoExistente;
    private Integer idTipoFinanciamentoExistente;
	private List<HistoricoSigtapBean> listaHistoricoDoSigtap;
	
	//SIGTAP IMPORTACAO ARQUIVO
	private UploadedFile arquivoImportacaoSelecionado;
	private static final String PASTA_RAIZ = "/WEB-INF/documentos/";
    private static final String EXTENSAO_TXT = ".txt";
    private static final String CONTENT_TYPE_ZIP = "application/x-zip-compressed";
    private List<CIDVinculado> listaCidDoArquivo;
    private List<DescricaoProcedimentoDTO> listaDescricaoProcedimentoDoArquivo;
    private List<TipoFinanciamentoType> listaTipoFinanciamentoDoArquivo;
    private List<FormaOrganizacaoType> listaFormaOrganizacaoDoArquivo;
    private List<GrupoType> listaGrupoDoArquivo;
    private List<ModalidadeAtendimentoType> listaModalidadeAtendimentoDoArquivo;
    private List<CBOType> listaCbosDoArquivo;
    private List<ProcedimentoType> listaProcedimentosDoArquivo;
    private List<InstrumentoRegistroType> listaInstrumentoRegistroDoArquivo;
    private List<RENASESType> listaRenasesDoArquivo;
    private List<ServicoType> listaServicoDoArquivo;
    private List<ServicoClassificacaoType> listaServicoClassificacaoDoArquivo;
    private List<SubgrupoType> listaSubgrupoDoArquivo;
    private List<RelacaoObjetoComProcedimentoDTO> listaRelacoesProcedimetoCid;
    private List<RelacaoObjetoComProcedimentoDTO> listaRelacoesProcedimetoModalidade;
    private List<RelacaoObjetoComProcedimentoDTO> listaRelacoesProcedimetoCbo;
    private List<RelacaoObjetoComProcedimentoDTO> listaRelacoesProcedimetoInstrumentoRegistro;
    private List<RelacaoObjetoComProcedimentoDTO> listaRelacoesProcedimetoRenases;
    private List<RelacaoObjetoComProcedimentoDTO> listaRelacoesProcedimetoServico;
    
    public ProcedimentoController() {
        this.proc = new ProcedimentoBean();
        this.listaProcedimentos = null;
        cid = new CidBean();
        cbo = new CboBean();
        recurso = new RecursoBean();
        
        //SIGTAP
        this.listaModalidadeAtendimentoExistente = new ArrayList();
        this.listaInstrumentosRegistroExistentes = new ArrayList();
        this.listaCBOsExistentes = new ArrayList();
        this.listaCidsExistentes = new ArrayList();
        this.listaFormasDeOrganizacaoExistentes = new ArrayList();
        this.listaRenasesExistentes = new ArrayList();
        this.listaTipoFinanciamentoExistente = new ArrayList();
        this.listaGravarProcedimentosMensaisDTO = new ArrayList();
        this.listaHistoricoDoSigtap = new ArrayList();
        limparListasDadosProcedimentos();
    }

	private void limparListasDadosProcedimentos() {
        this.gravarProcedimentoMensalDTO = new GravarProcedimentoMensalDTO();
		this.listaModalidadeAtendimentoNaoGravadosNoBanco = new ArrayList();
        this.listaInstrumentosRegistroNaoGravadosNoBanco = new ArrayList();
        this.listaCBOsNaoGravadosNoBanco = new ArrayList();
        this.listaCidsNaoGravadosNoBanco = new ArrayList();
        this.formaOrganizacaoNaoGravadaNoBanco = new FormaOrganizacaoType();
        this.listaRenasesNaoGravadosNoBanco = new ArrayList();
        this.tipoFinanciamentoNaoGravadoNoBanco = new TipoFinanciamentoType();
        this.listaServicosClassificacao = new ArrayList();
        this.descricaoProcedimentoMensal = new String();
        this.listaIdModalidadeAtendimentoExistente = new ArrayList();
        this.listaIdInstrumentosRegistroExistente = new ArrayList();
        this.listaIdCBOsExistente = new ArrayList();
        this.listaIdCidsExistente = new ArrayList();
        this.listaIdRenasesExistente = new ArrayList();
        this.idFormasDeOrganizacaoExistente = null;
        this.idTipoFinanciamentoExistente = null;
	}

    public String redirectEdit() {
        return RedirecionarUtil.redirectEdit(ENDERECO_CADASTRO, ENDERECO_ID, this.proc.getIdProc(), ENDERECO_TIPO, tipo);
    }

    public String redirectInsert() {
        return RedirecionarUtil.redirectInsert(ENDERECO_CADASTRO, ENDERECO_TIPO, tipo);
    }

    public void getEditProcedimento() throws ProjetoException {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Map<String, String> params = facesContext.getExternalContext()
                .getRequestParameterMap();
        if (params.get("id") != null) {
            Integer id = Integer.parseInt(params.get("id"));
            tipo = Integer.parseInt(params.get("tipo"));
            this.proc = procedimentoDao.listarProcedimentoPorId(id);
            proc.setListaCid(procedimentoDao.listarCid(id));
            proc.setListaCbo(procedimentoDao.listarCbo(id));
            RecursoDAO rDao = new RecursoDAO();
            proc.setListaRecurso(rDao.listaRecursosPorProcedimento(id));
        } else {
            tipo = Integer.parseInt(params.get("tipo"));

        }

    }

    public void limparDados() throws ProjetoException {
        proc = new ProcedimentoBean();
        listaProcedimentos = new ArrayList<ProcedimentoBean>();
        listaProcedimentos = procedimentoDao.listarProcedimento();
        cid = new CidBean();
        cbo = new CboBean();
        recurso = new RecursoBean();
    }

    public void gravarProcedimento() throws ProjetoException, SQLException {

        boolean cadastrou = procedimentoDao.gravarProcedimento(proc);

        if (cadastrou == true) {
            limparDados();
            JSFUtil.adicionarMensagemSucesso("Procedimento cadastrado com sucesso!", "Sucesso");
        } else {
            JSFUtil.adicionarMensagemErro("Ocorreu um erro durante o cadastro!", "Erro");
        }
    }

    public void alterarProcedimento() throws ProjetoException {

        boolean alterou = procedimentoDao.alterarProcedimento(proc);

        if (alterou == true) {
            JSFUtil.adicionarMensagemSucesso("Procedimento alterado com sucesso!", "Sucesso");
            listaProcedimentos = procedimentoDao.listarProcedimento();
        } else {
            JSFUtil.adicionarMensagemErro("Ocorreu um erro durante a alteração!", "Erro");
        }

    }

    public void excluirProcedimento() throws ProjetoException {

        boolean excluiu = procedimentoDao.excluirProcedimento(proc);

        if (excluiu == true) {
            JSFUtil.adicionarMensagemSucesso("Procedimento excluído com sucesso!", "Sucesso");
            JSFUtil.fecharDialog("dialogExclusao");
        } else {
            JSFUtil.adicionarMensagemErro("Ocorreu um erro durante a exclusão!", "Erro");
            JSFUtil.fecharDialog("dialogExclusao");
        }
        listaProcedimentos = procedimentoDao.listarProcedimento();
    }

    public void addCid() {
        boolean existe = false;
        if (proc.getListaCid().size() == 0 && cid.getIdCid() > 0) {
            proc.getListaCid().add(cid);
        } else {
            for (int i = 0; i < proc.getListaCid().size(); i++) {
                if (proc.getListaCid().get(i).getIdCid() == cid.getIdCid()) {
                    existe = true;
                }
            }
            if (existe == false) {
                this.proc.getListaCid().add(this.cid);
            } else {
                JSFUtil.adicionarMensagemAdvertencia("Essa CID já foi adicionado!", "Advertência");
            }
        }
        cid = new CidBean();
    }

    public void removerCid() {
        proc.getListaCid().remove(cid);
    }

    public void addCbo() {
        boolean existe = false;
        if (proc.getListaCbo().size() == 0 && cbo.getCodCbo() > 0) {
            proc.getListaCbo().add(cbo);
        } else {
            for (int i = 0; i < proc.getListaCbo().size(); i++) {
                if (proc.getListaCbo().get(i).getCodCbo() == cbo.getCodCbo()) {
                    existe = true;
                }
            }
            if (existe == false) {
                this.proc.getListaCbo().add(this.cbo);
            } else {
                JSFUtil.adicionarMensagemAdvertencia("Essa CBO já foi adicionado!", "Advertência");
            }
        }
        cbo = new CboBean();
    }

    public void removerCbo() {
        proc.getListaCbo().remove(cbo);
    }

    public String getCabecalho() {
        if (this.tipo.equals(TipoCabecalho.INCLUSAO.getSigla())) {
            cabecalho = CABECALHO_INCLUSAO;
        } else if (this.tipo.equals(TipoCabecalho.ALTERACAO.getSigla())) {
            cabecalho = CABECALHO_ALTERACAO;
        }
        return cabecalho;
    }

    public void addRecurso() {
        boolean existe = false;
        if (proc.getListaRecurso().size() == 0 && recurso.getIdRecurso() > 0) {
            proc.getListaRecurso().add(recurso);
        } else {
            for (int i = 0; i < proc.getListaRecurso().size(); i++) {
                if (proc.getListaRecurso().get(i).getIdRecurso() == recurso.getIdRecurso()) {
                    existe = true;
                }
            }
            if (existe == false) {
                this.proc.getListaRecurso().add(this.recurso);
            } else {
                JSFUtil.adicionarMensagemAdvertencia("Essa Recurso já foi adicionado!", "Advertência");
            }
        }
        recurso = new RecursoBean();
    }

    public void removerRecurso() {
        proc.getListaRecurso().remove(recurso);
    }

    public void setCabecalho(String cabecalho) {
        this.cabecalho = cabecalho;
    }
    
    public List<ProcedimentoBean> listaProcedimentoQueGeramLaudoAutoComplete(String query)
            throws ProjetoException {
        List<ProcedimentoBean> result = procedimentoDao.listarProcedimentoQueGeramLaudoBusca(query, 1);
        return result;
    }

    public List<ProcedimentoBean> listaProcedimentoAutoComplete(String query)
            throws ProjetoException {
        List<ProcedimentoBean> result = procedimentoDao.listarProcedimentoBusca(query, 1);
        return result;
    }

    public void buscarProcedimentos() throws ProjetoException {
        this.listaProcedimentos = procedimentoDao.listarProcedimento();

    }

    public void listarProcedimentos() throws ProjetoException {
        this.listaProcedimentos = procedimentoDao.listarProcedimento();
        
    }

    public void buscarProcedimentosPorTipo() throws ProjetoException {
        this.listaProcedimentos = procedimentoDao.buscarProcedimento(campoBusca, tipoBusca);
    }

    public void limparCampoBusca(){
        campoBusca = "";
    }
    
    private FuncionarioBean obterUsuarioDaSessao() {
		FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
				.getSessionMap().get("obj_usuario");
		return user_session;
	}
    
	// **** IMPORTAÇÃO SIGTAP PELO WEBSERVICE ****
    
    public void novaCargaSigtap() throws ProjetoException {
    	listarProcedimentos();
    	if(this.listaProcedimentos.isEmpty())
    		JSFUtil.adicionarMensagemErro("N�o h� procedimentos cadastrados", "Erro");
		else {
			if (!verificaSeHouveCargaDoSigtapEsteMes()) {	

				FuncionarioBean user_session = obterUsuarioDaSessao();
				Integer idHistoricoConsumoSigtap = VALOR_ZERO;

				for (int i = 0; i < this.listaProcedimentos.size(); i++) {

					try {
						buscaCodigosDeDadosExistentesNoBanco();
						selecionaDetalheAdicionalEmSequencia(this.listaProcedimentos.get(i));
						setaDadosParaListaProcedimentosMensalDTO(i);
						limparListasDadosProcedimentos();
						idHistoricoConsumoSigtap = procedimentoDao.executaRotinaNovaCargaSigtap(
								listaGravarProcedimentosMensaisDTO.get(i), user_session.getId(),
								idHistoricoConsumoSigtap);
					}
					catch (SOAPFaultException soape) {
						JSFUtil.adicionarMensagemErro
						("Erro, algo de inesperado ocorreu durante a carga do procedimento "+this.listaProcedimentos.get(i).getCodProc()
								+" verifique se o c�digo do procedimento est� correto"
								+ " e execute a altera��o do procedimento para que seja poss�vel realizar uma nova"
								+ " carga do SIGTAP este m�s", "");
						soape.printStackTrace();
					}
					catch(SQLException sqle) {
						JSFUtil.adicionarMensagemErro(sqle.getMessage(), "Erro");
						sqle.printStackTrace();
					}
					catch (Exception e) {
						JSFUtil.adicionarMensagemErro("Erro, algo inesperado ocorreu", "Erro");
						e.printStackTrace();
					}
				}
				listaHistoricoSigtap();
				fecharDialogAvisoCargaSigtap();
				JSFUtil.adicionarMensagemSucesso("Dados atualizados com sucesso!", "");
				this.listaGravarProcedimentosMensaisDTO.clear();
			}
		}
    }
    
    private Boolean verificaSeHouveCargaDoSigtapEsteMes() {
    	try {
    		Boolean houveCargaEsteMes = procedimentoDao.houveCargaDoSigtapEsteMes();
    		if(houveCargaEsteMes) {
    			fecharDialogAvisoCargaSigtap();
        		JSFUtil.adicionarMensagemAdvertencia("O sistema est� atualizado, uma carga j� foi executada este m�s", "");
        		return houveCargaEsteMes;
        	}
		} catch (Exception e) {
			JSFUtil.adicionarMensagemErro(e.getMessage(), "Erro");
			e.printStackTrace();
		}
    	
    	return false;
    }

	private void setaDadosParaListaProcedimentosMensalDTO(int i) {
		
		listaGravarProcedimentosMensaisDTO.get(i).getProcedimentoMensal().getModalidadesAtendimento().
			getModalidadeAtendimento().clear();
		listaGravarProcedimentosMensaisDTO.get(i).getProcedimentoMensal().getModalidadesAtendimento().
			getModalidadeAtendimento().addAll(listaModalidadeAtendimentoNaoGravadosNoBanco);
		
		listaGravarProcedimentosMensaisDTO.get(i).getProcedimentoMensal().getInstrumentosRegistro().
			getInstrumentoRegistro().clear();
		listaGravarProcedimentosMensaisDTO.get(i).getProcedimentoMensal().getInstrumentosRegistro().
			getInstrumentoRegistro().addAll(listaInstrumentosRegistroNaoGravadosNoBanco);
		
		listaGravarProcedimentosMensaisDTO.get(i).getProcedimentoMensal().getCBOsVinculados().getCBO().clear();
		listaGravarProcedimentosMensaisDTO.get(i).getProcedimentoMensal().getCBOsVinculados().getCBO()
			.addAll(listaCBOsNaoGravadosNoBanco);
		
		listaGravarProcedimentosMensaisDTO.get(i).getProcedimentoMensal().getCIDsVinculados().
			getCIDVinculado().clear();
		listaGravarProcedimentosMensaisDTO.get(i).getProcedimentoMensal().getCIDsVinculados().
			getCIDVinculado().addAll(listaCidsNaoGravadosNoBanco);
		
		listaGravarProcedimentosMensaisDTO.get(i).getProcedimentoMensal().getRENASESVinculadas().getRENASES().clear();
		listaGravarProcedimentosMensaisDTO.get(i).getProcedimentoMensal().getRENASESVinculadas().getRENASES().
			addAll(listaRenasesNaoGravadosNoBanco);
		
		listaGravarProcedimentosMensaisDTO.get(i).getProcedimentoMensal().setDescricao(descricaoProcedimentoMensal);
		
		listaGravarProcedimentosMensaisDTO.get(i).getProcedimentoMensal().getServicosClassificacoesVinculados().
			getServicoClassificacao().clear();
		listaGravarProcedimentosMensaisDTO.get(i).getProcedimentoMensal().getServicosClassificacoesVinculados().getServicoClassificacao()
			.addAll(listaServicosClassificacao);
		
		listaGravarProcedimentosMensaisDTO.get(i).getProcedimentoMensal().setFormaOrganizacao(formaOrganizacaoNaoGravadaNoBanco);
		listaGravarProcedimentosMensaisDTO.get(i).getProcedimentoMensal().setTipoFinanciamento(tipoFinanciamentoNaoGravadoNoBanco);
		
		listaGravarProcedimentosMensaisDTO.get(i).getListaIdModalidadeAtendimentoExistente().addAll(listaIdModalidadeAtendimentoExistente);
		listaGravarProcedimentosMensaisDTO.get(i).getListaIdInstrumentosRegistroExistente().addAll(listaIdInstrumentosRegistroExistente);
		listaGravarProcedimentosMensaisDTO.get(i).getListaIdCBOsExistente().addAll(listaIdCBOsExistente);
		listaGravarProcedimentosMensaisDTO.get(i).getListaIdCidsExistente().addAll(listaIdCidsExistente);
		listaGravarProcedimentosMensaisDTO.get(i).getListaIdRenasesExistente().addAll(listaIdRenasesExistente);
		listaGravarProcedimentosMensaisDTO.get(i).setIdFormasDeOrganizacaoExistente(idFormasDeOrganizacaoExistente);
		listaGravarProcedimentosMensaisDTO.get(i).setIdTipoFinanciamentoExistente(idTipoFinanciamentoExistente);
	}
	
	private void fecharDialogAvisoCargaSigtap() {
		JSFUtil.fecharDialog("dlg-aviso");
	}

	private void buscaCodigosDeDadosExistentesNoBanco() throws ProjetoException {
		this.listaModalidadeAtendimentoExistente = procedimentoDao.buscaModalidadeAtendimentoExistente();
		this.listaInstrumentosRegistroExistentes = procedimentoDao.buscaInstrumentosRegistroExistente();
		this.listaCBOsExistentes = procedimentoDao.buscaCbosExistentes();
		this.listaCidsExistentes = procedimentoDao.buscaCidsExistentes();
		this.listaFormasDeOrganizacaoExistentes = procedimentoDao.buscaFormasOrganizacaoExistentes();
		this.listaRenasesExistentes = procedimentoDao.buscaRenasesExistentes();
		this.listaTipoFinanciamentoExistente = procedimentoDao.buscaTiposFinanciamentoExistentes();
	}

	private void selecionaDetalheAdicionalEmSequencia(ProcedimentoBean procedimento) {
		int detalheAdicional = 0;
		//CBOS, CIDS, DESCRICAO, RENASES, SERVICO_CLASSIFICACAO
		while (detalheAdicional < QUANTIDADE_FILTROS_DETALHAR_PROCEDIMENTO) {
			CategoriaDetalheAdicionalType categoriaDetalheAdicional;
			
			if (detalheAdicional == 0)
				categoriaDetalheAdicional = CategoriaDetalheAdicionalType.CBOS;
			else if (detalheAdicional == 1)
				categoriaDetalheAdicional = CategoriaDetalheAdicionalType.CIDS;
			else if (detalheAdicional == 2)
				categoriaDetalheAdicional = CategoriaDetalheAdicionalType.DESCRICAO;
			else if (detalheAdicional == 3)
				categoriaDetalheAdicional = CategoriaDetalheAdicionalType.RENASES;
			else
				categoriaDetalheAdicional = CategoriaDetalheAdicionalType.SERVICOS_CLASSIFICACOES;
			
			detalheAdicional = buscaTodosRegistrosDeUmProcedimentoSeguindoAhRegraDeVinteRegistrosPorVez(procedimento,
					detalheAdicional, categoriaDetalheAdicional);	
		}
	}

	private int buscaTodosRegistrosDeUmProcedimentoSeguindoAhRegraDeVinteRegistrosPorVez(ProcedimentoBean procedimento,
			int detalheAdicional, CategoriaDetalheAdicionalType categoriaDetalheAdicional) {
		try {
			BigInteger totalRegistros = detalharProcedimento(procedimento, REGISTRO_INICIAL, categoriaDetalheAdicional);
			Long registroInicialLong = REGISTRO_INICIAL.longValue();
			registroInicialLong += QUANTIDADE_MAXIMA_REGISTROS_POR_BUSCA;
			
			if(totalRegistros.longValue() > QUANTIDADE_MAXIMA_REGISTROS_POR_BUSCA) {
				while (registroInicialLong < totalRegistros.longValue()) {
					detalharProcedimento(procedimento, new BigInteger(registroInicialLong.toString()), categoriaDetalheAdicional);
					registroInicialLong += QUANTIDADE_MAXIMA_REGISTROS_POR_BUSCA;
				}
			}
			detalheAdicional++;
		} catch (SIGTAPFault e) {
			e.printStackTrace();
		}
		return detalheAdicional;
	}
    
    private BigInteger detalharProcedimento(ProcedimentoBean procedimento, BigInteger registroInicial, 
    		CategoriaDetalheAdicionalType categoriaDetalheAdicional) throws SIGTAPFault {
    	
		RequestDetalharProcedimento requestDetalharProcedimento = new RequestDetalharProcedimento();
		requestDetalharProcedimento.setCodigoProcedimento(procedimento.getCodProc());
		requestDetalharProcedimento.setCompetencia("202004");

		DetalhesAdicionais detalhesAdicionais = new DetalhesAdicionais();

		DetalheAdicionalType detalheAdicionalType = new DetalheAdicionalType();
		detalheAdicionalType.setCategoriaDetalheAdicional(categoriaDetalheAdicional);

		PaginacaoType paginacaoType = new PaginacaoType();
		paginacaoType.setRegistroInicial(registroInicial);
		paginacaoType.setQuantidadeRegistros(QUANTIDADE_MAXIMA_REGISTROS_POR_BUSCA);
		paginacaoType.setTotalRegistros(new BigInteger(VALOR_PADRAO_PARAMETRO_NAO_USADO));

		detalheAdicionalType.setPaginacao(paginacaoType);

		detalhesAdicionais.getDetalheAdicional().add(detalheAdicionalType);

		requestDetalharProcedimento.setDetalhesAdicionais(detalhesAdicionais);

		ResultadosDetalhaProcedimentosType resultadosDetalhaProcedimentosType = ProcedimentoUtil.detalharProcedimentos(requestDetalharProcedimento);

		adicionaProcedimentoParaListaProcedimentoMensalDTO(procedimento, resultadosDetalhaProcedimentosType);
		adicionaDadosFiltradosEmSuasRespectivasVariaveis(categoriaDetalheAdicional, resultadosDetalhaProcedimentosType);

		if (VerificadorUtil
				.verificarSeObjetoNulo(resultadosDetalhaProcedimentosType.getDetalheAdicional().get(0).getPaginacao()))
			return new BigInteger(BigInteger.ZERO.toString());
    		
		
    		
		return resultadosDetalhaProcedimentosType.getDetalheAdicional().get(0).getPaginacao().getTotalRegistros();	
	}
    
	private void adicionaProcedimentoParaListaProcedimentoMensalDTO(ProcedimentoBean procedimento,
			ResultadosDetalhaProcedimentosType resultadosDetalhaProcedimentosType) {
		Integer ultimoIndiceDaListaProcedimentosMensalDTO = listaGravarProcedimentosMensaisDTO.size() - 1;
		
		if(listaGravarProcedimentosMensaisDTO.isEmpty()) {
			gravarProcedimentoMensalDTO.setIdProcedimento(procedimento.getIdProc());
			gravarProcedimentoMensalDTO.setProcedimentoMensal(resultadosDetalhaProcedimentosType.getProcedimento());
			listaGravarProcedimentosMensaisDTO.add(gravarProcedimentoMensalDTO);
			adicionaDadosNaoGravadosEmSuasRespectivasVariaveis(resultadosDetalhaProcedimentosType);
		}
		
		else if(!listaGravarProcedimentosMensaisDTO.get(ultimoIndiceDaListaProcedimentosMensalDTO).getIdProcedimento().equals(procedimento.getIdProc())){
			gravarProcedimentoMensalDTO.setIdProcedimento(procedimento.getIdProc());
			gravarProcedimentoMensalDTO.setProcedimentoMensal(resultadosDetalhaProcedimentosType.getProcedimento());
			listaGravarProcedimentosMensaisDTO.add(gravarProcedimentoMensalDTO);
			adicionaDadosNaoGravadosEmSuasRespectivasVariaveis(resultadosDetalhaProcedimentosType);
		}
	}

	private void adicionaDadosNaoGravadosEmSuasRespectivasVariaveis(
			ResultadosDetalhaProcedimentosType resultadosDetalhaProcedimentosType) {
		adicionaModalidadeAtendimentoNaoGravados(resultadosDetalhaProcedimentosType.getProcedimento().getModalidadesAtendimento().getModalidadeAtendimento());
		adicionaInstrumentoRegistroNaoGravados(resultadosDetalhaProcedimentosType.getProcedimento().getInstrumentosRegistro().getInstrumentoRegistro());		
		adicionaFormaOrganizacaoNaoGravada(resultadosDetalhaProcedimentosType.getProcedimento().getFormaOrganizacao());
		adicionaTipoFinanciamentoNaoGravado(resultadosDetalhaProcedimentosType.getProcedimento().getTipoFinanciamento());
	}

	private void adicionaModalidadeAtendimentoNaoGravados(List<ModalidadeAtendimentoType> listaModalidadesAtendimento) {
		if(listaModalidadeAtendimentoExistente.isEmpty()) {
			listaModalidadeAtendimentoNaoGravadosNoBanco.addAll(listaModalidadesAtendimento);
		}else {
			for(ModalidadeAtendimentoType modalidadeAtendimento : listaModalidadesAtendimento) {				
				Boolean valorExistenteAdicionado = false;
				for (PropriedadeDeProcedimentoMensalExistenteDTO modalidadeExistente : listaModalidadeAtendimentoExistente) {
					if (modalidadeExistente.getCodigo().equals(modalidadeAtendimento.getCodigo())) {
						this.listaIdModalidadeAtendimentoExistente.add(modalidadeExistente.getId());
						valorExistenteAdicionado = true;
					}
				}
				if(!valorExistenteAdicionado)
					listaModalidadeAtendimentoNaoGravadosNoBanco.add(modalidadeAtendimento);	
			}
		}
	}
	
	private void adicionaInstrumentoRegistroNaoGravados(List<InstrumentoRegistroType> listaInstrumentoRegistro) {
		if(listaInstrumentosRegistroExistentes.isEmpty()) {
			listaInstrumentosRegistroNaoGravadosNoBanco.addAll(listaInstrumentoRegistro);
		}else {	
			for(InstrumentoRegistroType instrumentoRegistroType : listaInstrumentoRegistro) {
				Boolean valorExistente = false;
				for(PropriedadeDeProcedimentoMensalExistenteDTO instrumentoRegistroExistente : listaInstrumentosRegistroExistentes) {
					if(instrumentoRegistroExistente.getCodigo().equals(instrumentoRegistroType.getCodigo())) {
						listaIdInstrumentosRegistroExistente.add(instrumentoRegistroExistente.getId());
						valorExistente = true;
					}
				}
				if(!valorExistente)
					listaInstrumentosRegistroNaoGravadosNoBanco.add(instrumentoRegistroType);
			}
		}
	}
	
	private void adicionaFormaOrganizacaoNaoGravada(FormaOrganizacaoType formaOrganizacao) {
		if(listaFormasDeOrganizacaoExistentes.isEmpty()) {
			formaOrganizacaoNaoGravadaNoBanco = formaOrganizacao;
		}else {
			String codigoFormaOrganizacao = formaOrganizacao.getCodigo();
			Boolean valorExistente = false;
			for(PropriedadeDeProcedimentoMensalExistenteDTO formaOrganizacaoExistente : listaFormasDeOrganizacaoExistentes) {
				if (formaOrganizacaoExistente.getCodigo().equals(codigoFormaOrganizacao)) {
					idFormasDeOrganizacaoExistente = formaOrganizacaoExistente.getId();
					valorExistente = true;
				}
			}
			if(!valorExistente)
				formaOrganizacaoNaoGravadaNoBanco = formaOrganizacao;
		}
	}
	
	private void adicionaTipoFinanciamentoNaoGravado(TipoFinanciamentoType tipoFinanciamento) {
		if(listaTipoFinanciamentoExistente.isEmpty()) {
			tipoFinanciamentoNaoGravadoNoBanco = tipoFinanciamento;
		}else {
			String codigoTipoFinanciamento = tipoFinanciamento.getCodigo();
			Boolean valorExistente = false;
			for(PropriedadeDeProcedimentoMensalExistenteDTO tipoFinanciamentoExistente : listaTipoFinanciamentoExistente) {
				if (tipoFinanciamentoExistente.getCodigo().equals(codigoTipoFinanciamento)) {
					idTipoFinanciamentoExistente = tipoFinanciamentoExistente.getId();
					valorExistente = true;
				}
			}
			if (!valorExistente)
				tipoFinanciamentoNaoGravadoNoBanco = tipoFinanciamento;
		}
	}

	private void adicionaDadosFiltradosEmSuasRespectivasVariaveis(
			CategoriaDetalheAdicionalType categoriaDetalheAdicional,
			ResultadosDetalhaProcedimentosType resultadosDetalhaProcedimentosType) {
		if(categoriaDetalheAdicional.equals(CategoriaDetalheAdicionalType.CBOS)) {
			if(listaCBOsExistentes.isEmpty()) {
				listaCBOsNaoGravadosNoBanco.addAll(resultadosDetalhaProcedimentosType.getProcedimento().getCBOsVinculados().getCBO());
			}else {
				for (CBOType cboType : resultadosDetalhaProcedimentosType.getProcedimento().getCBOsVinculados().getCBO()) {
					Boolean valorExistente = false;
					for(PropriedadeDeProcedimentoMensalExistenteDTO cboExistenteDTO : listaCBOsExistentes) {
						if(cboExistenteDTO.getCodigo().equals(cboType.getCodigo())) {
							listaIdCBOsExistente.add(cboExistenteDTO.getId());
							valorExistente = true;
						}
					}
					if (!valorExistente)
						listaCBOsNaoGravadosNoBanco.add(cboType);
				}
			}
		}
		else if(categoriaDetalheAdicional.equals(CategoriaDetalheAdicionalType.CIDS)) {
			
			if(listaCidsExistentes.isEmpty()) {
				listaCidsNaoGravadosNoBanco.addAll
					(resultadosDetalhaProcedimentosType.getProcedimento().getCIDsVinculados().getCIDVinculado());
			}else {
				for (CIDVinculado cidVinculado : resultadosDetalhaProcedimentosType.getProcedimento().getCIDsVinculados().getCIDVinculado()) {
					Boolean valorExistente = false;
					for(PropriedadeDeProcedimentoMensalExistenteDTO cidExistenteDTO : listaCidsExistentes) {
						if(cidExistenteDTO.getCodigo().equals(cidVinculado.getCID().getCodigo())) { 
							listaIdCidsExistente.add(cidExistenteDTO.getId());
							valorExistente = true;
						}
					}
					if(!valorExistente)
						listaCidsNaoGravadosNoBanco.add(cidVinculado);
				}
			}			
		}
		
		else if(categoriaDetalheAdicional.equals(CategoriaDetalheAdicionalType.DESCRICAO))
			this.descricaoProcedimentoMensal = resultadosDetalhaProcedimentosType.getProcedimento().getDescricao();
		
		else if(categoriaDetalheAdicional.equals(CategoriaDetalheAdicionalType.RENASES)) {
			if(listaRenasesExistentes.isEmpty()) {
				listaRenasesNaoGravadosNoBanco.
					addAll(resultadosDetalhaProcedimentosType.getProcedimento().getRENASESVinculadas().getRENASES());
			}else {
				for (RENASESType renasesType : resultadosDetalhaProcedimentosType.getProcedimento().getRENASESVinculadas().getRENASES()) {
					Boolean valorExitente = false;
					for(PropriedadeDeProcedimentoMensalExistenteDTO renasesExistenteDTO : listaRenasesExistentes) {
						if(renasesExistenteDTO.getCodigo().equals(renasesType.getCodigo())) {
							listaIdRenasesExistente.add(renasesExistenteDTO.getId());
							valorExitente = true;
						}	
					}
					if(!valorExitente)
						listaRenasesNaoGravadosNoBanco.add(renasesType);
				}
			}
		}
		
		else if (categoriaDetalheAdicional.equals(CategoriaDetalheAdicionalType.SERVICOS_CLASSIFICACOES)){
			listaServicosClassificacao.addAll
				(resultadosDetalhaProcedimentosType.getProcedimento().getServicosClassificacoesVinculados().getServicoClassificacao());		
		}
	}
	
	public void listaHistoricoSigtap() {
		this.listaHistoricoDoSigtap = procedimentoDao.listaHistoricoCargasDoSigtap();
	}

	public void listarProcedimentosQueGeramLaudo() throws ProjetoException {
        this.listaProcedimentos = procedimentoDao.listarProcedimentoLaudo();
        
    }
	
	// **** IMPORTAÇÃO SIGTAP POR DOCUMENTOS ****
	
	public void verificaSeUploadFoiRealizado() {
		try {
			limparDadosImportacaoDadosDoSigtap();
			validaArquivo();
			Path pathRaiz = (Paths.get(this.getServleContext().getRealPath(PASTA_RAIZ) + File.separator));
			File arquivoZipCriado = salvaArquivoCompactadoNaPastaTemporariaDoProjeto(pathRaiz);
			List<File> arquivosDescompactados = descompactaArquivo(arquivoZipCriado.getPath(), pathRaiz.toString());
			List<File> arquivosTxtDescompactados = retornaArquivosTxtDescompactados(arquivosDescompactados);
			leArquivosDescompactados(arquivosTxtDescompactados);
			JSFUtil.adicionarMensagemSucesso(
					"Upload realizado com sucesso do arquivo " + this.arquivoImportacaoSelecionado.getFileName(), "");
		} catch (IOException ioe) {
			JSFUtil.adicionarMensagemErro("Um erro inexperada ocorreu: " + ioe.getMessage(), "");
			ioe.printStackTrace();
		} catch (ProjetoException pe) {
			pe.printStackTrace();
		}
		
	}
	
	private void limparDadosImportacaoDadosDoSigtap() {
		this.listaGravarProcedimentosMensaisDTO = new ArrayList<GravarProcedimentoMensalDTO>();
	    this.listaCidDoArquivo = new ArrayList<ProcedimentoType.CIDsVinculados.CIDVinculado>();
	    this.listaDescricaoProcedimentoDoArquivo = new ArrayList<DescricaoProcedimentoDTO>();
	    this.listaTipoFinanciamentoDoArquivo = new ArrayList<TipoFinanciamentoType>();
	    this.listaFormaOrganizacaoDoArquivo = new ArrayList<FormaOrganizacaoType>();
	    this.listaGrupoDoArquivo = new ArrayList<GrupoType>();
	    this.listaModalidadeAtendimentoDoArquivo = new ArrayList<ModalidadeAtendimentoType>();
	    this.listaCbosDoArquivo = new ArrayList<CBOType>();
	    this.listaProcedimentosDoArquivo = new ArrayList<ProcedimentoType>();
	    this.listaInstrumentoRegistroDoArquivo = new ArrayList<InstrumentoRegistroType>();
	    this.listaRenasesDoArquivo = new ArrayList<RENASESType>();
	    this.listaServicoDoArquivo = new ArrayList<ServicoType>();
	    this.listaServicoClassificacaoDoArquivo = new ArrayList<ServicoClassificacaoType>();
	    this.listaSubgrupoDoArquivo = new ArrayList<SubgrupoType>();
	    this.listaRelacoesProcedimetoCid = new ArrayList<RelacaoObjetoComProcedimentoDTO>();
	    this.listaRelacoesProcedimetoModalidade = new ArrayList<RelacaoObjetoComProcedimentoDTO>();
	    this.listaRelacoesProcedimetoCbo = new ArrayList<RelacaoObjetoComProcedimentoDTO>();
	    this.listaRelacoesProcedimetoInstrumentoRegistro = new ArrayList<RelacaoObjetoComProcedimentoDTO>();
	    this.listaRelacoesProcedimetoRenases = new ArrayList<RelacaoObjetoComProcedimentoDTO>();
	    this.listaRelacoesProcedimetoServico = new ArrayList<RelacaoObjetoComProcedimentoDTO>();
	}

	private void validaArquivo() throws ProjetoException {
		if (VerificadorUtil.verificarSeObjetoNuloOuZero(this.arquivoImportacaoSelecionado.getContents().length))
			throw new ProjetoException("Nenhum arquivo inserido");
		else if(!this.arquivoImportacaoSelecionado.getContentType().equals(CONTENT_TYPE_ZIP)) 
			throw new ProjetoException("Arquivo inválido, apenas arquivos .zip são permitidos");
	}

	private File salvaArquivoCompactadoNaPastaTemporariaDoProjeto(Path pathRaiz) throws IOException {
		File pastaDocumentos = pathRaiz.toFile();
		if(!pastaDocumentos.exists())
			pastaDocumentos.mkdir();
		
		Path path = Paths.get(
				this.getServleContext().getRealPath(PASTA_RAIZ + this.arquivoImportacaoSelecionado.getFileName()) + File.separator);
		File arquivo = path.toFile();
		
		OutputStream outputStream = new FileOutputStream(arquivo);
		outputStream.write(this.arquivoImportacaoSelecionado.getContents());
		outputStream.close();
		return arquivo;
	}
	
	private ServletContext getServleContext() {
		ServletContext scontext = (ServletContext) this.getFacesContext().getExternalContext().getContext();
		return scontext;
	}
	
	private FacesContext getFacesContext() {
		FacesContext context = FacesContext.getCurrentInstance();
		return context;
	}
	
	private List<File> descompactaArquivo(String zipFile, String outputFolder) {
		 List<File> arquivosDescompactados = new ArrayList<File>();
		 byte[] buffer = new byte[1024];

	     try{
	    	File folder = new File(outputFolder);
	    	if(!folder.exists()){
	    		folder.mkdir();
	    	}

	    	ZipInputStream zisInputStream = new ZipInputStream(new FileInputStream(zipFile));
	    	ZipEntry zipEntry = zisInputStream.getNextEntry();

	    	while(zipEntry!=null){
	    	   String fileName = zipEntry.getName();
	           File novoArquivo = new File(outputFolder + File.separator + fileName);
	           FileOutputStream fileOutputStrem = new FileOutputStream(novoArquivo);

	            int len;
	            while ((len = zisInputStream.read(buffer)) > 0) {
	            	fileOutputStrem.write(buffer, 0, len);
	            }

	            fileOutputStrem.close();
	            zipEntry = zisInputStream.getNextEntry();
	            arquivosDescompactados.add(novoArquivo);
	    	}

	        zisInputStream.closeEntry();
	    	zisInputStream.close();
	    }catch(IOException ex){
	       ex.printStackTrace();
	    }
	    return arquivosDescompactados;
	}
	
	private List<File> retornaArquivosTxtDescompactados(List<File> arquivosDesconpactados) {
		List<File> arquivosTxt = new ArrayList<File>();
		for (File arquivo : arquivosDesconpactados) {
			if(arquivo.getName().contains(EXTENSAO_TXT))
				arquivosTxt.add(arquivo);
		}
		return arquivosTxt;
	}
	
	private void leArquivosDescompactados(List<File> arquivos) throws FileNotFoundException, ProjetoException {
		HashMap<String, List<String>> dadosDosArquivos = new HashMap<String, List<String>>();
		for (File arquivo : arquivos) {			
			String nomeArquivo = arquivo.getName();
			List<String> linhasDoArquivo = new ArrayList<String>();
			
			Scanner scanner = new Scanner(arquivo);
		    while (scanner.hasNextLine()) {
		    	  String linha = scanner.nextLine();
		    	  linhasDoArquivo.add(linha);
			}
		    scanner.close();
		    dadosDosArquivos.put(nomeArquivo, linhasDoArquivo);
		}
		novaCargaSigtapPorDocumentos(dadosDosArquivos);
	}  
	
    public void novaCargaSigtapPorDocumentos(HashMap<String, List<String>> dadosDosArquivos) throws ProjetoException {
    	listarProcedimentos();
    	if(this.listaProcedimentos.isEmpty())
    		JSFUtil.adicionarMensagemErro("Não há procedimentos cadastrados", "Erro");
		else {
			if (!verificaSeHouveCargaDoSigtapEsteMes()) {	

				FuncionarioBean user_session = obterUsuarioDaSessao();
				Integer idHistoricoConsumoSigtap = VALOR_ZERO;
				
				transformaDadosDosArquivosTBEmListasDeObjetos(dadosDosArquivos);
				transformaDadosDosArquivosRLEmListasDeObjetos(dadosDosArquivos);
				buscaCodigosDeDadosExistentesNoBanco();
				removeProcedimentosQueNaoEstaoNoBanco();
				relacionarDadosDoProcedimento();
				////////////////////////
//				for (int i = 0; i < this.listaProcedimentos.size(); i++) {
//
//					try {
//						buscaCodigosDeDadosExistentesNoBanco();
//						selecionaDetalheAdicionalEmSequencia(this.listaProcedimentos.get(i));
//						setaDadosParaListaProcedimentosMensalDTO(i);
//						limparListasDadosProcedimentos();
//						idHistoricoConsumoSigtap = procedimentoDao.executaRotinaNovaCargaSigtap(
//								listaGravarProcedimentosMensaisDTO.get(i), user_session.getId(),
//								idHistoricoConsumoSigtap);
//					}
//					catch (SOAPFaultException soape) {
//						JSFUtil.adicionarMensagemErro
//						("Erro, algo de inesperado ocorreu durante a carga do procedimento "+this.listaProcedimentos.get(i).getCodProc()
//								+" verifique se o c�digo do procedimento est� correto"
//								+ " e execute a altera��o do procedimento para que seja poss�vel realizar uma nova"
//								+ " carga do SIGTAP este m�s", "");
//						soape.printStackTrace();
//					}
//					catch(SQLException sqle) {
//						JSFUtil.adicionarMensagemErro(sqle.getMessage(), "Erro");
//						sqle.printStackTrace();
//					}
//					catch (Exception e) {
//						JSFUtil.adicionarMensagemErro("Erro, algo inesperado ocorreu", "Erro");
//						e.printStackTrace();
//					}
//				}
//				listaHistoricoSigtap();
//				fecharDialogAvisoCargaSigtap();
//				JSFUtil.adicionarMensagemSucesso("Dados atualizados com sucesso!", "");
//				this.listaGravarProcedimentosMensaisDTO.clear();
			}
		}
    }

	private void transformaDadosDosArquivosTBEmListasDeObjetos(HashMap<String, List<String>> dadosDosArquivos) {
		for (String chave : dadosDosArquivos.keySet()) {
			if(chave.equals(DocumentosTBImportacaoSigtap.TB_CID.getSigla())) {
				List<String> linhasDocumentoCids = dadosDosArquivos.get(chave);
				for (String linha : linhasDocumentoCids) {
					this.listaCidDoArquivo.add((CIDVinculado) DocumentosTBImportacaoSigtap.TB_CID.retornarObjectoDaString(linha));
				}
				System.out.println(chave);
			}
			else if(chave.equals(DocumentosTBImportacaoSigtap.TB_DESCRICAO.getSigla())){
				List<String> linhasDocumentoDescricaoProcedimento = dadosDosArquivos.get(chave);
				for (String linha : linhasDocumentoDescricaoProcedimento) {
					this.listaDescricaoProcedimentoDoArquivo.add((DescricaoProcedimentoDTO) DocumentosTBImportacaoSigtap.TB_DESCRICAO.retornarObjectoDaString(linha));
				}
				System.out.println(chave);
			}
			else if(chave.equals(DocumentosTBImportacaoSigtap.TB_FINANCIAMENTO.getSigla())){
				List<String> linhasDocumentoTipoFinanciamento = dadosDosArquivos.get(chave);
				for (String linha : linhasDocumentoTipoFinanciamento) {
					this.listaTipoFinanciamentoDoArquivo.add((TipoFinanciamentoType) DocumentosTBImportacaoSigtap.TB_FINANCIAMENTO.retornarObjectoDaString(linha));
				}						
				System.out.println(chave);
			}
			else if(chave.equals(DocumentosTBImportacaoSigtap.TB_FORMA_ORGANIZACAO.getSigla())){
				List<String> linhasDocumentoFormaOrganizacao = dadosDosArquivos.get(chave);
				for (String linha : linhasDocumentoFormaOrganizacao) {
					this.listaFormaOrganizacaoDoArquivo.add((FormaOrganizacaoType) DocumentosTBImportacaoSigtap.TB_FORMA_ORGANIZACAO.retornarObjectoDaString(linha));
				}
				System.out.println(chave);
			}
			else if(chave.equals(DocumentosTBImportacaoSigtap.TB_GRUPO.getSigla())){
				List<String> linhasDocumentoGrupo = dadosDosArquivos.get(chave);
				for (String linha : linhasDocumentoGrupo) {
					this.listaGrupoDoArquivo.add((GrupoType) DocumentosTBImportacaoSigtap.TB_GRUPO.retornarObjectoDaString(linha));
				}
				System.out.println(chave);
			}
			else if(chave.equals(DocumentosTBImportacaoSigtap.TB_MODALIDADE.getSigla())){
				List<String> linhasDocumentoModalidade = dadosDosArquivos.get(chave);
				for (String linha : linhasDocumentoModalidade) {
					this.listaModalidadeAtendimentoDoArquivo.add((ModalidadeAtendimentoType) DocumentosTBImportacaoSigtap.TB_MODALIDADE.retornarObjectoDaString(linha));
				}
				System.out.println(chave);
			}
			else if(chave.equals(DocumentosTBImportacaoSigtap.TB_CBO.getSigla())){
				List<String> linhasDocumentoCbos = dadosDosArquivos.get(chave);
				for (String linha : linhasDocumentoCbos) {
					this.listaCbosDoArquivo.add((CBOType) DocumentosTBImportacaoSigtap.TB_CBO.retornarObjectoDaString(linha));
				}
				System.out.println(chave);
			}
			else if(chave.equals(DocumentosTBImportacaoSigtap.TB_PROCEDIMENTO.getSigla())){
				List<String> linhasDocumentoProcedimentos = dadosDosArquivos.get(chave);
				for (String linha : linhasDocumentoProcedimentos) {
					this.listaProcedimentosDoArquivo.add((ProcedimentoType) DocumentosTBImportacaoSigtap.TB_PROCEDIMENTO.retornarObjectoDaString(linha));
				}
				System.out.println(chave);
			}
			else if(chave.equals(DocumentosTBImportacaoSigtap.TB_INSTRUMENTO_REGISTRO.getSigla())){
				List<String> linhasDocumentoInstrumentoRegistro = dadosDosArquivos.get(chave);
				for (String linha : linhasDocumentoInstrumentoRegistro) {
					this.listaInstrumentoRegistroDoArquivo.add((InstrumentoRegistroType) DocumentosTBImportacaoSigtap.TB_INSTRUMENTO_REGISTRO.retornarObjectoDaString(linha));
				}
				System.out.println(chave);
			}
			else if(chave.equals(DocumentosTBImportacaoSigtap.TB_RENASES.getSigla())){
				List<String> linhasDocumentoRenases = dadosDosArquivos.get(chave);
				for (String linha : linhasDocumentoRenases) {
					this.listaRenasesDoArquivo.add((RENASESType) DocumentosTBImportacaoSigtap.TB_RENASES.retornarObjectoDaString(linha));
				}
				System.out.println(chave);
			}
			else if(chave.equals(DocumentosTBImportacaoSigtap.TB_SERVICO.getSigla())){
				List<String> linhasDocumentoServico = dadosDosArquivos.get(chave);
				for (String linha : linhasDocumentoServico) {
					this.listaServicoDoArquivo.add((ServicoType) DocumentosTBImportacaoSigtap.TB_SERVICO.retornarObjectoDaString(linha));
				}
				System.out.println(chave);
			}
			else if(chave.equals(DocumentosTBImportacaoSigtap.TB_SERVICO_CLASSIFICACAO.getSigla())){
				List<String> linhasDocumentoServicoClassificacao = dadosDosArquivos.get(chave);
				for (String linha : linhasDocumentoServicoClassificacao) {
					this.listaServicoClassificacaoDoArquivo.add((ServicoClassificacaoType) DocumentosTBImportacaoSigtap.TB_SERVICO_CLASSIFICACAO.retornarObjectoDaString(linha));
				}
				System.out.println(chave);
			}
			else if(chave.equals(DocumentosTBImportacaoSigtap.TB_SUBGRUPO.getSigla())){
				List<String> linhasDocumentoSubgrupo = dadosDosArquivos.get(chave);
				for (String linha : linhasDocumentoSubgrupo) {
					this.listaSubgrupoDoArquivo.add((SubgrupoType) DocumentosTBImportacaoSigtap.TB_SUBGRUPO.retornarObjectoDaString(linha));
				}
				System.out.println(chave);
			}
		}
	}
	
	private void transformaDadosDosArquivosRLEmListasDeObjetos(HashMap<String, List<String>> dadosDosArquivos) {
		for (String chave : dadosDosArquivos.keySet()) {
			if(chave.equals(DocumentosRLImportacaoSigtap.RL_PROCEDIMENTO_CID.getSigla())) {
				List<String> linhasDocumento = dadosDosArquivos.get(chave);
				for (String linha : linhasDocumento) {
					this.listaRelacoesProcedimetoCid.add(DocumentosRLImportacaoSigtap.RL_PROCEDIMENTO_CID.retornarCodigoObjeto(linha));
				}
				System.out.println(chave);
			}
			else if(chave.equals(DocumentosRLImportacaoSigtap.RL_PROCEDIMENTO_MODALIDADE.getSigla())) {
				List<String> linhasDocumento = dadosDosArquivos.get(chave);
				for (String linha : linhasDocumento) {
					this.listaRelacoesProcedimetoModalidade.add(DocumentosRLImportacaoSigtap.RL_PROCEDIMENTO_MODALIDADE.retornarCodigoObjeto(linha));
				}
				System.out.println(chave);
			}
			else if(chave.equals(DocumentosRLImportacaoSigtap.RL_PROCEDIMENTO_CBO.getSigla())) {
				List<String> linhasDocumento = dadosDosArquivos.get(chave);
				for (String linha : linhasDocumento) {
					this.listaRelacoesProcedimetoCbo.add(DocumentosRLImportacaoSigtap.RL_PROCEDIMENTO_CBO.retornarCodigoObjeto(linha));
				}
				System.out.println(chave);
			}
			else if(chave.equals(DocumentosRLImportacaoSigtap.RL_PROCEDIMENTO_INSTRUMENTO_REGISTRO.getSigla())) {
				List<String> linhasDocumento = dadosDosArquivos.get(chave);
				for (String linha : linhasDocumento) {
					this.listaRelacoesProcedimetoInstrumentoRegistro.add(DocumentosRLImportacaoSigtap.RL_PROCEDIMENTO_INSTRUMENTO_REGISTRO.retornarCodigoObjeto(linha));
				}
				System.out.println(chave);
			}
			else if(chave.equals(DocumentosRLImportacaoSigtap.RL_PROCEDIMENTO_RENASES.getSigla())) {
				List<String> linhasDocumento = dadosDosArquivos.get(chave);
				for (String linha : linhasDocumento) {
					this.listaRelacoesProcedimetoRenases.add(DocumentosRLImportacaoSigtap.RL_PROCEDIMENTO_RENASES.retornarCodigoObjeto(linha));
				}
				System.out.println(chave);
			}
			else if(chave.equals(DocumentosRLImportacaoSigtap.RL_PROCEDIMENTO_SERVICO.getSigla())) {
				List<String> linhasDocumento = dadosDosArquivos.get(chave);
				for (String linha : linhasDocumento) {
					this.listaRelacoesProcedimetoServico.add(DocumentosRLImportacaoSigtap.RL_PROCEDIMENTO_SERVICO.retornarCodigoObjeto(linha));
				}
				System.out.println(chave);
			}
		}
	}
	
	private void removeProcedimentosQueNaoEstaoNoBanco() {
		List<ProcedimentoType> listaProcedimentosDoArquivoAux = new ArrayList<ProcedimentoType>();
		
		for(int i = 0; i < this.listaProcedimentos.size(); i++) {
			for (ProcedimentoType procedimento : this.listaProcedimentosDoArquivo) {
				if(procedimento.getCodigo().equals(listaProcedimentos.get(i).getCodProc()))
					listaProcedimentosDoArquivoAux.add(procedimento);
			}
		}
		
		this.listaProcedimentosDoArquivo.clear();
		this.listaProcedimentosDoArquivo.addAll(listaProcedimentosDoArquivoAux);
	}

	private void relacionarDadosDoProcedimento() {
		//this.listaGravarProcedimentosMensaisDTO
		for (ProcedimentoType procedimento : this.listaProcedimentosDoArquivo) {
			relacionaDadosCidProcedimento(procedimento);
			relacionaDadosModalidadeProcedimento(procedimento);
			relacionaDadosCboProcedimento(procedimento);
			relacionaDadosInstrumentoRegistroProcedimento(procedimento);
			relacionaDadosRenasesProcedimento(procedimento);
			relacionaDadosServicoClassificacaoProcedimento(procedimento);
			System.err.println();
		}
	}

	private void relacionaDadosCidProcedimento(ProcedimentoType procedimento) {
		CIDsVinculados cidsVinculados = new CIDsVinculados();
		procedimento.setCIDsVinculados(cidsVinculados);
		
		RelacaoObjetoComProcedimentoDTO relacaoProcedimentoCidValido = new RelacaoObjetoComProcedimentoDTO();
		for(RelacaoObjetoComProcedimentoDTO relacaoProcedimentoCid : this.listaRelacoesProcedimetoCid) {
			if(relacaoProcedimentoCid.getCodigoProcedimento().equals(procedimento.getCodigo())) {
				relacaoProcedimentoCidValido = relacaoProcedimentoCid;
				for(CIDVinculado cidVinculado : this.listaCidDoArquivo) {
					if(cidVinculado.getCID().getCodigo().equals(relacaoProcedimentoCidValido.getCodigo()))
						procedimento.getCIDsVinculados().getCIDVinculado().add(cidVinculado);
				}
			}
		}
	}
	
	private void relacionaDadosModalidadeProcedimento(ProcedimentoType procedimento) {
		ModalidadesAtendimento modalidadesAtendimento = new ModalidadesAtendimento();
		procedimento.setModalidadesAtendimento(modalidadesAtendimento);
		
		RelacaoObjetoComProcedimentoDTO relacaoProcedimentoModalidadeValida = new RelacaoObjetoComProcedimentoDTO();
		for(RelacaoObjetoComProcedimentoDTO relacaoProcedimentoModalidade : this.listaRelacoesProcedimetoModalidade) {
			if(relacaoProcedimentoModalidade.getCodigoProcedimento().equals(procedimento.getCodigo())) {
				relacaoProcedimentoModalidadeValida = relacaoProcedimentoModalidade;
				for(ModalidadeAtendimentoType modaliadeAtendimento : this.listaModalidadeAtendimentoDoArquivo) {
					if(modaliadeAtendimento.getCodigo().equals(relacaoProcedimentoModalidadeValida.getCodigo()))
						procedimento.getModalidadesAtendimento().getModalidadeAtendimento().add(modaliadeAtendimento);
				}
			}
		}
	}
	
	private void relacionaDadosCboProcedimento(ProcedimentoType procedimento) {
		CBOsVinculados cbosVinculados = new CBOsVinculados();
		procedimento.setCBOsVinculados(cbosVinculados);
		
		RelacaoObjetoComProcedimentoDTO relacaoProcedimentoCboValido = new RelacaoObjetoComProcedimentoDTO();
		for(RelacaoObjetoComProcedimentoDTO relacaoProcedimentoCbo : this.listaRelacoesProcedimetoCbo) {
			if(relacaoProcedimentoCbo.getCodigoProcedimento().equals(procedimento.getCodigo())) {
				relacaoProcedimentoCboValido = relacaoProcedimentoCbo;
				for(CBOType cbo : this.listaCbosDoArquivo) {
					if(cbo.getCodigo().equals(relacaoProcedimentoCboValido.getCodigo()))
						procedimento.getCBOsVinculados().getCBO().add(cbo);
				}
			}
		}
	}

	private void relacionaDadosInstrumentoRegistroProcedimento(ProcedimentoType procedimento) {
		InstrumentosRegistro instrumentosRegistro = new InstrumentosRegistro();
		procedimento.setInstrumentosRegistro(instrumentosRegistro);
		
		RelacaoObjetoComProcedimentoDTO relacaoProcedimentoInstrumentoRegistroValido = new RelacaoObjetoComProcedimentoDTO();
		for(RelacaoObjetoComProcedimentoDTO relacaoProcedimentoInstrumento : this.listaRelacoesProcedimetoInstrumentoRegistro) {
			if(relacaoProcedimentoInstrumento.getCodigoProcedimento().equals(procedimento.getCodigo())) {
				relacaoProcedimentoInstrumentoRegistroValido = relacaoProcedimentoInstrumento;
				for(InstrumentoRegistroType instrumentoRegistro : this.listaInstrumentoRegistroDoArquivo) {
					if(instrumentoRegistro.getCodigo().equals(relacaoProcedimentoInstrumentoRegistroValido.getCodigo()))
						procedimento.getInstrumentosRegistro().getInstrumentoRegistro().add(instrumentoRegistro);
				}
			}
		}
	}
	
	private void relacionaDadosRenasesProcedimento(ProcedimentoType procedimento) {
		RENASESVinculadas cbosVinculados = new RENASESVinculadas();
		procedimento.setRENASESVinculadas(cbosVinculados);
		
		RelacaoObjetoComProcedimentoDTO relacaoProcedimentoRenasesValido = new RelacaoObjetoComProcedimentoDTO();
		for(RelacaoObjetoComProcedimentoDTO relacaoProcedimentoRenases : this.listaRelacoesProcedimetoRenases) {
			if(relacaoProcedimentoRenases.getCodigoProcedimento().equals(procedimento.getCodigo())) {
				relacaoProcedimentoRenasesValido = relacaoProcedimentoRenases;
				for(RENASESType renases : this.listaRenasesDoArquivo) {
					if(renases.getCodigo().equals(relacaoProcedimentoRenasesValido.getCodigo()))
						procedimento.getRENASESVinculadas().getRENASES().add(renases);
				}
			}
		}
	}
	
	private void relacionaDadosServicoClassificacaoProcedimento(ProcedimentoType procedimento) {
		ServicosClassificacoesVinculados servicosClassificacoesVinculados = new ServicosClassificacoesVinculados();
		procedimento.setServicosClassificacoesVinculados(servicosClassificacoesVinculados);
		
		RelacaoObjetoComProcedimentoDTO relacaoProcedimentoServicoClassificacaoValido = new RelacaoObjetoComProcedimentoDTO();
		for(RelacaoObjetoComProcedimentoDTO relacaoProcedimentoServico : this.listaRelacoesProcedimetoServico) {
			if(relacaoProcedimentoServico.getCodigoProcedimento().equals(procedimento.getCodigo())) {
				relacaoProcedimentoServicoClassificacaoValido = relacaoProcedimentoServico;
				
				for(ServicoClassificacaoType servicoClassificacao : this.listaServicoClassificacaoDoArquivo) {
					if( (servicoClassificacao.getCodigoClassificacao().equals(relacaoProcedimentoServicoClassificacaoValido.getCodigoClassificacao()) )
						&& servicoClassificacao.getServico().getCodigo().equals(relacaoProcedimentoServicoClassificacaoValido.getCodigo())	) {
						
						for(ServicoType servico : this.listaServicoDoArquivo) {
							if(servico.getCodigo().equals(servicoClassificacao.getServico().getCodigo()))
								procedimento.getServicosClassificacoesVinculados().getServicoClassificacao().add(servicoClassificacao);							
						}
					}
				}
				
			}
		}
	}

    public List<ProcedimentoBean> getListaProcedimentos() {
        return listaProcedimentos;
    }

    public ProcedimentoBean getProc() {
        return proc;
    }

    public void setProc(ProcedimentoBean proc) {
        this.proc = proc;
    }

    public void setListaProcedimentos(List<ProcedimentoBean> listaProcedimentos) {
        this.listaProcedimentos = listaProcedimentos;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public CidBean getCid() {
        return cid;
    }

    public void setCid(CidBean cid) {
        this.cid = cid;
    }

    public CboBean getCbo() {
        return cbo;
    }

    public void setCbo(CboBean cbo) {
        this.cbo = cbo;
    }

    public RecursoBean getRecurso() {
        return recurso;
    }

    public void setRecurso(RecursoBean recurso) {
        this.recurso = recurso;
    }

    public String getTipoBusca() {
        return tipoBusca;
    }

    public void setTipoBusca(String tipoBusca) {
        this.tipoBusca = tipoBusca;
    }

    public String getCampoBusca() {
        return campoBusca;
    }

    public void setCampoBusca(String campoBusca) {
        this.campoBusca = campoBusca;
    }

	public List<HistoricoSigtapBean> getListaHistoricoDoSigtap() {
		return listaHistoricoDoSigtap;
	}

	public void setListaHistoricoDoSigtap(List<HistoricoSigtapBean> listaHistoricoDoSigtap) {
		this.listaHistoricoDoSigtap = listaHistoricoDoSigtap;
	}

	public UploadedFile getArquivoImportacaoSelecionado() {
		return arquivoImportacaoSelecionado;
	}

	public void setArquivoImportacaoSelecionado(UploadedFile arquivoImportacaoSelecionado) {
		this.arquivoImportacaoSelecionado = arquivoImportacaoSelecionado;
	}
}
