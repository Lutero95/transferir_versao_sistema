package br.gov.al.maceio.sishosp.acl.control;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;
import org.primefaces.event.TransferEvent;
import org.primefaces.model.DualListModel;

import br.gov.al.maceio.sishosp.acl.dao.FuncionalidadeDAO;
import br.gov.al.maceio.sishosp.acl.dao.MenuDAO;
import br.gov.al.maceio.sishosp.acl.dao.SistemaDAO;
import br.gov.al.maceio.sishosp.acl.model.Funcionalidade;
import br.gov.al.maceio.sishosp.acl.model.Sistema;
import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;

@ManagedBean
@ViewScoped
public class FuncionalidadeMB implements Serializable {

	private Sistema sistema;
	private Funcionalidade funcionalidade;
	private List<Funcionalidade> listaFuncionalidadesGeral;
	private List<String> listaExtensoesPag;
	private List<Sistema> listaDiretorios;

	private Integer abaAtiva;
	private boolean statusFuncionalidade;

	private DualListModel<Sistema> listaSistemasDual;
	private List<Sistema> listaSistemasSoucer;
	private List<Sistema> listaSistemasTarget;

	private DualListModel<Sistema> listaSistemasDualAlt;
	private List<Sistema> listaSistemasSoucerAlt;
	private List<Sistema> listaSistemasTargetAlt;

	private String idMenuAlt = "0";
	private String indiceAux = "null";
	private String tipoMenuRel = "A";

	private String valorBusca;

	public FuncionalidadeMB() {
		funcionalidade = new Funcionalidade();
		sistema = new Sistema();

		funcionalidade.setExtensao(".faces");

		listaFuncionalidadesGeral = new ArrayList<>();
		listaFuncionalidadesGeral = null;

		listaExtensoesPag = new ArrayList<>();
		listaExtensoesPag.add(".faces");
		listaExtensoesPag.add(".jsf");
		listaExtensoesPag.add(".xhtml");

		listaDiretorios = new ArrayList<>();
		listaDiretorios = null;

		listaSistemasDual = null;
		listaSistemasSoucer = new ArrayList<>();
		listaSistemasTarget = new ArrayList<>();

		listaSistemasDualAlt = null;
		listaSistemasSoucerAlt = new ArrayList<>();
		listaSistemasTargetAlt = new ArrayList<>();

		valorBusca = "";
	}

	public void cadastrarFuncionalidade() throws ProjetoException {

		if (listaSistemasDual.getTarget().size() > 0) {
			List<Integer> listaSis = new ArrayList<>();

			for (Sistema s : listaSistemasDual.getTarget()) {
				listaSis.add(s.getId());
			}

			// funcionalidade.setDescPagina(null);
			// funcionalidade.setDiretorio(null);
			// funcionalidade.setExtensao(null);
			// sistema.setImagem(null);
			funcionalidade.setAtivo(statusFuncionalidade);
			funcionalidade.setListaSistemas(listaSis);

			FuncionalidadeDAO mdao = new FuncionalidadeDAO();
			boolean cadastrou = mdao.cadastrar(funcionalidade);

			if (cadastrou == true) {

				listaFuncionalidadesGeral = null;

				FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
						"Funcionalidade cadastrada com sucesso!", "Sucesso");
				FacesContext.getCurrentInstance().addMessage(null, msg);

				RequestContext.getCurrentInstance().execute(
						"PF('dlgCadMenu').hide();");
			} else {
				FacesMessage msg = new FacesMessage(
						FacesMessage.SEVERITY_ERROR,
						"Ocorreu um erro durante o cadastro!", "Erro");
				FacesContext.getCurrentInstance().addMessage(null, msg);

				RequestContext.getCurrentInstance().execute(
						"PF('dlgCadMenu').hide();");
			}
		} else {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Selecione pelo menos um sistema", "Erro");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
	}

