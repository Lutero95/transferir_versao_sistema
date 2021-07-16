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
import br.gov.al.maceio.sishosp.hosp.model.ProfissaoBean;

public class ProfissaoDAO {
    private Connection conexao = null;

    public Boolean cadastrar(ProfissaoBean profissao) throws ProjetoException {
        boolean retorno = false;

        String sql = "insert into hosp.profissao (descprofissao)"
                + " values (?)";

        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setString(1, profissao.getDescprofissao().toUpperCase().trim());

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

    public Boolean alterar(ProfissaoBean profissao) throws ProjetoException {
        boolean retorno = false;
        String sql = "update hosp.profissao set descprofissao = ? where id_profissao = ?";
        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setString(1, profissao.getDescprofissao());
            stmt.setInt(2, profissao.getCodprofissao());
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

    public Boolean excluir(ProfissaoBean profissao) throws ProjetoException {
        boolean retorno = false;
        String sql = "delete from hosp.profissao where id_profissao = ?";
        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setInt(1, profissao.getCodprofissao());
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

    public ArrayList<ProfissaoBean> listaProfissoes() throws ProjetoException {

        String sql = "select * from hosp.profissao order by descprofissao";

        ArrayList<ProfissaoBean> lista = new ArrayList();

        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stm = conexao.prepareStatement(sql);
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                ProfissaoBean p = new ProfissaoBean();

                p.setCodprofissao(rs.getInt("id_profissao"));
                p.setDescprofissao(rs.getString("descprofissao").toUpperCase());
                lista.add(p);
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

    public ProfissaoBean buscaprofissaocodigo(Integer id)
            throws ProjetoException {
        PreparedStatement ps = null;
        conexao = ConnectionFactory.getConnection();

        ProfissaoBean profissao = new ProfissaoBean();
        try {
            String sql = "select id_profissao, descprofissao from hosp.profissao where id_profissao=? order by descprofissao";
            ps = conexao.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                profissao.setCodprofissao(rs.getInt("id_profissao"));
                profissao.setDescprofissao(rs.getString("descprofissao"));
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
        return profissao;
    }

    public List<ProfissaoBean> buscaprofissao(String campoDeBusca) throws ProjetoException {
        PreparedStatement ps = null;
        conexao = ConnectionFactory.getConnection();

        List<ProfissaoBean> colecao = new ArrayList<ProfissaoBean>();
        try {
            String sql = "select id_profissao,id_profissao ||'-'|| descprofissao descprofissao from hosp.profissao "
                    + " where upper(id_profissao ||'-'|| descprofissao) like ? order by descprofissao";

            ps = conexao.prepareStatement(sql);
            ps.setString(1, "%" + campoDeBusca.toUpperCase() + "%");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                ProfissaoBean profissao = new ProfissaoBean();
                profissao.setCodprofissao(rs.getInt("id_profissao"));
                profissao.setDescprofissao(rs.getString("descprofissao"));
                colecao.add(profissao);
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
