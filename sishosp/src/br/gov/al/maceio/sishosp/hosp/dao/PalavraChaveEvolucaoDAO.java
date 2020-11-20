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
import br.gov.al.maceio.sishosp.hosp.model.CidBean;
import br.gov.al.maceio.sishosp.hosp.model.PalavraChaveEvolucaoBean;

public class PalavraChaveEvolucaoDAO {
	
	Connection con = null;
	PreparedStatement ps = null;
	
	public boolean gravarPalavraChaveEvolucao(PalavraChaveEvolucaoBean palavraChaveEvolucao) throws ProjetoException {
		Boolean retorno = false;
		String sql = "INSERT INTO hosp.palavra_chave_evolucao (descricao, atendimento_realizado, excluido) " + 
				"VALUES(?, ?, false); ";

		try {
			con = ConnectionFactory.getConnection();
			ps = con.prepareStatement(sql);

			ps.setString(1, palavraChaveEvolucao.getDescricao());
			ps.setBoolean(2, palavraChaveEvolucao.isAtendimentoRealizado());
			ps.executeUpdate();
			con.commit();
			retorno = true;
		}

		catch ( SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		}
		catch (Exception e) {
			throw new ProjetoException(e, this.getClass().getName());
		}finally {
			try {
				con.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return retorno;
	}
	
	public boolean alterarPalavraChaveEvolucao(PalavraChaveEvolucaoBean palavraChaveEvolucao) throws ProjetoException {
		Boolean retorno = false;
		String sql = "UPDATE hosp.palavra_chave_evolucao SET descricao = ?, atendimento_realizado = ? " + 
				"WHERE id = ?";

		try {
			con = ConnectionFactory.getConnection();
			ps = con.prepareStatement(sql);

			ps.setString(1, palavraChaveEvolucao.getDescricao());
			ps.setBoolean(2, palavraChaveEvolucao.isAtendimentoRealizado());
			ps.setInt(3, palavraChaveEvolucao.getId());
			ps.executeUpdate();
			con.commit();
			retorno = true;
		}

		catch ( SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		}
		catch (Exception e) {
			throw new ProjetoException(e, this.getClass().getName());
		}finally {
			try {
				con.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return retorno;
	}
	
	public boolean excluirPalavraChaveEvolucao(Integer id) throws ProjetoException {
		Boolean retorno = false;
		String sql = "UPDATE hosp.palavra_chave_evolucao SET excluido = true WHERE id = ?";

		try {
			con = ConnectionFactory.getConnection();
			ps = con.prepareStatement(sql);

			ps.setInt(1, id);
			ps.executeUpdate();
			con.commit();
			retorno = true;
		}

		catch ( SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		}
		catch (Exception e) {
			throw new ProjetoException(e, this.getClass().getName());
		}finally {
			try {
				con.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return retorno;
	}
	
	public PalavraChaveEvolucaoBean buscarPalavraChaveEvolucao(Integer id) throws ProjetoException {

		PalavraChaveEvolucaoBean palavraChaveEvolucao = new PalavraChaveEvolucaoBean();
		String sql = "SELECT id, descricao, atendimento_realizado " + 
				"FROM hosp.palavra_chave_evolucao where excluido = false and id = ?;";
		try {
			con = ConnectionFactory.getConnection();
			ps = con.prepareStatement(sql);
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				palavraChaveEvolucao.setId(rs.getInt("id"));
				palavraChaveEvolucao.setDescricao(rs.getString("descricao"));
				palavraChaveEvolucao.setAtendimentoRealizado(rs.getBoolean("atendimento_realizado"));
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
		return palavraChaveEvolucao;
	}
	
	public List<PalavraChaveEvolucaoBean> listarPalavraChaveEvolucao() throws ProjetoException {
		List<PalavraChaveEvolucaoBean> lista = new ArrayList<>();
		String sql = "SELECT id, descricao, atendimento_realizado " + 
				"FROM hosp.palavra_chave_evolucao where excluido = false;";
		try {
			con = ConnectionFactory.getConnection();
			ps = con.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				PalavraChaveEvolucaoBean palavraChaveEvolucao = new PalavraChaveEvolucaoBean();
				mapearResultsetPalavraChave(lista, rs, palavraChaveEvolucao);
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
	
	public List<PalavraChaveEvolucaoBean> listarPalavraChaveEvolucaoFiltro(String campoBusca) throws ProjetoException {
		List<PalavraChaveEvolucaoBean> lista = new ArrayList<>();
		String sql = "SELECT id, descricao, atendimento_realizado " + 
				"FROM hosp.palavra_chave_evolucao where excluido = false "+
				"and descricao ilike ?;";
		try {
			con = ConnectionFactory.getConnection();
			ps = con.prepareStatement(sql);
			ps.setString(1, "%"+campoBusca+"%");
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				PalavraChaveEvolucaoBean palavraChaveEvolucao = new PalavraChaveEvolucaoBean();
				mapearResultsetPalavraChave(lista, rs, palavraChaveEvolucao);
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

	private void mapearResultsetPalavraChave(List<PalavraChaveEvolucaoBean> lista, ResultSet rs,
			PalavraChaveEvolucaoBean palavraChaveEvolucao) throws SQLException {
		palavraChaveEvolucao.setId(rs.getInt("id"));
		palavraChaveEvolucao.setDescricao(rs.getString("descricao"));
		palavraChaveEvolucao.setAtendimentoRealizado(rs.getBoolean("atendimento_realizado"));

		lista.add(palavraChaveEvolucao);
	}
	
	public List<String> existePalavraDigitadaDeOutroTipoDeAtendimento(String[] palavrasDigitadas, Integer idSituacaoAtendimento) 
			throws ProjetoException {
		
		List<String> palavrasEncontradas = new ArrayList<>();
		
		String sql = "	select pce.descricao from hosp.palavra_chave_evolucao pce " + 
				"		where pce.excluido = false and pce.atendimento_realizado != " + 
				"			(select sa.atendimento_realizado from hosp.situacao_atendimento sa where sa.id = ?)  " + 
				"		and (pce.descricao ilike unaccent(?) ";
		
		String filtroDescricao = "or pce.descricao ilike unaccent(?) ";
		String finalSql = " ) order by descricao asc;";
		
		for(int i = 1; i < palavrasDigitadas.length; i++) {
			sql += filtroDescricao;
		}
		sql += finalSql;
		
		try {
			con = ConnectionFactory.getConnection();
			ps = con.prepareStatement(sql);
			ps.setInt(1, idSituacaoAtendimento);
			int contador = 2;
			
			for (String palavra : palavrasDigitadas) {
				ps.setString(contador, palavra);
				contador++;
			}
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				palavrasEncontradas.add(rs.getString("descricao"));
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
		return palavrasEncontradas;
	}
}
