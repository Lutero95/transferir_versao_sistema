package br.gov.al.maceio.sishosp.hosp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;
import br.gov.al.maceio.sishosp.hosp.model.ProcedimentoBean;

public class ProcedimentoDAO {
	Connection con = null;
	PreparedStatement ps = null;

	public boolean gravarProcedimento(ProcedimentoBean proc)
			throws SQLException, ProjetoException {

		String sql = "INSERT INTO hosp.proc (codproc, nome, auditivo, tipo_exame_auditivo, utiliza_equipamento, gera_laudo_digita, validade_laudo)"
				+ " VALUES (?, ?, ?, ?, ?, ?, ?);";
		try {
			con = ConnectionFactory.getConnection();
			ps = con.prepareStatement(sql);

			ps.setInt(1, proc.getCodProc());
			ps.setString(2, proc.getNomeProc().toUpperCase());
			ps.setBoolean(3, proc.getAuditivo());
			ps.setString(4, proc.getTipoExameAuditivo().toUpperCase());
			ps.setBoolean(5, proc.getUtilizaEquipamento());
			if (proc.isGera_laudo_digita() == false) {
				ps.setNull(6, Types.BOOLEAN);
			} else {
				ps.setBoolean(6, proc.isGera_laudo_digita());
			}
			if (proc.isGera_laudo_digita() == false) {
				ps.setNull(7, Types.INTEGER);
			} else {
				ps.setInt(7, proc.getValidade_laudo());
			}

			ps.execute();
			con.commit();
			con.close();
			return true;
		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		}
	}

	public List<ProcedimentoBean> listarProcedimento() throws ProjetoException {
		List<ProcedimentoBean> lista = new ArrayList<>();
		String sql = "select id, codproc, nome, auditivo, tipo_exame_auditivo, utiliza_equipamento, gera_laudo_digita, validade_laudo "
				+ " from hosp.proc order by nome";
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stm = con.prepareStatement(sql);
			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				ProcedimentoBean proc = new ProcedimentoBean();
				proc.setIdProc(rs.getInt("id"));
				proc.setCodProc(rs.getInt("codproc"));
				proc.setNomeProc(rs.getString("nome"));
				proc.setAuditivo(rs.getBoolean("auditivo"));
				proc.setTipoExameAuditivo(rs.getString("tipo_exame_auditivo"));
				proc.setUtilizaEquipamento(rs.getBoolean("utiliza_equipamento"));
				proc.setGera_laudo_digita(rs.getBoolean("gera_laudo_digita"));
				proc.setValidade_laudo(rs.getInt("validade_laudo"));
				lista.add(proc);
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
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

	public ProcedimentoBean listarProcedimentoPorId(int id)
			throws ProjetoException {
		ProcedimentoBean proc = new ProcedimentoBean();
		String sql = "select id, codproc, nome, auditivo, tipo_exame_auditivo, utiliza_equipamento, gera_laudo_digita, validade_laudo "
				+ "from hosp.proc where id = ? order by nome";
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
				proc.setAuditivo(rs.getBoolean("auditivo"));
				proc.setTipoExameAuditivo(rs.getString("tipo_exame_auditivo"));
				proc.setUtilizaEquipamento(rs.getBoolean("utiliza_equipamento"));
				proc.setGera_laudo_digita(rs.getBoolean("gera_laudo_digita"));
				proc.setValidade_laudo(rs.getInt("validade_laudo"));
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

	public boolean alterarProcedimento(ProcedimentoBean proc)
			throws ProjetoException {
		String sql = "update hosp.proc set nome = ?, apac = ?, bpi = ?, auditivo = ?, "
				+ " tipo_exame_auditivo = ?, utiliza_equipamento = ?, gera_laudo_digita = ?, validade_laudo = ?, codproc = ? "
				+ " where id = ?";
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setString(1, proc.getNomeProc().toUpperCase());
			stmt.setBoolean(2, proc.getAuditivo());
			stmt.setString(3, proc.getTipoExameAuditivo().toUpperCase());
			stmt.setBoolean(4, proc.getUtilizaEquipamento());
			if (proc.isGera_laudo_digita() == false) {
				stmt.setNull(5, Types.BOOLEAN);
			} else {
				stmt.setBoolean(5, proc.isGera_laudo_digita());
			}
			if (proc.isGera_laudo_digita() == false) {
				stmt.setNull(6, Types.INTEGER);
			} else {
				stmt.setInt(6, proc.getValidade_laudo());
			}
			stmt.setInt(7, proc.getCodProc());
			stmt.setInt(8, proc.getIdProc());
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

	public boolean excluirProcedimento(ProcedimentoBean proc)
			throws ProjetoException {
		String sql = "delete from hosp.proc where id = ?";
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setLong(1, proc.getIdProc());
			stmt.execute();
			con.commit();
			return true;
		} catch (SQLException ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		} finally {
			try {
				con.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}

	public List<ProcedimentoBean> listarProcedimentoBusca(
			String descricaoBusca, Integer tipoBuscar) throws ProjetoException {

		List<ProcedimentoBean> lista = new ArrayList<>();
		String sql = "select id,codproc  ||' - '|| nome as nome ,codproc, auditivo, tipo_exame_auditivo, utiliza_equipamento, gera_laudo_digita, validade_laudo "
				+ "from hosp.proc ";
		if (tipoBuscar == 1) {
			sql += " where upper(codproc ||' - '|| nome) LIKE ? order by nome";
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
				proc.setAuditivo(rs.getBoolean("auditivo"));
				proc.setTipoExameAuditivo(rs.getString("tipo_exame_auditivo"));
				proc.setUtilizaEquipamento(rs.getBoolean("utiliza_equipamento"));
				proc.setGera_laudo_digita(rs.getBoolean("gera_laudo_digita"));
				proc.setValidade_laudo(rs.getInt("validade_laudo"));
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
