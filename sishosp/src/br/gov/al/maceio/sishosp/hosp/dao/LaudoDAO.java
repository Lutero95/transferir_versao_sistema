package br.gov.al.maceio.sishosp.hosp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.gov.al.maceio.sishosp.acl.dao.FuncionarioDAO;
import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;
import br.gov.al.maceio.sishosp.hosp.model.GrupoBean;
import br.gov.al.maceio.sishosp.hosp.model.LaudoBean;
import br.gov.al.maceio.sishosp.hosp.model.ProcedimentoBean;
import br.gov.al.maceio.sishosp.hosp.model.ProgramaBean;

public class LaudoDAO {

	Connection con = null;
	PreparedStatement ps = null;
	private Connection conexao = null;
	private FuncionarioDAO profDao = new FuncionarioDAO();
	private ProgramaDAO progDao = new ProgramaDAO();
	private ProcedimentoDAO procDao = new ProcedimentoDAO();
	private PacienteDAO pacieDao = new PacienteDAO();
	private GrupoDAO grupoDao = new GrupoDAO();
	private FornecedorDAO forneDao = new FornecedorDAO();
	private EquipeDAO equipeDao = new EquipeDAO();
	private CidDAO cidDao = new CidDAO();
	private EquipamentoDAO equipamentoDao = new EquipamentoDAO();

