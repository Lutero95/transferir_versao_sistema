package br.gov.al.maceio.sishosp.hosp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.faces.context.FacesContext;

import br.gov.al.maceio.sishosp.acl.model.FuncionarioBean;
import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;
import br.gov.al.maceio.sishosp.hosp.model.ProgramaBean;

public class ProgramaDAO {

    Connection con = null;

    FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
            .getSessionMap().get("obj_funcionario");

    public boolean gravarPrograma(ProgramaBean prog) {

        Boolean retorno = false;
        PreparedStatement ps = null;

        String sql = "insert into hosp.programa (descprograma,  cod_empresa) values (?,  ?) RETURNING id_programa;";
        try {
            con = ConnectionFactory.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, prog.getDescPrograma().toUpperCase());
            ps.setInt(2, user_session.getEmpresa().getCodEmpresa());
            ResultSet rs = ps.executeQuery();

            int idProg = 0;
            if (rs.next()) {
                idProg = rs.getInt("id_programa");
            }

            String sql2 = "insert into hosp.grupo_programa (codprograma, codgrupo) values(?,?);";
            PreparedStatement ps2 = con.prepareStatement(sql2);

            if (prog.getGrupo().size() > 0) {
                for (int i = 0; i < prog.getGrupo().size(); i++) {
                    ps2.setInt(1, idProg);
                    ps2.setInt(2, prog.getGrupo().get(i).getIdGrupo());

                    ps2.execute();
                }
            }
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

    public Boolean alterarPrograma(ProgramaBean prog) {

        Boolean retorno = false;
        String sql = "update hosp.programa set descprograma = ? where id_programa = ?";

        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, prog.getDescPrograma().toUpperCase());
            stmt.setInt(2, prog.getIdPrograma());
            stmt.executeUpdate();

            String sql2 = "delete from hosp.grupo_programa where codprograma = ?";
            PreparedStatement stmt2 = con.prepareStatement(sql2);
            stmt2.setLong(1, prog.getIdPrograma());
            stmt2.execute();

            String sql3 = "insert into hosp.grupo_programa (codprograma, codgrupo) values(?,?);";
            PreparedStatement stmt3 = con.prepareStatement(sql3);

            if (prog.getGrupo().size() > 0) {
                for (int i = 0; i < prog.getGrupo().size(); i++) {
                    stmt3.setInt(1, prog.getIdPrograma());
                    stmt3.setInt(2, prog.getGrupo().get(i).getIdGrupo());

                    stmt3.execute();
                }
            }

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

    public Boolean excluirPrograma(ProgramaBean prog) {

        Boolean retorno = false;

        try {
            con = ConnectionFactory.getConnection();

            String sql1 = "delete from hosp.grupo_programa where codprograma = ?";
            PreparedStatement stmt = con.prepareStatement(sql1);
            stmt.setLong(1, prog.getIdPrograma());
            stmt.execute();

            String sql2 = "delete from hosp.programa where id_programa = ?";
            stmt = con.prepareStatement(sql2);
            stmt.setLong(1, prog.getIdPrograma());
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

    public List<ProgramaBean> listarProgramas() throws ProjetoException {
        List<ProgramaBean> lista = new ArrayList<>();
        String sql = "SELECT DISTINCT id_programa, descprograma "
                + "FROM hosp.programa LEFT JOIN hosp.profissional_programa_grupo ON programa.id_programa = profissional_programa_grupo.codprograma "
                + "WHERE cod_empresa = ? ORDER BY descprograma";


        GrupoDAO gDao = new GrupoDAO();
        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setInt(1, user_session.getEmpresa().getCodEmpresa());

            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                ProgramaBean programa = new ProgramaBean();
                programa.setIdPrograma(rs.getInt("id_programa"));
                programa.setDescPrograma(rs.getString("descprograma"));
                lista.add(programa);
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

    public ArrayList<ProgramaBean> BuscalistarProgramas()
            throws ProjetoException {
        PreparedStatement ps = null;
        con = ConnectionFactory.getConnection();


        String sql = "select id_programa, descprograma from hosp.programa "
                + "join hosp.profissional_programa_grupo on programa.id_programa = profissional_programa_grupo.codprograma "
                + "where programa.cod_empresa = ? "
                + "order by descprograma";
        GrupoDAO gDao = new GrupoDAO();
        ArrayList<ProgramaBean> lista = new ArrayList();
        try {
            ps = con.prepareStatement(sql);
            ps.setInt(1, user_session.getEmpresa().getCodEmpresa());

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                ProgramaBean programa = new ProgramaBean();
                programa.setIdPrograma(rs.getInt("id_programa"));
                programa.setDescPrograma(rs.getString("descprograma"));
                programa.setGrupo(gDao.listarGruposPorProgramaComConexao(rs
                        .getInt("id_programa"), con));
                lista.add(programa);
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

    public List<ProgramaBean> listarProgramasBusca(String descricao,
                                                   Integer tipo) throws ProjetoException {
        List<ProgramaBean> lista = new ArrayList<>();
        String sql = "select distinct id_programa,id_programa ||'-'|| descprograma as descprograma from hosp.programa "
                + "left join hosp.profissional_programa_grupo on programa.id_programa = profissional_programa_grupo.codprograma ";


        if (tipo == 1) {
            sql += " and upper(id_programa ||'-'|| descprograma) LIKE ? and cod_empresa = ? order by descprograma";
        }
        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);

            stm.setString(1, "%" + descricao.toUpperCase() + "%");
            stm.setInt(2, user_session.getEmpresa().getCodEmpresa());

            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                ProgramaBean programa = new ProgramaBean();
                programa.setIdPrograma(rs.getInt("id_programa"));
                programa.setDescPrograma(rs.getString("descprograma"));

                lista.add(programa);
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

    public List<ProgramaBean> listarProgramasBuscaUsuario(String descricao,
                                                          Integer tipo) throws ProjetoException {
        List<ProgramaBean> lista = new ArrayList<>();
        String sql = "select id_programa,id_programa ||'-'|| descprograma as descprograma  from hosp.programa "
                + "left join hosp.profissional_programa_grupo on programa.id_programa = profissional_programa_grupo.codprograma "
                + "where codprofissional = ?";

        if (tipo == 1) {
            sql += " and upper(id_programa ||'-'|| descprograma) LIKE ? order by descprograma";
        }
        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);

            stm.setInt(1, user_session.getCodigo());
            stm.setString(2, "%" + descricao.toUpperCase() + "%");

            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                ProgramaBean programa = new ProgramaBean();
                programa.setIdPrograma(rs.getInt("id_programa"));
                programa.setDescPrograma(rs.getString("descprograma"));

                lista.add(programa);
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

    public List<ProgramaBean> listarProgramasUsuario() throws ProjetoException {
        List<ProgramaBean> lista = new ArrayList<>();
        String sql = "select id_programa,id_programa ||'-'|| descprograma as descprograma  from hosp.programa "
                + "left join hosp.profissional_programa_grupo on programa.id_programa = profissional_programa_grupo.codprograma "
                + "where codprofissional = ? order by descprograma";

        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
            FuncionarioBean user_session = (FuncionarioBean) FacesContext
                    .getCurrentInstance().getExternalContext().getSessionMap()
                    .get("obj_usuario");

            stm.setLong(1, user_session.getId());

            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                ProgramaBean programa = new ProgramaBean();
                programa.setIdPrograma(rs.getInt("id_programa"));
                programa.setDescPrograma(rs.getString("descprograma"));

                lista.add(programa);
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

    public ProgramaBean listarProgramaPorId(int id) throws ProjetoException {

        ProgramaBean programa = new ProgramaBean();
        GrupoDAO gDao = new GrupoDAO();
        String sql = "select id_programa, descprograma  from hosp.programa where id_programa = ? order by descprograma";
        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setInt(1, id);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                programa = new ProgramaBean();
                programa.setIdPrograma(rs.getInt("id_programa"));
                programa.setDescPrograma(rs.getString("descprograma"));
                programa.setGrupo(gDao.listarGruposPorProgramaComConexao(rs
                        .getInt("id_programa"), con));
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
        return programa;
    }

    public List<ProgramaBean> listarProgramasEGrupos() throws ProjetoException {
        List<ProgramaBean> lista = new ArrayList<>();
        String sql = "select gp.codprograma, p.descprograma, gp.codgrupo, g.descgrupo "
                + "from hosp.grupo_programa gp "
                + "left join hosp.programa p on (gp.codprograma = p.id_programa) "
                + "left join hosp.grupo g on (gp.codgrupo = g.id_grupo) "
                + "where p.cod_empresa = ? order by p.descprograma";

        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setInt(1, user_session.getEmpresa().getCodEmpresa());
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                ProgramaBean programa = new ProgramaBean();
                programa.setIdPrograma(rs.getInt("codprograma"));
                programa.setDescPrograma(rs.getString("descprograma"));
                programa.getGrupoBean().setIdGrupo(rs.getInt("codgrupo"));
                programa.getGrupoBean().setDescGrupo(rs.getString("descgrupo"));

                lista.add(programa);
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
