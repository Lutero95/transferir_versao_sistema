package br.gov.al.maceio.sishosp.hosp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.gov.al.maceio.sishosp.acl.model.FuncionarioBean;
import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;
import br.gov.al.maceio.sishosp.hosp.model.GerenciarPacienteBean;

import javax.faces.context.FacesContext;

public class GerenciarPacienteDAO {

    PreparedStatement ps = null;
    private Connection conexao = null;

    FuncionarioBean user_session = (FuncionarioBean) FacesContext
            .getCurrentInstance().getExternalContext().getSessionMap()
            .get("obj_usuario");

    public List<GerenciarPacienteBean> carregarPacientesInstituicao()
            throws ProjetoException {

        String sql = "select p.id, p.codprograma,programa.descprograma, p.codgrupo, g.descgrupo, p.codpaciente, pa.nome, pa.cns, p.codequipe, e.descequipe, "
                + " p.codprofissional, f.descfuncionario, p.status, p.codlaudo, p.data_solicitacao, p.observacao, p.data_cadastro, pr.utiliza_equipamento, "
                + " pr.codproc, pr.nome nomeproc from hosp.paciente_instituicao p "
                + " left join hosp.laudo l on (l.id_laudo = p.codlaudo) "
                + " left join hosp.proc pr on (l.codprocedimento_primario = pr.id) "
                + " left join hosp.pacientes pa on (p.codpaciente = pa.id_paciente) "
                + " left join hosp.equipe e on (p.codequipe = e.id_equipe) "
                + " left join acl.funcionarios f on (p.codprofissional = f.id_funcionario) "
                + " left join hosp.grupo g on (g.id_grupo = p.codgrupo)"
                + " left join hosp.programa  on (programa.id_programa = p.codprograma)"
                + " where p.cod_empresa = ? AND p.status = 'A' ";

        List<GerenciarPacienteBean> lista = new ArrayList<>();

        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setInt(1, user_session.getEmpresa().getCodEmpresa());

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                GerenciarPacienteBean gp = new GerenciarPacienteBean();

                gp.setId(rs.getInt("id"));
                gp.getPrograma().setIdPrograma(rs.getInt("codprograma"));
                gp.getGrupo().setIdGrupo(rs.getInt("codgrupo"));
                gp.getGrupo().setDescGrupo(rs.getString("descgrupo"));
                gp.getPrograma().setIdPrograma(rs.getInt("codprograma"));
                gp.getPrograma().setDescPrograma(rs.getString("descprograma"));
                gp.getPaciente().setId_paciente(rs.getInt("codpaciente"));
                gp.getPaciente().setNome(rs.getString("nome"));
                gp.getPaciente().setCns(rs.getString("cns"));
                gp.getEquipe().setCodEquipe(rs.getInt("codequipe"));
                gp.getEquipe().setDescEquipe(rs.getString("descequipe"));
                gp.getFuncionario().setId(rs.getLong("codprofissional"));
                gp.getFuncionario().setNome(rs.getString("descfuncionario"));
                gp.setStatus(rs.getString("status"));
                gp.getLaudo().setId(rs.getInt("codlaudo"));
                gp.setData_solicitacao(rs.getDate("data_solicitacao"));
                gp.setObservacao(rs.getString("observacao"));
                gp.setData_cadastro(rs.getDate("data_cadastro"));
                gp.getLaudo().getProcedimentoPrimario().setUtilizaEquipamento(rs.getBoolean("utiliza_equipamento"));
                gp.getLaudo().getProcedimentoPrimario().setCodProc(rs.getInt("codproc"));
                gp.getLaudo().getProcedimentoPrimario().setNomeProc(rs.getString("nomeproc"));

                lista.add(gp);

            }
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        } finally {
            try {
                conexao.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return lista;
    }

    public List<GerenciarPacienteBean> carregarPacientesInstituicaoBusca(
            GerenciarPacienteBean gerenciar) throws ProjetoException {

        String sql = "select p.id, p.codprograma, p.codgrupo, g.descgrupo, p.codpaciente, pa.nome, pa.cns, p.codequipe, e.descequipe, "
                + " p.codprofissional, f.descfuncionario, p.status, p.codlaudo, p.data_solicitacao, p.observacao, p.data_cadastro, pr.utiliza_equipamento "
                + " from hosp.paciente_instituicao p "
                + " left join hosp.laudo l on (l.id_laudo = p.codlaudo) "
                + " left join hosp.proc pr on (l.codprocedimento_primario = pr.id) "
                + " left join hosp.pacientes pa on (p.codpaciente = pa.id_paciente) "
                + " left join hosp.equipe e on (p.codequipe = e.id_equipe) "
                + " left join acl.funcionarios f on (p.codprofissional = f.id_funcionario) "
                + " left join hosp.grupo g on (g.id_grupo = p.codgrupo) "
                + " where p.codprograma = ? and p.codgrupo = ? ";

        if (gerenciar.getStatus().equals("A")) {
            sql = sql + " and status = 'A'";
        }

        if (gerenciar.getStatus().equals("D")) {
            sql = sql + " and status = 'D'";
        }

        List<GerenciarPacienteBean> lista = new ArrayList<>();

        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stmt = conexao.prepareStatement(sql);

            stmt.setInt(1, gerenciar.getPrograma().getIdPrograma());
            stmt.setInt(2, gerenciar.getGrupo().getIdGrupo());

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                GerenciarPacienteBean gp = new GerenciarPacienteBean();

                gp.setId(rs.getInt("id"));
                gp.getPrograma().setIdPrograma(rs.getInt("codprograma"));
                gp.getGrupo().setIdGrupo(rs.getInt("codgrupo"));
                gp.getGrupo().setDescGrupo(rs.getString("descgrupo"));
                gp.getPaciente().setId_paciente(rs.getInt("codpaciente"));
                gp.getPaciente().setNome(rs.getString("nome"));
                gp.getPaciente().setCns(rs.getString("cns"));
                gp.getEquipe().setCodEquipe(rs.getInt("codequipe"));
                gp.getEquipe().setDescEquipe(rs.getString("descequipe"));
                gp.getFuncionario().setId(rs.getLong("codprofissional"));
                gp.getFuncionario().setNome(rs.getString("descfuncionario"));
                gp.setStatus(rs.getString("status"));
                gp.getLaudo().setId(rs.getInt("codlaudo"));
                gp.setData_solicitacao(rs.getDate("data_solicitacao"));
                gp.setObservacao(rs.getString("observacao"));
                gp.setData_cadastro(rs.getDate("data_cadastro"));
                gp.getLaudo().getProcedimentoPrimario().setUtilizaEquipamento(rs.getBoolean("utiliza_equipamento"));

                lista.add(gp);

            }
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        } finally {
            try {
                conexao.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return lista;
    }

    public Boolean desligarPaciente(GerenciarPacienteBean gerenciarRow,
                                    GerenciarPacienteBean gerenciar) throws ProjetoException {

        Boolean retorno = false;
        final String DESLIGADO = "D";

        
        String sql = "update hosp.paciente_instituicao set status = ? "
                + " where codlaudo = ?";
        try {
            conexao = ConnectionFactory.getConnection();
            
            GerenciarPacienteDAO gerenciarPacienteDAO = new GerenciarPacienteDAO();
                    if(!gerenciarPacienteDAO.apagarAtendimentos(gerenciarRow.getPaciente().getId_paciente(), conexao, false)){

                        conexao.close();

                        return retorno;
                    }
            
            PreparedStatement stmt = conexao.prepareStatement(sql);

            stmt.setString(1, DESLIGADO);
            stmt.setInt(2, gerenciarRow.getLaudo().getId());
            stmt.executeUpdate();

            String sql2 = "INSERT INTO hosp.historico_paciente_instituicao (codpaciente_instituicao, data_operacao, motivo_desligamento, tipo, observacao, id_funcionario_gravacao, data_desligamento) "
                    + " VALUES  (?, current_timestamp, ?, ?, ?,?, ?)";
            stmt = conexao.prepareStatement(sql2);
            stmt.setLong(1, gerenciarRow.getId());
            stmt.setInt(2, gerenciar.getMotivo_desligamento());
            stmt.setString(3, DESLIGADO);
            stmt.setString(4, gerenciar.getObservacao());
            stmt.setInt(5, user_session.getCodigo());
            stmt.setDate(6, new java.sql.Date(gerenciar.getDataDesligamento().getTime()));
            stmt.executeUpdate();

            conexao.commit();

            retorno = true;

        } catch (Exception ex) {
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

    public Boolean encaminharPaciente(GerenciarPacienteBean row,
                                      GerenciarPacienteBean gerenciar) throws ProjetoException {

        Boolean retorno = false;

        String sql = "update hosp.paciente_instituicao set status = 'D', cod_equipamento = ? "
                + " where codlaudo = ?";
        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stmt = conexao.prepareStatement(sql);

            stmt.setInt(1, gerenciar.getLaudo().getProcedimentoPrimario().getEquipamento().getId_equipamento());
            stmt.setInt(2, row.getLaudo().getId());
            stmt.executeUpdate();

            String sql2 = "INSERT INTO hosp.historico_paciente_instituicao (codpaciente_instituicao, data_operacao, motivo_desligamento, tipo, observacao,id_funcionario_gravacao) "
                    + " VALUES  (?, current_timestamp, (select motivo_padrao_desligamento_opm from hosp.parametro), ?, ?, ?)";
            stmt = conexao.prepareStatement(sql2);
            stmt.setLong(1, row.getPaciente().getId_paciente());
            stmt.setString(2, "D");
            stmt.setString(3, gerenciar.getObservacao());
            stmt.setInt(4, user_session.getCodigo());

            stmt.executeUpdate();

            conexao.commit();

            retorno = true;

        } catch (Exception ex) {
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

    public Boolean verificarPacienteAtivoInstituicao(Integer codPaciente) throws ProjetoException {

        Boolean retorno = false;

        String sql = "SELECT id FROM hosp.paciente_instituicao WHERE status = 'A' AND codpaciente = ?;";

        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stmt = conexao.prepareStatement(sql);

            stmt.setInt(1, codPaciente);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                retorno = true;
            }

        } catch (Exception ex) {
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

    public Boolean gravarHistoricoAcaoPaciente(Integer idPacienteInstituicao, String observacao, String tipo, Connection conAuxiliar) {

        Boolean retorno = false;

        String sql = "INSERT INTO hosp.historico_paciente_instituicao (codpaciente_instituicao, data_operacao, observacao, tipo,id_funcionario_gravacao) "
                + " VALUES  (?, CURRENT_TIMESTAMP , ?, ?,?)";

        try {
            ps = null;
            ps = conAuxiliar.prepareStatement(sql);

            ps.setLong(1, idPacienteInstituicao);
            ps.setString(2, observacao);
            ps.setString(3, tipo);
            ps.setInt(4, user_session.getCodigo());

            ps.executeUpdate();

            retorno = true;

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        } finally {
            try {
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return retorno;
    }

    public Boolean apagarAtendimentos(Integer idPacienteInstituicao, Connection conAuxiliar, Boolean alteracaoDePaciente) {

        Boolean retorno = false;
        ArrayList<Integer> lista = new ArrayList<Integer>();

        try {

            String sql = "SELECT DISTINCT a1.id_atendimento FROM hosp.atendimentos1 a1 " +
                    "LEFT JOIN hosp.atendimentos a ON (a.id_atendimento = a1.id_atendimento) " +
                    "WHERE a.id_paciente_instituicao = ? AND a.dtaatende >= current_date AND  " +
                    "(SELECT count(*) FROM hosp.atendimentos1 aa1 WHERE aa1.id_atendimento = a1.id_atendimento) = " +
                    "(SELECT count(*) FROM hosp.atendimentos1 aaa1 WHERE aaa1.id_atendimento = a1.id_atendimento AND situacao IS NULL) " +
                    "ORDER BY a1.id_atendimento;";


            ps = null;
            ps = conAuxiliar.prepareStatement(sql);
            ps.setLong(1, idPacienteInstituicao);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                lista.add(rs.getInt("id_atendimento"));
            }

            for (int i = 0; i < lista.size(); i++) {
                String sql2 = "delete from hosp.atendimentos1 where id_atendimento = ?";

                PreparedStatement ps2 = null;
                ps2 = conAuxiliar.prepareStatement(sql2);
                ps2.setLong(1, lista.get(i));
                ps2.execute();
            }

            for (int i = 0; i < lista.size(); i++) {
                String sql3 = "delete from hosp.atendimentos where id_atendimento = ?";

                PreparedStatement ps3 = null;
                ps3 = conAuxiliar.prepareStatement(sql3);
                ps3.setLong(1, lista.get(i));
                ps3.execute();
            }

            if (alteracaoDePaciente) {
            String sql4 = "delete from hosp.profissional_dia_atendimento where id_paciente_instituicao = ?";

            PreparedStatement ps4 = null;
            ps4 = conAuxiliar.prepareStatement(sql4);
            ps4.setLong(1, idPacienteInstituicao);
            ps4.execute();
            }
            retorno = true;

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        } finally {
            try {
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return retorno;
    }

    public Boolean gravarLiberacao(Integer idPacienteInstituicao, String motivo, Long usuarioLiberacao, Integer codAtendimento, Connection conAuxiliar) {

        Boolean retorno = false;

        String sql = "INSERT INTO hosp.liberacoes (motivo, usuario_liberacao, data_hora_liberacao, codatendimento, codpaciente_instituicao) "
                + " VALUES  (?, ?, CURRENT_TIMESTAMP, ?, ?)";

        try {
            ps = null;
            ps = conAuxiliar.prepareStatement(sql);

            ps.setString(1, motivo);
            ps.setLong(2, usuarioLiberacao);
            ps.setInt(3, codAtendimento);
            ps.setInt(4, idPacienteInstituicao);

            ps.executeUpdate();

            retorno = true;

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        } finally {
            try {
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return retorno;
    }

}
