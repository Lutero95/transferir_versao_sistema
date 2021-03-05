package br.gov.al.maceio.sishosp.hosp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.gov.al.maceio.sishosp.acl.dao.FuncionarioDAO;
import br.gov.al.maceio.sishosp.acl.model.FuncionarioBean;
import br.gov.al.maceio.sishosp.administrativo.model.RemocaoProfissionalEquipe;
import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;
import br.gov.al.maceio.sishosp.comum.util.DataUtil;
import br.gov.al.maceio.sishosp.comum.util.TratamentoErrosUtil;
import br.gov.al.maceio.sishosp.hosp.model.EquipeBean;
import br.gov.al.maceio.sishosp.hosp.model.ProgramaBean;
import br.gov.al.maceio.sishosp.hosp.model.dto.EquipeGrupoProgramaUnidadeDTO;
import br.gov.al.maceio.sishosp.hosp.model.dto.GrupoProgramaUnidadeDTO;
import br.gov.al.maceio.sishosp.hosp.model.GrupoBean;
import br.gov.al.maceio.sishosp.hosp.model.dto.SubstituicaoProfissionalEquipeDTO;

import javax.faces.context.FacesContext;

public class EquipeDAO {

    Connection con = null;
    PreparedStatement ps = null;
    FuncionarioDAO pDao = new FuncionarioDAO();

    FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
            .getSessionMap().get("obj_usuario");

    public boolean gravarEquipe(EquipeBean equipe) throws ProjetoException {

        FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
                .getSessionMap().get("obj_funcionario");

        Boolean retorno = false;
        String sql = "insert into hosp.equipe (descequipe, cod_unidade, realiza_avaliacao, turno, ativo) values (?, ?, ?, ?, true) RETURNING id_equipe;";

        try {
            con = ConnectionFactory.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, equipe.getDescEquipe().toUpperCase());
            ps.setInt(2, user_session.getUnidade().getId());
            ps.setBoolean(3, equipe.getRealizaAvaliacao());
            ps.setString(4, equipe.getTurno().toUpperCase());
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                retorno = insereEquipeProfissional(rs.getInt("id_equipe"), equipe);
            }

