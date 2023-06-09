package br.gov.al.maceio.sishosp.hosp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.context.FacesContext;

import br.gov.al.maceio.sishosp.acl.model.FuncionarioBean;
import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;
import br.gov.al.maceio.sishosp.comum.util.DataUtil;
import br.gov.al.maceio.sishosp.comum.util.TratamentoErrosUtil;
import br.gov.al.maceio.sishosp.comum.util.VerificadorUtil;
import br.gov.al.maceio.sishosp.hosp.model.GrupoBean;
import br.gov.al.maceio.sishosp.hosp.model.ParametroBean;
import br.gov.al.maceio.sishosp.hosp.model.ProgramaBean;
import br.gov.al.maceio.sishosp.hosp.model.ProgramaGrupoEvolucaoBean;
import br.gov.al.maceio.sishosp.hosp.model.UnidadeBean;

public class UnidadeDAO {

    Connection con = null;
    PreparedStatement ps = null;
    FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
            .getSessionMap().get("obj_funcionario");

    public boolean gravarUnidade(UnidadeBean unidade) throws ProjetoException {
        Boolean retorno = false;
        Integer codUnidade = null;
        String sql = "INSERT INTO hosp.unidade(rua, bairro, numero, cep, cidade, " +
                " estado, complemento, ddd_1, telefone_1, ddd_2, telefone_2, email, site, matriz, ativo, nome,  cod_empresa) " +
                " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?,  ?, ?, ?, ?, ?, true, ?, ?) returning id";

        try {
            con = ConnectionFactory.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, unidade.getRua());
            ps.setString(2, unidade.getBairro());
            if(unidade.getNumero() != null) {
                ps.setInt(3, unidade.getNumero());
            }
            else{
                ps.setNull(3, Types.NULL);
            }
            ps.setString(4, unidade.getCep());
            ps.setString(5, unidade.getCidade());
            ps.setString(6, unidade.getEstado());
            ps.setString(7, unidade.getComplemento());
            if(unidade.getDdd1() != null) {
                ps.setInt(8, unidade.getDdd1());
            }
            else{
                ps.setNull(8, Types.NULL);
            }

            if(unidade.getTelefone1() != null) {
                ps.setString(9, unidade.getTelefone1());
            }
            else{
                ps.setNull(9, Types.NULL);
            }

            if(unidade.getDdd2() != null) {
                ps.setInt(10, unidade.getDdd2());
            }
            else{
                ps.setNull(10, Types.NULL);
            }
            if(unidade.getTelefone2() != null) {
                ps.setString(11, unidade.getTelefone2());
            }
            else{
                ps.setNull(11, Types.NULL);
            }
            if(unidade.getEmail() != null) {
                ps.setString(12, unidade.getEmail());
            }
            else{
                ps.setNull(12, Types.NULL);
            }
            if(unidade.getSite() != null) {
                ps.setString(13, unidade.getSite());
            }
            else{
                ps.setNull(13, Types.NULL);
            }
            if(unidade.getMatriz() != null) {
                ps.setBoolean(14, unidade.getMatriz());
            }
            else{
                ps.setNull(14, Types.NULL);
            }

            ps.setString(15, unidade.getNomeUnidade());


            ps.setInt(16, unidade.getCodEmpresa());


            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                codUnidade = rs.getInt("id");
            }

            sql = "INSERT INTO hosp.parametro (motivo_padrao_desligamento_opm, opcao_atendimento, qtd_simultanea_atendimento_profissional, " +
                    "qtd_simultanea_atendimento_equipe, codunidade, horario_inicial, horario_final, intervalo, tipo_atendimento_terapia, " +
                    "programa_ortese_protese, grupo_ortese_protese, almoco_inicio, almoco_final, necessita_presenca_para_evolucao, " +
                    "pts_mostra_obs_gerais_curto , pts_mostra_obs_gerais_medio, pts_mostra_obs_gerais_longo, " +
                    "horario_limite_acesso, horario_inicio_funcionamento, horario_final_funcionamento, orgao_origem_responsavel_pela_informacao, "+
                    "sigla_orgao_origem_responsavel_pela_digitacao, cgcCpf_prestador_ou_orgao_publico, orgao_destino_informacao, "+
                    "indicador_orgao_destino_informacao, versao_sistema, validade_padrao_laudo, valida_dados_laudo_sigtap, minutos_tolerancia, "+
                    "acesso_permitido_domingo, acesso_permitido_segunda, acesso_permitido_terca, acesso_permitido_quarta, acesso_permitido_quinta, "+
                    "acesso_permitido_sexta, acesso_permitido_sabado, permite_agendamento_duplicidade, agenda_avulsa_valida_paciente_ativo, "+
                    "atribuir_cor_tabela_tela_evolucao_profissional,cpf_paciente_obrigatorio, dias_paciente_ativo_sem_evolucao, "+
                    "verifica_periodo_inicial_evolucao_programa, inicio_evolucao_unidade, busca_automatica_cep_paciente,  "+
                    "capacidades_funcionais_pts_obrigatorio, objetivos_gerais_pts_obrigatorio, cid_agenda_obrigatorio, cid_paciente_terapia_obrigatorio, "+
                    "bpa_com_laudo_autorizado, bloquear_acesso_em_afastamento, bloquear_edicao_evolucao, bloquear_edicao_pts) "+
                    " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "+
                    "?, ?, ?, ?, ?, ?, ?, ?)";

            ps = con.prepareStatement(sql);

            if(unidade.getParametro().getMotivoDesligamento() != null) {
                ps.setInt(1, unidade.getParametro().getMotivoDesligamento());
            }
            else{
                ps.setNull(1, Types.NULL);
            }
            if(unidade.getParametro().getOpcaoAtendimento() != null) {
                ps.setString(2, unidade.getParametro().getOpcaoAtendimento());
            }
            else{
                ps.setNull(2, Types.NULL);
            }
            if(unidade.getParametro().getQuantidadeSimultaneaProfissional() != null) {
                ps.setInt(3, unidade.getParametro().getQuantidadeSimultaneaProfissional());
            }
            else{
                ps.setNull(3, Types.NULL);
            }
            if(unidade.getParametro().getQuantidadeSimultaneaEquipe() != null) {
                ps.setInt(4, unidade.getParametro().getQuantidadeSimultaneaEquipe());
            }
            else{
                ps.setNull(4, Types.NULL);
            }
            ps.setInt(5, codUnidade);

