package br.gov.al.maceio.sishosp.hosp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;
import br.gov.al.maceio.sishosp.hosp.model.CboBean;
import br.gov.al.maceio.sishosp.hosp.model.EnderecoBean;
import br.gov.al.maceio.sishosp.hosp.model.PacienteBean;

public class EnderecoDAO {
	private Connection conexao = null;

	@SuppressWarnings("deprecation")
	public Boolean cadastrarMunicipio(EnderecoBean endereco)
			throws ProjetoException {
		boolean cadastrou = false;

		String sql = "insert into hosp.municipio (descmunicipio,codfederal,codmacregiao) "
				+ " values (?, ?, ?)";

		try {

			conexao = ConnectionFactory.getConnection();
			PreparedStatement stmt = conexao.prepareStatement(sql);
			stmt.setString(1, endereco.getMunicipio().toUpperCase().trim());
			stmt.setInt(2, endereco.getCodfederal());
			stmt.setInt(3, endereco.getCodmacregiao());

			stmt.execute();
			conexao.commit();
			cadastrou = true;
			conexao.close();

			return cadastrou;
		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		}
	}

	public Boolean cadastrarBairros(EnderecoBean endereco)
			throws ProjetoException {
		boolean cadastrou = false;

		String sql = "insert into hosp.bairros (descbairro, codmunicipio) "
				+ " values (?, ?)";

		try {

			conexao = ConnectionFactory.getConnection();
			PreparedStatement stmt = conexao.prepareStatement(sql);
			stmt.setString(1, endereco.getBairro().toUpperCase().trim());
			stmt.setInt(2, endereco.getCodmunicipio());

			stmt.execute();
			conexao.commit();
			cadastrou = true;
			conexao.close();

			return cadastrou;
		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		}
	}

