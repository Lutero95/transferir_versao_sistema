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
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class InsercaoProfissionalEquipeDAO {

    private Connection con = null;
    private PreparedStatement ps = null;

    private List<Integer> buscarAtendimentosParaAdicionarProfissional(InsercaoProfissionalEquipe insercaoProfissionalEquipe) {

        List<Integer> lista = new ArrayList<>();
        int i = 2;

        String sql = "SELECT DISTINCT a1.id_atendimento, a.turno " +
                "FROM hosp.atendimentos1 a1 " +
                "JOIN hosp.atendimentos a ON (a1.id_atendimento = a.id_atendimento) " +
                "WHERE a.codprograma = ? " +
                " 	and a1.id_atendimento not in (\n" + 
                "	select\n" + 
                "		aa1.id_atendimento\n" + 
                "	from\n" + 
                "		hosp.atendimentos1 aa1\n" + 
                "	where\n" + 
                "		codprofissionalatendimento  =?\n" + 
                "		and coalesce(aa1.excluido, 'N')= 'N') " +
                "AND a.codequipe is not null and coalesce(a1.excluido,'N')='N' ";


        if(!insercaoProfissionalEquipe.getTurno().equals(Turno.AMBOS.getSigla())){
            sql = sql + "AND a.turno = ? ";
        }
        if ((!VerificadorUtil.verificarSeObjetoNuloOuZero(insercaoProfissionalEquipe.getGrupo())) && (!VerificadorUtil.verificarSeObjetoNuloOuZero(insercaoProfissionalEquipe.getGrupo().getIdGrupo()))) {
            sql = sql + "AND a.codgrupo = ? ";
        }
        if ( (!VerificadorUtil.verificarSeObjetoNuloOuZero(insercaoProfissionalEquipe.getEquipe())) && (!VerificadorUtil.verificarSeObjetoNuloOuZero(insercaoProfissionalEquipe.getEquipe().getCodEquipe()))) {
            sql = sql + "AND a.codequipe = ? ";
        }
        if(VerificadorUtil.verificarSeObjetoNulo(insercaoProfissionalEquipe.getPeriodoFinal())){
            sql = sql + "AND a.dtaatende >= ?";
        }
        if(!VerificadorUtil.verificarSeObjetoNulo(insercaoProfissionalEquipe.getPeriodoFinal())){
            sql = sql + "AND a.dtaatende >= ? AND a.dtaatende <= ?";
        }


        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);

            stm.setInt(1, insercaoProfissionalEquipe.getPrograma().getIdPrograma());
            stm.setLong(2, insercaoProfissionalEquipe.getFuncionario().getId());

            if(!insercaoProfissionalEquipe.getTurno().equals(Turno.AMBOS.getSigla())) {
                i++;
                stm.setString(i, insercaoProfissionalEquipe.getTurno());
            }

            if ((!VerificadorUtil.verificarSeObjetoNuloOuZero(insercaoProfissionalEquipe.getGrupo())) && (!VerificadorUtil.verificarSeObjetoNuloOuZero(insercaoProfissionalEquipe.getGrupo().getIdGrupo()))) {
                i++;
                stm.setInt(i, insercaoProfissionalEquipe.getGrupo().getIdGrupo());
            }
            if (!VerificadorUtil.verificarSeObjetoNuloOuZero(insercaoProfissionalEquipe.getEquipe().getCodEquipe())) {
                i++;
                stm.setInt(i, insercaoProfissionalEquipe.getEquipe().getCodEquipe());
            }
            if(VerificadorUtil.verificarSeObjetoNulo(insercaoProfissionalEquipe.getPeriodoFinal())){
                i++;
                stm.setDate(i, DataUtil.converterDateUtilParaDateSql(insercaoProfissionalEquipe.getPeriodoInicio()));
            }
            if(!VerificadorUtil.verificarSeObjetoNulo(insercaoProfissionalEquipe.getPeriodoFinal())){
                i++;
                stm.setDate(i, DataUtil.converterDateUtilParaDateSql(insercaoProfissionalEquipe.getPeriodoInicio()));
                i++;
                stm.setDate(i, DataUtil.converterDateUtilParaDateSql(insercaoProfissionalEquipe.getPeriodoFinal()));
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
            
            
            if((VerificadorUtil.verificarSeObjetoNuloOuZero(gravarInsercaoDTO.getInsercaoProfissionalEquipe().getGrupo())) || (VerificadorUtil.verificarSeObjetoNuloOuZero(gravarInsercaoDTO.getInsercaoProfissionalEquipe().getGrupo().getIdGrupo()))){
                ps.setNull(3, Types.NULL);
            }
            else {
            	ps.setInt(3, gravarInsercaoDTO.getInsercaoProfissionalEquipe().getGrupo().getIdGrupo());
            }            
            
            if ((VerificadorUtil.verificarSeObjetoNuloOuZero(gravarInsercaoDTO.getInsercaoProfissionalEquipe().getEquipe())) || (VerificadorUtil.verificarSeObjetoNuloOuZero(gravarInsercaoDTO.getInsercaoProfissionalEquipe().getEquipe().getCodEquipe()))){
                ps.setNull(4, Types.NULL);
            }
            else {
                ps.setInt(4, gravarInsercaoDTO.getInsercaoProfissionalEquipe().getEquipe().getCodEquipe());
            }
            ps.setDate(5, DataUtil.converterDateUtilParaDateSql(gravarInsercaoDTO.getInsercaoProfissionalEquipe().getPeriodoInicio()));
            if(VerificadorUtil.verificarSeObjetoNulo(gravarInsercaoDTO.getInsercaoProfissionalEquipe().getPeriodoFinal())){
                ps.setNull(6, Types.DATE);
            }
            else {
                ps.setDate(6, DataUtil.converterDateUtilParaDateSql(gravarInsercaoDTO.getInsercaoProfissionalEquipe().getPeriodoFinal()));
            }
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

            List<Integer> listaInsercaoProfissionalEquipes = new ArrayList<>();
            int codigoAtendimento1 = 0;

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
                    codigoAtendimento1 = rs.getInt("id_atendimentos1");
                }

                listaInsercaoProfissionalEquipes.add(codigoAtendimento1);

            }

            GravarInsercaoProfissionalEquipeAtendimento1DTO gravarInsercao1 = new GravarInsercaoProfissionalEquipeAtendimento1DTO(
                    listaInsercaoProfissionalEquipes, gravarAtendimento1.getCodInsercaoProfissionalEquipeAtendimento(), con,
                    gravarAtendimento1.getInsercaoProfissionalEquipe());

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
            	//a merda esta nesse loop
                ps.setInt(1, gravarInsercao1.getListaInsercao().get(i));
                ps.setInt(2, gravarInsercao1.getCodInsercaoProfissionalEquipeAtendimento());
                ps.setLong(3, gravarInsercao1.getInsercaoProfissionalEquipe().getFuncionario().getId());
                ps.execute();
            }



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

    public List<InsercaoProfissionalEquipe> listarInsercoesRealizadas() {

        List<InsercaoProfissionalEquipe> lista = new ArrayList<>();

        String sql = "SELECT distinct f.descfuncionario, a.data_inicio, a.data_final, p.descprograma, g.descgrupo, e.descequipe, " +
                "CASE WHEN a.turno = 'A' THEN 'AMBOS' WHEN a.turno = 'T' THEN 'TARDE' " +
                "WHEN a.turno = 'M' THEN 'MANHÃ' END AS turno " +
                "FROM adm.insercao_profissional_equipe_atendimento a " +
                "JOIN adm.insercao_profissional_equipe_atendimento_1 a1 ON (a.id = a1.id_insercao_profissional_equipe_atendimento) " +
                "JOIN acl.funcionarios f ON (a1.id_profissional = f.id_funcionario) " +
                "LEFT JOIN hosp.programa p ON (a.cod_programa = p.id_programa) " +
                "LEFT JOIN hosp.grupo g ON (a.cod_grupo = g.id_grupo) " +
                "LEFT JOIN hosp.equipe e ON (a.cod_equipe = e.id_equipe);";

        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);

            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                InsercaoProfissionalEquipe insercaoProfissionalEquipe = new InsercaoProfissionalEquipe();
                insercaoProfissionalEquipe.getFuncionario().setNome(rs.getString("descfuncionario"));
                insercaoProfissionalEquipe.setPeriodoInicio(rs.getDate("data_inicio"));
                insercaoProfissionalEquipe.setPeriodoFinal(rs.getDate("data_final"));
                insercaoProfissionalEquipe.getPrograma().setDescPrograma(rs.getString("descprograma"));
                insercaoProfissionalEquipe.getGrupo().setDescGrupo(rs.getString("descgrupo"));
                insercaoProfissionalEquipe.getEquipe().setDescEquipe(rs.getString("descequipe"));
                insercaoProfissionalEquipe.setTurno(rs.getString("turno"));

                lista.add(insercaoProfissionalEquipe);
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

}
