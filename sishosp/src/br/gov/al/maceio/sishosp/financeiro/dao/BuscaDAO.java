package br.gov.al.maceio.sishosp.financeiro.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.gov.al.maceio.sishosp.acl.model.FuncionarioBean;
import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;
import br.gov.al.maceio.sishosp.comum.util.TratamentoErrosUtil;
import br.gov.al.maceio.sishosp.financeiro.model.CentroCustoBean;
import br.gov.al.maceio.sishosp.financeiro.model.ClienteBean;
import br.gov.al.maceio.sishosp.financeiro.model.DespesaBean;
import br.gov.al.maceio.sishosp.financeiro.model.FornecedorBean;
import br.gov.al.maceio.sishosp.financeiro.model.PortadorBean;
import br.gov.al.maceio.sishosp.financeiro.model.TipoDocumentoBean;



public class BuscaDAO {
	
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
				funcionarioBean.setId(set.getLong("codfunc"));
				funcionarioBean.setNome(set.getString("nome"));
			}

			ps.close();
			set.close();
		} catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
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
		} catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
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
		} catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
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

		ArrayList<PortadorBean> lista = new ArrayList<>();
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement ps = con.prepareStatement(sql);
			set = ps.executeQuery();

			while (set.next()) {
				PortadorBean portador = new PortadorBean();
				portador.setCodportador(set.getInt("codportador"));
				portador.setDescricao(set.getString("descricao"));
				lista.add(portador);
			}
			ps.close();
		} catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return lista;
	}

	public List<PortadorBean> lstPortador(String desc) throws ProjetoException {

		Connection con = null;
		ResultSet rs = null;

		String sql = " select * from financeiro.portador where descricao like ?  ";

		List<PortadorBean> lista = new ArrayList<PortadorBean>();

		try {

			con = ConnectionFactory.getConnection();
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, "%" + desc.toUpperCase() + "%");
			rs = ps.executeQuery();

			while (rs.next()) {
				PortadorBean portador = new PortadorBean();
				portador.setCodportador(rs.getInt("codportador"));
				portador.setDescricao(rs.getString("descricao"));
				lista.add(portador);
			}

			rs.close();
			ps.close();
		} catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return lista;
	}

	public List<FornecedorBean> buscaForn(String nome, String cnpj) throws ProjetoException {

		Connection con = null;
		ResultSet set = null;
		String sql = " SELECT codforn, nome, fantasia, cnpj FROM financeiro.fornecedor where (coalesce(nome,'') like ? or coalesce(fantasia,'') like ?) and coalesce(cnpj,'') like ? ";

		List<FornecedorBean> lista = new ArrayList<FornecedorBean>();

		try {
			con = ConnectionFactory.getConnection();
			
			if (cnpj == null) {
				cnpj = "";
			}

			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, "%" + nome.toUpperCase() + "%");
			ps.setString(2, "%" + nome.toUpperCase() + "%");
			ps.setString(3, "%" + cnpj.toUpperCase() + "%");
			set = ps.executeQuery();

			while (set.next()) {
				FornecedorBean fornecedor = new FornecedorBean();
				fornecedor.setCodforn(set.getInt("codforn"));
				fornecedor.setNome(set.getString("nome"));
				fornecedor.setFantasia(set.getString("fantasia"));
				fornecedor.setCpfcnpj(set.getString("cnpj"));
				lista.add(fornecedor);
			}
			ps.close();
		} catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return lista;
	}

	public List<FornecedorBean> buscaFornecedor() throws ProjetoException {

		Connection con = null;
		ResultSet set = null;

		String sql = "SELECT * FROM financeiro.fornecedor ";
		List<FornecedorBean> lista = new ArrayList<FornecedorBean>();

		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement ps = con.prepareStatement(sql);
			set = ps.executeQuery();

			while (set.next()) {
				FornecedorBean fornecedor = new FornecedorBean();
				fornecedor.setCodforn(set.getInt("codforn"));
				fornecedor.setNome(set.getString("nome"));
				fornecedor.setFantasia(set.getString("fantasia"));
				fornecedor.setCpfcnpj(set.getString("cnpj"));
				lista.add(fornecedor);
			}
			ps.close();
		} catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return lista;
	}

	public List<CentroCustoBean> buscaCentroCusto() throws ProjetoException {

		Connection con = null;
		ResultSet set = null;

		String sql = "select * from financeiro.ccusto";

		List<CentroCustoBean> lista = new ArrayList<CentroCustoBean>();

		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement ps = con.prepareStatement(sql);
			set = ps.executeQuery();

			while (set.next()) {
				CentroCustoBean centroCusto = new CentroCustoBean();
				centroCusto.setIdccusto(set.getInt("idccusto"));
				centroCusto.setDescricao(set.getString("descricao"));
				lista.add(centroCusto);
			}
			ps.close();
		} catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return lista;
	}

	public List<TipoDocumentoBean> buscaTipoDoc() throws ProjetoException {

		Connection con = null;
		ResultSet set = null;

		String sql = "select codtipodocumento, descricao from financeiro.tipodocumento  order by descricao";

		List<TipoDocumentoBean> lista = new ArrayList<TipoDocumentoBean>();

		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement ps = con.prepareStatement(sql);
			set = ps.executeQuery();

			while (set.next()) {
				TipoDocumentoBean tipoDocumento = new TipoDocumentoBean();
				tipoDocumento.setCodtipodocumento(set.getInt("codtipodocumento"));
				tipoDocumento.setDescricao(set.getString("descricao"));
				lista.add(tipoDocumento);
			}
			ps.close();
		} catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return lista;
	}

	public List<DespesaBean> buscaDespesa() throws ProjetoException {

		Connection con = null;
		ResultSet set = null;

		String sql = "select * from financeiro.despesa ;";

		List<DespesaBean> lista = new ArrayList<DespesaBean>();
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement ps = con.prepareStatement(sql);
			set = ps.executeQuery();
			while (set.next()) {
				DespesaBean despesa = new DespesaBean();
				despesa.setId(set.getInt("iddespesa"));
				despesa.setDescricao(set.getString("descricao"));
				lista.add(despesa);
			}
			ps.close();
		} catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return lista;
	}

	
	// INICIO BUSCAS AUTOCOMPLETE
	public List<ClienteBean> buscarClienteAC(String nome) throws ProjetoException {
		PreparedStatement ps = null;
		Connection con = ConnectionFactory.getConnection();

		List<ClienteBean> colecao = new ArrayList<ClienteBean>();
		try {
			String sql = "select coalesce(cpfcnpj,'')||'-'||nome ||'-'|| coalesce(telefone1,0) as nome, codcliente, codigo_alternativo, coalesce(cpfcnpj,'') as cpfcnpj, "
					+ " coalesce(acerto_sessao, false) acerto_sessao from financeiro.cliente where ativo is not false "
					+ " and coalesce(cpfcnpj,'')||'-'||coalesce(telefone1,0)||'-'||nome like ? "
					+ " order by nome limit 20";

			ps = con.prepareStatement(sql);
			ps.setString(1, "%" + nome.toUpperCase() + "%");
			ResultSet rs = ps.executeQuery();


			while (rs.next()) {
				ClienteBean cliente = new ClienteBean();
				cliente.setNome(rs.getString("nome"));
				cliente.setCodcliente(rs.getInt("codcliente"));
				cliente.setCpfcnpj(rs.getString("cpfcnpj"));
				cliente.setCodigo_alternativo(rs.getInt("codigo_alternativo"));
				if (rs.getBoolean("acerto_sessao") == true)
					cliente.setAcerto("S");
				else
					cliente.setAcerto("N");
				colecao.add(cliente);
			}
			
			rs.close();
			ps.close();
		} catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				con.close();
			} catch (Exception sqlc) {
				sqlc.printStackTrace();
				System.exit(1);
			}
		}
		return colecao;
	}
	
	public List<ClienteBean> buscarClienteACComInativos(String nome) throws ProjetoException {
		PreparedStatement ps = null;
		Connection con = ConnectionFactory.getConnection();

		List<ClienteBean> colecao = new ArrayList<ClienteBean>();
		try {
			String sql = "select coalesce(cpfcnpj,'')||'-'|| nome as nome, codcliente, codigo_alternativo, coalesce(cpfcnpj,'') as cpfcnpj, "
					+ " coalesce(acerto_sessao, false) acerto_sessao from financeiro.cliente where coalesce(cpfcnpj,'')||'-'||nome like ?  order by nome limit 20";

			ps = con.prepareStatement(sql);
			ps.setString(1, "%" + nome.toUpperCase() + "%");
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				ClienteBean cliente = new ClienteBean();
				cliente.setNome(rs.getString("nome"));
				cliente.setCodcliente(rs.getInt("codcliente"));
				cliente.setCpfcnpj(rs.getString("cpfcnpj"));
				cliente.setCodigo_alternativo(rs.getInt("codigo_alternativo"));
				if (rs.getBoolean("acerto_sessao") == true)
					cliente.setAcerto("S");
				else
					cliente.setAcerto("N");
				colecao.add(cliente);
			}
			
			rs.close();
			ps.close();
		} catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				con.close();
			} catch (Exception sqlc) {
				sqlc.printStackTrace();
				System.exit(1);
			}
		}
		return colecao;
	}
	
	public List<FornecedorBean> buscarFornecedorAC(String nome) throws ProjetoException {
		PreparedStatement ps = null;
		Connection con = ConnectionFactory.getConnection();
		List<FornecedorBean> colecao = new ArrayList<FornecedorBean>();

		try {
			String sql = "select nome, codforn from financeiro.fornecedor where nome like ? ";
			ps = con.prepareStatement(sql);
			ps.setString(1, "%" + nome.toUpperCase() + "%");
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				FornecedorBean fun = new FornecedorBean();
				fun.setNome(rs.getString("nome"));
				fun.setCodforn(rs.getInt("codforn"));
				colecao.add(fun);
			}
			
			rs.close();
			ps.close();
		} catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				con.close();
			} catch (Exception sqlc) {
				sqlc.printStackTrace();
				System.exit(1);
			}
		}
		return colecao;
	}

	public List<PortadorBean> buscarPortadorAC(String descricao) throws ProjetoException {
		PreparedStatement ps = null;
		Connection con = ConnectionFactory.getConnection();
		List<PortadorBean> colecao = new ArrayList<PortadorBean>();

		try {
			String sql = "select descricao, codportador from financeiro.portador where descricao like ? ";
			ps = con.prepareStatement(sql);
			ps.setString(1, "%" + descricao.toUpperCase() + "%");
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				PortadorBean ptd = new PortadorBean();
				ptd.setDescricao(rs.getString("descricao"));
				ptd.setCodportador(rs.getInt("codportador"));
				colecao.add(ptd);
			}
			
			rs.close();
			ps.close();
		} catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				con.close();
			} catch (Exception sqlc) {
				sqlc.printStackTrace();
				System.exit(1);
			}
		}
		return colecao;
	}

	public List<String> buscarListaVendasNomeAC(String nome) throws ProjetoException {
		PreparedStatement ps = null;
		Connection con = ConnectionFactory.getConnection();

		List<String> colecao = new ArrayList<String>();
		try {
			String sql = "SELECT v.codvenda, v.dtemissao, v.codcliente,cpfcnpj, nome FROM financeiro.venda v "
					+ "join financeiro.cliente c on c.codcliente = v.codcliente " + "where nome like ? ";
			ps = con.prepareStatement(sql);
			ps.setString(1, "%" + nome.toUpperCase() + "%");
			// ps.setString(2, "%"+cpfcnpj.toUpperCase()+"%");
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				colecao.add(rs.getString("nome"));
			}
			
			rs.close();
			ps.close();
		} catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				con.close();
			} catch (Exception sqlc) {
				sqlc.printStackTrace();
				System.exit(1);
			}
		}
		return colecao;
	}

	public List<String> buscarListaVendasCPFAC(String cpfcnpj) throws ProjetoException {
		PreparedStatement ps = null;
		Connection con = ConnectionFactory.getConnection();

		List<String> colecao = new ArrayList<String>();
		try {
			String sql = "SELECT v.codvenda, v.dtemissao, v.codcliente,cpfcnpj, nome FROM financeiro.venda v "
					+ "join financeiro.cliente c on c.codcliente = v.codcliente "
					+ "where cpfcnpj like ? ";
			ps = con.prepareStatement(sql);
			// ps.setString(1, "%"+nome.toUpperCase()+"%");
			ps.setString(1, "%" + cpfcnpj.toUpperCase() + "%");
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				colecao.add(rs.getString("cpfcnpj"));
			}
			
			rs.close();
			ps.close();
		} catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				con.close();
			} catch (Exception sqlc) {
				sqlc.printStackTrace();
				System.exit(1);
			}
		}
		return colecao;
	}
}
