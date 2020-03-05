package br.gov.al.maceio.sishosp.hosp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.gov.al.maceio.sishosp.acl.dao.FuncionarioDAO;
import br.gov.al.maceio.sishosp.acl.model.FuncionarioBean;
import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;
import br.gov.al.maceio.sishosp.comum.util.DataUtil;
import br.gov.al.maceio.sishosp.hosp.model.EquipeBean;

import javax.faces.context.FacesContext;

public class EquipeDAO {

	Connection con = null;
	PreparedStatement ps = null;
	FuncionarioDAO pDao = new FuncionarioDAO();

	FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
			.getSessionMap().get("obj_usuario");

	public boolean gravarEquipe(EquipeBean equipe) {

		FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
				.getSessionMap().get("obj_funcionario");

		Boolean retorno = false;
		String sql = "insert into hosp.equipe (descequipe, cod_unidade, realiza_avaliacao, turno) values (?, ?, ?, ?) RETURNING id_equipe;";

		try {
			con = ConnectionFactory.getConnection();
			ps = con.prepareStatement(sql);
			ps.setString(1, equipe.getDescEquipe().toUpperCase());
			ps.setInt(2, user_session.getUnidade().getId());
			ps.setBoolean(3, equipe.getRealizaAvaliacao());
			ps.setString(4, equipe.getTurno().toUpperCase());
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				retorno = insereEquipeProfissional(rs.getInt("id_equipe"), equipe);
			}

			if (retorno) {
				con.commit();
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			return retorno;
		}
	}

