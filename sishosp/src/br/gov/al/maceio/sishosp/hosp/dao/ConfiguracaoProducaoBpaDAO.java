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
}
