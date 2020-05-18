package br.gov.al.maceio.sishosp.hosp.control;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import br.gov.al.maceio.sishosp.acl.model.FuncionarioBean;
import br.gov.al.maceio.sishosp.comum.enums.TipoCabecalho;
import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.shared.DadosSessao;
import br.gov.al.maceio.sishosp.comum.util.DataUtil;
import br.gov.al.maceio.sishosp.comum.util.JSFUtil;
import br.gov.al.maceio.sishosp.comum.util.RedirecionarUtil;
import br.gov.al.maceio.sishosp.comum.util.SessionUtil;
import br.gov.al.maceio.sishosp.comum.util.VerificadorUtil;
import br.gov.al.maceio.sishosp.hosp.dao.CidDAO;
import br.gov.al.maceio.sishosp.hosp.dao.LaudoDAO;
import br.gov.al.maceio.sishosp.hosp.dao.ProcedimentoDAO;
import br.gov.al.maceio.sishosp.hosp.dao.UnidadeDAO;
import br.gov.al.maceio.sishosp.hosp.enums.SituacaoLaudo;
import br.gov.al.maceio.sishosp.hosp.model.AtendimentoBean;
import br.gov.al.maceio.sishosp.hosp.model.CidBean;
import br.gov.al.maceio.sishosp.hosp.model.LaudoBean;
import br.gov.al.maceio.sishosp.hosp.model.PacienteBean;
import br.gov.al.maceio.sishosp.hosp.model.dto.BuscaLaudoDTO;

@ManagedBean(name = "LaudoController")
@ViewScoped
public class LaudoController implements Serializable {

    private static final long serialVersionUID = 1L;
    private LaudoBean laudo;
    private String cabecalho;
    private Integer tipo;
    private List<LaudoBean> listaLaudo;
    private List<CidBean> listaCids;
    private LaudoDAO lDao = new LaudoDAO();
    private CidDAO cDao = new CidDAO();
    private Boolean renderizarDataAutorizacao;
    private Integer idLaudoGerado = null;
    private BuscaLaudoDTO buscaLaudoDTO;
    private UnidadeDAO unidadeDAO;
    private ProcedimentoDAO procedimentoDAO;
    FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
            .getSessionMap().get("obj_usuario");

    // CONSTANTES
    private static final String ENDERECO_CADASTRO = "cadastroLaudoDigita?faces-redirect=true";
    private static final String ENDERECO_GERENCIAMENTO = "gerenciarLaudoDigita?faces-redirect=true";
    private static final String ENDERECO_TIPO = "&amp;tipo=";
    private static final String ENDERECO_ID = "&amp;id=";
    private static final String CABECALHO_INCLUSAO = "Inclusão de Laudo";
    private static final String CABECALHO_ALTERACAO = "Alteração de Laudo";
    private static final String CABECALHO_RENOVACAO = "Renovação de Laudo";

    public LaudoController() {
        this.laudo = new LaudoBean();
        this.cabecalho = "";
        listaLaudo = new ArrayList<>();
        listaCids = new ArrayList<>();
        renderizarDataAutorizacao = false;
        buscaLaudoDTO = new BuscaLaudoDTO();
        buscaLaudoDTO.setSituacao("P");
        buscaLaudoDTO.setTipoBusca("paciente");
        buscaLaudoDTO.setCampoBusca("");
        tipo = 0;
        unidadeDAO = new UnidadeDAO();
        procedimentoDAO = new ProcedimentoDAO();
    }

    public String redirectEdit() {
        return RedirecionarUtil.redirectEdit(ENDERECO_CADASTRO, ENDERECO_ID, this.laudo.getId(), ENDERECO_TIPO, tipo);
    }

    public String redirectInsert() {
        return RedirecionarUtil.redirectInsert(ENDERECO_CADASTRO, ENDERECO_TIPO, tipo);
    }

    public void setaTipoUm() {
        tipo = 1;
    }

    public void setaTipoDois() {
        tipo = 2;
    }

    public void setaTipoTres() {
        tipo = 3;
    }

    public void carregaLaudoPorId(Integer idLaudo) throws ProjetoException {
        this.laudo = lDao.buscarLaudosPorId(idLaudo);
    }

