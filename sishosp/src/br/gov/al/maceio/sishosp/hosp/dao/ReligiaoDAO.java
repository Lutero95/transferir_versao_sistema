package br.gov.al.maceio.sishosp.hosp.dao;

import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;
import br.gov.al.maceio.sishosp.hosp.model.Religiao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ReligiaoDAO {
	private Connection conexao = null;
	Connection con = null;
	PreparedStatement ps = null;

	public boolean gravarReligiao(Religiao religiao) {
		Boolean retorno = false;
		String sql = "INSERT INTO hosp.religiao (descricao) VALUES (?);";

		try {
			con = ConnectionFactory.getConnection();
			ps = con.prepareStatement(sql);
			ps.setString(1, religiao.getDescricao().toUpperCase());
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

	public Boolean alterarReligiao(Religiao religiao) {
		Boolean retorno = false;
		String sql = "UPDATE hosp.religiao SET descricao = ? WHERE id = ?";

		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setString(1, religiao.getDescricao().toUpperCase());
			stmt.setInt(2, religiao.getId());
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

	public Boolean excluirReligiao(Integer id) {
		Boolean retorno = false;
		String sql = "DELETE FROM hosp.religiao WHERE id = ?";

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

	public ArrayList<Religiao> listarReligiao() throws ProjetoException {

		String sql = "SELECT id, descricao FROM hosp.religiao ORDER BY descricao";

		ArrayList<Religiao> lista = new ArrayList();

		try {
			conexao = ConnectionFactory.getConnection();
			PreparedStatement stm = conexao.prepareStatement(sql);
			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				Religiao r = new Religiao();
				r.setId(rs.getInt("id"));
				r.setDescricao(rs.getString("descricao"));

				lista.add(r);
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

	public Religiao buscarReligiaoPorId(Integer i)
			throws ProjetoException {
		
		String sql = "SELECT id, descricao FROM hosp.religiao WHERE id = ? ORDER BY descricao";
		try {
			con = ConnectionFactory.getConnection();
			ps = con.prepareStatement(sql);
			ps.setInt(1, i);
			ResultSet rs = ps.executeQuery();
			Religiao r = new Religiao();
			while (rs.next()) {
				r.setId(rs.getInt("id"));
				r.setDescricao(rs.getString("descricao"));
			}

			return r;
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