            if(unidade.getParametro().getHorarioInicial() != null) {
                ps.setTime(6, DataUtil.transformarDateEmTime(unidade.getParametro().getHorarioInicial()));
            }
            else{
                ps.setNull(6, Types.NULL);
            }

            if(unidade.getParametro().getHorarioFinal() != null) {
                ps.setTime(7, DataUtil.transformarDateEmTime(unidade.getParametro().getHorarioFinal()));
            }
            else{
                ps.setNull(7, Types.NULL);
            }

            if(unidade.getParametro().getIntervalo() != null) {
                ps.setInt(8, unidade.getParametro().getIntervalo());
            }
            else{
                ps.setNull(8, Types.NULL);
            }
            if(unidade.getParametro().getTipoAtendimento().getIdTipo() != null) {
                ps.setInt(9, unidade.getParametro().getTipoAtendimento().getIdTipo());
            }
            else{
                ps.setNull(9, Types.NULL);
            }
            if((unidade.getParametro().getOrteseProtese().getPrograma() != null) && (unidade.getParametro().getOrteseProtese().getPrograma().getIdPrograma() != null)) {
                ps.setInt(10, unidade.getParametro().getOrteseProtese().getPrograma().getIdPrograma());
            }
            else{
                ps.setNull(10, Types.NULL);
            }
            if ((unidade.getParametro().getOrteseProtese().getGrupo() != null) && (unidade.getParametro().getOrteseProtese().getGrupo().getIdGrupo() != null) ){
                ps.setInt(11, unidade.getParametro().getOrteseProtese().getGrupo().getIdGrupo());
            }
            else{
                ps.setNull(11, Types.NULL);
            }

            if(unidade.getParametro().getAlmocoInicio() != null) {
                ps.setTime(12, DataUtil.transformarDateEmTime(unidade.getParametro().getAlmocoInicio()));
            }
            else{
                ps.setNull(12, Types.NULL);
            }

            if(unidade.getParametro().getAlmocoFinal() != null) {
                ps.setTime(13, DataUtil.transformarDateEmTime(unidade.getParametro().getAlmocoFinal()));
            }
            else{
                ps.setNull(13, Types.NULL);
            }

            if(unidade.getParametro().getNecessitaPresencaParaEvolucao() != null) {
                ps.setString(14, unidade.getParametro().getNecessitaPresencaParaEvolucao());
            }
            else{
                ps.setNull(14, Types.NULL);
            }

            ps.setBoolean(15, unidade.getParametro().isPtsMostrarObjGeraisCurtoPrazo());
            ps.setBoolean(16, unidade.getParametro().isPtsMostrarObjGeraisMedioPrazo());
            ps.setBoolean(17, unidade.getParametro().isPtsMostrarObjGeraisLongoPrazo());
            ps.setBoolean(18, unidade.getParametro().getUsaHorarioLimiteParaAcesso());

            if(unidade.getParametro().getHorarioInicioFuncionamento() != null) {
                ps.setTime(19, DataUtil.transformarDateEmTime(unidade.getParametro().getHorarioInicioFuncionamento()));
            }
            else{
                ps.setNull(19, Types.NULL);
            }

            if(unidade.getParametro().getHorarioFinalFuncionamento() != null) {
                ps.setTime(20, DataUtil.transformarDateEmTime(unidade.getParametro().getHorarioFinalFuncionamento()));
            }
            else{
                ps.setNull(20, Types.NULL);
            }

            ps.setString(21, unidade.getParametro().getOrgaoOrigemResponsavelPelaInformacao());
            ps.setString(22, unidade.getParametro().getSiglaOrgaoOrigemResponsavelPelaDigitacao());
            ps.setString(23, unidade.getParametro().getCgcCpfPrestadorOuOrgaoPublico());
            ps.setString(24, unidade.getParametro().getOrgaoDestinoInformacao());
            ps.setString(25, unidade.getParametro().getIndicadorOrgaoDestinoInformacao());
            ps.setString(26, unidade.getParametro().getVersaoSistema());
            ps.setInt(27, unidade.getParametro().getValidadePadraoLaudo());
            ps.setBoolean(28, unidade.getParametro().isValidaDadosLaudoSigtap());
            ps.setInt(29, unidade.getParametro().getMinutosTolerancia());
            ps.setBoolean(30, unidade.getParametro().isAcessoPermitidoDomingo());
            ps.setBoolean(31, unidade.getParametro().isAcessoPermitidoSegunda());
            ps.setBoolean(32, unidade.getParametro().isAcessoPermitidoTerca());
            ps.setBoolean(33, unidade.getParametro().isAcessoPermitidoQuarta());
            ps.setBoolean(34, unidade.getParametro().isAcessoPermitidoQuinta());
            ps.setBoolean(35, unidade.getParametro().isAcessoPermitidoSexta());
            ps.setBoolean(36, unidade.getParametro().isAcessoPermitidoSabado());
            ps.setBoolean(37, unidade.getParametro().isPermiteAgendamentoDuplicidade());
            ps.setBoolean(38, unidade.getParametro().isAgendaAvulsaValidaPacienteAtivo());
            ps.setBoolean(39, unidade.getParametro().isAtribuirCorTabelaTelaEvolucaoProfissional());
            ps.setBoolean(40, unidade.getParametro().isCpfPacienteObrigatorio());
            ps.setInt(41, unidade.getParametro().getDiasPacienteAtivoSemEvolucao());
            ps.setBoolean(42, unidade.getParametro().isVerificaPeriodoInicialEvolucaoPrograma());

            if(VerificadorUtil.verificarSeObjetoNulo(unidade.getParametro().getInicioEvolucaoUnidade()))
                ps.setNull(43, Types.NULL);
            else
                ps.setDate(43, new java.sql.Date(unidade.getParametro().getInicioEvolucaoUnidade().getTime()));

