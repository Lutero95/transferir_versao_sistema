package br.gov.al.maceio.sishosp.hosp.control;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import br.gov.al.maceio.sishosp.comum.enums.TipoCabecalho;
import br.gov.al.maceio.sishosp.comum.util.JSFUtil;
import br.gov.al.maceio.sishosp.comum.util.RedirecionarUtil;
import br.gov.al.maceio.sishosp.comum.util.VerificadorUtil;
import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.hosp.dao.CboDAO;
import br.gov.al.maceio.sishosp.hosp.dao.GrupoDAO;
import br.gov.al.maceio.sishosp.hosp.dao.ProcedimentoDAO;
import br.gov.al.maceio.sishosp.hosp.dao.ProgramaDAO;
import br.gov.al.maceio.sishosp.hosp.enums.TipoProcedimentoPrograma;
import br.gov.al.maceio.sishosp.hosp.model.CboBean;
import br.gov.al.maceio.sishosp.hosp.model.CidBean;
import br.gov.al.maceio.sishosp.hosp.model.EspecialidadeBean;
import br.gov.al.maceio.sishosp.hosp.model.GrupoBean;
import br.gov.al.maceio.sishosp.hosp.model.ProcedimentoBean;
import br.gov.al.maceio.sishosp.hosp.model.ProgramaBean;
import br.gov.al.maceio.sishosp.hosp.model.dto.BuscaGrupoFrequenciaDTO;
import br.gov.al.maceio.sishosp.hosp.model.dto.ProcedimentoCboEspecificoDTO;
import br.gov.al.maceio.sishosp.hosp.model.dto.ProcedimentoIdadeEspecificaDTO;

@ManagedBean(name = "ProgramaController")
@ViewScoped
public class ProgramaController implements Serializable {

    private static final long serialVersionUID = 1L;
    private ProgramaBean prog;
    private List<ProgramaBean> listaProgramas;
    private List<ProgramaBean> buscalistaProgramas;
    private Integer tipo;
    private String cabecalho;
    private ProgramaDAO pDao = new ProgramaDAO();
    private List<ProgramaBean> listaProgramasUsuario, listaFiltradaProgramasusuario;
    private GrupoDAO gDao = new GrupoDAO();
    private List<ProcedimentoBean> listaProcedimentos;
    private List<CboBean> listaCbos;
    private ProcedimentoDAO procedimentoDAO;
    private CboDAO cboDAO;
    private ProcedimentoBean procedimentoSelecionado;
    private CboBean cboSelecionado;
    private GrupoBean grupo;
    private BuscaGrupoFrequenciaDTO grupoFrequenciaDTOSelecionado;
    private Integer frequencia;
    private String tipoProcedimento;
    private ProcedimentoIdadeEspecificaDTO procedimentoIdadeEspecificaSelecionado;

    //CONSTANTES
    private static final String ENDERECO_CADASTRO = "cadastroPrograma?faces-redirect=true";
    private static final String ENDERECO_TIPO = "&amp;tipo=";
    private static final String ENDERECO_ID = "&amp;id=";
    private static final String CABECALHO_INCLUSAO = "Inclusão de Programa";
    private static final String CABECALHO_ALTERACAO = "Alteração de Programa";
    private static final Integer VALOR_MAXIMO_IDADE = 130; 

    public ProgramaController() {
        this.prog = new ProgramaBean();
        this.listaProgramas = new ArrayList<ProgramaBean>();;
        buscalistaProgramas = new ArrayList<>();
        listaProgramasUsuario = new ArrayList<ProgramaBean>();
        listaFiltradaProgramasusuario = new ArrayList<ProgramaBean>();
        buscalistaProgramas = null;
        this.listaProcedimentos = new ArrayList<>();
        this.listaCbos = new ArrayList<>();
        this.procedimentoDAO = new ProcedimentoDAO();
        this.cboDAO = new CboDAO();
        this.procedimentoSelecionado = new ProcedimentoBean();
        this.cboSelecionado = new CboBean();
    }

    public String redirectEdit() {
        return RedirecionarUtil.redirectEdit(ENDERECO_CADASTRO, ENDERECO_ID, this.prog.getIdPrograma(), ENDERECO_TIPO, tipo);
    }

    public String redirectInsert() {
        return RedirecionarUtil.redirectInsert(ENDERECO_CADASTRO, ENDERECO_TIPO, tipo);
    }


