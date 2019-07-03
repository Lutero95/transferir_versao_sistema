package br.gov.al.maceio.sishosp.hosp.dao;

import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;
import br.gov.al.maceio.sishosp.hosp.model.Operadora;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class OperadoraDAO {
	private Connection conexao = null;
	Connection con = null;
	PreparedStatement ps = null;

	public boolean gravarOperadora(Operadora operadora) {
		Boolean retorno = false;
		String sql = "INSERT INTO hosp.operadora (descricao) VALUES (?);";

		try {
			con = ConnectionFactory.getConnection();
			ps = con.prepareStatement(sql);
			ps.setString(1, operadora.getDescricao().toUpperCase());
			ps.execute();
			con.commit();
			retorno = true;
		} catch (SQLException ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			return retorno;
		}
	}

	public Boolean alterarOperadora(Operadora operadora) {
		Boolean retorno = false;
		String sql = "UPDATE hosp.operadora SET descricao = ? WHERE id = ?";

		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setString(1, operadora.getDescricao().toUpperCase());
			stmt.setInt(2, operadora.getId());
			stmt.executeUpdate();
			con.commit();
			retorno = true;
		} catch (SQLException ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			return retorno;
		}
	}

	public Boolean excluirOperadora(Integer id) {
		Boolean retorno = false;
		String sql = "DELETE FROM hosp.operadora WHERE id = ?";

		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setLong(1, id);
			stmt.execute();
			con.commit();
			retorno = true;
		} catch (SQLException ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			return retorno;
		}
	}

	public ArrayList<Operadora> listarOperadoras() throws ProjetoException {

		String sql = "SELECT id, descricao FROM hosp.operadora ORDER BY descricao";

		ArrayList<Operadora> lista = new ArrayList();

		try {
			conexao = ConnectionFactory.getConnection();
			PreparedStatement stm = conexao.prepareStatement(sql);
			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				Operadora o = new Operadora();
				o.setId(rs.getInt("id"));
				o.setDescricao(rs.getString("descricao"));

				lista.add(o);
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		} finally {
			try {
				conexao.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return lista;
	}

	public Operadora buscarOperadoraPorId(Integer i)
			throws ProjetoException {
		
		String sql = "SELECT id, descricao FROM hosp.operadora WHERE id = ? ORDER BY descricao";
		try {
			con = ConnectionFactory.getConnection();
			ps = con.prepareStatement(sql);
			ps.setInt(1, i);
			ResultSet rs = ps.executeQuery();
			Operadora r = new Operadora();
			while (rs.next()) {
				r.setId(rs.getInt("id"));
				r.setDescricao(rs.getString("descricao"));
			}

			return r;
		} catch (SQLException ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

}
