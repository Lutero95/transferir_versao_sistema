package br.gov.al.maceio.sishosp.hosp.dao;

import java.sql.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import br.gov.al.maceio.sishosp.acl.dao.FuncionarioDAO;
import br.gov.al.maceio.sishosp.acl.model.FuncionarioBean;
import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;
import br.gov.al.maceio.sishosp.hosp.model.AgendaBean;
import br.gov.al.maceio.sishosp.hosp.model.BloqueioBean;
import br.gov.al.maceio.sishosp.hosp.model.FeriadoBean;

import javax.faces.context.FacesContext;

public class AgendaDAO {
    Connection con = null;
    PreparedStatement ps = null;

    public boolean gravarAgenda(AgendaBean agenda,
                                List<AgendaBean> listaNovosAgendamentos) {

        FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
                .getSessionMap().get("obj_funcionario");

        Boolean retorno = false;
        String sql = "INSERT INTO hosp.atendimentos(codpaciente, codmedico, codprograma,"
                + " dtaatende, situacao, dtamarcacao, codtipoatendimento,"
                + " turno, codequipe, observacao, ativo, cod_empresa, codgrupo)"
                + " VALUES "
                + "(?, ?, ?, ?, ?,"
                + " ?, ?, ?, ?, ?,"
                + " ?, ?, ?) RETURNING id_atendimento;";
        try {
            con = ConnectionFactory.getConnection();

            for(int i=0; i<listaNovosAgendamentos.size(); i++) {
                ps = con.prepareStatement(sql);

                ps.setInt(1, agenda.getPaciente().getId_paciente());
                if (agenda.getProfissional().getId() != null) {
                    ps.setLong(2, agenda.getProfissional().getId());
                } else {
                    ps.setNull(2, Types.NULL);
                }
                ps.setInt(3, agenda.getPrograma().getIdPrograma());
                ps.setDate(4, new java.sql.Date(listaNovosAgendamentos.get(i).getDataAtendimento().getTime()));
                ps.setString(5, "A");
                ps.setDate(6, new java.sql.Date(new Date().getTime()));
                ps.setInt(7, agenda.getTipoAt().getIdTipo());
                ps.setString(8, agenda.getTurno().toUpperCase());
                if (agenda.getEquipe().getCodEquipe() != null) {
                    ps.setInt(9, agenda.getEquipe().getCodEquipe());
                } else {
                    ps.setNull(9, Types.NULL);
                }
                ps.setString(10, agenda.getObservacao().toUpperCase());
                ps.setString(11, "S");
                ps.setInt(12, user_session.getEmpresa().getCodEmpresa());
                ps.setInt(13, agenda.getGrupo().getIdGrupo());

                ResultSet rs = ps.executeQuery();

                int idAgend = 0;
                if (rs.next()) {
                    idAgend = rs.getInt("id_atendimento");
                }

                String sql2 = "INSERT INTO hosp.atendimentos1 (codprofissionalatendimento, id_atendimento, "
                        + " cbo) VALUES  (?, ?, ?)";
                ps = con.prepareStatement(sql2);
                if (agenda.getProfissional().getId() != null) {
                    ps.setLong(1, agenda.getProfissional().getId());
                    ps.setInt(2, idAgend);
                    ps.setInt(3, agenda.getProfissional().getCbo().getCodCbo());
                } else if (agenda.getEquipe().getCodEquipe() != null) {
                    for (FuncionarioBean prof : agenda.getEquipe()
                            .getProfissionais()) {
                        ps.setLong(1, prof.getId());
                        ps.setInt(2, idAgend);
                        ps.setInt(3, prof.getCbo().getCodCbo());
                    }
                }

                ps.executeUpdate();
            }
            con.commit();

            retorno = true;
        } catch (SQLException ex) {
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

    public boolean excluirAgendamento(AgendaBean agenda) {
        Boolean retorno = false;
        String sql = "delete from hosp.atendimentos where id_atendimento = ?";
        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setLong(1, agenda.getIdAgenda());
            stmt.execute();
            con.commit();
            excluirTabelaAgendamentos1(agenda);
            retorno = true;
        } catch (SQLException ex) {
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

    public void excluirTabelaAgendamentos1(AgendaBean agenda)
            throws ProjetoException {
        String sql = "delete from hosp.atendimentos1 where id_atendimento = ?";
        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setLong(1, agenda.getIdAgenda());
            stmt.execute();
            con.commit();
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        } finally {
            try {
                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public FeriadoBean verificarFeriado(Date dataAtendimento)
            throws ProjetoException {

        String sql = "select codferiado, descferiado, dataferiado from hosp.feriado where dataferiado = ?";
        FeriadoBean fer = null;
        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setDate(1, new java.sql.Date(dataAtendimento.getTime()));
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                fer = new FeriadoBean();
                fer.setCodFeriado(rs.getInt("codferiado"));
                fer.setDescFeriado(rs.getString("descferiado"));
                fer.setDataFeriado(rs.getDate("dataferiado"));
            }
            return fer;
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        } finally {
            try {
                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public List<BloqueioBean> verificarBloqueioProfissional(
            FuncionarioBean prof, Date dataAtendimento, String turno)
            throws ProjetoException {

        List<BloqueioBean> lista = new ArrayList<>();
        FuncionarioDAO pDao = new FuncionarioDAO();
        String sql = "select id_bloqueioagenda, codmedico, dataagenda, turno, descricao "
                + " from hosp.bloqueio_agenda "
                + " where codmedico = ? and  dataagenda = ? and turno = ? order by id_bloqueioagenda";
        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setLong(1, prof.getId());
            stm.setDate(2, new java.sql.Date(dataAtendimento.getTime()));
            stm.setString(3, turno.toUpperCase());
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                BloqueioBean bloqueio = new BloqueioBean();
                bloqueio.setIdBloqueio(rs.getInt("id_bloqueioagenda"));
                bloqueio.setProf(pDao.buscarProfissionalPorId(rs
                        .getInt("codmedico")));
                bloqueio.setDataInicio(rs.getDate("dataagenda"));
                bloqueio.setTurno(rs.getString("turno"));
                bloqueio.setDescBloqueio(rs.getString("descricao"));

                lista.add(bloqueio);
            }
        } catch (SQLException ex) {
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

    public int verQtdMaxAgendaData(AgendaBean agenda) throws ProjetoException {
        int qtdMax = 0;
        String sqlPro = "select qtdmax from hosp.config_agenda where codmedico = ? and dataagenda = ? and turno = ?";
        String sqlEqui = "select qtdmax from hosp.config_agenda_equipe where codequipe = ? and dataagenda = ? and turno = ?";
        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = null;
            if (agenda.getProfissional().getId() != null) {
                stm = con.prepareStatement(sqlPro);
                stm.setLong(1, agenda.getProfissional().getId());
            } else if (agenda.getEquipe().getCodEquipe() != null) {
                stm = con.prepareStatement(sqlEqui);
                stm.setInt(1, agenda.getEquipe().getCodEquipe());
            }
            stm.setDate(2, new java.sql.Date(agenda.getDataAtendimento()
                    .getTime()));
            stm.setString(3, agenda.getTurno().toUpperCase());
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                qtdMax = rs.getInt("qtdmax");
            }
            return qtdMax;
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        } finally {
            try {
                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public int verQtdAgendadosData(AgendaBean agenda) throws ProjetoException {
        int qtd = 0;
        String sqlPro = "select count(*) as qtd from hosp.atendimentos where codmedico = ? and dtaatende = ? and turno = ?;";
        String sqlEqui = "select count(*) as qtd from hosp.atendimentos where codequipe = ? and dtaatende = ? and turno = ?;";
        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = null;
            if (agenda.getProfissional().getId() != null) {
                stm = con.prepareStatement(sqlPro);
                stm.setLong(1, agenda.getProfissional().getId());
            } else if (agenda.getEquipe().getCodEquipe() != null) {
                stm = con.prepareStatement(sqlEqui);
                stm.setInt(1, agenda.getEquipe().getCodEquipe());
            }
            stm.setDate(2, new java.sql.Date(agenda.getDataAtendimento()
                    .getTime()));
            stm.setString(3, agenda.getTurno().toUpperCase());
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                qtd = rs.getInt("qtd");
            }
            return qtd;
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        } finally {
            try {
                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public boolean buscarDataEspecifica(AgendaBean agenda)
            throws ProjetoException {
        int id = 0;
        String sqlPro = "select id_configagenda from hosp.config_agenda where codmedico = ? and dataagenda = ? and turno = ?";
        String sqlEqui = "select id_configagenda from hosp.config_agenda_equipe where codequipe = ? and dataagenda = ? and turno = ?";
        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = null;
            if (agenda.getProfissional().getId() != null) {
                stm = con.prepareStatement(sqlPro);
                stm.setLong(1, agenda.getProfissional().getId());
            } else if (agenda.getEquipe().getCodEquipe() != null) {
                stm = con.prepareStatement(sqlEqui);
                stm.setInt(1, agenda.getEquipe().getCodEquipe());
            }
            stm.setDate(2, new java.sql.Date(agenda.getDataAtendimento()
                    .getTime()));
            stm.setString(3, agenda.getTurno().toUpperCase());
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                id = rs.getInt("id_configagenda");
            }
            if (id == 0) {
                return false;
            } else
                return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        } finally {
            try {
                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public boolean buscarTabTipoAtendAgenda(AgendaBean agenda)
            throws ProjetoException {
        int achou = 0;
        String sql = "select codtipoatendimento from hosp.tipo_atend_agenda "
                + " where codtipoatendimento = ? and codprograma = ? and codgrupo = ?";
        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setInt(1, agenda.getTipoAt().getIdTipo());
            stm.setInt(2, agenda.getPrograma().getIdPrograma());
            stm.setInt(3, agenda.getGrupo().getIdGrupo());
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                achou = rs.getInt("codtipoatendimento");
            }
            if (achou == 0) {
                return false;
            } else
                return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        } finally {
            try {
                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public boolean buscarDiaSemana(AgendaBean agenda) throws ProjetoException {
        Calendar cal = Calendar.getInstance();
        cal.setTime(agenda.getDataAtendimento());
        int diaSemana = cal.get(Calendar.DAY_OF_WEEK);
        int mes = cal.get(Calendar.MONTH);
        int id = 0;
        String sqlPro = "select id_configagenda from hosp.config_agenda where codmedico = ? and diasemana = ? and turno = ? and mes = ?";
        String sqlEqui = "select id_configagenda from hosp.config_agenda_equipe where codequipe = ? and diasemana = ? and turno = ? and mes = ?";
        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = null;
            if (agenda.getProfissional().getId() != null) {
                stm = con.prepareStatement(sqlPro);
                stm.setLong(1, agenda.getProfissional().getId());
            } else if (agenda.getEquipe().getCodEquipe() != null) {
                stm = con.prepareStatement(sqlEqui);
                stm.setInt(1, agenda.getEquipe().getCodEquipe());
            }
            stm.setInt(2, diaSemana);
            stm.setString(3, agenda.getTurno().toUpperCase());
            stm.setInt(4, mes + 1);
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                id = rs.getInt("id_configagenda");
            }
            if (id == 0) {
                return false;
            } else
                return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        } finally {
            try {
                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public List<AgendaBean> listarAgendamentosData(AgendaBean ag)
            throws ProjetoException {
        List<AgendaBean> lista = new ArrayList<AgendaBean>();

        StringBuilder sqlProf = new StringBuilder();

        sqlProf.append("SELECT a.id_atendimento, a.codpaciente, a.codmedico, a.codprograma, ");
        sqlProf.append("a.codconvenio, a.dtaatende, a.horaatende, a.situacao, a.codatendente, ");
        sqlProf.append("a.dtamarcacao, a.codtipoatendimento, a.turno, a.codequipe, a.observacao, a.ativo, ");
        sqlProf.append("f.descfuncionario, p.nome, t.desctipoatendimento, e.descequipe ");
        sqlProf.append("FROM hosp.atendimentos a ");
        sqlProf.append("LEFT JOIN acl.funcionarios f ON (f.id_funcionario = a.codmedico) ");
        sqlProf.append("LEFT JOIN hosp.pacientes p ON (p.id_paciente = a.codpaciente) ");
        sqlProf.append("LEFT JOIN hosp.tipoatendimento t ON (t.id = a.codtipoatendimento) ");
        sqlProf.append("LEFT JOIN hosp.equipe e ON (e.id_equipe = a.codequipe) ");
        sqlProf.append("WHERE a.dtaatende = ? AND a.codmedico = ? AND a.turno = ?");

        StringBuilder sqlEqui = new StringBuilder();

        sqlEqui.append("SELECT a.id_atendimento, a.codpaciente, a.codmedico, a.codprograma, ");
        sqlEqui.append("a.codconvenio, a.dtaatende, a.horaatende, a.situacao, a.codatendente, ");
        sqlEqui.append("a.dtamarcacao, a.codtipoatendimento, a.turno, a.codequipe, a.observacao, a.ativo, ");
        sqlEqui.append("f.descfuncionario, p.nome, t.desctipoatendimento, e.descequipe ");
        sqlEqui.append("FROM hosp.atendimentos a ");
        sqlEqui.append("LEFT JOIN acl.funcionarios f ON (f.id_funcionario = a.codmedico) ");
        sqlEqui.append("LEFT JOIN hosp.pacientes p ON (p.id_paciente = a.codpaciente) ");
        sqlEqui.append("LEFT JOIN hosp.tipoatendimento t ON (t.id = a.codtipoatendimento) ");
        sqlEqui.append("LEFT JOIN hosp.equipe e ON (e.id_equipe = a.codequipe) ");
        sqlEqui.append("WHERE dtaatende = ? AND codequipe = ? AND turno = ?");

        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = null;

            if (ag.getProfissional().getId() != null) {
                stm = con.prepareStatement(sqlProf.toString());
                stm.setLong(2, ag.getProfissional().getId());
            } else if (ag.getEquipe().getCodEquipe() != null) {
                stm = con.prepareStatement(sqlEqui.toString());
                stm.setInt(2, ag.getEquipe().getCodEquipe());
            }

            stm.setDate(1, new java.sql.Date(ag.getDataAtendimento().getTime()));
            stm.setString(3, ag.getTurno());
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                AgendaBean agenda = new AgendaBean();
                agenda.setIdAgenda(rs.getInt("id_atendimento"));
                agenda.getPaciente().setId_paciente(rs
                        .getInt("codpaciente"));
                agenda.getPaciente().setNome(rs.getString("nome"));
                agenda.getProfissional().setId(rs
                        .getLong("codmedico"));
                agenda.getProfissional().setNome(rs.getString("descfuncionario"));
                agenda.setDataAtendimento(rs.getDate("dtaatende"));
                agenda.setSituacao(rs.getString("situacao"));
                agenda.setDataMarcacao(rs.getDate("dtamarcacao"));
                agenda.getTipoAt().setIdTipo(rs.getInt("codtipoatendimento"));
                agenda.getTipoAt().setDescTipoAt(rs.getString("desctipoatendimento"));
                agenda.setTurno(rs.getString("turno"));
                agenda.getEquipe().setCodEquipe(rs.getInt("codequipe"));
                agenda.getEquipe().setDescEquipe(rs.getString("descequipe"));
                agenda.setObservacao(rs.getString("observacao"));
                agenda.setAtivo(rs.getString("ativo"));
                lista.add(agenda);
            }
        } catch (SQLException ex) {
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

    public List<AgendaBean> consultarAgenda(Date dataAgenda,
                                            Date dataAgendaFinal) throws ProjetoException {
        List<AgendaBean> lista = new ArrayList<AgendaBean>();

        String sql = "SELECT a.id_atendimento, a.codpaciente, p.nome, p.cns, a.codmedico, m.descfuncionario, "
                + " a.dtaatende, a.dtamarcacao, a.codtipoatendimento, t.desctipoatendimento, a.turno, "
                + " a.codequipe, e.descequipe "
                + " FROM  hosp.atendimentos a "
                + " left join hosp.atendimentos1 a1 on (a.id_atendimento = a1.id_atendimento)"
                + " left join hosp.pacientes p on (p.id_paciente = a.codpaciente) "
                + " left join acl.funcionarios m on (m.id_funcionario = a.codmedico) "
                + " left join hosp.equipe e on (e.id_equipe = a.codequipe) "
                + " left join hosp.tipoatendimento t on (t.id = a.codtipoatendimento)";
        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = null;
            stm = con.prepareStatement(sql);

            if (dataAgenda != null) {
                sql += " where a.dtamarcacao >= ? and a.dtamarcacao <= ?";
                stm = con.prepareStatement(sql);
                stm.setDate(1, new java.sql.Date(dataAgenda.getTime()));
                stm.setDate(2, new java.sql.Date(dataAgendaFinal.getTime()));
            }

            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                AgendaBean agenda = new AgendaBean();
                agenda.setIdAgenda(rs.getInt("id_atendimento"));
                agenda.getPaciente().setId_paciente(rs.getInt("codpaciente"));
                agenda.getPaciente().setNome(rs.getString("nome"));
                agenda.getPaciente().setCns(rs.getString("cns"));
                agenda.getProfissional().setId(rs.getLong("codmedico"));
                agenda.getProfissional().setNome(
                        rs.getString("descfuncionario"));
                agenda.setDataAtendimento(rs.getDate("dtaatende"));
                agenda.setDataMarcacao(rs.getDate("dtamarcacao"));
                agenda.getTipoAt().setIdTipo(rs.getInt("codtipoatendimento"));
                agenda.getTipoAt().setDescTipoAt(
                        rs.getString("desctipoatendimento"));
                agenda.setTurno(rs.getString("turno"));
                agenda.getEquipe().setCodEquipe(rs.getInt("codequipe"));
                agenda.getEquipe().setDescEquipe(rs.getString("descequipe"));

                lista.add(agenda);
            }
        } catch (SQLException ex) {
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

    public int verQtdMaxAgendaEspec(AgendaBean agenda) throws ProjetoException {
        Calendar cal = Calendar.getInstance();
        cal.setTime(agenda.getDataAtendimento());
        int diaSemana = cal.get(Calendar.DAY_OF_WEEK);
        int qtdMax = 0;
        String sqlPro = "select qtdmax from hosp.config_agenda where codmedico = ? and diasemana = ? and turno = ?";
        String sqlEqui = "select qtdmax from hosp.config_agenda_equipe where codequipe = ? and diasemana = ? and turno = ?";
        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = null;
            if (agenda.getProfissional().getId() != null) {
                stm = con.prepareStatement(sqlPro);
                stm.setLong(1, agenda.getProfissional().getId());
            } else if (agenda.getEquipe().getCodEquipe() != null) {
                stm = con.prepareStatement(sqlEqui);
                stm.setInt(1, agenda.getEquipe().getCodEquipe());
            }
            stm.setInt(2, diaSemana);
            stm.setString(3, agenda.getTurno().toUpperCase());
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                qtdMax = rs.getInt("qtdmax");
            }
            return qtdMax;
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        } finally {
            try {
                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public int verQtdAgendadosEspec(AgendaBean agenda) throws ProjetoException {
        int qtd = 0;
        String sqlPro = "select count(*) as qtd from hosp.atendimentos where codmedico = ? and dtaatende = ? and turno = ?;";
        String sqlEqui = "select count(*) as qtd from hosp.atendimentos where codequipe = ? and dtaatende = ? and turno = ?;";
        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = null;
            if (agenda.getProfissional().getId() != null) {
                stm = con.prepareStatement(sqlPro);
                stm.setLong(1, agenda.getProfissional().getId());
            } else if (agenda.getEquipe().getCodEquipe() != null) {
                stm = con.prepareStatement(sqlEqui);
                stm.setInt(1, agenda.getEquipe().getCodEquipe());
            }
            stm.setDate(2, new java.sql.Date(agenda.getDataAtendimento()
                    .getTime()));
            stm.setString(3, agenda.getTurno().toUpperCase());
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                qtd = rs.getInt("qtd");
            }
            return qtd;
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        } finally {
            try {
                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public Boolean retornarIntervaloUltimoAgendamento(Integer codPaciente, Integer codTipoAtendimento, Integer intervaloMinimo)
            throws ProjetoException {

        Boolean resultado = false;

        String sql = "SELECT CASE WHEN dtamarcacao - CURRENT_DATE > concat(?,' minutes')::INTERVAL THEN TRUE ELSE FALSE END AS intervalo " +
                "FROM hosp.atendimentos " +
                "WHERE codpaciente = ? AND codtipoatendimento = ? " +
                "ORDER BY id_atendimento DESC limit 1 ";

        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setInt(1, intervaloMinimo);
            stm.setInt(2, codPaciente);
            stm.setInt(3, codTipoAtendimento);
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
               resultado = rs.getBoolean("intervalo");
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        } finally {
            try {
                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return resultado;
    }

}
