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
import br.gov.al.maceio.sishosp.hosp.model.EscolaBean;

public class EscolaDAO {
    private Connection conexao = null;

    public Boolean cadastrar(EscolaBean escola) throws ProjetoException {
        boolean retorno = false;

        String sql = "insert into hosp.escola (descescola) values (?)";

        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setString(1, escola.getDescescola().toUpperCase().trim());

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
                ex.printStackTrace();
            }
        }
        return retorno;
    }

    public Boolean alterar(EscolaBean escola) throws ProjetoException {
        boolean retorno = false;
        String sql = "update hosp.escola set descescola = ? where id_escola = ?";
        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setString(1, escola.getDescescola().toUpperCase());
            stmt.setInt(2, escola.getCodEscola());
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
                ex.printStackTrace();
            }
        }
        return retorno;
    }

    public Boolean excluir(EscolaBean escola) throws ProjetoException {
        boolean retorno = false;
        String sql = "delete from hosp.escola where id_escola = ?";
        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setInt(1, escola.getCodEscola());
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
                ex.printStackTrace();
            }
        }
        return retorno;
    }

    public ArrayList<EscolaBean> listaEscolas() throws ProjetoException {

        String sql = "select id_escola, descescola, codtipoescola from hosp.escola order by descescola";

        ArrayList<EscolaBean> lista = new ArrayList();

        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stm = conexao.prepareStatement(sql);
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                EscolaBean escola = new EscolaBean();

                escola.setCodEscola(rs.getInt("id_escola"));
                escola.setDescescola(rs.getString("descescola"));
                escola.setCodtipoescola(rs.getInt("codtipoescola"));

                lista.add(escola);
            }
        } catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
            try {
                conexao.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return lista;
    }

    public EscolaBean buscaescolacodigo(Integer idEscola) throws ProjetoException {
        PreparedStatement ps = null;
        conexao = ConnectionFactory.getConnection();

        EscolaBean escola = new EscolaBean();
        try {
            String sql = "select id_escola, descescola from hosp.escola where id_escola=? order by descescola";

            ps = conexao.prepareStatement(sql);
            ps.setInt(1, idEscola);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                escola.setCodEscola(rs.getInt("id_escola"));
                escola.setDescescola(rs.getString("descescola"));
            }
        } catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
            try {
                conexao.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return escola;
    }

    public List<EscolaBean> buscaescola(String s) throws ProjetoException {
        PreparedStatement ps = null;
        conexao = ConnectionFactory.getConnection();

        List<EscolaBean> colecao = new ArrayList<EscolaBean>();
        try {
            String sql = "select id_escola,id_escola ||'-'|| descescola descescola from hosp.escola "
                    + " where upper(id_escola ||'-'|| descescola) like ? order by descescola";

            ps = conexao.prepareStatement(sql);
            ps.setString(1, "%" + s.toUpperCase() + "%");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                EscolaBean escola = new EscolaBean();
                escola.setCodEscola(rs.getInt("id_escola"));
                escola.setDescescola(rs.getString("descescola"));
                colecao.add(escola);
            }
        } catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
            try {
                conexao.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return colecao;
    }

    public ArrayList<EscolaBean> listaTipoEscola() throws ProjetoException {

        String sql = "select * from hosp.tipoescola order by desctipoescola";

        ArrayList<EscolaBean> lista = new ArrayList();

        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stm = conexao.prepareStatement(sql);
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                EscolaBean escola = new EscolaBean();
                escola.setCodtipoescola(rs.getInt("codtipoescola"));
                escola.setDesctipoescola(rs.getString("desctipoescola").toUpperCase());
                lista.add(escola);
            }
        } catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
            try {
                conexao.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return lista;
    }
}