	public void alterarFuncionalidade() throws ProjetoException {

		/*
		 * if(listaSistemasDualAlt.getTarget().size() > 0) { List<Integer>
		 * listaSis = new ArrayList<>();
		 * 
		 * for(Sistema s : listaSistemasDualAlt.getTarget()) {
		 * listaSis.add(s.getId());
		 * 
		 * } else { FacesMessage msg = new
		 * FacesMessage(FacesMessage.SEVERITY_ERROR,
		 * "Selecione pelo menos um sistema", "Erro");
		 * FacesContext.getCurrentInstance().addMessage(null, msg); } }
		 */

		funcionalidade.setAtivo(statusFuncionalidade);
		// funcionalidade.setListaSistemas(listaSis);
		FuncionalidadeDAO mdao = new FuncionalidadeDAO();
		boolean alterou = mdao.alterar(funcionalidade);

		if (alterou == true) {

			listaFuncionalidadesGeral = null;

			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Funcionalidade alterada com sucesso!", "Sucesso");
			FacesContext.getCurrentInstance().addMessage(null, msg);

			RequestContext.getCurrentInstance().execute(
					"PF('dlgAltMenu').hide();");
		} else {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Ocorreu um erro durante o cadastro!", "Erro");
			FacesContext.getCurrentInstance().addMessage(null, msg);

			RequestContext.getCurrentInstance().execute(
					"PF('dlgAltMenu').hide();");
		}

	}

	public void excluirFuncionalidade() throws ProjetoException {

		FuncionalidadeDAO mdao = new FuncionalidadeDAO();
		boolean excluiu = mdao.excluirFuncionalidade(funcionalidade);

		if (excluiu == true) {

			listaFuncionalidadesGeral = null;

			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Funcionalidade excluída com sucesso!", "Sucesso");
			FacesContext.getCurrentInstance().addMessage(null, msg);

			RequestContext.getCurrentInstance().execute(
					"PF('dlgExcMenu').hide();");
		} else {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Ocorreu um erro durante a exclusão!", "Sucesso");
			FacesContext.getCurrentInstance().addMessage(null, msg);

			RequestContext.getCurrentInstance().execute(
					"PF('dlgExcMenu').hide();");
		}
	}

