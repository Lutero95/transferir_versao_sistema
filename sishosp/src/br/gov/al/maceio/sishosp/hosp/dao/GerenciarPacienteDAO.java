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
import br.gov.al.maceio.sishosp.hosp.model.BloqueioBean;
import br.gov.al.maceio.sishosp.hosp.model.GerenciarPacienteBean;
import br.gov.al.maceio.sishosp.hosp.model.GrupoBean;
import br.gov.al.maceio.sishosp.hosp.model.InsercaoPacienteBean;
import br.gov.al.maceio.sishosp.hosp.model.LaudoBean;
import br.gov.al.maceio.sishosp.hosp.model.ProcedimentoBean;
import br.gov.al.maceio.sishosp.hosp.model.ProgramaBean;

public class GerenciarPacienteDAO {

	PreparedStatement ps = null;
	private Connection conexao = null;

	public List<GerenciarPacienteBean> carregarPacientesInstituicao()
			throws ProjetoException {

		String sql = "select p.id, p.codprograma, p.codgrupo, g.descgrupo, p.codpaciente, pa.nome, pa.cns, p.codequipe, e.descequipe, "
				+ " p.codprofissional, f.descfuncionario, p.status, p.codlaudo, p.data_solicitacao, p.observacao, p.data_cadastro "
				+ " from hosp.paciente_instituicao p "
				+ " left join hosp.pacientes pa on (p.codpaciente = pa.id_paciente) "
				+ " left join hosp.equipe e on (p.codequipe = e.id_equipe) "
				+ " left join acl.funcionarios f on (p.codprofissional = f.id_funcionario) "
				+ " left join hosp.grupo g on (g.id_grupo = p.codgrupo)";

		List<GerenciarPacienteBean> lista = new ArrayList<>();

		try {
			conexao = ConnectionFactory.getConnection();
			PreparedStatement stmt = conexao.prepareStatement(sql);

			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				GerenciarPacienteBean gp = new GerenciarPacienteBean();

				gp.setId(rs.getInt("id"));
				gp.getPrograma().setIdPrograma(rs.getInt("codprograma"));
				gp.getGrupo().setIdGrupo(rs.getInt("codgrupo"));
				gp.getGrupo().setDescGrupo(rs.getString("descgrupo"));
				gp.getPaciente().setId_paciente(rs.getInt("codpaciente"));
				gp.getPaciente().setNome(rs.getString("nome"));
				gp.getPaciente().setCns(rs.getString("cns"));
				gp.getEquipe().setId_equipamento(rs.getInt("codequipe"));
				gp.getEquipe().setDescEquipamento(rs.getString("descequipe"));
				gp.getFuncionario().setId(rs.getLong("codprofissional"));
				gp.getFuncionario().setNome(rs.getString("descfuncionario"));
				gp.setStatus(rs.getString("status"));
				gp.getLaudo().setId(rs.getInt("codlaudo"));
				gp.setData_solicitacao(rs.getDate("data_solicitacao"));
				gp.setObservacao(rs.getString("observacao"));
				gp.setData_cadastro(rs.getDate("data_cadastro"));

				lista.add(gp);

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

	public List<GerenciarPacienteBean> carregarPacientesInstituicaoBusca(
			GerenciarPacienteBean gerenciar) throws ProjetoException {

		String sql = "select p.id, p.codprograma, p.codgrupo, g.descgrupo, p.codpaciente, pa.nome, pa.cns, p.codequipe, e.descequipe, "
				+ " p.codprofissional, f.descfuncionario, p.status, p.codlaudo, p.data_solicitacao, p.observacao, p.data_cadastro "
				+ " from hosp.paciente_instituicao p "
				+ " left join hosp.pacientes pa on (p.codpaciente = pa.id_paciente) "
				+ " left join hosp.equipe e on (p.codequipe = e.id_equipe) "
				+ " left join acl.funcionarios f on (p.codprofissional = f.id_funcionario) "
				+ " left join hosp.grupo g on (g.id_grupo = p.codgrupo) "
				+ " where p.codprograma = ? and p.codgrupo = ? ";

		if (gerenciar.getStatus().equals("A")) {
			sql = sql + " and status = 'A'";
		}

		if (gerenciar.getStatus().equals("D")) {
			sql = sql + " and status = 'D'";
		}

		List<GerenciarPacienteBean> lista = new ArrayList<>();

		try {
			conexao = ConnectionFactory.getConnection();
			PreparedStatement stmt = conexao.prepareStatement(sql);

			stmt.setInt(1, gerenciar.getPrograma().getIdPrograma());
			stmt.setInt(2, gerenciar.getGrupo().getIdGrupo());

			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				GerenciarPacienteBean gp = new GerenciarPacienteBean();

				gp.setId(rs.getInt("id"));
				gp.getPrograma().setIdPrograma(rs.getInt("codprograma"));
				gp.getGrupo().setIdGrupo(rs.getInt("codgrupo"));
				gp.getGrupo().setDescGrupo(rs.getString("descgrupo"));
				gp.getPaciente().setId_paciente(rs.getInt("codpaciente"));
				gp.getPaciente().setNome(rs.getString("nome"));
				gp.getPaciente().setCns(rs.getString("cns"));
				gp.getEquipe().setId_equipamento(rs.getInt("codequipe"));
				gp.getEquipe().setDescEquipamento(rs.getString("descequipe"));
				gp.getFuncionario().setId(rs.getLong("codprofissional"));
				gp.getFuncionario().setNome(rs.getString("descfuncionario"));
				gp.setStatus(rs.getString("status"));
				gp.getLaudo().setId(rs.getInt("codlaudo"));
				gp.setData_solicitacao(rs.getDate("data_solicitacao"));
				gp.setObservacao(rs.getString("observacao"));
				gp.setData_cadastro(rs.getDate("data_cadastro"));

				lista.add(gp);

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

	public Boolean desligarPaciente(GerenciarPacienteBean row,
			GerenciarPacienteBean gerenciar) throws ProjetoException {

		Boolean retorno = false;

		String sql = "update hosp.paciente_instituicao set status = 'D' "
				+ " where codlaudo = ?";
		try {
			conexao = ConnectionFactory.getConnection();
			PreparedStatement stmt = conexao.prepareStatement(sql);

			stmt.setInt(1, row.getLaudo().getId());
			stmt.executeUpdate();

			String sql2 = "INSERT INTO hosp.historico_paciente_instituicao (codpaciente_instituicao, data_operacao, motivo_desligamento, tipo, observacao) "
					+ " VALUES  (?, current_date, ?, ?, ?)";
			stmt = conexao.prepareStatement(sql2);
			stmt.setLong(1, row.getPaciente().getId_paciente());
			stmt.setInt(2, gerenciar.getMotivo_desligamento());
			stmt.setString(3, "D");
			stmt.setString(4, gerenciar.getObservacao());

			stmt.executeUpdate();

			conexao.commit();

			retorno = true;

		} catch (SQLException ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		} finally {
			try {
				conexao.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}

		return retorno;

	}

	public InsercaoPacienteBean carregarPacientesInstituicaoRenovacao(Integer id)
			throws ProjetoException {

		String sql = "select pi.id, pi.codprograma, p.descprograma, pi.codgrupo, g.descgrupo, pi.codpaciente, pi.codequipe, e.descequipe, "
				+ " pi.codprofissional, f.descfuncionario, pi.observacao "
				+ " from hosp.paciente_instituicao pi "
				+ " left join hosp.programa p on (p.id_programa = pi.codprograma) "
				+ " left join hosp.grupo g on (pi.codgrupo = g.id_grupo) "
				+ " left join hosp.equipe e on (pi.codequipe = e.id_equipe) "
				+ " left join acl.funcionarios f on (pi.codprofissional = f.id_funcionario) "
				+ " where id = ?";

		List<GerenciarPacienteBean> lista = new ArrayList<>();
		InsercaoPacienteBean ip = new InsercaoPacienteBean();
		try {
			conexao = ConnectionFactory.getConnection();
			PreparedStatement stmt = conexao.prepareStatement(sql);

			stmt.setInt(1, id);

			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				ip = new InsercaoPacienteBean();

				ip.setId(rs.getInt("id"));
				ip.getPrograma().setIdPrograma(rs.getInt("codprograma"));
				ip.getPrograma().setDescPrograma(rs.getString("descprograma"));
				ip.getGrupo().setIdGrupo(rs.getInt("codgrupo"));
				ip.getGrupo().setDescGrupo(rs.getString("descgrupo"));
				ip.getLaudo().getPaciente()
						.setId_paciente(rs.getInt("codpaciente"));
				ip.getEquipe().setCodEquipe(rs.getInt("codequipe"));
				ip.getEquipe().setDescEquipe(rs.getString("descequipe"));
				ip.getFuncionario().setId(rs.getLong("codprofissional"));
				ip.getFuncionario().setNome(rs.getString("descfuncionario"));
				ip.setObservacao(rs.getString("observacao"));
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
		return ip;
	}

	public ArrayList<GerenciarPacienteBean> listarDiasAtendimentoProfissional(Integer id)
			throws ProjetoException {

		ArrayList<GerenciarPacienteBean> lista = new ArrayList<>();
System.out.println("id: "+id);
		String sql = "select distinct(p.id_profissional), f.descfuncionario, p.id_paciente_instituicao, "
				+ " case when dia_semana = 1 then 'Domingo' when dia_semana = 2 then 'Segunda' "
				+ " when dia_semana = 3 then 'Terça' when dia_semana = 4 then 'Quarta' "
				+ " when dia_semana = 5 then 'Quinta' when dia_semana = 6 then 'Sexta' when dia_semana = 7 then 'Sábado' "
				+ " end as dia from hosp.profissional_dia_atendimento p "
				+ " left join acl.funcionarios f on (f.id_funcionario = p.id_profissional) "
				+ " where p.id_paciente_instituicao = ? "
				+ " order by id_profissional";
		try {
			conexao = ConnectionFactory.getConnection();
			PreparedStatement stm = conexao.prepareStatement(sql);
			
			stm.setInt(1, id);
			
			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				GerenciarPacienteBean ge = new GerenciarPacienteBean();
				ge.getFuncionario().setNome(rs.getString("descfuncionario"));
				ge.getFuncionario().setId(rs.getLong("id_profissional"));
				ge.setId(rs.getInt("id_paciente_instituicao"));
				ge.getFuncionario().setDiasSemana(rs.getString("dia"));
				
				lista.add(ge);
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
