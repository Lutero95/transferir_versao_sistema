package br.gov.al.maceio.sishosp.administrativo.dao;

import br.gov.al.maceio.sishosp.acl.model.FuncionarioBean;
import br.gov.al.maceio.sishosp.administrativo.model.InsercaoProfissionalEquipe;
import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;
import br.gov.al.maceio.sishosp.comum.util.DataUtil;
import br.gov.al.maceio.sishosp.comum.util.JSFUtil;
import br.gov.al.maceio.sishosp.comum.util.VerificadorUtil;
import br.gov.al.maceio.sishosp.hosp.enums.Turno;

import javax.faces.context.FacesContext;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class InsercaoProfissionalEquipeDAO {

    Connection con = null;
    PreparedStatement ps = null;

    public List<Integer> buscarAtendimentosParaAdicionarProfissional(InsercaoProfissionalEquipe insercaoProfissionalEquipe) {

        List<Integer> lista = new ArrayList<>();
        int i = 4;

        String sql = "SELECT a1.id_atendimentos " +
                "FROM hosp.atendimentos1 a1 " +
                "JOIN hosp.atendimentos a ON (a1.id_atendimento = a.id_atendimento) " +
                "WHERE a.codprograma = ?   " +
                "AND a.dtaatende >= ? AND a.dtaatende <= ? ";

        if(!insercaoProfissionalEquipe.getTurno().equals(Turno.AMBOS)){
            sql = sql + "AND a.turno = ? ";
        }
        if (!VerificadorUtil.verificarSeObjetoNuloOuZero(insercaoProfissionalEquipe.getGrupo().getIdGrupo())) {
            sql = sql + "AND a.codgrupo = ? ";
            i++;
        }
        if (!VerificadorUtil.verificarSeObjetoNuloOuZero(insercaoProfissionalEquipe.getEquipe().getCodEquipe())) {
            sql = sql + "AND a.codequipe = ? ";
            i++;
        }


        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setString(1, insercaoProfissionalEquipe.getTurno());
            stm.setInt(2, insercaoProfissionalEquipe.getPrograma().getIdPrograma());
            stm.setDate(3, DataUtil.converterDateUtilParaDateSql(insercaoProfissionalEquipe.getPeriodoInicio()));
            stm.setDate(4, DataUtil.converterDateUtilParaDateSql(insercaoProfissionalEquipe.getPeriodoFinal()));

            if (!VerificadorUtil.verificarSeObjetoNuloOuZero(insercaoProfissionalEquipe.getGrupo().getIdGrupo())) {
                stm.setInt(i, insercaoProfissionalEquipe.getGrupo().getIdGrupo());
            }
            if (!VerificadorUtil.verificarSeObjetoNuloOuZero(insercaoProfissionalEquipe.getGrupo().getIdGrupo())) {
                stm.setInt(i, insercaoProfissionalEquipe.getEquipe().getCodEquipe());
            }

            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                lista.add(rs.getInt("id_atendimentos"));
            }

        } catch (Exception ex) {
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

    public boolean gravarInsercaoGeral(InsercaoProfissionalEquipe insercaoProfissionalEquipe) {
        List<Integer> lista = buscarAtendimentosParaAdicionarProfissional(insercaoProfissionalEquipe);

        if(!lista.isEmpty()){
            return gravarInsercaoProfissionalEquipeAtendimento(insercaoProfissionalEquipe, lista);
        }

        else{
            return false;
        }


    }

    public boolean gravarInsercaoProfissionalEquipeAtendimento(InsercaoProfissionalEquipe insercaoProfissionalEquipe, List<Integer> listaAtendimentos) {

        Boolean retorno = false;

        String sql = "INSERT INTO adm.insercao_profissional_equipe_atendimento " +
                "(data_hora_operacao, codusuario_operacao, cod_programa, cod_grupo, cod_equipe, data_inicio, data_final, turno) " +
                "VALUES (current_timestamp, ?, ?, ?, ?, ?, ?, ?) RETURNING id";

        try {

            FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
                    .getSessionMap().get("obj_usuario");

            con = ConnectionFactory.getConnection();
            ps = con.prepareStatement(sql);
            ps.setLong(1, user_session.getId());
            ps.setInt(2, insercaoProfissionalEquipe.getPrograma().getIdPrograma());
            ps.setInt(3, insercaoProfissionalEquipe.getGrupo().getIdGrupo());
            ps.setInt(4, insercaoProfissionalEquipe.getEquipe().getCodEquipe());
            ps.setDate(5, DataUtil.converterDateUtilParaDateSql(insercaoProfissionalEquipe.getPeriodoInicio()));
            ps.setDate(6, DataUtil.converterDateUtilParaDateSql(insercaoProfissionalEquipe.getPeriodoFinal()));
            ps.setString(7, insercaoProfissionalEquipe.getTurno());

            ResultSet rs = ps.executeQuery();

            int id = 0;

            if (rs.next()) {
                id = rs.getInt("id");
            }

            gravarInsercaoAtendimento1(insercaoProfissionalEquipe, listaAtendimentos, id, con);


            con.commit();
            retorno = true;
        } catch (Exception ex) {
            ex.printStackTrace();
            JSFUtil.adicionarMensagemErro(ex.getMessage(), "Atenção");
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

    public boolean gravarInsercaoAtendimento1(InsercaoProfissionalEquipe insercaoProfissionalEquipe,
                                              List<Integer> listaAtendimentos, Integer codInsercaoProfissionalEquipeAtendimento, Connection conAuxiliar) {

        Boolean retorno = false;

        String sql = "INSERT INTO hosp.atendimentos1 " +
                "(codprofissionalatendimento, id_atendimento, cbo, codprocedimento) " +
                "VALUES (?, ?, ?, ?) RETURNING id_atendimento1";

        try {

            ps = conAuxiliar.prepareStatement(sql);

            List<InsercaoProfissionalEquipe> listaInsercaoProfissionalEquipes = new ArrayList<>();

            for (int i = 0; i < listaAtendimentos.size(); i++) {
                ps.setLong(1, insercaoProfissionalEquipe.getFuncionario().getId());
                ps.setInt(2, listaAtendimentos.get(i));
                ps.setInt(3, insercaoProfissionalEquipe.getFuncionario().getCbo().getCodCbo());
                ps.setInt(4, insercaoProfissionalEquipe.getFuncionario().getProc1().getIdProc());

                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    insercaoProfissionalEquipe.getAtendimentoBean().setId1(rs.getInt("id_atendimento"));
                }

                listaInsercaoProfissionalEquipes.add(insercaoProfissionalEquipe);

                ps.execute();
            }

            gravarInsercaoProfissionalEquipeAtendimento1(listaInsercaoProfissionalEquipes, codInsercaoProfissionalEquipeAtendimento, con);

            retorno = true;

        } catch (Exception ex) {
            ex.printStackTrace();
            JSFUtil.adicionarMensagemErro(ex.getMessage(), "Atenção");
            throw new RuntimeException(ex);
        } finally {
            try {
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return retorno;
        }
    }

    public boolean gravarInsercaoProfissionalEquipeAtendimento1(List<InsercaoProfissionalEquipe> listaInsercaoProfissionalEquipes,
                                                                Integer codInsercaoProfissionalEquipeAtendimento, Connection conAuxiliar) {

        Boolean retorno = false;

        String sql = "INSERT INTO adm.insercao_profissional_equipe_atendimento_1 " +
                "(id_atendimentos1, id_insercao_profissional_equipe_atendimento, id_profissional) " +
                "VALUES(?, ?, ?);";

        try {

            ps = conAuxiliar.prepareStatement(sql);

            for (int i = 0; i < listaInsercaoProfissionalEquipes.size(); i++) {
                ps.setInt(1, listaInsercaoProfissionalEquipes.get(i).getAtendimentoBean().getId1());
                ps.setInt(2, codInsercaoProfissionalEquipeAtendimento);
                ps.setLong(3, listaInsercaoProfissionalEquipes.get(i).getFuncionario().getId());
            }

            ResultSet rs = ps.executeQuery();


            ps.execute();
            retorno = true;
        } catch (Exception ex) {
            ex.printStackTrace();
            JSFUtil.adicionarMensagemErro(ex.getMessage(), "Atenção");
            throw new RuntimeException(ex);
        } finally {
            try {
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return retorno;
        }
    }


}
