package br.gov.al.maceio.sishosp.hosp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import br.gov.al.maceio.sishosp.acl.dao.FuncionarioDAO;
import br.gov.al.maceio.sishosp.acl.model.FuncionarioBean;
import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;
import br.gov.al.maceio.sishosp.hosp.model.AgendaBean;
import br.gov.al.maceio.sishosp.hosp.model.AtendimentoBean;
import br.gov.al.maceio.sishosp.hosp.model.BloqueioBean;
import br.gov.al.maceio.sishosp.hosp.model.EnderecoBean;
import br.gov.al.maceio.sishosp.hosp.model.FeriadoBean;
import br.gov.al.maceio.sishosp.hosp.model.TipoAtendimentoBean;

public class AtendimentoDAO {
	Connection con = null;
	PreparedStatement ps = null;

	public List<AtendimentoBean> carregaAtendimentos(AtendimentoBean atendimento)
			throws ProjetoException {

		String sql = "select a.id_atendimento, a.dtaatende, a.codpaciente, p.nome, p.cns, a.turno, a.codmedico, f.descfuncionario,"
				+ " a.codprograma, pr.descprograma, situacao, a.codtipoatendimento, t.desctipoatendimento,"
				+ " a.codequipe, e.descequipe,"
				+ " case when t.equipe_programa is true then 'Sim' else 'Não' end as ehEquipe"
				+ " from hosp.atendimentos a"
				+ " left join hosp.pacientes p on (p.id_paciente = a.codpaciente)"
				+ " left join acl.funcionarios f on (f.id_funcionario = a.codmedico)"
				+ " left join hosp.programa pr on (pr.id_programa = a.codprograma)"
				+ " left join hosp.tipoatendimento t on (t.id = a.codtipoatendimento)"
				+ " left join hosp.equipe e on (e.id_equipe = a.codequipe)"
				+ " where a.dtaatende >= ? and a.dtaatende <= ?"
				+ " order by a.dtaatende";

		ArrayList<AtendimentoBean> lista = new ArrayList<AtendimentoBean>();

		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stm = con.prepareStatement(sql);
			stm.setDate(1, new java.sql.Date(atendimento
					.getDataAtendimentoInicio().getTime()));
			stm.setDate(2, new java.sql.Date(atendimento
					.getDataAtendimentoFinal().getTime()));
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
				at.getTipoAt().setDescTipoAt(
						rs.getString("desctipoatendimento"));
				at.getEquipe().setCodEquipe(rs.getInt("codequipe"));
				at.getEquipe().setDescEquipe(rs.getString("descequipe"));
				at.setEhEquipe(rs.getString("ehEquipe"));

				lista.add(at);
			}

			return lista;

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

	public AtendimentoBean listarAtendimentoProfissionalPorId(int id)
			throws ProjetoException {

		AtendimentoBean at = new AtendimentoBean();
		String sql = "select a.id_atendimento, a.dtaatende, a.codpaciente, p.nome, a.codmedico, f.descfuncionario, f.codprocedimentopadrao, pr.nome as procedimento "
				+ "from hosp.atendimentos a "
				+ "left join hosp.pacientes p on (p.id_paciente = a.codpaciente) "
				+ "left join acl.funcionarios f on (f.id_funcionario = a.codmedico) "
				+ "left join hosp.proc pr on (pr.id = f.codprocedimentopadrao) "
				+ "where a.id_atendimento = ?";
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
				at.getProcedimento().setIdProc(
						rs.getInt("codprocedimentopadrao"));
				at.getProcedimento().setNomeProc(rs.getString("procedimento"));
				at.getFuncionario().setId(rs.getLong("codmedico"));
				at.getFuncionario().setNome(rs.getString("descfuncionario"));
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
		return at;
	}

}