    public void getEditProg() throws ProjetoException {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Map<String, String> params = facesContext.getExternalContext()
                .getRequestParameterMap();
        if (params.get("id") != null) {
            Integer id = Integer.parseInt(params.get("id"));
            tipo = Integer.parseInt(params.get("tipo"));
            this.prog = pDao.listarProgramaPorId(id);
        } else {
            tipo = Integer.parseInt(params.get("tipo"));
        }

    }

    private void limparDados() throws ProjetoException {
        prog = new ProgramaBean();
        listaProgramas = pDao.listarProgramas();
    }
    
    public void adicionarProcedimentoPadrao(ProcedimentoBean procedimento) {
    	if(!existeProcedimentoCbo(procedimento) 
    			&& !existeProcedimentoComIdadeAdicionado(procedimento)
    			&& !existeProcedimentoPermitido(procedimento)) {
    		this.prog.setProcedimento(procedimento);
    		JSFUtil.fecharDialog("dlgConsulProc");
    	}
    }

    public void gravarPrograma() throws ProjetoException {
       
    	if (this.prog.getListaGrupoFrequenciaDTO().isEmpty()) {
            JSFUtil.adicionarMensagemAdvertencia("É necessário ao menos um grupo!", "Advertência");
        } else {
        	if(procedimentoPadraoNaoEhNulo() && validarDiasAtivoPacienteSemLaudo()) {
				boolean cadastrou = pDao.gravarPrograma(this.prog);
				if (cadastrou == true) {
					limparDados();
					JSFUtil.adicionarMensagemSucesso("Programa cadastrado com sucesso!", "Sucesso");
				} else {
					JSFUtil.adicionarMensagemErro("Ocorreu um erro durante o cadastro!", "Erro");
				}
				listaProgramas = pDao.listarProgramas();
        	}
       }
    }
    
    private boolean procedimentoPadraoNaoEhNulo() {
    	if(VerificadorUtil.verificarSeObjetoNuloOuZero(this.prog.getProcedimento().getIdProc())) {
    		JSFUtil.adicionarMensagemAdvertencia("Selecione um procedimento padrão", "");
    		return false;
    	}
    	return true;
    }

    public void alterarPrograma() throws ProjetoException {
        if (this.prog.getListaGrupoFrequenciaDTO().isEmpty()) {
            JSFUtil.adicionarMensagemAdvertencia("É necessário ao menos um grupo!", "Advertência");
        } else if (validarDiasAtivoPacienteSemLaudo()){
            boolean alterou = pDao.alterarPrograma(this.prog);
            if (alterou == true) {
                JSFUtil.adicionarMensagemSucesso("Programa alterado com sucesso!", "Sucesso");
            } else {
                JSFUtil.adicionarMensagemErro("Ocorreu um erro durante a alteração!", "Erro");
            }
        }
    }

    public void excluirPrograma() throws ProjetoException {
        boolean excluiu = pDao.excluirPrograma(prog);
        if (excluiu == true) {
            JSFUtil.adicionarMensagemSucesso("Programa excluído com sucesso!", "Sucesso");
            JSFUtil.fecharDialog("dialogExclusao");
        } else {
            JSFUtil.adicionarMensagemErro("Ocorreu um erro durante a exclusão!", "Erro");
            JSFUtil.fecharDialog("dialogExclusao");
        }
        listaProgramas = pDao.listarProgramas();
    }

    public void carregaListaProgramas() throws ProjetoException {
    	listaProgramas = pDao.listarProgramas();
    }

    public List<ProgramaBean> listaProgramaAutoComplete(String query)
            throws ProjetoException {
        List<ProgramaBean> result = pDao.listarProgramasBusca(query, 1);
        return result;
    }

    public List<ProgramaBean> listaProgramaAutoCompleteUsuario(String query)
            throws ProjetoException {
        List<ProgramaBean> result = pDao.listarProgramasBuscaUsuario(query, 1);
        return result;
    }

    public void carregaListaProgramasUsuario()
            throws ProjetoException {
       listaProgramasUsuario = pDao.listarProgramasUsuario();
    }

    public ProgramaBean getProg() {
        return prog;
    }

    public void setProg(ProgramaBean prog) {
        this.prog = prog;
    } 

    public List<ProgramaBean> getListaProgramas() {
		return listaProgramas;
	}

