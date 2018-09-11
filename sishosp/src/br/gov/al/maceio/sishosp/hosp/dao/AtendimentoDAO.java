package br.gov.al.maceio.sishosp.hosp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import br.gov.al.maceio.sishosp.acl.dao.FuncionarioDAO;
import br.gov.al.maceio.sishosp.acl.model.FuncionarioBean;
import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;
import br.gov.al.maceio.sishosp.hosp.model.AgendaBean;
import br.gov.al.maceio.sishosp.hosp.model.AtendimentoBean;
import br.gov.al.maceio.sishosp.hosp.model.BloqueioBean;
import br.gov.al.maceio.sishosp.hosp.model.EnderecoBean;
import br.gov.al.maceio.sishosp.hosp.model.FeriadoBean;
import br.gov.al.maceio.sishosp.hosp.model.PacienteBean;
import br.gov.al.maceio.sishosp.hosp.model.TipoAtendimentoBean;

import javax.faces.context.FacesContext;

public class AtendimentoDAO {
    Connection con = null;
    PreparedStatement ps = null;

    public List<AtendimentoBean> carregaAtendimentos(AtendimentoBean atendimento)
            throws ProjetoException {

        String sql = "select a.id_atendimento, a.dtaatende, a.codpaciente, p.nome, p.cns, a.turno, a.codmedico, f.descfuncionario,"
                + " a.codprograma, pr.descprograma, a.codtipoatendimento, t.desctipoatendimento,"
                + " a.codequipe, e.descequipe,"
                + " case when t.equipe_programa is true then 'Sim' else 'Não' end as ehEquipe,"

                + " case when "
                + " (select count(*) from hosp.atendimentos1 a1 where a1.id_atendimento = a.id_atendimento and situacao is null) =  "
                + " (select count(*) from hosp.atendimentos1 a1 where a1.id_atendimento = a.id_atendimento) "
                + " then 'Atendimento Não Informado' "
                + " when "
                + " (select count(*) from hosp.atendimentos1 a1 where a1.id_atendimento = a.id_atendimento and situacao is not null) = "
                + " (select count(*) from hosp.atendimentos1 a1 where a1.id_atendimento = a.id_atendimento) "
                + " then 'Atendimento Informado' "
                + " else 'Atendimento Informado Parcialmente' "
                + " end as situacao "

                + " from hosp.atendimentos a"
                + " left join hosp.pacientes p on (p.id_paciente = a.codpaciente)"
                + " left join acl.funcionarios f on (f.id_funcionario = a.codmedico)"
                + " left join hosp.programa pr on (pr.id_programa = a.codprograma)"
                + " left join hosp.tipoatendimento t on (t.id = a.codtipoatendimento)"
                + " left join hosp.equipe e on (e.id_equipe = a.codequipe)"
                + " where a.dtaatende >= ? and a.dtaatende <= ?"
                + " order by a.dtaatende";

        ArrayList<AtendimentoBean> lista = new ArrayList<AtendimentoBean>();

        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setDate(1, new java.sql.Date(atendimento
                    .getDataAtendimentoInicio().getTime()));
            stm.setDate(2, new java.sql.Date(atendimento
                    .getDataAtendimentoFinal().getTime()));
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                AtendimentoBean at = new AtendimentoBean();
                at.setId(rs.getInt("id_atendimento"));
                at.setDataAtendimentoInicio(rs.getDate("dtaatende"));
                at.getPaciente().setId_paciente(rs.getInt("codpaciente"));
                at.getPaciente().setNome(rs.getString("nome"));
                at.getPaciente().setCns(rs.getString("cns"));
                at.setTurno(rs.getString("turno"));
                at.getFuncionario().setId(rs.getLong("codmedico"));
                at.getFuncionario().setNome(rs.getString("descfuncionario"));
                at.getPrograma().setIdPrograma(rs.getInt("codprograma"));
                at.getPrograma().setDescPrograma(rs.getString("descprograma"));
                at.setSituacao(rs.getString("situacao"));
                at.getTipoAt().setIdTipo(rs.getInt("codtipoatendimento"));
                at.getTipoAt().setDescTipoAt(
                        rs.getString("desctipoatendimento"));
                at.getEquipe().setCodEquipe(rs.getInt("codequipe"));
                at.getEquipe().setDescEquipe(rs.getString("descequipe"));
                at.setEhEquipe(rs.getString("ehEquipe"));

                lista.add(at);
            }

