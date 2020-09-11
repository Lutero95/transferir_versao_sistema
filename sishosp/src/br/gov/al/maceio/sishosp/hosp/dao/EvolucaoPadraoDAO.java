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
import br.gov.al.maceio.sishosp.hosp.model.EvolucaoPadraoBean;

public class EvolucaoPadraoDAO {
	
	private Connection con = null;
	
	public boolean gravarEvolucaoPadrao(EvolucaoPadraoBean evolucaoPadrao) throws ProjetoException {
		Boolean retorno = false;
		String sql = "INSERT INTO hosp.evolucao_padrao " + 
					 "(id_funcionario, titulo, descricao) VALUES(?, ?, ?); " ;

		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setLong(1, evolucaoPadrao.getIdFuncionario());
			ps.setString(2, evolucaoPadrao.getTitulo());
			ps.setString(3, evolucaoPadrao.getDescricao());
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
	
	public boolean alterarEvolucaoPadrao(EvolucaoPadraoBean evolucaoPadrao) throws ProjetoException {
		Boolean retorno = false;
		String sql = "UPDATE hosp.evolucao_padrao SET id_funcionario = ?, titulo = ?, descricao = ? WHERE id = ?; ";

		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setLong(1, evolucaoPadrao.getIdFuncionario());
			ps.setString(2, evolucaoPadrao.getTitulo());
			ps.setString(3, evolucaoPadrao.getDescricao());
			ps.setInt(4, evolucaoPadrao.getId());
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
	
	public List<EvolucaoPadraoBean> listarEvolucoesPadrao(Long idFuncionario) throws ProjetoException {
		
		String sql = "SELECT id, id_funcionario, titulo, descricao FROM hosp.evolucao_padrao WHERE id_funcionario = ? order by titulo";
		List<EvolucaoPadraoBean> listaEvolucoes = new ArrayList<>();

		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setLong(1, idFuncionario);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				EvolucaoPadraoBean evolucaoPadrao = new EvolucaoPadraoBean();
				mapearResultSet(rs, evolucaoPadrao);
				
				listaEvolucoes.add(evolucaoPadrao);
			}
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
		return listaEvolucoes;
	}

	public boolean excluirEvolucaoPadrao(Integer id) throws ProjetoException {
		Boolean retorno = false;
		String sql = "DELETE FROM hosp.evolucao_padrao WHERE id = ?;";

		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement ps = con.prepareStatement(sql);
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
	
	public EvolucaoPadraoBean buscarEvolucaoPadrao(Integer id) throws ProjetoException {
		
		String sql = "SELECT id, id_funcionario, titulo, descricao FROM hosp.evolucao_padrao WHERE id = ?";
		EvolucaoPadraoBean evolucaoPadrao = new EvolucaoPadraoBean();
		
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				mapearResultSet(rs, evolucaoPadrao);
			}
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
		return evolucaoPadrao;
	}
	
	
	private void mapearResultSet(ResultSet rs, EvolucaoPadraoBean evolucaoPadrao) throws SQLException {
		evolucaoPadrao.setId(rs.getInt("id"));
		evolucaoPadrao.setIdFuncionario(rs.getLong("id_funcionario"));
		evolucaoPadrao.setTitulo(rs.getString("titulo"));
		evolucaoPadrao.setDescricao(rs.getString("descricao"));
	}
}