            ps.setBoolean(44, unidade.getParametro().isBuscaAutomaticaCepPaciente());
            ps.setBoolean(45, unidade.getParametro().isCapacidadesFuncionaisPTSObrigatorio());
            ps.setBoolean(46, unidade.getParametro().isObjetivosGeraisPTSObrigatorio());
            ps.setBoolean(47, unidade.getParametro().isCidAgendaObrigatorio());
            ps.setBoolean(48, unidade.getParametro().isCidPacienteTerapiaObrigatorio());
            ps.setBoolean(49, unidade.getParametro().isBpaComLaudoAutorizado());
            ps.setBoolean(50, unidade.getParametro().isBloquearAcessoEmAfastamento());
            ps.setBoolean(51, unidade.getParametro().isBloquearEdicaoEvolucao());
            ps.setBoolean(52, unidade.getParametro().isBloquearEdicaoPTS());
            ps.execute();


            sql = "INSERT INTO hosp.config_evolucao_unidade_programa_grupo " +
                    "(codunidade, codprograma, codgrupo, inicio_evolucao, usuario_cadastro, data_hora_cadastro ) " +
                    "VALUES(?, ?, ?, ?,?, current_timestamp);";

            ps = null;
            ps = con.prepareStatement(sql);

            for (int i = 0; i < unidade.getListaProgramasGrupoEvolucao().size(); i++) {
                ps.setInt(1, codUnidade);
                ps.setInt(2, unidade.getListaProgramasGrupoEvolucao().get(i).getPrograma().getIdPrograma());
                ps.setInt(3, unidade.getListaProgramasGrupoEvolucao().get(i).getGrupo().getIdGrupo());
                ps.setDate(4, DataUtil.converterDateUtilParaDateSql(unidade.getListaProgramasGrupoEvolucao().get(i).getDataInicioEvolucao()));
                ps.setLong(5, user_session.getId());
                ps.execute();
            }


