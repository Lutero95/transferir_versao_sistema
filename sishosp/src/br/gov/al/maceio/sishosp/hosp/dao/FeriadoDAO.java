package br.gov.al.maceio.sishosp.hosp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;
import br.gov.al.maceio.sishosp.comum.util.DataUtil;
import br.gov.al.maceio.sishosp.hosp.enums.TipoDataAgenda;
import br.gov.al.maceio.sishosp.hosp.model.FeriadoBean;

public class FeriadoDAO {

    Connection con = null;
    PreparedStatement ps = null;

    public Boolean gravarFeriado(FeriadoBean feriado) {
        Boolean retorno = false;
        String sql = "insert into hosp.feriado (descferiado, dataferiado) values (?, ?);";
        try {
            con = ConnectionFactory.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, feriado.getDescFeriado().toUpperCase());
            ps.setDate(2, new java.sql.Date(feriado.getDataFeriado().getTime()));
            ps.execute();
            con.commit();
            retorno = true;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        } finally {
            try {
                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return retorno;
        }
    }

    public Boolean alterarFeriado(FeriadoBean feriado) {
        Boolean retorno = false;
        String sql = "update hosp.feriado set descferiado = ?, dataferiado = ? where codferiado = ?";
        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, feriado.getDescFeriado().toUpperCase());
            stmt.setDate(2, new java.sql.Date(feriado.getDataFeriado()
                    .getTime()));
            stmt.setInt(3, feriado.getCodFeriado());
            stmt.executeUpdate();
            con.commit();
            retorno = true;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        } finally {
            try {
                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return retorno;
        }
    }

    public Boolean excluirFeriado(FeriadoBean feriado) {
        Boolean retorno = false;
        String sql = "delete from hosp.feriado where codferiado = ?";
        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setLong(1, feriado.getCodFeriado());
            stmt.execute();
            con.commit();
            retorno = true;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        } finally {
            try {
                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return retorno;
        }
    }

    public List<FeriadoBean> listarFeriado() throws ProjetoException {
        List<FeriadoBean> lista = new ArrayList<>();
        String sql = "SELECT codferiado, descferiado, dataferiado FROM hosp.feriado ORDER BY dataferiado DESC";
        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                FeriadoBean feriado = new FeriadoBean();
                feriado.setCodFeriado(rs.getInt("codferiado"));
                feriado.setDescFeriado(rs.getString("descferiado"));
                feriado.setDataFeriado(rs.getDate("dataferiado"));

                lista.add(feriado);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
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

    public FeriadoBean listarFeriadoPorId(int id) throws ProjetoException {

        FeriadoBean feriado = new FeriadoBean();
        String sql = "select codferiado, descferiado, dataferiado from hosp.feriado where codferiado = ? ORDER BY dataferiado DESC";
        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setInt(1, id);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                feriado = new FeriadoBean();
                feriado.setCodFeriado(rs.getInt("codferiado"));
                feriado.setDescFeriado(rs.getString("descferiado"));
                feriado.setDataFeriado(rs.getDate("dataferiado"));
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        } finally {
            try {
                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return feriado;
    }

    public Boolean verificarSeEhFeriadoDataUnica(Date dataAtendimento)
            throws ProjetoException {

        String sql = "";

        sql = "SELECT codferiado FROM hosp.feriado WHERE dataferiado = ?";

        Boolean retorno = false;

        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);

            stm.setDate(1, DataUtil.converterDateUtilParaDateSql(dataAtendimento));

            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                retorno = true;
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        } finally {
            try {
                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return retorno;
    }

    public List<Date> verificarSeEhFeriadoIntervaloDeDatas(Date dataAtendimento, Date dataAtendimentoFinal)
            throws ProjetoException {

        String sql = "SELECT dataferiado FROM hosp.feriado WHERE dataferiado >= ? AND dataferiado <= ?";

        List<Date> lista = new ArrayList<>();

        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);

            stm.setDate(1, DataUtil.converterDateUtilParaDateSql(dataAtendimento));
            stm.setDate(2, DataUtil.converterDateUtilParaDateSql(dataAtendimentoFinal));


            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                Date data = rs.getDate("dataferiado");
                lista.add(data);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
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

    public Date verificarFeriadoDeData(Date data, Connection conAuxiliar) {

        String sql = "SELECT dataferiado FROM hosp.feriado WHERE dataferiado = ? ";

        Date dataSemBloqueio = null;

        try {
            PreparedStatement stm = conAuxiliar.prepareStatement(sql);
            stm.setDate(1, DataUtil.converterDateUtilParaDateSql(data));

            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                dataSemBloqueio = rs.getDate("dataferiado");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        } finally {
            try {
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return dataSemBloqueio;
    }

}