	public Boolean alterarMunicipio(EnderecoBean endereco)
			throws ProjetoException {
		boolean alterou = false;
		String sql = "update hosp.municipio set descmunicipio = ?, codfederal = ? , codmacregiao = ? where id_municipio = ?";
		try {
			conexao = ConnectionFactory.getConnection();
			PreparedStatement stmt = conexao.prepareStatement(sql);
			stmt.setString(1, endereco.getMunicipio().toUpperCase());
			if (endereco.getCodfederal() != null)
				stmt.setInt(2, endereco.getCodfederal());
			else
				stmt.setNull(2, Types.OTHER);

			if (endereco.getCodmacregiao() != null)
				stmt.setInt(3, endereco.getCodmacregiao());
			else
				stmt.setNull(3, Types.OTHER);
			stmt.setInt(4, endereco.getCodmunicipio());
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

	public Boolean alterarBairros(EnderecoBean endereco)
			throws ProjetoException {
		boolean alterou = false;
		String sql = "update hosp.bairros set descbairro = ?, codmunicipio = ? where id_bairro = ?";
		try {
			conexao = ConnectionFactory.getConnection();
			PreparedStatement stmt = conexao.prepareStatement(sql);
			stmt.setString(1, endereco.getBairro().toUpperCase());
			stmt.setInt(2, endereco.getCodmunicipio());
			stmt.setInt(3, endereco.getCodbairro());
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

	public Boolean excluirMunicipio(EnderecoBean endereco)
			throws ProjetoException {
		boolean excluir = false;
		String sql = "delete from hosp.municipio where id_municipio = ?";
		try {
			conexao = ConnectionFactory.getConnection();
			PreparedStatement stmt = conexao.prepareStatement(sql);
			stmt.setInt(1, endereco.getCodmunicipio());
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

	public Boolean excluirBairros(EnderecoBean endereco)
			throws ProjetoException {
		boolean excluir = false;
		String sql = "delete from hosp.bairros where id_bairro = ?";
		try {
			conexao = ConnectionFactory.getConnection();
			PreparedStatement stmt = conexao.prepareStatement(sql);
			stmt.setInt(1, endereco.getCodbairro());
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

	@SuppressWarnings("deprecation")
	public Boolean cadastrarLogradouro(EnderecoBean endereco)
			throws ProjetoException {
		boolean cadastrou = false;

		/*
		 * PacienteBean user_session = (PacienteBean) FacesContext
		 * .getCurrentInstance().getExternalContext().getSessionMap()
		 * .get("obj_paciente");
		 */

		String sql = "insert into hosp.logradouros (desclogra,codbairro,cep,codmunicipio) "
				+ " values (?, ?, ?, ?)";

		try {
			conexao = ConnectionFactory.getConnection();
			PreparedStatement stmt = conexao.prepareStatement(sql);
			stmt.setString(1, endereco.getLogradouro().toUpperCase().trim());
			stmt.setInt(2, endereco.getCodbairro());
			stmt.setString(3, endereco.getCep());
			stmt.setInt(4, endereco.getCodmunicipio());

			stmt.execute();
			conexao.commit();
			cadastrou = true;
			conexao.close();

			return cadastrou;
		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		}
	}

	public Boolean alterarLogradouro(EnderecoBean endereco)
			throws ProjetoException {
		boolean alterou = false;
		String sql = "update hosp.logradouros set desclogra = ?, codbairro = ?, cep = ?, codmunicipio = ? where id_logra = ?";
		try {
			conexao = ConnectionFactory.getConnection();
			PreparedStatement stmt = conexao.prepareStatement(sql);
			stmt.setString(1, endereco.getLogradouro());
			stmt.setInt(2, endereco.getCodbairro());
			stmt.setString(3, endereco.getCep());
			stmt.setInt(4, endereco.getCodmunicipio());
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

	public Boolean excluirLogradouro(EnderecoBean endereco)
			throws ProjetoException {
		boolean excluir = false;
		String sql = "delete from hosp.logradouros where id_logra = ?";
		try {
			conexao = ConnectionFactory.getConnection();
			PreparedStatement stmt = conexao.prepareStatement(sql);
			stmt.setLong(1, endereco.getId());
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

	public ArrayList<EnderecoBean> listaMunicipios() throws ProjetoException {

		String sql = "select * from hosp.municipio order by descmunicipio";

		ArrayList<EnderecoBean> lista = new ArrayList();

		try {
			conexao = ConnectionFactory.getConnection();
			PreparedStatement stm = conexao.prepareStatement(sql);
			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				EnderecoBean p = new EnderecoBean();

				p.setCodmunicipio(rs.getInt("id_municipio"));
				p.setMunicipio(rs.getString("descmunicipio").toUpperCase());
				p.setCodfederal(rs.getInt("codfederal"));
				p.setCodmacregiao(rs.getInt("codmacregiao"));

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

	public EnderecoBean listarBairroPorId(int id) throws ProjetoException {

		EnderecoBean end = new EnderecoBean();
		String sql = "select b.id_bairro, b.descbairro, b.codmunicipio, m.descmunicipio from hosp.bairros b "
				+ "left join hosp.municipio m on (b.codmunicipio = m.id_municipio) where id_bairro=? order by b.descbairro";
		try {
			conexao = ConnectionFactory.getConnection();
			PreparedStatement stm = conexao.prepareStatement(sql);
			stm.setInt(1, id);
			ResultSet rs = stm.executeQuery();
			while (rs.next()) {
				end = new EnderecoBean();
				end.setCodbairro(rs.getInt("id_bairro"));
				end.setBairro(rs.getString("descbairro"));
				end.setCodmunicipio(rs.getInt("codmunicipio"));
				end.setMunicipio(rs.getString("descmunicipio"));
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
		return end;
	}

	public ArrayList<EnderecoBean> listaBairros() throws ProjetoException {

		String sql = "select b.id_bairro, b.descbairro, b.codmunicipio, m.descmunicipio from hosp.bairros b "
				+ "left join hosp.municipio m on (b.codmunicipio = m.id_municipio) order by b.descbairro";

		ArrayList<EnderecoBean> lista = new ArrayList();

		try {
			conexao = ConnectionFactory.getConnection();
			PreparedStatement stm = conexao.prepareStatement(sql);
			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				EnderecoBean end = new EnderecoBean();
				end = new EnderecoBean();
				end.setCodbairro(rs.getInt("id_bairro"));
				end.setBairro(rs.getString("descbairro"));
				end.setCodmunicipio(rs.getInt("codmunicipio"));
				end.setMunicipio(rs.getString("descmunicipio"));

				lista.add(end);
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

	public EnderecoBean listarMunicipioPorId(int id) throws ProjetoException {

		EnderecoBean end = new EnderecoBean();
		String sql = "select municipio.id_municipio, municipio.descmunicipio, codfederal, codmacregiao from hosp.municipio where id_municipio=?";
		try {
			conexao = ConnectionFactory.getConnection();
			PreparedStatement stm = conexao.prepareStatement(sql);
			stm.setInt(1, id);
			ResultSet rs = stm.executeQuery();
			while (rs.next()) {
				end = new EnderecoBean();
				end.setId(rs.getLong("id_municipio"));
				end.setCodmunicipio(rs.getInt("id_municipio"));
				end.setMunicipio(rs.getString("descmunicipio"));
				end.setCodfederal(rs.getInt("codfederal"));
				end.setCodmacregiao(rs.getInt("codmacregiao"));
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
		return end;
	}

	public List<EnderecoBean> buscarTipoMunicipio(String valor, Integer tipo)
			throws ProjetoException {

		String sql = "select municipio.id_municipio, municipio.descmunicipio from hosp.municipio where";

		if (tipo == 1) {
			sql += " municipio.descmunicipio like ? order by municipio.descmunicipio ";
		}
		List<EnderecoBean> lista = new ArrayList<>();

		try {
			conexao = ConnectionFactory.getConnection();
			PreparedStatement stmt = conexao.prepareStatement(sql);
			if (tipo == 1) {
				stmt.setString(1, "%" + valor.toUpperCase() + "%");
			}

			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				EnderecoBean p = new EnderecoBean();

				p.setCodmunicipio(rs.getInt("id_municipio"));
				p.setMunicipio(rs.getString("descmunicipio").toLowerCase());

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

	public Integer municipioExiste(PacienteBean paciente)
			throws ProjetoException {

		String sql = "select id_municipio from hosp.municipio where codfederal = ?";
		int cod = 0;

		try {
			conexao = ConnectionFactory.getConnection();
			PreparedStatement stm = conexao.prepareStatement(sql);
			stm.setInt(1, paciente.getEndereco().getCodibge());

			ResultSet rs = stm.executeQuery();

			while (rs.next()) {

				cod = rs.getInt("id_municipio");

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
		return cod;
	}

	public List<EnderecoBean> buscaMunicipioAutoComplete(String str)
			throws ProjetoException {
		PreparedStatement ps = null;
		conexao = ConnectionFactory.getConnection();
		try {
			String sql = " select id_municipio, descmunicipio from hosp.municipio where descmunicipio like ? order by descmunicipio";

			ps = conexao.prepareStatement(sql);
			ps.setString(1, "%" + str.toUpperCase() + "%");
			ResultSet rs = ps.executeQuery();

			List<EnderecoBean> lista = new ArrayList<EnderecoBean>();

			while (rs.next()) {
				EnderecoBean e = new EnderecoBean();
				e.setCodmunicipio(rs.getInt("id_municipio"));
				e.setMunicipio(rs.getString("descmunicipio"));

				lista.add(e);

			}
			return lista;
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
