package br.gov.al.maceio.sishosp.gestao.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.faces.context.FacesContext;

import com.itextpdf.text.pdf.PdfStructTreeController.returnType;

import br.gov.al.maceio.sishosp.acl.model.FuncionarioBean;
import br.gov.al.maceio.sishosp.acl.model.Perfil;
import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;
import br.gov.al.maceio.sishosp.comum.util.ConnectionFactoryPublico;
import br.gov.al.maceio.sishosp.comum.util.TratamentoErrosUtil;
import br.gov.al.maceio.sishosp.gestao.enums.TipoInconsistencia;
import br.gov.al.maceio.sishosp.gestao.model.InconsistenciaBean;
import br.gov.al.maceio.sishosp.gestao.model.dto.InconsistenciaDTO;
import br.gov.al.maceio.sishosp.hosp.model.CidBean;
import br.gov.al.maceio.sishosp.hosp.model.LaudoBean;
import br.gov.al.maceio.sishosp.hosp.model.PacienteBean;
import br.gov.al.maceio.sishosp.hosp.model.ProcedimentoBean;

public class InconsistenciaDAO {

	FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
			.getSessionMap().get("obj_usuario");

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

	public List<InconsistenciaDTO> listarInconsistenciasPeloPerfil() throws ProjetoException {
		
		List<InconsistenciaDTO> listaInconsistenciasDTO = new ArrayList<>();
		Connection conexaoPublico = null;
		Connection conexaoEhosp = null;
		
		try {
			conexaoPublico = ConnectionFactoryPublico.getConnection();
			conexaoEhosp = ConnectionFactory.getConnection();
			List<Integer> listaIdInconsistencias = retornaIdsInconsistenciasDoPerfil(user_session.getPerfil().getId(), conexaoEhosp);
			listaInconsistenciasDTO = retornaInconsistenciasDoPerfil(listaIdInconsistencias, conexaoPublico);
			listaInconsistenciasDTO = filtraInconsistenciasComRegistro(listaInconsistenciasDTO);
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
		return listaInconsistenciasDTO;
	}

	private List<InconsistenciaDTO> filtraInconsistenciasComRegistro(List<InconsistenciaDTO> listaInconsistenciasDTO)
			throws ProjetoException {
		
		List<InconsistenciaDTO> listaInconsistenciaDTOAux = new ArrayList<>();
		
		for(InconsistenciaDTO inconsistenciaDTO : listaInconsistenciasDTO) {
			
			InconsistenciaBean inconsistencia = inconsistenciaDTO.getInconsistencia();
			List<Object> lista = listarConsultasDeInconsistencia(inconsistencia);
			
			if(!lista.isEmpty()) {
				if (inconsistencia.getTipoInconsistencia().equals(TipoInconsistencia.LAUDO)) {
					for (Object object : lista) {
						inconsistenciaDTO.getListaLaudos().add((LaudoBean) object);
					}
				}

				else if (inconsistencia.getTipoInconsistencia().equals(TipoInconsistencia.PACIENTE)) {
					for (Object object : lista) {
						inconsistenciaDTO.getListaPacientes().add((PacienteBean) object);
					}
				}

				else if (inconsistencia.getTipoInconsistencia().equals(TipoInconsistencia.PROCEDIMENTO)) {
					for (Object object : lista) {
						inconsistenciaDTO.getListaProcedimentos().add((ProcedimentoBean) object);
					}
				}
				listaInconsistenciaDTOAux.add(inconsistenciaDTO);
			}
		}
		return listaInconsistenciaDTOAux;
	}
	
	private List<Integer> retornaIdsInconsistenciasDoPerfil(Long idPerfil, Connection conexaoAuxiliar) throws ProjetoException, SQLException {
		List<Integer> listaIdInconsistencia = new ArrayList<>();

		String sql = "select ip.id_inconsistencia from hosp.inconsistencia_perfil ip where ip.id_perfil = ? ;";
		try {
			PreparedStatement ps = conexaoAuxiliar.prepareStatement(sql);
			ps.setLong(1, idPerfil);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				listaIdInconsistencia.add(rs.getInt("id_inconsistencia"));
			}
		} catch (SQLException ex2) {
			conexaoAuxiliar.rollback();
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(ex2), this.getClass().getName(), ex2);
		} catch (Exception ex) {
			conexaoAuxiliar.rollback();
			throw new ProjetoException(ex, this.getClass().getName());
		}
		return listaIdInconsistencia;
	}
	
	private List<InconsistenciaDTO> retornaInconsistenciasDoPerfil(List<Integer> listaIdInconsistencia, Connection conexaoPublicaAuxiliar) throws ProjetoException, SQLException {
		List<InconsistenciaDTO> listaInconsistencia = new ArrayList<>();

		String sql = "SELECT id, titulo, descricao, sql, tipo " + 
				"FROM gestao.inconsistencias where id = ? order by descricao; ";
		try {
			PreparedStatement ps = conexaoPublicaAuxiliar.prepareStatement(sql);
			
			for (Integer id : listaIdInconsistencia) {
				ps.setInt(1, id);
				ResultSet rs = ps.executeQuery();	
				if (rs.next()) {
					InconsistenciaBean inconsistencia = new InconsistenciaBean();
					mapearResultSetInconsistencias(inconsistencia, rs);
					InconsistenciaDTO inconsistenciaDTO = new InconsistenciaDTO(inconsistencia);
					listaInconsistencia.add(inconsistenciaDTO);
				}
			}
			
		} catch (SQLException ex2) {
			conexaoPublicaAuxiliar.rollback();
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(ex2), this.getClass().getName(), ex2);
		} catch (Exception ex) {
			conexaoPublicaAuxiliar.rollback();
			throw new ProjetoException(ex, this.getClass().getName());
		}
		return listaInconsistencia;
	}
	
	private List<Object> listarConsultasDeInconsistencia(InconsistenciaBean inconsistencia)
			throws ProjetoException {

		List<Object> lista = new ArrayList<>();
		Connection conexao = null;
		try {
			conexao = ConnectionFactory.getConnection();
			PreparedStatement ps = conexao.prepareStatement(inconsistencia.getSql());
			
			char sqlArray [] = inconsistencia.getSql().toCharArray();
			int quantidadeParametro = 0;
			
    		for(int i = 0; i < sqlArray.length; i++) {
				if(sqlArray[i] == '?') {
					quantidadeParametro++;
				}
			}
			
			for(int j = 1; j <= quantidadeParametro; j++) {
				ps.setObject(j, user_session.getUnidade().getId());				
			}			
			
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				if(inconsistencia.getTipoInconsistencia().equals(TipoInconsistencia.LAUDO)) {
					LaudoBean laudo = new LaudoBean();
					mapearResultSetLaudo(laudo, rs);
					lista.add(laudo);
				}
				else if(inconsistencia.getTipoInconsistencia().equals(TipoInconsistencia.PACIENTE)) {
					PacienteBean paciente = new PacienteBean();
					mapearResultSetPaciente(paciente, rs);
					lista.add(paciente);
				}
				else if(inconsistencia.getTipoInconsistencia().equals(TipoInconsistencia.PROCEDIMENTO)) {
					ProcedimentoBean procedimento = new ProcedimentoBean();
					mapearResultSetProcedimento(procedimento, rs);
					lista.add(procedimento);
				}
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
	
	private void mapearResultSetLaudo(LaudoBean laudo, ResultSet rs) throws SQLException {
		laudo.setId(rs.getInt("id_laudo"));
		laudo.getPaciente().setId_paciente(rs.getInt("codpaciente"));
		laudo.setDataSolicitacao(rs.getDate("data_solicitacao"));
		laudo.setMesInicio(rs.getInt("mes_inicio"));
		laudo.setAnoInicio(rs.getInt("ano_inicio"));
		laudo.setMesFinal(rs.getInt("mes_final"));
		laudo.setAnoFinal(rs.getInt("ano_final"));
	}
	
	private void mapearResultSetPaciente(PacienteBean paciente, ResultSet rs) throws SQLException {
		paciente.setId_paciente(rs.getInt("id_paciente"));
		paciente.setNome(rs.getString("nome"));
		paciente.setDtanascimento(rs.getDate("dtanascimento"));
	}
	
	private void mapearResultSetProcedimento(ProcedimentoBean procedimento, ResultSet rs) throws SQLException {
		procedimento.setIdProc(rs.getInt("id"));
		procedimento.setCodProc(rs.getString("codproc"));
		procedimento.setNomeProc(rs.getString("nome"));
	}
}
