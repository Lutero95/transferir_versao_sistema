package br.gov.al.maceio.sishosp.hosp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import br.gov.al.maceio.sishosp.acl.model.FuncionarioBean;
import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;
import br.gov.al.maceio.sishosp.comum.util.DataUtil;
import br.gov.al.maceio.sishosp.hosp.enums.SituacaoLaudo;
import br.gov.al.maceio.sishosp.hosp.model.InsercaoPacienteBean;
import br.gov.al.maceio.sishosp.hosp.model.LaudoBean;

import javax.faces.context.FacesContext;

public class LaudoDAO {

	PreparedStatement ps = null;
	private Connection conexao = null;

	FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
			.getSessionMap().get("obj_usuario");

	public Integer cadastrarLaudo(LaudoBean laudo) {

		FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
				.getSessionMap().get("obj_funcionario");

		Integer idLaudoGerado = null;

		String sql = "insert into hosp.laudo "
				+ "(codpaciente,  data_solicitacao, mes_inicio, ano_inicio, mes_final, ano_final, periodo, codprocedimento_primario, "
				+ "codprocedimento_secundario1, codprocedimento_secundario2, codprocedimento_secundario3, codprocedimento_secundario4, codprocedimento_secundario5, "
				+ "cid1, cid2, cid3, obs, ativo, cod_unidade, data_hora_operacao, situacao, cod_profissional ) "
				+ "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, true, ?, CURRENT_TIMESTAMP, ?, ?) returning id_laudo";

		try {
			conexao = ConnectionFactory.getConnection();
			PreparedStatement stmt = conexao.prepareStatement(sql);
			stmt.setLong(1, laudo.getPaciente().getId_paciente());
			stmt.setDate(2, new java.sql.Date(laudo.getDataSolicitacao().getTime()));
			stmt.setInt(3, laudo.getMesInicio());
			stmt.setInt(4, laudo.getAnoInicio());
			stmt.setInt(5, laudo.getMesFinal());
			stmt.setInt(6, laudo.getAnoFinal());
			stmt.setInt(7, laudo.getPeriodo());
			stmt.setInt(8, laudo.getProcedimentoPrimario().getIdProc());
			if (laudo.getProcedimentoSecundario1() != null) {
				if (laudo.getProcedimentoSecundario1().getIdProc() != null) {
					stmt.setInt(9, laudo.getProcedimentoSecundario1().getIdProc());
				} else {
					stmt.setNull(9, Types.NULL);
				}
			} else {
				stmt.setNull(9, Types.NULL);
			}

			if (laudo.getProcedimentoSecundario2() != null) {
				if (laudo.getProcedimentoSecundario2().getIdProc() != null) {
					stmt.setInt(10, laudo.getProcedimentoSecundario2().getIdProc());
				} else {
					stmt.setNull(10, Types.NULL);
				}
			} else {
				stmt.setNull(10, Types.NULL);
			}

			if (laudo.getProcedimentoSecundario3() != null) {
				if (laudo.getProcedimentoSecundario3().getIdProc() != null) {
					stmt.setInt(11, laudo.getProcedimentoSecundario3().getIdProc());
				} else {
					stmt.setNull(11, Types.NULL);
				}
			} else {
				stmt.setNull(11, Types.NULL);
			}

			if (laudo.getProcedimentoSecundario4() != null) {
				if (laudo.getProcedimentoSecundario4().getIdProc() != null) {
					stmt.setInt(12, laudo.getProcedimentoSecundario4().getIdProc());
				} else {
					stmt.setNull(12, Types.NULL);
				}
			} else {
				stmt.setNull(12, Types.NULL);
			}

			if (laudo.getProcedimentoSecundario5() != null) {
				if (laudo.getProcedimentoSecundario5().getIdProc() != null) {
					stmt.setInt(13, laudo.getProcedimentoSecundario5().getIdProc());
				} else {
					stmt.setNull(13, Types.NULL);
				}
			} else {
				stmt.setNull(13, Types.NULL);
			}

			if (laudo.getCid1() != null) {
				if (laudo.getCid1().getIdCid() != null) {
					stmt.setInt(14, laudo.getCid1().getIdCid());
				} else {
					stmt.setNull(14, Types.NULL);
				}
			} else {
				stmt.setNull(14, Types.NULL);
			}

			if (laudo.getCid2() != null) {
				if (laudo.getCid2().getIdCid() != null) {
					stmt.setInt(15, laudo.getCid2().getIdCid());
				} else {
					stmt.setNull(15, Types.NULL);
				}
			} else {
				stmt.setNull(15, Types.NULL);
			}

			if (laudo.getCid3() != null) {
				if (laudo.getCid3().getIdCid() != null) {
					stmt.setInt(16, laudo.getCid3().getIdCid());
				} else {
					stmt.setNull(16, Types.NULL);
				}
			} else {
				stmt.setNull(16, Types.NULL);
			}

			if (laudo.getObs() == null) {
				stmt.setNull(17, Types.NULL);
			} else {
				stmt.setString(17, laudo.getObs().toUpperCase().trim());
			}

			stmt.setInt(18, user_session.getUnidade().getId());

			stmt.setString(19, SituacaoLaudo.PENDENTE.getSigla());
			
			stmt.setLong(20, laudo.getProfissionalLaudo().getId());

			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				idLaudoGerado = rs.getInt("id_laudo");
			}
			conexao.commit();

		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		} finally {
			try {
				conexao.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			return idLaudoGerado;
		}
	}

