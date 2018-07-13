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

	public Boolean desligarPaciente(GerenciarPacienteBean row, GerenciarPacienteBean gerenciar)
			throws ProjetoException {
		
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

}
