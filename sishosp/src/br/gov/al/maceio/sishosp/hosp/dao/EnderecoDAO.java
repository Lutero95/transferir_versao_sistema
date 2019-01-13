package br.gov.al.maceio.sishosp.hosp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import br.gov.al.maceio.sishosp.acl.model.FuncionarioBean;
import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;
import br.gov.al.maceio.sishosp.hosp.model.EnderecoBean;
import br.gov.al.maceio.sishosp.hosp.model.PacienteBean;

import javax.faces.context.FacesContext;

public class EnderecoDAO {

    private Connection conexao = null;

    //BAIRRO

    public Boolean cadastrarBairros(EnderecoBean endereco) {

        Boolean retorno = false;

        String sql = "insert into hosp.bairros (descbairro, codmunicipio) "
                + " values (?, ?)";

        try {

            conexao = ConnectionFactory.getConnection();
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setString(1, endereco.getBairro().toUpperCase().trim());
            stmt.setInt(2, endereco.getCodmunicipio());

            stmt.execute();
            conexao.commit();
            retorno = true;

        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        } finally {
            try {
                conexao.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return retorno;
        }
    }

    public Boolean alterarBairros(EnderecoBean endereco) {

        Boolean retorno = false;

        String sql = "update hosp.bairros set descbairro = ?, codmunicipio = ? where id_bairro = ?";
        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setString(1, endereco.getBairro().toUpperCase());
            stmt.setInt(2, endereco.getCodmunicipio());
            stmt.setInt(3, endereco.getCodbairro());
            stmt.executeUpdate();

            conexao.commit();

            retorno = true;

        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        } finally {
            try {
                conexao.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return retorno;
        }
    }

    public Boolean excluirBairros(EnderecoBean endereco) {
        Boolean retorno = false;
        String sql = "delete from hosp.bairros where id_bairro = ?";
        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setInt(1, endereco.getCodbairro());
            stmt.executeUpdate();

            conexao.commit();

            retorno = true;

        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        } finally {
            try {
                conexao.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return retorno;
        }
    }

    public EnderecoBean listarBairroPorId(int id) throws ProjetoException {

        EnderecoBean end = new EnderecoBean();

        String sql = "select b.id_bairro, b.descbairro, b.codmunicipio, m.nome, m.uf, m.id_municipio from hosp.bairros b "
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
                end.setMunicipio(rs.getString("nome"));
                end.setUf(rs.getString("uf"));
                end.setCodmunicipio(rs.getInt("id_municipio"));
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        } finally {
            try {
                conexao.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return end;
    }

    public ArrayList<EnderecoBean> listaBairros() throws ProjetoException {

        String sql = "select b.id_bairro, b.descbairro, b.codmunicipio, m.nome, m.uf, m.id_municipio from hosp.bairros b "
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
                end.setMunicipio(rs.getString("nome"));
                end.setUf(rs.getString("uf"));
                end.setCodmunicipio(rs.getInt("id_municipio"));

                lista.add(end);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        } finally {
            try {
                conexao.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return lista;
    }

    public Integer bairroExiste(PacienteBean paciente, Integer codMunicipio)
            throws ProjetoException {

        String sql = "select id_bairro from hosp.bairros where descbairro = ? and codmunicipio = ?";
        int cod = 0;

        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stm = conexao.prepareStatement(sql);
            stm.setString(1, paciente.getEndereco().getBairro().replaceAll("[^a-zZ-Z1-9 ]", ""));
            stm.setInt(2, codMunicipio);

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
                ex.printStackTrace();
            }
        }
        return cod;
    }

    public List<EnderecoBean> buscaBairroAutoComplete(String str, Integer codMunicipio)
            throws ProjetoException {
        PreparedStatement ps = null;
        conexao = ConnectionFactory.getConnection();
        List<EnderecoBean> lista = new ArrayList<EnderecoBean>();
        try {
            String sql = " select id_bairro, desbairro from hosp.bairro where descbairro like ? and id_bairro = ? order by descbairro";

            ps = conexao.prepareStatement(sql);
            ps.setString(1, "%" + str.toUpperCase() + "%");
            ps.setInt(2, codMunicipio);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                EnderecoBean e = new EnderecoBean();
                e.setCodbairro(rs.getInt("id_bairro"));
                e.setBairro(rs.getString("descbairro"));

                lista.add(e);

            }
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ProjetoException(ex);
        } finally {
            try {
                conexao.close();
            } catch (Exception sqlc) {
                sqlc.printStackTrace();
            }
        }
        return lista;
    }

    //MUNICÍPIO

