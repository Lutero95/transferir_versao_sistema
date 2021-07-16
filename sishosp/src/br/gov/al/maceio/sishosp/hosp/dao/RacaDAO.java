package br.gov.al.maceio.sishosp.hosp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;
import br.gov.al.maceio.sishosp.comum.util.TratamentoErrosUtil;
import br.gov.al.maceio.sishosp.hosp.model.RacaBean;

public class RacaDAO {
    private Connection conexao = null;

    public Boolean cadastrar(RacaBean raca) throws ProjetoException {
        Boolean retorno = false;
        String sql = "insert into hosp.raca (descraca, codraca) values (?, ?)";

        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setString(1, raca.getDescRaca().toUpperCase().trim());
            stmt.setString(2, raca.getCodigoIbge().toUpperCase().trim());            
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
                //comentado walter erro log   ex.printStackTrace();
            }
        }
        return retorno;
    }

    public Boolean alterar(RacaBean raca) throws ProjetoException {
        Boolean retorno = false;
        String sql = "update hosp.raca set descraca = ?, codraca=? where id_raca = ?";

        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setString(1, raca.getDescRaca().toUpperCase());
            stmt.setString(2, raca.getCodigoIbge().toUpperCase());
            stmt.setInt(3, raca.getCodRaca());
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
//comentado walter erro log                ex.printStackTrace();
            }
        }
        return retorno;
    }

    public Boolean excluir(RacaBean raca) throws ProjetoException {
        Boolean retorno = false;
        String sql = "delete from hosp.raca where id_raca = ?";

        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setInt(1, raca.getCodRaca());
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
//comentado walter erro log                ex.printStackTrace();
            }
        }
        return retorno;
    }

    public ArrayList<RacaBean> listaCor() throws ProjetoException {

        String sql = "select  id_raca, descraca, codraca from hosp.raca order by descraca";

        ArrayList<RacaBean> lista = new ArrayList();

        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stm = conexao.prepareStatement(sql);
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                RacaBean raca = new RacaBean();
                raca.setCodRaca(rs.getInt("id_raca"));
                raca.setDescRaca(rs.getString("descraca").toUpperCase());
                raca.setCodigoIbge(rs.getString("codraca").toUpperCase());

                lista.add(raca);
            }
        } catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
            try {
                conexao.close();
            } catch (Exception ex) {
//comentado walter erro log                ex.printStackTrace();
            }
        }
        return lista;
    }


    public RacaBean listarRacaPorID(int id) throws ProjetoException {
        String sql = "select  id_raca, descraca, codraca from hosp.raca where id_raca = ? order by descraca";

        RacaBean raca = new RacaBean();
        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stm = conexao.prepareStatement(sql);
            stm.setInt(1, id);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                raca.setCodRaca(rs.getInt("id_raca"));
                raca.setDescRaca(rs.getString("descraca").toUpperCase());
                raca.setCodigoIbge(rs.getString("codraca").toUpperCase());
            }
        } catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
            try {
                conexao.close();
            } catch (Exception ex) {
//comentado walter erro log                ex.printStackTrace();
            }
        }
        return raca;
    }
}
