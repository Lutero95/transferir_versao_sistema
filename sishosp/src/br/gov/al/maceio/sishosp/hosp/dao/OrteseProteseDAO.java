package br.gov.al.maceio.sishosp.hosp.dao;

import br.gov.al.maceio.sishosp.acl.model.FuncionarioBean;
import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;
import br.gov.al.maceio.sishosp.hosp.model.OrteseProtese;

import javax.faces.context.FacesContext;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OrteseProteseDAO {

    Connection con = null;
    PreparedStatement ps = null;
    FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
            .getSessionMap().get("obj_funcionario");

    public OrteseProtese carregarOrteseIhProtese() {

        OrteseProtese orteseProtese = new OrteseProtese();

        String sql = "SELECT pa.programa_ortese_protese, pr.descprograma, pa.grupo_ortese_protese, g.descgrupo " +
                "FROM hosp.parametro pa " +
                "JOIN hosp.programa pr ON (pr.id_programa = pa.programa_ortese_protese) " +
                "JOIN hosp.grupo g ON (g.id_grupo = pa.grupo_ortese_protese) " +
                "WHERE pa.cod_empresa = ?;";

        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, user_session.getEmpresa().getCodEmpresa());
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                orteseProtese.getPrograma().setIdPrograma(rs.getInt("programa_ortese_protese"));
                orteseProtese.getPrograma().setDescPrograma(rs.getString("descprograma"));
                orteseProtese.getGrupo().setIdGrupo(rs.getInt("grupo_ortese_protese"));
                orteseProtese.getGrupo().setDescGrupo(rs.getString("descgrupo"));
            }
        } catch (SQLException | ProjetoException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        } finally {
            try {
                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return orteseProtese;
    }
}
