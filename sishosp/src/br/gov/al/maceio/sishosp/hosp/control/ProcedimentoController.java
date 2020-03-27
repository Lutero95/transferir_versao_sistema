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
import br.gov.al.maceio.sishosp.comum.enums.TipoCabecalho;
import br.gov.al.maceio.sishosp.comum.util.JSFUtil;
import br.gov.al.maceio.sishosp.comum.util.RedirecionarUtil;
import br.gov.al.maceio.sishosp.hosp.dao.RecursoDAO;
import br.gov.al.maceio.sishosp.hosp.model.RecursoBean;
import br.gov.saude.servicos.schema.sigtap.procedimento.servicoclassificacao.v1.servicoclassificacao.ServicoClassificacaoType;
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
    
    public ProcedimentoController() {
        this.proc = new ProcedimentoBean();
        this.listaProcedimentos = null;
        cid = new CidBean();
        cbo = new CboBean();
        recurso = new RecursoBean();
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
    
    public void novaCargaSigtap() {
    	for(ProcedimentoBean procedimento : listaProcedimentos) {
    		int detalheAdicional = 0;
    		selecionaDetalheAdicionalEmSequencia(procedimento, detalheAdicional);
    	}
    }

	private void selecionaDetalheAdicionalEmSequencia(ProcedimentoBean procedimento, int detalheAdicional) {
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
    
    private BigInteger detalharProcedimento(ProcedimentoBean procedimento, BigInteger registroInicial, CategoriaDetalheAdicionalType categoriaDetalheAdicional) throws SIGTAPFault {
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
		System.out.println("Detalhamento:" +categoriaDetalheAdicional.name());
		System.out.println("Procedimento:" +resultadosDetalhaProcedimentosType.getProcedimento().getNome());
		if (resultadosDetalhaProcedimentosType.getDetalheAdicional().get(0).getPaginacao() != null) {
			System.out.println("Registro Inicial:" +resultadosDetalhaProcedimentosType.getDetalheAdicional().get(0).getPaginacao().getRegistroInicial());
			System.out.println("Total de registros:" +resultadosDetalhaProcedimentosType.getDetalheAdicional().get(0).getPaginacao().getTotalRegistros());
		}
		else
			return new BigInteger(BigInteger.ZERO.toString());
		return resultadosDetalhaProcedimentosType.getDetalheAdicional().get(0).getPaginacao().getTotalRegistros();	
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
