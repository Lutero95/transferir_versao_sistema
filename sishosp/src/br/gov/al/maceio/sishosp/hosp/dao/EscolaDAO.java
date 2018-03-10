package br.gov.al.maceio.sishosp.hosp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;
import br.gov.al.maceio.sishosp.hosp.model.EscolaBean;

public class EscolaDAO {
	private Connection conexao = null;

	public Boolean cadastrar(EscolaBean escola) throws ProjetoException {
		boolean cadastrou = false;

		/*
		 * PacienteBean user_session = (PacienteBean) FacesContext
		 * .getCurrentInstance().getExternalContext().getSessionMap()
		 * .get("obj_paciente");
		 */

		String sql = "insert into hosp.escola (descescola) values (?)";
		// String sql =
		// "insert into hosp.escola (descescola) values ((select max(cod) +1 from hosp.escola where codempresa=1), ?";
		// returning id_paciente
		try {
			conexao = ConnectionFactory.getConnection();
			PreparedStatement stmt = conexao.prepareStatement(sql);
			stmt.setString(1, escola.getDescescola().toUpperCase().trim());
			// stmt.setInt(2, escola.getCodtipoescola());
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
			cadastrou = true;
			conexao.close();

			return cadastrou;
		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		}
	}

	public Boolean alterar(EscolaBean escola) throws ProjetoException {
		boolean alterou = false;
		String sql = "update hosp.escola set descescola = ? where id_escola = ?";
		try {
			conexao = ConnectionFactory.getConnection();
			PreparedStatement stmt = conexao.prepareStatement(sql);
			stmt.setString(1, escola.getDescescola().toUpperCase());
			stmt.setInt(2, escola.getCodEscola());
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

	public Boolean excluir(EscolaBean escola) throws ProjetoException {
		boolean excluir = false;
		String sql = "delete from hosp.escola where id_escola = ?";
		try {
			conexao = ConnectionFactory.getConnection();
			PreparedStatement stmt = conexao.prepareStatement(sql);
			stmt.setInt(1, escola.getCodEscola());
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

	public Boolean cadastrarTipoEscola(EscolaBean escola)
			throws ProjetoException {
		boolean cadastrou = false;

		/*
		 * PacienteBean user_session = (PacienteBean) FacesContext
		 * .getCurrentInstance().getExternalContext().getSessionMap()
		 * .get("obj_paciente");
		 */

		String sql = "insert into hosp.tipoescola (desctipoescola)"
				+ " values (?)";

		try {
			conexao = ConnectionFactory.getConnection();
			PreparedStatement stmt = conexao.prepareStatement(sql);
			stmt.setString(1, escola.getDesctipoescola().toUpperCase().trim());

			stmt.execute();
			conexao.commit();
			cadastrou = true;
			conexao.close();

			return cadastrou;
		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		}
	}

	public Boolean alterarTipoEscola(EscolaBean escola) throws ProjetoException {
		boolean alterou = false;
		String sql = "update hosp.tipoescola set desctipoescola = ? where codtipoescola = ?";
		try {
			conexao = ConnectionFactory.getConnection();
			PreparedStatement stmt = conexao.prepareStatement(sql);
			stmt.setString(1, escola.getDesctipoescola());
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

	public Boolean excluirTipoEscola(EscolaBean escola) throws ProjetoException {
		boolean excluir = false;
		String sql = "delete from hosp.escola where codtipoescola = ?";
		try {
			conexao = ConnectionFactory.getConnection();
			PreparedStatement stmt = conexao.prepareStatement(sql);
			stmt.setLong(1, escola.getCodtipoescola());
			stmt.execute();

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

	public ArrayList<EscolaBean> listaEscolas() throws ProjetoException {

		String sql = "select * from hosp.escola order by descescola";

		ArrayList<EscolaBean> lista = new ArrayList();

		try {
			conexao = ConnectionFactory.getConnection();
			PreparedStatement stm = conexao.prepareStatement(sql);
			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				EscolaBean p = new EscolaBean();

				p.setCodEscola(rs.getInt("id_escola"));
				p.setDescescola(rs.getString("descescola"));
				p.setCodtipoescola(rs.getInt("codtipoescola"));

				lista.add(p);
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

	public EscolaBean buscaescolacodigo(Integer i) throws ProjetoException {
		PreparedStatement ps = null;
		conexao = ConnectionFactory.getConnection();

		try {

			String sql = "select id_escola, descescola from hosp.escola where id_escola=? order by descescola";

			ps = conexao.prepareStatement(sql);
			ps.setInt(1, i);
			ResultSet rs = ps.executeQuery();

			EscolaBean escola = new EscolaBean();
			while (rs.next()) {

				escola.setCodEscola(rs.getInt(1));
				escola.setDescescola(rs.getString(2));

			}
			return escola;
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

	public List<EscolaBean> buscaescola(String s) throws ProjetoException {
		PreparedStatement ps = null;
		conexao = ConnectionFactory.getConnection();

		try {
			List<EscolaBean> listaescolas = new ArrayList<EscolaBean>();
			String sql = "select id_escola,id_escola ||'-'|| descescola descescola from hosp.escola "
					+ " where upper(id_escola ||'-'|| descescola) like ? order by descescola";

			ps = conexao.prepareStatement(sql);
			ps.setString(1, "%" + s.toUpperCase() + "%");
			ResultSet rs = ps.executeQuery();

			List<EscolaBean> colecao = new ArrayList<EscolaBean>();

			while (rs.next()) {

				EscolaBean escola = new EscolaBean();
				escola.setCodEscola(rs.getInt("id_escola"));
				escola.setDescescola(rs.getString("descescola"));
				colecao.add(escola);

				;

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

	public ArrayList<EscolaBean> listaTipoEscola() throws ProjetoException {

		String sql = "select * from hosp.tipoescola order by desctipoescola";

		ArrayList<EscolaBean> lista = new ArrayList();

		try {
			conexao = ConnectionFactory.getConnection();
			PreparedStatement stm = conexao.prepareStatement(sql);
			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				EscolaBean p = new EscolaBean();

				p.setCodtipoescola(rs.getInt("codtipoescola"));
				p.setDesctipoescola(rs.getString("desctipoescola")
						.toLowerCase());

				lista.add(p);
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

	public List<EscolaBean> buscarTipoEscola(String valor, Integer tipo)
			throws ProjetoException {

		String sql = "select escola.id_escola, escola.descescola from hosp.escola where";

		if (tipo == 1) {
			sql += " escola.descescola like ? order by escola.descescola ";
		}
		List<EscolaBean> lista = new ArrayList<>();

		try {
			conexao = ConnectionFactory.getConnection();
			PreparedStatement stmt = conexao.prepareStatement(sql);
			if (tipo == 1) {
				stmt.setString(1, "%" + valor.toUpperCase() + "%");
			}

			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				EscolaBean p = new EscolaBean();

				p.setCodEscola(rs.getInt("id_escola"));
				p.setDescescola(rs.getString("descescola").toUpperCase());

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
