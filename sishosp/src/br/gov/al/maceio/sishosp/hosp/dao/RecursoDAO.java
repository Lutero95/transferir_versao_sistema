package br.gov.al.maceio.sishosp.hosp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;
import br.gov.al.maceio.sishosp.comum.util.TratamentoErrosUtil;
import br.gov.al.maceio.sishosp.hosp.model.RecursoBean;


public class RecursoDAO {
    private Connection conexao = null;



    public Boolean cadastrar(RecursoBean recurso) throws ProjetoException {

        boolean retorno = false;

        String sql = "insert into hosp.recurso (descrecurso) values (?)";

        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setString(1, recurso.getDescRecurso().toUpperCase().trim());
            stmt.execute();
            conexao.commit();
            retorno = true;
        } catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
            try {
                conexao.close();
            } catch (Exception ex) {
                //comentado walter erro log ex.printStackTrace();
            }
        }
        return retorno;
    }

    public Boolean alterar(RecursoBean recurso) throws ProjetoException {

        boolean retorno = false;

        String sql = "update hosp.recurso set descrecurso = ? where id = ?";

        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stmt = conexao.prepareStatement(sql);

            stmt.setString(1, recurso.getDescRecurso().toUpperCase());
            stmt.setInt(2, recurso.getIdRecurso());
            stmt.executeUpdate();

            conexao.commit();
            retorno = true;
        } catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
            try {
                conexao.close();
            } catch (Exception ex) {
                //comentado walter erro log ex.printStackTrace();
            }
        }
        return retorno;
    }

    public Boolean excluir(RecursoBean recurso) throws ProjetoException {

        boolean retorno = false;

        String sql = "delete from hosp.recurso where id = ?";

        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setInt(1, recurso.getIdRecurso());
            stmt.executeUpdate();

            conexao.commit();
            retorno = true;
        } catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
            try {
                conexao.close();
            } catch (Exception ex) {
                //comentado walter erro log ex.printStackTrace();
            }
        }
        return retorno;
    }

    public ArrayList<RecursoBean> listaRecursos() throws ProjetoException {

        String sql = "select id, descrecurso from hosp.recurso  order by descrecurso";

        ArrayList<RecursoBean> lista = new ArrayList();

        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stm = conexao.prepareStatement(sql);
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                RecursoBean recurso = new RecursoBean();
                recurso.setIdRecurso(rs.getInt("id"));
                recurso.setDescRecurso(rs.getString("descrecurso"));
                lista.add(recurso);
            }

        } catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
            try {
                conexao.close();
            } catch (Exception ex) {
                //comentado walter erro log ex.printStackTrace();
            }
        }
        return lista;
    }

    public ArrayList<RecursoBean> listaRecursosPorProcedimento(Integer id_proc) throws ProjetoException {

        String sql = "select r.id, r.descrecurso from hosp.recurso r left join hosp.proc_recurso p on (r.id = p.id_recurso) " +
                "where p.id_proc = ? order by descrecurso";

        ArrayList<RecursoBean> lista = new ArrayList();

        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stm = conexao.prepareStatement(sql);
            stm.setInt(1, id_proc);
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                RecursoBean recurso = new RecursoBean();
                recurso.setIdRecurso(rs.getInt("id"));
                recurso.setDescRecurso(rs.getString("descrecurso"));
                lista.add(recurso);
            }

        } catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
            try {
                conexao.close();
            } catch (Exception ex) {
                //comentado walter erro log ex.printStackTrace();
            }
        }
        return lista;
    }

    public RecursoBean buscaRecursoCodigo(Integer id) throws ProjetoException {
        PreparedStatement ps = null;
        conexao = ConnectionFactory.getConnection();

        RecursoBean recurso = new RecursoBean();
        try {
            String sql = "select id, descrecurso from hosp.recurso where id = ? order by descrecurso";

            ps = conexao.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                recurso.setIdRecurso(rs.getInt("id"));
                recurso.setDescRecurso(rs.getString("descrecurso"));
            }
        } catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
            try {
                conexao.close();
            } catch (Exception ex) {
                //comentado walter erro log ex.printStackTrace();
            }
        }
        return recurso;
    }

    public List<RecursoBean> buscaRecursoAutoComplete(String s)
            throws ProjetoException {
        PreparedStatement ps = null;
        conexao = ConnectionFactory.getConnection();

        List<RecursoBean> colecao = new ArrayList<RecursoBean>();
        try {
            String sql = "select id , descrecurso from hosp.recurso "
                    + " where upper(descrecurso) like ? order by descrecurso";

            ps = conexao.prepareStatement(sql);
            ps.setString(1, "%" + s.toUpperCase() + "%");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                RecursoBean recurso = new RecursoBean();
                recurso.setIdRecurso(rs.getInt("id"));
                recurso.setDescRecurso(rs.getString("descrecurso"));
                colecao.add(recurso);
            }
        } catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
            try {
                conexao.close();
            } catch (Exception ex) {
                //comentado walter erro log ex.printStackTrace();
            }
        }
        return colecao;
    }

}
