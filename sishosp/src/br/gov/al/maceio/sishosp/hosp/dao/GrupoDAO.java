package br.gov.al.maceio.sishosp.hosp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import javax.faces.context.FacesContext;

import br.gov.al.maceio.sishosp.acl.model.FuncionarioBean;
import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;
import br.gov.al.maceio.sishosp.comum.util.TratamentoErrosUtil;
import br.gov.al.maceio.sishosp.hosp.model.EquipeBean;
import br.gov.al.maceio.sishosp.hosp.model.GrupoBean;
import br.gov.al.maceio.sishosp.hosp.model.ProgramaBean;
import br.gov.al.maceio.sishosp.hosp.model.UnidadeBean;
import br.gov.al.maceio.sishosp.hosp.model.dto.BuscaGrupoFrequenciaDTO;
import br.gov.al.maceio.sishosp.hosp.model.dto.GrupoProgramaUnidadeDTO;

public class GrupoDAO {

    Connection con = null;
    PreparedStatement ps = null;

    FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
            .getSessionMap().get("obj_funcionario");

    public boolean gravarGrupo(GrupoBean grupo) throws ProjetoException {

        Boolean retorno = false;
        String sql = "insert into hosp.grupo (descgrupo, auditivo, insercao_pac_institut, cod_unidade) values (?, ?, ?, ?) RETURNING id_grupo;";

        try {
            con = ConnectionFactory.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, grupo.getDescGrupo().toUpperCase());
            ps.setBoolean(2, grupo.isAuditivo());
            if (grupo.isinsercao_pac_institut() == false) {
                ps.setNull(3, Types.BOOLEAN);
            } else {
                ps.setBoolean(3, grupo.isinsercao_pac_institut());
            }
            ps.setInt(4, user_session.getUnidade().getId());
            ResultSet rs = ps.executeQuery();

            Integer idGrupo = 0;

            if (rs.next()) {
                idGrupo = rs.getInt("id_grupo");
                String sql2 = "insert into hosp.equipe_grupo (id_grupo, codequipe) values(?,?);";
                PreparedStatement ps2 = null;
                for (EquipeBean eq : grupo.getEquipes()) {
                    ps2 = null;
                    ps2 = con.prepareStatement(sql2);
                    ps2.setInt(1, idGrupo);
                    ps2.setInt(2, eq.getCodEquipe());
                    ps2.execute();
                }
            }

            con.commit();
            retorno = true;
        } catch (SQLException sqle) {
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
            throw new ProjetoException(ex, this.getClass().getName());
        } finally {
            try {
                con.close();
            } catch (Exception ex) {
                //comentado walter erro log ex.printStackTrace();
            }
        }
        return retorno;
    }

    public Boolean alterarGrupo(GrupoBean grupo) throws ProjetoException {
        Boolean retorno = false;
        String sql = "update hosp.grupo set descgrupo = ?, auditivo = ?, insercao_pac_institut = ? where id_grupo = ?";

        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, grupo.getDescGrupo().toUpperCase());
            stmt.setBoolean(2, grupo.isAuditivo());
            if (grupo.isinsercao_pac_institut() == false) {
                stmt.setNull(3, Types.BOOLEAN);
            } else {
                stmt.setBoolean(3, grupo.isinsercao_pac_institut());
            }
            stmt.setInt(4, grupo.getIdGrupo());
            stmt.executeUpdate();

            String sql2 = "delete from  hosp.equipe_grupo where id_grupo=?";
            PreparedStatement ps2 = null;
            ps2 = con.prepareStatement(sql2);
            ps2.setInt(1, grupo.getIdGrupo());
            ps2.execute();

            String sql3 = "insert into hosp.equipe_grupo (id_grupo, codequipe) values(?,?);";
            PreparedStatement ps3 = null;

            for (EquipeBean eq : grupo.getEquipes()) {
                ps3 = null;
                ps3 = con.prepareStatement(sql3);
                ps3.setInt(1, grupo.getIdGrupo());
                ps3.setInt(2, eq.getCodEquipe());
                ps3.execute();
            }

            con.commit();
            retorno = true;
        } catch (SQLException sqle) {
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
            throw new ProjetoException(ex, this.getClass().getName());
        } finally {
            try {
                con.close();
            } catch (Exception ex) {
                //comentado walter erro log ex.printStackTrace();
            }
        }
        return retorno;
    }

    public Boolean excluirGrupo(GrupoBean grupo) throws ProjetoException {
        Boolean retorno = false;
        String sqlGrupo = "delete from hosp.grupo where id_grupo = ?";
        String sqlEquipe = "delete from hosp.equipe_grupo where id_grupo = ?";

        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stmt = con.prepareStatement(sqlEquipe);
            stmt.setLong(1, grupo.getIdGrupo());
            stmt.execute();

            stmt = con.prepareStatement(sqlGrupo);
            stmt.setLong(1, grupo.getIdGrupo());
            stmt.execute();

            con.commit();
            retorno = true;
        } catch (SQLException sqle) {
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
            throw new ProjetoException(ex, this.getClass().getName());
        } finally {
            try {
                con.close();
            } catch (Exception ex) {
                //comentado walter erro log ex.printStackTrace();
            }
        }
        return retorno;
    }

    public List<EquipeBean> listarEquipesDoGrupo(int id, Connection conAuxiliar)
            throws ProjetoException, SQLException {

        List<EquipeBean> lista = new ArrayList<EquipeBean>();
        String sql = "select eg.codequipe, e.descequipe from hosp.equipe_grupo eg"
                + " join hosp.equipe e on e.id_equipe = eg.codequipe"
                + " where eg.id_grupo=?";

        try {
            PreparedStatement stm = conAuxiliar.prepareStatement(sql);
            stm.setInt(1, id);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                EquipeBean equipe = new EquipeBean();
                equipe.setCodEquipe(rs.getInt("codequipe"));
                equipe.setDescEquipe(rs.getString("descequipe"));
                lista.add(equipe);
            }
        } catch (SQLException sqle) {
            conAuxiliar.rollback();
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
            conAuxiliar.rollback();
            throw new ProjetoException(ex, this.getClass().getName());
        }
        return lista;
    }

    public List<GrupoBean> listarGruposPorPrograma(int codPrograma)
            throws ProjetoException {
        List<GrupoBean> lista = new ArrayList<>();
        String sql = "select distinct g.id_grupo, g.descgrupo, g.auditivo, g.equipe, g.insercao_pac_institut from hosp.grupo g  "
                + "left join hosp.grupo_programa gp on (g.id_grupo = gp.codgrupo) left join hosp.programa p on (gp.codprograma = p.id_programa)  "
                + "where p.id_programa = ?";

        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setInt(1, codPrograma);
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                GrupoBean grupo = new GrupoBean();
                grupo.setIdGrupo(rs.getInt("id_grupo"));
                grupo.setDescGrupo(rs.getString("descgrupo"));
                grupo.setAuditivo(rs.getBoolean("auditivo"));
                grupo.setEquipeSim(rs.getBoolean("equipe"));
                grupo.setinsercao_pac_institut(rs
                        .getBoolean("insercao_pac_institut"));
                lista.add(grupo);
            }

        } catch (SQLException sqle) {
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
            throw new ProjetoException(ex, this.getClass().getName());
        } finally {
            try {
                con.close();
            } catch (Exception ex) {
                //comentado walter erro log ex.printStackTrace();
            }
        }
        return lista;
    }

    public List<GrupoBean> listarGruposPorProgramaComConexao(int codPrograma, Connection conAuxiliar)
            throws ProjetoException, SQLException {
        List<GrupoBean> lista = new ArrayList<>();
        String sql = "select distinct g.id_grupo, g.descgrupo, g.auditivo, g.equipe, g.insercao_pac_institut from hosp.grupo g  "
                + "left join hosp.grupo_programa gp on (g.id_grupo = gp.codgrupo) left join hosp.programa p on (gp.codprograma = p.id_programa)  "
                + "where p.id_programa = ?";

        try {
            PreparedStatement stm = conAuxiliar.prepareStatement(sql);
            stm.setInt(1, codPrograma);
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                GrupoBean grupo = new GrupoBean();
                grupo.setIdGrupo(rs.getInt("id_grupo"));
                grupo.setDescGrupo(rs.getString("descgrupo"));
                grupo.setAuditivo(rs.getBoolean("auditivo"));
                grupo.setEquipeSim(rs.getBoolean("equipe"));
                grupo.setinsercao_pac_institut(rs
                        .getBoolean("insercao_pac_institut"));
                lista.add(grupo);
            }

        } catch (SQLException sqle) {
            conAuxiliar.rollback();
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
            conAuxiliar.rollback();
            throw new ProjetoException(ex, this.getClass().getName());
        }
        return lista;
    }

    public List<GrupoBean> listarGruposPorTipoAtend(int idTipo, Connection conAuxiliar)
            throws ProjetoException, SQLException {
        List<GrupoBean> lista = new ArrayList<>();
        String sql = "select g.id_grupo, g.descgrupo, g.auditivo, g.insercao_pac_institut from hosp.grupo g, "
                + " hosp.tipoatendimento_grupo tg, hosp.tipoatendimento t"
                + " where t.id = ? and g.cod_unidade = ? and g.id_grupo = tg.codgrupo and t.id = tg.codtipoatendimento order by g.descgrupo";
        try {
            PreparedStatement stm = conAuxiliar.prepareStatement(sql);
            stm.setInt(1, idTipo);
            stm.setInt(2, user_session.getUnidade().getId());
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                GrupoBean grupo = new GrupoBean();
                grupo.setIdGrupo(rs.getInt("id_grupo"));
                grupo.setDescGrupo(rs.getString("descgrupo"));
                grupo.setAuditivo(rs.getBoolean("auditivo"));
                grupo.setinsercao_pac_institut(rs
                        .getBoolean("insercao_pac_institut"));
                lista.add(grupo);
            }

        } catch (SQLException sqle) {
            conAuxiliar.rollback();
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
            conAuxiliar.rollback();
            throw new ProjetoException(ex, this.getClass().getName());
        }
        return lista;
    }

    public List<GrupoBean> listarGrupos() throws ProjetoException {
        List<GrupoBean> lista = new ArrayList<>();
        String sql = "select id_grupo, descgrupo, auditivo, insercao_pac_institut from hosp.grupo where cod_unidade = ? order by descgrupo";
        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setInt(1, user_session.getUnidade().getId());
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                GrupoBean grupo = new GrupoBean();
                grupo.setIdGrupo(rs.getInt("id_grupo"));
                grupo.setDescGrupo(rs.getString("descgrupo"));
                grupo.setAuditivo(rs.getBoolean("auditivo"));
                grupo.setinsercao_pac_institut(rs
                        .getBoolean("insercao_pac_institut"));
                lista.add(grupo);
            }
        } catch (SQLException sqle) {
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
            throw new ProjetoException(ex, this.getClass().getName());
        } finally {
            try {
                con.close();
            } catch (Exception ex) {
                //comentado walter erro log ex.printStackTrace();
            }
        }
        return lista;
    }

    public List<GrupoBean> listarGruposDoPrograma(Integer codprograma)
            throws ProjetoException {
        List<GrupoBean> lista = new ArrayList<>();
        String sql = "select gp.codgrupo, g.descgrupo from hosp.grupo_programa gp left join hosp.grupo g on (gp.codgrupo = g.id_grupo) "
                + " where codprograma = ? order by g.descgrupo";

        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setInt(1, codprograma);
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                GrupoBean grupo = new GrupoBean();
                grupo.setIdGrupo(rs.getInt("codgrupo"));
                grupo.setDescGrupo(rs.getString("descgrupo"));
                lista.add(grupo);
            }
        } catch (SQLException sqle) {
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
            throw new ProjetoException(ex, this.getClass().getName());
        } finally {
            try {
                con.close();
            } catch (Exception ex) {
                //comentado walter erro log ex.printStackTrace();
            }
        }
        return lista;
    }


    public List<GrupoBean> listarGruposAutoComplete(String descricao, ProgramaBean prog) throws ProjetoException {
        List<GrupoBean> lista = new ArrayList<>();
        String sql = "select distinct g.id_grupo, g.id_grupo ||'-'|| g.descgrupo as descgrupo, g.auditivo, g.equipe, g.insercao_pac_institut  "
                + " from hosp.grupo g left join hosp.grupo_programa gp on (g.id_grupo = gp.codgrupo) left join hosp.programa p on (gp.codprograma = p.id_programa)"
                + " where p.id_programa = ? and g.cod_unidade = ? and upper(g.id_grupo ||'-'|| g.descgrupo) LIKE ? order by descgrupo ";

        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setInt(1, prog.getIdPrograma());
            stm.setInt(2, user_session.getUnidade().getId());
            stm.setString(3, "%" + descricao.toUpperCase() + "%");
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                GrupoBean grupo = new GrupoBean();
                grupo.setIdGrupo(rs.getInt("id_grupo"));
                grupo.setDescGrupo(rs.getString("descgrupo"));
                grupo.setAuditivo(rs.getBoolean("auditivo"));
                grupo.setEquipeSim(rs.getBoolean("equipe"));
                grupo.setinsercao_pac_institut(rs
                        .getBoolean("insercao_pac_institut"));
                lista.add(grupo);
            }
        } catch (SQLException sqle) {
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
            throw new ProjetoException(ex, this.getClass().getName());
        } finally {
            try {
                con.close();
            } catch (Exception ex) {
                //comentado walter erro log ex.printStackTrace();
            }
        }
        return lista;
    }

    public List<GrupoBean> listarGruposNoAutoComplete(String descricao, Integer codPrograma) throws ProjetoException {
        List<GrupoBean> lista = new ArrayList<>();
        String sql = "select distinct g.id_grupo, g.id_grupo ||'-'|| g.descgrupo as descgrupo, g.auditivo, g.equipe, g.insercao_pac_institut  "
                + " from hosp.grupo g left join hosp.grupo_programa gp on (g.id_grupo = gp.codgrupo) left join hosp.programa p on (gp.codprograma = p.id_programa)"
                + " where p.id_programa = ? and g.cod_unidade = ? and upper(g.id_grupo ||'-'|| g.descgrupo) LIKE ? order by descgrupo ";

        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setInt(1, codPrograma);
            stm.setInt(2, user_session.getUnidade().getId());
            stm.setString(3, "%" + descricao.toUpperCase() + "%");
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                GrupoBean grupo = new GrupoBean();
                grupo.setIdGrupo(rs.getInt("id_grupo"));
                grupo.setDescGrupo(rs.getString("descgrupo"));
                grupo.setAuditivo(rs.getBoolean("auditivo"));
                grupo.setEquipeSim(rs.getBoolean("equipe"));
                grupo.setinsercao_pac_institut(rs
                        .getBoolean("insercao_pac_institut"));
                lista.add(grupo);
            }
        } catch (SQLException sqle) {
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
            throw new ProjetoException(ex, this.getClass().getName());
        } finally {
            try {
                con.close();
            } catch (Exception ex) {
                //comentado walter erro log ex.printStackTrace();
            }
        }
        return lista;
    }

    public List<GrupoBean> listarGruposGeralAutoComplete(String descricao) throws ProjetoException {
        List<GrupoBean> lista = new ArrayList<>();
        String sql = "select distinct g.id_grupo, g.id_grupo ||'-'|| g.descgrupo as descgrupo, g.auditivo, g.equipe, g.insercao_pac_institut  "
                + " from hosp.grupo g left join hosp.grupo_programa gp on (g.id_grupo = gp.codgrupo) left join hosp.programa p on (gp.codprograma = p.id_programa)"
                + " where g.cod_unidade = ? and upper(g.id_grupo ||'-'|| g.descgrupo) LIKE ? order by descgrupo ";

        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setInt(1, user_session.getUnidade().getId());
            stm.setString(2, "%" + descricao.toUpperCase() + "%");
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                GrupoBean grupo = new GrupoBean();
                grupo.setIdGrupo(rs.getInt("id_grupo"));
                grupo.setDescGrupo(rs.getString("descgrupo"));
                grupo.setAuditivo(rs.getBoolean("auditivo"));
                grupo.setEquipeSim(rs.getBoolean("equipe"));
                grupo.setinsercao_pac_institut(rs
                        .getBoolean("insercao_pac_institut"));
                lista.add(grupo);
            }
        } catch (SQLException sqle) {
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
            throw new ProjetoException(ex, this.getClass().getName());
        } finally {
            try {
                con.close();
            } catch (Exception ex) {
                //comentado walter erro log ex.printStackTrace();
            }
        }
        return lista;
    }


    public GrupoBean listarGrupoPorId(int id) throws ProjetoException {

        GrupoBean grupo = new GrupoBean();
        String sql = "select id_grupo, descgrupo, auditivo, insercao_pac_institut from hosp.grupo where id_grupo = ?";
        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setInt(1, id);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                grupo.setIdGrupo(rs.getInt("id_grupo"));
                grupo.setDescGrupo(rs.getString("descgrupo"));
                grupo.setAuditivo(rs.getBoolean("auditivo"));
                grupo.setinsercao_pac_institut(rs.getBoolean("insercao_pac_institut"));
                grupo.setEquipes(listarEquipesDoGrupo(rs.getInt("id_grupo"), con));
            }

        } catch (SQLException sqle) {
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
            throw new ProjetoException(ex, this.getClass().getName());
        } finally {
            try {
                con.close();
            } catch (Exception ex) {
                //comentado walter erro log ex.printStackTrace();
            }
        }
        return grupo;
    }

    public GrupoBean listarGrupoPorIdParaConverter(int id) throws ProjetoException {

        GrupoBean grupo = new GrupoBean();
        String sql = "select id_grupo, descgrupo, auditivo, insercao_pac_institut from hosp.grupo where id_grupo = ?";
        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setInt(1, id);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                grupo.setIdGrupo(rs.getInt("id_grupo"));
                grupo.setDescGrupo(rs.getString("descgrupo"));
                grupo.setAuditivo(rs.getBoolean("auditivo"));
                grupo.setinsercao_pac_institut(rs.getBoolean("insercao_pac_institut"));
            }

        } catch (SQLException sqle) {
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
            throw new ProjetoException(ex, this.getClass().getName());
        } finally {
            try {
                con.close();
            } catch (Exception ex) {
                //comentado walter erro log ex.printStackTrace();
            }
        }
        return grupo;
    }

    public GrupoBean listarGrupoPorIdComConexao(int id, Connection conAuxiliar) throws ProjetoException, SQLException {

        GrupoBean grupo = new GrupoBean();
        String sql = "select id_grupo, descgrupo, auditivo, insercao_pac_institut from hosp.grupo where id_grupo = ?";
        try {
            PreparedStatement stm = conAuxiliar.prepareStatement(sql);
            stm.setInt(1, id);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                grupo.setIdGrupo(rs.getInt("id_grupo"));
                grupo.setDescGrupo(rs.getString("descgrupo"));
                grupo.setAuditivo(rs.getBoolean("auditivo"));
            }

        } catch (SQLException sqle) {
            conAuxiliar.rollback();
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
            conAuxiliar.rollback();
            throw new ProjetoException(ex, this.getClass().getName());
        }
        return grupo;
    }

    public List<GrupoBean> listarGruposAutoComplete2(String descricao)
            throws ProjetoException {
        List<GrupoBean> lista = new ArrayList<>();

        String sql = "select id_grupo, descgrupo, auditivo, insercao_pac_institut from hosp.grupo  "
                + "where descgrupo LIKE ? and cod_unidade = ? order by descgrupo";

        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setString(1, "%" + descricao.toUpperCase() + "%");
            stm.setInt(2, user_session.getUnidade().getId());
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                GrupoBean grupo = new GrupoBean();
                grupo.setIdGrupo(rs.getInt("id_grupo"));
                grupo.setDescGrupo(rs.getString("descgrupo"));
                grupo.setAuditivo(rs.getBoolean("auditivo"));
                grupo.setinsercao_pac_institut(rs
                        .getBoolean("insercao_pac_institut"));
                lista.add(grupo);
            }
        } catch (SQLException sqle) {
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
            throw new ProjetoException(ex, this.getClass().getName());
        } finally {
            try {
                con.close();
            } catch (Exception ex) {
                //comentado walter erro log ex.printStackTrace();
            }
        }
        return lista;
    }


    public Integer buscarFrequencia(Integer idPrograma, Integer idGrupo)
            throws ProjetoException {

        Integer frequencia = 0;

        String sql = "SELECT qtdfrequencia FROM hosp.grupo_programa where codprograma = ? and codgrupo = ?;";

        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setInt(1, idPrograma);
            stm.setInt(2, idGrupo);
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                frequencia = rs.getInt("qtdfrequencia");
            }

        } catch (SQLException sqle) {
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
            throw new ProjetoException(ex, this.getClass().getName());
        } finally {
            try {
                con.close();
            } catch (Exception ex) {
                //comentado walter erro log ex.printStackTrace();
            }
        }
        return frequencia;
    }

    public List<BuscaGrupoFrequenciaDTO> buscaGruposComFrequecia(int codPrograma, Connection conAuxiliar)
            throws ProjetoException, SQLException {
        List<BuscaGrupoFrequenciaDTO> lista = new ArrayList<>();
        String sql = "select distinct g.id_grupo, g.descgrupo, g.auditivo, g.equipe, g.insercao_pac_institut, " + 
        		"gp.id_procedimento_padrao, gp.qtdfrequencia, pr.id id_procedimento, pr.nome procedimento " + 
        		"from hosp.grupo g  " + 
        		"left join hosp.grupo_programa gp on (g.id_grupo = gp.codgrupo) " + 
        		"left join hosp.programa p on (gp.codprograma = p.id_programa)  " + 
        		"left join hosp.proc pr on (gp.id_procedimento_padrao = pr.id ) " + 
        		"where p.id_programa = ?";

        try {
            PreparedStatement stm = conAuxiliar.prepareStatement(sql);
            stm.setInt(1, codPrograma);
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                BuscaGrupoFrequenciaDTO grupoDTO = new BuscaGrupoFrequenciaDTO();
                
                grupoDTO.getGrupo().setIdGrupo(rs.getInt("id_grupo"));
                grupoDTO.getGrupo().setDescGrupo(rs.getString("descgrupo"));
                grupoDTO.getGrupo().setAuditivo(rs.getBoolean("auditivo"));
                grupoDTO.getGrupo().setEquipeSim(rs.getBoolean("equipe"));
                grupoDTO.getGrupo().setinsercao_pac_institut(rs.getBoolean("insercao_pac_institut"));
                grupoDTO.getProcedimentoPadao().setIdProc(rs.getInt("id_procedimento"));
                grupoDTO.getProcedimentoPadao().setNomeProc(rs.getString("procedimento"));
                grupoDTO.setFrequencia(rs.getInt("qtdfrequencia"));
                lista.add(grupoDTO);
            }

        } catch (SQLException sqle) {
            conAuxiliar.rollback();
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
            conAuxiliar.rollback();
            throw new ProjetoException(ex, this.getClass().getName());
        }
        return lista;
    }
    
    public ArrayList<GrupoProgramaUnidadeDTO> buscaProgramasPorUnidade(List<ProgramaBean> listaPrograma) throws ProjetoException {
        PreparedStatement ps = null;
        con = ConnectionFactory.getConnection();

        String sql = "select g.id_grupo, g.descgrupo, p.id_programa, p.descprograma, u.id id_unidade, u.nome unidade " + 
        		"from hosp.grupo g " + 
        		"join hosp.grupo_programa gp on g.id_grupo = gp.codgrupo " + 
        		"join hosp.programa p on gp.codprograma = p.id_programa " + 
        		"join hosp.unidade u on g.cod_unidade = u.id " + 
        		"where (p.id_programa = ? and u.id = ?)";
        
        String filtroUnidade = " or (p.id_programa = ? and u.id = ?) ";
        String ordenacao = " order by g.descgrupo;";
        
        for (int i = 1; i < listaPrograma.size(); i++) {
			sql += filtroUnidade;
		}
        sql += ordenacao;
        
        ArrayList<GrupoProgramaUnidadeDTO> lista = new ArrayList<>();
        try {
            ps = con.prepareStatement(sql);
            ps.setInt(1, listaPrograma.get(0).getIdPrograma());
            ps.setInt(2, listaPrograma.get(0).getCodUnidade());
            int contador = 3;
            
            for (int i = 1; i < listaPrograma.size(); i++) {
    			ps.setInt(contador, listaPrograma.get(i).getIdPrograma());
    			contador++;
    			ps.setInt(contador, listaPrograma.get(i).getCodUnidade());
    			contador++;
    		}

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                GrupoProgramaUnidadeDTO grupoProgramaUnidadeDTO = new GrupoProgramaUnidadeDTO();
                grupoProgramaUnidadeDTO.getGrupo().setIdGrupo(rs.getInt("id_grupo"));
                grupoProgramaUnidadeDTO.getGrupo().setDescGrupo(rs.getString("descgrupo"));
                grupoProgramaUnidadeDTO.getPrograma().setIdPrograma(rs.getInt("id_programa"));
                grupoProgramaUnidadeDTO.getPrograma().setDescPrograma(rs.getString("descprograma"));
                grupoProgramaUnidadeDTO.getUnidade().setId(rs.getInt("id_unidade"));
                grupoProgramaUnidadeDTO.getUnidade().setNomeUnidade(rs.getString("unidade"));
                lista.add(grupoProgramaUnidadeDTO);
            }
        } catch (SQLException sqle) {
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
            throw new ProjetoException(ex, this.getClass().getName());
        } finally {
            try {
                con.close();
            } catch (Exception ex) {
                //comentado walter erro log ex.printStackTrace();
            }
        }
        return lista;
    }

}
