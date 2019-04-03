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
import br.gov.al.maceio.sishosp.comum.util.DataUtil;
import br.gov.al.maceio.sishosp.hosp.abstracts.VetorDiaSemanaAbstract;
import br.gov.al.maceio.sishosp.hosp.enums.TipoDataAgenda;
import br.gov.al.maceio.sishosp.hosp.model.*;

import javax.faces.context.FacesContext;

public class AgendaDAO extends VetorDiaSemanaAbstract {
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

            for (int i = 0; i < listaNovosAgendamentos.size(); i++) {
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
                ps.setInt(12, agenda.getEmpresa().getCodEmpresa());
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

    public int verQtdMaxAgendaData(AgendaBean agenda) throws ProjetoException {
        int qtdMax = 0;
        String sqlPro = "select p.qtdmax " +
                "from hosp.config_agenda_profissional p " +
                "left join hosp.config_agenda_profissional_dias d on (p.id_configagenda = d.id_config_agenda_profissional) " +
                "where p.codmedico = ? and d.data_especifica = ? and d.turno = ?;";

        String sqlEqui = "select e.qtdmax " +
                "from hosp.config_agenda_equipe e " +
                "left join hosp.config_agenda_equipe_dias d on (e.id_configagenda = d.id_config_agenda_equipe) " +
                "where e.codequipe = ? and d.data_especifica = ? and d.turno = 'M';";

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

        String sqlPro = "select p.id_configagenda " +
                "from hosp.config_agenda_profissional p " +
                "left join hosp.config_agenda_profissional_dias d on (p.id_configagenda = d.id_config_agenda_profissional) " +
                "where p.codmedico = ? and d.data_especifica = ? and d.turno = ?;";

        String sqlEqui = "select e.id_configagenda " +
                "from hosp.config_agenda_equipe e " +
                "left join hosp.config_agenda_equipe_dias d on (e.id_configagenda = d.id_config_agenda_equipe) " +
                "where e.codequipe = ? and d.data_especifica = ? and d.turno = ?;";

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

        String sqlPro = "select p.id_configagenda " +
                "from hosp.config_agenda_profissional p " +
                "left join hosp.config_agenda_profissional_dias d on (p.id_configagenda = d.id_config_agenda_profissional) " +
                "where p.codmedico = ? and d.dia = ? and d.turno = ? and p.mes = ?;";

        String sqlEqui = "select e.id_configagenda " +
                "from hosp.config_agenda_equipe e " +
                "left join hosp.config_agenda_equipe_dias d on (e.id_configagenda = d.id_config_agenda_equipe) " +
                "where e.codequipe = ? and d.dia = ? and d.turno = ? and e.mes = ?;";

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
                                            Date dataAgendaFinal, Integer codEmpresa) throws ProjetoException {
        List<AgendaBean> lista = new ArrayList<AgendaBean>();

        String sql = "SELECT a.id_atendimento, a.codpaciente, p.nome, p.cns, a.codmedico, m.descfuncionario, "
                + " a.dtaatende, a.dtamarcacao, a.codtipoatendimento, t.desctipoatendimento, a.turno, "
                + " a.codequipe, e.descequipe "
                + " FROM  hosp.atendimentos a "
                + " LEFT JOIN hosp.atendimentos1 a1 ON (a.id_atendimento = a1.id_atendimento)"
                + " LEFT JOIN hosp.pacientes p ON (p.id_paciente = a.codpaciente) "
                + " LEFT JOIN acl.funcionarios m ON (m.id_funcionario = a.codmedico) "
                + " LEFT JOIN hosp.equipe e ON (e.id_equipe = a.codequipe) "
                + " LEFT JOIN hosp.tipoatendimento t ON (t.id = a.codtipoatendimento) "
                + " WHERE a.cod_empresa = ? AND a.dtaatende >= ? AND a.dtaatende <= ?";

        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = null;
            stm = con.prepareStatement(sql);

            stm.setInt(1, codEmpresa);
            stm.setDate(2, new java.sql.Date(dataAgenda.getTime()));
            stm.setDate(3, new java.sql.Date(dataAgendaFinal.getTime()));


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

    public int verQtdMaxAgendaGeral(AgendaBean agenda) throws ProjetoException {
        Calendar cal = Calendar.getInstance();
        cal.setTime(agenda.getDataAtendimento());
        int diaSemana = cal.get(Calendar.DAY_OF_WEEK);
        int qtdMax = 0;

        String sqlPro = "select p.qtdmax " +
                "from hosp.config_agenda_profissional p " +
                "left join hosp.config_agenda_profissional_dias d on (p.id_configagenda = d.id_config_agenda_profissional) " +
                "where p.codmedico = ? and d.dia = ? and d.turno = ?;";

        String sqlEqui = "select e.qtdmax " +
                "from hosp.config_agenda_equipe e " +
                "left join hosp.config_agenda_equipe_dias d on (e.id_configagenda = d.id_config_agenda_equipe) " +
                "where e.codequipe = ? and d.dia = ? and d.turno = ?;";

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

    public int verQtdMaxAgendaEspec(AgendaBean agenda) throws ProjetoException {

        int diaSemana = DataUtil.extrairDiaDeData(agenda.getDataAtendimento());
        int mes = DataUtil.extrairMesDeData(agenda.getDataAtendimento());
        int ano = DataUtil.extrairAnoDeData(agenda.getDataAtendimento());
        int qtdMax = 0;

        String sqlPro = "select p.qtdmax " +
                "from hosp.config_agenda_profissional p " +
                "left join hosp.config_agenda_profissional_dias d on (p.id_configagenda = d.id_config_agenda_profissional) " +
                "where p.codmedico = ? and d.dia = ? and d.turno = ? AND p.mes = ? AND p.ano = ?;";

        String sqlEqui = "select e.qtdmax " +
                "from hosp.config_agenda_equipe e " +
                "left join hosp.config_agenda_equipe_dias d on (e.id_configagenda = d.id_config_agenda_equipe) " +
                "where e.codequipe = ? and d.dia = ? and d.turno = 'M' AND e.mes = ? AND e.ano = ?;";

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
            stm.setInt(4, mes);
            stm.setInt(5, ano);

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

        Boolean resultado = true;

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

    public List<ConfigAgendaParte1Bean> retornarDiaAtendimentoProfissional(Long codProfissional) throws ProjetoException {
        List<ConfigAgendaParte1Bean> lista = new ArrayList<ConfigAgendaParte1Bean>();

        StringBuilder sql = new StringBuilder();

        sql.append("SELECT f.descfuncionario, ");
        sql.append("CASE WHEN d.turno = 'M' THEN 'Manhã' ");
        sql.append("WHEN d.turno = 'T' THEN 'Tarde' END AS turno, ");
        sql.append("CASE WHEN d.dia = 1 THEN 'Domingo' ");
        sql.append("WHEN d.dia = 2 THEN 'Segunda' ");
        sql.append("WHEN d.dia = 3 THEN 'Terça' ");
        sql.append("WHEN d.dia = 4 THEN 'Quarta' ");
        sql.append("WHEN d.dia = 5 THEN 'Quinta' ");
        sql.append("WHEN d.dia = 6 THEN 'Sexta' ");
        sql.append("WHEN d.dia = 7 THEN 'Sábado' END AS dia, ");
        sql.append("NULL AS data_especifica ");
        sql.append("FROM hosp.config_agenda_profissional c ");
        sql.append("LEFT JOIN hosp.config_agenda_profissional_dias d ON (c.id_configagenda = d.id_config_agenda_profissional) ");
        sql.append("LEFT JOIN acl.funcionarios f ON (c.codmedico = f.id_funcionario) ");
        sql.append("WHERE c.codmedico = ? AND ");
        sql.append("c.mes = (SELECT DATE_PART('MONTH', CURRENT_TIMESTAMP)) AND c.ano = (SELECT DATE_PART('YEAR', CURRENT_TIMESTAMP)) ");
        sql.append("UNION ");
        sql.append("SELECT ff.descfuncionario, ");
        sql.append("CASE WHEN dd.turno = 'M' THEN 'Manhã' ");
        sql.append("WHEN dd.turno = 'T' THEN 'Tarde' END AS turno, ");
        sql.append("NULL AS dia, dd.data_especifica ");
        sql.append("FROM hosp.config_agenda_profissional cc ");
        sql.append("LEFT JOIN hosp.config_agenda_profissional_dias dd ON (cc.id_configagenda = dd.id_config_agenda_profissional) ");
        sql.append("LEFT JOIN acl.funcionarios ff ON (cc.codmedico = ff.id_funcionario) ");
        sql.append("WHERE cc.codmedico = ? AND ");
        sql.append("(SELECT DATE_PART('MONTH', dd.data_especifica)) = (SELECT DATE_PART('MONTH', CURRENT_TIMESTAMP)) ");
        sql.append("AND (SELECT DATE_PART('YEAR', dd.data_especifica)) = (SELECT DATE_PART('YEAR', CURRENT_TIMESTAMP)) ");
        sql.append("ORDER BY descfuncionario, dia, turno ");


        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = null;
            stm = con.prepareStatement(sql.toString());

            stm.setLong(1, codProfissional);
            stm.setLong(2, codProfissional);

            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                ConfigAgendaParte1Bean configAgendaParte1Bean = new ConfigAgendaParte1Bean();
                configAgendaParte1Bean.getProfissional().setNome(rs.getString("descfuncionario"));
                configAgendaParte1Bean.setTurno(rs.getString("turno"));
                configAgendaParte1Bean.setDiaSemana(rs.getString("dia"));
                configAgendaParte1Bean.setDataEspecifica(rs.getDate("data_especifica"));

                lista.add(configAgendaParte1Bean);
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

    public List<ConfigAgendaParte1Bean> retornarDiaAtendimentoEquipe(Integer codEquipe) throws ProjetoException {
        List<ConfigAgendaParte1Bean> lista = new ArrayList<ConfigAgendaParte1Bean>();

        StringBuilder sql = new StringBuilder();

        sql.append("SELECT e.descequipe, ");
        sql.append("CASE WHEN d.turno = 'M' THEN 'Manhã' ");
        sql.append("WHEN d.turno = 'T' THEN 'Tarde' END AS turno, ");
        sql.append("CASE WHEN d.dia = 1 THEN 'Domingo' ");
        sql.append("WHEN d.dia = 2 THEN 'Segunda' ");
        sql.append("WHEN d.dia = 3 THEN 'Terça' ");
        sql.append("WHEN d.dia = 4 THEN 'Quarta' ");
        sql.append("WHEN d.dia = 5 THEN 'Quinta' ");
        sql.append("WHEN d.dia = 6 THEN 'Sexta' ");
        sql.append("WHEN d.dia = 7 THEN 'Sábado' END AS dia ");
        sql.append("FROM hosp.config_agenda_equipe c ");
        sql.append("LEFT JOIN hosp.equipe e ON (c.codequipe = e.id_equipe) ");
        sql.append("LEFT JOIN hosp.config_agenda_equipe_dias d ON (c.id_configagenda = d.id_config_agenda_equipe) ");
        sql.append("WHERE e.id_equipe = ? AND  ");
        sql.append("c.mes = (SELECT DATE_PART('MONTH', CURRENT_TIMESTAMP)) AND c.ano = (SELECT DATE_PART('YEAR', CURRENT_TIMESTAMP)) ");
        sql.append("ORDER BY e.descequipe, d.dia, d.turno");


        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = null;
            stm = con.prepareStatement(sql.toString());

            stm.setInt(1, codEquipe);

            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                ConfigAgendaParte1Bean configAgendaParte1Bean = new ConfigAgendaParte1Bean();
                configAgendaParte1Bean.getEquipe().setDescEquipe(rs.getString("descequipe"));
                configAgendaParte1Bean.setTurno(rs.getString("turno"));
                configAgendaParte1Bean.setDiaSemana(rs.getString("dia"));

                lista.add(configAgendaParte1Bean);
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

    public Boolean numeroAtendimentosEquipe(InsercaoPacienteBean insercao) {

        Boolean resultado = false;

        FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
                .getSessionMap().get("obj_funcionario");

        StringBuilder sql = new StringBuilder();

        sql.append("SELECT ");
        sql.append("CASE WHEN (SELECT count(id_atendimento) FROM hosp.atendimentos WHERE ");
        sql.append("dtaatende = ? AND codequipe = ?) < p.qtd_simultanea_atendimento_equipe ");
        sql.append("THEN TRUE ELSE FALSE END AS pode_marcar ");
        sql.append("FROM hosp.parametro p WHERE p.cod_empresa = ?");

        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql.toString());
            stm.setDate(1, DataUtil.converterDateUtilParaDateSql(insercao.getData_solicitacao()));
            stm.setInt(2, insercao.getEquipe().getCodEquipe());
            stm.setInt(3, user_session.getEmpresa().getCodEmpresa());
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                resultado = rs.getBoolean("pode_marcar");
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        } catch (ProjetoException e) {
            e.printStackTrace();
        } finally {
            try {
                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return resultado;
    }

    public Boolean numeroAtendimentosProfissional(InsercaoPacienteBean insercao) {

        Boolean resultado = false;

        FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
                .getSessionMap().get("obj_funcionario");

        StringBuilder sql = new StringBuilder();

        sql.append("SELECT ");
        sql.append("CASE WHEN (SELECT count(id_atendimento) FROM hosp.atendimentos WHERE horario = ? ");
        sql.append("AND dtaatende = ? AND codmedico = ?) < p.qtd_simultanea_atendimento_profissional ");
        sql.append("THEN TRUE ELSE FALSE END AS pode_marcar ");
        sql.append("FROM hosp.parametro p WHERE p.cod_empresa = ?");

        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql.toString());
            stm.setTime(1, DataUtil.retornarHorarioEmTime(insercao.getAgenda().getHorario()));
            stm.setDate(2, DataUtil.converterDateUtilParaDateSql(insercao.getData_solicitacao()));
            stm.setLong(3, insercao.getFuncionario().getId());
            stm.setInt(4, user_session.getEmpresa().getCodEmpresa());
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                resultado = rs.getBoolean("pode_marcar");
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        } catch (ProjetoException e) {
            e.printStackTrace();
        } finally {
            try {
                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return resultado;
    }

    public List<AgendaBean> quantidadeDeAgendamentosDaEquipePorTurno() {

        List<AgendaBean> lista = new ArrayList<AgendaBean>();

        FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
                .getSessionMap().get("obj_funcionario");

        StringBuilder sql = new StringBuilder();

        sql.append("SELECT DISTINCT e.id_equipe, e.descequipe, ");
        sql.append("(SELECT count(codpaciente) FROM ( ");
        sql.append("SELECT DISTINCT aa.codpaciente AS codpaciente FROM hosp.atendimentos aa ");
        sql.append("JOIN hosp.paciente_instituicao pp ON (aa.id_paciente_instituicao = pp.id) ");
        sql.append("JOIN hosp.profissional_dia_atendimento pda ON (aa.id_paciente_instituicao = pda.id_paciente_instituicao) ");
        sql.append("WHERE ((aa.horario >= '07:00' AND aa.horario <= '12:00') OR aa.turno = 'M') ");
        sql.append("AND aa.codequipe = e.id_equipe ");
        sql.append("AND pp.status = 'A' ");
        sql.append("AND pda.dia_semana = 2 ) a1) AS qtd_manha_segunda, ");
        sql.append("(SELECT count(codpaciente) FROM ( ");
        sql.append("SELECT DISTINCT aa.codpaciente AS codpaciente FROM hosp.atendimentos aa ");
        sql.append("JOIN hosp.paciente_instituicao pp ON (aa.id_paciente_instituicao = pp.id) ");
        sql.append("JOIN hosp.profissional_dia_atendimento pda ON (aa.id_paciente_instituicao = pda.id_paciente_instituicao) ");
        sql.append("WHERE ((aa.horario >= '07:00' AND aa.horario <= '12:00') OR aa.turno = 'M') ");
        sql.append("AND aa.codequipe = e.id_equipe ");
        sql.append("AND pp.status = 'A' ");
        sql.append("AND pda.dia_semana = 3 ) a1) AS qtd_manha_terca, ");
        sql.append("(SELECT count(codpaciente) FROM ( ");
        sql.append("SELECT DISTINCT aa.codpaciente AS codpaciente FROM hosp.atendimentos aa ");
        sql.append("JOIN hosp.paciente_instituicao pp ON (aa.id_paciente_instituicao = pp.id) ");
        sql.append("JOIN hosp.profissional_dia_atendimento pda ON (aa.id_paciente_instituicao = pda.id_paciente_instituicao) ");
        sql.append("WHERE ((aa.horario >= '07:00' AND aa.horario <= '12:00') OR aa.turno = 'M') ");
        sql.append("AND aa.codequipe = e.id_equipe ");
        sql.append("AND pp.status = 'A' ");
        sql.append("AND pda.dia_semana = 4 ) a1) AS qtd_manha_quarta, ");
        sql.append("(SELECT count(codpaciente) FROM ( ");
        sql.append("SELECT DISTINCT aa.codpaciente AS codpaciente FROM hosp.atendimentos aa ");
        sql.append("JOIN hosp.paciente_instituicao pp ON (aa.id_paciente_instituicao = pp.id) ");
        sql.append("JOIN hosp.profissional_dia_atendimento pda ON (aa.id_paciente_instituicao = pda.id_paciente_instituicao) ");
        sql.append("WHERE ((aa.horario >= '07:00' AND aa.horario <= '12:00') OR aa.turno = 'M') ");
        sql.append("AND aa.codequipe = e.id_equipe ");
        sql.append("AND pp.status = 'A' ");
        sql.append("AND pda.dia_semana = 5 ) a1) AS qtd_manha_quinta, ");
        sql.append("(SELECT count(codpaciente) FROM ( ");
        sql.append("SELECT DISTINCT aa.codpaciente AS codpaciente FROM hosp.atendimentos aa ");
        sql.append("JOIN hosp.paciente_instituicao pp ON (aa.id_paciente_instituicao = pp.id) ");
        sql.append("JOIN hosp.profissional_dia_atendimento pda ON (aa.id_paciente_instituicao = pda.id_paciente_instituicao) ");
        sql.append("WHERE ((aa.horario >= '07:00' AND aa.horario <= '12:00') OR aa.turno = 'M') ");
        sql.append("AND aa.codequipe = e.id_equipe ");
        sql.append("AND pp.status = 'A' ");
        sql.append("AND pda.dia_semana = 6 ) a1) AS qtd_manha_sexta, ");
        sql.append("(SELECT count(codpaciente) FROM ( ");
        sql.append("SELECT DISTINCT aa.codpaciente AS codpaciente FROM hosp.atendimentos aa ");
        sql.append("JOIN hosp.paciente_instituicao pp ON (aa.id_paciente_instituicao = pp.id) ");
        sql.append("JOIN hosp.profissional_dia_atendimento pda ON (aa.id_paciente_instituicao = pda.id_paciente_instituicao) ");
        sql.append("WHERE (horario >'12:00' OR turno = 'T') ");
        sql.append("AND aa.codequipe = e.id_equipe ");
        sql.append("AND pp.status = 'A' ");
        sql.append("AND pda.dia_semana = 2 ) a1) AS qtd_tarde_segunda, ");
        sql.append("(SELECT count(codpaciente) FROM ( ");
        sql.append("SELECT DISTINCT aa.codpaciente AS codpaciente FROM hosp.atendimentos aa ");
        sql.append("JOIN hosp.paciente_instituicao pp ON (aa.id_paciente_instituicao = pp.id) ");
        sql.append("JOIN hosp.profissional_dia_atendimento pda ON (aa.id_paciente_instituicao = pda.id_paciente_instituicao) ");
        sql.append("WHERE (horario >'12:00' OR turno = 'T') ");
        sql.append("AND aa.codequipe = e.id_equipe ");
        sql.append("AND pp.status = 'A' ");
        sql.append("AND pda.dia_semana = 3 ) a1) AS qtd_tarde_terca, ");
        sql.append("(SELECT count(codpaciente) FROM ( ");
        sql.append("SELECT DISTINCT aa.codpaciente AS codpaciente FROM hosp.atendimentos aa ");
        sql.append("JOIN hosp.paciente_instituicao pp ON (aa.id_paciente_instituicao = pp.id) ");
        sql.append("JOIN hosp.profissional_dia_atendimento pda ON (aa.id_paciente_instituicao = pda.id_paciente_instituicao) ");
        sql.append("WHERE (horario >'12:00' OR turno = 'T') ");
        sql.append("AND aa.codequipe = e.id_equipe ");
        sql.append("AND pp.status = 'A' ");
        sql.append("AND pda.dia_semana = 4 ) a1) AS qtd_tarde_quarta, ");
        sql.append("(SELECT count(codpaciente) FROM ( ");
        sql.append("SELECT DISTINCT aa.codpaciente AS codpaciente FROM hosp.atendimentos aa ");
        sql.append("JOIN hosp.paciente_instituicao pp ON (aa.id_paciente_instituicao = pp.id) ");
        sql.append("JOIN hosp.profissional_dia_atendimento pda ON (aa.id_paciente_instituicao = pda.id_paciente_instituicao) ");
        sql.append("WHERE (horario >'12:00' OR turno = 'T') ");
        sql.append("AND aa.codequipe = e.id_equipe ");
        sql.append("AND pp.status = 'A' ");
        sql.append("AND pda.dia_semana = 5 ) a1) AS qtd_tarde_quinta, ");
        sql.append("(SELECT count(codpaciente) FROM ( ");
        sql.append("SELECT DISTINCT aa.codpaciente AS codpaciente FROM hosp.atendimentos aa ");
        sql.append("JOIN hosp.paciente_instituicao pp ON (aa.id_paciente_instituicao = pp.id) ");
        sql.append("JOIN hosp.profissional_dia_atendimento pda ON (aa.id_paciente_instituicao = pda.id_paciente_instituicao) ");
        sql.append("WHERE (horario >'12:00' OR turno = 'T') ");
        sql.append("AND aa.codequipe = e.id_equipe ");
        sql.append("AND pp.status = 'A' ");
        sql.append("AND pda.dia_semana = 6 ) a1) AS qtd_tarde_sexta ");
        sql.append("FROM hosp.atendimentos a ");
        sql.append("LEFT JOIN hosp.equipe e ON (a.codequipe = e.id_equipe) ");
        sql.append("WHERE a.codequipe IS NOT NULL AND a.cod_empresa = ? ");
        sql.append("ORDER BY e.descequipe ");

        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = null;
            stm = con.prepareStatement(sql.toString());

            stm.setInt(1, user_session.getEmpresa().getCodEmpresa());

            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                Integer vetor[] = new Integer[5];

                AgendaBean agendaBean = new AgendaBean();
                agendaBean.getEquipe().setCodEquipe(rs.getInt("id_equipe"));
                agendaBean.getEquipe().setDescEquipe(rs.getString("descequipe"));
                agendaBean.getQtdAtendimentosManha()[SEGUNDA]  = rs.getInt("qtd_manha_segunda");
                agendaBean.getQtdAtendimentosManha()[TERCA] = rs.getInt("qtd_manha_terca");
                agendaBean.getQtdAtendimentosManha()[QUARTA] = rs.getInt("qtd_manha_quarta");
                agendaBean.getQtdAtendimentosManha()[QUINTA] = rs.getInt("qtd_manha_quinta");
                agendaBean.getQtdAtendimentosManha()[SEXTA] = rs.getInt("qtd_manha_sexta");
                agendaBean.getQtdAtendimentosTarde()[SEGUNDA] = rs.getInt("qtd_tarde_segunda");
                agendaBean.getQtdAtendimentosTarde()[TERCA] = rs.getInt("qtd_tarde_terca");
                agendaBean.getQtdAtendimentosTarde()[QUARTA] = rs.getInt("qtd_tarde_quarta");
                agendaBean.getQtdAtendimentosTarde()[QUINTA] = rs.getInt("qtd_tarde_quinta");
                agendaBean.getQtdAtendimentosTarde()[SEXTA] = rs.getInt("qtd_tarde_sexta");

                lista.add(agendaBean);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        } catch (ProjetoException e) {
            e.printStackTrace();
        } finally {
            try {
                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return lista;
    }

    public Integer contarAtendimentosPorTipoAtendimentoPorProfissionalDataUnica(
            Long codProfissional, java.util.Date dataAtendimento, String turno, Integer codTipoAtendimento)
            throws ProjetoException {

        Integer qtd = null;

        String sql = "";

        sql = "SELECT count(id_atendimento) as qtd FROM hosp.atendimentos WHERE codtipoatendimento = ? AND codmedico = ? AND turno = ? AND dtaatende = ?";

        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setLong(1, codTipoAtendimento);
            stm.setLong(2, codProfissional);
            stm.setString(3, turno.toUpperCase());
            stm.setDate(4, new java.sql.Date(dataAtendimento.getTime()));

            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                qtd = rs.getInt("qtd");
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
        return qtd;
    }

}