	public Boolean alterarLaudo(LaudoBean laudo) {
		boolean retorno = false;
		String sql = "update hosp.laudo set codpaciente = ?,  data_solicitacao = ?, mes_inicio = ?, ano_inicio = ?, mes_final = ?, ano_final = ?, "
				+ "periodo = ?, codprocedimento_primario = ?, codprocedimento_secundario1 = ?, codprocedimento_secundario2 = ?, codprocedimento_secundario3 = ?, "
				+ "codprocedimento_secundario4 = ?, codprocedimento_secundario5 = ?, cid1 = ?, cid2 = ?, cid3 = ?, obs = ?, "
				+ "situacao = ?, data_autorizacao = ?, usuario_autorizou = ?, data_hora_operacao = CURRENT_TIMESTAMP, cod_profissional=? where id_laudo = ?";

		try {
			conexao = ConnectionFactory.getConnection();
			PreparedStatement stmt = conexao.prepareStatement(sql);
			stmt.setLong(1, laudo.getPaciente().getId_paciente());
			stmt.setDate(2, new java.sql.Date(laudo.getDataSolicitacao().getTime()));
			stmt.setInt(3, laudo.getMesInicio());
			stmt.setInt(4, laudo.getAnoInicio());
			stmt.setInt(5, laudo.getMesFinal());
			stmt.setInt(6, laudo.getAnoFinal());
			stmt.setInt(7, laudo.getPeriodo());
			stmt.setInt(8, laudo.getProcedimentoPrimario().getIdProc());
			if (laudo.getProcedimentoSecundario1() != null) {
				if (laudo.getProcedimentoSecundario1().getIdProc() != null) {
					stmt.setInt(9, laudo.getProcedimentoSecundario1().getIdProc());
				} else {
					stmt.setNull(9, Types.NULL);
				}
			} else {
				stmt.setNull(9, Types.NULL);
			}

			if (laudo.getProcedimentoSecundario2() != null) {
				if (laudo.getProcedimentoSecundario2().getIdProc() != null) {
					stmt.setInt(10, laudo.getProcedimentoSecundario2().getIdProc());
				} else {
					stmt.setNull(10, Types.NULL);
				}
			} else {
				stmt.setNull(10, Types.NULL);
			}

			if (laudo.getProcedimentoSecundario3() != null) {
				if (laudo.getProcedimentoSecundario3().getIdProc() != null) {
					stmt.setInt(11, laudo.getProcedimentoSecundario3().getIdProc());
				} else {
					stmt.setNull(11, Types.NULL);
				}
			} else {
				stmt.setNull(11, Types.NULL);
			}

			if (laudo.getProcedimentoSecundario4() != null) {
				if (laudo.getProcedimentoSecundario4().getIdProc() != null) {
					stmt.setInt(12, laudo.getProcedimentoSecundario4().getIdProc());
				} else {
					stmt.setNull(12, Types.NULL);
				}
			} else {
				stmt.setNull(12, Types.NULL);
			}

			if (laudo.getProcedimentoSecundario5() != null) {
				if (laudo.getProcedimentoSecundario5().getIdProc() != null) {
					stmt.setInt(13, laudo.getProcedimentoSecundario5().getIdProc());
				} else {
					stmt.setNull(13, Types.NULL);
				}
			} else {
				stmt.setNull(13, Types.NULL);
			}

			if (laudo.getCid1() != null) {
				if (laudo.getCid1().getIdCid() != null) {
					stmt.setInt(14, laudo.getCid1().getIdCid());
				} else {
					stmt.setNull(14, Types.NULL);
				}
			} else {
				stmt.setNull(14, Types.NULL);
			}

			if (laudo.getCid2() != null) {
				if (laudo.getCid2().getIdCid() != null) {
					stmt.setInt(15, laudo.getCid2().getIdCid());
				} else {
					stmt.setNull(15, Types.NULL);
				}
			} else {
				stmt.setNull(15, Types.NULL);
			}

			if (laudo.getCid3() != null) {
				if (laudo.getCid3().getIdCid() != null) {
					stmt.setInt(16, laudo.getCid3().getIdCid());
				} else {
					stmt.setNull(16, Types.NULL);
				}
			} else {
				stmt.setNull(16, Types.NULL);
			}

			if (laudo.getObs() == null) {
				stmt.setNull(17, Types.NULL);
			} else {
				stmt.setString(17, laudo.getObs().toUpperCase().trim());
			}

			stmt.setString(18, laudo.getSituacao().toUpperCase());

			if (laudo.getDataAutorizacao() == null) {
				stmt.setNull(19, Types.NULL);
			} else {
				stmt.setDate(19, DataUtil.converterDateUtilParaDateSql(laudo.getDataAutorizacao()));
			}

			stmt.setLong(20, user_session.getId());
			
			stmt.setLong(21, laudo.getProfissionalLaudo().getId());

			stmt.setInt(22, laudo.getId());

			stmt.executeUpdate();

			conexao.commit();

			retorno = true;
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		} finally {
			try {
				conexao.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			return retorno;
		}
	}

	public Boolean excluirLaudo(LaudoBean laudo) {
		boolean retorno = false;
		String sql = "update hosp.laudo set ativo = false where id_laudo = ?";

		try {
			conexao = ConnectionFactory.getConnection();
			PreparedStatement stmt = conexao.prepareStatement(sql);

			stmt.setInt(1, laudo.getId());

			stmt.executeUpdate();

			conexao.commit();

			retorno = true;
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		} finally {
			try {
				conexao.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			return retorno;
		}
	}

	public ArrayList<LaudoBean> listaLaudos(String situacao, String campoBusca, String tipoBusca)
			throws ProjetoException {

		String sql = "select id_laudo,p.id_paciente,p.matricula, p.nome, "
				+ "pr.codproc , pr.nome as procedimento, l.mes_final, l.ano_final, "
				+ "CASE WHEN l.situacao = 'A' THEN 'Autorizado' ELSE 'Pendente' END AS situacao, func.id_funcionario, func.descfuncionario " + "from hosp.laudo l "
				+ "left join hosp.pacientes p on (p.id_paciente = l.codpaciente) "
				+ "left join hosp.proc pr on (pr.id = l.codprocedimento_primario) "
				+ "left join acl.funcionarios func on (func.id_funcionario = l.cod_profissional) "
				+ " where l.ativo is true and l.cod_unidade = ? ";

		if (!situacao.equals(SituacaoLaudo.TODOS.getSigla())) {
			sql = sql + " AND l.situacao = ? ";
		}

		if ((tipoBusca.equals("paciente") && (!campoBusca.equals(null)) && (!campoBusca.equals("")))) {
			sql = sql + " and p.nome ilike ?";
		}

		if ((tipoBusca.equals("codproc") && (!campoBusca.equals(null)) && (!campoBusca.equals("")))) {
			sql = sql + " and pr.codproc = ?";
		}

		
		if ((tipoBusca.equals("matpaciente") && (!campoBusca.equals(null)) && (!campoBusca.equals("")))) {
			sql = sql + " and upper(p.matricula) = ?";
		}
		
		if ((tipoBusca.equals("prontpaciente") && (!campoBusca.equals(null)) && (!campoBusca.equals("")))) {
			sql = sql + " and p.id_paciente = ?";
		}
		
		sql = sql + " order by ano_final desc, mes_final desc, nome ";

		ArrayList<LaudoBean> lista = new ArrayList();

		try {
			conexao = ConnectionFactory.getConnection();
			PreparedStatement stm = conexao.prepareStatement(sql);
			stm.setInt(1, user_session.getUnidade().getId());
			int i = 2;
			if (!situacao.equals(SituacaoLaudo.TODOS.getSigla())) {
				stm.setString(i, situacao);
				i++;
			}

			if (((tipoBusca.equals("paciente")) && (!campoBusca.equals(null)) && (!campoBusca.equals("")))) {
				stm.setString(i, "%" + campoBusca.toUpperCase() + "%");
				i++;
			}
			
			
			if ( ((tipoBusca.equals("codproc")) || (tipoBusca.equals("matpaciente"))) && (!campoBusca.equals(null)) && (!campoBusca.equals(""))) {
				stm.setString(i, campoBusca.toUpperCase());
				i++;
			}

			if ((tipoBusca.equals("prontpaciente") && (!campoBusca.equals(null)) && (!campoBusca.equals("")))) {
				stm.setInt(i, Integer.valueOf(campoBusca));
				i++;
			}

			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				LaudoBean l = new LaudoBean();

				l.setId(rs.getInt("id_laudo"));
				l.getPaciente().setId_paciente(rs.getInt("id_paciente"));
				l.getPaciente().setMatricula(rs.getString("matricula"));
				l.getPaciente().setNome(rs.getString("nome"));
				l.setMesFinal(rs.getInt("mes_final"));
				l.setAnoFinal(rs.getInt("ano_final"));
				l.getProcedimentoPrimario().setCodProc(rs.getString("codproc"));
				l.getProcedimentoPrimario().setNomeProc(rs.getString("procedimento"));
				l.setSituacao(rs.getString("situacao"));
				l.setMesFinal(rs.getInt("mes_final"));
				l.setAnoFinal(rs.getInt("ano_final"));
				FuncionarioBean func = new FuncionarioBean();
				func.setId(rs.getLong("id_funcionario"));
				func.setNome(rs.getString("descfuncionario"));
				l.setProfissionalLaudo(func);
				l.setVencimento(DataUtil.mostraMesPorExtenso(l.getMesFinal()) + "/" + l.getAnoFinal().toString());

				lista.add(l);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		} finally {
			try {
				conexao.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return lista;
	}

	public LaudoBean buscarLaudosPorId(Integer id) throws ProjetoException {

		LaudoBean l = new LaudoBean();

		String sql = "select id_laudo,l.codpaciente, p.nome, l.data_solicitacao, l.mes_inicio, l.ano_inicio, l.mes_final, l.ano_final, "
				+ " l.periodo, l.codprocedimento_primario, pr.nome as procedimento, l.codprocedimento_secundario1, ps1.nome as nome1, "
				+ " l.codprocedimento_secundario2, ps2.nome as nome2, l.codprocedimento_secundario3, ps3.nome as nome3, "
				+ " l.codprocedimento_secundario4, ps4.nome as nome4, "
				+ " l.codprocedimento_secundario5, ps5.nome as nome5, l.cid1, c1.desccid as desccid1,c1.desccidabrev as desccidabrev1,  l.cid2, c2.desccid as desccid2, c2.desccidabrev as desccidabrev2, "
				+ " l.cid3, c3.desccid as desccid3, c3.desccidabrev as desccidabrev3, l.obs, data_autorizacao, situacao , func.id_funcionario, func.descfuncionario,  "
				+ " data_solicitacao as datainicio, " + 
				"	(SELECT * FROM hosp.fn_GetLastDayOfMonth(to_date(ano_final||'-'||'0'||''||mes_final||'-'||'01', 'YYYY-MM-DD'))) as datafinal"
				+ " from hosp.laudo l left join hosp.pacientes p on (p.id_paciente = l.codpaciente) "
				+ " left join hosp.proc pr on (pr.id = l.codprocedimento_primario) "
				+ " left join hosp.proc ps1 on (ps1.id = l.codprocedimento_secundario1) "
				+ " left join hosp.proc ps2 on (ps2.id = l.codprocedimento_secundario2) "
				+ " left join hosp.proc ps3 on (ps3.id = l.codprocedimento_secundario3) "
				+ " left join hosp.proc ps4 on (ps4.id = l.codprocedimento_secundario4) "
				+ " left join hosp.proc ps5 on (ps5.id = l.codprocedimento_secundario5) "
				+ " left join hosp.cid c1 on (c1.cod = l.cid1) " + " left join hosp.cid c2 on (c2.cod = l.cid2) "
				+ " left join hosp.cid c3 on (c3.cod = l.cid3) left join acl.funcionarios func on func.id_funcionario = l.cod_profissional " + "  where id_laudo = ?";

		try {
			conexao = ConnectionFactory.getConnection();
			PreparedStatement stm = conexao.prepareStatement(sql);
			stm.setInt(1, id);
			ResultSet rs = stm.executeQuery();

			while (rs.next()) {

				l.setId(rs.getInt("id_laudo"));
				l.getPaciente().setId_paciente(rs.getInt("codpaciente"));
				l.getPaciente().setNome(rs.getString("nome"));
				l.setDataSolicitacao(rs.getDate("data_solicitacao"));
				l.setDataAutorizacao(rs.getDate("data_autorizacao"));
				l.setMesInicio(rs.getInt("mes_inicio"));
				l.setAnoInicio(rs.getInt("ano_inicio"));
				l.setMesFinal(rs.getInt("mes_final"));
				l.setAnoFinal(rs.getInt("ano_final"));
				l.setPeriodo(rs.getInt("periodo"));
				l.setVigenciaInicial(rs.getDate("datainicio"));
				l.setVigenciaFinal(rs.getDate("datafinal"));
				l.getProcedimentoPrimario().setIdProc(rs.getInt("codprocedimento_primario"));
				l.getProcedimentoPrimario().setNomeProc(rs.getString("procedimento"));
				l.getProcedimentoSecundario1().setIdProc(rs.getInt("codprocedimento_secundario1"));
				l.getProcedimentoSecundario1().setNomeProc(rs.getString("nome1"));
				l.getProcedimentoSecundario2().setIdProc(rs.getInt("codprocedimento_secundario2"));
				l.getProcedimentoSecundario2().setNomeProc(rs.getString("nome2"));
				l.getProcedimentoSecundario3().setIdProc(rs.getInt("codprocedimento_secundario3"));
				l.getProcedimentoSecundario3().setNomeProc(rs.getString("nome3"));
				l.getProcedimentoSecundario4().setIdProc(rs.getInt("codprocedimento_secundario4"));
				l.getProcedimentoSecundario4().setNomeProc(rs.getString("nome4"));
				l.getProcedimentoSecundario5().setIdProc(rs.getInt("codprocedimento_secundario5"));
				l.getProcedimentoSecundario5().setNomeProc(rs.getString("nome5"));
				if (rs.getString("cid1") != null) { 
				l.getCid1().setIdCid(rs.getInt("cid1"));
				l.getCid1().setDescCid(rs.getString("desccid1"));
				l.getCid1().setDescCidAbrev(rs.getString("desccidabrev1"));
				}
				if (rs.getString("cid2") != null) { 
				l.getCid2().setIdCid(rs.getInt("cid2"));
				l.getCid2().setDescCid(rs.getString("desccid2"));
				l.getCid2().setDescCidAbrev(rs.getString("desccidabrev2"));
				}
				
				if (rs.getString("cid3") != null) { 
				l.getCid3().setIdCid(rs.getInt("cid3"));
				l.getCid3().setDescCid(rs.getString("desccid3"));
				l.getCid3().setDescCidAbrev(rs.getString("desccidabrev3"));
				}
				l.setObs(rs.getString("obs"));
				l.setSituacao(rs.getString("situacao"));
				FuncionarioBean func = new FuncionarioBean();
				func.setId(rs.getLong("id_funcionario"));
				func.setNome(rs.getString("descfuncionario"));
				l.setProfissionalLaudo(func);
				//l.getProfissionalLaudo().setCodigo(rs.getInt("id_funcionario"));
				//l.getProfissionalLaudo().setNome(rs.getString("descfuncionario"));

			}
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		} finally {
			try {
				conexao.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return l;
	}
	
	public LaudoBean carregaLaudoParaRenovacao(Integer id) throws ProjetoException {

		LaudoBean l = new LaudoBean();

		String sql = "select l.codpaciente, p.nome, l.data_solicitacao, l.mes_inicio, l.ano_inicio, l.mes_final, l.ano_final, "
				+ " l.periodo, l.codprocedimento_primario, pr.nome as procedimento, l.codprocedimento_secundario1, ps1.nome as nome1, "
				+ " l.codprocedimento_secundario2, ps2.nome as nome2, l.codprocedimento_secundario3, ps3.nome as nome3, "
				+ " l.codprocedimento_secundario4, ps4.nome as nome4, "
				+ " l.codprocedimento_secundario5, ps5.nome as nome5, l.cid1, c1.desccid as desccid1,  c1.desccidabrev desccidabrev1, l.cid2, c2.desccid as desccid2,  c2.desccidabrev desccidabrev2, "
				+ " l.cid3, c3.desccid as desccid3,  c3.desccidabrev desccidabrev3, l.obs, data_autorizacao, situacao, func.id_funcionario, func.descfuncionario  "
				+ " from hosp.laudo l left join hosp.pacientes p on (p.id_paciente = l.codpaciente) "
				+ " left join hosp.proc pr on (pr.id = l.codprocedimento_primario) "
				+ " left join hosp.proc ps1 on (ps1.id = l.codprocedimento_secundario1) "
				+ " left join hosp.proc ps2 on (ps2.id = l.codprocedimento_secundario2) "
				+ " left join hosp.proc ps3 on (ps3.id = l.codprocedimento_secundario3) "
				+ " left join hosp.proc ps4 on (ps4.id = l.codprocedimento_secundario4) "
				+ " left join hosp.proc ps5 on (ps5.id = l.codprocedimento_secundario5) "
				+ " left join hosp.cid c1 on (c1.cod = l.cid1) " + " left join hosp.cid c2 on (c2.cod = l.cid2) "
				+ " left join hosp.cid c3 on (c3.cod = l.cid3) left join acl.funcionarios func on func.id_funcionario = l.cod_profissional " + "  where id_laudo = ?";

		try {
			conexao = ConnectionFactory.getConnection();
			PreparedStatement stm = conexao.prepareStatement(sql);
			stm.setInt(1, id);
			ResultSet rs = stm.executeQuery();

			while (rs.next()) {

				l.getPaciente().setId_paciente(rs.getInt("codpaciente"));
				l.getPaciente().setNome(rs.getString("nome"));
				Date d = DataUtil.montarDataCompleta(1,rs.getInt("mes_final"),rs.getInt("ano_final"));

				Calendar c = Calendar.getInstance();
				c.setTime(d);
				c.set(Calendar.MONTH, c.get(Calendar.MONTH) + 1);


				l.setDataSolicitacao(c.getTime());
				l.setDataAutorizacao(rs.getDate("data_autorizacao"));
				l.setMesInicio(rs.getInt("mes_final"));
				l.setAnoInicio(rs.getInt("ano_final"));
				l.setPeriodo(rs.getInt("periodo"));
				l.getProcedimentoPrimario().setIdProc(rs.getInt("codprocedimento_primario"));
				l.getProcedimentoPrimario().setNomeProc(rs.getString("procedimento"));
				l.getProcedimentoSecundario1().setIdProc(rs.getInt("codprocedimento_secundario1"));
				l.getProcedimentoSecundario1().setNomeProc(rs.getString("nome1"));
				l.getProcedimentoSecundario2().setIdProc(rs.getInt("codprocedimento_secundario2"));
				l.getProcedimentoSecundario2().setNomeProc(rs.getString("nome2"));
				l.getProcedimentoSecundario3().setIdProc(rs.getInt("codprocedimento_secundario3"));
				l.getProcedimentoSecundario3().setNomeProc(rs.getString("nome3"));
				l.getProcedimentoSecundario4().setIdProc(rs.getInt("codprocedimento_secundario4"));
				l.getProcedimentoSecundario4().setNomeProc(rs.getString("nome4"));
				l.getProcedimentoSecundario5().setIdProc(rs.getInt("codprocedimento_secundario5"));
				l.getProcedimentoSecundario5().setNomeProc(rs.getString("nome5"));
				l.getCid1().setIdCid(rs.getInt("cid1"));
				l.getCid1().setDescCid(rs.getString("desccid1"));
				l.getCid1().setDescCidAbrev(rs.getString("desccidabrev1"));
				l.getCid2().setIdCid(rs.getInt("cid2"));
				l.getCid2().setDescCid(rs.getString("desccid2"));
				l.getCid1().setDescCidAbrev(rs.getString("desccidabrev2"));
				l.getCid3().setIdCid(rs.getInt("cid3"));
				l.getCid3().setDescCid(rs.getString("desccid3"));
				l.getCid1().setDescCidAbrev(rs.getString("desccidabrev3"));
				l.setObs(rs.getString("obs"));
				l.setSituacao(rs.getString("situacao"));
				FuncionarioBean func = new FuncionarioBean();
				func.setId(rs.getLong("id_funcionario"));
				func.setNome(rs.getString("descfuncionario"));
				l.setProfissionalLaudo(func);

			}
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		} finally {
			try {
				conexao.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return l;
	}
	

	public ArrayList<InsercaoPacienteBean> listarLaudosVigentesParaPaciente(Integer codPaciente)
			throws ProjetoException {

		ArrayList<InsercaoPacienteBean> lista = new ArrayList<>();

		String sql = "select codpaciente,nome, cns, id_laudo,mes_inicio, ano_inicio, mes_final, ano_final, "
				+ "to_date(ano_inicio||'-'||'0'||''||mes_inicio||'-'||'01', 'YYYY-MM-DD') as datainicio, "
				+ "(SELECT * FROM hosp.fn_GetLastDayOfMonth(to_date(ano_final||'-'||'0'||''||mes_final||'-'||'01', 'YYYY-MM-DD'))) as datafinal "
				+ "from ( "
				+ " select l.id_laudo, l.codpaciente, p.nome, p.cns, l.data_solicitacao, l.mes_inicio, l.ano_inicio, l.mes_final, l.ano_final, l.periodo, "
				+ " l.codprocedimento_primario, pr.nome as procedimento, l.cid1, ci.desccid,  "
				+ " to_date(ano_inicio||'-'||'0'||''||mes_inicio||'-'||'01', 'YYYY-MM-DD') as datainicio,  "
				+ " (SELECT * FROM hosp.fn_GetLastDayOfMonth(to_date(ano_final||'-'||'0'||''||mes_final||'-'||'01', 'YYYY-MM-DD'))) as datafinal "
				+ " from hosp.laudo l " + " left join hosp.pacientes p on (l.codpaciente = p.id_paciente) "
				+ " left join hosp.proc pr on (l.codprocedimento_primario = pr.id) "
				+ " left join hosp.cid ci on (l.cid1 = cast(ci.cod as integer)) " + " where 1=1 "
				// current_date <= (SELECT * FROM
				// hosp.fn_GetLastDayOfMonth(to_date(ano_final||'-'||'0'||''||mes_final||'-'||'01',
				// 'YYYY-MM-DD'))) "
				+ " AND l.codpaciente = ?  "//AND NOT EXISTS (SELECT pac.codlaudo FROM hosp.paciente_instituicao pac WHERE pac.codlaudo = l.id_laudo)"
				+ " ) a order by datainicio desc";
		try {
			conexao = ConnectionFactory.getConnection();
			PreparedStatement stm = conexao.prepareStatement(sql);
			stm.setInt(1, codPaciente);
			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				InsercaoPacienteBean insercao = new InsercaoPacienteBean();
				insercao.getLaudo().setId(rs.getInt("id_laudo"));
				insercao.getLaudo().getPaciente().setId_paciente(rs.getInt("codpaciente"));
				insercao.getLaudo().getPaciente().setNome(rs.getString("nome"));
				insercao.getLaudo().getPaciente().setCns(rs.getString("cns"));
				insercao.getLaudo().setVigenciaInicial(rs.getDate("datainicio"));
				insercao.getLaudo().setVigenciaFinal(rs.getDate("datafinal"));
				insercao.getLaudo().setMesFinal(rs.getInt("mes_final"));
				insercao.getLaudo().setAnoFinal(rs.getInt("ano_final"));
				insercao.getLaudo().setMesInicio(rs.getInt("mes_inicio"));
				insercao.getLaudo().setAnoInicio(rs.getInt("ano_inicio"));

				lista.add(insercao);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		} finally {
			try {
				conexao.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return lista;
	}

	public LaudoBean recuperarPeriodoInicioLaudo(Integer codLaudo) throws ProjetoException {

		LaudoBean laudo = new LaudoBean();

		String sql = "SELECT mes_inicio, ano_inicio FROM hosp.laudo WHERE id_laudo = ?;";

		try {
			conexao = ConnectionFactory.getConnection();
			PreparedStatement stm = conexao.prepareStatement(sql);
			stm.setInt(1, codLaudo);
			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				laudo.setMesInicio(rs.getInt("mes_inicio"));
				laudo.setAnoInicio(rs.getInt("ano_inicio"));
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		} finally {
			try {
				conexao.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return laudo;
	}
	
	public LaudoBean recuperarUltimoLaudoPaciente(Integer codPaciente, Integer codPrograma, Integer codGrupo) throws ProjetoException {

		LaudoBean laudo = new LaudoBean();

		String sql = "select mes_final, ano_final from hosp.laudo where laudo.id_laudo=(\n" + 
				" select max(l1.id_laudo) from hosp.paciente_instituicao pi1\n" + 
				" join hosp.laudo l1 on l1.id_laudo = pi1.codlaudo where l1.codpaciente=? and pi1.codprograma=? and pi1.codgrupo=? " + 
				" and to_char(l1.ano_final, '9999')||lpad(trim(to_char(l1.mes_final,'99')),2,'0')=\n" + 
				" (select max(to_char(l2.ano_final, '9999')||lpad(trim(to_char(l2.mes_final,'99')),2,'0')) from hosp.paciente_instituicao pi2\n" + 
				" join hosp.laudo l2 on l2.id_laudo = pi2.codlaudo\n" + 
				" where l2.codpaciente=? and pi2.codprograma=? and pi2.codgrupo=? " + 
				" )\n" + 
				" )";

		try {
			conexao = ConnectionFactory.getConnection();
			PreparedStatement stm = conexao.prepareStatement(sql);
			stm.setInt(1, codPaciente);
			stm.setInt(2, codPrograma);
			stm.setInt(3, codGrupo);
			stm.setInt(4, codPaciente);
			stm.setInt(5, codPrograma);
			stm.setInt(6, codGrupo);			
			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				laudo.setMesFinal(rs.getInt("mes_final"));
				laudo.setAnoFinal(rs.getInt("ano_final"));
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		} finally {
			try {
				conexao.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return laudo;
	}

	public LaudoBean listarLaudosVigentesPorId(Integer id) throws ProjetoException {

		LaudoBean laudoBean = new LaudoBean();

		String sql = "select nome, cns, id_laudo,mes_inicio, ano_inicio, mes_final, ano_final, "
				+ "to_date(ano_inicio||'-'||'0'||''||mes_inicio||'-'||'01', 'YYYY-MM-DD') as datainicio, "
				+ "(SELECT * FROM hosp.fn_GetLastDayOfMonth(to_date(ano_final||'-'||'0'||''||mes_final||'-'||'01', 'YYYY-MM-DD'))) as datafinal "
				+ "from ( "
				+ " select l.id_laudo, l.codpaciente, p.nome, p.cns, l.data_solicitacao, l.mes_inicio, l.ano_inicio, l.mes_final, l.ano_final, l.periodo, "
				+ " l.codprocedimento_primario, pr.nome as procedimento, l.cid1, ci.desccid,  "
				+ " to_date(ano_inicio||'-'||'0'||''||mes_inicio||'-'||'01', 'YYYY-MM-DD') as datainicio,  "
				+ " (SELECT * FROM hosp.fn_GetLastDayOfMonth(to_date(ano_final||'-'||'0'||''||mes_final||'-'||'01', 'YYYY-MM-DD'))) as datafinal "
				+ " from hosp.laudo l " + " left join hosp.pacientes p on (l.codpaciente = p.id_paciente) "
				+ " left join hosp.proc pr on (l.codprocedimento_primario = pr.id) "
				+ " left join hosp.cid ci on (l.cid1 = cast(ci.cod as integer)) " + " where 1=1 "
				// current_date <= (SELECT * FROM
				// hosp.fn_GetLastDayOfMonth(to_date(ano_final||'-'||'0'||''||mes_final||'-'||'01',
				// 'YYYY-MM-DD'))) "
				+ " AND l.id_laudo = ?  AND NOT EXISTS (SELECT pac.codlaudo FROM hosp.paciente_instituicao pac WHERE pac.codlaudo = l.id_laudo)"
				+ " ) a";
		try {
			conexao = ConnectionFactory.getConnection();
			PreparedStatement stm = conexao.prepareStatement(sql);
			stm.setInt(1, id);
			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				laudoBean.setId(rs.getInt("id_laudo"));
				laudoBean.getPaciente().setNome(rs.getString("nome"));
				laudoBean.getPaciente().setCns(rs.getString("cns"));
				laudoBean.setVigenciaInicial(rs.getDate("datainicio"));
				laudoBean.setVigenciaFinal(rs.getDate("datafinal"));
				laudoBean.setMesFinal(rs.getInt("mes_final"));
				laudoBean.setAnoFinal(rs.getInt("ano_final"));
				laudoBean.setMesInicio(rs.getInt("mes_inicio"));
				laudoBean.setAnoInicio(rs.getInt("ano_inicio"));

			}
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		} finally {
			try {
				conexao.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return laudoBean;
	}

}
