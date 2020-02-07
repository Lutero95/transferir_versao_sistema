package br.gov.al.maceio.sishosp.administrativo.dao;

import br.gov.al.maceio.sishosp.acl.model.FuncionarioBean;
import br.gov.al.maceio.sishosp.administrativo.enums.RetornoGravarInsercaoProfissionalAtendimento;
import br.gov.al.maceio.sishosp.administrativo.model.InsercaoProfissionalEquipe;
import br.gov.al.maceio.sishosp.administrativo.model.dto.GravarInsercaoAtendimento1DTO;
import br.gov.al.maceio.sishosp.administrativo.model.dto.GravarInsercaoProfissionalEquipeAtendimento1DTO;
import br.gov.al.maceio.sishosp.administrativo.model.dto.GravarInsercaoProfissionalEquipeAtendimentoDTO;
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

    private Connection con = null;
    private PreparedStatement ps = null;

    private List<Integer> buscarAtendimentosParaAdicionarProfissional(InsercaoProfissionalEquipe insercaoProfissionalEquipe) {

        List<Integer> lista = new ArrayList<>();
        int i = 4;

        String sql = "SELECT DISTINCT a1.id_atendimento, a.turno " +
                "FROM hosp.atendimentos1 a1 " +
                "JOIN hosp.atendimentos a ON (a1.id_atendimento = a.id_atendimento) " +
                "WHERE a.codprograma = ? " +
                "AND NOT EXISTS (SELECT aa1.codprofissionalatendimento FROM hosp.atendimentos1 aa1 WHERE codprofissionalatendimento IN (?)) " +
                "AND a.dtaatende >= ? AND a.dtaatende <= ? ";

        if(!insercaoProfissionalEquipe.getTurno().equals(Turno.AMBOS.getSigla())){
            sql = sql + "AND a.turno = ? ";
        }
        if (!VerificadorUtil.verificarSeObjetoNuloOuZero(insercaoProfissionalEquipe.getGrupo().getIdGrupo())) {
            sql = sql + "AND a.codgrupo = ? ";
        }
        if (!VerificadorUtil.verificarSeObjetoNuloOuZero(insercaoProfissionalEquipe.getEquipe().getCodEquipe())) {
            sql = sql + "AND a.codequipe = ? ";
        }


        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);

            stm.setInt(1, insercaoProfissionalEquipe.getPrograma().getIdPrograma());
            stm.setLong(2, insercaoProfissionalEquipe.getFuncionario().getId());
            stm.setDate(3, DataUtil.converterDateUtilParaDateSql(insercaoProfissionalEquipe.getPeriodoInicio()));
            stm.setDate(4, DataUtil.converterDateUtilParaDateSql(insercaoProfissionalEquipe.getPeriodoFinal()));

            if(!insercaoProfissionalEquipe.getTurno().equals(Turno.AMBOS.getSigla())) {
                i++;
                stm.setString(i, insercaoProfissionalEquipe.getTurno());
            }

            if (!VerificadorUtil.verificarSeObjetoNuloOuZero(insercaoProfissionalEquipe.getGrupo().getIdGrupo())) {
                i++;
                stm.setInt(i, insercaoProfissionalEquipe.getGrupo().getIdGrupo());
            }
            if (!VerificadorUtil.verificarSeObjetoNuloOuZero(insercaoProfissionalEquipe.getGrupo().getIdGrupo())) {
                i++;
                stm.setInt(i, insercaoProfissionalEquipe.getEquipe().getCodEquipe());
            }

            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                lista.add(rs.getInt("id_atendimento"));
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

    public String gravarInsercaoGeral(InsercaoProfissionalEquipe insercaoProfissionalEquipe) {
        List<Integer> lista = buscarAtendimentosParaAdicionarProfissional(insercaoProfissionalEquipe);

        if(!lista.isEmpty()){

            GravarInsercaoProfissionalEquipeAtendimentoDTO gravarInsercaoDTO =
                    new GravarInsercaoProfissionalEquipeAtendimentoDTO(insercaoProfissionalEquipe, lista);

            if(gravarInsercaoProfissionalEquipeAtendimento(gravarInsercaoDTO)){
                return RetornoGravarInsercaoProfissionalAtendimento.SUCESSO_GRAVACAO.getSigla();
            }
            else{
                return RetornoGravarInsercaoProfissionalAtendimento.FALHA_GRAVACAO.getSigla();
            }
        }

        else{
            return RetornoGravarInsercaoProfissionalAtendimento.FALHA_PROFISSIONAL.getSigla();
        }


    }

    private boolean gravarInsercaoProfissionalEquipeAtendimento(GravarInsercaoProfissionalEquipeAtendimentoDTO gravarInsercaoDTO) {

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
            ps.setInt(2, gravarInsercaoDTO.getInsercaoProfissionalEquipe().getPrograma().getIdPrograma());
            ps.setInt(3, gravarInsercaoDTO.getInsercaoProfissionalEquipe().getGrupo().getIdGrupo());
            ps.setInt(4, gravarInsercaoDTO.getInsercaoProfissionalEquipe().getEquipe().getCodEquipe());
            ps.setDate(5, DataUtil.converterDateUtilParaDateSql(gravarInsercaoDTO.getInsercaoProfissionalEquipe().getPeriodoInicio()));
            ps.setDate(6, DataUtil.converterDateUtilParaDateSql(gravarInsercaoDTO.getInsercaoProfissionalEquipe().getPeriodoFinal()));
            ps.setString(7, gravarInsercaoDTO.getInsercaoProfissionalEquipe().getTurno());

            ResultSet rs = ps.executeQuery();

            int id = 0;

            if (rs.next()) {
                id = rs.getInt("id");
            }

            GravarInsercaoAtendimento1DTO gravarAtendimento1 = new GravarInsercaoAtendimento1DTO(
                    gravarInsercaoDTO.getInsercaoProfissionalEquipe(), gravarInsercaoDTO.getListaAtendimentos(), id, con);

            retorno = gravarInsercaoAtendimento1(gravarAtendimento1);

            if(retorno) {
                con.commit();
            }

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

    private boolean gravarInsercaoAtendimento1(GravarInsercaoAtendimento1DTO gravarAtendimento1) {

        Boolean retorno = false;

        String sql = "INSERT INTO hosp.atendimentos1 " +
                "(codprofissionalatendimento, id_atendimento, cbo, codprocedimento) " +
                "VALUES (?, ?, ?, ?) RETURNING id_atendimentos1";

        try {

            ps = gravarAtendimento1.getConexaoAuxiliar().prepareStatement(sql);

            List<InsercaoProfissionalEquipe> listaInsercaoProfissionalEquipes = new ArrayList<>();

            for (int i = 0; i < gravarAtendimento1.getListaAtendimentos().size(); i++) {
                ps.setLong(1, gravarAtendimento1.getInsercaoProfissionalEquipe().getFuncionario().getId());
                ps.setInt(2, gravarAtendimento1.getListaAtendimentos().get(i));

                if (!VerificadorUtil.verificarSeObjetoNuloOuZero(gravarAtendimento1.getInsercaoProfissionalEquipe().getFuncionario().getCbo().getCodCbo())) {
                    ps.setInt(3, gravarAtendimento1.getInsercaoProfissionalEquipe().getFuncionario().getCbo().getCodCbo());
                } else {
                    ps.setNull(3, java.sql.Types.NULL);
                }


                if (!VerificadorUtil.verificarSeObjetoNuloOuZero(gravarAtendimento1.getInsercaoProfissionalEquipe().getFuncionario().getProc1().getIdProc())) {
                    ps.setInt(4, gravarAtendimento1.getInsercaoProfissionalEquipe().getFuncionario().getProc1().getIdProc());
                } else {
                    ps.setNull(4, java.sql.Types.NULL);
                }

                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    gravarAtendimento1.getInsercaoProfissionalEquipe().getAtendimentoBean().setId1(rs.getInt("id_atendimentos1"));
                }

                listaInsercaoProfissionalEquipes.add(gravarAtendimento1.getInsercaoProfissionalEquipe());

            }

            GravarInsercaoProfissionalEquipeAtendimento1DTO gravarInsercao1 = new GravarInsercaoProfissionalEquipeAtendimento1DTO(
                    listaInsercaoProfissionalEquipes, gravarAtendimento1.getCodInsercaoProfissionalEquipeAtendimento(), con);

            retorno = gravarInsercaoProfissionalEquipeAtendimento1(gravarInsercao1);


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

    private boolean gravarInsercaoProfissionalEquipeAtendimento1(GravarInsercaoProfissionalEquipeAtendimento1DTO gravarInsercao1) {

        Boolean retorno = false;

        String sql = "INSERT INTO adm.insercao_profissional_equipe_atendimento_1 " +
                "(id_atendimentos1, id_insercao_profissional_equipe_atendimento, id_profissional) " +
                "VALUES(?, ?, ?);";

        try {

            ps = gravarInsercao1.getConexaoAuxiliar().prepareStatement(sql);

            for (int i = 0; i < gravarInsercao1.getListaInsercao().size(); i++) {
                ps.setInt(1, gravarInsercao1.getListaInsercao().get(i).getAtendimentoBean().getId1());
                ps.setInt(2, gravarInsercao1.getCodInsercaoProfissionalEquipeAtendimento());
                ps.setLong(3, gravarInsercao1.getListaInsercao().get(i).getFuncionario().getId());
            }

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
