package br.gov.al.maceio.sishosp.acl.control;

import br.gov.al.maceio.sishosp.acl.model.Funcao;
import br.gov.al.maceio.sishosp.acl.model.Funcionalidade;
import br.gov.al.maceio.sishosp.acl.model.Menu;
import br.gov.al.maceio.sishosp.acl.model.Perfil;
import br.gov.al.maceio.sishosp.acl.model.Sistema;
import br.gov.al.maceio.sishosp.acl.dao.FuncaoDAO;
import br.gov.al.maceio.sishosp.acl.dao.FuncionalidadeDAO;
import br.gov.al.maceio.sishosp.acl.dao.MenuDAO;
import br.gov.al.maceio.sishosp.acl.dao.PerfilDAO;
import br.gov.al.maceio.sishosp.acl.dao.PermissaoDAO;
import br.gov.al.maceio.sishosp.acl.dao.SistemaDAO;
import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;

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
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.MenuModel;

@ManagedBean
@ViewScoped
public class PerfilMB implements Serializable {

	private Perfil perfil;
	private List<Perfil> listaPerfil;

	private DualListModel<Menu> listaMenusDual;
	private List<Menu> listaMenusSource;
	private List<Menu> listaMenusTarget;

	private DualListModel<Funcao> listaFuncoesDual;
	private List<Funcao> listaFuncoesSource;
	private List<Funcao> listaFuncoesTarget;

	private DualListModel<Menu> listaMenusDualEdit;
	private List<Menu> listaMenusSourceEdit;
	private List<Menu> listaMenusTargetEdit;

	private DualListModel<Funcao> listaFuncoesDualEdit;
	private List<Funcao> listaFuncoesSourceEdit;
	private List<Funcao> listaFuncoesTargetEdit;

	private DualListModel<Funcionalidade> listaFuncionalidadesDualEdit;
	private List<Funcionalidade> listaFuncionalidadesSourceEdit;
	private List<Funcionalidade> listaFuncionalidadesTargetEdit;

	private String perfilSelecionado = "1";

	// Menu Preview --------------------------------------- //
	private List<Sistema> listaSistemasPreMenu;
	private String sisSelecionadoPreMenu;
	private MenuModel menuModelPreview;
	private List<Menu> listaPreMenusAux;
	private Sistema sisPreMenu;
	// ---------------------------------------------------- //

	private String descPerfilBusca = "";

	public PerfilMB() {
		perfil = new Perfil();
		listaPerfil = new ArrayList<>();
		listaPerfil = null;

		listaMenusDual = null;
		listaMenusSource = new ArrayList<>();
		listaMenusTarget = new ArrayList<>();

		listaFuncoesDual = null;
		listaFuncoesSource = new ArrayList<>();
		listaFuncoesTarget = new ArrayList<>();

		listaMenusDualEdit = new DualListModel<Menu>();
		listaMenusSourceEdit = new ArrayList<>();
		listaMenusTargetEdit = new ArrayList<>();

		listaFuncoesDualEdit = new DualListModel<Funcao>();
		listaFuncoesSourceEdit = new ArrayList<>();
		listaFuncoesTargetEdit = new ArrayList<>();

		listaFuncionalidadesDualEdit = new DualListModel<Funcionalidade>();
		listaFuncionalidadesSourceEdit = new ArrayList<>();
		listaFuncionalidadesTargetEdit = new ArrayList<>();

		listaSistemasPreMenu = new ArrayList<>();
		menuModelPreview = new DefaultMenuModel();
		listaPreMenusAux = new ArrayList<>();
		sisPreMenu = new Sistema();
	}

