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
import br.gov.al.maceio.sishosp.hosp.model.FormaTransporteBean;

public class FormaTransporteDAO {
    private Connection conexao = null;

    public Boolean cadastrar(FormaTransporteBean transporte) throws ProjetoException {
        boolean retorno = false;

        String sql = "insert into hosp.formatransporte (descformatransporte)"
                + " values (?)";
        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setString(1, transporte.getDescformatransporte().toUpperCase()
                    .trim());

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

    public Boolean alterar(FormaTransporteBean transporte) throws ProjetoException {
        boolean retorno = false;
        String sql = "update hosp.formatransporte set descformatransporte = ? where id_formatransporte = ?";
        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setString(1, transporte.getDescformatransporte().toUpperCase());
            stmt.setInt(2, transporte.getCodformatransporte());
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

    public Boolean excluir(FormaTransporteBean transporte) throws ProjetoException {
        boolean retorno = false;
        String sql = "delete from hosp.formatransporte where id_formatransporte = ?";
        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setInt(1, transporte.getCodformatransporte());
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

    public ArrayList<FormaTransporteBean> listaTransportes()
            throws ProjetoException {

        String sql = "select * from hosp.formatransporte order by descformatransporte";

        ArrayList<FormaTransporteBean> lista = new ArrayList();

        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stm = conexao.prepareStatement(sql);
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                FormaTransporteBean formaTransporte = new FormaTransporteBean();

                formaTransporte.setCodformatransporte(rs.getInt("id_formatransporte"));
                formaTransporte.setDescformatransporte(rs.getString("descformatransporte").toUpperCase());

                lista.add(formaTransporte);
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

    public FormaTransporteBean buscatransportecodigo(Integer i)
            throws ProjetoException {
        PreparedStatement ps = null;
        conexao = ConnectionFactory.getConnection();

        FormaTransporteBean transporte = new FormaTransporteBean();
        try {
            String sql = "select id_formatransporte, descformatransporte from hosp.formatransporte where id_formatransporte=? order by descformatransporte";
            ps = conexao.prepareStatement(sql);
            ps.setInt(1, i);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                transporte.setCodformatransporte(rs.getInt("id_formatransporte"));
                transporte.setDescformatransporte(rs.getString("descformatransporte"));
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
        return transporte;
    }

    public List<FormaTransporteBean> buscatransporte(String s)
            throws ProjetoException {
        PreparedStatement ps = null;
        conexao = ConnectionFactory.getConnection();

        List<FormaTransporteBean> colecao = new ArrayList<FormaTransporteBean>();
        try {
            String sql = "select id_formatransporte,id_formatransporte ||'-'|| descformatransporte descformatransporte from hosp.formatransporte "
                    + " where upper(id_formatransporte ||'-'|| descformatransporte) like ? order by descformatransporte";

            ps = conexao.prepareStatement(sql);
            ps.setString(1, "%" + s.toUpperCase() + "%");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                FormaTransporteBean transporte = new FormaTransporteBean();
                transporte.setCodformatransporte(rs
                        .getInt("id_formatransporte"));
                transporte.setDescformatransporte(rs
                        .getString("descformatransporte"));
                colecao.add(transporte);
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

}
