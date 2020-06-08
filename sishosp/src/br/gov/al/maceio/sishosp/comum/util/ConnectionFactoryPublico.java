package br.gov.al.maceio.sishosp.comum.util;

import br.gov.al.maceio.sishosp.comum.Configuracao.ConexaoBuilder;
import br.gov.al.maceio.sishosp.comum.Configuracao.Conexoes;
import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactoryPublico {

    public static Connection getConnection() throws ProjetoException {

        Conexoes conexoes = ConexaoBuilder.carregarDadosConexao("publico");
  
        try {
            Class.forName("org.postgresql.Driver");
            Connection con;
            con = DriverManager.getConnection(conexoes.getUrlBanco(), conexoes.getUsuario(), conexoes.getSenha());
            con.setAutoCommit(false);
            return con;
        } catch (ClassNotFoundException cnf) {

            FacesMessage msg = null;
            msg = new FacesMessage(FacesMessage.SEVERITY_WARN,
                    "Driver de conexao com o banco nao encontrado \n Mensagem original: "
                            + cnf.getMessage(), "Erro");

            FacesContext.getCurrentInstance().addMessage(null, msg);
            throw new ProjetoException(cnf, ConnectionFactoryPublico.class.getName());

        } catch (SQLException sql) {
            System.out.println("Não abriu conexão com o banco público");
            FacesMessage msg = null;
            msg = new FacesMessage(FacesMessage.SEVERITY_WARN,
                    "Nao foi possivel abrir a conexao com o banco \n Mensagem original: "
                            + sql.getMessage(), "Erro");

            FacesContext.getCurrentInstance().addMessage(null, msg);
            throw new ProjetoException(
					TratamentoErrosUtil.retornarMensagemDeErro(((SQLException) sql).getSQLState(), sql.getMessage()), ConnectionFactoryPublico.class.getName(), sql);
        }
    }
}