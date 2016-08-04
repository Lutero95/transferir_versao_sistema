package br.gov.al.maceio.sishosp.hosp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;
import br.gov.al.maceio.sishosp.hosp.model.EscolaridadeBean;

public class EscolaridadeDAO {
	private Connection conexao = null;

	// COMEÇO DO CODIGO

	public Boolean cadastrar(EscolaridadeBean escolaridade)
			throws ProjetoException {
		boolean cadastrou = false;
		System.out.println("passou aqui 2");

		/*
		 * PacienteBean user_session = (PacienteBean) FacesContext
		 * .getCurrentInstance().getExternalContext().getSessionMap()
		 * .get("obj_paciente");
		 */

		String sql = "insert into hosp.escolaridade (descescolaridade)"
				+ " values (?)";
		// returning id_paciente
		try {
			System.out.println("passou aqui 3");
			conexao = ConnectionFactory.getConnection();
			PreparedStatement stmt = conexao.prepareStatement(sql);
			stmt.setString(1, escolaridade.getDescescolaridade().toUpperCase()
					.trim());
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
			System.out.println("passou aqui 4");
			conexao.commit();
			cadastrou = true;
			conexao.close();

			return cadastrou;
		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		}
	}

	public Boolean alterar(EscolaridadeBean escolaridade)
			throws ProjetoException {
		boolean alterou = false;
		String sql = "update hosp.escolaridade set descescolaridade = ? where id_escolaridade = ?";
		try {
			conexao = ConnectionFactory.getConnection();
			PreparedStatement stmt = conexao.prepareStatement(sql);
			stmt.setString(1, escolaridade.getDescescolaridade());
			stmt.setInt(2, escolaridade.getCodescolaridade());
			stmt.executeUpdate();

			conexao.commit();

			alterou = true;

			return alterou;
		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		} finally {
			try {
				conexao.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}

	public Boolean excluir(EscolaridadeBean escolaridade)
			throws ProjetoException {
		boolean excluir = false;
		String sql = "delete from hosp.escolaridade where id_escolaridade = ?";
		try {
			conexao = ConnectionFactory.getConnection();
			PreparedStatement stmt = conexao.prepareStatement(sql);
			stmt.setInt(1, escolaridade.getCodescolaridade());
			stmt.executeUpdate();

			conexao.commit();

			excluir = true;

			return excluir;
		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		} finally {
			try {
				conexao.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}

	public ArrayList<EscolaridadeBean> listaEscolaridade() throws ProjetoException {

		String sql = "select * from hosp.escolaridade order by id_escolaridade,descescolaridade";

		ArrayList<EscolaridadeBean> lista = new ArrayList();

		try {
			conexao = ConnectionFactory.getConnection();
			PreparedStatement stm = conexao.prepareStatement(sql);
			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				EscolaridadeBean e = new EscolaridadeBean();

				e.setCodescolaridade(rs.getInt("id_escolaridade"));
				e.setDescescolaridade(rs.getString("descescolaridade")
						.toUpperCase());

				lista.add(e);
			}
		} catch (SQLException ex) {
			throw new RuntimeException(ex);
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

	public EscolaridadeBean buscaescolaridadecodigo(Integer i)
			throws ProjetoException {
		System.out.println("buscaescolaridadecodigo");
		PreparedStatement ps = null;
		conexao = ConnectionFactory.getConnection();

		try {

			String sql = "select id_escolaridade, descescolaridade from hosp.escolaridade where id_escolaridade=? order by descescolaridade";

			ps = conexao.prepareStatement(sql);
			ps.setInt(1, i);
			ResultSet rs = ps.executeQuery();

			EscolaridadeBean escolaridade = new EscolaridadeBean();
			while (rs.next()) {

				escolaridade.setCodescolaridade(rs.getInt(1));
				escolaridade.setDescescolaridade(rs.getString(2));

			}
			return escolaridade;
		} catch (Exception sqle) {

			throw new ProjetoException(sqle);

		} finally {
			try {
				conexao.close();
			} catch (Exception sqlc) {
				sqlc.printStackTrace();
				System.exit(1);
				// TODO: handle exception
			}

		}
	}

	public List<EscolaridadeBean> buscaescolaridade(String s)
			throws ProjetoException {
		System.out.println("buscaescolaridade");
		PreparedStatement ps = null;
		conexao = ConnectionFactory.getConnection();

		try {
			List<EscolaridadeBean> listaescolaridades = new ArrayList<EscolaridadeBean>();
			String sql = "select id_escolaridade,id_escolaridade ||'-'|| descescolaridade descescolaridade from hosp.escolaridade where upper(id_escolaridade ||'-'|| descescolaridade) like ? order by descescolaridade";

			ps = conexao.prepareStatement(sql);
			ps.setString(1, "%" + s.toUpperCase() + "%");
			ResultSet rs = ps.executeQuery();

			List<EscolaridadeBean> colecao = new ArrayList<EscolaridadeBean>();

			while (rs.next()) {

				EscolaridadeBean escolaridade = new EscolaridadeBean();
				escolaridade.setCodescolaridade(rs.getInt("id_escolaridade"));
				escolaridade.setDescescolaridade(rs
						.getString("descescolaridade"));
				colecao.add(escolaridade);

			}
			return colecao;
		} catch (Exception sqle) {

			throw new ProjetoException(sqle);

		} finally {
			try {
				conexao.close();
			} catch (Exception sqlc) {
				sqlc.printStackTrace();
				System.exit(1);
				// TODO: handle exception
			}

		}
	}

	public List<EscolaridadeBean> buscarTipoEscolaridade(String valor,
			Integer tipo) throws ProjetoException {

		String sql = "select escolaridade.id_escolaridade, escolaridade.descescolaridade from hosp.escolaridade where";

		if (tipo == 1) {
			sql += " escolaridade.descescolaridade like ? order by escolaridade.descescolaridade ";
		}
		List<EscolaridadeBean> lista = new ArrayList<>();

		try {
			conexao = ConnectionFactory.getConnection();
			PreparedStatement stmt = conexao.prepareStatement(sql);
			if (tipo == 1) {
				stmt.setString(1, "%" + valor.toUpperCase() + "%");
			}

			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				EscolaridadeBean p = new EscolaridadeBean();

				p.setCodescolaridade(rs.getInt("id_escolaridade"));
				p.setDescescolaridade(rs.getString("descescolaridade")
						.toUpperCase());

				lista.add(p);

			}
		} catch (SQLException ex) {
			ex.printStackTrace();
			// throw new RuntimeException(ex); //
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
}
