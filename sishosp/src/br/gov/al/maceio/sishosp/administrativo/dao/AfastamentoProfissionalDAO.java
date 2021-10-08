package br.gov.al.maceio.sishosp.administrativo.dao;

import br.gov.al.maceio.sishosp.acl.model.FuncionarioBean;
import br.gov.al.maceio.sishosp.administrativo.enums.MotivoAfastamento;
import br.gov.al.maceio.sishosp.administrativo.model.AfastamentoProfissional;
import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;
import br.gov.al.maceio.sishosp.comum.util.DataUtil;
import br.gov.al.maceio.sishosp.comum.util.TratamentoErrosUtil;
import br.gov.al.maceio.sishosp.comum.util.VerificadorUtil;
import br.gov.al.maceio.sishosp.hosp.enums.Turno;

import javax.faces.context.FacesContext;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class AfastamentoProfissionalDAO {

	Connection con = null;
	PreparedStatement ps = null;

	public boolean gravarAfastamentoProfissional(AfastamentoProfissional afastamentoProfissional) throws ProjetoException {

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
				ps.setString(7, Turno.AMBOS.getSigla()); // quando nao informar o turno subentende que sera os dois turnos (Ambos)
			}
			
			List<Integer> listaIdsAtendimentos1 = listaAtendimentos1DoProfissionalNoPeriodo(con, afastamentoProfissional);
			Integer idSituacao = buscaIdSituacaoPadraoDoAfastamento(con, afastamentoProfissional.getMotivoAfastamento());
			atualizaSituacaoAtendimentosDoProfissionalAfastado(con, listaIdsAtendimentos1, idSituacao);
			
			if(afastamentoProfissional.getMotivoAfastamento().equals(MotivoAfastamento.DESLIGAMENTO.getSigla())) {
				desativarFuncionario(afastamentoProfissional.getFuncionario().getId(), con);
			}
			
			ps.executeUpdate();
			con.commit();
			retorno = true;
		} catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				//comentado walter erro log ex.printStackTrace();
			}
		}
		return retorno;
	}
	
	private List<Integer> listaAtendimentos1DoProfissionalNoPeriodo
		(Connection conexaoAuxiliar, AfastamentoProfissional afastamentoProfissional)
			throws ProjetoException, SQLException {

		String sql = "select a1.id_atendimentos1 from hosp.atendimentos1 a1 " + 
				"join hosp.atendimentos a on a1.id_atendimento = a.id_atendimento " + 
				"where a1.codprofissionalatendimento = ? ";		
		
		String filtroTurno = " and a.turno = ? ";
		
		String sqlFiltroPeriodoSemDesligamento = " and a.dtaatende between ? and ? ";
		String sqlFiltroPeriodoComDesligamento = " and a.dtaatende >= ? ";		
		
		if(afastamentoProfissional.getMotivoAfastamento().equals(MotivoAfastamento.DESLIGAMENTO.getSigla()))
			sql += sqlFiltroPeriodoComDesligamento;
		else
			sql += sqlFiltroPeriodoSemDesligamento;
		
		if(!VerificadorUtil.verificarSeObjetoNuloOuVazio(afastamentoProfissional.getTurno())
				&& !afastamentoProfissional.getTurno().equals(Turno.AMBOS.getSigla())) {
			sql += filtroTurno;
		}
		
		List<Integer> listaIdsAtendimentos1 = new ArrayList<>();
		
		try {
			PreparedStatement stm = conexaoAuxiliar.prepareStatement(sql);
			stm.setLong(1, afastamentoProfissional.getFuncionario().getId());
			
			int parametro = 3; 
			if(afastamentoProfissional.getMotivoAfastamento().equals(MotivoAfastamento.DESLIGAMENTO.getSigla())) 
				stm.setDate(2, new Date(afastamentoProfissional.getPeriodoInicio().getTime()));
			else {
				stm.setDate(2, new Date(afastamentoProfissional.getPeriodoInicio().getTime()));
				stm.setDate(3, new Date(afastamentoProfissional.getPeriodoFinal().getTime()));
				parametro++;
			}
			
			if(!VerificadorUtil.verificarSeObjetoNuloOuVazio(afastamentoProfissional.getTurno())
					&& !afastamentoProfissional.getTurno().equals(Turno.AMBOS.getSigla())) {
				stm.setString(parametro, afastamentoProfissional.getTurno());
			}
			
			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				listaIdsAtendimentos1.add(rs.getInt("id_atendimentos1"));
			}

		} catch (SQLException sqle) {
			conexaoAuxiliar.rollback();
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			conexaoAuxiliar.rollback();
			throw new ProjetoException(ex, this.getClass().getName());
		}
		return listaIdsAtendimentos1;
	}
	
	private void desativarFuncionario(Long idProfissional, Connection conexaoAuxiliar)
			throws ProjetoException, SQLException {

		String sql = "UPDATE acl.funcionarios SET ativo = 'N' where id_funcionario = ?";

		try {
			PreparedStatement ps = conexaoAuxiliar.prepareStatement(sql);

			ps.setLong(1, idProfissional);
			ps.executeUpdate();
		} catch (SQLException sqle) {
			conexaoAuxiliar.rollback();
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			conexaoAuxiliar.rollback();
			throw new ProjetoException(ex, this.getClass().getName());
		} 
	}
	
	private Integer buscaIdSituacaoPadraoDoAfastamento(Connection conexaoAuxiliar,
			String motivoAfastamento) throws ProjetoException, SQLException {

		String sql = "select pe.situacao_padrao_falta_profissional, pe.situacao_padrao_licenca_medica, pe.situacao_padrao_ferias, situacao_padrao_desligamento_profissional  "
				+ " from hosp.parametro_empresa pe where pe.id_empresa = ?;";
		
		FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
				.getSessionMap().get("obj_usuario");

		Integer idSituacao = null;

		try {
			PreparedStatement stm = conexaoAuxiliar.prepareStatement(sql);
			stm.setInt(1, user_session.getUnidade().getCodEmpresa());
			ResultSet rs = stm.executeQuery();

			if (rs.next()) {
				if (motivoAfastamento.equals("FE"))
					idSituacao = rs.getInt("situacao_padrao_ferias");
				else if (motivoAfastamento.equals("LM"))
					idSituacao = rs.getInt("situacao_padrao_licenca_medica");
				else if (motivoAfastamento.equals("FA"))
					idSituacao = rs.getInt("situacao_padrao_falta_profissional");
				else if (motivoAfastamento.equals("DE"))
					idSituacao = rs.getInt("situacao_padrao_desligamento_profissional");
			}

		} catch (SQLException sqle) {
			conexaoAuxiliar.rollback();
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(),
					sqle);
		} catch (Exception ex) {
			conexaoAuxiliar.rollback();
			throw new ProjetoException(ex, this.getClass().getName());
		}
		return idSituacao;
	}
	
	private void atualizaSituacaoAtendimentosDoProfissionalAfastado(Connection conexaoAuxiliar,
			List<Integer> listaIdsAtendimentos1, Integer idSituacaoAtendimento) throws ProjetoException, SQLException {

		String sql = "UPDATE hosp.atendimentos1 SET id_situacao_atendimento = ? WHERE id_atendimentos1 = ? ";

		try {
			for (Integer idAtendimento1 : listaIdsAtendimentos1) {
				PreparedStatement stm = conexaoAuxiliar.prepareStatement(sql);
				stm.setInt(1, idSituacaoAtendimento);
				stm.setInt(2, idAtendimento1);
				stm.executeUpdate();
			}
		} catch (SQLException sqle) {
			conexaoAuxiliar.rollback();
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(),
					sqle);
		} catch (Exception ex) {
			conexaoAuxiliar.rollback();
			throw new ProjetoException(ex, this.getClass().getName());
		}
	}

	public List<AfastamentoProfissional> listarAfastamentoProfissionais() throws ProjetoException {

		List<AfastamentoProfissional> lista = new ArrayList<>();

		String sql = "SELECT a.id, a.tipo_afastamento, a.id_funcionario_afastado, f.descfuncionario, "
				+ "a.inicio_afastamento, a.fim_afastamento, a.motivo_afastamento, a.turno, "
				+ "CASE WHEN a.motivo_afastamento = 'FE' THEN 'Férias' "
				+ "WHEN a.motivo_afastamento = 'LM' THEN 'Licença Médica' WHEN a.motivo_afastamento = 'DE' THEN 'Desligamento' WHEN a.motivo_afastamento = 'FA' THEN 'Falta' END AS motivo_afastamento_extenso "
				+ "FROM adm.afastamento_funcionario a "
				+ "JOIN acl.funcionarios f ON (a.id_funcionario_afastado = f.id_funcionario) "
				+ "ORDER BY a.inicio_afastamento desc";

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
		} catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				//comentado walter erro log ex.printStackTrace();
			}
		}
		return lista;
	}

	public boolean verificaSeExisteAfastamentoProfissionalNoPeriodo(AfastamentoProfissional afastamento) throws ProjetoException {
		boolean existeAfastamentoProfissionalNoPeriodo = false;

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
		} catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				//comentado walter erro log ex.printStackTrace();
			}
		}
		return existeAfastamentoProfissionalNoPeriodo;
	}

	public AfastamentoProfissional listarAfastamentoProfissionalPorId(int id) throws ProjetoException {

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

		} catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				//comentado walter erro log ex.printStackTrace();
			}
		}
		return afastamentoProfissional;
	}

	public Boolean excluirAfastamentoProfissional(int id) throws ProjetoException {

		Boolean retorno = false;

		String sql = "delete from adm.afastamento_funcionario where id = ?";

		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setLong(1, id);
			stmt.execute();
			con.commit();
			retorno = true;
		} catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				con.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return retorno;
	}

	public AfastamentoProfissional carregarAfastamentoPeloId(int idAfastamento) throws ProjetoException {

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

		} catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				//comentado walter erro log ex.printStackTrace();
			}
		}
		return afastamento;
	}
	
	public boolean tiposDeAfastamentoPossuemSituacaoAtendimento() throws ProjetoException {

		String sql = "select exists (select pe.id from hosp.parametro_empresa pe " + 
				"	where id_empresa = ? and " + 
				"	(pe.situacao_padrao_falta_profissional is not null " + 
				"	and pe.situacao_padrao_licenca_medica is not null " + 
				"	and pe.situacao_padrao_ferias is not null)) as possui;";

		boolean possui = false;
		FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
				.getSessionMap().get("obj_usuario");
		
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stm = con.prepareStatement(sql);
			stm.setInt(1, user_session.getUnidade().getCodEmpresa());
			ResultSet rs = stm.executeQuery();

			if (rs.next()) {
				possui = rs.getBoolean("possui");
			}

		} catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				//comentado walter erro log ex.printStackTrace();
			}
		}
		return possui;
	}
}
