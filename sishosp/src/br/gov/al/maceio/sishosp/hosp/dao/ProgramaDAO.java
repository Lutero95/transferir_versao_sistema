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
import br.gov.al.maceio.sishosp.comum.util.TratamentoErrosUtil;
import br.gov.al.maceio.sishosp.hosp.model.ProgramaBean;
import br.gov.al.maceio.sishosp.hosp.model.UnidadeBean;
import br.gov.al.maceio.sishosp.hosp.model.dto.ProcedimentoCboEspecificoDTO;

public class ProgramaDAO {

    Connection con = null;

    FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
            .getSessionMap().get("obj_funcionario");

    public boolean gravarPrograma(ProgramaBean prog) throws ProjetoException {

        Boolean retorno = false;
        PreparedStatement ps = null;

        String sql = "insert into hosp.programa (descprograma, cod_unidade, cod_procedimento) values (?, ?, ?) RETURNING id_programa;";
        try {
            con = ConnectionFactory.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, prog.getDescPrograma().toUpperCase());
            ps.setInt(2, user_session.getUnidade().getId());
            ps.setInt(3, prog.getProcedimento().getIdProc());
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                prog.setIdPrograma(rs.getInt("id_programa"));
            }

            String sql2 = "insert into hosp.grupo_programa (codprograma, codgrupo, qtdfrequencia) values(?, ?, ?);";
            PreparedStatement ps2 = con.prepareStatement(sql2);

            if (prog.getGrupo().size() > 0) {
                for (int i = 0; i < prog.getGrupo().size(); i++) {
                    ps2.setInt(1, prog.getIdPrograma());
                    ps2.setInt(2, prog.getGrupo().get(i).getIdGrupo());
                    ps2.setInt(3, prog.getGrupo().get(i).getQtdFrequencia());
                    ps2.execute();
                }
            }
            
            inserirProcedimentosIhCbosEspecificos(prog, con);
            
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
        String sql = "update hosp.programa set descprograma = ?, cod_procedimento = ? where id_programa = ?";

        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, prog.getDescPrograma().toUpperCase());
            stmt.setInt(2, prog.getProcedimento().getIdProc());
            stmt.setInt(3, prog.getIdPrograma());
            stmt.executeUpdate();

            String sql2 = "delete from hosp.grupo_programa where codprograma = ?";
            PreparedStatement stmt2 = con.prepareStatement(sql2);
            stmt2.setLong(1, prog.getIdPrograma());
            stmt2.execute();

            String sql3 = "insert into hosp.grupo_programa (codprograma, codgrupo, qtdfrequencia) values(?,?,?);";
            PreparedStatement stmt3 = con.prepareStatement(sql3);

            if (prog.getGrupo().size() > 0) {
                for (int i = 0; i < prog.getGrupo().size(); i++) {
                    stmt3.setInt(1, prog.getIdPrograma());
                    stmt3.setInt(2, prog.getGrupo().get(i).getIdGrupo());
                    stmt3.setInt(3, prog.getGrupo().get(i).getQtdFrequencia());
                    stmt3.execute();
                }
            }
            
            excluirProcedimentosIhCbosEspecificos(prog.getIdPrograma(), con);
            inserirProcedimentosIhCbosEspecificos(prog, con);

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

    public ArrayList<ProgramaBean> BuscalistarProgramas()
            throws ProjetoException {
        PreparedStatement ps = null;
        con = ConnectionFactory.getConnection();


        String sql = "select id_programa, descprograma, cod_procedimento from hosp.programa "
                + "join hosp.profissional_programa_grupo on programa.id_programa = profissional_programa_grupo.codprograma "
                + "where programa.cod_unidade = ? "
                + "order by descprograma";
        GrupoDAO gDao = new GrupoDAO();
        ArrayList<ProgramaBean> lista = new ArrayList();
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
        String sql = "select distinct id_programa,id_programa ||'-'|| descprograma as descprograma, cod_procedimento  from hosp.programa "
                + "left join hosp.profissional_programa_grupo on programa.id_programa = profissional_programa_grupo.codprograma "
                + "where codprofissional = ? and programa.cod_unidade=?";

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

    public List<ProgramaBean> listarProgramasUsuario() throws ProjetoException {
        List<ProgramaBean> lista = new ArrayList<>();
        String sql = "select distinct id_programa,id_programa ||'-'|| descprograma as descprograma, cod_procedimento,  proc.nome descproc  from hosp.programa "
                + "left join hosp.profissional_programa_grupo on programa.id_programa = profissional_programa_grupo.codprograma left join hosp.proc on proc.id = programa.cod_procedimento "
                + "where codprofissional = ? and programa.cod_unidade=? order by descprograma";

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
        String sql = "select id_programa, descprograma, cod_procedimento from hosp.programa where id_programa = ? order by descprograma";
        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setInt(1, id);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                programa = new ProgramaBean();
                programa.setIdPrograma(rs.getInt("id_programa"));
                programa.setDescPrograma(rs.getString("descprograma"));
                programa.setProcedimento(new ProcedimentoDAO().listarProcedimentoPorIdComConexao(rs.getInt("cod_procedimento"), con));
                programa.setGrupo(gDao.listarGruposPorProgramaComConexao(rs.getInt("id_programa"), con));
                programa.setListaProcedimentoCboEspecificoDTO(listarProcedimentosIhCbosEspecificos(rs.getInt("id_programa"), con));
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
        String sql = "select id_programa, descprograma, cod_procedimento,  proc.nome descproc from hosp.programa join hosp.proc on proc.id = programa.cod_procedimento where programa.id_programa = ? order by descprograma";
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
        String sql = "select id_programa, descprograma, cod_procedimento from hosp.programa where id_programa = ? order by descprograma";
        try {
            PreparedStatement stm = conAuxiliar.prepareStatement(sql);
            stm.setInt(1, id);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                programa = new ProgramaBean();
                programa.setIdPrograma(rs.getInt("id_programa"));
                programa.setDescPrograma(rs.getString("descprograma"));
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
    
    public List<ProcedimentoCboEspecificoDTO> listarProcedimentosIhCbosEspecificos(Integer idPrograma, Connection conAuxiliar) throws SQLException, ProjetoException {
        FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
                .getSessionMap().get("obj_funcionario");
    	List<ProcedimentoCboEspecificoDTO> lista = new ArrayList<>();

        
        String sql = "select pr.id id_procedimento, pr.nome nome_procedimento, pr.codproc, " + 
        		"c.id id_cbo, c.descricao nome_cbo, c.codigo " + 
        		"	from hosp.programa_procedimento_cbo_especifico ppc " + 
        		"	join hosp.programa p on ppc.id_programa = p.id_programa " + 
        		"	join hosp.proc pr on ppc.id_procedimento = pr.id " + 
        		"	join hosp.cbo c on ppc.id_cbo = c.id " + 
        		"	where p.id_programa = ? and p.cod_unidade = ?;";
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

}
