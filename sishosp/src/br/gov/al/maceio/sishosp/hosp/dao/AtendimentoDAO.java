package br.gov.al.maceio.sishosp.hosp.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import br.gov.al.maceio.sishosp.acl.model.FuncionarioBean;
import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;
import br.gov.al.maceio.sishosp.comum.util.DataUtil;
import br.gov.al.maceio.sishosp.comum.util.JSFUtil;
import br.gov.al.maceio.sishosp.comum.util.VerificadorUtil;
import br.gov.al.maceio.sishosp.hosp.model.AtendimentoBean;
import br.gov.al.maceio.sishosp.hosp.model.GrupoBean;

import javax.faces.context.FacesContext;

public class AtendimentoDAO {
	Connection con = null;
	PreparedStatement ps = null;

	FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
			.getSessionMap().get("obj_usuario");

	public Boolean verificarSeCboEhDoProfissionalPorEquipe(List<AtendimentoBean> lista) {

		Boolean retorno = false;
		ArrayList<Integer> listaAux = new ArrayList<>();
		int valor = 0;

		try {

			con = ConnectionFactory.getConnection();
			for (int i = 0; i < lista.size(); i++) {
				String sql = "SELECT p.id_cbo " + "FROM hosp.proc_cbo p " + "LEFT JOIN hosp.cbo c ON (c.id = p.id_cbo) "
						+ "LEFT JOIN acl.funcionarios f ON (f.codcbo = p.id_cbo) " + "WHERE 1=1 " + // p.id_proc =
																									// f.codprocedimentopadrao
						" AND f.id_funcionario = ? AND p.id_proc = ?;";

				PreparedStatement stm = con.prepareStatement(sql);

				stm.setLong(1, lista.get(i).getFuncionario().getId());
				stm.setInt(2, lista.get(i).getProcedimento().getIdProc());

				ResultSet rs = stm.executeQuery();

				valor = 0;

				while (rs.next()) {
					valor = rs.getInt("id_cbo");

					if (valor > 0) {
						listaAux.add(lista.get(i).getProcedimento().getIdProc());
					}

				}

			}

			if (lista.size() == listaAux.size()) {
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

	public String gerarMensagemSeCboNaoEhPermitidoParaProcedimento(List<AtendimentoBean> lista) {

		String retorno = "";
		Boolean permiteAtendimento = false;

		try {

			con = ConnectionFactory.getConnection();
			for (int i = 0; i < lista.size(); i++) {
				permiteAtendimento = false;

				String sql = "SELECT p.id_cbo " + "FROM hosp.proc_cbo p " + "LEFT JOIN hosp.cbo c ON (c.id = p.id_cbo) "
						+ "LEFT JOIN acl.funcionarios f ON (f.codcbo = p.id_cbo) " + "WHERE 1=1 " + // p.id_proc =
																									// f.codprocedimentopadrao
						" AND f.id_funcionario = ? AND p.id_proc = ?;";

				PreparedStatement stm = con.prepareStatement(sql);

				stm.setLong(1, lista.get(i).getFuncionario().getId());
				stm.setInt(2, lista.get(i).getProcedimento().getIdProc());

				ResultSet rs = stm.executeQuery();

				while (rs.next()) {
					permiteAtendimento = true;
				}

				if (!permiteAtendimento) {
					retorno = retorno + "O procedimento: " + lista.get(i).getProcedimento().getNomeProc()
							+ " não pode ser atendimento pelo CBO: " + lista.get(i).getCbo().getDescCbo() + ".\n";
				}

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

	public Boolean verificarSeCboEhDoProfissionalPorProfissional(Long idProfissional, Integer idProcedimento) {

		Boolean retorno = false;

		String sql = "select p.id_cbo from hosp.proc_cbo p left join acl.funcionarios f on (p.id_proc = ?) left join hosp.cbo c on (c.id = p.id_cbo)"
				+ " where p.id_proc = f.codprocedimentopadrao and c.id = p.id_cbo and f.id_funcionario = ?";
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stm = con.prepareStatement(sql);
			stm.setInt(1, idProcedimento);
			stm.setLong(2, idProfissional);

			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
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

	public Boolean realizaAtendimentoProfissional(FuncionarioBean funcionario, AtendimentoBean atendimento)
			throws ProjetoException {
		boolean alterou = false;
		con = ConnectionFactory.getConnection();
		try {

			String sql = "update hosp.atendimentos1 set codprocedimento = ?, "
					+ "dtaatendido = current_timestamp, situacao = ?, evolucao = ? " + " where id_atendimento = ? and codprofissionalatendimento = ?";

			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setInt(1, atendimento.getProcedimento().getIdProc());
			stmt.setString(2, "A");
			stmt.setString(3, atendimento.getEvolucao());
			stmt.setInt(4, atendimento.getId());
			stmt.setLong(5, funcionario.getId());

			stmt.executeUpdate();

			String sql2 = "update hosp.atendimentos set situacao = 'F' " + " where id_atendimento = ?";

			PreparedStatement stmt2 = con.prepareStatement(sql2);
			stmt2.setInt(1, atendimento.getId());

			stmt2.executeUpdate();

			con.commit();

			alterou = true;

		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			return alterou;
		}
	}

	public Boolean realizaAtendimentoEquipe(List<AtendimentoBean> lista, Integer idLaudo, Integer grupoAvaliacao)
			throws ProjetoException {
		boolean alterou = false;
		con = ConnectionFactory.getConnection();
		try {

			for (int i = 0; i < lista.size(); i++) {

				if (i == 0) {
					String sql = "delete from hosp.atendimentos1 where id_atendimento = ?";

					PreparedStatement stmt = con.prepareStatement(sql);
					stmt.setLong(1, lista.get(i).getId());
					stmt.execute();
				}

				String sql2 = "INSERT INTO hosp.atendimentos1(dtaatendido, codprofissionalatendimento, id_atendimento, "
						+ " cbo, codprocedimento, situacao, evolucao, perfil_avaliacao, horario_atendimento) VALUES (current_timestamp, ?, ?, ?, ?, ?, ?, ?, ?);";

				PreparedStatement stmt2 = con.prepareStatement(sql2);
				stmt2.setLong(1, lista.get(i).getFuncionario().getId());
				stmt2.setInt(2, lista.get(i).getId());
				if (VerificadorUtil.verificarSeObjetoNuloOuZero(lista.get(i).getCbo().getCodCbo())) {
					stmt2.setNull(3, Types.NULL);
				} else {
					stmt2.setInt(3, lista.get(i).getCbo().getCodCbo());
				}
				
				if (VerificadorUtil.verificarSeObjetoNuloOuZero(lista.get(i).getProcedimento().getIdProc())) {
					stmt2.setNull(4, Types.NULL);
				} else {
					stmt2.setInt(4, lista.get(i).getProcedimento().getIdProc());
				}
				
				
				if ((lista.get(i).getStatus() != null) && (!lista.get(i).getStatus().equals("")))
					stmt2.setString(5, lista.get(i).getStatus());
				else
					stmt2.setNull(5, Types.NULL);
				stmt2.setString(6, lista.get(i).getEvolucao());
				stmt2.setString(7, lista.get(i).getPerfil());
				if (lista.get(i).getHorarioAtendimento() != null)
					stmt2.setTime(8, DataUtil.retornarHorarioEmTime(lista.get(i).getHorarioAtendimento()));
				else
					stmt2.setNull(8, Types.NULL);
				stmt2.executeUpdate();

			}

			if (!VerificadorUtil.verificarSeObjetoNuloOuZero(grupoAvaliacao)) {
				String sql3 = "update hosp.atendimentos set cod_laudo = ?, grupo_avaliacao = ? where id_atendimento = ?";

				PreparedStatement stmt3 = con.prepareStatement(sql3);
				stmt3.setInt(1, idLaudo);
				stmt3.setInt(2, grupoAvaliacao);
				stmt3.setInt(3, lista.get(0).getId());
				stmt3.executeUpdate();
			}

			con.commit();

			alterou = true;

		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			return alterou;
		}
	}

	public Boolean limpaAtendimentoProfissional(AtendimentoBean atendimento) throws ProjetoException {
		boolean alterou = false;
		con = ConnectionFactory.getConnection();
		try {

			String sql = "update hosp.atendimentos1 set dtaatendido = null, situacao = ? "
					+ " where id_atendimento = ?";

			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setString(1, "");
			stmt.setInt(2, atendimento.getId());

			stmt.executeUpdate();

			con.commit();

			alterou = true;

		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			return alterou;
		}
	}

	public List<AtendimentoBean> carregaAtendimentos(AtendimentoBean atendimento, String campoBusca, String tipo)
			throws ProjetoException {

		FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
				.getSessionMap().get("obj_funcionario");

		String sql = "select a.id_atendimento, a.dtaatende, a.codpaciente, p.nome, p.cns, a.turno, a.codmedico, f.descfuncionario,"
				+ " a.codprograma, pr.descprograma, a.codtipoatendimento, t.desctipoatendimento,"
				+ " a.codequipe, e.descequipe, a.avaliacao,  "
				+ " case when t.equipe_programa is true then 'Sim' else 'Não' end as ehEquipe,"

				+ " case when "
				+ " (select count(*) from hosp.atendimentos1 a1 where a1.id_atendimento = a.id_atendimento and situacao is null) =  "
				+ " (select count(*) from hosp.atendimentos1 a1 where a1.id_atendimento = a.id_atendimento) "
				+ " then 'Atendimento Não Informado' " + " when "
				+ " (select count(*) from hosp.atendimentos1 a1 where a1.id_atendimento = a.id_atendimento and situacao is not null) = "
				+ " (select count(*) from hosp.atendimentos1 a1 where a1.id_atendimento = a.id_atendimento) "
				+ " then 'Atendimento Informado' " + " else 'Atendimento Informado Parcialmente' " + " end as situacao "

				+ " from hosp.atendimentos a" + " left join hosp.pacientes p on (p.id_paciente = a.codpaciente)"
				+ " left join acl.funcionarios f on (f.id_funcionario = a.codmedico)"
				+ " left join hosp.programa pr on (pr.id_programa = a.codprograma)"
				+ " left join hosp.tipoatendimento t on (t.id = a.codtipoatendimento)"
				+ " left join hosp.equipe e on (e.id_equipe = a.codequipe)"
				+ " where a.dtaatende >= ? and a.dtaatende <= ? and a.cod_unidade = ?";

		if ((atendimento.getPrograma() != null) && (atendimento.getPrograma().getIdPrograma() != null)) {
			sql = sql + " and  a.codprograma = ?";
		}
		if ((atendimento.getGrupo() != null) && (atendimento.getGrupo().getIdGrupo() != null)) {
			sql = sql + " and  a.codgrupo = ?";
		}

		if (tipo.equals("nome")) {
			sql = sql + " and p.nome like ?";
		} else if (tipo.equals("cpf")) {
			sql = sql + " and p.cpf like ?";
		} else if (tipo.equals("cns")) {
			sql = sql + " and p.cns like ?";
		} else if (tipo.equals("prontuario")) {
			sql = sql + " and p.id_paciente = ?";
		} else if (tipo.equals("matricula")) {
			sql = sql + " and p.matricula like ?";
		}

		sql = sql + " order by a.dtaatende";

		ArrayList<AtendimentoBean> lista = new ArrayList<AtendimentoBean>();



		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stm = con.prepareStatement(sql);
			int i = 4;
			stm.setDate(1, new java.sql.Date(atendimento.getDataAtendimentoInicio().getTime()));
			stm.setDate(2, new java.sql.Date(atendimento.getDataAtendimentoFinal().getTime()));
			stm.setInt(3, user_session.getUnidade().getId());
			
            if ((atendimento.getPrograma()!=null) && (atendimento.getPrograma().getIdPrograma()!=null)) {
            stm.setInt(i, atendimento.getPrograma().getIdPrograma());
            i = i+1;
            }
            if ((atendimento.getGrupo()!=null) && (atendimento.getGrupo().getIdGrupo()!=null)) {            
            stm.setInt(i, atendimento.getGrupo().getIdGrupo());
            i = i+1;
            }			

			if (!campoBusca.equals(null)) {
				if ((tipo.equals("nome")) || (tipo.equals("cpf")) || (tipo.equals("cns")) || (tipo.equals("matricula")))
					stm.setString(i, "%" + campoBusca.toUpperCase() + "%");
				else
					stm.setInt(i, Integer.valueOf(campoBusca));
				i = i+1;
			}

			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				AtendimentoBean at = new AtendimentoBean();
				at.setId(rs.getInt("id_atendimento"));
				at.setDataAtendimentoInicio(rs.getDate("dtaatende"));
				at.getPaciente().setId_paciente(rs.getInt("codpaciente"));
				at.getPaciente().setNome(rs.getString("nome"));
				at.getPaciente().setCns(rs.getString("cns"));
				at.setTurno(rs.getString("turno"));
				at.getFuncionario().setId(rs.getLong("codmedico"));
				at.getFuncionario().setNome(rs.getString("descfuncionario"));
				at.getPrograma().setIdPrograma(rs.getInt("codprograma"));
				at.getPrograma().setDescPrograma(rs.getString("descprograma"));
				at.setSituacao(rs.getString("situacao"));
				at.getTipoAt().setIdTipo(rs.getInt("codtipoatendimento"));
				at.getTipoAt().setDescTipoAt(rs.getString("desctipoatendimento"));
				at.getEquipe().setCodEquipe(rs.getInt("codequipe"));
				at.getEquipe().setDescEquipe(rs.getString("descequipe"));
				at.setEhEquipe(rs.getString("ehEquipe"));
				at.setAvaliacao(rs.getBoolean("avaliacao"));

				lista.add(at);
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
	
	public List<AtendimentoBean> carregaAtendimentosDoProfissionalNaEquipe(AtendimentoBean atendimento, String campoBusca, String tipo)
			throws ProjetoException {

		FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
				.getSessionMap().get("obj_funcionario");

		String sql = "select a.id_atendimento, a.dtaatende, a.codpaciente, p.nome, p.cns, a.turno, a.codmedico, f.descfuncionario,"
				+ " a.codprograma, pr.descprograma, a.codtipoatendimento, t.desctipoatendimento,"
				+ " a.codequipe, e.descequipe, a.avaliacao,  "
				+ " case when t.equipe_programa is true then 'Sim' else 'Não' end as ehEquipe,"

				+ " case when "
				+ " (select count(*) from hosp.atendimentos1 a1 where a1.id_atendimento = a.id_atendimento and situacao is null) =  "
				+ " (select count(*) from hosp.atendimentos1 a1 where a1.id_atendimento = a.id_atendimento) "
				+ " then 'Atendimento Não Informado' " + " when "
				+ " (select count(*) from hosp.atendimentos1 a1 where a1.id_atendimento = a.id_atendimento and situacao is not null) = "
				+ " (select count(*) from hosp.atendimentos1 a1 where a1.id_atendimento = a.id_atendimento) "
				+ " then 'Atendimento Informado' " + " else 'Atendimento Informado Parcialmente' " + " end as situacao "

				+ " from hosp.atendimentos a" + " left join hosp.pacientes p on (p.id_paciente = a.codpaciente)"
				+ " left join acl.funcionarios f on (f.id_funcionario = a.codmedico)"
				+ " left join hosp.programa pr on (pr.id_programa = a.codprograma)"
				+ " left join hosp.tipoatendimento t on (t.id = a.codtipoatendimento)"
				+ " left join hosp.equipe e on (e.id_equipe = a.codequipe)"
				+ " where a.dtaatende >= ? and a.dtaatende <= ? and a.cod_unidade = ?"
				+ " and exists (select id_atendimento from hosp.atendimentos1 a11 "
				+ " where a11.codprofissionalatendimento=? and a11.id_atendimento = a.id_atendimento)";

		if ((atendimento.getPrograma() != null) && (atendimento.getPrograma().getIdPrograma() != null)) {
			sql = sql + " and  a.codprograma = ?";
		}
		if ((atendimento.getGrupo() != null) && (atendimento.getGrupo().getIdGrupo() != null)) {
			sql = sql + " and  a.codgrupo = ?";
		}

		if (tipo.equals("nome")) {
			sql = sql + " and p.nome like ?";
		} else if (tipo.equals("cpf")) {
			sql = sql + " and p.cpf like ?";
		} else if (tipo.equals("cns")) {
			sql = sql + " and p.cns like ?";
		} else if (tipo.equals("prontuario")) {
			sql = sql + " and p.id_paciente = ?";
		} else if (tipo.equals("matricula")) {
			sql = sql + " and p.matricula like ?";
		}

		sql = sql + " order by a.dtaatende";

		ArrayList<AtendimentoBean> lista = new ArrayList<AtendimentoBean>();



		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stm = con.prepareStatement(sql);
			int i = 5;
			stm.setDate(1, new java.sql.Date(atendimento.getDataAtendimentoInicio().getTime()));
			stm.setDate(2, new java.sql.Date(atendimento.getDataAtendimentoFinal().getTime()));
			stm.setInt(3, user_session.getUnidade().getId());
			stm.setLong(4, user_session.getId());
			
            if ((atendimento.getPrograma()!=null) && (atendimento.getPrograma().getIdPrograma()!=null)) {
            stm.setInt(i, atendimento.getPrograma().getIdPrograma());
            i = i+1;
            }
            if ((atendimento.getGrupo()!=null) && (atendimento.getGrupo().getIdGrupo()!=null)) {            
            stm.setInt(i, atendimento.getGrupo().getIdGrupo());
            i = i+1;
            }			

			if (!campoBusca.equals(null)) {
				if ((tipo.equals("nome")) || (tipo.equals("cpf")) || (tipo.equals("cns")) || (tipo.equals("matricula")))
					stm.setString(i, "%" + campoBusca.toUpperCase() + "%");
				else
					stm.setInt(i, Integer.valueOf(campoBusca));
				i = i+1;
			}

			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				AtendimentoBean at = new AtendimentoBean();
				at.setId(rs.getInt("id_atendimento"));
				at.setDataAtendimentoInicio(rs.getDate("dtaatende"));
				at.getPaciente().setId_paciente(rs.getInt("codpaciente"));
				at.getPaciente().setNome(rs.getString("nome"));
				at.getPaciente().setCns(rs.getString("cns"));
				at.setTurno(rs.getString("turno"));
				at.getFuncionario().setId(rs.getLong("codmedico"));
				at.getFuncionario().setNome(rs.getString("descfuncionario"));
				at.getPrograma().setIdPrograma(rs.getInt("codprograma"));
				at.getPrograma().setDescPrograma(rs.getString("descprograma"));
				at.setSituacao(rs.getString("situacao"));
				at.getTipoAt().setIdTipo(rs.getInt("codtipoatendimento"));
				at.getTipoAt().setDescTipoAt(rs.getString("desctipoatendimento"));
				at.getEquipe().setCodEquipe(rs.getInt("codequipe"));
				at.getEquipe().setDescEquipe(rs.getString("descequipe"));
				at.setEhEquipe(rs.getString("ehEquipe"));
				at.setAvaliacao(rs.getBoolean("avaliacao"));

				lista.add(at);
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

	public AtendimentoBean listarAtendimentoProfissionalPorId(int id) throws ProjetoException {

		AtendimentoBean at = new AtendimentoBean();
		String sql = "select a.id_atendimento, a.dtaatende, a.codpaciente, p.nome, a.codmedico, f.descfuncionario, a1.codprocedimento, "
				+ "pr.nome as procedimento, a1.situacao, a1.evolucao, a.avaliacao, a.cod_laudo, a.grupo_avaliacao, a.codprograma "
				+ "from hosp.atendimentos a " + "join hosp.atendimentos1 a1 on a1.id_atendimento = a.id_atendimento "
				+ "left join hosp.pacientes p on (p.id_paciente = a.codpaciente) "
				+ "left join acl.funcionarios f on (f.id_funcionario = a.codmedico) "
				+ "left join hosp.proc pr on (pr.id = a1.codprocedimento) " + "where a.id_atendimento = ?";
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stm = con.prepareStatement(sql);
			stm.setInt(1, id);
			ResultSet rs = stm.executeQuery();
			while (rs.next()) {
				at.setId(rs.getInt("id_atendimento"));
				at.setDataAtendimentoInicio(rs.getDate("dtaatende"));
				at.getPaciente().setId_paciente(rs.getInt("codpaciente"));
				at.getPaciente().setNome(rs.getString("nome"));
				at.getProcedimento().setIdProc(rs.getInt("codprocedimento"));
				at.getProcedimento().setNomeProc(rs.getString("procedimento"));
				at.getFuncionario().setId(rs.getLong("codmedico"));
				at.getFuncionario().setNome(rs.getString("descfuncionario"));
				at.setStatus(rs.getString("situacao"));
				at.setEvolucao(rs.getString("evolucao"));
				at.setAvaliacao(rs.getBoolean("avaliacao"));
				at.getInsercaoPacienteBean().getLaudo().setId(rs.getInt("cod_laudo"));
				at.setGrupoAvaliacao(new GrupoDAO().listarGrupoPorIdComConexao(rs.getInt("grupo_avaliacao"), con));
				at.setPrograma(new ProgramaDAO().listarProgramaPorIdComConexao(rs.getInt("codprograma"), con));
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
		return at;
	}
	
	public AtendimentoBean listarAtendimentoProfissionalPaciente(int id) throws ProjetoException {

		AtendimentoBean at = new AtendimentoBean();
		String sql = "select a.id_atendimento, a.dtaatende, a.codpaciente, p.nome, a.codmedico, f.descfuncionario, a1.codprocedimento, "
				+ "pr.nome as procedimento, a1.situacao, a1.evolucao, a.avaliacao, a.cod_laudo, a.grupo_avaliacao, a.codprograma "
				+ "from hosp.atendimentos a " + "join hosp.atendimentos1 a1 on a1.id_atendimento = a.id_atendimento "
				+ "left join hosp.pacientes p on (p.id_paciente = a.codpaciente) "
				+ "left join acl.funcionarios f on (f.id_funcionario =a1.codprofissionalatendimento) "
				+ "left join hosp.proc pr on (pr.id = a1.codprocedimento) " + "where a.id_atendimento = ? and a1.codprofissionalatendimento=?";
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stm = con.prepareStatement(sql);
			stm.setInt(1, id);
			stm.setLong(2, user_session.getId());
			ResultSet rs = stm.executeQuery();
			while (rs.next()) {
				at.setId(rs.getInt("id_atendimento"));
				at.setDataAtendimentoInicio(rs.getDate("dtaatende"));
				at.getPaciente().setId_paciente(rs.getInt("codpaciente"));
				at.getPaciente().setNome(rs.getString("nome"));
				at.getProcedimento().setIdProc(rs.getInt("codprocedimento"));
				at.getProcedimento().setNomeProc(rs.getString("procedimento"));
				at.getFuncionario().setId(rs.getLong("codmedico"));
				at.getFuncionario().setNome(rs.getString("descfuncionario"));
				at.setStatus(rs.getString("situacao"));
				at.setEvolucao(rs.getString("evolucao"));
				at.setAvaliacao(rs.getBoolean("avaliacao"));
				at.getInsercaoPacienteBean().getLaudo().setId(rs.getInt("cod_laudo"));
				at.setGrupoAvaliacao(new GrupoDAO().listarGrupoPorIdComConexao(rs.getInt("grupo_avaliacao"), con));
				at.setPrograma(new ProgramaDAO().listarProgramaPorIdComConexao(rs.getInt("codprograma"), con));
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
		return at;
	}	

	public List<AtendimentoBean> carregaAtendimentosEquipe(Integer id) throws ProjetoException {

		String sql = "select a1.id_atendimentos1, a1.id_atendimento, a1.codprofissionalatendimento, f.descfuncionario, f.cns,"
				+ " f.codcbo, c.descricao, a1.situacao, pr.id, a1.codprocedimento, pr.nome as procedimento, a1.evolucao, a1.perfil_avaliacao, to_char(a1.horario_atendimento,'HH24:MI') horario_atendimento "
				+ " from hosp.atendimentos1 a1"
				+ " left join acl.funcionarios f on (f.id_funcionario = a1.codprofissionalatendimento)"
				+ " left join hosp.cbo c on (f.codcbo = c.id)"
				+ " left join hosp.proc pr on (a1.codprocedimento = pr.id)" + " where a1.id_atendimento = ?"
				+ " order by a1.id_atendimentos1";

		ArrayList<AtendimentoBean> lista = new ArrayList<AtendimentoBean>();

		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stm = con.prepareStatement(sql);
			stm.setInt(1, id);

			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				AtendimentoBean at = new AtendimentoBean();
				at.setId(rs.getInt("id_atendimento"));
				at.setId1(rs.getInt("id_atendimentos1"));
				at.getFuncionario().setId(rs.getLong("codprofissionalatendimento"));
				at.getFuncionario().setNome(rs.getString("descfuncionario"));
				at.getFuncionario().setCns(rs.getString("cns"));
				at.getCbo().setCodCbo(rs.getInt("codcbo"));
				at.getCbo().setDescCbo(rs.getString("descricao"));
				at.setStatus(rs.getString("situacao"));
				at.getProcedimento().setCodProc(rs.getString("codprocedimento"));
				at.getProcedimento().setNomeProc(rs.getString("procedimento"));
				at.getProcedimento().setIdProc(rs.getInt("id"));
				at.setEvolucao(rs.getString("evolucao"));
				at.setPerfil(rs.getString("perfil_avaliacao"));
				if (!VerificadorUtil.verificarSeObjetoNulo(rs.getString("horario_atendimento")))
					at.setHorarioAtendimento(rs.getString("horario_atendimento"));
				lista.add(at);
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

	public List<AtendimentoBean> carregarEvolucoesDoPaciente(Integer codPaciente) throws ProjetoException {

		String sql = "SELECT a1.evolucao, a1.dtaatendido, f.descfuncionario, p.nome " + "FROM hosp.atendimentos1 a1 "
				+ "LEFT JOIN hosp.atendimentos a ON (a.id_atendimento = a1.id_atendimento) "
				+ "LEFT JOIN hosp.proc p ON (p.id = a1.codprocedimento) "
				+ "LEFT JOIN acl.funcionarios f ON (f.id_funcionario = a1.codprofissionalatendimento) "
				+ "WHERE a1.evolucao IS NOT NULL AND a.codpaciente = ? " + "ORDER BY a1.dtaatendido DESC ";

		ArrayList<AtendimentoBean> lista = new ArrayList<AtendimentoBean>();

		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stm = con.prepareStatement(sql);
			stm.setInt(1, codPaciente);

			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				AtendimentoBean at = new AtendimentoBean();
				at.getProcedimento().setNomeProc(rs.getString("nome"));
				at.getFuncionario().setNome(rs.getString("descfuncionario"));
				at.setEvolucao(rs.getString("evolucao"));
				at.setDataAtendimentoInicio(rs.getDate("dtaatendido"));

				lista.add(at);
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

}
