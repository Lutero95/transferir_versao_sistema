package br.gov.al.maceio.sishosp.questionario.dao;

import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;
import br.gov.al.maceio.sishosp.questionario.model.Pestalozzi;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PestalozziDAO {
    //conexao = ConnectionFactory.getConnection();
    Connection conexao = null;


    public Boolean gravarQuestionario(Pestalozzi pestalozzi, Integer idPaciente) throws ProjetoException {
        boolean retorno = false;
        String sql = "INSERT INTO questionario_social.questionario_social_pestalozzimaceio VALUES id_paciente RETURNING id";
        this.conexao = ConnectionFactory.getConnection();
        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setInt(1, idPaciente);
            ResultSet rs = stmt.executeQuery();
            Integer idInsercao = rs.getInt("id");

            //MÉTODOS PARA INSERIR EM CADA CATEGORIA ESPECIFICA

            conexao.commit();
            retorno = true;
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        } finally {
            try {
                conexao.close();
            } catch (Exception ex) {
                ex.printStackTrace();
                System.exit(1);
            }
        }
        return retorno;
    }
}
