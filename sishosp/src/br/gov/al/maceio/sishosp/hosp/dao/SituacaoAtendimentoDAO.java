package br.gov.al.maceio.sishosp.hosp.dao;

import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;
import br.gov.al.maceio.sishosp.comum.util.JSFUtil;
import br.gov.al.maceio.sishosp.comum.util.VerificadorUtil;
import br.gov.al.maceio.sishosp.hosp.model.SituacaoAtendimentoBean;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class SituacaoAtendimentoDAO {
	private Connection conexao = null;
	Connection con = null;
	PreparedStatement ps = null;

	public Boolean gravarSituacaoAtendimento(SituacaoAtendimentoBean situacaoAtendimento) {
		Boolean retorno = false;
		String sql = "INSERT INTO hosp.situacao_atendimento " + 
				"(descricao, atendimento_realizado) " + 
				"VALUES(?, ?);";

		try {
			con = ConnectionFactory.getConnection();
			ps = con.prepareStatement(sql);
			ps.setString(1, situacaoAtendimento.getDescricao().toUpperCase());
			ps.setBoolean(2, situacaoAtendimento.getAtendimentoRealizado());
			ps.execute();
			con.commit();
			retorno = true;
		} catch (Exception ex) {
			JSFUtil.adicionarMensagemErro("Erro ao gravar Situação de Atendimento: " + ex.getMessage(), "Erro");
			ex.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return retorno;
	}

	public Boolean alterarSituacaoAtendimento(SituacaoAtendimentoBean situacao){
		Boolean retorno = false;
		String sql = "UPDATE hosp.situacao_atendimento " + 
				"SET descricao = ?, atendimento_realizado = ?" + 
				"WHERE id = ?";
		try {
			con = ConnectionFactory.getConnection();
			ps = con.prepareStatement(sql);
			ps.setString(1, situacao.getDescricao());
			ps.setBoolean(2, situacao.getAtendimentoRealizado());
			ps.setInt(3, situacao.getId());
			ps.executeUpdate();
			con.commit();
			retorno = true;
		} catch (Exception ex) {
			JSFUtil.adicionarMensagemErro("Erro ao alterar Situação de Atendimento: "+ex.getMessage(), "Erro");
			ex.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return retorno;
	}

	public Boolean excluirSituacaoAtendimento(Integer id) {
		Boolean retorno = false;
		String sql = "DELETE FROM hosp.situacao_atendimento WHERE id = ?";

		try {
			con = ConnectionFactory.getConnection();
			ps = con.prepareStatement(sql);
			ps.setInt(1, id);
			ps.execute();
			con.commit();
			retorno = true;
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return retorno;
	}

	public List<SituacaoAtendimentoBean> listarSituacaoAtendimento() {

		String sql = "select sa.id, sa.descricao, sa.atendimento_realizado " + 
				"from hosp.situacao_atendimento sa order by sa.descricao ";

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
				listaSituacoes.add(situacaoAtendimento);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				conexao.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return listaSituacoes;
	}

	public List<SituacaoAtendimentoBean> buscarSituacaoAtendimento(String descricao) {
		
		String sql = "select sa.id, sa.descricao, sa.atendimento_realizado " + 
				"from hosp.situacao_atendimento sa where sa.descricao ilike ? order by sa.descricao ";
		List<SituacaoAtendimentoBean> listaSituacoes = new ArrayList<SituacaoAtendimentoBean>();
		try {
			con = ConnectionFactory.getConnection();
			ps = con.prepareStatement(sql);
			ps.setString(1, "%"+descricao+"%");
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				SituacaoAtendimentoBean situacaoAtendimento = new SituacaoAtendimentoBean();
				situacaoAtendimento.setId(rs.getInt("id"));
				situacaoAtendimento.setDescricao(rs.getString("descricao"));
				situacaoAtendimento.setAtendimentoRealizado(rs.getBoolean("atendimento_realizado"));
				listaSituacoes.add(situacaoAtendimento);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return listaSituacoes;
	}
	
	public SituacaoAtendimentoBean buscaSituacaoAtendimentoPorId(Integer idSituacao) {

		String sql = "select sa.id, sa.descricao, sa.atendimento_realizado " + 
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
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				conexao.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return situacaoAtendimento;
	}
	
	public Boolean existeOutraSituacaoComAtendimentoRealizado(Integer idSituacao) {

		String sqlParaEdicao = "select exists " + 
				"	(select sa.id from hosp.situacao_atendimento sa where sa.atendimento_realizado = true and sa.id != ?) " + 
				"as existe_situacao_com_atendimento_realizado";
		
		String sqlParaCadastro = "select exists " + 
				"	(select sa.id from hosp.situacao_atendimento sa where sa.atendimento_realizado = true) " + 
				"as existe_situacao_com_atendimento_realizado";
		
		Boolean existeSituacaoComStendimentoRealizado = true;
		
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
				existeSituacaoComStendimentoRealizado = rs.getBoolean("existe_situacao_com_atendimento_realizado");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				conexao.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return existeSituacaoComStendimentoRealizado;
	}

}