	public Boolean insereEquipeProfissional(int idEquipe, EquipeBean equipe) {
		Boolean retorno = false;
		String sql = "insert into hosp.equipe_medico (equipe, medico) values(?,?);";

		try {
			ps = con.prepareStatement(sql);
			for (FuncionarioBean prof : equipe.getProfissionais()) {
				ps.setInt(1, idEquipe);
				ps.setLong(2, prof.getId());
				ps.execute();
			}
			retorno = true;

		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		} finally {
			try {
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			return retorno;
		}
	}

	public List<EquipeBean> listarEquipe() throws ProjetoException {
		List<EquipeBean> lista = new ArrayList<>();
		String sql = "select id_equipe, descequipe, cod_unidade, realiza_avaliacao, turno from hosp.equipe where cod_unidade = ? order by descequipe";

		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stm = con.prepareStatement(sql);
			stm.setInt(1, user_session.getUnidade().getId());
			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				EquipeBean equipe = new EquipeBean();
				equipe.setCodEquipe(rs.getInt("id_equipe"));
				equipe.setDescEquipe(rs.getString("descequipe"));
				equipe.setCodUnidade(rs.getInt("cod_unidade"));
				equipe.setRealizaAvaliacao(rs.getBoolean("realiza_avaliacao"));
				equipe.setTurno(rs.getString("turno"));
				lista.add(equipe);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return lista;
	}

	public List<EquipeBean> listarEquipeBusca(String descricao) throws ProjetoException {
		List<EquipeBean> lista = new ArrayList<>();
		String sql = "select id_equipe,id_equipe ||'-'|| descequipe as descequipe, cod_unidade, turno from hosp.equipe "
				+ "where upper(id_equipe ||'-'|| descequipe) LIKE ? and cod_unidade = ? order by descequipe";

		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stm = con.prepareStatement(sql);
			stm.setString(1, "%" + descricao.toUpperCase() + "%");
			stm.setInt(2, user_session.getUnidade().getId());
			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				EquipeBean equipe = new EquipeBean();
				equipe.setCodEquipe(rs.getInt("id_equipe"));
				equipe.setDescEquipe(rs.getString("descequipe"));
				equipe.setCodUnidade(rs.getInt("cod_unidade"));
				equipe.setTurno(rs.getString("turno"));
				lista.add(equipe);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return lista;
	}

	public List<EquipeBean> listarEquipePorGrupoAutoComplete(String descricao, Integer codgrupo)
			throws ProjetoException {
		List<EquipeBean> lista = new ArrayList<>();
		String sql = "select distinct e.id_equipe, e.id_equipe ||'-'|| e.descequipe as descequipe, e.turno from hosp.equipe e "
				+ " left join hosp.equipe_grupo eg on (e.id_equipe = eg.codequipe) where eg.id_grupo = ? and descequipe like ? order by descequipe ";

		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stm = con.prepareStatement(sql);
			stm.setInt(1, codgrupo);
			stm.setString(2, "%" + descricao.toUpperCase() + "%");
			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				EquipeBean equipe = new EquipeBean();
				equipe.setCodEquipe(rs.getInt("id_equipe"));
				equipe.setDescEquipe(rs.getString("descequipe"));
				equipe.setTurno(rs.getString("turno"));
				lista.add(equipe);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return lista;
	}

	public List<EquipeBean> listarEquipeAvaliacaoPorProgramaAutoComplete(String descricao, Integer codPrograma)
			throws ProjetoException {
		List<EquipeBean> lista = new ArrayList<>();
		String sql = "select distinct e.id_equipe, e.id_equipe ||'-'|| e.descequipe as descequipe, e.turno "
				+ "from hosp.equipe e " + "left join hosp.equipe_grupo eg on (e.id_equipe = eg.codequipe) "
				+ "LEFT JOIN hosp.grupo_programa gp ON (gp.codgrupo = eg.id_grupo)"
				+ "where gp.codprograma = ? and descequipe like ? and realiza_avaliacao is true order by descequipe ";

		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stm = con.prepareStatement(sql);
			stm.setInt(1, codPrograma);
			stm.setString(2, "%" + descricao.toUpperCase() + "%");
			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				EquipeBean equipe = new EquipeBean();
				equipe.setCodEquipe(rs.getInt("id_equipe"));
				equipe.setDescEquipe(rs.getString("descequipe"));
				equipe.setTurno(rs.getString("turno"));

				lista.add(equipe);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return lista;
	}

	public List<EquipeBean> listarEquipeAvaliacaoPorPrograma(Integer codPrograma)
			throws ProjetoException {
		List<EquipeBean> lista = new ArrayList<>();
		String sql = "select distinct e.id_equipe, e.id_equipe ||'-'|| e.descequipe as descequipe, e.turno "
				+ "from hosp.equipe e " + "left join hosp.equipe_grupo eg on (e.id_equipe = eg.codequipe) "
				+ "LEFT JOIN hosp.grupo_programa gp ON (gp.codgrupo = eg.id_grupo)"
				+ "where gp.codprograma = ?  and realiza_avaliacao is true order by descequipe ";

		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stm = con.prepareStatement(sql);
			stm.setInt(1, codPrograma);
			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				EquipeBean equipe = new EquipeBean();
				equipe.setCodEquipe(rs.getInt("id_equipe"));
				equipe.setDescEquipe(rs.getString("descequipe"));
				equipe.setTurno(rs.getString("turno"));

				lista.add(equipe);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return lista;
	}

	public List<EquipeBean> listarEquipePorGrupo(Integer codgrupo) throws ProjetoException {
		List<EquipeBean> lista = new ArrayList<>();
		String sql = "select distinct e.id_equipe, e.id_equipe ||'-'|| e.descequipe as descequipe, e.turno from hosp.equipe e "
				+ " left join hosp.equipe_grupo eg on (e.id_equipe = eg.codequipe) where eg.id_grupo = ? order by descequipe ";

		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stm = con.prepareStatement(sql);
			stm.setInt(1, codgrupo);
			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				EquipeBean equipe = new EquipeBean();
				equipe.setCodEquipe(rs.getInt("id_equipe"));
				equipe.setDescEquipe(rs.getString("descequipe"));
				equipe.setTurno(rs.getString("turno"));

				lista.add(equipe);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return lista;
	}

	public Boolean alterarEquipe(EquipeBean equipe) {
		Boolean retorno = false;
		String sql = "update hosp.equipe set descequipe = ?, realiza_avaliacao=?, turno=? where id_equipe = ?";
		ps = null;
		try {
			con = ConnectionFactory.getConnection();
			ps = con.prepareStatement(sql);
			ps.setString(1, equipe.getDescEquipe().toUpperCase());
			ps.setBoolean(2, equipe.getRealizaAvaliacao());
			ps.setString(3, equipe.getTurno().toUpperCase());
			ps.setInt(4, equipe.getCodEquipe());
			ps.executeUpdate();

			retorno = excluirTabEquipeProf(equipe.getCodEquipe());

			if (retorno) {
				retorno = insereEquipeProfissional(equipe.getCodEquipe(), equipe);
			}

			if (retorno) {
				con.commit();
			}

			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			return retorno;
		}
	}

	public Boolean excluirTabEquipeProf(int id) {
		Boolean retorno = false;
		String sql = "delete from hosp.equipe_medico where equipe = ?";

		try {
			ps = con.prepareStatement(sql);
			ps.setLong(1, id);
			ps.execute();
			retorno = true;
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		} finally {
			try {
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			return retorno;
		}
	}

	public Boolean excluirEquipe(EquipeBean equipe) throws ProjetoException {
		con = ConnectionFactory.getConnection();
		Boolean retorno = excluirTabEquipeProf(equipe.getCodEquipe());
		String sql = "delete from hosp.equipe where id_equipe = ?";

		try {

			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setLong(1, equipe.getCodEquipe());
			stmt.execute();

			if (retorno) {
				con.commit();
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			return retorno;
		}
	}

	public EquipeBean buscarEquipePorID(Integer id) throws ProjetoException {
		EquipeBean equipe = null;

		String sql = "select id_equipe, descequipe, cod_unidade, realiza_avaliacao, turno from hosp.equipe where id_equipe = ?";

		try {
			con = ConnectionFactory.getConnection();
			ps = con.prepareStatement(sql);
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				equipe = new EquipeBean();
				equipe.setCodEquipe(rs.getInt("id_equipe"));
				equipe.setDescEquipe(rs.getString("descequipe"));
				equipe.setProfissionais(pDao.listarProfissionaisPorEquipe(rs.getInt("id_equipe"), con));
				equipe.setCodUnidade(rs.getInt("cod_unidade"));
				equipe.setRealizaAvaliacao(rs.getBoolean("realiza_avaliacao"));
				equipe.setTurno(rs.getString("turno"));
			}

			return equipe;
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	public EquipeBean buscarEquipePorIDComConexao(Integer id, Connection conAuxiliar) throws ProjetoException {
		EquipeBean equipe = null;

		String sql = "select id_equipe, descequipe, cod_unidade, realiza_avaliacao, turno from hosp.equipe where id_equipe = ?";

		try {
			ps = conAuxiliar.prepareStatement(sql);
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				equipe = new EquipeBean();
				equipe.setCodEquipe(rs.getInt("id_equipe"));
				equipe.setDescEquipe(rs.getString("descequipe"));
				equipe.setCodUnidade(rs.getInt("cod_unidade"));
				equipe.setRealizaAvaliacao(rs.getBoolean("realiza_avaliacao"));
				equipe.setTurno(rs.getString("turno"));
			}

			return equipe;
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		} finally {
			try {
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
	
	public EquipeBean buscarEquipePorIDParaConverter(Integer id) throws ProjetoException {
		EquipeBean equipe = null;

		String sql = "select id_equipe, descequipe, cod_unidade, realiza_avaliacao, turno from hosp.equipe where id_equipe = ?";

		try {
			con = ConnectionFactory.getConnection();
			ps = con.prepareStatement(sql);
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				equipe = new EquipeBean();
				equipe.setCodEquipe(rs.getInt("id_equipe"));
				equipe.setDescEquipe(rs.getString("descequipe"));
				equipe.setCodUnidade(rs.getInt("cod_unidade"));
				equipe.setRealizaAvaliacao(rs.getBoolean("realiza_avaliacao"));
				equipe.setTurno(rs.getString("turno"));
			}

			return equipe;
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	public ArrayList<FuncionarioBean> listarProfissionaisDaEquipe(Integer codequipe) throws ProjetoException {
		ArrayList<FuncionarioBean> lista = new ArrayList<>();
		String sql = "select e.medico, f.descfuncionario, f.codespecialidade, es.descespecialidade, f.codcbo "
				+ "from hosp.equipe_medico e left join acl.funcionarios f on (e.medico = f.id_funcionario) "
				+ "left join hosp.especialidade es on (f.codespecialidade = es.id_especialidade) "
				+ " where equipe = ? order by f.descfuncionario ";

		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stm = con.prepareStatement(sql);
			stm.setInt(1, codequipe);
			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				FuncionarioBean func = new FuncionarioBean();
				func.setId(rs.getLong("medico"));
				func.setNome(rs.getString("descfuncionario"));
				func.getEspecialidade().setCodEspecialidade(rs.getInt("codespecialidade"));
				func.getEspecialidade().setDescEspecialidade(rs.getString("descespecialidade"));
				func.getCbo().setCodCbo(rs.getInt("codcbo"));

				lista.add(func);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return lista;
	}

	public ArrayList<FuncionarioBean> listarProfissionaisDaEquipeInsercao(Integer codequipe,
			Boolean todosOsProfissionais) throws ProjetoException {
		ArrayList<FuncionarioBean> lista = new ArrayList<>();

		String sql = "";

		if (todosOsProfissionais) {
			sql = "select distinct e.medico, f.descfuncionario, f.codespecialidade, es.descespecialidade, f.codcbo, f.codprocedimentopadrao "
					+ "from hosp.equipe_medico e left join acl.funcionarios f on (e.medico = f.id_funcionario) "
					+ "left join hosp.especialidade es on (f.codespecialidade = es.id_especialidade) "
					+ " where codunidade = ? order by f.descfuncionario ";
		} else {
			sql = "select e.medico, f.descfuncionario, f.codespecialidade, es.descespecialidade, f.codcbo, f.codprocedimentopadrao "
					+ "from hosp.equipe_medico e left join acl.funcionarios f on (e.medico = f.id_funcionario) "
					+ "left join hosp.especialidade es on (f.codespecialidade = es.id_especialidade) "
					+ " where equipe = ? order by f.descfuncionario ";
		}

		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stm = con.prepareStatement(sql);

			if (todosOsProfissionais) {
				stm.setInt(1, user_session.getUnidade().getId());
			} else {
				stm.setInt(1, codequipe);
			}
			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				FuncionarioBean func = new FuncionarioBean();
				func.setId(rs.getLong("medico"));
				func.setNome(rs.getString("descfuncionario"));
				func.getEspecialidade().setCodEspecialidade(rs.getInt("codespecialidade"));
				func.getEspecialidade().setDescEspecialidade(rs.getString("descespecialidade"));
				func.getCbo().setCodCbo(rs.getInt("codcbo"));
				func.getProc1().setIdProc(rs.getInt("codprocedimentopadrao"));

				lista.add(func);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return lista;
	}

	public Boolean removerProfissionalEquipe(int codigoEquipe, Long codigoProfissional, Date dataSaida) {

		FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
				.getSessionMap().get("obj_funcionario");

		Boolean retorno = false;
		String sql = "insert into logs.remocao_profissional_equipe " +
				"(cod_equipe, cod_profissional, data_saida, usuario_acao, data_hora_acao) " +
				"values (?, ?, ?, ?, CURRENT_TIMESTAMP) RETURNING id;";

		try {
			con = ConnectionFactory.getConnection();
			ps = con.prepareStatement(sql);
			ps.setInt(1, codigoEquipe);
			ps.setLong(2, codigoProfissional);
			ps.setDate(3, DataUtil.converterDateUtilParaDateSql(dataSaida));
			ps.setLong(4, user_session.getId());
			ResultSet rs = ps.executeQuery();

			Integer id = null;

			if (rs.next()) {
				id = rs.getInt("id");
			}

			if (excluirProfissionalDeEquipe(codigoEquipe, codigoProfissional, dataSaida, id, con)) {
				con.commit();
				retorno = true;
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			return retorno;
		}
	}

	public Boolean excluirProfissionalDeEquipe(int codigoEquipe, Long codigoProfissional, Date dataSaida, int idRemocaoProfissionalEquipe, Connection conAuxiliar) {

		Boolean retorno = false;

		String sql = "delete from hosp.equipe_medico where equipe = ? and medico = ?";

		try {
			ps = conAuxiliar.prepareStatement(sql);
			ps.setLong(1, codigoEquipe);
			ps.setLong(2, codigoProfissional);
			ps.execute();

			retorno = gravarLogRemocaoProfissionalEquipeAtendimentos1(
					listarAtendimentosAhSeremExcluidos(codigoProfissional, dataSaida, codigoEquipe, con), codigoProfissional, idRemocaoProfissionalEquipe, con);

		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		} finally {
			try {
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			return retorno;
		}
	}

	public ArrayList<Integer> listarAtendimentosAhSeremExcluidos(Long codigoProfissional, Date dataSaida, int codigoEquipe, Connection conAuxiliar) {

		ArrayList<Integer> lista = new ArrayList<>();

		String sql = "SELECT a1.id_atendimentos1  " +
				"FROM hosp.atendimentos1 a1 " +
				"JOIN hosp.atendimentos a ON (a1.id_atendimento = a.id_atendimento) " +
				"WHERE a1.codprofissionalatendimento = ? AND a.dtamarcacao >= ? AND a.codequipe = ?;";

		try {
			PreparedStatement stm = conAuxiliar.prepareStatement(sql);
			stm.setLong(1, codigoProfissional);
			stm.setDate(2, DataUtil.converterDateUtilParaDateSql(dataSaida));
			stm.setInt(3, codigoEquipe);
			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				int idAtendimento1 = rs.getInt("id_atendimentos1");
				lista.add(idAtendimento1);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		} finally {
			try {
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return lista;
	}

	public Boolean gravarLogRemocaoProfissionalEquipeAtendimentos1(
			List<Integer> listaAtendimentos1, Long codigoProfissional, int idRemocaoProfissionalEquipe, Connection conAuxiliar) {

		Boolean retorno = false;
		String sql = "insert into logs.remocao_profissional_equipe_atendimentos1 " +
				"(id_remocao_profissional_equipe, id_atendimentos1, id_funcionario) " +
				"values (?, ?, ?);";

		try {
			for(int i=0; i<listaAtendimentos1.size(); i++) {
				ps = conAuxiliar.prepareStatement(sql);
				ps.setInt(1, idRemocaoProfissionalEquipe);
				ps.setLong(2, listaAtendimentos1.get(i));
				ps.setLong(3, codigoProfissional);
				ps.execute();
			}

			retorno = excluirAtendimentosProfissionalRemovidoEquipe(
					listaAtendimentos1, codigoProfissional, idRemocaoProfissionalEquipe, con);

		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		} finally {
			try {
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			return retorno;
		}
	}

	public Boolean excluirAtendimentosProfissionalRemovidoEquipe(
			List<Integer> listaAtendimentos1, Long codigoProfissional, int idRemocaoProfissionalEquipe, Connection conAuxiliar) {

		Boolean retorno = false;

		String sql = "UPDATE hosp.atendimentos1 SET excluido = 'S' WHERE id_atendimentos1 = ?";

		try {
			for(int i=0; i<listaAtendimentos1.size(); i++) {
				ps = conAuxiliar.prepareStatement(sql);
				ps.setLong(1, listaAtendimentos1.get(i));
				ps.execute();
			}

			retorno = gravarLogRemocaoProfissionalEquipePacienteInstituicao(
					listarIdPacienteInstituicaoAhSeremApagados(
							codigoProfissional, listaAtendimentos1, conAuxiliar), codigoProfissional, idRemocaoProfissionalEquipe, conAuxiliar);

		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		} finally {
			try {
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			return retorno;
		}
	}

	public ArrayList<Integer> listarIdPacienteInstituicaoAhSeremApagados(Long codigoProfissional, List<Integer> listaAtendimentos1, Connection conAuxiliar) {

		ArrayList<Integer> lista = new ArrayList<>();

		String sql = "SELECT DISTINCT pda.id_paciente_instituicao " +
				"FROM hosp.profissional_dia_atendimento pda " +
				"JOIN hosp.atendimentos a ON (pda.id_paciente_instituicao = a.id_paciente_instituicao) " +
				"WHERE pda.id_profissional = ? " +
				"AND a.id_atendimento = " +
				"(SELECT DISTINCT a1.id_atendimento FROM hosp.atendimentos1 a1 WHERE a1.id_atendimentos1 = ?);";

		try {
			for(int i=0; i<listaAtendimentos1.size(); i++) {
				PreparedStatement stm = conAuxiliar.prepareStatement(sql);
				stm.setLong(1, codigoProfissional);
				stm.setInt(2, listaAtendimentos1.get(i));
				ResultSet rs = stm.executeQuery();

				while (rs.next()) {
					int idPacienteInstituicao = rs.getInt("id_paciente_instituicao");
					lista.add(idPacienteInstituicao);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		} finally {
			try {
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return lista;
	}

	public Boolean gravarLogRemocaoProfissionalEquipePacienteInstituicao(
			List<Integer> listaIdPacienteInstituicao, Long codigoProfissional, int idRemocaoProfissionalEquipe, Connection conAuxiliar) {

		Boolean retorno = false;
		String sql = "insert into logs.remocao_profissional_equipe_profissional_dia_atendimento " +
				"(id_remocao_profissional_equipe, id_paciente_instituicao, id_funcionario, dia_semana) " +
				"values (?, ?, ?, ?);";

		try {
			for(int i=0; i<listaIdPacienteInstituicao.size(); i++) {

				List<Integer> listaDias = listarDiasDaSemanaAtendimentoProfissional(codigoProfissional, listaIdPacienteInstituicao.get(i), conAuxiliar);

				for (int j = 0; j < listaDias.size(); i++) {
					ps = conAuxiliar.prepareStatement(sql);
					ps.setInt(1, idRemocaoProfissionalEquipe);
					ps.setLong(2, listaIdPacienteInstituicao.get(i));
					ps.setLong(3, codigoProfissional);
					ps.setInt(4, listaDias.get(j));
					ps.execute();
				}
			}

			retorno = excluirProfissionalDiaAtendimento(listaIdPacienteInstituicao, codigoProfissional, conAuxiliar);

		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		} finally {
			try {
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			return retorno;
		}
	}

	public Boolean excluirProfissionalDiaAtendimento(List<Integer> listaIdPacienteInstituicao, Long codigoProfissional,  Connection conAuxiliar) {

		Boolean retorno = false;

		String sql = "delete from hosp.profissional_dia_atendimento where id_paciente_instituicao = ? and id_profissional = ?";

		try {
			for(int i=0; i<listaIdPacienteInstituicao.size(); i++) {

					ps = conAuxiliar.prepareStatement(sql);
					ps.setLong(1, listaIdPacienteInstituicao.get(i));
					ps.setLong(2, codigoProfissional);
					ps.execute();
				}

			retorno = true;

		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		} finally {
			try {
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			return retorno;
		}
	}

	public List<Integer> listarDiasDaSemanaAtendimentoProfissional(Long idProfissional, int idPacienteInstituicao, Connection conAuxiliar) {

		ArrayList<Integer> lista = new ArrayList<>();

		String sql = "SELECT dia_semana FROM hosp.profissional_dia_atendimento WHERE id_paciente_instituicao = ? AND id_profissional = ?;";

		try {
				PreparedStatement stm = conAuxiliar.prepareStatement(sql);
				stm.setLong(1, idProfissional);
				stm.setInt(2, idPacienteInstituicao);
				ResultSet rs = stm.executeQuery();

				while (rs.next()) {
					int diaSemana = rs.getInt("id_paciente_instituicao");
					lista.add(diaSemana);
				}

		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		} finally {
			try {
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return lista;
	}

}