            return lista;

        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        } finally {
            try {
                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
                System.exit(1);
            }
        }
    }

    public AtendimentoBean listarAtendimentoProfissionalPorId(int id)
            throws ProjetoException {

        AtendimentoBean at = new AtendimentoBean();
        String sql = "select a.id_atendimento, a.dtaatende, a.codpaciente, p.nome, a.codmedico, f.descfuncionario, f.codprocedimentopadrao, "
                + "pr.nome as procedimento, a.situacao "
                + "from hosp.atendimentos a "
                + "left join hosp.pacientes p on (p.id_paciente = a.codpaciente) "
                + "left join acl.funcionarios f on (f.id_funcionario = a.codmedico) "
                + "left join hosp.proc pr on (pr.id = f.codprocedimentopadrao) "
                + "where a.id_atendimento = ?";
        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setInt(1, id);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                at.setId(rs.getInt("id_atendimento"));
                at.setDataAtendimentoInicio(rs.getDate("dtaatende"));
                at.getPaciente().setId_paciente(rs.getInt("codpaciente"));
                at.getPaciente().setNome(rs.getString("nome"));
                at.getProcedimento().setIdProc(
                        rs.getInt("codprocedimentopadrao"));
                at.getProcedimento().setNomeProc(rs.getString("procedimento"));
                at.getFuncionario().setId(rs.getLong("codmedico"));
                at.getFuncionario().setNome(rs.getString("descfuncionario"));
                at.setSituacao(rs.getString("situacao"));
            }

        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        } finally {
            try {
                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
                System.exit(1);
            }
        }
        return at;
    }

    public Boolean realizaAtendimentoProfissional(FuncionarioBean funcionario,
                                                  AtendimentoBean atendimento) throws ProjetoException {
        boolean alterou = false;
        con = ConnectionFactory.getConnection();
        try {

            String sql = "update hosp.atendimentos1 set codprofissionalatendimento = ?, codprocedimento = ?, dtaatendido = current_timestamp, situacao = ? "
                    + " where id_atendimento = ?";

            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setLong(1, funcionario.getId());
            stmt.setInt(2, atendimento.getProcedimento().getIdProc());
            stmt.setString(3, atendimento.getStatus());
            stmt.setInt(4, atendimento.getId());

            stmt.executeUpdate();

            String sql2 = "update hosp.atendimentos set situacao = 'F' "
                    + " where id_atendimento = ?";

            PreparedStatement stmt2 = con.prepareStatement(sql2);
            stmt2.setInt(1, atendimento.getId());

            stmt2.executeUpdate();

            con.commit();

            alterou = true;

            return alterou;
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        } finally {
            try {
                con.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    public Boolean realizaAtendimentoEquipe(List<AtendimentoBean> lista)
            throws ProjetoException {
        boolean alterou = false;
        con = ConnectionFactory.getConnection();
        try {

            for (int i = 0; i < lista.size(); i++) {

                String sql = "delete from hosp.atendimentos1 where id_atendimento = ?";
                con = ConnectionFactory.getConnection();
                PreparedStatement stmt = con.prepareStatement(sql);
                stmt.setLong(1, lista.get(i).getId());
                stmt.execute();

                String sql2 = "INSERT INTO hosp.atendimentos1(dtaatendido, codprofissionalatendimento, id_atendimento, "
                        + " cbo, codprocedimento, situacao) VALUES (current_timestamp, ?, ?, ?, ?, ?);";

                PreparedStatement stmt2 = con.prepareStatement(sql2);
                stmt2.setLong(1, lista.get(i).getFuncionario().getId());
                stmt2.setInt(2, lista.get(i).getId());
                stmt2.setInt(3, lista.get(i).getCbo().getCodCbo());
                stmt2.setInt(4, lista.get(i).getProcedimento().getCodProc());
                stmt2.setString(5, lista.get(i).getStatus());
                stmt2.executeUpdate();

            }
            con.commit();

            alterou = true;

            return alterou;
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        } finally {
            try {
                con.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    public Boolean limpaAtendimentoProfissional(AtendimentoBean atendimento)
            throws ProjetoException {
        boolean alterou = false;
        con = ConnectionFactory.getConnection();
        try {

            String sql = "update hosp.atendimentos1 set dtaatendido = null, situacao = ? "
                    + " where id_atendimento = ?";

            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, "");
            stmt.setInt(2, atendimento.getId());

            stmt.executeUpdate();

            con.commit();

            alterou = true;

            return alterou;
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        } finally {
            try {
                con.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    public List<AtendimentoBean> carregaAtendimentosEquipe(Integer id)
            throws ProjetoException {

        String sql = "select a1.id_atendimentos1, a1.id_atendimento, a1.codprofissionalatendimento, f.descfuncionario, f.cns,"
                + " f.codcbo, c.descricao, a1.situacao, a1.codprocedimento, pr.nome as procedimento"
                + " from hosp.atendimentos1 a1"
                + " left join acl.funcionarios f on (f.id_funcionario = a1.codprofissionalatendimento)"
                + " left join hosp.cbo c on (f.codcbo = c.id)"
                + " left join hosp.proc pr on (a1.codprocedimento = pr.codproc)"
                + " where a1.id_atendimento = ?"
                + " order by a1.id_atendimentos1";

        ArrayList<AtendimentoBean> lista = new ArrayList<AtendimentoBean>();

        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setInt(1, id);

            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                AtendimentoBean at = new AtendimentoBean();
                at.setId(rs.getInt("id_atendimento"));
                at.setId1(rs.getInt("id_atendimentos1"));
                at.getFuncionario().setId(
                        rs.getLong("codprofissionalatendimento"));
                at.getFuncionario().setNome(rs.getString("descfuncionario"));
                at.getFuncionario().setCns(rs.getString("cns"));
                at.getCbo().setCodCbo(rs.getInt("codcbo"));
                at.getCbo().setDescCbo(rs.getString("descricao"));
                at.setStatus(rs.getString("situacao"));
                at.getProcedimento().setCodProc(rs.getInt("codprocedimento"));
                at.getProcedimento().setNomeProc(rs.getString("procedimento"));

                lista.add(at);
            }

            return lista;

        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        } finally {
            try {
                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
                System.exit(1);
            }
        }
    }

    public List<AtendimentoBean> carregaAtendimentosEquipeProfissional(
            Integer id) throws ProjetoException {

        String sql = "select a1.id_atendimentos1, a1.id_atendimento, a1.codprofissionalatendimento, f.descfuncionario, f.cns,"
                + " f.codcbo, c.descricao, a1.situacao, a1.codprocedimento, pr.nome as procedimento"
                + " from hosp.atendimentos1 a1"
                + " left join acl.funcionarios f on (f.id_funcionario = a1.codprofissionalatendimento)"
                + " left join hosp.cbo c on (f.codcbo = c.id)"
                + " left join hosp.proc pr on (a1.codprocedimento = pr.id)"
                + " where a1.id_atendimentos1 = ?"
                + " order by a1.id_atendimentos1";

        ArrayList<AtendimentoBean> lista = new ArrayList<AtendimentoBean>();

        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setInt(1, id);

            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                AtendimentoBean at = new AtendimentoBean();
                at.setId(rs.getInt("id_atendimento"));
                at.setId1(rs.getInt("id_atendimentos1"));
                at.getFuncionario().setId(
                        rs.getLong("codprofissionalatendimento"));
                at.getFuncionario().setNome(rs.getString("descfuncionario"));
                at.getFuncionario().setCns(rs.getString("cns"));
                at.getCbo().setCodCbo(rs.getInt("codcbo"));
                at.getCbo().setDescCbo(rs.getString("descricao"));
                at.setStatus(rs.getString("situacao"));
                at.getProcedimento().setCodProc(rs.getInt("codprocedimento"));
                at.getProcedimento().setNomeProc(rs.getString("procedimento"));

                lista.add(at);
            }

            return lista;

        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        } finally {
            try {
                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
                System.exit(1);
            }
        }
    }

    public Boolean verificarSeCboEhDoProfissionalPorEquipe(List<AtendimentoBean> lista)
            throws ProjetoException {

        Boolean retorno = false;
        ArrayList<Integer> listaAux = new ArrayList<>();


        try {

            con = ConnectionFactory.getConnection();
            for (int i = 0; i < lista.size(); i++) {
                String sql = "select p.id_cbo from hosp.proc_cbo p left join acl.funcionarios f on (p.id_proc = ?) left join hosp.cbo c on (c.id = p.id_cbo)"
                        + " where p.id_proc = f.codprocedimentopadrao and c.id = p.id_cbo and f.id_funcionario = ?";

                PreparedStatement stm = con.prepareStatement(sql);

                stm.setInt(1, lista.get(i).getProcedimento().getIdProc());
                stm.setLong(2, lista.get(i).getFuncionario().getId());

                ResultSet rs = stm.executeQuery();

                while (rs.next()) {

                    if (listaAux.size() == 0) {
                        listaAux.add(lista.get(i).getProcedimento().getIdProc());
                    }
                    if (!listaAux.contains(lista.get(i).getProcedimento().getIdProc())) {
                        listaAux.add(lista.get(i).getProcedimento().getIdProc());
                    }
                }


            }

            if (lista.size() == listaAux.size()) {
                retorno = true;
            }

        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        } finally {
            try {
                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
                System.exit(1);
            }
        }
        return retorno;
    }

    public Boolean verificarSeCboEhDoProfissionalPorProfissional(Long idProfissional, Integer idProcedimento)
            throws ProjetoException {

        Boolean retorno = false;

        String sql = "select p.id_cbo from hosp.proc_cbo p left join acl.funcionarios f on (p.id_proc = ?) left join hosp.cbo c on (c.id = p.id_cbo)"
                + " where p.id_proc = f.codprocedimentopadrao and c.id = p.id_cbo and f.id_funcionario = ?";
        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setInt(1, idProcedimento);
            stm.setLong(2, idProfissional);

            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                retorno = true;
            }

        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        } finally {
            try {
                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
                System.exit(1);
            }
        }
        return retorno;
    }

}
