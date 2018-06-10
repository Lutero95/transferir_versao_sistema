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

public class GerenciarPacienteDAO {

	Connection con = null;
	PreparedStatement ps = null;
	private Connection conexao = null;

	public Boolean cadastrarLaudo(LaudoBean laudo) throws ProjetoException {
		boolean cadastrou = false;

		/*
		 * PacienteBean user_session = (PacienteBean) FacesContext
		 * .getCurrentInstance().getExternalContext().getSessionMap()
		 * .get("obj_paciente");
		 */

		String sql = "insert into hosp.laudo "
				+ "(codpaciente, recuso, data_solicitacao, mes_inicio, ano_inicio, mes_final, ano_final, periodo, codprocedimento_primario, "
				+ "codprocedimento_secundario1, codprocedimento_secundario2, codprocedimento_secundario3, codprocedimento_secundario4, codprocedimento_secundario5 "
				+ "cid1, cid2, cid3, obs ) "
				+ "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		try {
			conexao = ConnectionFactory.getConnection();
			PreparedStatement stmt = conexao.prepareStatement(sql);
			stmt.setLong(1, laudo.getPaciente().getId_paciente());
			stmt.setString(2, laudo.getRecuso());
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

	public Boolean alterarLaudo(LaudoBean laudo) throws ProjetoException {
		boolean alterou = false;
		String sql = "update hosp.apac set codpaciente = ?, codprograma = ?, codgrupo = ?, codequipe = ?, codmedico = ?, "
				+ "codproc = ?, dtasolicitacao = ?, recurso = ?, apac = ?, unidade = ?, situacao = ?, dtautorizacao = ?, "
				+ "cid10_1 = ?, cid10_2 = ?, cid10_3 = ?, codfornecedor = ?, valor = ?, nota = ?, qtd = ?, codequipamento = ?, obs = ?, (select coalesce(gera_laudo_digita, false) laudo from hosp.proc where proc.codproc=?) where id_apac = ?";
		try {
			conexao = ConnectionFactory.getConnection();
			PreparedStatement stmt = conexao.prepareStatement(sql);
			stmt.setLong(1, laudo.getPaciente().getId_paciente());
			stmt.setInt(2, laudo.getPrograma().getIdPrograma());
			stmt.setInt(3, laudo.getGrupo().getIdGrupo());
			stmt.setInt(4, laudo.getEquipe().getCodEquipe());
			stmt.setLong(5, laudo.getProfissional().getId());
			stmt.setInt(6, laudo.getProcedimento().getIdProc());
			// stmt.setString(6,
			// laudo.getPaciente().getCns().toUpperCase().trim());
			// stmt.setString(7,
			// laudo.getPaciente().getCpf().toUpperCase().trim());
			stmt.setDate(7, new java.sql.Date(laudo.getDtasolicitacao()
					.getTime()));
			stmt.setString(8, laudo.getRecurso());
			stmt.setString(9, laudo.getApac().toUpperCase().trim());
			stmt.setString(10, laudo.getUnidade().toUpperCase().trim());
			stmt.setString(11, laudo.getSituacao().toUpperCase().trim());
			stmt.setDate(12, new java.sql.Date(laudo.getDtautorizacao()
					.getTime()));
			// stmt.setDate(12, new
			// java.sql.Date(laudo.getDtachegada().getTime()));
			if (laudo.getCid10_1() == null) {
				stmt.setNull(13, Types.CHAR);
			} else {
				stmt.setString(13, laudo.getCid10_1().toUpperCase().trim());
			}
			if (laudo.getCid10_2() == null) {
				stmt.setNull(14, Types.CHAR);
			} else {
				stmt.setString(14, laudo.getCid10_2().toUpperCase().trim());
			}
			if (laudo.getCid10_3() == null) {
				stmt.setNull(15, Types.CHAR);
			} else {
				stmt.setString(15, laudo.getCid10_3().toUpperCase().trim());
			}
			if (laudo.getFornecedor().getIdFornecedor() == null) {
				stmt.setNull(16, Types.INTEGER);
			} else {
				stmt.setInt(16, laudo.getFornecedor().getIdFornecedor());
			}
			if (laudo.getFornecedor().getValor() == null) {
				stmt.setNull(17, Types.DOUBLE);
			} else {
				stmt.setDouble(17, laudo.getFornecedor().getValor());
			}
			if (laudo.getNota() == null) {
				stmt.setNull(18, Types.CHAR);
			} else {
				stmt.setString(18, laudo.getNota().toUpperCase().trim());
			}
			if (laudo.getQtd() == null) {
				stmt.setNull(19, Types.INTEGER);
			} else {
				stmt.setInt(19, laudo.getQtd());
			}
			if (laudo.getEquipamento().getId_equipamento() == null) {
				stmt.setNull(20, Types.INTEGER);
			} else {
				stmt.setInt(20, laudo.getEquipamento().getId_equipamento());
			}
			if (laudo.getObs() == null) {
				stmt.setNull(21, Types.CHAR);
			} else {
				stmt.setString(21, laudo.getObs().toUpperCase().trim());
			}
			stmt.setInt(22, laudo.getProcedimento().getIdProc());
			stmt.setInt(23, laudo.getId_apac());

			stmt.executeUpdate();
			conexao.commit();

			alterou = true;

			return alterou;
		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		} finally {
			try {
				conexao.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}

	public Boolean excluirLaudo(LaudoBean laudo) throws ProjetoException {
		boolean excluir = false;
		String sql = "delete from hosp.apac where id_apac = ?";
		try {
			conexao = ConnectionFactory.getConnection();
			PreparedStatement stmt = conexao.prepareStatement(sql);
			stmt.setInt(1, laudo.getId_apac());
			stmt.executeUpdate();

			conexao.commit();

			excluir = true;

			return excluir;
		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		} finally {
			try {
				conexao.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}

	public Boolean cadastrarLaudoDigita(LaudoBean laudo)
			throws ProjetoException {
		boolean cadastrou = false;

		/*
		 * PacienteBean user_session = (PacienteBean) FacesContext
		 * .getCurrentInstance().getExternalContext().getSessionMap()
		 * .get("obj_paciente");
		 */

		String sql = "insert into hosp.pacienteslaudo (id_paciente, codmedico, codprocedimento, "
				+ "datasolicitacao, periodofim) values (?, ?, ?, ?, ?)";
		// String sql =
		// "insert into hosp.escola (descescola) values ((select max(cod) +1 from hosp.escola where codempresa=1), ?";

		try {
			conexao = ConnectionFactory.getConnection();
			PreparedStatement stmt = conexao.prepareStatement(sql);
			stmt.setLong(1, laudo.getPaciente().getId_paciente());
			stmt.setLong(2, laudo.getProfissional().getId());
			stmt.setInt(3, laudo.getProcedimento().getIdProc());
			// stmt.setString(6,
			// laudo.getPaciente().getCns().toUpperCase().trim());
			// stmt.setString(7,
			// laudo.getPaciente().getCpf().toUpperCase().trim());
			stmt.setDate(4, new java.sql.Date(laudo.getDtasolicitacao()
					.getTime()));
			// stmt.setInt(7, laudo.getProrrogar());
			// stmt.setDate(5, new
			// java.sql.Date(laudo.getDtainicio().getTime()));
			stmt.setDate(5, new java.sql.Date(laudo.getDtafim().getTime()));

			stmt.execute();
			conexao.commit();
			cadastrou = true;
			conexao.close();

			return cadastrou;
		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		}
	}

	public Boolean alterarLaudoDigita(LaudoBean laudo) throws ProjetoException {
		boolean alterou = false;
		String sql = "update hosp.pacienteslaudo set id_paciente = ?, codmedico = ?, "
				+ "codprocedimento = ?, datasolicitacao = ?, periodofim = ? where id = ?";
		try {
			conexao = ConnectionFactory.getConnection();
			PreparedStatement stmt = conexao.prepareStatement(sql);
			stmt.setLong(1, laudo.getPaciente().getId_paciente());
			stmt.setLong(2, laudo.getProfissional().getId());
			stmt.setInt(3, laudo.getProcedimento().getIdProc());
			// stmt.setString(6,
			// laudo.getPaciente().getCns().toUpperCase().trim());
			// stmt.setString(7,
			// laudo.getPaciente().getCpf().toUpperCase().trim());
			stmt.setDate(4, new java.sql.Date(laudo.getDtasolicitacao()
					.getTime()));
			// stmt.setInt(7, laudo.getProrrogar());
			// stmt.setDate(5, new
			// java.sql.Date(laudo.getDtasolicitacao().getTime()));
			stmt.setDate(5, new java.sql.Date(laudo.getDtafim().getTime()));
			stmt.setInt(6, laudo.getCodLaudoDigita());
			stmt.executeUpdate();

			conexao.commit();

			alterou = true;

			return alterou;
		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		} finally {
			try {
				conexao.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}

	public Boolean excluirLaudoDigita(LaudoBean laudo) throws ProjetoException {
		boolean excluir = false;
		String sql = "delete from hosp.pacienteslaudo where id = ?";
		try {
			conexao = ConnectionFactory.getConnection();
			PreparedStatement stmt = conexao.prepareStatement(sql);
			stmt.setInt(1, laudo.getCodLaudoDigita());
			stmt.executeUpdate();

			conexao.commit();

			excluir = true;

			return excluir;
		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		} finally {
			try {
				conexao.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}

	public ArrayList<LaudoBean> listaLaudos() throws ProjetoException {

		String sql = "select id_laudo, l.codpaciente, p.nome, l.data_solicitacao, l.mes_inicio, l.ano_inicio, l.mes_final, l.ano_final, "
				+ " l.periodo, l.codprocedimento_primario, pr.nome as procedimento, l.codprocedimento_secundario1, l.codprocedimento_secundario2, "
				+ " l.codprocedimento_secundario3, l.codprocedimento_secundario4, l.codprocedimento_secundario5, l.cid1, l.cid2, l.cid3, l.obs "
				+ " from hosp.laudo l left join hosp.pacientes p on (p.id_paciente = l.codpaciente) "
				+ " left join hosp.proc pr on (pr.codproc = l.codprocedimento_primario) order by id_laudo";

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
				l.setRecuso(rs.getString("recuso"));
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

	public List<LaudoBean> buscarTipoLaudo(String nome, String situacao,
			String recurso, Integer prontuario, Date dataSolicitacao,
			Date dataAutorizacao, Integer idPrograma, Integer idGrupo)
			throws ProjetoException {

		String sql = "select * from hosp.apac left join hosp.pacientes on apac.codpaciente=pacientes.id_paciente where "
				+ " CAST(apac.codprograma AS INT) = ? and CAST(apac.codgrupo AS INT) = ? ";

		if (situacao == "P") {
			sql += "and apac.situacao = ? and apac.dtsolicitacao between ? and ? ";
		}
		if (situacao == "A") {
			sql += "and apac.situacao = ? and apac.dtautorizacao between ? and ? ";

		}
		if (nome != null) {
			sql += "and pacientes.nome like ? ";
		}
		if (!recurso.equals("T")) {
			sql += "and apac.recurso = ? ";
		}
		if ((prontuario != null) && (prontuario != 0)) {
			sql += " and apac.id_apac = ? ";
		}
		List<LaudoBean> lista = new ArrayList<>();
		Integer i = 2;
		try {
			conexao = ConnectionFactory.getConnection();
			PreparedStatement stmt = conexao.prepareStatement(sql);

			stmt.setInt(1, idPrograma);
			stmt.setInt(2, idGrupo);

			if (situacao == "P") {
				i = i + 3;
				stmt.setString(i, situacao);
				stmt.setDate(i, new java.sql.Date(dataSolicitacao.getTime()));
				stmt.setDate(i, new java.sql.Date(dataAutorizacao.getTime()));
			}
			if (situacao == "A") {
				i = i + 3;
				stmt.setString(i, situacao);
				stmt.setDate(i, new java.sql.Date(dataSolicitacao.getTime()));
				stmt.setDate(i, new java.sql.Date(dataAutorizacao.getTime()));
			}
			if (nome != null) {
				i = i + 1;
				stmt.setString(i, "%" + nome.toUpperCase() + "%");
			}
			if (!recurso.equals("T")) {
				i = i + 1;
				stmt.setString(i, recurso);
			}

			else if ((prontuario != null) && (prontuario != 0)) {
				i = i + 1;
				stmt.setInt(i, prontuario);
			}

			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				LaudoBean l = new LaudoBean();

				l.setId_apac(rs.getInt("id_apac"));
				l.setApac(rs.getString("apac").toUpperCase());
				l.setPaciente(pacieDao.listarPacientePorID(rs
						.getInt("codpaciente")));
				l.setPrograma(progDao.listarProgramaPorId(rs
						.getInt("codprograma")));
				l.setGrupo(grupoDao.listarGrupoPorId(rs.getInt("codgrupo")));
				l.setEquipe(equipeDao.buscarEquipePorID(rs.getInt("codequipe")));
				l.setProfissional(profDao.buscarProfissionalPorId(rs
						.getInt("codmedico")));
				l.setProcedimento(procDao.listarProcedimentoPorId(rs
						.getInt("codproc")));
				l.setDtasolicitacao(rs.getDate("dtasolicitacao"));
				l.setRecurso(rs.getString("recurso"));
				l.setUnidade(rs.getString("unidade"));
				l.setSituacao(rs.getString("situacao"));
				l.setDtautorizacao(rs.getDate("dtautorizacao"));
				l.setCid10_1(rs.getString("cid10_1"));
				l.setCid10_2(rs.getString("cid10_2"));
				l.setCid10_2(rs.getString("cid10_3"));
				l.setFornecedor(forneDao.listarFornecedorPorId(rs
						.getInt("codfornecedor")));
				l.setValor(rs.getDouble("valor"));
				l.setNota(rs.getString("nota"));
				l.setQtd(rs.getInt("qtd"));
				l.setCodequipamento(rs.getInt("codequipamento"));
				l.setObs(rs.getString("obs"));

				lista.add(l);

			}
		} catch (SQLException ex) {
			ex.printStackTrace();
			// throw new RuntimeException(ex); //
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

	public List<LaudoBean> buscarTipoLaudo1(Integer idPrograma,
			Date dataSolicitacao, Date dataAutorizacao) throws ProjetoException {

		String sql = "select * from hosp.apac left join hosp.pacientes on apac.codpaciente=pacientes.id_paciente where";
		if (idPrograma == null)
			System.out.println("");
		if (idPrograma != null && dataSolicitacao != null
				&& dataAutorizacao != null) {
			sql += " CAST(apac.codprograma AS INT) = ? and apac.dtautorizacao between ? and ? order by pacientes.nome";
		}
		/*
		 * if (situacao!=null && recurso!=null && idPrograma!=null &&
		 * idGrupo!=null && dataAutorizacao!=null && dataSolicitacao!=null) {
		 * sql +=
		 * " apac.situacao = ? and apac. recurso = ? and CAST(apac.codprograma AS INT) = ? and CAST(apac.codgrupo AS INT) = ? and apac.dtautorizacao = ? and apac.dtasolicitacao = ? order by pacientes.nome"
		 * ; } if (tipo == 3) { sql +=
		 * " apac.recurso like ? order by pacientes.nome"; } if (tipo == 4) {
		 * sql += " CAST(apac.id_apac AS INT) = ? order by pacientes.nome"; } if
		 * (tipo == 5) { sql += " BETWEEN = ? AND = ? order by pacientes.nome";
		 * }
		 */

		List<LaudoBean> lista = new ArrayList<>();

		try {
			conexao = ConnectionFactory.getConnection();
			PreparedStatement stmt = conexao.prepareStatement(sql);
			if (idPrograma != null && dataSolicitacao != null
					&& dataAutorizacao != null) {

				stmt.setInt(1, idPrograma);
				stmt.setDate(2, new java.sql.Date(dataSolicitacao.getTime()));
				stmt.setDate(3, new java.sql.Date(dataAutorizacao.getTime()));
			}
			/*
			 * if (tipo == 2) { stmt.setString(1, "%" + valor.toUpperCase() +
			 * "%"); } if (tipo == 3) { stmt.setString(1, "%" +
			 * valor.toUpperCase() + "%"); } if (tipo == 4) {
			 * stmt.setInt(1,numero); } if (tipo == 5) { stmt.setDate(1, new
			 * java.sql.Date(data.getTime())); stmt.setDate(2, new
			 * java.sql.Date(data.getTime())); }
			 */

			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				LaudoBean l = new LaudoBean();

				l.setId_apac(rs.getInt("id_apac"));
				l.setApac(rs.getString("apac").toUpperCase());
				l.setPaciente(pacieDao.listarPacientePorID(rs
						.getInt("codpaciente")));
				l.setPrograma(progDao.listarProgramaPorId(rs
						.getInt("codprograma")));
				l.setGrupo(grupoDao.listarGrupoPorId(rs.getInt("codgrupo")));
				l.setEquipe(equipeDao.buscarEquipePorID(rs.getInt("codequipe")));
				l.setProfissional(profDao.buscarProfissionalPorId(rs
						.getInt("codmedico")));
				l.setProcedimento(procDao.listarProcedimentoPorId(rs
						.getInt("codproc")));
				l.setDtasolicitacao(rs.getDate("dtasolicitacao"));
				l.setRecurso(rs.getString("recurso"));
				l.setUnidade(rs.getString("unidade"));
				l.setSituacao(rs.getString("situacao"));
				l.setDtautorizacao(rs.getDate("dtautorizacao"));
				l.setCid10_1(rs.getString("cid10_1"));
				l.setCid10_2(rs.getString("cid10_2"));
				l.setCid10_2(rs.getString("cid10_3"));
				l.setFornecedor(forneDao.listarFornecedorPorId(rs
						.getInt("codfornecedor")));
				l.setValor(rs.getDouble("valor"));
				l.setNota(rs.getString("nota"));
				l.setQtd(rs.getInt("qtd"));
				l.setCodequipamento(rs.getInt("codequipamento"));
				l.setObs(rs.getString("obs"));

				lista.add(l);

			}
		} catch (SQLException ex) {
			ex.printStackTrace();
			// throw new RuntimeException(ex); //
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

	public ArrayList<LaudoBean> listaLaudosDigita() throws ProjetoException {

		String sql = "select id, id_paciente, codequipe, codgrupo,codmedico, codprocedimento,datasolicitacao, periodoinicio, periodofim "
				+ " from hosp.pacienteslaudo order by id_paciente";

		ArrayList<LaudoBean> lista = new ArrayList();

		try {
			conexao = ConnectionFactory.getConnection();
			PreparedStatement stm = conexao.prepareStatement(sql);
			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				LaudoBean l = new LaudoBean();

				// p.setDesceApac(rs.getString("descescola").toUpperCase());
				l.setCodLaudoDigita(rs.getInt("id"));
				l.setPaciente(pacieDao.listarPacientePorID(rs
						.getInt("id_paciente")));
				l.setEquipe(equipeDao.buscarEquipePorID(rs.getInt("codequipe")));
				l.setGrupo(grupoDao.listarGrupoPorId(rs.getInt("codgrupo")));
				l.setProfissional(profDao.buscarProfissionalPorId(rs
						.getInt("codmedico")));
				l.setProcedimento(procDao.listarProcedimentoPorId(rs
						.getInt("codprocedimento")));
				l.setDtasolicitacao(rs.getDate("datasolicitacao"));
				l.setDtainicio(rs.getDate("periodoinicio"));
				l.setDtafim(rs.getDate("periodofim"));

				lista.add(l);
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

	public List<LaudoBean> buscarTipoLaudoDigita(String valor, Integer tipo)
			throws ProjetoException {

		String sql = "select * from hosp.pacienteslaudo left join hosp.pacientes on pacienteslaudo.id_paciente=pacientes.id_paciente where";

		if (tipo == 1) {
			sql += " pacientes.nome like ? order by pacientes.nome";
		}
		List<LaudoBean> lista = new ArrayList<>();

		try {
			conexao = ConnectionFactory.getConnection();
			PreparedStatement stmt = conexao.prepareStatement(sql);
			if (tipo == 1) {
				stmt.setString(1, "%" + valor.toUpperCase() + "%");
			}

			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				LaudoBean l = new LaudoBean();

				l.setCodLaudoDigita(rs.getInt("id"));
				l.setPaciente(pacieDao.listarPacientePorID(rs
						.getInt("id_paciente")));
				l.setEquipe(equipeDao.buscarEquipePorID(rs.getInt("codequipe")));
				l.setGrupo(grupoDao.listarGrupoPorId(rs.getInt("codgrupo")));
				l.setProfissional(profDao.buscarProfissionalPorId(rs
						.getInt("codmedico")));
				l.setProcedimento(procDao.listarProcedimentoPorId(rs
						.getInt("codprocedimento")));
				// l.setPrograma(progDao.listarProgramaPorId(rs.getInt("codprograma")));
				l.setDtasolicitacao(rs.getDate("datasolicitacao"));
				l.setDtainicio(rs.getDate("periodoinicio"));
				l.setDtafim(rs.getDate("periodofim"));

				lista.add(l);

			}
		} catch (SQLException ex) {
			ex.printStackTrace();
			// throw new RuntimeException(ex); //
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

	public List<LaudoBean> buscarLaudoPersonalizado() throws ProjetoException {
		LaudoBean l = new LaudoBean();

		String sql = "select * from hosp.apac left join hosp.pacientes on apac.codpaciente=pacientes.id_paciente "
				+ " left join hosp.programa on apac.codprograma=programa.id_programa where";

		if (l.getPrograma().getIdPrograma() != null
				&& l.getDtautorizacao() != null) {
			sql += " CAST(programa.id_programa AS TEXT) = ? and apac.dtautorizacao BETWEEN ? order by pacientes.nome";
		}
		List<LaudoBean> lista = new ArrayList<>();

		try {
			conexao = ConnectionFactory.getConnection();
			PreparedStatement stmt = conexao.prepareStatement(sql);
			if (l.getPrograma().getIdPrograma() != null
					&& l.getDtautorizacao() != null) {
				stmt.setInt(1, l.getPrograma().getIdPrograma());
				stmt.setDate(2, new java.sql.Date(l.getDtautorizacao()
						.getTime()));
			}

			else {

			}

			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {

				l.setCodLaudoDigita(rs.getInt("id"));
				l.setId_apac(rs.getInt("id_apac"));
				l.setPaciente(pacieDao.listarPacientePorID(rs
						.getInt("codpaciente")));
				l.setPrograma(progDao.listarProgramaPorId(rs
						.getInt("codprograma")));
				l.setGrupo(grupoDao.listarGrupoPorId(rs.getInt("codgrupo")));
				l.setEquipe(equipeDao.buscarEquipePorID(rs.getInt("codequipe")));
				l.setProfissional(profDao.buscarProfissionalPorId(rs
						.getInt("codmedico")));
				l.setProcedimento(procDao.listarProcedimentoPorId(rs
						.getInt("codproc")));
				l.setFornecedor(forneDao.listarFornecedorPorId(rs
						.getInt("codfornecedor")));
				l.setRecurso(rs.getString("recurso"));
				l.setApac(rs.getString("apac"));
				l.setUnidade(rs.getString("unidade"));
				l.setSituacao(rs.getString("situacao"));
				l.setDtainicio(rs.getDate("periodoinicio"));
				l.setDtafim(rs.getDate("periodofim"));
				l.setDtautorizacao(rs.getDate("dtautorizacao"));
				l.setDtasolicitacao(rs.getDate("dtasolicitacao"));
				l.setCid10_1(rs.getString("cid10_1"));
				l.setCid10_2(rs.getString("cid10_2"));
				l.setCid10_3(rs.getString("cid10_3"));
				l.setValor(rs.getDouble("valor"));
				l.setNota(rs.getString("nota"));
				l.setQtd(rs.getInt("qtd"));
				l.setCodequipamento(rs.getInt("codequipamento"));
				l.setObs(rs.getString("obs"));

				lista.add(l);

			}
		} catch (SQLException ex) {
			ex.printStackTrace();
			// throw new RuntimeException(ex); //
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

	public LaudoBean buscaLaudoPorId(Integer i) throws ProjetoException {
		String sql = "select * from hosp.apac where id_apac = ?";
		LaudoBean l = new LaudoBean();
		try {
			conexao = ConnectionFactory.getConnection();
			ps = conexao.prepareStatement(sql);
			ps.setInt(1, i);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				l = new LaudoBean();

				l.setCodLaudoDigita(rs.getInt("id"));
				l.setId_apac(rs.getInt("id_apac"));
				l.setPaciente(pacieDao.listarPacientePorID(rs
						.getInt("codpaciente")));
				l.setPrograma(progDao.listarProgramaPorId(rs
						.getInt("codprograma")));
				l.setGrupo(grupoDao.listarGrupoPorId(rs.getInt("codgrupo")));
				l.setEquipe(equipeDao.buscarEquipePorID(rs.getInt("codequipe")));
				l.setProfissional(profDao.buscarProfissionalPorId(rs
						.getInt("codmedico")));
				l.setProcedimento(procDao.listarProcedimentoPorId(rs
						.getInt("codproc")));
				l.setFornecedor(forneDao.listarFornecedorPorId(rs
						.getInt("codfornecedor")));
				l.setRecurso(rs.getString("recurso"));
				l.setApac(rs.getString("apac"));
				l.setUnidade(rs.getString("unidade"));
				l.setSituacao(rs.getString("situacao"));
				l.setDtainicio(rs.getDate("periodoinicio"));
				l.setDtafim(rs.getDate("periodofim"));
				l.setDtautorizacao(rs.getDate("dtautorizacao"));
				l.setDtasolicitacao(rs.getDate("dtasolicitacao"));
				l.setCid10_1(rs.getString("cid10_1"));
				l.setCid10_2(rs.getString("cid10_2"));
				l.setCid10_3(rs.getString("cid10_3"));
				l.setValor(rs.getDouble("valor"));
				l.setNota(rs.getString("nota"));
				l.setQtd(rs.getInt("qtd"));
				l.setCodequipamento(rs.getInt("codequipamento"));
				l.setObs(rs.getString("obs"));
				l.setLaudo(rs.getBoolean("laudo"));
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
		return l;
	}

	public LaudoBean listarLaudoPorId(int id) throws ProjetoException {

		LaudoBean l = new LaudoBean();
		String sql = "select id_apac, codpaciente, codprograma,codgrupo, codequipe, codmedico, "
				+ "codproc,dtasolicitacao, recurso,apac, unidade, situacao, dtautorizacao, "
				+ "cid10_1, cid10_2, cid10_3,codfornecedor,valor, nota, qtd, codequipamento, "
				+ "obs from hosp.apac where id_apac = ?";
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stm = con.prepareStatement(sql);
			stm.setInt(1, id);
			ResultSet rs = stm.executeQuery();
			while (rs.next()) {
				l = new LaudoBean();
				l.setId_apac(rs.getInt("id_apac"));
				l.setPaciente(pacieDao.listarPacientePorID(rs
						.getInt("codpaciente")));
				l.setPrograma(progDao.listarProgramaPorId(rs
						.getInt("codprograma")));
				l.setGrupo(grupoDao.listarGrupoPorId(rs.getInt("codgrupo")));
				l.setEquipe(equipeDao.buscarEquipePorID(rs.getInt("codequipe")));
				l.setProfissional(profDao.buscarProfissionalPorId(rs
						.getInt("codmedico")));
				l.setProcedimento(procDao.listarProcedimentoPorId(rs
						.getInt("codproc")));
				l.setDtasolicitacao(rs.getDate("dtasolicitacao"));
				l.setRecurso(rs.getString("recurso"));
				l.setApac(rs.getString("apac"));
				l.setUnidade(rs.getString("unidade"));
				l.setSituacao(rs.getString("situacao"));
				l.setDtautorizacao(rs.getDate("dtautorizacao"));
				l.setCid10_1(rs.getString("cid10_1"));
				l.setCid10_2(rs.getString("cid10_2"));
				l.setCid10_3(rs.getString("cid10_3"));
				l.setFornecedor(forneDao.listarFornecedorPorId(rs
						.getInt("codfornecedor")));
				l.setValor(rs.getDouble("valor"));
				l.setNota(rs.getString("nota"));
				l.setQtd(rs.getInt("qtd"));
				l.setCodequipamento(rs.getInt("codequipamento"));
				l.setObs(rs.getString("obs"));

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
		return l;
	}

}
