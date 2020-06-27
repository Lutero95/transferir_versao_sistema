package br.gov.al.maceio.sishosp.hosp.control;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//import javax.el.EvaluationListener;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import br.gov.al.maceio.sishosp.acl.model.FuncionarioBean;
import br.gov.al.maceio.sishosp.comum.enums.TipoCabecalho;
import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.JSFUtil;
import br.gov.al.maceio.sishosp.comum.util.RedirecionarUtil;
import br.gov.al.maceio.sishosp.comum.util.VerificadorUtil;
import br.gov.al.maceio.sishosp.hosp.dao.EspecialidadeDAO;
import br.gov.al.maceio.sishosp.hosp.dao.GrupoDAO;
import br.gov.al.maceio.sishosp.hosp.dao.UnidadeDAO;
import br.gov.al.maceio.sishosp.hosp.model.EquipeBean;
import br.gov.al.maceio.sishosp.hosp.model.GrupoBean;
import br.gov.al.maceio.sishosp.hosp.model.ProgramaBean;
import br.gov.al.maceio.sishosp.hosp.model.ProgramaGrupoEvolucaoBean;
import br.gov.al.maceio.sishosp.hosp.model.TipoAtendimentoBean;
import br.gov.al.maceio.sishosp.hosp.model.UnidadeBean;
import br.gov.al.maceio.sishosp.hosp.model.dto.ConfiguracaoAgendaEquipeEspecialidadeDTO;
import net.sf.jasperreports.components.list.UnusedSpaceImageRenderer;

@ManagedBean(name = "UnidadeController")
@ViewScoped
public class UnidadeController implements Serializable {

    private static final long serialVersionUID = 1L;
    private UnidadeBean unidade;
    private ProgramaGrupoEvolucaoBean evolucaoAux;
    private Integer tipo;
    private String cabecalho;
    private UnidadeDAO eDao = new UnidadeDAO();
    private ArrayList<String> listaEstados;
    private List<GrupoBean> listaGruposProgramasOpm, listaGruposProgramasEvolucao;
    private GrupoDAO gDao = new GrupoDAO();
    private List<UnidadeBean> listaUnidades;

    //CONSTANTES
    private static final String ENDERECO_CADASTRO = "cadastrounidade?faces-redirect=true";
    private static final String ENDERECO_TIPO = "&amp;tipo=";
    private static final String ENDERECO_ID = "&amp;id=";
    private static final String CABECALHO_INCLUSAO = "Inclusão de Unidade";
    private static final String CABECALHO_ALTERACAO = "Alteração de Unidade";

    public UnidadeController() {
    	evolucaoAux = new ProgramaGrupoEvolucaoBean();
        this.unidade = new UnidadeBean();
        this.cabecalho = "";
        listaUnidades = new ArrayList<>();
        listaGruposProgramasOpm = new ArrayList<>();
        listaGruposProgramasEvolucao = new ArrayList<>();
        listaEstados = new ArrayList<>();
        listaEstados.add("AC");
        listaEstados.add("AL");
        listaEstados.add("AM");
        listaEstados.add("AP");
        listaEstados.add("BA");
        listaEstados.add("CE");
        listaEstados.add("DF");
        listaEstados.add("ES");
        listaEstados.add("GO");
        listaEstados.add("MA");
        listaEstados.add("MG");
        listaEstados.add("MS");
        listaEstados.add("MT");
        listaEstados.add("PA");
        listaEstados.add("PB");
        listaEstados.add("PE");
        listaEstados.add("PI");
        listaEstados.add("PR");
        listaEstados.add("RJ");
        listaEstados.add("RN");
        listaEstados.add("RO");
        listaEstados.add("RR");
        listaEstados.add("RS");
        listaEstados.add("SC");
        listaEstados.add("SE");
        listaEstados.add("SP");
        listaEstados.add("TO");

    }

    public void limparDados() {
        this.unidade = new UnidadeBean();
    }

    public String redirectEdit() {
        return RedirecionarUtil.redirectEdit(ENDERECO_CADASTRO, ENDERECO_ID, this.unidade.getId(), ENDERECO_TIPO, tipo);
    }

    public String redirectInsert() {
        return RedirecionarUtil.redirectInsert(ENDERECO_CADASTRO, ENDERECO_TIPO, tipo);
    }

    public void getEditUnidade() throws ProjetoException {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Map<String, String> params = facesContext.getExternalContext()
                .getRequestParameterMap();
        if (params.get("id") != null) {
            Integer id = Integer.parseInt(params.get("id"));
            tipo = Integer.parseInt(params.get("tipo"));
            this.unidade = eDao.buscarUnidadePorId(id);
        } else {

            tipo = Integer.parseInt(params.get("tipo"));

        }

    }
    
