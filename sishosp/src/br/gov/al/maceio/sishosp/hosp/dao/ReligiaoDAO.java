package br.gov.al.maceio.sishosp.hosp.dao;

import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;
import br.gov.al.maceio.sishosp.comum.util.TratamentoErrosUtil;
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

	public boolean gravarReligiao(Religiao religiao) throws ProjetoException {
		Boolean retorno = false;
		String sql = "INSERT INTO hosp.religiao (descricao) VALUES (?);";

		try {
			con = ConnectionFactory.getConnection();
			ps = con.prepareStatement(sql);
			ps.setString(1, religiao.getDescricao().toUpperCase());
			ps.execute();
			con.commit();
			retorno = true;
		} catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return retorno;
	}

	public Boolean alterarReligiao(Religiao religiao) throws ProjetoException {
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
		} catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return retorno;
	}

	public Boolean excluirReligiao(Integer id) throws ProjetoException {
		Boolean retorno = false;
		String sql = "DELETE FROM hosp.religiao WHERE id = ?";

		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setLong(1, id);
			stmt.execute();
			con.commit();
			retorno = true;
		} catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return retorno;
	}

	public ArrayList<Religiao> listarReligiao() throws ProjetoException {

		String sql = "SELECT id, descricao FROM hosp.religiao ORDER BY descricao";

		ArrayList<Religiao> lista = new ArrayList();

		try {
			conexao = ConnectionFactory.getConnection();
			PreparedStatement stm = conexao.prepareStatement(sql);
			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				Religiao religiao = new Religiao();
				religiao.setId(rs.getInt("id"));
				religiao.setDescricao(rs.getString("descricao"));
				lista.add(religiao);
			}
		} catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				conexao.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return lista;
	}

	public Religiao buscarReligiaoPorId(Integer id) throws ProjetoException {
		
		String sql = "SELECT id, descricao FROM hosp.religiao WHERE id = ? ORDER BY descricao";
		Religiao religiao = new Religiao();
		try {
			con = ConnectionFactory.getConnection();
			ps = con.prepareStatement(sql);
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				religiao.setId(rs.getInt("id"));
				religiao.setDescricao(rs.getString("descricao"));
			}
		} catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return religiao;
	}
}