    public Boolean cadastrarMunicipio(EnderecoBean endereco) {
        boolean retorno = false;

        String sql = "insert into hosp.municipio (nome,codigo,uf) "
                + " values (?, ?, ?)";

        try {

            conexao = ConnectionFactory.getConnection();
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setString(1, endereco.getMunicipio().toUpperCase().trim());
            stmt.setInt(2, endereco.getCodIbge());
            stmt.setString(3, endereco.getUf());

            stmt.execute();
            conexao.commit();
            retorno = true;

        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        } finally {
            try {
                conexao.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return retorno;
        }
    }

    public Boolean alterarMunicipio(EnderecoBean endereco) {
        boolean retorno = false;
        String sql = "update hosp.municipio set nome = ?, codigo = ? , uf = ? where id_municipio = ?";
        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setString(1, endereco.getMunicipio().toUpperCase());
            stmt.setInt(2, endereco.getCodIbge());
            stmt.setString(3, endereco.getUf());
            stmt.setInt(4, endereco.getCodmunicipio());
            stmt.executeUpdate();

            conexao.commit();

            retorno = true;

        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        } finally {
            try {
                conexao.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return retorno;
        }
    }


    public Boolean excluirMunicipio(EnderecoBean endereco) {
        boolean retorno = false;
        String sql = "delete from hosp.municipio where id_municipio = ?";
        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setInt(1, endereco.getCodmunicipio());
            stmt.executeUpdate();

            conexao.commit();

            retorno = true;

        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        } finally {
            try {
                conexao.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return retorno;
        }
    }

    public ArrayList<EnderecoBean> listaMunicipios() throws ProjetoException {

        String sql = "select id_municipio, nome, codigo, uf from hosp.municipio order by nome";

        ArrayList<EnderecoBean> lista = new ArrayList();

        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stm = conexao.prepareStatement(sql);
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                EnderecoBean p = new EnderecoBean();

                p.setCodmunicipio(rs.getInt("id_municipio"));
                p.setMunicipio(rs.getString("nome").toUpperCase());
                p.setCodIbge(rs.getInt("codigo"));
                p.setUf(rs.getString("uf"));

                lista.add(p);
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        } finally {
            try {
                conexao.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        return lista;
    }

    public ArrayList<EnderecoBean> listaMunicipiosPorEstado(String uf) throws ProjetoException {

        String sql = "select id_municipio, nome, codigo, uf from hosp.municipio where uf = ? order by nome";

        ArrayList<EnderecoBean> lista = new ArrayList();

        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stm = conexao.prepareStatement(sql);
            stm.setString(1, uf);
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                EnderecoBean p = new EnderecoBean();

                p.setCodmunicipio(rs.getInt("id_municipio"));
                p.setMunicipio(rs.getString("nome").toUpperCase());
                p.setCodIbge(rs.getInt("codigo"));
                p.setUf(rs.getString("uf"));

                lista.add(p);
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        } finally {
            try {
                conexao.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        return lista;
    }


    public EnderecoBean listarMunicipioPorId(int id) throws ProjetoException {

        EnderecoBean end = new EnderecoBean();
        String sql = "select municipio.id_municipio, municipio.nome, codigo, uf from hosp.municipio where id_municipio=? order by nome";
        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stm = conexao.prepareStatement(sql);
            stm.setInt(1, id);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                end = new EnderecoBean();
                end.setId(rs.getLong("id_municipio"));
                end.setCodmunicipio(rs.getInt("id_municipio"));
                end.setMunicipio(rs.getString("nome"));
                end.setCodIbge(rs.getInt("codigo"));
                end.setUf(rs.getString("uf"));
            }

        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        } finally {
            try {
                conexao.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        return end;
    }

    public List<EnderecoBean> buscaMunicipioAutoComplete(String str)
            throws ProjetoException {
        PreparedStatement ps = null;
        conexao = ConnectionFactory.getConnection();
        try {
            String sql = " select id_municipio, nome, codigo from hosp.municipio where nome like ? order by nome";

            ps = conexao.prepareStatement(sql);
            ps.setString(1, "%" + str.toUpperCase() + "%");
            ResultSet rs = ps.executeQuery();

            List<EnderecoBean> lista = new ArrayList<EnderecoBean>();

            while (rs.next()) {
                EnderecoBean e = new EnderecoBean();
                e.setCodmunicipio(rs.getInt("id_municipio"));
                e.setMunicipio(rs.getString("nome"));
                e.setCodIbge(rs.getInt("codigo"));

                lista.add(e);

            }
            return lista;
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        } finally {
            try {
                conexao.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

    }

    public String retornarEstadoDaEmpresa()
            throws ProjetoException {

        String uf = null;
        PreparedStatement ps = null;
        conexao = ConnectionFactory.getConnection();
        FuncionarioBean user_session = (FuncionarioBean) FacesContext
                .getCurrentInstance().getExternalContext().getSessionMap()
                .get("obj_usuario");
        try {
            String sql = " select estado from hosp.empresa where cod_empresa = ?";

            ps = conexao.prepareStatement(sql);
            ps.setInt(1, user_session.getEmpresa().getCodEmpresa());
            ResultSet rs = ps.executeQuery();


            while (rs.next()) {
              uf = rs.getString("estado");

            }
            return uf;
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        } finally {
            try {
                conexao.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

    }

}
