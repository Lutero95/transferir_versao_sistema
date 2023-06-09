package br.gov.al.maceio.sishosp.hosp.dao;

import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;
import br.gov.al.maceio.sishosp.comum.util.TratamentoErrosUtil;
import br.gov.al.maceio.sishosp.comum.util.VerificadorUtil;
import br.gov.al.maceio.sishosp.hosp.model.SituacaoAtendimentoBean;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SituacaoAtendimentoDAO {
	private Connection conexao = null;
	Connection con = null;
	PreparedStatement ps = null;

	public Boolean gravarSituacaoAtendimento(SituacaoAtendimentoBean situacaoAtendimento) throws ProjetoException {
		Boolean retorno = false;
		String sql = "INSERT INTO hosp.situacao_atendimento  " +
				"(descricao, atendimento_realizado, abono_falta, paciente_faltou, falta_profissional) " +
				"VALUES(?, ?,?, ?,?);";

		try {
			con = ConnectionFactory.getConnection();
			ps = con.prepareStatement(sql);
			ps.setString(1, situacaoAtendimento.getDescricao().toUpperCase());
			ps.setBoolean(2, situacaoAtendimento.getAtendimentoRealizado());
			ps.setBoolean(3, situacaoAtendimento.isAbonoFalta());
			ps.setBoolean(4, situacaoAtendimento.isPacienteFaltou());
			ps.setBoolean(5, situacaoAtendimento.isFaltaProfissional());
			ps.execute();
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

	public Boolean alterarSituacaoAtendimento(SituacaoAtendimentoBean situacao) throws ProjetoException{
		Boolean retorno = false;
		String sql = "UPDATE hosp.situacao_atendimento " +
				"SET descricao = ?, atendimento_realizado = ?, abono_falta=?, paciente_faltou = ?, falta_profissional=? " +
				"WHERE id = ?";
		try {
			con = ConnectionFactory.getConnection();
			ps = con.prepareStatement(sql);
			ps.setString(1, situacao.getDescricao().toUpperCase());
			ps.setBoolean(2, situacao.getAtendimentoRealizado());
			ps.setBoolean(3, situacao.isAbonoFalta());
			ps.setBoolean(4, situacao.isPacienteFaltou());
			ps.setBoolean(5, situacao.isFaltaProfissional());
			ps.setInt(6, situacao.getId());
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

	public Boolean excluirSituacaoAtendimento(Integer id) throws ProjetoException {
		Boolean retorno = false;
		String sql = "DELETE FROM hosp.situacao_atendimento WHERE id = ?";

		try {
			con = ConnectionFactory.getConnection();
			ps = con.prepareStatement(sql);
			ps.setInt(1, id);
			ps.execute();
			con.commit();
			retorno = true;
		} catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		}finally {
			try {
				con.close();
			} catch (Exception ex) {
				//comentado walter erro log ex.printStackTrace();
			}
		}
		return retorno;
	}

	public List<SituacaoAtendimentoBean> listarSituacaoAtendimento() throws ProjetoException {

		String sql = "select sa.id, sa.descricao, sa.atendimento_realizado, abono_falta, paciente_faltou, falta_profissional  " +
				"from hosp.situacao_atendimento sa  order by sa.descricao ";

		List<SituacaoAtendimentoBean> listaSituacoes = new ArrayList<SituacaoAtendimentoBean>();

		try {
			conexao = ConnectionFactory.getConnection();
			ps = conexao.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				SituacaoAtendimentoBean situacaoAtendimento = new SituacaoAtendimentoBean();
				situacaoAtendimento.setId(rs.getInt("id"));
				situacaoAtendimento.setDescricao(rs.getString("descricao"));
				situacaoAtendimento.setAtendimentoRealizado(rs.getBoolean("atendimento_realizado"));
				situacaoAtendimento.setAbonoFalta(rs.getBoolean("abono_falta"));
				situacaoAtendimento.setPacienteFaltou(rs.getBoolean("paciente_faltou"));
				situacaoAtendimento.setFaltaProfissional(rs.getBoolean("falta_profissional"));
				listaSituacoes.add(situacaoAtendimento);
			}
		} catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				conexao.close();
			} catch (Exception ex) {
				//comentado walter erro log ex.printStackTrace();
			}
		}
		return listaSituacoes;
	}
	
	public List<SituacaoAtendimentoBean> listarSituacaoAtendimentoFaltaPaciente() throws ProjetoException {

		String sql = "select sa.id, sa.descricao, sa.atendimento_realizado, sa.abono_falta, sa.paciente_faltou  " +
				"from hosp.situacao_atendimento sa where sa.paciente_faltou = true";

		List<SituacaoAtendimentoBean> listaSituacoes = new ArrayList<SituacaoAtendimentoBean>();

		try {
			conexao = ConnectionFactory.getConnection();
			ps = conexao.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				mapearResultSetSituacaoAtendimento(listaSituacoes, rs);
			}
		} catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				conexao.close();
			} catch (Exception ex) {
				//comentado walter erro log ex.printStackTrace();
			}
		}
		return listaSituacoes;
	}

	public List<SituacaoAtendimentoBean> listarSituacaoAtendimentoFiltro(Boolean atendimentoRealizado) throws ProjetoException {

		String sql = "select sa.id, sa.descricao, sa.atendimento_realizado, sa.abono_falta, sa.paciente_faltou  " +
				"from hosp.situacao_atendimento sa where sa.atendimento_realizado = ? and sa.id not in (select sa2.id from hosp.situacao_atendimento sa2 where ((abono_falta is true) ))  order by sa.descricao ";

		List<SituacaoAtendimentoBean> listaSituacoes = new ArrayList<SituacaoAtendimentoBean>();

		try {
			conexao = ConnectionFactory.getConnection();
			ps = conexao.prepareStatement(sql);
			ps.setBoolean(1, atendimentoRealizado);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				mapearResultSetSituacaoAtendimento(listaSituacoes, rs);
			}
		} catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				conexao.close();
			} catch (Exception ex) {
				//comentado walter erro log ex.printStackTrace();
			}
		}
		return listaSituacoes;
	}
	
	public List<SituacaoAtendimentoBean> listarSituacaoAtendimentoFiltroRelatorioAtendimentoProgramaGrupo() throws ProjetoException {

		String sql = "select sa.id, sa.descricao, sa.atendimento_realizado, sa.abono_falta, sa.paciente_faltou  " +
				"from hosp.situacao_atendimento sa where sa.atendimento_realizado is true and sa.id not in (select sa2.id from hosp.situacao_atendimento sa2 where abono_falta is true)  order by sa.descricao ";

		List<SituacaoAtendimentoBean> listaSituacoes = new ArrayList<SituacaoAtendimentoBean>();

		try {
			conexao = ConnectionFactory.getConnection();
			ps = conexao.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				mapearResultSetSituacaoAtendimento(listaSituacoes, rs);
			}
		} catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				conexao.close();
			} catch (Exception ex) {
				//comentado walter erro log ex.printStackTrace();
			}
		}
		return listaSituacoes;
	}

	private void mapearResultSetSituacaoAtendimento(List<SituacaoAtendimentoBean> listaSituacoes, ResultSet rs)
			throws SQLException {
		SituacaoAtendimentoBean situacaoAtendimento = new SituacaoAtendimentoBean();
		situacaoAtendimento.setId(rs.getInt("id"));
		situacaoAtendimento.setDescricao(rs.getString("descricao"));
		situacaoAtendimento.setAtendimentoRealizado(rs.getBoolean("atendimento_realizado"));
		situacaoAtendimento.setAbonoFalta(rs.getBoolean("abono_falta"));
		situacaoAtendimento.setPacienteFaltou(rs.getBoolean("paciente_faltou"));
		listaSituacoes.add(situacaoAtendimento);
	}

	public List<SituacaoAtendimentoBean> buscarSituacaoAtendimento(String descricao) throws ProjetoException {

		String sql = "select sa.id, sa.descricao, sa.atendimento_realizado, sa.abono_falta, sa.paciente_faltou " +
				"from hosp.situacao_atendimento sa where sa.descricao ilike ? order by sa.descricao ";
		List<SituacaoAtendimentoBean> listaSituacoes = new ArrayList<SituacaoAtendimentoBean>();
		try {
			con = ConnectionFactory.getConnection();
			ps = con.prepareStatement(sql);
			ps.setString(1, "%"+descricao+"%");
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				mapearResultSetSituacaoAtendimento(listaSituacoes, rs);
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
		return listaSituacoes;
	}

	public SituacaoAtendimentoBean buscaSituacaoAtendimentoPorId(Integer idSituacao) throws ProjetoException {

		String sql = "select sa.id, sa.descricao, sa.atendimento_realizado, sa.abono_falta, sa.paciente_faltou, sa.falta_profissional " +
				"from hosp.situacao_atendimento sa where sa.id = ?";

		SituacaoAtendimentoBean situacaoAtendimento = new SituacaoAtendimentoBean();
		try {
			conexao = ConnectionFactory.getConnection();
			ps = conexao.prepareStatement(sql);
			ps.setInt(1, idSituacao);
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				situacaoAtendimento.setId(rs.getInt("id"));
				situacaoAtendimento.setDescricao(rs.getString("descricao"));
				situacaoAtendimento.setAtendimentoRealizado(rs.getBoolean("atendimento_realizado"));
				situacaoAtendimento.setAbonoFalta(rs.getBoolean("abono_falta"));
				situacaoAtendimento.setPacienteFaltou(rs.getBoolean("paciente_faltou"));
				situacaoAtendimento.setFaltaProfissional(rs.getBoolean("falta_profissional"));
			}
		} catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				conexao.close();
			} catch (Exception ex) {
				//comentado walter erro log ex.printStackTrace();
			}
		}
		return situacaoAtendimento;
	}

	public Boolean existeOutraSituacaoComAbonoFalta(Integer idSituacao) throws ProjetoException {

		String sqlParaEdicao = "select exists " +
				"	(select sa.id from hosp.situacao_atendimento sa where sa.abono_falta = true and sa.id != ?) " +
				"as existe_situacao_com_abono_falta";

		String sqlParaCadastro = "select exists " +
				"	(select sa.id from hosp.situacao_atendimento sa where sa.abono_falta = true) " +
				"as existe_situacao_com_abono_falta";

		Boolean existeSituacaoComAbonoFalta = true;

		try {
			conexao = ConnectionFactory.getConnection();
			if(VerificadorUtil.verificarSeObjetoNuloOuZero(idSituacao))
				ps = conexao.prepareStatement(sqlParaCadastro);
			else {
				ps = conexao.prepareStatement(sqlParaEdicao);
				ps.setInt(1, idSituacao);
			}
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				existeSituacaoComAbonoFalta = rs.getBoolean("existe_situacao_com_abono_falta");
			}
		} catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		}finally {
			try {
				conexao.close();
			} catch (Exception ex) {
				//comentado walter erro log ex.printStackTrace();
			}
		}
		return existeSituacaoComAbonoFalta;
	}

	public Boolean existeSituacaoAtendimentoAbonoFalta() throws ProjetoException {
		String sql = "select exists " +
				"	(select sa.id from hosp.situacao_atendimento sa where sa.abono_falta = true) " +
				"as existe_situacao_com_abono_falta";

		Boolean existeSituacaoComAbonoFalta = true;

		try {
			conexao = ConnectionFactory.getConnection();
			ps = conexao.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				existeSituacaoComAbonoFalta = rs.getBoolean("existe_situacao_com_abono_falta");
			}
		} catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		}finally {
			try {
				conexao.close();
			} catch (Exception ex) {
				//comentado walter erro log ex.printStackTrace();
			}
		}
		return existeSituacaoComAbonoFalta;
	}

	public String buscaDescricaoSituacaoAtendimento(Integer idSituacao, Connection conexaoAuxiliar) 
			throws ProjetoException, SQLException {

		String sql = "select sa.descricao from hosp.situacao_atendimento sa where sa.id = ? ";
		String descricao = "";
		try {
			PreparedStatement ps = conexaoAuxiliar.prepareStatement(sql);
			ps.setInt(1, idSituacao);
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				descricao = rs.getString("descricao");
			}
		} catch (SQLException sqle) {
			conexaoAuxiliar.rollback();
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			conexaoAuxiliar.rollback();
			throw new ProjetoException(ex, this.getClass().getName());
		}
		return descricao;
	}
}
