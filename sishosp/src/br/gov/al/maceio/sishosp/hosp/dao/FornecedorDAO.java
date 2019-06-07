package br.gov.al.maceio.sishosp.hosp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.gov.al.maceio.sishosp.acl.model.FuncionarioBean;
import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;
import br.gov.al.maceio.sishosp.hosp.model.FornecedorBean;

import javax.faces.context.FacesContext;

public class FornecedorDAO {

    Connection con = null;
    PreparedStatement ps = null;

    FuncionarioBean user_session = (FuncionarioBean) FacesContext
            .getCurrentInstance().getExternalContext().getSessionMap()
            .get("obj_usuario");

    public Boolean gravarFornecedor(FornecedorBean fornecedor) {

        Boolean retorno = false;

        String sql = "INSERT INTO hosp.fornecedor (descfornecedor, cnpj, endereco, bairro, cep, cod_municipio, estado, " +
                "telefone1, telefone2, ie, cod_empresa, valor) " +
                "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
        try {

            con = ConnectionFactory.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, fornecedor.getDescricao().toUpperCase());
            ps.setString(2, fornecedor.getCnpj().toUpperCase());
            ps.setString(3, fornecedor.getEndereco().getLogradouro().toUpperCase());
            ps.setString(4, fornecedor.getEndereco().getBairro().toUpperCase());
            ps.setString(5, fornecedor.getEndereco().getCep().toUpperCase());
            ps.setInt(6, fornecedor.getEndereco().getCodmunicipio());
            ps.setString(7, fornecedor.getEndereco().getUf().toUpperCase());
            ps.setString(8, fornecedor.getTelefone1().toUpperCase());
            ps.setString(9, fornecedor.getTelefone2().toUpperCase());
            ps.setString(10, fornecedor.getIe().toUpperCase());
            ps.setInt(11, user_session.getEmpresa().getCodEmpresa());
            ps.setDouble(12, fornecedor.getValor());

            ps.execute();
            con.commit();

            retorno = true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        } finally {
            try {
                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return retorno;
        }
    }

    public List<FornecedorBean> listarFornecedores() throws ProjetoException {
        List<FornecedorBean> lista = new ArrayList<>();
        String sql = "SELECT id_fornecedor, descfornecedor, cnpj, endereco, valor, bairro, cep, cod_municipio, estado, " +
                "telefone1, telefone2, ie FROM hosp.fornecedor WHERE cod_empresa = ? ORDER BY descfornecedor";
        try {

            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setInt(1, user_session.getEmpresa().getCodEmpresa());
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                FornecedorBean fornecedor = new FornecedorBean();
                fornecedor.setId(rs.getInt("id_fornecedor"));
                fornecedor.setDescricao(rs.getString("descfornecedor"));
                fornecedor.setCnpj(rs.getString("cnpj"));
                fornecedor.getEndereco().setLogradouro(rs.getString("endereco"));
                fornecedor.setValor(rs.getDouble("valor"));
                fornecedor.getEndereco().setBairro(rs.getString("bairro"));
                fornecedor.getEndereco().setCep("cep");
                fornecedor.getEndereco().setCodmunicipio(rs.getInt("cod_municipio"));
                fornecedor.getEndereco().setUf(rs.getString("estado"));
                fornecedor.setTelefone1(rs.getString("telefone1"));
                fornecedor.setTelefone2(rs.getString("telefone2"));
                fornecedor.setIe(rs.getString("ie"));
                lista.add(fornecedor);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        } finally {
            try {
                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return lista;
    }

    public Boolean alterarFornecedor(FornecedorBean fornecedor) {

        Boolean retorno = false;

        String sql = "UPDATE hosp.fornecedor SET descfornecedor = ?, cnpj = ?, endereco = ?, bairro = ?, cep = ?, cod_municipio = ?, estado = ?, " +
                "telefone1 = ?, telefone2 = ?, ie = ?, valor = ? WHERE id_fornecedor = ?";
        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, fornecedor.getDescricao().toUpperCase());
            ps.setString(2, fornecedor.getCnpj().toUpperCase());
            ps.setString(3, fornecedor.getEndereco().getLogradouro().toUpperCase());
            ps.setString(4, fornecedor.getEndereco().getBairro().toUpperCase());
            ps.setString(5, fornecedor.getEndereco().getCep().toUpperCase());
            ps.setInt(6, fornecedor.getEndereco().getCodmunicipio());
            ps.setString(7, fornecedor.getEndereco().getUf().toUpperCase());
            ps.setString(8, fornecedor.getTelefone1().toUpperCase());
            ps.setString(9, fornecedor.getTelefone2().toUpperCase());
            ps.setString(10, fornecedor.getIe().toUpperCase());
            ps.setDouble(11, fornecedor.getValor());
            ps.setInt(12, fornecedor.getId());
            ps.executeUpdate();
            con.commit();
            retorno = true;

        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        } finally {
            try {
                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return retorno;
        }
    }

    public Boolean excluirFornecedor(Integer id) {

        Boolean retorno = false;

        String sql = "DELETE FROM hosp.fornecedor WHERE id_fornecedor = ?";
        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setLong(1, id);
            stmt.execute();
            con.commit();

            retorno = true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        } finally {
            try {
                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return retorno;
        }
    }

    public FornecedorBean listarFornecedorPorId(int id) throws ProjetoException {

        FornecedorBean fornecedor = new FornecedorBean();
        String sql = "SELECT id_fornecedor, descfornecedor, cnpj, endereco, valor, bairro, cep, cod_municipio, estado, " +
                "telefone1, telefone2, ie FROM hosp.fornecedor WHERE id_fornecedor = ?";
        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setInt(1, id);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                fornecedor.setId(rs.getInt("id_fornecedor"));
                fornecedor.setDescricao(rs.getString("descfornecedor"));
                fornecedor.setCnpj(rs.getString("cnpj"));
                fornecedor.getEndereco().setLogradouro(rs.getString("endereco"));
                fornecedor.setValor(rs.getDouble("valor"));
                fornecedor.getEndereco().setBairro(rs.getString("bairro"));
                fornecedor.getEndereco().setCep("cep");
                fornecedor.getEndereco().setCodmunicipio(rs.getInt("cod_municipio"));
                fornecedor.getEndereco().setUf(rs.getString("estado"));
                fornecedor.setTelefone1(rs.getString("telefone1"));
                fornecedor.setTelefone2(rs.getString("telefone2"));
                fornecedor.setIe(rs.getString("ie"));
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        } finally {
            try {
                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
                System.exit(1);
            }
        }
        return fornecedor;
    }

    public List<FornecedorBean> listarFornecedorBusca(String descricao)
            throws ProjetoException {
        List<FornecedorBean> lista = new ArrayList<>();
        String sql = "SELECT id_fornecedor, descfornecedor FROM hosp.fornecedor WHERE descfornecedor LIKE ? AND cod_empresa = ? ORDER BY descfornecedor";

        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setString(1, "%" + descricao.toUpperCase() + "%");
            stm.setInt(2, user_session.getEmpresa().getCodEmpresa());
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                FornecedorBean f = new FornecedorBean();
                f.setId(rs.getInt("id_fornecedor"));
                f.setDescricao(rs.getString("descfornecedor"));

                lista.add(f);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        } finally {
            try {
                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        return lista;
    }

}
