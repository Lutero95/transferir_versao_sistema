package br.gov.al.maceio.sishosp.hosp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import javax.faces.context.FacesContext;

import br.gov.al.maceio.sishosp.acl.dao.FuncionarioDAO;
import br.gov.al.maceio.sishosp.acl.model.FuncionarioBean;
import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;
import br.gov.al.maceio.sishosp.comum.util.TratamentoErrosUtil;
import br.gov.al.maceio.sishosp.comum.util.VerificadorUtil;
import br.gov.al.maceio.sishosp.hosp.model.*;
import br.gov.al.maceio.sishosp.hosp.model.CidBean;
import br.gov.al.maceio.sishosp.hosp.model.EspecialidadeBean;
import br.gov.al.maceio.sishosp.hosp.model.ProcedimentoBean;
import br.gov.al.maceio.sishosp.hosp.model.ProgramaBean;
import br.gov.al.maceio.sishosp.hosp.model.UnidadeBean;
import br.gov.al.maceio.sishosp.hosp.model.dto.BuscaGrupoFrequenciaDTO;
import br.gov.al.maceio.sishosp.hosp.model.dto.ProcedimentoCboEspecificoDTO;
import br.gov.al.maceio.sishosp.hosp.model.dto.ProcedimentoIdadeEspecificaDTO;
import br.gov.al.maceio.sishosp.hosp.model.dto.ProcedimentoProfissionalEquipeEspecificoDTO;

public class ProgramaDAO {

    Connection con = null;

    FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
            .getSessionMap().get("obj_funcionario");

    public boolean gravarPrograma(ProgramaBean prog) throws ProjetoException {

        Boolean retorno = false;
        PreparedStatement ps = null;

        String sql = "insert into hosp.programa (descprograma, cod_unidade, cod_procedimento, permite_paciente_sem_laudo, "+
                "dias_paciente_sem_laudo_ativo, permite_alteracao_cid_evolucao) "+
                " values (?, ?, ?, ?, ?, ?) RETURNING id_programa;";
        try {
            con = ConnectionFactory.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, prog.getDescPrograma().toUpperCase());
            ps.setInt(2, user_session.getUnidade().getId());
            ps.setInt(3, prog.getProcedimento().getIdProc());
            ps.setBoolean(4, prog.isPermitePacienteSemLaudo());
            if (!VerificadorUtil.verificarSeObjetoNuloOuZero(prog.getDiasPacienteSemLaudoAtivo())) {
                ps.setInt(5, prog.getDiasPacienteSemLaudoAtivo());
            } else {
                ps.setNull(5, Types.NULL);
            }
            ps.setBoolean(6, prog.isPermiteAlteracaoCidNaEvolucao());
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                prog.setIdPrograma(rs.getInt("id_programa"));
            }