	public void setListaProgramas(List<ProgramaBean> listaProgramas) {
		this.listaProgramas = listaProgramas;
	}

    public String getCabecalho() {
        if (this.tipo.equals(TipoCabecalho.INCLUSAO.getSigla())) {
            cabecalho = CABECALHO_INCLUSAO;
        } else if (this.tipo.equals(TipoCabecalho.ALTERACAO.getSigla())) {
            cabecalho = CABECALHO_ALTERACAO;
        }
        return cabecalho;
    }
    
    public void listarProcedimentosIhCbos() throws ProjetoException {
    	listarProcedimentos();
    	listarCbo();
    }
    
    private void listarProcedimentos() throws ProjetoException {
    	this.listaProcedimentos = this.procedimentoDAO.listarProcedimento();
    }
    
    private void listarCbo() throws ProjetoException {
    	this.listaCbos = this.cboDAO.listarCbo();
    }
    
    public void limparProcedimentoIhCboSelecionado() {
    	this.procedimentoSelecionado = new ProcedimentoBean();
    	this.cboSelecionado = new CboBean();
    }
    
    public void adicionarProcedimentoCboEspecifico() {
    	ProcedimentoCboEspecificoDTO procedimentoCboEspecifico = new ProcedimentoCboEspecificoDTO();
    	procedimentoCboEspecifico.setProcedimento(this.procedimentoSelecionado);
    	procedimentoCboEspecifico.setCbo(this.cboSelecionado);
    	
    	if(!existeEsteCbo(procedimentoCboEspecifico)
    			&& !existeProcedimentoComIdadeAdicionado(procedimentoCboEspecifico.getProcedimento())
    			&& !existeProcedimentoPadrao(procedimentoCboEspecifico.getProcedimento())
    			&& !existeProcedimentoPermitido(procedimentoCboEspecifico.getProcedimento())) {
    		this.prog.getListaProcedimentoCboEspecificoDTO().add(procedimentoCboEspecifico);
    		JSFUtil.fecharDialog("dlgConsulProcOcup");
    	}
    }
    
    private boolean existeEsteCbo(ProcedimentoCboEspecificoDTO procedimentoCboEspecifico) {
    	for (ProcedimentoCboEspecificoDTO procedimentoIhCbo : this.prog.getListaProcedimentoCboEspecificoDTO()) {
    		if(procedimentoIhCbo.getCbo().getCodCbo().equals(procedimentoCboEspecifico.getCbo().getCodCbo())) {
    			JSFUtil.adicionarMensagemAdvertencia("Já exite este CBO inserido!", "");
    			return true;    			
    		}
		}
    	return false;
    }
    
    public void removerProcedimentoCboEspecifico(ProcedimentoCboEspecificoDTO procedimentoCboEspecifico) {
    	this.prog.getListaProcedimentoCboEspecificoDTO().remove(procedimentoCboEspecifico);
    }
    
    public void validaFrequencia() {
    	if(VerificadorUtil.verificarSeObjetoNuloOuZero(this.frequencia))
    		JSFUtil.adicionarMensagemErro("Frequência: Campo Obrigatório", "");
    	else if (VerificadorUtil.verificarSeObjetoNuloOuMenorQueZero(this.frequencia))
    		JSFUtil.adicionarMensagemErro("Frequência não pode ser menor ou igual a zero", "");
    	else {
    		JSFUtil.fecharDialog("dlgFreq");
    		JSFUtil.fecharDialog("dlgConsuGrupos");
    		
    		if(!grupoFoiAdicionado(this.grupo)) {
    			BuscaGrupoFrequenciaDTO buscaGrupoFrequenciaDTO = new BuscaGrupoFrequenciaDTO(this.grupo, this.frequencia);
    			this.prog.getListaGrupoFrequenciaDTO().add(buscaGrupoFrequenciaDTO);
    		}
    	}
    }
    
    private boolean grupoFoiAdicionado(GrupoBean grupo) {
    	for(BuscaGrupoFrequenciaDTO buscaGrupoFrequenciaDTO : this.prog.getListaGrupoFrequenciaDTO()) {
    		if(buscaGrupoFrequenciaDTO.getGrupo().getIdGrupo().equals(grupo.getIdGrupo())) {
    			JSFUtil.adicionarMensagemErro("Grupo "+grupo.getDescGrupo()+" já foi adicionado", "");
    			return true;    			
    		}
    	}
    	return false;
    }
    
