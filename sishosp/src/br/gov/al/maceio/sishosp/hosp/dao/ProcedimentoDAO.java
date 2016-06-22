package br.gov.al.maceio.sishosp.hosp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;
import br.gov.al.maceio.sishosp.hosp.model.ProcedimentoBean;

public class ProcedimentoDAO {
	Connection con = null;
	PreparedStatement ps = null;

	public boolean gravarProcedimento(ProcedimentoBean proc)
			throws SQLException {

		String sql = "INSERT INTO hosp.proc (codproc, nome, apac, bpi, auditivo, tipo_exame_auditivo, utiliza_equipamento)"
				+ " VALUES (?, ?, ?, ?, ?, ?, ?);";
		try {
			System.out.println("VAI CADASTRAR Procedimento");
			con = ConnectionFactory.getConnection();
			ps = con.prepareStatement(sql);

			ps.setInt(1, proc.getCodProc());
			ps.setString(2, proc.getNomeProc().toUpperCase());
			ps.setBoolean(3, proc.getApac());
			ps.setBoolean(4, proc.getBpi());
			ps.setBoolean(5, proc.getAuditivo());
			ps.setString(6, proc.getTipoExameAuditivo().toUpperCase());
			ps.setBoolean(7, proc.getUtilizaEquipamento());

			ps.execute();
			con.commit();
			con.close();
			System.out.println("CADASTROU Procedimento");
			return true;
		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		}
	}

	public List<ProcedimentoBean> listarProcedimento() {
		List<ProcedimentoBean> lista = new ArrayList<>();
		String sql = "select id, codproc, nome, apac, bpi, auditivo,"
				+ " tipo_exame_auditivo, utiliza_equipamento from hosp.proc order by codproc";
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stm = con.prepareStatement(sql);
			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				ProcedimentoBean proc = new ProcedimentoBean();
				proc.setIdProc(rs.getInt("id"));
				proc.setCodProc(rs.getInt("codproc"));
				proc.setNomeProc(rs.getString("nome"));
				proc.setApac(rs.getBoolean("apac"));
				proc.setBpi(rs.getBoolean("bpi"));
				proc.setAuditivo(rs.getBoolean("auditivo"));
				proc.setTipoExameAuditivo(rs.getString("tipo_exame_auditivo"));
				proc.setUtilizaEquipamento(rs.getBoolean("utiliza_equipamento"));
				lista.add(proc);
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

	public ProcedimentoBean listarProcedimentoPorId(int id) {

		ProcedimentoBean proc = new ProcedimentoBean();
		String sql = "select id, codproc, nome, apac, bpi, auditivo, tipo_exame_auditivo, utiliza_equipamento from hosp.proc where id = ?";
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stm = con.prepareStatement(sql);
			stm.setInt(1, id);
			ResultSet rs = stm.executeQuery();
			while (rs.next()) {
				proc = new ProcedimentoBean();
				proc.setIdProc(rs.getInt("id"));
				proc.setCodProc(rs.getInt("codproc"));
				proc.setNomeProc(rs.getString("nome"));
				proc.setApac(rs.getBoolean("apac"));
				proc.setBpi(rs.getBoolean("bpi"));
				proc.setAuditivo(rs.getBoolean("auditivo"));
				proc.setTipoExameAuditivo(rs.getString("tipo_exame_auditivo"));
				proc.setUtilizaEquipamento(rs.getBoolean("utiliza_equipamento"));

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
		return proc;
	}

	public boolean alterarProcedimento(ProcedimentoBean proc) {
		String sql = "update hosp.proc set nome = ?, apac = ?, bpi = ?, auditivo = ?, "
				+ " tipo_exame_auditivo = ?, utiliza_equipamento = ?"
				+ " where codproc = ?";
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setString(1, proc.getNomeProc().toUpperCase());
			stmt.setBoolean(2, proc.getApac());
			stmt.setBoolean(3, proc.getBpi());
			stmt.setBoolean(4, proc.getAuditivo());
			stmt.setString(5, proc.getTipoExameAuditivo().toUpperCase());
			stmt.setBoolean(6, proc.getUtilizaEquipamento());
			stmt.setInt(7, proc.getCodProc());
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

	public boolean excluirProcedimento(ProcedimentoBean proc) {
		String sql = "delete from hosp.proc where codproc = ?";
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setLong(1, proc.getCodProc());
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

	public List<ProcedimentoBean> listarProcedimentoBusca(String descricaoBusca,
			Integer tipoBuscar) {

		List<ProcedimentoBean> lista = new ArrayList<>();
		String sql = "select id,id  ||'-'|| nome as nome ,codproc, apac, bpi, auditivo, tipo_exame_auditivo, utiliza_equipamento"
				+ " from hosp.proc ";
		if (tipoBuscar == 1) {
			sql += " where upper(id ||'-'|| nome) LIKE ?";
		}
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stm = con.prepareStatement(sql);
			stm.setString(1, "%" + descricaoBusca.toUpperCase() + "%");
			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				ProcedimentoBean proc = new ProcedimentoBean();
				proc.setIdProc(rs.getInt("id"));
				proc.setNomeProc(rs.getString("nome"));
				proc.setCodProc(rs.getInt("codproc"));
				proc.setApac(rs.getBoolean("apac"));
				proc.setBpi(rs.getBoolean("bpi"));
				proc.setAuditivo(rs.getBoolean("auditivo"));
				proc.setTipoExameAuditivo(rs.getString("tipo_exame_auditivo"));
				proc.setUtilizaEquipamento(rs.getBoolean("utiliza_equipamento"));
				lista.add(proc);
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
}
