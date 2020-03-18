package br.gov.al.maceio.sishosp.hosp.control;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import br.gov.al.maceio.sishosp.acl.dao.FuncionarioDAO;
import br.gov.al.maceio.sishosp.administrativo.model.RemocaoProfissionalEquipe;
import br.gov.al.maceio.sishosp.comum.enums.TipoCabecalho;
import br.gov.al.maceio.sishosp.comum.util.ConverterUtil;
import br.gov.al.maceio.sishosp.comum.util.JSFUtil;
import br.gov.al.maceio.sishosp.comum.util.RedirecionarUtil;
import br.gov.al.maceio.sishosp.comum.util.VerificadorUtil;
import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.hosp.dao.EquipeDAO;
import br.gov.al.maceio.sishosp.hosp.model.EquipeBean;
import br.gov.al.maceio.sishosp.hosp.model.dto.SubstituicaoProfissionalEquipeDTO;

@ManagedBean(name = "EquipeController")
@ViewScoped
public class EquipeController implements Serializable {

    private static final long serialVersionUID = 1L;
    private EquipeBean equipe;
    private List<EquipeBean> listaEquipe;
    private Integer tipo;
    private String cabecalho;
    private EquipeDAO eDao = new EquipeDAO();
    private Long codigoProfissional;
    private List<RemocaoProfissionalEquipe> listaRemocoes;
    private SubstituicaoProfissionalEquipeDTO substituicaoProfissionalEquipeDTO;
    private RemocaoProfissionalEquipe remocaoProfissionalEquipe;

    //CONSTANTES
    private static final String ENDERECO_CADASTRO = "cadastroEquipe?faces-redirect=true";
    private static final String ENDERECO_SUBSTITUIR = "substituicaofuncionarioequipe?faces-redirect=true";
    private static final String ENDERECO_TIPO = "&amp;tipo=";
    private static final String ENDERECO_ID = "&amp;id=";
    private static final String CABECALHO_INCLUSAO = "Inclusão de Equipe";
    private static final String CABECALHO_ALTERACAO = "Alteração de Equipe";

    public EquipeController() {
        this.equipe = new EquipeBean();
        this.listaEquipe = null;
        listaRemocoes = new ArrayList<>();
        substituicaoProfissionalEquipeDTO = new SubstituicaoProfissionalEquipeDTO();
        remocaoProfissionalEquipe = new RemocaoProfissionalEquipe();
    }

    public void limparDados() throws ProjetoException {
        equipe = new EquipeBean();
        this.listaEquipe = eDao.listarEquipe();
    }

    public String redirectEdit() {
        return RedirecionarUtil.redirectEdit(ENDERECO_CADASTRO, ENDERECO_ID, this.equipe.getCodEquipe(), ENDERECO_TIPO, tipo);
    }

    public String redirectInsert() {
        return RedirecionarUtil.redirectInsert(ENDERECO_CADASTRO, ENDERECO_TIPO, tipo);
    }

    public String redirectSubstituir() {
        return RedirecionarUtil.redirectEditSemTipo(ENDERECO_SUBSTITUIR, ENDERECO_ID, remocaoProfissionalEquipe.getId());
    }

    public void getEditEquipe() throws ProjetoException {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Map<String, String> params = facesContext.getExternalContext()
                .getRequestParameterMap();
        if (params.get("id") != null) {
            Integer id = Integer.parseInt(params.get("id"));
            tipo = Integer.parseInt(params.get("tipo"));
            EquipeDAO cDao = new EquipeDAO();
            this.equipe = cDao.buscarEquipePorID(id);

        } else {
            tipo = Integer.parseInt(params.get("tipo"));

        }
    }

    public void carregarSubstituicaoProfissionalAfastadoEquipe() throws ProjetoException {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Map<String, String> params = facesContext.getExternalContext()
                .getRequestParameterMap();
        if (params.get("id") != null) {
            Integer id = Integer.parseInt(params.get("id"));
            this.setSubstituicaoProfissionalEquipeDTO(eDao.listarRemocaoPorId(id));
        }
    }

    public void ListarTodasEquipes() throws ProjetoException {
        this.listaEquipe = eDao.listarEquipe();
    }


    public void gravarEquipe() throws ProjetoException {
        if (this.equipe.getProfissionais().isEmpty()) {
            JSFUtil.adicionarMensagemAdvertencia("É necessário ao menos um profissional na equipe!", "Advertência");
        } else {
            boolean cadastrou = eDao.gravarEquipe(this.equipe);

            if (cadastrou == true) {
                limparDados();
                JSFUtil.adicionarMensagemSucesso("Equipe cadastrada com sucesso!", "Sucesso");
                JSFUtil.atualizarComponente("msgPagina");
            } else {
                JSFUtil.adicionarMensagemErro("Ocorreu um erro durante o cadastro!", "Erro");
            }
        }
    }