    public void removerGrupoFrequenciaDTO(BuscaGrupoFrequenciaDTO buscaGrupoFrequenciaDTO) {
    	this.prog.getListaGrupoFrequenciaDTO().remove(buscaGrupoFrequenciaDTO);
    }
    
    public void selecionarProcedimento(ProcedimentoBean procedimento) {
    	this.procedimentoSelecionado = procedimento;
    }
    
    public void configuraDialogProcedimentosParaProcedimentoPadrao() {
    	this.tipoProcedimento = TipoProcedimentoPrograma.PROCEDIMENTO_PADRAO.getSigla();
    }
    
    public void configuraDialogProcedimentosParaProcedimentoIdade() {
    	this.tipoProcedimento = TipoProcedimentoPrograma.PROCEDIMENTO_IDADE.getSigla();
    }
    
    public void configuraDialogProcedimentosParaProcedimentoPermitido() {
    	this.tipoProcedimento = TipoProcedimentoPrograma.PROCEDIMENTO_PERMITIDO.getSigla();
    }
    
    public void selecionaProcedimentoIdadeEspecifica() {
    	this.procedimentoIdadeEspecificaSelecionado = new ProcedimentoIdadeEspecificaDTO();
    	this.procedimentoIdadeEspecificaSelecionado.setProcedimento(this.procedimentoSelecionado);
    }
    
    public void adicionarProcedimentoIdadeEspecifica() {
    	
    	Integer idadeMinima = procedimentoIdadeEspecificaSelecionado.getIdadeMinima();
    	Integer idadeMaxima = procedimentoIdadeEspecificaSelecionado.getIdadeMaxima();
    	
    	if(!VerificadorUtil.verificarSeObjetoNuloOuMenorQueZero(idadeMinima) && 
    			!VerificadorUtil.verificarSeObjetoNuloOuMenorQueZero(idadeMaxima)
    			&& !idadeMinimaEhMaiorIdadeMaxima(idadeMinima, idadeMaxima)
    			&& !idadeMaiorLimitePermitido(idadeMinima, idadeMaxima)) {
    		
			if (!existeProcedimentoComIdadeAdicionado(procedimentoIdadeEspecificaSelecionado.getProcedimento())
					&& !existeProcedimentoPadrao(procedimentoIdadeEspecificaSelecionado.getProcedimento())
					&& !existeProcedimentoCbo(procedimentoIdadeEspecificaSelecionado.getProcedimento())
					&& !existeProcedimentoPermitido(procedimentoIdadeEspecificaSelecionado.getProcedimento())) {
				this.prog.getListaProcedimentoIdadeEspecificaDTO().add(procedimentoIdadeEspecificaSelecionado);
				JSFUtil.fecharDialog("dlgConsulProc");
				JSFUtil.fecharDialog("dlgIdade");
			}
    	}
    	else {
    		JSFUtil.adicionarMensagemErro("Há um valor inválido!", "Erro");
    	}
    }
    
    
    private boolean existeProcedimentoComIdadeAdicionado(ProcedimentoBean procedimento) {
    	for (ProcedimentoIdadeEspecificaDTO procedimentoIdade : this.prog.getListaProcedimentoIdadeEspecificaDTO()) {
    		if(procedimentoIdade.getProcedimento().getIdProc().equals(procedimento.getIdProc())) {
    			JSFUtil.adicionarMensagemAdvertencia("Procedimento já foi inserido na lista de Procedimentos para Idades Específicas!", "");
    			return true;    			
    		}
		}
    	return false;
    }
    
    private boolean existeProcedimentoPadrao(ProcedimentoBean procedimento) {
    	if(!VerificadorUtil.verificarSeObjetoNuloOuZero(this.prog.getProcedimento().getIdProc()) 
    			&& this.prog.getProcedimento().getIdProc().equals(procedimento.getIdProc())) {
    		JSFUtil.adicionarMensagemAdvertencia("Não é possível adicionar o procedimento padrão novamente", "");
    		return true;
    	}
    	return false;
    }
    
    private boolean existeProcedimentoCbo(ProcedimentoBean procedimento) {
    	for (ProcedimentoCboEspecificoDTO procedimentoCbo : this.prog.getListaProcedimentoCboEspecificoDTO()) {
    		if(procedimentoCbo.getProcedimento().getIdProc().equals(procedimento.getIdProc())) {
    			JSFUtil.adicionarMensagemAdvertencia("Procedimento já foi inserido na lista de Procedimentos para Ocupações Específicas", "");
    			return true;    			
    		}
		}
    	return false;
    }
    
