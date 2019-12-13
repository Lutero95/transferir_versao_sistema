package br.gov.al.maceio.sishosp.hosp.dao;

import br.gov.al.maceio.sishosp.acl.model.FuncionarioBean;
import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;
import br.gov.al.maceio.sishosp.comum.util.DataUtil;
import br.gov.al.maceio.sishosp.comum.util.VerificadorUtil;
import br.gov.al.maceio.sishosp.hosp.enums.FiltroBuscaVencimentoPTS;
import br.gov.al.maceio.sishosp.hosp.enums.StatusPTS;
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

        String sql = "SELECT id FROM hosp.pts WHERE id = ? ";

        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stmt = conexao.prepareStatement(sql);

            stmt.setInt(1, id);

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

    public String verificarStatusPts(Integer id)
            throws ProjetoException {

        String retorno = "";

        String sql = "SELECT status FROM hosp.pts WHERE id = ? ";

        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stmt = conexao.prepareStatement(sql);

            stmt.setInt(1, id);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                retorno = rs.getString("status");
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

    public Pts ptsCarregarPacientesInstituicao(Integer id)
            throws ProjetoException {

        String sql = "SELECT pi.id, pi.codprograma, p.descprograma, pi.codgrupo, g.descgrupo, " +
                "laudo.codpaciente, pa.nome, pa.cns, pa.cpf " +
                "FROM hosp.paciente_instituicao pi " +
                "LEFT JOIN hosp.laudo  ON (laudo.id_laudo = pi.codlaudo) " +
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
        return pts;
    }

    public Pts ptsCarregarPtsPorId(Integer id)
            throws ProjetoException {

        String sql = "SELECT p.id AS id_pts, pi.id, p.data, p.cod_programa, pr.descprograma, p.cod_grupo, g.descgrupo, p.cod_paciente, pa.nome, pa.cns, pa.cpf, " +
                "p.incapacidades_funcionais, p.capacidades_funcionais, p.objetivos_familiar_paciente, p.objetivos_gerais_multidisciplinar, " +
                "objetivos_gerais_curto_prazo, objetivos_gerais_medio_prazo, objetivos_gerais_longo_prazo, analise_resultados_objetivos_gerais, " +
                "novas_estrategias_tratamento, condulta_alta " +
                "FROM hosp.pts p " +
                "LEFT JOIN hosp.programa pr ON (pr.id_programa = p.cod_programa) " +
                "LEFT JOIN hosp.grupo g ON (p.cod_grupo = g.id_grupo) " +
                "LEFT JOIN hosp.pacientes pa ON (pa.id_paciente = p.cod_paciente) " +
                "LEFT JOIN hosp.paciente_instituicao pi ON (pi.codgrupo = p.cod_grupo AND pi.codprograma = p.cod_programa) " +
                "LEFT JOIN hosp.laudo  ON (laudo.id_laudo = pi.codlaudo) " +
                "WHERE pi.status = 'A' and  coalesce(laudo.codpaciente, pi.id_paciente)  = p.cod_paciente AND p.id = ?;";


        Pts pts = new Pts();

        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stmt = conexao.prepareStatement(sql);

            stmt.setInt(1, id);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                pts = new Pts();
                pts.getGerenciarPaciente().setId(rs.getInt("id"));
                pts.getPrograma().setIdPrograma(rs.getInt("cod_programa"));
                pts.getPrograma().setDescPrograma(rs.getString("descprograma"));
                pts.getGrupo().setIdGrupo(rs.getInt("cod_grupo"));
                pts.getGrupo().setDescGrupo(rs.getString("descgrupo"));
                pts.getPaciente().setId_paciente(rs.getInt("cod_paciente"));
                pts.getPaciente().setNome(rs.getString("nome"));
                pts.getPaciente().setCpf(rs.getString("cpf"));
                pts.getPaciente().setCns(rs.getString("cns"));
                pts.setId(rs.getInt("id_pts"));
                pts.setData(rs.getDate("data"));
                pts.setIncapacidadesFuncionais(rs.getString("incapacidades_funcionais"));
                pts.setCapacidadesFuncionais(rs.getString("capacidades_funcionais"));
                pts.setObjetivosFamiliarPaciente(rs.getString("objetivos_familiar_paciente"));
                pts.setObjetivosGeraisMultidisciplinar(rs.getString("objetivos_gerais_multidisciplinar"));
                pts.setObjetivosGeraisCurtoPrazo(rs.getString("objetivos_gerais_curto_prazo"));
                pts.setObjetivosGeraisMedioPrazo(rs.getString("objetivos_gerais_medio_prazo"));
                pts.setObjetivosGeraisLongoPrazo(rs.getString("objetivos_gerais_longo_prazo"));
                pts.setAnaliseDosResultadosDosObjetivosGerias(rs.getString("analise_resultados_objetivos_gerais"));
                pts.setNovasEstrategiasDeTratamento(rs.getString("novas_estrategias_tratamento"));
                pts.setCondultaAlta(rs.getString("condulta_alta"));
                pts.setListaPtsArea(carregarAreasPts(id, conexao));

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
        return pts;
    }

    public List<PtsArea> carregarAreasPts(Integer id, Connection conAuxiliar) {

        String sql = "SELECT pa.id, pa.id_area, e.descespecialidade, f.descfuncionario, pa.objetivo_curto, pa.objetivo_medio, pa.objetivo_longo, " +
                "pa.plano_curto, pa.plano_medio, pa.plano_longo, pa.id_funcionario " +
                "FROM hosp.pts_area pa " +
                "LEFT JOIN hosp.especialidade e ON (pa.id_area = e.id_especialidade) " +
                " left join acl.funcionarios f on f.id_funcionario = pa.id_funcionario " +
                " LEFT JOIN hosp.pts p ON (pa.id_pts = p.id) " +
                "WHERE p.id = ? ";

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
                ptsArea.getFuncionario().setNome(rs.getString("descfuncionario"));

                lista.add(ptsArea);

            }
        } catch (Exception ex) {
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
        return ptsArea;
    }

    public Integer gravarPts(Pts pts, Boolean ehParaDeletar, String statusPTS) {

        Integer retorno = null;

        final Integer SEIS_MESES_VENCIMENTO = 180;

        String sql1 = "INSERT INTO hosp.pts (data, id_funcionario, data_hora_operacao, data_vencimento, cod_programa, cod_grupo, cod_paciente, status, " +
                "incapacidades_funcionais, capacidades_funcionais, objetivos_familiar_paciente, objetivos_gerais_multidisciplinar, " +
                "objetivos_gerais_curto_prazo, objetivos_gerais_medio_prazo, objetivos_gerais_longo_prazo, analise_resultados_objetivos_gerais, " +
                "novas_estrategias_tratamento, condulta_alta, cod_unidade) " +
                "values (?, ?, CURRENT_TIMESTAMP, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING id;";

        try {
            conexao = ConnectionFactory.getConnection();

            if (ehParaDeletar) {
                verificarExclusoesPtsIhArea(excluirListaArea(pts.getId(), conexao), excluirPts(pts.getId(), conexao), conexao);
            }

            ps = conexao.prepareStatement(sql1);
            ps.setDate(1, DataUtil.converterDateUtilParaDateSql(pts.getData()));
            ps.setLong(2, user_session.getId());
            ps.setDate(3, DataUtil.converterDateUtilParaDateSql(DataUtil.adicionarDiasAData(pts.getData(), SEIS_MESES_VENCIMENTO)));
            ps.setInt(4, pts.getPrograma().getIdPrograma());
            ps.setInt(5, pts.getGrupo().getIdGrupo());
            ps.setInt(6, pts.getPaciente().getId_paciente());
            ps.setString(7, statusPTS);
            ps.setString(8, pts.getIncapacidadesFuncionais());
            ps.setString(9, pts.getCapacidadesFuncionais());
            ps.setString(10, pts.getObjetivosFamiliarPaciente());
            ps.setString(11, pts.getObjetivosGeraisMultidisciplinar());
            ps.setString(12, pts.getObjetivosGeraisCurtoPrazo());
            ps.setString(13, pts.getObjetivosGeraisMedioPrazo());
            ps.setString(14, pts.getObjetivosGeraisLongoPrazo());
            ps.setString(15, pts.getAnaliseDosResultadosDosObjetivosGerias());
            ps.setString(16, pts.getNovasEstrategiasDeTratamento());
            ps.setString(17, pts.getCondultaAlta());
            ps.setInt(18, user_session.getUnidade().getId());

            ResultSet rs = ps.executeQuery();
            Integer codPts = null;

            if (rs.next()) {
                codPts = rs.getInt("id");
            }


            if (inserirAreaPts(pts, codPts, conexao)) {
                if (statusPTS.equals(StatusPTS.RENOVADO.getSigla())) {
                    if (desativarPts(pts.getId(), conexao)) {
                        conexao.commit();
                    } else {
                        codPts = null;
                    }
                } else {
                    //codPts = null;
                    conexao.commit();
                }

            } else {
                codPts = null;
            }

            conexao.close();

            retorno = codPts;

        } catch (Exception ex) {
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

    public Boolean alterarPts(Pts pts, String statusPTS, FuncionarioBean usuarioLiberacao) {

        Boolean retorno = false;

        final Integer SEIS_MESES_VENCIMENTO = 180;


        String sql1 = "UPDATE hosp.pts SET " +
                "data=?, id_funcionario=?, data_hora_operacao=CURRENT_TIMESTAMP, data_vencimento=?, cod_programa=?, cod_grupo=?, cod_paciente=?, status=?, " +
                "incapacidades_funcionais=?, capacidades_funcionais=?, objetivos_familiar_paciente=?, objetivos_gerais_multidisciplinar=?, "+
                "objetivos_gerais_curto_prazo=?, objetivos_gerais_medio_prazo=?, objetivos_gerais_longo_prazo=?, analise_resultados_objetivos_gerais=?, " +
                "novas_estrategias_tratamento=?, condulta_alta=?, cod_unidade=? " +
                "where id=?";


        try {
            conexao = ConnectionFactory.getConnection();

            Boolean verificarSeDataPtsMudou = verificarSeDataPtsMudou(DataUtil.converterDateUtilParaDateSql(pts.getData()), pts.getId());

            ps = conexao.prepareStatement(sql1);
            ps.setDate(1, DataUtil.converterDateUtilParaDateSql(pts.getData()));
            ps.setLong(2, user_session.getId());
            ps.setDate(3, DataUtil.converterDateUtilParaDateSql(DataUtil.adicionarDiasAData(pts.getData(), SEIS_MESES_VENCIMENTO)));
            ps.setInt(4, pts.getPrograma().getIdPrograma());
            ps.setInt(5, pts.getGrupo().getIdGrupo());
            ps.setInt(6, pts.getPaciente().getId_paciente());
            ps.setString(7, statusPTS);
            ps.setString(8, pts.getIncapacidadesFuncionais());
            ps.setString(9, pts.getCapacidadesFuncionais());
            ps.setString(10, pts.getObjetivosFamiliarPaciente());
            ps.setString(11, pts.getObjetivosGeraisMultidisciplinar());
            ps.setString(12, pts.getObjetivosGeraisCurtoPrazo());
            ps.setString(13, pts.getObjetivosGeraisMedioPrazo());
            ps.setString(14, pts.getObjetivosGeraisLongoPrazo());
            ps.setString(15, pts.getAnaliseDosResultadosDosObjetivosGerias());
            ps.setString(16, pts.getNovasEstrategiasDeTratamento());
            ps.setString(17, pts.getCondultaAlta());
            ps.setInt(18, user_session.getUnidade().getId());
            ps.setInt(19, pts.getId());

           ps.executeUpdate();

            excluirListaArea(pts.getId(), conexao);
            inserirAreaPts(pts, pts.getId(), conexao);

            if (verificarSeDataPtsMudou) {
                inserirLiberacaoPts(pts.getId(), usuarioLiberacao, conexao);
            }

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
            return retorno;
        }
    }

    public Boolean inserirAreaPts(Pts pts, Integer codPts, Connection conAuxiliar) {

        Boolean retorno = false;

        String sql = "INSERT INTO hosp.pts_area (id_pts, id_area, objetivo_curto, objetivo_medio, objetivo_longo, plano_curto, plano_medio, plano_longo, " +
                "id_funcionario, data_hora_operacao) values (?, ?, ?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP);";


        try {

            for (int i = 0; i < pts.getListaPtsArea().size(); i++) {
                PreparedStatement ps2 = conAuxiliar.prepareStatement(sql);
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

            retorno = true;

        } catch (Exception ex) {
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

    public Boolean inserirLiberacaoPts(Integer codPts, FuncionarioBean usuarioLiberacao, Connection conAuxiliar) {

        Boolean retorno = false;

        String sql = "INSERT INTO hosp.liberacoes (motivo, usuario_liberacao, data_hora_liberacao, id_pts) " +
                "values (?, ?, CURRENT_TIMESTAMP, ?);";


        try {

            PreparedStatement ps2 = conAuxiliar.prepareStatement(sql);
            ps2.setString(1, "LIBERAÇÃO PARA ALTERAR A DATA DO PTS");
            ps2.setLong(2, usuarioLiberacao.getId());
            ps2.setInt(3, codPts);
            ps2.execute();

            retorno = true;

        } catch (Exception ex) {
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

    public Boolean desativarPts(Integer codPts, Connection conAuxiliar) {

        Boolean retorno = false;

        String sql = "UPDATE hosp.pts SET status = ? WHERE id = ?;";


        try {
            PreparedStatement ps = conAuxiliar.prepareStatement(sql);
            ps.setString(1, StatusPTS.INVALIDADO.getSigla());
            ps.setInt(2, codPts);
            ps.execute();

            retorno = true;

        } catch (Exception ex) {
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

    public Boolean excluirListaArea(Integer id, Connection conAuxiliar) {

        boolean retorno = false;

        String sql = "DELETE FROM hosp.pts_area WHERE id_pts = ?";

        try {
            PreparedStatement stmt = conAuxiliar.prepareStatement(sql);
            stmt.setInt(1, id);
            stmt.executeUpdate();

            retorno = true;
        } catch (Exception ex) {
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
        } catch (Exception ex) {
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

    public Boolean verificarSeExistePtsParaProgramaGrupoPaciente(Pts pts) throws ProjetoException {

        Boolean retorno = false;

        String sql = "SELECT id FROM hosp.pts WHERE cod_programa = ? AND cod_grupo = ? AND cod_paciente = ? AND status IN ('A', 'R');";

        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stm = conexao.prepareStatement(sql);
            stm.setInt(1, pts.getPrograma().getIdPrograma());
            stm.setInt(2, pts.getGrupo().getIdGrupo());
            stm.setInt(3, pts.getPaciente().getId_paciente());

            ResultSet rs = stm.executeQuery();

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

    public List<Pts> buscarPtsPacientesAtivos(Integer codPrograma, Integer codGrupo, String tipoFiltroVencimento,
                                              Integer filtroMesVencimento, Integer filtroAnoVencimento, Boolean filtroApenasPacientesSemPTS, String campoBusca, String tipoBusca)
            throws ProjetoException {

        //Inicia com 2, pois é o número mínimo de parametros.

        String sql = "SELECT p.id, pi.id as id_paciente_instituicao, p.data, p.data_vencimento, g.descgrupo, pr.descprograma, pa.nome, p.status, " +
                "pi.codgrupo, pi.codprograma, pa.id_paciente codpaciente, pa.cpf, pa.cns, pa.matricula, " +
                "CASE WHEN p.status = 'A' THEN 'Ativo' WHEN p.status = 'C' THEN 'Cancelado' " +
                "WHEN p.status = 'R' THEN 'Renovado' END AS status_por_extenso " +
                "FROM hosp.paciente_instituicao pi " +
                "LEFT JOIN hosp.laudo  ON (laudo.id_laudo = pi.codlaudo) " +
                "LEFT JOIN hosp.programa pr ON (pr.id_programa = pi.codprograma) " +
                "LEFT JOIN hosp.grupo g ON (g.id_grupo = pi.codgrupo) " +
                "LEFT JOIN hosp.pacientes pa ON (pa.id_paciente = coalesce(laudo.codpaciente, pi.id_paciente) ) " +
                "LEFT JOIN hosp.pts p ON " +
                "(p.cod_grupo = pi.codgrupo) AND (p.cod_programa = pi.codprograma) AND (p.cod_paciente =coalesce(laudo.codpaciente, pi.id_paciente)) and  coalesce(p.status,'')<>'C' and coalesce(p.status,'')<>'I' " +
                " left join (select p2.id, p2.cod_grupo, p2.cod_programa, p2.cod_paciente from hosp.pts p2 where p2.id = " +
                " (select max(id) from hosp.pts p3 " +
                " where (p3.cod_grupo = p2.cod_grupo) AND (p3.cod_programa = p2.cod_programa) AND (p3.cod_paciente = p2.cod_paciente) " +
                " and  coalesce(p3.status,'')<>'C' and coalesce(p3.status,'')<>'I' ) ) ptsmax on " +
                " ptsmax.cod_grupo = p.cod_grupo AND ptsmax.cod_programa = p.cod_programa AND ptsmax.cod_paciente = p.cod_paciente " +
                "WHERE pi.status = 'A'  AND pi.codgrupo = ? AND pi.codprograma = ? and (case when p.id is not null then p.id=ptsmax.id else 1=1 end)";
        if (filtroApenasPacientesSemPTS)
            sql = sql + " and not exists (select id from hosp.pts where pts.status='A' and pts.cod_programa=pi.codprograma and pts.cod_grupo=pi.codgrupo and pts.cod_paciente=laudo.codpaciente)";


        if ((tipoBusca.equals("paciente") && (!campoBusca.equals(null)) && (!campoBusca.equals("")))) {
            sql = sql + " and pa.nome ilike ?";
        }

        if ((tipoBusca.equals("matpaciente") && (!campoBusca.equals(null)) && (!campoBusca.equals("")))) {
            sql = sql + " and pa.matricula = ?";
        }

        if ((tipoBusca.equals("prontpaciente") && (!campoBusca.equals(null)) && (!campoBusca.equals("")))) {
            sql = sql + " and pa.id_paciente = ?";
        }


        if (tipoFiltroVencimento.equals(FiltroBuscaVencimentoPTS.VINGENTES.getSigla())) {
            sql = sql + " AND p.data_vencimento >= current_date";
        }

        if (tipoFiltroVencimento.equals(FiltroBuscaVencimentoPTS.VENCIDOS.getSigla())) {
            sql = sql + " AND p.data_vencimento < current_date";
        }

        if (!VerificadorUtil.verificarSeObjetoNuloOuZero(filtroMesVencimento)) {
            sql = sql + " AND EXTRACT(month FROM p.data_vencimento) = ? ";
        }

        if (!VerificadorUtil.verificarSeObjetoNuloOuZero(filtroAnoVencimento)) {
            sql = sql + " AND EXTRACT(year FROM p.data_vencimento) = ? ";
        }

        sql = sql + " ORDER BY p.data ";

        List<Pts> lista = new ArrayList<>();

        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setInt(1, codGrupo);
            stmt.setInt(2, codPrograma);
            int i = 3;
            if (!VerificadorUtil.verificarSeObjetoNuloOuZero(filtroMesVencimento)) {
                stmt.setInt(i, filtroMesVencimento);
                i++;
            }

            if (!VerificadorUtil.verificarSeObjetoNuloOuZero(filtroAnoVencimento)) {
                stmt.setInt(i, filtroAnoVencimento);
                i++;
            }

            if (((tipoBusca.equals("paciente")) && (!campoBusca.equals(null)) && (!campoBusca.equals("")))) {
                stmt.setString(i, "%" + campoBusca.toUpperCase() + "%");
                i++;
                ;
            }


            if (((tipoBusca.equals("codproc")) || (tipoBusca.equals("matpaciente"))) && (!campoBusca.equals(null)) && (!campoBusca.equals(""))) {
                stmt.setString(i, campoBusca.toUpperCase());
                i++;
            }

            if ((tipoBusca.equals("prontpaciente") && (!campoBusca.equals(null)) && (!campoBusca.equals("")))) {
                stmt.setInt(i, Integer.valueOf(campoBusca));
                i++;
            }

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Pts pts = new Pts();
                pts.setId(rs.getInt("id"));
                pts.getGerenciarPaciente().setId(rs.getInt("id_paciente_instituicao"));
                pts.setData(rs.getDate("data"));
                pts.setDataVencimento(rs.getDate("data_vencimento"));
                pts.getGrupo().setIdGrupo(rs.getInt("codgrupo"));
                pts.getGrupo().setDescGrupo(rs.getString("descgrupo"));
                pts.getPrograma().setIdPrograma(rs.getInt("codprograma"));
                pts.getPrograma().setDescPrograma(rs.getString("descprograma"));
                pts.getPaciente().setId_paciente(rs.getInt("codpaciente"));
                pts.getPaciente().setNome(rs.getString("nome"));
                pts.getPaciente().setCns(rs.getString("cns"));
                pts.getPaciente().setCpf(rs.getString("cpf"));
                pts.getPaciente().setMatricula(rs.getString("matricula"));
                pts.setStatus(rs.getString("status"));
                pts.setStatusPorExtenso(rs.getString("status_por_extenso"));
                lista.add(pts);
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

    public Pts carregarPtsDoPaciente(Integer codPaciente) throws ProjetoException {


        String sql = "SELECT p.id AS id_pts, p.data, p.cod_programa, pr.descprograma, p.cod_grupo, g.descgrupo, p.cod_paciente, pa.nome, pa.cns, pa.cpf, " +
                "p.incapacidades_funcionais, p.capacidades_funcionais, p.objetivos_familiar_paciente, p.objetivos_gerais_multidisciplinar, " +
                "objetivos_gerais_curto_prazo, objetivos_gerais_medio_prazo, objetivos_gerais_longo_prazo, analise_resultados_objetivos_gerais, " +
                "novas_estrategias_tratamento, condulta_alta " +
                "FROM hosp.pts p " +
                "LEFT JOIN hosp.programa pr ON (pr.id_programa = p.cod_programa) " +
                "LEFT JOIN hosp.grupo g ON (p.cod_grupo = g.id_grupo) " +
                "LEFT JOIN hosp.pacientes pa ON (pa.id_paciente = p.cod_paciente) " +
                "LEFT JOIN hosp.paciente_instituicao pi ON (pi.codgrupo = p.cod_grupo AND pi.codprograma = p.cod_programa) " +
                "LEFT JOIN hosp.laudo  ON (laudo.id_laudo = pi.codlaudo) " +
                "WHERE laudo.codpaciente = ?";

        Pts pts = new Pts();

        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stm = conexao.prepareStatement(sql);
            stm.setInt(1, codPaciente);

            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                pts.getPrograma().setIdPrograma(rs.getInt("cod_programa"));
                pts.getPrograma().setDescPrograma(rs.getString("descprograma"));
                pts.getGrupo().setIdGrupo(rs.getInt("cod_grupo"));
                pts.getGrupo().setDescGrupo(rs.getString("descgrupo"));
                pts.getPaciente().setId_paciente(rs.getInt("cod_paciente"));
                pts.getPaciente().setNome(rs.getString("nome"));
                pts.getPaciente().setCpf(rs.getString("cpf"));
                pts.getPaciente().setCns(rs.getString("cns"));
                pts.setId(rs.getInt("id_pts"));
                pts.setData(rs.getDate("data"));
                pts.setIncapacidadesFuncionais(rs.getString("incapacidades_funcionais"));
                pts.setCapacidadesFuncionais(rs.getString("capacidades_funcionais"));
                pts.setObjetivosFamiliarPaciente(rs.getString("objetivos_familiar_paciente"));
                pts.setObjetivosGeraisMultidisciplinar(rs.getString("objetivos_gerais_multidisciplinar"));
                pts.setObjetivosGeraisCurtoPrazo(rs.getString("objetivos_gerais_curto_prazo"));
                pts.setObjetivosGeraisMedioPrazo(rs.getString("objetivos_gerais_medio_prazo"));
                pts.setObjetivosGeraisLongoPrazo(rs.getString("objetivos_gerais_longo_prazo"));
                pts.setAnaliseDosResultadosDosObjetivosGerias(rs.getString("analise_resultados_objetivos_gerais"));
                pts.setNovasEstrategiasDeTratamento(rs.getString("novas_estrategias_tratamento"));
                pts.setCondultaAlta(rs.getString("condulta_alta"));
                pts.setListaPtsArea(carregarAreasPts(pts.getId(), conexao));

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
        return pts;
    }

    public Boolean cancelarPts(Integer id) {

        Boolean retorno = false;

        String sql = "UPDATE hosp.pts SET status = ?, operador_cancelamento = ?, data_hora_cancelamento = CURRENT_TIMESTAMP WHERE id = ?;";

        try {
            conexao = ConnectionFactory.getConnection();

            ps = conexao.prepareStatement(sql);
            ps.setString(1, StatusPTS.CANCELADO.getSigla());
            ps.setLong(2, user_session.getId());
            ps.setInt(3, id);
            ps.executeUpdate();

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
            return retorno;
        }
    }

    public Boolean verificarSeDataPtsMudou(Date data, Integer idPts) {

        Boolean retorno = false;

        String sql = "SELECT CASE WHEN DATA <> ? THEN TRUE ELSE FALSE END AS data_mudou FROM hosp.pts WHERE id = ?;";

        Connection conexao1 = null;

        try {
            conexao1 = ConnectionFactory.getConnection();
            PreparedStatement stm = conexao1.prepareStatement(sql);
            stm.setDate(1, data);
            stm.setInt(2, idPts);

            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                retorno = rs.getBoolean("data_mudou");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        } finally {
            try {
                conexao1.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return retorno;
    }

}
