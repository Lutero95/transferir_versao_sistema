package br.gov.al.maceio.sishosp.hosp.dao;

import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;
import br.gov.al.maceio.sishosp.comum.util.TratamentoErrosUtil;
import br.gov.al.maceio.sishosp.comum.util.VerificadorUtil;
import br.gov.al.maceio.sishosp.hosp.model.TipoLogradouroBean;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TipoLogradouroDAO {

	Connection con = null;
	PreparedStatement ps = null;


	public boolean gravarTipoLogradouro(TipoLogradouroBean tipoLogradouro) throws ProjetoException {
		Boolean retorno = false;
		String sql = "insert into hosp.tipologradouro (codigo,abreviatura, logradouro) values (?,?,?);";

		try {
			con = ConnectionFactory.getConnection();
			ps = con.prepareStatement(sql);
			// FuncionarioBean func = null;
			// func.getNome();
			ps.setString(1, tipoLogradouro.getCodigo().toUpperCase());
			ps.setString(2, tipoLogradouro.getAbreviatura().toUpperCase());
			ps.setString(3, tipoLogradouro.getLogradouro().toUpperCase());

			ps.execute();
			con.commit();
			retorno = true;
		}

		catch ( SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		}
		catch (Exception e) {
			throw new ProjetoException(e, this.getClass().getName());
		}finally {
			try {
				con.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return retorno;
	}

	public List<TipoLogradouroBean> listarTipoLogradouro() throws ProjetoException {
		List<TipoLogradouroBean> lista = new ArrayList<>();
		String sql = "select id, codigo,abreviatura, logradouro from hosp.tipologradouro order by logradouro";
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stm = con.prepareStatement(sql);
			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				TipoLogradouroBean tipoLogradouro = new TipoLogradouroBean();
				tipoLogradouro.setId(rs.getInt("id"));
				tipoLogradouro.setCodigo(rs.getString("codigo"));
				tipoLogradouro.setAbreviatura(rs.getString("abreviatura"));
				tipoLogradouro.setLogradouro(rs.getString("logradouro"));

				lista.add(tipoLogradouro);
			}
		} catch (SQLException ex2) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(ex2), this.getClass().getName(), ex2);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return lista;
	}

	public Boolean alterarTipoLogradouro(TipoLogradouroBean tipoLogradouro) throws ProjetoException {
		Boolean retorno = false;
		String sql = "update hosp.tipologradouro set codigo = ?, abreviatura = ?, logradouro=? where id = ?";
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setString(1, tipoLogradouro.getCodigo().toUpperCase());
			stmt.setString(2, tipoLogradouro.getAbreviatura().toUpperCase());
			stmt.setString(3, tipoLogradouro.getLogradouro().toUpperCase());
			stmt.setInt(4, tipoLogradouro.getId());
			stmt.executeUpdate();
			con.commit();
			retorno = true;
		} catch (SQLException ex2) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(ex2), this.getClass().getName(), ex2);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				con.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return retorno;
	}

	public Boolean excluirTipoLogradouro(TipoLogradouroBean tipoLogradouro) throws ProjetoException {
		Boolean retorno = false;
		String sql = "delete from hosp.tipologradouro where id = ?";

		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setLong(1, tipoLogradouro.getId());
			stmt.execute();
			con.commit();
			retorno = true;
		} catch (SQLException ex2) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(ex2), this.getClass().getName(), ex2);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				con.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return retorno;
	}


	private void mapearResultSet(List<TipoLogradouroBean> lista, ResultSet rs) throws SQLException {
		TipoLogradouroBean tipoLogradouro = new TipoLogradouroBean();
		tipoLogradouro.setId(rs.getInt("id"));
		tipoLogradouro.setCodigo(rs.getString("codigo"));
		tipoLogradouro.setAbreviatura(rs.getString("abreviatura"));
		tipoLogradouro.setLogradouro(rs.getString("logradouro"));

		lista.add(tipoLogradouro);
	}

	public TipoLogradouroBean buscaTipoLogradouroPorId(Integer id) throws ProjetoException {
		con = ConnectionFactory.getConnection();
		String sql = "select id, codigo, abreviatura, logradouro from hosp.tipologradouro where id = ?";
		TipoLogradouroBean tipoLogradouro = new TipoLogradouroBean();
		try {
			ps = con.prepareStatement(sql);
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				tipoLogradouro.setId(rs.getInt("id"));
				tipoLogradouro.setCodigo(rs.getString("codigo"));
				tipoLogradouro.setAbreviatura(rs.getString("abreviatura"));
				tipoLogradouro.setLogradouro(rs.getString("logradouro"));
			}
		} catch (SQLException ex2) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(ex2), this.getClass().getName(), ex2);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				con.close();
			} catch (Exception sqlc) {
				sqlc.printStackTrace();
			}
		}
		return tipoLogradouro;
	}

	public List<TipoLogradouroBean> buscarTipoLogradouro(String campoBusca, String tipo) throws ProjetoException {
		List<TipoLogradouroBean> lista = new ArrayList<>();
		String sql = "select id, codigo, abreviatura, logradouro from hosp.tipologradouro where ";

		if(tipo.equals("codigo")){
			sql = sql + "codigo like ?";
		}
		else if(tipo.equals("abreviatura")){
			sql = sql + "desccabreviatura ilike ?";
		}
		else if(tipo.equals("logradouro")){
			sql = sql + "logradouro ilike ?";
		}

		sql = sql + "order by logradouro";

		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stm = con.prepareStatement(sql);
			stm.setString(1, "%" + campoBusca.toUpperCase() + "%");
			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				TipoLogradouroBean tipoLogradouro = new TipoLogradouroBean();
				tipoLogradouro.setId(rs.getInt("id"));
				tipoLogradouro.setCodigo(rs.getString("codigo"));
				tipoLogradouro.setAbreviatura(rs.getString("abreviatura"));
				tipoLogradouro.setLogradouro(rs.getString("logradouro"));

				lista.add(tipoLogradouro);
			}
		} catch (SQLException ex2) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(ex2), this.getClass().getName(), ex2);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return lista;
	}
}
