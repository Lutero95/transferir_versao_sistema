package br.gov.al.maceio.sishosp.financeiro.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import br.gov.al.maceio.sishosp.acl.model.FuncionarioBean;
import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;
import br.gov.al.maceio.sishosp.financeiro.model.CentroCustoBean;
import br.gov.al.maceio.sishosp.financeiro.model.ClienteBean;
import br.gov.al.maceio.sishosp.financeiro.model.DespesaBean;
import br.gov.al.maceio.sishosp.financeiro.model.FornecedorBean;
import br.gov.al.maceio.sishosp.financeiro.model.PortadorBean;
import br.gov.al.maceio.sishosp.financeiro.model.TipoDocumentoBean;



public class BuscaDAO {
	
	/*
	public ClienteBean buscarClienteCod(Integer codCliente) throws ProjetoException {

		Connection con = null;
		String sql = "select codcliente, nome, codigo_alternativo from financeiro.cliente where codcliente = ?   and ((cliente.codfilial = ?) or (cliente.codfilial in (select codempresa_autorizada from financeiro.empresa_aut_clientes"
				+ " where codempresa_origem=?))) ";
		ResultSet set = null;
		ClienteBean clienteBean = new ClienteBean();
		try {
			UsuarioBean user_session = (UsuarioBean) FacesContext.getCurrentInstance().getExternalContext()
					.getSessionMap().get("obj_usuario");
			con = ConnectFactory.getConnection();
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, codCliente);
			ps.setInt(2, user_session.getEmpresa().getCodfilial());
			ps.setInt(3, user_session.getEmpresa().getCodfilial());
			set = ps.executeQuery();

			while (set.next()) {

				clienteBean.setCodcliente(set.getInt("codcliente"));
				clienteBean.setNome(set.getString("nome"));
				clienteBean.setCodigo_alternativo(set.getInt("codigo_alternativo"));
			}

			ps.close();
			set.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {

			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return clienteBean;
	}
	*/

	public FuncionarioBean buscarFuncionarioCod(Integer codfunc) throws ProjetoException {

		Connection con = null;
		// String sql =
		// "select * from financeiro.func where codfunc = ? and angariador = true;";
		String sql = "select codfunc, nome, medico from financeiro.func where codfunc = ?";
		ResultSet set = null;
		FuncionarioBean funcionarioBean = new FuncionarioBean();
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, codfunc);
			set = ps.executeQuery();

			while (set.next()) {

				funcionarioBean.setCodigo(set.getInt("codfunc"));
				funcionarioBean.setNome(set.getString("nome"));
			}

			ps.close();
			set.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {

			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return funcionarioBean;
	}

	public FornecedorBean buscarFornecedorCod(Integer codforn) throws ProjetoException {

		Connection con = null;
		// String sql =
		// "select * from financeiro.func where codfunc = ? and angariador = true;";
		String sql = "select codforn, nome from financeiro.fornecedor where codforn = ?";
		ResultSet set = null;
		FornecedorBean fornecedorBean = new FornecedorBean();
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, codforn);
			set = ps.executeQuery();

			while (set.next()) {

				fornecedorBean.setCodforn(set.getInt("codforn"));
				fornecedorBean.setNome(set.getString("nome"));
			}

			ps.close();
			set.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {

			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return fornecedorBean;
	}

	public PortadorBean buscarPortadorCod(Integer codportador) throws ProjetoException {

		Connection con = null;
		// String sql =
		// "select * from financeiro.func where codfunc = ? and angariador = true;";
		String sql = "select codportador, descricao from financeiro.portador where codportador = ?";
		ResultSet set = null;
		PortadorBean portadorBean = new PortadorBean();
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, codportador);
			set = ps.executeQuery();

			while (set.next()) {

				portadorBean.setCodportador(set.getInt("codportador"));
				portadorBean.setDescricao(set.getString("descricao"));
			}
			
			ps.close();
			set.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {

			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return portadorBean;
	}


	

