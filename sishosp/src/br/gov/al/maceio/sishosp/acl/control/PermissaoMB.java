package br.gov.al.maceio.sishosp.acl.control;

import br.gov.al.maceio.sishosp.acl.model.Permissao;
import br.gov.al.maceio.sishosp.acl.dao.PermissaoDAO;
import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;

@ManagedBean
@ViewScoped
public class PermissaoMB implements Serializable {

	private Permissao permissao;
	private List<Permissao> listaPermissoes;

	private String valorBusca;

	public PermissaoMB() {
		permissao = new Permissao();
		listaPermissoes = new ArrayList<>();
		listaPermissoes = null;

		valorBusca = "";
	}

	public void cadastrarPermissao() {

		// PermissaoDAO pdao = new PermissaoDAO();
		// boolean cadastrou = pdao.cadastrarPermissao(permissao);

		boolean cadastrou = false;

		if (cadastrou == true) {

			listaPermissoes = null;

			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Permissão cadastrada com sucesso!", "Sucesso");
			FacesContext.getCurrentInstance().addMessage(null, msg);

			RequestContext.getCurrentInstance().execute(
					"PF('dlgCadPermissao').hide();");
		} else {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Ocorreu um erro durante o cadastro!", "Erro");
			FacesContext.getCurrentInstance().addMessage(null, msg);

			RequestContext.getCurrentInstance().execute(
					"PF('dlgCadPermissao').hide();");
		}
	}

	public void alterarPermissao() {

		// PermissaoDAO pdao = new PermissaoDAO();
		// boolean alterou = pdao.alterarPermissao(editPermissao);

		boolean alterou = false;

		if (alterou == true) {

			listaPermissoes = null;

			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Permissão alterada com sucesso!", "Sucesso");
			FacesContext.getCurrentInstance().addMessage(null, msg);

			RequestContext.getCurrentInstance().execute(
					"PF('dlgAltPermissao').hide();");
		} else {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Ocorreu um erro durante a alteração!", "Erro");
			FacesContext.getCurrentInstance().addMessage(null, msg);

			RequestContext.getCurrentInstance().execute(
					"PF('dlgAltPermissao').hide();");
		}
	}

	public void excluirPermissao() {

		// PermissaoDAO pdao = new PermissaoDAO();
		// boolean excluiu = pdao.excluirPermissao(excluirPermissao);

		boolean excluiu = false;

		if (excluiu == true) {

			listaPermissoes = null;

			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Permissão excluída com sucesso!", "Sucesso");
			FacesContext.getCurrentInstance().addMessage(null, msg);

			RequestContext.getCurrentInstance().execute(
					"PF('dlgExcPermissao').hide();");
		} else {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Ocorreu um erro durante a exclusão!", "Erro");
			FacesContext.getCurrentInstance().addMessage(null, msg);

			RequestContext.getCurrentInstance().execute(
					"PF('dlgExcPermissao').hide();");
		}
	}

	public void buscarPermissao() throws ProjetoException {
		PermissaoDAO pdao = new PermissaoDAO();
		List<Permissao> listaAux = pdao.buscarPermissaoDesc(valorBusca);

		if (listaAux != null && listaAux.size() > 0) {
			listaPermissoes = null;
			listaPermissoes = listaAux;
		} else {
			listaPermissoes = null;

			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN,
					"Permissão não encontrada!", "Aviso");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
	}

	public void limparDados() {
		permissao = new Permissao();
	}

	public void limparBusca() {
		valorBusca = "";
		listaPermissoes = null;
	}

	public Permissao getPermissao() {
		return permissao;
	}

	public void setPermissao(Permissao permissao) {
		this.permissao = permissao;
	}

	public List<Permissao> getListaPermissoes() throws ProjetoException {
		if (listaPermissoes == null) {
			PermissaoDAO pdao = new PermissaoDAO();
			listaPermissoes = pdao.listarPermissoes();
		}
		return listaPermissoes;
	}

	public void setListaPermissoes(List<Permissao> listaPermissoes) {
		this.listaPermissoes = listaPermissoes;
	}

	public String getValorBusca() {
		return valorBusca;
	}

	public void setValorBusca(String valorBusca) {
		this.valorBusca = valorBusca;
	}
}