    public void carregaLaudoParaRenovacao(Integer idLaudo) throws ProjetoException {
        this.laudo = lDao.carregaLaudoParaRenovacao(idLaudo);
        calcularPeriodoLaudo();
    }


    public void getEditLaudo() throws ProjetoException {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Map<String, String> params = facesContext.getExternalContext().getRequestParameterMap();
        if ((params.get("id") != null)) {
            Integer id = Integer.parseInt(params.get("id"));
            tipo = Integer.parseInt(params.get("tipo"));
            if (tipo == 2)
                this.laudo = lDao.buscarLaudosPorId(id);
            if (tipo == 3) {
                this.laudo = lDao.carregaLaudoParaRenovacao(id);
                calcularPeriodoLaudo();
            }

            renderizarDadosDeAutorizacao();

        } else {

            tipo = Integer.parseInt(params.get("tipo"));

        }

    }

    public void setaValidadeProcPrimLaudo(Integer validade) {
        laudo.setPeriodo(validade);
        calcularPeriodoLaudo();
    }

    public void renderizarDadosDeAutorizacao() {
        if (laudo.getSituacao().equals(SituacaoLaudo.AUTORIZADO.getSigla())) {
            renderizarDataAutorizacao = true;
        }
    }

    public void limparDados() {
        laudo = new LaudoBean();
    }

    public void calcularPeriodoLaudo() {
        laudo.setMesInicio(null);
        laudo.setMesFinal(null);
        if (laudo.getDataSolicitacao() != null) {
            laudo.setMesInicio(DataUtil.extrairMesDeData(laudo.getDataSolicitacao()));
            laudo.setAnoInicio(DataUtil.extrairAnoDeData(laudo.getDataSolicitacao()));
        }
        if (laudo.getPeriodo() != null && laudo.getPeriodo() != 0 && laudo.getMesInicio() != null
                && laudo.getAnoInicio() != null) {

            int periodo = (laudo.getPeriodo() / 30) - 1; // o periodo do laudo considera o mes atual, por isso a
            // inclusao do -1
            int mes = 0;
            int ano = 0;

            if (laudo.getMesInicio() + periodo > 12) {
                mes = laudo.getMesInicio() + periodo - 12;
                ano = laudo.getAnoInicio() + 1;
            } else {
                mes = laudo.getMesInicio() + periodo;
                ano = laudo.getAnoInicio();
            }

            laudo.setMesFinal(mes);
            laudo.setAnoFinal(ano);

        }

    }

    public void gravarLaudo() {
    	try {
    		if(verificarUnidadeEstaConfiguradaParaValidarDadosDoSigtap()) {
    			validaCboProfissionalParaProcedimento();
    			validaCidParaProcedimento();
    			validaIdadePacienteParaProcedimento();
    		}
    		
			if (!existeLaudoComMesmosDados()) {
				idLaudoGerado = null;
				idLaudoGerado = lDao.cadastrarLaudo(laudo);

				if (idLaudoGerado != null) {
					limparDados();
					JSFUtil.adicionarMensagemSucesso("Laudo cadastrado com sucesso!", "Sucesso");
					JSFUtil.abrirDialog("dlgImprimir");
				} else {
					JSFUtil.adicionarMensagemErro("Ocorreu um erro durante o cadastro!", "Erro");
				}
			}
    	} catch (ProjetoException pe) {
		}
    }
    
    public boolean existeLaudoComMesmosDados() {
    	try {
			if(lDao.existeLaudoComMesmosDados(laudo)) {
				JSFUtil.adicionarMensagemErro
				("Já existe laudo para este Paciente, com as mesmas condições digitadas!", "Erro");
				return true;
			}
		} catch (ProjetoException e) {
			JSFUtil.adicionarMensagemErro(e.getMessage(), "Erro");
			e.printStackTrace();
		}
    	return false;
    }

