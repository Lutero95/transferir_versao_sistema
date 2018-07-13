package br.gov.al.maceio.sishosp.hosp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;
import br.gov.al.maceio.sishosp.hosp.model.MotivoDesligamentoBean;

public class MotivoDesligamentoDAO {
	private Connection conexao = null;
	Connection con = null;
	PreparedStatement ps = null;

	public boolean gravarMotivo(MotivoDesligamentoBean motivo)
			throws SQLException, ProjetoException {

		String sql = "insert into hosp.motivo_desligamento (motivo) values (?);";
		try {
			con = ConnectionFactory.getConnection();
			ps = con.prepareStatement(sql);
			ps.setString(1, motivo.getMotivo_desligamento().toUpperCase());
			ps.execute();
			con.commit();
			return true;
		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				ex.printStackTrace();
				System.exit(1);
			}
		}
	}

	public ArrayList<MotivoDesligamentoBean> listarMotivos() throws SQLException,
			ProjetoException {
		String sql = "select id, motivo from hosp.motivo_desligamento order by motivo";

		ArrayList<MotivoDesligamentoBean> lista = new ArrayList();

		try {
			conexao = ConnectionFactory.getConnection();
			PreparedStatement stm = conexao.prepareStatement(sql);
			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				MotivoDesligamentoBean m = new MotivoDesligamentoBean();
				m.setId_motivo(rs.getInt("id"));
				m.setMotivo_desligamento(rs.getString("motivo"));

				lista.add(m);
			}
		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		} finally {
			try {
				conexao.close();
			} catch (Exception ex) {
				ex.printStackTrace();
				System.exit(1);
			}
		}
		return lista;
	}

	public List<MotivoDesligamentoBean> listarMotivosBusca(String descricao,
			Integer tipo) throws ProjetoException {
		
		List<MotivoDesligamentoBean> lista = new ArrayList<>();
		
		String sql = "select id, motivo from hosp.motivo_desligamento ";
		if (tipo == 1) {
			sql += " where motivo LIKE ?  order by motivo";
		}
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stm = con.prepareStatement(sql);
			stm.setString(1, "%" + descricao.toUpperCase() + "%");
			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				MotivoDesligamentoBean motivo = new MotivoDesligamentoBean();
				motivo.setId_motivo(rs.getInt("id"));
				motivo.setMotivo_desligamento(rs.getString("motivo"));

				lista.add(motivo);
			}
		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				ex.printStackTrace();
				System.exit(1);
			}
		}

		return lista;
	}

	public Boolean alterarMotivo(MotivoDesligamentoBean motivo)
			throws ProjetoException {
		String sql = "update hosp.motivo_desligamento set motivo = ? where id = ?";
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setString(1, motivo.getMotivo_desligamento().toUpperCase());
			stmt.setInt(2, motivo.getId_motivo());
			stmt.executeUpdate();
			con.commit();
			return true;
		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		} finally {
			try {
				con.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}

	public Boolean excluirMotivo(MotivoDesligamentoBean motivo)
			throws ProjetoException {
		String sql = "delete from hosp.motivo_desligamento where id = ?";
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setLong(1, motivo.getId_motivo());
			stmt.execute();
			con.commit();
			return true;
		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		} finally {
			try {
				con.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}

	public MotivoDesligamentoBean buscaMotivoPorId(Integer i)
			throws ProjetoException {
		
		String sql = "select id, motivo from hosp.motivo_desligamento where id =? order by motivo";
		try {
			con = ConnectionFactory.getConnection();
			ps = con.prepareStatement(sql);
			ps.setInt(1, i);
			ResultSet rs = ps.executeQuery();
			MotivoDesligamentoBean m = new MotivoDesligamentoBean();
			while (rs.next()) {
				m.setId_motivo(rs.getInt("id"));
				m.setMotivo_desligamento(rs.getString("motivo"));

			}

			return m;
		} catch (Exception sqle) {
			sqle.printStackTrace();
			throw new ProjetoException(sqle);

		} finally {
			try {
				con.close();
			} catch (Exception sqlc) {
				sqlc.printStackTrace();
				System.exit(1);
				// TODO: handle exception
			}

		}
	}

}