	public void cadastrarPerfil() throws ProjetoException {

		List<Long> permissoes = new ArrayList<>();
		List<Menu> listaMenusAux = listaMenusDual.getTarget();
		List<Funcao> listaFuncoesAux = listaFuncoesDual.getTarget();

		if ((listaMenusAux != null && listaMenusAux.size() > 0)
				|| (listaFuncoesAux != null && listaFuncoesAux.size() > 0)) {

			MenuMB mmb = new MenuMB();
			List<Menu> listaFiltrada = mmb.filtrarListaMenu(listaMenusAux);

			PermissaoDAO pmdao = new PermissaoDAO();
			for (Menu m : listaFiltrada) {
				permissoes.add(pmdao.recIdPermissoesMenu(m.getId()));
			}
			for (Funcao f : listaFuncoesAux) {
				permissoes.add(pmdao.recIdPermissoesFuncao(f.getId()));
			}
			perfil.setListaPermissoes(permissoes);

			PerfilDAO pdao = new PerfilDAO();
			boolean cadastrou = pdao.cadastrar(perfil);

			if (cadastrou == true) {

				listaPerfil = null;

				FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
						"Perfil cadastrado com sucesso!", "Sucesso");
				FacesContext.getCurrentInstance().addMessage(null, msg);

				RequestContext.getCurrentInstance().execute(
						"PF('dlgCadPerfil').hide();");
			} else {
				FacesMessage msg = new FacesMessage(
						FacesMessage.SEVERITY_ERROR,
						"Ocorreu um erro durante o cadastro!", "Erro");
				FacesContext.getCurrentInstance().addMessage(null, msg);

				RequestContext.getCurrentInstance().execute(
						"PF('dlgCadPerfil').hide();");
			}
		} else {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN,
					"Associe pelo menos um item ao perfil!", "Aviso");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
	}

	public void alterarPerfil() throws ProjetoException {

		List<Long> permissoes = new ArrayList<>();
		List<Menu> listaMenusAux = listaMenusDualEdit.getTarget();
		List<Funcao> listaFuncoesAux = listaFuncoesDualEdit.getTarget();

		if ((listaMenusAux != null && listaMenusAux.size() > 0)
				|| (listaFuncoesAux != null && listaFuncoesAux.size() > 0)) {

			MenuMB mmb = new MenuMB();
			List<Menu> listaFiltrada = mmb.filtrarListaMenu(listaMenusAux);

			PermissaoDAO pmdao = new PermissaoDAO();
			for (Menu m : listaFiltrada) {
				permissoes.add(pmdao.recIdPermissoesMenu(m.getId()));
			}
			for (Funcao f : listaFuncoesAux) {
				permissoes.add(pmdao.recIdPermissoesFuncao(f.getId()));
			}

			perfil.setListaPermissoes(permissoes);

			PerfilDAO pdao = new PerfilDAO();
			boolean alterou = pdao.alterar(perfil);

			if (alterou == true) {

				listaPerfil = null;

				FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
						"Perfil alterado com sucesso!", "Sucesso");
				FacesContext.getCurrentInstance().addMessage(null, msg);

				RequestContext.getCurrentInstance().execute(
						"PF('dlgAltPerfil').hide();");
			} else {
				FacesMessage msg = new FacesMessage(
						FacesMessage.SEVERITY_ERROR,
						"Ocorreu um erro durante a alteração!", "Erro");
				FacesContext.getCurrentInstance().addMessage(null, msg);

				RequestContext.getCurrentInstance().execute(
						"PF('dlgAltPerfil').hide();");
			}
		} else {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN,
					"Associe pelo menos um item ao perfil!", "Aviso");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
	}

	public void excluirPerfil() throws ProjetoException {

		PerfilDAO pdao = new PerfilDAO();
		boolean excluiu = pdao.excluirPerfil(perfil);

		if (excluiu == true) {

			listaPerfil = null;

			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Perfil excluído com sucesso!", "Sucesso");
			FacesContext.getCurrentInstance().addMessage(null, msg);

			RequestContext.getCurrentInstance().execute(
					"PF('dlgExcPerfil').hide();");
		} else {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Ocorreu um erro durante a exclusão!", "Erro");
			FacesContext.getCurrentInstance().addMessage(null, msg);

			RequestContext.getCurrentInstance().execute(
					"PF('dlgExcPerfil').hide();");
		}
	}

	public void buscarPerfil() throws ProjetoException {
		PerfilDAO pdao = new PerfilDAO();
		List<Perfil> listaAux = pdao.buscarPerfisDesc(descPerfilBusca);

		if (listaAux != null && listaAux.size() > 0) {
			listaPerfil = null;
			listaPerfil = listaAux;
		} else {
			listaPerfil = null;

			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN,
					"Perfil não encontrado!", "Aviso");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
	}

	public void gerarPreMenuPerfil(String tipo) throws ProjetoException {

		MenuMB mmb = new MenuMB();
		List<Menu> listaVerificada = mmb.filtrarPreMenu(tipo,
				listaMenusDual.getTarget(), listaMenusDualEdit.getTarget());

		menuModelPreview = mmb.gerarMenusPreview(listaVerificada,
				Integer.parseInt(sisSelecionadoPreMenu));

		SistemaDAO sdao = new SistemaDAO();
		sisPreMenu = sdao.buscarSisMenuPreview(Integer
				.parseInt(sisSelecionadoPreMenu));

		RequestContext.getCurrentInstance().execute("dlgMenuPreview.show();");
	}

	public void onTransferMenu(TransferEvent event) {
		StringBuilder builder = new StringBuilder();

		for (Object item : event.getItems()) {
			builder.append(((Menu) item).getId());
			if (listaMenusTarget.contains(item)) {
				listaMenusTarget.remove(item);
			} else {
				listaMenusTarget.add((Menu) item);
			}
		}
	}

	public void onTransferFuncao(TransferEvent event) {
		StringBuilder builder = new StringBuilder();

		for (Object item : event.getItems()) {
			builder.append(((Funcao) item).getId());
			if (listaFuncoesTarget.contains(item)) {
				listaFuncoesTarget.remove(item);
			} else {
				listaFuncoesTarget.add((Funcao) item);
			}
		}
	}

	public void onTransferMenuAlt(TransferEvent event) {
		StringBuilder builder = new StringBuilder();

		for (Object item : event.getItems()) {
			builder.append(((Menu) item).getId());
			if (listaMenusTargetEdit.contains(item)) {
				listaMenusTargetEdit.remove(item);
			} else {
				listaMenusTargetEdit.add((Menu) item);
			}
		}
	}

	public void onTransferFuncionalidadeAlt(TransferEvent event) {
		StringBuilder builder = new StringBuilder();

		for (Object item : event.getItems()) {
			builder.append(((Menu) item).getId());
			if (listaFuncionalidadesTargetEdit.contains(item)) {
				listaFuncionalidadesTargetEdit.remove(item);
			} else {
				listaFuncionalidadesTargetEdit.add((Funcionalidade) item);
			}
		}
	}

	public void onTransferFuncaoAlt(TransferEvent event) {
		StringBuilder builder = new StringBuilder();

		for (Object item : event.getItems()) {
			builder.append(((Funcao) item).getId());
			if (listaFuncoesTargetEdit.contains(item)) {
				listaFuncoesTargetEdit.remove(item);
			} else {
				listaFuncoesTargetEdit.add((Funcao) item);
			}
		}
	}

	public void limparDados() {
		perfil = new Perfil();
		listaSistemasPreMenu = null;
		sisSelecionadoPreMenu = "0";
		sisPreMenu = new Sistema();
		menuModelPreview = new DefaultMenuModel();
		descPerfilBusca = "";
	}

	public void limparDualCad() {
		listaMenusDual = null;
		listaMenusTarget = null;
		listaMenusTarget = new ArrayList<>();

		listaFuncoesDual = null;
		listaFuncoesTarget = null;
		listaFuncoesTarget = new ArrayList<>();

		listaSistemasPreMenu = null;
	}

	public void limparDualEdit() {

		listaMenusSourceEdit = new ArrayList<>();
		listaMenusTargetEdit = new ArrayList<>();
		listaMenusDualEdit = null;

		listaFuncoesSourceEdit = new ArrayList<>();
		listaFuncoesTargetEdit = new ArrayList<>();
		listaFuncoesDualEdit = null;

		listaFuncionalidadesSourceEdit = new ArrayList<>();
		listaFuncionalidadesTargetEdit = new ArrayList<>();
		listaFuncionalidadesDualEdit = null;

		listaSistemasPreMenu = null;
	}

	public void limparBusca() {
		descPerfilBusca = "";
		listaPerfil = null;
	}

	public Perfil getPerfil() {
		return perfil;
	}

	public void setPerfil(Perfil perfil) {
		this.perfil = perfil;
	}

	public List<Perfil> getListaPerfil() throws ProjetoException {
		if (listaPerfil == null) {
			PerfilDAO pdao = new PerfilDAO();
			listaPerfil = pdao.listarPerfil();
		}
		return listaPerfil;
	}

	public void setListaPerfil(List<Perfil> listaPerfil) {
		this.listaPerfil = listaPerfil;
	}

	public DualListModel<Menu> getListaMenusDual() throws ProjetoException {
		if (listaMenusDual == null) {
			listaMenusSource = null;
			listaMenusTarget = new ArrayList<>();
			getListaMenusSource();
			listaMenusDual = new DualListModel<>(listaMenusSource,
					listaMenusTarget);
		}
		return listaMenusDual;
	}

	public void setListaMenusDual(DualListModel<Menu> listaMenusDual) {
		this.listaMenusDual = listaMenusDual;
	}

	public List<Menu> getListaMenusSource() throws ProjetoException {
		if (listaMenusSource == null) {
			MenuDAO mdao = new MenuDAO();
			listaMenusSource = mdao.listarMenuItemComSis();
		}
		return listaMenusSource;
	}

	public void setListaMenusSource(List<Menu> listaMenusSource) {
		this.listaMenusSource = listaMenusSource;
	}

	public List<Menu> getListaMenusTarget() {
		return listaMenusTarget;
	}

	public void setListaMenusTarget(List<Menu> listaMenusTarget) {
		this.listaMenusTarget = listaMenusTarget;
	}

	public DualListModel<Funcao> getListaFuncoesDual() throws ProjetoException {
		if (listaFuncoesDual == null) {
			listaFuncoesSource = null;
			listaFuncoesTarget = new ArrayList<>();
			getListaFuncoesSource();
			listaFuncoesDual = new DualListModel<>(listaFuncoesSource,
					listaFuncoesTarget);
		}
		return listaFuncoesDual;
	}

	public void setListaFuncoesDual(DualListModel<Funcao> listaFuncoesDual) {
		this.listaFuncoesDual = listaFuncoesDual;
	}

	public List<Funcao> getListaFuncoesSource() throws ProjetoException {
		if (listaFuncoesSource == null) {
			FuncaoDAO fdao = new FuncaoDAO();
			listaFuncoesSource = fdao.listarFuncoesComSisRot();
		}
		return listaFuncoesSource;
	}

	public void setListaFuncoesSource(List<Funcao> listaFuncoesSource) {
		this.listaFuncoesSource = listaFuncoesSource;
	}

	public List<Funcao> getListaFuncoesTarget() {
		return listaFuncoesTarget;
	}

	public void setListaFuncoesTarget(List<Funcao> listaFuncoesTarget) {
		this.listaFuncoesTarget = listaFuncoesTarget;
	}

	public DualListModel<Menu> getListaMenusDualEdit()
			throws NumberFormatException, ProjetoException {
		if (listaMenusDualEdit == null) {
			listaMenusSourceEdit = null;
			listaMenusTargetEdit = null;
			getListaMenusSourceEdit();
			getListaMenusTargetEdit();
			listaMenusDualEdit = new DualListModel<>(listaMenusSourceEdit,
					listaMenusTargetEdit);
		}
		return listaMenusDualEdit;
	}

	public void setListaMenusDualEdit(DualListModel<Menu> listaMenusDualEdit) {
		this.listaMenusDualEdit = listaMenusDualEdit;
	}

	public List<Menu> getListaMenusSourceEdit() throws NumberFormatException,
			ProjetoException {
		if (listaMenusSourceEdit == null) {
			MenuDAO mdao = new MenuDAO();
			listaMenusSourceEdit = mdao.listarMenuItemSourcerEdit(Integer
					.parseInt(perfilSelecionado));
		}
		return listaMenusSourceEdit;
	}

	public void setListaMenusSourceEdit(List<Menu> listaMenusSourceEdit) {
		this.listaMenusSourceEdit = listaMenusSourceEdit;
	}

	public List<Menu> getListaMenusTargetEdit() throws NumberFormatException,
			ProjetoException {
		if (listaMenusTargetEdit == null) {
			MenuDAO mdao = new MenuDAO();
			listaMenusTargetEdit = mdao.listarMenuItemTargetEdit(Integer
					.parseInt(perfilSelecionado));
		}
		return listaMenusTargetEdit;
	}

	public void setListaMenusTargetEdit(List<Menu> listaMenusTargetEdit) {
		this.listaMenusTargetEdit = listaMenusTargetEdit;
	}

	public DualListModel<Funcao> getListaFuncoesDualEdit()
			throws NumberFormatException, ProjetoException {
		if (listaFuncoesDualEdit == null) {
			listaFuncoesSourceEdit = null;
			listaFuncoesTargetEdit = null;
			getListaFuncoesSourceEdit();
			getListaFuncoesTargetEdit();
			listaFuncoesDualEdit = new DualListModel<>(listaFuncoesSourceEdit,
					listaFuncoesTargetEdit);
		}
		return listaFuncoesDualEdit;
	}

	public void setListaFuncoesDualEdit(
			DualListModel<Funcao> listaFuncoesDualEdit) {
		this.listaFuncoesDualEdit = listaFuncoesDualEdit;
	}

	public List<Funcao> getListaFuncoesSourceEdit()
			throws NumberFormatException, ProjetoException {
		if (listaFuncoesSourceEdit == null) {
			FuncaoDAO fdao = new FuncaoDAO();
			listaFuncoesSourceEdit = fdao.listarFuncoesSourceEdit(Integer
					.parseInt(perfilSelecionado));
		}
		return listaFuncoesSourceEdit;
	}

	public void setListaFuncoesSourceEdit(List<Funcao> listaFuncoesSourceEdit) {
		this.listaFuncoesSourceEdit = listaFuncoesSourceEdit;
	}

	public List<Funcao> getListaFuncoesTargetEdit()
			throws NumberFormatException, ProjetoException {
		if (listaFuncoesTargetEdit == null) {
			FuncaoDAO fdao = new FuncaoDAO();
			listaFuncoesTargetEdit = fdao.listarFuncoesTargetEdit(Integer
					.parseInt(perfilSelecionado));
		}
		return listaFuncoesTargetEdit;
	}

	public void setListaFuncoesTargetEdit(List<Funcao> listaFuncoesTargetEdit) {
		this.listaFuncoesTargetEdit = listaFuncoesTargetEdit;
	}

	public String getPerfilSelecionado() {
		return perfilSelecionado;
	}

	public void setPerfilSelecionado(String perfilSelecionado) {
		this.perfilSelecionado = perfilSelecionado;
	}

	public List<Sistema> getListaSistemasPreMenu() throws ProjetoException {
		if (listaSistemasPreMenu == null) {
			SistemaDAO sdao = new SistemaDAO();
			listaSistemasPreMenu = sdao.listarSistemas();
		}
		return listaSistemasPreMenu;
	}

	public void setListaSistemasPreMenu(List<Sistema> listaSistemasPreMenu) {
		this.listaSistemasPreMenu = listaSistemasPreMenu;
	}

	public String getSisSelecionadoPreMenu() {
		return sisSelecionadoPreMenu;
	}

	public void setSisSelecionadoPreMenu(String sisSelecionadoPreMenu) {
		this.sisSelecionadoPreMenu = sisSelecionadoPreMenu;
	}

	public MenuModel getMenuModelPreview() {
		return menuModelPreview;
	}

	public void setMenuModelPreview(MenuModel menuModelPreview) {
		this.menuModelPreview = menuModelPreview;
	}

	public List<Menu> getListaPreMenusAux() {
		return listaPreMenusAux;
	}

	public void setListaPreMenusAux(List<Menu> listaPreMenusAux) {
		this.listaPreMenusAux = listaPreMenusAux;
	}

	public Sistema getSisPreMenu() {
		return sisPreMenu;
	}

	public void setSisPreMenu(Sistema sisPreMenu) {
		this.sisPreMenu = sisPreMenu;
	}

	public String getDescPerfilBusca() {
		return descPerfilBusca;
	}

	public void setDescPerfilBusca(String descPerfilBusca) {
		this.descPerfilBusca = descPerfilBusca;
	}

	public DualListModel<Funcionalidade> getListaFuncionalidadesDualEdit()
			throws NumberFormatException, ProjetoException {
		if (listaFuncionalidadesDualEdit == null) {
			listaFuncionalidadesSourceEdit = null;
			listaFuncionalidadesTargetEdit = null;
			getListaFuncionalidadesSourceEdit();
			getListaFuncionalidadesTargetEdit();
			listaFuncionalidadesDualEdit = new DualListModel<>(
					listaFuncionalidadesSourceEdit,
					listaFuncionalidadesTargetEdit);
		}
		return listaFuncionalidadesDualEdit;
	}

	public void setListaFuncionalidadesDualEdit(
			DualListModel<Funcionalidade> listaFuncionalidadesDualEdit) {
		this.listaFuncionalidadesDualEdit = listaFuncionalidadesDualEdit;
	}

	public List<Funcionalidade> getListaFuncionalidadesSourceEdit()
			throws NumberFormatException, ProjetoException {
		if (listaFuncionalidadesSourceEdit == null) {
			FuncionalidadeDAO mdao = new FuncionalidadeDAO();
			listaFuncionalidadesSourceEdit = mdao
					.listarFuncionalidadeItemSourcerEdit(Integer
							.parseInt(perfilSelecionado));
		}

		return listaFuncionalidadesSourceEdit;
	}

	public void setListaFuncionalidadesSourceEdit(
			List<Funcionalidade> listaFuncionalidadesSourceEdit) {
		this.listaFuncionalidadesSourceEdit = listaFuncionalidadesSourceEdit;
	}

	public List<Funcionalidade> getListaFuncionalidadesTargetEdit()
			throws NumberFormatException, ProjetoException {
		if (listaFuncionalidadesTargetEdit == null) {
			FuncionalidadeDAO mdao = new FuncionalidadeDAO();
			listaFuncionalidadesTargetEdit = mdao
					.listarFuncionalidadeItemTargetEdit(Integer
							.parseInt(perfilSelecionado));
		}
		return listaFuncionalidadesTargetEdit;
	}

	public void setListaFuncionalidadesTargetEdit(
			List<Funcionalidade> listaFuncionalidadesTargetEdit) {
		this.listaFuncionalidadesTargetEdit = listaFuncionalidadesTargetEdit;
	}

}