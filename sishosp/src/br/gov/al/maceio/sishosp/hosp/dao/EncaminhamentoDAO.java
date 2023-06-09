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
import br.gov.al.maceio.sishosp.hosp.model.EncaminhamentoBean;

public class EncaminhamentoDAO {
	private Connection conexao = null;

	public Boolean cadastrar(EncaminhamentoBean encaminhamento)
			throws ProjetoException {
		boolean cadastrou = false;

		/*
		 * PacienteBean user_session = (PacienteBean) FacesContext
		 * .getCurrentInstance().getExternalContext().getSessionMap()
		 * .get("obj_paciente");
		 */

		String sql = "insert into hosp.encaminhamento (descencaminhado, dtacadastro)"
				+ " values (?, CURRENT_TIMESTAMP)";
		// returning id_paciente
		try {
			conexao = ConnectionFactory.getConnection();
			PreparedStatement stmt = conexao.prepareStatement(sql);
			stmt.setString(1, encaminhamento.getTipo1().toUpperCase().trim());
			// stmt.setString(2, paciente.getCpf().replaceAll("[^0-9]", ""));
			// stmt.setBoolean(3, true);
			// stmt.setInt(3, paciente.getIdpessoa());
			/*
			 * ResultSet rs = stmt.executeQuery(); if(rs.next()) { PacienteBean
			 * p = paciente; String idRetorno = null; idRetorno =
			 * String.valueOf(rs.getLong("id_usuario"));
			 * p.setId_paciente(Long.parseLong(idRetorno));
			 * 
			 * }
			 */
			stmt.execute();
			conexao.commit();
		} catch (SQLException ex2) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(ex2), this.getClass().getName(), ex2);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		}finally {
			try {
				conexao.close();
			} catch (SQLException e) {
				//comentado walter erro log ex.printStackTrace();
			}
		}
		return cadastrou;
	}

	public Boolean alterar(EncaminhamentoBean encaminhamento)
			throws ProjetoException {
		boolean alterou = false;
		String sql = "update hosp.encaminhamento set descencaminhado = ? where id_encaminhado = ?";
		try {
			conexao = ConnectionFactory.getConnection();
			PreparedStatement stmt = conexao.prepareStatement(sql);
			stmt.setString(1, encaminhamento.getTipo1());
			stmt.executeUpdate();

			conexao.commit();

			alterou = true;
		} catch (SQLException ex2) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(ex2), this.getClass().getName(), ex2);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				conexao.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return alterou;
	}

	public Boolean excluir(EncaminhamentoBean encaminhamento)
			throws ProjetoException {
		boolean excluir = false;
		String sql = "delete from hosp.encaminhado where id_encaminhado = ?";
		try {
			conexao = ConnectionFactory.getConnection();
			PreparedStatement stmt = conexao.prepareStatement(sql);
			stmt.setLong(1, encaminhamento.getCod());
			stmt.execute();

			conexao.commit();

			excluir = true;
		} catch (SQLException ex2) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(ex2), this.getClass().getName(), ex2);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				conexao.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return excluir;
	}

	public ArrayList<EncaminhamentoBean> listaEncaminhados()
			throws ProjetoException {

		String sql = "select * from hosp.encaminhado order by id_encaminhado,descencaminhado";

		ArrayList<EncaminhamentoBean> lista = new ArrayList();

		try {
			conexao = ConnectionFactory.getConnection();
			PreparedStatement stm = conexao.prepareStatement(sql);
			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				EncaminhamentoBean encaminhamento = new EncaminhamentoBean();
				encaminhamento.setCod(rs.getInt("id_encaminhado"));
				encaminhamento.setTipo1(rs.getString("descencaminhado").toLowerCase());
				lista.add(encaminhamento);
			}
		} catch (SQLException ex2) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(ex2), this.getClass().getName(), ex2);
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

	public EncaminhamentoBean buscaencaminhamentocodigo(Integer i)
			throws ProjetoException {
		PreparedStatement ps = null;
		conexao = ConnectionFactory.getConnection();

		EncaminhamentoBean encaminhamento = new EncaminhamentoBean();
		
		try {
			String sql = "select id_encaminhamento, descencaminhado from hosp.encaminhado where id_encaminhado=? order by descencaminhado";

			ps = conexao.prepareStatement(sql);
			ps.setInt(1, i);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {

				encaminhamento.setCod(rs.getInt("id_encaminhamento"));
				encaminhamento.setTipo1(rs.getString("descencaminhado"));

			}
		} catch (SQLException ex2) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(ex2), this.getClass().getName(), ex2);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				conexao.close();
			} catch (Exception sqlc) {
				sqlc.printStackTrace();
				System.exit(1);
			}
		}
		return encaminhamento;
	}

	public List<EncaminhamentoBean> buscaencaminhamento(String s)
			throws ProjetoException {
		PreparedStatement ps = null;
		conexao = ConnectionFactory.getConnection();

		List<EncaminhamentoBean> colecao = new ArrayList<EncaminhamentoBean>();
		try {
			List<EncaminhamentoBean> listaencaminhados = new ArrayList<EncaminhamentoBean>();
			String sql = "select id_encaminhado, descencaminhado from hosp.encaminhado where upper(descencaminhado) like ? order by descencaminhado";

			ps = conexao.prepareStatement(sql);
			ps.setString(1, "%" + s.toUpperCase() + "%");
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				EncaminhamentoBean encaminhamento = new EncaminhamentoBean();
				encaminhamento.setCod(rs.getInt("id_encaminhado"));
				encaminhamento.setTipo1(rs.getString("descencaminhado"));
				colecao.add(encaminhamento);
			}
		} catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				conexao.close();
			} catch (Exception sqlc) {
				sqlc.printStackTrace();
				System.exit(1);
			}
		}
		return colecao;
	}

}
