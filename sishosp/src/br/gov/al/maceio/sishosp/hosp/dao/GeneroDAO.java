package br.gov.al.maceio.sishosp.hosp.dao;

import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;
import br.gov.al.maceio.sishosp.hosp.model.Genero;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class GeneroDAO {
	private Connection conexao = null;
	Connection con = null;
	PreparedStatement ps = null;

	public boolean gravarGenero(Genero genero) {
		Boolean retorno = false;
		String sql = "INSERT INTO hosp.genero (descricao) VALUES (?);";

		try {
			con = ConnectionFactory.getConnection();
			ps = con.prepareStatement(sql);
			ps.setString(1, genero.getDescricao().toUpperCase());
			ps.execute();
			con.commit();
			retorno = true;
		} catch (Exception ex) {
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

	public Boolean alterarGenero(Genero genero) {
		Boolean retorno = false;
		String sql = "UPDATE hosp.genero SET descricao = ? WHERE id = ?";

		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setString(1, genero.getDescricao().toUpperCase());
			stmt.setInt(2, genero.getId());
			stmt.executeUpdate();
			con.commit();
			retorno = true;
		} catch (Exception ex) {
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

	public Boolean excluirGenero(Integer id) {
		Boolean retorno = false;
		String sql = "DELETE FROM hosp.genero WHERE id = ?";

		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setLong(1, id);
			stmt.execute();
			con.commit();
			retorno = true;
		} catch (Exception ex) {
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

	public ArrayList<Genero> listarGeneros() throws ProjetoException {

		String sql = "SELECT id, descricao FROM hosp.genero ORDER BY descricao";

		ArrayList<Genero> lista = new ArrayList();

		try {
			conexao = ConnectionFactory.getConnection();
			PreparedStatement stm = conexao.prepareStatement(sql);
			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				Genero g = new Genero();
				g.setId(rs.getInt("id"));
				g.setDescricao(rs.getString("descricao"));

				lista.add(g);
			}
		} catch (Exception ex) {
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

	public Genero buscaGeneroPorId(Integer i)
			throws ProjetoException {
		
		String sql = "SELECT id, descricao FROM hosp.genero WHERE id = ? ORDER BY descricao";
		try {
			con = ConnectionFactory.getConnection();
			ps = con.prepareStatement(sql);
			ps.setInt(1, i);
			ResultSet rs = ps.executeQuery();
			Genero g = new Genero();
			while (rs.next()) {
				g.setId(rs.getInt("id"));
				g.setDescricao(rs.getString("descricao"));
			}

			return g;
		} catch (Exception ex) {
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
