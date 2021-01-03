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
import br.gov.al.maceio.sishosp.hosp.model.CboBean;
import br.gov.al.maceio.sishosp.hosp.model.ConselhoBean;

public class ConselhoDAO {
    private Connection con;

	public List<ConselhoBean> listaConselho() throws ProjetoException {

        List<ConselhoBean> lista = new ArrayList<>();
        String sql = "SELECT id, descricao FROM hosp.conselho; ";

        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                ConselhoBean conselho = new ConselhoBean();
                mapearResultSetConselho(conselho, rs);
                lista.add(conselho);
            }
        } catch (SQLException ex2) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(ex2), this.getClass().getName(), ex2);
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
	
	public ConselhoBean buscaConselhoPorId(Integer id) throws ProjetoException {

		ConselhoBean conselho = new ConselhoBean();
        String sql = "SELECT id, descricao FROM hosp.conselho where id = ?; ";

        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                mapearResultSetConselho(conselho, rs);
                conselho.setListaCbos(listaCbosConselho(id, con));
            }
        } catch (SQLException ex2) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(ex2), this.getClass().getName(), ex2);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
            try {
                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return conselho;
    }

	public List<ConselhoBean> buscaConselhoFiltro(String valorBusca) throws ProjetoException {

        List<ConselhoBean> lista = new ArrayList<>();
        String sql = "SELECT id, descricao FROM hosp.conselho where descricao ilike ? ";

        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, "%"+valorBusca+"%");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                ConselhoBean conselho = new ConselhoBean();
                mapearResultSetConselho(conselho, rs);
                lista.add(conselho);
            }
        } catch (SQLException ex2) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(ex2), this.getClass().getName(), ex2);
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
	
	private List<CboBean> listaCbosConselho(Integer idConselho, Connection conexaoAuxilar) 
			throws ProjetoException, SQLException {

		List<CboBean> listaCbos = new ArrayList<>();
        String sql = "select cbo.id, cbo.descricao, cbo.codigo FROM hosp.conselho c " + 
        		"	join hosp.cbo_conselho cc on c.id = cc.id_conselho " + 
        		"	join hosp.cbo cbo on cc.id_cbo = cbo.id where cc.id_conselho = ?; ";

        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement ps = conexaoAuxilar.prepareStatement(sql);
            ps.setInt(1, idConselho);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
            	CboBean cbo = new CboBean();
            	cbo.setCodCbo(rs.getInt("id"));
            	cbo.setDescCbo(rs.getString("descricao"));
            	cbo.setCodigo(rs.getString("codigo"));
            	listaCbos.add(cbo);
            }
        } catch (SQLException ex2) {
        	conexaoAuxilar.rollback();
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(ex2), this.getClass().getName(), ex2);
		} catch (Exception ex) {
			conexaoAuxilar.rollback();
			throw new ProjetoException(ex, this.getClass().getName());
		}
        return listaCbos;
    }
	
	private void mapearResultSetConselho(ConselhoBean conselho, ResultSet rs) throws SQLException {
		conselho.setId(rs.getInt("id"));
		conselho.setDescricao(rs.getString("descricao"));
	}
	
	public boolean gravarConselho(ConselhoBean conselho) throws ProjetoException {

		boolean cadastrado = false;
        String sql = "INSERT INTO hosp.conselho (descricao) VALUES(?) returning id;";

        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, conselho.getDescricao());
            ResultSet rs = ps.executeQuery();
            if(rs.next())
            	conselho.setId(rs.getInt("id"));
            
            gravarCboConselho(conselho, con);

            con.commit();
            cadastrado = true;
        } catch (SQLException ex2) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(ex2), this.getClass().getName(), ex2);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
            try {
                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return cadastrado;
    }
	
	public boolean alterarConselho(ConselhoBean conselho) throws ProjetoException {

		boolean cadastrado = false;
        String sql = "UPDATE hosp.conselho SET descricao = ? WHERE id = ?;";

        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, conselho.getDescricao());
            ps.setInt(2, conselho.getId());
            ps.executeUpdate();
            
            removerCboConselho(conselho.getId(), con);
            gravarCboConselho(conselho, con);

            con.commit();
            cadastrado = true;
        } catch (SQLException ex2) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(ex2), this.getClass().getName(), ex2);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
            try {
                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return cadastrado;
    }
	
	private void gravarCboConselho(ConselhoBean conselho, Connection conexaoAuxiliar)
			throws ProjetoException, SQLException {

        String sql = "INSERT INTO hosp.cbo_conselho (id_cbo, id_conselho) VALUES(?, ?);";

        try {
            PreparedStatement ps = conexaoAuxiliar.prepareStatement(sql);
            for (CboBean cbo : conselho.getListaCbos()) {
				ps.setInt(1, cbo.getCodCbo());
				ps.setInt(2, conselho.getId());
				ps.executeUpdate();
			}
        } catch (SQLException ex2) {
        	conexaoAuxiliar.rollback();
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(ex2), this.getClass().getName(), ex2);
		} catch (Exception ex) {
			conexaoAuxiliar.rollback();
			throw new ProjetoException(ex, this.getClass().getName());
		}
    } 
	
	private void removerCboConselho(Integer idConselho, Connection conexaoAuxiliar)
			throws ProjetoException, SQLException {

        String sql = "DELETE FROM hosp.cbo_conselho WHERE id_conselho = ?;";

        try {
            PreparedStatement ps = conexaoAuxiliar.prepareStatement(sql);
            
			ps.setInt(1, idConselho);
			ps.executeUpdate();
        } catch (SQLException ex2) {
        	conexaoAuxiliar.rollback();
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(ex2), this.getClass().getName(), ex2);
		} catch (Exception ex) {
			conexaoAuxiliar.rollback();
			throw new ProjetoException(ex, this.getClass().getName());
		}
    }
	
	public boolean excluiConselho(ConselhoBean conselho) throws ProjetoException {

		boolean removido = false;
        String sql = "DELETE FROM hosp.conselho WHERE id = ?";

        try {
            con = ConnectionFactory.getConnection();
            removerCboConselho(conselho.getId(), con);
            
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, conselho.getId());
            ps.executeUpdate();
            

            con.commit();
            removido = true;
        } catch (SQLException ex2) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(ex2), this.getClass().getName(), ex2);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
            try {
                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return removido;
    }
	
}
