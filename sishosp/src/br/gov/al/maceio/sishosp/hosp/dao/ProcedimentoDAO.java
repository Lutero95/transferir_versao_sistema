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
import br.gov.al.maceio.sishosp.hosp.model.CboBean;
import br.gov.al.maceio.sishosp.hosp.model.CidBean;
import br.gov.al.maceio.sishosp.hosp.model.EquipeBean;
import br.gov.al.maceio.sishosp.hosp.model.ProcedimentoBean;

import javax.faces.context.FacesContext;

public class ProcedimentoDAO {
    Connection con = null;
    PreparedStatement ps = null;

    FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
            .getSessionMap().get("obj_funcionario");

    public boolean gravarProcedimento(ProcedimentoBean proc) {

        Boolean retorno = false;

        String sql = "INSERT INTO hosp.proc (codproc, nome, auditivo, tipo_exame_auditivo, utiliza_equipamento, gera_laudo_digita, validade_laudo, "
                + "idade_minima, idade_maxima, qtd_maxima, prazo_minimo_nova_execucao, sexo, cod_empresa)"
                + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING id;";
        try {
            con = ConnectionFactory.getConnection();
            ps = con.prepareStatement(sql);

            ps.setInt(1, proc.getCodProc());
            ps.setString(2, proc.getNomeProc().toUpperCase());
            ps.setBoolean(3, proc.getAuditivo());
            ps.setString(4, proc.getTipoExameAuditivo().toUpperCase());
            ps.setBoolean(5, proc.getUtilizaEquipamento());
            if (proc.isGera_laudo_digita() == false) {
                ps.setNull(6, Types.BOOLEAN);
            } else {
                ps.setBoolean(6, proc.isGera_laudo_digita());
            }
            if (proc.isGera_laudo_digita() == false) {
                ps.setNull(7, Types.INTEGER);
            } else {
                ps.setInt(7, proc.getValidade_laudo());
            }
            ps.setInt(8, proc.getIdadeMinima());
            ps.setInt(9, proc.getIdadeMaxima());
            ps.setInt(10, proc.getQtdMaxima());
            ps.setInt(11, proc.getPrazoMinimoNovaExecucao());
            ps.setString(12, proc.getSexo());
            ps.setInt(13, user_session.getEmpresa().getCodEmpresa());

            ResultSet rs = ps.executeQuery();

            int idProc = 0;
            if (rs.next()) {
                idProc = rs.getInt("id");
            }

            sql = "INSERT INTO hosp.proc_cbo (id_proc, id_cbo) VALUES (?, ?);";
            ps = con.prepareStatement(sql);
            for (int i = 0; i < proc.getListaCbo().size(); i++) {
                ps.setInt(1, idProc);
                ps.setInt(2, proc.getListaCbo().get(i).getCodCbo());
                ps.execute();
            }

            sql = "INSERT INTO hosp.proc_cid (id_proc, id_cid) VALUES (?, ?);";
            ps = con.prepareStatement(sql);
            for (int i = 0; i < proc.getListaCid().size(); i++) {
                ps.setInt(1, idProc);
                ps.setInt(2, proc.getListaCid().get(i).getIdCid());
                ps.execute();
            }

            sql = "INSERT INTO hosp.proc_recurso (id_proc, id_recurso) VALUES (?, ?);";
            ps = con.prepareStatement(sql);
            for (int i = 0; i < proc.getListaRecurso().size(); i++) {
                ps.setInt(1, idProc);
                ps.setInt(2, proc.getListaRecurso().get(i).getIdRecurso());
                ps.execute();
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

    public boolean alterarProcedimento(ProcedimentoBean proc) {

        Boolean retorno = false;

        String sql = "update hosp.proc set nome = ?, auditivo = ?, tipo_exame_auditivo = ?, utiliza_equipamento = ?, "
                + "gera_laudo_digita = ?, validade_laudo = ?, codproc = ?, idade_minima = ?, idade_maxima = ?, qtd_maxima = ?, "
                + "prazo_minimo_nova_execucao = ?, sexo = ? "
                + "where id = ?";
        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, proc.getNomeProc().toUpperCase());
            stmt.setBoolean(2, proc.getAuditivo());
            stmt.setString(3, proc.getTipoExameAuditivo().toUpperCase());
            stmt.setBoolean(4, proc.getUtilizaEquipamento());
            if (proc.isGera_laudo_digita() == false) {
                stmt.setNull(5, Types.BOOLEAN);
            } else {
                stmt.setBoolean(5, proc.isGera_laudo_digita());
            }
            if (proc.isGera_laudo_digita() == false) {
                stmt.setNull(6, Types.INTEGER);
            } else {
                stmt.setInt(6, proc.getValidade_laudo());
            }
            stmt.setInt(7, proc.getCodProc());
            stmt.setInt(8, proc.getIdadeMinima());
            stmt.setInt(9, proc.getIdadeMaxima());
            stmt.setInt(10, proc.getQtdMaxima());
            stmt.setInt(11, proc.getPrazoMinimoNovaExecucao());
            stmt.setString(12, proc.getSexo());
            stmt.setInt(13, proc.getIdProc());

            stmt.executeUpdate();

            sql = "delete from hosp.proc_cbo where id_proc = ?";
            stmt = con.prepareStatement(sql);
            stmt.setLong(1, proc.getIdProc());
            stmt.execute();

            sql = "delete from hosp.proc_cid where id_proc = ?";
            stmt = con.prepareStatement(sql);
            stmt.setLong(1, proc.getIdProc());
            stmt.execute();

            sql = "delete from hosp.proc_recurso where id_proc = ?";
            stmt = con.prepareStatement(sql);
            stmt.setLong(1, proc.getIdProc());
            stmt.execute();

            sql = "INSERT INTO hosp.proc_cbo (id_proc, id_cbo) VALUES (?, ?);";
            ps = con.prepareStatement(sql);
            for (int i = 0; i < proc.getListaCbo().size(); i++) {
                ps.setInt(1, proc.getIdProc());
                ps.setInt(2, proc.getListaCbo().get(i).getCodCbo());
                ps.execute();
            }

            sql = "INSERT INTO hosp.proc_cid (id_proc, id_cid) VALUES (?, ?);";
            ps = con.prepareStatement(sql);
            for (int i = 0; i < proc.getListaCid().size(); i++) {
                ps.setInt(1, proc.getIdProc());
                ps.setInt(2, proc.getListaCid().get(i).getIdCid());
                ps.execute();
            }

            sql = "INSERT INTO hosp.proc_recurso (id_proc, id_recurso) VALUES (?, ?);";
            ps = con.prepareStatement(sql);
            for (int i = 0; i < proc.getListaRecurso().size(); i++) {
                ps.setInt(1, proc.getIdProc());
                ps.setInt(2, proc.getListaRecurso().get(i).getIdRecurso());
                ps.execute();
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

    public boolean excluirProcedimento(ProcedimentoBean proc){

        Boolean retorno = false;

        try {
            con = ConnectionFactory.getConnection();

            String sql = "delete from hosp.proc_cbo where id_proc = ?";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setLong(1, proc.getIdProc());
            stmt.execute();

            sql = "delete from hosp.proc_recurso where id_proc = ?";
            stmt = con.prepareStatement(sql);
            stmt.setLong(1, proc.getIdProc());
            stmt.execute();

            sql = "delete from hosp.proc_cid where id_proc = ?";
            stmt = con.prepareStatement(sql);
            stmt.setLong(1, proc.getIdProc());
            stmt.execute();

            sql = "delete from hosp.proc where id = ?";
            stmt = con.prepareStatement(sql);
            stmt.setLong(1, proc.getIdProc());
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

    public List<ProcedimentoBean> listarProcedimento() throws ProjetoException {
        List<ProcedimentoBean> lista = new ArrayList<>();
        String sql = "select id, codproc, nome, auditivo, tipo_exame_auditivo, utiliza_equipamento, gera_laudo_digita, validade_laudo, "
                + " idade_minima, idade_maxima, qtd_maxima, prazo_minimo_nova_execucao, sexo "
                + " from hosp.proc where cod_empresa = ? order by nome";

        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setInt(1, user_session.getEmpresa().getCodEmpresa());
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                ProcedimentoBean proc = new ProcedimentoBean();
                proc.setIdProc(rs.getInt("id"));
                proc.setCodProc(rs.getInt("codproc"));
                proc.setNomeProc(rs.getString("nome"));
                proc.setAuditivo(rs.getBoolean("auditivo"));
                proc.setTipoExameAuditivo(rs.getString("tipo_exame_auditivo"));
                proc.setUtilizaEquipamento(rs.getBoolean("utiliza_equipamento"));
                proc.setGera_laudo_digita(rs.getBoolean("gera_laudo_digita"));
                proc.setValidade_laudo(rs.getInt("validade_laudo"));
                proc.setIdadeMinima(rs.getInt("idade_minima"));
                proc.setIdadeMaxima(rs.getInt("idade_maxima"));
                proc.setQtdMaxima(rs.getInt("qtd_maxima"));
                proc.setPrazoMinimoNovaExecucao(rs.getInt("prazo_minimo_nova_execucao"));
                proc.setSexo(rs.getString("sexo"));

                lista.add(proc);
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
    
    public List<ProcedimentoBean> listarProcedimentoLaudo() throws ProjetoException {
        List<ProcedimentoBean> lista = new ArrayList<>();
        String sql = "select id, codproc, nome, auditivo, tipo_exame_auditivo, utiliza_equipamento, gera_laudo_digita, validade_laudo, "
                + " idade_minima, idade_maxima, qtd_maxima, prazo_minimo_nova_execucao, sexo "
                + " from hosp.proc where cod_empresa = ? and gera_laudo_digita is true order by nome";

        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setInt(1, user_session.getEmpresa().getCodEmpresa());
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                ProcedimentoBean proc = new ProcedimentoBean();
                proc.setIdProc(rs.getInt("id"));
                proc.setCodProc(rs.getInt("codproc"));
                proc.setNomeProc(rs.getString("nome"));
                proc.setAuditivo(rs.getBoolean("auditivo"));
                proc.setTipoExameAuditivo(rs.getString("tipo_exame_auditivo"));
                proc.setUtilizaEquipamento(rs.getBoolean("utiliza_equipamento"));
                proc.setGera_laudo_digita(rs.getBoolean("gera_laudo_digita"));
                proc.setValidade_laudo(rs.getInt("validade_laudo"));
                proc.setIdadeMinima(rs.getInt("idade_minima"));
                proc.setIdadeMaxima(rs.getInt("idade_maxima"));
                proc.setQtdMaxima(rs.getInt("qtd_maxima"));
                proc.setPrazoMinimoNovaExecucao(rs.getInt("prazo_minimo_nova_execucao"));
                proc.setSexo(rs.getString("sexo"));

                lista.add(proc);
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

    public ProcedimentoBean listarProcedimentoPorId(int id)
            throws ProjetoException {
        ProcedimentoBean proc = new ProcedimentoBean();
        String sql = "select id, codproc, nome, auditivo, tipo_exame_auditivo, utiliza_equipamento, gera_laudo_digita, validade_laudo,"
                + " idade_minima, idade_maxima, qtd_maxima, prazo_minimo_nova_execucao, sexo "
                + "from hosp.proc where id = ? order by nome";
        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setInt(1, id);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                proc = new ProcedimentoBean();
                proc.setIdProc(rs.getInt("id"));
                proc.setCodProc(rs.getInt("codproc"));
                proc.setNomeProc(rs.getString("nome"));
                proc.setAuditivo(rs.getBoolean("auditivo"));
                proc.setTipoExameAuditivo(rs.getString("tipo_exame_auditivo"));
                proc.setUtilizaEquipamento(rs.getBoolean("utiliza_equipamento"));
                proc.setGera_laudo_digita(rs.getBoolean("gera_laudo_digita"));
                proc.setValidade_laudo(rs.getInt("validade_laudo"));
                proc.setIdadeMinima(rs.getInt("idade_minima"));
                proc.setIdadeMaxima(rs.getInt("idade_maxima"));
                proc.setQtdMaxima(rs.getInt("qtd_maxima"));
                proc.setPrazoMinimoNovaExecucao(rs.getInt("prazo_minimo_nova_execucao"));
                proc.setSexo(rs.getString("sexo"));
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
        return proc;
    }


    public List<ProcedimentoBean> listarProcedimentoBusca(
            String descricaoBusca, Integer tipoBuscar) throws ProjetoException {

        List<ProcedimentoBean> lista = new ArrayList<>();
        String sql = "select id,codproc  ||' - '|| nome as nome ,codproc, auditivo, tipo_exame_auditivo, utiliza_equipamento, gera_laudo_digita, validade_laudo,"
                + "idade_minima, idade_maxima, qtd_maxima, prazo_minimo_nova_execucao, sexo  "
                + "from hosp.proc ";
        if (tipoBuscar == 1) {
            sql += " where upper(codproc ||' - '|| nome) LIKE ? and cod_empresa = ? order by nome";
        }
        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setString(1, "%" + descricaoBusca.toUpperCase() + "%");
            stm.setInt(2, user_session.getEmpresa().getCodEmpresa());
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                ProcedimentoBean proc = new ProcedimentoBean();
                proc.setIdProc(rs.getInt("id"));
                proc.setNomeProc(rs.getString("nome"));
                proc.setCodProc(rs.getInt("codproc"));
                proc.setAuditivo(rs.getBoolean("auditivo"));
                proc.setTipoExameAuditivo(rs.getString("tipo_exame_auditivo"));
                proc.setUtilizaEquipamento(rs.getBoolean("utiliza_equipamento"));
                proc.setGera_laudo_digita(rs.getBoolean("gera_laudo_digita"));
                proc.setValidade_laudo(rs.getInt("validade_laudo"));
                proc.setIdadeMinima(rs.getInt("idade_minima"));
                proc.setIdadeMaxima(rs.getInt("idade_maxima"));
                proc.setQtdMaxima(rs.getInt("qtd_maxima"));
                proc.setPrazoMinimoNovaExecucao(rs.getInt("prazo_minimo_nova_execucao"));
                proc.setSexo(rs.getString("sexo"));

                lista.add(proc);
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
    
    public List<ProcedimentoBean> listarProcedimentoQueGeramLaudoBusca(
            String descricaoBusca, Integer tipoBuscar) throws ProjetoException {

        List<ProcedimentoBean> lista = new ArrayList<>();
        String sql = "select id,codproc  ||' - '|| nome as nome ,codproc, auditivo, tipo_exame_auditivo, utiliza_equipamento, gera_laudo_digita, validade_laudo,"
                + "idade_minima, idade_maxima, qtd_maxima, prazo_minimo_nova_execucao, sexo  "
                + "from hosp.proc ";
        if (tipoBuscar == 1) {
            sql += " where upper(codproc ||' - '|| nome) LIKE ? and cod_empresa = ? and gera_laudo_digita is true order by nome";
        }
        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setString(1, "%" + descricaoBusca.toUpperCase() + "%");
            stm.setInt(2, user_session.getEmpresa().getCodEmpresa());
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                ProcedimentoBean proc = new ProcedimentoBean();
                proc.setIdProc(rs.getInt("id"));
                proc.setNomeProc(rs.getString("nome"));
                proc.setCodProc(rs.getInt("codproc"));
                proc.setAuditivo(rs.getBoolean("auditivo"));
                proc.setTipoExameAuditivo(rs.getString("tipo_exame_auditivo"));
                proc.setUtilizaEquipamento(rs.getBoolean("utiliza_equipamento"));
                proc.setGera_laudo_digita(rs.getBoolean("gera_laudo_digita"));
                proc.setValidade_laudo(rs.getInt("validade_laudo"));
                proc.setIdadeMinima(rs.getInt("idade_minima"));
                proc.setIdadeMaxima(rs.getInt("idade_maxima"));
                proc.setQtdMaxima(rs.getInt("qtd_maxima"));
                proc.setPrazoMinimoNovaExecucao(rs.getInt("prazo_minimo_nova_execucao"));
                proc.setSexo(rs.getString("sexo"));

                lista.add(proc);
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

    public ArrayList<CidBean> listarCid(Integer id_proc) throws ProjetoException {

        ArrayList<CidBean> lista = new ArrayList<>();
        String sql = "select c.cod, c.cid, c.desccid from hosp.cid c left join hosp.proc_cid p on (c.cod = p.id_cid) where id_proc = ? order by cid";

        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setInt(1, id_proc);
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                CidBean cid = new CidBean();

                cid.setIdCid(rs.getInt("cod"));
                cid.setDescCid(rs.getString("desccid"));
                cid.setCid(rs.getString("cid"));

                lista.add(cid);
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

    public ArrayList<CboBean> listarCbo(Integer id_proc) throws ProjetoException {

        ArrayList<CboBean> lista = new ArrayList<>();
        String sql = "select c.id, c.descricao from hosp.cbo c left join hosp.proc_cbo p on (c.id = p.id_cbo) where id_proc = ? order by descricao";

        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setInt(1, id_proc);
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                CboBean cbo = new CboBean();

                cbo.setCodCbo(rs.getInt("id"));
                cbo.setDescCbo(rs.getString("descricao"));

                lista.add(cbo);
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

    public ProcedimentoBean listarProcedimentoPorIdComConexao(int id, Connection conAuxiliar)
            throws ProjetoException {
        ProcedimentoBean proc = new ProcedimentoBean();
        String sql = "select id, codproc, nome, auditivo, tipo_exame_auditivo, utiliza_equipamento, gera_laudo_digita, validade_laudo,"
                + " idade_minima, idade_maxima, qtd_maxima, prazo_minimo_nova_execucao, sexo "
                + "from hosp.proc where id = ? order by nome";
        try {
            PreparedStatement stm = conAuxiliar.prepareStatement(sql);
            stm.setInt(1, id);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                proc = new ProcedimentoBean();
                proc.setIdProc(rs.getInt("id"));
                proc.setCodProc(rs.getInt("codproc"));
                proc.setNomeProc(rs.getString("nome"));
                proc.setAuditivo(rs.getBoolean("auditivo"));
                proc.setTipoExameAuditivo(rs.getString("tipo_exame_auditivo"));
                proc.setUtilizaEquipamento(rs.getBoolean("utiliza_equipamento"));
                proc.setGera_laudo_digita(rs.getBoolean("gera_laudo_digita"));
                proc.setValidade_laudo(rs.getInt("validade_laudo"));
                proc.setIdadeMinima(rs.getInt("idade_minima"));
                proc.setIdadeMaxima(rs.getInt("idade_maxima"));
                proc.setQtdMaxima(rs.getInt("qtd_maxima"));
                proc.setPrazoMinimoNovaExecucao(rs.getInt("prazo_minimo_nova_execucao"));
                proc.setSexo(rs.getString("sexo"));
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
        return proc;
    }

}
