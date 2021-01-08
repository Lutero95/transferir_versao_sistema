package br.gov.al.maceio.sishosp.hosp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;
import br.gov.al.maceio.sishosp.comum.util.TratamentoErrosUtil;
import br.gov.al.maceio.sishosp.comum.util.VerificadorUtil;
import br.gov.al.maceio.sishosp.hosp.model.ConfiguracaoProducaoBpaBean;
import br.gov.al.maceio.sishosp.hosp.model.UnidadeBean;

public class ConfiguracaoProducaoBpaDAO {

	private Connection con;

	public List<ConfiguracaoProducaoBpaBean> listarConfiguracoesBpa() throws ProjetoException {
		List<ConfiguracaoProducaoBpaBean> lista = new ArrayList<>();
		
		String sql = "SELECT id, descricao, status "+
				" FROM hosp.configuracao_producao_bpa where status = 'A';";
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement ps = con.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				ConfiguracaoProducaoBpaBean configuracaoProducaoBpa = new ConfiguracaoProducaoBpaBean();
				configuracaoProducaoBpa.setId(rs.getInt("id"));
				configuracaoProducaoBpa.setDescricao(rs.getString("descricao"));
				configuracaoProducaoBpa.setStatus(rs.getString("status"));
				lista.add(configuracaoProducaoBpa);
			}
		} catch (SQLException ex2) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(ex2), this.getClass().getName(), ex2);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return lista;
	}
	
	public ConfiguracaoProducaoBpaBean buscaConfiguracoesBpa(Integer id) throws ProjetoException {

		ConfiguracaoProducaoBpaBean configuracaoProducaoBpa = new ConfiguracaoProducaoBpaBean();
		String sql = "SELECT id, descricao, status "+
				" FROM hosp.configuracao_producao_bpa where status = 'A' and id = ?;";
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				configuracaoProducaoBpa.setId(rs.getInt("id"));
				configuracaoProducaoBpa.setDescricao(rs.getString("descricao"));
				configuracaoProducaoBpa.setStatus(rs.getString("status"));
				configuracaoProducaoBpa.setListaUnidades(listaUnidadesDaConfiguracoesBpa(id, con));
			}
		} catch (SQLException ex2) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(ex2), this.getClass().getName(), ex2);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return configuracaoProducaoBpa;
	}
	
	private List<UnidadeBean> listaUnidadesDaConfiguracoesBpa(Integer idConfiguracao, Connection conexaoAuxiliar)
			throws ProjetoException, SQLException {
		List<UnidadeBean> lista = new ArrayList<>();
		
		String sql = "select u.id, u.nome from hosp.configuracao_producao_bpa_unidade cpbu " + 
				"	join hosp.unidade u on cpbu.id_unidade = u.id " + 
				"	where cpbu.id_configuracao_producao_bpa = ?";
		try {
			PreparedStatement ps = conexaoAuxiliar.prepareStatement(sql);
			ps.setInt(1, idConfiguracao);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				UnidadeBean unidade = new UnidadeBean();
				unidade.setId(rs.getInt("id"));
				unidade.setNomeUnidade(rs.getString("nome"));
				lista.add(unidade);
			}
		} catch (SQLException ex2) {
			conexaoAuxiliar.rollback();
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(ex2), this.getClass().getName(), ex2);
		} catch (Exception ex) {
			conexaoAuxiliar.rollback();
			throw new ProjetoException(ex, this.getClass().getName());
		}
		return lista;
	}
	
	public boolean gravarConfiguracoesBpa(ConfiguracaoProducaoBpaBean configuracaoProducaoBpa)
			throws ProjetoException {

		boolean cadastrou = false; 
		String sql = "INSERT INTO hosp.configuracao_producao_bpa " + 
				"(descricao, operador_gravacao, datahora_gravacao, status) " + 
				"VALUES(?, ?, CURRENT_TIMESTAMP, 'A') returning id; ";
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, configuracaoProducaoBpa.getDescricao());
			ps.setInt(2, configuracaoProducaoBpa.getOperadorGravacao());
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				configuracaoProducaoBpa.setId(rs.getInt("id"));
				gravarUnidadesConfiguracoesBpa(configuracaoProducaoBpa, con);
			}
			con.commit();
			cadastrou = true;
		} catch (SQLException ex2) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(ex2), this.getClass().getName(), ex2);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return cadastrou;
	}
	
	private void gravarUnidadesConfiguracoesBpa 
		(ConfiguracaoProducaoBpaBean configuracaoProducaoBpa, Connection conexaoAuxiliar)
			throws ProjetoException, SQLException {

		String sql = "INSERT INTO hosp.configuracao_producao_bpa_unidade " + 
				"(id_configuracao_producao_bpa, id_unidade) " + 
				"VALUES(?, ?);";
		try {
			PreparedStatement ps = conexaoAuxiliar.prepareStatement(sql);
			for (UnidadeBean unidade : configuracaoProducaoBpa.getListaUnidades()) {
				ps.setInt(1, configuracaoProducaoBpa.getId());
				ps.setInt(2, unidade.getId());
				ps.executeUpdate();
			}
		} catch (SQLException ex2) {
			conexaoAuxiliar.rollback();
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(ex2), this.getClass().getName(), ex2);
		} catch (Exception ex) {
			conexaoAuxiliar.rollback();
			throw new ProjetoException(ex, this.getClass().getName());
		} 
	}
	
	public String  retornaUnidadesQueJaPossuemConfiguracao(ConfiguracaoProducaoBpaBean configuracaoProducaoBpa)
			throws ProjetoException {

		String sql = "select exists (select cpbu.id_configuracao_producao_bpa from hosp.configuracao_producao_bpa_unidade cpbu " + 
				"				join hosp.configuracao_producao_bpa cp on cpbu.id_configuracao_producao_bpa = cp.id " + 
				"				where cp.status = 'A' and cpbu.id_unidade = ? ";
		
		String sqlCondicaoEdicao = "and cpbu.id_configuracao_producao_bpa != ? ";
		
		if(!VerificadorUtil.verificarSeObjetoNuloOuZero(configuracaoProducaoBpa.getId())) {
			sql += sqlCondicaoEdicao;
		}
		
		sql+= ") existe;";
		
		String nomeUnidades = "";
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement ps = con.prepareStatement(sql);
			for(UnidadeBean unidade : configuracaoProducaoBpa.getListaUnidades()) {
				ps.setInt(1, unidade.getId());
				if(!VerificadorUtil.verificarSeObjetoNuloOuZero(configuracaoProducaoBpa.getId())) {
					ps.setInt(2, configuracaoProducaoBpa.getId());
				}
				ResultSet rs = ps.executeQuery();
				if(rs.next()) {
					if (rs.getBoolean("existe")) {
						nomeUnidades += unidade.getNomeUnidade() + "; ";
					}
				}
			}
			
		} catch (SQLException ex2) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(ex2), this.getClass().getName(), ex2);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return nomeUnidades;
	}
	
	public boolean alterarConfiguracoesBpa(ConfiguracaoProducaoBpaBean configuracaoProducaoBpa)
			throws ProjetoException {

		boolean cadastrou = false; 
		String sql = "UPDATE hosp.configuracao_producao_bpa " + 
				"SET descricao = ?, operador_gravacao = ?, datahora_gravacao = CURRENT_TIMESTAMP " + 
				"WHERE id = ?;";
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, configuracaoProducaoBpa.getDescricao());
			ps.setInt(2, configuracaoProducaoBpa.getOperadorGravacao());
			ps.setInt(3, configuracaoProducaoBpa.getId());
			ps.executeUpdate();

			excluirUnidadesConfiguracoesBpa(configuracaoProducaoBpa.getId(), con);
			gravarUnidadesConfiguracoesBpa(configuracaoProducaoBpa, con);
			
			con.commit();
			cadastrou = true;
		} catch (SQLException ex2) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(ex2), this.getClass().getName(), ex2);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return cadastrou;
	}

	private void excluirUnidadesConfiguracoesBpa(Integer idConfiguracao, Connection conexaoAuxiliar)
			throws ProjetoException, SQLException {

		String sql = "DELETE FROM hosp.configuracao_producao_bpa_unidade WHERE id_configuracao_producao_bpa = ?";
		try {
			PreparedStatement ps = conexaoAuxiliar.prepareStatement(sql);
			ps.setInt(1, idConfiguracao);
			ps.executeUpdate();
		} catch (SQLException ex2) {
			conexaoAuxiliar.rollback();
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(ex2), this.getClass().getName(), ex2);
		} catch (Exception ex) {
			conexaoAuxiliar.rollback();
			throw new ProjetoException(ex, this.getClass().getName());
		}
	}
	
	public boolean excluirConfiguracoesBpa(ConfiguracaoProducaoBpaBean configuracaoProducaoBpa)
			throws ProjetoException {

		boolean excluiu = false; 
		String sql = "UPDATE hosp.configuracao_producao_bpa " + 
				"SET status='D', operador_exclusao = ?, datahora_exclusao = CURRENT_TIMESTAMP " + 
				"WHERE id = ?;";
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, configuracaoProducaoBpa.getOperadorExclusao());
			ps.setInt(2, configuracaoProducaoBpa.getId());
			ps.executeUpdate();
			con.commit();
			excluiu = true;
		} catch (SQLException ex2) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(ex2), this.getClass().getName(), ex2);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return excluiu;
	}
}
