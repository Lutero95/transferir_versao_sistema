package br.gov.al.maceio.sishosp.gestao.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.itextpdf.text.pdf.PdfStructTreeController.returnType;

import br.gov.al.maceio.sishosp.acl.model.Perfil;
import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;
import br.gov.al.maceio.sishosp.comum.util.ConnectionFactoryPublico;
import br.gov.al.maceio.sishosp.comum.util.TratamentoErrosUtil;
import br.gov.al.maceio.sishosp.gestao.enums.TipoInconsistencia;
import br.gov.al.maceio.sishosp.gestao.model.InconsistenciaBean;
import br.gov.al.maceio.sishosp.hosp.model.CidBean;

public class InconsistenciaDAO {



	public boolean gravarInconsistencia(InconsistenciaBean inconsistencia) throws ProjetoException {
		Boolean retorno = false;
		String sql = "INSERT INTO gestao.inconsistencias (titulo, descricao, sql, tipo) VALUES(?, ?, ?, ?) "
				+ " returning id; ";
		Connection conexaoPublico = null;
		Connection conexaoEhosp = null;
		
		try {
			conexaoPublico = ConnectionFactoryPublico.getConnection();
			conexaoEhosp = ConnectionFactory.getConnection();
			
			PreparedStatement ps = conexaoPublico.prepareStatement(sql);
			ps.setString(1, inconsistencia.getTitulo());
			ps.setString(2, inconsistencia.getDescricao());
			ps.setString(3, inconsistencia.getSql());
			ps.setString(4, inconsistencia.getTipoInconsistencia().name());

			ResultSet rs = ps.executeQuery();
			if(rs.next())
				inconsistencia.setId(rs.getInt("id"));
			
			gravarPerfisInconsistencia(inconsistencia, conexaoEhosp);
			
			conexaoPublico.commit();
			conexaoEhosp.commit();
			retorno = true;
		}

		catch ( SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		}
		catch (Exception e) {
			throw new ProjetoException(e, this.getClass().getName());
		}finally {
			try {
				conexaoPublico.close();
				conexaoEhosp.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return retorno;
	}
	
	private void gravarPerfisInconsistencia(InconsistenciaBean inconsistencia, Connection conexaoEhosp) throws ProjetoException, SQLException {
		String sql = "INSERT INTO hosp.inconsistencia_perfil (id_inconsistencia, id_perfil) VALUES(?, ?); ";
		
		try {
			PreparedStatement ps = conexaoEhosp.prepareStatement(sql);
			
			for (Perfil perfil : inconsistencia.getListaPerfis()) {
				ps.setInt(1, inconsistencia.getId());
				ps.setLong(2, perfil.getId());
				ps.executeUpdate();
			}
		}

		catch ( SQLException sqle) {
			conexaoEhosp.rollback();
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		}
		catch (Exception e) {
			conexaoEhosp.rollback();
			throw new ProjetoException(e, this.getClass().getName());
		}
	}
	
	private void removerPerfisInconsistencia(Integer idInconsistencia, Connection conexaoEhosp) throws ProjetoException, SQLException {
		String sql = "DELETE FROM hosp.inconsistencia_perfil WHERE id_inconsistencia = ?";
		
		try {
			PreparedStatement ps = conexaoEhosp.prepareStatement(sql);
			
			ps.setInt(1, idInconsistencia);
			ps.executeUpdate();
		}

		catch ( SQLException sqle) {
			conexaoEhosp.rollback();
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		}
		catch (Exception e) {
			conexaoEhosp.rollback();
			throw new ProjetoException(e, this.getClass().getName());
		}
	}
	
	public List<InconsistenciaBean> listarInconsistencias() throws ProjetoException {
		String sql = "SELECT id, titulo, descricao, sql, tipo FROM gestao.inconsistencias order by descricao; ";
		Connection conexaoPublico = null;
		List<InconsistenciaBean> listaInconsistencia = new ArrayList<>();
		
		try {
			conexaoPublico = ConnectionFactoryPublico.getConnection();
			
			PreparedStatement ps = conexaoPublico.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				InconsistenciaBean inconsistencia = new InconsistenciaBean();
				mapearResultSetInconsistencias(inconsistencia, rs);
				
				listaInconsistencia.add(inconsistencia);
			}
		}

		catch ( SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		}
		catch (Exception e) {
			throw new ProjetoException(e, this.getClass().getName());
		} finally {
			try {
				conexaoPublico.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return listaInconsistencia;
	}
	
	public InconsistenciaBean buscarInconsistenciasPorId(Integer id) throws ProjetoException {
		
		InconsistenciaBean inconsistencia = new InconsistenciaBean();
		String sql = "SELECT id, titulo, descricao, sql, tipo FROM gestao.inconsistencias where id = ? order by descricao; ";
		
		Connection conexaoPublico = null;
		Connection conexaoEhosp = null;
		
		try {
			conexaoPublico = ConnectionFactoryPublico.getConnection();
			conexaoEhosp = ConnectionFactory.getConnection();
			
			PreparedStatement ps = conexaoPublico.prepareStatement(sql);
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				mapearResultSetInconsistencias(inconsistencia, rs);
				inconsistencia.setListaPerfis(listarPerfisInconsistencia(id, conexaoEhosp));
			}
		}

		catch ( SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		}
		catch (Exception e) {
			throw new ProjetoException(e, this.getClass().getName());
		} finally {
			try {
				conexaoPublico.close();
				conexaoEhosp.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return inconsistencia;
	}

	private void mapearResultSetInconsistencias(InconsistenciaBean inconsistencia, ResultSet rs) throws SQLException {
		inconsistencia.setId(rs.getInt("id"));
		inconsistencia.setTitulo(rs.getString("titulo"));
		inconsistencia.setDescricao(rs.getString("descricao"));
		inconsistencia.setSql(rs.getString("sql"));

		String tipo = rs.getString("tipo");
		if(tipo.equals(TipoInconsistencia.LAUDO.name()))
			inconsistencia.setTipoInconsistencia(TipoInconsistencia.LAUDO);
		else if(tipo.equals(TipoInconsistencia.PACIENTE.name()))
			inconsistencia.setTipoInconsistencia(TipoInconsistencia.PACIENTE);
		else if(tipo.equals(TipoInconsistencia.PROCEDIMENTO.name()))
			inconsistencia.setTipoInconsistencia(TipoInconsistencia.PROCEDIMENTO);
	}
	
	private List<Perfil> listarPerfisInconsistencia(Integer idInconsitencia, Connection conexaoEhosp) throws ProjetoException, SQLException {
		
		List<Perfil> listaPerfis = new ArrayList<>();
		
		String sql = "select p.id, p.descricao from hosp.inconsistencia_perfil ip " + 
				"	join acl.perfil p on ip.id_perfil = p.id where ip.id_inconsistencia = ? order by p.descricao";
		
		try {
			PreparedStatement ps = conexaoEhosp.prepareStatement(sql);
			ps.setInt(1, idInconsitencia);
			ResultSet rs = ps.executeQuery();
			
			while (rs.next()) {
				Perfil perfil = new Perfil();
				perfil.setId(rs.getLong("id"));
				perfil.setDescricao(rs.getString("descricao"));
				
				listaPerfis.add(perfil);
			}
		}

		catch ( SQLException sqle) {
			conexaoEhosp.rollback();
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		}
		catch (Exception e) {
			conexaoEhosp.rollback();
			throw new ProjetoException(e, this.getClass().getName());
		}
		return listaPerfis;
	}
	
	public ArrayList<Perfil> listarPerfisNaoAssociadosAhInconsistencia(Integer idInconsistencia) throws ProjetoException {

		String sql = "select p.id, p.descricao from acl.perfil p where not exists " + 
				"		(select p2.id from hosp.inconsistencia_perfil ip " + 
				"		join acl.perfil p2 on ip.id_perfil = p2.id where ip.id_inconsistencia = ? and p2.id = p.id ) order by p.descricao";

		ArrayList<Perfil> lista = new ArrayList<>();
		Connection conexao = null;
		try {
			conexao = ConnectionFactory.getConnection();
			PreparedStatement stm = conexao.prepareStatement(sql);
			stm.setInt(1, idInconsistencia);
			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				Perfil perfil = new Perfil();
				perfil.setId(rs.getLong("id"));
				perfil.setDescricao(rs.getString("descricao"));
				lista.add(perfil);
			}
		} catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
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
	
	public boolean alterarInconsistencia(InconsistenciaBean inconsistencia) throws ProjetoException {
		Boolean retorno = false;
		
		String sql = "UPDATE gestao.inconsistencias " + 
				"SET titulo = ?, descricao = ?, sql = ?, tipo = ? WHERE id = ?; ";
		Connection conexaoPublico = null;
		Connection conexaoEhosp = null;
		
		try {
			conexaoPublico = ConnectionFactoryPublico.getConnection();
			conexaoEhosp = ConnectionFactory.getConnection();
			
			PreparedStatement ps = conexaoPublico.prepareStatement(sql);
			ps.setString(1, inconsistencia.getTitulo());
			ps.setString(2, inconsistencia.getDescricao());
			ps.setString(3, inconsistencia.getSql());
			ps.setString(4, inconsistencia.getTipoInconsistencia().name());
			ps.setInt(5, inconsistencia.getId());

			ps.executeUpdate();
			
			removerPerfisInconsistencia(inconsistencia.getId(), conexaoEhosp);
			gravarPerfisInconsistencia(inconsistencia, conexaoEhosp);
			
			conexaoPublico.commit();
			conexaoEhosp.commit();
			retorno = true;
		}

		catch ( SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		}
		catch (Exception e) {
			throw new ProjetoException(e, this.getClass().getName());
		}finally {
			try {
				conexaoPublico.close();
				conexaoEhosp.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return retorno;
	}
	
	public boolean excluirInconsistencia(Integer idInconsistencia) throws ProjetoException {
		Boolean retorno = false;
		String sql = "DELETE FROM gestao.inconsistencias WHERE id = ? ";

		Connection conexaoPublico = null;
		Connection conexaoEhosp = null;
		
		try {
			conexaoPublico = ConnectionFactoryPublico.getConnection();
			conexaoEhosp = ConnectionFactory.getConnection();
			
			PreparedStatement ps = conexaoPublico.prepareStatement(sql);
			ps.setInt(1, idInconsistencia);
			ps.executeUpdate();
			
			removerPerfisInconsistencia(idInconsistencia, conexaoEhosp);
			
			conexaoPublico.commit();
			conexaoEhosp.commit();
			retorno = true;
		}

		catch ( SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		}
		catch (Exception e) {
			throw new ProjetoException(e, this.getClass().getName());
		}finally {
			try {
				conexaoPublico.close();
				conexaoEhosp.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return retorno;
	}
	
	
	public List<InconsistenciaBean> buscarInconsistencias(String campoBusca, String tipo) throws ProjetoException {
		List<InconsistenciaBean> listaInconsistencia = new ArrayList<>();
		String sql = "SELECT id, titulo, descricao, sql, tipo FROM gestao.inconsistencias where ";

		if(tipo.equals("titulo")){
			sql = sql + "titulo ilike ?";
		}
		else if(tipo.equals("descricao")){
			sql = sql + "descricao ilike ?";
		}

		sql = sql + "order by descricao";
		Connection conexaoPublica = null;

		try {
			conexaoPublica = ConnectionFactoryPublico.getConnection();
			PreparedStatement stm = conexaoPublica.prepareStatement(sql);
			stm.setString(1, "%" + campoBusca.toUpperCase() + "%");
			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				InconsistenciaBean inconsistencia = new InconsistenciaBean();
				mapearResultSetInconsistencias(inconsistencia, rs);
				listaInconsistencia.add(inconsistencia);
			}
		} catch (SQLException ex2) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(ex2), this.getClass().getName(), ex2);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				conexaoPublica.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return listaInconsistencia;
	}

}
