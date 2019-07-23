package br.gov.al.maceio.sishosp.hosp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import br.gov.al.maceio.sishosp.acl.dao.FuncionarioDAO;
import br.gov.al.maceio.sishosp.acl.model.FuncionarioBean;
import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;
import br.gov.al.maceio.sishosp.comum.util.DataUtil;
import br.gov.al.maceio.sishosp.hosp.enums.TipoDataAgenda;
import br.gov.al.maceio.sishosp.hosp.enums.Turno;
import br.gov.al.maceio.sishosp.hosp.model.AgendaBean;
import br.gov.al.maceio.sishosp.hosp.model.BloqueioBean;

import javax.faces.context.FacesContext;
import javax.xml.crypto.Data;

public class BloqueioDAO {
    Connection con = null;
    PreparedStatement ps = null;
    FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
            .getSessionMap().get("obj_funcionario");

    public Boolean gravarBloqueioInicio(BloqueioBean bloqueio) throws ProjetoException {
        Boolean retorno = false;

        try {
            con = ConnectionFactory.getConnection();

            if (bloqueio.getTurno().equals(Turno.AMBOS.getSigla())) {
                retorno = gravarBloqueio(bloqueio, con, Turno.MANHA.getSigla());
                if (retorno) {
                    retorno = gravarBloqueio(bloqueio, con, Turno.TARDE.getSigla());
                }
            } else {
                retorno = gravarBloqueio(bloqueio, con, bloqueio.getTurno());
            }

            if(retorno) {
                con.commit();
            }

        } catch (
                SQLException ex) {
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

    public Boolean gravarBloqueio(BloqueioBean bloqueio, Connection conAuxiliar, String turno) {

        Calendar calendarData = Calendar.getInstance();
        Boolean condicao = true;
        String sql = "insert into hosp.bloqueio_agenda (codmedico, dataagenda, turno, descricao, cod_empresa) values (?, ?, ?, ?, ?);";

        try {
            ps = conAuxiliar.prepareStatement(sql);
            Date dataInicio = bloqueio.getDataInicio();
            Date dataFim = bloqueio.getDataFim();
            do {
                if (dataInicio.after(dataFim) == false) {
                    ps.setLong(1, bloqueio.getProf().getId());
                    ps.setDate(2, new java.sql.Date(dataInicio.getTime()));
                    ps.setString(3, turno.toUpperCase());
                    ps.setString(4, bloqueio.getDescBloqueio().toUpperCase());
                    ps.setInt(5, user_session.getEmpresa().getCodEmpresa());
                    ps.execute();
                    condicao = true;
                    calendarData.setTime(dataInicio);
                    calendarData.add(Calendar.DATE, 1);
                    dataInicio = calendarData.getTime();
                } else {
                    condicao = false;
                }
            } while (condicao);

            condicao = true;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        } finally {
            try {
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return condicao;
        }
    }

    public Boolean alterarBloqueio(BloqueioBean bloqueio) {

        Boolean retorno = false;
        String sql = "update hosp.bloqueio_agenda set codmedico = ?, dataagenda = ?,turno = ?, descricao = ?"
                + " where id_bloqueioagenda = ?";

        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stmt = con.prepareStatement(sql);

            stmt.setLong(1, bloqueio.getProf().getId());
            stmt.setDate(2, new java.sql.Date(bloqueio.getDataInicio()
                    .getTime()));
            stmt.setString(3, bloqueio.getTurno().toUpperCase());
            stmt.setString(4, bloqueio.getDescBloqueio().toUpperCase());
            stmt.setInt(5, bloqueio.getIdBloqueio());
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

    public Boolean excluirBloqueio(BloqueioBean bloqueio) {

        Boolean retorno = false;
        String sql = "delete from hosp.bloqueio_agenda where id_bloqueioagenda = ?";

        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setLong(1, bloqueio.getIdBloqueio());
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

    public List<BloqueioBean> listarBloqueio() throws ProjetoException {
        List<BloqueioBean> lista = new ArrayList<>();
        String sql = "select b.id_bloqueioagenda, b.codmedico, m.descfuncionario, b.dataagenda, b.turno, b.descricao "
                + " from hosp.bloqueio_agenda b left join acl.funcionarios m on (b.codmedico = m.id_funcionario) " +
                " where b.cod_empresa = ? order by b.id_bloqueioagenda";
        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setInt(1, user_session.getEmpresa().getCodEmpresa());
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                BloqueioBean bloqueio = new BloqueioBean();
                bloqueio.setIdBloqueio(rs.getInt("id_bloqueioagenda"));
                bloqueio.getProf().setId(rs.getLong("codmedico"));
                bloqueio.getProf().setNome(rs.getString("descfuncionario"));
                bloqueio.setDataInicio(rs.getDate("dataagenda"));
                bloqueio.setTurno(rs.getString("turno"));
                bloqueio.setDescBloqueio(rs.getString("descricao"));
                lista.add(bloqueio);
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

    public BloqueioBean listarBloqueioPorId(int id) throws ProjetoException {

        BloqueioBean bloqueio = new BloqueioBean();
        String sql = "select b.id_bloqueioagenda, b.descricao, b.dataagenda, b.codmedico, m.descfuncionario "
                + " from hosp.bloqueio_agenda b left join acl.funcionarios m on (b.codmedico = m.id_funcionario) where b.id_bloqueioagenda = ?";
        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setInt(1, id);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                bloqueio = new BloqueioBean();
                bloqueio.setIdBloqueio(rs.getInt("id_bloqueioagenda"));
                bloqueio.setDescBloqueio(rs.getString("descricao"));
                bloqueio.setDataInicio(rs.getDate("dataagenda"));
                bloqueio.getProf().setId(rs.getLong("codmedico"));
                bloqueio.getProf().setNome(rs.getString("descfuncionario"));
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
        return bloqueio;
    }

    public Boolean verificarBloqueioProfissionalDataUnica(
            Long codProfissional, Date dataAtendimento, String turno)
            throws ProjetoException {

        Boolean retorno = false;

        String sql = "SELECT b.id_bloqueioagenda, b.codmedico, b.dataagenda, b.turno, b.descricao, " +
                "f.descfuncionario, f.cpf, f.cns, f.codprocedimentopadrao " +
                "FROM hosp.bloqueio_agenda b " +
                "LEFT JOIN acl.funcionarios f ON (b.codmedico = f.id_funcionario) " +
                "WHERE codmedico = ? AND turno = ? AND dataagenda = ?";

        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setLong(1, codProfissional);
            stm.setString(2, turno.toUpperCase());
            stm.setDate(3, new java.sql.Date(dataAtendimento.getTime()));

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

    public List<Date> verificarBloqueioProfissionalIntervaloDeDatas(
            Long codProfissional, Date dataAtendimento, Date dataAtendimentoFinal, String turno)
            throws ProjetoException {

        Boolean retorno = false;
        List<Date> lista = new ArrayList<>();

        String sql = "SELECT dataagenda " +
                "FROM hosp.bloqueio_agenda b " +
                "LEFT JOIN acl.funcionarios f ON (b.codmedico = f.id_funcionario) " +
                "WHERE codmedico = ? AND turno = ? AND dataagenda >= ? AND dataagenda <= ?";

        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setLong(1, codProfissional);
            stm.setString(2, turno.toUpperCase());
            stm.setDate(3, DataUtil.converterDateUtilParaDateSql(dataAtendimento));
            stm.setDate(4, DataUtil.converterDateUtilParaDateSql(dataAtendimentoFinal));

            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                Date data = rs.getDate("dataagenda");
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

}
