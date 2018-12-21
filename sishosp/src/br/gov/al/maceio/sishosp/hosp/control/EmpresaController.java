package br.gov.al.maceio.sishosp.hosp.control;

import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.JSFUtil;
import br.gov.al.maceio.sishosp.comum.util.RedirecionarUtil;
import br.gov.al.maceio.sishosp.hosp.dao.EmpresaDAO;
import br.gov.al.maceio.sishosp.hosp.model.EmpresaBean;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

@ManagedBean(name = "EmpresaController")
@ViewScoped
public class EmpresaController implements Serializable {

	private static final long serialVersionUID = 1L;
	private EmpresaBean empresa;
	private int tipo;
	private String cabecalho;
	private EmpresaDAO eDao = new EmpresaDAO();

	//CONSTANTES
	private static final String ENDERECO_CADASTRO = "cadastroEmpresa?faces-redirect=true";
	private static final String ENDERECO_TIPO = "&amp;tipo=";
	private static final String ENDERECO_ID = "&amp;id=";
	private static final String CABECALHO_INCLUSAO = "Inclusão de Empresa";
    private static final String CABECALHO_ALTERACAO = "Alteração de Empresa";

	public EmpresaController() {
		this.empresa = new EmpresaBean();
		this.cabecalho = "";
	}

	public void limparDados() {
		this.empresa = new EmpresaBean();
	}

	public String redirectEdit() {
		return RedirecionarUtil.redirectEdit(ENDERECO_CADASTRO, ENDERECO_ID, this.empresa.getCodEmpresa(), ENDERECO_TIPO, tipo);
	}

	public String redirectInsert() {
	    return RedirecionarUtil.redirectInsert(ENDERECO_CADASTRO, ENDERECO_TIPO, tipo);
	}

	public void getEditEmpresa() throws ProjetoException {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		Map<String, String> params = facesContext.getExternalContext()
				.getRequestParameterMap();
		if (params.get("id") != null) {
			Integer id = Integer.parseInt(params.get("id"));
			tipo = Integer.parseInt(params.get("tipo"));

			this.empresa = eDao.buscarEmpresaPorId(id);
		} else {

			tipo = Integer.parseInt(params.get("tipo"));

		}

	}

	public void gravarEmpresa() {

		boolean cadastrou = eDao.gravarEmpresa(empresa);

		if (cadastrou == true) {
			limparDados();
			JSFUtil.adicionarMensagemSucesso("Empresa cadastrada com sucesso!", "Sucesso");
		} else {
			JSFUtil.adicionarMensagemErro("Ocorreu um erro durante o cadastro!", "Erro");
		}
	}

	public void alterarEmpresa() {
		boolean alterou = eDao.alterarEmpresa(empresa);

		if (alterou == true) {
            JSFUtil.adicionarMensagemSucesso("Empresa alterada com sucesso!", "Sucesso");
		} else {
            JSFUtil.adicionarMensagemErro("Ocorreu um erro durante a alteração!", "Erro");
		}
	}

	public void desativarEmpresa() {
		boolean desativou = eDao.desativarEmpresa(empresa);

		if (desativou == true) {
            JSFUtil.adicionarMensagemSucesso("Empresa desativada com sucesso!", "Sucesso");
			JSFUtil.fecharDialog("dialogExclusao");
		} else {
            JSFUtil.adicionarMensagemErro("Ocorreu um erro durante a exclusão!", "Erro");
            JSFUtil.fecharDialog("dialogExclusao");
		}
	}

	public List<EmpresaBean> listarEmpresa() throws ProjetoException {
		return eDao.listarEmpresa();
    }

	public String getCabecalho() {
		if (this.tipo == 1) {
			cabecalho = CABECALHO_INCLUSAO;
		} else if (this.tipo == 2) {
			cabecalho = CABECALHO_ALTERACAO;
		}
		return cabecalho;
	}

	public void setCabecalho(String cabecalho) {
		this.cabecalho = cabecalho;
	}

	public EmpresaBean getEmpresa() {
		return empresa;
	}

	public void setEmpresa(EmpresaBean empresa) {
		this.empresa = empresa;
	}

	public int getTipo() {
		return tipo;
	}

	public void setTipo(int tipo) {
		this.tipo = tipo;
	}

}
