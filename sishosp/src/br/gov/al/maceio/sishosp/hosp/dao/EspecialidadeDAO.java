package br.gov.al.maceio.sishosp.hosp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;
import br.gov.al.maceio.sishosp.hosp.model.EspecialidadeBean;

public class EspecialidadeDAO {

	Connection con = null;
	PreparedStatement ps = null;

	public boolean gravarEspecialidade(EspecialidadeBean esp)
			throws SQLException, ProjetoException {

		String sql = "insert into hosp.especialidade (descespecialidade) values (?);";
		try {
			con = ConnectionFactory.getConnection();
			ps = con.prepareStatement(sql);
			ps.setString(1, esp.getDescEspecialidade().toUpperCase());
			ps.execute();
			con.commit();
			con.close();
			return true;
		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		}
	}

	public List<EspecialidadeBean> listarEspecialidades()
			throws ProjetoException {
		List<EspecialidadeBean> lista = new ArrayList<>();
		String sql = "select id_especialidade, descespecialidade, codempresa from hosp.especialidade order by descespecialidade";
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stm = con.prepareStatement(sql);
			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				EspecialidadeBean esp = new EspecialidadeBean();
				esp.setCodEspecialidade(rs.getInt("id_especialidade"));
				esp.setDescEspecialidade(rs.getString("descespecialidade"));
				esp.setCodEmpresa(rs.getInt("codempresa"));

				lista.add(esp);
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

	public List<EspecialidadeBean> listarEspecialidadesBusca(String descricao,
			Integer tipo) throws ProjetoException {
		List<EspecialidadeBean> lista = new ArrayList<>();
		String sql = "select id_especialidade, descespecialidade, codempresa from hosp.especialidade ";
		if (tipo == 1) {
			sql += " where descespecialidade LIKE ?";
		}
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stm = con.prepareStatement(sql);
			stm.setString(1, "%" + descricao.toUpperCase() + "%");
			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				EspecialidadeBean esp = new EspecialidadeBean();
				esp.setCodEspecialidade(rs.getInt("id_especialidade"));
				esp.setDescEspecialidade(rs.getString("descespecialidade"));
				esp.setCodEmpresa(rs.getInt("codempresa"));

				lista.add(esp);
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

	public boolean alterarEspecialidade(EspecialidadeBean espec)
			throws ProjetoException {
		String sql = "update hosp.especialidade set descespecialidade = ? where id_especialidade = ?";
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setString(1, espec.getDescEspecialidade().toUpperCase());
			stmt.setInt(2, espec.getCodEspecialidade());
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

	public boolean excluirEspecialidade(EspecialidadeBean espec)
			throws ProjetoException {
		String sql = "delete from hosp.especialidade where id_especialidade = ?";
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setLong(1, espec.getCodEspecialidade());
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

	public EspecialidadeBean listarEspecialidadePorId(int id)
			throws ProjetoException {

		EspecialidadeBean esp = new EspecialidadeBean();
		String sql = "select id_especialidade, descespecialidade, codempresa from hosp.especialidade where id_especialidade = ?";
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stm = con.prepareStatement(sql);
			stm.setInt(1, id);
			ResultSet rs = stm.executeQuery();
			while (rs.next()) {
				esp = new EspecialidadeBean();
				esp.setCodEspecialidade(rs.getInt("id_especialidade"));
				esp.setDescEspecialidade(rs.getString("descespecialidade"));
				// esp.setCodEmpresa(rs.getInt("codempresa"));// COD EMPRESA ??
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
		return esp;
	}

}
