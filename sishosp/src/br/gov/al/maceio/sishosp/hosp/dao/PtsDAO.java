package br.gov.al.maceio.sishosp.hosp.dao;

import br.gov.al.maceio.sishosp.acl.model.FuncionarioBean;
import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;
import br.gov.al.maceio.sishosp.comum.util.DataUtil;
import br.gov.al.maceio.sishosp.hosp.model.Pts;
import br.gov.al.maceio.sishosp.hosp.model.PtsArea;

import javax.faces.context.FacesContext;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PtsDAO {

    PreparedStatement ps = null;
    private Connection conexao = null;

    FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
            .getSessionMap().get("obj_funcionario");

    public Boolean verificarSeExistePts(Integer id)
            throws ProjetoException {

        Boolean retorno = false;

        String sql = "SELECT id FROM hosp.pts WHERE id_paciente_instituicao = ? ";

        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stmt = conexao.prepareStatement(sql);

            stmt.setInt(1, id);

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

    public Pts ptsCarregarPacientesInstituicao(Integer id)
            throws ProjetoException {

        String sql = "SELECT pi.id, pi.codprograma, p.descprograma, pi.codgrupo, g.descgrupo, " +
                "pi.codpaciente, pa.nome, pa.cns, pa.cpf " +
                "FROM hosp.paciente_instituicao pi " +
                "LEFT JOIN hosp.programa p ON (p.id_programa = pi.codprograma) " +
                "LEFT JOIN hosp.grupo g ON (pi.codgrupo = g.id_grupo) " +
                "LEFT JOIN hosp.pacientes pa ON (pa.id_paciente = pi.codpaciente) " +
                "WHERE pi.id = ? ";

        Pts pts = new Pts();

        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stmt = conexao.prepareStatement(sql);

            stmt.setInt(1, id);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                pts = new Pts();
                pts.getGerenciarPaciente().setId(rs.getInt("id"));
                pts.getPrograma().setIdPrograma(rs.getInt("codprograma"));
                pts.getPrograma().setDescPrograma(rs.getString("descprograma"));
                pts.getGrupo().setIdGrupo(rs.getInt("codgrupo"));
                pts.getGrupo().setDescGrupo(rs.getString("descgrupo"));
                pts.getPaciente().setId_paciente(rs.getInt("codpaciente"));
                pts.getPaciente().setNome(rs.getString("nome"));
                pts.getPaciente().setCpf(rs.getString("cpf"));
                pts.getPaciente().setCns(rs.getString("cns"));
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
        return pts;
    }

    public Pts ptsCarregarPacientesInstituicaoIhPts(Integer id)
            throws ProjetoException {

        String sql = "SELECT p.id AS id_pts, p.id_paciente_instituicao, p.data, p.diagnostico_funcional, p.necessidades_e_desejos, " +
                "pi.codprograma, pr.descprograma, pi.codgrupo, g.descgrupo, pi.codpaciente, pa.nome, pa.cns, pa.cpf " +
                "FROM hosp.pts p " +
                "LEFT JOIN hosp.paciente_instituicao pi ON (pi.id = p.id_paciente_instituicao) " +
                "LEFT JOIN hosp.programa pr ON (pr.id_programa = pi.codprograma) " +
                "LEFT JOIN hosp.grupo g ON (pi.codgrupo = g.id_grupo) " +
                "LEFT JOIN hosp.pacientes pa ON (pa.id_paciente = pi.codpaciente) " +
                "WHERE p.id_paciente_instituicao = ?";

        Pts pts = new Pts();

        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stmt = conexao.prepareStatement(sql);

            stmt.setInt(1, id);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                pts = new Pts();
                pts.getGerenciarPaciente().setId(rs.getInt("id_paciente_instituicao"));
                pts.getPrograma().setIdPrograma(rs.getInt("codprograma"));
                pts.getPrograma().setDescPrograma(rs.getString("descprograma"));
                pts.getGrupo().setIdGrupo(rs.getInt("codgrupo"));
                pts.getGrupo().setDescGrupo(rs.getString("descgrupo"));
                pts.getPaciente().setId_paciente(rs.getInt("codpaciente"));
                pts.getPaciente().setNome(rs.getString("nome"));
                pts.getPaciente().setCpf(rs.getString("cpf"));
                pts.getPaciente().setCns(rs.getString("cns"));
                pts.setId(rs.getInt("id_pts"));
                pts.setData(rs.getDate("data"));
                pts.setDiagnosticoFuncional(rs.getString("diagnostico_funcional"));
                pts.setNecessidadesIhDesejos(rs.getString("necessidades_e_desejos"));
                pts.setListaPtsArea(carregarAreasPts(id, conexao));

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
        return pts;
    }

    public List<PtsArea> carregarAreasPts(Integer id, Connection conAuxiliar)
            throws ProjetoException {

        String sql = "SELECT pa.id, pa.id_area, e.descespecialidade, pa.objetivo_curto, pa.objetivo_medio, pa.objetivo_longo, " +
                "pa.plano_curto, pa.plano_medio, pa.plano_longo, pa.id_funcionario " +
                "FROM hosp.pts_area pa " +
                "LEFT JOIN hosp.especialidade e ON (pa.id_area = e.id_especialidade) " +
                "LEFT JOIN hosp.pts p ON (pa.id_pts = p.id) " +
                "WHERE p.id_paciente_instituicao = ? ";

        List<PtsArea> lista = new ArrayList<>();

        try {
            PreparedStatement stmt = conAuxiliar.prepareStatement(sql);
            stmt.setInt(1, id);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                PtsArea ptsArea = new PtsArea();
                ptsArea.setId(rs.getInt("id"));
                ptsArea.getArea().setCodEspecialidade(rs.getInt("id_area"));
                ptsArea.getArea().setDescEspecialidade(rs.getString("descespecialidade"));
                ptsArea.setObjetivoCurto(rs.getString("objetivo_curto"));
                ptsArea.setObjetivoMedio(rs.getString("objetivo_medio"));
                ptsArea.setObjetivoLongo(rs.getString("objetivo_longo"));
                ptsArea.setPlanoDeIntervencoesCurto(rs.getString("plano_curto"));
                ptsArea.setPlanoDeIntervencoesMedio(rs.getString("plano_medio"));
                ptsArea.setPlanoDeIntervencoesLongo(rs.getString("plano_longo"));
                ptsArea.getFuncionario().setId(rs.getLong("id_funcionario"));

                lista.add(ptsArea);

            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        } finally {
            try {
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return lista;
    }

    public PtsArea carregarAreaTabelaPts(Integer idPtsArea)
            throws ProjetoException {

        String sql = "SELECT id, id_pts, id_area, objetivo_curto, objetivo_medio, objetivo_longo, " +
                "plano_curto, plano_medio, plano_longo, id_funcionario " +
                "FROM hosp.pts_area pa " +
                "WHERE id = ?";


        PtsArea ptsArea = new PtsArea();

        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stmt = conexao.prepareStatement(sql);

            stmt.setInt(1, idPtsArea);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                ptsArea.setId(rs.getInt("id"));
                ptsArea.getArea().setCodEspecialidade(rs.getInt("id_area"));
                ptsArea.setObjetivoCurto(rs.getString("objetivo_curto"));
                ptsArea.setObjetivoMedio(rs.getString("objetivo_medio"));
                ptsArea.setObjetivoLongo(rs.getString("objetivo_longo"));
                ptsArea.setPlanoDeIntervencoesCurto(rs.getString("plano_curto"));
                ptsArea.setPlanoDeIntervencoesMedio(rs.getString("plano_medio"));
                ptsArea.setPlanoDeIntervencoesLongo(rs.getString("plano_longo"));
                ptsArea.getFuncionario().setId(rs.getLong("id_funcionario"));
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
        return ptsArea;
    }

    public Boolean gravarPts(Pts pts, Boolean ehParaDeletar) {

        Boolean retorno = false;

        String sql1 = "INSERT INTO hosp.pts (id_paciente_instituicao, data, diagnostico_funcional, necessidades_e_desejos, id_funcionario, data_hora_operacao) " +
                "values (?, ?, ?, ?, ?, CURRENT_TIMESTAMP) RETURNING id;";

        try {
            conexao = ConnectionFactory.getConnection();

            if (ehParaDeletar) {
                verificarExclusoesPtsIhArea(excluirListaArea(pts.getId(), conexao), excluirPts(pts.getId(), conexao), conexao);
            }

            ps = conexao.prepareStatement(sql1);
            ps.setInt(1, pts.getGerenciarPaciente().getId());
            ps.setDate(2, DataUtil.converterDateUtilParaDateSql(pts.getData()));
            ps.setString(3, pts.getDiagnosticoFuncional());
            ps.setString(4, pts.getNecessidadesIhDesejos());
            ps.setInt(5, user_session.getCodigo());

            ResultSet rs = ps.executeQuery();
            Integer codPts = null;

            if (rs.next()) {
                codPts = rs.getInt("id");
            }

            String sql2 = "INSERT INTO hosp.pts_area (id_pts, id_area, objetivo_curto, objetivo_medio, objetivo_longo, plano_curto, plano_medio, plano_longo, " +
                    "id_funcionario, data_hora_operacao) values (?, ?, ?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP);";


            for (int i = 0; i < pts.getListaPtsArea().size(); i++) {
                PreparedStatement ps2 = conexao.prepareStatement(sql2);
                ps2.setInt(1, codPts);
                ps2.setInt(2, pts.getListaPtsArea().get(i).getArea().getCodEspecialidade());
                ps2.setString(3, pts.getListaPtsArea().get(i).getObjetivoCurto());
                ps2.setString(4, pts.getListaPtsArea().get(i).getObjetivoMedio());
                ps2.setString(5, pts.getListaPtsArea().get(i).getObjetivoLongo());
                ps2.setString(6, pts.getListaPtsArea().get(i).getPlanoDeIntervencoesCurto());
                ps2.setString(7, pts.getListaPtsArea().get(i).getPlanoDeIntervencoesMedio());
                ps2.setString(8, pts.getListaPtsArea().get(i).getPlanoDeIntervencoesLongo());
                ps2.setLong(9, pts.getListaPtsArea().get(i).getFuncionario().getId());
                ps2.execute();
            }

            conexao.commit();

            conexao.close();

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
            return retorno;
        }
    }

    public Boolean excluirListaArea(Integer id, Connection conAuxiliar) {

        boolean retorno = false;

        String sql = "DELETE FROM hosp.pts_area WHERE id_pts = ?";

        try {
            PreparedStatement stmt = conAuxiliar.prepareStatement(sql);
            stmt.setInt(1, id);
            stmt.executeUpdate();

            retorno = true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        } finally {
            try {
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return retorno;
        }
    }

    public Boolean excluirPts(Integer id, Connection conAuxiliar) {

        boolean retorno = false;

        String sql = "DELETE FROM hosp.pts WHERE id = ?";

        try {
            PreparedStatement stmt = conAuxiliar.prepareStatement(sql);
            stmt.setInt(1, id);
            stmt.executeUpdate();

            retorno = true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        } finally {
            try {
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return retorno;
        }
    }

    public void verificarExclusoesPtsIhArea(Boolean area, Boolean pts, Connection conAuxiliar) throws SQLException {

        Boolean resultado = false;

        resultado = area;

        if (resultado) {
            resultado = pts;
        }

        if (!resultado) {
            conAuxiliar.close();
        }

    }

    public Pts carregarPtsDoPaciente(Integer codPaciente) throws ProjetoException {

        String sql = "SELECT p.data, p.diagnostico_funcional, p.necessidades_e_desejos, p.id_paciente_instituicao " +
                "FROM hosp.pts p " +
                "LEFT JOIN hosp.paciente_instituicao pi ON (pi.id = p.id_paciente_instituicao) " +
                "WHERE pi.codpaciente = ?";


        Pts pts = new Pts();

        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stm = conexao.prepareStatement(sql);
            stm.setInt(1, codPaciente);

            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                pts.setData(rs.getDate("data"));
                pts.setDiagnosticoFuncional(rs.getString("diagnostico_funcional"));
                pts.setNecessidadesIhDesejos(rs.getString("necessidades_e_desejos"));
                pts.getGerenciarPaciente().setId(rs.getInt("id_paciente_instituicao"));
                pts.setListaPtsArea(carregarAreasPts(pts.getGerenciarPaciente().getId(), conexao));

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
        return pts;
    }

}
