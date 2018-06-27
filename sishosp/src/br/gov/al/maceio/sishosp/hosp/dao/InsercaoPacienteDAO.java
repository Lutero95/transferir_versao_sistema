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
import br.gov.al.maceio.sishosp.hosp.model.AgendaBean;
import br.gov.al.maceio.sishosp.hosp.model.BloqueioBean;
import br.gov.al.maceio.sishosp.hosp.model.InsercaoPacienteBean;

public class InsercaoPacienteDAO {
	Connection con = null;
	PreparedStatement ps = null;

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

	public boolean gravarInsercaoEquipe(InsercaoPacienteBean insercao,
			List<FuncionarioBean> lista) throws ProjetoException {

		String sql = "insert into hosp.paciente_instituicao (codprograma, codgrupo, codpaciente, codequipe, status, codlaudo, observacao) "
				+ " values (?, ?, ?, ?, ?, ?, ?) RETURNING id;";
		try {
			con = ConnectionFactory.getConnection();
			ps = con.prepareStatement(sql);
			ps.setInt(1, insercao.getPrograma().getIdPrograma());
			ps.setInt(2, insercao.getGrupo().getIdGrupo());
			ps.setInt(3, insercao.getLaudo().getPaciente().getId_paciente());
			ps.setInt(4, insercao.getEquipe().getCodEquipe());
			ps.setString(5, "A");
			ps.setInt(6, insercao.getLaudo().getId());
			ps.setString(7, insercao.getObservacao());

			ResultSet rs = ps.executeQuery();
			int id = 0;
			if (rs.next()) {
				id = rs.getInt("id");
			}

			String sql2 = "INSERT INTO hosp.profissional_dia_atendimento (id_paciente_instituicao, id_profissional, dia_semana) VALUES  (?, ?, ?)";
			ps = con.prepareStatement(sql2);

			for (int i = 0; i < lista.size(); i++) {
				ps.setLong(1, id);
				ps.setLong(2, lista.get(i).getId());
				for (int j = 0; j < insercao.getDiasSemana().size(); j++) {
					ps.setInt(3,
							Integer.parseInt(insercao.getDiasSemana().get(j)));
					ps.executeUpdate();
				}
			}
			con.commit();

			return true;
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
	}

	public boolean gravarInsercaoProfissional(InsercaoPacienteBean insercao,
			List<FuncionarioBean> lista) throws ProjetoException {

		String sql = "insert into hosp.paciente_instituicao (codprofissional, status, codlaudo, observacao, data_solicitacao) "
				+ " values (?, ?, ?, ?, ?) RETURNING id;";
		try {
			con = ConnectionFactory.getConnection();
			ps = con.prepareStatement(sql);
			ps.setLong(1, insercao.getFuncionario().getId());
			ps.setString(2, "A");
			ps.setInt(3, insercao.getLaudo().getId());
			ps.setString(4, insercao.getObservacao());
			ps.setDate(5, new java.sql.Date(insercao.getData_solicitacao()
					.getTime()));

			ResultSet rs = ps.executeQuery();
			int id = 0;
			if (rs.next()) {
				id = rs.getInt("id");
			}

			String sql2 = "INSERT INTO hosp.profissional_dia_atendimento (id_paciente_instituicao, id_profissional, dia_semana) VALUES  (?, ?, ?)";
			ps = con.prepareStatement(sql2);

			for (int i = 0; i < insercao.getDiasSemana().size(); i++) {
				ps.setLong(1, id);
				ps.setLong(2, insercao.getFuncionario().getId());
				ps.setInt(3, Integer.parseInt(insercao.getDiasSemana().get(i)));
				ps.executeUpdate();

			}
			con.commit();

			return true;
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
	}
}
