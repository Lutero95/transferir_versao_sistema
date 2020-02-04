package br.gov.al.maceio.sishosp.administrativo.dao;

import br.gov.al.maceio.sishosp.acl.model.FuncionarioBean;
import br.gov.al.maceio.sishosp.administrativo.model.AfastamentoProfissional;
import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;
import br.gov.al.maceio.sishosp.comum.util.DataUtil;
import br.gov.al.maceio.sishosp.comum.util.JSFUtil;
import br.gov.al.maceio.sishosp.comum.util.VerificadorUtil;

import javax.faces.context.FacesContext;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class AfastamentoProfissionalDAO {

	Connection con = null;
	PreparedStatement ps = null;

	public boolean gravarAfastamentoProfissional(AfastamentoProfissional afastamentoProfissional) {

		Boolean retorno = false;

		String sql = "insert into adm.afastamento_funcionario (tipo_afastamento, id_funcionario_afastado, inicio_afastamento, fim_afastamento, motivo_afastamento, "
				+ "operador_cadastro, data_hora_cadastro, turno) " + "values (?,?,?,?,?,?,CURRENT_TIMESTAMP, ? );";
		try {

			FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
					.getSessionMap().get("obj_usuario");

			con = ConnectionFactory.getConnection();
			ps = con.prepareStatement(sql);
			ps.setString(1, afastamentoProfissional.getTipoAfastamento());
			ps.setLong(2, afastamentoProfissional.getFuncionario().getId());
			ps.setDate(3, DataUtil.converterDateUtilParaDateSql(afastamentoProfissional.getPeriodoInicio()));
			if (afastamentoProfissional.getPeriodoFinal()!= null) {
				ps.setDate(4, DataUtil.converterDateUtilParaDateSql(afastamentoProfissional.getPeriodoFinal()));
            } else {
            	ps.setDate(4, DataUtil.converterDateUtilParaDateSql(afastamentoProfissional.getPeriodoInicio()));
            }
			
			ps.setString(5, afastamentoProfissional.getMotivoAfastamento());
			ps.setLong(6, user_session.getId());
			if (afastamentoProfissional.getTurno() != null) {
				ps.setString(7, afastamentoProfissional.getTurno());
			} else {
				ps.setString(7, "A"); // quando nao informar o turno subentende que sera os dois turnos (Ambos)
			}

			ps.execute();
			con.commit();
			retorno = true;
		} catch (Exception ex) {
			ex.printStackTrace();
			JSFUtil.adicionarMensagemErro(ex.getMessage(), "Atenção");
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

	public List<AfastamentoProfissional> listarAfastamentoProfissionais() {

		List<AfastamentoProfissional> lista = new ArrayList<>();

		String sql = "SELECT a.id, a.tipo_afastamento, a.id_funcionario_afastado, f.descfuncionario, "
				+ "a.inicio_afastamento, a.fim_afastamento, a.motivo_afastamento, a.turno, "
				+ "CASE WHEN a.motivo_afastamento = 'FE' THEN 'Férias' "
				+ "WHEN a.motivo_afastamento = 'LM' THEN 'Licença Médica' WHEN a.motivo_afastamento = 'FA' THEN 'Falta' END AS motivo_afastamento_extenso "
				+ "FROM adm.afastamento_funcionario a "
				+ "JOIN acl.funcionarios f ON (a.id_funcionario_afastado = f.id_funcionario) "
				+ "ORDER BY a.inicio_afastamento ";

		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stm = con.prepareStatement(sql);
			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				AfastamentoProfissional afastamentoProfissional = new AfastamentoProfissional();
				afastamentoProfissional.setId(rs.getInt("id"));
				afastamentoProfissional.setTipoAfastamento(rs.getString("tipo_afastamento"));
				afastamentoProfissional.getFuncionario().setId(rs.getLong("id_funcionario_afastado"));
				afastamentoProfissional.getFuncionario().setNome(rs.getString("descfuncionario"));
				afastamentoProfissional.setPeriodoInicio(rs.getDate("inicio_afastamento"));
				afastamentoProfissional.setPeriodoFinal(rs.getDate("fim_afastamento"));
				afastamentoProfissional.setMotivoAfastamento(rs.getString("motivo_afastamento"));
				afastamentoProfissional.setTurno(rs.getString("turno"));
				afastamentoProfissional.setMotivoAfastamentoPorExtenso(rs.getString("motivo_afastamento_extenso"));
				lista.add(afastamentoProfissional);
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

	public boolean verificaSeExisteAfastamentoProfissionalNoPeriodo(AfastamentoProfissional afastamento) {
		boolean existeAfastamentoProfissionalNoPeriodo = false;
		List<AfastamentoProfissional> lista = new ArrayList<>();

		String sql = "select id from adm.afastamento_funcionario where id_funcionario_afastado	=?\n"
				+ "	and ((? between inicio_afastamento and fim_afastamento)\n"
				+ "	or (? between inicio_afastamento and fim_afastamento)) ";
		if (!VerificadorUtil.verificarSeObjetoNulo(afastamento.getTurno())) {
			if (afastamento.getTurno().equals("M"))
				sql = sql + " and ((afastamento_funcionario.turno='M') or (afastamento_funcionario.turno='A'))";

			if (afastamento.getTurno().equals("T"))
				sql = sql + " and ((afastamento_funcionario.turno='T') or (afastamento_funcionario.turno='A'))";
		}
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stm = con.prepareStatement(sql);
			stm.setLong(1, afastamento.getFuncionario().getId());
			stm.setDate(2, DataUtil.converterDateUtilParaDateSql(afastamento.getPeriodoInicio()));
            
			if (afastamento.getPeriodoFinal() != null) {
				stm.setDate(3, DataUtil.converterDateUtilParaDateSql(afastamento.getPeriodoFinal()));
            } else {
            	stm.setNull(3, Types.NULL);
            }
			
			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				existeAfastamentoProfissionalNoPeriodo = true;
			}
		} catch (Exception ex) {
			JSFUtil.adicionarMensagemErro("Erro: ", "Atenção");
			ex.printStackTrace();
			throw new RuntimeException(ex);
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return existeAfastamentoProfissionalNoPeriodo;
	}

	public AfastamentoProfissional listarAfastamentoProfissionalPorId(int id) {

		AfastamentoProfissional afastamentoProfissional = new AfastamentoProfissional();

		String sql = "select id, tipo_afastamento, id_funcionario_afastado, inicio_afastamento, fim_afastamento, motivo_afastamento "
				+ "from adm.afastamento_funcionario where id = ? ";

		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stm = con.prepareStatement(sql);
			stm.setInt(1, id);
			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				afastamentoProfissional = new AfastamentoProfissional();
				afastamentoProfissional.setId(rs.getInt("id"));
				afastamentoProfissional.setTipoAfastamento(rs.getString("tipo_afastamento"));
				afastamentoProfissional.getFuncionario().setId(rs.getLong("id_funcionario_afastado"));
				afastamentoProfissional.setPeriodoInicio(rs.getDate("inicio_afastamento"));
				afastamentoProfissional.setPeriodoFinal(rs.getDate("fim_afastamento"));
				afastamentoProfissional.setMotivoAfastamento(rs.getString("motivo_afastamento"));
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
		return afastamentoProfissional;
	}

	public Boolean excluirAfastamentoProfissional(int id) {

		Boolean retorno = false;

		String sql = "delete from adm.afastamento_funcionario where id = ?";

		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setLong(1, id);
			stmt.execute();
			con.commit();
			retorno = true;
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		} finally {
			try {
				con.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
			return retorno;
		}
	}

	public AfastamentoProfissional carregarAfastamentoPeloId(int idAfastamento) {

		AfastamentoProfissional afastamento = new AfastamentoProfissional();

		String sql = "SELECT a.id, a.id_funcionario_afastado, f.descfuncionario, a.inicio_afastamento, a.fim_afastamento, a.turno, a.motivo_afastamento "
				+ "FROM adm.afastamento_funcionario a "
				+ "JOIN acl.funcionarios f ON (a.id_funcionario_afastado = f.id_funcionario) " + "WHERE id = ?;";

		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stm = con.prepareStatement(sql);
			stm.setInt(1, idAfastamento);
			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				afastamento.setId(rs.getInt("id"));
				afastamento.setPeriodoInicio(rs.getDate("inicio_afastamento"));
				afastamento.setPeriodoFinal(rs.getDate("fim_afastamento"));
				afastamento.getFuncionario().setId(rs.getLong("id_funcionario_afastado"));
				afastamento.getFuncionario().setNome(rs.getString("descfuncionario"));
				afastamento.setTurno(rs.getString("turno"));
				afastamento.setMotivoAfastamento(rs.getString("motivo_afastamento"));
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
		return afastamento;
	}
}