    public void alterarEquipe() {

        if (this.equipe.getProfissionais().isEmpty()) {
            JSFUtil.adicionarMensagemAdvertencia("É necessário ao menos um profissional na equipe!", "Advertência");
        } else {

            boolean alterou = eDao.alterarEquipe(equipe);

            if (alterou == true) {
                JSFUtil.adicionarMensagemSucesso("Equipe alterada com sucesso!", "Sucesso");
                JSFUtil.atualizarComponente("msgPagina");
            } else {
                JSFUtil.adicionarMensagemErro("Ocorreu um erro durante a alteração!", "Erro");
            }
        }
    }

    public void excluirEquipe() throws ProjetoException {

        boolean excluiu = eDao.excluirEquipe(equipe);

        if (excluiu == true) {
            JSFUtil.adicionarMensagemSucesso("Equipe excluída com sucesso!", "Sucesso");
            JSFUtil.fecharDialog("dialogExclusao");
        } else {
            JSFUtil.adicionarMensagemErro("Ocorreu um erro durante a alteração!", "Erro");
            JSFUtil.fecharDialog("dialogExclusao");
        }
        ListarTodasEquipes();
    }

    public void removerProfissionalAlteracaoEquipe() throws ProjetoException {

        boolean removeu = eDao.removerProfissionalEquipe(equipe.getCodEquipe(), codigoProfissional, equipe.getDataExclusao());

        if (removeu == true) {
            JSFUtil.adicionarMensagemSucesso("Profissional removido com sucesso!", "Sucesso");
            JSFUtil.fecharDialog("dlgExcluirProfissional");
            JSFUtil.atualizarComponente("msgPagina");
            this.equipe = eDao.buscarEquipePorID(equipe.getCodEquipe());
        } else {
            JSFUtil.adicionarMensagemErro("Ocorreu um erro durante a ação!", "Erro");
        }

    }

    public List<EquipeBean> listaEquipeAutoComplete(String query)
            throws ProjetoException {
        List<EquipeBean> result = eDao.listarEquipeBusca(query);
        return result;
    }

    public void listarProfissionaisDaEquipe() throws ProjetoException {
        this.equipe = eDao.buscarEquipePorID(equipe.getCodEquipe());
    }

    public void listarRemocoes() {
        listaRemocoes = eDao.listarProfissionaisDaEquipeRemovidos(equipe.getCodEquipe());
    }

    public void gravarSubstituicaoProfissionalEquipe(){
        try {
			dataDeSubstituicaoEhAnteriorAhDataDeSaida();
	        boolean cadastrou = eDao.gravarSubstituicaoProfissionalEquipe(substituicaoProfissionalEquipeDTO);
			if (cadastrou == true) {
				limparDados();
				JSFUtil.adicionarMensagemSucesso("Substituição realizada com sucesso!", "Sucesso");
			} else {
				JSFUtil.adicionarMensagemErro("Ocorreu um erro durante a substituição!", "Erro");
			}
		} catch (ProjetoException e) {
			e.printStackTrace();
		} 
    }
	
	private void dataDeSubstituicaoEhAnteriorAhDataDeSaida() throws ProjetoException{
		if(substituicaoProfissionalEquipeDTO.getDataDeSubstituicao().before(
				substituicaoProfissionalEquipeDTO.getRemocaoProfissionalEquipe().getDataSaida())) {
        	throw new ProjetoException("A data de substituição não pode ser anterior a data de saída");
        }
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

    public void setListaEquipe(List<EquipeBean> listaEquipe) {
        this.listaEquipe = listaEquipe;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public List<EquipeBean> getListaEquipe() {
        return listaEquipe;
    }

    public EquipeBean getEquipe() {
        return equipe;
    }

    public void setEquipe(EquipeBean equipe) {
        this.equipe = equipe;
    }

    public Long getCodigoProfissional() {
        return codigoProfissional;
    }

    public void setCodigoProfissional(Long codigoProfissional) {
        this.codigoProfissional = codigoProfissional;
    }

    public List<RemocaoProfissionalEquipe> getListaRemocoes() {
        return listaRemocoes;
    }

    public void setListaRemocoes(List<RemocaoProfissionalEquipe> listaRemocoes) {
        this.listaRemocoes = listaRemocoes;
    }

    public SubstituicaoProfissionalEquipeDTO getSubstituicaoProfissionalEquipeDTO() {
        return substituicaoProfissionalEquipeDTO;
    }

    public void setSubstituicaoProfissionalEquipeDTO(SubstituicaoProfissionalEquipeDTO substituicaoProfissionalEquipeDTO) {
        this.substituicaoProfissionalEquipeDTO = substituicaoProfissionalEquipeDTO;
    }

    public RemocaoProfissionalEquipe getRemocaoProfissionalEquipe() {
        return remocaoProfissionalEquipe;
    }

    public void setRemocaoProfissionalEquipe(RemocaoProfissionalEquipe remocaoProfissionalEquipe) {
        this.remocaoProfissionalEquipe = remocaoProfissionalEquipe;
    }
}