	public List<PortadorBean> lstTodosPortadores() throws ProjetoException {

		Connection con = null;
		String sql = "select * from financeiro.portador";
		ResultSet set = null;

		ArrayList<PortadorBean> lst = new ArrayList<>();
		try {
			FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
					.getSessionMap().get("obj_usuario");
			con = ConnectionFactory.getConnection();
			PreparedStatement ps = con.prepareStatement(sql);
			set = ps.executeQuery();

			while (set.next()) {

				PortadorBean bean = new PortadorBean();

				bean.setCodportador(set.getInt("codportador"));
				bean.setDescricao(set.getString("descricao"));

				lst.add(bean);

			}

			ps.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {

			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return lst;

	}

	/*
	public List<ClienteBean> lstTodosClientes() throws ProjetoException {

		Connection con = null;
		String sql = "select * from financeiro.cliente    where ((cliente.codfilial = ?) or (cliente.codfilial in (select codempresa_autorizada from financeiro.empresa_aut_clientes"
				+ " where codempresa_origem=?))) ";
		ResultSet set = null;

		ArrayList<ClienteBean> lst = new ArrayList<>();
		try {
			UsuarioBean user_session = (UsuarioBean) FacesContext.getCurrentInstance().getExternalContext()
					.getSessionMap().get("obj_usuario");
			con = ConnectFactory.getConnection();
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, user_session.getEmpresa().getCodfilial());
			ps.setInt(2, user_session.getEmpresa().getCodfilial());
			set = ps.executeQuery();

			while (set.next()) {

				ClienteBean bean = new ClienteBean();

				bean.setCodcliente(set.getInt("codcliente"));
				bean.setNome(set.getString("nome"));

				lst.add(bean);

			}
			
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {

			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return lst;

	}
	*/
	
	public List<PortadorBean> lstPortador(String desc) throws ProjetoException {

		Connection con = null;
		ResultSet rs = null;

		String sql = " select * from financeiro.portador where descricao like ?  ";

		List<PortadorBean> lst = new ArrayList<PortadorBean>();

		try {
			FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
					.getSessionMap().get("obj_usuario");
			con = ConnectionFactory.getConnection();
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, "%" + desc.toUpperCase() + "%");
			rs = ps.executeQuery();

			while (rs.next()) {

				PortadorBean portBean = new PortadorBean();

				portBean.setCodportador(rs.getInt("codportador"));
				portBean.setDescricao(rs.getString("descricao"));

				lst.add(portBean);

			}

			rs.close();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {

			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return lst;

	}

	

	public List<FornecedorBean> buscaForn(String nome, String cnpj) throws ProjetoException {

		Connection con = null;
		ResultSet set = null;
		String sql = " SELECT codforn, nome, fantasia, cnpj FROM financeiro.fornecedor where (coalesce(nome,'') like ? or coalesce(fantasia,'') like ?) and coalesce(cnpj,'') like ? ";

		List<FornecedorBean> lst = new ArrayList<FornecedorBean>();

		try {
			con = ConnectionFactory.getConnection();
			FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
					.getSessionMap().get("obj_usuario");
			if (cnpj == null) {
				cnpj = "";
			}

			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, "%" + nome.toUpperCase() + "%");
			ps.setString(2, "%" + nome.toUpperCase() + "%");
			ps.setString(3, "%" + cnpj.toUpperCase() + "%");
			set = ps.executeQuery();

			while (set.next()) {

				FornecedorBean forn = new FornecedorBean();

				forn.setCodforn(set.getInt("codforn"));
				forn.setNome(set.getString("nome"));
				forn.setFantasia(set.getString("fantasia"));
				forn.setCnpj(set.getString("cnpj"));

				lst.add(forn);

			}

			ps.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return lst;

	}

	public List<FornecedorBean> buscaFornecedor() throws ProjetoException, SQLException {

		Connection con = null;
		ResultSet set = null;

		String sql = "SELECT * FROM financeiro.fornecedor ";

		List<FornecedorBean> lst = new ArrayList<FornecedorBean>();

		try {
			con = ConnectionFactory.getConnection();

			FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
					.getSessionMap().get("obj_usuario");

			PreparedStatement ps = con.prepareStatement(sql);



			set = ps.executeQuery();

			while (set.next()) {

				FornecedorBean forn = new FornecedorBean();

				forn.setCodforn(set.getInt("codforn"));
				forn.setNome(set.getString("nome"));
				forn.setFantasia(set.getString("fantasia"));
				forn.setCnpj(set.getString("cnpj"));

				lst.add(forn);

			}
			
			ps.close();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {

			con.close();
		}

		return lst;

	}

	public List<CentroCustoBean> buscaCentroCusto() throws ProjetoException, SQLException {

		Connection con = null;
		ResultSet set = null;

		String sql = "select * from financeiro.ccusto";

		List<CentroCustoBean> lst = new ArrayList<CentroCustoBean>();

		try {
			con = ConnectionFactory.getConnection();
			FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
					.getSessionMap().get("obj_usuario");

			PreparedStatement ps = con.prepareStatement(sql);



			set = ps.executeQuery();

			while (set.next()) {

				CentroCustoBean bean = new CentroCustoBean();

				bean.setIdccusto(set.getInt("idccusto"));
				bean.setDescricao(set.getString("descricao"));
				lst.add(bean);

			}

			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {

			con.close();
		}

		return lst;

	}

	public List<TipoDocumentoBean> buscaTipoDoc() throws ProjetoException, SQLException {

		Connection con = null;
		ResultSet set = null;

		String sql = "select codtipodocumento, descricao from financeiro.tipodocumento  order by descricao";

		List<TipoDocumentoBean> lst = new ArrayList<TipoDocumentoBean>();

		try {
			con = ConnectionFactory.getConnection();

			FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
					.getSessionMap().get("obj_usuario");

			PreparedStatement ps = con.prepareStatement(sql);


			set = ps.executeQuery();

			while (set.next()) {

				TipoDocumentoBean bean = new TipoDocumentoBean();

				bean.setCodtipodocumento(set.getInt("codtipodocumento"));
				bean.setDescricao(set.getString("descricao"));
				lst.add(bean);

			}
			
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {

			con.close();
		}

		return lst;

	}

	public List<DespesaBean> buscaDespesa() throws ProjetoException, SQLException {

		Connection con = null;
		ResultSet set = null;

		String sql = "select * from financeiro.despesa ;";

		List<DespesaBean> lst = new ArrayList<DespesaBean>();

		try {
			con = ConnectionFactory.getConnection();

			FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
					.getSessionMap().get("obj_usuario");

			PreparedStatement ps = con.prepareStatement(sql);


			set = ps.executeQuery();

			while (set.next()) {

				DespesaBean bean = new DespesaBean();

				bean.setId(set.getInt("iddespesa"));
				bean.setDescricao(set.getString("descricao"));

				lst.add(bean);

			}

			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {

			con.close();
		}

		return lst;

	}

	
	// INICIO BUSCAS AUTOCOMPLETE
	public List<ClienteBean> buscarClienteAC(String nome) throws ProjetoException {
		PreparedStatement ps = null;
		Connection con = ConnectionFactory.getConnection();

		FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
				.get("obj_usuario");
		try {
			String sql = "select coalesce(cpfcnpj,'')||'-'||nome ||'-'|| coalesce(telefone1,0) as nome, codcliente, codigo_alternativo, coalesce(cpfcnpj,'') as cpfcnpj, "
					+ " coalesce(acerto_sessao, false) acerto_sessao from financeiro.cliente where ativo is not false "
					+ " and coalesce(cpfcnpj,'')||'-'||coalesce(telefone1,0)||'-'||nome like ? "
					+ " order by nome limit 20";

			ps = con.prepareStatement(sql);
			ps.setString(1, "%" + nome.toUpperCase() + "%");
			ResultSet rs = ps.executeQuery();

			List<ClienteBean> colecao = new ArrayList<ClienteBean>();

			while (rs.next()) {

				ClienteBean cli = new ClienteBean();
				cli.setNome(rs.getString("nome"));
				cli.setCodcliente(rs.getInt("codcliente"));
				cli.setCpfcnpj(rs.getString("cpfcnpj"));
				cli.setCodigo_alternativo(rs.getInt("codigo_alternativo"));
				if (rs.getBoolean("acerto_sessao") == true)
					cli.setAcerto("S");
				else
					cli.setAcerto("N");
				colecao.add(cli);

			}
			
			rs.close();
			ps.close();
			return colecao;
		} catch (Exception sqle) {

			throw new ProjetoException(sqle);

		} finally {
			try {
				con.close();
			} catch (Exception sqlc) {
				sqlc.printStackTrace();
				System.exit(1);
			}

		}
	}
	
	public List<ClienteBean> buscarClienteACComInativos(String nome) throws ProjetoException {
		PreparedStatement ps = null;
		Connection con = ConnectionFactory.getConnection();

		FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
				.get("obj_usuario");
		try {
			String sql = "select coalesce(cpfcnpj,'')||'-'|| nome as nome, codcliente, codigo_alternativo, coalesce(cpfcnpj,'') as cpfcnpj, "
					+ " coalesce(acerto_sessao, false) acerto_sessao from financeiro.cliente where coalesce(cpfcnpj,'')||'-'||nome like ?  order by nome limit 20";

			ps = con.prepareStatement(sql);
			ps.setString(1, "%" + nome.toUpperCase() + "%");
			ResultSet rs = ps.executeQuery();

			List<ClienteBean> colecao = new ArrayList<ClienteBean>();

			while (rs.next()) {

				ClienteBean cli = new ClienteBean();
				cli.setNome(rs.getString("nome"));
				cli.setCodcliente(rs.getInt("codcliente"));
				cli.setCpfcnpj(rs.getString("cpfcnpj"));
				cli.setCodigo_alternativo(rs.getInt("codigo_alternativo"));
				if (rs.getBoolean("acerto_sessao") == true)
					cli.setAcerto("S");
				else
					cli.setAcerto("N");
				colecao.add(cli);

			}
			
			rs.close();
			ps.close();
			return colecao;
		} catch (Exception sqle) {

			throw new ProjetoException(sqle);

		} finally {
			try {
				con.close();
			} catch (Exception sqlc) {
				sqlc.printStackTrace();
				System.exit(1);
			}

		}
	}

	
	public List<FornecedorBean> buscarFornecedorAC(String nome) throws ProjetoException {
		PreparedStatement ps = null;
		Connection con = ConnectionFactory.getConnection();
		FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
				.get("obj_usuario");
		try {
			String sql = "select nome, codforn from financeiro.fornecedor where nome like ? ";
			ps = con.prepareStatement(sql);
			ps.setString(1, "%" + nome.toUpperCase() + "%");
			ResultSet rs = ps.executeQuery();
			List<FornecedorBean> colecao = new ArrayList<FornecedorBean>();
			while (rs.next()) {
				FornecedorBean fun = new FornecedorBean();
				fun.setNome(rs.getString("nome"));
				fun.setCodforn(rs.getInt("codforn"));
				colecao.add(fun);
			}
			
			rs.close();
			ps.close();
			return colecao;
		} catch (Exception sqle) {
			throw new ProjetoException(sqle);
		} finally {
			try {
				con.close();
			} catch (Exception sqlc) {
				sqlc.printStackTrace();
				System.exit(1);
			}
		}
	}

	public List<PortadorBean> buscarPortadorAC(String descricao) throws ProjetoException {
		PreparedStatement ps = null;
		Connection con = ConnectionFactory.getConnection();
		FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
				.get("obj_usuario");
		try {
			String sql = "select descricao, codportador from financeiro.portador where descricao like ? ";
			ps = con.prepareStatement(sql);
			ps.setString(1, "%" + descricao.toUpperCase() + "%");
			ResultSet rs = ps.executeQuery();
			List<PortadorBean> colecao = new ArrayList<PortadorBean>();
			while (rs.next()) {
				PortadorBean ptd = new PortadorBean();
				ptd.setDescricao(rs.getString("descricao"));
				ptd.setCodportador(rs.getInt("codportador"));
				colecao.add(ptd);
			}
			
			rs.close();
			ps.close();
			return colecao;
		} catch (Exception sqle) {
			throw new ProjetoException(sqle);
		} finally {
			try {
				con.close();
			} catch (Exception sqlc) {
				sqlc.printStackTrace();
				System.exit(1);
			}
		}
	}

	public List<String> buscarListaVendasNomeAC(String nome) throws ProjetoException {
		PreparedStatement ps = null;
		Connection con = ConnectionFactory.getConnection();
		FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
				.get("obj_usuario");
		try {
			String sql = "SELECT v.codvenda, v.dtemissao, v.codcliente,cpfcnpj, nome FROM financeiro.venda v "
					+ "join financeiro.cliente c on c.codcliente = v.codcliente " + "where nome like ? ";
			ps = con.prepareStatement(sql);
			ps.setString(1, "%" + nome.toUpperCase() + "%");
			// ps.setString(2, "%"+cpfcnpj.toUpperCase()+"%");
			ResultSet rs = ps.executeQuery();
			List<String> colecao = new ArrayList<String>();
			while (rs.next()) {
				colecao.add(rs.getString("nome"));
			}
			
			rs.close();
			ps.close();
			return colecao;
		} catch (Exception sqle) {
			throw new ProjetoException(sqle);
		} finally {
			try {
				con.close();
			} catch (Exception sqlc) {
				sqlc.printStackTrace();
				System.exit(1);
			}
		}
	}

	public List<String> buscarListaVendasCPFAC(String cpfcnpj) throws ProjetoException {
		PreparedStatement ps = null;
		Connection con = ConnectionFactory.getConnection();
		FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
				.get("obj_usuario");
		try {
			String sql = "SELECT v.codvenda, v.dtemissao, v.codcliente,cpfcnpj, nome FROM financeiro.venda v "
					+ "join financeiro.cliente c on c.codcliente = v.codcliente "
					+ "where cpfcnpj like ? ";
			ps = con.prepareStatement(sql);
			// ps.setString(1, "%"+nome.toUpperCase()+"%");
			ps.setString(1, "%" + cpfcnpj.toUpperCase() + "%");
			ResultSet rs = ps.executeQuery();
			List<String> colecao = new ArrayList<String>();
			while (rs.next()) {
				colecao.add(rs.getString("cpfcnpj"));
			}
			
			rs.close();
			ps.close();
			return colecao;
		} catch (Exception sqle) {
			throw new ProjetoException(sqle);
		} finally {
			try {
				con.close();
			} catch (Exception sqlc) {
				sqlc.printStackTrace();
				System.exit(1);
			}
		}
	}

	

}
