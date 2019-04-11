package br.gov.al.maceio.sishosp.acl.control;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.PrimeFaces;

import br.gov.al.maceio.sishosp.acl.dao.SistemaDAO;
import br.gov.al.maceio.sishosp.acl.model.Sistema;
import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;

@ManagedBean
@ViewScoped
public class SistemaMB implements Serializable {

	private Sistema sistema;
	private List<Sistema> listaSistemas;

	private String extensaoPag = ".faces";
	private List<String> listaExtensoesPag;

	private String extensaoImg = ".png";
	private List<String> listaExtensoesImg;

	private String valorBusca;

	public SistemaMB() {
		sistema = new Sistema();
		listaSistemas = new ArrayList<>();
		listaSistemas = null;

		listaExtensoesPag = new ArrayList<>();
		listaExtensoesPag.add(".faces");
		listaExtensoesPag.add(".jsf");
		listaExtensoesPag.add(".xhtml");

		listaExtensoesImg = new ArrayList<>();
		listaExtensoesImg.add(".gif");
		listaExtensoesImg.add(".jpg");
		listaExtensoesImg.add(".png");

		valorBusca = "";
	}

	public void cadastrarSistema() throws ProjetoException {

		String dirPag = "/pages/" + sistema.getSigla().toLowerCase() + "/";
		String dirImg = "../../imgs/";

		String aux = dirPag + sistema.getUrl() + extensaoPag;
		sistema.setUrl(aux);

		aux = dirImg + sistema.getImagem() + extensaoImg;
		sistema.setImagem(aux);

		SistemaDAO sdao = new SistemaDAO();
		boolean cadastrou = sdao.cadastrarSistema(sistema);

		if (cadastrou == true) {

			listaSistemas = null;

			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Sistema cadastrado com sucesso!", "Sucesso");
			FacesContext.getCurrentInstance().addMessage(null, msg);

			PrimeFaces.current().executeScript(
					"PF('dlgCadSistema').hide();");
		} else {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Ocorreu um erro durante o cadastro!", "Erro");
			FacesContext.getCurrentInstance().addMessage(null, msg);

			PrimeFaces.current().executeScript(
					"PF('dlgCadSistema').hide();");
		}
	}

	public void alterarSistema() throws ProjetoException {

		String dirImg = "../../imgs/";
		String aux = dirImg + sistema.getImagem() + extensaoImg;
		sistema.setImagem(aux);

		SistemaDAO sdao = new SistemaDAO();
		boolean alterou = sdao.alterarSistema(sistema);

		if (alterou == true) {

			listaSistemas = null;

			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Sistema alterado com sucesso!", "Sucesso");
			FacesContext.getCurrentInstance().addMessage(null, msg);

			PrimeFaces.current().executeScript(
					"PF('dlgAltSistema').hide();");
		} else {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Ocorreu um erro durante a alteração!", "Erro");
			FacesContext.getCurrentInstance().addMessage(null, msg);

			PrimeFaces.current().executeScript(
					"PF('dlgAltSistema').hide();");
		}
	}

	public void excluirSistema() throws ProjetoException {

		SistemaDAO sdao = new SistemaDAO();
		boolean excluiu = sdao.excluirSistema(sistema);

		if (excluiu == true) {

			listaSistemas = null;

			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Sistema excluído com sucesso!", "Sucesso");
			FacesContext.getCurrentInstance().addMessage(null, msg);

			PrimeFaces.current().executeScript(
					"PF('dlgExcSistema').hide();");
		} else {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Ocorreu um erro durante a exclusão!", "Erro");
			FacesContext.getCurrentInstance().addMessage(null, msg);

			PrimeFaces.current().executeScript(
					"PF('dlgExcSistema').hide();");
		}
	}

	public void buscarSistema() throws ProjetoException {
		SistemaDAO sdao = new SistemaDAO();
		List<Sistema> listaAux = sdao.buscarSistemaDesc(valorBusca);

		if (listaAux != null && listaAux.size() > 0) {
			listaSistemas = null;
			listaSistemas = listaAux;
		} else {
			listaSistemas = null;

			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN,
					"Sistema não encontrado!", "Aviso");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
	}

	public String verificarBolTab(boolean ativo) {
		if (ativo == true) {
			return "../../imgs/status_green.png";
		} else {
			return "../../imgs/status_red.png";
		}
	}

	public void limparDados() {
		sistema = new Sistema();
		extensaoPag = ".faces";
		extensaoImg = ".png";
	}

	public void limparBusca() {
		valorBusca = "";
		listaSistemas = null;
	}

	public Sistema getSistema() {
		return sistema;
	}

	public void setSistema(Sistema sistema) {
		this.sistema = sistema;
	}

	public List<Sistema> getListaSistemas() throws ProjetoException {
		if (listaSistemas == null) {
			SistemaDAO sdao = new SistemaDAO();
			listaSistemas = sdao.listarSistemas();
		}
		return listaSistemas;
	}

	public void setListaSistemas(List<Sistema> listaSistemas) {
		this.listaSistemas = listaSistemas;
	}

	public String getExtensaoPag() {
		return extensaoPag;
	}

	public void setExtensaoPag(String extensaoPag) {
		this.extensaoPag = extensaoPag;
	}

	public List<String> getListaExtensoesPag() {
		return listaExtensoesPag;
	}

	public void setListaExtensoesPag(List<String> listaExtensoesPag) {
		this.listaExtensoesPag = listaExtensoesPag;
	}

	public String getExtensaoImg() {
		return extensaoImg;
	}

	public void setExtensaoImg(String extensaoImg) {
		this.extensaoImg = extensaoImg;
	}

	public List<String> getListaExtensoesImg() {
		return listaExtensoesImg;
	}

	public void setListaExtensoesImg(List<String> listaExtensoesImg) {
		this.listaExtensoesImg = listaExtensoesImg;
	}

	public String getValorBusca() {
		return valorBusca;
	}

	public void setValorBusca(String valorBusca) {
		this.valorBusca = valorBusca;
	}
}