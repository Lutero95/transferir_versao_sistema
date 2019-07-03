package br.gov.al.maceio.sishosp.hosp.dao;

import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;
import br.gov.al.maceio.sishosp.hosp.model.Sexo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SexoDAO {
	private Connection conexao = null;
	Connection con = null;
	PreparedStatement ps = null;

	public boolean gravarSexo(Sexo sexo) {
		Boolean retorno = false;
		String sql = "INSERT INTO hosp.sexo (descricao) VALUES (?);";

		try {
			con = ConnectionFactory.getConnection();
			ps = con.prepareStatement(sql);
			ps.setString(1, sexo.getDescricao().toUpperCase());
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

	public Boolean alterarSexo(Sexo sexo) {
		Boolean retorno = false;
		String sql = "UPDATE hosp.sexo SET descricao = ? WHERE id = ?";

		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setString(1, sexo.getDescricao().toUpperCase());
			stmt.setInt(2, sexo.getId());
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

	public Boolean excluirSexo(Integer id) {
		Boolean retorno = false;
		String sql = "DELETE FROM hosp.sexo WHERE id = ?";

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

	public ArrayList<Sexo> listarSexos() throws ProjetoException {

		String sql = "SELECT id, descricao FROM hosp.sexo ORDER BY descricao";

		ArrayList<Sexo> lista = new ArrayList();

		try {
			conexao = ConnectionFactory.getConnection();
			PreparedStatement stm = conexao.prepareStatement(sql);
			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				Sexo s = new Sexo();
				s.setId(rs.getInt("id"));
				s.setDescricao(rs.getString("descricao"));

				lista.add(s);
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

	public Sexo buscaSexoPorId(Integer i)
			throws ProjetoException {
		
		String sql = "SELECT id, descricao FROM hosp.sexo WHERE id = ? ORDER BY descricao";
		try {
			con = ConnectionFactory.getConnection();
			ps = con.prepareStatement(sql);
			ps.setInt(1, i);
			ResultSet rs = ps.executeQuery();
			Sexo m = new Sexo();
			while (rs.next()) {
				m.setId(rs.getInt("id"));
				m.setDescricao(rs.getString("descricao"));
			}

			return m;
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
