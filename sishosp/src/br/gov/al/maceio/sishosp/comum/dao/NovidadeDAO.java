package br.gov.al.maceio.sishosp.comum.dao;


import static br.gov.al.maceio.sishosp.comum.util.persistence.StatementFactory.criarPreparedStatement;


import br.gov.al.maceio.sishosp.acl.model.Sistema;
import br.gov.al.maceio.sishosp.comum.model.Novidade;
import br.gov.al.maceio.sishosp.comum.model.builder.NovidadeBuilder;
import br.gov.al.maceio.sishosp.comum.util.ConnectionFactoryPublico;
import br.gov.al.maceio.sishosp.comum.util.SessionUtil;
import br.gov.al.maceio.sishosp.comum.util.exception.DaoException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


public class NovidadeDAO {

    public List<Novidade> listarNovidades(List<Sistema> sistemas, Long idUsuario){
        List<Novidade> novidades = new ArrayList<>();

        try (Connection connection = ConnectionFactoryPublico.getConnection()) {
            try (PreparedStatement preparedStatement = criarPreparedStatement(connection,
                    "select n.id,  n.novidade, n.descricao, n.documento, n.documento_nome, n.documento_extensao, n.sigla_sistema " +
                    "from novidade.novidades n " +
                    "where not exists (select id_novidade, id_usuario " +
                    "from novidade.novidades_usuario " +
                    " where n.id = id_novidade and id_usuario = ? and banco_acesso =?) and n.sigla_sistema = any(?)")
                    .comParametros(obterParametrosListarNovidades(sistemas, idUsuario))
                    .construir()) {
                ResultSet resultSet;
                resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    novidades.add(new NovidadeBuilder().mapear(resultSet));
                }
            }
        } catch (Exception ex) {
            throw new DaoException(ex);
        }
        return novidades;
    }

    public void marcarVisualizado(List<Novidade> novidadesVisualizadas, Long idUsuario){
        try(Connection connection = ConnectionFactoryPublico.getConnection()){
            for(Novidade novidade : novidadesVisualizadas){
                PreparedStatement preparedStatement = criarPreparedStatement(connection,
                        "insert into novidade.novidades_usuario(id_novidade, id_usuario, nao_visualizar_novamente, data_hora_acao, banco_acesso) " +
                        "values (?,?,?,?, ?)")
                        .comParametro(novidade.getId())
                        .comParametro(idUsuario)
                        .comParametro(novidade.getVisualizado())
                        .comParametroTimeStamp(new Date())
                        .comParametro( (String) SessionUtil.resgatarDaSessao("nomeBancoAcesso"))
                        .construir();
                preparedStatement.executeUpdate();
            }
            connection.commit();
        } catch (Exception ex) {
            throw new DaoException(ex);
        }
    }

    private Object[] obterParametrosListarNovidades(List<Sistema> sistemas, Long idUsuario){
        List<Object> parametros = new ArrayList<>();
        parametros.add(idUsuario);
        parametros.add( (String) SessionUtil.resgatarDaSessao("nomeBancoAcesso"));
        parametros.add(obterListaSiglasSistemasUsuarioLogado(sistemas));

        return parametros.toArray();
    }

    public List<String> obterListaSiglasSistemasUsuarioLogado(List<Sistema> sistemas) {
        return sistemas.stream().map(Sistema::getSigla).collect(Collectors.toList());
    }
}