    private boolean idadeMinimaEhMaiorIdadeMaxima(Integer idadeMinima, Integer idadeMaxima) {
    	if(idadeMinima > idadeMaxima) {
    		JSFUtil.adicionarMensagemErro("Idade Mínima não pode ser maior que a Idade Máxima", "Erro");
    		return true;
    	}
    	return false;
    }
    
    private boolean idadeMaiorLimitePermitido(Integer idadeMinima, Integer idadeMaxima) {
    	if(idadeMinima > VALOR_MAXIMO_IDADE || idadeMaxima > VALOR_MAXIMO_IDADE) {
    		JSFUtil.adicionarMensagemErro("Idade não pode ser maior que 130", "Erro");
    		return true;
    	}
    	return false;
    }
    
    public void removerProcedimentoIdadeEspecifica(ProcedimentoIdadeEspecificaDTO procedimentoIdadeEspecificaDTO) {
     	for (int i = 0; i < this.prog.getListaProcedimentoIdadeEspecificaDTO().size(); i++) {
    		if(this.prog.getListaProcedimentoIdadeEspecificaDTO().get(i).getProcedimento().getIdProc().equals(procedimentoIdadeEspecificaDTO.getProcedimento().getIdProc())) {
    			this.prog.getListaProcedimentoIdadeEspecificaDTO().remove(i);
    			break;
    		}
		}
    }
    
    public void adicionarEspecialidade(EspecialidadeBean especialidade) {
    	if(!existeEspecialidade(especialidade)) {
    		prog.getListaEspecialidadesEspecificas().add(especialidade);
    		JSFUtil.fecharDialog("dlgConsulEspecialidade");
    	}
    }
    
    private boolean existeEspecialidade(EspecialidadeBean especialidadeSelecionada) {
    	
    	for (EspecialidadeBean especialidade : prog.getListaEspecialidadesEspecificas()) {
    		if(especialidadeSelecionada.getCodEspecialidade().equals(especialidade.getCodEspecialidade())) {
    			JSFUtil.adicionarMensagemAdvertencia("Não é possível adicionar a especialidade novamente", "");
    			return true;
    		}			
		} 
    	return false;
    }
    
    public void removerEspecialidade(EspecialidadeBean especialidadeSelecionada) {
    	prog.getListaEspecialidadesEspecificas().remove(especialidadeSelecionada);
    }
    
    public void adicionarProcedimentoPermitido(ProcedimentoBean procedimento) {
    	if (!existeProcedimentoPermitido(procedimento)) {
    		prog.getListaProcedimentosPermitidos().add(procedimento);
    		JSFUtil.fecharDialog("dlgConsulProc");
    	}
    }
    
    private boolean existeProcedimentoPermitido(ProcedimentoBean procedimentoSelecionado) {
    	for (ProcedimentoBean procedimento : prog.getListaProcedimentosPermitidos()) {
    		if(procedimentoSelecionado.getIdProc().equals(procedimento.getIdProc())) {
    			JSFUtil.adicionarMensagemAdvertencia("Não é possível adicionar o procedimento permitido novamente", "");
    			return true;
    		}			
		} 
    	return false;
    }
    
    public void removerProcedimentoPermitido(ProcedimentoBean procedimentoSelecionado) {
    	prog.getListaProcedimentosPermitidos().remove(procedimentoSelecionado);
    }
    
    public void adicionarCidPermitido(CidBean cid) {
    	if (!existeCidPermitido(cid)) {
    		prog.getListaCidsPermitidos().add(cid);
    		JSFUtil.fecharDialog("dlgConsulCid");
    	}
    }
    
    private boolean existeCidPermitido(CidBean cidSelecionado) {
    	for (CidBean cid : prog.getListaCidsPermitidos()) {
    		if(cidSelecionado.getIdCid().equals(cid.getIdCid())) {
    			JSFUtil.adicionarMensagemAdvertencia("Não é possível adicionar o CID novamente", "");
    			return true;
    		}			
		} 
    	return false;
    }
    
    public void removerCidPermitido(CidBean cidSelecionado) {
    	prog.getListaCidsPermitidos().remove(cidSelecionado);
    }
    
