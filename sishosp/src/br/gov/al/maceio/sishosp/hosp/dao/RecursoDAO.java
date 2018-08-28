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
import br.gov.al.maceio.sishosp.hosp.model.RecursoBean;

public class RecursoDAO {
	private Connection conexao = null;

	public Boolean cadastrar(RecursoBean recurso) throws ProjetoException {
		boolean cadastrou = false;

		String sql = "insert into hosp.recurso (descrecurso) values (?)";

		try {
			conexao = ConnectionFactory.getConnection();
			PreparedStatement stmt = conexao.prepareStatement(sql);
			stmt.setString(1, recurso.getDescRecurso().toUpperCase().trim());

			stmt.execute();
			conexao.commit();
			cadastrou = true;

			return cadastrou;
		} catch (SQLException ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		} finally {
			try {
				conexao.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}

	public Boolean alterar(RecursoBean recurso) throws ProjetoException {

		boolean alterou = false;

		String sql = "update hosp.recurso set descrecurso = ? where id = ?";

		try {
			conexao = ConnectionFactory.getConnection();
			PreparedStatement stmt = conexao.prepareStatement(sql);

			stmt.setString(1, recurso.getDescRecurso().toUpperCase());
			stmt.setInt(2, recurso.getIdRecurso());
			stmt.executeUpdate();

			conexao.commit();

			alterou = true;

			return alterou;
		} catch (SQLException ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		} finally {
			try {
				conexao.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}

	public Boolean excluir(RecursoBean recurso) throws ProjetoException {

		boolean excluir = false;

		String sql = "delete from hosp.recurso where id = ?";

		try {
			conexao = ConnectionFactory.getConnection();
			PreparedStatement stmt = conexao.prepareStatement(sql);
			stmt.setInt(1, recurso.getIdRecurso());
			stmt.executeUpdate();

			conexao.commit();

			excluir = true;

			return excluir;
		} catch (SQLException ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		} finally {
			try {
				conexao.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}

	public ArrayList<RecursoBean> listaRecursos() throws ProjetoException {

		String sql = "select id, descrecurso from hosp.recurso order by descrecurso";

		ArrayList<RecursoBean> lista = new ArrayList();

		try {
			conexao = ConnectionFactory.getConnection();
			PreparedStatement stm = conexao.prepareStatement(sql);
			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				RecursoBean r = new RecursoBean();

				r.setIdRecurso(rs.getInt("id"));
				r.setDescRecurso(rs.getString("descrecurso"));

				lista.add(r);
			}
			
		} catch (SQLException ex) {
			ex.printStackTrace();
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

	public RecursoBean buscaRecursoCodigo(Integer id) throws ProjetoException {
		PreparedStatement ps = null;
		conexao = ConnectionFactory.getConnection();

		try {

			String sql = "select id, descrecurso from hosp.recurso where id = ? order by descrecurso";

			ps = conexao.prepareStatement(sql);
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();

			RecursoBean recurso = new RecursoBean();
			while (rs.next()) {

				recurso.setIdRecurso(rs.getInt("id"));
				recurso.setDescRecurso(rs.getString("descrecurso"));

			}
			return recurso;
		} catch (Exception sqle) {
			sqle.printStackTrace();
			throw new ProjetoException(sqle);

		} finally {
			try {
				conexao.close();
			} catch (Exception sqlc) {
				sqlc.printStackTrace();
				System.exit(1);
			}

		}
	}

	public List<RecursoBean> buscaRecursoAutoComplete(String s)
			throws ProjetoException {
		PreparedStatement ps = null;
		conexao = ConnectionFactory.getConnection();

		try {
			List<RecursoBean> listaRecursos = new ArrayList<RecursoBean>();
			String sql = "select id , descrecurso from hosp.recurso "
					+ " where upper(descrecurso) like ? order by descrecurso";

			ps = conexao.prepareStatement(sql);
			ps.setString(1, "%" + s.toUpperCase() + "%");
			ResultSet rs = ps.executeQuery();

			List<RecursoBean> colecao = new ArrayList<RecursoBean>();

			while (rs.next()) {

				RecursoBean recurso = new RecursoBean();
				recurso.setIdRecurso(rs.getInt("id"));
				recurso.setDescRecurso(rs.getString("descrecurso"));

				colecao.add(recurso);

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

}