	public Boolean cadastrarLaudo(LaudoBean laudo) throws ProjetoException {
		boolean cadastrou = false;

		/*
		 * PacienteBean user_session = (PacienteBean) FacesContext
		 * .getCurrentInstance().getExternalContext().getSessionMap()
		 * .get("obj_paciente");
		 */

		String sql = "insert into hosp.laudo "
				+ "(codpaciente, id_recurso, data_solicitacao, mes_inicio, ano_inicio, mes_final, ano_final, periodo, codprocedimento_primario, "
				+ "codprocedimento_secundario1, codprocedimento_secundario2, codprocedimento_secundario3, codprocedimento_secundario4, codprocedimento_secundario5 "
				+ "cid1, cid2, cid3, obs ) "
				+ "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		try {
			conexao = ConnectionFactory.getConnection();
			PreparedStatement stmt = conexao.prepareStatement(sql);
			stmt.setLong(1, laudo.getPaciente().getId_paciente());
			stmt.setInt(2, laudo.getRecurso().getIdRecurso());
			stmt.setDate(3, new java.sql.Date(laudo.getData_solicitacao()
					.getTime()));
			stmt.setInt(4, laudo.getMes_inicio());
			stmt.setInt(5, laudo.getAno_inicio());
			stmt.setInt(6, laudo.getMes_final());
			stmt.setInt(7, laudo.getAno_final());
			stmt.setInt(8, laudo.getPeriodo());
			stmt.setInt(9, laudo.getProcedimento_primario().getCodProc());
			if (laudo.getProcedimento_secundario1() != null) {
				if (laudo.getProcedimento_secundario1().getCodProc() != null) {
					stmt.setInt(10, laudo.getProcedimento_secundario1()
							.getCodProc());
				} else {
					stmt.setNull(10, Types.CHAR);
				}
			} else {
				stmt.setNull(10, Types.CHAR);
			}

			if (laudo.getProcedimento_secundario2() != null) {
				if (laudo.getProcedimento_secundario2().getCodProc() != null) {
					stmt.setInt(11, laudo.getProcedimento_secundario2()
							.getCodProc());
				} else {
					stmt.setNull(11, Types.CHAR);
				}
			} else {
				stmt.setNull(11, Types.CHAR);
			}

			if (laudo.getProcedimento_secundario3() != null) {
				if (laudo.getProcedimento_secundario3().getCodProc() != null) {
					stmt.setInt(12, laudo.getProcedimento_secundario3()
							.getCodProc());
				} else {
					stmt.setNull(12, Types.CHAR);
				}
			} else {
				stmt.setNull(12, Types.CHAR);
			}

			if (laudo.getProcedimento_secundario4() != null) {
				if (laudo.getProcedimento_secundario4().getCodProc() != null) {
					stmt.setInt(13, laudo.getProcedimento_secundario4()
							.getCodProc());
				} else {
					stmt.setNull(13, Types.CHAR);
				}
			} else {
				stmt.setNull(13, Types.CHAR);
			}

			if (laudo.getProcedimento_secundario5() != null) {
				if (laudo.getProcedimento_secundario5().getCodProc() != null) {
					stmt.setInt(14, laudo.getProcedimento_secundario5()
							.getCodProc());
				} else {
					stmt.setNull(14, Types.CHAR);
				}
			} else {
				stmt.setNull(14, Types.CHAR);
			}

			if (laudo.getCid1() != null) {
				if (laudo.getCid1().getIdCid() != null) {
					stmt.setInt(15, laudo.getCid1().getIdCid());
				} else {
					stmt.setNull(15, Types.CHAR);
				}
			} else {
				stmt.setNull(15, Types.CHAR);
			}

			if (laudo.getCid2() != null) {
				if (laudo.getCid2().getIdCid() != null) {
					stmt.setInt(16, laudo.getCid2().getIdCid());
				} else {
					stmt.setNull(16, Types.CHAR);
				}
			} else {
				stmt.setNull(16, Types.CHAR);
			}

			if (laudo.getCid3() != null) {
				if (laudo.getCid3().getIdCid() != null) {
					stmt.setInt(17, laudo.getCid3().getIdCid());
				} else {
					stmt.setNull(17, Types.CHAR);
				}
			} else {
				stmt.setNull(17, Types.CHAR);
			}

			if (laudo.getObs() == null) {
				stmt.setNull(18, Types.CHAR);
			} else {
				stmt.setString(18, laudo.getObs().toUpperCase().trim());
			}

			stmt.execute();
			conexao.commit();
			cadastrou = true;
			conexao.close();

			return cadastrou;
		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		}
	}

	public ArrayList<LaudoBean> listaLaudos() throws ProjetoException {

		String sql = "select id_laudo, l.id_recurso, r.descrecurso, l.codpaciente, p.nome, l.data_solicitacao, l.mes_inicio, l.ano_inicio, l.mes_final, l.ano_final, "
				+ " l.periodo, l.codprocedimento_primario, pr.nome as procedimento, l.codprocedimento_secundario1, l.codprocedimento_secundario2, "
				+ " l.codprocedimento_secundario3, l.codprocedimento_secundario4, l.codprocedimento_secundario5, l.cid1, l.cid2, l.cid3, l.obs "
				+ " from hosp.laudo l left join hosp.pacientes p on (p.id_paciente = l.codpaciente) "
				+ " left join hosp.proc pr on (pr.codproc = l.codprocedimento_primario) "
				+ " left join hosp.recurso r on (l.id_recurso = r.id) order by id_laudo";

		ArrayList<LaudoBean> lista = new ArrayList();

		try {
			conexao = ConnectionFactory.getConnection();
			PreparedStatement stm = conexao.prepareStatement(sql);
			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				LaudoBean l = new LaudoBean();

				l.setId(rs.getInt("id_laudo"));
				l.getPaciente().setId_paciente(rs.getInt("codpaciente"));
				l.getPaciente().setNome(rs.getString("nome"));
				l.getRecurso().setIdRecurso(rs.getInt("id_recurso"));
				l.getRecurso().setDescRecurso(rs.getString("descrecurso"));
				l.setData_solicitacao(rs.getDate("data_solicitacao"));
				l.setMes_inicio(rs.getInt("mes_inicio"));
				l.setAno_inicio(rs.getInt("ano_inicio"));
				l.setMes_final(rs.getInt("mes_final"));
				l.setAno_final(rs.getInt("ano_final"));
				l.setPeriodo(rs.getInt("periodo"));
				l.getProcedimento_primario().setCodProc(rs.getInt("codprocedimento_primario"));
				l.getProcedimento_primario().setNomeProc(rs.getString("procedimento"));
				l.getProcedimento_secundario1().setCodProc(rs.getInt("codprocedimento_secundario1"));
				l.getProcedimento_secundario2().setCodProc(rs.getInt("codprocedimento_secundario2"));
				l.getProcedimento_secundario3().setCodProc(rs.getInt("codprocedimento_secundario3"));
				l.getProcedimento_secundario4().setCodProc(rs.getInt("codprocedimento_secundario4"));
				l.getProcedimento_secundario5().setCodProc(rs.getInt("codprocedimento_secundario5"));
				l.getCid1().setIdCid(rs.getInt("cid1"));
				l.getCid2().setIdCid(rs.getInt("cid2"));
				l.getCid3().setIdCid(rs.getInt("cid3"));
				l.setObs(rs.getString("obs"));

				lista.add(l);
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
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

}
