package br.gov.al.maceio.sishosp.hosp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.gov.al.maceio.sishosp.acl.dao.FuncionarioDAO;
import br.gov.al.maceio.sishosp.acl.model.FuncionarioBean;
import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;
import br.gov.al.maceio.sishosp.hosp.model.EquipeBean;

import javax.faces.context.FacesContext;

public class EquipeDAO {

    Connection con = null;
    PreparedStatement ps = null;
    FuncionarioDAO pDao = new FuncionarioDAO();

    FuncionarioBean user_session = (FuncionarioBean) FacesContext
            .getCurrentInstance().getExternalContext().getSessionMap()
            .get("obj_usuario");

    public boolean gravarEquipe(EquipeBean equipe) {

        FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
                .getSessionMap().get("obj_funcionario");

        Boolean retorno = false;
        String sql = "insert into hosp.equipe (descequipe, cod_empresa) values (?, ?) RETURNING id_equipe;";

        try {
            con = ConnectionFactory.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, equipe.getDescEquipe().toUpperCase());
            ps.setInt(2, user_session.getEmpresa().getCodEmpresa());
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                retorno = insereEquipeProfissional(rs.getInt("id_equipe"), equipe);
            }

            if (retorno) {
                con.commit();
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
            return retorno;
        }
    }

    public Boolean insereEquipeProfissional(int idEquipe, EquipeBean equipe) {
        Boolean retorno = false;
        String sql = "insert into hosp.equipe_medico (equipe, medico) values(?,?);";

        try {
            ps = con.prepareStatement(sql);
            for (FuncionarioBean prof : equipe.getProfissionais()) {
                ps.setInt(1, idEquipe);
                ps.setLong(2, prof.getId());
                ps.execute();
            }
            retorno = true;

        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        } finally {
            try {
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return retorno;
        }
    }

    public List<EquipeBean> listarEquipe() throws ProjetoException {
        List<EquipeBean> lista = new ArrayList<>();
        String sql = "select id_equipe, descequipe, cod_empresa from hosp.equipe where cod_empresa = ? order by descequipe";

        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setInt(1, user_session.getEmpresa().getCodEmpresa());
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                EquipeBean equipe = new EquipeBean();
                equipe.setCodEquipe(rs.getInt("id_equipe"));
                equipe.setDescEquipe(rs.getString("descequipe"));
                equipe.setCodEmpresa(rs.getInt("cod_empresa"));
                lista.add(equipe);
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

    public List<EquipeBean> listarEquipeBusca(String descricao)
            throws ProjetoException {
        List<EquipeBean> lista = new ArrayList<>();
        String sql = "select id_equipe,id_equipe ||'-'|| descequipe as descequipe, cod_empresa from hosp.equipe " +
                "where upper(id_equipe ||'-'|| descequipe) LIKE ? and cod_empresa = ? order by descequipe";

        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setString(1, "%" + descricao.toUpperCase() + "%");
            stm.setInt(2, user_session.getEmpresa().getCodEmpresa());
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                EquipeBean equipe = new EquipeBean();
                equipe.setCodEquipe(rs.getInt("id_equipe"));
                equipe.setDescEquipe(rs.getString("descequipe"));
                equipe.setCodEmpresa(rs.getInt("cod_empresa"));
                lista.add(equipe);
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

    public List<EquipeBean> listarEquipePorGrupoAutoComplete(String descricao,
                                                             Integer codgrupo) throws ProjetoException {
        List<EquipeBean> lista = new ArrayList<>();
        String sql = "select distinct e.id_equipe, e.id_equipe ||'-'|| e.descequipe as descequipe from hosp.equipe e "
                + " left join hosp.equipe_grupo eg on (e.id_equipe = eg.codequipe) where eg.id_grupo = ? and descequipe like ? order by descequipe ";

        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setInt(1, codgrupo);
            stm.setString(2, "%" + descricao.toUpperCase() + "%");
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                EquipeBean equipe = new EquipeBean();
                equipe.setCodEquipe(rs.getInt("id_equipe"));
                equipe.setDescEquipe(rs.getString("descequipe"));

                lista.add(equipe);
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

    public List<EquipeBean> listarEquipePorProgramaAutoComplete(String descricao,
                                                             Integer codPrograma) throws ProjetoException {
        List<EquipeBean> lista = new ArrayList<>();
        String sql = "select distinct e.id_equipe, e.id_equipe ||'-'|| e.descequipe as descequipe "
                + "from hosp.equipe e "
                + "left join hosp.equipe_grupo eg on (e.id_equipe = eg.codequipe) "
                + "LEFT JOIN hosp.grupo_programa gp ON (gp.codgrupo = eg.id_grupo)"
                + "where gp.codprograma = ? and descequipe like ? order by descequipe ";

        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setInt(1, codPrograma);
            stm.setString(2, "%" + descricao.toUpperCase() + "%");
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                EquipeBean equipe = new EquipeBean();
                equipe.setCodEquipe(rs.getInt("id_equipe"));
                equipe.setDescEquipe(rs.getString("descequipe"));

                lista.add(equipe);
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

    public List<EquipeBean> listarEquipePorGrupo(Integer codgrupo)
            throws ProjetoException {
        List<EquipeBean> lista = new ArrayList<>();
        String sql = "select distinct e.id_equipe, e.id_equipe ||'-'|| e.descequipe as descequipe from hosp.equipe e "
                + " left join hosp.equipe_grupo eg on (e.id_equipe = eg.codequipe) where eg.id_grupo = ? order by descequipe ";

        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setInt(1, codgrupo);
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                EquipeBean equipe = new EquipeBean();
                equipe.setCodEquipe(rs.getInt("id_equipe"));
                equipe.setDescEquipe(rs.getString("descequipe"));

                lista.add(equipe);
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

    public Boolean alterarEquipe(EquipeBean equipe) {
        Boolean retorno = false;
        String sql = "update hosp.equipe set descequipe = ? where id_equipe = ?";
        ps = null;
        try {
            con = ConnectionFactory.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, equipe.getDescEquipe().toUpperCase());
            ps.setInt(2, equipe.getCodEquipe());
            ps.executeUpdate();

            retorno = excluirTabEquipeProf(equipe.getCodEquipe());

            if (retorno) {
                retorno = insereEquipeProfissional(equipe.getCodEquipe(), equipe);
            }

            if (retorno) {
                con.commit();
            }

            return true;
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

    public Boolean excluirTabEquipeProf(int id) {
        Boolean retorno = false;
        String sql = "delete from hosp.equipe_medico where equipe = ?";

        try {
            ps = con.prepareStatement(sql);
            ps.setLong(1, id);
            ps.execute();
            retorno = true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        } finally {
            try {
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return retorno;
        }
    }

    public Boolean excluirEquipe(EquipeBean equipe) throws ProjetoException {
        con = ConnectionFactory.getConnection();
        Boolean retorno = excluirTabEquipeProf(equipe.getCodEquipe());
        String sql = "delete from hosp.equipe where id_equipe = ?";

        try {

            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setLong(1, equipe.getCodEquipe());
            stmt.execute();

            if (retorno) {
                con.commit();
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
            return retorno;
        }
    }

    public EquipeBean buscarEquipePorID(Integer id) throws ProjetoException {
        EquipeBean equipe = null;

        String sql = "select id_equipe, descequipe, cod_empresa from hosp.equipe where id_equipe = ?";

        try {
            con = ConnectionFactory.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                equipe = new EquipeBean();
                equipe.setCodEquipe(rs.getInt("id_equipe"));
                equipe.setDescEquipe(rs.getString("descequipe"));
                equipe.setProfissionais(pDao.listarProfissionaisPorEquipe(rs
                        .getInt("id_equipe"), con));
                equipe.setCodEmpresa(rs.getInt("cod_empresa"));
            }

            return equipe;
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
    }

    public ArrayList<FuncionarioBean> listarProfissionaisDaEquipe(Integer codequipe)
            throws ProjetoException {
        ArrayList<FuncionarioBean> lista = new ArrayList<>();
        String sql = "select e.medico, f.descfuncionario, f.codespecialidade, es.descespecialidade, f.codcbo "
                + "from hosp.equipe_medico e left join acl.funcionarios f on (e.medico = f.id_funcionario) "
                + "left join hosp.especialidade es on (f.codespecialidade = es.id_especialidade) "
                + " where equipe = ? order by f.descfuncionario ";

        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setInt(1, codequipe);
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                FuncionarioBean func = new FuncionarioBean();
                func.setId(rs.getLong("medico"));
                func.setNome(rs.getString("descfuncionario"));
                func.getEspecialidade().setCodEspecialidade(rs.getInt("codespecialidade"));
                func.getEspecialidade().setDescEspecialidade(rs.getString("descespecialidade"));
                func.getCbo().setCodCbo(rs.getInt("codcbo"));

                lista.add(func);
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

    public ArrayList<FuncionarioBean> listarProfissionaisDaEquipeInsercao(Integer codequipe, Boolean todosOsProfissionais)
            throws ProjetoException {
        ArrayList<FuncionarioBean> lista = new ArrayList<>();

        String sql = "";

        if(todosOsProfissionais){
            sql = "select distinct e.medico, f.descfuncionario, f.codespecialidade, es.descespecialidade, f.codcbo "
                    + "from hosp.equipe_medico e left join acl.funcionarios f on (e.medico = f.id_funcionario) "
                    + "left join hosp.especialidade es on (f.codespecialidade = es.id_especialidade) "
                    + " where cod_empresa = ? order by f.descfuncionario ";
        }
        else {
            sql = "select e.medico, f.descfuncionario, f.codespecialidade, es.descespecialidade, f.codcbo "
                    + "from hosp.equipe_medico e left join acl.funcionarios f on (e.medico = f.id_funcionario) "
                    + "left join hosp.especialidade es on (f.codespecialidade = es.id_especialidade) "
                    + " where equipe = ? order by f.descfuncionario ";
        }

        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);

            if(todosOsProfissionais) {
                stm.setInt(1, user_session.getEmpresa().getCodEmpresa());
            }
            else{
                stm.setInt(1, codequipe);
            }
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                FuncionarioBean func = new FuncionarioBean();
                func.setId(rs.getLong("medico"));
                func.setNome(rs.getString("descfuncionario"));
                func.getEspecialidade().setCodEspecialidade(rs.getInt("codespecialidade"));
                func.getEspecialidade().setDescEspecialidade(rs.getString("descespecialidade"));
                func.getCbo().setCodCbo(rs.getInt("codcbo"));

                lista.add(func);
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