    public void alterarLaudo() {

    	try {			
		
    		if(verificarUnidadeEstaConfiguradaParaValidarDadosDoSigtap()) {
    			validaCboProfissionalParaProcedimento();
    			validaCidParaProcedimento();
    			validaIdadePacienteParaProcedimento();
    		}
			// if(verificarSeLaudoAssociadoPacienteTerapia()) {
			boolean alterou = lDao.alterarLaudo(laudo);

			if (alterou == true) {
				JSFUtil.adicionarMensagemSucesso("Laudo alterado com sucesso!", "Sucesso");
				JSFUtil.fecharDialog("dlgcadlaudo");
			} else {
				JSFUtil.adicionarMensagemErro("Ocorreu um erro durante a alteração!", "Erro");
			}
			// }
    	} catch (ProjetoException pe) {
		}
    }

    public Boolean verificarUnidadeEstaConfiguradaParaValidarDadosDoSigtap() {
    	return unidadeDAO.verificarUnidadeEstaConfiguradaParaValidarDadosDoSigtap();
    }

    public void validaCboProfissionalParaProcedimento() throws ProjetoException {    	
		if (!procedimentoDAO.validaCboProfissionalParaProcedimento(laudo.getProcedimentoPrimario().getIdProc(),
				laudo.getProfissionalLaudo().getId())) {
			throw new ProjetoException("O profissional " + laudo.getProfissionalLaudo().getNome()
					+ " não possui um CBO válido para o procedimento primário");
		}
    }
    
    public void validaCidParaProcedimento() throws ProjetoException {    	
		if (!procedimentoDAO.validaCidParaProcedimento(laudo.getProcedimentoPrimario().getIdProc(),
				laudo.getCid1().getCid())) {
			throw new ProjetoException("O CID "+laudo.getCid1().getDescCid()+" não é válido para o procedimento primário");
		}
		
		if(!VerificadorUtil.verificarSeObjetoNuloOuVazio(laudo.getCid2().getCid())) {
			if (!procedimentoDAO.validaCidParaProcedimento(laudo.getProcedimentoPrimario().getIdProc(),
					laudo.getCid2().getCid())) {
				throw new ProjetoException("O CID "+laudo.getCid2().getDescCid()+" não é válido para o procedimento primário");
			}
		}
		
		if(!VerificadorUtil.verificarSeObjetoNuloOuVazio(laudo.getCid3().getCid())) {
			if (!procedimentoDAO.validaCidParaProcedimento(laudo.getProcedimentoPrimario().getIdProc(),
					laudo.getCid3().getCid())) {
				throw new ProjetoException("O CID "+laudo.getCid3().getDescCid()+" não é válido para o procedimento primário");
			}
		}
    }
    
    public void validaIdadePacienteParaProcedimento() throws ProjetoException {    	
		if (!procedimentoDAO.validaIdadePacienteParaProcedimento(laudo.getProcedimentoPrimario().getIdProc(),
				laudo.getPaciente().getId_paciente())) {
			throw new ProjetoException("A idade do paciente é inválida para o limite aceito para o procedimento primário");
		}
    }
    
    public Boolean verificarSeLaudoAssociadoPacienteTerapia() {
        if (!lDao.verificarSeLaudoAssociadoPacienteTerapia(laudo.getId())) {
            JSFUtil.adicionarMensagemErro("Não é possível alterar pois o laudo já está associado a um paciente em terapia!", "Erro");
            return false;
        }
        return true;
    }

    public void excluirLaudo() {
        boolean excluiu = lDao.excluirLaudo(laudo);

        if (excluiu == true) {
            JSFUtil.adicionarMensagemSucesso("Laudo excluído com sucesso!", "Sucesso");
            JSFUtil.fecharDialog("dialogExclusao");
        } else {
            JSFUtil.adicionarMensagemErro("Ocorreu um erro durante a exclusão!", "Erro");
            JSFUtil.fecharDialog("dialogExclusao");
        }

    }

    public void listarLaudo(String situacao, String campoBusca, String tipoBusca) throws ProjetoException {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Map<String, String> params = facesContext.getExternalContext().getRequestParameterMap();
        Integer campoBuscaSeTemPacienteLaudoNaSessao = verificarSeExistePacienteLaudoNaSessao();
        if (!VerificadorUtil.verificarSeObjetoNuloOuVazio(campoBuscaSeTemPacienteLaudoNaSessao)) {
            buscaLaudoDTO.setCampoBusca(campoBuscaSeTemPacienteLaudoNaSessao.toString());
            buscaLaudoDTO.setTipoBusca("prontpaciente");
            buscaLaudoDTO.setSituacao("T");
        }


        listaLaudo = lDao.listaLaudos(buscaLaudoDTO.getSituacao(), buscaLaudoDTO.getCampoBusca(), buscaLaudoDTO.getTipoBusca());
    }