            con.commit();
            retorno = true;

        } catch (SQLException sqle) {
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
            throw new ProjetoException(ex, this.getClass().getName());
        } finally {
            try {
                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return retorno;
    }

    public List<UnidadeBean> listarUnidade() throws ProjetoException {

        List<UnidadeBean> lista = new ArrayList<>();
        String sql = "SELECT id,nome,  nome_principal, e.nome_fantasia, e.cnpj,  e.restringir_laudo_unidade, unidade.rua, unidade.bairro, " +
                " unidade.numero, unidade.complemento, unidade.cep, unidade.cidade, unidade.estado, unidade.ddd_1, unidade.telefone_1, unidade.ddd_2, unidade.telefone_2, " +
                " unidade.email, unidade.site, matriz, unidade.ativo, case when matriz is true then 'Matriz' else 'Filial' end as tipo " +
                " FROM hosp.unidade join hosp.empresa e on e.cod_empresa = unidade.cod_empresa where unidade.ativo is true;";

        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                UnidadeBean unidade = new UnidadeBean();
                unidade.setId(rs.getInt("id"));
                unidade.setNomeUnidade(rs.getString("nome"));
                unidade.setNomeEmpresa(rs.getString("nome_principal"));
                unidade.setNomeFantasia(rs.getString("nome_fantasia"));
                unidade.setRestringirLaudoPorUnidade(rs.getBoolean("restringir_laudo_unidade"));
                unidade.setCnpj(rs.getString("cnpj"));
                unidade.setRua(rs.getString("rua"));
                unidade.setBairro(rs.getString("bairro"));
                unidade.setNumero(rs.getInt("numero"));
                unidade.setComplemento(rs.getString("complemento"));
                unidade.setCep(rs.getString("cep"));
                unidade.setCidade(rs.getString("cidade"));
                unidade.setEstado(rs.getString("estado"));
                unidade.setDdd1(rs.getInt("ddd_1"));
                unidade.setTelefone1(rs.getString("telefone_1"));
                unidade.setDdd2(rs.getInt("ddd_2"));
                unidade.setTelefone2(rs.getString("telefone_2"));
                unidade.setEmail(rs.getString("email"));
                unidade.setSite(rs.getString("site"));
                unidade.setMatriz(rs.getBoolean("matriz"));
                unidade.setAtivo(rs.getBoolean("ativo"));
                unidade.setTipoString(rs.getString("tipo"));
                unidade.setParametro(carregarParametro(unidade.getId(), con));

                lista.add(unidade);
            }
        } catch (SQLException sqle) {
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
            throw new ProjetoException(ex, this.getClass().getName());
        } finally {
            try {
                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return lista;
    }

    public Boolean alterarUnidade(UnidadeBean unidade) throws ProjetoException {

        boolean retorno;
        String sql = "UPDATE hosp.unidade SET   rua=?, " +
                " bairro=?, numero=?, cep=?, cidade=?, estado=?, ddd_1=?, telefone_1=?, " +
                " ddd_2=?, telefone_2=?, email=?, site=?, matriz=?, complemento=?, nome=?,  cod_empresa=? " +
                "  WHERE id = ?;";
        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, unidade.getRua());
            ps.setString(2, unidade.getBairro());
            if (unidade.getNumero()!=null){
                ps.setInt(3, unidade.getNumero());
            }
            else{
                ps.setNull(3, Types.NULL);
            }

            ps.setString(4, unidade.getCep());
            ps.setString(5, unidade.getCidade());
            ps.setString(6, unidade.getEstado());
            if (unidade.getDdd1()!=null){
                ps.setInt(7, unidade.getDdd1());
            }
            else{
                ps.setNull(7, Types.NULL);
            }

            if (unidade.getTelefone1()!=null){
                ps.setString(8, unidade.getTelefone1());
            }
            else{
                ps.setNull(8, Types.NULL);
            }

            if (unidade.getDdd2()!=null){
                ps.setInt(9, unidade.getDdd2());
            }
            else{
                ps.setNull(9, Types.NULL);
            }

            if (unidade.getTelefone2()!=null){
                ps.setString(10, unidade.getTelefone2());
            }
            else{
                ps.setNull(10, Types.NULL);
            }



            ps.setString(11, unidade.getEmail());
            ps.setString(12, unidade.getSite());
            ps.setBoolean(13, unidade.getMatriz());
            ps.setString(14, unidade.getComplemento());
            ps.setString(15, unidade.getNomeUnidade());
            ps.setInt(16, unidade.getCodEmpresa());
            ps.setInt(17, unidade.getId());
            ps.executeUpdate();

            sql = "UPDATE hosp.parametro SET motivo_padrao_desligamento_opm = ?, opcao_atendimento = ?, " +
                    "qtd_simultanea_atendimento_profissional = ?, qtd_simultanea_atendimento_equipe = ?, " +
                    "horario_inicial = ?, horario_final = ?, intervalo = ?, tipo_atendimento_terapia = ?, " +
                    "programa_ortese_protese = ?, grupo_ortese_protese = ?, almoco_inicio = ?, almoco_final = ?,  " +
                    " necessita_presenca_para_evolucao=?, pts_mostra_obs_gerais_curto=? , pts_mostra_obs_gerais_medio=?, " +
                    "pts_mostra_obs_gerais_longo=?, horario_limite_acesso = ?, horario_inicio_funcionamento = ?, horario_final_funcionamento = ?, " +
                    "bloqueia_por_pendencia_evolucao_anterior = ?, orgao_origem_responsavel_pela_informacao = ?, "+
                    "sigla_orgao_origem_responsavel_pela_digitacao = ?, cgcCpf_prestador_ou_orgao_publico = ?, orgao_destino_informacao = ?, "+
                    "indicador_orgao_destino_informacao = ?, versao_sistema = ? , validade_padrao_laudo = ?, "+
                    "valida_dados_laudo_sigtap = ?, minutos_tolerancia = ?, "+
                    "acesso_permitido_domingo = ?, acesso_permitido_segunda = ?, acesso_permitido_terca = ?, acesso_permitido_quarta = ?, "+
                    "acesso_permitido_quinta = ?, acesso_permitido_sexta = ?, acesso_permitido_sabado = ?, permite_agendamento_duplicidade = ?,  "+
                    "agenda_avulsa_valida_paciente_ativo = ?, atribuir_cor_tabela_tela_evolucao_profissional = ?, cpf_paciente_obrigatorio = ?, dias_paciente_ativo_sem_evolucao = ?, "+
                    "verifica_periodo_inicial_evolucao_programa = ?, inicio_evolucao_unidade = ?, busca_automatica_cep_paciente = ?,  "+
                    "capacidades_funcionais_pts_obrigatorio = ?, objetivos_gerais_pts_obrigatorio = ?, cid_agenda_obrigatorio = ?, cid_paciente_terapia_obrigatorio=?, "+
                    "bpa_com_laudo_autorizado = ?, bloquear_acesso_em_afastamento = ?, bloquear_edicao_evolucao = ?, bloquear_edicao_pts = ? WHERE codunidade = ?";

            ps = con.prepareStatement(sql);

            ps.setInt(1, unidade.getParametro().getMotivoDesligamento());
            ps.setString(2, unidade.getParametro().getOpcaoAtendimento());
            ps.setInt(3, unidade.getParametro().getQuantidadeSimultaneaProfissional());
            ps.setInt(4, unidade.getParametro().getQuantidadeSimultaneaEquipe());
            ps.setTime(5, DataUtil.transformarDateEmTime(unidade.getParametro().getHorarioInicial()));
            ps.setTime(6, DataUtil.transformarDateEmTime(unidade.getParametro().getHorarioFinal()));
            ps.setInt(7, unidade.getParametro().getIntervalo());
            if ((unidade.getParametro().getTipoAtendimento() != null) && (unidade.getParametro().getTipoAtendimento().getIdTipo() != null)){
                ps.setInt(8, unidade.getParametro().getTipoAtendimento().getIdTipo());
            }
            else{
                ps.setNull(8, Types.NULL);
            }

            if ((unidade.getParametro().getOrteseProtese().getPrograma() != null) && (unidade.getParametro().getOrteseProtese().getPrograma().getIdPrograma() != null)){
                ps.setInt(9, unidade.getParametro().getOrteseProtese().getPrograma().getIdPrograma());
            }
            else{
                ps.setNull(9, Types.NULL);
            }


            if (( unidade.getParametro().getOrteseProtese().getGrupo() != null) && ( unidade.getParametro().getOrteseProtese().getGrupo().getIdGrupo() != null)){
                ps.setInt(10,  unidade.getParametro().getOrteseProtese().getGrupo().getIdGrupo());
            }
            else{
                ps.setNull(10, Types.NULL);
            }

            if(unidade.getParametro().getAlmocoInicio() != null) {
                ps.setTime(11, DataUtil.transformarDateEmTime(unidade.getParametro().getAlmocoInicio()));
            }
            else{
                ps.setNull(11, Types.NULL);
            }

            if(unidade.getParametro().getAlmocoFinal() != null) {
                ps.setTime(12, DataUtil.transformarDateEmTime(unidade.getParametro().getAlmocoFinal()));
            }
            else{
                ps.setNull(12, Types.NULL);
            }

            if ((unidade.getParametro().getNecessitaPresencaParaEvolucao() != null) ){
                ps.setString(13, unidade.getParametro().getNecessitaPresencaParaEvolucao());
            }
            else{
                ps.setNull(13, Types.NULL);
            }

            ps.setBoolean(14, unidade.getParametro().isPtsMostrarObjGeraisCurtoPrazo());
            ps.setBoolean(15, unidade.getParametro().isPtsMostrarObjGeraisMedioPrazo());
            ps.setBoolean(16, unidade.getParametro().isPtsMostrarObjGeraisLongoPrazo());
            ps.setBoolean(17, unidade.getParametro().getUsaHorarioLimiteParaAcesso());

            if(unidade.getParametro().getHorarioInicioFuncionamento() != null && unidade.getParametro().getUsaHorarioLimiteParaAcesso()) {
                ps.setTime(18, DataUtil.transformarDateEmTime(unidade.getParametro().getHorarioInicioFuncionamento()));
            }
            else{
                ps.setNull(18, Types.NULL);
            }

            if(unidade.getParametro().getHorarioFinalFuncionamento() != null && unidade.getParametro().getUsaHorarioLimiteParaAcesso()) {
                ps.setTime(19, DataUtil.transformarDateEmTime(unidade.getParametro().getHorarioFinalFuncionamento()));
            }
            else{
                ps.setNull(19, Types.NULL);
            }

            ps.setBoolean(20, unidade.getParametro().isBloqueiaPorPendenciaEvolucaoAnterior());
            ps.setString(21, unidade.getParametro().getOrgaoOrigemResponsavelPelaInformacao());
            ps.setString(22, unidade.getParametro().getSiglaOrgaoOrigemResponsavelPelaDigitacao());
            ps.setString(23, unidade.getParametro().getCgcCpfPrestadorOuOrgaoPublico());
            ps.setString(24, unidade.getParametro().getOrgaoDestinoInformacao());
            ps.setString(25, unidade.getParametro().getIndicadorOrgaoDestinoInformacao());
            ps.setString(26, unidade.getParametro().getVersaoSistema());
            ps.setInt(27, unidade.getParametro().getValidadePadraoLaudo());
            ps.setBoolean(28, unidade.getParametro().isValidaDadosLaudoSigtap());
            ps.setInt(29, unidade.getParametro().getMinutosTolerancia());
            ps.setBoolean(30, unidade.getParametro().isAcessoPermitidoDomingo());
            ps.setBoolean(31, unidade.getParametro().isAcessoPermitidoSegunda());
            ps.setBoolean(32, unidade.getParametro().isAcessoPermitidoTerca());
            ps.setBoolean(33, unidade.getParametro().isAcessoPermitidoQuarta());
            ps.setBoolean(34, unidade.getParametro().isAcessoPermitidoQuinta());
            ps.setBoolean(35, unidade.getParametro().isAcessoPermitidoSexta());
            ps.setBoolean(36, unidade.getParametro().isAcessoPermitidoSabado());
            ps.setBoolean(37, unidade.getParametro().isPermiteAgendamentoDuplicidade());
            ps.setBoolean(38, unidade.getParametro().isAgendaAvulsaValidaPacienteAtivo());
            ps.setBoolean(39, unidade.getParametro().isAtribuirCorTabelaTelaEvolucaoProfissional());
            ps.setBoolean(40, unidade.getParametro().isCpfPacienteObrigatorio());
            ps.setInt(41, unidade.getParametro().getDiasPacienteAtivoSemEvolucao());

            ps.setBoolean(42, unidade.getParametro().isVerificaPeriodoInicialEvolucaoPrograma());

            if(VerificadorUtil.verificarSeObjetoNulo(unidade.getParametro().getInicioEvolucaoUnidade()))
                ps.setNull(43, Types.NULL);
            else
                ps.setDate(43, new java.sql.Date(unidade.getParametro().getInicioEvolucaoUnidade().getTime()));

            ps.setBoolean(44, unidade.getParametro().isBuscaAutomaticaCepPaciente());
            ps.setBoolean(45, unidade.getParametro().isCapacidadesFuncionaisPTSObrigatorio());
            ps.setBoolean(46, unidade.getParametro().isObjetivosGeraisPTSObrigatorio());
            ps.setBoolean(47, unidade.getParametro().isCidAgendaObrigatorio());
            ps.setBoolean(48, unidade.getParametro().isCidPacienteTerapiaObrigatorio());
            ps.setBoolean(49, unidade.getParametro().isBpaComLaudoAutorizado());
            ps.setBoolean(50, unidade.getParametro().isBloquearAcessoEmAfastamento());
            ps.setBoolean(51, unidade.getParametro().isBloquearEdicaoEvolucao());
            ps.setBoolean(52, unidade.getParametro().isBloquearEdicaoPTS());
            ps.setInt(53, unidade.getId());

            ps.executeUpdate();

            sql = "delete from hosp.config_evolucao_unidade_programa_grupo where codunidade = ?";

            PreparedStatement ps2 = null;
            ps2 = con.prepareStatement(sql);
            ps2.setLong(1,unidade.getId());
            ps2.execute();



            sql = "INSERT INTO hosp.config_evolucao_unidade_programa_grupo " +
                    "(codunidade, codprograma, codgrupo, inicio_evolucao, usuario_cadastro, data_hora_cadastro ) " +
                    "VALUES(?, ?, ?, ?,?, current_timestamp);";

            ps = null;
            ps = con.prepareStatement(sql);

            for (int i = 0; i < unidade.getListaProgramasGrupoEvolucao().size(); i++) {
                ps.setInt(1, unidade.getId());
                ps.setInt(2,  unidade.getListaProgramasGrupoEvolucao().get(i).getPrograma().getIdPrograma());
                ps.setInt(3,  unidade.getListaProgramasGrupoEvolucao().get(i).getGrupo().getIdGrupo());
                ps.setDate(4, DataUtil.converterDateUtilParaDateSql( unidade.getListaProgramasGrupoEvolucao().get(i).getDataInicioEvolucao()));
                ps.setLong(5, user_session.getId());
                ps.execute();
            }

            con.commit();
            retorno = true;

        } catch (SQLException sqle) {
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
            throw new ProjetoException(ex, this.getClass().getName());
        } finally {
            try {
                con.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return retorno;
    }

    public Boolean desativarUnidade(UnidadeBean unidade) throws ProjetoException {

        Boolean retorno = false;
        String sql = "update hosp.unidade set ativo = false where id = ?";

        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setLong(1, unidade.getId());
            stmt.execute();

            con.commit();
            retorno = true;
        } catch (SQLException sqle) {
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
            throw new ProjetoException(ex, this.getClass().getName());
        } finally {
            try {
                con.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return retorno;
    }

    public UnidadeBean buscarUnidadePorId(Integer id) throws ProjetoException {

        UnidadeBean unidade = new UnidadeBean();
        String sql = "SELECT id,nome, e.cod_empresa,nome_principal, e.nome_fantasia, e.restringir_laudo_unidade, cnpj, unidade.rua, unidade.bairro, " +
                " unidade.numero, unidade.complemento, unidade.cep, unidade.cidade, unidade.estado, unidade.ddd_1, unidade.telefone_1, unidade.ddd_2, unidade.telefone_2, " +
                " unidade.email, unidade.site, unidade.matriz, unidade.ativo  " +
                " FROM hosp.unidade join hosp.empresa e on e.cod_empresa = unidade.cod_empresa where unidade.id = ?;";

        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                unidade.setId(rs.getInt("id"));
                unidade.setNomeUnidade(rs.getString("nome"));
                unidade.setNomeEmpresa(rs.getString("nome_principal"));
                unidade.setRestringirLaudoPorUnidade(rs.getBoolean("restringir_laudo_unidade"));
                unidade.setNomeFantasia(rs.getString("nome_fantasia"));
                unidade.setCodEmpresa(rs.getInt("cod_empresa"));
                unidade.setCnpj(rs.getString("cnpj"));
                unidade.setRua(rs.getString("rua"));
                unidade.setBairro(rs.getString("bairro"));
                unidade.setNumero(rs.getInt("numero"));
                unidade.setComplemento(rs.getString("complemento"));
                unidade.setCep(rs.getString("cep"));
                unidade.setCidade(rs.getString("cidade"));
                unidade.setEstado(rs.getString("estado"));
                unidade.setDdd1(rs.getInt("ddd_1"));
                unidade.setTelefone1(rs.getString("telefone_1"));
                unidade.setDdd2(rs.getInt("ddd_2"));
                unidade.setTelefone2(rs.getString("telefone_2"));
                unidade.setEmail(rs.getString("email"));
                unidade.setSite(rs.getString("site"));
                unidade.setMatriz(rs.getBoolean("matriz"));
                unidade.setAtivo(rs.getBoolean("ativo"));
                unidade.setParametro(carregarParametro(id, con));
                unidade.setListaProgramasGrupoEvolucao(carregarProgramasEGruposEmEvolucao(id, con));
                unidade.setNomeUnidade(rs.getString("nome"));

            }
        } catch (SQLException sqle) {
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
            throw new ProjetoException(ex, this.getClass().getName());
        } finally {
            try {
                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return unidade;
    }

    public ParametroBean carregarParametro(Integer id, Connection conAuxiliar) throws ProjetoException, SQLException {

        ParametroBean parametro = new ParametroBean();

        String sql = "SELECT id, motivo_padrao_desligamento_opm, opcao_atendimento, qtd_simultanea_atendimento_profissional, qtd_simultanea_atendimento_equipe, " +
                "horario_inicial, horario_final, intervalo, tipo_atendimento_terapia, programa_ortese_protese, grupo_ortese_protese, almoco_inicio, almoco_final, " +
                "necessita_presenca_para_evolucao, coalesce(pts_mostra_obs_gerais_curto, false) pts_mostra_obs_gerais_curto,valida_dados_laudo_sigtap,  " +
                "coalesce(pts_mostra_obs_gerais_medio,false) pts_mostra_obs_gerais_medio, coalesce(pts_mostra_obs_gerais_longo,false) pts_mostra_obs_gerais_longo, " +
                "horario_limite_acesso, horario_inicio_funcionamento, horario_final_funcionamento, bloqueia_por_pendencia_evolucao_anterior, horario_limite_acesso, " +
                "orgao_origem_responsavel_pela_informacao, sigla_orgao_origem_responsavel_pela_digitacao, cgcCpf_prestador_ou_orgao_publico, orgao_destino_informacao, "+
                "indicador_orgao_destino_informacao, versao_sistema, validade_padrao_laudo, minutos_tolerancia, acesso_permitido_domingo, acesso_permitido_segunda, acesso_permitido_terca, "+
                "acesso_permitido_quarta, acesso_permitido_quinta, acesso_permitido_sexta, acesso_permitido_sabado, permite_agendamento_duplicidade, agenda_avulsa_valida_paciente_ativo, "+
                "atribuir_cor_tabela_tela_evolucao_profissional, cpf_paciente_obrigatorio, dias_paciente_ativo_sem_evolucao, verifica_periodo_inicial_evolucao_programa, "+
                "inicio_evolucao_unidade, busca_automatica_cep_paciente, capacidades_funcionais_pts_obrigatorio, objetivos_gerais_pts_obrigatorio, cid_agenda_obrigatorio, "+
                "cid_paciente_terapia_obrigatorio, bpa_com_laudo_autorizado, bloquear_acesso_em_afastamento, bloquear_edicao_evolucao, bloquear_edicao_pts  "+
                " FROM hosp.parametro where codunidade = ?;";

        try {
            PreparedStatement ps = conAuxiliar.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                parametro.setMotivoDesligamento(rs.getInt("motivo_padrao_desligamento_opm"));
                parametro.setOpcaoAtendimento(rs.getString("opcao_atendimento"));
                parametro.setQuantidadeSimultaneaProfissional(rs.getInt("qtd_simultanea_atendimento_profissional"));
                parametro.setQuantidadeSimultaneaEquipe(rs.getInt("qtd_simultanea_atendimento_equipe"));
                parametro.setHorarioInicial(rs.getTime("horario_inicial"));
                parametro.setHorarioFinal(rs.getTime("horario_final"));
                parametro.setIntervalo(rs.getInt("intervalo"));
                parametro.getTipoAtendimento().setIdTipo(rs.getInt("tipo_atendimento_terapia"));
                if(!VerificadorUtil.verificarSeObjetoNuloOuZero(rs.getInt("programa_ortese_protese"))) {
                    parametro.getOrteseProtese().setPrograma(new ProgramaDAO().listarProgramaPorIdComConexao(rs.getInt("programa_ortese_protese"), conAuxiliar));
                }
                else{
                    parametro.getOrteseProtese().setPrograma(new ProgramaBean());
                }
                if(!VerificadorUtil.verificarSeObjetoNuloOuZero(rs.getInt("grupo_ortese_protese"))) {
                    parametro.getOrteseProtese().setGrupo(new GrupoDAO().listarGrupoPorIdComConexao(rs.getInt("grupo_ortese_protese"), conAuxiliar));
                }
                else{
                    parametro.getOrteseProtese().setGrupo(new GrupoBean());
                }
                parametro.setAlmocoInicio(rs.getTime("almoco_inicio"));
                parametro.setAlmocoFinal(rs.getTime("almoco_final"));
                parametro.setNecessitaPresencaParaEvolucao(rs.getString("necessita_presenca_para_evolucao"));
                parametro.setValidaDadosLaudoSigtap(rs.getBoolean("valida_dados_laudo_sigtap"));
                parametro.setPtsMostrarObjGeraisCurtoPrazo(rs.getBoolean("pts_mostra_obs_gerais_curto"));
                parametro.setPtsMostrarObjGeraisMedioPrazo(rs.getBoolean("pts_mostra_obs_gerais_medio"));
                parametro.setPtsMostrarObjGeraisLongoPrazo(rs.getBoolean("pts_mostra_obs_gerais_longo"));
                parametro.setUsaHorarioLimiteParaAcesso(rs.getBoolean("horario_limite_acesso"));
                parametro.setHorarioInicioFuncionamento(rs.getTime("horario_inicio_funcionamento"));
                parametro.setHorarioFinalFuncionamento(rs.getTime("horario_final_funcionamento"));
                parametro.setBloqueiaPorPendenciaEvolucaoAnterior(rs.getBoolean("bloqueia_por_pendencia_evolucao_anterior"));
                parametro.setUsaHorarioLimiteParaAcesso(rs.getBoolean("horario_limite_acesso"));
                parametro.setOrgaoOrigemResponsavelPelaInformacao(rs.getString("orgao_origem_responsavel_pela_informacao"));
                parametro.setSiglaOrgaoOrigemResponsavelPelaDigitacao(rs.getString("sigla_orgao_origem_responsavel_pela_digitacao"));
                parametro.setCgcCpfPrestadorOuOrgaoPublico(rs.getString("cgcCpf_prestador_ou_orgao_publico"));
                parametro.setOrgaoDestinoInformacao(rs.getString("orgao_destino_informacao"));
                parametro.setIndicadorOrgaoDestinoInformacao(rs.getString("indicador_orgao_destino_informacao"));
                parametro.setVersaoSistema(rs.getString("versao_sistema"));
                parametro.setValidadePadraoLaudo(rs.getInt("validade_padrao_laudo"));
                parametro.setMinutosTolerancia(rs.getInt("minutos_tolerancia"));
                parametro.setAcessoPermitidoDomingo(rs.getBoolean("acesso_permitido_domingo"));
                parametro.setAcessoPermitidoSegunda(rs.getBoolean("acesso_permitido_segunda"));
                parametro.setAcessoPermitidoTerca(rs.getBoolean("acesso_permitido_terca"));
                parametro.setAcessoPermitidoQuarta(rs.getBoolean("acesso_permitido_quarta"));
                parametro.setAcessoPermitidoQuinta(rs.getBoolean("acesso_permitido_quinta"));
                parametro.setAcessoPermitidoSexta(rs.getBoolean("acesso_permitido_sexta"));
                parametro.setAcessoPermitidoSabado(rs.getBoolean("acesso_permitido_sabado"));
                parametro.setPermiteAgendamentoDuplicidade(rs.getBoolean("permite_agendamento_duplicidade"));
                parametro.setAgendaAvulsaValidaPacienteAtivo(rs.getBoolean("agenda_avulsa_valida_paciente_ativo"));
                parametro.setAtribuirCorTabelaTelaEvolucaoProfissional(rs.getBoolean("atribuir_cor_tabela_tela_evolucao_profissional"));
                parametro.setCpfPacienteObrigatorio(rs.getBoolean("cpf_paciente_obrigatorio"));
                parametro.setDiasPacienteAtivoSemEvolucao(rs.getInt("dias_paciente_ativo_sem_evolucao"));
                parametro.setVerificaPeriodoInicialEvolucaoPrograma(rs.getBoolean("verifica_periodo_inicial_evolucao_programa"));
                parametro.setInicioEvolucaoUnidade(rs.getDate("inicio_evolucao_unidade"));
                parametro.setBuscaAutomaticaCepPaciente(rs.getBoolean("busca_automatica_cep_paciente"));
                parametro.setCapacidadesFuncionaisPTSObrigatorio(rs.getBoolean("capacidades_funcionais_pts_obrigatorio"));
                parametro.setObjetivosGeraisPTSObrigatorio(rs.getBoolean("objetivos_gerais_pts_obrigatorio"));
                parametro.setCidAgendaObrigatorio(rs.getBoolean("cid_agenda_obrigatorio"));
                parametro.setCidPacienteTerapiaObrigatorio(rs.getBoolean("cid_paciente_terapia_obrigatorio"));
                parametro.setBpaComLaudoAutorizado(rs.getBoolean("bpa_com_laudo_autorizado"));
                parametro.setBloquearAcessoEmAfastamento(rs.getBoolean("bloquear_acesso_em_afastamento"));
                parametro.setBloquearEdicaoEvolucao(rs.getBoolean("bloquear_edicao_evolucao"));
                parametro.setBloquearEdicaoPTS(rs.getBoolean("bloquear_edicao_pts"));
            }
        } catch (SQLException sqle) {
            conAuxiliar.rollback();
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
            conAuxiliar.rollback();
            throw new ProjetoException(ex, this.getClass().getName());
        }
        return parametro;
    }

    public List<ProgramaGrupoEvolucaoBean> carregarProgramasEGruposEmEvolucao
            (Integer id, Connection conAuxiliar) throws ProjetoException, SQLException {

        List<ProgramaGrupoEvolucaoBean> listaProgramasGruposEvolucao = new ArrayList<ProgramaGrupoEvolucaoBean>();

        String sql = "select cev.codunidade,cev.codprograma, p.descprograma, cev.codgrupo, g.descgrupo , cev.inicio_evolucao\n" +
                "from hosp.config_evolucao_unidade_programa_grupo cev\n" +
                "left join hosp.programa p on (p.id_programa = cev.codprograma) \n" +
                "left join hosp.grupo g on (g.id_grupo = cev.codgrupo) where cev.codunidade = ?";

        try {
            PreparedStatement ps = conAuxiliar.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                ProgramaGrupoEvolucaoBean programaGrupoEvolucao = new ProgramaGrupoEvolucaoBean();
                programaGrupoEvolucao.getPrograma().setIdPrograma(rs.getInt("codprograma"));
                programaGrupoEvolucao.getPrograma().setDescPrograma(rs.getString("descprograma"));
                programaGrupoEvolucao.getGrupo().setIdGrupo(rs.getInt("codgrupo"));
                programaGrupoEvolucao.getGrupo().setDescGrupo(rs.getString("descgrupo"));
                programaGrupoEvolucao.setDataInicioEvolucao(rs.getDate("inicio_evolucao"));
                programaGrupoEvolucao.getUnidade().setId(rs.getInt("codunidade"));
                listaProgramasGruposEvolucao.add(programaGrupoEvolucao);

            }
        } catch (SQLException sqle) {
            conAuxiliar.rollback();
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
            conAuxiliar.rollback();
            throw new ProjetoException(ex, this.getClass().getName());
        }
        return listaProgramasGruposEvolucao;
    }


    public String carregarOpcaoAtendimentoDaUnidade() throws ProjetoException {

        String retorno = null;

        FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
                .getSessionMap().get("obj_funcionario");

        String sql = "SELECT opcao_atendimento FROM hosp.parametro where codunidade = ?;";

        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, user_session.getUnidade().getId());
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                retorno = rs.getString("opcao_atendimento");
            }

        } catch (SQLException sqle) {
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
            throw new ProjetoException(ex, this.getClass().getName());
        } finally {
            try {
                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return retorno;
    }

    public ParametroBean carregarDetalhesAtendimentoDaUnidade() throws ProjetoException {

        ParametroBean parametro = new ParametroBean();

        String sql = "SELECT qtd_simultanea_atendimento_profissional, qtd_simultanea_atendimento_equipe, " +
                "horario_inicial, horario_final, intervalo, almoco_inicio, almoco_final " +
                " FROM hosp.parametro where codunidade = ?;";

        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, user_session.getUnidade().getId());
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                parametro.setQuantidadeSimultaneaProfissional(rs.getInt("qtd_simultanea_atendimento_profissional"));
                parametro.setQuantidadeSimultaneaEquipe(rs.getInt("qtd_simultanea_atendimento_equipe"));
                parametro.setHorarioInicial(rs.getTime("horario_inicial"));
                parametro.setHorarioFinal(rs.getTime("horario_final"));
                parametro.setIntervalo(rs.getInt("intervalo"));
                parametro.setAlmocoInicio(rs.getTime("almoco_inicio"));
                parametro.setAlmocoFinal(rs.getTime("almoco_final"));

            }
        } catch (SQLException sqle) {
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
            throw new ProjetoException(ex, this.getClass().getName());
        } finally {
            try {
                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return parametro;
    }

    public List<UnidadeBean> carregarUnidadesDoFuncionario() throws ProjetoException {

        List<UnidadeBean> lista = new ArrayList<>();

        String sql = "SELECT u.nome, fu.cod_unidade " +
                "FROM hosp.funcionario_unidades fu " +
                "JOIN hosp.unidade u ON (fu.cod_unidade = u.id) " +
                "WHERE fu.cod_funcionario = ? " +
                "UNION  " +
                "SELECT u.nome, f.codunidade " +
                "FROM acl.funcionarios f " +
                "JOIN hosp.unidade u ON (f.codunidade = u.id) " +
                "WHERE f.id_funcionario = ?";

        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setLong(1, user_session.getId());
            ps.setLong(2, user_session.getId());
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                UnidadeBean unidade = new UnidadeBean();
                unidade.setId(rs.getInt("cod_unidade"));
                unidade.setNomeUnidade(rs.getString("nome"));
                lista.add(unidade);
            }

        } catch (SQLException sqle) {
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
            throw new ProjetoException(ex, this.getClass().getName());
        } finally {
            try {
                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return lista;
    }

    public Boolean verificarHorariosCheios(Integer codEquipe, Integer codProfissional, Date data, String horario) throws ProjetoException {

        Boolean resultado = true;

        String sql = "SELECT a.dtaatende " +
                "FROM hosp.parametro p " +
                "JOIN hosp.atendimentos a ON (p.codunidade = a.cod_unidade) " +
                "where a.dtaatende = ? AND a.horario = ? AND ";

        if(!VerificadorUtil.verificarSeObjetoNuloOuZero(codEquipe)) {
            sql = sql + "a.codequipe = ? and " +
                    "p.qtd_simultanea_atendimento_equipe > COALESCE((SELECT count(*) FROM hosp.atendimentos aa WHERE aa.horario = ? AND aa.dtaatende = ?),0); ";
        }
        else{
            sql = sql + "a.codmedico = ? and " +
                    "p.qtd_simultanea_atendimento_profissional > COALESCE((SELECT count(*) FROM hosp.atendimentos aa WHERE aa.horario = ? AND aa.dtaatende = ?),0); ";
        }

        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setDate(1, DataUtil.converterDateUtilParaDateSql(data));
            ps.setTime(2, DataUtil.retornarHorarioEmTime(horario));
            ps.setInt(3, (!VerificadorUtil.verificarSeObjetoNuloOuZero(codEquipe) ? codEquipe : codProfissional));
            ps.setTime(4, DataUtil.retornarHorarioEmTime(horario));
            ps.setDate(5, DataUtil.converterDateUtilParaDateSql(data));
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                resultado = false;
            }
        } catch (SQLException sqle) {
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
            throw new ProjetoException(ex, this.getClass().getName());
        } finally {
            try {
                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return resultado;
    }

    public Boolean verificarHorarioDeAlmocoUnidade(String horario) throws ProjetoException {

        Boolean resultado = true;

        String sql = "SELECT p.id " +
                "FROM hosp.parametro p " +
                "WHERE p.codunidade = ? AND ? >= p.almoco_inicio AND ? <= p.almoco_final;";

        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, user_session.getUnidade().getId());
            ps.setTime(2, DataUtil.retornarHorarioEmTime(horario));
            ps.setTime(3, DataUtil.retornarHorarioEmTime(horario));
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                resultado = false;

            }
        } catch (SQLException sqle) {
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
            throw new ProjetoException(ex, this.getClass().getName());
        } finally {
            try {
                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return resultado;
    }

    public Boolean verificarUnidadeEstaConfiguradaParaValidarDadosDoSigtap() throws ProjetoException {

        Boolean resultado = true;
        String sql = "select valida_dados_laudo_sigtap from hosp.parametro where codunidade = ?";

        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, user_session.getUnidade().getId());
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                resultado = rs.getBoolean("valida_dados_laudo_sigtap");
            }
        } catch (SQLException sqle) {
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
            throw new ProjetoException(ex, this.getClass().getName());
        } finally {
            try {
                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return resultado;
    }

    public Integer retornaValidadePadraoLaudo() throws ProjetoException {

        Integer validade = null;
        String sql = "select validade_padrao_laudo from hosp.parametro where codunidade = ?";

        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, user_session.getUnidade().getId());
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                validade = rs.getInt("validade_padrao_laudo");
            }
        } catch (SQLException sqle) {
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
            throw new ProjetoException(ex, this.getClass().getName());
        } finally {
            try {
                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return validade;
    }

}
