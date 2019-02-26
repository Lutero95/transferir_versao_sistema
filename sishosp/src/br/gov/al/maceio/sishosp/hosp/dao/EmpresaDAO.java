package br.gov.al.maceio.sishosp.hosp.dao;

import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;
import br.gov.al.maceio.sishosp.hosp.model.EmpresaBean;
import br.gov.al.maceio.sishosp.hosp.model.ParametroBean;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmpresaDAO {

    Connection con = null;
    PreparedStatement ps = null;

    public boolean gravarEmpresa(EmpresaBean empresa) {
        Boolean retorno = false;
        Integer codEmpresa = null;

        String sql = "INSERT INTO hosp.empresa(nome_principal, nome_fantasia, cnpj, rua, bairro, numero, cep, cidade, " +
                " estado, complemento, ddd_1, telefone_1, ddd_2, telefone_2, email, site, matriz, ativo) " +
                " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, true) returning cod_empresa;";

        try {
            con = ConnectionFactory.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, empresa.getNomeEmpresa());
            ps.setString(2, empresa.getNomeFantasia());
            ps.setString(3, empresa.getCnpj());
            ps.setString(4, empresa.getRua());
            ps.setString(5, empresa.getBairro());
            ps.setInt(6, empresa.getNumero());
            ps.setString(7, empresa.getCep());
            ps.setString(8, empresa.getCidade());
            ps.setString(9, empresa.getEstado());
            ps.setString(10, empresa.getComplemento());
            ps.setInt(11, empresa.getDdd1());
            ps.setInt(12, empresa.getTelefone1());
            if(empresa.getDdd2() != null) {
                ps.setInt(13, empresa.getDdd2());
            }
            else{
                ps.setNull(13, Types.NULL);
            }
            if(empresa.getTelefone2() != null) {
                ps.setInt(14, empresa.getTelefone2());
            }
            else{
                ps.setNull(14, Types.NULL);
            }
            if(empresa.getEmail() != null) {
                ps.setString(15, empresa.getEmail());
            }
            else{
                ps.setNull(15, Types.NULL);
            }
            if(empresa.getSite() != null) {
                ps.setString(16, empresa.getSite());
            }
            else{
                ps.setNull(16, Types.NULL);
            }
            if(empresa.getMatriz() != null) {
                ps.setBoolean(17, empresa.getMatriz());
            }
            else{
                ps.setNull(17, Types.NULL);
            }

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                codEmpresa = rs.getInt("cod_empresa");
            }

            sql = "INSERT INTO hosp.parametro(motivo_padrao_desligamento_opm, opcao_atendimento, qtd_simultanea_atendimento_profissional, qtd_simultanea_atendimento_equipe, cod_empresa) " +
                    " VALUES (?, ?, ?, ?, ?)";

            ps = con.prepareStatement(sql);

            if(empresa.getParametro().getMotivoDesligamento() != null) {
                ps.setInt(1, empresa.getParametro().getMotivoDesligamento());
            }
            else{
                ps.setNull(1, Types.NULL);
            }
            if(empresa.getParametro().getOpcaoAtendimento() != null) {
                ps.setString(2, empresa.getParametro().getOpcaoAtendimento());
            }
            else{
                ps.setNull(2, Types.NULL);
            }
            if(empresa.getParametro().getQuantidadeSimultaneaProfissional() != null) {
                ps.setInt(3, empresa.getParametro().getQuantidadeSimultaneaProfissional());
            }
            else{
                ps.setNull(3, Types.NULL);
            }
            if(empresa.getParametro().getQuantidadeSimultaneaEquipe() != null) {
                ps.setInt(4, empresa.getParametro().getQuantidadeSimultaneaEquipe());
            }
            else{
                ps.setNull(4, Types.NULL);
            }
            ps.setInt(5, codEmpresa);

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

    public List<EmpresaBean> listarEmpresa() throws ProjetoException {

        List<EmpresaBean> lista = new ArrayList<>();
        String sql = "SELECT cod_empresa, nome_principal, nome_fantasia, cnpj, rua, bairro, " +
                " numero, complemento, cep, cidade, estado, ddd_1, telefone_1, ddd_2, telefone_2, " +
                " email, site, matriz, ativo, case when matriz is true then 'Matriz' else 'Filial' end as tipo " +
                " FROM hosp.empresa where ativo is true;";

        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                EmpresaBean empresa = new EmpresaBean();
                empresa.setCodEmpresa(rs.getInt("cod_empresa"));
                empresa.setNomeEmpresa(rs.getString("nome_principal"));
                empresa.setNomeFantasia(rs.getString("nome_fantasia"));
                empresa.setCnpj(rs.getString("cnpj"));
                empresa.setRua(rs.getString("rua"));
                empresa.setBairro(rs.getString("bairro"));
                empresa.setNumero(rs.getInt("numero"));
                empresa.setComplemento(rs.getString("complemento"));
                empresa.setCep(rs.getString("cep"));
                empresa.setCidade(rs.getString("cidade"));
                empresa.setEstado(rs.getString("estado"));
                empresa.setDdd1(rs.getInt("ddd_1"));
                empresa.setTelefone1(rs.getInt("telefone_1"));
                empresa.setDdd2(rs.getInt("ddd_2"));
                empresa.setTelefone2(rs.getInt("telefone_2"));
                empresa.setEmail(rs.getString("email"));
                empresa.setSite(rs.getString("site"));
                empresa.setMatriz(rs.getBoolean("matriz"));
                empresa.setAtivo(rs.getBoolean("ativo"));
                empresa.setTipoString(rs.getString("tipo"));
                empresa.setParametro(carregarParametro(empresa.getCodEmpresa(), con));

                lista.add(empresa);
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

    public Boolean alterarEmpresa(EmpresaBean empresa) {

        Boolean retorno = false;
        String sql = "UPDATE hosp.empresa SET nome_principal=?, nome_fantasia=?, cnpj=?, rua=?, " +
                " bairro=?, numero=?, cep=?, cidade=?, estado=?, ddd_1=?, telefone_1=?, " +
                " ddd_2=?, telefone_2=?, email=?, site=?, matriz=?, complemento=? " +
                " WHERE cod_empresa = ?;";
        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, empresa.getNomeEmpresa());
            ps.setString(2, empresa.getNomeFantasia());
            ps.setString(3, empresa.getCnpj());
            ps.setString(4, empresa.getRua());
            ps.setString(5, empresa.getBairro());
            ps.setInt(6, empresa.getNumero());
            ps.setString(7, empresa.getCep());
            ps.setString(8, empresa.getCidade());
            ps.setString(9, empresa.getEstado());
            ps.setInt(10, empresa.getDdd1());
            ps.setInt(11, empresa.getTelefone1());
            ps.setInt(12, empresa.getDdd2());
            ps.setInt(13, empresa.getTelefone2());
            ps.setString(14, empresa.getEmail());
            ps.setString(15, empresa.getSite());
            ps.setBoolean(16, empresa.getMatriz());
            ps.setString(17, empresa.getComplemento());
            ps.setInt(18, empresa.getCodEmpresa());
            ps.executeUpdate();

            sql = "UPDATE hosp.parametro SET motivo_padrao_desligamento_opm = ?, opcao_atendimento = ?, " +
                    "qtd_simultanea_atendimento_profissional = ?, qtd_simultanea_atendimento_equipe = ? " +
                    "WHERE cod_empresa = ?";

            ps = con.prepareStatement(sql);

            ps.setInt(1, empresa.getParametro().getMotivoDesligamento());
            ps.setString(2, empresa.getParametro().getOpcaoAtendimento());
            ps.setInt(3, empresa.getParametro().getQuantidadeSimultaneaProfissional());
            ps.setInt(4, empresa.getParametro().getQuantidadeSimultaneaEquipe());
            ps.setInt(5, empresa.getCodEmpresa());
            ps.executeUpdate();

            con.commit();
            retorno = true;

        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        } finally {
            try {
                con.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
            return retorno;
        }
    }

    public Boolean desativarEmpresa(EmpresaBean empresa) {

        Boolean retorno = false;
        String sql = "update hosp.empresa set ativo = false where cod_empresa = ?";

        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setLong(1, empresa.getCodEmpresa());
            stmt.execute();

            con.commit();
            retorno = true;

        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        } finally {
            try {
                con.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
            return retorno;
        }
    }

    public EmpresaBean buscarEmpresaPorId(Integer id) throws ProjetoException {

        EmpresaBean empresa = new EmpresaBean();
        String sql = "SELECT cod_empresa, nome_principal, nome_fantasia, cnpj, rua, bairro, " +
                " numero, complemento, cep, cidade, estado, ddd_1, telefone_1, ddd_2, telefone_2, " +
                " email, site, matriz, ativo " +
                " FROM hosp.empresa where cod_empresa = ?;";

        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                empresa.setCodEmpresa(rs.getInt("cod_empresa"));
                empresa.setNomeEmpresa(rs.getString("nome_principal"));
                empresa.setNomeFantasia(rs.getString("nome_fantasia"));
                empresa.setCnpj(rs.getString("cnpj"));
                empresa.setRua(rs.getString("rua"));
                empresa.setBairro(rs.getString("bairro"));
                empresa.setNumero(rs.getInt("numero"));
                empresa.setComplemento(rs.getString("complemento"));
                empresa.setCep(rs.getString("cep"));
                empresa.setCidade(rs.getString("cidade"));
                empresa.setEstado(rs.getString("estado"));
                empresa.setDdd1(rs.getInt("ddd_1"));
                empresa.setTelefone1(rs.getInt("telefone_1"));
                empresa.setDdd2(rs.getInt("ddd_2"));
                empresa.setTelefone2(rs.getInt("telefone_2"));
                empresa.setEmail(rs.getString("email"));
                empresa.setSite(rs.getString("site"));
                empresa.setMatriz(rs.getBoolean("matriz"));
                empresa.setAtivo(rs.getBoolean("ativo"));
                empresa.setParametro(carregarParametro(id, con));

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
        return empresa;
    }

    public ParametroBean carregarParametro(Integer id, Connection conAuxiliar) {

        ParametroBean parametro = new ParametroBean();

        String sql = "SELECT id, motivo_padrao_desligamento_opm, opcao_atendimento, qtd_simultanea_atendimento_profissional, qtd_simultanea_atendimento_equipe " +
                " FROM hosp.parametro where cod_empresa = ?;";

        try {
            PreparedStatement ps = conAuxiliar.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                parametro.setMotivoDesligamento(rs.getInt("motivo_padrao_desligamento_opm"));
                parametro.setOpcaoAtendimento(rs.getString("opcao_atendimento"));
                parametro.setQuantidadeSimultaneaProfissional(rs.getInt("qtd_simultanea_atendimento_profissional"));
                parametro.setQuantidadeSimultaneaEquipe(rs.getInt("qtd_simultanea_atendimento_equipe"));

            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        } finally {
            try {
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return parametro;
    }

}
