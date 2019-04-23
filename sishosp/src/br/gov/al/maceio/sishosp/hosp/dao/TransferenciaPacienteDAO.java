package br.gov.al.maceio.sishosp.hosp.dao;

import br.gov.al.maceio.sishosp.acl.model.FuncionarioBean;
import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;
import br.gov.al.maceio.sishosp.comum.util.DataUtil;
import br.gov.al.maceio.sishosp.hosp.model.GerenciarPacienteBean;
import br.gov.al.maceio.sishosp.hosp.model.InsercaoPacienteBean;

import javax.faces.context.FacesContext;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransferenciaPacienteDAO {

    PreparedStatement ps = null;
    private Connection conexao = null;

    FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
            .getSessionMap().get("obj_funcionario");

    public InsercaoPacienteBean carregarPacientesInstituicaoAlteracao(Integer id)
            throws ProjetoException {

        String sql = "select pi.id, pi.codprograma, p.descprograma, pi.codgrupo, g.descgrupo, pi.codpaciente, pi.codequipe, e.descequipe, a.turno, a.situacao, "
                + " pi.codprofissional, f.descfuncionario, pi.observacao, a.codtipoatendimento, t.desctipoatendimento, pi.codlaudo, pi.data_solicitacao "
                + " from hosp.paciente_instituicao pi "
                + " left join hosp.programa p on (p.id_programa = pi.codprograma) "
                + " left join hosp.grupo g on (pi.codgrupo = g.id_grupo) "
                + " left join hosp.equipe e on (pi.codequipe = e.id_equipe) "
                + " left join hosp.atendimentos a on (a.id_paciente_instituicao = pi.id) "
                + " left join hosp.tipoatendimento t on (a.codtipoatendimento = t.id) "
                + " left join acl.funcionarios f on (pi.codprofissional = f.id_funcionario) "
                + " where pi.id = ?";

        List<GerenciarPacienteBean> lista = new ArrayList<>();
        InsercaoPacienteBean ip = new InsercaoPacienteBean();
        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stmt = conexao.prepareStatement(sql);

            stmt.setInt(1, id);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                ip = new InsercaoPacienteBean();

                ip.setId(rs.getInt("id"));
                ip.getPrograma().setIdPrograma(rs.getInt("codprograma"));
                ip.getPrograma().setDescPrograma(rs.getString("descprograma"));
                ip.getGrupo().setIdGrupo(rs.getInt("codgrupo"));
                ip.getGrupo().setDescGrupo(rs.getString("descgrupo"));
                ip.getLaudo().getPaciente()
                        .setId_paciente(rs.getInt("codpaciente"));
                ip.getEquipe().setCodEquipe(rs.getInt("codequipe"));
                ip.getEquipe().setDescEquipe(rs.getString("descequipe"));
                ip.getFuncionario().setId(rs.getLong("codprofissional"));
                ip.getFuncionario().setNome(rs.getString("descfuncionario"));
                ip.setObservacao(rs.getString("observacao"));
                ip.getAgenda().getTipoAt()
                        .setIdTipo(rs.getInt("codtipoatendimento"));
                ip.getAgenda().getTipoAt()
                        .setDescTipoAt(rs.getString("desctipoatendimento"));
                ip.getAgenda().setTurno(rs.getString("turno"));
                ip.getAgenda().setSituacao(rs.getString("situacao"));
                ip.getLaudo().setId(rs.getInt("codlaudo"));
                ip.setData_solicitacao(rs.getDate("data_solicitacao"));

            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        } finally {
            try {
                conexao.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return ip;
    }

    public boolean gravarTransferenciaEquipe(InsercaoPacienteBean insercao,
                                         InsercaoPacienteBean insercaoParaLaudo,
                                         List<InsercaoPacienteBean> listAgendamentoProfissional, Integer id_paciente, List<FuncionarioBean> listaProfissionais) throws ProjetoException {

        Boolean retorno = false;
        ResultSet rs = null;
        ArrayList<Integer> lista = new ArrayList<Integer>();

        try {
            conexao = ConnectionFactory.getConnection();

            String sql2 = "select id_atendimento from hosp.atendimentos where situacao = 'A' and id_paciente_instituicao = ? AND dtaatende > ?";

            PreparedStatement ps2 = null;
            ps2 = conexao.prepareStatement(sql2);
            ps2.setLong(1, id_paciente);
            ps2.setDate(2, DataUtil.converterDateUtilParaDateSql(insercao.getData_solicitacao()));
            ps2.execute();

            rs = ps2.executeQuery();

            while (rs.next()) {
                lista.add(rs.getInt("id_atendimento"));
            }

            for (int i = 0; i < lista.size(); i++) {
                String sql3 = "delete from hosp.atendimentos1 where id_atendimento = ?";

                PreparedStatement ps3 = null;
                ps3 = conexao.prepareStatement(sql3);
                ps3.setLong(1, lista.get(i));
                ps3.execute();
            }

            String sql4 = "delete from hosp.atendimentos where situacao = 'A' and id_paciente_instituicao = ? AND dtaatende > ?";

            PreparedStatement ps4 = null;
            ps4 = conexao.prepareStatement(sql4);
            ps4.setLong(1, id_paciente);
            ps4.setDate(2, DataUtil.converterDateUtilParaDateSql(insercao.getData_solicitacao()));
            ps4.execute();

            String sql5 = "delete from hosp.profissional_dia_atendimento where id_paciente_instituicao = ?";

            PreparedStatement ps5 = null;
            ps5 = conexao.prepareStatement(sql5);
            ps5.setLong(1, id_paciente);
            ps5.execute();


            String sql6 = "INSERT INTO hosp.profissional_dia_atendimento (id_paciente_instituicao, id_profissional, dia_semana, cod_empresa) VALUES  (?, ?, ?, ?)";
            PreparedStatement ps6 = null;
            ps6 = conexao.prepareStatement(sql6);

            for (int i = 0; i < listaProfissionais.size(); i++) {
                ps6.setLong(1, insercao.getId());
                ps6.setLong(2, listAgendamentoProfissional.get(i).getAgenda().getProfissional()
                        .getId());
                for (int j = 0; j < listAgendamentoProfissional.get(i).getAgenda().getProfissional()
                        .getListDiasSemana().size(); j++) {
                    ps6.setInt(3,
                            Integer.parseInt(listAgendamentoProfissional.get(i).getAgenda().getProfissional()
                                    .getListDiasSemana().get(j)));
                    ps6.setInt(4, user_session.getEmpresa().getCodEmpresa());
                    ps6.executeUpdate();
                }
            }

            String sql7 = "INSERT INTO hosp.atendimentos(codpaciente, codmedico, situacao, dtaatende, codtipoatendimento, turno, "
                    + " observacao, ativo, id_paciente_instituicao, cod_empresa, horario, dtamarcacao, codprograma, codgrupo, codequipe)"
                    + " VALUES (?, ?, 'A', ?, ?, ?, ?, 'S', ?, ?, ?, CURRENT_TIMESTAMP, ?, ?, ?) RETURNING id_atendimento;";

            PreparedStatement ps7 = null;
            ps7 = conexao.prepareStatement(sql7);

            for (int i = 0; i < listAgendamentoProfissional.size(); i++) {

                ps7.setInt(1, insercaoParaLaudo.getLaudo().getPaciente()
                        .getId_paciente());
                ps7.setLong(2, listAgendamentoProfissional.get(i).getAgenda().getProfissional()
                        .getId());
                ps7.setDate(3, new Date(listAgendamentoProfissional.get(i).getAgenda()
                        .getDataMarcacao().getTime()));
                ps7.setInt(4, user_session.getEmpresa().getParametro().getTipoAtendimento().getIdTipo());

                if(insercao.getAgenda().getTurno() != null) {
                    ps7.setString(5, insercao.getAgenda().getTurno());
                }
                else{
                    ps7.setNull(5, Types.NULL);
                }

                ps7.setString(6, insercao.getObservacao());
                ps7.setInt(7, id_paciente);
                ps7.setInt(8, user_session.getEmpresa().getCodEmpresa());

                if(insercao.getAgenda().getHorario() != null) {
                    ps7.setTime(9, DataUtil.retornarHorarioEmTime(insercao.getAgenda().getHorario()));
                }
                else{
                    ps7.setNull(9, Types.NULL);
                }

                ps7.setInt(10, insercao.getPrograma().getIdPrograma());

                ps7.setInt(11, insercao.getGrupo().getIdGrupo());

                ps7.setInt(12, insercao.getEquipe().getCodEquipe());

                rs = ps7.executeQuery();

                int idAgend = 0;
                if (rs.next()) {
                    idAgend = rs.getInt("id_atendimento");
                }

                for(int j=0; j<listaProfissionais.size(); j++) {

                    for (int h = 0; h < listaProfissionais.get(j).getListDiasSemana().size(); h++) {

                        if (DataUtil.extrairDiaDeData(listAgendamentoProfissional.get(i).getAgenda().getDataMarcacao()) ==
                                Integer.parseInt(listaProfissionais.get(j).getListDiasSemana().get(h))) {

                            String sql8 = "INSERT INTO hosp.atendimentos1 (codprofissionalatendimento, id_atendimento, cbo, codprocedimento) VALUES  (?, ?, ?, ?)";

                            PreparedStatement ps8 = null;
                            ps8 = conexao.prepareStatement(sql8);

                            ps8.setLong(1, listaProfissionais.get(j).getId());
                            ps8.setInt(2, idAgend);
                            ps8.setInt(3, listaProfissionais.get(j).getCbo().getCodCbo());
                            ps8.setInt(4, insercao.getPrograma().getProcedimento().getIdProc());

                            ps8.executeUpdate();
                        }
                    }
                }

            }

            String sql9 = "INSERT INTO hosp.historico_paciente_instituicao (codpaciente_instituicao, data_operacao, observacao, tipo) "
                    + " VALUES  (?, current_date, ?, ?)";

            PreparedStatement ps9 = null;
            ps9 = conexao.prepareStatement(sql9);

            ps9.setLong(1, id_paciente);
            ps9.setString(2, insercao.getObservacao());
            ps9.setString(3, "R");

            ps9.executeUpdate();

            conexao.commit();

            retorno = true;

        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        } finally {
            try {
                conexao.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return retorno;
    }

}