            inserirGruposPrograma(prog, con);
            inserirProcedimentosIhCbosEspecificos(prog, con);
            inserirProcedimentosParaIdadesEspecificas(prog, con);
            inserirEspecialidadesPrograma(prog, con);
            inserirProcedimentosPermitidos(prog, con);
            inserirCidsPermitidos(prog, con);
            inserirProcedimentoProfissionalEquipeEspecifico(prog, con);

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
                ex.printStackTrace();
            }
        }
        return retorno;
    }

    public Boolean alterarPrograma(ProgramaBean prog) throws ProjetoException {

        Boolean retorno = false;
        String sql = "update hosp.programa set descprograma = ?, cod_procedimento = ?, "
                + "permite_paciente_sem_laudo = ?, dias_paciente_sem_laudo_ativo = ?, permite_alteracao_cid_evolucao = ? "
                + " where id_programa = ?";

        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, prog.getDescPrograma().toUpperCase());
            stmt.setInt(2, prog.getProcedimento().getIdProc());
            stmt.setBoolean(3, prog.isPermitePacienteSemLaudo());
            stmt.setInt(4, prog.getDiasPacienteSemLaudoAtivo());
            stmt.setBoolean(5, prog.isPermiteAlteracaoCidNaEvolucao());
            stmt.setInt(6, prog.getIdPrograma());
            stmt.executeUpdate();

            excluirGruposPrograma(prog.getIdPrograma(), con);
            inserirGruposPrograma(prog, con);

            excluirProcedimentosIhCbosEspecificos(prog.getIdPrograma(), con);
            inserirProcedimentosIhCbosEspecificos(prog, con);

            excluirProcedimentosParaIdadesEspecificas(prog.getIdPrograma(), con);
            inserirProcedimentosParaIdadesEspecificas(prog, con);

            excluirEspecialidadesPrograma(prog.getIdPrograma(), con);
            inserirEspecialidadesPrograma(prog, con);

            excluirProcedimentosPermitidos(prog.getIdPrograma(), con);
            inserirProcedimentosPermitidos(prog, con);

            excluirCidsPermitidos(prog.getIdPrograma(), con);
            inserirCidsPermitidos(prog, con);
            
            excluirProcedimentoProfissionalEquipeEspecifico(prog.getIdPrograma(), con);
            inserirProcedimentoProfissionalEquipeEspecifico(prog, con);

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
                ex.printStackTrace();
            }
        }
        return retorno;
    }

    public Boolean excluirPrograma(ProgramaBean prog) throws ProjetoException {

        Boolean retorno = false;

        try {
            con = ConnectionFactory.getConnection();

            String sql1 = "delete from hosp.grupo_programa where codprograma = ?";
            PreparedStatement stmt = con.prepareStatement(sql1);
            stmt.setLong(1, prog.getIdPrograma());
            stmt.execute();

            excluirProcedimentosIhCbosEspecificos(prog.getIdPrograma(), con);
            excluirProcedimentosParaIdadesEspecificas(prog.getIdPrograma(), con);
            excluirEspecialidadesPrograma(prog.getIdPrograma(), con);
            excluirProcedimentosPermitidos(prog.getIdPrograma(), con);
            excluirCidsPermitidos(prog.getIdPrograma(), con);

            String sql2 = "delete from hosp.programa where id_programa = ?";
            stmt = con.prepareStatement(sql2);
            stmt.setLong(1, prog.getIdPrograma());
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
                ex.printStackTrace();
            }
        }
        return retorno;
    }

    public List<ProgramaBean> listarProgramas() throws ProjetoException {
        List<ProgramaBean> lista = new ArrayList<>();
        String sql = "SELECT DISTINCT id_programa, descprograma, cod_procedimento "
                + "FROM hosp.programa LEFT JOIN hosp.profissional_programa_grupo ON programa.id_programa = profissional_programa_grupo.codprograma "
                + "WHERE cod_unidade = ? ORDER BY descprograma";

        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setInt(1, user_session.getUnidade().getId());

            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                ProgramaBean programa = new ProgramaBean();
                programa.setIdPrograma(rs.getInt("id_programa"));
                programa.setDescPrograma(rs.getString("descprograma"));
                programa.setProcedimento(new ProcedimentoDAO().listarProcedimentoPorIdComConexao(rs.getInt("cod_procedimento"), con));
                lista.add(programa);
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

    public ArrayList<ProgramaBean> BuscalistarProgramas() throws ProjetoException {
        PreparedStatement ps = null;
        con = ConnectionFactory.getConnection();


        String sql = "select id_programa, descprograma, cod_procedimento from hosp.programa "
                + "join hosp.profissional_programa_grupo on programa.id_programa = profissional_programa_grupo.codprograma "
                + "where programa.cod_unidade = ? "
                + "order by descprograma";
        GrupoDAO gDao = new GrupoDAO();
        ArrayList<ProgramaBean> lista = new ArrayList<>();
        try {
            ps = con.prepareStatement(sql);
            ps.setInt(1, user_session.getUnidade().getId());

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                ProgramaBean programa = new ProgramaBean();
                programa.setIdPrograma(rs.getInt("id_programa"));
                programa.setDescPrograma(rs.getString("descprograma"));
                programa.setProcedimento(new ProcedimentoDAO().listarProcedimentoPorIdComConexao(rs.getInt("cod_procedimento"), con));
                programa.setGrupo(gDao.listarGruposPorProgramaComConexao(rs
                        .getInt("id_programa"), con));
                lista.add(programa);
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

    public List<ProgramaBean> listarProgramasBusca(String descricao,
                                                   Integer tipo) throws ProjetoException {
        List<ProgramaBean> lista = new ArrayList<>();
        String sql = "select distinct id_programa,id_programa ||'-'|| descprograma as descprograma, cod_procedimento from hosp.programa "
                + "left join hosp.profissional_programa_grupo on programa.id_programa = profissional_programa_grupo.codprograma ";


        if (tipo == 1) {
            sql += " and upper(id_programa ||'-'|| descprograma) LIKE ? and cod_unidade = ? order by descprograma";
        }
        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);

            stm.setString(1, "%" + descricao.toUpperCase() + "%");
            stm.setInt(2, user_session.getUnidade().getId());

            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                ProgramaBean programa = new ProgramaBean();
                programa.setIdPrograma(rs.getInt("id_programa"));
                programa.setDescPrograma(rs.getString("descprograma"));
                programa.setProcedimento(new ProcedimentoDAO().listarProcedimentoPorIdComConexao(rs.getInt("cod_procedimento"), con));
                lista.add(programa);
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

    public List<ProgramaBean> listarProgramasBuscaUsuario(String descricao,
                                                          Integer tipo) throws ProjetoException {
        List<ProgramaBean> lista = new ArrayList<>();
        String sql = "select distinct id_programa,id_programa ||'-'|| descprograma as descprograma, cod_procedimento, coalesce(permite_duplicar_proc_atendimento,false)  permite_duplicar_proc_atendimento from hosp.programa "
                + "left join hosp.profissional_programa_grupo on programa.id_programa = profissional_programa_grupo.codprograma "
                + "left join hosp.proc on proc.id = programa.cod_procedimento "
                + "where proc.ativo = 'S' and codprofissional = ? and programa.cod_unidade=?";

        if (tipo == 1) {
            sql += " and upper(id_programa ||'-'|| descprograma) LIKE ? order by descprograma";
        }
        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);

            stm.setLong(1, user_session.getId());
            stm.setLong(2, user_session.getUnidade().getId());
            stm.setString(3, "%" + descricao.toUpperCase() + "%");

            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                ProgramaBean programa = new ProgramaBean();
                programa.setIdPrograma(rs.getInt("id_programa"));
                programa.setDescPrograma(rs.getString("descprograma"));
                programa.setProcedimento(new ProcedimentoDAO().listarProcedimentoPorIdComConexao(rs.getInt("cod_procedimento"), con));
                programa.setPermiteDuplicarProcedimentoAtendimento(rs.getBoolean("permite_duplicar_proc_atendimento"));
                lista.add(programa);
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


    public List<ProgramaBean> listarProgramasSemLaudoBusca(String descricao) throws ProjetoException {
        List<ProgramaBean> lista = new ArrayList<>();
        String sql = "select distinct id_programa, id_programa ||'-'|| descprograma as descprograma, cod_procedimento, "
                + "dias_paciente_sem_laudo_ativo from hosp.programa "
                + "left join hosp.profissional_programa_grupo on programa.id_programa = profissional_programa_grupo.codprograma "
                + "where programa.permite_paciente_sem_laudo = true and programa.cod_unidade = ? "
                + "and codprofissional = ? "
                + "and upper(id_programa ||'-'|| descprograma) LIKE ? order by descprograma ";

        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);

            stm.setLong(1, user_session.getUnidade().getId());
            stm.setLong(2, user_session.getId());
            stm.setString(3, "%" + descricao.toUpperCase() + "%");

            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                ProgramaBean programa = new ProgramaBean();
                programa.setIdPrograma(rs.getInt("id_programa"));
                programa.setDescPrograma(rs.getString("descprograma"));
                programa.getProcedimento().setIdProc(rs.getInt("cod_procedimento"));
                programa.setDiasPacienteSemLaudoAtivo(rs.getInt("dias_paciente_sem_laudo_ativo"));
                lista.add(programa);
            }
        } catch (SQLException sqle) {
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(),
                    sqle);
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


    public List<ProgramaBean> listarProgramasUsuario() throws ProjetoException {
        List<ProgramaBean> lista = new ArrayList<>();
        String sql = "select distinct id_programa,id_programa ||'-'|| descprograma as descprograma, cod_procedimento,  proc.nome descproc, coalesce(permite_duplicar_proc_atendimento, false) permite_duplicar_proc_atendimento from hosp.programa "
                + "left join hosp.profissional_programa_grupo on programa.id_programa = profissional_programa_grupo.codprograma left join hosp.proc on proc.id = programa.cod_procedimento "
                + "where proc.ativo = 'S' and codprofissional = ? and programa.cod_unidade=? order by descprograma";

        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
            FuncionarioBean user_session = (FuncionarioBean) FacesContext
                    .getCurrentInstance().getExternalContext().getSessionMap()
                    .get("obj_usuario");

            stm.setLong(1, user_session.getId());
            stm.setLong(2, user_session.getUnidade().getId());

            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                ProgramaBean programa = new ProgramaBean();
                programa.setIdPrograma(rs.getInt("id_programa"));
                programa.setDescPrograma(rs.getString("descprograma"));
                programa.getProcedimento().setIdProc(rs.getInt("cod_procedimento"));
                programa.getProcedimento().setNomeProc(rs.getString("descproc"));
                programa.setPermiteDuplicarProcedimentoAtendimento(rs.getBoolean("permite_duplicar_proc_atendimento"));
                lista.add(programa);
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

    public List<ProgramaBean> listarProgramasSemLaudo() throws ProjetoException {
        List<ProgramaBean> lista = new ArrayList<>();
        String sql = "select distinct id_programa,id_programa ||'-'|| descprograma as descprograma, cod_procedimento, "
                + " dias_paciente_sem_laudo_ativo from hosp.programa "
                + " left join hosp.profissional_programa_grupo on programa.id_programa = profissional_programa_grupo.codprograma " +
                "where programa.cod_unidade = ? and codprofissional = ? and permite_paciente_sem_laudo = true order by descprograma";

        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);

            stm.setLong(1, user_session.getUnidade().getId());
            stm.setLong(2, user_session.getId());

            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                ProgramaBean programa = new ProgramaBean();
                programa.setIdPrograma(rs.getInt("id_programa"));
                programa.setDescPrograma(rs.getString("descprograma"));
                programa.getProcedimento().setIdProc(rs.getInt("cod_procedimento"));
                programa.setDiasPacienteSemLaudoAtivo(rs.getInt("dias_paciente_sem_laudo_ativo"));
                lista.add(programa);
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

    public ProgramaBean listarProgramaPorId(int id) throws ProjetoException {

        ProgramaBean programa = new ProgramaBean();
        GrupoDAO gDao = new GrupoDAO();
        String sql = "select id_programa, descprograma, cod_procedimento, permite_paciente_sem_laudo, "
                + "dias_paciente_sem_laudo_ativo, permite_alteracao_cid_evolucao "
                + " from hosp.programa where id_programa = ? order by descprograma";
        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setInt(1, id);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                programa = new ProgramaBean();
                Integer idPrograma = rs.getInt("id_programa");
                programa.setIdPrograma(idPrograma);
                programa.setDescPrograma(rs.getString("descprograma"));
                programa.setPermitePacienteSemLaudo(rs.getBoolean("permite_paciente_sem_laudo"));
                programa.setDiasPacienteSemLaudoAtivo(rs.getInt("dias_paciente_sem_laudo_ativo"));
                programa.setPermiteAlteracaoCidNaEvolucao(rs.getBoolean("permite_alteracao_cid_evolucao"));
                programa.setProcedimento(new ProcedimentoDAO().listarProcedimentoPorIdComConexao(rs.getInt("cod_procedimento"), con));
                programa.setListaGrupoFrequenciaDTO(gDao.buscaGruposComFrequecia(idPrograma, con));
                programa.setListaProcedimentoCboEspecificoDTO(listarProcedimentosIhCbosEspecificos(idPrograma, con));
                programa.setListaProcedimentoIdadeEspecificaDTO(listarProcedimentosIdadeEspecifica(idPrograma, con));
                programa.setListaEspecialidadesEspecificas(listarEspecialidadePrograma(idPrograma, con));
                programa.setListaProcedimentosPermitidos(listarProcedimentosPermitidos(idPrograma, con));
                programa.setListaCidsPermitidos(listarCidsPermitidos(idPrograma, con));
                programa.setListaProcedimentoProfissionalEquipeEspecificaDTO(listarProcedimentoProfissionalEquipeEspecifico(idPrograma, con));
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
        return programa;
    }

    public ProgramaBean listarProgramaPorIdParaConverter(int id) throws ProjetoException {

        ProgramaBean programa = new ProgramaBean();
        String sql = "select id_programa, descprograma, cod_procedimento,  proc.nome descproc, dias_paciente_sem_laudo_ativo, coalesce(permite_duplicar_proc_atendimento,false)  permite_duplicar_proc_atendimento  from hosp.programa "
                + "join hosp.proc on proc.id = programa.cod_procedimento where programa.id_programa = ? and proc.ativo = 'S'  order by descprograma";
        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setInt(1, id);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                programa = new ProgramaBean();
                programa.setIdPrograma(rs.getInt("id_programa"));
                programa.setDescPrograma(rs.getString("descprograma"));
                programa.getProcedimento().setIdProc(rs.getInt("cod_procedimento"));
                programa.getProcedimento().setNomeProc(rs.getString("descproc"));
                programa.setDiasPacienteSemLaudoAtivo(rs.getInt("dias_paciente_sem_laudo_ativo"));
                programa.setPermiteDuplicarProcedimentoAtendimento(rs.getBoolean("permite_duplicar_proc_atendimento"));
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
        return programa;
    }

    public ProgramaBean listarProgramaPorIdComConexao(int id, Connection conAuxiliar) throws SQLException, ProjetoException {

        ProgramaBean programa = new ProgramaBean();
        String sql = "select id_programa, descprograma, cod_procedimento, permite_alteracao_cid_evolucao from hosp.programa where id_programa = ? order by descprograma";
        try {
            PreparedStatement stm = conAuxiliar.prepareStatement(sql);
            stm.setInt(1, id);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                programa = new ProgramaBean();
                programa.setIdPrograma(rs.getInt("id_programa"));
                programa.setDescPrograma(rs.getString("descprograma"));
                programa.setPermiteAlteracaoCidNaEvolucao(rs.getBoolean("permite_alteracao_cid_evolucao"));
            }

        } catch (SQLException sqle) {
            conAuxiliar.rollback();
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
            conAuxiliar.rollback();
            throw new ProjetoException(ex, this.getClass().getName());
        }
        return programa;
    }

    public List<ProgramaBean> listarProgramasEGrupos() throws ProjetoException {
        List<ProgramaBean> lista = new ArrayList<>();
        String sql = "select gp.codprograma, p.descprograma, gp.codgrupo, g.descgrupo, p.cod_procedimento "
                + "from hosp.grupo_programa gp "
                + "left join hosp.programa p on (gp.codprograma = p.id_programa) "
                + "left join hosp.grupo g on (gp.codgrupo = g.id_grupo) "
                + "where p.cod_unidade = ? order by p.descprograma";

        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setInt(1, user_session.getUnidade().getId());
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                ProgramaBean programa = new ProgramaBean();
                programa.setIdPrograma(rs.getInt("codprograma"));
                programa.setDescPrograma(rs.getString("descprograma"));
                programa.setProcedimento(new ProcedimentoDAO().listarProcedimentoPorIdComConexao(rs.getInt("cod_procedimento"), con));
                programa.getGrupoBean().setIdGrupo(rs.getInt("codgrupo"));
                programa.getGrupoBean().setDescGrupo(rs.getString("descgrupo"));

                lista.add(programa);
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

    public List<ProgramaBean> listarProgramasEGruposPorUnidade(int codigoUnidade) throws ProjetoException {
        List<ProgramaBean> lista = new ArrayList<>();
        String sql = "select gp.codprograma, p.descprograma, gp.codgrupo, g.descgrupo, "+
                "p.cod_procedimento, u.nome as unidade, u.id " +
                "from hosp.grupo_programa gp " +
                "join hosp.programa p on (gp.codprograma = p.id_programa) " +
                "join hosp.grupo g on (gp.codgrupo = g.id_grupo) " +
                "join hosp.unidade u on u.id = p.cod_unidade " +
                "where p.cod_unidade = ? order by p.descprograma";

        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setInt(1, codigoUnidade);
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                ProgramaBean programa = new ProgramaBean();
                programa.setIdPrograma(rs.getInt("codprograma"));
                programa.setDescPrograma(rs.getString("descprograma"));
                programa.setProcedimento(new ProcedimentoDAO().listarProcedimentoPorIdComConexao(rs.getInt("cod_procedimento"), con));
                programa.getGrupoBean().setIdGrupo(rs.getInt("codgrupo"));
                programa.getGrupoBean().setDescGrupo(rs.getString("descgrupo"));
                programa.setDescricaoUnidade(rs.getString("unidade"));
                programa.setCodUnidade(rs.getInt("id"));
                lista.add(programa);
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

    public List<ProgramaBean> listarProgramasBuscaUsuarioOutraUnidade(String descricao, Integer codEmpresa) throws ProjetoException {
        List<ProgramaBean> lista = new ArrayList<>();
        String sql = "select id_programa,id_programa ||'-'|| descprograma as descprograma, cod_procedimento from hosp.programa "
                + "left join hosp.profissional_programa_grupo on programa.id_programa = profissional_programa_grupo.codprograma "
                + "where codprofissional = ? and cod_unidade = ?"
                + "and upper(id_programa ||'-'|| descprograma) LIKE ? order by descprograma";

        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);

            FuncionarioBean user_session = (FuncionarioBean) FacesContext
                    .getCurrentInstance().getExternalContext().getSessionMap()
                    .get("obj_usuario");

            stm.setLong(1, user_session.getId());
            stm.setInt(2, codEmpresa);
            stm.setString(3, "%" + descricao.toUpperCase() + "%");

            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                ProgramaBean programa = new ProgramaBean();
                programa.setIdPrograma(rs.getInt("id_programa"));
                programa.setDescPrograma(rs.getString("descprograma"));
                programa.setProcedimento(new ProcedimentoDAO().listarProcedimentoPorIdComConexao(rs.getInt("cod_procedimento"), con));

                lista.add(programa);
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

    public List<ProgramaBean> listarProgramasPorTipoAtend(int idTipo, Connection conAuxiliar) throws SQLException, ProjetoException {

        List<ProgramaBean> lista = new ArrayList<>();

        String sql = "SELECT p.id_programa, p.descprograma " +
                "FROM hosp.programa p " +
                "JOIN hosp.tipoatendimento_programa t ON (t.codprograma = p.id_programa) " +
                "WHERE t.codtipoatendimento = ? ORDER BY p.descprograma";
        try {
            PreparedStatement stm = conAuxiliar.prepareStatement(sql);
            stm.setInt(1, idTipo);
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                ProgramaBean programa = new ProgramaBean();
                programa.setIdPrograma(rs.getInt("id_programa"));
                programa.setDescPrograma(rs.getString("descprograma"));
                lista.add(programa);
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

    private void excluirGruposPrograma(Integer idPrograma, Connection conAuxiliar)
            throws ProjetoException, SQLException {

        try {
            String sql = "delete from hosp.grupo_programa where codprograma = ?";

            PreparedStatement stmt = conAuxiliar.prepareStatement(sql);
            stmt.setInt(1, idPrograma);
            stmt.executeUpdate();
        } catch (SQLException sqle) {
            conAuxiliar.rollback();
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
            conAuxiliar.rollback();
            throw new ProjetoException(ex, this.getClass().getName());
        }
    }

    private void inserirGruposPrograma (ProgramaBean programa, Connection conAuxiliar)
            throws ProjetoException, SQLException {

        try {
            String sql = "insert into hosp.grupo_programa (codprograma, codgrupo, qtdfrequencia, id_procedimento_padrao) values(?,?,?,?);";;

            PreparedStatement stmt = conAuxiliar.prepareStatement(sql);
            for (BuscaGrupoFrequenciaDTO grupoFrequencia : programa.getListaGrupoFrequenciaDTO()) {
                stmt.setInt(1, programa.getIdPrograma());
                stmt.setInt(2, grupoFrequencia.getGrupo().getIdGrupo());
                stmt.setInt(3, grupoFrequencia.getFrequencia());

                if(VerificadorUtil.verificarSeObjetoNuloOuZero(grupoFrequencia.getProcedimentoPadao().getIdProc()))
                    stmt.setNull(4, Types.NULL);
                else
                    stmt.setInt(4, grupoFrequencia.getProcedimentoPadao().getIdProc());
                stmt.executeUpdate();
            }

        } catch (SQLException sqle) {
            conAuxiliar.rollback();
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
            conAuxiliar.rollback();
            throw new ProjetoException(ex, this.getClass().getName());
        }
    }


    private void excluirProcedimentosIhCbosEspecificos(Integer idPrograma, Connection conAuxiliar)
            throws ProjetoException, SQLException {

        try {
            String sql = "delete from hosp.programa_procedimento_cbo_especifico where id_programa = ?;";

            PreparedStatement stmt = conAuxiliar.prepareStatement(sql);
            stmt.setInt(1, idPrograma);
            stmt.executeUpdate();
        } catch (SQLException sqle) {
            conAuxiliar.rollback();
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
            conAuxiliar.rollback();
            throw new ProjetoException(ex, this.getClass().getName());
        }
    }


    private void inserirProcedimentosIhCbosEspecificos (ProgramaBean programa, Connection conAuxiliar)
            throws ProjetoException, SQLException {

        try {
            String sql = "INSERT INTO hosp.programa_procedimento_cbo_especifico " +
                    "(id_programa, id_procedimento, id_cbo) " +
                    "VALUES(?, ?, ?); ";

            PreparedStatement stmt = conAuxiliar.prepareStatement(sql);
            for (ProcedimentoCboEspecificoDTO procedimentoCboEspecificoDTO : programa.getListaProcedimentoCboEspecificoDTO()) {
                stmt.setInt(1, programa.getIdPrograma());
                stmt.setInt(2, procedimentoCboEspecificoDTO.getProcedimento().getIdProc());
                stmt.setInt(3, procedimentoCboEspecificoDTO.getCbo().getCodCbo());
                stmt.executeUpdate();
            }
        } catch (SQLException sqle) {
            conAuxiliar.rollback();
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
            conAuxiliar.rollback();
            throw new ProjetoException(ex, this.getClass().getName());
        }
    }

    private void excluirProcedimentosParaIdadesEspecificas (Integer idPrograma, Connection conAuxiliar)
            throws ProjetoException, SQLException {

        try {
            String sql = "delete from hosp.programa_procedimento_idade_especifica where id_programa = ?;";

            PreparedStatement stmt = conAuxiliar.prepareStatement(sql);
            stmt.setInt(1, idPrograma);
            stmt.executeUpdate();
        } catch (SQLException sqle) {
            conAuxiliar.rollback();
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
            conAuxiliar.rollback();
            throw new ProjetoException(ex, this.getClass().getName());
        }
    }

    private void inserirProcedimentosParaIdadesEspecificas (ProgramaBean programa, Connection conAuxiliar)
            throws ProjetoException, SQLException {

        try {
            String sql = "INSERT INTO hosp.programa_procedimento_idade_especifica "
                    + "(id_programa, id_procedimento, idade_minima, idade_maxima) VALUES(?, ?, ?, ?); ";

            PreparedStatement stmt = conAuxiliar.prepareStatement(sql);
            for (ProcedimentoIdadeEspecificaDTO procedimentoIdadeEspecificaDTO : programa
                    .getListaProcedimentoIdadeEspecificaDTO()) {
                stmt.setInt(1, programa.getIdPrograma());
                stmt.setInt(2, procedimentoIdadeEspecificaDTO.getProcedimento().getIdProc());
                stmt.setInt(3, procedimentoIdadeEspecificaDTO.getIdadeMinima());
                stmt.setInt(4, procedimentoIdadeEspecificaDTO.getIdadeMaxima());
                stmt.executeUpdate();
            }
        } catch (SQLException sqle) {
            conAuxiliar.rollback();
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(),
                    sqle);
        } catch (Exception ex) {
            conAuxiliar.rollback();
            throw new ProjetoException(ex, this.getClass().getName());
        }
    }

    private void excluirEspecialidadesPrograma (Integer idPrograma, Connection conAuxiliar)
            throws ProjetoException, SQLException {

        try {
            String sql = "delete from hosp.programa_especialidade where id_programa = ?;";

            PreparedStatement stmt = conAuxiliar.prepareStatement(sql);
            stmt.setInt(1, idPrograma);
            stmt.executeUpdate();
        } catch (SQLException sqle) {
            conAuxiliar.rollback();
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
            conAuxiliar.rollback();
            throw new ProjetoException(ex, this.getClass().getName());
        }
    }

    private void inserirEspecialidadesPrograma (ProgramaBean programa, Connection conAuxiliar)
            throws ProjetoException, SQLException {

        try {
            String sql = "INSERT INTO hosp.programa_especialidade (id_programa, id_especialidade) VALUES(?, ?); ";

            PreparedStatement stmt = conAuxiliar.prepareStatement(sql);
            for (EspecialidadeBean especialidade : programa.getListaEspecialidadesEspecificas()) {
                stmt.setInt(1, programa.getIdPrograma());
                stmt.setInt(2, especialidade.getCodEspecialidade());
                stmt.executeUpdate();
            }
        } catch (SQLException sqle) {
            conAuxiliar.rollback();
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(),
                    sqle);
        } catch (Exception ex) {
            conAuxiliar.rollback();
            throw new ProjetoException(ex, this.getClass().getName());
        }
    }

    private void excluirProcedimentosPermitidos (Integer idPrograma, Connection conAuxiliar)
            throws ProjetoException, SQLException {

        try {
            String sql = "delete from hosp.programa_procedimento_permitido where id_programa = ?;";

            PreparedStatement stmt = conAuxiliar.prepareStatement(sql);
            stmt.setInt(1, idPrograma);
            stmt.executeUpdate();
        } catch (SQLException sqle) {
            conAuxiliar.rollback();
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
            conAuxiliar.rollback();
            throw new ProjetoException(ex, this.getClass().getName());
        }
    }

    private void inserirProcedimentosPermitidos (ProgramaBean programa, Connection conAuxiliar)
            throws ProjetoException, SQLException {

        try {
            String sql = "INSERT INTO hosp.programa_procedimento_permitido (id_programa, id_procedimento) VALUES(?, ?); ";

            PreparedStatement stmt = conAuxiliar.prepareStatement(sql);
            for (ProcedimentoBean procedimento : programa.getListaProcedimentosPermitidos()) {
                stmt.setInt(1, programa.getIdPrograma());
                stmt.setInt(2, procedimento.getIdProc());
                stmt.executeUpdate();
            }
        } catch (SQLException sqle) {
            conAuxiliar.rollback();
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(),
                    sqle);
        } catch (Exception ex) {
            conAuxiliar.rollback();
            throw new ProjetoException(ex, this.getClass().getName());
        }
    }

    private void excluirCidsPermitidos (Integer idPrograma, Connection conAuxiliar)
            throws ProjetoException, SQLException {

        try {
            String sql = "delete from hosp.programa_cid where id_programa = ?;";

            PreparedStatement stmt = conAuxiliar.prepareStatement(sql);
            stmt.setInt(1, idPrograma);
            stmt.executeUpdate();
        } catch (SQLException sqle) {
            conAuxiliar.rollback();
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
            conAuxiliar.rollback();
            throw new ProjetoException(ex, this.getClass().getName());
        }
    }

    private void inserirCidsPermitidos (ProgramaBean programa, Connection conAuxiliar)
            throws ProjetoException, SQLException {

        try {
            String sql = "INSERT INTO hosp.programa_cid (id_programa, id_cid) VALUES(?, ?); ";

            PreparedStatement stmt = conAuxiliar.prepareStatement(sql);
            for (CidBean cid : programa.getListaCidsPermitidos()) {
                stmt.setInt(1, programa.getIdPrograma());
                stmt.setInt(2, cid.getIdCid());
                stmt.executeUpdate();
            }
        } catch (SQLException sqle) {
            conAuxiliar.rollback();
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
            conAuxiliar.rollback();
            throw new ProjetoException(ex, this.getClass().getName());
        }
    }

    private List<ProcedimentoCboEspecificoDTO> listarProcedimentosIhCbosEspecificos(Integer idPrograma, Connection conAuxiliar)
            throws SQLException, ProjetoException {

        List<ProcedimentoCboEspecificoDTO> lista = new ArrayList<>();
        FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
                .getSessionMap().get("obj_funcionario");

        String sql = "select pr.id id_procedimento, pr.nome nome_procedimento, pr.codproc, " +
                "c.id id_cbo, c.descricao nome_cbo, c.codigo " +
                "	from hosp.programa_procedimento_cbo_especifico ppc " +
                "	join hosp.programa p on ppc.id_programa = p.id_programa " +
                "	join hosp.proc pr on ppc.id_procedimento = pr.id " +
                "	join hosp.cbo c on ppc.id_cbo = c.id " +
                "	where p.id_programa = ? and p.cod_unidade = ? "+
                "   and pr.ativo = 'S' ;";
        try {
            PreparedStatement stm = conAuxiliar.prepareStatement(sql);
            stm.setInt(1, idPrograma);
            stm.setInt(2, user_session.getUnidade().getId());
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                ProcedimentoCboEspecificoDTO procedimentoCboEspecificoDTO = new ProcedimentoCboEspecificoDTO();
                procedimentoCboEspecificoDTO.getProcedimento().setIdProc(rs.getInt("id_procedimento"));
                procedimentoCboEspecificoDTO.getProcedimento().setNomeProc(rs.getString("nome_procedimento"));
                procedimentoCboEspecificoDTO.getProcedimento().setCodProc(rs.getString("codproc"));
                procedimentoCboEspecificoDTO.getCbo().setCodCbo(rs.getInt("id_cbo"));
                procedimentoCboEspecificoDTO.getCbo().setDescCbo(rs.getString("nome_cbo"));
                procedimentoCboEspecificoDTO.getCbo().setCodigo(rs.getString("codigo"));

                lista.add(procedimentoCboEspecificoDTO);
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


    private List<ProcedimentoIdadeEspecificaDTO> listarProcedimentosIdadeEspecifica(Integer idPrograma, Connection conAuxiliar)
            throws SQLException, ProjetoException {

        List<ProcedimentoIdadeEspecificaDTO> lista = new ArrayList<>();
        FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
                .getSessionMap().get("obj_funcionario");

        String sql = "select pr.id id_procedimento, pr.nome nome_procedimento, pr.codproc, ppie.idade_minima, ppie.idade_maxima " +
                "	from hosp.programa_procedimento_idade_especifica ppie " +
                "	join hosp.programa p on ppie.id_programa = p.id_programa " +
                "	join hosp.proc pr on ppie.id_procedimento = pr.id " +
                "	where p.id_programa = ? and p.cod_unidade = ? and pr.ativo = 'S' ;";
        try {
            PreparedStatement stm = conAuxiliar.prepareStatement(sql);
            stm.setInt(1, idPrograma);
            stm.setInt(2, user_session.getUnidade().getId());
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                ProcedimentoIdadeEspecificaDTO procedimentoIdadeEspecificaDTO = new ProcedimentoIdadeEspecificaDTO();
                procedimentoIdadeEspecificaDTO.getProcedimento().setIdProc(rs.getInt("id_procedimento"));
                procedimentoIdadeEspecificaDTO.getProcedimento().setNomeProc(rs.getString("nome_procedimento"));
                procedimentoIdadeEspecificaDTO.getProcedimento().setCodProc(rs.getString("codproc"));
                procedimentoIdadeEspecificaDTO.setIdadeMinima(rs.getInt("idade_minima"));
                procedimentoIdadeEspecificaDTO.setIdadeMaxima(rs.getInt("idade_maxima"));

                lista.add(procedimentoIdadeEspecificaDTO);
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

    private List<EspecialidadeBean> listarEspecialidadePrograma(Integer idPrograma, Connection conAuxiliar)
            throws SQLException, ProjetoException {

        List<EspecialidadeBean> lista = new ArrayList<>();
        FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
                .getSessionMap().get("obj_funcionario");

        String sql = "select e.id_especialidade, e.descespecialidade from hosp.programa_especialidade pe " +
                "join hosp.programa p on pe.id_programa = p.id_programa " +
                "join hosp.especialidade e on pe.id_especialidade = e.id_especialidade " +
                "where p.id_programa = ? and p.cod_unidade = ?; ";
        try {
            PreparedStatement stm = conAuxiliar.prepareStatement(sql);
            stm.setInt(1, idPrograma);
            stm.setInt(2, user_session.getUnidade().getId());
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                EspecialidadeBean especialidade = new EspecialidadeBean();
                especialidade.setCodEspecialidade(rs.getInt("id_especialidade"));
                especialidade.setDescEspecialidade(rs.getString("descespecialidade"));

                lista.add(especialidade);
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

    private List<ProcedimentoBean> listarProcedimentosPermitidos(Integer idPrograma, Connection conAuxiliar)
            throws SQLException, ProjetoException {
        FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
                .getSessionMap().get("obj_funcionario");
        List<ProcedimentoBean> lista = new ArrayList<>();

        String sql = "select proc.id, proc.nome, proc.codproc " +
                "from hosp.programa_procedimento_permitido ppp " +
                "join hosp.programa p on ppp.id_programa = p.id_programa " +
                "join hosp.proc on ppp.id_procedimento = proc.id " +
                "where ppp.id_programa = ? and p.cod_unidade = ? and proc.ativo = 'S' ;";
        try {
            PreparedStatement stm = conAuxiliar.prepareStatement(sql);
            stm.setInt(1, idPrograma);
            stm.setInt(2, user_session.getUnidade().getId());
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                ProcedimentoBean procedimento = new ProcedimentoBean();
                mapearResultSetProcedimento(rs, procedimento);
                lista.add(procedimento);
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

    private List<CidBean> listarCidsPermitidos(Integer idPrograma, Connection conAuxiliar)
            throws SQLException, ProjetoException {
        FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
                .getSessionMap().get("obj_funcionario");
        List<CidBean> lista = new ArrayList<>();

        String sql = "select c.cod, c.desccidabrev, c.cid from hosp.programa_cid pc " +
                "join hosp.programa p on pc.id_programa = p.id_programa " +
                "join hosp.cid c on pc.id_cid = c.cod " +
                "where pc.id_programa = ? and p.cod_unidade = ?";
        try {
            PreparedStatement stm = conAuxiliar.prepareStatement(sql);
            stm.setInt(1, idPrograma);
            stm.setInt(2, user_session.getUnidade().getId());
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                CidBean cid = new CidBean();
                cid.setIdCid(rs.getInt("cod"));
                cid.setDescCidAbrev(rs.getString("desccidabrev"));
                cid.setCid(rs.getString("cid"));
                lista.add(cid);
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

    public ArrayList<FuncionarioBean> listarProfissionaisInsercaoSemlaudo(Integer idPrograma, Integer idGrupo) throws ProjetoException {
        ArrayList<FuncionarioBean> lista = new ArrayList<>();

        String sql = "select distinct e.medico, f.descfuncionario, f.codespecialidade, es.descespecialidade, f.codprocedimentopadrao " +
                " from hosp.equipe_medico e left join acl.funcionarios f on (e.medico = f.id_funcionario) " +
                " left join hosp.especialidade es on (f.codespecialidade = es.id_especialidade) " +
                " join hosp.programa_especialidade pe on (es.id_especialidade = pe.id_especialidade) " +
                " join hosp.programa p on (pe.id_programa = p.id_programa) " +
                " join hosp.grupo_programa gp on (p.id_programa = gp.codprograma) " +
                " join hosp.grupo g on (gp.codgrupo = g.id_grupo) " +
                " where codunidade = ? and pe.id_programa = ? and g.id_grupo = ?" +
                " order by f.descfuncionario ";

        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);

            stm.setInt(1, user_session.getUnidade().getId());
            stm.setInt(2, idPrograma);
            stm.setInt(3, idGrupo);

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
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(),
                    sqle);
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

    public List<ProcedimentoBean> listarProcedimentosPermitidos (Integer idPrograma)
            throws ProjetoException {

        List<ProcedimentoBean> lista = new ArrayList<>();

        String sql = "select proc.id, proc.nome, proc.codproc " +
                "from hosp.programa_procedimento_permitido ppp " +
                "join hosp.programa p on ppp.id_programa = p.id_programa " +
                "join hosp.proc on ppp.id_procedimento = proc.id " +
                "where ppp.id_programa = ? and p.cod_unidade = ? and proc.ativo = 'S' ;";
        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setInt(1, idPrograma);
            stm.setInt(2, user_session.getUnidade().getId());
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                ProcedimentoBean procedimento = new ProcedimentoBean();
                mapearResultSetProcedimento(rs, procedimento);
                lista.add(procedimento);
            }

        } catch (SQLException sqle) {
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
            throw new ProjetoException(ex, this.getClass().getName());
        }finally {
            try {
                con.close();
            } catch (Exception e) {
                //comentado walter erro log ex.printStackTrace();
            }
        }
        return lista;
    }

    public List<CidBean> listarCidsPermitidos(Integer idPrograma)
            throws ProjetoException {

        FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
                .getSessionMap().get("obj_funcionario");

        List<CidBean> lista = new ArrayList<>();
        String sql = "select c.cid, c.desccidabrev, c.cod from hosp.cid c " +
                "join hosp.programa_cid pc on (c.cod = pc.id_cid) " +
                "join hosp.programa p on (pc.id_programa = p.id_programa) " +
                "where pc.id_programa = ? and p.cod_unidade = ?";

        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setInt(1, idPrograma);
            stm.setInt(2, user_session.getUnidade().getId());

            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                CidBean cid = new CidBean();
                cid.setIdCid(rs.getInt("cod"));
                cid.setDescCidAbrev(rs.getString("desccidabrev"));
                cid.setCid(rs.getString("cid"));

                lista.add(cid);
            }
        } catch (Exception ex) {
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

    public ProcedimentoBean retornaProcedimentoPadraoDoGrupoNoPrograma (ProgramaBean programa, GrupoBean grupo)
            throws ProjetoException, SQLException {
        ProcedimentoBean procedimento = null;
        try {
            con = ConnectionFactory.getConnection();
            String sql = "select id_procedimento_padrao from  hosp.grupo_programa gp where gp.codprograma =? and gp.codgrupo=?";;

            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, programa.getIdPrograma());
            stmt.setInt(2, grupo.getIdGrupo());
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                procedimento = new ProcedimentoBean();
                procedimento.setIdProc(rs.getInt("id_procedimento_padrao"));
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
        return procedimento;
    }


    public ProcedimentoBean retornaProcedimentoPadraoDoProgramaPorIdade (Integer idPrograma, Integer idPaciente) throws ProjetoException, SQLException {
        ProcedimentoBean procedimento = new ProcedimentoBean();
        try {
            con = ConnectionFactory.getConnection();
            String sql = "SELECT id_procedimento FROM hosp.programa_procedimento_idade_especifica where id_programa = ? " +
                    "	and   (select extract (year from age(p.dtanascimento)) idade from hosp.pacientes p " +
                    "        			where p.id_paciente = ?) " +
                    "        		between idade_minima and idade_maxima;";

            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, idPrograma);
            stmt.setInt(2, idPaciente);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                procedimento = new ProcedimentoBean();
                procedimento.setIdProc(rs.getInt("id_procedimento"));
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
        return procedimento;
    }

    public ArrayList<ProgramaBean> buscaProgramasPorUnidade(List<UnidadeBean> listaUnidade) throws ProjetoException {
        PreparedStatement ps = null;
        con = ConnectionFactory.getConnection();

        String sql = "SELECT p.id_programa, p.descprograma, u.id id_unidade, u.nome FROM hosp.programa p "+
                "JOIN hosp.unidade u ON (p.cod_unidade = u.id ) where u.id = ? ";

        String filtroUnidade = " or u.id = ? ";
        String ordenacao = " order by descprograma";

        for (int i = 1; i < listaUnidade.size(); i++) {
            sql += filtroUnidade;
        }
        sql += ordenacao;

        ArrayList<ProgramaBean> lista = new ArrayList<>();
        try {
            ps = con.prepareStatement(sql);
            ps.setInt(1, listaUnidade.get(0).getId());
            for (int i = 1; i < listaUnidade.size(); i++) {
                ps.setInt(i+1, listaUnidade.get(i).getId());
            }

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                ProgramaBean programa = new ProgramaBean();
                programa.setIdPrograma(rs.getInt("id_programa"));
                programa.setDescPrograma(rs.getString("descprograma"));
                programa.setCodUnidade(rs.getInt("id_unidade"));
                programa.setDescricaoUnidade(rs.getString("nome"));
                lista.add(programa);
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

    public ProcedimentoBean listaProcedimentoEspecificoCboParaPrograma(Integer idPrograma, Long idFuncionario)
            throws SQLException, ProjetoException {

        FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
                .getSessionMap().get("obj_funcionario");

        String sql = "select pr.id id_procedimento, pr.nome nome_procedimento, pr.codproc " +
                "	from hosp.programa_procedimento_cbo_especifico ppc " +
                "	join hosp.programa p on ppc.id_programa = p.id_programa " +
                "	join hosp.proc pr on ppc.id_procedimento = pr.id " +
                "	join hosp.cbo c on ppc.id_cbo = c.id " +
                "	join hosp.cbo_funcionario cf on c.id = cf.id_cbo " +
                "	where p.id_programa = ? and p.cod_unidade = ? and cf.id_profissional =? " +
                "   and pr.ativo = 'S' ;";
        ProcedimentoBean procedimento = new ProcedimentoBean();
        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setInt(1, idPrograma);
            stm.setInt(2, user_session.getUnidade().getId());
            stm.setLong(3, idFuncionario);
            ResultSet rs = stm.executeQuery();

            if (rs.next()) {
                procedimento.setIdProc(rs.getInt("id_procedimento"));
                procedimento.setNomeProc(rs.getString("nome_procedimento"));
                procedimento.setCodProc(rs.getString("codproc"));
            }

        } catch (SQLException sqle) {
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
            throw new ProjetoException(ex, this.getClass().getName());
        } finally {
            try {
                con.close();
            } catch (Exception e) {
            }
        }
        return procedimento;
    }
    
    private ArrayList<ProcedimentoProfissionalEquipeEspecificoDTO>
    	listarProcedimentoProfissionalEquipeEspecifico(Integer idPrograma, Connection conexaoAuxiliar)
    			throws ProjetoException, SQLException {
        PreparedStatement ps = null;

        String sql = "select f.id_funcionario, f.descfuncionario, p.nome, p.id id_procedimento, p.codproc, e.id_equipe, e.descequipe \r\n" + 
        		"	from hosp.programa_procedimento_profissional_equipe pppe \r\n" + 
        		"	join hosp.programa prog on pppe.id_programa = prog.id_programa \r\n" + 
        		"	join hosp.proc p on pppe.id_procedimento = p.id \r\n" + 
        		"	join acl.funcionarios f on pppe.id_funcionario = f.id_funcionario \r\n" + 
        		"	join hosp.equipe e on pppe.id_equipe = e.id_equipe \r\n" + 
        		"	where pppe.id_programa = ?  order by f.descfuncionario;";

        ArrayList<ProcedimentoProfissionalEquipeEspecificoDTO> lista = new ArrayList<>();
        try {
            ps = conexaoAuxiliar.prepareStatement(sql);
            ps.setInt(1, idPrograma);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
            	ProcedimentoProfissionalEquipeEspecificoDTO dto 
            		= new ProcedimentoProfissionalEquipeEspecificoDTO();
            	dto.getProfissional().setId(rs.getLong("id_funcionario"));
            	dto.getProfissional().setNome(rs.getString("descfuncionario"));
            	dto.getProcedimento().setNomeProc(rs.getString("nome"));
            	dto.getProcedimento().setIdProc(rs.getInt("id_procedimento"));
            	dto.getProcedimento().setCodProc(rs.getString("codproc"));
            	dto.getEquipe().setCodEquipe(rs.getInt("id_equipe"));
            	dto.getEquipe().setDescEquipe(rs.getString("descequipe"));
                lista.add(dto);
            }
        } catch (SQLException sqle) {
        	conexaoAuxiliar.rollback();
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
        	conexaoAuxiliar.rollback();
            throw new ProjetoException(ex, this.getClass().getName());
        }
        return lista;
    }
    
	private void inserirProcedimentoProfissionalEquipeEspecifico(
			ProgramaBean programa, Connection conexaoAuxiliar) throws ProjetoException, SQLException {
		PreparedStatement ps = null;

		String sql = "INSERT INTO hosp.programa_procedimento_profissional_equipe " + 
				"(id_programa, id_procedimento, id_funcionario, id_equipe) " + 
				"VALUES(?, ?, ?, ?); ";

		try {
			ps = conexaoAuxiliar.prepareStatement(sql);
			for (ProcedimentoProfissionalEquipeEspecificoDTO dto : programa.getListaProcedimentoProfissionalEquipeEspecificaDTO()) {
				ps.setInt(1, programa.getIdPrograma());
				ps.setInt(2, dto.getProcedimento().getIdProc());
				ps.setLong(3, dto.getProfissional().getId());
				ps.setInt(4, dto.getEquipe().getCodEquipe());
				ps.executeUpdate();
			}

		} catch (SQLException sqle) {
			conexaoAuxiliar.rollback();
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(),
					sqle);
		} catch (Exception ex) {
			conexaoAuxiliar.rollback();
			throw new ProjetoException(ex, this.getClass().getName());
		}
	}
	
    private void excluirProcedimentoProfissionalEquipeEspecifico (Integer idPrograma, Connection conAuxiliar)
            throws ProjetoException, SQLException {

        try {
            String sql = "delete from hosp.programa_procedimento_profissional_equipe where id_programa = ?;";

            PreparedStatement stmt = conAuxiliar.prepareStatement(sql);
            stmt.setInt(1, idPrograma);
            stmt.executeUpdate();
        } catch (SQLException sqle) {
            conAuxiliar.rollback();
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
            conAuxiliar.rollback();
            throw new ProjetoException(ex, this.getClass().getName());
        }
    }
    
    public List<ProcedimentoBean> listarProcedimentosPermitidosIhPadrao (Integer idPrograma)
            throws ProjetoException {

        List<ProcedimentoBean> lista = new ArrayList<>();

        String sql = "\r\n" + 
        		"select id, nome, codproc from (\r\n" + 
        		"	select proc.id, proc.nome, proc.codproc \r\n" + 
        		"	from hosp.programa_procedimento_permitido ppp \r\n" + 
        		"	join hosp.programa p on ppp.id_programa = p.id_programa \r\n" + 
        		"	join hosp.proc on ppp.id_procedimento = proc.id \r\n" + 
        		"	where ppp.id_programa = ? and p.cod_unidade = ? and proc.ativo = 'S' \r\n" + 
        		"\r\n" + 
        		"	union \r\n" + 
        		"	select proc.id, proc.nome, proc.codproc \r\n" + 
        		"	from hosp.programa p \r\n" + 
        		"	join hosp.proc on p.cod_procedimento = proc.id \r\n" + 
        		"	where p.id_programa = ? and p.cod_unidade = ? and proc.ativo = 'S'\r\n" + 
        		") as a";
        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setInt(1, idPrograma);
            stm.setInt(2, user_session.getUnidade().getId());
            stm.setInt(3, idPrograma);
            stm.setInt(4, user_session.getUnidade().getId());
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                ProcedimentoBean procedimento = new ProcedimentoBean();
                mapearResultSetProcedimento(rs, procedimento);
                lista.add(procedimento);
            }

        } catch (SQLException sqle) {
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
            throw new ProjetoException(ex, this.getClass().getName());
        }finally {
            try {
                con.close();
            } catch (Exception e) {
                //comentado walter erro log ex.printStackTrace();
            }
        }
        return lista;
    }

	private void mapearResultSetProcedimento(ResultSet rs, ProcedimentoBean procedimento) throws SQLException {
		procedimento.setIdProc(rs.getInt("id"));
		procedimento.setNomeProc(rs.getString("nome"));
		procedimento.setCodProc(rs.getString("codproc"));
	}
}
