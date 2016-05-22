package br.gov.al.maceio.sishosp.hosp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;
import br.gov.al.maceio.sishosp.hosp.model.ProfissionalBean;

public class ProfissionalDAO {

	private Connection con = null;
	private PreparedStatement ps = null;
	private EspecialidadeDAO espDao = new EspecialidadeDAO();
	private ProgramaDAO progDao = new ProgramaDAO();
	private CboDAO cDao = new CboDAO();
	private ProcedimentoDAO procDao = new ProcedimentoDAO();

	public boolean gravarProfissional(ProfissionalBean prof)
			throws SQLException {
		
;
		String sql = "insert into hosp.medicos (descmedico, codprograma, codespecialidade, cns, ativo, codcbo,"
				+ " codprocedimentopadrao, codprocedimentopadrao2, codempresa) values (?, ?, ?, ?, ?, ?, ?, ?, ?);";
		try {
			System.out.println("VAI CADASTRAR PROFISSIONAL");
			con = ConnectionFactory.getConnection();
			ps = con.prepareStatement(sql);
			ps.setString(1, prof.getDescricaoProf().toUpperCase());
			ps.setInt(2, prof.getPrograma().getIdPrograma());
			ps.setInt(3, prof.getEspecialidade().getCodEspecialidade());
			ps.setString(4, prof.getCns().toUpperCase());
			ps.setBoolean(5, prof.isAtivo());
			ps.setInt(6, prof.getCbo().getCodCbo());
			ps.setInt(7, prof.getProc1().getCodProc());
			ps.setInt(8, prof.getProc2().getCodProc());
			ps.setInt(9, 0);//COD EMPRESA ??
			ps.execute();
			con.commit();
			System.out.println("CADASTROU PROFISSIONAL");
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

	/*
	 * public List<TipoAtendimentoBean> listarTipoAtPorGrupo(){
	 * List<TipoAtendimentoBean> lista = new ArrayList<>(); String sql =
	 * "select t.id, t.codgrupo, t.desctipoatendimento, t.primeiroatendimento, t.codempresa, t.equipe_programa"
	 * +" from hosp.grupo g, hosp.tipoatendimento t"
	 * +" where g.id_grupo = ? and g.id_grupo = t.codgrupo"; try { con =
	 * ConnectionFactory.getConnection(); PreparedStatement stm =
	 * con.prepareStatement(sql); stm.setInt(1, codGrupo); ResultSet rs =
	 * stm.executeQuery();
	 * 
	 * while (rs.next()) { TipoAtendimentoBean tipo = new TipoAtendimentoBean();
	 * tipo.setIdTipo(rs.getInt(1)); tipo.setIdGrupo(rs.getInt(2));
	 * tipo.setDescTipoAt(rs.getString(3));
	 * tipo.setPrimeiroAt(rs.getBoolean(4)); tipo.setCodEmpresa(rs.getInt(5));
	 * tipo.setEquipe(rs.getBoolean(6));
	 * 
	 * lista.add(tipo); } } catch (SQLException ex) { throw new
	 * RuntimeException(ex); } finally { try { con.close(); } catch (Exception
	 * ex) { ex.printStackTrace(); System.exit(1); } } return lista; }
	 */

	public List<ProfissionalBean> listarProfissionalBusca(
			String descricaoBusca, Integer tipoBuscar) {
		
		return null;
	}

	public List<ProfissionalBean> listarProfissional() {
		List<ProfissionalBean> listaProf = new ArrayList<ProfissionalBean>();
		String sql = "select id_medico, descmedico, codprograma, codespecialidade, cns, ativo, codcbo, codprocedimentopadrao, codprocedimentopadrao2, codempresa"
				+ " from hosp.medicos order by id_medico";
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stm = con.prepareStatement(sql);
			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				ProfissionalBean prof = new ProfissionalBean();
				prof.setIdProfissional(rs.getInt("id_medico"));
				prof.setDescricaoProf(rs.getString("descmedico"));
				prof.setPrograma(progDao.listarProgramaPorId(rs.getInt("codprograma")));
				prof.setEspecialidade(espDao.listarEspecialidadePorId(rs.getInt("codespecialidade")));
				prof.setCns(rs.getString("cns"));
				prof.setAtivo(rs.getBoolean("ativo"));
				prof.setCbo(cDao.listarCboPorId(rs.getInt("codcbo")));
				prof.setProc1(procDao.listarProcedimentoPorId(rs.getInt("codprocedimentopadrao")));
				prof.setProc1(procDao.listarProcedimentoPorId(rs.getInt("codprocedimentopadrao2")));
				//prof.setIdProfissional(rs.getInt("codempresa"));// COD EMPRESA ??

				listaProf.add(prof);
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
		
		return listaProf;
	}

	public boolean excluirProfissional(ProfissionalBean profissional) {
		return false;
	}

	public boolean alterarProfissional(ProfissionalBean profissional) {
		String sql = "update hosp.medicos set descmedico = ?, codprograma = ?, codespecialidade = ?, cns = ?, ativo = ?"
				+ " codcbo = ?, codprocedimentopadrao = ?, codprocedimentopadrao2 = ?, codempresa = ? "
				+ " where id_medico = ?";
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(sql);
			
			ps.setString(1, profissional.getDescricaoProf().toUpperCase());
			ps.setInt(2, profissional.getPrograma().getIdPrograma());
			ps.setInt(3, profissional.getEspecialidade().getCodEspecialidade());
			ps.setString(4, profissional.getCns().toUpperCase());
			ps.setBoolean(5, profissional.isAtivo());
			ps.setInt(6, profissional.getCbo().getCodCbo());
			ps.setInt(7, profissional.getProc1().getCodProc());
			ps.setInt(8, profissional.getProc2().getCodProc());
			ps.setInt(9, 0);//COD EMPRESA ?
			ps.setInt(10, profissional.getIdProfissional());
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

}