            if (retorno) {
                con.commit();
            }

        } catch (SQLException sqle) {
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
            throw new ProjetoException(ex, this.getClass().getName());
        } finally {
            try {
                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return retorno;
    }

    private Boolean insereEquipeProfissional(int idEquipe, EquipeBean equipe) throws ProjetoException, SQLException {
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

        } catch (SQLException sqle) {
            con.rollback();
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
            con.rollback();
            throw new ProjetoException(ex, this.getClass().getName());
        }
        return retorno;
    }

    public List<EquipeBean> listarEquipe() throws ProjetoException {
        List<EquipeBean> lista = new ArrayList<>();
        String sql = "select id_equipe, descequipe, cod_unidade, realiza_avaliacao, turno, ativo from hosp.equipe where cod_unidade = ? order by descequipe";

        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setInt(1, user_session.getUnidade().getId());
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                EquipeBean equipe = new EquipeBean();
                equipe.setCodEquipe(rs.getInt("id_equipe"));
                equipe.setDescEquipe(rs.getString("descequipe"));
                equipe.setCodUnidade(rs.getInt("cod_unidade"));
                equipe.setRealizaAvaliacao(rs.getBoolean("realiza_avaliacao"));
                equipe.setTurno(rs.getString("turno"));
                equipe.setAtivo(rs.getBoolean("ativo"));
                lista.add(equipe);
            }
        } catch (SQLException sqle) {
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
            throw new ProjetoException(ex, this.getClass().getName());
        } finally {
            try {
                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return lista;
    }

    public List<EquipeBean> listarEquipeBusca(String descricao) throws ProjetoException {
        List<EquipeBean> lista = new ArrayList<>();
        String sql = "select id_equipe,id_equipe ||'-'|| descequipe as descequipe, cod_unidade, turno from hosp.equipe "
                + "where upper(id_equipe ||'-'|| descequipe) LIKE ? and cod_unidade = ? and ativo = true order by descequipe";

        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setString(1, "%" + descricao.toUpperCase() + "%");
            stm.setInt(2, user_session.getUnidade().getId());
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                EquipeBean equipe = new EquipeBean();
                equipe.setCodEquipe(rs.getInt("id_equipe"));
                equipe.setDescEquipe(rs.getString("descequipe"));
                equipe.setCodUnidade(rs.getInt("cod_unidade"));
                equipe.setTurno(rs.getString("turno"));
                lista.add(equipe);
            }
        } catch (SQLException sqle) {
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
            throw new ProjetoException(ex, this.getClass().getName());
        } finally {
            try {
                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return lista;
    }

    public List<EquipeBean> listarEquipePorGrupoAutoComplete(String descricao, Integer codgrupo)
            throws ProjetoException {
        List<EquipeBean> lista = new ArrayList<>();
        String sql = "select distinct e.id_equipe, e.id_equipe ||'-'|| e.descequipe as descequipe, e.turno from hosp.equipe e "
                + " left join hosp.equipe_grupo eg on (e.id_equipe = eg.codequipe) where e.ativo = true and eg.id_grupo = ? and descequipe like ? order by descequipe ";

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
                equipe.setTurno(rs.getString("turno"));
                lista.add(equipe);
            }
        } catch (SQLException sqle) {
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
            throw new ProjetoException(ex, this.getClass().getName());
        } finally {
            try {
                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return lista;
    }

    public List<EquipeBean> listarEquipeAvaliacaoPorProgramaAutoComplete(String descricao, Integer codPrograma)
            throws ProjetoException {
        List<EquipeBean> lista = new ArrayList<>();
        String sql = "select distinct e.id_equipe, e.id_equipe ||'-'|| e.descequipe as descequipe, e.turno "
                + "from hosp.equipe e " + "left join hosp.equipe_grupo eg on (e.id_equipe = eg.codequipe) "
                + "LEFT JOIN hosp.grupo_programa gp ON (gp.codgrupo = eg.id_grupo)"
                + "where e.ativo = true and gp.codprograma = ? and descequipe like ? and realiza_avaliacao is true order by descequipe ";

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
                equipe.setTurno(rs.getString("turno"));

                lista.add(equipe);
            }
        } catch (SQLException sqle) {
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
            throw new ProjetoException(ex, this.getClass().getName());
        } finally {
            try {
                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return lista;
    }

    public List<EquipeBean> listarEquipeAvaliacaoPorPrograma(Integer codPrograma)
            throws ProjetoException {
        List<EquipeBean> lista = new ArrayList<>();
        String sql = "select distinct e.id_equipe, e.id_equipe ||'-'|| e.descequipe as descequipe, e.turno "
                + "from hosp.equipe e " + "left join hosp.equipe_grupo eg on (e.id_equipe = eg.codequipe) "
                + "LEFT JOIN hosp.grupo_programa gp ON (gp.codgrupo = eg.id_grupo)"
                + "where e.ativo = true and gp.codprograma = ?  and realiza_avaliacao is true order by descequipe ";

        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setInt(1, codPrograma);
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                EquipeBean equipe = new EquipeBean();
                equipe.setCodEquipe(rs.getInt("id_equipe"));
                equipe.setDescEquipe(rs.getString("descequipe"));
                equipe.setTurno(rs.getString("turno"));

                lista.add(equipe);
            }
        } catch (SQLException sqle) {
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
            throw new ProjetoException(ex, this.getClass().getName());
        } finally {
            try {
                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return lista;
    }

    public List<EquipeBean> listarEquipePorGrupo(Integer codgrupo) throws ProjetoException {
        List<EquipeBean> lista = new ArrayList<>();
        String sql = "select distinct e.id_equipe, e.id_equipe ||'-'|| e.descequipe as descequipe, e.turno from hosp.equipe e "
                + " left join hosp.equipe_grupo eg on (e.id_equipe = eg.codequipe) where e.ativo = true and eg.id_grupo = ? order by descequipe ";

        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setInt(1, codgrupo);
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                EquipeBean equipe = new EquipeBean();
                equipe.setCodEquipe(rs.getInt("id_equipe"));
                equipe.setDescEquipe(rs.getString("descequipe"));
                equipe.setTurno(rs.getString("turno"));

                lista.add(equipe);
            }
        } catch (SQLException sqle) {
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
            throw new ProjetoException(ex, this.getClass().getName());
        } finally {
            try {
                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return lista;
    }

    public Boolean alterarEquipe(EquipeBean equipe) throws ProjetoException {
        Boolean retorno = false;
        String sql = "update hosp.equipe set descequipe = ?, realiza_avaliacao=?, turno=?, ativo = ? where id_equipe = ?";
        ps = null;
        try {
            con = ConnectionFactory.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, equipe.getDescEquipe().toUpperCase());
            ps.setBoolean(2, equipe.getRealizaAvaliacao());
            ps.setString(3, equipe.getTurno().toUpperCase());
            ps.setBoolean(4, equipe.isAtivo());
            ps.setInt(5, equipe.getCodEquipe());
            ps.executeUpdate();

            retorno = excluirTabEquipeProf(equipe.getCodEquipe());

            if (retorno) {
                retorno = insereEquipeProfissional(equipe.getCodEquipe(), equipe);
            }

            if (retorno) {
                con.commit();
            }

        } catch (SQLException sqle) {
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
            throw new ProjetoException(ex, this.getClass().getName());
        } finally {
            try {
                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return retorno;
    }

    private Boolean excluirTabEquipeProf(int id) throws ProjetoException, SQLException {
        Boolean retorno = false;
        String sql = "delete from hosp.equipe_medico where equipe = ?";

        try {
            ps = con.prepareStatement(sql);
            ps.setLong(1, id);
            ps.execute();
            retorno = true;
        }  catch (SQLException sqle) {
            con.rollback();
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
            con.rollback();
            throw new ProjetoException(ex, this.getClass().getName());
        }
        return retorno;
    }

    public Boolean excluirEquipe(EquipeBean equipe) throws ProjetoException, SQLException {
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

        } catch (SQLException sqle) {
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
            throw new ProjetoException(ex, this.getClass().getName());
        } finally {
            try {
                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return retorno;
    }

    public EquipeBean buscarEquipePorID(Integer id) throws ProjetoException, SQLException {
        EquipeBean equipe = null;

        String sql = "select id_equipe, descequipe, cod_unidade, realiza_avaliacao, turno, ativo from hosp.equipe where id_equipe = ?";

        try {
            con = ConnectionFactory.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                equipe = new EquipeBean();
                equipe.setCodEquipe(rs.getInt("id_equipe"));
                equipe.setDescEquipe(rs.getString("descequipe"));
                equipe.setProfissionais(pDao.listarProfissionaisPorEquipe(rs.getInt("id_equipe"), con));
                equipe.setCodUnidade(rs.getInt("cod_unidade"));
                equipe.setRealizaAvaliacao(rs.getBoolean("realiza_avaliacao"));
                equipe.setTurno(rs.getString("turno"));
                equipe.setAtivo(rs.getBoolean("ativo"));
            }

        } catch (SQLException sqle) {
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
            throw new ProjetoException(ex, this.getClass().getName());
        } finally {
            try {
                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return equipe;
    }

    public EquipeBean buscarEquipePorIDComConexao(Integer id, Connection conAuxiliar) throws ProjetoException, SQLException {
        EquipeBean equipe = null;

        String sql = "select id_equipe, descequipe, cod_unidade, realiza_avaliacao, turno, ativo from hosp.equipe where id_equipe = ? and ativo = true";

        try {
            ps = conAuxiliar.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                equipe = new EquipeBean();
                equipe.setCodEquipe(rs.getInt("id_equipe"));
                equipe.setDescEquipe(rs.getString("descequipe"));
                equipe.setCodUnidade(rs.getInt("cod_unidade"));
                equipe.setRealizaAvaliacao(rs.getBoolean("realiza_avaliacao"));
                equipe.setTurno(rs.getString("turno"));
                equipe.setAtivo(rs.getBoolean("ativo"));
            }

        } catch (SQLException sqle) {
            conAuxiliar.rollback();
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
            conAuxiliar.rollback();
            throw new ProjetoException(ex, this.getClass().getName());
        }
        return equipe;
    }

    public EquipeBean buscarEquipePorIDParaConverter(Integer id) throws ProjetoException {
        EquipeBean equipe = null;

        String sql = "select id_equipe, descequipe, cod_unidade, realiza_avaliacao, turno, ativo from hosp.equipe where id_equipe = ? and ativo = true";

        try {
            con = ConnectionFactory.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                equipe = new EquipeBean();
                equipe.setCodEquipe(rs.getInt("id_equipe"));
                equipe.setDescEquipe(rs.getString("descequipe"));
                equipe.setCodUnidade(rs.getInt("cod_unidade"));
                equipe.setRealizaAvaliacao(rs.getBoolean("realiza_avaliacao"));
                equipe.setTurno(rs.getString("turno"));
                equipe.setAtivo(rs.getBoolean("ativo"));
            }

        } catch (SQLException sqle) {
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
            throw new ProjetoException(ex, this.getClass().getName());
        } finally {
            try {
                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return equipe;
    }

    public ArrayList<FuncionarioBean> listarProfissionaisDaEquipe(Integer codequipe) throws ProjetoException {
        ArrayList<FuncionarioBean> lista = new ArrayList<>();
        String sql = "select e.medico, f.descfuncionario, f.codespecialidade, es.descespecialidade "
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

                lista.add(func);
            }
        } catch (SQLException sqle) {
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
            throw new ProjetoException(ex, this.getClass().getName());
        } finally {
            try {
                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return lista;
    }

    public ArrayList<FuncionarioBean> listarProfissionaisDaEquipeInsercao(Integer codequipe,
                                                                          Boolean todosOsProfissionais) throws ProjetoException {
        ArrayList<FuncionarioBean> lista = new ArrayList<>();

        String sql = "";

        if (todosOsProfissionais) {
            sql = "select distinct e.medico, f.descfuncionario, f.codespecialidade, es.descespecialidade, f.codprocedimentopadrao "
                    + " from hosp.equipe_medico e left join acl.funcionarios f on (e.medico = f.id_funcionario) "
                    + " left join hosp.especialidade es on (f.codespecialidade = es.id_especialidade) "
                    + " where codunidade = ? order by f.descfuncionario ";
        } else {
            sql = "select e.medico, f.descfuncionario, f.codespecialidade, es.descespecialidade, f.codprocedimentopadrao "
                    + " from hosp.equipe_medico e left join acl.funcionarios f on (e.medico = f.id_funcionario) "
                    + " left join hosp.especialidade es on (f.codespecialidade = es.id_especialidade) "
                    + " where equipe = ? order by f.descfuncionario ";
        }

        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);

            if (todosOsProfissionais) {
                stm.setInt(1, user_session.getUnidade().getId());
            } else {
                stm.setInt(1, codequipe);
            }
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                FuncionarioBean funcionario = new FuncionarioBean();
                funcionario.setId(rs.getLong("medico"));
                funcionario.setNome(rs.getString("descfuncionario"));
                funcionario.getEspecialidade().setCodEspecialidade(rs.getInt("codespecialidade"));
                funcionario.getEspecialidade().setDescEspecialidade(rs.getString("descespecialidade"));
                funcionario.getProc1().setIdProc(rs.getInt("codprocedimentopadrao"));
                funcionario.setListaCbos(new FuncionarioDAO().listarCbosProfissional(funcionario.getId(), con));

                lista.add(funcionario);
            }
        } catch (SQLException sqle) {
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
            throw new ProjetoException(ex, this.getClass().getName());
        } finally {
            try {
                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return lista;
    }

    public Boolean removerProfissionalEquipe(int codigoEquipe, Long codigoProfissional, Date dataSaida) throws ProjetoException {

        FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
                .getSessionMap().get("obj_funcionario");

        Boolean retorno = false;
        String sql = "insert into logs.remocao_profissional_equipe " +
                "(cod_equipe, cod_profissional, data_saida, usuario_acao, data_hora_acao) " +
                "values (?, ?, ?, ?, CURRENT_TIMESTAMP) RETURNING id;";

        try {
            con = ConnectionFactory.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, codigoEquipe);
            ps.setLong(2, codigoProfissional);
            ps.setDate(3, DataUtil.converterDateUtilParaDateSql(dataSaida));
            ps.setLong(4, user_session.getId());
            ResultSet rs = ps.executeQuery();

            Integer id = null;

            if (rs.next()) {
                id = rs.getInt("id");
            }

            if (excluirProfissionalDeEquipe(codigoEquipe, codigoProfissional, dataSaida, id, con)) {
                con.commit();
                retorno = true;
            }

        } catch (SQLException sqle) {
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
            throw new ProjetoException(ex, this.getClass().getName());
        } finally {
            try {
                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return retorno;
    }

    private Boolean excluirProfissionalDeEquipe
            (int codigoEquipe, Long codigoProfissional, Date dataSaida, int idRemocaoProfissionalEquipe, Connection conAuxiliar)
            throws ProjetoException, SQLException {

        Boolean retorno = false;

        String sql = "delete from hosp.equipe_medico where equipe = ? and medico = ?";

        try {
            ps = conAuxiliar.prepareStatement(sql);
            ps.setLong(1, codigoEquipe);
            ps.setLong(2, codigoProfissional);
            ps.execute();

            retorno = gravarLogRemocaoProfissionalEquipeAtendimentos1(listarAtendimentosAhSeremExcluidos(codigoProfissional, dataSaida, codigoEquipe, con),
                    codigoProfissional, idRemocaoProfissionalEquipe, codigoEquipe, con);

        } catch (SQLException sqle) {
            conAuxiliar.rollback();
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
            conAuxiliar.rollback();
            throw new ProjetoException(ex, this.getClass().getName());
        }
        return retorno;
    }

    private ArrayList<Integer> listarAtendimentosAhSeremExcluidos(Long codigoProfissional, Date dataSaida, int codigoEquipe, Connection conAuxiliar)
            throws ProjetoException, SQLException {

        ArrayList<Integer> lista = new ArrayList<>();

        String sql = "SELECT a1.id_atendimentos1  " +
                "FROM hosp.atendimentos1 a1 " +
                "JOIN hosp.atendimentos a ON (a1.id_atendimento = a.id_atendimento) " +
                "WHERE a1.codprofissionalatendimento = ? AND a.dtaatende >= ? AND a.codequipe = ?;";

        try {
            PreparedStatement stm = conAuxiliar.prepareStatement(sql);
            stm.setLong(1, codigoProfissional);
            stm.setDate(2, DataUtil.converterDateUtilParaDateSql(dataSaida));
            stm.setInt(3, codigoEquipe);
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                int idAtendimento1 = rs.getInt("id_atendimentos1");
                lista.add(idAtendimento1);
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

    private Boolean gravarLogRemocaoProfissionalEquipeAtendimentos1(
            List<Integer> listaAtendimentos1, Long codigoProfissional, int idRemocaoProfissionalEquipe, Integer codigoEquipe, Connection conAuxiliar)
            throws SQLException, ProjetoException {

        Boolean retorno = false;
        String sql = "insert into logs.remocao_profissional_equipe_atendimentos1 " +
                "(id_remocao_profissional_equipe, id_atendimentos1, id_funcionario) " +
                "values (?, ?, ?);";

        try {
            for (int i = 0; i < listaAtendimentos1.size(); i++) {
                ps = conAuxiliar.prepareStatement(sql);
                ps.setInt(1, idRemocaoProfissionalEquipe);
                ps.setLong(2, listaAtendimentos1.get(i));
                ps.setLong(3, codigoProfissional);
                ps.execute();
            }

            retorno = excluirAtendimentosProfissionalRemovidoEquipe(
                    listaAtendimentos1, codigoProfissional, idRemocaoProfissionalEquipe, codigoEquipe, con);

        } catch (SQLException sqle) {
            conAuxiliar.rollback();
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
            conAuxiliar.rollback();
            throw new ProjetoException(ex, this.getClass().getName());
        }
        return retorno;
    }

    private Boolean excluirAtendimentosProfissionalRemovidoEquipe(
            List<Integer> listaAtendimentos1, Long codigoProfissional, int idRemocaoProfissionalEquipe, Integer codigoEquipe, Connection conAuxiliar)
            throws ProjetoException, SQLException {

        Boolean retorno = false;

        String sql = "UPDATE hosp.atendimentos1 SET excluido = 'S', usuario_exclusao=?, data_hora_exclusao=current_timestamp WHERE id_atendimentos1 = ?";

        try {
            for (int i = 0; i < listaAtendimentos1.size(); i++) {
                ps = conAuxiliar.prepareStatement(sql);
                ps.setLong(1, user_session.getId());
                ps.setLong(2, listaAtendimentos1.get(i));
                ps.execute();
            }

            retorno = gravarLogRemocaoProfissionalEquipePacienteInstituicao(
                    listarIdPacienteInstituicaoAhSeremApagados(
                            codigoProfissional, codigoEquipe, conAuxiliar), codigoProfissional, idRemocaoProfissionalEquipe, conAuxiliar);

        } catch (SQLException sqle) {
            conAuxiliar.rollback();
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
            conAuxiliar.rollback();
            throw new ProjetoException(ex, this.getClass().getName());
        }
        return retorno;
    }

    private ArrayList<Integer> listarIdPacienteInstituicaoAhSeremApagados(Long codigoProfissional, Integer idEquipe, Connection conAuxiliar)
            throws ProjetoException, SQLException {

        ArrayList<Integer> lista = new ArrayList<>();

        String sql = "select distinct pda.id_paciente_instituicao from hosp.profissional_dia_atendimento pda " +
                "join hosp.paciente_instituicao pi on pda.id_paciente_instituicao = pi.id " +
                "where pi.status = 'A' and pi.codequipe = ? and pda.id_profissional = ?;";

        try {
            PreparedStatement stm = conAuxiliar.prepareStatement(sql);
            stm.setInt(1, idEquipe);
            stm.setLong(2, codigoProfissional);
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                Integer idPacienteInstituicao = null;
                idPacienteInstituicao = rs.getInt("id_paciente_instituicao");
                lista.add(idPacienteInstituicao);
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

    private Boolean gravarLogRemocaoProfissionalEquipePacienteInstituicao(
            List<Integer> listaIdPacienteInstituicao, Long codigoProfissional, int idRemocaoProfissionalEquipe, Connection conAuxiliar)
            throws ProjetoException, SQLException {

        Boolean retorno = false;
        String sql = "insert into logs.remocao_profissional_equipe_profissional_dia_atendimento " +
                "(id_remocao_profissional_equipe, id_paciente_instituicao, id_funcionario, dia_semana) " +
                "values (?, ?, ?, ?);";

        try {
            for (int i = 0; i < listaIdPacienteInstituicao.size(); i++) {

                List<Integer> listaDias = listarDiasDaSemanaAtendimentoProfissional(codigoProfissional, listaIdPacienteInstituicao.get(i), conAuxiliar);

                for (int j = 0; j < listaDias.size(); j++) {
                    ps = conAuxiliar.prepareStatement(sql);
                    ps.setInt(1, idRemocaoProfissionalEquipe);
                    ps.setLong(2, listaIdPacienteInstituicao.get(i));
                    ps.setLong(3, codigoProfissional);
                    ps.setInt(4, listaDias.get(j));
                    ps.execute();
                }
            }

            retorno = excluirProfissionalDiaAtendimento(listaIdPacienteInstituicao, codigoProfissional, conAuxiliar);

        } catch (SQLException sqle) {
            conAuxiliar.rollback();
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
            conAuxiliar.rollback();
            throw new ProjetoException(ex, this.getClass().getName());
        }
        return retorno;
    }

    public Boolean excluirProfissionalDiaAtendimento(List<Integer> listaIdPacienteInstituicao, Long codigoProfissional, Connection conAuxiliar)
            throws ProjetoException, SQLException {

        Boolean retorno = false;

        String sql = "delete from hosp.profissional_dia_atendimento where id_paciente_instituicao = ? and id_profissional = ?";

        try {
            for (int i = 0; i < listaIdPacienteInstituicao.size(); i++) {

                ps = conAuxiliar.prepareStatement(sql);
                ps.setLong(1, listaIdPacienteInstituicao.get(i));
                ps.setLong(2, codigoProfissional);
                ps.execute();
            }

            retorno = true;

        } catch (SQLException sqle) {
            conAuxiliar.rollback();
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
            conAuxiliar.rollback();
            throw new ProjetoException(ex, this.getClass().getName());
        }
        return retorno;
    }

    public List<Integer> listarDiasDaSemanaAtendimentoProfissional(Long idProfissional, int idPacienteInstituicao, Connection conAuxiliar)
            throws ProjetoException, SQLException {

        ArrayList<Integer> lista = new ArrayList<>();

        String sql = "SELECT dia_semana FROM hosp.profissional_dia_atendimento WHERE id_paciente_instituicao = ? AND id_profissional = ?;";

        try {
            PreparedStatement stm = conAuxiliar.prepareStatement(sql);
            stm.setInt(1, idPacienteInstituicao);
            stm.setLong(2, idProfissional);
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                int diaSemana = rs.getInt("dia_semana");
                lista.add(diaSemana);
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

    public ArrayList<RemocaoProfissionalEquipe> listarProfissionaisDaEquipeRemovidos(int codigoEquipe) throws ProjetoException {

        ArrayList<RemocaoProfissionalEquipe> lista = new ArrayList<>();

        String sql = "SELECT rpe.id, rpe.cod_profissional, f.descfuncionario, rpe.cod_equipe, e.descequipe, rpe.data_saida " +
                "FROM logs.remocao_profissional_equipe rpe " +
                "JOIN acl.funcionarios f ON (rpe.cod_profissional = f.id_funcionario) " +
                "JOIN hosp.equipe e ON (rpe.cod_equipe = e.id_equipe) " +
                "WHERE rpe.cod_equipe = ?;";

        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setInt(1, codigoEquipe);
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                RemocaoProfissionalEquipe remocao = new RemocaoProfissionalEquipe();
                remocao.setId(rs.getInt("id"));
                remocao.getFuncionario().setId(rs.getLong("cod_profissional"));
                remocao.getFuncionario().setNome(rs.getString("descfuncionario"));
                remocao.getEquipe().setCodEquipe(rs.getInt("cod_equipe"));
                remocao.getEquipe().setDescEquipe(rs.getString("descequipe"));
                remocao.setDataSaida(rs.getDate("data_saida"));

                lista.add(remocao);
            }

        } catch (SQLException sqle) {
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
            throw new ProjetoException(ex, this.getClass().getName());
        } finally {
            try {
                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return lista;
    }

    public SubstituicaoProfissionalEquipeDTO listarRemocaoPorId(int codigoRemocao) throws ProjetoException {

        SubstituicaoProfissionalEquipeDTO substituicao = new SubstituicaoProfissionalEquipeDTO();

        String sql = "SELECT rpe.cod_profissional, f.descfuncionario, rpe.cod_equipe, e.descequipe, rpe.data_saida " +
                "FROM logs.remocao_profissional_equipe rpe " +
                "JOIN acl.funcionarios f ON (rpe.cod_profissional = f.id_funcionario) " +
                "JOIN hosp.equipe e ON (rpe.cod_equipe = e.id_equipe) " +
                "WHERE rpe.id = ?;";

        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setInt(1, codigoRemocao);
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                substituicao.getFuncionarioRemovido().setId(rs.getLong("cod_profissional"));
                substituicao.getFuncionarioRemovido().setNome(rs.getString("descfuncionario"));
                substituicao.getEquipe().setCodEquipe(rs.getInt("cod_equipe"));
                substituicao.getEquipe().setDescEquipe(rs.getString("descequipe"));
                substituicao.getRemocaoProfissionalEquipe().setDataSaida(rs.getDate("data_saida"));
            }

        } catch (SQLException sqle) {
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
            throw new ProjetoException(ex, this.getClass().getName());
        } finally {
            try {
                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return substituicao;
    }

    public ArrayList<Integer> listarAtendimentosParaProfissionalAhSerSubstituido(long codigoProfissional, int codigoEquipe, Date dataDeSubstituicao) throws ProjetoException {

        ArrayList<Integer> lista = new ArrayList<>();

        String sql = "select a1.id_atendimentos1 " +
                "from logs.remocao_profissional_equipe rpe " +
                "join logs.remocao_profissional_equipe_atendimentos1 rpea on rpea.id_remocao_profissional_equipe = rpe.id " +
                "join hosp.atendimentos1 a1 on a1.id_atendimentos1 = rpea.id_atendimentos1 " +
                "join hosp.atendimentos a on a.id_atendimento = a1.id_atendimento " +
                "join hosp.programa p on p.id_programa = a.codprograma " +
                "join hosp.grupo g on g.id_grupo = a.codgrupo " +
                "join hosp.pacientes on pacientes.id_paciente = a.codpaciente " +
                "where rpe.cod_equipe = ? and rpe.cod_profissional = ? and a.dtaatende>=?";

        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setInt(1, codigoEquipe);
            stm.setLong(2, codigoProfissional);
            stm.setDate(3, new java.sql.Date(dataDeSubstituicao.getTime()));

            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                int idAtendimentos1 = rs.getInt("id_atendimentos1");
                lista.add(idAtendimentos1);
            }

        } catch (SQLException sqle) {
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
            throw new ProjetoException(ex, this.getClass().getName());
        } finally {
            try {
                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return lista;
    }

    public boolean gravarSubstituicaoProfissionalEquipe(SubstituicaoProfissionalEquipeDTO substituicao) throws ProjetoException {

        Boolean retorno = false;

        try {
            List<Integer> listaAtendimentos1 = listarAtendimentosParaProfissionalAhSerSubstituido(
                    substituicao.getFuncionarioRemovido().getId(), substituicao.getEquipe().getCodEquipe(), substituicao.getDataDeSubstituicao());

            substituicao.setListaAtendimentos1(listaAtendimentos1);

            con = ConnectionFactory.getConnection();
            String sql = "UPDATE hosp.atendimentos1  SET excluido  = 'N', codprofissionalatendimento = ? WHERE id_atendimentos1  = ? and not exists (select atendimentos.id_atendimento from hosp.atendimentos  " +
                    "join hosp.atendimentos1 on atendimentos1.id_atendimento = atendimentos.id_atendimento " +
                    "where atendimentos.id_atendimento=( " +
                    "select a1.id_atendimento from hosp.atendimentos1 a1  " +
                    "join hosp.atendimentos a on a.id_atendimento = a1.id_atendimento " +
                    "where a1.id_atendimentos1=? ) " +
                    "and coalesce(atendimentos.situacao,'A')<>'C' " +
                    "and coalesce(atendimentos1.excluido,'N' )='N' " +
                    "and atendimentos1.codprofissionalatendimento=? )";
            for (int i = 0; i < listaAtendimentos1.size(); i++) {
                ps = null;
                ps = con.prepareStatement(sql);

                ps.setLong(1, substituicao.getFuncionarioAssumir().getId());
                ps.setInt(2, listaAtendimentos1.get(i));
                ps.setInt(3, listaAtendimentos1.get(i));
                ps.setLong(4, substituicao.getFuncionarioAssumir().getId());
                ps.execute();
            }

            retorno = gravarLogSubstituicaoProfissionalEquipe(substituicao, con);
            if (retorno)
                con.commit();
            else
                con.rollback();

        } catch (SQLException sqle) {
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
            throw new ProjetoException(ex, this.getClass().getName());
        } finally {
            try {
                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return retorno;
    }

    public Boolean gravarLogSubstituicaoProfissionalEquipe(SubstituicaoProfissionalEquipeDTO substituicao, Connection conAuxiliar)
            throws ProjetoException, SQLException {

        Boolean retorno = false;

        FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
                .getSessionMap().get("obj_funcionario");

        String sql = "insert into logs.substituicao_profissional_equipe " +
                "(cod_equipe, cod_profissional_substituto, cod_profissional_substituido, usuario_acao, data_hora_acao) " +
                "values (?, ?, ?, ?, CURRENT_TIMESTAMP) RETURNING id;";

        try {
            ps = conAuxiliar.prepareStatement(sql);
            ps.setInt(1, substituicao.getEquipe().getCodEquipe());
            ps.setLong(2, substituicao.getFuncionarioAssumir().getId());
            ps.setLong(3, substituicao.getFuncionarioRemovido().getId());
            ps.setLong(4, user_session.getId());

            ResultSet rs = ps.executeQuery();

            sql = "insert into hosp.equipe_medico (equipe, medico) values(?,?);";

            ps = con.prepareStatement(sql);
            ps.setInt(1, substituicao.getEquipe().getCodEquipe());
            ps.setLong(2, substituicao.getFuncionarioAssumir().getId());
            ps.execute();


            if (rs.next()) {
                substituicao.setId(rs.getInt("id"));
            }

            retorno = gravarLogSubstituicaoProfissionalEquipeAtendimentos1(substituicao, conAuxiliar);

        } catch (SQLException sqle) {
            conAuxiliar.rollback();
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
            conAuxiliar.rollback();
            throw new ProjetoException(ex, this.getClass().getName());
        }
        return retorno;
    }

    public Boolean gravarLogSubstituicaoProfissionalEquipeAtendimentos1(SubstituicaoProfissionalEquipeDTO substituicao, Connection conAuxiliar)
            throws ProjetoException, SQLException {

        Boolean retorno = false;

        String sql = "insert into logs.substituicao_profissional_equipe_atendimentos1 " +
                "(id_substituicao_profissional_equipe, id_atendimentos1) values (?, ?)";
        try {

            for(int i=0; i<substituicao.getListaAtendimentos1().size(); i++) {
                ps = conAuxiliar.prepareStatement(sql);
                ps.setInt(1, substituicao.getId());
                ps.setLong(2, substituicao.getListaAtendimentos1().get(i));
                ps.execute();
            }

            retorno = gravarLogSubstituicaoProfissionalEquipeProfissionalDiaAtendimentos(substituicao, conAuxiliar);

        } catch (SQLException sqle) {
            conAuxiliar.rollback();
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
            conAuxiliar.rollback();
            throw new ProjetoException(ex, this.getClass().getName());
        }
        return retorno;
    }

    public Boolean gravarLogSubstituicaoProfissionalEquipeProfissionalDiaAtendimentos(SubstituicaoProfissionalEquipeDTO substituicao, Connection conAuxiliar)
            throws ProjetoException, SQLException {

        Boolean retorno = false;

        substituicao.setListaRemocoes(listarIdPacienteSubstituicaoSubstituicaoProfissionalEquipe(substituicao, conAuxiliar));

        String sql = "insert into logs.substituicao_profissional_equipe_profissional_dia_atendimento " +
                "(id_substituicao_profissional_equipe, id_paciente_instituicao, dia_semana) values (?, ?, ?)";

        try {

            for(int i=0; i<substituicao.getListaRemocoes().size(); i++) {
                ps = conAuxiliar.prepareStatement(sql);
                ps.setInt(1, substituicao.getId());
                ps.setInt(2, substituicao.getListaRemocoes().get(i).getIdPacienteInstituicao());
                ps.setInt(3, substituicao.getListaRemocoes().get(i).getDiaSemana());
                ps.execute();
            }

            retorno = gravarProfissionalProfissionalDiaAtendimento(substituicao, conAuxiliar);

        } catch (SQLException sqle) {
            conAuxiliar.rollback();
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
            conAuxiliar.rollback();
            throw new ProjetoException(ex, this.getClass().getName());
        }
        return retorno;
    }

    public Boolean gravarProfissionalProfissionalDiaAtendimento(SubstituicaoProfissionalEquipeDTO substituicao, Connection conAuxiliar)
            throws ProjetoException, SQLException {

        Boolean retorno = false;

        substituicao.setListaRemocoes(listarIdPacienteSubstituicaoSubstituicaoProfissionalEquipe(substituicao, conAuxiliar));

        String sql = "insert into hosp.profissional_dia_atendimento " +
                "(id_paciente_instituicao, id_profissional, dia_semana) values (?, ?, ?)";

        try {

            for(int i=0; i<substituicao.getListaRemocoes().size(); i++) {
                ps = conAuxiliar.prepareStatement(sql);
                ps.setInt(1, substituicao.getListaRemocoes().get(i).getIdPacienteInstituicao());
                ps.setLong(2, substituicao.getFuncionarioAssumir().getId());
                ps.setInt(3, substituicao.getListaRemocoes().get(i).getDiaSemana());
                ps.execute();
            }

            retorno = true;

        } catch (SQLException sqle) {
            conAuxiliar.rollback();
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
            conAuxiliar.rollback();
            throw new ProjetoException(ex, this.getClass().getName());
        }
        return retorno;
    }

    public ArrayList<RemocaoProfissionalEquipe> listarIdPacienteSubstituicaoSubstituicaoProfissionalEquipe(
            SubstituicaoProfissionalEquipeDTO substituicaoProfissionalEquipeDTO, Connection conAuxiliar)
            throws SQLException, ProjetoException {

        ArrayList<RemocaoProfissionalEquipe> lista = new ArrayList<>();

        String sql = "SELECT DISTINCT rp.id_paciente_instituicao, rp.dia_semana " +
                "FROM logs.remocao_profissional_equipe_profissional_dia_atendimento rp " +
                "JOIN logs.remocao_profissional_equipe r ON (rp.id_remocao_profissional_equipe = r.id) " +
                "WHERE rp.id_funcionario = ? AND r.cod_equipe = ? AND r.data_saida >= ?;";

        try {
            PreparedStatement stm = conAuxiliar.prepareStatement(sql);
            stm.setLong(1, substituicaoProfissionalEquipeDTO.getFuncionarioRemovido().getId());
            stm.setInt(2, substituicaoProfissionalEquipeDTO.getEquipe().getCodEquipe());
            stm.setDate(3, DataUtil.converterDateUtilParaDateSql(substituicaoProfissionalEquipeDTO.getRemocaoProfissionalEquipe().getDataSaida()));

            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                RemocaoProfissionalEquipe remocaoProfissionalEquipe = new RemocaoProfissionalEquipe();
                remocaoProfissionalEquipe.setIdPacienteInstituicao(rs.getInt("id_paciente_instituicao"));
                remocaoProfissionalEquipe.setDiaSemana(rs.getInt("dia_semana"));

                lista.add(remocaoProfissionalEquipe);
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


    public ArrayList<EquipeGrupoProgramaUnidadeDTO> buscaEquipesPorProgramas
            (List<GrupoProgramaUnidadeDTO> listaGrupoProgramaDTO) throws ProjetoException {

        PreparedStatement ps = null;
        con = ConnectionFactory.getConnection();

        String sql = "select e.id_equipe, e.descequipe, g.id_grupo, g.descgrupo, p.id_programa, p.descprograma, u.id id_unidade, u.nome unidade " +
                "from hosp.equipe e " +
                "join hosp.equipe_grupo eg on e.id_equipe = eg.codequipe " +
                "join hosp.grupo g on eg.id_grupo = g.id_grupo " +
                "join hosp.grupo_programa gp on g.id_grupo = gp.codgrupo " +
                "join hosp.programa p on gp.codprograma = p.id_programa " +
                "join hosp.unidade u on g.cod_unidade = u.id " +
                "where (g.id_grupo = ? and p.id_programa = ?)   ";

        String filtroUnidade = " or (g.id_grupo = ? and p.id_programa = ?) ";
        String ordenacao = " order by e.descequipe;";

        for (int i = 1; i < listaGrupoProgramaDTO.size(); i++) {
            sql += filtroUnidade;
        }
        sql += ordenacao;

        ArrayList<EquipeGrupoProgramaUnidadeDTO> lista = new ArrayList<>();
        try {
            ps = con.prepareStatement(sql);
            ps.setInt(1, listaGrupoProgramaDTO.get(0).getGrupo().getIdGrupo());
            ps.setInt(2, listaGrupoProgramaDTO.get(0).getPrograma().getIdPrograma());
            int contador = 3;

            for (int i = 1; i < listaGrupoProgramaDTO.size(); i++) {
                ps.setInt(contador, listaGrupoProgramaDTO.get(i).getGrupo().getIdGrupo());
                contador++;
                ps.setInt(contador, listaGrupoProgramaDTO.get(i).getPrograma().getIdPrograma());
                contador++;
            }

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                EquipeGrupoProgramaUnidadeDTO equipeGrupoProgramaUnidadeDTO = new EquipeGrupoProgramaUnidadeDTO();
                equipeGrupoProgramaUnidadeDTO.getEquipe().setCodEquipe(rs.getInt("id_equipe"));
                equipeGrupoProgramaUnidadeDTO.getEquipe().setDescEquipe(rs.getString("descequipe"));
                equipeGrupoProgramaUnidadeDTO.getGrupo().setIdGrupo(rs.getInt("id_grupo"));
                equipeGrupoProgramaUnidadeDTO.getGrupo().setDescGrupo(rs.getString("descgrupo"));
                equipeGrupoProgramaUnidadeDTO.getPrograma().setIdPrograma(rs.getInt("id_programa"));
                equipeGrupoProgramaUnidadeDTO.getPrograma().setDescPrograma(rs.getString("descprograma"));
                equipeGrupoProgramaUnidadeDTO.getUnidade().setId(rs.getInt("id_unidade"));
                equipeGrupoProgramaUnidadeDTO.getUnidade().setNomeUnidade(rs.getString("unidade"));
                lista.add(equipeGrupoProgramaUnidadeDTO);
            }
        } catch (SQLException sqle) {
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
            throw new ProjetoException(ex, this.getClass().getName());
        } finally {
            try {
                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return lista;
    }

    public List<EquipeBean> listarEquipePorGrupo(List<GrupoBean> listaGrupo) throws ProjetoException {
        List<EquipeBean> lista = new ArrayList<>();
        String sql = "select distinct e.id_equipe, e.id_equipe ||'-'|| e.descequipe as descequipe, e.turno from hosp.equipe e "
                + " left join hosp.equipe_grupo eg on (e.id_equipe = eg.codequipe) where e.ativo = true and eg.id_grupo = ? order by descequipe ";

        List<Integer> idEquipes = new ArrayList<>();
        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);

            for (GrupoBean grupo : listaGrupo) {
                stm.setInt(1, grupo.getIdGrupo());
                ResultSet rs = stm.executeQuery();

                while (rs.next()) {
                    if(!idEquipes.contains(rs.getInt("id_equipe"))) {
                        EquipeBean equipe = new EquipeBean();
                        equipe.setCodEquipe(rs.getInt("id_equipe"));
                        equipe.setDescEquipe(rs.getString("descequipe"));
                        equipe.setTurno(rs.getString("turno"));

                        lista.add(equipe);
                    }
                }
            }

        } catch (SQLException sqle) {
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
            throw new ProjetoException(ex, this.getClass().getName());
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
