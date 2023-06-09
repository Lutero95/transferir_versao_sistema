package br.gov.al.maceio.sishosp.administrativo.control;

import br.gov.al.maceio.sishosp.acl.dao.FuncionarioDAO;
import br.gov.al.maceio.sishosp.acl.model.FuncionarioBean;
import br.gov.al.maceio.sishosp.administrativo.dao.AfastamentoProfissionalDAO;
import br.gov.al.maceio.sishosp.administrativo.model.AfastamentoProfissional;
import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.JSFUtil;
import br.gov.al.maceio.sishosp.comum.util.RedirecionarUtil;
import br.gov.al.maceio.sishosp.comum.util.VerificadorUtil;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@ManagedBean
@ViewScoped
public class AfastamentoProfissionalController implements Serializable {

    private static final long serialVersionUID = 1L;
    private AfastamentoProfissional afastamentoProfissional;
    private AfastamentoProfissionalDAO aDao = new AfastamentoProfissionalDAO();
    private List<AfastamentoProfissional> listaAfastamentosProfissionais;
    private boolean tiposDeAfastamentoPossuemSituacaoAtendimento;
    private FuncionarioDAO fDao = new FuncionarioDAO();
	private List<FuncionarioBean> listaProfissional;
    private FuncionarioBean user_session;

    //CONSTANTES
    private static final String ENDERECO_CADASTRO = "cadastroafastamentoprofissional?faces-redirect=true";


    public AfastamentoProfissionalController() {
        this.afastamentoProfissional = new AfastamentoProfissional();
        listaAfastamentosProfissionais = new ArrayList<>();
        this.user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
                .getSessionMap().get("obj_funcionario");
    }
    
    public void limpaSelecaoTurnoAfastamento() {
    	afastamentoProfissional.setTurno(null);
    }

    public String redirectInsert() {
        return RedirecionarUtil.redirectInsertSemTipo(ENDERECO_CADASTRO);
    }

    public void limparDados() {
        afastamentoProfissional = new AfastamentoProfissional();

    }

    public void gravarAfastamentoProfissional() throws ProjetoException {
    	if (!aDao.verificaSeExisteAfastamentoProfissionalNoPeriodo(afastamentoProfissional)) {
        boolean cadastrou = aDao.gravarAfastamentoProfissional(this.afastamentoProfissional);

        if (cadastrou == true) {
            limparDados();
            JSFUtil.adicionarMensagemSucesso("Afastamento do Profissional cadastrado com sucesso!", "Sucesso");
        } else {
            JSFUtil.adicionarMensagemErro("Ocorreu um erro durante o cadastro", "Erro");
        }
    	}
    	else
    	{
    		JSFUtil.adicionarMensagemSucesso("Já existe Afastamento para o funcionário no período informado!", "Sucesso");
    		
    	}
    }

    public void excluirAfastamentoProfissional() throws ProjetoException {
        boolean excluiu = aDao.excluirAfastamentoProfissional(afastamentoProfissional.getId(), this.user_session.getId());
        if (excluiu == true) {
            JSFUtil.adicionarMensagemSucesso("Afastamento do Profissional excluído com sucesso!", "Sucesso");
            JSFUtil.fecharDialog("dlgExclusao");
            listarAfastamentosProfissionais();
        } else {
            JSFUtil.adicionarMensagemErro("Ocorreu um erro durante a exclusão", "Erro");
            JSFUtil.fecharDialog("dlgExclusao");
        }
    }

    public void validarDatasDeAfastamentoProfissional() {
        if (!VerificadorUtil.verificarSeObjetoNulo(afastamentoProfissional.getPeriodoInicio())
                && !VerificadorUtil.verificarSeObjetoNulo(afastamentoProfissional.getPeriodoFinal())) {
            if (afastamentoProfissional.getPeriodoFinal().before(afastamentoProfissional.getPeriodoInicio()) ) {
                limparDatas();
                JSFUtil.adicionarMensagemErro("A data de início do afastamento não pode ser maior que a data fim do afastamento!", "Erro");
            }
        }
    }

    public void limparDatas(){
        afastamentoProfissional.setPeriodoInicio(null);
        afastamentoProfissional.setPeriodoFinal(null);
    }

    public void listarAfastamentosProfissionais() throws ProjetoException {
        listaAfastamentosProfissionais = aDao.listarAfastamentoProfissionais();
    }
      
    public void listarAfastamentosProfissionalFiltro(int id) throws ProjetoException {
    	if(!VerificadorUtil.verificarSeObjetoNulo(id)){    		
    		listaAfastamentosProfissionais = aDao.listarAfastamentoFiltroProfissional(id);
    	}
    }

    public void afastamentoPossuemSituacaoAtendimento() throws ProjetoException {
    	this.tiposDeAfastamentoPossuemSituacaoAtendimento = aDao.tiposDeAfastamentoPossuemSituacaoAtendimento();
    	if(!this.tiposDeAfastamentoPossuemSituacaoAtendimento) {
    		JSFUtil.adicionarMensagemAdvertencia
    			("Não há Situação de Atendimento Atribuída aos Tipos de Afastamento do Profissional no Cadastro de Empresa", "");
    	}
    }
    
    public List<FuncionarioBean> listaProfissionalAtivoAutoComplete(String query) throws ProjetoException {
		List<FuncionarioBean> result = fDao.listarProfissionalAtivoBusca(query, 1);
		return result;
	}
    
    public void listarProfissionaisAtivos() throws ProjetoException {
		setListaProfissional(fDao.listarTodosOsProfissionaisAtivos());
	}

	public AfastamentoProfissional getAfastamentoProfissional() {
		return afastamentoProfissional;
	}

	public void setAfastamentoProfissional(AfastamentoProfissional afastamentoProfissional) {
		this.afastamentoProfissional = afastamentoProfissional;
	}

	public List<AfastamentoProfissional> getListaAfastamentosProfissionais() {
		return listaAfastamentosProfissionais;
	}

	public void setListaAfastamentosProfissionais(List<AfastamentoProfissional> listaAfastamentosProfissionais) {
		this.listaAfastamentosProfissionais = listaAfastamentosProfissionais;
	}

	public boolean isTiposDeAfastamentoPossuemSituacaoAtendimento() {
		return tiposDeAfastamentoPossuemSituacaoAtendimento;
	}

	public FuncionarioDAO getfDao() {
		return fDao;
	}

	public void setfDao(FuncionarioDAO fDao) {
		this.fDao = fDao;
	}

	public List<FuncionarioBean> getListaProfissional() {
		return listaProfissional;
	}

	public void setListaProfissional(List<FuncionarioBean> listaProfissional) {
		this.listaProfissional = listaProfissional;
	}
	
}
