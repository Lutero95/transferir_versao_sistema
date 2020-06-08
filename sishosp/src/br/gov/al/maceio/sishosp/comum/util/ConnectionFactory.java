package br.gov.al.maceio.sishosp.comum.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import br.gov.al.maceio.sishosp.comum.Configuracao.ConexaoBuilder;
import br.gov.al.maceio.sishosp.comum.Configuracao.Conexoes;
import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;

public class ConnectionFactory {

    public static Connection getConnection() throws ProjetoException {

        String nomeBancoAcesso = (String) SessionUtil.resgatarDaSessao("nomeBancoAcesso");

        Conexoes conexoes = ConexaoBuilder.carregarDadosConexao(nomeBancoAcesso);

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
            throw new ProjetoException(cnf, ConnectionFactory.class.getName());

        } catch (SQLException sql) {
            System.out.println("Não abriu conexão");
            FacesMessage msg = null;
            msg = new FacesMessage(FacesMessage.SEVERITY_WARN,
                    "Nao foi possivel abrir a conexao com o banco \n Mensagem original: "
                            + sql.getMessage(), "Erro");

            FacesContext.getCurrentInstance().addMessage(null, msg);
            throw new ProjetoException(
					TratamentoErrosUtil.retornarMensagemDeErro(((SQLException) sql).getSQLState(), sql.getMessage()), ConnectionFactory.class.getName(), sql);

        }
    }
}