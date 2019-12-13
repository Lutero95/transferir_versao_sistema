package br.gov.al.maceio.sishosp.financeiro.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import br.gov.al.maceio.sishosp.acl.model.FuncionarioBean;
import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;
import br.gov.al.maceio.sishosp.financeiro.model.BuscaBeanFornecedor;
import br.gov.al.maceio.sishosp.financeiro.model.FornecedorBean;



public class FornecedorDAO {

	public void salvarFornecedor(FornecedorBean bean) throws ProjetoException {

		FuncionarioBean user_session = (FuncionarioBean) FacesContext
				.getCurrentInstance().getExternalContext().getSessionMap()
				.get("obj_usuario");

		String sql = " INSERT INTO financeiro.fornecedor (nome, fantasia, endereco, complemento, "
				+ " cep, fone1, cpfcnpj, opcad, dtcadastro, site, email, inscest, tipopessoa) "
				+ " VALUES (?, ?, ?, ?, ?, ?, ?,  ?, current_date, ?, ?, ?,?); ";

		Connection con = ConnectionFactory.getConnection();

		try {

			PreparedStatement ps = con.prepareStatement(sql);

			ps.setString(1, bean.getNome().toUpperCase());
			ps.setString(2, bean.getFantasia().toUpperCase());
			ps.setString(3, bean.getEndereco().toUpperCase());
			ps.setString(4, bean.getComplemento().toUpperCase());
			ps.setString(5, bean.getCep().replaceAll("[^0-9]", ""));
			ps.setString(6, bean.getTelefone().replaceAll("[^0-9]", ""));
			ps.setString(7, bean.getCpfcnpj().replaceAll("[^0-9]", ""));
			ps.setLong(8, user_session.getId());
			ps.setString(9, bean.getSite());
			ps.setString(10, bean.getEmail());
			ps.setString(11, bean.getInscest());
			ps.setString(12, bean.getTipoPessoa());

			ps.execute();

			con.commit();

			ps.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public boolean editarFornecedor(FornecedorBean bean) throws ProjetoException {
		boolean result = false;

		Connection con = ConnectionFactory.getConnection();
		String sql = " UPDATE financeiro.fornecedor  SET  "
				+ "nome=?,"
				+ " fantasia=?, endereco=?, complemento=?,  cep=?,  fone1=?, cpfcnpj=?,  site=?,  email=?, inscest=?,  tipopessoa=? "
				+ " WHERE codforn=? ";

		try {

			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, bean.getNome().toUpperCase());
			ps.setString(2, bean.getFantasia().toUpperCase());
			ps.setString(3, bean.getEndereco().toUpperCase());
			ps.setString(4, bean.getComplemento().toUpperCase());
			ps.setString(5, bean.getCep().replaceAll("[^0-9]", ""));
			ps.setString(6, bean.getTelefone().replaceAll("[^0-9]", ""));
			ps.setString(7, bean.getCpfcnpj().replaceAll("[^0-9]", ""));
			ps.setString(8, bean.getSite().toUpperCase());
			ps.setString(9, bean.getEmail().toUpperCase());
			ps.setString(10, bean.getInscest().toUpperCase());
			ps.setString(11, bean.getTipoPessoa().toUpperCase());
			ps.setInt(12, bean.getCodforn());

			ps.execute();
			con.commit();
			result = true;
			
			ps.close();
		} catch (SQLException e) {
			result = false;
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
					e.getMessage(), "Erro:"+e.getMessage());
			FacesContext.getCurrentInstance().addMessage(null, msg);
		} finally {

			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return result;

	}

	public boolean deleteFornecedor(FornecedorBean fornecedorBean)
			throws ProjetoException {

		Connection con = ConnectionFactory.getConnection();
		boolean sucesso = true;
		String sql = " DELETE FROM financeiro.fornecedor WHERE codforn = ?; ";

		try {

			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, fornecedorBean.getCodforn());
			ps.execute();
			con.commit();

			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
			sucesso = false;
		} finally {

			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return sucesso;

	}

	public List<FornecedorBean> lstFornecedor(BuscaBeanFornecedor busca) throws ProjetoException {

		FuncionarioBean user_session = (FuncionarioBean) FacesContext
				.getCurrentInstance().getExternalContext().getSessionMap()
				.get("obj_usuario");

		String sql = " select codforn, nome, cpfcnpj, complemento, email, site, fone1, fantasia, cep, endereco, inscest, tipopessoa from financeiro.fornecedor where 1=1 ";
		if ((busca.getValor()!= null) && (!busca.getValor().equals(""))) {
			 if (busca.getTipoBusca().equals("C"))
            sql = sql + " and fornecedor.codforn=?  ";
			 if (busca.getTipoBusca().equals("N"))
		            sql = sql + " and fornecedor.nome like ?  ";
			 
			 if (busca.getTipoBusca().equals("P"))
		            sql = sql + " and fornecedor.cpfcnpj=?  ";
        }
		if (busca.getOrdenacao().equals("C")) {
            sql = sql + " order by codforn; ";
        }

        if (busca.getOrdenacao().equals("N")) {
            sql = sql + " order by nome ";
        }
        
        if (busca.getOrdenacao().equals("P")) {
            sql = sql + " order by cpfcnpj ";
        }

		ResultSet set = null;
		Connection con = ConnectionFactory.getConnection();
		ArrayList<FornecedorBean> lst = new ArrayList<>();

		try {

			PreparedStatement ps = con.prepareStatement(sql);

			if ((busca.getValor()!= null) && (!busca.getValor().equals(""))) {
				 if (busca.getTipoBusca().equals("C"))
					 ps.setInt(1,Integer.valueOf(busca.getValor()));
				 if (busca.getTipoBusca().equals("N"))
					 ps.setString(1, "%" + busca.getValor().toUpperCase() + "%");
				 if (busca.getTipoBusca().equals("P"))
					 ps.setString(1,busca.getValor().toUpperCase());
	        }
			set = ps.executeQuery();

			while (set.next()) {

				FornecedorBean f = new FornecedorBean();

				f.setCodforn(set.getInt("codforn"));
				f.setNome(set.getString("nome"));
				f.setCpfcnpj(set.getString("cpfcnpj"));
				f.setComplemento(set.getString("complemento"));
				f.setEmail(set.getString("email"));
				f.setSite(set.getString("site"));
				f.setTelefone(set.getString("fone1"));
				f.setFantasia(set.getString("fantasia"));
				f.setCep(set.getString("cep"));
				f.setEndereco(set.getString("endereco"));
				f.setInscest(set.getString("inscest"));
				f.setTipoPessoa(set.getString("tipopessoa"));

				lst.add(f);

			}
			
			set.close();
			ps.close();

		} catch (SQLException e) {

			// TODO Auto-generated catch block
			e.printStackTrace();

		} finally {

			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return lst;

	}

	public List<FornecedorBean> buscaFornecedor(String nome, String cpfcnpj)
			throws ProjetoException {

		FuncionarioBean user_session = (FuncionarioBean) FacesContext
				.getCurrentInstance().getExternalContext().getSessionMap()
				.get("obj_usuario");

		String sql = " select * from financeiro.fornecedor where   nome like ?;";
		ResultSet set = null;
		Connection con = ConnectionFactory.getConnection();
		ArrayList<FornecedorBean> lst = new ArrayList<>();

		try {

			PreparedStatement ps = con.prepareStatement(sql);

			ps.setString(1, "%" + nome + "%");

			set = ps.executeQuery();

			while (set.next()) {

				FornecedorBean f = new FornecedorBean();

				f.setCodforn(set.getInt("codforn"));
				f.setNome(set.getString("nome"));
				f.setCpfcnpj(set.getString("cpfcnpj"));

				lst.add(f);

			}

			set.close();
			ps.close();
		} catch (SQLException e) {

			// TODO Auto-generated catch block
			e.printStackTrace();

		} finally {

			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return lst;

	}

}