    public void limparGrupoNaBuscaEvolucao() {
        evolucaoAux.setGrupo(new GrupoBean());
    }
    
    public Boolean validarProgramaGrupoDataInicioEvolucao() {
        Boolean retorno = true;
        if ((VerificadorUtil.verificarSeObjetoNulo(evolucaoAux.getPrograma()) || (VerificadorUtil.verificarSeObjetoNulo(evolucaoAux.getPrograma().getIdPrograma()))) ||
        		((VerificadorUtil.verificarSeObjetoNulo(evolucaoAux.getGrupo()))  || (VerificadorUtil.verificarSeObjetoNulo(evolucaoAux.getGrupo().getIdGrupo()))) ||
        (VerificadorUtil.verificarSeObjetoNuloOuZero(evolucaoAux.getDataInicioEvolucao()))){
            retorno = false;
        }
        return retorno;
    }    
    
    public void validarAdicionarProgramaGrupoEvolucaoo() throws ProjetoException {

        if(!validarProgramaGrupoDataInicioEvolucao()){
        	JSFUtil.adicionarMensagemAdvertencia("Informe o Programa, Grupo e a data de início da evolução", "Atenção");
            return;
        }

        boolean existe = false;
        if (unidade.getListaProgramasGrupoEvolucao().size() == 0) {
        	adicionarProgramaGrupoInicioEvolucao();
        } else {

            for (int i = 0; i < unidade.getListaProgramasGrupoEvolucao().size(); i++) {
                if ((unidade.getListaProgramasGrupoEvolucao().get(i).getPrograma().getIdPrograma() == evolucaoAux.getPrograma().getIdPrograma()) && (unidade.getListaProgramasGrupoEvolucao().get(i).getGrupo().getIdGrupo() == evolucaoAux.getGrupo().getIdGrupo())) {
                    existe = true;
                }
            }
            if (existe == false) {
                adicionarProgramaGrupoInicioEvolucao();
            } else {
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        "Esse Programa/Grupo já foi adicionado!", "Erro");
                FacesContext.getCurrentInstance().addMessage(null, msg);
            }

        }

    }    

    private void adicionarProgramaGrupoInicioEvolucao() throws ProjetoException {
        unidade.getListaProgramasGrupoEvolucao().add(evolucaoAux);
        evolucaoAux = new ProgramaGrupoEvolucaoBean();
    }    
    public void gravarUnidade() throws ProjetoException {
    	boolean cadastrou = false;
    	if(validaDiasAcessoPermitidoAoSistema())
    		cadastrou = eDao.gravarUnidade(unidade);

        if (cadastrou == true) {
            limparDados();
            JSFUtil.adicionarMensagemSucesso("Unidade cadastrada com sucesso!", "Sucesso");
        } else {
            JSFUtil.adicionarMensagemErro("Ocorreu um erro durante o cadastro!", "Erro");
        }
    }

    public void alterarUnidade() throws ProjetoException {
        boolean alterou = false;
        if(validaDiasAcessoPermitidoAoSistema())
        	alterou = eDao.alterarUnidade(unidade);

        if (alterou == true) {
        	limpaHorarioDeInicioIhFimDeFuncionamento();
            JSFUtil.adicionarMensagemSucesso("Unidade alterada com sucesso!", "Sucesso");
        } else {
            JSFUtil.adicionarMensagemErro("Ocorreu um erro durante a alteração!", "Erro");
        }
    }
    
    private boolean validaDiasAcessoPermitidoAoSistema() {
    	if(!unidade.getParametro().isAcessoPermitidoDomingo() && !unidade.getParametro().isAcessoPermitidoSegunda() &&
    			!unidade.getParametro().isAcessoPermitidoTerca() && !unidade.getParametro().isAcessoPermitidoQuarta() &&
    			!unidade.getParametro().isAcessoPermitidoQuinta() && !unidade.getParametro().isAcessoPermitidoSexta() && 
    			!unidade.getParametro().isAcessoPermitidoSabado()) {
    		JSFUtil.adicionarMensagemAdvertencia("Selecione pelo menos um dia de acesso permitido ao sistema", "");
    		return false;
    	}
    	return true;
    }

	private void limpaHorarioDeInicioIhFimDeFuncionamento() {
		if(!unidade.getParametro().getUsaHorarioLimiteParaAcesso()) {
			unidade.getParametro().setHorarioInicioFuncionamento(null);
			unidade.getParametro().setHorarioFinalFuncionamento(null);
		}
	}

    public void desativarUnidade() throws ProjetoException {
        boolean desativou = eDao.desativarUnidade(unidade);

        if (desativou == true) {
            JSFUtil.adicionarMensagemSucesso("Unidade desativada com sucesso!", "Sucesso");
            JSFUtil.fecharDialog("dialogExclusao");
            listarUnidade();
        } else {
            JSFUtil.adicionarMensagemErro("Ocorreu um erro durante a exclusão!", "Erro");
            JSFUtil.fecharDialog("dialogExclusao");
        }
    }

    public void listarUnidade() throws ProjetoException {
        listaUnidades = eDao.listarUnidade();
    }

    public List<UnidadeBean> listarTodasAsUnidades() throws ProjetoException {
        if (listaUnidades.isEmpty()) {
    	listaUnidades =  eDao.listarUnidade();
        }
        return listaUnidades;
    }

    public List<UnidadeBean> listarUnidadesDoFuncionario() throws ProjetoException {
        return eDao.carregarUnidadesDoFuncionario();
    }

    public List<GrupoBean> listaGrupoAutoCompleteComProgramaOPM(String query)
            throws ProjetoException {
        List<GrupoBean> listaGrupo = new ArrayList<>();

        if(verificarProgramaPreenchido(unidade.getParametro().getOrteseProtese().getPrograma().getIdPrograma())) {
            listaGrupo = gDao.listarGruposNoAutoComplete(query, unidade.getParametro().getOrteseProtese().getPrograma().getIdPrograma());
        }

        return listaGrupo;
    }
    
    public List<GrupoBean> listaGrupoAutoCompleteComProgramaEvolucao(String query)
            throws ProjetoException {
        List<GrupoBean> listaGrupo = new ArrayList<>();

        if(verificarProgramaPreenchido(evolucaoAux.getPrograma().getIdPrograma())) {
            listaGrupo = gDao.listarGruposNoAutoComplete(query, evolucaoAux.getPrograma().getIdPrograma());
        }

        return listaGrupo;
    }    

    public void listaGrupoPorProgramaOPM(Integer codPrograma)
            throws ProjetoException {

        if(verificarProgramaPreenchido(codPrograma)) {
            listaGruposProgramasOpm = gDao.listarGruposPorPrograma(codPrograma);
        }

    }
    
    public void listaGrupoPorProgramaEvolucao(Integer codPrograma)
            throws ProjetoException {

        if(verificarProgramaPreenchido(codPrograma)) {
            listaGruposProgramasEvolucao = gDao.listarGruposPorPrograma(codPrograma);
        }

    }    

    private Boolean verificarProgramaPreenchido(Integer idPrograma){
        Boolean retorno = false;

        if(!VerificadorUtil.verificarSeObjetoNulo(idPrograma)) {
            retorno = true;
        }

        return retorno;
    }

    public String getCabecalho() {
        if (this.tipo.equals(TipoCabecalho.INCLUSAO.getSigla())) {
            cabecalho = CABECALHO_INCLUSAO;
        } else if (this.tipo.equals(TipoCabecalho.ALTERACAO.getSigla())) {
            cabecalho = CABECALHO_ALTERACAO;
        }
        return cabecalho;
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

    public ArrayList<String> getListaEstados() {
        return listaEstados;
    }

    public void setListaEstados(ArrayList<String> listaEstados) {
        this.listaEstados = listaEstados;
    }

  

   

	public UnidadeBean getUnidade() {
		return unidade;
	}

	public UnidadeDAO geteDao() {
		return eDao;
	}

	public GrupoDAO getgDao() {
		return gDao;
	}

	public List<UnidadeBean> getListaUnidades() {
		return listaUnidades;
	}

	public void setUnidade(UnidadeBean unidade) {
		this.unidade = unidade;
	}

	public void setTipo(Integer tipo) {
		this.tipo = tipo;
	}

	public void seteDao(UnidadeDAO eDao) {
		this.eDao = eDao;
	}

	public void setgDao(GrupoDAO gDao) {
		this.gDao = gDao;
	}

	public void setListaUnidades(List<UnidadeBean> listaUnidades) {
		this.listaUnidades = listaUnidades;
	}

	public ProgramaGrupoEvolucaoBean getEvolucaoAux() {
		return evolucaoAux;
	}

	public void setEvolucaoAux(ProgramaGrupoEvolucaoBean evolucaoAux) {
		this.evolucaoAux = evolucaoAux;
	}

	public List<GrupoBean> getListaGruposProgramasOpm() {
		return listaGruposProgramasOpm;
	}

	public List<GrupoBean> getListaGruposProgramasEvolucao() {
		return listaGruposProgramasEvolucao;
	}

	public void setListaGruposProgramasOpm(List<GrupoBean> listaGruposProgramasOpm) {
		this.listaGruposProgramasOpm = listaGruposProgramasOpm;
	}

	public void setListaGruposProgramasEvolucao(List<GrupoBean> listaGruposProgramasEvolucao) {
		this.listaGruposProgramasEvolucao = listaGruposProgramasEvolucao;
	}


}
