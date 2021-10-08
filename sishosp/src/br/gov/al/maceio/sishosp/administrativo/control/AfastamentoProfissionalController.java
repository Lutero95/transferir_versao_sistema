package br.gov.al.maceio.sishosp.administrativo.control;

import br.gov.al.maceio.sishosp.administrativo.dao.AfastamentoProfissionalDAO;
import br.gov.al.maceio.sishosp.administrativo.model.AfastamentoProfissional;
import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.JSFUtil;
import br.gov.al.maceio.sishosp.comum.util.RedirecionarUtil;
import br.gov.al.maceio.sishosp.comum.util.VerificadorUtil;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
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

    //CONSTANTES
    private static final String ENDERECO_CADASTRO = "cadastroafastamentoprofissional?faces-redirect=true";


    public AfastamentoProfissionalController() {
        this.afastamentoProfissional = new AfastamentoProfissional();
        listaAfastamentosProfissionais = new ArrayList<>();
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
        boolean excluiu = aDao.excluirAfastamentoProfissional(afastamentoProfissional.getId());
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
	
}
