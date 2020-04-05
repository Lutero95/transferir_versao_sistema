package br.gov.al.maceio.sishosp.hosp.control;

import java.io.Serializable;
import java.math.BigInteger;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import br.gov.al.maceio.sigtapclient.util.ProcedimentoUtil;
import br.gov.al.maceio.sishosp.acl.model.FuncionarioBean;
import br.gov.al.maceio.sishosp.comum.enums.TipoCabecalho;
import br.gov.al.maceio.sishosp.comum.util.JSFUtil;
import br.gov.al.maceio.sishosp.comum.util.RedirecionarUtil;
import br.gov.al.maceio.sishosp.comum.util.VerificadorUtil;
import br.gov.al.maceio.sishosp.hosp.dao.RecursoDAO;
import br.gov.al.maceio.sishosp.hosp.model.RecursoBean;
import br.gov.al.maceio.sishosp.hosp.model.dto.PropriedadeDeProcedimentoMensalExistenteDTO;
import br.gov.al.maceio.sishosp.hosp.model.dto.GravarProcedimentoMensalDTO;
import br.gov.saude.servicos.schema.cbo.v1.cbo.CBOType;
import br.gov.saude.servicos.schema.sigtap.procedimento.financiamento.v1.tipofinanciamento.TipoFinanciamentoType;
import br.gov.saude.servicos.schema.sigtap.procedimento.nivelagregacao.v1.formaorganizacao.FormaOrganizacaoType;
import br.gov.saude.servicos.schema.sigtap.procedimento.renases.v1.renases.RENASESType;
import br.gov.saude.servicos.schema.sigtap.procedimento.servicoclassificacao.v1.servicoclassificacao.ServicoClassificacaoType;
import br.gov.saude.servicos.schema.sigtap.procedimento.v1.instrumentoregistro.InstrumentoRegistroType;
import br.gov.saude.servicos.schema.sigtap.procedimento.v1.modalidadeatendimento.ModalidadeAtendimentoType;
import br.gov.saude.servicos.schema.sigtap.procedimento.v1.procedimento.ProcedimentoType.CIDsVinculados.CIDVinculado;
import br.gov.saude.servicos.sigtap.v1.procedimentoservice.RequestDetalharProcedimento;
import br.gov.saude.servicos.sigtap.v1.procedimentoservice.SIGTAPFault;
import br.gov.saude.servicos.sigtap.v1.procedimentoservice.RequestDetalharProcedimento.DetalhesAdicionais;
import br.gov.saude.servicos.wsdl.mensageria.sigtap.v1.detalheadicional.CategoriaDetalheAdicionalType;
import br.gov.saude.servicos.wsdl.mensageria.sigtap.v1.detalheadicional.DetalheAdicionalType;
import br.gov.saude.servicos.wsdl.mensageria.v1.paginacao.PaginacaoType;
import br.gov.saude.servicos.wsdl.mensageria.v1r0.resultadosdetalhaprocedimentos.ResultadosDetalhaProcedimentosType;
import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.hosp.dao.ProcedimentoDAO;
import br.gov.al.maceio.sishosp.hosp.model.CboBean;
import br.gov.al.maceio.sishosp.hosp.model.CidBean;
import br.gov.al.maceio.sishosp.hosp.model.ProcedimentoBean;

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
    private static final String CABECALHO_INCLUSAO = "Inclusão de Procedimento";
    private static final String CABECALHO_ALTERACAO = "Alteração de Procedimento";
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
    
    private List<ModalidadeAtendimentoType> listaModalidadeAtendimentoNaoGravadosNoBanco;
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
    
    public void novaCargaSigtap() throws ProjetoException {
    	if(!verificaSeHouveCargaDoSigtapEsteMes()) {
    		
			FuncionarioBean user_session = obterUsuarioDaSessao();
			Integer idHistoricoConsumoSigtap = VALOR_ZERO;

			for (int i = 0; i < this.listaProcedimentos.size(); i++) {
				buscaCodigosDeDadosExistentesNoBanco();
				selecionaDetalheAdicionalEmSequencia(this.listaProcedimentos.get(i));
				setaDadosParaListaProcedimentosMensalDTO(i);
				limparListasDadosProcedimentos();
				try {
					idHistoricoConsumoSigtap = procedimentoDao.executaRotinaNovaCargaSigtap(
							listaGravarProcedimentosMensaisDTO.get(i), user_session.getId(), idHistoricoConsumoSigtap);
				} catch (Exception e) {
					fecharDialogAvisoCargaSigtap();
					JSFUtil.adicionarMensagemErro(e.getMessage(), "Erro");
					e.printStackTrace();
				}
			}
			fecharDialogAvisoCargaSigtap();
			JSFUtil.adicionarMensagemSucesso("Dados atualizados com sucesso!", "");
			this.listaGravarProcedimentosMensaisDTO.clear();
    	}
    }
    
    private Boolean verificaSeHouveCargaDoSigtapEsteMes() {
    	try {
    		Boolean houveCargaEsteMes = procedimentoDao.houveCargaDoSigtapEsteMes();
    		if(houveCargaEsteMes) {
    			fecharDialogAvisoCargaSigtap();
        		JSFUtil.adicionarMensagemAdvertencia("O sistema está atualizado, uma carga já foi executada este mês", "");
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

	private void buscaCodigosDeDadosExistentesNoBanco()
			throws ProjetoException {
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
			JSFUtil.adicionarMensagemErro(e.getMessage(), "Erro!");
			e.printStackTrace();
		}
		return detalheAdicional;
	}
    
    private BigInteger detalharProcedimento(ProcedimentoBean procedimento, BigInteger registroInicial, 
    		CategoriaDetalheAdicionalType categoriaDetalheAdicional) throws SIGTAPFault {
    	RequestDetalharProcedimento requestDetalharProcedimento = new RequestDetalharProcedimento();
		requestDetalharProcedimento.setCodigoProcedimento(procedimento.getCodProc());
		
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
		
		ResultadosDetalhaProcedimentosType resultadosDetalhaProcedimentosType = 
			ProcedimentoUtil.detalharProcedimentos(requestDetalharProcedimento);
		
		adicionaProcedimentoParaListaProcedimentoMensalDTO(procedimento, resultadosDetalhaProcedimentosType);
		adicionaDadosFiltradosEmSuasRespectivasVariaveis(categoriaDetalheAdicional, resultadosDetalhaProcedimentosType);
		
		if (VerificadorUtil.verificarSeObjetoNulo(resultadosDetalhaProcedimentosType.getDetalheAdicional().get(0).getPaginacao())) 
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
			adicionaDadosNaoFiltradosEmSuasRespectivasVariaveis(resultadosDetalhaProcedimentosType);
		}
		
		else if(!listaGravarProcedimentosMensaisDTO.get(ultimoIndiceDaListaProcedimentosMensalDTO).getIdProcedimento().equals(procedimento.getIdProc())){
			gravarProcedimentoMensalDTO.setIdProcedimento(procedimento.getIdProc());
			gravarProcedimentoMensalDTO.setProcedimentoMensal(resultadosDetalhaProcedimentosType.getProcedimento());
			listaGravarProcedimentosMensaisDTO.add(gravarProcedimentoMensalDTO);
			adicionaDadosNaoFiltradosEmSuasRespectivasVariaveis(resultadosDetalhaProcedimentosType);
		}
	}

	private void adicionaDadosNaoFiltradosEmSuasRespectivasVariaveis(
			ResultadosDetalhaProcedimentosType resultadosDetalhaProcedimentosType) {
		//MODALIDADE ATENDIMENTO
		if(listaModalidadeAtendimentoExistente.isEmpty()) {
			listaModalidadeAtendimentoNaoGravadosNoBanco.
			addAll(resultadosDetalhaProcedimentosType.getProcedimento().getModalidadesAtendimento().getModalidadeAtendimento());
		}else {
			
			for(ModalidadeAtendimentoType modalidadeAtendimento : 
				resultadosDetalhaProcedimentosType.getProcedimento().getModalidadesAtendimento().getModalidadeAtendimento()) {				
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
		
		//INSTRUMENTO DE REGISTRO
		if(listaInstrumentosRegistroExistentes.isEmpty()) {
			listaInstrumentosRegistroNaoGravadosNoBanco.
			addAll(resultadosDetalhaProcedimentosType.getProcedimento().getInstrumentosRegistro().getInstrumentoRegistro());
		}else {
			
			for(InstrumentoRegistroType instrumentoRegistroType : 
				resultadosDetalhaProcedimentosType.getProcedimento().getInstrumentosRegistro().getInstrumentoRegistro()) {
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
		
		//FORMA ORGANIZAÇÃO
		if(listaFormasDeOrganizacaoExistentes.isEmpty()) {
			formaOrganizacaoNaoGravadaNoBanco = resultadosDetalhaProcedimentosType.getProcedimento().getFormaOrganizacao();
		}else {
			String codigoFormaOrganizacao = resultadosDetalhaProcedimentosType.getProcedimento().getFormaOrganizacao().getCodigo();
			Boolean valorExistente = false;
			for(PropriedadeDeProcedimentoMensalExistenteDTO formaOrganizacaoExistente : listaFormasDeOrganizacaoExistentes) {
				if (formaOrganizacaoExistente.getCodigo().equals(codigoFormaOrganizacao)) {
					idFormasDeOrganizacaoExistente = formaOrganizacaoExistente.getId();
					valorExistente = true;
				}
			}
			if(!valorExistente)
				formaOrganizacaoNaoGravadaNoBanco = resultadosDetalhaProcedimentosType.getProcedimento().getFormaOrganizacao();
		}
		
		//TIPO FINANCIAMENTO
		if(listaTipoFinanciamentoExistente.isEmpty()) {
			tipoFinanciamentoNaoGravadoNoBanco = resultadosDetalhaProcedimentosType.getProcedimento().getTipoFinanciamento();
		}else {
			String codigoTipoFinanciamento = resultadosDetalhaProcedimentosType.getProcedimento().getTipoFinanciamento().getCodigo();
			Boolean valorExistente = false;
			for(PropriedadeDeProcedimentoMensalExistenteDTO tipoFinanciamentoExistente : listaTipoFinanciamentoExistente) {
				if (tipoFinanciamentoExistente.getCodigo().equals(codigoTipoFinanciamento)) {
					idTipoFinanciamentoExistente = tipoFinanciamentoExistente.getId();
					valorExistente = true;
				}
			}
			if (!valorExistente)
				tipoFinanciamentoNaoGravadoNoBanco = resultadosDetalhaProcedimentosType.getProcedimento().getTipoFinanciamento();
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

	public void listarProcedimentosQueGeramLaudo() throws ProjetoException {
        this.listaProcedimentos = procedimentoDao.listarProcedimentoLaudo();
        
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
}
