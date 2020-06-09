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

import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.hosp.dao.MotivoDesligamentoDAO;
import br.gov.al.maceio.sishosp.hosp.model.MotivoDesligamentoBean;

@ManagedBean(name = "MotivoController")
@ViewScoped
public class MotivoDesligamentoController implements Serializable {

    private static final long serialVersionUID = 1L;
    private MotivoDesligamentoBean motivo;
    private Integer tipo;
    private String cabecalho;
    private ArrayList<MotivoDesligamentoBean> listaMotivos;
    private MotivoDesligamentoDAO pDao = new MotivoDesligamentoDAO();

    //CONSTANTES
    private static final String ENDERECO_CADASTRO = "cadastroMotivoDesligamento?faces-redirect=true";
    private static final String ENDERECO_TIPO = "&amp;tipo=";
    private static final String ENDERECO_ID = "&amp;id=";
    private static final String CABECALHO_INCLUSAO = "Inclusão de Motivo de Desligamento";
    private static final String CABECALHO_ALTERACAO = "Alteração de Motivo de Desligamento";

    public MotivoDesligamentoController() {
        motivo = new MotivoDesligamentoBean();
        this.cabecalho = "";
        listaMotivos = new ArrayList<>();
    }

    public String redirectEdit() {
        return RedirecionarUtil.redirectEdit(ENDERECO_CADASTRO, ENDERECO_ID, this.motivo.getId_motivo(), ENDERECO_TIPO, tipo);
    }

    public String redirectInsert() {
        return RedirecionarUtil.redirectInsert(ENDERECO_CADASTRO, ENDERECO_TIPO, tipo);
    }

    public void getEditMotivo() throws ProjetoException {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Map<String, String> params = facesContext.getExternalContext()
                .getRequestParameterMap();
        if (params.get("id") != null) {
            Integer id = Integer.parseInt(params.get("id"));
            tipo = Integer.parseInt(params.get("tipo"));
            this.motivo = pDao.buscaMotivoPorId(id);
        } else {
            tipo = Integer.parseInt(params.get("tipo"));

        }

    }

    public void limparDados() {
        motivo = new MotivoDesligamentoBean();
    }

    public ArrayList<MotivoDesligamentoBean> listarMotivos() throws ProjetoException {
        if (listaMotivos.size()==0)
        	listaMotivos = pDao.listarMotivos();
        return listaMotivos;
    }
    
    public void carregaMotivosDesligamento() throws ProjetoException {
        	listaMotivos = pDao.listarMotivos();
    }

    public void gravarMotivo() throws ProjetoException {
        boolean cadastrou = pDao.gravarMotivo(motivo);

        if (cadastrou == true) {
            limparDados();
            JSFUtil.adicionarMensagemSucesso("Motivo de Desligamento cadastrado com sucesso!", "Sucesso");
        } else {
            JSFUtil.adicionarMensagemErro("Ocorreu um erro durante o cadastro!", "Erro");
        }
    }

    public void alterarMotivo() throws ProjetoException {

        boolean alterou = pDao.alterarMotivo(motivo);
        if (alterou == true) {
            JSFUtil.adicionarMensagemSucesso("Motivo de Desligamento alterado com sucesso!", "Sucesso");
        } else {
            JSFUtil.adicionarMensagemErro("Ocorreu um erro durante a alteração!", "Erro");
        }
    }

    public void excluirMotivo() throws ProjetoException {

        boolean excluiu = pDao.excluirMotivo(motivo);
        if (excluiu == true) {
            JSFUtil.adicionarMensagemSucesso("Motivo de Desligamento excluído com sucesso!", "Sucesso");
            JSFUtil.fecharDialog("dialogExclusao");
            carregaMotivosDesligamento();
        } else {
            JSFUtil.adicionarMensagemErro("Ocorreu um erro durante a exclusão!", "Erro");
            JSFUtil.fecharDialog("dialogExclusao");
        }
        listarMotivos();
    }

    public MotivoDesligamentoBean getMotivo() {
        return motivo;
    }

    public void setMotivo(MotivoDesligamentoBean motivo) {
        this.motivo = motivo;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public void setCabecalho(String cabecalho) {
        this.cabecalho = cabecalho;
    }

    public String getCabecalho() {
        if (this.tipo.equals(TipoCabecalho.INCLUSAO.getSigla())) {
            cabecalho = CABECALHO_INCLUSAO;
        } else if (this.tipo.equals(TipoCabecalho.ALTERACAO.getSigla())) {
            cabecalho = CABECALHO_ALTERACAO;
        }
        return cabecalho;
    }

	public ArrayList<MotivoDesligamentoBean> getListaMotivos() {
		return listaMotivos;
	}

	public void setListaMotivos(ArrayList<MotivoDesligamentoBean> listaMotivos) {
		this.listaMotivos = listaMotivos;
	}


}
