package br.gov.al.maceio.sishosp.hosp.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.gov.al.maceio.sishosp.acl.model.FuncionarioBean;
import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;
import br.gov.al.maceio.sishosp.comum.util.DataUtil;
import br.gov.al.maceio.sishosp.hosp.model.InsercaoPacienteBean;

import javax.faces.context.FacesContext;

public class InsercaoPacienteDAO {
    Connection con = null;
    PreparedStatement ps = null;

    public ArrayList<InsercaoPacienteBean> listarLaudosVigentes()
            throws ProjetoException {

        ArrayList<InsercaoPacienteBean> lista = new ArrayList<>();

        String sql = "select nome, cns, id_laudo from ( "
                + " select l.id_laudo, l.codpaciente, p.nome, p.cns, l.id_recurso, l.data_solicitacao, l.mes_inicio, l.ano_inicio, l.mes_final, l.ano_final, l.periodo, "
                + " l.codprocedimento_primario, pr.nome as procedimento, l.cid1, ci.desccid,  "
                + " to_date(ano_inicio||'-'||'0'||''||mes_inicio||'-'||'01', 'YYYY-MM-DD') as datainicio,  "
                + " (SELECT * FROM hosp.fn_GetLastDayOfMonth(to_date(ano_final||'-'||'0'||''||mes_final||'-'||'01', 'YYYY-MM-DD'))) as datafinal "
                + " from hosp.laudo l "
                + " left join hosp.pacientes p on (l.codpaciente = p.id_paciente) "
                + " left join hosp.proc pr on (l.codprocedimento_primario = pr.id) "
                + " left join hosp.cid ci on (l.cid1 = cast(ci.cod as integer)) "
                + " where current_date >= to_date(ano_inicio||'-'||'0'||''||mes_inicio||'-'||'01', 'YYYY-MM-DD') "
                + " and current_date <= (SELECT * FROM hosp.fn_GetLastDayOfMonth(to_date(ano_final||'-'||'0'||''||mes_final||'-'||'01', 'YYYY-MM-DD'))) "
                + " ) a";
        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                InsercaoPacienteBean insercao = new InsercaoPacienteBean();
                insercao.getLaudo().setId(rs.getInt("id_laudo"));
                insercao.getLaudo().getPaciente().setNome(rs.getString("nome"));
                insercao.getLaudo().getPaciente().setCns(rs.getString("cns"));

                lista.add(insercao);
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

    public InsercaoPacienteBean carregarLaudoPaciente(int id)
            throws ProjetoException {

        InsercaoPacienteBean insercao = new InsercaoPacienteBean();

        String sql = "select l.id_laudo, l.codpaciente, p.nome, p.cns, l.id_recurso, descrecurso, l.data_solicitacao, l.mes_inicio, l.ano_inicio, "
                + " l.mes_final, l.ano_final, l.periodo, "
                + " l.codprocedimento_primario, pr.nome as procedimento, l.cid1, ci.desccid, "
                + " to_date(ano_inicio||'-'||'0'||''||mes_inicio||'-'||'01', 'YYYY-MM-DD') as datainicio,  "
                + " (SELECT * FROM hosp.fn_GetLastDayOfMonth(to_date(ano_final||'-'||'0'||''||mes_final||'-'||'01', 'YYYY-MM-DD'))) as datafinal "
                + " from hosp.laudo l "
                + " left join hosp.pacientes p on (l.codpaciente = p.id_paciente) "
                + " left join hosp.proc pr on (l.codprocedimento_primario = pr.id) "
                + " left join hosp.cid ci on (l.cid1 = cast(ci.cod as integer)) "
                + " left join hosp.recurso r on (l.id_recurso = r.id) "
                + " where current_date >= to_date(ano_inicio||'-'||'0'||''||mes_inicio||'-'||'01', 'YYYY-MM-DD') "
                + " and current_date <= (SELECT * FROM hosp.fn_GetLastDayOfMonth(to_date(ano_final||'-'||'0'||''||mes_final||'-'||'01', 'YYYY-MM-DD'))) "
                + " and l.id_laudo = ?";
        try {
            con = ConnectionFactory.getConnection();

            PreparedStatement stm = con.prepareStatement(sql);

            stm.setInt(1, id);

            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                insercao = new InsercaoPacienteBean();
                insercao.getLaudo().setId(rs.getInt("id_laudo"));
                insercao.getLaudo().getPaciente()
                        .setId_paciente(rs.getInt("codpaciente"));
                insercao.getLaudo().getPaciente().setNome(rs.getString("nome"));
                insercao.getLaudo().getPaciente().setCns(rs.getString("cns"));
                insercao.getLaudo().getRecurso().setIdRecurso(rs.getInt("id_recurso"));
                insercao.getLaudo().getRecurso().setDescRecurso(rs.getString("descrecurso"));
                insercao.getLaudo().setData_solicitacao(
                        rs.getDate("data_solicitacao"));
                insercao.getLaudo().setMes_inicio(rs.getInt("mes_inicio"));
                insercao.getLaudo().setAno_inicio(rs.getInt("ano_inicio"));
                insercao.getLaudo().setMes_final(rs.getInt("mes_final"));
                insercao.getLaudo().setAno_final(rs.getInt("ano_final"));
                insercao.getLaudo().setPeriodo(rs.getInt("periodo"));
                insercao.getLaudo().getProcedimento_primario()
                        .setIdProc(rs.getInt("codprocedimento_primario"));
                insercao.getLaudo().getProcedimento_primario()
                        .setNomeProc(rs.getString("procedimento"));
                insercao.getLaudo().getCid1().setIdCid(rs.getInt("cid1"));
                insercao.getLaudo().getCid1()
                        .setDescCid(rs.getString("desccid"));
                insercao.getLaudo().setVigencia_inicial(
                        rs.getDate("datainicio"));
                insercao.getLaudo().setVigencia_final(rs.getDate("datafinal"));
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
        return insercao;
    }

    public boolean gravarInsercaoEquipe(InsercaoPacienteBean insercao,
                                        List<FuncionarioBean> lista, ArrayList<InsercaoPacienteBean> listaAgendamento) throws ProjetoException {

        FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
                .getSessionMap().get("obj_funcionario");

        Boolean retorno = false;

        String sql = "insert into hosp.paciente_instituicao (codprograma, codgrupo, codpaciente, codequipe, status, codlaudo, observacao, cod_empresa) "
                + " values (?, ?, ?, ?, ?, ?, ?, ?) RETURNING id;";
        try {
            con = ConnectionFactory.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, insercao.getPrograma().getIdPrograma());
            ps.setInt(2, insercao.getGrupo().getIdGrupo());
            ps.setInt(3, insercao.getLaudo().getPaciente().getId_paciente());
            ps.setInt(4, insercao.getEquipe().getCodEquipe());
            ps.setString(5, "A");
            ps.setInt(6, insercao.getLaudo().getId());
            ps.setString(7, insercao.getObservacao());
            ps.setInt(8, user_session.getEmpresa().getCodEmpresa());

            ResultSet rs = ps.executeQuery();
            int id = 0;
            if (rs.next()) {
                id = rs.getInt("id");
            }

            String sql2 = "INSERT INTO hosp.profissional_dia_atendimento (id_paciente_instituicao, id_profissional, dia_semana, cod_empresa) VALUES  (?, ?, ?, ?)";
            ps = con.prepareStatement(sql2);

            for (int i = 0; i < lista.size(); i++) {
                ps.setLong(1, id);
                ps.setLong(2, lista.get(i).getId());
                for (int j = 0; j < lista.get(i).getListDiasSemana().size(); j++) {
                    ps.setInt(
                            3,
                            Integer.parseInt(lista.get(i).getListDiasSemana()
                                    .get(j)));
                    ps.setInt(4, user_session.getEmpresa().getCodEmpresa());
                    ps.executeUpdate();
                }
            }

            String sql3 = "INSERT INTO hosp.atendimentos(codpaciente, codmedico, situacao, dtamarcacao, codtipoatendimento, turno, "
                    + " observacao, ativo, id_paciente_instituicao, cod_empresa, horario)"
                    + " VALUES (?, ?, 'A', ?, ?, ?, ?, 'S', ?, ?, ?) RETURNING id_atendimento;";

            PreparedStatement ps3 = null;
            ps3 = con.prepareStatement(sql3);

            for (int i = 0; i < listaAgendamento.size(); i++) {

                ps3.setInt(1, insercao.getLaudo().getPaciente().getId_paciente());
                ps3.setLong(2, listaAgendamento.get(i).getAgenda().getProfissional().getId());
                ps3.setDate(3, new java.sql.Date(listaAgendamento.get(i)
                        .getAgenda().getDataMarcacao().getTime()));
                ps3.setInt(4, insercao.getAgenda().getTipoAt().getIdTipo());

                if(insercao.getAgenda().getTurno() != null) {
                    ps3.setString(5, insercao.getAgenda().getTurno());
                }
                else{
                    ps3.setNull(5, Types.NULL);
                }

                ps3.setString(6, insercao.getObservacao());
                ps3.setInt(7, id);
                ps3.setInt(8, user_session.getEmpresa().getCodEmpresa());

                if(insercao.getAgenda().getHorario() != null) {
                    ps3.setTime(9, DataUtil.retornarHorarioEmTime(insercao.getAgenda().getHorario()));
                }
                else{
                    ps3.setNull(9, Types.NULL);
                }

                rs = ps3.executeQuery();

                int idAgend = 0;
                if (rs.next()) {
                    idAgend = rs.getInt("id_atendimento");
                }

                String sql4 = "INSERT INTO hosp.atendimentos1 (codprofissionalatendimento, id_atendimento, cbo) VALUES  (?, ?, ?)";

                PreparedStatement ps4 = null;
                ps4 = con.prepareStatement(sql4);

                ps4.setLong(1, listaAgendamento.get(i).getAgenda().getProfissional().getId());
                ps4.setInt(2, idAgend);
                ps4.setInt(3, listaAgendamento.get(i).getAgenda().getProfissional().getCbo().getCodCbo());

                ps4.executeUpdate();

            }

            String sql5 = "INSERT INTO hosp.historico_paciente_instituicao (codpaciente_instituicao, data_operacao, observacao, tipo) "
                    + " VALUES  (?, current_date, ?, ?)";

            PreparedStatement ps5 = null;
            ps5 = con.prepareStatement(sql5);

            ps5.setLong(1, id);
            ps5.setString(2, insercao.getObservacao());
            ps5.setString(3, "I");

            ps5.executeUpdate();

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
        }
        return retorno;
    }

    public boolean gravarInsercaoProfissional(InsercaoPacienteBean insercao,
                                              ArrayList<InsercaoPacienteBean> listaAgendamento)
            throws ProjetoException {

        FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
                .getSessionMap().get("obj_funcionario");

        Boolean retorno = false;

        String sql = "insert into hosp.paciente_instituicao (codprofissional, status, codlaudo, observacao, data_solicitacao, cod_empresa) "
                + " values (?, ?, ?, ?, ?, ?) RETURNING id;";
        try {
            con = ConnectionFactory.getConnection();
            ps = con.prepareStatement(sql);
            ps.setLong(1, insercao.getFuncionario().getId());
            ps.setString(2, "A");
            ps.setInt(3, insercao.getLaudo().getId());
            ps.setString(4, insercao.getObservacao());
            ps.setDate(5, new java.sql.Date(insercao.getData_solicitacao()
                    .getTime()));
            ps.setInt(6, user_session.getEmpresa().getCodEmpresa());

            ResultSet rs = ps.executeQuery();
            int id = 0;
            if (rs.next()) {
                id = rs.getInt("id");
            }

            String sql2 = "INSERT INTO hosp.profissional_dia_atendimento (id_paciente_instituicao, id_profissional, dia_semana, cod_empresa) VALUES  (?, ?, ?, ?)";
            PreparedStatement ps2 = null;
            ps2 = con.prepareStatement(sql2);

            for (int i = 0; i < insercao.getFuncionario().getListDiasSemana()
                    .size(); i++) {
                ps2.setLong(1, id);
                ps2.setLong(2, insercao.getFuncionario().getId());
                ps2.setInt(
                        3,
                        Integer.parseInt(insercao.getFuncionario()
                                .getListDiasSemana().get(i)));
                ps2.setInt(4, user_session.getEmpresa().getCodEmpresa());
                ps2.executeUpdate();

            }

            String sql3 = "INSERT INTO hosp.atendimentos(codpaciente, codmedico, situacao, dtamarcacao, codtipoatendimento, turno, observacao, ativo, id_paciente_instituicao, cod_empresa)"
                    + " VALUES (?, ?, 'A', ?, ?, ?, ?, 'S', ?, ?) RETURNING id_atendimento;";

            PreparedStatement ps3 = null;
            ps3 = con.prepareStatement(sql3);

            for (int i = 0; i < listaAgendamento.size(); i++) {

                ps3.setInt(1, insercao.getLaudo().getPaciente().getId_paciente());
                ps3.setLong(2, insercao.getFuncionario().getId());
                ps3.setDate(3, new java.sql.Date(listaAgendamento.get(i)
                        .getAgenda().getDataMarcacao().getTime()));
                ps3.setInt(4, insercao.getAgenda().getTipoAt().getIdTipo());
                ps3.setString(5, insercao.getAgenda().getTurno());
                ps3.setString(6, insercao.getObservacao());
                ps3.setInt(7, id);
                ps3.setInt(8, user_session.getEmpresa().getCodEmpresa());

                rs = ps3.executeQuery();

                int idAgend = 0;
                if (rs.next()) {
                    idAgend = rs.getInt("id_atendimento");
                }

                String sql4 = "INSERT INTO hosp.atendimentos1 (codprofissionalatendimento, id_atendimento, cbo) VALUES  (?, ?, ?)";

                PreparedStatement ps4 = null;
                ps4 = con.prepareStatement(sql4);

                ps4.setLong(1, insercao.getFuncionario().getId());
                ps4.setInt(2, idAgend);
                ps4.setInt(3, insercao.getFuncionario().getCbo().getCodCbo());

                ps4.executeUpdate();

            }

            String sql5 = "INSERT INTO hosp.historico_paciente_instituicao (codpaciente_instituicao, data_operacao, observacao, tipo) "
                    + " VALUES  (?, current_date, ?, ?)";

            PreparedStatement ps5 = null;
            ps5 = con.prepareStatement(sql5);

            ps5.setLong(1, id);
            ps5.setString(2, insercao.getObservacao());
            ps5.setString(3, "I");

            ps5.executeUpdate();

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
        }
        return retorno;
    }

    public Date dataFinalLaudo(int id) throws ProjetoException {

        Date data = null;

        String sql = "select (SELECT * FROM hosp.fn_GetLastDayOfMonth(to_date(ano_final||'-'||'0'||''||mes_final||'-'||'01', 'YYYY-MM-DD'))) as datafinal "
                + " from hosp.laudo l where l.id_laudo = ?";

        try {
            con = ConnectionFactory.getConnection();

            PreparedStatement stm = con.prepareStatement(sql);

            stm.setInt(1, id);

            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                data = rs.getDate("datafinal");
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
        return data;
    }

}
