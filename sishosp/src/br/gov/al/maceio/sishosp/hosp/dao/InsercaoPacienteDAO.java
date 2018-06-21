package br.gov.al.maceio.sishosp.hosp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import br.gov.al.maceio.sishosp.acl.dao.FuncionarioDAO;
import br.gov.al.maceio.sishosp.acl.model.FuncionarioBean;
import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;
import br.gov.al.maceio.sishosp.hosp.model.BloqueioBean;
import br.gov.al.maceio.sishosp.hosp.model.InsercaoPacienteBean;

public class InsercaoPacienteDAO {
	Connection con = null;
	PreparedStatement ps = null;
	FuncionarioDAO pDao = new FuncionarioDAO();

	// INSERÇÃO INÍCIO

	public ArrayList<InsercaoPacienteBean> listarLaudosVigentes()
			throws ProjetoException {

		ArrayList<InsercaoPacienteBean> lista = new ArrayList<>();

		String sql = "select nome, cns, id_laudo from ( "
				+ " select l.id_laudo, l.codpaciente, p.nome, p.cns, l.recuso, l.data_solicitacao, l.mes_inicio, l.ano_inicio, l.mes_final, l.ano_final, l.periodo, "
				+ " l.codprocedimento_primario, pr.nome as procedimento, l.cid1, ci.desccid,  "
				+ " to_date(ano_inicio||'-'||'0'||''||mes_inicio||'-'||'01', 'YYYY-MM-DD') as datainicio,  "
				+ " (SELECT * FROM fn_GetLastDayOfMonth(to_date(ano_final||'-'||'0'||''||mes_final||'-'||'01', 'YYYY-MM-DD'))) as datafinal "
				+ " from hosp.laudo l "
				+ " left join hosp.pacientes p on (l.codpaciente = p.id_paciente) "
				+ " left join hosp.proc pr on (l.codprocedimento_primario = pr.id) "
				+ " left join hosp.cid ci on (l.cid1 = cast(ci.cod as integer)) "
				+ " where current_date >= to_date(ano_inicio||'-'||'0'||''||mes_inicio||'-'||'01', 'YYYY-MM-DD') "
				+ " and current_date <= (SELECT * FROM fn_GetLastDayOfMonth(to_date(ano_final||'-'||'0'||''||mes_final||'-'||'01', 'YYYY-MM-DD'))) "
				// + " and l.codpaciente = 13 "
				+ " ) a";
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stm = con.prepareStatement(sql);
			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				InsercaoPacienteBean insercao = new InsercaoPacienteBean();
				insercao.getLaudo().setId(rs.getInt("id_laudo"));
				insercao.getLaudo().getPaciente().setNome(rs.getString("nome"));
				insercao.getLaudo().getPaciente().setCns(rs.getString("cns"));

				lista.add(insercao);
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

	public InsercaoPacienteBean carregarLaudoPaciente(int id)
			throws ProjetoException {

		InsercaoPacienteBean insercao = new InsercaoPacienteBean();

		String sql = "select l.id_laudo, l.codpaciente, p.nome, p.cns, l.recuso, l.data_solicitacao, l.mes_inicio, l.ano_inicio, "
				+ " l.mes_final, l.ano_final, l.periodo, "
				+ " l.codprocedimento_primario, pr.nome as procedimento, l.cid1, ci.desccid, "
				+ " to_date(ano_inicio||'-'||'0'||''||mes_inicio||'-'||'01', 'YYYY-MM-DD') as datainicio,  "
				+ " (SELECT * FROM fn_GetLastDayOfMonth(to_date(ano_final||'-'||'0'||''||mes_final||'-'||'01', 'YYYY-MM-DD'))) as datafinal "
				+ " from hosp.laudo l "
				+ " left join hosp.pacientes p on (l.codpaciente = p.id_paciente) "
				+ " left join hosp.proc pr on (l.codprocedimento_primario = pr.id) "
				+ " left join hosp.cid ci on (l.cid1 = cast(ci.cod as integer)) "
				+ " where current_date >= to_date(ano_inicio||'-'||'0'||''||mes_inicio||'-'||'01', 'YYYY-MM-DD') "
				+ " and current_date <= (SELECT * FROM fn_GetLastDayOfMonth(to_date(ano_final||'-'||'0'||''||mes_final||'-'||'01', 'YYYY-MM-DD'))) "
				+ " and l.id_laudo = ?";
		try {
			con = ConnectionFactory.getConnection();

			PreparedStatement stm = con.prepareStatement(sql);

			stm.setInt(1, id);

			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				insercao = new InsercaoPacienteBean();
				insercao.getLaudo().setId(rs.getInt("id_laudo"));
				insercao.getLaudo().getPaciente()
						.setId_paciente(rs.getInt("codpaciente"));
				insercao.getLaudo().getPaciente().setNome(rs.getString("nome"));
				insercao.getLaudo().getPaciente().setCns(rs.getString("cns"));
				insercao.getLaudo().setRecuso(rs.getString("recuso"));
				insercao.getLaudo().setData_solicitacao(
						rs.getDate("data_solicitacao"));
				insercao.getLaudo().setMes_inicio(rs.getInt("mes_inicio"));
				insercao.getLaudo().setAno_inicio(rs.getInt("ano_inicio"));
				insercao.getLaudo().setMes_final(rs.getInt("mes_final"));
				insercao.getLaudo().setAno_final(rs.getInt("ano_final"));
				insercao.getLaudo().setPeriodo(rs.getInt("periodo"));
				insercao.getLaudo().getProcedimento_primario()
						.setIdProc(rs.getInt("codprocedimento_primario"));
				insercao.getLaudo().getProcedimento_primario()
						.setNomeProc(rs.getString("procedimento"));
				insercao.getLaudo().getCid1().setIdCid(rs.getInt("cid1"));
				insercao.getLaudo().getCid1()
						.setDescCid(rs.getString("desccid"));
				insercao.getLaudo().setVigencia_inicial(
						rs.getDate("datainicio"));
				insercao.getLaudo().setVigencia_final(rs.getDate("datafinal"));
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
		return insercao;
	}

	// INSERÇÃO FINAL

	public boolean gravarBloqueio(BloqueioBean bloqueio) throws SQLException,
			ProjetoException {

		Calendar calendarData = Calendar.getInstance();
		boolean condicao = true;
		String sql = "insert into hosp.bloqueio_agenda (codmedico, dataagenda, turno, descricao, codempresa) "
				+ " values (?, ?, ?, ?, ?);";
		try {
			con = ConnectionFactory.getConnection();
			ps = con.prepareStatement(sql);
			Date dataInicio = bloqueio.getDataInicio();
			Date dataFim = bloqueio.getDataFim();
			do {
				if (dataInicio.after(dataFim) == false) {
					ps.setLong(1, bloqueio.getProf().getId());
					ps.setDate(2, new java.sql.Date(dataInicio.getTime()));
					ps.setString(3, bloqueio.getTurno().toUpperCase());
					ps.setString(4, bloqueio.getDescBloqueio().toUpperCase());
					ps.setInt(5, 0); // COD EMPRESA ?
					ps.execute();
					con.commit();
					condicao = true;
					calendarData.setTime(dataInicio);
					calendarData.add(Calendar.DATE, 1);
					dataInicio = calendarData.getTime();
				} else {
					condicao = false;
				}
			} while (condicao);
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

	public List<BloqueioBean> listarBloqueioPorProfissional(FuncionarioBean prof)
			throws ProjetoException {
		List<BloqueioBean> lista = new ArrayList<>();
		String sql = "select id_bloqueioagenda, codmedico, dataagenda, turno, descricao, codempresa "
				+ "from hosp.bloqueio_agenda where codmedico = ? order by id_bloqueioagenda";
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stm = con.prepareStatement(sql);
			stm.setLong(1, prof.getId());
			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				BloqueioBean bloqueio = new BloqueioBean();
				bloqueio.setIdBloqueio(rs.getInt("id_bloqueioagenda"));
				bloqueio.setProf(pDao.buscarProfissionalPorId(rs
						.getInt("codmedico")));
				bloqueio.setDataInicio(rs.getDate("dataagenda"));
				bloqueio.setTurno(rs.getString("turno"));
				bloqueio.setDescBloqueio(rs.getString("descricao"));
				bloqueio.setCodEmpresa(rs.getInt("codempresa"));

				lista.add(bloqueio);
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

	public Boolean alterarBloqueio(BloqueioBean bloqueio)
			throws ProjetoException {
		String sql = "update hosp.bloqueio_agenda set codmedico = ?, dataagenda = ?,turno = ?, descricao = ?"
				+ " where id_bloqueioagenda = ?";
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(sql);

			stmt.setLong(1, bloqueio.getProf().getId());
			stmt.setDate(2, new java.sql.Date(bloqueio.getDataInicio()
					.getTime()));
			stmt.setString(3, bloqueio.getTurno().toUpperCase());
			stmt.setString(4, bloqueio.getDescBloqueio().toUpperCase());
			stmt.setInt(5, bloqueio.getIdBloqueio());
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

	public Boolean excluirBloqueio(BloqueioBean bloqueio)
			throws ProjetoException {
		String sql = "delete from hosp.bloqueio_agenda where id_bloqueioagenda = ?";
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setLong(1, bloqueio.getIdBloqueio());
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

	public List<BloqueioBean> verificarBloqueioProfissional(
			FuncionarioBean prof, Date dataAtendimento, String turno)
			throws ProjetoException {
		List<BloqueioBean> lista = new ArrayList<>();
		String sql = "select id_bloqueioagenda, codmedico, dataagenda, turno, descricao, codempresa "
				+ " from hosp.bloqueio_agenda "
				+ " where codmedico = ? and  dataagenda = ? and turno = ? order by id_bloqueioagenda";
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stm = con.prepareStatement(sql);
			stm.setLong(1, prof.getId());
			stm.setDate(2, new java.sql.Date(dataAtendimento.getTime()));
			stm.setString(3, turno.toUpperCase());
			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				BloqueioBean bloqueio = new BloqueioBean();
				bloqueio.setIdBloqueio(rs.getInt("id_bloqueioagenda"));
				bloqueio.setProf(pDao.buscarProfissionalPorId(rs
						.getInt("codmedico")));
				bloqueio.setDataInicio(rs.getDate("dataagenda"));
				bloqueio.setTurno(rs.getString("turno"));
				bloqueio.setDescBloqueio(rs.getString("descricao"));
				bloqueio.setCodEmpresa(rs.getInt("codempresa"));

				lista.add(bloqueio);
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