    public void limparFrequencia() {
    	this.frequencia = 0;
    }
    
    public void corrigeDiasAtivoPacienteSemLaudo(boolean permitePacienteSemLaudo) {
    	if(!permitePacienteSemLaudo)
    		this.prog.setDiasPacienteSemLaudoAtivo(0);
    }
    
    private boolean validarDiasAtivoPacienteSemLaudo() {
    	if(this.prog.isPermitePacienteSemLaudo() && 
    			VerificadorUtil.verificarSeObjetoNuloOuMenorQueUm(this.prog.getDiasPacienteSemLaudoAtivo())){
    		JSFUtil.adicionarMensagemErro("Dias Ativo Paciente sem Laudo deve ser maior que zero", "");
    		return false;
    	}
    	
    	return true;
    }
    
    public void selecionarCbo(CboBean cbo) {
    	this.cboSelecionado = cbo;
    }

    public void setCabecalho(String cabecalho) {
        this.cabecalho = cabecalho;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public List<ProgramaBean> getBuscalistaProgramas() throws ProjetoException {
        if (buscalistaProgramas == null) {

            buscalistaProgramas = pDao.BuscalistarProgramas();
        }
        return buscalistaProgramas;
    }

    public void setBuscalistaProgramas(List<ProgramaBean> buscalistaProgramas) {
        this.buscalistaProgramas = buscalistaProgramas;
    }

	public List<ProgramaBean> getListaProgramasUsuario() {
		return listaProgramasUsuario;
	}

	public void setListaProgramasUsuario(List<ProgramaBean> listaProgramasUsuario) {
		this.listaProgramasUsuario = listaProgramasUsuario;
	}

	public List<ProgramaBean> getListaFiltradaProgramasusuario() {
		return listaFiltradaProgramasusuario;
	}

	public void setListaFiltradaProgramasusuario(List<ProgramaBean> listaFiltradaProgramasusuario) {
		this.listaFiltradaProgramasusuario = listaFiltradaProgramasusuario;
	}

	public List<ProcedimentoBean> getListaProcedimentos() {
		return listaProcedimentos;
	}

	public void setListaProcedimentos(List<ProcedimentoBean> listaProcedimentos) {
		this.listaProcedimentos = listaProcedimentos;
	}

	public List<CboBean> getListaCbos() {
		return listaCbos;
	}

	public void setListaCbos(List<CboBean> listaCbos) {
		this.listaCbos = listaCbos;
	}

	public ProcedimentoBean getProcedimentoSelecionado() {
		return procedimentoSelecionado;
	}

	public void setProcedimentoSelecionado(ProcedimentoBean procedimentoSelecionado) {
		this.procedimentoSelecionado = procedimentoSelecionado;
	}

	public CboBean getCboSelecionado() {
		return cboSelecionado;
	}

	public void setCboSelecionado(CboBean cboSelecionado) {
		this.cboSelecionado = cboSelecionado;
	}

	public GrupoBean getGrupo() {
		return grupo;
	}

	public void setGrupo(GrupoBean grupo) {
		this.grupo = grupo;
	}

	public Integer getFrequencia() {
		return frequencia;
	}

	public void setFrequencia(Integer frequencia) {
		this.frequencia = frequencia;
	}

	public BuscaGrupoFrequenciaDTO getGrupoFrequenciaDTOSelecionado() {
		return grupoFrequenciaDTOSelecionado;
	}

	public void setGrupoFrequenciaDTOSelecionado(BuscaGrupoFrequenciaDTO grupoFrequenciaDTOSelecionado) {
		this.grupoFrequenciaDTOSelecionado = grupoFrequenciaDTOSelecionado;
	}

	public String getTipoProcedimento() {
		return tipoProcedimento;
	}

	public void setTipoProcedimento(String tipoProcedimento) {
		this.tipoProcedimento = tipoProcedimento;
	}

	public ProcedimentoIdadeEspecificaDTO getProcedimentoIdadeEspecificaSelecionado() {
		return procedimentoIdadeEspecificaSelecionado;
	}

	public void setProcedimentoIdadeEspecificaSelecionado(
			ProcedimentoIdadeEspecificaDTO procedimentoIdadeEspecificaSelecionado) {
		this.procedimentoIdadeEspecificaSelecionado = procedimentoIdadeEspecificaSelecionado;
	}
	
}
