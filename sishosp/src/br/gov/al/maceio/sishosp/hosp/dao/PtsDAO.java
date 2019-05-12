package br.gov.al.maceio.sishosp.hosp.dao;

import br.gov.al.maceio.sishosp.acl.model.FuncionarioBean;
import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;
import br.gov.al.maceio.sishosp.comum.util.DataUtil;
import br.gov.al.maceio.sishosp.hosp.model.Pts;
import br.gov.al.maceio.sishosp.hosp.model.PtsArea;

import javax.faces.context.FacesContext;
import java.sql.*;
import java.util.List;

public class PtsDAO {

    PreparedStatement ps = null;
    private Connection conexao = null;

    FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
            .getSessionMap().get("obj_funcionario");

    public Pts carregarPacientesInstituicaoPts(Integer id)
            throws ProjetoException {

        String sql = "SELECT pi,id, pi.codprograma, p.descprograma, pi.codgrupo, g.descgrupo, " +
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

    public Boolean gravarPts(Pts pts, List<PtsArea> listaPts) {

        Boolean retorno = false;

        String sql1 = "INSERT INTO hosp.pts (id_paciente_instituicao, data, diagnostico_funcional, necessidades_e_desejos, id_funcionario, data_hora_operacao) " +
                "values (?, ?, ?, ?, ?, CURRENT_TIMESTAMP) RETURNING id;";

        try {
            conexao = ConnectionFactory.getConnection();
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


            for(int i=0; i<listaPts.size(); i++) {
                PreparedStatement ps2 = conexao.prepareStatement(sql2);
                ps2.setInt(1, codPts);
                ps2.setInt(2, listaPts.get(i).getArea().getCodEspecialidade());
                ps2.setString(3, listaPts.get(i).getObjetivoCurto());
                ps2.setString(4, listaPts.get(i).getObjetivoMedio());
                ps2.setString(5, listaPts.get(i).getObjetivoLongo());
                ps2.setString(6, listaPts.get(i).getPlanoDeIntervencoesCurto());
                ps2.setString(7, listaPts.get(i).getPlanoDeIntervencoesMedio());
                ps2.setString(8, listaPts.get(i).getPlanoDeIntervencoesLongo());
                ps2.setLong(9, listaPts.get(i).getFuncionario().getId());
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


}
