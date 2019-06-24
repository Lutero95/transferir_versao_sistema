package br.gov.al.maceio.sishosp.financeiro.control;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.event.SelectEvent;

import br.gov.al.maceio.sishosp.acl.model.FuncionarioBean;
import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.financeiro.dao.BuscaDAO;
import br.gov.al.maceio.sishosp.financeiro.model.ClienteBean;
import br.gov.al.maceio.sishosp.financeiro.model.FornecedorBean;
import br.gov.al.maceio.sishosp.financeiro.model.PortadorBean;


@ManagedBean
@ViewScoped
public class BuscaController implements Serializable {

	// CLIENTE INICIO
	
	private static final long serialVersionUID = 1L;

	public List<ClienteBean> completeBuscaCliente(String nome)
			throws ProjetoException {
		List<ClienteBean> result = new ArrayList<ClienteBean>();
		BuscaDAO bdao = new BuscaDAO();
		result = bdao.buscarClienteAC(nome);
		return result;
	}
	
	public List<ClienteBean> completeBuscaClienteComInativos(String nome)
			throws ProjetoException {
		List<ClienteBean> result = new ArrayList<ClienteBean>();
		BuscaDAO bdao = new BuscaDAO();
		result = bdao.buscarClienteACComInativos(nome);
		return result;
	}
	

	public void onItemSelectCliente(SelectEvent event) throws Exception {
		ClienteBean cliCom = new ClienteBean();
		cliCom = (ClienteBean) event.getObject();
	}

	// CLIENTE FIM

	// FUNCIONARIOS INICIO





	





	public void onItemSelectFuncionario(SelectEvent event) throws Exception {
		FuncionarioBean funCom = new FuncionarioBean();
		funCom = (FuncionarioBean) event.getObject();
	}
	
	public void onItemSelectFuncionarioCortesia(SelectEvent event) throws Exception {
		Integer funCom = new Integer(0);
		funCom = (Integer) event.getObject();
	}

	// FUNCIONARIO FIM

	// FORNECEDOR INICIO
	public List<FornecedorBean> completeBuscaFornecedor(String nome)
			throws ProjetoException {
		List<FornecedorBean> result = new ArrayList<FornecedorBean>();
		BuscaDAO bdao = new BuscaDAO();
		result = bdao.buscarFornecedorAC(nome);
		return result;
	}

	public void onItemSelectFornecedor(SelectEvent event) throws Exception {
		FornecedorBean forCom = new FornecedorBean();
		forCom = (FornecedorBean) event.getObject();
	}

	// FORNECEDOR FIM

	// PROCEDIMENTO INICIO

	
	// PROCEDIMENTO FIM

	// PORTADOR INICIO

	public List<PortadorBean> completeBuscaPortador(String descricao)
			throws ProjetoException {
		List<PortadorBean> result = new ArrayList<PortadorBean>();
		BuscaDAO bdao = new BuscaDAO();
		result = bdao.buscarPortadorAC(descricao);
		return result;
	}

	public void onItemSelectPortador(SelectEvent event) throws Exception {
		PortadorBean ptdCom = new PortadorBean();
		ptdCom = (PortadorBean) event.getObject();
	}

	// PORTADOR FIM

	// KIT INICIO

	

	// OUTROS FIM

}
