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

        String sql = "select p.id, p.codprograma, p.codgrupo, g.descgrupo, p.codpaciente, pa.nome, pa.cns, p.codequipe, e.descequipe, "
                + " p.codprofissional, f.descfuncionario, p.status, p.codlaudo, p.data_solicitacao, p.observacao, p.data_cadastro, pr.utiliza_equipamento "
                + " from hosp.paciente_instituicao p "
                + " left join hosp.laudo l on (l.id_laudo = p.codlaudo) "
                + " left join hosp.proc pr on (l.codprocedimento_primario = pr.id) "
                + " left join hosp.pacientes pa on (p.codpaciente = pa.id_paciente) "
                + " left join hosp.equipe e on (p.codequipe = e.id_equipe) "
                + " left join acl.funcionarios f on (p.codprofissional = f.id_funcionario) "
                + " left join hosp.grupo g on (g.id_grupo = p.codgrupo)"
                + " where p.cod_empresa = ?";

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
                gp.getPaciente().setId_paciente(rs.getInt("codpaciente"));
                gp.getPaciente().setNome(rs.getString("nome"));
                gp.getPaciente().setCns(rs.getString("cns"));
                gp.getEquipe().setId_equipamento(rs.getInt("codequipe"));
                gp.getEquipe().setDescEquipamento(rs.getString("descequipe"));
                gp.getFuncionario().setId(rs.getLong("codprofissional"));
                gp.getFuncionario().setNome(rs.getString("descfuncionario"));
                gp.setStatus(rs.getString("status"));
                gp.getLaudo().setId(rs.getInt("codlaudo"));
                gp.setData_solicitacao(rs.getDate("data_solicitacao"));
                gp.setObservacao(rs.getString("observacao"));
                gp.setData_cadastro(rs.getDate("data_cadastro"));
                gp.getLaudo().getProcedimento_primario().setUtilizaEquipamento(rs.getBoolean("utiliza_equipamento"));

                lista.add(gp);

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
                gp.getEquipe().setId_equipamento(rs.getInt("codequipe"));
                gp.getEquipe().setDescEquipamento(rs.getString("descequipe"));
                gp.getFuncionario().setId(rs.getLong("codprofissional"));
                gp.getFuncionario().setNome(rs.getString("descfuncionario"));
                gp.setStatus(rs.getString("status"));
                gp.getLaudo().setId(rs.getInt("codlaudo"));
                gp.setData_solicitacao(rs.getDate("data_solicitacao"));
                gp.setObservacao(rs.getString("observacao"));
                gp.setData_cadastro(rs.getDate("data_cadastro"));
                gp.getLaudo().getProcedimento_primario().setUtilizaEquipamento(rs.getBoolean("utiliza_equipamento"));

                lista.add(gp);

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
            PreparedStatement stmt = conexao.prepareStatement(sql);

            stmt.setString(1, DESLIGADO);
            stmt.setInt(2, gerenciarRow.getLaudo().getId());
            stmt.executeUpdate();

            String sql2 = "INSERT INTO hosp.historico_paciente_instituicao (codpaciente_instituicao, data_operacao, motivo_desligamento, tipo, observacao) "
                    + " VALUES  (?, current_date, ?, ?, ?)";
            stmt = conexao.prepareStatement(sql2);
            stmt.setLong(1, gerenciarRow.getId());
            stmt.setInt(2, gerenciar.getMotivo_desligamento());
            stmt.setString(3, DESLIGADO);
            stmt.setString(4, gerenciar.getObservacao());

            stmt.executeUpdate();

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

    public Boolean encaminharPaciente(GerenciarPacienteBean row,
                                      GerenciarPacienteBean gerenciar) throws ProjetoException {

        Boolean retorno = false;

        String sql = "update hosp.paciente_instituicao set status = 'D', cod_equipamento = ? "
                + " where codlaudo = ?";
        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stmt = conexao.prepareStatement(sql);

            stmt.setInt(1, gerenciar.getLaudo().getProcedimento_primario().getEquipamento().getId_equipamento());
            stmt.setInt(2, row.getLaudo().getId());
            stmt.executeUpdate();

            String sql2 = "INSERT INTO hosp.historico_paciente_instituicao (codpaciente_instituicao, data_operacao, motivo_desligamento, tipo, observacao) "
                    + " VALUES  (?, current_date, (select motivo_padrao_desligamento_opm from hosp.parametro), ?, ?)";
            stmt = conexao.prepareStatement(sql2);
            stmt.setLong(1, row.getPaciente().getId_paciente());
            stmt.setString(2, "D");
            stmt.setString(3, gerenciar.getObservacao());

            stmt.executeUpdate();

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