    public void iniciaNovoLaudoTelaEvolucao() {
        PacienteBean pacienteLaudo = (PacienteBean) SessionUtil.resgatarDaSessao(DadosSessao.PACIENTE_LAUDO);
        if (!VerificadorUtil.verificarSeObjetoNulo(pacienteLaudo)) {
            laudo.setPaciente(pacienteLaudo);
            laudo.setProfissionalLaudo(user_session);
            if (user_session.getProc1().getIdProc() != null) {
                laudo.getProcedimentoPrimario().setIdProc(user_session.getProc1().getIdProc());
                laudo.getProcedimentoPrimario().setNomeProc(user_session.getProc1().getNomeProc());
                laudo.setPeriodo(90); //laudo.getProcedimentoPrimario().getValidade_laudo());
            }
            laudo.setDataSolicitacao(new java.util.Date());
            laudo.setCid1(null);

            calcularPeriodoLaudo();
        }

    }

    private Integer verificarSeExistePacienteLaudoNaSessao() {
        PacienteBean pacienteLaudo = (PacienteBean) SessionUtil.resgatarDaSessao(DadosSessao.PACIENTE_LAUDO);
        if (!VerificadorUtil.verificarSeObjetoNulo(pacienteLaudo)) {
            return pacienteLaudo.getId_paciente();
        } else {
            return null;
        }
    }

    public void fecharTelaLaudoEvolucao() {
        JSFUtil.fecharDialog("dlgImprimir");
        JSFUtil.fecharDialog("dlglaudo");
        JSFUtil.fecharDialog("dlgcadlaudo");
    }

    public List<CidBean> listaCidAutoCompletePorProcedimento(String query) throws ProjetoException {
        List<CidBean> result = cDao.listarCidsBuscaPorProcedimentoAutoComplete(query);
        return result;
    }

    public void listarCids(String campoBusca) throws ProjetoException {
        listaCids = cDao.listarCidsBusca(campoBusca);
    }

    public LaudoBean getLaudo() {
        return laudo;
    }

    public void setLaudo(LaudoBean laudo) {
        this.laudo = laudo;
    }

    public String getCabecalho() {
        if (this.tipo.equals(TipoCabecalho.INCLUSAO.getSigla())) {
            cabecalho = CABECALHO_INCLUSAO;
        } else if (this.tipo.equals(TipoCabecalho.ALTERACAO.getSigla())) {
            cabecalho = CABECALHO_ALTERACAO;
        } else if (this.tipo.equals(TipoCabecalho.RENOVACAO.getSigla())) {
            cabecalho = CABECALHO_RENOVACAO;
        }
        return cabecalho;
    }

    public void setCabecalho(String cabecalho) {
        this.cabecalho = cabecalho;
    }

    public List<LaudoBean> getListaLaudo() {
        return listaLaudo;
    }

    public void setListaLaudo(List<LaudoBean> listaLaudo) {
        this.listaLaudo = listaLaudo;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public List<CidBean> getListaCids() {
        return listaCids;
    }

    public void setListaCids(List<CidBean> listaCids) {
        this.listaCids = listaCids;
    }

    public Boolean getRenderizarDataAutorizacao() {
        return renderizarDataAutorizacao;
    }

    public void setRenderizarDataAutorizacao(Boolean renderizarDataAutorizacao) {
        this.renderizarDataAutorizacao = renderizarDataAutorizacao;
    }

    public Integer getIdLaudoGerado() {
        return idLaudoGerado;
    }

    public void setIdLaudoGerado(Integer idLaudoGerado) {
        this.idLaudoGerado = idLaudoGerado;
    }


    public BuscaLaudoDTO getBuscaLaudoDTO() {
        return buscaLaudoDTO;
    }

    public void setBuscaLaudoDTO(BuscaLaudoDTO buscaLaudoDTO) {
        this.buscaLaudoDTO = buscaLaudoDTO;
    }
}