	public void buscarFuncionalidade() throws ProjetoException {
		FuncionalidadeDAO mdao = new FuncionalidadeDAO();
		List<Funcionalidade> listaAux = mdao
				.buscarFuncionalidadeDesc(valorBusca);

		if (listaAux != null && listaAux.size() > 0) {
			listaFuncionalidadesGeral = null;
			listaFuncionalidadesGeral = listaAux;
		} else {
			listaFuncionalidadesGeral = null;

			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN,
					"Funcionalidade não encontrada!", "Aviso");
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

	public void onTransferMenuSis(TransferEvent event) {
		StringBuilder builder = new StringBuilder();

		for (Object item : event.getItems()) {
			builder.append(((Sistema) item).getId());
			if (listaSistemasTarget.contains(item)) {
				listaSistemasTarget.remove(item);
			} else {
				listaSistemasTarget.add((Sistema) item);
			}
		}
	}

	public void limparDados() {
		abaAtiva = 0;
		funcionalidade.setExtensao(".faces");
		listaDiretorios = null;
		statusFuncionalidade = true;
	}

	public void limparBusca() {
		valorBusca = "";
		listaFuncionalidadesGeral = null;
	}

	public void limparDualMenuSis() {
		listaSistemasDual = null;

		listaSistemasTarget = null;
		listaSistemasTarget = new ArrayList<>();
	}

	public void limparDualMenuSisAlt() {
		listaSistemasDualAlt = null;

		listaSistemasTargetAlt = null;
		listaSistemasTargetAlt = new ArrayList<>();

	}

	public List<String> getListaExtensoesPag() {
		return listaExtensoesPag;
	}

	public void setListaExtensoesPag(List<String> listaExtensoesPag) {
		this.listaExtensoesPag = listaExtensoesPag;
	}

	public List<Sistema> getListaDiretorios() throws ProjetoException {
		if (listaDiretorios == null) {
			SistemaDAO sdao = new SistemaDAO();
			listaDiretorios = sdao.listarSiglas();
		}
		return listaDiretorios;
	}

	public void setListaDiretorios(List<Sistema> listaDiretorios) {
		this.listaDiretorios = listaDiretorios;
	}

	public DualListModel<Sistema> getListaSistemasDual()
			throws ProjetoException {
		if (listaSistemasDual == null) {
			listaSistemasSoucer = null;
			listaSistemasTarget = new ArrayList<>();
			getListaSistemasSoucer();
			listaSistemasDual = new DualListModel<>(listaSistemasSoucer,
					listaSistemasTarget);
		}
		return listaSistemasDual;
	}

	public void setListaSistemasDual(DualListModel<Sistema> listaSistemasDual) {
		this.listaSistemasDual = listaSistemasDual;
	}

	public List<Sistema> getListaSistemasSoucer() throws ProjetoException {
		if (listaSistemasSoucer == null) {
			SistemaDAO sdao = new SistemaDAO();
			listaSistemasSoucer = sdao.listarSistemasSource();
		}
		return listaSistemasSoucer;
	}

	public void setListaSistemasSoucer(List<Sistema> listaSistemasSoucer) {
		this.listaSistemasSoucer = listaSistemasSoucer;
	}

	public List<Sistema> getListaSistemasTarget() {
		return listaSistemasTarget;
	}

	public void setListaSistemasTarget(List<Sistema> listaSistemasTarget) {
		this.listaSistemasTarget = listaSistemasTarget;
	}

	public Integer getAbaAtiva() {
		return abaAtiva;
	}

	public void setAbaAtiva(Integer abaAtiva) {
		this.abaAtiva = abaAtiva;
	}

	public DualListModel<Sistema> getListaSistemasDualAlt()
			throws NumberFormatException, ProjetoException {
		if (listaSistemasDualAlt == null) {
			listaSistemasSoucerAlt = null;
			listaSistemasTargetAlt = null;
			getListaSistemasSoucerAlt();
			getListaSistemasTargetAlt();
			listaSistemasDualAlt = new DualListModel<>(listaSistemasSoucerAlt,
					listaSistemasTargetAlt);
		}
		return listaSistemasDualAlt;
	}

	public void setListaSistemasDualAlt(
			DualListModel<Sistema> listaSistemasDualAlt) {
		this.listaSistemasDualAlt = listaSistemasDualAlt;
	}

	public List<Sistema> getListaSistemasSoucerAlt()
			throws NumberFormatException, ProjetoException {
		if (listaSistemasSoucerAlt == null) {
			MenuDAO mdao = new MenuDAO();
			listaSistemasSoucerAlt = mdao.listarSisAssNaoMenuSource(Long
					.valueOf(idMenuAlt));
		}
		return listaSistemasSoucerAlt;
	}

	public void setListaSistemasSoucerAlt(List<Sistema> listaSistemasSoucerAlt) {
		this.listaSistemasSoucerAlt = listaSistemasSoucerAlt;
	}

	public List<Sistema> getListaSistemasTargetAlt()
			throws NumberFormatException, ProjetoException {

		if (listaSistemasTargetAlt == null) {
			MenuDAO mdao = new MenuDAO();
			listaSistemasTargetAlt = mdao.listarSisAssMenuTarget(Long
					.valueOf(idMenuAlt));
		}
		return listaSistemasTargetAlt;
	}

	public void setListaSistemasTargetAlt(List<Sistema> listaSistemasTargetAlt) {
		this.listaSistemasTargetAlt = listaSistemasTargetAlt;
	}

	public String getIdMenuAlt() {
		return idMenuAlt;
	}

	public void setIdMenuAlt(String idMenuAlt) {
		this.idMenuAlt = idMenuAlt;
	}

	public String getIndiceAux() {
		return indiceAux;
	}

	public void setIndiceAux(String indiceAux) {
		this.indiceAux = indiceAux;
	}

	public boolean isStatusFuncionalidade() {
		return statusFuncionalidade;
	}

	public void setStatusFuncionalidade(boolean statusFuncionalidade) {
		this.statusFuncionalidade = statusFuncionalidade;
	}

	public String getTipoMenuRel() {
		return tipoMenuRel;
	}

	public void setTipoMenuRel(String tipoMenuRel) {
		this.tipoMenuRel = tipoMenuRel;
	}

	public String getValorBusca() {
		return valorBusca;
	}

	public void setValorBusca(String valorBusca) {
		this.valorBusca = valorBusca;
	}

	public Funcionalidade getFuncionalidade() {
		return funcionalidade;
	}

	public void setFuncionalidade(Funcionalidade funcionalidade) {
		this.funcionalidade = funcionalidade;
	}

	public Sistema getSistema() {
		return sistema;
	}

	public void setSistema(Sistema sistema) {
		this.sistema = sistema;
	}

	public List<Funcionalidade> getListaFuncionalidadesGeral()
			throws ProjetoException {
		if (listaFuncionalidadesGeral == null) {
			FuncionalidadeDAO mdao = new FuncionalidadeDAO();
			listaFuncionalidadesGeral = mdao.listarFuncionalidadesGeral();
		}
		return listaFuncionalidadesGeral;
	}

	public void setListaFuncionalidadesGeral(
			List<Funcionalidade> listaFuncionalidadesGeral) {
		this.listaFuncionalidadesGeral = listaFuncionalidadesGeral;
	}

}