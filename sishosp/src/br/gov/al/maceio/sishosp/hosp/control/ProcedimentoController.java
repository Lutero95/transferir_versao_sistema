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
import org.primefaces.model.UploadedFile;

import br.gov.al.maceio.sishosp.acl.model.FuncionarioBean;
import br.gov.al.maceio.sishosp.comum.enums.TipoCabecalho;
import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.JSFUtil;
import br.gov.al.maceio.sishosp.comum.util.RedirecionarUtil;
import br.gov.al.maceio.sishosp.comum.util.VerificadorUtil;
import br.gov.al.maceio.sishosp.hosp.dao.ProcedimentoDAO;
import br.gov.al.maceio.sishosp.hosp.dao.RecursoDAO;
import br.gov.al.maceio.sishosp.hosp.dao.UnidadeDAO;
import br.gov.al.maceio.sishosp.hosp.enums.DocumentosRLImportacaoSigtap;
import br.gov.al.maceio.sishosp.hosp.enums.DocumentosTBImportacaoSigtap;
import br.gov.al.maceio.sishosp.hosp.model.CboBean;
import br.gov.al.maceio.sishosp.hosp.model.CidBean;
import br.gov.al.maceio.sishosp.hosp.model.HistoricoSigtapBean;
import br.gov.al.maceio.sishosp.hosp.model.ProcedimentoBean;
import br.gov.al.maceio.sishosp.hosp.model.RecursoBean;
import br.gov.al.maceio.sishosp.hosp.model.UnidadeBean;
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
import sigtap.br.gov.saude.servicos.schema.sigtap.procedimento.v1.procedimento.ProcedimentoType;
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
    private String anoCompetenciaAtual;
    private String mesCompetenciaAtual;
    private List<String> listaFiltroMesIhAno = new ArrayList<>();
    private String filtroMesIhAnoSelecionado;
    private ProcedimentoType procedimentoMensal;
    private UnidadeBean unidadeSelecionada;

    //CONSTANTES
    private static final String ENDERECO_CADASTRO = "cadastroProcedimento?faces-redirect=true";
    private static final String ENDERECO_TIPO = "&amp;tipo=";
    private static final String ENDERECO_ID = "&amp;id=";
    private static final String CABECALHO_INCLUSAO = "Inclusão de Procedimento";
    private static final String CABECALHO_ALTERACAO = "Alteração de Procedimento";
    //SIGTAP
    private static final String VALOR_PADRAO_PARAMETRO_NAO_USADO = "0";
    private static final Integer QUANTIDADE_FILTROS_DETALHAR_PROCEDIMENTO = 5;
    private static final Integer QUANTIDADE_MAXIMA_REGISTROS_POR_BUSCA = 20;
    private static final BigInteger REGISTRO_INICIAL = BigInteger.ONE;
    private static final Integer VALOR_ZERO = 0;

    private List<GravarProcedimentoMensalDTO> listaGravarProcedimentosMensaisDTO;
    private GravarProcedimentoMensalDTO gravarProcedimentoMensalDTO;
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
        unidadeSelecionada = new UnidadeBean();

        //SIGTAP
        this.listaHistoricoDoSigtap = new ArrayList<>();
        limparListaGravarProcedimentoMensalDTO();
        limparGravarProcedimentoDTO();
    }

    private void limparGravarProcedimentoDTO() {
        this.gravarProcedimentoMensalDTO = new GravarProcedimentoMensalDTO();
    }

    private void limparListaGravarProcedimentoMensalDTO() {
        this.listaGravarProcedimentosMensaisDTO = new ArrayList();
    }

    public String redirectEdit() {
        return RedirecionarUtil.redirectEdit(ENDERECO_CADASTRO, ENDERECO_ID, this.proc.getIdProc(), ENDERECO_TIPO, tipo);
    }

    public String redirectInsert() {
        return RedirecionarUtil.redirectInsert(ENDERECO_CADASTRO, ENDERECO_TIPO, tipo);
    }

    public void getEditProcedimento() throws ProjetoException, SQLException {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Map<String, String> params = facesContext.getExternalContext()
                .getRequestParameterMap();
        if (params.get("id") != null) {
            Integer id = Integer.parseInt(params.get("id"));
            tipo = Integer.parseInt(params.get("tipo"));
            this.proc = procedimentoDao.listarProcedimentoPorId(id);
            listaDadosDoProcedimentoSelecionadoPorMesIhAnoAtual();
            listaMesesIhAnosDoHistorico();
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

    public void listaMesesIhAnosDoHistorico() throws ProjetoException, SQLException {
        this.listaFiltroMesIhAno = procedimentoDao.listaMesesIhAnosDoHistorico();
    }

    public void listaDadosDoProcedimentoSelecionadoPorMesIhAnoAtual() {
        try {
            this.procedimentoMensal = procedimentoDao.buscaDadosProcedimentoMensal(proc.getCodProc(), procedimentoDao.retornaMaiorAnoCargaSigtap(), procedimentoDao.retornaMaiorMêsNoUltimoAnoCargaSigtap());
            exibeMensagemSeProcedimentoNaoPossuiDadosNoPeriodoSelecionado();
        } catch (Exception e) {
            JSFUtil.adicionarMensagemErro(e.getLocalizedMessage(), "Erro!");
            e.printStackTrace();
        }
    }

    private void exibeMensagemSeProcedimentoNaoPossuiDadosNoPeriodoSelecionado() {
        if(VerificadorUtil.verificarSeObjetoNuloOuVazio(this.procedimentoMensal.getCodigo())) {
            JSFUtil.adicionarMensagemAdvertencia("Procedimento não possui dados para o mês e ano selecionado", "");
            JSFUtil.atualizarComponente(":frmCadProc:message");
        }
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
        this.listaProcedimentos = procedimentoDao.listarTodosProcedimentos();

    }

   public void listarProcedimentos() throws ProjetoException {
        this.listaProcedimentos = procedimentoDao.listarProcedimento();

    }

    public void listarTodosProcedimentosEmpresa() throws ProjetoException {
        this.listaProcedimentos = procedimentoDao.listarProcedimentosDaEmpresa();

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
        limparListaGravarProcedimentoMensalDTO();
        if(this.listaProcedimentos.isEmpty())
            JSFUtil.adicionarMensagemErro("Não há procedimentos cadastrados", "Erro");
        else {
            if (!verificaSeHouveCargaDoSigtapEsteMes( -1,  -1)) {

                FuncionarioBean user_session = obterUsuarioDaSessao();

                for (int i = 0; i < this.listaProcedimentos.size(); i++) {
                    try {
                        selecionaDetalheAdicionalEmSequencia(this.listaProcedimentos.get(i));
                        limparGravarProcedimentoDTO();
                    }
                    catch(Exception sqle) {
                        JSFUtil.adicionarMensagemErro(sqle.getMessage(), "Erro");
                        sqle.printStackTrace();
                    }
                }

                try {
                    procedimentoDao.executaRotinaNovaCargaSigtap(this.listaGravarProcedimentosMensaisDTO,
                            listaGrupoDoArquivo, listaSubgrupoDoArquivo,listaFormaOrganizacaoDoArquivo,  user_session.getId(), 1, 1);
                    JSFUtil.adicionarMensagemSucesso("Dados atualizados com sucesso!", "");
                }catch(Exception sqle) {
                    JSFUtil.adicionarMensagemErro(sqle.getMessage(), "Erro");
                    sqle.printStackTrace();
                }

                listaHistoricoSigtap();
                fecharDialogAvisoCargaSigtap();
            }
        }
    }

    private Boolean verificaSeHouveCargaDoSigtapEsteMes(Integer mes, Integer ano) {
        try {
            Boolean houveCargaEsteMes = procedimentoDao.houveCargaDoSigtapEsteMes(mes, ano);
            if(houveCargaEsteMes) {
                fecharDialogAvisoCargaSigtap();
                JSFUtil.adicionarMensagemAdvertencia("Os Dados do SIGTAP já estão atualizados para a competência "+String.format("%02d", mes)+"/"+ano+".", "");
                return houveCargaEsteMes;
            }
        } catch (Exception e) {
            JSFUtil.adicionarMensagemErro(e.getMessage(), "Erro");
            e.printStackTrace();
        }

        return false;
    }

    private void fecharDialogAvisoCargaSigtap() {
        JSFUtil.fecharDialog("dlg-aviso");
    }

    private void selecionaDetalheAdicionalEmSequencia(ProcedimentoBean procedimento) {
        int detalheAdicional = 0;
        while (detalheAdicional < QUANTIDADE_FILTROS_DETALHAR_PROCEDIMENTO) {
            CategoriaDetalheAdicionalType categoriaDetalheAdicional;

            if (detalheAdicional == 0)
                categoriaDetalheAdicional = CategoriaDetalheAdicionalType.DESCRICAO;
            else if (detalheAdicional == 1)
                categoriaDetalheAdicional = CategoriaDetalheAdicionalType.CBOS;
            else if (detalheAdicional == 2)
                categoriaDetalheAdicional = CategoriaDetalheAdicionalType.CIDS;
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
        requestDetalharProcedimento.setCompetencia(anoCompetenciaAtual+mesCompetenciaAtual);

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
        adicionaDadosExtrasParaListaProcedimentoMensalDTO(resultadosDetalhaProcedimentosType.getProcedimento(), categoriaDetalheAdicional);

        if (VerificadorUtil.verificarSeObjetoNulo
                (resultadosDetalhaProcedimentosType.getDetalheAdicional().get(0).getPaginacao()))
            return new BigInteger(BigInteger.ZERO.toString());

        return resultadosDetalhaProcedimentosType.getDetalheAdicional().get(0).getPaginacao().getTotalRegistros();
    }

    private void adicionaProcedimentoParaListaProcedimentoMensalDTO(ProcedimentoBean procedimento,
                                                                    ResultadosDetalhaProcedimentosType resultadosDetalhaProcedimentosType) {
        Integer ultimoIndiceDaListaProcedimentosMensalDTO = this.listaGravarProcedimentosMensaisDTO.size() - 1;

        if(this.listaGravarProcedimentosMensaisDTO.isEmpty()) {
            gravarProcedimentoMensalDTO.setIdProcedimento(procedimento.getIdProc());
            gravarProcedimentoMensalDTO.setProcedimentoMensal(resultadosDetalhaProcedimentosType.getProcedimento());
            this.listaGravarProcedimentosMensaisDTO.add(gravarProcedimentoMensalDTO);
        }

        else if(!this.listaGravarProcedimentosMensaisDTO.get(ultimoIndiceDaListaProcedimentosMensalDTO).getIdProcedimento().equals(procedimento.getIdProc())){
            gravarProcedimentoMensalDTO.setIdProcedimento(procedimento.getIdProc());
            gravarProcedimentoMensalDTO.setProcedimentoMensal(resultadosDetalhaProcedimentosType.getProcedimento());
            this.listaGravarProcedimentosMensaisDTO.add(gravarProcedimentoMensalDTO);
        }
    }

    private void adicionaDadosExtrasParaListaProcedimentoMensalDTO(ProcedimentoType procedimento,
                                                                   CategoriaDetalheAdicionalType categoriaDetalheAdicional) {
        Integer ultimoIndiceDaListaProcedimentosMensalDTO = this.listaGravarProcedimentosMensaisDTO.size() - 1;
        if(categoriaDetalheAdicional.equals(CategoriaDetalheAdicionalType.CBOS))
            setarDadosCbo(procedimento, ultimoIndiceDaListaProcedimentosMensalDTO);
        else if (categoriaDetalheAdicional.equals(CategoriaDetalheAdicionalType.CIDS))
            setarDadosCid(procedimento, ultimoIndiceDaListaProcedimentosMensalDTO);
        else if (categoriaDetalheAdicional.equals(CategoriaDetalheAdicionalType.RENASES))
            setarDadosRenases(procedimento, ultimoIndiceDaListaProcedimentosMensalDTO);
        else if (categoriaDetalheAdicional.equals(CategoriaDetalheAdicionalType.SERVICOS_CLASSIFICACOES))
            setarDadosServicoClassificacao(procedimento, ultimoIndiceDaListaProcedimentosMensalDTO);
    }

    private void setarDadosCbo(ProcedimentoType procedimento, Integer ultimoIndiceDaListaProcedimentosMensalDTO) {
        this.listaGravarProcedimentosMensaisDTO.get(ultimoIndiceDaListaProcedimentosMensalDTO)
                .getProcedimentoMensal().getCBOsVinculados().getCBO().addAll(procedimento.getCBOsVinculados().getCBO());
    }

    private void setarDadosCid(ProcedimentoType procedimento, Integer ultimoIndiceDaListaProcedimentosMensalDTO) {
        this.listaGravarProcedimentosMensaisDTO.get(ultimoIndiceDaListaProcedimentosMensalDTO)
                .getProcedimentoMensal().getCIDsVinculados().getCIDVinculado().addAll(procedimento.getCIDsVinculados().getCIDVinculado());
    }

    private void setarDadosRenases(ProcedimentoType procedimento, Integer ultimoIndiceDaListaProcedimentosMensalDTO) {
        this.listaGravarProcedimentosMensaisDTO.get(ultimoIndiceDaListaProcedimentosMensalDTO)
                .getProcedimentoMensal().getRENASESVinculadas().getRENASES().addAll(procedimento.getRENASESVinculadas().getRENASES());
    }

    private void setarDadosServicoClassificacao(ProcedimentoType procedimento,
                                                Integer ultimoIndiceDaListaProcedimentosMensalDTO) {

        this.listaGravarProcedimentosMensaisDTO.get(ultimoIndiceDaListaProcedimentosMensalDTO)
                .getProcedimentoMensal().getServicosClassificacoesVinculados().getServicoClassificacao()
                .addAll(procedimento.getServicosClassificacoesVinculados().getServicoClassificacao());
    }

    public void listaHistoricoSigtap() throws ProjetoException {
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
        listarTodosProcedimentosEmpresa();
        if(this.listaProcedimentos.isEmpty())
            JSFUtil.adicionarMensagemErro("Não há procedimentos cadastrados", "Erro");
        else {
            String anoMesCompetencia = recuperaCompetenciaCarga(dadosDosArquivos);
            int ano = Integer.parseInt(anoMesCompetencia.substring(0,4));
            int mes = Integer.parseInt((anoMesCompetencia.substring(4,6)));
            if (!verificaSeHouveCargaDoSigtapEsteMes(mes, ano)) {

                transformaDadosDosArquivosTBEmListasDeObjetos(dadosDosArquivos);
                transformaDadosDosArquivosRLEmListasDeObjetos(dadosDosArquivos);
                removeProcedimentosQueNaoEstaoNoBanco();
                relacionarDadosDoProcedimento();
                try {
                    FuncionarioBean user_session = obterUsuarioDaSessao();
                    if(this.listaGravarProcedimentosMensaisDTO.isEmpty() || VerificadorUtil.verificarSeObjetoNulo(this.listaGravarProcedimentosMensaisDTO))
                        JSFUtil.adicionarMensagemErro("Erro não há dados válidos neste arquivo", "");
                    else {
                        procedimentoDao.executaRotinaNovaCargaSigtap(this.listaGravarProcedimentosMensaisDTO,
                                listaGrupoDoArquivo, listaSubgrupoDoArquivo,listaFormaOrganizacaoDoArquivo, user_session.getId(), mes, ano);
                        JSFUtil.adicionarMensagemSucesso("Dados atualizados com sucesso!", "");
                    }
                }catch(Exception sqle) {
                    JSFUtil.adicionarMensagemErro(sqle.getMessage(), "Erro");
                    sqle.printStackTrace();
                }
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
            }
            else if(chave.equals(DocumentosTBImportacaoSigtap.TB_DESCRICAO.getSigla())){
                List<String> linhasDocumentoDescricaoProcedimento = dadosDosArquivos.get(chave);
                for (String linha : linhasDocumentoDescricaoProcedimento) {
                    this.listaDescricaoProcedimentoDoArquivo.add((DescricaoProcedimentoDTO) DocumentosTBImportacaoSigtap.TB_DESCRICAO.retornarObjectoDaString(linha));
                }
            }
            else if(chave.equals(DocumentosTBImportacaoSigtap.TB_FINANCIAMENTO.getSigla())){
                List<String> linhasDocumentoTipoFinanciamento = dadosDosArquivos.get(chave);
                for (String linha : linhasDocumentoTipoFinanciamento) {
                    this.listaTipoFinanciamentoDoArquivo.add((TipoFinanciamentoType) DocumentosTBImportacaoSigtap.TB_FINANCIAMENTO.retornarObjectoDaString(linha));
                }
            }
            else if(chave.equals(DocumentosTBImportacaoSigtap.TB_FORMA_ORGANIZACAO.getSigla())){
                List<String> linhasDocumentoFormaOrganizacao = dadosDosArquivos.get(chave);
                for (String linha : linhasDocumentoFormaOrganizacao) {
                    this.listaFormaOrganizacaoDoArquivo.add((FormaOrganizacaoType) DocumentosTBImportacaoSigtap.TB_FORMA_ORGANIZACAO.retornarObjectoDaString(linha));
                }
            }
            else if(chave.equals(DocumentosTBImportacaoSigtap.TB_GRUPO.getSigla())){
                List<String> linhasDocumentoGrupo = dadosDosArquivos.get(chave);
                for (String linha : linhasDocumentoGrupo) {
                    this.listaGrupoDoArquivo.add((GrupoType) DocumentosTBImportacaoSigtap.TB_GRUPO.retornarObjectoDaString(linha));
                }
            }
            else if(chave.equals(DocumentosTBImportacaoSigtap.TB_MODALIDADE.getSigla())){
                List<String> linhasDocumentoModalidade = dadosDosArquivos.get(chave);
                for (String linha : linhasDocumentoModalidade) {
                    this.listaModalidadeAtendimentoDoArquivo.add((ModalidadeAtendimentoType) DocumentosTBImportacaoSigtap.TB_MODALIDADE.retornarObjectoDaString(linha));
                }
            }
            else if(chave.equals(DocumentosTBImportacaoSigtap.TB_CBO.getSigla())){
                List<String> linhasDocumentoCbos = dadosDosArquivos.get(chave);
                for (String linha : linhasDocumentoCbos) {
                    this.listaCbosDoArquivo.add((CBOType) DocumentosTBImportacaoSigtap.TB_CBO.retornarObjectoDaString(linha));
                }
            }
            else if(chave.equals(DocumentosTBImportacaoSigtap.TB_PROCEDIMENTO.getSigla())){
                List<String> linhasDocumentoProcedimentos = dadosDosArquivos.get(chave);
                for (String linha : linhasDocumentoProcedimentos) {
                    this.listaProcedimentosDoArquivo.add((ProcedimentoType) DocumentosTBImportacaoSigtap.TB_PROCEDIMENTO.retornarObjectoDaString(linha));
                }
            }
            else if(chave.equals(DocumentosTBImportacaoSigtap.TB_INSTRUMENTO_REGISTRO.getSigla())){
                List<String> linhasDocumentoInstrumentoRegistro = dadosDosArquivos.get(chave);
                for (String linha : linhasDocumentoInstrumentoRegistro) {
                    this.listaInstrumentoRegistroDoArquivo.add((InstrumentoRegistroType) DocumentosTBImportacaoSigtap.TB_INSTRUMENTO_REGISTRO.retornarObjectoDaString(linha));
                }
            }
            else if(chave.equals(DocumentosTBImportacaoSigtap.TB_RENASES.getSigla())){
                List<String> linhasDocumentoRenases = dadosDosArquivos.get(chave);
                for (String linha : linhasDocumentoRenases) {
                    this.listaRenasesDoArquivo.add((RENASESType) DocumentosTBImportacaoSigtap.TB_RENASES.retornarObjectoDaString(linha));
                }
            }
            else if(chave.equals(DocumentosTBImportacaoSigtap.TB_SERVICO.getSigla())){
                List<String> linhasDocumentoServico = dadosDosArquivos.get(chave);
                for (String linha : linhasDocumentoServico) {
                    this.listaServicoDoArquivo.add((ServicoType) DocumentosTBImportacaoSigtap.TB_SERVICO.retornarObjectoDaString(linha));
                }
            }
            else if(chave.equals(DocumentosTBImportacaoSigtap.TB_SERVICO_CLASSIFICACAO.getSigla())){
                List<String> linhasDocumentoServicoClassificacao = dadosDosArquivos.get(chave);
                for (String linha : linhasDocumentoServicoClassificacao) {
                    this.listaServicoClassificacaoDoArquivo.add((ServicoClassificacaoType) DocumentosTBImportacaoSigtap.TB_SERVICO_CLASSIFICACAO.retornarObjectoDaString(linha));
                }
            }
            else if(chave.equals(DocumentosTBImportacaoSigtap.TB_SUBGRUPO.getSigla())){
                List<String> linhasDocumentoSubgrupo = dadosDosArquivos.get(chave);
                for (String linha : linhasDocumentoSubgrupo) {
                    this.listaSubgrupoDoArquivo.add((SubgrupoType) DocumentosTBImportacaoSigtap.TB_SUBGRUPO.retornarObjectoDaString(linha));
                }
            }
        }
    }

    private String recuperaCompetenciaCarga(HashMap<String, List<String>> dadosDosArquivos) {
        ProcedimentoType procedimento = new ProcedimentoType();
        for (String chave : dadosDosArquivos.keySet()) {
            if (chave.equals(DocumentosTBImportacaoSigtap.TB_PROCEDIMENTO.getSigla())) {
                List<String> linhasDocumentoProcedimentos = dadosDosArquivos.get(chave);
                for (String linha : linhasDocumentoProcedimentos) {
                    procedimento = (ProcedimentoType) DocumentosTBImportacaoSigtap.TB_PROCEDIMENTO.retornarObjectoDaString(linha);
                    break;
                }
            }
        }
        return procedimento.getCompetenciaValidade();
    }

    private void transformaDadosDosArquivosRLEmListasDeObjetos(HashMap<String, List<String>> dadosDosArquivos) {
        for (String chave : dadosDosArquivos.keySet()) {
            if(chave.equals(DocumentosRLImportacaoSigtap.RL_PROCEDIMENTO_CID.getSigla())) {
                List<String> linhasDocumento = dadosDosArquivos.get(chave);
                for (String linha : linhasDocumento) {
                    this.listaRelacoesProcedimetoCid.add(DocumentosRLImportacaoSigtap.RL_PROCEDIMENTO_CID.retornarCodigoObjeto(linha));
                }
            }
            else if(chave.equals(DocumentosRLImportacaoSigtap.RL_PROCEDIMENTO_MODALIDADE.getSigla())) {
                List<String> linhasDocumento = dadosDosArquivos.get(chave);
                for (String linha : linhasDocumento) {
                    this.listaRelacoesProcedimetoModalidade.add(DocumentosRLImportacaoSigtap.RL_PROCEDIMENTO_MODALIDADE.retornarCodigoObjeto(linha));
                }
            }
            else if(chave.equals(DocumentosRLImportacaoSigtap.RL_PROCEDIMENTO_CBO.getSigla())) {
                List<String> linhasDocumento = dadosDosArquivos.get(chave);
                for (String linha : linhasDocumento) {
                    this.listaRelacoesProcedimetoCbo.add(DocumentosRLImportacaoSigtap.RL_PROCEDIMENTO_CBO.retornarCodigoObjeto(linha));
                }
            }
            else if(chave.equals(DocumentosRLImportacaoSigtap.RL_PROCEDIMENTO_INSTRUMENTO_REGISTRO.getSigla())) {
                List<String> linhasDocumento = dadosDosArquivos.get(chave);
                for (String linha : linhasDocumento) {
                    this.listaRelacoesProcedimetoInstrumentoRegistro.add(DocumentosRLImportacaoSigtap.RL_PROCEDIMENTO_INSTRUMENTO_REGISTRO.retornarCodigoObjeto(linha));
                }
            }
            else if(chave.equals(DocumentosRLImportacaoSigtap.RL_PROCEDIMENTO_RENASES.getSigla())) {
                List<String> linhasDocumento = dadosDosArquivos.get(chave);
                for (String linha : linhasDocumento) {
                    this.listaRelacoesProcedimetoRenases.add(DocumentosRLImportacaoSigtap.RL_PROCEDIMENTO_RENASES.retornarCodigoObjeto(linha));
                }
            }
            else if(chave.equals(DocumentosRLImportacaoSigtap.RL_PROCEDIMENTO_SERVICO.getSigla())) {
                List<String> linhasDocumento = dadosDosArquivos.get(chave);
                for (String linha : linhasDocumento) {
                    this.listaRelacoesProcedimetoServico.add(DocumentosRLImportacaoSigtap.RL_PROCEDIMENTO_SERVICO.retornarCodigoObjeto(linha));
                }
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

        for (ProcedimentoType procedimento : this.listaProcedimentosDoArquivo) {
            Integer idProcedimento = retornaIdProcedimentoNoBanco(procedimento.getCodigo());
            GravarProcedimentoMensalDTO gravarProcedimentoMensalDTO = new GravarProcedimentoMensalDTO();

            ModalidadesAtendimento modalidadesAtendimento = relacionaDadosModalidadeProcedimento(procedimento);
            InstrumentosRegistro instrumentosRegistro = relacionaDadosInstrumentoRegistroProcedimento(procedimento);
            CBOsVinculados cbosVinculados = relacionaDadosCboProcedimento(procedimento);
            CIDsVinculados cidsVinculados = relacionaDadosCidProcedimento(procedimento);
            RENASESVinculadas renasesVinculados = relacionaDadosRenasesProcedimento(procedimento);
            relacionaDadosServicoClassificacaoProcedimento(procedimento);
            TipoFinanciamentoType tipoFinanciamento = relacionaDadosTipoFinanciamentoProcedimento(procedimento);
            FormaOrganizacaoType formaOrganizacao = relacionaDadosFormaOrganizacaoProcedimento(procedimento);

            procedimento.setModalidadesAtendimento(modalidadesAtendimento);
            procedimento.setInstrumentosRegistro(instrumentosRegistro);
            procedimento.setCBOsVinculados(cbosVinculados);
            procedimento.setCIDsVinculados(cidsVinculados);
            procedimento.setRENASESVinculadas(renasesVinculados);
            procedimento.setTipoFinanciamento(tipoFinanciamento);
            procedimento.setFormaOrganizacao(formaOrganizacao);

            gravarProcedimentoMensalDTO.setIdProcedimento(idProcedimento);
            gravarProcedimentoMensalDTO.setProcedimentoMensal(procedimento);
            this.listaGravarProcedimentosMensaisDTO.add(gravarProcedimentoMensalDTO);
        }
    }

    private Integer retornaIdProcedimentoNoBanco(String codigoProcedimento) {
        Integer idProcedimento = null;
        for (ProcedimentoBean procedimento : this.listaProcedimentos) {
            if(procedimento.getCodProc().equals(codigoProcedimento))
                idProcedimento = procedimento.getIdProc();
        }
        return idProcedimento;
    }

    private CIDsVinculados relacionaDadosCidProcedimento(ProcedimentoType procedimento) {
        CIDsVinculados cidsVinculados = new CIDsVinculados();

        RelacaoObjetoComProcedimentoDTO relacaoProcedimentoCidValido = new RelacaoObjetoComProcedimentoDTO();
        for(RelacaoObjetoComProcedimentoDTO relacaoProcedimentoCid : this.listaRelacoesProcedimetoCid) {
            if(relacaoProcedimentoCid.getCodigoProcedimento().equals(procedimento.getCodigo())) {
                relacaoProcedimentoCidValido = relacaoProcedimentoCid;
                for(CIDVinculado cidVinculado : this.listaCidDoArquivo) {
                    if(cidVinculado.getCID().getCodigo().equals(relacaoProcedimentoCidValido.getCodigo()))
                        cidsVinculados.getCIDVinculado().add(cidVinculado);
                }
            }
        }
        return cidsVinculados;
    }

    private ModalidadesAtendimento relacionaDadosModalidadeProcedimento(ProcedimentoType procedimento) {
        ModalidadesAtendimento modalidadesAtendimento = new ModalidadesAtendimento();

        RelacaoObjetoComProcedimentoDTO relacaoProcedimentoModalidadeValida = new RelacaoObjetoComProcedimentoDTO();
        for(RelacaoObjetoComProcedimentoDTO relacaoProcedimentoModalidade : this.listaRelacoesProcedimetoModalidade) {
            if(relacaoProcedimentoModalidade.getCodigoProcedimento().equals(procedimento.getCodigo())) {
                relacaoProcedimentoModalidadeValida = relacaoProcedimentoModalidade;
                for(ModalidadeAtendimentoType modaliadeAtendimento : this.listaModalidadeAtendimentoDoArquivo) {
                    if(modaliadeAtendimento.getCodigo().equals(relacaoProcedimentoModalidadeValida.getCodigo()))
                        modalidadesAtendimento.getModalidadeAtendimento().add(modaliadeAtendimento);
                }
            }
        }
        return modalidadesAtendimento;
    }

    private CBOsVinculados relacionaDadosCboProcedimento(ProcedimentoType procedimento) {
        CBOsVinculados cbosVinculados = new CBOsVinculados();

        RelacaoObjetoComProcedimentoDTO relacaoProcedimentoCboValido = new RelacaoObjetoComProcedimentoDTO();
        for(RelacaoObjetoComProcedimentoDTO relacaoProcedimentoCbo : this.listaRelacoesProcedimetoCbo) {
            if(relacaoProcedimentoCbo.getCodigoProcedimento().equals(procedimento.getCodigo())) {
                relacaoProcedimentoCboValido = relacaoProcedimentoCbo;
                for(CBOType cbo : this.listaCbosDoArquivo) {
                    if(cbo.getCodigo().equals(relacaoProcedimentoCboValido.getCodigo()))
                        cbosVinculados.getCBO().add(cbo);
                }
            }
        }
        return cbosVinculados;
    }

    private InstrumentosRegistro relacionaDadosInstrumentoRegistroProcedimento(ProcedimentoType procedimento) {
        InstrumentosRegistro instrumentosRegistro = new InstrumentosRegistro();

        RelacaoObjetoComProcedimentoDTO relacaoProcedimentoInstrumentoRegistroValido = new RelacaoObjetoComProcedimentoDTO();
        for(RelacaoObjetoComProcedimentoDTO relacaoProcedimentoInstrumento : this.listaRelacoesProcedimetoInstrumentoRegistro) {
            if(relacaoProcedimentoInstrumento.getCodigoProcedimento().equals(procedimento.getCodigo())) {
                relacaoProcedimentoInstrumentoRegistroValido = relacaoProcedimentoInstrumento;
                for(InstrumentoRegistroType instrumentoRegistro : this.listaInstrumentoRegistroDoArquivo) {
                    if(instrumentoRegistro.getCodigo().equals(relacaoProcedimentoInstrumentoRegistroValido.getCodigo()))
                        instrumentosRegistro.getInstrumentoRegistro().add(instrumentoRegistro);
                }
            }
        }
        return instrumentosRegistro;
    }

    private RENASESVinculadas relacionaDadosRenasesProcedimento(ProcedimentoType procedimento) {
        RENASESVinculadas renasesVinculados = new RENASESVinculadas();

        RelacaoObjetoComProcedimentoDTO relacaoProcedimentoRenasesValido = new RelacaoObjetoComProcedimentoDTO();
        for(RelacaoObjetoComProcedimentoDTO relacaoProcedimentoRenases : this.listaRelacoesProcedimetoRenases) {
            if(relacaoProcedimentoRenases.getCodigoProcedimento().equals(procedimento.getCodigo())) {
                relacaoProcedimentoRenasesValido = relacaoProcedimentoRenases;
                for(RENASESType renases : this.listaRenasesDoArquivo) {
                    if(renases.getCodigo().equals(relacaoProcedimentoRenasesValido.getCodigo()))
                        renasesVinculados.getRENASES().add(renases);
                }
            }
        }
        return renasesVinculados;
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
                            if(servico.getCodigo().equals(servicoClassificacao.getServico().getCodigo())) {
                                servicoClassificacao.getServico().setNome(servico.getNome());
                                procedimento.getServicosClassificacoesVinculados().getServicoClassificacao().add(servicoClassificacao);
                            }
                        }
                    }
                }

            }
        }
    }

    public void listaDadosDoProcedimentoSelecionadoPorMesIhAnoSelecionado() {
        try {
            String mesAno = this.filtroMesIhAnoSelecionado.replace(" \\ ", "");
            Integer mes = Integer.valueOf(String.valueOf(mesAno.charAt(VALOR_ZERO)));
            Integer ano = Integer.valueOf(mesAno.replaceFirst(mes.toString(), ""));
            this.procedimentoMensal = procedimentoDao.buscaDadosProcedimentoMensal(proc.getCodProc(), ano, mes);
            exibeMensagemSeProcedimentoNaoPossuiDadosNoPeriodoSelecionado();
        } catch (Exception e) {
            JSFUtil.adicionarMensagemErro(e.getLocalizedMessage(), "Erro!");
            e.printStackTrace();
        }
    }

    private TipoFinanciamentoType relacionaDadosTipoFinanciamentoProcedimento(ProcedimentoType procedimento) {
        TipoFinanciamentoType tipoFinanciamento = new TipoFinanciamentoType();
        for(TipoFinanciamentoType tipoFinanciamentoType : this.listaTipoFinanciamentoDoArquivo) {
            if(tipoFinanciamentoType.getCodigo().equals(procedimento.getTipoFinanciamento().getCodigo()))
                tipoFinanciamento = tipoFinanciamentoType;
        }
        return tipoFinanciamento;
    }
    
    private FormaOrganizacaoType relacionaDadosFormaOrganizacaoProcedimento(ProcedimentoType procedimento) {
    	FormaOrganizacaoType formaOrganizacao = new FormaOrganizacaoType();
        for(FormaOrganizacaoType formaOrganizacaoType : this.listaFormaOrganizacaoDoArquivo) {
            if(formaOrganizacaoType.getCodigo().equals(procedimento.getFormaOrganizacao().getCodigo()))
            	formaOrganizacao = formaOrganizacaoType;
        }
        return formaOrganizacao;
    }

    public void adicionarUnidade(UnidadeBean unidade) throws ProjetoException {
    	if(!VerificadorUtil.verificarSeObjetoNuloOuZero(unidade.getId()) 
    			&& !unidadeJaFoiAdicionada(unidade)) {
    		unidade = new UnidadeDAO().buscarUnidadePorId(unidade.getId());
    		proc.getListaUnidadesVisualizam().add(unidade);
    	}
    }
    
    private boolean unidadeJaFoiAdicionada(UnidadeBean unidade) {
    	for (UnidadeBean unidadeLista : proc.getListaUnidadesVisualizam()) {
			if(unidadeLista.getId().equals(unidade.getId())) {
				JSFUtil.adicionarMensagemErro("Esta unidade já foi adicionada", "");
				return true;
			}
		}
    	return false;
    }
    
    public void removerUnidade(UnidadeBean unidade) {
    	ArrayList<UnidadeBean> listaUnidadesAux = new ArrayList<>();
    	listaUnidadesAux.addAll(proc.getListaUnidadesVisualizam());
    	
    	for (UnidadeBean unidadeLista : listaUnidadesAux) {
			if(unidadeLista.getId().equals(unidade.getId())) {
				proc.getListaUnidadesVisualizam().remove(unidadeLista);
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

    public String getAnoCompetenciaAtual() {
        return anoCompetenciaAtual;
    }

    public void setAnoCompetenciaAtual(String anoCompetenciaAtual) {
        this.anoCompetenciaAtual = anoCompetenciaAtual;
    }

    public String getMesCompetenciaAtual() {
        return mesCompetenciaAtual;
    }

    public void setMesCompetenciaAtual(String mesCompetenciaAtual) {
        this.mesCompetenciaAtual = mesCompetenciaAtual;
    }
    public ProcedimentoType getProcedimentoMensal() {
        return procedimentoMensal;
    }

    public void setProcedimentoMensal(ProcedimentoType procedimentoMensal) {
        this.procedimentoMensal = procedimentoMensal;
    }

    public List<String> getListaFiltroMesIhAno() {
        return listaFiltroMesIhAno;
    }

    public void setListaFiltroMesIhAno(List<String> listaFiltroMesIhAno) {
        this.listaFiltroMesIhAno = listaFiltroMesIhAno;
    }

    public String getFiltroMesIhAnoSelecionado() {
        return filtroMesIhAnoSelecionado;
    }

    public void setFiltroMesIhAnoSelecionado(String filtroMesIhAnoSelecionado) {
        this.filtroMesIhAnoSelecionado = filtroMesIhAnoSelecionado;
    }
    public UploadedFile getArquivoImportacaoSelecionado() {
        return arquivoImportacaoSelecionado;
    }

    public void setArquivoImportacaoSelecionado(UploadedFile arquivoImportacaoSelecionado) {
        this.arquivoImportacaoSelecionado = arquivoImportacaoSelecionado;
    }

	public UnidadeBean getUnidadeSelecionada() {
		return unidadeSelecionada;
	}

	public void setUnidadeSelecionada(UnidadeBean unidadeSelecionada) {
		this.unidadeSelecionada = unidadeSelecionada;
	}
    
}
