package br.gov.al.maceio.sishosp.hosp.dao;

import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;
import br.gov.al.maceio.sishosp.comum.util.TratamentoErrosUtil;
import br.gov.al.maceio.sishosp.hosp.model.Parentesco;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ParentescoDAO {
    private Connection conexao = null;

    public Boolean cadastrar(Parentesco parentesco) throws ProjetoException {
        boolean retorno = false;

        String sql = "insert into hosp.parentesco (descparentesco, tipo) values (?, ?)";

        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setString(1, parentesco.getDescParentesco().toUpperCase().trim());
            stmt.setString(2, parentesco.getTipoParentesco());
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

    public Boolean alterar(Parentesco parentesco) throws ProjetoException {
        boolean retorno = false;
        String sql = "update hosp.parentesco set descparentesco = ?, tipo = ? where id_parentesco = ?";
        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setString(1, parentesco.getDescParentesco().toUpperCase());
            stmt.setString(2, parentesco.getTipoParentesco());
            stmt.setInt(3, parentesco.getCodParentesco());
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

    public Boolean excluir(Parentesco parentesco) throws ProjetoException {
        boolean retorno = false;
        String sql = "delete from hosp.parentesco where id_parentesco = ?";
        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setInt(1, parentesco.getCodParentesco());
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

    public ArrayList<Parentesco> listaParentescos() throws ProjetoException {

        String sql = "select id_parentesco, descparentesco, tipo from hosp.parentesco order by descparentesco";

        ArrayList<Parentesco> lista = new ArrayList();

        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stm = conexao.prepareStatement(sql);
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                Parentesco parentesco = new Parentesco();

                parentesco.setCodParentesco(rs.getInt("id_parentesco"));
                parentesco.setDescParentesco(rs.getString("descparentesco"));
                parentesco.setTipoParentesco(rs.getString("tipo"));
                lista.add(parentesco);
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

    public Parentesco buscaParentesCocodigo(Integer cod) throws ProjetoException {
        PreparedStatement ps = null;
        conexao = ConnectionFactory.getConnection();

        Parentesco parentesco = new Parentesco();
        try {
            String sql = "select id_parentesco, descparentesco, coalesce(tipo,'O') tipoparentesco from hosp.parentesco where id_parentesco=? order by descparentesco";

            ps = conexao.prepareStatement(sql);
            ps.setInt(1, cod);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                parentesco.setCodParentesco(rs.getInt("id_parentesco"));
                parentesco.setDescParentesco(rs.getString("descparentesco"));
                parentesco.setTipoParentesco(rs.getString("tipoparentesco"));
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
        return parentesco;
    }

    public List<Parentesco> buscaParentesco(String s) throws ProjetoException {
        PreparedStatement ps = null;
        conexao = ConnectionFactory.getConnection();
        List<Parentesco> colecao = new ArrayList<Parentesco>();

        try {
            String sql = "select id_parentesco, descparentesco, tipo from hosp.parentesco "
                    + " where upper(descparentesco) like ? order by descparentesco";

            ps = conexao.prepareStatement(sql);
            ps.setString(1, "%" + s.toUpperCase() + "%");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Parentesco parentesco = new Parentesco();
                parentesco.setCodParentesco(rs.getInt("id_parentesco"));
                parentesco.setDescParentesco(rs.getString("descparentesco"));
                parentesco.setTipoParentesco(rs.getString("tipo"));
                colecao.add(parentesco);
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
    
    public boolean existeParentescoCadastrado(Parentesco parentesco, boolean editarParentesco) throws ProjetoException {
        PreparedStatement ps = null;
        conexao = ConnectionFactory.getConnection();
        boolean existeParentescoCadastrado = true;
        try {
        	String sql = "";
        	if(!editarParentesco)
        		sql = "select exists (select p.id_parentesco from hosp.parentesco p where tipo = ?) existe_este_parentesco";
        	else
        		sql = "select exists (select p.id_parentesco from hosp.parentesco p "
        				+ "where tipo = ? and p.id_parentesco != ?) existe_este_parentesco";

            ps = conexao.prepareStatement(sql);
            ps.setString(1, parentesco.getTipoParentesco());
            if(editarParentesco)
            	ps.setInt(2, parentesco.getCodParentesco());
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
            	existeParentescoCadastrado = rs.getBoolean("existe_este_parentesco");
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
        return existeParentescoCadastrado;
    }
}
